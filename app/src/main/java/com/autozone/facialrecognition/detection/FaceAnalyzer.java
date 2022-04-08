package com.autozone.facialrecognition.detection;

import android.graphics.PointF;
import android.graphics.Rect;

import androidx.camera.view.PreviewView;
import androidx.fragment.app.FragmentActivity;

import com.autozone.facialrecognition.fragments.ScanfaceFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceContour;

import java.util.List;

public class FaceAnalyzer implements OnSuccessListener<List<Face>> {
    private ScanfaceFragment scanfaceFragment;
    private FragmentActivity activity;
    private DrawType faceDrawType;
    private PreviewView previewView;

    public FaceAnalyzer(ScanfaceFragment scanfaceFragment, FragmentActivity activity, PreviewView previewView, DrawType faceDrawType) {
        this.scanfaceFragment = scanfaceFragment;
        this.activity = activity;
        this.previewView = previewView;
        this.faceDrawType = faceDrawType;
    }


    @Override
    public void onSuccess(List<Face> faces) {
            for (Face face : faces) {
                if (faceDrawType == DrawType.BOUNDARY_BOX) {
                    Rect bounds = face.getBoundingBox();
                    ShowFaceBox faceBox = new ShowFaceBox(activity, bounds);           // DRAW FACE BOX
                    previewView.addView(faceBox);
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
    }

}
