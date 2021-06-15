package in.kgec.ideabook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {
    EditText emailForgotPass;
    Button btnSendPassResetEmail;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        getSupportActionBar().hide();
        
        auth = FirebaseAuth.getInstance();
        emailForgotPass = findViewById(R.id.emailForgotPass);
        btnSendPassResetEmail = findViewById(R.id.btnPassResetEmail);
        
        btnSendPassResetEmail.setOnClickListener(v -> {
            auth.sendPasswordResetEmail(emailForgotPass.getText().toString().trim()).addOnCompleteListener(task -> {
               if(task.isSuccessful()){
                   Toast.makeText(this, "Email sent, please check your email", Toast.LENGTH_SHORT).show();
                   startActivity(new Intent(this,LoginActivity.class));
                   finish();
               }
               else{
                   Toast.makeText(this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
               }
            });
        });
    }
}