package my.edu.utem.ftmk.workshop2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView logInUser, logIn;
    private EditText studentEmail, password;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mStore;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar

        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();

        logInUser = findViewById(R.id.signUp);
        logInUser.setOnClickListener(this);

        logIn = findViewById(R.id.logIn);
        logIn.setOnClickListener(this);

        studentEmail = findViewById(R.id.studentEmail);
        password = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressBar);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signUp: //user click sign up
                startActivity(new Intent(this, SignUpNewUser.class));
                break;

            case R.id.logIn: //user click log in button
                userLogin();
                break;
        }
    }

    //validation for log in button
    private void userLogin() {
        String email = studentEmail.getText().toString().trim();
        String pass = password.getText().toString().trim();
        if (email.isEmpty()) {
            studentEmail.setError("Email is required!");
            studentEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            studentEmail.setError("Please enter a valid email!");
            studentEmail.requestFocus();
            return;
        }
        if (pass.isEmpty()) {
            password.setError("Password is required!");
            password.requestFocus();
            return;
        }
        if (pass.length() < 8) {
            password.setError("Minimum password required is 8 characters");
            password.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_LONG).show();
                checkUserAccessLevel(authResult.getUser().getUid());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    private void checkUserAccessLevel(String uid) {
        DocumentReference df = mStore.collection("students").document(uid);
        //extract data from document
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("TAG", "onSuccess:"+documentSnapshot.getData());

                //identify user access level
                if (documentSnapshot.getString("isAdmin")!=null)
                {
                    //user is admin
                    startActivity(new Intent(getApplicationContext(), AdminActivity.class));
                }
                if (documentSnapshot.getString("isStudent")!=null)
                {
                    //user is student
                    startActivity(new Intent(getApplicationContext(), Menu.class));
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null)
        {
            DocumentReference df = FirebaseFirestore.getInstance().collection("students").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
            df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.getString("isAdmin")!=null)
                    {
                        startActivity(new Intent(getApplicationContext(), AdminActivity.class));
                    }
                    if (documentSnapshot.getString("isStudent")!=null)
                    {
                        startActivity(new Intent(getApplicationContext(), Menu.class));
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}

