package sg.edu.np.mad.socrata;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ModuleInfoActivity extends AppCompatActivity {
    TextView textViewModuleName, hours, goal,
            purplePercentage, greyPercentage,
            viewMore, percentageText,
            completedStatus, inProgressStatus,
            addHomework;

    ImageButton backButton;

    ImageView edit, delete;

    ProgressBar progressBar;

    Button buttonStudy;

    RecyclerView recyclerView;

    Module module;

    String moduleName;

    LocalStorage localStorage;

    ArrayList<Module> moduleArrayList;

    FirebaseUtils firebaseUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_module_info);

        Intent intent = getIntent();

        moduleName = intent.getStringExtra("module_name");

        textViewModuleName = findViewById(R.id.moduleName);
        hours = findViewById(R.id.targetHours);
        goal = findViewById(R.id.goal);

        completedStatus = findViewById(R.id.textViewCompleted);
        inProgressStatus = findViewById(R.id.textViewInProgress);

        purplePercentage = findViewById(R.id.percentageProgressbar);
        greyPercentage = findViewById(R.id.overallPercentage);
        percentageText = findViewById(R.id.percentageText);
        progressBar = findViewById(R.id.activeProgress);
        addHomework = findViewById(R.id.addHomework);

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(view -> finish());

        buttonStudy = findViewById(R.id.buttonStudy);

        edit = findViewById(R.id.editmodule);

        delete = findViewById(R.id.deletemodule);

        viewMore = findViewById(R.id.viewmore);
        viewMoreHomework();

        createHomeWork();

        recyclerView = findViewById(R.id.recyclerView2);

        LinearLayoutManager layout = new LinearLayoutManager(ModuleInfoActivity.this);

        firebaseUtils = new FirebaseUtils();

        recyclerView.setLayoutManager(layout);
        recyclerView.setNestedScrollingEnabled(false);
    }

    @Override
    protected void onResume() {
        super.onResume();

        localStorage = new LocalStorage(this);

        User user = localStorage.getUser();

        moduleArrayList = user.getModuleArrayList();

        module = moduleArrayList.get(ModuleUtils.findModule(moduleArrayList, moduleName));

        updateModuleInfo();
    }

    /**
     * Redirect to timer activity
     *
     * @param module
     */
    private void startTimer(Module module) {
        buttonStudy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent timerIntent = new Intent(ModuleInfoActivity.this, TimerActivity.class);
                timerIntent.putExtra("module_name", module.getModuleName());
                startActivity(timerIntent);
            }
        });
    }

    /**
     * update the module information
     *
     */
    private void updateModuleInfo() {
        startTimer(module);
        editModule(module);
        deleteModule(module);

        int hoursStudied = module.getTargetHoursPerWeek();

        textViewModuleName.setText(module.getModuleName());

        hours.setText(Integer.toString(hoursStudied));

        goal.setText(module.getTargetGrade());

        setHomeworkRecyclerView();

        ArrayList<Double> studyTimings = new ArrayList<>();

        setStudyProgressBarSection(studyTimings, module);
    }

    /**
     * Get the total study time divided by the target study time and multiplied by 100 to set the progress
     * bar percentage and the percentage text
     *
     * @param studyTimings
     * @param module
     */
    private void setStudyProgressBarSection(ArrayList<Double> studyTimings,  Module module) {
        for (StudySession studySession : module.getStudySessionArrayList()) {
            Double time = studySession.getStudyTime();
            studyTimings.add(time);
        }

        // in hours
        double totalStudyTime = getTotalStudyTime(studyTimings);

        DecimalFormat df = new DecimalFormat("#.#");

        checkWeek();

        greyPercentage.setText(" / " + module.getTargetHoursPerWeek() + "h");
        purplePercentage.setText(String.format("%.2fh", totalStudyTime));

        Double percentage = (totalStudyTime / module.getTargetHoursPerWeek()) * 100;

        progressBar.setProgress((int) Math.round(percentage));

        percentageText.setText((df.format(percentage) + "%"));

        if (percentage >= 100) {
            percentageText.setTextColor(getResources().getColor(com.github.dhaval2404.colorpicker.R.color.light_green_A700));
        }
    }

    /**
     * Gets all user homework and modules and insert the top 3 most urgent homework into the recycler view
     *
     */
    private void setHomeworkRecyclerView() {
        ArrayList<Homework> inProgressHomeworkArrayList = HomeworkUtils.splitByIsCompletedHomeworkArrayList(module)[0];
        ArrayList<Homework> doneHomeworkArrayList = HomeworkUtils.splitByIsCompletedHomeworkArrayList(module)[1];

        if (inProgressHomeworkArrayList.size() > 0) {
            //addHomework.setVisibility(View.VISIBLE);
            viewMore.setVisibility(View.VISIBLE);
        }

        int inProgress = inProgressHomeworkArrayList.size();

        int done = doneHomeworkArrayList.size();

        inProgressStatus.setText(Integer.toString(inProgress));

        completedStatus.setText(Integer.toString(done));

        if (inProgressHomeworkArrayList.size() > 3) {
            inProgressHomeworkArrayList = new ArrayList<>(inProgressHomeworkArrayList.subList(0, 3));
        }

        HomeworkAdapter adapter = new HomeworkAdapter(inProgressHomeworkArrayList, localStorage.getModuleArrayList());
        recyclerView.setAdapter(adapter);
    }


    private void createHomeWork() {
        View.OnClickListener createHw = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addHw = new Intent(ModuleInfoActivity.this, HomeworkCreateActivity.class);
                startActivity(addHw);
            }
        };
        addHomework.setOnClickListener(createHw);
    }

    private void viewMoreHomework() {
        View.OnClickListener viewMoreHw = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editInfo = new Intent(ModuleInfoActivity.this, MainActivity.class);
                editInfo.putExtra("fragment", "homework");
                startActivity(editInfo);
            }
        };
        viewMore.setOnClickListener(viewMoreHw);
    }

    private void deleteModule(Module module) {
        View.OnClickListener deleteModule = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ModuleInfoActivity.this);

                builder.setMessage("Are you sure you want to delete " + moduleName + " module?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(ModuleInfoActivity.this, "Module deleted", Toast.LENGTH_SHORT).show();

                        ModuleUtils.removeModule(moduleArrayList, module);

                        localStorage.setModuleArrayList(moduleArrayList);

                        firebaseUtils.updateModuleArrayList(moduleArrayList);

                        finish();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        };
        delete.setOnClickListener(deleteModule);
    }

    private void editModule(Module module) {
        View.OnClickListener editModule = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editInfo = new Intent(ModuleInfoActivity.this, ModuleUpdateActivity.class);
                Gson gson = new Gson();
                String moduleString = gson.toJson(module);
                editInfo.putExtra("module", moduleString);
                startActivity(editInfo);
            }
        };
        edit.setOnClickListener(editModule);
    }

    public void checkWeek() {
        int lastCallDate = Calendar.MONDAY;
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_WEEK);
        if (day % lastCallDate == 0) {
            progressBar.setProgress(0);
        }

    }

    public double getTotalStudyTime(ArrayList<Double> timeList) {
        double sum = 0f;
        for (Double i : timeList) {
            sum += i;
        }
        return sum / 3600;
    }


}
