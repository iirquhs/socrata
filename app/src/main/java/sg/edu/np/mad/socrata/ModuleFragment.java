package sg.edu.np.mad.socrata;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class ModuleFragment extends Fragment{

    String currentUser;
    DatabaseReference moduleReference;

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

        currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        moduleReference = FirebaseDatabase.getInstance().getReference("Users").child(currentUser).child("modules");

        setAddModuleButton();

        updateModuleRecyclerView(view);


    }

    @Override
    public void onResume() {
        super.onResume();

        updateModuleRecyclerView(requireView());

    }

    private void updateModuleRecyclerView(@NonNull View view) {
        moduleReference.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Get map of users in datasnapshot
                        Map<String, Module> moduleMap = ModuleUtils.parseModuleMap((Map<String, Object>) dataSnapshot.getValue());

                        if (moduleMap == null) {
                            return;
                        }

                        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);

                        ModuleAdapter moduleAdapter = new ModuleAdapter(moduleMap);
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

    public void setAddModuleButton(){
        FloatingActionButton addModule = (FloatingActionButton) getView().findViewById(R.id.addmodule);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityName = new Intent(ModuleFragment.this.getActivity() , ModuleUpdateActivity.class);
                startActivity(activityName);
            }
        };
        addModule.setOnClickListener(listener);
    }
}