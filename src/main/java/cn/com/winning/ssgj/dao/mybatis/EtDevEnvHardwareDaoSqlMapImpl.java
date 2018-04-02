package cn.com.winning.ssgj.dao.mybatis;

import org.springframework.stereotype.Service;

import cn.com.winning.ssgj.dao.EtDevEnvHardwareDao;
import cn.com.winning.ssgj.domain.EtDevEnvHardware;
import cn.com.winning.ssgj.dao.mybatis.EntityDaoSqlMapImpl;

import java.util.List;

/**
 *
 *
 * @author SSGJ
 * @date 2018-01-18 10:11:47
 */
@Service
public class EtDevEnvHardwareDaoSqlMapImpl extends EntityDaoSqlMapImpl<EtDevEnvHardware> implements EtDevEnvHardwareDao {

    @Override
    public List<EtDevEnvHardware> selectEtDevEnvHardwareMergeList(EtDevEnvHardware etDevEnvHardware) {
        return super.getSqlSession().selectList("selectEtDevEnvHardwareMergeList",etDevEnvHardware);
    }

    @Override
    public void insertEtDevEnvHardwareByList(List<EtDevEnvHardware> etDevEnvHardwares) {
        super.getSqlSession().insert("insertEtDevEnvHardwareByList",etDevEnvHardwares);
    }
}
