package sg.edu.np.mad.socrata;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class ModuleFragment extends Fragment{

    public ModuleFragment() {
        super(R.layout.fragment_module);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference Module = FirebaseDatabase.getInstance().getReference("Users").child(currentuser).child("modules");
        addmodule();
        Module.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Get map of users in datasnapshot
                        ArrayList<Module> moduleArrayList = getdata((Map<String,Object>) dataSnapshot.getValue(), (Map<Integer,Object>) dataSnapshot.getValue());
                        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
                        ModuleAdapter moduleAdapter = new ModuleAdapter(moduleArrayList);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(moduleAdapter);
                        recyclerView.setNestedScrollingEnabled(false);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });



    }


    public void addmodule(){
        FloatingActionButton addmodule = (FloatingActionButton) getView().findViewById(R.id.addmodule);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityName = new Intent(ModuleFragment.this.getActivity() ,ModuleCreate.class);
                startActivity(activityName);
            }
        };
        addmodule.setOnClickListener(listener);
    }

    private ArrayList<Module> getdata(Map<String,Object> users, Map<Integer,Object> users1) {
        ArrayList<Module> moduleArrayList = new ArrayList<>();
        ArrayList<String> namelist = new ArrayList<>();
        ArrayList<String> goallist = new ArrayList<>();
        ArrayList<Integer> hrlist = new ArrayList<>();
        ArrayList<Integer> colourlist = new ArrayList<>();
        int finalcolour = 17170459;
        //iterate through each user, ignoring their UID
        if(users != null){
            for (Map.Entry<String, Object> entry : users.entrySet()){
                //Get user map
                Map singleUser = (Map) entry.getValue();
                String name = (String) singleUser.get("moduleName");
                String goal = (String) singleUser.get("targetGrade");
                namelist.add(name);
                goallist.add(goal);

            }

        }

        if(users1 != null){
            for (Map.Entry<Integer, Object> entry : users1.entrySet()){
                //Get user map
                Map singleUser = (Map) entry.getValue();
                Long hours = (Long) singleUser.get("targetHoursPerWeek");
                Long colour = (Long) singleUser.get("color");
                hrlist.add(Integer.parseInt(hours.toString()));
                colourlist.add(Integer.parseInt(colour.toString()));
                Log.d("h", hrlist.toString());
                //16777215 - colourlist.get(i)
            }

        }
        for(int i=0; i < namelist.size(); i++){
            if(colourlist.get(i) == -16720385){finalcolour = 17170459;}
            else if(colourlist.get(i) == -16737844){finalcolour = 17170451;}
            else if(colourlist.get(i) == -10053376){finalcolour = 17170453;}
            else if(colourlist.get(i) == -30720){finalcolour = 17170457;}
            else if(colourlist.get(i) == -6697984){finalcolour = 17170452;}
            else if(colourlist.get(i) == -17613){finalcolour = 17170456;}
            else if(colourlist.get(i) == -5609780){finalcolour = 17170458;}
            else if(colourlist.get(i) == -48060){finalcolour = 17170454;}
            @SuppressLint("ResourceType") Module module = new Module(namelist.get(i), goallist.get(i), hrlist.get(i), finalcolour, getContext());
            moduleArrayList.add(module);
        }
        return moduleArrayList;
    }






}