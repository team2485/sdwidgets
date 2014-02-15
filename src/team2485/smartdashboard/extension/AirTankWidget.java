package team2485.smartdashboard.extension;

import edu.wpi.first.smartdashboard.gui.*;
import edu.wpi.first.smartdashboard.properties.Property;
import edu.wpi.first.smartdashboard.types.DataType;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class AirTankWidget extends Widget {

    public static final String NAME = "Air Widget";
    public static final DataType[] TYPES = {DataType.NUMBER};

    private static final double MAX_VAL = 120, MIN_VAL = 0;
    private static final int MAX_DRAW_WIDTH = 132;

    private double value = 70;
    private int drawWidth = 0;
    private Color color = Color.RED;

    private BufferedImage airtank;
    private JLabel label;

    @Override
    public void init() {
        try {
            airtank = ImageIO.read(BackgroundLeft.class.getResourceAsStream("/team2485/smartdashboard/extension/res/Tank.png"));
        } catch (IOException e) {
        }

        final Dimension size = new Dimension(143, 84);
        this.setSize(size);
        this.setPreferredSize(size);
        this.setMinimumSize(size);
        this.setMaximumSize(size);

        final BorderLayout layout = new BorderLayout(0, 0);
        this.setLayout(layout);

        this.add(new airtankPanel(), BorderLayout.CENTER);


        label = new JLabel("UNKNOWN");
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setForeground(Color.LIGHT_GRAY);
        label.setFont(new java.awt.Font("Ubuntu", Font.BOLD, 15));

        this.add(label, BorderLayout.SOUTH);


    }

    @Override
    public void propertyChanged(final Property prprt) {
    }

    @Override
    public void setValue(Object o) {
        value = ((Number) o).doubleValue();
        label.setText(String.format("%.1f PSI", value));

        drawWidth = (int) ((value - MIN_VAL) / (MAX_VAL - MIN_VAL) * MAX_DRAW_WIDTH);
        repaint();
    }

    private class airtankPanel extends JPanel {

        public airtankPanel() {
        }

        @Override
        protected void paintComponent(final Graphics gg) {

            final Graphics2D g = (Graphics2D) gg;
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (drawWidth > 0) {
                g.setColor(new java.awt.Color(100, 100, 100, 100));
                g.fillRoundRect(5, 5,  MAX_DRAW_WIDTH, 50, 8, 8);
                if (value>120)
                    value=120;
                color = (new java.awt.Color(((int)(255-(1/(value*value*.005) + (value)))), ((int)(((value*value*.005) + (value)))+25), ((int)(0.2*Math.abs((Math.abs((value)-70))-60)))));
                g.setColor(color);
                g.fillRoundRect(5, 5, drawWidth, 50, 8, 8);

            }
            g.drawImage(airtank, 0, 0, this);

        }
    }
}
