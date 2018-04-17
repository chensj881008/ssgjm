package cn.com.winning.ssgj.web.controller.vue;

import cn.com.winning.ssgj.base.Constants;
import cn.com.winning.ssgj.base.annoation.ILog;
import cn.com.winning.ssgj.base.helper.SSGJHelper;
import cn.com.winning.ssgj.base.util.ConnectionUtil;
import cn.com.winning.ssgj.base.util.DateUtil;
import cn.com.winning.ssgj.base.util.ExcelUtil;
import cn.com.winning.ssgj.base.util.StringUtil;
import cn.com.winning.ssgj.domain.*;
import cn.com.winning.ssgj.domain.support.Row;
import cn.com.winning.ssgj.web.controller.common.BaseController;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.*;

/**
 * @author chenfeng
 * @Description 接口信息controller
 * @Date 2018年3月29日09:59:48
 */
@CrossOrigin
@Controller
@RequestMapping(value = "/vue/thirdInterface")
public class EtThirdIntterfaceController extends BaseController {

    @Autowired
    private SSGJHelper ssgjHelper;


    /**
     * 初始化数据
     *
     * @param pmId
     * @param operator
     */
    @RequestMapping(value = "/initSourceData.do")
    @ResponseBody
    public Map<String, Object> initSourceData(Long pmId, Long operator) {
        Map map = new HashMap();
        EtThirdIntterface etThirdIntterface = new EtThirdIntterface();
        etThirdIntterface.setPmId(pmId);
        //根据pmid获取所有接口信息
        List<EtThirdIntterface> etThirdIntterfaces = getFacade().getEtThirdIntterfaceService().selectPmisInterfaceList(etThirdIntterface);
        for (EtThirdIntterface intterface : etThirdIntterfaces) {
            //查询数据是否入库
            EtThirdIntterface temp = getFacade().getEtThirdIntterfaceService().getEtThirdIntterface(intterface);
            if (temp == null) {
                //不存在则将数据插入
                intterface.setId(ssgjHelper.createThirdInterfaceId());
                getFacade().getEtThirdIntterfaceService().createEtThirdIntterface(intterface);
            }
        }
        map.put("status", Constants.SUCCESS);
        map.put("msg", "初始化数据成功！");
        return map;
    }

