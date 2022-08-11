package my.edu.utem.ftmk.workshop2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpNewUser extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;
    private FirebaseFirestore mStore;

    private TextView title, signUp;
    private EditText fullName, faculty, studentEmail, password, phoneNumber;
    private ProgressBar progressBar;
    private CheckBox isAdminBox, isStudentBox;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar


        setContentView(R.layout.activity_sign_up_new_user);

        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();

        title = (TextView) findViewById(R.id.title);
        title.setOnClickListener(this);

        signUp = (Button) findViewById(R.id.signUp);
        signUp.setOnClickListener(this);

        fullName = (EditText) findViewById(R.id.userFullName);
        faculty = (EditText) findViewById(R.id.faculty);
        studentEmail = (EditText) findViewById(R.id.studentEmail);
        password = (EditText) findViewById(R.id.password);
        phoneNumber = (EditText) findViewById(R.id.phoneNumber);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.title:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.signUp:
                signUp();
                break;
        }
    }

    private void signUp()
    {
        String name = fullName.getText().toString().trim();
        String fac = faculty.getText().toString().trim();
        String email = studentEmail.getText().toString().trim();
        String pass = password.getText().toString().trim();
        String phone = phoneNumber.getText().toString().trim();

        if (name.isEmpty())
        {
            fullName.setError("Your full name is required!");
            fullName.requestFocus();
            return;
        }

        if (name.length() < 6 )
        {
            fullName.setError(("Please enter your full name as in your IC"));
            fullName.requestFocus();
            return;
        }

        if (fac.isEmpty())
        {
            faculty.setError("Your faculty is required!");
            faculty.requestFocus();
            return;
        }

        if (phone.isEmpty())
        {
            phoneNumber.setError("Phone number is required!");
        }

        if (phone.length() != 10)
        {
            phoneNumber.setError("Please insert a valid phone number!");
            phoneNumber.requestFocus();
        }

        if (email.isEmpty())
        {
            studentEmail.setError("Your email is required!");
            studentEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            studentEmail.setError("Please enter a valid email!!");
            studentEmail.requestFocus();
            return;
        }

        if (pass.isEmpty())
        {
            password.setError("Please enter password!");
            password.requestFocus();
            return;
        }

        if (pass.length() < 8 )
        {
            password.setError("Minimum password required is 8 characters");
            password.requestFocus();
            return;
        }


        //enter data to database
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    Student student = new Student(name, fac, email, pass, phone);

                    FirebaseDatabase.getInstance().getReference("student").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(student).addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            //if success to reg
                            if (task.isSuccessful()) {
                                FirebaseUser mUser = mAuth.getCurrentUser();
                                Toast.makeText(SignUpNewUser.this, "You has been registered successfuly! Thankyou :)", Toast.LENGTH_LONG).show();
                                DocumentReference df = mStore.collection("students").document(mUser.getUid());
                                Map<String, Object> userInfo = new HashMap<>();
                                userInfo.put("FullName", fullName.getText().toString());
                                userInfo.put("Faculty", faculty.getText().toString());
                                userInfo.put("userEmail", studentEmail.getText().toString());
                                userInfo.put("PhoneNumber", phoneNumber.getText().toString());
                                userInfo.put("isStudent", "1");
                                df.set(userInfo);

                                progressBar.setVisibility(View.GONE);//change to visible to see the progress bar
                            }else
                            {
                                Toast.makeText(SignUpNewUser.this, "Your registration is failed, please try again!", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });

                } else  //if task is failed to proceed
                {
                    Toast.makeText(SignUpNewUser.this, "Your registration is failed, please try again!", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

    }
}