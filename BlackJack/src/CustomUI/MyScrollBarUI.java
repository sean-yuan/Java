package CustomUI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class MyScrollBarUI extends BasicScrollBarUI{
	Color one, two;
	public MyScrollBarUI(){
		one = Color.ORANGE;
		two = Color.ORANGE;
	}
 
	@Override
	protected JButton createDecreaseButton(int orientation) {
		BufferedImage buttonIcon = null;
		try {
			buttonIcon = ImageIO.read(new File("resources/img/scrollbar/up.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    JButton button = new JButton(new ImageIcon(buttonIcon));
		button.setMargin(new Insets(0, 0, 0, 0));
		button.setBorder(null);
	    return button;
	}
 
	@Override
	protected JButton createIncreaseButton(int orientation) {
		BufferedImage buttonIcon = null;
		try {
			buttonIcon = ImageIO.read(new File("resources/img/scrollbar/down.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    JButton button = new JButton(new ImageIcon(buttonIcon));
		button.setMargin(new Insets(0, 0, 0, 0));
		button.setBorder(null);
	    return button;
	}
 
	@Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        g.fillRect(0, 0, 20, 130);
    }
 
    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
    	g.setColor(two);
    	g.translate(thumbBounds.x, thumbBounds.y);
    	g.fillRect(0, 0, thumbBounds.width, thumbBounds.height);
    	g.translate(-thumbBounds.x, -thumbBounds.y);
    }
}