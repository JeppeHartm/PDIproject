package pdi.group14.finalproject.layouts;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.Collection;

import pdi.group14.finalproject.model.Utilities;
import pdi.group14.finalproject.views.ItemView;

/**
 * Created by jeppe on 15-04-15.
 */
public class PrioritySoup extends ViewGroup {
    enum EdgeOrient {horizontal,vertical}
    enum Position {bottom,top,left,right}
    Cluster cluster = null;

    @Override
    public void addView(View child, int index, LayoutParams params) {
        assert child instanceof ItemView;
        super.addView(child, index, params);
        cluster.addItem((ItemView)child);
    }

    @Override
    public void removeView(View view) {
        super.removeView(view);
        cluster.removeItem((ItemView)view);
    }

    @Override
    public void removeViewInLayout(View view) {
        super.removeViewInLayout(view);
        cluster.removeItem((ItemView)view);
    }

    @Override
    public void removeViewsInLayout(int start, int count) {
        for(int i = start+count-1; i >= start; i--){
            cluster.removeItem((ItemView) getChildAt(i));
        }
        super.removeViewsInLayout(start, count);
    }

    @Override
    public void removeViewAt(int index) {
        cluster.removeItem((ItemView)getChildAt(index));
        super.removeViewAt(index);
    }

    @Override
    public void removeViews(int start, int count) {
        for(int i = start+count-1; i >= start; i--){
            cluster.removeItem((ItemView)getChildAt(i));
        }
        super.removeViews(start, count);
    }

    @Override
    public void removeAllViewsInLayout() {
        super.removeAllViewsInLayout();
        cluster = new Cluster(cluster.getOrigin().x,cluster.getOrigin().y);
    }


