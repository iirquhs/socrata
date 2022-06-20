package sg.edu.np.mad.socrata;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.github.dhaval2404.colorpicker.ColorPickerDialog;
import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog;
import com.github.dhaval2404.colorpicker.listener.ColorListener;
import com.github.dhaval2404.colorpicker.model.ColorShape;
import com.github.dhaval2404.colorpicker.model.ColorSwatch;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.Map;

public class ModuleCreate extends AppCompatActivity{
    EditText modulename,targethours,goals;
    View colour;
    String name;
    String hours;
    String goal;
    Integer finalcolour;

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
        View colourselect = findViewById(R.id.colourselector);
        View colourbox = findViewById(R.id.colourbox);
        View yellow = findViewById(R.id.yelllow);
        View orange = findViewById(R.id.orange);
        View red = findViewById(R.id.red);
        View green = findViewById(R.id.green);
        View lightblue = findViewById(R.id.lightblue);
        View blue = findViewById(R.id.blue);
        View purple = findViewById(R.id.purple);
        View darkblue = findViewById(R.id.darkblue);
        View yellowtick = findViewById(R.id.yellowtick);
        View orangetick = findViewById(R.id.orangetick);
        View redtick = findViewById(R.id.redtick);
        View greentick = findViewById(R.id.greentick);
        View lightbluetick = findViewById(R.id.lightbluetick);
        View bluetick = findViewById(R.id.bluetick);
        View purpletick = findViewById(R.id.purpletick);
        View darkbluetick = findViewById(R.id.darkbluetick);
        View background = findViewById(R.id.background);
        View close = findViewById(R.id.close);
        finalcolour = 17170459;
        //#ff00ddff, #ff0099cc, #ff669900, #ffff8800,  #ff99cc00, #ffffbb33, #ffaa66cc, #ffff4444
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colourbox.setVisibility(v.VISIBLE);
                yellow.setVisibility(v.VISIBLE);
                orange.setVisibility(v.VISIBLE);
                red.setVisibility(v.VISIBLE);
                green.setVisibility(v.VISIBLE);
                lightblue.setVisibility(v.VISIBLE);
                blue.setVisibility(v.VISIBLE);
                purple.setVisibility(v.VISIBLE);
                darkblue.setVisibility(v.VISIBLE);
                close.setVisibility(v.VISIBLE);
                if(finalcolour == 17170459){yellowtick.setVisibility(View.VISIBLE);}
                else if(finalcolour == 17170451){orangetick.setVisibility(View.VISIBLE);}
                else if(finalcolour == 17170453){redtick.setVisibility(View.VISIBLE);}
                else if(finalcolour == 17170457){greentick.setVisibility(View.VISIBLE);}
                else if(finalcolour == 17170452){lightbluetick.setVisibility(View.VISIBLE);}
                else if(finalcolour == 17170456 ){bluetick.setVisibility(View.VISIBLE);}
                else if(finalcolour == 17170458 ){purpletick.setVisibility(View.VISIBLE);}
                else if(finalcolour == 17170454){darkbluetick.setVisibility(View.VISIBLE);}
            }
        };
        colourselect.setOnClickListener(listener);
        View.OnClickListener listener1 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yellowtick.setVisibility(v.VISIBLE);
                orangetick.setVisibility(v.INVISIBLE);
                redtick.setVisibility(v.INVISIBLE);
                greentick.setVisibility(v.INVISIBLE);
                lightbluetick.setVisibility(v.INVISIBLE);
                bluetick.setVisibility(v.INVISIBLE);
                purpletick.setVisibility(v.INVISIBLE);
                darkbluetick.setVisibility(v.INVISIBLE);
                ImageView lineColorCode = findViewById(R.id.colourselector);
                lineColorCode.setColorFilter(Color.parseColor("#ff00ddff"));
                finalcolour = Color.parseColor("#ff00ddff");
            }
        };
        yellow.setOnClickListener(listener1);
        View.OnClickListener listener2 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yellowtick.setVisibility(v.INVISIBLE);
                orangetick.setVisibility(v.VISIBLE);
                redtick.setVisibility(v.INVISIBLE);
                greentick.setVisibility(v.INVISIBLE);
                lightbluetick.setVisibility(v.INVISIBLE);
                bluetick.setVisibility(v.INVISIBLE);
                purpletick.setVisibility(v.INVISIBLE);
                darkbluetick.setVisibility(v.INVISIBLE);
                ImageView lineColorCode = findViewById(R.id.colourselector);
                lineColorCode.setColorFilter(Color.parseColor("#ff0099cc"));
                finalcolour = Color.parseColor("#ff0099cc");
            }
        };
        orange.setOnClickListener(listener2);
        View.OnClickListener listener3 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yellowtick.setVisibility(v.INVISIBLE);
                orangetick.setVisibility(v.INVISIBLE);
                redtick.setVisibility(v.VISIBLE);
                greentick.setVisibility(v.INVISIBLE);
                lightbluetick.setVisibility(v.INVISIBLE);
                bluetick.setVisibility(v.INVISIBLE);
                purpletick.setVisibility(v.INVISIBLE);
                darkbluetick.setVisibility(v.INVISIBLE);
                ImageView lineColorCode = findViewById(R.id.colourselector);
                lineColorCode.setColorFilter(Color.parseColor("#ff669900"));
                finalcolour = Color.parseColor("#ff669900");

            }
        };
        red.setOnClickListener(listener3);
        View.OnClickListener listener4 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yellowtick.setVisibility(v.INVISIBLE);
                orangetick.setVisibility(v.INVISIBLE);
                redtick.setVisibility(v.INVISIBLE);
                greentick.setVisibility(v.VISIBLE);
                lightbluetick.setVisibility(v.INVISIBLE);
                bluetick.setVisibility(v.INVISIBLE);
                purpletick.setVisibility(v.INVISIBLE);
                darkbluetick.setVisibility(v.INVISIBLE);
                ImageView lineColorCode = findViewById(R.id.colourselector);

                lineColorCode.setColorFilter(Color.parseColor("#ffff8800"));
                finalcolour = Color.parseColor("#ffff8800");

            }
        };
        green.setOnClickListener(listener4);
        View.OnClickListener listener5 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yellowtick.setVisibility(v.INVISIBLE);
                orangetick.setVisibility(v.INVISIBLE);
                redtick.setVisibility(v.INVISIBLE);
                greentick.setVisibility(v.INVISIBLE);
                lightbluetick.setVisibility(v.VISIBLE);
                bluetick.setVisibility(v.INVISIBLE);
                purpletick.setVisibility(v.INVISIBLE);
                darkbluetick.setVisibility(v.INVISIBLE);
                ImageView lineColorCode = findViewById(R.id.colourselector);
                lineColorCode.setColorFilter(Color.parseColor("#ff99cc00"));
                finalcolour = Color.parseColor("#ff99cc00");
            }
        };
        lightblue.setOnClickListener(listener5);
        View.OnClickListener listener6 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yellowtick.setVisibility(v.INVISIBLE);
                orangetick.setVisibility(v.INVISIBLE);
                redtick.setVisibility(v.INVISIBLE);
                greentick.setVisibility(v.INVISIBLE);
                lightbluetick.setVisibility(v.INVISIBLE);
                bluetick.setVisibility(v.VISIBLE);
                purpletick.setVisibility(v.INVISIBLE);
                darkbluetick.setVisibility(v.INVISIBLE);
                ImageView lineColorCode = findViewById(R.id.colourselector);
                lineColorCode.setColorFilter(Color.parseColor("#ffffbb33"));
                finalcolour = Color.parseColor("#ffffbb33");
            }
        };
        blue.setOnClickListener(listener6);
        View.OnClickListener listener7 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yellowtick.setVisibility(v.INVISIBLE);
                orangetick.setVisibility(v.INVISIBLE);
                redtick.setVisibility(v.INVISIBLE);
                greentick.setVisibility(v.INVISIBLE);
                lightbluetick.setVisibility(v.INVISIBLE);
                bluetick.setVisibility(v.INVISIBLE);
                purpletick.setVisibility(v.VISIBLE);
                darkbluetick.setVisibility(v.INVISIBLE);
                ImageView lineColorCode = findViewById(R.id.colourselector);
                lineColorCode.setColorFilter(Color.parseColor("#ffaa66cc"));
                finalcolour = Color.parseColor("#ffaa66cc");
            }
        };
        purple.setOnClickListener(listener7);
        View.OnClickListener listener8 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yellowtick.setVisibility(v.INVISIBLE);
                orangetick.setVisibility(v.INVISIBLE);
                redtick.setVisibility(v.INVISIBLE);
                greentick.setVisibility(v.INVISIBLE);
                lightbluetick.setVisibility(v.INVISIBLE);
                bluetick.setVisibility(v.INVISIBLE);
                purpletick.setVisibility(v.INVISIBLE);
                darkbluetick.setVisibility(v.VISIBLE);
                ImageView lineColorCode = findViewById(R.id.colourselector);
                lineColorCode.setColorFilter(Color.parseColor("#ffff4444"));
                finalcolour = Color.parseColor("#ffff4444");
            }
        };
        darkblue.setOnClickListener(listener8);
        View.OnClickListener listener9 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colourbox.setVisibility(v.INVISIBLE);
                yellow.setVisibility(v.INVISIBLE);
                orange.setVisibility(v.INVISIBLE);
                red.setVisibility(v.INVISIBLE);
                green.setVisibility(v.INVISIBLE);
                lightblue.setVisibility(v.INVISIBLE);
                blue.setVisibility(v.INVISIBLE);
                purple.setVisibility(v.INVISIBLE);
                darkblue.setVisibility(v.INVISIBLE);
                yellowtick.setVisibility(v.INVISIBLE);
                orangetick.setVisibility(v.INVISIBLE);
                redtick.setVisibility(v.INVISIBLE);
                greentick.setVisibility(v.INVISIBLE);
                lightbluetick.setVisibility(v.INVISIBLE);
                bluetick.setVisibility(v.INVISIBLE);
                purpletick.setVisibility(v.INVISIBLE);
                darkbluetick.setVisibility(v.INVISIBLE);
                close.setVisibility(v.INVISIBLE);
            }
        };
        background.setOnClickListener(listener9);
        close.setOnClickListener(listener9);
        DatabaseReference check = FirebaseDatabase.getInstance().getReference("Users").child(currentuser).child("modules");
        check.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<String> namelist =  ModuleUtils.getModuleNames((Map<String,Object>) dataSnapshot.getValue());
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

                                if (ModuleUtils.doesModuleExists(namelist, name)) {
                                    modulename.setError("Module already exists");
                                    modulename.requestFocus();
                                    return;
                                }

                                String id = Module.push().getKey();

                                @ColorInt int blackColor = ContextCompat.getColor(ModuleCreate.this, R.color.black);

                                Module module = new Module(name, goal, Integer.parseInt(hours), finalcolour);
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
}
