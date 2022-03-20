package com.autozone.facialrecognition;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

public class SecondActivity extends AppCompatActivity {
    PreviewView previewView;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private ImageCapture imageCapture;
    private FaceDetector faceDetector;
    String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    public static boolean hasPerms = true;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        for (String permission : permissions) {
            hasPerms = ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
        }
        if (hasPerms) {
            openCamera();
        } else {
            requestPermissions(permissions, 104);
        }

        FaceDetectorOptions options = new FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                .setContourMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                .enableTracking()
                .build();

        faceDetector = FaceDetection.getClient(options);
    }

    public void openCamera() {
        previewView = findViewById(R.id.previewView);
//        Button bCapture = findViewById(R.id.bCapture);
//        bCapture.setText("CAPTURE PHOTO");        the bCapture Button can be manipulated manually through code as well as in the res layout

//        bCapture.setOnClickListener(this);

        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                startCameraX(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, getExecutor());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 104) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(SecondActivity.this, "Insufficient Permissions", Toast.LENGTH_LONG).show();
            }
        }
    }

    Executor getExecutor() {
        return ContextCompat.getMainExecutor(this);
    }

    @SuppressLint("RestrictedApi")
    private void startCameraX(ProcessCameraProvider cameraProvider) {
        cameraProvider.unbindAll();
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
//                .requireLensFacing(CameraSelector.LENS_FACING_FRONT)          This should be uncommented and the above line should be commented for our app, but for testing purposes this is easier to work with.
                .build();
        Preview preview = new Preview.Builder()
                .build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());
        imageCapture = new ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build();
        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();

        imageAnalysis.setAnalyzer(getExecutor(), image -> {
            @SuppressLint("UnsafeOptInUsageError") Image mediaImage = image.getImage();
            if (mediaImage != null) {
                InputImage inputImage = InputImage.fromMediaImage(mediaImage, image.getImageInfo().getRotationDegrees());
                Task<List<Face>> result =
                        faceDetector.process(inputImage)
                                .addOnSuccessListener(
                                        faces -> {
                                            for (Face face : faces) {
                                                System.out.println("FACE BOX: "  + face.getBoundingBox());

                                                Rect bounds = face.getBoundingBox();
                                                ShowFaceBox faceBox = new ShowFaceBox(this, bounds);
                                                previewView.addView(faceBox);
                                            }
                                        })
                                .addOnFailureListener(Throwable::printStackTrace)
                                .addOnCompleteListener(task -> image.close());
//                                .addOnCompleteListener(x -> image.close());
                Log.d("TAG", "Result " + result);
            }
        });

        cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis);
    }


//    @SuppressLint({"RestrictedApi", "NonConstantResourceId"})
//    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.bCapture:
//                capturePhoto();
//                break;
//        }
//    }


    private void capturePhoto() {
        long timestamp = System.currentTimeMillis();

        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, timestamp);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");


        imageCapture.takePicture(
                new ImageCapture.OutputFileOptions.Builder(
                        getContentResolver(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        contentValues
                ).build(),
                getExecutor(),
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        Toast.makeText(SecondActivity.this, "Photo has been saved.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        Toast.makeText(SecondActivity.this, "ERROR: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

    }
}




