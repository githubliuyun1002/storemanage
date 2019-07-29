package com.xiabuxiabu.storemanage.controller.store;

import com.xiabuxiabu.storemanage.controller.equip.ClassficationController;
import com.xiabuxiabu.storemanage.entity.equip.Classification;
import com.xiabuxiabu.storemanage.entity.equip.EquipName;
import com.xiabuxiabu.storemanage.entity.equip.Item;
import com.xiabuxiabu.storemanage.entity.equip.Items;
import com.xiabuxiabu.storemanage.entity.store.Store;
import com.xiabuxiabu.storemanage.publicutils.ExcelTool;
import com.xiabuxiabu.storemanage.publicutils.TitleEntity;
import com.xiabuxiabu.storemanage.service.equip.ClassificationService;
import com.xiabuxiabu.storemanage.service.equip.ItemService;
import com.xiabuxiabu.storemanage.service.store.StoreService;
import net.bytebuddy.asm.Advice;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.Column;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.lang.System.out;
import static java.lang.System.setOut;

/**
 * 出报表的文件Controller
 */
@Controller
@RequestMapping("/report")
public class ReportController {
    @Autowired
    private StoreService storeService;
    @Autowired
    private ClassificationService classificationService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;
    @Autowired
    private BaseController baseController;
    /*
      已经确认的设备，生成门店列表的展示形式json格式，形成报表的展示格式
     */
    @RequestMapping("/reportTable")
    public String reportTable(){
        return "/store/reporttable";
    }
    @RequestMapping("/findAll")
    @ResponseBody
    public Map<String,Object> findAllClass(int page,int pageSize,String searchName){
        Map<String,Object> map = new HashMap<>();
        /**
         * 设置页面按钮的
         */

        map.put("classitem",classificationService.findAll());
        Page<Store> storePage = storeService.findByRepostList(page,pageSize,searchName);
        //需要生成报表的门店的信息
        List<Store> reportList = storePage.getContent();
        String store="[";
        System.out.println("reportSize---->"+reportList.size());
        for (int i = 0; i <reportList.size() ; i++) {
            Store storeIndex = reportList.get(i);
            int index = 1;
            store+="{\"storeId\":\""+storeIndex.getStoreId()+"\",\"storeCode\":\""+storeIndex.getStoreCode()+"\",\"storeName\":\""+storeIndex.getStoreName()+"\",\"address\":\""+storeIndex.getAddress()+"\"," +
                    "\"marger\":\""+storeIndex.getMarger()+"\"," +
                    "\"openDate\":\""+storeIndex.getOpenDate()+"\"," +
                    "\"marketName\":\""+storeIndex.getMarketName()+"\"," +
                    "\"items\":{" ;
            for (Items items:storeIndex.getItemsSet()) {
                store += "\""+items.getItem().getName()+"\":"+ items.getNum();
                if(index!=storeIndex.getItemsSet().size()){
                    store+=",";
                }
                index++;
            }
            store+="}}";
            if(i!=reportList.size()-1){
                store+=",";
            }

        }
        store+="]";
        map.put("store",store);
        map.put("page",storePage);
        return  map;
    }
    @RequestMapping("/outputExcel")
    @ResponseBody
    public ResponseEntity<FileSystemResource> outputExcel(int page,int pageSize,String searchName){
        //填充的页面中表头的数据
        Map<String,Object> map = new HashMap<>();
        List<TitleEntity> titleList=new ArrayList<>();
        TitleEntity titleEntity_bt=new TitleEntity("0",null,"门店列表展示页面",null);
        TitleEntity titleEntity_storeCode=new TitleEntity("1","0","门店编码","storeCode");
        TitleEntity titleEntity_storeName=new TitleEntity("2","0","门店名称","storeName");

        titleList.add(titleEntity_bt);
        titleList.add(titleEntity_storeCode);
        titleList.add(titleEntity_storeName);

        List<Classification> classificationServiceAll = classificationService.findAll();
        for (int i = 0; i < classificationServiceAll.size(); i++) {
            Classification classification = classificationServiceAll.get(i);
            TitleEntity titleEntity_top = new TitleEntity(String.valueOf(classification.getClassId()),"0",classification.getName(),classification.getName());
            // System.out.println("titleEntity_top---->"+titleEntity_top);
            titleList.add(titleEntity_top);
            List<EquipName> equipNameList = new ArrayList<>(classification.getEquipNames());
            for (int j = 0; j <equipNameList.size() ; j++) {
                EquipName equipName = equipNameList.get(j);
                TitleEntity titleEntity_mid = new TitleEntity(String.valueOf(equipName.getEquipId()),String.valueOf(classification.getClassId()),equipName.getName(),equipName.getName());
//                System.out.println("titleEntity_mid----->"+titleEntity_mid);
                titleList.add(titleEntity_mid);
                List<Item> itemList = new ArrayList<>(equipName.getItemSet());
                for (int k = 0; k <itemList.size() ; k++) {
                    Item item = itemList.get(k);
                    TitleEntity titleEntity_foot = new TitleEntity(String.valueOf(item.getItemId()),String.valueOf(equipName.getEquipId()),item.getName(),item.getName());
                    // System.out.println("titleEntity_foot----->"+titleEntity_foot);
                    titleList.add(titleEntity_foot);
                }
            }
        }

        //单级的 行内数据页面中表格中的数据
        List<Map<String,String>> rowList=new ArrayList<>();
        //按照搜索按钮选选择的数据填充页面数据
        List<Store> storeList = storeService.findByRepostList(page,pageSize,searchName).getContent();
        for (int i = 0; i <storeList.size() ; i++) {
            Map mapContent= new HashMap<String,String>();
            Store store = storeList.get(i);
            List<Items> itemsList = new ArrayList<>(store.getItemsSet());
            List<Item> itemList = itemService.findAll();
            //设备开始填充0
            for (int j = 0; j <itemList.size() ; j++) {
                mapContent.put(itemList.get(j).getName(),0);
            }
            //门店有该设备，填充数量
            for (int j = 0; j < itemsList.size(); j++) {
                Items items = itemsList.get(j);
                mapContent.put("storeCode",store.getStoreCode());
                mapContent.put("storeName",store.getStoreName());
                mapContent.put(items.getItem().getName(),items.getNum());
            }
            rowList.add(mapContent);

        }
        ExcelTool excelTool = new ExcelTool("门店设备清单列表",20,20);
        List<Column>  titleData = null;
        try {
            titleData = excelTool.columnTransformer(titleList,"t_id","t_pid","t_content","t_fielName","0");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //##############################################
        FileOutputStream os =null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String excelTitle = "门店设备清单"+ simpleDateFormat.format(new Date())+".xls";
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
        return baseController.export(file);

    }


}
