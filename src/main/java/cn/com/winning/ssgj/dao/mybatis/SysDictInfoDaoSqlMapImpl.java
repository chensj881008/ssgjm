package cn.com.winning.ssgj.dao.mybatis;

import org.springframework.stereotype.Service;

import cn.com.winning.ssgj.dao.SysDictInfoDao;
import cn.com.winning.ssgj.domain.SysDictInfo;
import cn.com.winning.ssgj.dao.mybatis.EntityDaoSqlMapImpl;

import java.util.List;

/**
 *
 *
 * @author SSGJ
 * @date 2018-01-18 10:11:47
 */
@Service
public class SysDictInfoDaoSqlMapImpl extends EntityDaoSqlMapImpl<SysDictInfo> implements SysDictInfoDao {

    @Override
    public List<SysDictInfo> selectEntityListBySelectiveKeyForAnd(SysDictInfo dict) {
        String statement = "selectEntityListBySelectiveKeyForAnd";
        return super.getSqlSession().selectList(statement,dict);
    }

    @Override
    public Integer selectEntityCountBySelectiveKeyForAnd(SysDictInfo dict) {
        String statement = "selectEntityCountBySelectiveKeyForAnd";
        return  super.getSqlSession().selectOne(statement,dict);
    }

    @Override
    public List<SysDictInfo> selectEntityListBySelectiveKeyForOr(SysDictInfo dict) {
        String statement = "selectEntityListBySelectiveKeyForOr";
        return super.getSqlSession().selectList(statement,dict);
    }

    @Override
    public Integer selectEntityCountBySelectiveKeyForOr(SysDictInfo dict) {
        String statement = "selectEntityCountBySelectiveKeyForOr";
        return  super.getSqlSession().selectOne(statement,dict);
    }

    @Override
    public List<SysDictInfo> selectSysDictInfoListByType(SysDictInfo dict) {
        return  super.getSqlSession().selectList("selectSysDictInfoListByType",dict);
    }

    @Override
    public List<SysDictInfo> selectSysDictInfoListByValue(SysDictInfo dict) {
        return  super.getSqlSession().selectList("selectSysDictInfoListByValue",dict);
    }

    @Override
    public List<SysDictInfo> selectSysDictInfoListBySelectKey(SysDictInfo info) {
        return  super.getSqlSession().selectList("selectSysDictInfoListBySelectKey",info);
    }

    @Override
    public List<SysDictInfo> selectSysDictInfoListByDesc(SysDictInfo dict) {
        return  super.getSqlSession().selectList("selectSysDictInfoListByDesc",dict);
    }
}
