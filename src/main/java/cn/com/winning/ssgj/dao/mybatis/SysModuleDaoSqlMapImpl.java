package cn.com.winning.ssgj.dao.mybatis;

import cn.com.winning.ssgj.domain.SysRoleInfo;
import cn.com.winning.ssgj.domain.SysUserInfo;
import cn.com.winning.ssgj.domain.expand.ZTreeNode;
import org.springframework.stereotype.Service;

import cn.com.winning.ssgj.dao.SysModuleDao;
import cn.com.winning.ssgj.domain.SysModule;
import cn.com.winning.ssgj.dao.mybatis.EntityDaoSqlMapImpl;

import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author SSGJ
 * @date 2018-01-18 10:11:47
 */
@Service
public class SysModuleDaoSqlMapImpl extends EntityDaoSqlMapImpl<SysModule> implements SysModuleDao  {

    @Override
    public List<SysModule> selectSysModulePaginatedListFuzzy(SysModule module) {
        String statement = "selectSysModulePaginatedListFuzzy";
        return super.getSqlSession().selectList(statement,module);
    }

    @Override
    public int selectSysModuleCountFuzzy(SysModule module) {
        String statement = "selectSysModuleCountFuzzy";
        return super.getSqlSession().selectOne(statement,module);
    }

    @Override
    public int selectSysModuleMaxOrderValue() {
        String statement = "selectSysModuleMaxOrderValue";
        return super.getSqlSession().selectOne(statement);
    }

    @Override
    public List<SysModule> selectSysModuleDaoListForName(SysModule module) {
        String statement = "selectSysModuleDaoListForName";
        return super.getSqlSession().selectList(statement,module);
    }

    @Override
    public List<SysModule> selectUserParentMenuList(SysUserInfo sysUserInfo) {
        String statement = "selectUserParentMenuList";
        return super.getSqlSession().selectList(statement,sysUserInfo);
    }

    @Override
    public List<SysModule> selectUserChildMenuList(Map<String,Object> param) {
        String statement = "selectUserChildMenuList";
        return super.getSqlSession().selectList(statement,param);
    }

    @Override
    public List<SysModule> selectRoleParentMenuList(SysRoleInfo sysRoleInfo) {
        return super.getSqlSession().selectList("selectRoleParentMenuList",sysRoleInfo);
    }

    @Override
    public List<SysModule> selectRoleChildMenuList(Map<String, Object> param) {
        return super.getSqlSession().selectList("selectRoleChildMenuList",param);
    }

    @Override
    public List<ZTreeNode> selectSysModuleParentTree() {
        return super.getSqlSession().selectList("selectSysModuleParentTree",null);
    }

    @Override
    public List<ZTreeNode> selectSysModuleChildTree(SysModule module) {
        return super.getSqlSession().selectList("selectSysModuleChildTree",module);
    }

    @Override
    public List<ZTreeNode> selectSysModuleTree() {
        return super.getSqlSession().selectList("selectSysModuleTree",null);
    }

    @Override
    public List<String> selectBtnModuleListByModuleURL(SysModule module) {
        return super.getSqlSession().selectList("selectBtnModuleListByModuleURL",module);
    }
}
