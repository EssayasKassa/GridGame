import java.awt.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Class to represent a square on the game board
 * This is what is rendered on the game board
 * This class is also what is used to set the score
 *
 */
public class Rectangle {

    //the x-coordinate of this square
    private int x;
    //the y-coordinate of this square
    private int y;
    //the length of one side of this square
    private int size;
    //the color of the square
    private Color color;
    //is the square visible (or is it covered by its children)
    private boolean isVisible;
    //is this the currently selected square (yellow highlighted)
    private boolean isSelected;

    //the amount of this square is touching the edge of the game board
    private int borderSize;
    /**
     * Contructor
     * @param x1 x coord
     * @param y1 y coord
     * @param sz length of size
     * @param colr color
     * @param visible is visible
     * @param selected is selected
     */
    public Rectangle(int x1, int y1, int sz, Color colr, boolean visible, boolean selected) {

        x = x1;
        y = y1;
        size = sz;
        color = colr;
        isVisible = visible;
        isSelected = selected;
        borderSize = 0;
    }
    public void setX(int xx){
        x=xx;
    }
    //return the x coord
    public int getX() {
        return x;
    }

    public void setY(int yy){
        y=yy;
    }
    //return the y coord
    public int getY() {
        return y;
    }

    //return the length of one side
    public int getSize() {
        return size;
    }

    //return the color
    public Color getColor() {
        return color;
    }

    //is this square visible
    public void setVisible(boolean value){
        isVisible=value;
    }
    public boolean isVisible() {
        return isVisible;
    }

    //is it selected (yellow highlighted)
    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean value){
        isSelected=value;
    }

    //set the amount of this square touching the edge of game board
    public void setBorderSize(int border) {
        borderSize = border;
    }

    public void setBorderSize(Rectangle root){
        int count=0;
        if(x==root.getX() || x+size==root.getX()+root.getSize()){
            count++;
        }
        if(y==root.getY() || y+size==root.getX()+root.getSize()){
            count++;
        }
        borderSize=(size)*count;
    }

    //get the amount touching the edge of the game board
    public int getBorderSize() {
        return borderSize;
    }


}
