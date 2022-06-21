package sg.edu.np.mad.socrata;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

import org.w3c.dom.Text;

public class HomeFragment extends Fragment {

    TextView textViewUsername;

    String username;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        textViewUsername = view.findViewById(R.id.username);
        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference getusername = FirebaseDatabase.getInstance().getReference("Users").child(currentUser);
        DatabaseReference usernameref = getusername.child("username");

        usernameref.addValueEventListener(new ValueEventListener() {
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

}