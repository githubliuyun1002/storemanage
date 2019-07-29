package com.xiabuxiabu.storemanage.service.store;

import com.xiabuxiabu.storemanage.entity.store.WidthBand;
import com.xiabuxiabu.storemanage.repository.store.WidthBandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WidthBandService {
    @Autowired
    private WidthBandRepository widthBandRepository;
    public WidthBand save(WidthBand widthBand){
        return  widthBandRepository.save(widthBand);
    }
    public WidthBand findById(int id){
        return widthBandRepository.findById(id).get();
    }
    public void  deleteById(int id){
        widthBandRepository.deleteById(id);
    }
}
