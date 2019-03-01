package oryx2D.util.hint;

import javax.swing.*;
import java.awt.*;

/**
 * Text field with hint text
 * <p>
 * from https://stackoverflow.com/a/24571681
 *
 * @Author Adam Gawne-Cain
 */
public class PasswordHintTextField extends JPasswordField {

    private final String hint;

    public PasswordHintTextField(String value, String hint) {
        super(value);
        this.hint = hint;
        this.setEnabled(true);
    }

    public PasswordHintTextField(String hint) {
        this.hint = hint;
        this.setEnabled(true);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (this.getPassword().length == 0) {
            int h = this.getHeight();
            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            Insets ins = this.getInsets();
            FontMetrics fm = g.getFontMetrics();
            int c0 = this.getBackground().getRGB();
            int c1 = this.getForeground().getRGB();
            int m = 0xfefefefe;
            int c2 = ((c0 & m) >>> 1) + ((c1 & m) >>> 1);
            g.setColor(new Color(c2, true));
            g.drawString(this.hint, ins.left, ((h / 2) + (fm.getAscent() / 2)) - 2);
        }
    }

}

/**
 * A service providing autocompletion support.
 *
 * @param <T> the type to be returned by the service
 * @author Samuel Sjoberg, http://samuelsjoberg.com
 * @version 1.0.0
 * @see AutoCompleteDocument
 */


