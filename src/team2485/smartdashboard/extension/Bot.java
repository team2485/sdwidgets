package team2485.smartdashboard.extension;

import edu.wpi.first.smartdashboard.gui.*;
import edu.wpi.first.smartdashboard.properties.Property;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

public class BackgroundLeft extends StaticWidget {
    public static final String NAME = "Layout > Background Left";

    private BufferedImage image;

    private static final int IMG_WIDTH = 160;

    @Override
    public void init() {
        try {
            image = ImageIO.read(BackgroundLeft.class.getResourceAsStream("/team2485/smartdashboard/extension/res/bg-left.png"));
        } catch (IOException e) { }

        final Dimension size = new Dimension(IMG_WIDTH, 876);
        this.setSize(size);
        this.setPreferredSize(size);
        this.setMinimumSize(new Dimension(IMG_WIDTH, 100));
        this.setMaximumSize(new Dimension(IMG_WIDTH, Integer.MAX_VALUE));
    }

    @Override
    public void propertyChanged(Property prprt) {
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.drawImage(this.image, 0, 0, null);
    }
}
