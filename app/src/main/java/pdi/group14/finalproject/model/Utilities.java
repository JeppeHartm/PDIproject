package pdi.group14.finalproject.model;

import android.graphics.Paint;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import pdi.group14.finalproject.views.ItemView;

/**
 * Created by Jeppe on 07-04-2015.
 */
public class Utilities {
    private static final float BIG_TEXT_SIZE = 60;
    private static final float SML_TEXT_SIZE = 30;
    private static ShoppingList sl;
    private static HashMap<String,Item> itemMap;
    static{
        itemMap = new HashMap<>();
    }
    private Utilities(){}
    public static Paint[] createPaint(Item item) {
        //Text
        Paint p1 = new Paint();
        p1.setARGB(0xff,0xff,0x00,0x00);
        p1.setTextSize(getPTextSize(item));
        //Background
        Paint p2 = new Paint();
        p2.setStyle(Paint.Style.FILL);
        p2.setARGB(0xff,0xff,0x00,0x00);
        //Border
        Paint p3 = new Paint();
        p3.setStyle(Paint.Style.FILL);
        p3.setARGB(0xff,0x00,0xff,0xff);
        return new Paint[]{p1,p2,p3};
    }

    private static float getPTextSize(Item item) {
        //biggest 60, smallest 40
        int slcount = sl.getCount();
        float diff = BIG_TEXT_SIZE-SML_TEXT_SIZE;
        float delta = diff/(float)slcount;
        return (float)(BIG_TEXT_SIZE-delta*(float)sl.getIndex(item));
    }

    public static Item findItem(String query) {
        Item output = itemMap.get(query);
        if(output == null){
            output = new Item(query);
            itemMap.put(query,output);
        }
        return output;
    }

    public static void setSl(ShoppingList sl){
        Utilities.sl = sl;
    }
    public static ShoppingList getSl() {
        return sl;
    }
}
