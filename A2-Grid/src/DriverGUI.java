import javax.swing.*;
import java.awt.*;

/**
 * Main program entry point
 */
public class DriverGUI {

    public static void main(String [] args) {

        JFrame frame = new JFrame("Grid Game");

        GridRenderer renderer = new GridRenderer();

        frame.add(renderer);

        frame.setSize(new Dimension(NestedGrid.MAX_SIZE+15, NestedGrid.MAX_SIZE+145));
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
