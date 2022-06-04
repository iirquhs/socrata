package sg.edu.np.mad.socrata;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class ModuleFragment extends Fragment {

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

        ArrayList<Module> moduleArrayList = new ArrayList<>();

        Module dummyModule = new Module("MAD", "A", 30, R.color.black, getContext());
        for (int i = 0; i < 10; i++) {
            moduleArrayList.add(dummyModule);
        }

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        ModuleAdapter moduleAdapter = new ModuleAdapter(moduleArrayList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(moduleAdapter);
        recyclerView.setNestedScrollingEnabled(false);


    }
}