    public PrioritySoup(Context context) {
        super(context);
        cluster = new Cluster(getPaddingLeft()+(getWidth()/2),getPaddingTop()+(getHeight()/2));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for(int i = 0; i < getChildCount(); i++){
            ItemView iv = (ItemView)getChildAt(i);
            Cluster.Quadruple q = cluster.getQuad(iv);
            iv.layout(q.getLeft(),q.getTop(),q.getRight(),q.getBottom());
        }

    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int minx = 0,maxx = 0,miny = 0,maxy = 0;
//        for(Cluster.Quadruple q: cluster.itemBounds){
//            minx = q.getLeft()<minx?q.getLeft():minx;
//            maxx = q.getRight()>maxx?q.getRight():maxx;
//            miny = q.getBottom()<miny?q.getBottom():miny;
//            maxy = q.getTop()<maxy?q.getTop():maxy;
//        }
//        int width = maxx - minx;
//        int height = miny - maxy;
//        setMeasuredDimension(width,height);
//    }

    class Cluster{
        ArrayList<Edge> freeEdges;
        ArrayList<ItemView> itemViews;
        ArrayList<Quadruple> itemBounds;
        public Point getOrigin() {
            return origin;
        }

        Point origin;
        public Cluster(int x, int y){
            freeEdges = new ArrayList<Edge>();
            itemViews = new ArrayList<ItemView>();
            itemBounds = new ArrayList<Quadruple>();
            origin = new Point(x,y);
            freeEdges.add(new Edge(origin,origin,Position.top,null));
        }

        public void addItem(ItemView iv){
            AlignedEdge best = closestEdgeThatFits(iv.getWidth(),iv.getHeight());
            freeEdges.remove(best.E);
            ArrayList<Edge> newEdges = (ArrayList<Edge>) generateEdges(best.E,iv,best.ALIGNMENT,best.BACKWARDS_ALIGNED);
            Point tl = newEdges.get(1).p1;
            freeEdges.addAll(newEdges);
            itemBounds.add(new Quadruple(tl.x,tl.x+iv.getWidth(),tl.y,tl.y+iv.getHeight(),iv));
            itemViews.add(iv);
        }
        public void removeItem(ItemView iv){
            assert iv!=null;
            int index = itemViews.indexOf(iv);
            ArrayList nIVs = (ArrayList) itemViews.subList(0,index);
            nIVs.remove(iv);
            ArrayList dep = (ArrayList) itemViews.subList(index,itemViews.size()-1);
            dep.remove(iv);
            itemViews = nIVs;
            for(int i = 0; i < freeEdges.size(); i++){
                if(freeEdges.get(i).itemView == iv) {
                    freeEdges = (ArrayList)freeEdges.subList(0,i-1);
                    break;
                }
            }
            for( ItemView e: (ArrayList<ItemView>)dep){
                addItem(e);
            }
            for( Quadruple q: itemBounds){
                if(q.getIv()==iv){
                    itemBounds.remove(q);
                    break;
                }
            }
        }
        private Collection<? extends Edge> generateEdges(Edge edge, ItemView iv, Point anchor, boolean backwardsAligned) {
            ArrayList<Edge> output = new ArrayList<Edge>();
            int edif;
            int h = iv.getHeight(), w = iv.getWidth();
            Point tl = null,tr,bl,br;
            switch(edge.position){
                case top:
                    tl = new Point(backwardsAligned?anchor.x-w:anchor.x,anchor.y-h);
                    edif = edge.getSize()-w;
                    if(edif > 0){
                        Point rp1 = new Point(backwardsAligned?anchor.x-edge.getSize():anchor.x+w,anchor.y);
                        Point rp2 = new Point(rp1.x+edif,anchor.y);
                        Edge rest = new Edge(rp1,rp2,edge.position,edge.itemView);
                        output.add(rest);
                    }
                    break;
                case bottom:
                    tl = new Point(backwardsAligned?anchor.x-w:anchor.x,anchor.y);
                    edif = edge.getSize()-w;
                    if(edif > 0){
                        Point rp1 = new Point(backwardsAligned?anchor.x-edge.getSize():anchor.x+w,anchor.y);
                        Point rp2 = new Point(rp1.x+edif,anchor.y);
                        Edge rest = new Edge(rp1,rp2,edge.position,edge.itemView);
                        output.add(rest);
                    }
                    break;
                case left:
                    tl = new Point(anchor.x-w,backwardsAligned?anchor.y-h:anchor.y);
                    edif = edge.getSize()-h;
                    if(edif > 0){
                        Point rp1 = new Point(anchor.x,backwardsAligned?anchor.y:anchor.y+h);
                        Point rp2 = new Point(anchor.x,rp1.y+edif);
                        Edge rest = new Edge(rp1,rp2,edge.position,edge.itemView);
                        output.add(rest);
                    }
                    break;
                case right:
                    tl = new Point(anchor.x,backwardsAligned?anchor.y-h:anchor.y);
                    edif = edge.getSize()-h;
                    if(edif > 0){
                        Point rp1 = new Point(anchor.x,backwardsAligned?anchor.y:anchor.y+h);
                        Point rp2 = new Point(anchor.x,rp1.y+edif);
                        Edge rest = new Edge(rp1,rp2,edge.position,edge.itemView);
                        output.add(rest);
                    }
                    break;
                default:
            }
            tr = new Point(tl.x+w,tl.y);
            bl = new Point(tl.x,tl.y+h);
            br = new Point(tl.x+w,tl.y+h);
            output.add(new Edge(tl,tr,Position.top,iv));
            output.add(new Edge(bl,br,Position.bottom,iv));
            output.add(new Edge(tl,bl,Position.left,iv));
            output.add(new Edge(tr,br,Position.right,iv));
            return output;
        }

        private AlignedEdge closestEdgeThatFits(int width,int height){
            for(int i = 0; i < freeEdges.size(); i++){
                Edge current = freeEdges.get(i);
                EdgeOrient orientation = current.getOrientation();
                int determiningSize = orientation==EdgeOrient.horizontal?width:height;
                if(determiningSize >= current.getSize()){
                    return new AlignedEdge(current,current.p1, false);
                }else{
                    Point p1 = current.p1;
                    Point p2 = current.p2;
                    boolean p1free = true;
                    boolean p2free = true;
                    for(Edge e : freeEdges){
                        p1free = !intersects(p1,e);
                        p2free = !intersects(p2,e);
                    }
                    if(p1free || p2free) return new AlignedEdge(current,p2free?p1:p2, !p2free);
                }
            }
            return null;
        }

        private boolean intersects(Point point, Edge e) {
            Point e1, e2;
            e1 = e.p1;
            e2 = e.p2;
            if(e1.y == point.y && e2.y == point.y){
                return Math.min(e1.x,e2.x) <= point.x && Math.max(e1.x,e2.x) >= point.x;
            }else if(e1.x == point.x && e2.x == point.x){
                return Math.min(e1.y,e2.y) <= point.y && Math.max(e1.y,e2.y) >= point.y;
            }else{
                return false;
            }
        }

        public Quadruple getQuad(ItemView iv) {
            for(Quadruple q : itemBounds){
                if(q.getIv()==iv)return q;
            }
            return null;
        }

        private class AlignedEdge {
            public final Edge E;
            public final Point ALIGNMENT;
            public final boolean BACKWARDS_ALIGNED;
            AlignedEdge(Edge e, Point alignment, boolean backwards_aligned) {
                E = e;
                ALIGNMENT = alignment;
                BACKWARDS_ALIGNED = backwards_aligned;
            }
        }
        private class Point{
            public int x,y;
            public Point(int x,int y){
                this.x = x;
                this.y = y;
            }
        }
        private class Edge{
            public Position position;
            public ItemView itemView;
            public Point p1, p2;
            public int size;
            public Edge(Point p1, Point p2,Position position,ItemView itemView){
                this.p1 = p1;
                this.p2 = p2;
                this.itemView = itemView;
                this.position = position;
                initSize();
            }
            public EdgeOrient getOrientation(){
                return p1.x==p2.x?EdgeOrient.vertical:EdgeOrient.horizontal;
            }
            public int getSize(){
                return size;
            }
            private void initSize(){
                int dx = p2.x-p1.x;
                int dy = p2.y-p1.y;
                size = (int)Math.sqrt(dx*dx+dy*dy);
            }
        }

        private class Quadruple {
            int l,r,t,b;
            ItemView iv;

            private Quadruple(int l, int r, int t, int b, ItemView iv) {
                this.l = l;
                this.r = r;
                this.t = t;
                this.b = b;
                this.iv = iv;
            }

            public int getLeft() {
                return l;
            }

            public int getRight() {
                return r;
            }

            public int getTop() {
                return t;
            }

            public int getBottom() {
                return b;
            }

            public ItemView getIv() {
                return iv;
            }
        }
    }
}
