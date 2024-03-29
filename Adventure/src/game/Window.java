package game;

import java.awt.Dimension;

import javax.swing.JFrame;

public class Window {
	
	private static JFrame frame;

	public Window(int w, int h, String title, Game game) {
		
		game.setPreferredSize(new Dimension(w, h));
		game.setMinimumSize(new Dimension(w, h));
		game.setMaximumSize(new Dimension(w, h));
		
		frame = new JFrame(title);
		
		frame.add(game);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		game.start();
	}

	public static JFrame getFrame() {
		return frame;
	}
	
	
}
