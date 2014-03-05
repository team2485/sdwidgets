package team2485.smartdashboard.extension;

import edu.wpi.first.smartdashboard.gui.*;
import edu.wpi.first.smartdashboard.properties.BooleanProperty;
import edu.wpi.first.smartdashboard.properties.DoubleProperty;
import edu.wpi.first.smartdashboard.properties.IntegerProperty;
import edu.wpi.first.smartdashboard.properties.Property;
import edu.wpi.first.smartdashboard.types.DataType;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class AirTankWidget extends Widget {

    public static final String NAME = "Air Widget";
    public static final DataType[] TYPES = {DataType.NUMBER};

    private static final double MAX_VAL = 120, MIN_VAL = 0;

    private int MAX_DRAW_WIDTH = 132;
    private double value = 000;
    private int drawWidth = 0;
    private int X = 0;
    int val;
    private Color color = Color.RED;
    private double R;
    private double G;
    private double B;
    private double stylemulti1;
    private double stylemulti2;
    private String text;
    private final Property prop    = new BooleanProperty(this, "Smoothing", true);
    private final Property propval = new DoubleProperty(this, "Smoothing Mulltipyler (0.0 - 0.9)", .8);
    private final Property style   = new IntegerProperty(this, "Style (0 - 2)", 2);
    private BufferedImage airtank;
    private BufferedImage airtank2;
    private BufferedImage airtank3;

    @Override
    public void init() {
        try {
            airtank = ImageIO.read(getClass().getResourceAsStream("/team2485/smartdashboard/extension/res/Tank.png"));
            airtank2 = ImageIO.read(getClass().getResourceAsStream("/team2485/smartdashboard/extension/res/Tank2.png"));
            airtank3 = ImageIO.read(getClass().getResourceAsStream("/team2485/smartdashboard/extension/res/Tank3.png"));
        } catch (IOException e) {
        }

        final Dimension size = new Dimension(300, 140);
        this.setSize(size);
        this.setPreferredSize(size);
        this.setMinimumSize(new Dimension(10, 7));


        this.setMaximumSize(new Dimension(4000, 2000));
        this.setValue(10);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                    }
                    val++;
                    //System.out.println(value);
                    setValue(val);
                    if (val > 120) {
                        val = 0;
                    }
                }
            }
        }).start();
    }

    @Override
    public void propertyChanged(final Property prprt) {
    }

    @Override
    public void setValue(Object o) {
        if (prop.getValue().equals(true)) {
            //System.out.println(value * ((double) propval.getValue()));
            value = (value * ((double) propval.getValue())) + (((Number) o).doubleValue() * (1 - ((double) propval.getValue())));

            //System.out.println((value));
        } else {
            value = ((Number) o).doubleValue();
        }
        text = (int) value + "";
        repaint();
    }

