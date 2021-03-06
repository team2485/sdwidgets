package team2485.smartdashboard.extension;

import edu.wpi.first.smartdashboard.gui.Widget;
import edu.wpi.first.smartdashboard.properties.Property;
import edu.wpi.first.smartdashboard.types.DataType;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class CoolBool extends Widget {
    public static final String NAME = "Cool Bool";
    public static final DataType[] TYPES = { DataType.BOOLEAN };
    
    private boolean value;
    private ImageIcon trueImage, falseImage;

    @Override
    public void init() {
        initComponents();
        
        try {
             this.trueImage = new ImageIcon(ImageIO.read(PandaboardIndicator.class.getResourceAsStream("/team2485/smartdashboard/extension/res/boolean-on.png")));
            this.falseImage = new ImageIcon(ImageIO.read(PandaboardIndicator.class.getResourceAsStream("/team2485/smartdashboard/extension/res/boolean-off.png")));
        } catch (IOException e) {
            System.err.println("Error loading boolean images.");
            e.printStackTrace();
        }
        
        this.title.setText(this.getFieldName());
        this.booleanDisplay.setIcon(this.falseImage);
    }
    
    @Override
    public void setValue(Object o) {
        value = (Boolean)o;
        this.booleanDisplay.setIcon(value ? this.trueImage : this.falseImage);
    }

    @Override
    public void propertyChanged(Property prprt) {
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        booleanDisplay = new javax.swing.JLabel();
        filler = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767));
        title = new javax.swing.JLabel();

        setMaximumSize(new java.awt.Dimension(500, 40));
        setMinimumSize(new java.awt.Dimension(100, 40));
        setPreferredSize(new java.awt.Dimension(200, 40));
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));

        booleanDisplay.setIcon(this.falseImage);
        booleanDisplay.setMaximumSize(new java.awt.Dimension(40, 40));
        booleanDisplay.setMinimumSize(new java.awt.Dimension(40, 40));
        booleanDisplay.setPreferredSize(new java.awt.Dimension(40, 40));
        add(booleanDisplay);
        add(filler);

        title.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        title.setForeground(new java.awt.Color(255, 255, 255));
        title.setText("Field Name");
        add(title);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel booleanDisplay;
    private javax.swing.Box.Filler filler;
    private javax.swing.JLabel title;
    // End of variables declaration//GEN-END:variables
}
