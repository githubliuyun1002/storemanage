package com.xiabuxiabu.storemanage.service.store;

import com.xiabuxiabu.storemanage.entity.store.DBFile;
import com.xiabuxiabu.storemanage.repository.store.DBFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DBFileService {
    @Autowired
    private DBFileRepository dbFileRepository;
    public DBFile save(DBFile dbFile){
        return  dbFileRepository.save(dbFile);
    }
    public DBFile findById(int fileId){
        return dbFileRepository.findById(fileId).get();
    }

}
