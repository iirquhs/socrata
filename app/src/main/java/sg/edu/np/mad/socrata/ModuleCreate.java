package sg.edu.np.mad.socrata;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.dhaval2404.colorpicker.ColorPickerDialog;
import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog;
import com.github.dhaval2404.colorpicker.listener.ColorListener;
import com.github.dhaval2404.colorpicker.model.ColorShape;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;


import java.util.ArrayList;
import java.util.Map;

public class ModuleCreate extends AppCompatActivity{
    EditText modulename,targethours,goals;
    View colour;
    String name;
    String hours;
    String goal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_module);
        String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d("m", currentuser);
        DatabaseReference Module = FirebaseDatabase.getInstance().getReference("Users").child(currentuser);
        modulename = findViewById(R.id.updatemodulename);
        targethours = findViewById(R.id.updatehours);
        goals = findViewById(R.id.updategoal);
        colour = findViewById(R.id.colourdecide);
        String[] colorArray = new String[]{"#f6e58d", "#ffbe76", "#ff7979",
                "#badc58", "#dff9fb", "#7ed6df", "#e056fd", "#686de0", "#30336b", "#95afc0"};
        View colourbox = findViewById(R.id.colourdecide);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Java Code

                /*new MaterialColorPickerDialog
                        .Builder(requireActivity())

                        // Option 1: Pass Hex Color Codes
                        //.setColors(colorArray)

                        // Option 2: Pass Hex Color Codes from string.xml
                        //.setColors(getResources().getStringArray(R.array.themeColorHex))

                        // Option 3: Pass color array from colors.xml
                        .setColorRes(getResources().getIntArray(R.array.themeColors))

                        .setColorListener(object : ColorListener {
                    override fun onColorSelected(color: Int, colorHex: String) {
                        // Handle Color Selection
                    }
                })
    .show();*/

            }
        };
        colourbox.setOnClickListener(listener);
        DatabaseReference check = FirebaseDatabase.getInstance().getReference("Users").child(currentuser).child("modules");
        check.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<String> namelist = comparedata((Map<String,Object>) dataSnapshot.getValue());
                        Button buttoncreate = findViewById(R.id.buttonupdatemodule);
                        buttoncreate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                name = modulename.getText().toString().trim();
                                hours = targethours.getText().toString().trim();
                                goal = goals.getText().toString().trim();

                                if (name.isEmpty()) {
                                    modulename.setError("Name is required");
                                    modulename.requestFocus();
                                    return;
                                }

                                if (hours.isEmpty()) {
                                    targethours.setError("set the target hours");
                                    targethours.requestFocus();
                                    return;
                                }
                                if (goal.isEmpty()) {
                                    goals.setError("set a goal");
                                    goals.requestFocus();
                                    return;
                                }
                                if(Integer.parseInt(hours) > 168){
                                    targethours.setError("a week only has 168 hours");
                                    targethours.requestFocus();
                                    return;
                                }
                                if(Integer.parseInt(hours) < 0 || Integer.parseInt(hours) == 0){
                                    targethours.setError("value cant be 0 or less");
                                    targethours.requestFocus();
                                    return;
                                }

                                for(int i = 0; i < namelist.size();i++) {
                                    if (name.equals(namelist.get(i))) {
                                        modulename.setError("Module already exists");
                                        modulename.requestFocus();
                                        return;
                                    }
                                }


                                String id = Module.push().getKey();
                                Module module = new Module(name, goal, Integer.parseInt(hours), R.color.black, ModuleCreate.this);
                                Module.child("modules").push().setValue(module);
                                //Module.child(id).setValue(module);
                                Intent intent = new Intent(ModuleCreate.this, MainActivity.class);
                                Toast.makeText(ModuleCreate.this,"Module created", Toast.LENGTH_SHORT).show();
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