    /**
     * 获取接口信息集合
     *
     * @param etThirdIntterface
     * @param row
     * @return
     */
    @RequestMapping(value = "/initData.do")
    @ResponseBody
    public Map<String, Object> initData(EtThirdIntterface etThirdIntterface, Row row) {
        etThirdIntterface.setRow(row);
        //根据pmid获取所有接口数据
        List<EtThirdIntterface> etThirdIntterfaces = getFacade().getEtThirdIntterfaceService().getEtThirdIntterfacePaginatedList(etThirdIntterface);
        //根据pmid获取接口数
        Integer total = etThirdIntterfaces == null ? 0 : etThirdIntterfaces.size();
        PmisProductLineInfo pmisProductLineInfo = null;
        Map map = null;
        //封装产品条线名(即产品名称)
        for (EtThirdIntterface intterface : etThirdIntterfaces) {
            if (StringUtil.isEmptyOrNull(intterface.getProductName())) {
                pmisProductLineInfo = new PmisProductLineInfo();
                pmisProductLineInfo.setId(intterface.getPlId());
                pmisProductLineInfo = getFacade().getPmisProductLineInfoService().getPmisProductLineInfo(pmisProductLineInfo);
                intterface.setProductName(pmisProductLineInfo == null ? null : pmisProductLineInfo.getName());
            }
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("total", total);
        result.put("status", Constants.SUCCESS);
        result.put("rows", etThirdIntterfaces);
        return result;
    }

    /**
     * 获取接口信息集合
     *
     * @param etThirdIntterface
     * @param row
     * @return
     */
    @RequestMapping(value = "/list.do")
    @ResponseBody
    public Map<String, Object> list(EtThirdIntterface etThirdIntterface, Row row) {
        etThirdIntterface.setRow(row);
        //根据pmid获取所有接口数据
        List<EtThirdIntterface> etThirdIntterfaces = getFacade().getEtThirdIntterfaceService().getEtThirdIntterfacePaginatedList(etThirdIntterface);
        //根据pmid获取接口数
        Integer total = etThirdIntterfaces == null ? 0 : etThirdIntterfaces.size();
        PmisProductLineInfo pmisProductLineInfo = null;
        Map map = null;
        String contentType = null;
        String[] contentArr = null;
        Integer completeNum = 0;
        //封装产品条线名、完成情况
        for (EtThirdIntterface intterface : etThirdIntterfaces) {
            map = new HashMap();
            //产品条线
            pmisProductLineInfo = new PmisProductLineInfo();
            pmisProductLineInfo.setId(intterface.getPlId());
            pmisProductLineInfo = getFacade().getPmisProductLineInfoService().getPmisProductLineInfo(pmisProductLineInfo);
            //完成情况
            contentType = intterface.getContentType();
            if (contentType != null && contentType.contains("1") && contentType.contains("2") && contentType.contains("3")) {
                ++completeNum;
            }
            contentArr = contentType == null ? null : contentType.split(",");
            map.put("contentList", contentArr);
            if (intterface.getStatus() == null || intterface.getStatus() == 0) {
                map.put("status", false);
            } else {
                map.put("status", true);
            }

            map.put("plName", pmisProductLineInfo == null ? null : pmisProductLineInfo.getName());
            intterface.setMap(map);
        }
        //根据项目Id和项目类型查询产品信息
        List<PmisProductInfo> pmisProductInfos = getFacade().getCommonQueryService().queryProductOfProjectByProjectIdAndType(etThirdIntterface.getPmId(), 9);
        //根据产品信息获取产品条线
        List<PmisProductLineInfo> pmisProductLineInfoList =getFacade().getCommonQueryService().selectPmisProductLineInfoByProductInfo(pmisProductInfos);
        //获取所有不在本期范围原因
        SysDictInfo sysDictInfo = new SysDictInfo();
        sysDictInfo.setDictCode("NotInScope");
        List<SysDictInfo> sysDictInfoList = getFacade().getSysDictInfoService().getSysDictInfoList(sysDictInfo);

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("completeNum", completeNum);
        result.put("total", total);
        result.put("status", Constants.SUCCESS);
        result.put("rows", etThirdIntterfaces);
        result.put("plList", pmisProductLineInfoList);
        result.put("resonList", sysDictInfoList);
        return result;
    }

    /**
     * 添加或修改接口
     *
     * @param etThirdIntterface
     * @return
     */
    @RequestMapping(value = "/addOrModify.do")
    @ResponseBody
    @ILog
    @Transactional
    public Map<String, Object> addOrModify(EtThirdIntterface etThirdIntterface) {
        //创建临时变量
        EtThirdIntterface etThirdIntterfaceTemp = new EtThirdIntterface();
        String noScopeCode = etThirdIntterface.getNoScopeCode();
        if (StringUtil.isEmptyOrNull(noScopeCode)) {
            etThirdIntterface.setIsScope(1);
        } else {
            etThirdIntterface.setIsScope(0);
        }
        etThirdIntterfaceTemp.setId(etThirdIntterface.getId());
        etThirdIntterfaceTemp = super.getFacade().getEtThirdIntterfaceService().getEtThirdIntterface(etThirdIntterfaceTemp);
        PmisProjectBasicInfo basicInfo = new PmisProjectBasicInfo();
        basicInfo.setId(etThirdIntterface.getPmId());
        basicInfo = super.getFacade().getPmisProjectBasicInfoService().getPmisProjectBasicInfo(basicInfo);
        etThirdIntterface.setSerialNo(basicInfo.getKhxx() + "");
        etThirdIntterface.setcId(basicInfo.getHtxx());
        if (etThirdIntterfaceTemp != null) {
            etThirdIntterface.setOperatorTime(new Timestamp(new Date().getTime()));
            super.getFacade().getEtThirdIntterfaceService().modifyEtThirdIntterface(etThirdIntterface);
        } else {
            etThirdIntterface.setId(ssgjHelper.createThirdInterfaceId());
            etThirdIntterface.setCreator(etThirdIntterface.getOperator());
            etThirdIntterface.setCreateTime(new Timestamp(new Date().getTime()));
            etThirdIntterface.setOperatorTime(new Timestamp(new Date().getTime()));
            super.getFacade().getEtThirdIntterfaceService().createEtThirdIntterface(etThirdIntterface);
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", Constants.SUCCESS);
        return result;

    }

    /**
     * 导出Excel
     *
     * @param response
     * @param etThirdIntterface
     * @throws IOException
     */
    @RequestMapping(value = "/exportExcel.do")
    @ILog
    public void wiriteExcel(HttpServletResponse response, EtThirdIntterface etThirdIntterface) throws IOException {
        //根据pmid获取所有接口数据
        List<EtThirdIntterface> etThirdIntterfaces = getFacade().getEtThirdIntterfaceService().selectEtThirdIntterfaceMergeList(etThirdIntterface);
        //参数集合
        List<Map> dataList = new ArrayList<>();
        for (int i = 0; i < etThirdIntterfaces.size(); i++) {
            dataList.add(ConnectionUtil.objectToMap(etThirdIntterfaces.get(i)));
        }
        //属性数组
        Field[] fields = EtThirdIntterface.class.getDeclaredFields();
        //属性集合
        List<String> attrNameList = new ArrayList<>();
        for (int i = 0; i < fields.length; i++) {
            attrNameList.add(fields[i].getName());
        }
        String filename = "InterfaceInfo" + DateUtil.format(DateUtil.PATTERN_14) + ".xls";
        //创建工作簿
        Workbook workbook = new HSSFWorkbook();
        ExcelUtil.exportExcelByStream(dataList, attrNameList, response, workbook, filename);
    }

    /**
     * 删除接口信息
     *
     * @param etThirdIntterface
     * @return
     */
    @RequestMapping(value = "/delete.do")
    @ResponseBody
    @ILog
    @Transactional
    public Map<String, Object> delete(EtThirdIntterface etThirdIntterface) {
        super.getFacade().getEtThirdIntterfaceService().removeEtThirdIntterface(etThirdIntterface);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", Constants.SUCCESS);
        return result;
    }

    /**
     * 确认完成
     *
     * @param pmId
     * @return
     */
    @RequestMapping(value = "/confirm.do")
    @ResponseBody
    @ILog
    @Transactional
    public Map<String, Object> confirm(Long pmId) {

        if (pmId == null) {
            return null;
        }
        //根据项目id获取项目基本信息
        PmisProjectBasicInfo pmisProjectBasicInfo = getFacade().getCommonQueryService().queryPmisProjectBasicInfoByProjectId(pmId);
        //获取合同id
        Long contractId = pmisProjectBasicInfo.getHtxx();
        //获取单据号即客户
        Long customerId = pmisProjectBasicInfo.getKhxx();

        EtThirdIntterface etThirdIntterface = new EtThirdIntterface();

        etThirdIntterface.setPmId(pmId);

        etThirdIntterface.setcId(contractId);

        etThirdIntterface.setSerialNo(customerId.toString());
        int total = getFacade().getEtThirdIntterfaceService().getEtThirdIntterfaceCount(etThirdIntterface);

        EtProcessManager etProcessManager = new EtProcessManager();
        etProcessManager.setPmId(pmId);
        Map<String, Object> map = new HashMap<String, Object>();
        if (total > 0) {
            etProcessManager.setIsInterfaceDev(1);
            getFacade().getEtProcessManagerService().updateEtProcessManagerByPmId(etProcessManager);
            map.put("type", Constants.SUCCESS);
            map.put("msg", "确认成功！");
        } else {
            map.put("type", "info");
            map.put("msg", "无数据，确认失败！");
        }
        return map;
    }


    /**
     * 更改范围
     *
     * @param etThirdIntterface
     * @return
     */
    @RequestMapping(value = "/changeScope.do")
    @ResponseBody
    @ILog
    @Transactional
    public Map<String, Object> changeScope(EtThirdIntterface etThirdIntterface) {
        String noScopeCode = etThirdIntterface.getNoScopeCode();
        if (StringUtil.isEmptyOrNull(noScopeCode)) {
            etThirdIntterface.setIsScope(1);
        } else {
            etThirdIntterface.setIsScope(0);
        }
        Map map = new HashMap();
        getFacade().getEtThirdIntterfaceService().modifyEtThirdIntterface(etThirdIntterface);
        map.put("type", Constants.SUCCESS);
        map.put("msg", "范围修改成功！");
        return map;
    }

    /**
     * 更改完成情况
     *
     * @param etThirdIntterface
     * @return
     */
    @RequestMapping(value = "/changeContent.do")
    @ResponseBody
    @ILog
    @Transactional
    public Map<String, Object> changeContent(EtThirdIntterface etThirdIntterface) {
        getFacade().getEtThirdIntterfaceService().modifyEtThirdIntterface(etThirdIntterface);
        Map map = new HashMap();
        map.put("type", Constants.SUCCESS);
        map.put("msg", "完成情况修改成功！");
        return map;
    }

    /**
     * 更改审核状态
     *
     * @param status
     * @return
     */
    @RequestMapping(value = "/changeStatus.do")
    @ResponseBody
    @ILog
    @Transactional
    public Map<String, Object> changeStatus(Boolean status, Long id) {
        Map map = new HashMap();
        EtThirdIntterface etThirdIntterface = new EtThirdIntterface();
        etThirdIntterface.setId(id);
        if (status) {
            etThirdIntterface.setStatus(1);
        } else {
            etThirdIntterface.setStatus(0);
        }
        getFacade().getEtThirdIntterfaceService().modifyEtThirdIntterface(etThirdIntterface);
        map.put("type", Constants.SUCCESS);
        map.put("msg", "完成情况修改成功！");
        return map;
    }


}
