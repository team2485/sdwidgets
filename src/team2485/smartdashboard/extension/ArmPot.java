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
    public static int X = 344;
    public static int Y = 344;
    public int LX;
    public int LY;
    private double value = 2000;
    private int rawPotVal = 25990;
    private int preVal[] = new int[5];
    public int armX;
    public int armY;
    private double dec = 0;
    private int spin;
    private Color color;
    private String string;
    private int hyp;
    private int i = 0;

    private BufferedImage armS;
    private BufferedImage arm;
    private BufferedImage circle;
    private BufferedImage circleS;

    @Override
    public void init() {
        try {
            arm = ImageIO.read(BackgroundLeft.class.getResourceAsStream("/team2485/smartdashboard/extension/res/ARMVI.png"));
        } catch (IOException e) {
        }
        try {
            armS = ImageIO.read(BackgroundLeft.class.getResourceAsStream("/team2485/smartdashboard/extension/res/ARMVIC.png"));
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
        this.setMinimumSize(new Dimension(100, 100));
        this.setMaximumSize((new Dimension(1000, 1000)));

        final BorderLayout layout = new BorderLayout(0, 0);
        this.setLayout(layout);

        for (int r = 0; r > preVal.length - 1; r++) {
            preVal[r] = 2000;
        }

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
//                    //rawPotVal=rawPotVal + (int)((Math.random()-.5)*10);
//                    if (rawPotVal > 30001) {
//                        rawPotVal = 20001;
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
        if (i >= preVal.length) {
            i = 0;
        }
        preVal[i] = rawPotVal / 10;
        i++;
        if (rawPotVal > 9999) {
            if (((rawPotVal % 10) == 1) || ((rawPotVal % 10) == 0)) {
                //System.out.println(value);
                spin = (int) (rawPotVal % 10);
                value = ((preVal[0] + preVal[1] + preVal[2] + preVal[3] + preVal[4]) / 5);
                //System.out.println(value);
            }
        }

        dec = (value - 2427) / 4;

        if (((rawPotVal / 10) > 2826) && ((rawPotVal / 10) < 2844)) {
            value = 2835;
            color = Color.green;
        } else if (((rawPotVal / 10) > 2985) && ((rawPotVal / 10) < 3015)) {
            value = 3000;
            color = Color.green;
        } else if ((value > 2800) && (value < 2870)) {
            color = Color.cyan;
        } else if ((value > 2975) && (value < 3025)) {
            color = Color.orange;
        } else {
            color = Color.yellow;
        }

        repaint();
    }

    private class airtanksPanel extends JPanel {

        public airtanksPanel() {
        }

        @Override
        protected void paintComponent(final Graphics gg) {

            X = getWidth();
            Y = getHeight();
            armX = X / 2;
            armY = X / 5;
            string = (int) Math.abs(dec) + "";// + "°";
            final Graphics2D g = (Graphics2D) gg;
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.translate(X / 2, (Y / 2));
            if (spin == 0) {
                g.rotate((dec - 90) * Math.PI / 180);
                g.drawImage(arm, -(armY / 8 ), -(armY / 7), armX, armY, this);
                g.rotate(-(dec - 90) * Math.PI / 180);
                g.drawImage(circle, -(armY / 6), -(armY / 6), (armY / 3), (armY / 3), this);
            }
            if (spin == 1) {
                g.rotate((dec - 90) * Math.PI / 180);
                g.drawImage(armS, -(armY / 8), -(armY / 7), armX, armY, this);
                g.rotate(-(dec - 90) * Math.PI / 180);
                g.drawImage(circleS, -(armY / 4), -(armY / 4), (armY / 2), (armY / 2), this);
            }
            hyp = ((g.getFontMetrics().stringWidth(string) / 2) + 30);
            LX = (int) (Math.cos((dec - 90) * Math.PI / 180) * -armY/1.5) + 2;
            LY = (int) (Math.sin((dec - 90) * Math.PI / 180) * -armY/1.7) - 7;
            g.setFont(new java.awt.Font("Ubuntu", 0, (int)(armY/4.667)));
            g.setColor(Color.GREEN);
            g.drawString("°", (LX + g.getFontMetrics().stringWidth(string)), LY + 5);
            g.setFont(new java.awt.Font("Ubuntu", Font.BOLD, (int)(armY/2.333)));
            g.setColor(color);
            //g.fillOval(LX, LY, 5, 5);
            LX = LX - (g.getFontMetrics().stringWidth(string) / 2);
            LY = LY + (g.getFontMetrics().getHeight() / 2);
            //g.drawOval(LX, LY, 5, 5);
            if (dec <= -1) {
                g.drawString("-", (LX - g.getFontMetrics().charWidth('-')), LY);
            }
            g.drawString(string, LX, (LY));
            g.setFont(new java.awt.Font("Consolas", 0, (armY/10)));
            g.setColor(Color.GREEN);
            g.drawString("" + rawPotVal, LX + (g.getFontMetrics().charWidth(2)), LY + (armY/10));
            g.translate(-X / 2, (-Y / 2));

        }
    }
}
