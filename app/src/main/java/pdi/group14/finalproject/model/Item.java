package pdi.group14.finalproject.model;

/**
 * Created by Jeppe on 24-03-2015.
 */
public class Item implements Comparable<Item>{
    private String ID;
    private String text;
    private int entries;
    private double avgtime;
    public Item(String ID){
        this.ID = ID;
        this.entries = 0;
        this.avgtime = 0;
    }
    public String getText() {
        return ID;
    }
    public double getAvgTimeAfter(){
        if(entries == 0)return -1;
        return avgtime;
    }
    public void updateAvgTimeAfter(double time){
        entries++;
        avgtime = (avgtime+time)/entries;
    }

    public String getID() {
        return ID;
    }

    @Override
    public int compareTo(Item another) {
        if(getAvgTimeAfter() < another.getAvgTimeAfter()) {
            return -1;
        }else if(another.getAvgTimeAfter() < getAvgTimeAfter()){
            return 1;
        }else{
            return 0;
        }
    }
}
