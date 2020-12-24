import java.awt.*;
import java.util.ArrayList;
/**
 * The back-end for the Grid Game
 * Each Square in the Grid Game can have either 0 or 4
 * Children - it's a quad tree
 *
 * @author - Essayas Kassa and Moshiur Rahman
 *
 **/
public class NestedGrid {
    /**
     *    |-----|-----|
     *    |  UL |  UR |
     *    |-----+-----|
     *    |  LL |  LR |
     *    |_____|_____|
     */
    public static final int MAX_SIZE = 512;
    public  int lowestSize;
    public Color[] colr;
    private Node root;
    private Node selected;
    class Node {
       private Rectangle rectangle;
       private Node parent;
       private Node []children;
        Node (Rectangle rect, Node parent){
            rectangle = rect;
            this.parent = parent;
        }
    }

    /**
     * Create a NestedGrid w/ 5 random colored squares to start
     * a root and its 4 children the root is at level 1 and children at 2
     * the selected square (denoted as yellow highlight)
     * is the root square (the owner of the 4 child squares)
     * @param mxLevels the max depth of the game board
     * @param palette the color palette to use
     */
    public NestedGrid(int mxLevels, Color[] palette) {
        lowestSize = calcLowestSize(mxLevels);
        colr = palette;
        root=new Node(createRectangle(0,0,MAX_SIZE),null);
        root.children=createChildren(root);
        selected=root;
        selected.rectangle.setSelected(true);
    }
    /**
     * this method creates all the four children of a node object with all the necessary attributes.
     * @param grid Node object to create the children for
     * @return the Node[] array consisting of children
     */
    private Node [] createChildren(Node grid){
        if(grid==null) return null;

        grid.rectangle.setVisible(false);
        Node []children=new Node[4];
        children[3]=new Node(createRectangle(grid.rectangle.getX(),grid.rectangle.getY(),grid.rectangle.getSize()/2),grid);
        children[2]=new Node(createRectangle(grid.rectangle.getX(),grid.rectangle.getY()+grid.rectangle.getSize()/2,grid.rectangle.getSize()/2),grid);
        children[0]=new Node (createRectangle(grid.rectangle.getX()+(grid.rectangle.getSize()/2), grid.rectangle.getY(),grid.rectangle.getSize()/2),grid);
        children[1]=new Node(createRectangle(grid.rectangle.getX()+(grid.rectangle.getSize()/2), grid.rectangle.getY()+(grid.rectangle.getSize()/2),grid.rectangle.getSize()/2),grid);

        return children;
    }
    /**
     * creates a rectangle with specific  x,y and size attributes
     * @param x the x co-ordinate of the rectangle
     * @param y the y co-ordinate of the rectangle
     * @param size the size of the rectangle
     * @return new rectangle object
     */
    private Rectangle createRectangle(int x, int y,int size){
        return new Rectangle(x,y,size,colr[(int)(Math.random()*5)],true,false);
    }
    /**
     * The selected square moves up to be its parent (if possible)
     */
    public void moveUp() {
        selected.rectangle.setSelected(false);
        if(selected.parent!=null) {
            selected = selected.parent;
            selected.rectangle.setSelected(true);
        }
    }
    /**
     * the selected square moves into the upper right child (if possible)
     * of the currently selected square
     */
    public void moveDown() {
        if(!hasNoChildren(selected)) {
            selected.rectangle.setSelected(false);
            selected.children[0].rectangle.setSelected(true);
            selected=selected.children[0];
        }
    }
    /**
     * the selected square moves counter clockwise to a sibling
     */
    public void moveLeft() {
        if(selected.parent==null) return;

        int i = (getIndex() + 3) % 4;
        selected.rectangle.setSelected(false);
        selected=selected.parent.children[i];
        selected.rectangle.setSelected(true);
    }
    /**
     * Move the selected square to the next sibling clockwise
     */
    public void moveRight() {
        if (selected.parent == null) return;

        int i = (getIndex() + 1) % 4;
        selected.rectangle.setSelected(false);
        selected = selected.parent.children[i];
        selected.rectangle.setSelected(true);
    }
    /**
     * Return an array of the squares (as class Rectangle) to draw on the screen
     * @return the array of containing all the rectangles
     */
    public Rectangle[] rectanglesToDraw ( ) {
        ArrayList<Rectangle> allBlocks = collectRec(root);
        return allBlocks.toArray(new Rectangle[allBlocks.size()]);
    }
    /**
     * collects all the rectangle class of each node recursively
     * @param grid the first Node of the tree.
     * @return array list with all the rectangles
     */
    private ArrayList<Rectangle> collectRec(Node grid){
        ArrayList<Rectangle>block=new ArrayList<>();
        grid.rectangle.setBorderSize(root.rectangle);
        block.add(grid.rectangle);

        if(hasNoChildren(grid)) return block;

        block.addAll(collectRec(grid.children[0]));
        block.addAll(collectRec(grid.children[1]));
        block.addAll(collectRec(grid.children[2]));
        block.addAll(collectRec(grid.children[3]));
        return block;
    }
    /**
     * smash a square into 4 smaller squares (if possible)
     * a square at max depth level is not allowed to be smashed
     * leave the selected square as the square that was just
     * smashed (it's just not visible anymore)
     */
    public void smash() {
        if(!hasNoChildren(selected) || selected.rectangle.getSize() <= lowestSize) return;

        selected.rectangle.setVisible(false);
        selected.children=createChildren(selected);
    }
    /**
     * Rotate the descendants of the currently selected square
     * @param clockwise if true rotate clockwise, else counterclockwise
     */
    public void rotate(boolean clockwise) {
        rotate(clockwise,selected);
    }
    public void rotate(boolean clockwise, Node root){
        if(hasNoChildren(root)) return;

        Node temp = root.children[0];

        if(clockwise) {
            root.children[0] = root.children[3];
            root.children[3] = root.children[2];
            root.children[2] = root.children[1];
            root.children[1] = temp;
        }
        else{
            root.children[0] = root.children[1];
            root.children[1] = root.children[2];
            root.children[2] = root.children[3];
            root.children[3] = temp;
        }
        updateCoOrdinate(root);

        rotate(clockwise,root.children[0]);
        rotate(clockwise,root.children[1]);
        rotate(clockwise,root.children[2]);
        rotate(clockwise,root.children[3]);
    }
    /**
     * flip the descendants of the currently selected square
     * the descendants will become the mirror image
     * @param horizontally if true then flip over the x-axis,
     *                     else flip over the y-axis
     */
    public void swap (boolean horizontally) {
       swap(selected,horizontally);
    }
    /**
     * flip the descendants of the currently selected square.
     * flip every children node as well recursively.
     * @param grid the current grid
     * @param horizontally if true then flip over the x-axis,
     *                      else flip over the y-axis
     */
    private void swap(Node grid, boolean horizontally){
        if(hasNoChildren(grid)) return;

        Node temp = grid.children[0];

        if(horizontally) {
            grid.children[0] = grid.children[1];
            grid.children[1] = temp;
            temp = grid.children[2];
            grid.children[2] = grid.children[3];
            grid.children[3] = temp;
        }
        else{
            grid.children[0] = grid.children[3];
            grid.children[3] = temp;
            temp = grid.children[2];
            grid.children[2] = grid.children[1];
            grid.children[1] = temp;
        }
        updateCoOrdinate(grid);

        swap(grid.children[0],horizontally);
        swap(grid.children[1],horizontally);
        swap(grid.children[2],horizontally);
        swap(grid.children[3],horizontally);
    }
    /**
     * this method checks if the given Node has any children or not
     * @param grid the given Grid
     * @return True if given has no children, False if  it doesn't
     */
    private boolean hasNoChildren(Node grid){ return grid.children == null;}
    /**
     * calculate the current index of the child in the Children array.
     * @return i the index of the child
     */
    private int getIndex(){
        Node parent = selected.parent;
        for(int i=0; i<4;i++){
            if(parent.children[i].equals(selected)) return i;
        }
        return -1;
    }
    /**
     * this method update every children's x and y co-ordinate according to its parents x,y co-ordinate.
     * @param grid the parent node
     */
    private void updateCoOrdinate(Node grid){
        grid.children[0].rectangle.setX(grid.rectangle.getX() + grid.rectangle.getSize()/2);
        grid.children[0].rectangle.setY(grid.rectangle.getY());

        grid.children[1].rectangle.setX(grid.rectangle.getX() + grid.rectangle.getSize()/2);
        grid.children[1].rectangle.setY(grid.rectangle.getY() + grid.rectangle.getSize()/2);

        grid.children[2].rectangle.setX(grid.rectangle.getX());
        grid.children[2].rectangle.setY(grid.rectangle.getY() + grid.rectangle.getSize()/2);

        grid.children[3].rectangle.setX(grid.rectangle.getX());
        grid.children[3].rectangle.setY(grid.rectangle.getY());
    }
    /**
     * calculate the lowest size of the rectangle of the furthest child
     * @param mxLvls the number of maximum levels
     * @return the lowest size
     */
    private int calcLowestSize(int mxLvls){
        int s = MAX_SIZE;
        for(int i=0;i<mxLvls;i++){
            s=s/2;
        }
        return s;
    }
}
