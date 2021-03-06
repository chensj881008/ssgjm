package cn.com.winning.ssgj.service.impl;

import java.util.List;

import javax.annotation.Resource;


import cn.com.winning.ssgj.base.util.StringUtil;
import org.springframework.stereotype.Service;

import cn.com.winning.ssgj.dao.EtCustomerDetailDao;
import cn.com.winning.ssgj.domain.EtCustomerDetail;
import cn.com.winning.ssgj.service.EtCustomerDetailService;

/**
 * @author SSGJ
 * @date 2018-01-18 10:11:48
 */
@Service
public class EtCustomerDetailServiceImpl implements EtCustomerDetailService {

    @Resource
    private EtCustomerDetailDao etCustomerDetailDao;


    public Integer createEtCustomerDetail(EtCustomerDetail t) {
        return this.etCustomerDetailDao.insertEntity(t);
    }


    public EtCustomerDetail getEtCustomerDetail(EtCustomerDetail t) {
        return this.etCustomerDetailDao.selectEntity(t);
    }


    public Integer getEtCustomerDetailCount(EtCustomerDetail t) {
        return (Integer) this.etCustomerDetailDao.selectEntityCount(t);
    }


    public List<EtCustomerDetail> getEtCustomerDetailList(EtCustomerDetail t) {
        return this.etCustomerDetailDao.selectEntityList(t);
    }


    public int modifyEtCustomerDetail(EtCustomerDetail t) {
        return this.etCustomerDetailDao.updateEntity(t);
    }


    public int removeEtCustomerDetail(EtCustomerDetail t) {
        return this.etCustomerDetailDao.deleteEntity(t);
    }


    public List<EtCustomerDetail> getEtCustomerDetailPaginatedList(EtCustomerDetail t) {
        return this.etCustomerDetailDao.selectEntityPaginatedList(t);
    }

    @Override
    public EtCustomerDetail getMergeEtCustomerDetail(EtCustomerDetail t) {
        t = this.etCustomerDetailDao.selectMergeEtCustomerDetail(t);
        String name = t.getPmName();
        String code = t.getPmCode();
        if( StringUtil.isEmptyOrNull(code)){
            String[] nameCode = name.split("_");
            t.setPmName(nameCode[1]);
            t.setPmCode(nameCode[0]);
        }
        return t;
    }

}
