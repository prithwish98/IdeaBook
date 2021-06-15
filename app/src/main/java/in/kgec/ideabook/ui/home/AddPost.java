package in.kgec.ideabook.ui.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import in.kgec.ideabook.R;

public class AddPost extends AppCompatActivity {
    EditText et_status;
    TextView username;
    Button btn_post;
    CircleImageView img_pro_pic;
    FirebaseFirestore dbroot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        et_status=(EditText) findViewById(R.id.et_status);
        btn_post=(Button) findViewById(R.id.save_button);
        username=(TextView) findViewById(R.id.tv_userName);
        img_pro_pic=(CircleImageView) findViewById(R.id.img_proPic);
        dbroot=FirebaseFirestore.getInstance();
        btn_post.setOnClickListener(view -> {
            insertdata();
        });
    }

    public void insertdata(){
        Map<String,Object> items=new HashMap<>();
        items.put("noOfLikes",0);
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