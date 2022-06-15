package sg.edu.np.mad.socrata;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class ModuleUpdate extends AppCompatActivity {
    EditText modulename, targethours, goals;
    View colour;
    String name;
    String hours;
    String goal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.updatemodule);
        String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        goal = intent.getStringExtra("goal");
        hours = intent.getStringExtra("hours");
        Log.d("n", name);
        Log.d("g", goal);
        Log.d("h", hours);
        modulename = findViewById(R.id.updatemodulename);
        targethours = findViewById(R.id.updatehours);
        goals = findViewById(R.id.updategoal);
        modulename.setText(name, TextView.BufferType.EDITABLE);
        targethours.setText(hours, TextView.BufferType.EDITABLE);
        goals.setText(goal, TextView.BufferType.EDITABLE);
        DatabaseReference check = FirebaseDatabase.getInstance().getReference("Users").child(currentuser).child("modules");
        check.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<String> namelist = comparedata((Map<String,Object>) dataSnapshot.getValue());
                        Button buttonupdate = findViewById(R.id.buttonupdatemodule);
                        buttonupdate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                modulename = findViewById(R.id.updatemodulename);
                                targethours = findViewById(R.id.updatehours);
                                goals = findViewById(R.id.updategoal);
                                String updatename = modulename.getText().toString().trim();
                                String updatehours = targethours.getText().toString().trim();
                                String updategoal = goals.getText().toString().trim();

                                if (updatename.isEmpty()) {
                                    modulename.setError("Name is required");
                                    modulename.requestFocus();
                                    return;
                                }

                                if (updatehours.isEmpty()) {
                                    targethours.setError("set the target hours");
                                    targethours.requestFocus();
                                    return;
                                }
                                if (updategoal.isEmpty()) {
                                    goals.setError("set a goal");
                                    goals.requestFocus();
                                    return;
                                }
                                if(Integer.parseInt(updatehours) > 168){
                                    targethours.setError("a week only has 168 hours");
                                    targethours.requestFocus();
                                    return;
                                }
                                if(Integer.parseInt(updatehours) < 0 || Integer.parseInt(updatehours) == 0){
                                    targethours.setError("value cant be 0 or less");
                                    targethours.requestFocus();
                                    return;
                                }

                                for(int i = 0; i < namelist.size();i++) {
                                    if (updatename.equals(namelist.get(i))) {
                                        if(updatename.equals(name)){
                                            continue;
                                        }
                                        else {
                                            modulename.setError("Module already exists");
                                            modulename.requestFocus();
                                            return;
                                        }
                                    }

                                }
                                Query update = check.orderByChild("moduleName").equalTo(name);
                                update.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot updateSnapshot: dataSnapshot.getChildren()) {
                                            updateSnapshot.getRef().child("moduleName").setValue(updatename);
                                            updateSnapshot.getRef().child("targetGrade").setValue(updategoal);
                                            updateSnapshot.getRef().child("targetHoursPerWeek").setValue(Integer.parseInt(updatehours));
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Log.e("tag", "onCancelled", databaseError.toException());
                                    }
                                });
                                Intent intent = new Intent(ModuleUpdate.this, MainActivity.class);
                                Toast.makeText(ModuleUpdate.this,"Module updated", Toast.LENGTH_SHORT).show();
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);

                            }

                        });

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        return;
                    }
                });
    }
    private ArrayList<String> comparedata(Map<String,Object> users) {
        //iterate through each user, ignoring their UID
        ArrayList<String> namelist = new ArrayList<>();
        for (Map.Entry<String, Object> entry : users.entrySet()){
            //Get user map
            Map singleUser = (Map) entry.getValue();
            String name = (String) singleUser.get("moduleName");
            namelist.add(name);

        }
        return namelist;
    }
}
