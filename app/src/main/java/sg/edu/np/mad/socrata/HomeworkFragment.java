package sg.edu.np.mad.socrata;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeworkFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeworkFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeworkFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeworkFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeworkFragment newInstance(String param1, String param2) {
        HomeworkFragment fragment = new HomeworkFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_homework, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArrayList<Homework> data = new ArrayList<>();

        //@ColorInt int webColor = ContextCompat.getColor(container.getContext(), R.color.secondary_color);
        //@ColorInt int ooadColor = ContextCompat.getColor(container.getContext(), R.color.text_color);

        /*Module web = new Module("Web Applications Development", "AD", 30, getResources().getColor(R.color.black));
        Module ooad = new Module("Object-Oriented Analysis and Design", "AD", 5, getResources().getColor(R.color.black));
        Homework homework1 = new Homework("assignment gg", web);
        Homework homework2 = new Homework("assignenwer", ooad);
        data.add(homework1);
        data.add(homework2);*/
        RecyclerView rv = view.findViewById(R.id.homework_rcv);
        HomeworkAdapter adapter = new HomeworkAdapter(data);
        LinearLayoutManager layout = new LinearLayoutManager(view.getContext());
        rv.setAdapter(adapter);
        rv.setLayoutManager(layout);
        rv.setNestedScrollingEnabled(false);
        String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference check = FirebaseDatabase.getInstance().getReference("Users").child(currentuser).child("modules");
        check.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<String> nameList = ModuleUtils.getModuleNames((Map<String, Object>) dataSnapshot.getValue());
                        addHomework(nameList);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    public void addHomework(ArrayList<String> nameList){
        FloatingActionButton addHomework = requireView().findViewById(R.id.createhomework);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityName = new Intent(HomeworkFragment.this.getActivity() ,HomeworkCreate.class);
                startActivity(activityName);
                if(nameList.isEmpty() == true){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Please create a module in the module page before making a homework");
                    builder.setCancelable(true);
                    builder.setPositiveButton(
                            "CREATE MODULE", new
                                    DialogInterface.OnClickListener(){
                                        public void onClick(DialogInterface dialog, int id){
                                            Intent activityName = new Intent(HomeworkFragment.this.getActivity(),ModuleCreate.class);
                                            startActivity(activityName);

                                        }
                                    });
                    builder.setNegativeButton("CLOSE", new
                            DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialog, int id){
                                    dialog.cancel();

                                }
                            });
                    AlertDialog alert11 = builder.create();
                    alert11.show();
                }
                else{
                    Intent intent = new Intent(HomeworkFragment.this.getActivity() ,HomeworkCreate.class);
                    startActivity(intent);
                }
            }
        };
        addHomework.setOnClickListener(listener);
    }


}