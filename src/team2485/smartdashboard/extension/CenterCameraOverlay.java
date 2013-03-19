package team2485.smartdashboard.extension;

import edu.wpi.first.smartdashboard.camera.WPICameraExtension;
import java.awt.*;

public class CenterCameraOverlay extends WPICameraExtension {
    protected int xCoord = 20, yCoord = 20;

    public CenterCameraOverlay() {
        super();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        final int centerX = this.getWidth() / 2, height = this.getHeight();

        g.setColor(Color.red);
        g.fillRect(centerX + 2, 0, 1, height);
        g.fillRect(centerX - 3, 0, 1, height);

        g.setColor(Color.blue);
        g.fillRect(centerX + 60, 0, 1, height);
        g.fillRect(centerX - 61, 0, 1, height);

        g.setColor(Color.green);
        g.fillRect(centerX + 110, 0, 1, height);
        g.fillRect(centerX - 111, 0, 1, height);
    }
}
