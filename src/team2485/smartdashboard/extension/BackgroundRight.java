package team2485.smartdashboard.extension;

import edu.wpi.first.smartdashboard.gui.*;
import edu.wpi.first.smartdashboard.properties.Property;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;

public class BackgroundRight extends StaticWidget {
    public static final String NAME = "Layout > Background Right";

    private BufferedImage image;

    private static final int IMG_WIDTH = 166;

    @Override
    public void init() {
        try {
            image = ImageIO.read(BackgroundRight.class.getResourceAsStream("/team2485/smartdashboard/extension/res/bg-right.png"));
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
