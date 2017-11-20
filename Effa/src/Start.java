

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.nust.heroine.view.MainFrame;

public class Start {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				MainFrame window = new MainFrame();
				window.setTitle("Effa");
				window.setSize(400,300);
				window.setVisible(true);
				window.setLocationRelativeTo(null);
				window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			}
		});
	}
}
