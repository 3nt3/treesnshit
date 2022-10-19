import basis.*;

import javax.swing.*;
import java.util.*;

public class JFrameTest {
    public JFrameTest() {
        Fenster window = new Fenster();
        JFrame jFrame = window.getMeinJFrame();
        jFrame.setTitle("[SWING] whatever");

        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }

        for (Map.Entry<Object, Object> entry : UIManager.getLookAndFeel().getDefaults().entrySet()) {
            System.out.format("%s %s\n", entry.getKey(), entry.getValue());
        }

        System.out.println(UIManager.getLookAndFeel().getDefaults().keys());


        JButton button = new JButton("yo");
        button.setBounds(0, 0, 100, 100);
        jFrame.add(button);
    }
}
