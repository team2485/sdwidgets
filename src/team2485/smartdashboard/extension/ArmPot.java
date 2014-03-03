package team2485.smartdashboard.extension;

import edu.wpi.first.smartdashboard.gui.*;
//import edu.wpi.first.smartdashboard.gui.elements.bindings.AbstractValueWidget;
import edu.wpi.first.smartdashboard.properties.IntegerProperty;
import edu.wpi.first.smartdashboard.properties.Property;
import edu.wpi.first.smartdashboard.types.DataType;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ArmPot extends Widget {

    public static final String NAME = "ArmPot";
    public static final DataType[] TYPES = {DataType.NUMBER};

    private static final double MIN_VAL = 2000, MAX_VAL = 3000;
    public static int X = 344;
    //public static int Y = 344;
    public int LX;
    public int LY;
    private double value = 2000;
    private int rawPotVal = 25990;
    public int armX;
    public int armY;
    private double deca = 0;
    private double decb = 0;
    private int spin;
    private Color color;
    private String string;
    private int hyp;
    private int i = 0;
    private final int[] preVal = new int[15];
    private int offset;
    private int length;
    private int valueField;
    //private int preval[] = new int[15];

    Property potset;
    Property smoothingfactor;

    private BufferedImage arm, armS, circle, circleS;

    @Override
    public void init() {
        potset = new IntegerProperty(this, "Potentiometer Offset",2427);
        smoothingfactor = new IntegerProperty(this, "Smoothing Factor",5);
//        if (potset.getValue() == null){
//            potset.setValue(2427);
//        }
//        if (smoothingfactor.getValue() == null){
//            smoothingfactor.setValue(5);
//        }
        try {
            arm = ImageIO.read(getClass().getResourceAsStream("/team2485/smartdashboard/extension/res/arm.png"));
            armS = ImageIO.read(getClass().getResourceAsStream("/team2485/smartdashboard/extension/res/arm-c.png"));
            circle = ImageIO.read(getClass().getResourceAsStream("/team2485/smartdashboard/extension/res/arm-pivot.png"));
            circleS = ImageIO.read(getClass().getResourceAsStream("/team2485/smartdashboard/extension/res/arm-pivot-s.png"));
        } catch (IOException e) { }

        final Dimension size = new Dimension(X, X);
        this.setSize(size);
        this.setPreferredSize(size);
        this.setMinimumSize(new Dimension(100, 100));
        this.setMaximumSize((new Dimension(800, 800)));

        final BorderLayout layout = new BorderLayout(0, 0);
        this.setLayout(layout);

        for (int r = 0; r > length; r++) {
            preVal[r] = 2000;
        }

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (true) {
//                    try {
//                        Thread.sleep(30);
//                    } catch (InterruptedException ex) { }
//                    rawPotVal = rawPotVal + 10;
//
//                    setValue(rawPotVal);
//                    if (rawPotVal > 30001) {
//                        rawPotVal = 20001;
//                    }
//                }
//            }
//        }).start();
    }

    @Override
    public void propertyChanged(final Property prprt) {
//        if (prprt == editable) {
//            valueField.setEnabled(editable.getValue());
//        }
    }

    @Override
    public void setValue(Object o) {
        potset.setValue(potset.getSaveValue());
        offset = (int)potset.getValue();
        potset.setValue(potset.getValue());
        potset.setSaveValue("Potentiometer Offset");
        length = (int)smoothingfactor.getValue();
        smoothingfactor.setSaveValue("Smoothing Factor");
        if (length > preVal.length){
            length = preVal.length;
            smoothingfactor.setValue(preVal.length);
        }
        rawPotVal = ((Number) o).intValue();
        if (value == 0) {
            value = ((Number) o).intValue();
            value = (int) value / 10;
        }
        if (i >= length) {
            i = 0;
        }
        preVal[i] = rawPotVal / 10;
        i++;
        if (rawPotVal > 9999) {
            if (((rawPotVal % 10) == 1) || ((rawPotVal % 10) == 0)) {
                //System.out.println(value);
                spin = (int) (rawPotVal % 10);
                value = 0;
                for (int j = 0; j < length; j++) {
                    value += (preVal[j])/length;
                }


            }
                //value = ((preVal[0] + preVal[1] + preVal[2] + preVal[3] + preVal[4]) / 5);
                //System.out.println(value);
            }


        deca = (value - (int)potset.getValue()  ) / 4;
        if (((value) > (offset+399)) && ((value) < (offset+417))) {
            value = offset+408;
            color = Color.cyan;
        } else if ((value > (offset+558)) && (value < (offset+583))) {
            value = (offset+573);
            color = Color.cyan;
        } else if ((value > (offset+373)) && (value < (offset+443))) {
            color = Color.green;
        } else if ((value > (offset+548)) && (value < (offset+593))) {
            color = Color.orange;
        } else {
            color = Color.yellow;
        }
        decb = (value - (int)potset.getValue()) / 4;
        repaint();
    }

    @Override
    protected void paintComponent(final Graphics gg) {
        X = Math.min(getWidth(), getHeight());

        //Y = X;
        armX = X / 2;
        armY = X / 5;
        string = (int) Math.abs(decb) + "";// + "°";
        final Graphics2D g = (Graphics2D) gg;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.translate(X / 2, (X / 2));
        if (spin == 0) {
            g.rotate((deca - 90) * Math.PI / 180);
            g.drawImage(arm, -(armY / 8), -(armY / 7), armX, armY, this);
            g.rotate(-(deca - 90) * Math.PI / 180);
            g.drawImage(circle, -(int) (armY / 3.6), -(int) (armY / 3.6), (int) (armY / 1.8), (int) (armY / 1.8), this);
        }
        if (spin == 1) {
            g.rotate((deca - 90) * Math.PI / 180);
            g.drawImage(armS, -(armY / 8), -(armY / 7), armX, armY, this);
            g.rotate(-(deca - 90) * Math.PI / 180);
            g.drawImage(circleS, -(int) (armY / 3.6), -(int) (armY / 3.6), (int) (armY / 1.8), (int) (armY / 1.8), this);
        }
        hyp = ((g.getFontMetrics().stringWidth(string) / 2) + 30);
        LX = (int) (Math.cos((deca - 90) * Math.PI / 180) * -armY / 1.4) + 4;
        LY = (int) (Math.sin((deca - 90) * Math.PI / 180) * -armY / 1.6) - 14;
        g.setFont(new java.awt.Font("Ubuntu", 0, (int) (armY / 4.667)));
        g.setColor(Color.GREEN);
        g.drawString("°", (LX + g.getFontMetrics().stringWidth(string)), LY + 5);
        g.setFont(new java.awt.Font("Ubuntu", Font.BOLD, (int) (armY / 2.333)));
        g.setColor(color);
        //g.fillOval(LX, LY, 5, 5);
        LX = LX - (g.getFontMetrics().stringWidth(string) / 2);
        LY = LY + (g.getFontMetrics().getHeight() / 2);
        //g.drawOval(LX, LY, 5, 5);
        if (deca <= -1) {
            g.drawString("-", (LX - g.getFontMetrics().charWidth('-')), LY);
        }
        g.drawString(string, LX, (LY));
        g.setFont(new java.awt.Font("Consolas", 0, (armY / 10)));
        g.setColor(Color.GREEN);
        g.drawString("" + rawPotVal, LX + (g.getFontMetrics().charWidth(2)), LY + (armY / 10));
        g.translate(-X / 2, (-X / 2));

    }
}
