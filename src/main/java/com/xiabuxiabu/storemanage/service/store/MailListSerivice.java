package com.xiabuxiabu.storemanage.service.store;

import com.xiabuxiabu.storemanage.entity.publicutil.MarketEntity;
import com.xiabuxiabu.storemanage.entity.store.MailList;
import com.xiabuxiabu.storemanage.entity.store.Store;
import com.xiabuxiabu.storemanage.entity.user.User;
import com.xiabuxiabu.storemanage.repository.store.MailListRepository;
import com.xiabuxiabu.storemanage.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MailListSerivice {
    @Autowired
    private MailListRepository mailListRepository;
    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    private UserService userService;
    public MailList findById(int id){
        return mailListRepository.findById(id).get();
    }
    public MailList save(MailList mailList){
        return mailListRepository.save(mailList);
    }
    public List<MailList> findAll(){
        return mailListRepository.findAll();
    }
    public Page<MailList> findAllPage(int page,int size,String values){
        Sort sort = new Sort(Sort.Direction.ASC,"id");
        Pageable pageable = PageRequest.of(page-1,size,sort);
        if(values!=null){
            return mailListRepository.findAll(new Specification<MailList>() {
                @Override
                public Predicate toPredicate(Root<MailList> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    Path<String>  marketNameMail = root.get("marketName");
                    //拿到登录人的市场信息
                    String userName = (String) httpServletRequest.getSession().getAttribute("userName");
                    User user = userService.findByUserName(userName);
                    String marketName = user.getMarketName();
                    if(marketName!="总部"){
                        Predicate marketInfo = criteriaBuilder.equal(marketNameMail, marketName);
                        criteriaQuery.where(marketInfo);
                    }
                    return null;
                }
            },pageable);
        }
        return mailListRepository.findAll(pageable);
    }
    public Map<String,Object>  findAllMap(int page,int size,String values){
        Map<String,Object> map = new HashMap<>();
        Sort sort = new Sort(Sort.Direction.ASC,"id");
        Pageable pageable = PageRequest.of(page-1,size,sort);
        String userName = (String) httpServletRequest.getSession().getAttribute("userName");
        User user = userService.findByUserName(userName);
        String userMail = user.getMail();
        if(values!=null){
            Page<MailList> all = mailListRepository.findAll(new Specification<MailList>() {
                @Override
                public Predicate toPredicate(Root<MailList> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    Path<String> marketNameMail = root.get("marketName");
                    //拿到登录人的市场信息
                    String marketName = user.getMarketName();

                    if (marketName != "总部") {
                        Predicate marketInfo = criteriaBuilder.equal(marketNameMail, marketName);
                        criteriaQuery.where(marketInfo);
                    }
                    return null;
                }
            }, pageable);
            map.put("pageDate",all);
            map.put("userMail",userMail);
        }else{
            Page<MailList> all = mailListRepository.findAll(pageable);
            map.put("pageDate",all);
            map.put("userMail",userMail);
        }
        return map;
    }

    public MailList findByStoreCode(String storeCode){
        return mailListRepository.findByStoreCode(storeCode);
    }
    public List<String> marketList(){
        return mailListRepository.marketList();
    }
    public List<MailList> findByMarketName(String marketName){
        return mailListRepository.findByMarketName(marketName);
    }

}
