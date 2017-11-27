package org.nust.wsong.test;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.nust.wsong.util.CSVUtils;


/**
 * CSV操作(导出和导入)
 * 
 * @author 林计钦
 * @version 1.0 Jan 27, 2014 4:17:02 PM
 */
public class CSVTest {

    /**
     * CSV导出
     * 
     * @throws Exception
     */
    @Test
    public void exportCsv() {
        List<String> list=new ArrayList<String>();
        list.add("1,张三,男");
        list.add("2,李四,男");
        list.add("3,小红,女");
        boolean isSuccess=CSVUtils.exportCsv(new File("D:/1.csv"), list);
        System.out.println(isSuccess);
    }
    
    /**
     * CSV导出
     * 
     * @throws Exception
     */
    @Test
    public void importCsv()  {
        List<String> dataList=CSVUtils.importCsv(new File("D:/1.csv"));
        if(dataList!=null && !dataList.isEmpty()){
            for(String data : dataList){
                System.out.println(data);
            }
        }
    }
    
    
}