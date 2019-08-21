package com.xiabuxiabu.storemanage.service.cceuipservice;

import com.xiabuxiabu.storemanage.entity.ccequip.ccEquip;
import com.xiabuxiabu.storemanage.repository.ccequip.CCEquipRepository;
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
public class CCEquipService {
    @Autowired
    private CCEquipRepository ccEquipRepository;
    public Page<ccEquip> findAll(int page,int size,String values){
        Sort sort = new Sort(Sort.Direction.ASC, "equipId");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        if(values!=null){
            return ccEquipRepository.findAll(new Specification<ccEquip>() {
                @Override
                public Predicate toPredicate(Root<ccEquip> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    //根据特殊设备的名称进行查询
                    Path<String> name = root.get("name");
                    Predicate p1 = criteriaBuilder.like(name,"%"+values+"%");
                    criteriaQuery.where(p1);
                    return null;
                }
            },pageable);

        }
        return ccEquipRepository.findAll(pageable);
    }
    public ccEquip save(ccEquip ccEquip){
        return ccEquipRepository.save(ccEquip);
    }
    public  ccEquip findByName(String name){
        return ccEquipRepository.findByName(name);
    }
    public ccEquip findById(int id){
        return ccEquipRepository.findById(id).get();
    }
    public List<ccEquip> findAll(){
        return  ccEquipRepository.findAll();
    }

}
