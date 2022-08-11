package my.edu.utem.ftmk.workshop2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.FileOutputStream;

public class ShareStatus extends AppCompatActivity {

    Button button, logOut;
    ImageView image;
    BitmapDrawable drawable;
    Bitmap bitmap;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar

        setContentView(R.layout.activity_share_status);

        mAuth = FirebaseAuth.getInstance();

        button = findViewById(R.id.button);
        logOut = findViewById(R.id.logout);
        image = findViewById(R.id.imageView);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareImage();
            }
        });
    }
    private void shareImage() {

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        drawable = (BitmapDrawable) image.getDrawable();
        bitmap = drawable.getBitmap();
        File file = new File(getExternalCacheDir() + "/image.png");

        Intent intent;
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
            intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        startActivity(Intent.createChooser(intent, "Share image via: "));
    }

    public void logOut(View view) {
        mAuth.signOut();
        startActivity(new Intent(ShareStatus.this, MainActivity.class));
    }
}