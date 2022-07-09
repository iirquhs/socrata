package sg.edu.np.mad.socrata;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class HomeworkFragment extends Fragment {

    TextView textViewCompleted, textViewInProgress;

    RecyclerView recyclerView;

    LocalStorage localStorage;
    FirebaseUtils firebaseUtils;

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

        localStorage = new LocalStorage(requireActivity());
        firebaseUtils = new FirebaseUtils();

        setAddHomeworkButton(view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        ArrayList<Module> moduleArrayList = localStorage.getModuleArrayList();

        ArrayList<Homework> homeworkArrayList = HomeworkUtils.getAllHomework(moduleArrayList);

        updateHomeworkStatus(homeworkArrayList);
        updateHomework(moduleArrayList);

    }

    private void updateHomework(ArrayList<Module> moduleArrayList) {

        ArrayList<Homework> notCompletedHomework = HomeworkUtils.splitByIsCompletedHomeworkArrayList(moduleArrayList)[0];

        HomeworkAdapter adapter = new HomeworkAdapter(notCompletedHomework, moduleArrayList);
        recyclerView.setAdapter(adapter);
    }

    /**
     * count the number of homework in progress and homework
     * completed and update the dashboard
     * @param homeworkArrayList
     */
    private void updateHomeworkStatus(ArrayList<Homework> homeworkArrayList) {
        int homeworkInProgressCount = 0;
        int homeworkCompletedCount = 0;

        for (Homework homework : homeworkArrayList) {
            boolean isCompleted = homework.getIsCompleted();

            if (isCompleted) {
                homeworkCompletedCount++;
            } else {
                homeworkInProgressCount++;
            }
        }

        textViewCompleted.setText(Integer.toString(homeworkCompletedCount));
        textViewInProgress.setText(Integer.toString(homeworkInProgressCount));

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