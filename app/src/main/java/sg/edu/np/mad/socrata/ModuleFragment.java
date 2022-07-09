package sg.edu.np.mad.socrata;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ModuleFragment extends Fragment {

    LocalStorage localStorage;

    RecyclerView recyclerView;

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

        recyclerView = view.findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        localStorage = new LocalStorage(requireActivity());

        setAddModuleButton();
    }

    @Override
    public void onResume() {
        super.onResume();

        User user = localStorage.getUser();
        updateModuleRecyclerView(requireView(), user);
    }

    /**
     * Retrieve modules from firebase and update the recycler view
     * @param view
     */
    private void updateModuleRecyclerView(@NonNull View view, User user) {

        ArrayList<Module> moduleArrayList = user.getModuleArrayList();

        if (moduleArrayList == null || moduleArrayList.size() <= 0) {
            Toast.makeText(getContext(), "You have no module created", Toast.LENGTH_SHORT).show();
            return;
        }

        ModuleAdapter moduleAdapter = new ModuleAdapter(moduleArrayList);

        recyclerView.setAdapter(moduleAdapter);

    }

    public void setAddModuleButton() {
        FloatingActionButton addModule = (FloatingActionButton) getView().findViewById(R.id.addmodule);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityName = new Intent(ModuleFragment.this.getActivity(), ModuleUpdateActivity.class);
                startActivity(activityName);
            }
        };
        addModule.setOnClickListener(listener);
    }
}