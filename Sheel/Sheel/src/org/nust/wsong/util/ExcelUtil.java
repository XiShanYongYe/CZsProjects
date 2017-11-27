package org.nust.wsong.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

/**
 * @description excel文件工具类
 * @author xxx
 * @since 2016-10-1
 */
public class ExcelUtil {
	/**
	 * 根据文件名 表头 创建一个Excel表格
	 * 
	 * @param file
	 * @param headerStr
	 */
	public static File createExcel(File file, List<String> headerStr) {
		if (file.exists())
			return file;
		else {
			FileOutputStream fos = null;
			HSSFWorkbook wb = null;
			try {
				file.createNewFile();
				wb = new HSSFWorkbook();
				HSSFSheet sheet = wb.createSheet("测试");
				HSSFRow row = sheet.createRow(0);
				HSSFCell cell;
				if (!headerStr.isEmpty()) {
					for (int i = 0; i < headerStr.size(); i++) {
						cell = row.createCell(i);
						cell.setCellValue(headerStr.get(i));
					}
				}

				fos = new FileOutputStream(file);
				wb.write(fos);

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					fos.close();
					wb.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
			return file;
		}
	}

	public static void append2File(File file, List<String> record) throws Exception {

		FileInputStream fs = new FileInputStream(file); // 获取d://test.xls
		POIFSFileSystem ps = new POIFSFileSystem(fs); // 使用POI提供的方法得到excel的信息
		HSSFWorkbook wb = new HSSFWorkbook(ps);
		HSSFSheet sheet = wb.getSheetAt(0); // 获取到工作表，因为一个excel可能有多个工作表
		HSSFRow row = sheet.getRow(0); // 获取第一行（excel中的行默认从0开始，所以这就是为什么，一个excel必须有字段列头），即，字段列头，便于赋值
		// System.out.println(sheet.getLastRowNum()+" "+row.getLastCellNum());
		// //分别得到最后一行的行号，和一条记录的最后一个单元格

		FileOutputStream out = new FileOutputStream(file); // 向d://test.xls中写数据
		row = sheet.createRow(sheet.getLastRowNum() + 1); // 在现有行号后追加数据
		for(int i=0;i<record.size();i++){
			row.createCell(i).setCellValue(record.get(i));
			
			
		}

		out.flush();
		wb.write(out);
		out.close();
//		System.out.println(row.getPhysicalNumberOfCells() + " " + row.getLastCellNum());

	}

}
