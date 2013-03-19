package team2485.smartdashboard.extension;

import edu.wpi.first.smartdashboard.gui.*;
import edu.wpi.first.smartdashboard.properties.Property;
import edu.wpi.first.smartdashboard.types.DataType;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class FieldWidget extends Widget {
    public static final String NAME = "Field";
    public static final DataType[] TYPES = { DataType.NUMBER };

    private BufferedImage fieldImage, fieldGradImage;
    private Font font;
    private String noDistanceString;
    private Shape clip;
    private KillableThread animationThread;
    private double distance = 0;

    public FieldWidget() {
        try {
            this.fieldImage = ImageIO.read(FieldWidget.class.getResourceAsStream("/team2485/smartdashboard/extension/res/field.png"));
            this.fieldGradImage = ImageIO.read(FieldWidget.class.getResourceAsStream("/team2485/smartdashboard/extension/res/field-gradient.png"));
        } catch (IOException e) {
            System.err.println("Error loading field images.");
            e.printStackTrace();
        }

        final Dimension size = new Dimension(500, 240);
        this.setSize(size);
        this.setPreferredSize(size);
        this.setMinimumSize(size);
        this.setMinimumSize(size);

        this.font = new Font("BoomBox 2", Font.PLAIN, 20);
        this.noDistanceString = "No distance";

        this.clip = new Polygon(
                new int[] {  77, 421, 473, 473, 424, 424,  75,  75,  27,  27 },
                new int[] {  47,  47,  77, 169, 198, 223, 221, 197, 168,  77 },
                10);
    }

    @Override
    public void init() {
    }

    @Override
    public void setValue(Object o) {
        final double newDistance = ((Number)o).doubleValue();

        if (this.distance == 0) {
            // first time, so just set the distance
            this.distance = newDistance;
        }
        else {
            if (this.animationThread != null) this.animationThread.kill();

            // start animation thread
            this.animationThread = new KillableThread() {
                @Override
                public void run() {
                    try {
                        final double prevDistance = distance;

                        final int duration = 1000, interval = 30,
                                chunks = duration / interval;
                        final double chunkDiff = (newDistance - prevDistance) / chunks;
                        for (int t = 0; t < duration; t += interval) {
                            if (this.killed) return;

                            distance += chunkDiff;
                            repaint();

                            Thread.sleep(interval);
                        }
                    } catch (InterruptedException e) { }
                }
            };
            this.animationThread.start();
        }
    }

    @Override
    protected void paintComponent(final Graphics gg) {
        Graphics2D g = (Graphics2D)gg;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.drawImage(this.fieldImage, 0, 15, null);
        
        final boolean inBounds = this.distance > 0 && this.distance <= 54;

        // draw text
        g.setFont(font);
        if (this.distance == -1) {
            g.setColor(Color.red);
            g.drawString(noDistanceString, 250 - g.getFontMetrics().stringWidth(noDistanceString) / 2, 28);
        }
        else if (this.distance > 0) {
            final String distanceString = String.format("%.2f", this.distance);
            g.setColor(inBounds ? Color.white : Color.red);
            g.drawString(distanceString, 250 - (g.getFontMetrics().stringWidth(distanceString) / 2), 28);
        }
        else {
            g.setColor(Color.red);
            g.fillRect(240, 20, 20, 3);
        }

        // draw gradient
        if (inBounds) {
            int x;
            if ((boolean)HiddenField.getField("Alliance")) // red
                x = 472 - (int)((this.distance / 54) * 445) - 23;
            else // blue
                x =  27 + (int)((this.distance / 54) * 445) - 23;

            g.setClip(this.clip);
            g.drawImage(this.fieldGradImage, x, 15, null);
        }
    }

    @Override
    public void propertyChanged(Property prprt) {
    }

    private class KillableThread extends Thread {
        protected boolean killed = false;
        public void kill() {
            killed = true;
        }
    }
}
