package a07150835.gdmec.com.camerademo;

import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    private ImageView imageView;
    private File file;
    private Camera camera;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SurfaceView mSurfaceView = (SurfaceView) this.findViewById(R.id.surfaceView1);
        SurfaceHolder mSurfaceHOlder = mSurfaceView.getHolder();
        mSurfaceHOlder.addCallback(this);
        mSurfaceHOlder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

    }

    public void takePhoto(View v) {
        camera.takePicture(null, null, pictureCallback);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        camera = Camera.open();
        Camera.Parameters params = camera.getParameters();
        params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        try {
            camera.setPreviewDisplay(surfaceHolder);
        } catch (IOException e) {
            camera.release();
            camera = null;
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
    }

    Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] bytes, Camera camera) {
            if (bytes != null) {
                savePicture(bytes);
            }
        }
    };


    private void savePicture(byte[] bytes) {
        try {
            String imageId = System.currentTimeMillis() + "";
            String pathName = android.os.Environment.
                    getExternalStorageDirectory().getPath() + "/";
            File file = new File(pathName);
            if (!file.exists()) {
                file.mkdirs();
            }
            pathName += imageId + ".jpeg";
            file = new File(pathName);
            if (!file.exists()) {

                file.createNewFile();

            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bytes);
            fos.close();
            Toast.makeText(this, "已经保存在路径:" + pathName, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

