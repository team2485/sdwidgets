package team2485.smartdashboard.extension;

import edu.wpi.first.smartdashboard.gui.*;
import edu.wpi.first.smartdashboard.properties.Property;
import edu.wpi.first.smartdashboard.types.DataType;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class ArmPot extends Widget {

    public static final String NAME = "ArmPot";
    public static final DataType[] TYPES = {DataType.NUMBER};

    private static final double MIN_VAL = 2000, MAX_VAL = 3000;
    public static int X = 250;
    public static int Y = 250;
    public int LX;
    public int LY;
    private int value = 0;
    private int dec = 0;
    private int spin;
    private Color color;
    private String a;

    private BufferedImage armS;
    private BufferedImage arm;
    private BufferedImage circle;
    private BufferedImage circleS;

    @Override
    public void init() {
        try {
            arm = ImageIO.read(BackgroundLeft.class.getResourceAsStream("/team2485/smartdashboard/extension/res/ARMII.png"));
        } catch (IOException e) {
        }
        try {
            armS = ImageIO.read(BackgroundLeft.class.getResourceAsStream("/team2485/smartdashboard/extension/res/ARMV.png"));
        } catch (IOException e) {
        }
        try {
            circle = ImageIO.read(BackgroundLeft.class.getResourceAsStream("/team2485/smartdashboard/extension/res/Circle.png"));
        } catch (IOException e) {
        }
        try {
            circleS = ImageIO.read(BackgroundLeft.class.getResourceAsStream("/team2485/smartdashboard/extension/res/CircleS.png"));
        } catch (IOException e) {
        }

        final Dimension size = new Dimension(X, Y);
        this.setSize(size);
        this.setPreferredSize(size);
        this.setMinimumSize(size);
        this.setMaximumSize(size);

        final BorderLayout layout = new BorderLayout(0, 0);
        this.setLayout(layout);

        this.add(new airtanksPanel());
        a = new String();
        //this.add(back);
    }

    @Override
    public void propertyChanged(final Property prprt) {
    }

    @Override
    public void setValue(Object o) {
        value = ((Number) o).intValue();
        if (((value / 10000) == 2) || (value / 10000 == 3)) {
            if (((value % 10) == 1) || ((value % 10) == 0)) {
                spin = value % 10;
                value /= 10;
            }
        }
        dec = (value - 2427) / 4;
        //if (value == 0)
        // value = Math.random()*180;
        if ((value > 2759) && (value < 2781)) {
            color = Color.GREEN;
        } else if ((value > 2730) && (value < 2800)) {
            color = Color.orange;
        } else {
            color = Color.LIGHT_GRAY;
        }
        LX = (int)(Math.cos((dec - 90) * Math.PI / 180)*-45);
        LY = (int)(Math.sin((dec - 90) * Math.PI / 180)*-45);

        repaint();
    }

    private class airtanksPanel extends JPanel {

        public airtanksPanel() {
        }

        @Override
        protected void paintComponent(final Graphics gg) {

            final Graphics2D g = (Graphics2D) gg;
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.translate(X / 2, (Y / 2));
            if (spin == 0) {
                g.rotate((dec - 90) * Math.PI / 180);
                g.drawImage(arm, -5, -6, this);
                g.rotate(-(dec - 90) * Math.PI / 180);
                g.drawImage(circle, -15, -15, this);
            }
            if (spin == 1) {
                g.rotate((dec - 90) * Math.PI / 180);
                g.drawImage(armS, -5, -6, this);
                g.rotate(-(dec - 90) * Math.PI / 180);
                g.drawImage(circleS, -15, -15, this);
            }
            g.setColor(color);
            g.setFont(new java.awt.Font("Ubuntu", Font.BOLD, 30));
            g.drawString((int)dec + "°", LX, LY-10);
            g.translate(-X / 2, (-Y / 2));


        }
    }
}
