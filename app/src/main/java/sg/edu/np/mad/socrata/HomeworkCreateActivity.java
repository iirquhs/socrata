package sg.edu.np.mad.socrata;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class HomeworkCreateActivity extends AppCompatActivity {
    String hwName, moduleName, dueDate;

    String createNewModule = "Create New Module + ";

    ArrayList<Module> moduleArrayList;

    EditText editTextHomeworkName;

    TextView textViewDueTime, textViewDatePicker, textViewAddNotification;

    Spinner spinnerModules;

    private DatePickerDialog datePickerDialog;

    FirebaseUtils firebaseUtils;

    LocalStorage localStorage;

    RecyclerView recyclerViewNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_homework);

        initDatePicker();

        HomeworkReminderAdapter homeworkReminderAdapter = new HomeworkReminderAdapter();

        recyclerViewNotification = findViewById(R.id.recyclerViewNotification);
        recyclerViewNotification.setAdapter(homeworkReminderAdapter);

        LinearLayoutManager layout = new LinearLayoutManager(this);

        recyclerViewNotification.setLayoutManager(layout);
        recyclerViewNotification.setNestedScrollingEnabled(false);

        textViewAddNotification = findViewById(R.id.textViewAddNotification);
        textViewAddNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNotificationDialog addNotificationDialog = new AddNotificationDialog(
                        HomeworkCreateActivity.this, homeworkReminderAdapter);
                addNotificationDialog.show();
            }
        });

        textViewDueTime = findViewById(R.id.textViewDueTime);
        textViewDueTime.setText("23:59");

        textViewDatePicker = findViewById(R.id.textViewDatePicker);
        textViewDatePicker.setText(getTodayDate());

        editTextHomeworkName = findViewById(R.id.editTextHomeworkName);

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
                        textViewDueTime.setText(String.format(Locale.getDefault() ,"%02d:%02d", selectedHour, selectedMinute));
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

                String dueDateTimeString = textViewDatePicker.getText() + " " + textViewDueTime.getText();

                Homework homework = new Homework(hwName, dueDateTimeString, module.getModuleName());

                if (LocalDateTime.now().isAfter(homework.ConvertDueDateTime(homework.getDueDateTimeString()))) {
                    textViewDatePicker.setError("Cannot set the due date time to be before the current date time");
                    textViewDatePicker.requestFocus();
                    return;
                }

                AlarmManagerHelper alarmManagerHelper = new AlarmManagerHelper(HomeworkCreateActivity.this);

                ArrayList<PendingIntent> notificationPendingIntents = new ArrayList<>();

                for (int i = 0; i < homeworkReminderAdapter.getItemCount(); i++) {
                    HomeworkReminder homeworkReminder = homeworkReminderAdapter.getHomeworkReminderArrayList().get(i);

                    PendingIntent notificationPendingIntent = alarmManagerHelper.setHomeworkReminderAlarm(homework, homeworkReminder);
                    notificationPendingIntents.add(notificationPendingIntent);
                }

                localStorage.addHomeworkNotificationPendingIntents(homework, notificationPendingIntents);

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
                textViewDatePicker.setText(date);
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
