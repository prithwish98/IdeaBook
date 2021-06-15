package in.kgec.ideabook.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import in.kgec.ideabook.R;

public class HomeFragment extends Fragment {

    RecyclerView recyclerFeed;
    PostAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerFeed=(RecyclerView) root.findViewById(R.id.recycler_feed);
        recyclerFeed.setLayoutManager(new LinearLayoutManager(getContext()));

        FirestoreRecyclerOptions<Post> options = new FirestoreRecyclerOptions.Builder<Post>()
                .setQuery(FirebaseFirestore.getInstance().collection("Posts").orderBy("timeOfPost", Query.Direction.DESCENDING), Post.class)
                .build();
        adapter=new PostAdapter(options);
        recyclerFeed.setAdapter(adapter);


        FloatingActionButton fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivityForResult(new Intent(getContext(), AddPost.class),100);
//                Snackbar.make(view, "YO", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });


        return root;
    }


    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}