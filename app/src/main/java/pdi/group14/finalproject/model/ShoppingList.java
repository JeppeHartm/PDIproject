package pdi.group14.finalproject.model;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by jeppe on 18-03-15.
 */
public class ShoppingList {

    ArrayList<Item> items;

    public ShoppingList(){
        initialize();
    }

    private void initialize() {
        items = new ArrayList<Item>();
    }

    public Item addItem(String query){

        Item item = Utilities.findItem(query);
        if(getIndex(item)!=-1)return null;
        items.add(item);
        Collections.sort(items);
        return item;
    }

    public int getIndex(Item item) {
        return items.indexOf(item);
    }

    public int getCount() {
        return items.size();
    }

    public void check(Item item) {
        items.remove(item);
    }
}