//    private Color RotateColorCW(Color color){
//        int R;
//        int G;
//        int B;
//        B = color.getRed();
//        R = color.getGreen();
//        G = color.getBlue();
//        color = (new java.awt.Color(R, G, B));
//        return color;
//    }
//    private Color RotateColorCCW(Color color){
//        int R;
//        int G;
//        int B;
//        B = color.getGreen();
//        R = color.getBlue();
//        G = color.getRed();
//        color = (new java.awt.Color(R, G, B));
//        return color;
//    }
//    private Color InvertColor(Color color){
//        int R = (255-color.getRed());
//        int G = (255-color.getGreen());
//        int B = (255-color.getBlue());
//
//        color = (new java.awt.Color(R, G, B));
//        return color;
//    }
    @Override
    protected void paintComponent(final Graphics gg) {
        X = (int) Math.min(getWidth(), getHeight() * 1.7);
        //this.setSize(X, getHeight());

        if (value <= 60) {
            R = 251;
            G = 0;
            B = 0;
        } else if ((value > 60) && (value <= 90)) {
            R = (255 - ((value - 60) * (.2)));
            G = ((value - 60) * 4.367);
            B = 0;
        } else if ((value > 90) && (value <= 120)) {
            R = (int) 255 - ((value - 90) * ((value - 90) / 3.66));
            G = ((value - 90) * ((value - 90) / 14.05)) + 131;
            B = 0;
        } else if (value > 120) {
            R = 0;
            G = 195;
            B = 0;
        }
        color = (new java.awt.Color(((int) R), ((int) G), ((int) B)));
        final Graphics2D g = (Graphics2D) gg;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (value >= 000) {

            if (style.getValue().equals(0)) {

                if (drawWidth > MAX_DRAW_WIDTH) {
                    drawWidth = MAX_DRAW_WIDTH;
                }

                MAX_DRAW_WIDTH = (int) (X * .79);
                drawWidth = (int) ((value - MIN_VAL) / (MAX_VAL - MIN_VAL) * MAX_DRAW_WIDTH);

                stylemulti1 = (X / 2) * (0.24285714285);
                stylemulti2 = (X / 2) * (0.47285714285);

                //System.out.println(stylemulti1 + " " + stylemulti2);
                g.setColor(color);
                g.fillRoundRect(X / 10, (int) stylemulti1, drawWidth, (int) stylemulti2, X / 15, X / 15);

                g.setColor(Color.WHITE);
                g.setFont(new Font("Ubuntu", Font.PLAIN, X / 11));
                g.drawString(text + " PSI", (X - g.getFontMetrics().stringWidth(text + "PSI")) / 2, (int) (X / 1.97) + g.getFontMetrics().getHeight() / 2);

                if ((prop.getValue().equals(true))) {
                    g.drawString("~", (int) (X / 2 - (g.getFontMetrics().stringWidth("~"))) - (g.getFontMetrics().stringWidth(text + "PSI") / 2), (int) (X / 1.97) + g.getFontMetrics().getHeight() / 4);
                }

                g.drawImage(airtank2, 0, 0, (X), (X / 2), this);

            } else if (style.getValue().equals(1)) {
                MAX_DRAW_WIDTH = (int) (X * .92);
                drawWidth = (int) ((value - MIN_VAL) / (MAX_VAL - MIN_VAL) * MAX_DRAW_WIDTH);

                g.setColor(new java.awt.Color(100, 100, 100, 100));
                g.fillRoundRect((X / 23), (X / 15), MAX_DRAW_WIDTH, (int) (X / 2.7), 8, 8);

                if (drawWidth > MAX_DRAW_WIDTH) {
                    drawWidth = MAX_DRAW_WIDTH;
                }

                g.setColor(color);
                g.fillRoundRect(X / 23, X / 13, drawWidth, (int) (X / 2.7), 8, 8);

                g.setColor(Color.GREEN);
                g.setFont(new Font("Ubuntu", Font.BOLD, (X / 8)));
                g.drawString(text, (X - g.getFontMetrics().stringWidth(text)) / 2, (int) (X / 1.97) + g.getFontMetrics().getHeight() / 2);

                g.setFont(new Font("Consolas", 0, (X / 16)));
                g.drawString("PSI", (int) (X + (g.getFontMetrics(new Font("Ubuntu", Font.BOLD, (X / 8))).stringWidth(text))) / 2, (int) (X / 1.97) + g.getFontMetrics().getHeight() / 2);

                if ((prop.getValue().equals(true))) {
                    g.setFont(new Font("Consolas", 0, (X / 10)));
                    g.drawString("~", (int) (X / 2 - (g.getFontMetrics().stringWidth("~"))) - (g.getFontMetrics(new Font("Ubuntu", Font.BOLD, (X / 8))).stringWidth(text) / 2), (int) (X / 1.97) + g.getFontMetrics().getHeight() / 4);
                }

                g.drawImage(airtank, 0, 0, (X), (X / 2), this);

            } else {
                X = X*8/10;
                g.translate(X/16,X/16);
                MAX_DRAW_WIDTH = (int) (X);
                drawWidth = (int) ((value - MIN_VAL) / (MAX_VAL - MIN_VAL) * MAX_DRAW_WIDTH);

                g.setColor(new java.awt.Color(100, 100, 100, 100));
                g.fillRoundRect((X / 22), (X / 17), MAX_DRAW_WIDTH, (int) (X / 2.8), 8, 8);

                if (drawWidth > MAX_DRAW_WIDTH) {
                    drawWidth = MAX_DRAW_WIDTH;
                }

                g.setColor(color);
                g.fillRect(X / 22, X / 17, drawWidth, (int) (X / 2.8));
                g.setColor(Color.YELLOW);
                g.translate(X/20,0);
                g.setFont(new Font("Ubuntu", Font.BOLD, (X / 8)));
                g.drawString(text, (X - g.getFontMetrics().stringWidth(text)) / 2, (int) (X / 1.97) + g.getFontMetrics().getHeight() / 2);

                g.setFont(new Font("Consolas", 0, (X / 16)));
                g.drawString("PSI", (int) (X + (g.getFontMetrics(new Font("Ubuntu", Font.BOLD, (X / 8))).stringWidth(text))) / 2, (int) (X / 1.97) + g.getFontMetrics().getHeight() / 2);

                if ((prop.getValue().equals(true))) {
                    g.setFont(new Font("Consolas", 0, (X / 10)));
                    g.drawString("~", (int) (X / 2 - (g.getFontMetrics().stringWidth("~"))) - (g.getFontMetrics(new Font("Ubuntu", Font.BOLD, (X / 8))).stringWidth(text) / 2), (int) (X / 1.97) + g.getFontMetrics().getHeight() / 4);
                }
                g.translate(-X/20,0);
                g.drawImage(airtank3, -X/19, -X/19, X*12/10, (X*12/10 / 2), null);


            }

            //g.drawString("." + (int)value%1 , (int)(X + (g.getFontMetrics(new Font("Ubuntu",Font.BOLD,(X/8))).stringWidth(text)))/2, (int)(X/1.97) + g.getFontMetrics().getHeight());
        }

    }
}
