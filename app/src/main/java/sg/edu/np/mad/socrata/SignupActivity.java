package sg.edu.np.mad.socrata;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class SignupActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private EditText editTextUsername, editTextEmail, editTextPassword, editTextConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        TextView toLoginTextView = findViewById(R.id.toLoginTextView);
        toLoginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);

                startActivity(intent);
            }
        });

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);

        Button buttonSignUp = findViewById(R.id.buttonSignUp);
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = editTextUsername.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                String confirmPassword = editTextConfirmPassword.getText().toString().trim();

                if (!isInputValid(username, email, password, confirmPassword)) return;

                ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this);
                progressDialog.setTitle("Creating your account");
                progressDialog.setMessage("Please wait...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (!task.isSuccessful()) {
                                try {
                                    throw Objects.requireNonNull(task.getException());
                                } catch (FirebaseAuthUserCollisionException firebaseAuthUserCollisionException) {
                                    Toast.makeText(SignupActivity.this, "Email already exists", Toast.LENGTH_SHORT).show();
                                } catch (Exception exception) {
                                    Toast.makeText(SignupActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                                progressDialog.dismiss();
                                return;
                            }

                            User user = new User(username, email);

                            FirebaseFirestore db = FirebaseFirestore.getInstance();

                            // Create a new user account in firebase
                            db.collection("Users")
                                    .document(Objects.requireNonNull(task.getResult().getUser()).getUid())
                                    .set(user)
                                    .addOnSuccessListener(documentReference -> {
                                        Toast.makeText(SignupActivity.this, "Sign Up successful", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        progressDialog.dismiss();
                                        startActivity(intent);
                                    })
                                    .addOnFailureListener(e -> {
                                        progressDialog.dismiss();
                                        Toast.makeText(SignupActivity.this, "Failed to register. Please try again", Toast.LENGTH_SHORT).show();
                                    });
                        });
            }
        });
    }

    /**
     * Check whether the sign up fields are valid
     * @param username
     * @param email
     * @param password
     * @param confirmPassword
     * @return
     */
    private boolean isInputValid(String username, String email, String password, String confirmPassword) {
        if (username.isEmpty()) {
            editTextUsername.setError("Username is required!");
            editTextUsername.requestFocus();
            return false;
        }

        if (email.isEmpty()) {
            editTextEmail.setError("Email is required!");
            editTextEmail.requestFocus();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please provide a valid email");
            editTextEmail.requestFocus();
            return false;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password is required!");
            editTextPassword.requestFocus();
            return false;
        }

        if (password.length() < 6) {
            editTextPassword.setError("Min password length should be 6 characters!");
            editTextPassword.requestFocus();
            return false;
        }

        if (confirmPassword.isEmpty()) {
            editTextConfirmPassword.setError("Confirm Password is required!");
            editTextConfirmPassword.requestFocus();
            return false;
        }

        if (!password.equals(confirmPassword)) {
            editTextConfirmPassword.setError("Password does not match");
            editTextConfirmPassword.requestFocus();
            return false;
        }
        return true;
    }
}