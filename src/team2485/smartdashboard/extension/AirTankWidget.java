package team2485.smartdashboard.extension;

import edu.wpi.first.smartdashboard.gui.*;
import edu.wpi.first.smartdashboard.properties.Property;
import edu.wpi.first.smartdashboard.types.DataType;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;

public class AirTankWidget extends Widget {

    public static final String NAME = "Air Widget";
    public static final DataType[] TYPES = {DataType.NUMBER};

    private static final double MAX_VAL = 120, MIN_VAL = 0;

    private int MAX_DRAW_WIDTH = 132;
    private double value = 0;
    private int drawWidth = 0;
    private int X = 0;
    private Color color = Color.RED;
    private double R;
    private double G;
    private double B;
    private String text;

    private BufferedImage airtank;
    private JLabel label;

    @Override
    public void init() {
        try {
            airtank = ImageIO.read(BackgroundLeft.class.getResourceAsStream("/team2485/smartdashboard/extension/res/Tank2.png"));
        } catch (IOException e) {
        }

        final Dimension size = new Dimension(150, 75);
        this.setSize(size);
        this.setPreferredSize(size);
        this.setMinimumSize(new Dimension(10,7));
        this.setMaximumSize(new Dimension(1500,750));

        final BorderLayout layout = new BorderLayout(0, 0);
        this.setLayout(layout);

        this.add(new airtankPanel(), BorderLayout.CENTER);
        new Thread(new Runnable() {

            @Override
            public void run(){
            while(true){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    Logger.getLogger(AirTankWidget.class.getName()).log(Level.SEVERE, null, ex);
                }
                value ++;
                //System.out.println(value);
                setValue(label);
                if (value > 120)
                    value = 0;
            }
        }
        }).start();
    }

    @Override
    public void propertyChanged(final Property prprt) {
    }

    @Override
    public void setValue(Object o) {
       // value = ((Number) o).doubleValue();
        text = value + "";


        repaint();
    }

    private class airtankPanel extends JPanel {

        public airtankPanel() {
        }

public Color RotateColorCW(Color color){
        int R;
        int G;
        int B;
        B = color.getRed();
        R = color.getGreen();
        G = color.getBlue();
        color = (new java.awt.Color(R, G, B));
        return color;
    }
    public Color RotateColorCCW(Color color){
        int R;
        int G;
        int B;
        B = color.getGreen();
        R = color.getBlue();
        G = color.getRed();
        color = (new java.awt.Color(R, G, B));
        return color;
    }
    public Color InvertColor(Color color){
        int R = (255-color.getRed());
        int G = (255-color.getGreen());
        int B = (255-color.getBlue());
        
        color = (new java.awt.Color(R, G, B));
        return color;
    }
        @Override
        protected void paintComponent(final Graphics gg) {
            X = getHeight()*2;
            MAX_DRAW_WIDTH = (int)(X*.92);
            drawWidth = (int) ((value - MIN_VAL) / (MAX_VAL - MIN_VAL) * MAX_DRAW_WIDTH);
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
            }
            color = (new java.awt.Color(((int) R), ((int) G), ((int) B)));
            final Graphics2D g = (Graphics2D) gg;
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (drawWidth > 0) {
                g.setColor(new java.awt.Color(100, 100, 100, 100));
                g.fillRoundRect((X/23), (X/15), MAX_DRAW_WIDTH, (int)(X/2.7), 8, 8);
                if (drawWidth > MAX_DRAW_WIDTH) {
                    drawWidth = MAX_DRAW_WIDTH;
                }
                g.setColor(color);
                g.fillRoundRect(X/23, X/15, drawWidth, (int)(X/2.7), 8, 8);
                color = RotateColorCCW(color);
                color = InvertColor(color);
                //System.out.println(color);
                g.setColor(color);
                g.setFont(new Font("Ubuntu",Font.BOLD,(X/8)));
                g.drawString(text, (X-g.getFontMetrics().stringWidth(text))/2, (int)(getHeight()/1.75) + g.getFontMetrics().getHeight()/2);

            }
            g.drawImage(airtank, 0, 0, (X), (X/2), this);

        }
    }
}
