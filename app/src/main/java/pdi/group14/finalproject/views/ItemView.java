package pdi.group14.finalproject.views;

import android.app.Application;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import pdi.group14.finalproject.model.Item;
import pdi.group14.finalproject.model.Utilities;

/**
 * Created by Jeppe on 07-04-2015.
 */
public class ItemView extends View {
    Item item;
    private final float MARGIN = 5;
    private final float PADDING = 2;
    Paint paintBucketText;
    Paint paintBucketBackground;
    Paint paintBucketBorder;
    public ItemView(Context context,AttributeSet attributeSet, Item item) {
        super(context,attributeSet);
        this.item = item;
        //buckets are created once on construction to limit calculations at paint
        Paint[] buckets = Utilities.createPaint(this.item);
        this.paintBucketText = buckets[0];
        this.paintBucketBackground = buckets[1];
        this.paintBucketBorder = buckets[2];

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                Utilities.getSl().check(item);
                ((LinearLayout)getParent()).removeView(this);
                break;
            case MotionEvent.ACTION_UP:

                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas){
        //super.onDraw(canvas);
        float width = paintBucketText.measureText(item.getText());
        float height = paintBucketText.getTextSize();
        Rect bounds = new Rect(0,0,(int)(width+MARGIN*2+PADDING*2),(int)(height+MARGIN*2+PADDING*2));
        canvas.drawRect(bounds,paintBucketBackground);
        Rect innerBounds = new Rect((int)MARGIN,(int)MARGIN,(int)(bounds.right-MARGIN),(int)(bounds.bottom-MARGIN));
        canvas.drawRect(innerBounds,paintBucketBorder);
        canvas.drawText(item.getText(),PADDING+MARGIN,bounds.bottom-PADDING-MARGIN,paintBucketText);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        float width = paintBucketText.measureText(item.getText());
        float height = paintBucketText.getTextSize();
        setMeasuredDimension((int)(width+2*MARGIN+2*PADDING),(int)(height+2*MARGIN+2*PADDING));
    }

}
