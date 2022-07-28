package sg.edu.np.mad.socrata;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.time.Duration;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class HomeworkCreateActivity extends AppCompatActivity {
    String hwName, moduleName, dueDate;

    String createNewModule = "Create New Module + ";

    ArrayList<Module> moduleArrayList;

    EditText editTextHomeworkName, editTextTimeBeforeDueDate;

    TextView textViewDueTime;

    Spinner spinnerModules, spinnerDateFrequency;

    private DatePickerDialog datePickerDialog;
    private Button dateButton;

    FirebaseUtils firebaseUtils;

    LocalStorage localStorage;

    ArrayAdapter<CharSequence> adapterDateFrequency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_homework);

        initDatePicker();

        textViewDueTime = findViewById(R.id.textViewDueTime);
        textViewDueTime.setText("23:59");

        editTextTimeBeforeDueDate = findViewById(R.id.editTextTimeInterval);
        editTextTimeBeforeDueDate.setText("1");

        dateButton = findViewById(R.id.datePickerButton);
        dateButton.setText(getTodayDate());

        editTextHomeworkName = findViewById(R.id.editTextHomeworkName);

        spinnerDateFrequency = findViewById(R.id.spinnerDateFrequency);

        adapterDateFrequency = ArrayAdapter.createFromResource(this, R.array.date_frequency_array,
                android.R.layout.simple_spinner_item);

        adapterDateFrequency.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerDateFrequency.setAdapter(adapterDateFrequency);
        spinnerDateFrequency.setSelection(2);

        localStorage = new LocalStorage(this);
        firebaseUtils = new FirebaseUtils();

        //Difficulty = findViewById(R.id.Difficulty);
        //String[] items = new String[]{"Easy", "Medium", "Hard"};
        //ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //Difficulty.setAdapter(adapter);

        textViewDueTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener()
                {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String dueTime = selectedHour + ":" + selectedMinute;
                        textViewDueTime.setText(String.format(dueTime, "%02d:%02d"));
                    }
                };

                TimePickerDialog timePickerDialog = new TimePickerDialog(HomeworkCreateActivity.this, onTimeSetListener,
                        23, 59, true);

                timePickerDialog.show();
            }
        });

        ImageButton buttonBack = findViewById(R.id.backButton);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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

                Module module = moduleArrayList.get(ModuleUtils.findModule(moduleArrayList, moduleName));

                // Check if homework name is taken for this module
                for (Homework homework : module.getHomeworkArrayList()) {
                    if (homework.getHomeworkName().equalsIgnoreCase(hwName)) {
                        editTextHomeworkName.setError("Homework name is taken");
                        editTextHomeworkName.requestFocus();
                        return;
                    }
                }

                String dueDateTimeString = dateButton.getText() + " " + textViewDueTime.getText();

                Homework homework = new Homework(hwName, dueDateTimeString, module.getModuleName());

                AlarmManagerHelper alarmManagerHelper = new AlarmManagerHelper(HomeworkCreateActivity.this);

                long minutesBeforeDueDate = Long.parseLong(editTextTimeBeforeDueDate.getText().toString());
                String dateFrequency = spinnerDateFrequency.getSelectedItem().toString();

                alarmManagerHelper.setHomeworkReminderAlarm(homework, minutesBeforeDueDate, dateFrequency);

                ArrayList<Homework> moduleHomeworkArrayList = module.getHomeworkArrayList();
                moduleHomeworkArrayList.add(homework);

                localStorage.setHomeworkArrayList(moduleHomeworkArrayList, module.getModuleName());
                firebaseUtils.updateHomeworkArrayList(moduleHomeworkArrayList, moduleArrayList, module.getModuleName());

                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        moduleArrayList = localStorage.getModuleArrayList();
        setModuleDropDown(moduleArrayList);
    }

    /**
     * Set module drop down list to display to the user
     * @param moduleArrayList
     */
    private void setModuleDropDown(ArrayList<Module> moduleArrayList) {
        ArrayList<String> nameList = new ArrayList<>();

        if (moduleArrayList.size() <= 0) {
            Toast.makeText(this, "You must create a module first before creating a new homework.", Toast.LENGTH_SHORT).show();
        }

        for (Module module : moduleArrayList) {
            nameList.add(module.getModuleName());
        }

        nameList.add(createNewModule);

        spinnerModules = findViewById(R.id.moduleDropdown);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(HomeworkCreateActivity.this, android.R.layout.simple_spinner_item, nameList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerModules.setAdapter(adapter);

        spinnerModules.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                moduleName = spinnerModules.getSelectedItem().toString();
                if (moduleName.equals(createNewModule)) {
                    Intent intent = new Intent(HomeworkCreateActivity.this, ModuleUpdateActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    /**
     * Convert today's date into string
     * @return
     */
    private String getTodayDate() {
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        return makeDateString(zonedDateTime.getDayOfMonth(), zonedDateTime.getMonthValue(), zonedDateTime.getYear());
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                dateButton.setText(date);
                dueDate = date;
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

    private String makeDateString(int day, int month, int year) {
        if (day >= 10) {
            return getMonthFormat(month) + " " + day + " " + year;
        } else {
            return getMonthFormat(month) + " 0" + day + " " + year;
        }
    }

    private String getMonthFormat(int month) {
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

    public void openDatePicker(View view) {
        datePickerDialog.show();
    }

}
