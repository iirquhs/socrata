package sg.edu.np.mad.socrata;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ModuleUpdateActivity extends AppCompatActivity {

    ImageView lightBlueTick, blueTick,
            darkGreenTick, orangeTick,
            lightGreenTick,
            yellowTick, purpleTick, redTick;

    ImageView colourSelector, colourBox, close;

    EditText editTextModuleName, editTextTargetHours, editTextGoal;

    TextView textViewTitle;

    ConstraintLayout background;

    Map<ImageView, Integer> colourSelectorToColour = new LinkedHashMap<>();

    @ColorInt int colours[] = {
        Color.parseColor("#ff00ddff"),
        Color.parseColor("#ff0099cc"),
        Color.parseColor("#ff669900"),
        Color.parseColor("#ffff8800"),
        Color.parseColor("#ff99cc00"),
        Color.parseColor("#ffffbb33"),
        Color.parseColor("#ffaa66cc"),
        Color.parseColor("#ffff4444")
    };

    @ColorInt Integer selectedColour = colours[0];

    Map<Integer, View> colourToView = new LinkedHashMap<Integer, View>();

    List<String> moduleNameList = new ArrayList<>();

    DatabaseReference myRef;

    String refKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_module);
        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Intent intent = getIntent();

        myRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUser).child("modules");
        refKey = myRef.push().getKey();

        Gson gson = new Gson();
        Module module = gson.fromJson(intent.getStringExtra("module"), Module.class);

        textViewTitle = findViewById(R.id.sliderTitle);

        if (module != null) {
            textViewTitle.setText("Edit Module");
            selectedColour = module.getColor();
            getModuleNames(myRef, module.getModuleName());
            getModuleRefKey(module.getModuleName());

        } else {
            getModuleNames(myRef, null);
        }

        //Gets the colour
        colourPicker();

        editTextModuleName = findViewById(R.id.updatemodulename);
        editTextTargetHours = findViewById(R.id.updatehours);
        editTextGoal = findViewById(R.id.updategoal);

        if (module != null) {
            editTextModuleName.setText(module.getModuleName());
            editTextTargetHours.setText(Integer.toString(module.getTargetHoursPerWeek()));
            editTextGoal.setText(module.getTargetGrade());
            selectedColour = module.getColor();
        }

        ImageButton buttonBack = findViewById(R.id.backButton);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button buttonUpdate = findViewById(R.id.buttonupdatemodule);
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextModuleName.getText().toString().trim();
                String targetHours = editTextTargetHours.getText().toString().trim();
                String goal = editTextGoal.getText().toString().trim();

                if (!isInputValid(name, targetHours, goal)) return;

                Module module = new Module(name, goal, Integer.parseInt(targetHours), selectedColour);

                myRef.child(refKey).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        myRef.child(refKey).child("color").setValue(module.getColor());
                        myRef.child(refKey).child("moduleName").setValue(module.getModuleName());
                        myRef.child(refKey).child("targetGrade").setValue(module.getTargetGrade());
                        myRef.child(refKey).child("targetHoursPerWeek").setValue(module.getTargetHoursPerWeek());

                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
