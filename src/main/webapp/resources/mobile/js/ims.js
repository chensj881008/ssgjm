/**
 * 卫宁健康--实施管理工具
 * @Author jinxl
 * @Data 2018/3/1
 * 
 */
const IMS = {
    //初始化
    init: function () {
    	service.init();
    },
    //取消空链接
    cclDefault: function () {
        $(document).on('click', 'a[href=""]', function (e) {
            e.preventDefault();
        });
    },
    /*获取批量被选中的ID
     *@selector:事件源；
     *@scrop：范围
     * */
    getDeleteChk:function(selector,scrop){
    	$(selector).on("click",function(){
    		$("input:checked",scrop).each(function(index,ele){
    			alert(index);
    		});
    	});
    },
     /*全选
     *@selector:事件源,必须是复选框；
     *@scrop：范围
     * */
    checkedAll:function(element,scrop){
    	$(element).on("click","th>input[type='checkbox']",function(){
           	$("#pageContent input[type='checkbox']").prop("checked",$(this).prop("checked"));
      	});
    },
    /*
	 * dropdown
	 * */
	dropDown:function(){
		$(".select").on("click","a",function(){
			$(this).find("i").toggleClass("reverse");
			$(this).next("ul").slideToggle();
		});
		$(".select").on("click","ul>li",function(){
			var _this=$(this),_dropd=_this.parent("ul"), _val=_this.data("val"),_txt=_this.text();
			_dropd.slideToggle();
			_dropd.siblings("[type='hidden']").val(_val);
			_dropd.siblings("a").find("span").text(_txt);
			_dropd.siblings("a").find("i").toggleClass("reverse");
		});
	}
};
/*服务号*/
const service = {
	init:function(){
		this.tabSwitch();
	},
	tabSwitch:function(){
		$(".course-tab li").click(function(){
			$(this).addClass('active')
				.siblings('li')
				.removeClass('active');
			$('.course-tab .btm>div').eq($(this).index())
				.addClass('current')
				.siblings('div')
				.removeClass('current');
		});
	}
};
/*企业号*/
const enterprise = {
	init:function(){
		this.delUpladImg();
		this.largeImg();
	},
	//删除上传图片
	delUpladImg:function(){
		$('.datum-upload').on('click','.icon-close',function(){
			$(this).parent('div').remove();
		});
	},
	largeImg:function(){
		$('.datum').on('click','.datum-upload>div',function(){
			let _img = $(this).find('img');
			if(_img.length>0){
				if(_img.attr('src')){
					$('.large-img>img').attr('src',_img.attr('src'));
					$('.large-img').fadeIn();
				}
			}else{
				$('.large-img').fadeOut();
			}
		});
		
		$('.large-img>span').click(function(){
			enterprise.closeLargeImg();
		})
	},
	closeLargeImg:function(){
		$('.large-img').fadeOut();
	}
};
