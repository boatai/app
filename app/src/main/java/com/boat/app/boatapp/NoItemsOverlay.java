package com.boat.app.boatapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import com.boat.app.boatapp.R;

/**
 * Created by robin on 07-Jun-18.
 */

public class NoItemsOverlay extends View {
    private Paint paint = new Paint();
    private Paint paintStroke = new Paint();

    public NoItemsOverlay(Context context) {
        super(context);
    }

    // Override the onDraw() Method
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        Center
        int fullHeight = canvas.getHeight();
        int fullWidth = canvas.getWidth();
        int halfWidth = fullWidth/2;
        int halfHeight = fullHeight/2;
        int quarterHeight = halfHeight/2;

//        Line
        paint.setColor(getResources().getColor(R.color.colorPrimary));
        canvas.drawRect(halfWidth-5, quarterHeight + 140, halfWidth+5, fullHeight - 135, paint);
        canvas.drawRect(halfWidth-5, fullHeight - 135, fullWidth - 235, fullHeight - 125, paint);

//        Text
        paint.setTextSize(62);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(getResources().getString(R.string.noItems), halfWidth, quarterHeight + 100, paint);

//        Circle around FAB
        paintStroke.setColor(getResources().getColor(R.color.colorPrimary));
        paintStroke.setStrokeWidth(10);
        paintStroke.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(fullWidth - 131, fullHeight - 131, (float) (145/(1.4)), paintStroke);
    }
}
