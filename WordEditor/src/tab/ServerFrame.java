package tab;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.ServerSocket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import seanyuan_CSCI201L_Assignment4.CustomButton;
import ui.MyScrollBarUI;

public class ServerFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	public JTextArea textArea;
	private ServerSocket server;
	public ServerFrame(ServerSocket ss) {
		super("Server");
		server = ss;
		super.setPreferredSize(new Dimension(500, 300));
		super.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		JButton close = new JButton("CLOSE");
		close.addActionListener(new CloserActionListener(this, ss));
		textArea = new JTextArea();
		textArea.setEditable(false); 
		textArea.setSelectionColor(Color.orange);
		JScrollPane scroll = new JScrollPane ( textArea );
	    scroll.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
		super.getContentPane().add(scroll, BorderLayout.CENTER);
		super.getContentPane().add(close, BorderLayout.SOUTH);
		super.pack();
		super.setVisible(true);
		super.setBackground(Color.gray);
	}
	class CloserActionListener implements ActionListener{
		private JFrame frame;
		private ServerSocket s;
		public CloserActionListener(JFrame frame, ServerSocket ss) {
			this.frame = frame;
			this.s = ss;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			frame.setVisible(false);
			try {
				s.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}

