package sg.edu.np.mad.socrata;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Map;

public class HomeFragment extends Fragment {

    TextView textViewUsername, textViewHoursStudied, textViewHomeworkDueThisWeek;

    String username;

    DatabaseReference userReference;

    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        textViewHoursStudied = view.findViewById(R.id.textViewInProgress);
        textViewHomeworkDueThisWeek = view.findViewById(R.id.textViewCompleted);

        textViewUsername = view.findViewById(R.id.username);

        recyclerView = view.findViewById(R.id.recyclerView);

        LinearLayoutManager layout = new LinearLayoutManager(view.getContext());

        recyclerView.setLayoutManager(layout);
        recyclerView.setNestedScrollingEnabled(false);

        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        userReference = FirebaseDatabase.getInstance().getReference("Users").child(currentUser);

        setHomeworkAdapter();

        DatabaseReference moduleReference = userReference.child("modules");
        updateTotalTimeStudiedThisWeek(moduleReference);

        DatabaseReference homeworkReference = userReference.child("homework");
        updateHomeworkDueThisWeek(homeworkReference);

        DatabaseReference usernameReference = userReference.child("username");
        updateUsername(usernameReference);

    }

    /**
     * Retrieve all study sessions from firebase that belongs to the user
     * and calculate how much time the user has studied this week
     * @param moduleReference
     */
    private void updateTotalTimeStudiedThisWeek(DatabaseReference moduleReference) {
        moduleReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Double totalStudyTimeForThisWeek = 0.0;

                for (DataSnapshot moduleSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot studySessionSnapshot : moduleSnapshot.child("studySessions").getChildren()) {
                        StudySession studySession = studySessionSnapshot.getValue(StudySession.class);

                        if (studySession == null) {
                            continue;
                        }

                        //Check if the study session is within this week and sum it to the total study time for this week

                        LocalDateTime studyStartDateTime = studySession.ConvertDueDateTime(studySession.getStudyStartDateTime());

                        LocalDateTime previousSunday = LocalDateTime.now().with(TemporalAdjusters.previous(DayOfWeek.SUNDAY));
                        LocalDateTime nextSunday = LocalDateTime.now().with(TemporalAdjusters.next(DayOfWeek.SUNDAY));

                        if (studyStartDateTime.isAfter(previousSunday) && studyStartDateTime.isBefore(nextSunday)) {
                            totalStudyTimeForThisWeek += studySession.getStudyTime();
                        }
                    }
                }

                // Convert second to hour
                totalStudyTimeForThisWeek /= 3600;

                textViewHoursStudied.setText(String.format("%.2fh", totalStudyTimeForThisWeek));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * Retrieve all homework from firebase that belongs to the user
     * and calculate how many homework is not completed that is due this week
     * @param homeworkReference
     */
    private void updateHomeworkDueThisWeek(DatabaseReference homeworkReference) {
        homeworkReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                int homeworkDueThisWeek = 0;

                for (DataSnapshot homeworkSnapshot : snapshot.getChildren()) {
                    Homework homework = homeworkSnapshot.getValue(Homework.class);

                    //Check if the homework is due this week and sum it to the total homework that needs to be submitted

                    LocalDateTime dueDate = homework.ConvertDueDateTime(homework.getDueDateTimeString());

                    LocalDateTime previousSunday = LocalDateTime.now().with(TemporalAdjusters.previous(DayOfWeek.SUNDAY));
                    LocalDateTime nextSunday = LocalDateTime.now().with(TemporalAdjusters.next(DayOfWeek.SUNDAY));

                    if (dueDate.isAfter(previousSunday) && dueDate.isBefore(nextSunday) && homework.getStatus().equals("In Progress")) {
                        homeworkDueThisWeek++;
                    }

                }

                textViewHomeworkDueThisWeek.setText(Integer.toString(homeworkDueThisWeek));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * Retrieve the username from firebase and update to the textViewUsername
     * @param usernameReference
     */
    private void updateUsername(DatabaseReference usernameReference) {
        usernameReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                username = dataSnapshot.getValue(String.class);
                textViewUsername.setText(username + "!" + " \uD83D\uDC4B");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("tah", "onCancelled", databaseError.toException());
            }
        });
    }

    /**
     * Retrieve module and homework map from firebase, convert it into an object
     * and set the recycler view adapter for homework
     */
    private void setHomeworkAdapter() {
        DatabaseReference moduleReference = userReference.child("modules");
        moduleReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Map<String, Module> moduleMap = ModuleUtils.parseModuleMap((Map<String, Object>) snapshot.getValue());

                    Log.d("TAG", moduleMap.toString());

                    DatabaseReference homeworkReference = userReference.child("homework");
                    homeworkReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ArrayList<Homework> urgentHomeworkArrayList = new ArrayList<>();

                            // Get homework that has less than 24h before the due data and is not done yet.
                            for (DataSnapshot homeworkSnapshot : snapshot.getChildren()) {
                                Homework homework = homeworkSnapshot.getValue(Homework.class);

                                assert homework != null;
                                LocalDateTime homeworkDueDate = homework.ConvertDueDateTime(homework.getDueDateTimeString());

                                long secondsLeft = homework.CalculateSecondsLeftBeforeDueDate(homeworkDueDate);

                                if (secondsLeft / 3600.0 < 24 && homework.getStatus().equals("In Progress")) {
                                    urgentHomeworkArrayList.add(homework);
                                }
                            }

                            HomeworkAdapter homeworkAdapter = new HomeworkAdapter(urgentHomeworkArrayList, moduleMap);

                            recyclerView.setAdapter(homeworkAdapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}