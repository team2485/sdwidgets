package team2485.smartdashboard.extension;

import edu.wpi.first.smartdashboard.gui.*;
import edu.wpi.first.smartdashboard.properties.Property;
import edu.wpi.first.smartdashboard.types.DataType;
import edu.wpi.first.wpilibj.tables.IRemote;
import edu.wpi.first.wpilibj.tables.IRemoteConnectionListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class PandaboardIndicator extends Widget implements IRemoteConnectionListener {
    public static final String NAME = "Pandaboard Indicator";
    public static final DataType[] TYPES = { DataType.BOOLEAN };
    
    private BufferedImage onImage, offImage;
    private boolean pandaboardOn = false;
    @Override
    public void init() {
        try {
             this.onImage = ImageIO.read(PandaboardIndicator.class.getResourceAsStream("/team2485/smartdashboard/extension/res/panda-on.png"));
            this.offImage = ImageIO.read(PandaboardIndicator.class.getResourceAsStream("/team2485/smartdashboard/extension/res/panda-off.png"));
        } catch (IOException e) {
            System.err.println("Error loading Pandaboard indicator images.");
            e.printStackTrace();
        }
        
        final Dimension size = new Dimension(91, 100);
        this.setSize(size);
        this.setPreferredSize(size);
        this.setMaximumSize(size);
        this.setMinimumSize(size);
        
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getClickCount() >= 2) sendHaltCommand();
            }
        });
        this.setToolTipText("Double-click to shutdown Pandaboard.");
    }

    @Override
    public void propertyChanged(Property prprt) {
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.drawImage(pandaboardOn ? onImage : offImage, 0, 0, null);
    }
    
//    @Override
    public void setValue(Object o) {
        pandaboardOn = (Boolean)o;
        this.setToolTipText(pandaboardOn ? "Double-click to shutdown Pandaboard." : "Pandaboard is not connected.");
        repaint();
    }

    @Override
    public void connected(IRemote ir) {
    }

    @Override
    public void disconnected(IRemote ir) {
        sendHaltCommand();
    }
    
    private void sendHaltCommand() {
        try {
            // send the "sudo halt" command via PuTTY to the Pandaboard
            Runtime.getRuntime().exec("\"C:\\Program Files (x86)\\PuTTY\\putty.exe\"", new String[] { "-ssh", "-m", "\"C:\\Program Files (x86)\\PuTTY\\sudohalt.txt\"", "root@10.24.85.72", "-pw" ,"Warlord10" });
            pandaboardOn = false;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error shutting down Pandaboard:\n" + e.getMessage(), "Pandaboard Indicator", JOptionPane.ERROR_MESSAGE);
            System.err.println("Error shutting down Pandaboard.");
            e.printStackTrace();
        }
    }
}
