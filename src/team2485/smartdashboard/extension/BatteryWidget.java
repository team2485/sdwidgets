package team2485.smartdashboard.extension;

import edu.wpi.first.smartdashboard.gui.*;
import edu.wpi.first.smartdashboard.properties.Property;
import edu.wpi.first.smartdashboard.types.DataType;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class BatteryWidget extends Widget {
    public static final String NAME = "Battery Widget";
    public static final DataType[] TYPES = { DataType.NUMBER };

    private static final double MAX_VOLTAGE = 13, MIN_VOLTAGE = 11;
    private static final int MAX_DRAW_WIDTH = 110;

    private double value = 0;
    private int drawWidth = 0;
    private Color color;

    private BufferedImage battery;
    private JLabel label;

    @Override
    public void init() {
        try {
            battery = ImageIO.read(BackgroundLeft.class.getResourceAsStream("/team2485/smartdashboard/extension/res/battery.png"));
        } catch (IOException e) { }

        final Dimension size = new Dimension(144, 83);
        this.setSize(size);
        this.setPreferredSize(size);
        this.setMinimumSize(size);
        this.setMaximumSize(size);

        final BorderLayout layout = new BorderLayout(0, 0);
        this.setLayout(layout);

        this.add(new BatteryPanel(), BorderLayout.CENTER);

        label = new JLabel("unknown");
        label.setFont(new Font("Ubuntu", Font.PLAIN, 14));
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setForeground(Color.white);
        this.add(label, BorderLayout.SOUTH);
    }

    @Override
    public void propertyChanged(final Property prprt) {
    }

    @Override
    public void setValue(Object o) {
        value = ((Number)o).doubleValue();
        drawWidth = (int)((value - MIN_VOLTAGE) / (MAX_VOLTAGE - MIN_VOLTAGE) * MAX_DRAW_WIDTH);

        if (value > 12.3) color = Color.green;
        else if (value > 12) color = Color.orange;
        else color = Color.red;

        label.setText(String.format("%.2f volts", value));
        label.setForeground(value > 12 ? Color.white : Color.red);

        repaint();
    }

    private class BatteryPanel extends JPanel {
        public BatteryPanel() {
        }

        @Override
        protected void paintComponent(final Graphics gg) {
            final Graphics2D g = (Graphics2D)gg;
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g.drawImage(battery, 0, 0, this);

            if (drawWidth > 0) {
                g.setColor(color);
                g.fillRoundRect(13, 13, drawWidth, 41, 8, 8);
            }
        }
    }
}
