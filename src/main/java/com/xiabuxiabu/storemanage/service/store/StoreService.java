package com.xiabuxiabu.storemanage.service.store;

import com.xiabuxiabu.storemanage.entity.store.Store;
import com.xiabuxiabu.storemanage.repository.store.StoreRepository;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;

@Service
public class StoreService {
    @Autowired
    private StoreRepository storeRepository;
    public Page<Store> findAll(int page,int size,String values){
        Sort sort = new Sort(Sort.Direction.ASC,"id");
        Pageable pageable = PageRequest.of(page-1,size,sort);
        if(values!=null){
            return storeRepository.findAll(new Specification<Store>() {
                @Override
                public Predicate toPredicate(Root<Store> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    Path<String> name = root.get("name");  //餐厅名称进行修改
                    criteriaQuery.where(criteriaBuilder.like(name,"%"+ values +"%"));
                    return null;
                }
            },pageable);
        }
        return storeRepository.findAll(pageable);
    }

    public Store findById(int id) {
        return storeRepository.findById(id).get();
    }
    public Store save(Store store){
        return  storeRepository.save(store);
    }
    /*public Store updateById(int id){
        return storeRepository.
    }*/
}
