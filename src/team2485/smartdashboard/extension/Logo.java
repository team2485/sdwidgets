package team2485.smartdashboard.extension;

import edu.wpi.first.smartdashboard.gui.*;
import edu.wpi.first.smartdashboard.properties.*;
import edu.wpi.first.smartdashboard.robot.Robot;
import edu.wpi.first.wpilibj.tables.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Logo extends StaticWidget implements IRemoteConnectionListener {
    public static final String NAME = "Logo Connection Indicator";

    private static final String DS_FOCUS_EXE = "C:\\Program Files\\SmartDashboard\\DSFocusAssistant.exe";

    private BufferedImage disconnectedImage, connectedImage;
    private boolean connected = false;

    private final Runnable repainter = new Runnable() {
        @Override
        public void run() {
            repaint();
        }
    };

    public Logo() {
        try {
            this.connectedImage    = ImageIO.read(Logo.class.getResourceAsStream("/team2485/smartdashboard/extension/res/logo-connected.png"));
            this.disconnectedImage = ImageIO.read(Logo.class.getResourceAsStream("/team2485/smartdashboard/extension/res/logo-disconnected.png"));
        } catch (IOException e) {
            System.err.println("Error loading connection indicator images.");
            e.printStackTrace();
        }

        final Dimension size = new Dimension(719, 232);
        this.setSize(size);
        this.setPreferredSize(size);
        this.setMinimumSize(size);
        this.setMaximumSize(size);

        this.setOpaque(false);
    }

    @Override
    public void init() {
        Robot.addConnectionListener(this, true);

        // maximize window and set background color
        final Logo self = this;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // set background
                self.getParent().setBackground(new Color(0x212121));

                // maximize it
                ((JFrame)SwingUtilities.getWindowAncestor(self)).setExtendedState(JFrame.MAXIMIZED_BOTH);
            }
        });

        focusDS();
    }

    @Override
    public void disconnect() {
        Robot.removeConnectionListener(this);
    }

    @Override
    public void propertyChanged(Property property) {
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.drawImage(connected ? connectedImage : disconnectedImage, 0, 0, null);
    }

    @Override
    public void connected(IRemote remote) {
        if (!connected) {
            connected = true;
            SwingUtilities.invokeLater(repainter);
        }
    }

    @Override
    public void disconnected(IRemote remote) {
        if (connected) {
            connected = false;
            SwingUtilities.invokeLater(repainter);
        }
    }

    private static void focusDS() {
        try {
            Runtime.getRuntime().exec(DS_FOCUS_EXE);
        } catch (IOException e) {
            System.err.println("Could not focus driver station.");
        }
    }
}
