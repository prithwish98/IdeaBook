package in.kgec.ideabook;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private EditText mEmail,mPassword;
    private Button btnSignIn;
    private TextView gotoSignUp,forgotPassword;
    private ProgressDialog progressDialog;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        mEmail = findViewById(R.id.email_SignIn);
        mPassword = findViewById(R.id.password_SignIn);
        btnSignIn = findViewById(R.id.btnSignIn);
        gotoSignUp = findViewById(R.id.gotoSignUp);
        forgotPassword = findViewById(R.id.forgotPass);

        auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Signing in!");

        btnSignIn.setOnClickListener(v -> {
            String email = mEmail.getText().toString().trim();
            String password = mPassword.getText().toString();

            if(TextUtils.isEmpty(email)){
                mEmail.setError("Enter your email");
                return;
            }
            if(TextUtils.isEmpty(password)) {
                mPassword.setError("Enter your password");
                return;
            }
            progressDialog.show();
            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(task -> {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                AlertDialog alertDialog;

                progressDialog.dismiss();
                if(task.isSuccessful()){
                    if(auth.getCurrentUser().isEmailVerified()){
                        startActivity(new Intent(this,NavigationDrawerActivity.class));
                        finish();
                    }
                    else {
                        builder.setMessage("Please verify your email").setPositiveButton("OK",null);
                        alertDialog = builder.create();
                        alertDialog.show();
                    }

                }
                else{
                    builder.setMessage(task.getException().getMessage()).setPositiveButton("OK",null);
                    alertDialog = builder.create();
                    alertDialog.show();
                }
            });
        });
        forgotPassword.setOnClickListener(v -> {
            startActivity(new Intent(this,ForgotPassword.class));
        });
        gotoSignUp.setOnClickListener(v -> {
            startActivity(new Intent(this,SignupActivity.class));
            finish();
        });
        if(auth.getCurrentUser() != null && auth.getCurrentUser().isEmailVerified()){
            startActivity(new Intent(this,NavigationDrawerActivity.class));
            finish();
        }
    }
}