package org.nust.wsong.test;

import java.io.File;
import java.io.IOException;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class TestJXL {
	public static void main(String[] args) {
		File file = new File("1.xls");
		
		
		try {
			WritableWorkbook workbook = Workbook.createWorkbook(file);
			WritableSheet sheet = workbook.createSheet("测试", 1);
			
			sheet.addCell(new Cell);
			WritableCell
			workbook.write();
			
			
			
			
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
	}
	 public void CreateWorkbook(File file,int i) {
	        try {
	            if (!file.exists()) { //判断文件是否已存在，如果没有存在则创建新文件
	                WritableWorkbook wwb = Workbook.createWorkbook(new File("result.xls"));
	                WritableSheet ws = wwb.createSheet("Test Sheet 1", 0);
	                int i = 0;
	                ws.setColumnView(0, 20); //设置列宽
	                jxl.write.NumberFormat nf = new jxl.write.NumberFormat(
	                        "0.0000000000000000"); //定义数值格式
	                WritableCellFormat wcfN = new WritableCellFormat(nf);
	                String str2 = "第" + 1 + "次试验";
	                Label label = new Label(0, 0, str2);
	                ws.addCell(label);
	                ws.addCell(i);
	                while (i < a.length) {
	                    jxl.write.Number num = new jxl.write.Number(0, i + 1,
	                            a[i], wcfN);
	                    ws.addCell(num);
	                    i++;
	                }
	                //写入Exel工作表
	                wwb.write();
	                //关闭Excel工作薄对象
	                wwb.close();
	            } else {
	                Workbook rwb = Workbook.getWorkbook(file);
	                File tempfile = new File(System.getProperty("user.dir") +
	                                         "\\tempfile.xls");
	                WritableWorkbook wwb = Workbook.createWorkbook(tempfile, rwb);
	                WritableSheet ws = wwb.getSheet(0);
	                int num = rwb.getSheet(0).getColumns();
	                int num1 = num + 1;
	                ws.setColumnView(num, 20); //设置列宽
	                String str2 = "第" + num1 + "次试验"; //添加列名
	                Label label = new Label(num, 0, str2);
	                ws.addCell(label);
	                int i = 0;
	                jxl.write.NumberFormat nf = new jxl.write.NumberFormat(
	                        "0.000000000000000"); //定义数值格式
	                WritableCellFormat wcfN = new WritableCellFormat(nf);
	                while (i < a.length) {
	                    jxl.write.Number number = new jxl.write.Number(num,
	                            i + 1,
	                            a[i], wcfN);
	                    ws.addCell(number);
	                    i++;
	                }
	                wwb.write();
	                wwb.close();
	                rwb.close();
	                String filename = file.getPath();
	                System.out.println("filename:" + filename);
	                file.delete();
	                tempfile.renameTo(file);
	                System.out.println("tempfile:" + tempfile.getPath());
	                System.out.println(tempfile.exists());
	                System.out.println(file.exists());
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
}
