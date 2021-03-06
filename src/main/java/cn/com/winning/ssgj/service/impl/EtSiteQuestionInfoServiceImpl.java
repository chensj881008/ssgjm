package cn.com.winning.ssgj.service.impl;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Resource;


import cn.com.winning.ssgj.base.exception.SSGJException;
import cn.com.winning.ssgj.base.helper.SSGJHelper;
import cn.com.winning.ssgj.base.util.DateUtil;
import cn.com.winning.ssgj.base.util.ExcelUtil;
import cn.com.winning.ssgj.base.util.StringUtil;
import cn.com.winning.ssgj.dao.EtTempQuestionInfoDao;
import cn.com.winning.ssgj.domain.EtTempQuestionInfo;
import cn.com.winning.ssgj.domain.MobileSiteQuestion;
import cn.com.winning.ssgj.domain.SysUserInfo;
import cn.com.winning.ssgj.service.SysUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.winning.ssgj.dao.EtSiteQuestionInfoDao;
import cn.com.winning.ssgj.domain.EtSiteQuestionInfo;
import cn.com.winning.ssgj.service.EtSiteQuestionInfoService;

/**
 * @author SSGJ
 * @date 2018-01-18 10:11:48
 */
@Service
public class EtSiteQuestionInfoServiceImpl implements EtSiteQuestionInfoService {

    @Resource
    private EtSiteQuestionInfoDao etSiteQuestionInfoDao;
    @Autowired
    private EtTempQuestionInfoDao etTempQuestionInfoDao;
    @Autowired
    private SSGJHelper ssgjHelper;
    @Autowired
    private SysUserInfoService sysUserInfoService;


    public Integer createEtSiteQuestionInfo(EtSiteQuestionInfo t) {
        return this.etSiteQuestionInfoDao.insertEntity(t);
    }


    public EtSiteQuestionInfo getEtSiteQuestionInfo(EtSiteQuestionInfo t) {
        return this.etSiteQuestionInfoDao.selectEntity(t);
    }


    public Integer getEtSiteQuestionInfoCount(EtSiteQuestionInfo t) {
        return (Integer) this.etSiteQuestionInfoDao.selectEntityCount(t);
    }


    public List<EtSiteQuestionInfo> getEtSiteQuestionInfoList(EtSiteQuestionInfo t) {
        return this.etSiteQuestionInfoDao.selectEntityList(t);
    }


    public int modifyEtSiteQuestionInfo(EtSiteQuestionInfo t) {
        return this.etSiteQuestionInfoDao.updateEntity(t);
    }


    public int removeEtSiteQuestionInfo(EtSiteQuestionInfo t) {
        return this.etSiteQuestionInfoDao.deleteEntity(t);
    }


    public List<EtSiteQuestionInfo> getEtSiteQuestionInfoPaginatedList(EtSiteQuestionInfo t) {
        return this.etSiteQuestionInfoDao.selectEntityPaginatedList(t);
    }

    public void updateEtSiteQuestionInfoImg(EtSiteQuestionInfo t) {
        this.etSiteQuestionInfoDao.updateEtSiteQuestionInfoImg(t);

    }

    @Override
    public List<EtSiteQuestionInfo> getEtSiteQuestionInfoUserTotal(EtSiteQuestionInfo t) {
        return this.etSiteQuestionInfoDao.selectEtSiteQuestionInfoUserTotal(t);
    }

    @Override
    public void generateEtSiteQuestionInfo(EtSiteQuestionInfo info, String path) {
        List<EtSiteQuestionInfo> infoList = this.etSiteQuestionInfoDao.selectEntityList(info);
        List<String> colList = new ArrayList<String>();
        colList.add("deptName");
        colList.add("sysName");
        colList.add("menuName");
        colList.add("requireNo");
        colList.add("questionType");
        colList.add("questionDesc");
        colList.add("operTypeString");
        colList.add("priorityString");
        colList.add("allocateUser");
        colList.add("create_name");
        colList.add("createTimeString");
        colList.add("isPmis");

        List<Map> dataList = new ArrayList<Map>();

        for (EtSiteQuestionInfo qinfo : infoList) {
            Map<String, String> dataMap = new HashMap<>();
            dataMap.put("deptName", qinfo.getMap().get("deptName").toString());
            dataMap.put("sysName", qinfo.getMap().get("plName").toString());
            dataMap.put("menuName", qinfo.getMenuName());
            dataMap.put("requireNo", qinfo.getRequirementNo());
            dataMap.put("questionType", qinfo.getMap().get("dict_label").toString());
            dataMap.put("questionDesc", qinfo.getQuestionDesc());
            if (qinfo.getMap().get("operTypeString") == null) {
                dataMap.put("operTypeString", "");
            } else {
                dataMap.put("operTypeString", qinfo.getMap().get("operTypeString").toString());
            }
            if (qinfo.getMap().get("priorityString") == null) {
                dataMap.put("priorityString", "");
            } else {
                dataMap.put("priorityString", qinfo.getMap().get("priorityString").toString());
            }
            if (qinfo.getMap().get("allocate_name") == null) { // modify chensj 分配人map key 错误
                dataMap.put("allocateUser", "");
            } else {
                dataMap.put("allocateUser", qinfo.getMap().get("allocate_name").toString());// modify chensj 分配人map key 错误
            }
            dataMap.put("create_name", qinfo.getMap().get("create_name").toString());
            dataMap.put("createTimeString", qinfo.getMap().get("createTimeString").toString());
            dataMap.put("isPmis", qinfo.getPmisStatus() == 2 ? "否" : "是");
            dataList.add(dataMap);
        }
        ExcelUtil.writeExcel(dataList, colList, colList.size(), path);
    }

