package team2485.smartdashboard.extension;

import edu.wpi.first.smartdashboard.gui.StaticWidget;
import edu.wpi.first.smartdashboard.properties.Property;
import java.awt.Color;
import java.awt.Container;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class BackgroundHack extends StaticWidget {
    public static final String NAME = "Layout > Background";
    
    private Container parent;

    @Override
    public void init() {
        final BackgroundHack self = this;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                parent = self.getParent();

                // set background
                parent.setBackground(new Color(0x111111));

                // find the root frame
                while (!(parent instanceof JFrame))
                    parent = parent.getParent();

                // maximize it
                ((JFrame)parent).setExtendedState(JFrame.MAXIMIZED_BOTH);
            }
        });

        focusDS();
    }

    @Override
    public void propertyChanged(Property prprt) {
    }

    public static void focusDS() {
        try {
            Runtime.getRuntime().exec("C:\\Program Files\\SmartDashboard\\extensions\\SDWindowFocusAssistant.exe");
        } catch (IOException e) { }
    }
}
