package com.example.facialrecognition;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.hardware.camera2.CameraDevice;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button camera_open_id;
    private ImageView image_id;
    private static final int pic_id = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        camera_open_id = (Button)findViewById(R.id.camera_button);
        image_id = (ImageView)findViewById(R.id.click_image);
        camera_open_id.setOnClickListener(v -> {

            Intent camera_intent
                    = new Intent(MediaStore
                    .ACTION_IMAGE_CAPTURE);
            startActivityForResult(camera_intent, pic_id);
        });
    }

    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == pic_id) {

            Bitmap photo = (Bitmap) data.getExtras()
                    .get("data");


            image_id.setImageBitmap(photo);
        }
    }
}