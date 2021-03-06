package cn.com.winning.ssgj.service;

import java.util.List;

import cn.com.winning.ssgj.domain.EtSiteInstall;

/**
 *
 *
 * @author SSGJ
 * @date 2018-01-18 10:11:48
 */
public interface EtSiteInstallService {

    Integer createEtSiteInstall(EtSiteInstall t);

    int modifyEtSiteInstall(EtSiteInstall t);

    int removeEtSiteInstall(EtSiteInstall t);

    EtSiteInstall getEtSiteInstall(EtSiteInstall t);

    List<EtSiteInstall> getEtSiteInstallList(EtSiteInstall t);

    Integer getEtSiteInstallCount(EtSiteInstall t);

    List<EtSiteInstall> getEtSiteInstallPaginatedList(EtSiteInstall t);

    List<EtSiteInstall> getEtSiteInstallNameList(EtSiteInstall info);

    List<EtSiteInstall> getEtSiteInstallListWithSum(EtSiteInstall entity);

    List<EtSiteInstall> getEtSiteInstallListWithInfo(EtSiteInstall info);

    void getNerateSiteIntallExcel(EtSiteInstall info, String path);

    List<EtSiteInstall> getEtSiteInstallGroupPuser(EtSiteInstall info);

    void createEtSiteInstallDeptInfo(List<List<Object>> deptList, EtSiteInstall info);
}