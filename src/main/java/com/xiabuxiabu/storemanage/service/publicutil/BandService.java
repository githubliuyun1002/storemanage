package com.xiabuxiabu.storemanage.service.publicutil;

import com.xiabuxiabu.storemanage.entity.publicutil.Band;
import com.xiabuxiabu.storemanage.repository.publicutil.BandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BandService {
    @Autowired
    private BandRepository bandRepository;
    public Band save(Band band){
        return bandRepository.save(band);
    }
    public List<Band> findAllBand(){
        return bandRepository.findAll();
    }
    public Band findByName(String name){
        return bandRepository.findByName(name);
    }
    public Band findById(int id){
        return bandRepository.findById(id).get();
    }
}
