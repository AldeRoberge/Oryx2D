package oryx2D.util.hint;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;

import javax.swing.JTextField;

/**
 * Text field with hint text
 * <p>
 * from https://stackoverflow.com/a/24571681
 *
 * @Author Adam Gawne-Cain
 */
public class HintTextField extends JTextField {

	private final String hint;

	public HintTextField(String value, String hint) {
		super(value);
		this.hint = hint;
		this.setEnabled(true);
	}

	public HintTextField(String hint) {
		this.hint = hint;
		this.setEnabled(true);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if (this.getText().length() == 0) {
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

