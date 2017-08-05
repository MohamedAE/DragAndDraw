package com.bignerdranch.android.draganddraw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class BoxDrawingView extends View {

    private static final String TAG = "BoxDrawingView";

    //Reference to current box being acted upon
    private Box mCurrentBox;
    //Collection of drawn boxes
    private List<Box> mBoxen = new ArrayList<>();
    //Paint class defines style and colour characteristics
    private Paint mBoxPaint;
    private Paint mBackgroundPaint;

    //Used when creating the view in code
    public BoxDrawingView(Context context) {
        this(context, null);
    }

    /*Used when inflating the view from XML
    * AttributeSet passed when inflated from XML*/
    public BoxDrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);

        //Transparent red boxes
        mBoxPaint = new Paint();
        mBoxPaint.setColor(0x22ff0000);

        //Off-white background
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(0xfff8efe0);
    }

    /*Handle various types of touch inputs from the user*/
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        PointF current = new PointF(event.getX(), event.getY());
        String action = "";

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                action = "ACTION_DOWN";
                //Reset drawing state
                mCurrentBox = new Box(current);
                mBoxen.add(mCurrentBox);
                break;
            case MotionEvent.ACTION_MOVE:
                action = "ACTION_MOVE";
                if (mCurrentBox != null) {
                    mCurrentBox.setCurrent(current);
                    //Force the box to redraw itself so it can respond to the user's changing input
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                action = "ACTION_UP";
                mCurrentBox = null;
                break;
            case MotionEvent.ACTION_CANCEL:
                action = "ACTION_CANCEL";
                mCurrentBox = null;
                break;
        }

        Log.i(TAG, action + " at x = " + current.x + ", y = " + current.y);
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //Fill in the background white
        canvas.drawPaint(mBackgroundPaint);

        for (Box box : mBoxen) {
            //Determine the edges of each box
            float left = Math.min(box.getOrigin().x, box.getCurrent().x);
            float right = Math.max(box.getOrigin().x, box.getCurrent().x);
            float top = Math.min(box.getOrigin().y, box.getCurrent().y);
            float bottom = Math.max(box.getOrigin().y, box.getCurrent().y);

            //Draw rectangle
            canvas.drawRect(left, top, right, bottom, mBoxPaint);
        }
    }

}