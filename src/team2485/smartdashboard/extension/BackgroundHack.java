package team2485.smartdashboard.extension;

import edu.wpi.first.smartdashboard.gui.StaticWidget;
import edu.wpi.first.smartdashboard.properties.Property;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;

public class BackgroundHack extends StaticWidget {
    public static final String NAME = "Background Hack";
    
    private Container parent;

    @Override
    public void init() {
        final BackgroundHack self = this;
        this.addHierarchyListener(new HierarchyListener() {
            @Override
            public void hierarchyChanged(HierarchyEvent e) {
                parent = self.getParent();
                parent.setBackground(new Color(0x111111));
            }
        });
    }

    @Override
    public void propertyChanged(Property prprt) {
    }
}
