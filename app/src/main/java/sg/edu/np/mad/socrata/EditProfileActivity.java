package sg.edu.np.mad.socrata;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class EditProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(uid);

        // GET USERNAME STRING
        TextView profileUsername = findViewById(R.id.editProfileUsername);
        DatabaseReference usernameRef = userRef.child("username");
        readDataToTextView(usernameRef, profileUsername);

        // GET EMAIL STRING
        TextView profileEmail = findViewById(R.id.editProfileEmail);
        DatabaseReference emailRef = userRef.child("email");
        readDataToTextView(emailRef, profileEmail);

        // IF SAVE BUTTON IS CLICKED
        Button profileSaveBtn = findViewById(R.id.profileSaveBtn);
        profileSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog dialog = ProgressDialog.show(EditProfileActivity.this,
                        "Updating your profile", "Loading. Please wait...", true);

                String newUsername = profileUsername.getText().toString();
                String newEmailAddr = profileEmail.getText().toString();

                usernameRef.setValue(newUsername);
                emailRef.setValue(newEmailAddr);

                dialog.dismiss();
                finish();
            }
        });

        // IF EXIT BUTTON IS CLICKED
        TextView profileExitBtn = findViewById(R.id.ProfileExitBtn);
        profileExitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // IF CHANGE PASSWORD BUTTON IS CLICKED
        TextView changePwdBtn = findViewById(R.id.changePwdBtn);
        changePwdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditProfileActivity.this, UpdatePasswordActivity.class);
                startActivity(intent);
            }
        });

        // IF DELETE ACCOUNT BUTTON IS CLICKED
        TextView deleteAccBtn = findViewById(R.id.deleteAccBtn);
        deleteAccBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
                builder
                        .setTitle("Confirm your selection")
                        .setMessage("Are you sure you want to delete your account? Your data will be permanently lost.")
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ProgressDialog dialog = ProgressDialog.show(EditProfileActivity.this,
                                        "Deleting your account", "Loading. Please wait...", true);
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                user.delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Intent intent = new Intent(EditProfileActivity.this, LoginActivity.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    dialog.dismiss();
                                                    startActivity(intent);
                                                }
                                            }
                                        });
                            }
                        });
            }
        });
    }

    public void readDataToTextView(DatabaseReference dataRef, TextView textView) {

        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                textView.setText(value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, textView.toString() + ":onCancelled", error.toException());
            }
        });
    }


}