package com.example.shoppingcart.util;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * @author ricky.yao on 2017/6/5.
 */

public class ViewDirectionDetector implements android.view.GestureDetector.OnGestureListener {

    private final GestureDetector gestureDetector;
    private IDirection iDirection;

    public ViewDirectionDetector(Context ctx){
        gestureDetector = new GestureDetector(ctx, this);
    }
    public boolean onTouchEvent(MotionEvent me,IDirection iDirection) {
        this.iDirection = iDirection;
        return gestureDetector.onTouchEvent(me);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        float minMove = 120;         //最小滑动距离
        float minVelocity = 0;      //最小滑动速度
        float beginX = e1.getX();
        float endX = e2.getX();
        float beginY = e1.getY();
        float endY = e2.getY();

        if (beginX - endX > minMove && Math.abs(velocityX) > minVelocity) {   //左滑
            if(iDirection!=null)iDirection.onDirection(Direction.LEFT);
        } else if (endX - beginX > minMove && Math.abs(velocityX) > minVelocity) {   //右滑
            if(iDirection!=null)iDirection.onDirection(Direction.RIGHT);
        } else if (beginY - endY > minMove && Math.abs(velocityY) > minVelocity) {   //上滑
            if(iDirection!=null)iDirection.onDirection(Direction.TOP);
        } else if (endY - beginY > minMove && Math.abs(velocityY) > minVelocity) {   //下滑
            if(iDirection!=null)iDirection.onDirection(Direction.BOTTOM);
        }
        return false;
    }

    public enum Direction {
        TOP,
        BOTTOM,
        LEFT,
        RIGHT
    }
    public interface IDirection{
        void onDirection(Direction direction);
    }
}
