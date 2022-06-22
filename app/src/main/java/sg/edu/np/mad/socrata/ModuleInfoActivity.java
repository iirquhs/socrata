package sg.edu.np.mad.socrata;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

public class ModuleInfoActivity extends AppCompatActivity {
    TextView textViewModuleName, hours, goal, purplePercentage, greyPercentage, viewMore, percentageText, completedStatus, inProgressStatus, addHomework;
    ImageButton backButton;
    ImageView edit, delete;
    ProgressBar progressBar;
    Button buttonStudy;

    RecyclerView recyclerView;

    Map<String, Module> moduleMap;

    DatabaseReference userReference;

    Module module;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_module_info);

        Intent intent = getIntent();

        Gson gson = new Gson();

        module = gson.fromJson(intent.getStringExtra("module"), Module.class);

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
        buttonStudy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent timerIntent = new Intent(ModuleInfoActivity.this, TimerActivity.class);
                timerIntent.putExtra("module_name", module.getModuleName());
                startActivity(timerIntent);
            }
        });

        edit = findViewById(R.id.editmodule);
        editModule(module);

        delete = findViewById(R.id.deletemodule);
        deleteModule(module);

        viewMore = findViewById(R.id.viewmore);
        viewMoreHomework();

        createHomeWork();

        int H = module.getTargetHoursPerWeek();
        textViewModuleName.setText(module.getModuleName());
        hours.setText(Integer.toString(H));
        goal.setText(module.getTargetGrade());

        recyclerView = findViewById(R.id.recyclerView2);

        LinearLayoutManager layout = new LinearLayoutManager(ModuleInfoActivity.this);

        recyclerView.setLayoutManager(layout);
        recyclerView.setNestedScrollingEnabled(false);

        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        userReference = FirebaseDatabase.getInstance().getReference("Users").child(currentUser);

        DatabaseReference moduleReference = userReference.child("modules");
        moduleReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                moduleMap = ModuleUtils.parseModuleMap((Map<String, Object>) snapshot.getValue());

                DatabaseReference homeworkReference = userReference.child("homework");
                setHomeworkRecyclerView(homeworkReference, module);

                String moduleRef = "";

                for (Map.Entry<String, Module> moduleEntry : moduleMap.entrySet()) {
                    if (moduleEntry.getValue().getModuleName().equals(module.getModuleName())) {
                        moduleRef = moduleEntry.getKey();
                        break;
                    }
                }

                ArrayList<Double> studyTimings = new ArrayList<>();

                DatabaseReference studySessionReference = userReference.child("modules").child(moduleRef).child("studySessions");

                setStudyProgressBarSection(studyTimings, studySessionReference, module);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        DatabaseReference homeworkReference = userReference.child("homework");
        setHomeworkRecyclerView(homeworkReference, module);
    }

    private void setStudyProgressBarSection(ArrayList<Double> studyTimings, DatabaseReference studySessionReference, Module module) {
        studySessionReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot studySessionSnapshot : snapshot.getChildren()) {
                    Double time = studySessionSnapshot.child("studyTime").getValue(Double.class);
                    studyTimings.add(time);

                    assert time != null;
                    Log.d("Asdasdf", time.toString());
                }

                // in hours
                double holder = getTotalStudyTime(studyTimings);

                module.setStudyTime(holder);

                DecimalFormat df = new DecimalFormat("#.#");

                checkWeek();

                greyPercentage.setText(" / "+ module.getTargetHoursPerWeek()+"h");
                purplePercentage.setText(df.format(module.getStudyTime())+"h");

                Double percentage = (module.getStudyTime()/ module.getTargetHoursPerWeek()) * 100;

                progressBar.setProgress((int) Math.round(percentage));

                percentageText.setText((df.format(percentage)+"%"));

                if(percentage >= 100){
                    percentageText.setTextColor( getResources().getColor(com.github.dhaval2404.colorpicker.R.color.light_green_A700));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setHomeworkRecyclerView(DatabaseReference homeworkReference, Module module) {
        homeworkReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ArrayList<Homework> inProgressHomeworkArrayList = new ArrayList<>();

                for (DataSnapshot homeworkSnapshot : snapshot.getChildren()) {
                    Homework homework = homeworkSnapshot.getValue(Homework.class);

                    assert homework != null;
                    String moduleRef = homework.getModuleRef();
                    Module moduleCheck =  moduleMap.get(moduleRef);

                    assert moduleCheck != null;
                    if(moduleCheck.getModuleName().equals(module.getModuleName()) && inProgressHomeworkArrayList.size() < 3 && homework.getStatus().equals("In Progress")){
                        inProgressHomeworkArrayList.add(homework);
                    }

                }

                if(inProgressHomeworkArrayList.size() <= 0){
                    addHomework.setVisibility(View.VISIBLE);
                    viewMore.setVisibility(View.INVISIBLE);
                }

                HomeworkAdapter adapter = new HomeworkAdapter(inProgressHomeworkArrayList, moduleMap);

                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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

                builder.setMessage("Are you sure you want to delete " + module.getModuleName() + " module?");

                String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference Module = FirebaseDatabase.getInstance().getReference("Users").child(currentUser).child("modules");
                Query delete = Module.orderByChild("moduleName").equalTo( module.getModuleName());

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        delete.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                    appleSnapshot.getRef().removeValue();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.e("tag", "onCancelled", databaseError.toException());
                            }
                        });

                        Toast.makeText(ModuleInfoActivity.this,"Module deleted", Toast.LENGTH_SHORT).show();
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

    public void checkWeek(){
        int lastCallDate = Calendar.MONDAY;
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_WEEK);
        if (day %lastCallDate ==0){
            progressBar.setProgress(0);
        }

    }
    public double getTotalStudyTime(ArrayList<Double> timeList) {
        double sum = 0f;
        for (Double i : timeList) {
            sum += i;
        }
        return sum/3600;
    }


}
