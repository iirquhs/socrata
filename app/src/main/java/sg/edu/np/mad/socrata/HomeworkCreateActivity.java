package sg.edu.np.mad.socrata;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class HomeworkCreateActivity extends AppCompatActivity {
    String hwName, moduleName, duedate;

    Map<String, Module> moduleMap;
    ArrayList<Homework> homeworkArrayList = new ArrayList<>();

    EditText editTextHomeworkName, DueDate;

    Spinner spinnerModules;

    private DatePickerDialog datePickerDialog;

    private Button dateButton;

    DatabaseReference currentUserRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_homework);

        initDatePicker();

        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        currentUserRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUser);

        getHomework();

        dateButton = findViewById(R.id.datePickerButton);
        dateButton.setText(getTodaysDate());

        editTextHomeworkName = findViewById(R.id.editTextHomeworkName);

        //Difficulty = findViewById(R.id.Difficulty);
        //String[] items = new String[]{"Easy", "Medium", "Hard"};
        //ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //Difficulty.setAdapter(adapter);

        currentUserRef.child("modules").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                moduleMap = ModuleUtlis.parseModuleMap((Map<String, Object>) dataSnapshot.getValue());

                assert moduleMap != null;
                setModuleDropDown(moduleMap.values());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Button buttonCreate = findViewById(R.id.buttoncreatehomework);
        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moduleName = spinnerModules.getSelectedItem().toString();
                hwName = editTextHomeworkName.getText().toString().trim();

                //difficulty = Difficulty.getSelectedItem().toString();

                if (hwName.isEmpty()) {
                    editTextHomeworkName.setError("Name is required");
                    editTextHomeworkName.requestFocus();
                    return;
                }

                for (Homework homework : homeworkArrayList) {
                    if (homework.getHomeworkName().equals(hwName) && moduleMap.get(homework.getModuleRef()).getModuleName().equals(moduleName)) {
                        editTextHomeworkName.setError("Homework name is taken");
                        editTextHomeworkName.requestFocus();
                        return;
                    }
                }

                currentUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String moduleKey = getModule(moduleMap, moduleName);

                        String dueDateTimeString = dateButton.getText() + " 23:59";

                        Map<String, Object> homeworkMap = new HashMap<>();

                        String refKey = snapshot.child("homework").getRef().push().getKey();

                        Homework homework = new Homework(hwName, dueDateTimeString, moduleKey);

                        homeworkMap.put(refKey, homework);

                        snapshot.child("homework").getRef().updateChildren(homeworkMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Intent intent = new Intent(HomeworkCreateActivity.this, MainActivity.class);
                                intent.putExtra("fragment", "homework");
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    private void getHomework() {
        currentUserRef.child("homework").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot homeworkSnapshot : snapshot.getChildren()) {
                    Homework homework = homeworkSnapshot.getValue(Homework.class);
                    homeworkArrayList.add(homework);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private String getModule(Map<String, Module> moduleMap, String moduleName) {
        for (Map.Entry<String, Module> moduleEntry : moduleMap.entrySet()) {
            if (moduleEntry.getValue().getModuleName().equals(moduleName)) {
                return moduleEntry.getKey();
            }
        }
        return null;
    }

    private void setModuleDropDown (Collection<Module> moduleArrayList)
    {
        ArrayList<String> nameList = new ArrayList<>();

        for (Module module : moduleArrayList) {
            nameList.add(module.getModuleName());
        }

        spinnerModules = findViewById(R.id.moduleDropdown);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(HomeworkCreateActivity.this, android.R.layout.simple_spinner_item, nameList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerModules.setAdapter(adapter);
    }

    private String getTodaysDate ()
    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        day += 1;
        return makeDateString(day, month, year);
    }

    private void initDatePicker ()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
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

    private String makeDateString ( int day, int month, int year)
    {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat ( int month)
    {
        if (month == 1)
            return "JAN";
        if (month == 2)
            return "FEB";
        if (month == 3)
            return "MAR";
        if (month == 4)
            return "APR";
        if (month == 5)
            return "MAY";
        if (month == 6)
            return "JUN";
        if (month == 7)
            return "JUL";
        if (month == 8)
            return "AUG";
        if (month == 9)
            return "SEP";
        if (month == 10)
            return "OCT";
        if (month == 11)
            return "NOV";
        if (month == 12)
            return "DEC";

        //default should never happen
        return "JAN";
    }

    public void openDatePicker (View view)
    {
        datePickerDialog.show();
    }

}
