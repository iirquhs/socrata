package sg.edu.np.mad.socrata;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.Map;

public class ModuleUpdate extends AppCompatActivity {
    EditText modulename, targethours, goals;
    View colour;
    String name;
    String hours;
    String goal;
    Integer finalcolourholder;
    Integer finalcolour;
    Integer updatecolour;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.updatemodule);
        String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        goal = intent.getStringExtra("goal");
        hours = intent.getStringExtra("hours");
        finalcolourholder = intent.getIntExtra("colour",0);
        Log.d("c", finalcolourholder.toString());
        modulename = findViewById(R.id.updatemodulename);
        targethours = findViewById(R.id.updatehours);
        goals = findViewById(R.id.updategoal);
        modulename.setText(name, TextView.BufferType.EDITABLE);
        targethours.setText(hours, TextView.BufferType.EDITABLE);
        goals.setText(goal, TextView.BufferType.EDITABLE);
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
        //#ff00ddff, #ff0099cc, #ff669900, #ffff8800,  #ff99cc00, #ffffbb33, #ffaa66cc, #ffff4444
        if(finalcolourholder == -16720385){
            finalcolour = 17170459;
            ImageView lineColorCode = findViewById(R.id.colourselector);
            lineColorCode.setColorFilter(Color.parseColor("#ff00ddff"));
        }
        else if(finalcolourholder == -16737844){
            finalcolour = 17170451;
            ImageView lineColorCode = findViewById(R.id.colourselector);
            lineColorCode.setColorFilter(Color.parseColor("#ff0099cc"));
        }
        else if(finalcolourholder == -10053376){
            finalcolour = 17170453;
            ImageView lineColorCode = findViewById(R.id.colourselector);
            lineColorCode.setColorFilter(Color.parseColor("#ff669900"));
        }
        else if(finalcolourholder == -30720){
            finalcolour = 17170457;
            ImageView lineColorCode = findViewById(R.id.colourselector);
            lineColorCode.setColorFilter(Color.parseColor("#ffff8800"));
        }
        else if(finalcolourholder == -6697984){
            finalcolour = 17170452;
            ImageView lineColorCode = findViewById(R.id.colourselector);
            lineColorCode.setColorFilter(Color.parseColor("#ff99cc00"));
        }
        else if(finalcolourholder == -17613){
            finalcolour = 17170456;
            ImageView lineColorCode = findViewById(R.id.colourselector);
            lineColorCode.setColorFilter(Color.parseColor("#ffffbb33"));
        }
        else if(finalcolourholder == -5609780){
            finalcolour = 17170458;
            ImageView lineColorCode = findViewById(R.id.colourselector);
            lineColorCode.setColorFilter(Color.parseColor("#ffaa66cc"));
        }
        else if(finalcolourholder == -48060){
            finalcolour = 17170454;
            ImageView lineColorCode = findViewById(R.id.colourselector);
            lineColorCode.setColorFilter(Color.parseColor("#ffff4444"));
        }
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
                if(finalcolour == 17170459){ yellowtick.setVisibility(View.VISIBLE);}
                else if(finalcolour == 17170451){
                    orangetick.setVisibility(View.VISIBLE);}
                else if(finalcolour == 17170453){
                    redtick.setVisibility(View.VISIBLE);}
                else if(finalcolour == 17170457){
                    greentick.setVisibility(View.VISIBLE);}
                else if(finalcolour == 17170452){
                    lightbluetick.setVisibility(View.VISIBLE);}
                else if(finalcolour == 17170456 ){
                    bluetick.setVisibility(View.VISIBLE);
                    }
                else if(finalcolour == 17170458 ){
                    purpletick.setVisibility(View.VISIBLE);
                }
                else if(finalcolour == 17170454){
                    darkbluetick.setVisibility(View.VISIBLE);
                }
            }
        };
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
                        ArrayList<String> namelist = ModuleUtils.getModuleNames((Map<String,Object>) dataSnapshot.getValue());
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
                                updatecolour = -16720385;
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
                                updatecolour = -16737844;
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
                                updatecolour = -10053376;

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
                                updatecolour = -30720;

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
                                updatecolour = -6697984;
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
                                updatecolour = -17613 ;
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
                                updatecolour = -5609780 ;
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
                                updatecolour = -48060;
                            }
                        };
                        darkblue.setOnClickListener(listener8);
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
                                Log.d("updatecik", updatecolour.toString());
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
                                            updateSnapshot.getRef().child("color").setValue(updatecolour);
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
}
