package com.boat.app.boatapp.camera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by robin on 04-Apr-18.
 */

public class CameraOverlay extends View {
    private Paint paint = new Paint();
    public CameraOverlay(Context context) {
        super(context);
    }

    // Override the onDraw() Method
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.FILL);

//        center
        int fullHeight = canvas.getHeight();
        int fullWith = canvas.getWidth();
        int halfWidth = fullWith/2;
        int halfHeight = fullHeight/2;
        int quarterHeight = halfHeight/2;

//        left top corner
        canvas.drawRect(halfWidth-quarterHeight, quarterHeight, halfWidth-quarterHeight+100, quarterHeight+15, paint);
        canvas.drawRect(halfWidth-quarterHeight, quarterHeight, halfWidth-quarterHeight+15, quarterHeight+100, paint);

//        left bottom corner
        canvas.drawRect(halfWidth-quarterHeight, halfHeight+quarterHeight-15, halfWidth-quarterHeight+100, halfHeight+quarterHeight, paint);
        canvas.drawRect(halfWidth-quarterHeight, halfHeight+quarterHeight-100, halfWidth-quarterHeight+15, halfHeight+quarterHeight, paint);

//        right top corner
        canvas.drawRect(halfWidth+quarterHeight-100, quarterHeight, halfWidth+quarterHeight, quarterHeight+15, paint);
        canvas.drawRect(halfWidth+quarterHeight-15, quarterHeight, halfWidth+quarterHeight, quarterHeight+100, paint);

//        right bottom corner
        canvas.drawRect(halfWidth+quarterHeight-100, halfHeight+quarterHeight-15, halfWidth+quarterHeight, halfHeight+quarterHeight, paint);
        canvas.drawRect(halfWidth+quarterHeight-15, halfHeight+quarterHeight-100, halfWidth+quarterHeight, halfHeight+quarterHeight, paint);

//        background
        paint.setColor(Color.argb(110,0,0,0));
        canvas.drawRect(0, 0, halfWidth-quarterHeight, fullHeight, paint);
        canvas.drawRect(halfWidth+quarterHeight, 0, fullWith, fullHeight, paint);
        canvas.drawRect(halfWidth-quarterHeight, 0, halfWidth+quarterHeight, halfHeight-quarterHeight, paint);
        canvas.drawRect(halfWidth-quarterHeight, halfHeight+quarterHeight, halfWidth+quarterHeight, fullHeight, paint);
    }
}
