package com.xiabuxiabu.storemanage.service.equip;

import com.xiabuxiabu.storemanage.entity.equip.Item;
import com.xiabuxiabu.storemanage.repository.equip.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.List;

@Service
public class ItemService {
    @Autowired
    private ItemRepository itemRepository;
    public List<Item> findAll(){
        return  itemRepository.findAll();
    }

    public Item save(Item item) {
        return itemRepository.save(item);
    }
    public Item findById(int id){

        return itemRepository.findById(id).get();
    }

    public Page<Item> findAllItem(int page,int size,String value){
        Sort sort = new Sort(Sort.Direction.ASC, "itemId");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        if(value!=null){
            return itemRepository.findAll(new Specification<Item>() {
                @Override
                public Predicate toPredicate(Root<Item> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    Path<String> className = root.get("className");
                    Path<String> equipName = root.get("equipName");
                    Path<String> name  = root.get("name");
                    Predicate p1 = criteriaBuilder.like(equipName, "%" + value + "%");
                    Predicate p2 = criteriaBuilder.like(className, "%" + value + "%");
                    Predicate p3 = criteriaBuilder.like(name, "%" + value + "%");
                    Predicate p = criteriaBuilder.or(p1,p2,p3);
                    criteriaQuery.where(p);
                    criteriaQuery.distinct(true);
                    return null;
                }
            },pageable);
        }
        return  itemRepository.findAll(pageable);
    }
    public Item findByName(String name,String equipName){
        return itemRepository.findByName(name,equipName);
    }

}
