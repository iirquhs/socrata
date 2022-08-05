package sg.edu.np.mad.socrata;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.Group;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.time.LocalTime;
import java.time.ZonedDateTime;

public class NotificationSettingActivity extends AppCompatActivity implements View.OnClickListener,
        CompoundButton.OnCheckedChangeListener, AdapterView.OnItemSelectedListener, TextWatcher {

    ImageButton imageButtonBack;

    Spinner spinnerDateFrequency;
    ArrayAdapter<CharSequence> adapterDateFrequency;

    SwitchCompat switchToggleNotification;

    TextView textViewSetTime, textViewTime;

    Button buttonSave;

    EditText editTextTimeInterval;

    LocalStorage localStorage;

    MotivationalQuoteSetting motivationalQuoteSetting;

    Group groupTimeOfDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_setting);

        localStorage = new LocalStorage(this);

        groupTimeOfDay = findViewById(R.id.groupTimeOfDay);

        imageButtonBack = findViewById(R.id.buttonBack);
        imageButtonBack.setOnClickListener(this);

        editTextTimeInterval = findViewById(R.id.editTextTimeBeforeDueDate);

        buttonSave = findViewById(R.id.buttonSave);

        switchToggleNotification = findViewById(R.id.switchToggleNotification);

        spinnerDateFrequency = findViewById(R.id.spinnerDateFrequency);

        textViewSetTime = findViewById(R.id.textViewSetTime);
        textViewTime = findViewById(R.id.textViewTime);

        adapterDateFrequency = ArrayAdapter.createFromResource(this, R.array.date_frequency_array,
                android.R.layout.simple_spinner_item);

        adapterDateFrequency.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerDateFrequency.setAdapter(adapterDateFrequency);

        buttonSave.setOnClickListener(this);
        textViewSetTime.setOnClickListener(this);

        switchToggleNotification.setOnCheckedChangeListener(this);

        spinnerDateFrequency.setOnItemSelectedListener(this);

        editTextTimeInterval.addTextChangedListener(this);

        loadSetting();

    }

    private void loadSetting() {
        motivationalQuoteSetting = localStorage.getMotivationalQuoteSetting();

        int position = adapterDateFrequency.getPosition(motivationalQuoteSetting.getFrequency());
        spinnerDateFrequency.setSelection(position);

        switchToggleNotification.setChecked(motivationalQuoteSetting.isNotificationOn());

        textViewTime.setText(motivationalQuoteSetting.getTimeOfDay().toString());

        editTextTimeInterval.setText(String.valueOf(motivationalQuoteSetting.getMultiplier()));

        if (motivationalQuoteSetting.getFrequency().equals("Minute") || motivationalQuoteSetting.getFrequency().equals("Hour")) {
            groupTimeOfDay.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.buttonBack) {
            finish();
        } else if (id == R.id.buttonSave) {
            localStorage.setMotivationalQuoteSetting(motivationalQuoteSetting);

            AlarmManagerHelper alarmManagerHelper = new AlarmManagerHelper(this);
            ZonedDateTime nextMotivationQuoteZonedDateTime = alarmManagerHelper.setMotivationalSettingAlarm(motivationalQuoteSetting);

            if (nextMotivationQuoteZonedDateTime != null) {
                Toast.makeText(NotificationSettingActivity.this, "Next Motivational quote will be in: " + nextMotivationQuoteZonedDateTime.toString(),
                        Toast.LENGTH_LONG).show();
            }

            finish();

        } else if (id == R.id.textViewSetTime) {
            TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener()
            {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    motivationalQuoteSetting.setTimeOfDay(LocalTime.of(selectedHour, selectedMinute));
                    textViewTime.setText(String.format(motivationalQuoteSetting.getTimeOfDay().toString(), "%02d:%02d"));
                }
            };

            LocalTime localTime = motivationalQuoteSetting.getTimeOfDay();
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener,
                    localTime.getHour(), localTime.getMinute(), true);

            timePickerDialog.show();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isOn) {
        if (compoundButton.getId() == R.id.switchToggleNotification) {
            motivationalQuoteSetting.setNotificationOn(isOn);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        if (adapterView.getId() == R.id.spinnerDateFrequency) {
            String frequency = adapterView.getSelectedItem().toString();
            motivationalQuoteSetting.setFrequency(frequency);
            if (frequency.equals("Minute") || frequency.equals("Hour")) {
                groupTimeOfDay.setVisibility(View.INVISIBLE);
            } else {
                groupTimeOfDay.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        try {
            motivationalQuoteSetting.setMultiplier(Integer.parseInt(editable.toString()));
        } catch (NumberFormatException ignored) {

        }
    }
}