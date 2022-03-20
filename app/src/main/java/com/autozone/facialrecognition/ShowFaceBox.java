package com.autozone.facialrecognition;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

public class ShowFaceBox extends View {

    private Rect rectangle_box;
    private Paint paint;

    public ShowFaceBox(Context context, Rect facebox) {
        super(context);

        rectangle_box = facebox;
        paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);
        rectangle_box.bottom*=1.5;
        rectangle_box.top*=1.5;
        rectangle_box.left*=1.5;
        rectangle_box.right*=1.5;
        canvas.drawRect(rectangle_box, paint);
        this.setVisibility(View.GONE);
    }
}
