package my.edu.utem.ftmk.workshop2;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.firebase.auth.FirebaseAuth;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class RecognizeActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {


    private FirebaseAuth mAuth;

    JavaCameraView javaCameraView;
    File cascfile;
    CascadeClassifier facedetector;
    private Mat mRgba, mGrey;
    Button recognize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar

        setContentView(R.layout.activity_recognize);

        mAuth = FirebaseAuth.getInstance();


        recognize = findViewById(R.id.btn_recognize);

        ActivityCompat.requestPermissions(RecognizeActivity.this, new String[]
                {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        javaCameraView = findViewById(R.id.java_camera_view);

        if(!OpenCVLoader.initDebug())
        {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0,this, baseCallback);
        }
        else
        {
            try {
                baseCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
                startActivity(new Intent(RecognizeActivity.this, VotingModule.class));
            } catch (IOException e) {
                e.printStackTrace();
            }
            //startActivity(new Intent(RecognizeActivity.this, VotingModule.class));
        }
        javaCameraView.setCameraPermissionGranted();
        javaCameraView.setCameraIndex(CameraBridgeViewBase.CAMERA_ID_FRONT);
        javaCameraView.setVisibility(CameraBridgeViewBase.VISIBLE);
        javaCameraView.setCvCameraViewListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==200 && resultCode==RESULT_OK)
        {
            Uri uri = data.getData();
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            //imageView.setImageURI(uri);
            String imagePathLoaded = getAbsolutePath(uri);
            mRgba = Imgcodecs.imread(imagePathLoaded);
            cascfile = new File(getAbsolutePath(uri));
        }
    }
    private String getAbsolutePath(Uri uri)
    {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, proj, null, null, null);
        int colum_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(colum_index);
    }


    @Override
    public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat();
        mGrey = new Mat();

    }

    @Override
    public void onCameraViewStopped() {

        mRgba.release();
        mGrey.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRgba=inputFrame.rgba();
        mGrey=inputFrame.gray();

        //detect face

        MatOfRect facedetection = new MatOfRect();
        Imgproc.cvtColor(mRgba, mRgba, Imgproc.COLOR_RGB2BGR);
        int numFaces = facedetection.toArray().length;
        Mat matrix = mRgba.clone();
        facedetector.detectMultiScale(mRgba,facedetection);
        for(Rect rect: facedetection.toArray())
        {
            Imgproc.rectangle(mRgba,new Point(rect.x,rect.y),
                    new Point(rect.x+rect.width,rect.y+rect.height)
                    ,new Scalar(255,0,0));
        }

        //Mat finalmatrix = matrix.clone();
        //Bitmap bitmap = Bitmap.createBitmap(finalmatrix.cols(), finalmatrix.rows(), Bitmap.Config.ARGB_8888);
        //Utils.matToBitmap(finalmatrix, bitmap);
        return mRgba;
    }

    private final BaseLoaderCallback baseCallback=new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) throws IOException {
            switch (status)
            {
                case LoaderCallbackInterface.SUCCESS:
                {
                    InputStream is = getResources().openRawResource(R.raw.haarcascade_frontalface_alt2);
                    File cascadedir = getDir( "cascade" , Context.MODE_PRIVATE);
                    cascfile= new File(cascadedir, "haarcascade_frontalface_alt2.xml" );
                    FileOutputStream fos= new FileOutputStream(cascfile);

                    byte[] buffer=new byte[4096];
                    int bytesread;
                    while((bytesread = is.read(buffer))!=-1)
                    {
                        fos.write(buffer,0,bytesread);
                    }
                    is.close();
                    fos.close();

                    //load the cascade classifier
                    facedetector=new CascadeClassifier(cascfile.getAbsolutePath());
                    if(facedetector.empty())
                    {
                        facedetector=null;
                    }
                    else
                    {
                        cascadedir.delete();
                    }
                    javaCameraView.enableView();
                }
                break;

                default:
                {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };
}
