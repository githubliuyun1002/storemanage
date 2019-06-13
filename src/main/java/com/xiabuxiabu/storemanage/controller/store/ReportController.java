package com.xiabuxiabu.storemanage.controller.store;

import com.xiabuxiabu.storemanage.controller.equip.ClassficationController;
import com.xiabuxiabu.storemanage.entity.equip.Items;
import com.xiabuxiabu.storemanage.entity.store.Store;
import com.xiabuxiabu.storemanage.service.equip.ClassificationService;
import com.xiabuxiabu.storemanage.service.store.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Controller
@RequestMapping("/report")
public class ReportController {
    @Autowired
    private StoreService storeService;
    @Autowired
    private ClassificationService classificationService;
    /*
      已经确认的设备，生成门店列表的展示形式json格式，形成报表的展示格式
     */
    @RequestMapping("/reportTable")
    public String reportTable(){
        return "/store/reporttable";
    }
    @RequestMapping("/findAll")
    @ResponseBody
    public Map<String,Object> findAllClass(){
        Map<String,Object> map = new HashMap<>();
        map.put("classitem",classificationService.findAll());
        List<Store> reportList = storeService.findByStoreStatus();
        String store="[";
        System.out.println("reportSize---->"+reportList.size());
        for (int i = 0; i <reportList.size() ; i++) {
            Store storeIndex = reportList.get(i);
            int index = 1;
             store+="{\"storeId\":\""+storeIndex.getStoreId()+"\",\"storeCode\":\""+storeIndex.getStoreCode()+"\",\"storeName\":\""+storeIndex.getStoreName()+"\",\"address\":\""+storeIndex.getAddress()+"\"," +
                    "\"marger\":\""+storeIndex.getMarger()+"\"," +
                    "\"openDate\":\""+storeIndex.getOpenDate()+"\"," +
                   // "\"closeDate\":\""+$("#closeDate").val()+"\"," +
                    "\"marketCode\":{\"marketCode\":\""+storeIndex.getMarketCode().getMarketCode()+"\",\"name\":\""+storeIndex.getMarketCode().getName()+"\"}," +
                    "\"widthBand\":{\"wid\":\""+storeIndex.getWidthBand().getWid()+"\",\"payMoney\":\""+storeIndex.getWidthBand().getPayMoney()+"\",\"endDate\":\""+storeIndex.getWidthBand().getEndDate()+"\"," +
                                  "\"servicePerson\":{\"sid\":"+storeIndex.getWidthBand().getServicePerson().getSid()+",\"serviceName\":\""+storeIndex.getWidthBand().getServicePerson().getServiceName()+"\"}," +
                                  "\"accessMethod\":{\"aid\":"+storeIndex.getWidthBand().getAccessMethod().getAid()+",\"accessName\":\""+storeIndex.getWidthBand().getAccessMethod().getAccessName()+"\"}," +
                                  "\"payMethod\":{\"payid\":"+storeIndex.getWidthBand().getPayMethod().getPayid()+",\"method\":\""+storeIndex.getWidthBand().getPayMethod().getMethod()+"\"}," +
                                  "\"identity\":\""+storeIndex.getWidthBand().getIdentity()+"\",\"password\":\""+storeIndex.getWidthBand().getPassword()+"\",\"tapewidth\":\""+storeIndex.getWidthBand().getTapewidth()+"\"" +
                     "}," +
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
        System.out.println("store----->"+store);
        map.put("store",store);
        return map;
    }


}
