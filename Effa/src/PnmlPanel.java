import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PnmlPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JTextField textField;

	/**
	 * Create the panel.
	 */
	public PnmlPanel() {
		setSize(400, 30);
		setLayout(new GridLayout(0, 3, 0, 0));

		JLabel lblNewLabel = new JLabel("petri net");
		add(lblNewLabel);

		textField = new JTextField();
		add(textField);
		textField.setColumns(10);

		JButton btnNewButton = new JButton("Open");
		// DPNMLManage dpm = new DPNMLManage();
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				int value = chooser.showOpenDialog(PnmlPanel.this);
				if (value == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
				}

			}
		});
		add(btnNewButton);

	}

}
