package sg.edu.np.mad.socrata;

import android.os.Bundle;

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

public class HomeFragment extends Fragment {

    String username;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        TextView usernamebox = getView().findViewById(R.id.username);
        String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference getusername = FirebaseDatabase.getInstance().getReference("Users").child(currentuser);
        DatabaseReference usernameref = getusername.child("username");
        usernameref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                username = dataSnapshot.getValue(String.class);
                usernamebox.setText(username + "!" + " \uD83D\uDC4B");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("tah", "onCancelled", databaseError.toException());
            }
        });



    }

}