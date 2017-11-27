package org.nust.wsong.test;

import java.io.File;  
import java.io.IOException;  
import java.text.SimpleDateFormat;  
import java.util.Date;  
  
import jxl.Sheet;  
import jxl.Workbook;  
import jxl.read.biff.BiffException;  
import jxl.write.Label;  
import jxl.write.WritableSheet;  
import jxl.write.WritableWorkbook;  
import jxl.write.WriteException;  
import jxl.write.biff.RowsExceededException;  
public class WriteExcel {  
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
  
    private static File hasFile;  
  
    /** 
     * 同步操作，防止并发。 
     *  
     * @param args 
     * @return 
     * @throws IOException 
     * @throws RowsExceededException 
     * @throws WriteException 
     */  
    public synchronized static String[] write(String[] args)  
            throws IOException, RowsExceededException, WriteException {  
  
        // 文件路径  
        // 判断文件是否存在，如果存在就不创建，追加，如果不存在则创建文件并追加。  
        WritableWorkbook book = Workbook.createWorkbook(getHasFile());  
        book.setProtected(true);  
        // -- 第一个参数是Sheet名，第二个参数是Sheet下标  
        // -- 下标是整数，只起标识作用，建立的时候会以create顺序建立，本例生成的EXCEL文件第一个Sheet是sheet1  
        WritableSheet sheet = book.createSheet("测试", 1);   
        String[] title = { "支付宝交易号", "订单号", "交易总金额", "商品名称/订单名称", "商品描述/订单备注",  
                "买家支付宝账号", "交易状态", "sign", "交易时间" };  
        for (int i = 0; i < title.length; i++) {  
            Label lable = new Label(i, 0, title[i]);  
            sheet.addCell(lable);  
        }  
        // 初次创建，写入一行。  
        for (int i = 0; i < title.length; i++) {  
            Label lable = new Label(i, 1, args[i]);  
            sheet.addCell(lable);  
        }  
        // 每次写入数据时，写到最后一行。  
        book.write();  
        book.close();  
        System.out.println("写入成功");  
        return null;  
    }  
  
    /** 
     * 追加excel 
     *  
     * @param args 
     * @throws IOException 
     * @throws BiffException 
     * @throws WriteException 
     * @throws RowsExceededException 
     */  
    public static void addExcel(File file, String[] args) throws Exception {  
        Workbook book = Workbook.getWorkbook(file);  
        Sheet sheet = book.getSheet(1);  
        // 获取行  
        int length = sheet.getRows();  
        System.out.println(length);  
        
        WritableWorkbook wbook = Workbook.createWorkbook(file, book); // 根据book创建一个操作对象  
        WritableSheet sh = wbook.getSheet(0);// 得到一个工作对象  
        // 从最后一行开始加  
        for (int i = 0; i < args.length; i++) {  
            Label label = new Label(i, length, args[i]);  
            sh.addCell(label);  
        }  
        wbook.write();  
        wbook.close();  
    }  
  
    /** 
     * 判断文件是否已经写入 
     *  
     * @param filename 
     * @return 
     */  
    public static boolean filecheck(String filename) {  
        boolean flag = false;  
        File file = new File(filename);  
        if (file.exists()) {  
            flag = true;  
        }  
        setHasFile(file);  
        return flag;  
    }  
  
    /** 
     * 不管神马类型，都转换成string 
     *  
     * @param obj 
     * @return 
     */  
    public static String converToString(Object obj) {  
        return obj.toString();  
    }  
  
    public static void main(String[] args) throws RowsExceededException,  
            WriteException, IOException, BiffException {  
        String filepath = WriteExcel.class.getResource("/").getPath()  
                + sdf.format(new Date()) + ".xls";  
        String[] str = { "20101020102032032", "2012203203232032032", "50",  
                "100元朗识币", "这个订单没有备注", "1234566@163.com", "STATU_SUCCESS",  
                "ssdhfksdhfksdjhfkshdsdlfd", sdf.format(new Date()) };  
        boolean has = WriteExcel.filecheck(filepath);  
        // 如果存在  
        if (has)
			try {
				addExcel(getHasFile(), str);
			} catch (Exception e) {
				e.printStackTrace();
			}
		else {  
            write(str);  
        }  
  
    }  
  
    /** 
     * @return the hasFile 
     */  
    public static File getHasFile() {  
        return hasFile;  
    }  
  
    /** 
     * @param hasFile 
     *            the hasFile to set 
     */  
    public static void setHasFile(File hasFile) {  
        WriteExcel.hasFile = hasFile;  
    }  
  
}  
