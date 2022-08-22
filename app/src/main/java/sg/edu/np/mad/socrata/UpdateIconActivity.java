package sg.edu.np.mad.socrata;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import android.view.View;

public class UpdateIconActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_icon);

        Button Icon1 =findViewById(R.id.Icon1);
        Icon1.setText("Set");
        Button Icon2 =findViewById(R.id.Icon2);
        Icon2.setText("Set");

        Button Goback = findViewById(R.id.goback);
        Goback.setText("Return to Profile");


        Icon1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setIcon1();
            }
        });
        Icon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setIcon2();
            }
        });
        Goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpdateIconActivity.this, ProfileEditActivity.class);
                startActivity(intent);
            }
        });



    }

    private void setIcon1() {

        // enable old icon
        getPackageManager().setComponentEnabledSetting(
                new ComponentName(UpdateIconActivity.this, "sg.edu.np.mad.socrata.MainActivity"),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
        getPackageManager().setComponentEnabledSetting(
                new ComponentName(UpdateIconActivity.this, "sg.edu.np.mad.socrata.MainActivityAlias"),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        Toast.makeText(UpdateIconActivity.this,"Enabled Icon1" ,Toast.LENGTH_LONG).show();
    }
    private void setIcon2() {

        // disable old icon
        // enable old icon
        getPackageManager().setComponentEnabledSetting(
                new ComponentName(UpdateIconActivity.this, "sg.edu.np.mad.socrata.MainActivityAlias"),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
        getPackageManager().setComponentEnabledSetting(
                new ComponentName(UpdateIconActivity.this, "sg.edu.np.mad.socrata.MainActivity"),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        Toast.makeText(UpdateIconActivity.this,"Enabled Icon2" ,Toast.LENGTH_LONG).show();
    }

}