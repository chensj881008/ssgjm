<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/header.jsp" %>
<!doctype html>
<html lang="en">

	<head>
		<meta charset="UTF-8" />
		<title>课程学习-详情页</title>
		<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
		<link rel="stylesheet" type="text/css" href="<%=basePath%>resources/mobile/css/normalize.css" />
		<link rel="stylesheet" type="text/css" href="<%=basePath%>resources/mobile/css/service.css" />
		<link rel="stylesheet" type="text/css" href="<%=basePath%>resources/mobile/css/mui.min.css" />
		<link rel="stylesheet" type="text/css" href="<%=basePath%>resources/mobile/css/common.css" />
	</head>

	<body>
		<div class="mui-content detail">
			<!--header-->
			<div class="header">
				<span class="mui-icon mui-icon-arrowleft" onclick="history.go(-1)"></span>
				<div>站点安装登记</div>
				<span class="mui-icon mui-icon-more"></span>
			</div>
			<div class="hole"></div>
			<div class="detail-play">
				<video width="100%" height="200" controls>

					<source src="<%=basePath%>shareFolder${repo.remotePath}" type="video/mp4">
					<source src="<%=basePath%>shareFolder${repo.remotePath}" type="video/avi">
					<source src="<%=basePath%>shareFolder${repo.remotePath}" type="video/wmv">

					您的浏览器不支持 HTML5 video 标签。
				</video>
				<div class="course-tab">
					<div class="btm">
						<dl class="item">
							<dd class="item-title">医生接诊及诊断录入<span>-门诊医生站</span></dd>
							<dd class="item-time">视频时长 <span>04:06</span></dd>
							<dd class="item-count">
								<span>学习  <strong>1</strong>次</span>
							</dd>
						</dl>
					</div>
				</div>
			</div>
			<div class="detail-support">相关视频</div>
			<!--tab-->
			<div class="course-tab">
				<div class="btm">
					<dl class="item">
						<dt><img src="<%=basePath%>resources/mobile/images/video.png"/></dt>
						<dd class="item-title">医生接诊及诊断录入<span>-门诊医生站</span></dd>
						<dd class="item-time">视频时长 <span>04:06</span></dd>
						<dd class="item-count">
							<span>学习  <strong>1</strong>次</span>
						</dd>
					</dl>
					<dl class="item">
						<dt><img src="<%=basePath%>resources/mobile/images/video.png"/></dt>
						<dd class="item-title">医生接诊及诊断录入<span>-门诊医生站</span></dd>
						<dd class="item-time">视频时长 <span>04:06</span></dd>
						<dd class="item-count">
							<span>学习  <strong>0</strong>次</span>
						</dd>
					</dl>
				</div>
			</div>
		</div>
		<ul>
		</ul>
		<script src="<%=basePath%>resources/mobile/js/mui.js" type="text/javascript" charset="utf-8"></script>
		<script type="text/javascript">
			mui.init()
		</script>
	</body>

</html>