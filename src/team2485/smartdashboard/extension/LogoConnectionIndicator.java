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

public class LogoConnectionIndicator extends StaticWidget implements IRemoteConnectionListener {
    public static final String NAME = "Logo Connection Indicator";
    private BufferedImage disconnectedImage, connectedImage;
    
    private boolean connected = false;
    private Runnable repainter = new Runnable() {
        @Override
        public void run() {
            repaint();
        }
    };

    public LogoConnectionIndicator() {
        try {
            this.   connectedImage = ImageIO.read(LogoConnectionIndicator.class.getResourceAsStream("/team2485/smartdashboard/extension/res/logo-connected.png"));
            this.disconnectedImage = ImageIO.read(LogoConnectionIndicator.class.getResourceAsStream("/team2485/smartdashboard/extension/res/logo-disconnected.png"));
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
}
