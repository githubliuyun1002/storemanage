package com.xiabuxiabu.storemanage.controller.store;

import com.xiabuxiabu.storemanage.entity.store.Store;
import com.xiabuxiabu.storemanage.entity.store.StoreRemarks;
import com.xiabuxiabu.storemanage.publicutils.ExcelTool;
import com.xiabuxiabu.storemanage.publicutils.TitleEntity;
import com.xiabuxiabu.storemanage.service.store.StoreRemarksService;
import com.xiabuxiabu.storemanage.service.store.StoreService;
import net.bytebuddy.asm.Advice;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.Column;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 门店设备修改日志查询history
 */
@Controller
@RequestMapping("/record")
public class RecordController {
    @Autowired
    private StoreRemarksService storeRemarksService;
    @Autowired
    private BaseController baseController;

    /**
     * 设备修改历史表的展示页面
     * @return
     */
    @RequestMapping("/home")
    public String home(){
        return "/store/itemhistory";
    }
    @RequestMapping("/itemHistoryList")
    @ResponseBody
    public Page<StoreRemarks> itemHistory(@RequestParam("page")int page, @RequestParam("pageSize") int pageSize, @RequestParam("searchName") String searchName){
        return  storeRemarksService.findAll(page,pageSize,searchName);

    }
    //outputExcel
    @RequestMapping("/outputExcel")
    @ResponseBody
    public ResponseEntity<FileSystemResource> outputExcel(int page, int pageSize, String searchName){
        //填充的页面中表头的数据
        Map<String,Object> map = new HashMap<>();
        List<TitleEntity> titleList=new ArrayList<>();
        TitleEntity titleEntity_bt=new TitleEntity("0",null,"门店设备日志展示页面",null);
        TitleEntity titleEntity_storeName=new TitleEntity("1","0","门店名称","storeName");
        TitleEntity titleEntity_storeCode=new TitleEntity("2","0","门店编码","storeCode");
        TitleEntity titleEntity_marketName=new TitleEntity("3","0","市场名称","marketName");
        TitleEntity titleEntity_itemName=new TitleEntity("4","0","设备名称","itemName");
        TitleEntity titleEntity_operatePerson=new TitleEntity("5","0","操作人","operatePerson");
        TitleEntity titleEntity_updateTime=new TitleEntity("6","0","操作时间","updateTime");
        TitleEntity titleEntity_checkMan=new TitleEntity("7","0","审批人","checkMan");
        TitleEntity titleEntity_checkTime=new TitleEntity("8","0","审批时间","checkTime");
        TitleEntity titleEntity_remarks=new TitleEntity("9","0","审批时间","remarks");


        titleList.add(titleEntity_bt);
        titleList.add(titleEntity_storeCode);
        titleList.add(titleEntity_storeName);
        titleList.add(titleEntity_marketName);
        titleList.add(titleEntity_itemName);
        titleList.add(titleEntity_operatePerson);
        titleList.add(titleEntity_updateTime);
        titleList.add(titleEntity_checkMan);
        titleList.add(titleEntity_checkTime);
        titleList.add(titleEntity_remarks);

        //单级的 行内数据页面中表格中的数据
        List<Map<String,String>> rowList=new ArrayList<>();
        List<StoreRemarks> storeRemarksList = storeRemarksService.findAll(page,pageSize,searchName).getContent();
        for (int i = 0; i <storeRemarksList.size() ; i++) {
            Map mapContent= new HashMap<String,String>();
            StoreRemarks storeRemarks = storeRemarksList.get(i);
            mapContent.put("storeName",storeRemarks.getStoreName());
            mapContent.put("storeCode",storeRemarks.getStoreCode());
            mapContent.put("marketName",storeRemarks.getMarketName());
            mapContent.put("itemName",storeRemarks.getItemName());
            mapContent.put("operatePerson",storeRemarks.getOperatePerson());
            mapContent.put("updateTime",storeRemarks.getUpdateTime());
            mapContent.put("checkMan",storeRemarks.getCheckMan());
            mapContent.put("checkTime",storeRemarks.getCheckTime());
            mapContent.put("remarks","数量由"+storeRemarks.getOrignnum()+"变为"+storeRemarks.getNownum()+"变化量为"+storeRemarks.getChangenum());
            rowList.add(mapContent);
        }
        ExcelTool excelTool = new ExcelTool("门店设备日志列表",20,20);
        List<Column>  titleData = null;
        try {
            titleData = excelTool.columnTransformer(titleList,"t_id","t_pid","t_content","t_fielName","0");
        } catch (Exception e) {
            e.printStackTrace();
        }
        FileOutputStream os =null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String excelTitle = "门店设备日志列表"+ simpleDateFormat.format(new Date())+".xls";
        try {
            os = new FileOutputStream("output/"+excelTitle);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            HSSFWorkbook hssfWorkbook = excelTool.exportWorkbook(titleData, rowList, true);
            hssfWorkbook.write(os);
            os.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (os != null) {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        File file = new File("output/"+excelTitle);

        return baseController.exportRecord(file);
    }

    @RequestMapping("/outputAll")
    @ResponseBody
    public ResponseEntity<FileSystemResource> outputAll(){
        //填充的页面中表头的数据
        Map<String,Object> map = new HashMap<>();
        List<TitleEntity> titleList=new ArrayList<>();
        TitleEntity titleEntity_bt=new TitleEntity("0",null,"门店设备日志展示页面",null);
        TitleEntity titleEntity_storeName=new TitleEntity("1","0","门店名称","storeName");
        TitleEntity titleEntity_storeCode=new TitleEntity("2","0","门店编码","storeCode");
        TitleEntity titleEntity_marketName=new TitleEntity("3","0","市场名称","marketName");
        TitleEntity titleEntity_itemName=new TitleEntity("4","0","设备名称","itemName");
        TitleEntity titleEntity_operatePerson=new TitleEntity("5","0","操作人","operatePerson");
        TitleEntity titleEntity_updateTime=new TitleEntity("6","0","操作时间","updateTime");
        TitleEntity titleEntity_checkMan=new TitleEntity("7","0","审批人","checkMan");
        TitleEntity titleEntity_checkTime=new TitleEntity("8","0","审批时间","checkTime");
        TitleEntity titleEntity_remarks=new TitleEntity("9","0","审批时间","remarks");


        titleList.add(titleEntity_bt);
        titleList.add(titleEntity_storeCode);
        titleList.add(titleEntity_storeName);
        titleList.add(titleEntity_marketName);
        titleList.add(titleEntity_itemName);
        titleList.add(titleEntity_operatePerson);
        titleList.add(titleEntity_updateTime);
        titleList.add(titleEntity_checkMan);
        titleList.add(titleEntity_checkTime);
        titleList.add(titleEntity_remarks);

        //单级的 行内数据页面中表格中的数据
        List<Map<String,String>> rowList=new ArrayList<>();
        List<StoreRemarks> storeRemarksList = storeRemarksService.findAllList();
        for (int i = 0; i <storeRemarksList.size() ; i++) {
            Map mapContent= new HashMap<String,String>();
            StoreRemarks storeRemarks = storeRemarksList.get(i);
            mapContent.put("storeName",storeRemarks.getStoreName());
            mapContent.put("storeCode",storeRemarks.getStoreCode());
            mapContent.put("marketName",storeRemarks.getMarketName());
            mapContent.put("itemName",storeRemarks.getItemName());
            mapContent.put("operatePerson",storeRemarks.getOperatePerson());
            mapContent.put("updateTime",storeRemarks.getUpdateTime());
            mapContent.put("checkMan",storeRemarks.getCheckMan());
            mapContent.put("checkTime",storeRemarks.getCheckTime());
            mapContent.put("remarks","数量由"+storeRemarks.getOrignnum()+"变为"+storeRemarks.getNownum()+"变化量为"+storeRemarks.getChangenum());
            rowList.add(mapContent);
        }
        ExcelTool excelTool = new ExcelTool("门店设备日志列表",20,20);
        List<Column>  titleData = null;
        try {
            titleData = excelTool.columnTransformer(titleList,"t_id","t_pid","t_content","t_fielName","0");
        } catch (Exception e) {
            e.printStackTrace();
        }
        FileOutputStream os =null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String excelTitle = "门店设备日志列表"+ simpleDateFormat.format(new Date())+".xls";
        try {
            os = new FileOutputStream("output/"+excelTitle);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            HSSFWorkbook hssfWorkbook = excelTool.exportWorkbook(titleData, rowList, true);
            hssfWorkbook.write(os);
            os.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (os != null) {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        File file = new File("output/"+excelTitle);
        return baseController.exportRecord(file);


    }

}
