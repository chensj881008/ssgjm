<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/header.jsp" %>
<!DOCTYPE html >
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>流程信息</title>
    <meta name="author" content="卫宁实施工具">
    <link rel="stylesheet" href="<%=basePath%>resources/bootstrap/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="<%=basePath%>resources/bootstrap/css/bootstrap-table.min.css"/>
    <link rel="stylesheet" href="<%=basePath%>resources/bootstrap/jquery-treegrid/css/jquery.treegrid.css"/>
    <link rel="stylesheet" href="<%=basePath%>resources/bootstrap/bootstrapValidator/css/bootstrapValidator.min.css"/>
    <link rel="stylesheet" href="<%=basePath%>resources/bootstrap/css/toastr.min.css"/>
    <link rel="stylesheet" href="<%=basePath%>resources/bootstrap/file/css/fileinput.min.css"/>
    <link rel="stylesheet" href="<%=basePath%>resources/assets/css/common.css"/>
    <%--<base href="<%=basePath%>">--%>
    <link rel="shortcut icon" href="<%=basePath%>resources/img/logo.ico"/>
    <style type="text/css">
        .table-align{
            table-layout:fixed;/* 只有定义了表格的布局算法为fixed，下面td的定义才能起作用。 */
            font-size:12px;
        }
        .table-align tr td:nth-child(3),
        .table-align tr td:nth-child(4),
        .table-align tr td:nth-child(6) {
            word-break:keep-all;/* 不换行 */
            white-space:nowrap;/* 不换行 */
            overflow:hidden;/* 内容超出宽度时隐藏超出部分的内容 */
            text-overflow:ellipsis;/* 当对象内文本溢出时显示省略标记(...) ；需与overflow:hidden;一起使用。*/
            font-size:12px;
        }
        .table-align tr td:nth-child(5) {
            word-break:keep-all;/* 不换行 */
            white-space:nowrap;/* 不换行 */
            overflow:hidden;/* 内容超出宽度时隐藏超出部分的内容 */
            text-overflow:ellipsis;/* 当对象内文本溢出时显示省略标记(...) ；需与overflow:hidden;一起使用。*/
            font-size:12px;
        }
        .error_font{
            color: red;
            font-size: 12px;
        }
    </style>
