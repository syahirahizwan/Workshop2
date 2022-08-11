package my.edu.utem.ftmk.workshop2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class FaceDetection extends AppCompatActivity {

   /* ImageView imageView;
    ImageView imageView2;
    String imagePathLoaded;
    Mat matrix;*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_face_detection);


        Button train = (Button)findViewById(R.id.btn_train);
        train.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent swap = new Intent(FaceDetection.this, TrainActivity.class);
                startActivity(swap);
            }
        });
        Button recognize = (Button)findViewById(R.id.btn_recognize);
        recognize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent swap = new Intent(FaceDetection.this, RecognizeActivity.class);
                startActivity(swap);
            }
        });
    }
}