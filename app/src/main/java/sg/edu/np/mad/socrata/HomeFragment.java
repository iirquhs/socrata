package sg.edu.np.mad.socrata;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;

public class HomeFragment extends Fragment {

    TextView textViewUsername;

    String username;

    DatabaseReference userReference;

    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        textViewUsername = view.findViewById(R.id.username);

        recyclerView = view.findViewById(R.id.recyclerView);

        LinearLayoutManager layout = new LinearLayoutManager(view.getContext());

        recyclerView.setLayoutManager(layout);
        recyclerView.setNestedScrollingEnabled(false);

        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        userReference = FirebaseDatabase.getInstance().getReference("Users").child(currentUser);

        setHomeworkAdapter();

        DatabaseReference usernameReference = userReference.child("username");

        usernameReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                username = dataSnapshot.getValue(String.class);
                textViewUsername.setText(username + "!" + " \uD83D\uDC4B");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("tah", "onCancelled", databaseError.toException());
            }
        });



    }

    private void setHomeworkAdapter() {
        DatabaseReference moduleReference = userReference.child("modules");
        moduleReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Map<String, Module> moduleMap = ModuleUtils.parseModuleMap((Map<String, Object>) snapshot.getValue());

                    Log.d("TAG", moduleMap.toString());

                    DatabaseReference homeworkReference = userReference.child("homework");
                    homeworkReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ArrayList<Homework> urgentHomeworkArrayList = new ArrayList<>();

                            for (DataSnapshot homeworkSnapshot : snapshot.getChildren()) {
                                Homework homework = homeworkSnapshot.getValue(Homework.class);

                                assert homework != null;
                                LocalDateTime homeworkDueDate = homework.ConvertDueDateTime(homework.getDueDateTimeString());

                                long secondsLeft = homework.CalculateSecondsLeftBeforeDueDate(homeworkDueDate);

                                if (secondsLeft / 3600.0 < 24 && homework.getStatus().equals("In Progress")) {
                                    urgentHomeworkArrayList.add(homework);
                                }
                            }

                            HomeworkAdapter homeworkAdapter = new HomeworkAdapter(urgentHomeworkArrayList, moduleMap);

                            recyclerView.setAdapter(homeworkAdapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}