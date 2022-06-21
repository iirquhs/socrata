package sg.edu.np.mad.socrata;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

public class ModuleInfo extends AppCompatActivity {
    TextView moduleName, hours, goal, purplePercentage, greyPercentage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moduleinfo);
        Intent intent = getIntent();
        Gson gson = new Gson();
        Module module = gson.fromJson(intent.getStringExtra("module"), Module.class);
        moduleName = findViewById(R.id.moduleName);
        hours = findViewById(R.id.targetHours);
        goal = findViewById(R.id.goal);
        Integer H = module.getTargetHoursPerWeek();
        moduleName.setText(module.getModuleName());
        hours.setText(H.toString());
        goal.setText(module.getTargetGrade());

    }


}
