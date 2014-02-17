package team2485.smartdashboard.extension;

import edu.wpi.first.smartdashboard.gui.*;
import edu.wpi.first.smartdashboard.properties.Property;
import edu.wpi.first.smartdashboard.types.DataType;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import javax.swing.*;

public class ArmPot extends Widget {

    public static final String NAME = "ArmPot";
    public static final DataType[] TYPES = {DataType.NUMBER};

    private static final double MIN_VAL = 2000, MAX_VAL = 3000;
    public static int X = 245;
    public static int Y = 245;
    public int LX;
    public int LY;
    private double value = 0;
    private int rawPotVal = 9999;
    private double dec = 0;
    private int spin;
    private Color color;
    private String string;
    private int hyp;

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
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (true) {
//                    try {
//                        Thread.sleep(10);
//                    } catch (InterruptedException ex) {
//                    }
//                    rawPotVal = rawPotVal + 10;
//                    //System.out.println(value);
//                    setValue(arm);
//                    if (value > 3001) {
//                        value = 2000;
//                    }
//                }
//            }
//        }).start();
    }

    @Override
    public void propertyChanged(final Property prprt) {
    }

    @Override
    public void setValue(Object o) {
        rawPotVal = ((Number) o).intValue();
        if (value == 0) {
            value = ((Number) o).intValue();
            value = (int) value / 10;
        }
        if (rawPotVal > 9999) {
            if (((rawPotVal / 10000) == 2) || (rawPotVal / 10000 == 3)) {
                if (((rawPotVal % 10) == 1) || ((rawPotVal % 10) == 0)) {
                    spin = (int) (rawPotVal % 10);
                    value = ((value * .8) + ((rawPotVal / 10) * .2));
                }
            }
        }

        if ((value > 2800) && (value < 2870)) {
            color = Color.orange;
        } else {
            color = Color.GRAY;
        }
        if (((rawPotVal / 10) > 2830) && ((rawPotVal / 10) < 2840)) {
            value = 2835;
            color = Color.green;
        }
        if (((rawPotVal / 10) > 2990) && ((rawPotVal / 10) < 3010)) {
            value = 2835;
            color = Color.yellow;
        }
        dec = (value - 2427) / 4;

        repaint();
    }

    private class airtanksPanel extends JPanel {

        public airtanksPanel() {
        }

        @Override
        protected void paintComponent(final Graphics gg) {
            string = (int) Math.abs(dec) + "";// + "°";
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
            g.setFont(new java.awt.Font("Ubuntu", 0, 15));
            hyp = ((g.getFontMetrics().stringWidth(string) / 2) + 30);
            LX = (int) (Math.cos((dec - 90) * Math.PI / 180) * -50);
            LY = (int) (Math.sin((dec - 90) * Math.PI / 180) * -50);
            g.setColor(Color.GREEN);
            g.drawString("°", (LX + g.getFontMetrics().stringWidth(string)), LY+5);
            g.setFont(new java.awt.Font("Ubuntu", Font.BOLD, 30));
            //g.fillOval(LX, LY, 5, 5);
            LX = LX - (g.getFontMetrics().stringWidth(string) / 2);
            LY = LY + (g.getFontMetrics().getHeight() / 2);
            //g.drawOval(LX, LY, 5, 5);
            if (dec < 0) {
                g.drawString("-", (LX - g.getFontMetrics().charWidth('-')), LY);
            }
            g.setColor(color);
            g.drawString(string, LX, (LY));
            g.setFont(new java.awt.Font("Consolas", 0, 8));
            g.setColor(Color.GREEN);
            g.drawString("" + rawPotVal, LX + (g.getFontMetrics().charWidth(2)), LY + 7);
            g.translate(-X / 2, (-Y / 2));

        }
    }
}
