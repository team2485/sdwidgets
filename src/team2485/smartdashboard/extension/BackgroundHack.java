package team2485.smartdashboard.extension;

import edu.wpi.first.smartdashboard.gui.StaticWidget;
import edu.wpi.first.smartdashboard.properties.Property;
import java.awt.Color;
import java.awt.Container;
import javax.swing.SwingUtilities;

public class BackgroundHack extends StaticWidget {
    public static final String NAME = "Layout > Background Hack";
    
    private Container parent;

    @Override
    public void init() {
        final BackgroundHack self = this;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                parent = self.getParent();
                parent.setBackground(new Color(0x111111));
            }
        });
    }

    @Override
    public void propertyChanged(Property prprt) {
    }
}