    @Override
    public List<Map<String, Object>> getEtSiteQuestionCountInfo(EtSiteQuestionInfo info) {

        return this.etSiteQuestionInfoDao.selectEtSiteQuestionCountInfo(info);
    }

    @Override
    public void createEtSiteQuestionInfo(List<List<Object>> questionList, EtSiteQuestionInfo info) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
        for (List<Object> list : questionList) {
            if (list.size() < 13) {
                throw new SSGJException("上传文件中存在必填项未填写。");
            }
        }
        SysUserInfo user = new SysUserInfo();
        user.setId(info.getCreator());
        user = sysUserInfoService.getSysUserInfo(user);
        for (List<Object> list : questionList) {
            EtTempQuestionInfo tempInfo = new EtTempQuestionInfo();
            tempInfo.setId(ssgjHelper.createtempWorkReportGenerateService());
            tempInfo.setPmId(info.getPmId());
            tempInfo.setCId(info.getCId());
            tempInfo.setSerialNo(info.getSerialNo());
            tempInfo.setPriorityType(list.get(0).toString());
            tempInfo.setSiteName(list.get(1).toString());
            tempInfo.setProductName(list.get(2).toString());
            tempInfo.setMenuName(list.get(3).toString());
            tempInfo.setQuestionDesc(list.get(4).toString());
            tempInfo.setQuestionVar(list.get(5).toString());
            tempInfo.setReasonVar(list.get(6).toString());
            tempInfo.setManuscriptVar(list.get(7).toString());
            tempInfo.setDiffcultVar(list.get(8).toString());
            tempInfo.setDevUser(user.getUserid());
            tempInfo.setDevUserName(user.getYhmc());
            tempInfo.setLinkman(list.get(9).toString());
            tempInfo.setMobile(list.get(10).toString());
            tempInfo.setOperVar(list.get(11).toString());
            tempInfo.setHopeFinishDate(list.get(12).toString());
            String userMsg = "";
            if (list.size() >= 14 && !StringUtil.isEmptyOrNull(list.get(13).toString())) {
                userMsg = list.get(13).toString();
            }
            tempInfo.setUserMessage(userMsg);
            String requireNo = "";
            if (list.size() >= 15 && !StringUtil.isEmptyOrNull(list.get(14).toString())) {
                requireNo = list.get(14).toString();
            }
            tempInfo.setRequirementNo(requireNo);
            etTempQuestionInfoDao.insertEntity(tempInfo);
        }
        EtTempQuestionInfo tempQuestionInfo = new EtTempQuestionInfo();
        tempQuestionInfo.setSerialNo(info.getSerialNo());
        etTempQuestionInfoDao.updateEtTempQuestionInfoDictValue(tempQuestionInfo);
        List<EtTempQuestionInfo> etTempQuestionInfos = etTempQuestionInfoDao.selectEntityList(tempQuestionInfo);
        for (EtTempQuestionInfo tinfo : etTempQuestionInfos) {
            EtSiteQuestionInfo qinfo = new EtSiteQuestionInfo();
            qinfo.setId(ssgjHelper.createSiteQuestionIdService());
            qinfo.setPmId(info.getPmId());
            qinfo.setCId(info.getCId());
            qinfo.setSerialNo(info.getSerialNo());
            qinfo.setSiteName(tinfo.getSiteId());
            qinfo.setProductName(tinfo.getProductId());
            qinfo.setMenuName(tinfo.getMenuName());
            qinfo.setQuestionType(tinfo.getQuestionType());
            qinfo.setQuestionDesc(tinfo.getQuestionDesc());
            qinfo.setOperType(tinfo.getOperType());
            qinfo.setPriority(tinfo.getPriority());
            qinfo.setReasonType(tinfo.getReasonType());
            qinfo.setManuscriptStatus(tinfo.getManuscriptStatus());
            qinfo.setDevUser(tinfo.getDevUser());
            qinfo.setDevUserName(tinfo.getDevUserName());
            qinfo.setLinkman(tinfo.getLinkman());
            qinfo.setMobile(tinfo.getMobile());
            qinfo.setHopeFinishDate(tinfo.getHopeFinishDate());
            qinfo.setCreator(info.getCreator());
            qinfo.setCreateTime(new Timestamp(new Date().getTime()));
            if (tinfo.getRequirementNo() != null && !StringUtil.isEmptyOrNull(tinfo.getRequirementNo())) {
                qinfo.setRequirementNo(tinfo.getRequirementNo());
                qinfo.setPmisStatus(1);
            } else {
                qinfo.setPmisStatus(2);
            }
            qinfo.setCreateNo(user.getUserid());
            qinfo.setIsOperation(0);
            etSiteQuestionInfoDao.insertEntity(qinfo);
        }
        etTempQuestionInfoDao.deleteEntity(tempQuestionInfo);
    }

    @Override
    public List<EtSiteQuestionInfo> getEtSiteQuestionInfoTotalCountByUser(EtSiteQuestionInfo info) {
        return this.etSiteQuestionInfoDao.selectEtSiteQuestionInfoTotalCountByUser(info);
    }

    /**
     * 根据用户ID和客户ID查询当前用户的可以查看的问题
     * 注：只能看自己的问题的列表
     *
     * @param info 包含创建人、客户号
     * @return
     */
    @Override
    public List<MobileSiteQuestion> getSiteQuestionInfoByUser(EtSiteQuestionInfo info) throws ParseException {
        List<MobileSiteQuestion> resultMap = new ArrayList<>();
        List<Map<String, Object>> countInfo = etSiteQuestionInfoDao.selectEtSiteQuestionInfoCountByUser(info);
        for (Map<String, Object> map : countInfo) {
            MobileSiteQuestion<EtSiteQuestionInfo> question = new MobileSiteQuestion<>();
            question.setGroupName(DateUtil.convertDateToMMDD(map.get("createDate").toString()));
            question.setNum(map.get("countNum").toString());
            question.setListQuery(querySiteQuestionByUserAndDate(info, map.get("createDate").toString()));
            resultMap.add(question);
        }
        return resultMap;
    }

    /**
     * 查看问题展示首页信息
     */
    @Override
    public List<EtSiteQuestionInfo> selectMobileEtSiteQuestionInfo(EtSiteQuestionInfo t) {

        return etSiteQuestionInfoDao.selectMobileEtSiteQuestionInfo(t);
    }

    @Override
    public EtSiteQuestionInfo getEtSiteQuestionProcessStatus(EtSiteQuestionInfo t) {
        return etSiteQuestionInfoDao.selectEtSiteQuestionProcessStatus(t);
    }

    /**
     * 判断当前的问题信息是否可以删除
     *
     * @param info
     * @return
     */
    @Override
    public int checkEtSiteQuestionInfoStatus(EtSiteQuestionInfo info) {
        info = etSiteQuestionInfoDao.selectEntity(info);
        if (info.getAllocateUser() != null || info.getRequirementNo() != null) {
            return 1;
        }
        return 0;
    }

    /**
     * 检查问题的标题是否为空，为空则返回 0 反之返回 1
     * 0 作为前端允许删除
     * 1 作为前端不操作
     *
     * @param info
     * @return
     */
    @Override
    public int checkQuestionStatus(EtSiteQuestionInfo info) {
        info = etSiteQuestionInfoDao.selectEntity(info);
        if (StringUtil.isEmptyOrNull(info.getMenuName())) {
            return 0;
        }
        return 1;
    }

    /**
     * 企业微信首页处理情况
     *
     * @param t
     * @return
     */
    @Override
    public EtSiteQuestionInfo getEtSiteQuestionProcessStatusService(EtSiteQuestionInfo t) {
        return etSiteQuestionInfoDao.selectEtSiteQuestionProcessStatusService(t);
    }

    @Override
    public List<EtSiteQuestionInfo> selectEtSiteQuestionInfoUserTotalBySerialNo(EtSiteQuestionInfo etSiteQuestionInfo) {
        return etSiteQuestionInfoDao.selectEtSiteQuestionInfoUserTotalBySerialNo(etSiteQuestionInfo);
    }

    @Override
    public void updateProcessStatus(EtSiteQuestionInfo etSiteQuestionInfo) {
        etSiteQuestionInfoDao.updateProcessStatus(etSiteQuestionInfo);
    }

    /**
     * 查询当前用户在指定日期下的问题里列表
     *
     * @param info       包含创建人、客户号
     * @param createDate 创建时间
     * @return
     */
    private List<EtSiteQuestionInfo> querySiteQuestionByUserAndDate(EtSiteQuestionInfo info, String createDate) {
        info.getMap().put("createDate", createDate);
        return etSiteQuestionInfoDao.selectEtSiteQuestionInfoListByUserAndDate(info);

    }

}
