package sg.edu.np.mad.socrata;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class HomeworkCreate extends AppCompatActivity {
    EditText homeworkName, DueDate;
    Spinner Modules, Difficulty;
    String hwName, Module, difficulty, duedate;
    private DatePickerDialog datePickerDialog;
    private Button dateButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_homework);
        initDatePicker();
        dateButton = findViewById(R.id.datePickerButton);
        dateButton.setText(getTodaysDate());
        Difficulty = findViewById(R.id.Difficulty);
        String[] items = new String[]{"Easy", "Medium", "Hard"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        Difficulty.setAdapter(adapter);
        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference check = FirebaseDatabase.getInstance().getReference("Users").child(currentUser).child("modules");
        check.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<String> nameList =  ModuleUtils.getModuleNames((Map<String,Object>) dataSnapshot.getValue());
                        ArrayList<Module> moduleArrayList = getModules((Map<String,Object>) dataSnapshot.getValue());
                        Modules = findViewById(R.id.moduleDropdown);
                        ArrayAdapter<String>adapter = new ArrayAdapter<>(HomeworkCreate.this, android.R.layout.simple_spinner_item,nameList);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        Modules.setAdapter(adapter);
                        Button buttoncreate = findViewById(R.id.buttoncreatehomework);
                        buttoncreate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                homeworkName = findViewById(R.id.homeworkName);
                                Module = Modules.getSelectedItem().toString();
                                difficulty = Difficulty.getSelectedItem().toString();
                                hwName = homeworkName.getText().toString().trim();
                                Module module;
                                if (hwName.isEmpty()) {
                                    homeworkName.setError("Name is required");
                                    homeworkName.requestFocus();
                                    return;
                                }
                                for (int i =0; i < moduleArrayList.size(); i++){
                                    if(moduleArrayList.get(i).getModuleName().equals(Module)){
                                        module = moduleArrayList.get(i);
                                        Homework homework = new Homework(hwName, module);
                                        String id = check.push().getKey();
                                        Query moduleQuery = FirebaseDatabase.getInstance().getReference("Users").child(currentUser).child("modules")
                                                .orderByChild("moduleName").equalTo(Module);
                                        break;
                                    }
                                }


                            }

                        });

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        return;
                    }
                });


    }
    private String getTodaysDate()
    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        day += 1;
        return makeDateString(day, month, year);
    }

    private void initDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                String date = makeDateString(day, month, year);
                dateButton.setText(date);
                duedate = date;
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(new Date().getTime() - 10000);

    }

    private String makeDateString(int day, int month, int year)
    {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month)
    {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";

        //default should never happen
        return "JAN";
    }

    public void openDatePicker(View view)
    {
        datePickerDialog.show();
    }
    private ArrayList<Module> getModules(Map<String,Object> modules) {
        ArrayList<Module> moduleArrayList = new ArrayList<>();

        if (modules == null) {
            return null;
        }

        for (Map.Entry<String, Object> moduleMap : modules.entrySet()){
            Map moduleMapValue = (Map) moduleMap.getValue();

            String name = (String) moduleMapValue.get("moduleName");
            String goal = (String) moduleMapValue.get("targetGrade");
            int targetHoursPerWeek = ((Number) moduleMapValue.get("targetHoursPerWeek")).intValue();
            @ColorInt int colorInt = ((Number) moduleMapValue.get("color")).intValue();;

            Module module = new Module(name, goal, targetHoursPerWeek, colorInt);

            moduleArrayList.add(module);
        }

        return moduleArrayList;
    }

}
