package my.edu.utem.ftmk.workshop2;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class SendOTP extends AppCompatActivity {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_otp);

        mAuth =FirebaseAuth.getInstance();

        final EditText phoneNum = findViewById(R.id.phoneNum);
        Button sendOtp = findViewById(R.id.sendOtp);

        sendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (phoneNum.getText().toString().trim().isEmpty()) {
                    Toast.makeText(SendOTP.this, "Enter mobile number", Toast.LENGTH_LONG).show();
                    return;
                }*/
                String myPhoneNumber = "+60-14-5384034";
                PhoneAuthOptions options =
                        PhoneAuthOptions.newBuilder(mAuth)
                                .setPhoneNumber(myPhoneNumber)       // Phone number to verify
                                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                .setActivity(SendOTP.this)                 // Activity (for callback binding)
                                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                    @Override
                                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                        Log.d("TAG", "onVerificationCompleted:"+phoneAuthCredential);
                                        //startActivity(new Intent(SendOTP.this, VerifyOTP.class));
                                    }

                                    @Override
                                    public void onVerificationFailed(@NonNull FirebaseException e) {
                                       // Log.w("TAG", "onVerificationFailed", e);
                                    }

                                    @Override
                                    public void onCodeSent(@NonNull String verificationId,
                                                           @NonNull PhoneAuthProvider.ForceResendingToken token) {

                                        Intent intent = new Intent(getApplicationContext(), VerifyOTP.class);
                                        intent.putExtra("mobile", myPhoneNumber);
                                        intent.putExtra("verificationId", verificationId);
                                        startActivity(intent);
                                        // The SMS verification code has been sent to the provided phone number, we
                                        // now need to ask the user to enter the code and then construct a credential
                                        // by combining the code with a verification ID.
                                        Log.d(TAG, "onCodeSent:" + verificationId);

                                    }
                                })          // OnVerificationStateChangedCallbacks
                                .build();
                PhoneAuthProvider.verifyPhoneNumber(options);
            }
        });
    }
}