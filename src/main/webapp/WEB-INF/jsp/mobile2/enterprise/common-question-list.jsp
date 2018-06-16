<%--
  Created by IntelliJ IDEA.
  User: LENOVO
  Date: 2018/6/16
  Time: 10:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/commons/header.jsp" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>任务列表</title>
    <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
    <link rel="stylesheet" type="text/css" href="<%=basePath%>resources/mobile/css/normalize.css" />
    <link rel="stylesheet" type="text/css" href="<%=basePath%>resources/mobile/css/mui.min.css" />
    <link rel="stylesheet" type="text/css" href="<%=basePath%>resources/mobile/css/common.css" />
    <link rel="stylesheet" type="text/css" href="<%=basePath%>resources/mobile/css/enterprise.css" />
    <link rel="stylesheet" type="text/css" href="//at.alicdn.com/t/font_575705_9raiir53539.css"/>
    <style type="text/css">
        .wap-tab{
            display: flex;
            margin-bottom: 10px;
            padding: 0px 0px;
        }
        .wap-tab>span{
            flex: 1;
            height: 30px;
            line-height: 30px;
            font-size: 12px;
            color: #666666;
            text-align: center;
            border: 1px solid #FFFFFF;
        }
        .wap-tab>span.active{
            color: #fff;
            background: #81B3FF;
            border: 1px solid #81B3FF;
        }
    </style>
</head>
<body>
<%--数据传输隐藏域--%>
<input id="userId" type="hidden" name="userId" value="${userId}">
<input id="serialNo" type="hidden" name="serialNo" value="${serialNo}">
<input id="status" type="hidden" name="status" value="${status}">
<input id="searchType" type="hidden" name="searchType" value="${searchType}">
<input id="searchText" type="hidden" name="searchText" value="${searchText}">
<input id="userType" type="hidden" name="userType" value="${userType}">
<input id="priority" type="hidden" name="priority" value="${priority}">
<div class="wrap">
    <div class="wrap-cnt">
        <div>
            <div class="wap-tab">
                <span class="active" onclick="openQuestionListByPriority()">全部</span>
                <span onclick="openQuestionListByPriority(1)">A</span>
                <span onclick="openQuestionListByPriority(2)">B</span>
                <span onclick="openQuestionListByPriority(3)">C</span>
                <span onclick="openQuestionListByPriority(4)">D</span>
                <span ><i class="iconfont icon-search"></i></span>
            </div>
            <c:forEach var="vwr" items="${questionList}">
                <div class="index-date">
                    <p>${vwr.groupName}（${vwr.num}条）</p>
                <c:forEach var="vwr1" items="${vwr.listQuery}">
                    <a href='openQuestionDetail(${vwr1.id})'>
                        <span class="index-date_txt">${vwr1.map.deptName}-${vwr1.menuName}</span>
                        <span class="index-date_status">
                            <i class="index-${vwr1.map.priorityString}">${vwr1.map.priorityString}</i>
                            <i class="index-wqr">${vwr1.map.processStr}</i>
                        </span>
                    </a>
                </c:forEach>
                </div>
            </c:forEach>
        </div>
        <div class="hide">
            站点
        </div>
        <div class="hide">
            分享
        </div>
        <div class="hide">
            我的
        </div>
    </div>
    <!--新增-->
    <a href="<%=basePath%>/mobile/wechatSiteQuestion/addPage.do?userId=${userId}&serialNo=${serialNo}&source=1" class="wrap-add" >
        <i class="iconfont icon-add"></i>
    </a>
    <!--底部菜单-->
    <div class="wrap-foot">
        <div class="active">
            <i class="iconfont icon-task"></i>
            任务
        </div>
        <div>
            <i class="iconfont icon-site"></i>
            站点
        </div>
        <div>
            <i class="iconfont icon-upload"></i>
            上传
        </div>
        <div>
            <i class="iconfont icon-wo"></i>
            我
        </div>
    </div>
</div>
<script src="<%=basePath%>resources/mobile/js/jquery-3.3.1.min.js" type="text/javascript"></script>
<script src="<%=basePath%>resources/mobile/js/ims.js" type="text/javascript"></script>
<script type="text/javascript">
    $(function () {
        IMS.menuTab();
        if(${priority != null}){
            setListLevel(${priority});
        }
    })
    function openQuestionDetail(id){
        location.href = "<%=basePath%>mobile/wechatSiteQuestion/goUpdate.do?id="+id+"&serialNo=${serialNo}&userId=${userId}";
    }

    function setListLevel(val) {
        var spanArr = $('.wap-tab>span');
        for (var i = 0; i < spanArr.length; i++) {
            var text = $(spanArr[i]).text().substr(0, 1);
            if (text === val) {
                $(spanArr[i]).addClass('active').siblings('span').removeClass('active');
            }
        }
    }
    /**
     * 点击优先级获取信息
     * @param priority
     */
    function openQuestionListByPriority(priority) {
        location.href = "<%=basePath%>mobile/commons/list.do?serialNo=${serialNo}&userId=${userId}&status=${status}&searchType=${searchType}&searchText=${searchText}&userType=${userType}&priority="+priority;
    }
</script>
</body>
</html>
