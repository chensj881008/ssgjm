package cn.com.winning.ssgj.web.controller.mobile;

import cn.com.winning.ssgj.base.helper.SSGJHelper;
import cn.com.winning.ssgj.base.util.StringUtil;
import cn.com.winning.ssgj.domain.EtLog;
import cn.com.winning.ssgj.web.controller.common.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

/**
 * 楼层问题汇报
 *
 * @author ChenKuai
 * @create 2018-03-06 上午 11:06
 **/
@Controller
@CrossOrigin
@RequestMapping("/mobile/log")
public class LogController extends BaseController {
    @Autowired
    private SSGJHelper ssgjHelper;

    @RequestMapping(value = "/list.do")
    public String floorQuestionList(Model model, EtLog etLog) {
        //参数校验
//        String serialNo = etLog.getSerialNo();
        Long sourceId = etLog.getSourceId();
        if (sourceId == null) {
            //当缺少参数客户号或者sourceId，则无数据
            return "mobile2/service/update-record";
        }
        List<EtLog> etLogList = getFacade().getEtLogService().getEtLogList(etLog);
        resultMap.put("logs", etLogList);
        model.addAllAttributes(resultMap);
        return "mobile2/service/update-record";
    }

//    @RequestMapping(value = "/add.do")
//    @ResponseBody
//    public Map<String, Object> add(EtLog etLog) {
//        this.addEtLog(etLog.getSerialNo(), etLog.getSourceType(), etLog.getSourceId(), etLog.getContent(), etLog.getStatus(), etLog.getOperator());
//        resultMap.put("msg", "新增操作日志成功。");
//        return resultMap;
//    }

}
