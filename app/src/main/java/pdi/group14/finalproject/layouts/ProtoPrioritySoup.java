package pdi.group14.finalproject.layouts;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Created by jeppe on 28-04-15.
 */
public class ProtoPrioritySoup extends ViewGroup {
    public ProtoPrioritySoup(Context context) {
        super(context);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int x = r + (l-r)/2;
        int y = t + (b-t)/2;
        for(int i = 0; i< getChildCount(); i++){
            View v = getChildAt(i);
            int width = v.getWidth();
            int height = v.getHeight();
            if( i == 0){
                v.layout(x-width/2,y-height/2,x+width/2,y+height/2);
            }else if(i == 1){

            }else if(i == 2){

            }else if(i == 3){

            }else if(i == 4){

            }
        }

    }
}