</head>
<body>
<div class="container-fluid">
    <div class="row text-center" id="queryScope">
        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
            <div class="col-xs-4 col-sm-4 col-md-4 col-lg-4 div-style text-center">
                <label class="col-sm-4 form-text" for="flowQName">流程名称：</label>
                <input type="text" class="input-style" id="flowQName"/>
            </div>
            <div class="col-xs-4 col-sm-4 col-md-4 col-lg-4 div-style text-center">
                <label class="col-sm-4 form-text" for="flowQCode">流程编码：</label>
                <input type="text" class="input-style" id="flowQCode"/>
            </div>
            <div class="col-xs-4 col-sm-4 col-md-4 col-lg-4 div-style text-center">
                <button type="button" class="btn btn-success btn-sm" id="query">
                    <span class="glyphicon glyphicon-search"></span>
                    查询
                </button>
            </div>
        </div>
    </div>
    <!--表格区域  -->
    <table id="infoTable" class="table-align"></table>
    <!--toolbar区域  -->
    <div class="btn-group" id="btntoolbar">
        <button id="add" class="btn btn-primary" type="button"><span class="glyphicon glyphicon-plus"></span>增加</button>
    </div>
    <!--模态框  -->
    <div class="modal fade" id="flowModal" tabindex="-1" role="dialog" aria-labelledby="flowFormModal">
        <div class="modal-dialog" role="document">
            <div class="modal-content" style="width:90%;">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">×</span>
                    </button>
                    <h4 class="modal-title" id="myModalLabel">流程信息</h4>
                </div>
                <div class="modal-body">
                    <div class="container">
                        <div class="row">
                            <form class="form-horizontal col-lg-6 col-md-6 col-sm-6 col-xs-6" role="form" id="flowForm">
                                <div class="form-group" id="flowTypeDiv">
                                    <label class="col-sm-3 control-label" for="flowType">流程类型</label>
                                    <div class="col-sm-6">
                                      <select class="form-control" name="flowType" id="flowType">
                                          <option value="0">流程大类</option>
                                          <option value="1">流程小类</option>
                                          <option value="2">流程方案</option>
                                          <option value="3">配置脚本</option>
                                      </select>
                                    </div>
                                </div>
                                <div id='flowParent'>
                                    <div class="form-group">
                                        <label class="col-sm-3 control-label" for="flowParentCode" id="flowParentCodeLabel">上级流程编号</label>
                                        <div class="col-sm-6">
                                            <input type="text" class="form-control" id="flowParentCode" name="flowParentCode"
                                                   data-provide="typeahead" placeholder="请输入上级流程编号">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="col-sm-3 control-label" for="flowParentName" id="flowParentNameLabel">上级流程名称</label>
                                        <div class="col-sm-6">
                                            <input type="text" class="form-control" id="flowParentName" name="flowParentName" readonly="true"
                                                   placeholder="请输入上级流程名称">
                                        </div>
                                    </div>
                                </div>
                                <div id="flowCodeDiv">
                                    <div class="form-group">
                                        <label class="col-sm-3 control-label" for="flowCode" id="flowCodeLabel">流程编号</label>
                                        <div class="col-sm-6">
                                            <input type="text" class="form-control" id="flowCode" name="flowCode"
                                                   placeholder="请输入流程编号" readonly>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-group" id="dbTypeDiv">
                                    <label class="col-sm-3 control-label" for="dbType">数据库类型</label>
                                    <div class="col-sm-6">
                                        <select class="form-control" id="dbType" name="dbType">
                                            <option value="1">HIS</option>
                                            <option value="2">CISDB</option>
                                            <option value="3">CISDB_DATA</option>
                                            <option value="4">NIS</option>
                                            <option value="5">BQHS</option>
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-3 control-label" for="flowName" id="flowNameLabel">流程名称</label>
                                    <div class="col-sm-6">
                                        <input type="text" class="form-control" id="flowName" name="flowName"
                                               placeholder="请输入流程名称">
                                    </div>
                                </div>
                                <div class="form-group" id="procDiv">
                                    <label class="col-sm-3 control-label" for="procName" id="procNameLabel">存储名称</label>
                                    <div class="col-sm-6">
                                        <input type="text" class="form-control" id="procName" name="procName"
                                               placeholder="请输入存储名称">
                                    </div>
                                </div>
                                <div class="form-group" id="isMustDiv">
                                    <label class="col-sm-3 control-label" for="isMust">是否必须</label>
                                    <div class="col-sm-6">
                                        <select class="form-control" id="isMust" name="isMust">
                                            <option value="0">否</option>
                                            <option value="1">是</option>
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group" id="flowDescDiv">
                                    <label class="col-sm-3 control-label" for="flowDesc" id="flowDescLabel">流程描述</label>
                                    <div class="col-sm-6">
                                        <textarea class="form-control" id="flowDesc" name="flowDesc" rows="5"
                                                  placeholder="请输入流程描述"></textarea>
                                    </div>
                                </div>
                                <div class="form-group" id="contentDescDiv">
                                    <label class="col-sm-3 control-label" for="contentDesc" id="contentDescLabel">详细说明</label>
                                    <div class="col-sm-6">
                                        <textarea class="form-control" id="contentDesc" name="contentDesc" rows="5"
                                                  placeholder="请输入详细说明"></textarea>
                                    </div>
                                </div>

                                <div class="form-group" id="uploadFileDiv">
                                    <label class="col-sm-3 control-label" for="remotePath">调研问卷</label>
                                    <div class="col-sm-6">
                                        <input name="uploadFile" type="file" class="file" id="uploadFile">
                                    </div>
                                </div>
                                <div class="col-sm-8 text-center">
                                    <button class="btn btn-primary" id="saveFlow" type="button">保存</button>
                                    <button class="btn btn-danger" data-dismiss="modal">取消</button>
                                </div>
                                <input type="hidden" name="id" id="id">
                                <input type="hidden" name="vid" id="vid">
                                <input type="hidden" name="flowPid" id="flowPid">
                                <input type="hidden" name="remotePath" id="remotePath">
                                <input type="reset" style="display:none;"/>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
<script type="text/javascript" src="<%=basePath%>resources/bootstrap/js/jquery-1.12.4.min.js"></script>
<script type="text/javascript" src="<%=basePath%>resources/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="<%=basePath%>resources/bootstrap/js/bootstrap-table.min.js"></script>
<script type="text/javascript" src="<%=basePath%>resources/bootstrap/js/bootstrap-table-zh-CN.min.js"></script>
<script type="text/javascript" src="<%=basePath%>resources/bootstrap/js/bootstrap-table-treegrid.min.js"></script>
<script type="text/javascript" src="<%=basePath%>resources/bootstrap/jquery-treegrid/js/jquery.treegrid.min.js"></script>
<script type="text/javascript" src="<%=basePath%>resources/bootstrap/bootstrapValidator/js/bootstrapValidator.min.js"></script>
<script type="text/javascript" src="<%=basePath%>resources/bootstrap/bootstrapValidator/js/zh_CN.js"></script>
<script type="text/javascript" src="<%=basePath%>resources/bootstrap/js/toastr.min.js"></script>
<script type="text/javascript" src="<%=basePath%>resources/bootstrap/js/bootstrap3-typeahead.min.js"></script>
<script type="text/javascript" src="<%=basePath%>resources/bootstrap/file/js/fileinput.min.js"></script>
<script type="text/javascript" src="<%=basePath%>resources/bootstrap/file/js/zh.js"></script>
<script type="text/javascript" src="<%=basePath%>resources/js/common.js"></script>
<script type="text/javascript" src="<%=basePath%>resources/js/auth/module/flowinfo.js"></script>
</html>