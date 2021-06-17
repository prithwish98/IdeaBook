package in.kgec.ideabook.ui.home;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;

import de.hdodenhof.circleimageview.CircleImageView;
import in.kgec.ideabook.R;

public class PostAdapter extends FirestoreRecyclerAdapter<Post, PostAdapter.myViewHolder> {
    SimpleDateFormat sdf=new SimpleDateFormat("MMM dd yyyy hh:mm:ss a");


    public PostAdapter(@NonNull FirestoreRecyclerOptions<Post> options) {
        super(options);
    }


    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull Post post) {
        String formatted_date =sdf.format(post.getTimeOfPost());
        String customised_date=formatted_date.substring(0,11)+" at "+formatted_date.substring(12,17)+formatted_date.substring(20);
        holder.tv_postTime.setText(customised_date);
        holder.tv_usrName.setText(post.getUserName());
        holder.tv_NoOfComments.setText(Integer.toString(post.getNoOfComments())+" Comments");
        holder.tv_noOfLikes.setText(Integer.toString(post.getLikersList().size()));
        holder.tv_status.setText(post.getStatus());
        Glide.with(holder.img_proPic.getContext()).load(post.getImg_UserDp_url()).into(holder.img_proPic);
        Log.i("DOCUMENT", "ID: "+post.getDocumentId());

        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        final String userid=firebaseUser.getUid();
        final  String postid=post.getDocumentId();
//        holder.getLikeBtnStatus(postid,userid);
        if (post.likersList.contains(userid)){
            holder.like_btn.setIcon(holder.like_btn.getContext().getDrawable(R.drawable.ic_baseline_favorite_24));
            holder.like_btn.setTextColor(Color.parseColor("#195DFF"));
            holder.like_btn.setIconTintResource(R.color.like_blue);
        }
        else {
            holder.like_btn.setIcon(holder.like_btn.getContext().getDrawable(R.drawable.ic_baseline_favorite_border_24));
            holder.like_btn.setTextColor(Color.parseColor("#000000"));
            holder.like_btn.setIconTintResource(R.color.black);
        }
        holder.like_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentReference currentPost=FirebaseFirestore.getInstance().collection("Posts").document(postid);

                if (post.likersList.contains(userid)){
//                post.likersList.remove(userid);
                    Log.i("BUTTON", "onClick: "+postid+"->"+userid);
                    currentPost.update("likersList", FieldValue.arrayRemove(userid));
                }
                else {
                    Log.i("BUTTON", "onClick: "+postid+"->"+userid);
//                post.likersList.add(userid);
                    currentPost.update("likersList", FieldValue.arrayUnion(userid));

                }

            }
        });
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_post,parent,false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{

        CircleImageView img_proPic;
        TextView tv_usrName,tv_postTime,tv_status,tv_noOfLikes,tv_NoOfComments;
        MaterialButton like_btn, comment_btn;
        DocumentReference docref;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            img_proPic=(CircleImageView) itemView.findViewById(R.id.imgView_proPic);
            tv_usrName=(TextView) itemView.findViewById(R.id.tv_userName);
            tv_postTime=(TextView) itemView.findViewById(R.id.tv_postTime);
            tv_status=(TextView) itemView.findViewById(R.id.tv_status);
            tv_noOfLikes=(TextView) itemView.findViewById(R.id.tv_noOfLikes);
            tv_NoOfComments=(TextView) itemView.findViewById(R.id.tv_NoOfComments);

            like_btn=(MaterialButton) itemView.findViewById(R.id.btn_like);
            comment_btn=(MaterialButton) itemView.findViewById(R.id.btn_comment);
        }


        public void getLikeBtnStatus(final String postid, final String userid){
            docref =FirebaseFirestore.getInstance().collection("Posts/"+postid+"/Likes").document(userid);
            docref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot document) {

                    like_btn.setIcon(like_btn.getContext().getDrawable(R.drawable.ic_baseline_favorite_24));
                    like_btn.setTextColor(Color.parseColor("#195DFF"));
                    like_btn.setIconTintResource(R.color.like_blue);
                    int likeCount =document.getData().size();
                    tv_noOfLikes.setText(Integer.toString(likeCount));

                }
            });


        }

//       public void getlikes(){
//            like_btn.setIcon(like_btn.getContext().getDrawable(R.drawable.ic_baseline_favorite_24));
//            like_btn.setIconTint(like_btn.getb);
//            like_btn.setTextColor();
//       }
    }
}
