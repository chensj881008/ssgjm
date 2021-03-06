package cn.com.winning.ssgj.web.controller.admin;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import cn.com.winning.ssgj.base.annoation.ILog;
import cn.com.winning.ssgj.domain.SysReportInfo;
import cn.com.winning.ssgj.domain.SysUserInfo;
import cn.com.winning.ssgj.domain.support.Row;
import cn.com.winning.ssgj.web.controller.common.BaseController;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import cn.com.winning.ssgj.base.Constants;
import cn.com.winning.ssgj.base.helper.SSGJHelper;

/**
 * 报表类信息表Controller
 *
 * @date 2018-01-04
 */
@Controller
@RequestMapping("/admin/report")
public class SysReportInfoController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(SysReportInfoController.class);
    @Autowired
    private SSGJHelper ssgjHelper;

    @RequestMapping(value = "/reportInfo.do")
    public String userinfo(HttpServletRequest request, Model model) {
        return "auth/module/sysReportInfo";
    }

    /**
     * 报表类信息表
     *
     * @param row
     * @return
     */
    @RequestMapping("/list.do")
    @ResponseBody
    public Map<String, Object> list(SysReportInfo info, Row row) {
        info.setRow(row);
        info.setStatus(Constants.STATUS_USE);
        if(info.getReportType() == -1){
            info.setReportType(null);
        }
        List<SysReportInfo> infos = getFacade().getSysReportInfoService().getSysReportInfoPaginatedListForSelectiveKey(info);
        int total = getFacade().getSysReportInfoService().getSysReportInfoCountForSelectiveKey(info);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("rows", infos);
        map.put("total", total);
        map.put("status", Constants.SUCCESS);
        return map;
    }

    /**
     * 通过产品ID查询报表类信息表
     *
     * @param t
     * @return
     */
    @RequestMapping("/getById.do")
    @ResponseBody
    public Map<String, Object> getById(SysReportInfo t) {
        System.err.println("通过产品ID查询报表类信息表");
        t = getFacade().getSysReportInfoService().getSysReportInfo(t);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("data", t);
        map.put("status", Constants.SUCCESS);
        return map;
    }

    /**
     * 通过产品ID删除报表类信息表
     *
     * @return
     */
    @RequestMapping("/deleteById.do")
    @ResponseBody
    @Transactional
    @ILog(operationName = "通过产品ID删除报表类信息表", operationType = "deleteById")
    public Map<String, Object> deleteById(SysReportInfo t) {
        SysUserInfo user = (SysUserInfo) SecurityUtils.getSubject().getPrincipal();
        t.setStatus(0);
        t.setLastUpdator(user.getId());
        t.setLastUpdateTime(new Timestamp(new Date().getTime()));
        getFacade().getSysReportInfoService().modifySysReportInfo(t);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", Constants.SUCCESS);
        return map;
    }

    /**
     * 添加报表类信息表
     *
     * @param
     * @return
     */
    @RequestMapping("/addSysReportInfo.do")
    @ResponseBody
    @Transactional
    @ILog(operationName = "添加报表类信息表", operationType = "addSysReportInfo")
    public Map<String, Object> addSysReportInfo(SysReportInfo t) {
        SysUserInfo user = (SysUserInfo) SecurityUtils.getSubject().getPrincipal();
        Long id = ssgjHelper.createReportInfoId();
        String code = ssgjHelper.createReportInfoCode();
        t.setReportCode(code);
        t.setId(id);
        t.setReportCode(ssgjHelper.createReportInfoCode());
        t.setStatus(1);
        t.setLastUpdator(user.getId());
        t.setLastUpdateTime(new Timestamp(new Date().getTime()));
        getFacade().getSysReportInfoService().createSysReportInfo(t);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", Constants.SUCCESS);
        return map;
    }

    /**
     * 修改报表类信息表
     *
     * @param t
     * @return
     */
    @RequestMapping("/update.do")
    @ResponseBody
    @Transactional
    @ILog(operationName = "修改报表类信息表", operationType = "update")
    public Map<String, Object> update(SysReportInfo t) {
        SysUserInfo user = (SysUserInfo) SecurityUtils.getSubject().getPrincipal();
        t.setLastUpdator(user.getId());
        t.setLastUpdateTime(new Timestamp(new Date().getTime()));
        getFacade().getSysReportInfoService().modifySysReportInfo(t);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", Constants.SUCCESS);
        return map;
    }

    /**
     * 报表类信息表
     *
     * @param info
     * @return
     */
    @RequestMapping("/listNoPage.do")
    @ResponseBody
    public Map<String, Object> listNoPage(SysReportInfo info) {
        info.setStatus(Constants.STATUS_USE);
        List<SysReportInfo> infos = getFacade().getSysReportInfoService().getSysReportInfolistNoPage(info);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("rows", infos);
        map.put("status", Constants.SUCCESS);
        return map;
    }
}
