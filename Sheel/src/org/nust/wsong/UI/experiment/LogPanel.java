package org.nust.wsong.UI.experiment;

import static org.nust.wsong.UI.experiment.UISettings.COLOUR_ITEM_BACKGROUND;
import static org.nust.wsong.UI.experiment.UISettings.MARGIN;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

public class LogPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private JPanel left;
	private JPanel right;
	private InputConfigurationPanel icp;
	private Component box;
	private Map<String, List<String>> mappings;
	private JFileSelectionBox logFileSelector;
	private JMassFileSelectionBox modelFilesSelector;
	private boolean complete;


	public boolean isComplete() {
		return complete;
	}

	public LogPanel(InputConfigurationPanel icp, Component box) {
		complete = false;
		mappings = new TreeMap<String, List<String>>();
//		List<String> models = new ArrayList<String>();
		this.icp = icp;
		this.box = box;
		this.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.setBackground(COLOUR_ITEM_BACKGROUND);
		this.setLayout(new ColumnLayout(2, MARGIN));
		this.setBorder(BorderFactory.createEmptyBorder(MARGIN + 2, MARGIN,
				MARGIN, MARGIN));
		initLeft();
		initRight();
		this.add(left);
		this.add(right);
		initRemove();
	}

	
	public JFileSelectionBox getLogFileSelector() {
		return logFileSelector;
	}

	public void setLogFileSelector(JFileSelectionBox logFileSelector) {
		this.logFileSelector = logFileSelector;
	}

	public JMassFileSelectionBox getModelFilesSelector() {
		return modelFilesSelector;
	}

	public void setModelFilesSelector(JMassFileSelectionBox modelFilesSelector) {
		this.modelFilesSelector = modelFilesSelector;
	}

	public Component getBox() {
		return box;
	}

	private void initLeft() {
		left = new JPanel();
		left.setAlignmentY(Component.TOP_ALIGNMENT);
		left.setOpaque(false);
		left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

		JLabel label = new JLabel("Select the compliant logfile:");
		JPanel labelWrap = new JPanel();
		labelWrap.setLayout(new BoxLayout(labelWrap, BoxLayout.X_AXIS));
		labelWrap.setOpaque(false);
		labelWrap.add(label);
		labelWrap.add(Box.createHorizontalGlue());
		left.add(labelWrap);

		logFileSelector = new JFileSelectionBox(new FileNameExtensionFilter(
				"Log file (*.mxml, *.xes)", "mxml", "xes"));
		left.add(logFileSelector);
//		logFileSelector.setFile(logFileSelector.);

		left.add(Box.createVerticalStrut(MARGIN));

	}

	private void initRight() {
		right = new JPanel();
		right.setAlignmentY(Component.TOP_ALIGNMENT);
		right.setOpaque(false);
		right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
		JLabel label = new JLabel("Select the corresponding deviated log(s):");
		JPanel labelWrap = new JPanel();
		labelWrap.setOpaque(false);
		labelWrap.setLayout(new BoxLayout(labelWrap, BoxLayout.X_AXIS));
		labelWrap.add(label);
		labelWrap.add(Box.createHorizontalGlue());
		right.add(labelWrap);
		modelFilesSelector = new JMassFileSelectionBox(
				new FileNameExtensionFilter("Log file (*.mxml, *.xes)", "mxml", "xes"));
//		modelFilesSelector.setFiles(models.toArray(new String[models.size()]));
		right.add(modelFilesSelector);
	}

	private void initRemove() {
		this.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				boolean x = getWidth() - arg0.getX() <= 16;
				boolean y = arg0.getY() <= 16;
				if (x && y) {
					arg0.consume();
					icp.removeLog(LogPanel.this);
				}
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

	
	public Map<String, List<String>> getMappings() {
		List<String> mappingTo = new ArrayList<>();
//		
//		
//		List<LogPanel> logPanels = inputConfigurationPanel.getLogPanels();
//		for(LogPanel panel :logPanels){
//			System.out.println(panel.getLogFileSelector().getFile()+"--"+panel.getModelFilesSelector().getFiles());
//		}
		Collections.addAll(mappingTo, getModelFilesSelector().getFiles());
		mappings.put(getLogFileSelector().getFile(), mappingTo);
		return mappings;
	}
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(new Color(193, 21, 2));
		g2.fillOval(this.getWidth() - 16, 0, 16, 16);
		// g2.fillRect(this.getWidth() - 16, 0, 16, 16);
		g2.fillRect(this.getWidth() - 16, 0, 16, 8);
		g2.fillRect(this.getWidth() - 8, 0, 8, 16);
		g2.setColor(Color.WHITE);
		g2.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND,
				BasicStroke.JOIN_ROUND));
		g2.drawLine(this.getWidth() - 5, 5, this.getWidth() - 11, 11);
		g2.drawLine(this.getWidth() - 5, 11, this.getWidth() - 11, 5);
	}
}