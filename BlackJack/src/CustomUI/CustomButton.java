package CustomUI;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class CustomButton extends JButton {
	private static final long serialVersionUID = 1L;
	public Boolean hoverOver = false;
	public CustomButton(String name){
		super(name);
	}
	
	 @Override
	    protected void paintComponent(Graphics g) {
	        this.setOpaque(false);
	        this.setContentAreaFilled(false);
	        this.setBorderPainted(false);
	        Dimension size = this.getSize();
	        this.getModel().addChangeListener(new ChangeListener() {
		        @Override
		        public void stateChanged(ChangeEvent e) {
		            ButtonModel model = (ButtonModel) e.getSource();
		            if (model.isRollover()) {
		            	hoverOver = true;
		            }
		            else{
		            	hoverOver = false;
		            }
		         }
		    });
	        if(hoverOver) {
	        	try {
					g.drawImage(ImageIO.read(new File("resources/img/menu/red_button11_selected.png")), 0, 0, size.width, size.height, this);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        } else {
	            g.drawImage(Toolkit.getDefaultToolkit().getImage("resources/img/menu/red_button11.png"), 0, 0, size.width, size.height, null);
	        }
//	        repaint();
	        super.paintComponent(g);
	    }
}
