package cn.com.winning.ssgj.dao.mybatis;

import org.springframework.stereotype.Service;

import cn.com.winning.ssgj.dao.EtUserInfoDao;
import cn.com.winning.ssgj.domain.EtUserInfo;
import cn.com.winning.ssgj.dao.mybatis.EntityDaoSqlMapImpl;

import java.util.List;

/**
 * @author SSGJ
 * @date 2018-01-18 10:11:47
 */
@Service
public class EtUserInfoDaoSqlMapImpl extends EntityDaoSqlMapImpl<EtUserInfo> implements EtUserInfoDao {

    @Override
    public EtUserInfo getAllEtUserInfo(EtUserInfo t) {
        return getSqlSession().selectOne("getAllEtUserInfo", t);
    }
}
