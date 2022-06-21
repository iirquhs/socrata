package sg.edu.np.mad.socrata;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeworkFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        @ColorInt int webColor = ContextCompat.getColor(container.getContext(), R.color.secondary_color);
//        @ColorInt int ooadColor = ContextCompat.getColor(container.getContext(), R.color.text_color);

//        Module web = new Module("Web Applications Development", "AD", 30, webColor);
//        Module ooad = new Module("Object-Oriented Analysis and Design", "AD", 5, ooadColor);
//
//        Homework homework1 = new Homework("help", LocalDateTime.now().plusHours(5), web);
//        Homework homework2 = new Homework("help1", LocalDateTime.now().plusDays(5), ooad);
//
//        data.add(homework1);
//        data.add(homework2);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_homework, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.homework_rcv);

        LinearLayoutManager layout = new LinearLayoutManager(view.getContext());

        recyclerView.setLayoutManager(layout);
        recyclerView.setNestedScrollingEnabled(false);

        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("Users").child(currentUser);

        DatabaseReference moduleReference = userReference.child("modules");
        moduleReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String, Module> moduleMap = new HashMap<>();

                for (DataSnapshot moduleEntry : snapshot.getChildren()) {
                    String moduleRef = moduleEntry.getKey();

                    int targetHoursPerWeek = moduleEntry.child("targetHoursPerWeek").getValue(int.class);
                    @ColorInt int color = moduleEntry.child("color").getValue(int.class);
                    String moduleName = moduleEntry.child("moduleName").getValue(String.class);
                    String targetGrade = moduleEntry.child("targetGrade").getValue(String.class);

                    Module module = new Module(moduleName, targetGrade, targetHoursPerWeek, color);

                    moduleMap.put(moduleRef, module);
                }


                DatabaseReference homeworkReference = userReference.child("homework");
                homeworkReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList<Homework> homeworkArrayList = new ArrayList<>();

                        for (DataSnapshot homeworkSnapshot : snapshot.getChildren()) {
                            Homework homework = homeworkSnapshot.getValue(Homework.class);
                            //LocalDateTime dueDateTime = LocalDateTime.of()
                            //Log.d("TAG", dueDateTime.toString());
                            homeworkArrayList.add(homework);
                        }

                        HomeworkAdapter adapter = new HomeworkAdapter(homeworkArrayList, moduleMap);

                        recyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FloatingActionButton addHomework = view.findViewById(R.id.createhomework);
        addHomework.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent activityName = new Intent(view.getContext(), HomeworkCreateActivity.class);
                startActivity(activityName);
            }
        });

//        String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();

//        DatabaseReference check = FirebaseDatabase.getInstance().getReference("Users").child(currentuser).child("modules");
//        check.addListenerForSingleValueEvent( new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                //ArrayList<String> nameList = ModuleUtils.getModuleNames((Map<String, Object>) dataSnapshot.getValue());
//                //addHomework(nameList);
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        return view;
    }

//    public void addHomework(ArrayList<String> nameList){
//        FloatingActionButton addHomework = requireView().findViewById(R.id.createhomework);
//        View.OnClickListener listener = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent activityName = new Intent(HomeworkFragment.this.getActivity() ,HomeworkCreate.class);
//                startActivity(activityName);
//                if(nameList.isEmpty() == true){
//                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//                    builder.setMessage("Please create a module in the module page before making a homework");
//                    builder.setCancelable(true);
//                    builder.setPositiveButton(
//                            "CREATE MODULE", new
//                                    DialogInterface.OnClickListener(){
//                                        public void onClick(DialogInterface dialog, int id){
//                                            Intent activityName = new Intent(HomeworkFragment.this.getActivity(),ModuleCreate.class);
//                                            startActivity(activityName);
//
//                                        }
//                                    });
//                    builder.setNegativeButton("CLOSE", new
//                            DialogInterface.OnClickListener(){
//                                public void onClick(DialogInterface dialog, int id){
//                                    dialog.cancel();
//
//                                }
//                            });
//                    AlertDialog alert11 = builder.create();
//                    alert11.show();
//                }
//                else{
//                    Intent intent = new Intent(HomeworkFragment.this.getActivity() ,HomeworkCreate.class);
//                    startActivity(intent);
//                }
//            }
//        };
//        addHomework.setOnClickListener(listener);
//    }
}