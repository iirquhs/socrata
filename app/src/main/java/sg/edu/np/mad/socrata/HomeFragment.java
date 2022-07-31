package sg.edu.np.mad.socrata;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Map;

public class HomeFragment extends Fragment {

    TextView textViewUsername, textViewHoursStudied, textViewHomeworkDueThisWeek;

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

        LocalStorage localStorage = new LocalStorage(requireActivity());

        User user = localStorage.getUser();

        ArrayList<Module> moduleArrayList = user.getModuleArrayList();

        updateUsername(user.getUsername());

        if (moduleArrayList == null) {
            return;
        }
        updateTotalTimeStudiedThisWeek(moduleArrayList);

        ArrayList<Homework> homeworkArrayList = HomeworkUtils.getAllHomework(moduleArrayList);
        updateHomeworkDueThisWeek(homeworkArrayList);

        setHomeworkAdapter(moduleArrayList, homeworkArrayList);
    }


    /**
     * Retrieve all study sessions from firebase that belongs to the user
     * and calculate how much time the user has studied this week
     *
     * @param moduleArrayList
     */
    private void updateTotalTimeStudiedThisWeek(ArrayList<Module> moduleArrayList) {
        Double totalStudyTimeForThisWeek = 0.0;

        for (Module module : moduleArrayList) {
            for (StudySession studySession : module.getStudySessionArrayList()) {

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

    /**
     * Retrieve all homework from firebase that belongs to the user
     * and calculate how many homework is not completed that is due this week
     *
     * @param homeworkArrayList
     */
    private void updateHomeworkDueThisWeek(ArrayList<Homework> homeworkArrayList) {
        int homeworkDueThisWeek = 0;

        for (Homework homework : homeworkArrayList) {

            //Check if the homework is due this week and sum it to the total homework that needs to be submitted

            LocalDateTime dueDate = homework.ConvertDueDateTime(homework.getDueDateTimeString());

            LocalDateTime previousSunday = LocalDateTime.now().with(TemporalAdjusters.previous(DayOfWeek.SUNDAY));
            LocalDateTime nextSunday = LocalDateTime.now().with(TemporalAdjusters.next(DayOfWeek.SUNDAY));

            if (dueDate.isAfter(previousSunday) && dueDate.isBefore(nextSunday) && !homework.getIsCompleted()) {
                homeworkDueThisWeek++;
            }

        }

        textViewHomeworkDueThisWeek.setText(Integer.toString(homeworkDueThisWeek));

    }

    private void updateUsername(String username) {
        textViewUsername.setText(username + "!" + " \uD83D\uDC4B");
    }

    /**
     * Retrieve module and homework map from firebase, convert it into an object
     * and set the recycler view adapter for homework
     */
    private void setHomeworkAdapter(ArrayList<Module> moduleArrayList, ArrayList<Homework> homeworkArrayList) {

        if (homeworkArrayList.size() <= 0) {
            Toast.makeText(getContext(), "You currently have no homework that is due in 24h", Toast.LENGTH_SHORT).show();
            return;
        }

        ArrayList<Homework> urgentHomeworkArrayList = new ArrayList<>();

        // Get homework that has less than 24h before the due data and is not done yet.
        for (Homework homework : homeworkArrayList) {

            long secondsLeft = homework.CalculateSecondsLeftBeforeDueDate();

            if (secondsLeft / 3600.0 < 24 && !homework.getIsCompleted()) {
                urgentHomeworkArrayList.add(homework);
            }
        }

        HomeworkAdapter homeworkAdapter = new HomeworkAdapter(urgentHomeworkArrayList, moduleArrayList);

        recyclerView.setAdapter(homeworkAdapter);
    }


}