package my.edu.utem.ftmk.workshop2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class AddCandidate extends AppCompatActivity implements View.OnClickListener {

    private FirebaseFirestore mStore;
    private EditText candidateName, candidateFaculty, course, manifesto, campaignurl;
    private Button addCand, upload;
    private ImageView candidateImage;
    private ProgressBar progressBar;
    public Uri imageUri;
    private  FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_add_candidate);

        mStore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        addCand = findViewById(R.id.addCand);
        addCand.setOnClickListener(this);

        upload = findViewById(R.id.upload);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });

        candidateName = findViewById(R.id.candidateName);
        candidateFaculty = findViewById(R.id.candidateFaculty);
        course = findViewById(R.id.course);
        manifesto = findViewById(R.id.manifesto);
        campaignurl = findViewById(R.id.campaignurl);
        progressBar = findViewById(R.id.progressBar);

        candidateImage = findViewById(R.id.candidateImage);
        candidateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //successfully selecting image from gallery
                mGetContent.launch("image/*");

            }
        });
    }

    private void uploadImage() {
        if (imageUri!=null) {
            StorageReference reference = storage.getReference().child("image/"+ imageUri.getLastPathSegment());

            //create reference to store image in firebase storage inside image folder of the storage
            //store the file
            reference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        //image uploaded successfully
                        Toast.makeText(AddCandidate.this, "Image uploaded successfully!", Toast.LENGTH_LONG).show();
                    } else {
                        //failed to upload
                        Toast.makeText(AddCandidate.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
            reference.getRoot();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.addCand:
                addcandidate();
                break;
        }

    }

    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    //this is the result of uri
                    if (result!=null) {
                        candidateImage.setImageURI(result);

                        //set result in imageUri
                        imageUri = result;
                    }
                }
            });


    private void addcandidate() {
        progressBar.setVisibility(View.VISIBLE);
        DocumentReference df = mStore.collection("candidates").document();
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("FullName", candidateName.getText().toString());
        userInfo.put("Faculty", candidateFaculty.getText().toString());
        userInfo.put("Course", course.getText().toString());
        userInfo.put("Manifesto", manifesto.getText().toString());
        userInfo.put("Url", campaignurl.getText().toString());
        userInfo.put("Image", imageUri.toString());
        df.set(userInfo);
        Toast.makeText(AddCandidate.this, "Candidate has been successfully added to the database", Toast.LENGTH_LONG).show();
        progressBar.setVisibility(View.GONE);
        startActivity(new Intent(AddCandidate.this, AdminActivity.class));
    }
}