//
//                moduleMap.put(refKey, module);
//
//                Log.d("TAG", moduleMap.toString());
//
//                myRef.updateChildren(moduleMap).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (!task.isSuccessful()) {
//                            Log.d("TAG", task.toString());
//                            return;
//                        }
//                        Toast.makeText(ModuleUpdateActivity.this,"Module updated", Toast.LENGTH_SHORT).show();
//
//                        finish();
//                    }
//                });
            }

        });

    }

    private void getModuleRefKey(String moduleName) {
        Query q = myRef.orderByChild("moduleName").equalTo(moduleName);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot module : snapshot.getChildren()) {
                    Log.d("TAGss", module.getKey());

                    refKey = module.getKey();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private boolean isInputValid(String name, String targetHours, String goal) {
        if (name.isEmpty()) {
            editTextModuleName.setError("Name is required");
            editTextModuleName.requestFocus();
            return false;
        }

        if (targetHours.isEmpty()) {
            ModuleUpdateActivity.this.editTextTargetHours.setError("set the target hours");
            ModuleUpdateActivity.this.editTextTargetHours.requestFocus();
            return false;
        }
        if (goal.isEmpty()) {
            editTextGoal.setError("set a goal");
            editTextGoal.requestFocus();
            return false;
        }
        if(Integer.parseInt(targetHours) > 168){
            ModuleUpdateActivity.this.editTextTargetHours.setError("a week only has 168 hours");
            ModuleUpdateActivity.this.editTextTargetHours.requestFocus();
            return false;
        }
        if(Integer.parseInt(targetHours) < 0 || Integer.parseInt(targetHours) == 0){
            ModuleUpdateActivity.this.editTextTargetHours.setError("value cant be 0 or less");
            ModuleUpdateActivity.this.editTextTargetHours.requestFocus();
            return false;
        }

        if (moduleNameList.contains(name)) {
            editTextModuleName.setError("Module already exists");
            editTextModuleName.requestFocus();
            return false;
        }
        return true;
    }

    private void getModuleNames(DatabaseReference myRef, String currentModuleName) {
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String, Object> moduleMap = (Map<String, Object>) snapshot.getValue();

                if (moduleMap == null) {
                    return;
                }

                for (Map.Entry<String, Object> entry : moduleMap.entrySet()) {
                    //Get user map
                    Map singleUser = (Map) entry.getValue();
                    String name = (String) singleUser.get("moduleName");

                    if (name.equals(currentModuleName)) {
                        continue;
                    }

                    moduleNameList.add(name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //#ff00ddff, #ff0099cc, #ff669900, #ffff8800,  #ff99cc00, #ffffbb33, #ffaa66cc, #ffff4444
    private void colourPicker() {
        colourSelector = findViewById(R.id.colourselector);
        colourSelector.setBackgroundTintList(ColorStateList.valueOf(selectedColour));

        colourBox = findViewById(R.id.colourbox);

        colourSelectorToColour.put(findViewById(R.id.lightBlue), colours[0]);
        colourSelectorToColour.put(findViewById(R.id.blue), colours[1]);
        colourSelectorToColour.put(findViewById(R.id.darkGreen), colours[2]);
        colourSelectorToColour.put(findViewById(R.id.orange), colours[3]);
        colourSelectorToColour.put(findViewById(R.id.lightGreen), colours[4]);
        colourSelectorToColour.put(findViewById(R.id.yellow), colours[5]);
        colourSelectorToColour.put(findViewById(R.id.purple), colours[6]);
        colourSelectorToColour.put(findViewById(R.id.red), colours[7]);

        for (int i = 0; i < colourSelectorToColour.keySet().size(); i++) {
            ImageView colourSelector = (ImageView) colourSelectorToColour.keySet().toArray()[i];
            colourSelector.setBackgroundTintList(ColorStateList.valueOf(colours[i]));
        }

        //#ff00ddff, #ff0099cc, #ff669900, #ffff8800,  #ff99cc00, #ffffbb33, #ffaa66cc, #ffff4444
        lightBlueTick = findViewById(R.id.lightBlueTick);
        blueTick = findViewById(R.id.blueTick);
        darkGreenTick = findViewById(R.id.darkGreenTick);
        orangeTick = findViewById(R.id.orangeTick);
        lightGreenTick = findViewById(R.id.lightGreenTick);
        yellowTick = findViewById(R.id.yellowTick);
        purpleTick = findViewById(R.id.purpleTick);
        redTick = findViewById(R.id.redTick);

        colourToView.put(colours[0], lightBlueTick);
        colourToView.put(colours[1], blueTick);
        colourToView.put(colours[2], darkGreenTick);
        colourToView.put(colours[3], orangeTick);
        colourToView.put(colours[4], lightGreenTick);
        colourToView.put(colours[5], yellowTick);
        colourToView.put(colours[6], purpleTick);
        colourToView.put(colours[7], redTick);

        background = findViewById(R.id.background);
        close = findViewById(R.id.close);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colourBox.setVisibility(v.VISIBLE);

                for (ImageView colourSelector : colourSelectorToColour.keySet()) {
                    colourSelector.setVisibility(v.VISIBLE);
                }

                close.setVisibility(v.VISIBLE);

                Log.d("TAG", selectedColour.toString());
                Log.d("TAG", colourToView.toString());

                View colourTick = colourToView.get(selectedColour);
                colourTick.setVisibility(View.VISIBLE);
            }
        };

        View.OnClickListener hideColorSelectListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeColourPicker(v);
            }
        };
        background.setOnClickListener(hideColorSelectListener);
        close.setOnClickListener(hideColorSelectListener);

        colourSelector.setOnClickListener(listener);

        View.OnClickListener changeTick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideAllTick(view);
                int colourImageViewId = view.getId();
                Log.d("TAG", Integer.toString(colourImageViewId));

                switch (colourImageViewId){
                    case R.id.lightBlue:
                        lightBlueTick.setVisibility(View.VISIBLE);
                        selectedColour = colours[0];
                        colourSelector.setBackgroundTintList(ColorStateList.valueOf(colours[0]));
                        break;
                    case R.id.blue:
                        blueTick.setVisibility(View.VISIBLE);
                        selectedColour = colours[1];
                        colourSelector.setBackgroundTintList(ColorStateList.valueOf(colours[1]));
                        break;
                    case R.id.darkGreen:
                        darkGreenTick.setVisibility(View.VISIBLE);
                        selectedColour = colours[2];
                        colourSelector.setBackgroundTintList(ColorStateList.valueOf(colours[2]));
                        break;
                    case R.id.orange:
                        orangeTick.setVisibility(View.VISIBLE);
                        selectedColour = colours[3];
                        colourSelector.setBackgroundTintList(ColorStateList.valueOf(colours[3]));
                        break;
                    case R.id.lightGreen:
                        lightGreenTick.setVisibility(View.VISIBLE);
                        selectedColour = colours[4];
                        colourSelector.setBackgroundTintList(ColorStateList.valueOf(colours[4]));
                        break;
                    case R.id.yellow:
                        yellowTick.setVisibility(View.VISIBLE);
                        selectedColour = colours[5];
                        colourSelector.setBackgroundTintList(ColorStateList.valueOf(colours[5]));
                        break;
                    case R.id.purple:
                        purpleTick.setVisibility(View.VISIBLE);
                        selectedColour = colours[6];
                        colourSelector.setBackgroundTintList(ColorStateList.valueOf(colours[6]));
                        break;
                    case R.id.red:
                        redTick.setVisibility(View.VISIBLE);
                        selectedColour = colours[7];
                        colourSelector.setBackgroundTintList(ColorStateList.valueOf(colours[7]));
                        break;
                }
                closeColourPicker(view);
            }
        };

        for (ImageView colourSelector : colourSelectorToColour.keySet()) {
            colourSelector.setOnClickListener(changeTick);
        }
    }

    private void closeColourPicker(View view) {
        colourBox.setVisibility(view.INVISIBLE);

        for (ImageView colourSelector : colourSelectorToColour.keySet()) {
            colourSelector.setVisibility(View.INVISIBLE);
        }

        hideAllTick(view);

        close.setVisibility(view.INVISIBLE);
    }

    private void hideAllTick(View v) {
        lightBlueTick.setVisibility(v.INVISIBLE);
        blueTick.setVisibility(v.INVISIBLE);
        darkGreenTick.setVisibility(v.INVISIBLE);
        orangeTick.setVisibility(v.INVISIBLE);
        lightGreenTick.setVisibility(v.INVISIBLE);
        yellowTick.setVisibility(v.INVISIBLE);
        purpleTick.setVisibility(v.INVISIBLE);
        redTick.setVisibility(v.INVISIBLE);
    }
}
