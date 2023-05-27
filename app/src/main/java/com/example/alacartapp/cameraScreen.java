package com.example.alacartapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.io.IOException;

public class cameraScreen extends AppCompatActivity implements SurfaceHolder.Callback, Camera.PreviewCallback{

    private Camera mCamera;
    private SurfaceHolder mSurfaceHolder;
    private SurfaceView mSurfaceView;
    private boolean isPreviewing;
    private TextView qrResultTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_screen);

        mSurfaceView = findViewById(R.id.surfaceView);
        qrResultTextView = findViewById(R.id.qrResultTextView);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);


    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (checkCameraPermission()) {
            startCamera();
        } else {
            Toast.makeText(this, "Camera permission not granted", Toast.LENGTH_SHORT).show();
        }
    }

    /*@Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (isPreviewing) {
            mCamera.stopPreview();
            isPreviewing = false;
        }

        try {
            mCamera.setPreviewDisplay(mSurfaceHolder);
            mCamera.setPreviewCallback(this);
            mCamera.startPreview();
            isPreviewing = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (isPreviewing) {
            mCamera.stopPreview();
            isPreviewing = false;
        }

        try {
            Camera.Parameters parameters = mCamera.getParameters();

            // Get the supported preview sizes
            List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();

            // Calculate the aspect ratio of the SurfaceView
            float aspectRatio = (float) width / height;

            // Find the closest preview size to match the aspect ratio
            Camera.Size closestSize = null;
            float closestAspectRatio = Float.MAX_VALUE;

            for (Camera.Size size : previewSizes) {
                float sizeAspectRatio = (float) size.width / size.height;
                float diff = Math.abs(aspectRatio - sizeAspectRatio);

                if (diff < closestAspectRatio) {
                    closestAspectRatio = diff;
                    closestSize = size;
                }
            }

            // Set the preview size
            if (closestSize != null) {
                parameters.setPreviewSize(closestSize.width, closestSize.height);
                mCamera.setParameters(parameters);
            }

            mCamera.setPreviewDisplay(mSurfaceHolder);
            mCamera.setPreviewCallback(this);
            mCamera.startPreview();
            isPreviewing = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        Camera.Size previewSize = camera.getParameters().getPreviewSize();
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new PlanarYUVLuminanceSource(data, previewSize.width, previewSize.height, 0, 0, previewSize.width, previewSize.height, false)));

        MultiFormatReader reader = new MultiFormatReader();
        try {
            Intent intent = new Intent(this, MainActivity.class);
            Result result = reader.decode(binaryBitmap);
            String qrCodeValue = result.getText();
            intent.putExtra("database", qrCodeValue);
            startActivity(intent);
        } catch (ReaderException e) {
            // QR code not found or could not be decoded
        }
    }

    private boolean checkCameraPermission() {
        return checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void startCamera() {
        try {
            mCamera = Camera.open();
        } catch (Exception e) {
            e.printStackTrace();

        }

    }
}