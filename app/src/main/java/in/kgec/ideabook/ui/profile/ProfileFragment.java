package in.kgec.ideabook.ui.profile;

import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import in.kgec.ideabook.Profile;
import in.kgec.ideabook.R;
import in.kgec.ideabook.databinding.FragmentHomeBinding;
import in.kgec.ideabook.databinding.ProfileFragmentBinding;
import io.grpc.Compressor;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    private EditText etName, etMob, etAge, etWork, etAddress;
    private TextView tvUserame, tvEmail;
    private RadioGroup rgSex;
    private RadioButton rbMale, rbFemale;
    private ImageButton btnEdit;
    private Button btnSave;
    private TextView email;
    private CircleImageView profileImage, camera;


    //choose image
    private Uri imageUri;
    private Bitmap compressor;
    private ProgressDialog progressDialog;


    private ProfileViewModel mViewModel;
    private FirebaseFirestore fstore;
    private FirebaseAuth auth;
    private StorageReference storageReference;
    private DocumentReference documentReference;

    ProfileFragmentBinding binding;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        //==============
        binding = ProfileFragmentBinding.inflate(getLayoutInflater());
        return binding.getRoot();
        //===============

//        return inflater.inflate(R.layout.profile_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        // TODO: Use the ViewModel
    }

    //=============

    String userId;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("We are saving your data");
        
        storageReference = FirebaseStorage.getInstance().getReference().child("user_image");
        auth = FirebaseAuth.getInstance();
        userId = auth.getCurrentUser().getUid();
        fstore = FirebaseFirestore.getInstance();
        documentReference = fstore.collection("user_profile").document(userId);

        profileImage = binding.profileImage;
        camera = binding.camera;
        tvUserame = binding.tvUsername;
        tvEmail = binding.tvEmail;
        etName = binding.etName;
        etMob = binding.etMob;
        etAddress = binding.etAddress;
        etAge = binding.etAge;
        etWork = binding.etwork;
        rbMale = binding.rbMale;
        rbFemale = binding.rbFemale;
        rgSex = binding.rgSex;
        btnEdit = binding.btnEdit;
        btnSave = binding.btnSave;

        btnSave.setVisibility(view.INVISIBLE);
        profileImage.setEnabled(false);
        camera.setEnabled(false);

        documentReference.addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null)
                {
                    return;
                }

                String link = value.getString("Image");
                Picasso.get().load(link).into(profileImage);
                tvUserame.setText(value.getString("FullName"));
                tvEmail.setText(value.getString("Email"));
                etName.setText(value.getString("FullName"));
                etMob.setText(value.getString("Mobile"));
                etWork.setText(value.getString("Work"));
                etAddress.setText(value.getString("Address"));
                etAge.setText(value.getString("Age"));
                String gender = value.getString("Sex");

                if(gender != null)
                {
                    if (gender.equalsIgnoreCase("Male")) {
                        rbMale.setChecked(true);
                    } else if (gender.equalsIgnoreCase("Female")) {
                        rbFemale.setChecked(true);
                    }
                }

            }
        });

        btnEdit.setOnClickListener(v -> {
            profileImage.setEnabled(true);
            camera.setEnabled(true);
            etName.setEnabled(true);
            etMob.setEnabled(true);
            etAge.setEnabled(true);
            etWork.setEnabled(true);
            etAddress.setEnabled(true);
            rbMale.setEnabled(true);
            rbFemale.setEnabled(true);
            btnSave.setEnabled(true);
            btnEdit.setEnabled(false);
            btnSave.setVisibility(v.VISIBLE);
        });
        
        profileImage.setOnClickListener(v -> {
            imageChooser();
        });

        
        btnSave.setOnClickListener(v -> {
            Map<String, Object> data = new HashMap<>();
            data.put("FullName", etName.getText().toString().trim());
            data.put("Mobile", etMob.getText().toString().trim());
            data.put("Age", etAge.getText().toString().trim());
            data.put("Work", etWork.getText().toString().trim());
            data.put("Address", etAddress.getText().toString().trim());

            if (rgSex.getCheckedRadioButtonId() == R.id.rb_male) {
                data.put("Sex", "Male");
            } else if(rgSex.getCheckedRadioButtonId() == R.id.rb_female){
                data.put("Sex", "Female");
            }
            progressDialog.show();

            documentReference.set(data, SetOptions.merge()).addOnCompleteListener(task -> {
                progressDialog.dismiss();
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), "Data saved successfully", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            profileImage.setEnabled(false);
            camera.setEnabled(false);
            etName.setEnabled(false);
            etMob.setEnabled(false);
            etAge.setEnabled(false);
            etWork.setEnabled(false);
            etAddress.setEnabled(false);
            rbMale.setEnabled(false);
            rbFemale.setEnabled(false);
            btnSave.setEnabled(false);
            btnEdit.setEnabled(true);

        });

    }

    private void imageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 2);
    }

    //=============

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 2) {
                // Get the url of the image from data
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // update the preview image in the layout
                    profileImage.setImageURI(selectedImageUri);
                    imageUri = data.getData();
                    StorageReference imagename = storageReference.child(userId);
                    imagename.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
                        imagename.getDownloadUrl().addOnSuccessListener(uri -> {
                            Map<String ,Object> hMap = new HashMap<>();
                            hMap.put("Image",String.valueOf(uri));
                            documentReference.set(hMap,SetOptions.merge()).addOnCompleteListener(task -> {
                                if(task.isSuccessful()){
                                    Toast.makeText(getContext(), "Image Uploaded", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        });
//                        Toast.makeText(getContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        }
    }
}