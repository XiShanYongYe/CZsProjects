package org.nust.wsong.UI.experiment;


import static org.nust.wsong.UI.experiment.UISettings.COLOUR_BACKGROUND;
import static org.nust.wsong.UI.experiment.UISettings.COLOUR_ERROR_DARK;
import static org.nust.wsong.UI.experiment.UISettings.COLOUR_ITEM_BACKGROUND;
import static org.nust.wsong.UI.experiment.UISettings.COLOUR_OK_DARK;
import static org.nust.wsong.UI.experiment.UISettings.COLOUR_WARNING_DARK;
import static org.nust.wsong.UI.experiment.UISettings.MARGIN;
import java.awt.BasicStroke;
import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

public class InputConfigurationPanel extends JPanel {

	private static final long serialVersionUID = -3953022280744267314L;
	private JPanel newLogPanel;
	private List<LogPanel> logs;
	
	
	public InputConfigurationPanel() {
	
		logs = new ArrayList<LogPanel>();
		this.setBackground(COLOUR_BACKGROUND);
		this.setBorder(BorderFactory.createEmptyBorder(UISettings.MARGIN,
				UISettings.MARGIN, UISettings.MARGIN, UISettings.MARGIN));
		BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
		this.setLayout(layout);
		initNewLogPanel();
		this.add(newLogPanel);
		addLogPanel();
	}

	private void initNewLogPanel() {
		newLogPanel = new JPanel();
		newLogPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		newLogPanel.setBackground(COLOUR_ITEM_BACKGROUND);
		newLogPanel.setLayout(new BoxLayout(newLogPanel, BoxLayout.Y_AXIS));
		JLabel label = new JLabel("add log");
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		newLogPanel.add(Box.createVerticalStrut(UISettings.MARGIN));
		newLogPanel.add(label);
		newLogPanel.add(Box.createVerticalStrut(UISettings.MARGIN));
		newLogPanel.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				addLogPanel();
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
			}
		});
	}

	private void addLogPanel() {
		Component box = Box.createVerticalStrut(UISettings.MARGIN);
		LogPanel panel = new LogPanel(this, box);
		logs.add(panel);
		this.remove(newLogPanel);
		this.add(panel);
		panel.revalidate();
		this.add(box);
		this.add(newLogPanel);
		this.revalidate();
	}

	 void removeLog(LogPanel panel) {
		this.remove(panel);
		logs.remove(panel);
		this.remove(panel.getBox());
		this.revalidate();
	}
	 
	 public List<LogPanel> getLogPanels(){
		 return logs;
	 }
}
