package in.kgec.ideabook;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import in.kgec.ideabook.ui.home.AddPost;

public class NavigationDrawerActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private FirebaseAuth auth;
    private FirebaseFirestore fStore;
    private DocumentReference documentReference;
    private Button logOut;
    private ImageView displayPicture;
    private TextView displayName,displayEmail;

    private String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);







        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_profile, R.id.nav_mypost, R.id.nav_settings)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

//        displayPicture = findViewById(R.id.displayPicture); //null
//        Log.i("NAVDRAWER", "onCreate: "+displayPicture);
//        displayName = findViewById(R.id.displayName);
//        displayEmail = findViewById(R.id.displayEmail);

        auth = FirebaseAuth.getInstance();
        userId = auth.getCurrentUser().getUid();
        fStore = FirebaseFirestore.getInstance();
        documentReference = fStore.collection("user_profile").document(userId);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                displayPicture = findViewById(R.id.displayPicture); //null
                Log.i("ONEVENT", "onCreate: "+displayPicture);
                displayName = findViewById(R.id.displayName);
                displayEmail = findViewById(R.id.displayEmail);
                if(error != null)
                {
                    return;
                }

                String link = value.getString("Image");
                Picasso.get().load(link).into(displayPicture);
                displayName.setText(value.getString("FullName"));
                displayEmail.setText(value.getString("Email"));
            }
        });

        logOut = findViewById(R.id.logout);
        logOut.setOnClickListener(v -> {
            auth.signOut();
            startActivity(new Intent(this,LoginActivity.class));
            finish();
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}