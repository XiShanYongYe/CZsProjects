package org.nust.wsong;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.nust.wsong.UI.experiment.InputConfigurationPanel;
import org.nust.wsong.UI.experiment.LogPanel;
import org.nust.wsong.UI.experiment.RepairThread;
import org.nust.wsong.algorithm.Algorithm1;
import org.nust.wsong.model.Trace;
import org.nust.wsong.util.CSVUtils;
import org.nust.wsong.util.LogUtils;


/**
 * 修改并使用了cobefra的 InputConfigurationPanel用于文件的输入
 * @author xxx
 *
 */
public class start {
	public static void main(String[] args) {
		final JFrame frame = new JFrame("Sheel");
		frame.setSize(1000, 800);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		final InputConfigurationPanel inputConfigurationPanel = new InputConfigurationPanel();
		JScrollPane scrollPane = new JScrollPane(inputConfigurationPanel);
		frame.getContentPane().add(scrollPane,BorderLayout.CENTER);
		
		final ExecutorService singleThreadPool = Executors.newSingleThreadExecutor();
		
		Button repairBtn = new Button("start repair");
		
		final List<String> dataList = new ArrayList<>();
		
		
		repairBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("start recovery");
				List<LogPanel> logPanels = inputConfigurationPanel.getLogPanels();
				for(LogPanel panel :logPanels){
					Map<String, List<String>> mappings = panel.getMappings();
					for(Entry<String,List<String>> entry:mappings.entrySet()){
						String key = entry.getKey();
						for(String value:entry.getValue()){
							final File file1 = new File(key);
							final File file2 = new File(value);

							singleThreadPool.execute(
									new RepairThread(file1, file2, dataList)
							);
						}
					}
				}
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e2) {
					e2.printStackTrace();
				}
				
				while(true){
					if(!RepairThread.hasThreadRunning()){  
						break;  
					}  
					try {
						Thread.sleep(500);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
				
				System.out.println("所有修复线程已经结束");
				System.out.println(dataList);
				
				CSVUtils.exportCsv(new File("d://2.csv"), dataList);
				
			}
		});
		JPanel panel = new JPanel();
		panel.setBackground(Color.gray);
		panel.add(repairBtn);
		frame.getContentPane().add(panel,BorderLayout.NORTH);
		frame.getContentPane().revalidate();
	}
}
