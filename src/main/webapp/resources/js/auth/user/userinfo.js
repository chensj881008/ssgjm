/**
 * 用户信息js
 * @author chensj
 * @version 1.0.0
 */



$(function () {
    /**
     * 查询
     * @constructor
     */
    function SearchData(){
        $('#userTable').bootstrapTable('refresh', { pageNumber: 1 });
    }

    /**
     *  查询参数信息
     * @param params
     * @returns {{count: *|number, first, sort, order, yhmc: *|string, userid: *|string, mobile: *|string}}
     */
    function queryParams(params) {
        return {
            count: params.limit,    // 每页显示条数
            first: params.offset,   // 显示条数
            sort: params.sort,      // 排序列名
            order: params.order,     // 排位命令（desc，asc）
            yhmc: $.trim($('#cName').val()),
            userid: $.trim($('#userCard').val()),
            mobile: $.trim($('#telephone').val())
        };
    }

    $('#userTable').bootstrapTable({
        url: Common.getRootPath() + '/admin/user/list.do',// 要请求数据的文件路径
        method: 'GET', // 请求方法
        // contentType: "application/x-www-form-urlencoded",//必须要有！！！！ POST必须有
        cache: false,                       // 是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: true,                   // 是否显示分页（*）
        sortable: true,                     // 是否启用排序
        sortOrder: "asc",                   // 排序方式
        sidePagination: "server",           // 分页方式：client客户端分页，server服务端分页（*）
        pageNumber: 1,                      // 初始化加载第一页，默认第一页,并记录
        pageSize: 10,                     // 每页的记录行数（*）
        pageList: [10, 25, 50, 100],        // 可供选择的每页的行数（*）
        showPaginationSwitch: false,			//显示 数据条数选择框
        search: false,                       // 是否显示表格搜索
        strictSearch: true,
        showColumns: false,                  // 是否显示所有的列（选择显示的列）
        showRefresh: false,                  // 是否显示刷新按钮
        minimumCountColumns: 2,             // 最少允许的列数
        clickToSelect: true,                // 是否启用点击选中行
        idField: 'id',
        sortName: 'id',
        uniqueId: "id",                 // 每一行的唯一标识，一般为主键列
        //showToggle: true,                   // 是否显示详细视图和列表视图的切换按钮
        cardView: false,                    // 是否显示详细视图
        detailView: false,                  // 是否显示父子表
        toolbar: '#btntoolbar',
        toolbarAlign: 'right',
        paginationPreText: '上一页',
        paginationNextText: '下一页',
        paginationLoop: false, //分页条无限循环的功能
        singleSelect: true,
        selectItemName: '单选框',
        /*showColumns:true,           //内容列下拉框  */
        // 得到查询的参数
        queryParams:queryParams,
        columns: [{
            checkbox: true,
            align: 'center',
            valign: 'middle',
            title: '单选框',
            halign: 'middle',
            width: '10px',
        }, {
            field: "id",
            title: "ID",
            width: '40px',
            align: 'center'
        }, {
            field: "userid",
            title: "登录名",
            width: '40px',
            align: 'center'
        }, {
            field: "yhmc",
            title: "用户名称",
            width: '40px',
            align: 'center'
        }, {
            field: "name",
            title: "用户姓名",
            width: '40px',
            align: 'center'
        }, {
            field: "email",
            title: "邮箱",
            width: '40px',
            align: 'center'
        }, {
            field: 'mobile',
            title: '手机号码',
            width: '40px',
            align: 'center'
        }, {
            field: 'status',
            title: '允许登陆',
            width: '40px',
            formatter: function (value) {
                if (value == '1') {
                    return '是';
                } else if (value = '1') {
                    return '否';
                }
            },
            align: 'center'
        }, {
            field: 'userType',
            title: '用户类型',
            width: '40px',
            formatter: function (value) {
                if (value == '1') {
                    return '公司';
                } else {
                    return '医院';
                }
            },
            align: 'center'
        }, {
            title: '操作',
            field: 'id',
            align: 'center',
            width: '80px',
            formatter: function (value, row, index) {
                var e = '<a href="####" class="btn btn-info btn-xs" name="edit" mce_href="#" aid="' + row.userId + '">编辑</a> ';
                var d = '<a href="####" class="btn btn-danger btn-xs" name="delete" mce_href="#" aid="' + row.userId + '">删除</a> ';
                return e + d;
            }
        }],
    });

    $('#queryUser').on('click',SearchData);

    /**
     * 新增用户
     * 需要清理表格数据
     */
    $('#addUser').on('click', function () {
        $("input[type=reset]").trigger("click");
        $('#userId').val('');
        $('#password').val('');
        $('#userModal').modal('show');
    });
    /**
     * 修改用户
     * 只能修改一条数据
     */
    $('#modifyUser').on('click', function () {
        var arrselections = $("#userTable").bootstrapTable('getSelections');
        if (arrselections.length > 1) {
            Ewin.alert('只能选择一行进行编辑');
            return;
        }
        if (arrselections.length <= 0) {
            Ewin.alert('请选择有效数据');
            return;
        }
        var userId = arrselections[0].id;
        $.ajax({
            url: Common.getRootPath() + '/admin/user/getById.do',
            data: {'userId': userId},
            type: "post",
            dataType: 'json',
            async: false,
            success: function (result) {
                var _result = eval(result);
                if (_result.status == Common.SUCCESS) {
                    $('#userForm').initForm(_result.data);
                    $('#orgid').val(_result.data.orgid);
                    $('#password').val(_result.data.password);
                    $('#userModal').modal('show');
                }

            }
        });
    });
    /**
     * 列表中按钮
     *   编辑用户信息
     */
    $('#userTable').on('click', 'a[name="edit"]', function (e) {
        e.preventDefault();
        var userId = $(this).attr('aid');
        $.ajax({
            url: Common.getRootPath() + '/admin/user/getById.do',
            data: {'userId': userId},
            type: "post",
            dataType: 'json',
            async: false,
            success: function (result) {
                var _result = eval(result);
                if (_result.status == Common.SUCCESS) {
                    $('#userForm').initForm(_result.data);
                    $('#orgid').val(_result.data.orgid);
                    $('#password').val(_result.data.password);
                    $('#userModal').modal('show');
                }

            }
        });
    });
    /**
     * 列表中按钮
     *   删除用户信息
     */
    $('#userTable').on('click', 'a[name="delete"]', function (e) {
        e.preventDefault();
        var userId = $(this).attr('aid');
        Ewin.confirm({message: "确认要删除选择的数据吗？"}).on(function (e) {
            if (!e) {
                return;
            }
            $.ajax({
                type: "post",
                url: Common.getRootPath() + '/admin/user/deleteById.do',
                data: {"userId": userId},
                dataType: 'json',
                success: function (data, status) {
                    if (status == Common.SUCCESS) {
                        toastr.success('提交数据成功');
                        $("#userTable").bootstrapTable('refresh');
                    }
                },
                error: function () {
                    toastr.error('Error');
                },
                complete: function () {
                }
            });
        });
    });
    /**
     * 删除用户
     * 只能删除一条数据
     */
    $('#deleteUser').on('click', function () {
        var arrselections = $("#userTable").bootstrapTable('getSelections');
        if (arrselections.length > 1) {
            Ewin.alert('只能选择一行进行编辑');
            return;
        }
        if (arrselections.length <= 0) {
            Ewin.alert('请选择有效数据');
            return;
        }
        var userId = arrselections[0].userId;
        Ewin.confirm({message: "确认要删除选择的数据吗？"}).on(function (e) {
            if (!e) {
                return;
            }
            $.ajax({
                type: "post",
                url: Common.getRootPath() + '/admin/user/deleteById.do',
                data: {"userId": userId},
                dataType: 'json',
                success: function (data, status) {
                    if (status == Common.SUCCESS) {
                        Ewin.alert('提交数据成功');
                        $("#userTable").bootstrapTable('refresh');
                    }
                },
                error: function () {
                    Ewin.alert('Error');
                },
                complete: function () {
                }
            });
        });
    });
    /**
     * 保存用户按钮
     * 通过隐藏域判断用户是否存在，而使用不同的方法进行新增或者修改
     */
    $('#saveUser').on('click', function (e) {
        //阻止默认行为
        e.preventDefault();
        var bootstrapValidator = $("#userForm").data('bootstrapValidator');
        //修复记忆的组件不验证
        if (bootstrapValidator) {
            bootstrapValidator.validate();
        }
        var url = '';
        if ($('#orgid').val().length == 0) {
            url = Common.getRootPath() + '/admin/user/add.do';
        } else {
            url = Common.getRootPath() + '/admin/user/update.do';
        }
        if (bootstrapValidator.isValid()) {
            $.ajax({
                url: url,
                data: $("#userForm").serialize(),
                type: "post",
                dataType: 'json',
                async: false,
                success: function (result) {
                    var _result = eval(result);
                    if (_result.status == Common.SUCCESS) {
                        $('#userModal').modal('hide');
                        $("#userTable").bootstrapTable('refresh');
                    }

                }
            });
        }
    });


    //表单验证
    //this._changeEvent = (ieVersion === 9 || !('oninput' in el)) ? 'keyup' : 'input'; 源码修改
    //this._changeEvent = (ieVersion === 9 || !('onblur' in el)) ? 'keyup' : 'blur';
    $('#userForm').bootstrapValidator({
        message: '输入的值不符合规格',
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            userid: {
                message: '登录名验证失败',
                validators: {
                    notEmpty: {
                        message: '登录名不能为空'
                    },
                    stringLength: {
                        min: 2,
                        max: 18,
                        message: '登录名长度必须在2到18位之间'
                    },
                    regexp: {
                        regexp: /^[a-zA-Z0-9_]+$/,
                        message: '登录名只能包含大写、小写、数字和下划线'
                    }
                }
            },
            yhmc: {
                validators: {
                    notEmpty: {
                        message: '用户名称不能为空'
                    },
                    stringLength: {
                        min: 2,
                        max: 10,
                        message: '用户名称长度必须在2到10位之间'
                    }
                }
            },
            mobile : {
                validators: {
                    notEmpty: {
                        message: '手机号码不能为空'
                    },
                    stringLength: {
                        min: 11,
                        max: 11,
                        message: '手机号码长度必须为11位'
                    },
                    regexp: {
                        regexp: /^1[3|4|5|8][0-9]\d{4,8}$/,
                        message: '手机号码只能包含数字'
                    }
                }
            },
            email: {
                validators: {
                    notEmpty: {
                        message: '邮箱地址不能为空'
                    },
                    emailAddress: {
                        message: '邮箱地址格式有误'
                    }
                }
            }
        }
    });

});
