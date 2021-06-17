package in.kgec.ideabook.ui.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import in.kgec.ideabook.R;

public class AddPost extends AppCompatActivity {
    EditText et_status;
    TextView username;
    Button btn_post;
    CircleImageView img_pro_pic;
    private FirebaseFirestore dbroot;
    String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        dbroot=FirebaseFirestore.getInstance();
        et_status=findViewById(R.id.et_status);
        btn_post=findViewById(R.id.save_button);
        username=findViewById(R.id.tv_userName);
        img_pro_pic=findViewById(R.id.img_proPic);
        getUserData();
        Log.i("USER", "onCreate: crossed");

        username.setTextColor(Color.parseColor("#000000"));
        btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertdata();
            }
        });
    }
    public void getUserData(){
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        final String userid=firebaseUser.getUid();
        dbroot.collection("user_profile").document(userid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot document) {
                user=document.get("FullName").toString();
                username.setText(user);
                Log.i("USER", "onSuccess: "+user);
            }
        });
    }
    public void insertdata(){
        Map<String,Object> items=new HashMap<>();
        items.put("likersList",new ArrayList<String>());
        items.put("noOfComments",0);
        items.put("userName",username.getText().toString().trim());
        items.put("status",et_status.getText().toString().trim());
        items.put("timeOfPost",new java.util.Date());
        items.put("img_UserDp_url","https://firebasestorage.googleapis.com/v0/b/recylerviewapp.appspot.com/o/default-user-avatar-300x293.png?alt=media&token=55c0887a-24a7-4ece-b7a7-4cc8b44043a6");

        dbroot.collection("Posts").add(items)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        setResult(2);
                        et_status.setText("");
                        finish();
                    }
                });
    }
}