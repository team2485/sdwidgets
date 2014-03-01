package team2485.smartdashboard.extension;

import edu.wpi.first.smartdashboard.gui.StaticWidget;
import edu.wpi.first.smartdashboard.properties.Property;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

/**
 * Logitech G13 G-key input sender
 * @author Bryce Matsumori
 */
public class G13Sender extends StaticWidget {
    public static final String NAME = "G13 Sender";

    private static final int NUM_KEYS = 24;
    private static final long MIN_RELEASE_TIME = 100L;

    private NetworkTable table;
    private BufferedImage image, imageActivity;

    private final long[] keyPressTime = new long[NUM_KEYS];
    private final ScheduledFuture[] keyPressFutures = new ScheduledFuture[NUM_KEYS];

    private boolean activity;
    private final HashMap<Character, Integer> keystrokeMap;
    private final ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
    private final Runnable hideActivity = new Runnable() {
        @Override
        public void run() {
            activity = false;
            repaint();
        }
    };
    private ScheduledFuture activityFuture;

    public G13Sender() {
        final Dimension size = new Dimension(54, 81);
        setSize(size);
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);

        try {
            image = ImageIO.read(getClass().getResource("/team2485/smartdashboard/extension/res/G13.png"));
            imageActivity = ImageIO.read(getClass().getResource("/team2485/smartdashboard/extension/res/G13-activity.png"));
        } catch (IOException ex) {
            System.err.println("Could not load G13 images.");
            ex.printStackTrace();
        }

        // <editor-fold defaultstate="collapsed" desc="Keystroke -> G key map">

        keystrokeMap = new HashMap<>(NUM_KEYS);
        keystrokeMap.put('Q', 1);
        keystrokeMap.put('W', 2);
        keystrokeMap.put('E', 3);
        keystrokeMap.put('R', 4);
        keystrokeMap.put('T', 5);
        keystrokeMap.put('Y', 6);
        keystrokeMap.put('U', 7);

        keystrokeMap.put('A', 8);
        keystrokeMap.put('S', 9);
        keystrokeMap.put('D', 10);
        keystrokeMap.put('F', 11);
        keystrokeMap.put('G', 12);
        keystrokeMap.put('H', 13);
        keystrokeMap.put('J', 14);

        keystrokeMap.put('Z', 15);
        keystrokeMap.put('X', 16);
        keystrokeMap.put('C', 17);
        keystrokeMap.put('V', 18);
        keystrokeMap.put('B', 19);

        keystrokeMap.put('N', 20);
        keystrokeMap.put('M', 21);
        keystrokeMap.put(',', 22);

        keystrokeMap.put('L', 23);
        keystrokeMap.put('.', 24);

        // </editor-fold>
    }

    @Override
    public void init() {
        table = NetworkTable.getTable("G13");

        GlobalScreen.getInstance().addNativeKeyListener(new NativeKeyListener() {
            @Override
            public void nativeKeyPressed(NativeKeyEvent nke) {
                if (nke.getModifiers() == 11) { // ctrl + shift + alt
                    switch (nke.getKeyCode()) {
                        case NativeKeyEvent.VK_CONTROL:
                        case NativeKeyEvent.VK_SHIFT:
                        case NativeKeyEvent.VK_ALT:
                            break;
                        default: {
                            try {
                                final int key = keystrokeMap.get((char)nke.getKeyCode());
                                table.putBoolean(Integer.toString(key), true);
                                keyPressTime[key - 1] = System.currentTimeMillis();
                                if (keyPressFutures[key - 1] != null) keyPressFutures[key - 1].cancel(false);
                                System.out.println("Pressed G" + key);

                                activity = true;
                                repaint();
                                if (activityFuture != null) activityFuture.cancel(false);
                                activityFuture = exec.schedule(hideActivity, 400, TimeUnit.MILLISECONDS);
                            }
                            catch (NullPointerException ex) {
                                System.err.println("Could not map key press '" + (char)nke.getKeyCode() + "' (" + nke.getKeyCode() + ").");
                            }
                        }
                    }
                }
            }

            @Override
            public void nativeKeyReleased(NativeKeyEvent nke) {
                switch (nke.getKeyCode()) {
                    case NativeKeyEvent.VK_CONTROL:
                    case NativeKeyEvent.VK_SHIFT:
                    case NativeKeyEvent.VK_ALT:
                        break;
                    default: {
                        try {
                            final int key = keystrokeMap.get((char)nke.getKeyCode());
                            if (keyPressTime[key - 1] != 0) {
                                final long time = System.currentTimeMillis();
                                System.out.println(time - keyPressTime[key - 1]);
                                if (time - keyPressTime[key - 1] < MIN_RELEASE_TIME) {
                                    // minimum time between press and release not elapsed, so schedule the release
                                    keyPressFutures[key - 1] = exec.schedule(new Runnable() {
                                        @Override
                                        public void run() {
                                            table.putBoolean(Integer.toString(key), false);
                                            keyPressTime[key - 1] = 0;
                                            System.out.println("Released G" + key + " delayed");
                                            keyPressFutures[key - 1] = null;
                                        }
                                    }, MIN_RELEASE_TIME - (time - keyPressTime[key - 1]), TimeUnit.MILLISECONDS);
                                }
                                else {
                                    // otherwise do it now!
                                    table.putBoolean(Integer.toString(key), false);
                                    keyPressTime[key - 1] = 0;
                                    System.out.println("Released G" + key + " immediately");
                                }
                            }
                        }
                        catch (NullPointerException ex) {
                        }
                    }
                }
            }

            @Override
            public void nativeKeyTyped(NativeKeyEvent nke) {
            }
        });

        try {
            GlobalScreen.registerNativeHook();
        }
        catch (NativeHookException ex) {
            System.err.println("Could not register native key hook!");
            ex.printStackTrace();

            String confirm;
            do {
                confirm = JOptionPane.showInputDialog(null, "WARNING: Could not register native key hook. This means the G13 is UNUSABLE!\nEnter \"OK\" to dismiss.\n\n" + ex.getMessage(), "G13 Sender", JOptionPane.ERROR_MESSAGE);
            } while (!confirm.equalsIgnoreCase("ok"));
        }
    }

    @Override
    protected void paintComponent(Graphics gg) {
        Graphics2D g = (Graphics2D)gg;

        g.drawImage(activity ? imageActivity : image, 0, 0, null);
    }

    @Override
    public void propertyChanged(Property property) {
    }
}
