package sg.edu.np.mad.socrata;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class AddNotificationDialog extends Dialog implements View.OnClickListener {

    EditText editTextTimeBeforeDueDate;

    Spinner spinnerDateFrequency;

    Button buttonDone;

    HomeworkReminderAdapter homeworkReminderAdapter;

    public AddNotificationDialog(@NonNull Context context, HomeworkReminderAdapter homeworkReminderAdapter) {
        super(context);
        this.setContentView(R.layout.dialog_add_notification);

        this.homeworkReminderAdapter = homeworkReminderAdapter;

        editTextTimeBeforeDueDate = findViewById(R.id.editTextTimeBeforeDueDate);
        editTextTimeBeforeDueDate.setText("1");

        spinnerDateFrequency = findViewById(R.id.spinnerDateFrequency);

        ArrayAdapter<CharSequence> adapterDateFrequency = ArrayAdapter.createFromResource(context, R.array.date_frequency_array,
                android.R.layout.simple_spinner_item);
        adapterDateFrequency.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerDateFrequency.setAdapter(adapterDateFrequency);

        spinnerDateFrequency.setSelection(2);

        buttonDone = findViewById(R.id.buttonDone);
        buttonDone.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.buttonDone) {
            HomeworkReminder homeworkReminder = new HomeworkReminder(
                    Long.parseLong(editTextTimeBeforeDueDate.getText().toString()),
                    spinnerDateFrequency.getSelectedItem().toString());

            if (isHomeworkReminderUnique(homeworkReminder, homeworkReminderAdapter.getHomeworkReminderArrayList())) {
                homeworkReminderAdapter.addHomeworkReminder(homeworkReminder);
            }
        }
        dismiss();
    }

    boolean isHomeworkReminderUnique(HomeworkReminder newHomeworkReminder, ArrayList<HomeworkReminder> homeworkReminderArrayList) {
        for (HomeworkReminder homeworkReminder : homeworkReminderArrayList) {
            if (homeworkReminder.makeText().equals(newHomeworkReminder.makeText())) {
                return false;
            }
        }
        return true;
    }
}
