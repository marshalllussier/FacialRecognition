package com.autozone.facialrecognition.detection;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.media.Image;
import android.os.Environment;
import android.util.Log;

import androidx.camera.core.ImageProxy;
import androidx.camera.view.PreviewView;
import androidx.fragment.app.FragmentActivity;

import com.autozone.facialrecognition.fragments.ScanfaceFragment;
import com.autozone.facialrecognition.ml.MobileFaceNet;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceContour;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.common.ops.NormalizeOp;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.List;

public class FaceAnalyzer implements OnSuccessListener<List<Face>> {
    private final ScanfaceFragment scanfaceFragment;
    private final FragmentActivity activity;
    private final DrawType faceDrawType;
    private final PreviewView previewView;
    private InputImage inputImage;
    private ImageProxy imageProxy;
    private final ImageProcessor imageTensorProcessor = new ImageProcessor.Builder()
            .add(new ResizeOp( 112 , 112 , ResizeOp.ResizeMethod.BILINEAR ))
            .add(new NormalizeOp( 112f , 112f ))
            .build();

    public FaceAnalyzer(ScanfaceFragment scanfaceFragment, FragmentActivity activity, PreviewView previewView, DrawType faceDrawType, InputImage inputImage, ImageProxy imageProxy) {
        this.scanfaceFragment = scanfaceFragment;
        this.activity = activity;
        this.previewView = previewView;
        this.faceDrawType = faceDrawType;
        this.inputImage = inputImage;
        this.imageProxy = imageProxy;
    }


    @SuppressLint("UnsafeOptInUsageError")
    @Override
    public void onSuccess(List<Face> faces) {
            for (Face face : faces) {
                if (faceDrawType == DrawType.BOUNDARY_BOX) {
                    Rect bounds = face.getBoundingBox();
                    ShowFaceBox faceBox = new ShowFaceBox(activity, bounds);           // DRAW FACE BOX
                    previewView.addView(faceBox);
                    try {
                        MobileFaceNet model = MobileFaceNet.newInstance(scanfaceFragment.requireContext());

                        ContextWrapper cw = new ContextWrapper(scanfaceFragment.requireContext());
                        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
                        File file = new File(directory, "BitmapOfDetectedFace" + ".jpg"); // Creates a image file located at '/data/data/com.autozone.facialrecognition/app_imageDir'
                        if (!file.exists()) {
                            FileOutputStream fileOutputStream = null;
                            try {
                                fileOutputStream = new FileOutputStream(file);
                                Bitmap bitmap = toBitmap(imageProxy.getImage());  // Converts image proxy into a initial bitmap
                                float degrees = 90; //rotation degree
                                Matrix matrix = new Matrix();
                                matrix.setRotate(degrees);
                                Bitmap formattedBitmap = Bitmap.createBitmap(bitmap, (int)bounds.exactCenterX()-150, (int)bounds.exactCenterY()-150, 275, 275, matrix, false); // This takes our initial bitmap and crops it and rotates it. The values for cropping are currently hardcoded but will need to be adjust per-device for resolution.
//                                Bitmap bitmapToBufferToBitmap = getBitmap(convertBitmapToBuffer(formattedBitmap), 50 ,50); // This converts the formatted bitmap from above into a buffer, and then back into a bitmap and saves it for previewing. Currently it is an unrecognizable image. This is likely where our issues are originating from.
//                                bitmapToBufferToBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos); // Uncomment line line and comment out the line below to preview the byte buffer
                                formattedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                                fileOutputStream.flush();
                                fileOutputStream.close();
                            } catch (java.io.IOException exc) {
                                exc.printStackTrace();
                            }

                        }



                        // Creates inputs for reference.
//                        if(true) {
//
//                            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{2, 112, 112, 3}, DataType.FLOAT32);
////                        inputFeature0.loadBuffer(convertBitmapToBuffer(toBitmap(imageProxy.getImage())));
//                            ByteBuffer byteBuffer = convertBitmapToBuffer(toBitmap(imageProxy.getImage()));
//                            // Runs model inference and gets result.
//                            MobileFaceNet.Outputs outputs = model.process(inputFeature0);
//                            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
//                            System.out.println(outputFeature0 + " output feature");
//                            // Releases model resources if no longer used.
//                            model.close();
//                        }
                    } catch (IOException exc) {
                        exc.printStackTrace();
                    }
                } else {
                    for (FaceContour faceContour : face.getAllContours()) {               // DRAW CONTOUR POINTS
                        for (PointF point : faceContour.getPoints()) {
                            ShowContourPoint contours = new ShowContourPoint(activity, point);
                            previewView.addView(contours);

                        }
                    }
                }
//                                                            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LoginhistoryFragment()).commit(); // This will redirect to a different fragment upon successful login
            }
            imageProxy.close();
    }

    private ByteBuffer convertBitmapToBuffer(Bitmap image)  {
        TensorImage imageTensor = imageTensorProcessor.process( TensorImage.fromBitmap(image ));
        return imageTensor.getBuffer();
    }

    private Bitmap toBitmap(Image image) {
        Image.Plane[] planes = image.getPlanes();
        ByteBuffer yBuffer = planes[0].getBuffer();
        ByteBuffer uBuffer = planes[1].getBuffer();
        ByteBuffer vBuffer = planes[2].getBuffer();

        int ySize = yBuffer.remaining();
        int uSize = uBuffer.remaining();
        int vSize = vBuffer.remaining();

        byte[] nv21 = new byte[ySize + uSize + vSize];
        //U and V are swapped
        yBuffer.get(nv21, 0, ySize);
        vBuffer.get(nv21, ySize, vSize);
        uBuffer.get(nv21, ySize + vSize, uSize);

        YuvImage yuvImage = new YuvImage(nv21, ImageFormat.NV21, image.getWidth(), image.getHeight(), null);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        yuvImage.compressToJpeg(new Rect(0, 0, yuvImage.getWidth(), yuvImage.getHeight()), 75, out);

        byte[] imageBytes = out.toByteArray();
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }

    private Bitmap getBitmap(Buffer buffer, int width, int height) {
        buffer.rewind();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.copyPixelsFromBuffer(buffer);
        return bitmap;
    }



}
