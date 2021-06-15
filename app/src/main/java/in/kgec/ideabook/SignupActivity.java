package in.kgec.ideabook;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class SignupActivity extends AppCompatActivity {
    private EditText mName, mEmail, mPassword, mConfirmPassword;
    private Button btnSignUp;
    private TextView haveAcc;
    private ProgressDialog progressDialog;
    private String userId;

    private FirebaseFirestore fStore;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().hide();

        mName = findViewById(R.id.fullName);
        mEmail = findViewById(R.id.email_SignUp);
        mPassword = findViewById(R.id.password_SignUp);
        mConfirmPassword = findViewById(R.id.confirmPass_SignUp);
        btnSignUp = findViewById(R.id.btnSignUp);
        haveAcc = findViewById(R.id.alreadyHaveAccount);


        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("We are creating your account!");

        fStore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        btnSignUp.setOnClickListener(v -> {
            String fullName = mName.getText().toString().trim();
            String email = mEmail.getText().toString().trim();
            String password = mPassword.getText().toString();
            String confirmPassword = mConfirmPassword.getText().toString();

            if (TextUtils.isEmpty(fullName)) {
                mName.setError("This is required");
                return;
            }
            if (TextUtils.isEmpty(email)) {
                mEmail.setError("This is required");
                return;
            }
            if (TextUtils.isEmpty(password)) {
                mPassword.setError("This is required");
                return;
            }
            if (password.length() < 8 || password.length() > 14) {
                mPassword.setError("Password must have 8 to 14 characters");
                return;
            }
            if (TextUtils.isEmpty(confirmPassword)) {
                mConfirmPassword.setError("This is required");
                return;
            }

            if (password.equals(confirmPassword)) {
                progressDialog.show();
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(task1 -> {

                            AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                            AlertDialog alertDialog;

                            if (task.isSuccessful()) {

                                userId = auth.getCurrentUser().getUid();
                                DocumentReference documentReference = fStore.collection("user_profile").document(userId);
                                Map<String, Object> data = new HashMap<>();
                                data.put("FullName", fullName);
                                data.put("Email", email);
                                documentReference.set(data).addOnSuccessListener(aVoid -> {
                                });

                                builder.setMessage("User Created! Please check email to verify").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                                        finish();
                                    }
                                });
                                alertDialog = builder.create();
                                alertDialog.show();


                            } else {
                                builder.setMessage(task.getException().getMessage()).setPositiveButton("OK", null);
                                alertDialog = builder.create();
                                alertDialog.show();
                            }
                        });

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage(task.getException().getMessage()).setPositiveButton("OK", null);
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                });
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Password doesn't match").setPositiveButton("OK", null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }

        });
        haveAcc.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}