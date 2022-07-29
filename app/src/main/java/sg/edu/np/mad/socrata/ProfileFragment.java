package sg.edu.np.mad.socrata;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

public class ProfileFragment extends Fragment {

    TextView profileUsername, accountName, profileEmail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView editProfileBtn = getView().findViewById(R.id.editProfileBtn);
        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ProfileEditActivity.class);
                startActivity(intent);
            }
        });

        // SIGN OUT FROM ACCOUNT
        TextView signOutButton = getView().findViewById(R.id.signOutBtn);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getActivity(), "Logging out...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        profileUsername = getView().findViewById(R.id.profileUsername);
        accountName = getView().findViewById(R.id.accountName);

        profileEmail = getView().findViewById(R.id.profileEmail);

//        // DELETE ACCOUNT FROM DATABASE
//        TextView deleteAccBtn = getView().findViewById(R.id.deleteAccBtn);
//        deleteAccBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                builder
//                        .setTitle("PLEASE CONFIRM")
//                        .setMessage("Are you sure you want to delete your account?")
//                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//
//                            }
//                        })
//                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                ProgressDialog dialog = ProgressDialog.show(getActivity(), "Deleting your account", "Loading. Please wait...", true);
//                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                                user.delete()
//                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<Void> task) {
//                                                if (task.isSuccessful()) {
//                                                    Intent intent = new Intent(getActivity(), LoginActivity.class);
//                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                                    dialog.dismiss();
//                                                    startActivity(intent);
//                                                }
//                                            }
//                                        });
//                            }
//                        })
//                        .show();
//            }
//        });


    }

    @Override
    public void onResume() {
        super.onResume();
        LocalStorage localStorage = new LocalStorage(requireActivity());

        User user = localStorage.getUser();

        String username = user.getUsername();
        profileUsername.setText(username);
        accountName.setText(username);

        String email = user.getEmail();
        profileEmail.setText(email);
    }
}