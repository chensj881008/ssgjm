package cn.com.winning.ssgj.web.controller.vue;

import cn.com.winning.ssgj.base.Constants;
import cn.com.winning.ssgj.base.annoation.ILog;
import cn.com.winning.ssgj.base.helper.SSGJHelper;
import cn.com.winning.ssgj.base.util.*;
import cn.com.winning.ssgj.domain.*;
import cn.com.winning.ssgj.domain.support.Row;
import cn.com.winning.ssgj.web.controller.common.BaseController;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基础数据类型处理Controller
 *
 * @author FengChen
 * @date 2018年3月18日10:37:18
 */
@CrossOrigin
@Controller
@RequestMapping("/vue/dataCheck")
public class EtDataCheckController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(EtDataCheckController.class);
    @Autowired
    private SSGJHelper ssgjHelper;

    /**
     * 根据项目id初始化数据源
     *
     * @param pmId
     * @param operator
     */
    @RequestMapping("/initSourceData.do")
    @ResponseBody
    public Map<String, Object> initSourceData(Long pmId, Long operator, Integer dataType) {
        Map map = new HashMap();
        if (pmId == null) {
            return null;
        }
        //根据pmId获取项目基础信息
        PmisProjectBasicInfo pmisProjectBasicInfo = this.getFacade().getCommonQueryService().queryPmisProjectBasicInfoByProjectId(pmId);
        //cId
        Long cId = pmisProjectBasicInfo.getHtxx();
        //serialNo
        Long serialNo = pmisProjectBasicInfo.getKhxx();
        EtDataCheck etDataCheck = new EtDataCheck();
        etDataCheck.setPmId(pmId);
        etDataCheck.setcId(cId);
        etDataCheck.setSerialNo(serialNo.toString());
        //根据pmId获取基础数据校验数据
        List<EtDataCheck> etDataCheckList = getFacade().getEtDataCheckService().getInitEtDataCheck(etDataCheck);
        List<EtDataCheck> list = null;
        EtDataCheck etDataCheckTemp = null;
        for (EtDataCheck dataCheck : etDataCheckList) {
            etDataCheckTemp = new EtDataCheck();
            etDataCheckTemp.setPmId(dataCheck.getPmId());
            etDataCheckTemp.setSourceId(dataCheck.getSourceId());
            list = getFacade().getEtDataCheckService().getEtDataCheckList(etDataCheckTemp);
            if (list.size() == 0) {
                //不存在则插入
                dataCheck.setId(ssgjHelper.createEtDataCheckId());
                getFacade().getEtDataCheckService().createEtDataCheck(dataCheck);
            }
        }
        map.put("status", Constants.SUCCESS);
        map.put("msg", "初始化数据成功！");
        return map;
    }

    /**
     * 基础数据类型列表
     *
     * @param row
     * @param pmId 项目id
     * @return
     * @description 根据项目id获取基础数据
     */
    @RequestMapping("/list.do")
    @ResponseBody
    public Map<String, Object> list(Row row, Long pmId, String operator) {
        //项目id
        if (pmId == null) {
            return null;
        }
        //根据项目id获取项目基本信息
        PmisProjectBasicInfo pmisProjectBasicInfo = getFacade().getCommonQueryService().queryPmisProjectBasicInfoByProjectId(pmId);
        //获取合同id
        Long contractId = pmisProjectBasicInfo.getHtxx();
        //获取单据号即客户
        Long customerId = pmisProjectBasicInfo.getKhxx();

        EtDataCheck etDataCheck = new EtDataCheck();

        etDataCheck.setRow(row);

        etDataCheck.setPmId(pmId);

        etDataCheck.setcId(contractId);

        etDataCheck.setSerialNo(customerId.toString());
        //获取基础数据校验
        List<EtDataCheck> etDataCheckList =
                getFacade().getEtDataCheckService().getEtDataCheckPaginatedList(etDataCheck);
        for (EtDataCheck e : etDataCheckList) {
            Map<String, Object> map = new HashMap();
            SysDataCheckScript sysDataCheckScript = new SysDataCheckScript();
            sysDataCheckScript.setId(e.getSourceId());
            sysDataCheckScript = getFacade().getSysDataCheckScriptService().getSysDataCheckScript(sysDataCheckScript);
//            map.put("type", pmisProductLineInfo.getName());
            if (sysDataCheckScript != null) {
                String scriptPath = sysDataCheckScript.getRemotePath();
                scriptPath = scriptPath.substring(scriptPath.lastIndexOf("/") + 1, scriptPath.lastIndexOf("."));
                map.put("type", scriptPath);
            }
            String checkResult = e.getCheckResult();
            if (checkResult == null || "没问题".equals(checkResult) || "校验正常".equals(checkResult) || "".equals(checkResult)) {
                map.put("state", 0);
            } else {
                map.put("state", 1);
            }
            Long ipId = e.getIpId();
            String databaseIp = "";
            if (ipId != null) {
                EtDatabasesList database = new EtDatabasesList();
                database.setId(ipId);
                database = getFacade().getEtDatabasesListService().getEtDatabasesList(database);
                databaseIp = database == null ? "" : database.getDataAlias();
            }
//            String ip = databaseIp.split(":")[0];
            map.put("ip", databaseIp);
            e.setMap(map);
        }
        int total = getFacade().getEtDataCheckService().getEtDataCheckCount(etDataCheck);
        //根据pmid获取项目进程
        EtProcessManager etProcessManager = new EtProcessManager();
        etProcessManager.setPmId(pmId);
        etProcessManager = getFacade().getEtProcessManagerService().getEtProcessManager(etProcessManager);
        //获取ipList
        EtDatabasesList etDatabasesList = new EtDatabasesList();
        etDatabasesList.setPmId(pmId);
        List<EtDatabasesList> etDatabasesListList = getFacade().getEtDatabasesListService().getEtDatabasesListList(etDatabasesList);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("rows", etDataCheckList);
        map.put("total", total);
        map.put("ipList", etDatabasesListList);
        map.put("status", Constants.SUCCESS);
        map.put("process", etProcessManager);
        return map;
    }


    /**
     * 获取基础数据详情
     *
     * @param t
     * @return
     */
    @RequestMapping("/detail.do")
    @ResponseBody
    public Map<String, Object> detail(EtDataCheck t) {
        //根据id获取表属性
        EtDataCheck etDataCheck = getFacade().getEtDataCheckService().getEtDataCheck(t);
        //获取content
        String content = etDataCheck.getContent();
        if (StringUtil.isEmptyOrNull(content)) {
            return null;
        }
        List<List<Object>> parse = (List<List<Object>>) JSONArray.parse(content);
        //封装content
        List<JSONObject> detailList = new ArrayList<>();
        for (List<Object> objList : parse) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("question", objList.get(1).toString());
            jsonObject.put("everyTime", objList.get(2).toString());
            detailList.add(jsonObject);
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("data", detailList);
        map.put("status", Constants.SUCCESS);
        return map;
    }


    /**
     * @param request
     * @param file
     * @return
     * @throws IOException
     * @description 文件上传
     */
    @RequestMapping(value = "/upload.do")
    @ResponseBody
    @Transactional
    public Map<String, Object> upload(HttpServletRequest request, MultipartFile file, EtDataCheck t, Long pmId) throws IOException {
        //根据id获取表属性
        EtDataCheck temp = new EtDataCheck();
        temp.setId(t.getId());
        EtDataCheck etDataCheck = getFacade().getEtDataCheckService().getEtDataCheck(temp);
        etDataCheck.setOperator(t.getOperator());
        SysDataCheckScript sysDataCheckScript = new SysDataCheckScript();
        sysDataCheckScript.setId(etDataCheck.getSourceId());
        sysDataCheckScript = getFacade().getSysDataCheckScriptService().getSysDataCheckScript(sysDataCheckScript);
        Map<String, Object> result = new HashMap<String, Object>();
        //如果文件不为空，写入上传路径
        if (!file.isEmpty()) {
            //上传文件路径
            String path = request.getServletContext().getRealPath("/script/");
            //上传文件名
            String filename = file.getOriginalFilename();
            File filepath = new File(path, filename);
            //判断路径是否存在，如果不存在就创建一个
            if (!filepath.getParentFile().exists()) {
                filepath.getParentFile().mkdirs();
            }
            //将上传文件保存到一个目标文件当中
            File newFile = new File(path + File.separator + filename);
            if (newFile.exists()) {
                newFile.delete();
            }
            file.transferTo(newFile);
            JSONArray jsonArray = new JSONArray();
            //错误计数
            Integer failNum = 0;
            //检测结果
            String checkResult = "";
            try {
                List<List<Object>> etDataCheckList = ExcelUtil.importExcel(newFile.getPath());
                for (List<Object> e : etDataCheckList) {
                    for (int i = 0; i < e.size(); i++) {
                        if ("F".equalsIgnoreCase(e.get(i).toString())) {
                            failNum++;
                        }
                    }
                    jsonArray.add(e);
                }
                System.out.println(jsonArray.toJSONString());
                etDataCheck.setContent(jsonArray.toJSONString());
                if (failNum == null || failNum == 0) {
                    checkResult = "校验正常";
                } else {
                    checkResult = "校验出" + failNum + "个问题";
                }
                etDataCheck.setCheckResult(checkResult);
                //文件夹路径
//                String dir = Constants.UPLOAD_PC_PREFIX + pmId + "/dataCheck/" + sysDataCheckScript.getAppName() + "/";
//                String src = newFile.getAbsolutePath();
//                String fileName = newFile.getName();
//                etDataCheck.setScriptPath(dir + fileName);
                etDataCheck.setOperatorTime(new Timestamp(new Date().getTime()));
                getFacade().getEtDataCheckService().modifyEtDataCheck(etDataCheck);
                //将文件上传到ftp服务器
//                SFtpUtils.uploadFile(src, dir, fileName);
                newFile.delete();
                result.put("status", "success");
            } catch (Exception e) {
                e.printStackTrace();
                result.put("status", "error");
                result.put("msg", "上传文件失败,原因是：" + e.getMessage());
            }
        } else {
            result.put("status", "error");
            result.put("msg", "上传文件失败,原因是：上传文件为空");
        }
        return result;
    }


    /**
     * @param response
     * @return
     * @throws IOException
     * @description 导出sql
     */
    @RequestMapping(value = "/exportSql.do")
    @ResponseBody
    public Map<String, Object> exportSql(HttpServletResponse response, EtDataCheck t) throws IOException {
        //获取数据校验信息
        EtDataCheck etDataCheck = getFacade().getEtDataCheckService().getEtDataCheck(t);
        SysDataCheckScript temp = new SysDataCheckScript();
        temp.setId(etDataCheck.getSourceId());
//        temp.setAppId(etDataCheck.getPlId());
        SysDataCheckScript sysDataCheckScript = getFacade().getSysDataCheckScriptService().getSysDataCheckScript(temp);
        Map map = new HashMap();
        //获取脚本地址
        if (sysDataCheckScript == null) {
            logger.error("Script is not exist!");
            map.put("error", "Script is not exist!");
            return map;
        }
        String scriptPath = sysDataCheckScript.getRemotePath();
        if (StringUtil.isEmptyOrNull(scriptPath)) {
            logger.error("Script path is null or empty!");
            map.put("error", "Script path is null or empty!");
            return map;
        }
        //获取文件名
        String filename = scriptPath.substring(scriptPath.lastIndexOf("/") + 1);
        String ftpPath = scriptPath.replace(filename, "");
//        ChannelSftp sftpConnect = null;
        byte[] bytes = null;
        try {
//            sftpConnect = SFtpUtils.getSftpConnect();
            //sftpConnect.setFilenameEncoding("GBK");
            bytes = CommonFtpUtils.downloadFileAsByte(filename, ftpPath);

        } catch (Exception e) {
            e.printStackTrace();
        }


        response.setCharacterEncoding("utf-8");
        //设置响应内容的类型
        response.setContentType("text/plain");
        //设置文件的名称和格式
        response.addHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(filename, "UTF-8"))));
        BufferedOutputStream buff = null;
        OutputStream outStr = null;
        try {
            outStr = response.getOutputStream();
            buff = new BufferedOutputStream(outStr);
            buff.write(bytes);
            buff.flush();
            buff.close();
        } catch (Exception e) {
            logger.error("导出文件文件出错，e:{}", e);
        } finally {
            try {
                buff.close();
                outStr.close();
            } catch (Exception e) {
                logger.error("关闭流对象出错 e:{}", e);
            }
        }
        map.put("status", Constants.SUCCESS);
        return map;
    }

    /**
     * @param response
     * @return
     * @throws IOException
     * @description 导出sql
     */
    @RequestMapping(value = "/exportModel.do")
    @ResponseBody
    public Map<String, Object> exportModel(HttpServletResponse response, EtDataCheck t) throws IOException {

        String realPath = Thread.currentThread().getContextClassLoader().getResource("/template").getPath();
        //获取文件名
        String filename = realPath + "\\DataCheckModel.xlsx";
        File file = new File(filename);
        FileInputStream fileInputStream = new FileInputStream(file);
        InputStream inputStream = new BufferedInputStream(fileInputStream);
        String excelName = "基础数据校验模板" + DateUtil.format(DateUtil.PATTERN_14) + ".xlsx";
        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes);
        response.setCharacterEncoding("utf-8");
        //设置响应内容的类型
        response.setContentType("text/plain");
        //设置文件的名称和格式
        response.addHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(excelName, "UTF-8"))));
        BufferedOutputStream buff = null;
        OutputStream outStr = null;
        try {
            outStr = response.getOutputStream();
            buff = new BufferedOutputStream(outStr);
            buff.write(bytes);
            buff.flush();
            buff.close();
        } catch (Exception e) {
            logger.error("导出模板出错，e:{}", e);
        } finally {
            try {
                buff.close();
                outStr.close();
            } catch (Exception e) {
                logger.error("关闭流对象出错 e:{}", e);
            }
        }
        Map map = new HashMap();
        map.put("status", Constants.SUCCESS);
        return map;

    }


    /**
     * 确认完成
     *
     * @param etProcessManager
     * @return
     */
    @RequestMapping(value = "/confirm.do")
    @ResponseBody
    @Transactional
    public Map<String, Object> confirm(EtProcessManager etProcessManager) {
        EtProcessManager temp = new EtProcessManager();
        temp.setPmId(etProcessManager.getPmId());
        temp = super.getFacade().getEtProcessManagerService().getEtProcessManager(temp);
        temp.setOperator(etProcessManager.getOperator());
        temp.setOperatorTime(new Timestamp(new Date().getTime()));
        temp.setIsBasicDataCheck(etProcessManager.getIsBasicDataCheck());
        super.getFacade().getEtProcessManagerService().modifyEtProcessManager(temp);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("status", Constants.SUCCESS);
        return result;
    }

    /**
     * 脚本校验
     *
     * @param etDataCheck
     * @return
     */
    @RequestMapping(value = "/doScriptCheck.do")
    @ResponseBody
    @Transactional
    public Map<String, Object> doScriptCheck(EtDataCheck etDataCheck) {
        if (etDataCheck.getIpId() == null) {
            resultMap.put("status", Constants.FAILD);
            return resultMap;
        }
        //ipId
        EtDatabasesList etDatabasesList = new EtDatabasesList();
        etDatabasesList.setId(etDataCheck.getIpId());
        etDatabasesList = getFacade().getEtDatabasesListService().getEtDatabasesList(etDatabasesList);
        if (etDatabasesList == null) {
            resultMap.put("status", Constants.FAILD);
            return resultMap;
        }
        //数据库参数
        String ip = etDatabasesList.getIp();
        String userName = etDatabasesList.getUserName();
        String pw = etDatabasesList.getPw();
//        String databaseName = etDataCheck.getDatabaseName();
        String databaseName = etDatabasesList.getDatabaseName();
        //连接数据库
        Connection connection = ConnectionUtil.getConnection(ip, userName, pw, databaseName);
        if (connection == null) {
            resultMap.put("status", Constants.FAILD);
            return resultMap;
        }
        EtDataCheck temp = new EtDataCheck();
        temp.setId(etDataCheck.getId());
        temp = getFacade().getEtDataCheckService().getEtDataCheck(temp);
        SysDataCheckScript sysDataCheckScript = new SysDataCheckScript();
        sysDataCheckScript.setId(temp.getSourceId());
        sysDataCheckScript = getFacade().getSysDataCheckScriptService().getSysDataCheckScript(sysDataCheckScript);
        //获取存储过程名称
        String procName = sysDataCheckScript.getName();
        //判断存储过程是否存在
        String existsProcSql = "if exists (select * from dbo.sysobjects where id = object_id(N'[dbo].[" + procName + "]') and OBJECTPROPERTY(id, N'IsProcedure') = 1)   begin drop procedure [dbo].[" + procName + "] end";
        //配置
        String preSql = "set QUOTED_IDENTIFIER  OFF;\n" +
                "set ANSI_NULLS  OFF;\n" +
                "set ANSI_NULL_DFLT_ON OFF;\n" +
                "set ANSI_PADDING OFF ;\n" +
                "set ANSI_WARNINGS OFF; ";
        //存储过程
        String createProcSql = sysDataCheckScript.getsDesc();
        String runProcSql = "exec " + procName + " '0'";
        ResultSet rs = null;
        //错误计数
        Integer failNum = 0;
        //检测结果
        String checkResult = "";
        try {
            PreparedStatement ps = connection.prepareStatement(existsProcSql);
            ps.execute();
            ps = connection.prepareStatement(preSql);
            ps.execute();
            createProcSql = createProcSql.replace("\"", "\'");
            ps = connection.prepareStatement(createProcSql);
            ps.execute();
            ps = connection.prepareStatement(runProcSql);
            rs = ps.executeQuery();
            JSONArray jsonArray = new JSONArray();
            while (rs.next()) {
                List<String> resetList = new ArrayList<>();
                resetList.add(rs.getString(1));
                if ("F".equalsIgnoreCase(rs.getString(1))) {
                    failNum++;
                }
                resetList.add(rs.getString(2));
                resetList.add(rs.getString(3));
                System.out.println(rs.getString(1) + "--" + rs.getString(2) + "--" + rs.getString(3));
                jsonArray.add(resetList);
            }
            etDataCheck.setId(etDataCheck.getId());
            etDataCheck.setContent(jsonArray.toJSONString());
            if (failNum == null || failNum == 0) {
                checkResult = "校验正常";
            } else {
                checkResult = "校验出" + failNum + "个问题";
            }
            etDataCheck.setCheckResult(checkResult);
            etDataCheck.setOperatorTime(new Timestamp(new Date().getTime()));
            getFacade().getEtDataCheckService().modifyEtDataCheck(etDataCheck);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("status", Constants.FAILD);
            resultMap.put("msg", e.getMessage());
            return resultMap;
        }
        resultMap.put("status", Constants.SUCCESS);
        return resultMap;

    }


    /**
     * @param response
     * @return
     * @throws IOException
     * @description 导出sql
     */
    @RequestMapping(value = "/exportExceptionResult.do")
    @ResponseBody
    public Map<String, Object> exportExceptionResult(HttpServletResponse response, String sql, String question, Long id) throws IOException {
        EtDataCheck dataCheck = new EtDataCheck();
        dataCheck.setId(id);
        dataCheck = getFacade().getEtDataCheckService().getEtDataCheck(dataCheck);

        //ipId
        EtDatabasesList etDatabasesList = new EtDatabasesList();
        etDatabasesList.setId(dataCheck.getIpId());
        etDatabasesList = getFacade().getEtDatabasesListService().getEtDatabasesList(etDatabasesList);
        Connection connection = null;
        if (etDatabasesList == null) {
            connection = ConnectionUtil.getConnection();
        } else {
            //数据库参数
            String ip = etDatabasesList.getIp();
            String userName = etDatabasesList.getUserName();
            String pw = etDatabasesList.getPw();
            String databaseName = etDatabasesList.getDatabaseName();
            connection = ConnectionUtil.getConnection(ip, userName, pw, databaseName);
        }
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        //创建Excel工作簿 Excel 2003
        Workbook wb = new HSSFWorkbook();
        try {
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            //将表数据写入表格
            ResultSetUtil.resultSetToExcel(resultSet, wb);


        } catch (SQLException e) {
            e.printStackTrace();
        }
        String filename = question + ".xls";
        response.setContentType("application/msexcel;charset=UTF-8");
        response.addHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(filename, "UTF-8"))));
        OutputStream toClient = response.getOutputStream();
        wb.write(toClient);
        toClient.flush();
        toClient.close();
        resultMap.put("status", Constants.SUCCESS);
        return resultMap;
    }

}


