package sg.edu.np.mad.socrata;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UpdatePasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        TextView cancelBtn = findViewById(R.id.updatePwdExitBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button changePwdBtn = findViewById(R.id.updatePwdConfirmBtn);
        changePwdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView currentPasswordField = findViewById(R.id.currentPasswordField);
                TextView newPasswordField = findViewById(R.id.newPasswordField);
                TextView confirmPasswordField = findViewById(R.id.confirmPasswordField);

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String userEmail = user.getEmail();

                String currentPassword = currentPasswordField.getText().toString().trim();
                String newPassword = newPasswordField.getText().toString().trim();
                String confirmPassword = confirmPasswordField.getText().toString().trim();

                if (currentPassword.isEmpty()) {
                    currentPasswordField.setError("Password is required");
                    currentPasswordField.requestFocus();
                    return;
                }

                if (newPassword.isEmpty()) {
                    newPasswordField.setError("Password is required");
                    newPasswordField.requestFocus();
                    return;
                }

                if (newPasswordField.length() < 6) {
                    newPasswordField.setError("Min password length should be 6 characters!");
                    newPasswordField.requestFocus();
                    return;
                }

                if (confirmPassword.isEmpty()) {
                    confirmPasswordField.setError("Password is required");
                    confirmPasswordField.requestFocus();
                    return;
                }

                if (!newPassword.equals(confirmPassword)) {
                    confirmPasswordField.setError("Passwords do not match.");
                    confirmPasswordField.requestFocus();
                    return;
                }

                ProgressDialog progressDialog = new ProgressDialog(UpdatePasswordActivity.this);
                progressDialog.setTitle("Changing password");
                progressDialog.setMessage("Please wait...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();


                AuthCredential credential = EmailAuthProvider.getCredential(userEmail, currentPassword);

                user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        // TODO: PASSWORD UPDATED
                                        Toast.makeText(UpdatePasswordActivity.this, "Password updated successfully.", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                        finish();
                                    } else {
                                        Toast.makeText(UpdatePasswordActivity.this, "Error occured. Please try again.", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });


    }
}