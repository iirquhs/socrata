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

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
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
import java.util.HashMap;
import java.util.Map;

public class ModuleInfoActivity extends AppCompatActivity {
    TextView moduleName, hours, goal, purplePercentage, greyPercentage, viewMore, percentageText, completedStatus, inProgressStatus, addHomework;
    ImageButton backButton;
    ImageView edit, delete;
    ProgressBar progressBar;
    Button buttonStudy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_module_info);
        Intent intent = getIntent();
        Gson gson = new Gson();
        Module module = gson.fromJson(intent.getStringExtra("module"), Module.class);
        moduleName = findViewById(R.id.moduleName);
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
        View.OnClickListener editModule = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editInfo = new Intent(ModuleInfoActivity.this, ModuleUpdate.class);
                Gson gson = new Gson();
                String moduleString = gson.toJson(module);
                editInfo.putExtra("module", moduleString);
                startActivity(editInfo);
            }
        };
        edit.setOnClickListener(editModule);

        delete = findViewById(R.id.deletemodule);
        View.OnClickListener deleteModule = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ModuleInfoActivity.this);

                builder.setMessage("Are you sure you want to delete " + module.getModuleName() + " module?");
                String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference Module = FirebaseDatabase.getInstance().getReference("Users").child(currentuser).child("modules");
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
                        Intent goBack = new Intent(ModuleInfoActivity.this, MainActivity.class);
                        Gson gson = new Gson();
                        String moduleString = gson.toJson(module);
                        goBack.putExtra("module", moduleString);
                        goBack.putExtra("fragment", "module");
                        Toast.makeText(ModuleInfoActivity.this,"Module deleted", Toast.LENGTH_SHORT).show();
                        startActivity(goBack);
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

        viewMore = findViewById(R.id.viewmore);
        View.OnClickListener viewMoreHw = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editInfo = new Intent(ModuleInfoActivity.this, MainActivity.class);
                editInfo.putExtra("fragment", "homework");
                startActivity(editInfo);
            }
        };
        viewMore.setOnClickListener(viewMoreHw);

        View.OnClickListener createHw = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addHw = new Intent(ModuleInfoActivity.this, HomeworkCreateActivity.class);
                startActivity(addHw);
            }
        };
        addHomework.setOnClickListener(createHw);


        Integer H = module.getTargetHoursPerWeek();
        moduleName.setText(module.getModuleName());
        hours.setText(H.toString());
        goal.setText(module.getTargetGrade());
        RecyclerView recyclerView = findViewById(R.id.recyclerView2);
        LinearLayoutManager layout = new LinearLayoutManager(ModuleInfoActivity.this);
        recyclerView.setLayoutManager(layout);
        recyclerView.setNestedScrollingEnabled(false);
        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("Users").child(currentUser);
        DatabaseReference moduleReference = userReference.child("modules");
        moduleReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String, Module> moduleMap = new HashMap<>();
                String thisModuleRef = "";
                for (DataSnapshot moduleEntry : snapshot.getChildren()) {
                    String moduleRef = moduleEntry.getKey();

                    int targetHoursPerWeek = moduleEntry.child("targetHoursPerWeek").getValue(int.class);
                    @ColorInt int color = moduleEntry.child("color").getValue(int.class);
                    String moduleName = moduleEntry.child("moduleName").getValue(String.class);
                    String targetGrade = moduleEntry.child("targetGrade").getValue(String.class);

                    Module Module = new Module(moduleName, targetGrade, targetHoursPerWeek, color);
                    if(Module.getModuleName().equals(module.getModuleName())){
                        thisModuleRef = moduleRef;
                    }
                    moduleMap.put(moduleRef, Module);
                }


                DatabaseReference homeworkReference = userReference.child("homework");
                homeworkReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList<Homework> homeworkArrayList = new ArrayList<>(3);
                        ArrayList<Homework> doneHomeworkArrayList = new ArrayList<>();
                        for (DataSnapshot homeworkSnapshot : snapshot.getChildren()) {
                            Homework homework = homeworkSnapshot.getValue(Homework.class);
                            //LocalDateTime dueDateTime = LocalDateTime.of()
                            //Log.d("TAG", dueDateTime.toString());
                            String moduleRef = homework.getModuleRef();
                            Module moduleCheck =  moduleMap.get(moduleRef);
                            if(moduleCheck.getModuleName().equals(module.getModuleName()) && homeworkArrayList.size() < 3 && homework.getStatus().equals("In Progress")){
                                homeworkArrayList.add(homework);
                                Integer i = homeworkArrayList.size();
                                inProgressStatus.setText(i.toString());
                            }
                            if(moduleCheck.getModuleName().equals(module.getModuleName()) && homework.getStatus().equals("Done")){
                                doneHomeworkArrayList.add(homework);
                                Integer c = doneHomeworkArrayList.size();
                                completedStatus.setText(c.toString());
                            }

                        }
                        if(homeworkArrayList.size() == 0){
                            addHomework.setVisibility(View.VISIBLE);
                            viewMore.setVisibility(View.INVISIBLE);

                        }

                        HomeworkAdapter adapter = new HomeworkAdapter(homeworkArrayList, moduleMap);

                        recyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                ArrayList<Double> studyTimings = new ArrayList<>();
                DatabaseReference studySessionReference = userReference.child("modules").child(thisModuleRef).child("studySessions");
                studySessionReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot studySessionSnapshot : snapshot.getChildren()) {
                            Double time = studySessionSnapshot.child("studyTime").getValue(Double.class);
                            studyTimings.add(time);
                            Log.d("Asdasdf", time.toString());
                        }
                        Double holder = getTotalStudyTime(studyTimings);
                        module.setStudyTime(holder);
                        DecimalFormat df = new DecimalFormat("#.#");
                        checkWeek();
                        greyPercentage.setText(" / "+module.getTargetHoursPerWeek()+"h");
                        purplePercentage.setText(df.format(module.getStudyTime())+"h");
                        int a = (int) Math.round(holder);
                        progressBar.setProgress(a);
                        Double percentage = (module.getStudyTime()/module.getTargetHoursPerWeek()) * 100;
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

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




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
