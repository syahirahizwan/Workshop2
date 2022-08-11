package my.edu.utem.ftmk.workshop2;

import android.content.Context;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class TrainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2{

    CameraBridgeViewBase javaCameraView;
    File cascfile;
    CascadeClassifier facedetector;
    private Mat mRgba,mGrey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar

        setContentView(R.layout.activity_train);

        javaCameraView = (CameraBridgeViewBase)findViewById(R.id.java_camera_view);
        javaCameraView.setCameraIndex(CameraBridgeViewBase.CAMERA_ID_FRONT);
        javaCameraView.setVisibility(SurfaceView.VISIBLE);
        javaCameraView.setCvCameraViewListener(this);
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        mRgba=new Mat();
        mGrey=new Mat();

    }

    @Override
    public void onCameraViewStopped() {
        mGrey.release();
        mRgba.release();

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRgba=inputFrame.rgba();
        mGrey=inputFrame.gray();
        MatOfRect facedetection= new MatOfRect();
        facedetector.detectMultiScale(mRgba,facedetection);
        for(Rect rect: facedetection.toArray())
        {
            Imgproc.rectangle(mRgba,new Point(rect.x,rect.y),
                    new Point(rect.x+rect.width,rect.y+rect.height)
                    ,new Scalar(255,0,0));
        }
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
                    File cascadedir=getDir( "cascade" , Context.MODE_PRIVATE);
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