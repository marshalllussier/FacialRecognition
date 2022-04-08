package com.autozone.facialrecognition.detection;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.View;

import com.autozone.facialrecognition.R;

public class ShowContourPoint extends View {

    private PointF pointF;
    private Paint paint;

    public ShowContourPoint(Context context, PointF point) {
        super(context);

        pointF = point;
        paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        paint.setColor(getResources().getColor(R.color.white));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);
        pointF.x *=1.6;
        pointF.y *=1.6;
        canvas.drawPoint(pointF.x, pointF.y, paint);
        this.setVisibility(View.GONE);
    }
}
