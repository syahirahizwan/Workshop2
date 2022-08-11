package my.edu.utem.ftmk.workshop2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class AdminActivity extends AppCompatActivity implements View.OnClickListener {

    private Button addCandidates, viewVoteCount, logout;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_admin);

        mAuth = FirebaseAuth.getInstance();

        addCandidates = findViewById(R.id.addCandidates);
        addCandidates.setOnClickListener(this);

        viewVoteCount = findViewById(R.id.viewVoteCounts);
        viewVoteCount.setOnClickListener(this);

        logout = findViewById(R.id.logout);
        logout.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.addCandidates:
                startActivity(new Intent(this, AddCandidate.class));
                break;
            case R.id.viewVoteCounts:
                startActivity(new Intent(this, VoteCount.class));
                break;
            case R.id.logout:
                logOut();
                break;
        }

    }

    private void logOut() {
        mAuth.signOut();
        startActivity(new Intent(AdminActivity.this, MainActivity.class));
    }
}