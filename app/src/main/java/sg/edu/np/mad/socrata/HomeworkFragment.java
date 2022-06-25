package sg.edu.np.mad.socrata;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeworkFragment extends Fragment {

    TextView textViewCompleted, textViewInProgress;

    RecyclerView recyclerView;

    String currentUser;
    DatabaseReference userReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_homework, container, false);

        textViewCompleted = view.findViewById(R.id.textViewCompleted);
        textViewInProgress = view.findViewById(R.id.textViewInProgress);

        recyclerView = view.findViewById(R.id.homework_rcv);

        LinearLayoutManager layout = new LinearLayoutManager(view.getContext());

        recyclerView.setLayoutManager(layout);
        recyclerView.setNestedScrollingEnabled(false);

        currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        userReference = FirebaseDatabase.getInstance().getReference("Users").child(currentUser);

        updateHomeworkStatus();

        updateHomework();

        setAddHomeworkButton(view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        updateHomeworkStatus();
        updateHomework();

    }

    private void updateHomework() {
        DatabaseReference moduleReference = userReference.child("modules");
        moduleReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String, Module> moduleMap = new HashMap<>();

                for (DataSnapshot moduleEntry : snapshot.getChildren()) {
                    String moduleRef = moduleEntry.getKey();

                    int targetHoursPerWeek = moduleEntry.child("targetHoursPerWeek").getValue(int.class);
                    @ColorInt int color = moduleEntry.child("color").getValue(int.class);
                    String moduleName = moduleEntry.child("moduleName").getValue(String.class);
                    String targetGrade = moduleEntry.child("targetGrade").getValue(String.class);

                    Module module = new Module(moduleName, targetGrade, targetHoursPerWeek, color);

                    moduleMap.put(moduleRef, module);
                }


                DatabaseReference homeworkReference = userReference.child("homework");
                homeworkReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList<Homework> homeworkArrayList = new ArrayList<>();

                        for (DataSnapshot homeworkSnapshot : snapshot.getChildren()) {
                            Homework homework = homeworkSnapshot.getValue(Homework.class);

                            assert homework != null;
                            if (homework.getStatus().equals("Done")) {
                                continue;
                            }

                            homeworkArrayList.add(homework);
                        }

                        HomeworkAdapter adapter = new HomeworkAdapter(homeworkArrayList, moduleMap);

                        recyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateHomeworkStatus() {
        DatabaseReference homeworkReference = userReference.child("homework");

        homeworkReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                int homeworkInProgressCount = 0;
                int homeworkCompletedCount = 0;

                for (DataSnapshot homeworkSnapshot : snapshot.getChildren()) {
                    Homework homework = homeworkSnapshot.getValue(Homework.class);

                    String homeworkStatus = homework.getStatus();

                    if (homeworkStatus.equals("In Progress")) {
                        homeworkInProgressCount++;
                    } else if (homeworkStatus.equals("Done")) {
                        homeworkCompletedCount++;
                    }
                }

                textViewCompleted.setText(Integer.toString(homeworkCompletedCount));
                textViewInProgress.setText(Integer.toString(homeworkInProgressCount));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setAddHomeworkButton(View view) {
        FloatingActionButton addHomework = view.findViewById(R.id.createhomework);
        addHomework.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent activityName = new Intent(view.getContext(), HomeworkCreateActivity.class);
                startActivity(activityName);
            }
        });
    }
}