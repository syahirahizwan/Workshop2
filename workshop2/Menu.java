package my.edu.utem.ftmk.workshop2;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Menu extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private FirebaseFirestore mStore;
    private Button view, vote;
    private TextView email, phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar


        setContentView(R.layout.activity_menu);

        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();

        view = findViewById(R.id.view);
        view.setOnClickListener(this);

        vote = findViewById(R.id.vote);
        vote.setOnClickListener(this);

        email = findViewById(R.id.email);
        phoneNumber = findViewById(R.id.phoneNumber);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null) {
            email.setText(firebaseUser.getEmail());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view:
                startActivity(new Intent(this, ShowCandidates.class));
                break;
            case R.id.vote:
                checkUserVoteStatus(mAuth.getCurrentUser().getUid());
                //startActivity(new Intent(this, VotingModule.class));
                break;
        }
    }

    private void checkUserVoteStatus(String uid) {
        DocumentReference df = mStore.collection("students").document(uid);
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getString("hasVote") == null) {
                    startActivity(new Intent(Menu.this, SendOTP.class));
                }
                if (documentSnapshot.getString("hasVote") != null) {
                    Toast.makeText(Menu.this, "You can only vote for one time", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(Menu.this, ShareStatus.class));
                }
            }
            });
        }

    public void logOut (View v)
    {
        mAuth.signOut();
        startActivity(new Intent(Menu.this, MainActivity.class));
    }
    }