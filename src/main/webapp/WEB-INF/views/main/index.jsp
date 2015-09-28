<%@page import="me.kafeitu.demo.activiti.util.PropertyFileUtil"%>
<%@page
	import="org.springframework.beans.factory.config.PropertiesFactoryBean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
	PropertyFileUtil.init();
%>
<html lang="en">
<head>
<%@ include file="/common/global.jsp"%>
<!-- <script>
		var notLogon = ${empty user};
		if (notLogon) {
			location.href = '${ctx}/login?timeout=true';
		}
</script> -->


<%@ include file="/common/meta.jsp"%>
<title>Activiti-演示系统</title>
<%@ include file="/common/include-base-styles.jsp"%>
<%@ include file="/common/include-jquery-ui-theme.jsp"%>
<link rel="stylesheet" type="text/css" href="${ctx }/css/menu.css" />
<%@ include file="/common/include-custom-styles.jsp"%>
<link href="${ctx }/css/main.css" type="text/css" rel="stylesheet" />
<style type="text/css">
.ui-tabs-panel {
	height: 100%;
	width: 100%;
}

.ui-tabs .ui-tabs-nav li a {
	padding-right: .5em;
}

#tabs li .ui-icon-close {
	float: left;
	margin: 0.5em 0.2em 0 0;
	cursor: pointer;
}

#add_tab {
	cursor: pointer;
}

/* .top-header {
	position: absolute;
	margin: 0px;
	top: 0px;
	bottom: auto;
	left: 0px;
	right: 0px;
	width: auto;
	z-index: 0;
	height: 45px;
	display: block;
	visibility: visible;
} */
</style>

<script src="${ctx }/js/common/jquery-1.8.3.js" type="text/javascript"></script>

<%-- <script src="${ctx }/js/common/jquery.min.js" type="text/javascript"></script> --%>
<script
	src="${ctx }/js/common/plugins/jui/jquery-ui-${themeVersion }.min.js"
	type="text/javascript"></script>
<script
	src="${ctx }/js/common/plugins/jui/extends/themeswitcher/jquery.ui.switcher.js"
	type="text/javascript"></script>
<script src="${ctx }/js/common/plugins/tools/jquery.cookie.js"
	type="text/javascript"></script>
<script
	src="${ctx }/js/common/plugins/jui/extends/layout/jquery.layout.min.js?v=1.3"
	type="text/javascript"></script>
<script src='${ctx }/js/common/common.js' type="text/javascript"></script>
<script src='${ctx }/js/module/main/main-frame.js'
	type="text/javascript"></script>
<script src='${ctx }/js/common/bootstrap.js' type="text/javascript"></script>
<!-- <script type="text/javascript">
	$(document).ready(function() {
		
		function getAutoDelegateTasks() {
			
			$.ajax({
				url : ctx + '/workflow/task/todo/list',
				type : "get",
				contentType : 'application/json',
				dataType : 'json',
				success : function(data) {
					/* if (data.length > myTasks) */
						$('#messages').html(data.length);
				}
			});

			/* setTimeout(getAutoDelegateTasks(), 50); */
		}
		/* getAutoDelegateTasks(); */
		setTimeout(getAutoDelegateTasks(), 50);
	})
</script> -->
<script type="text/javascript"
	src="${ctx }/js/common/sockjs-0.3.min.js"></script>
<script>
	var websocket;
	if ('WebSocket' in window) {
		websocket = new WebSocket("ws://localhost:8080/webSocketServer");
	} else if ('MozWebSocket' in window) {
		websocket = new MozWebSocket(
				"ws://localhost:8080/webSocketServer");
	} else {
		websocket = new SockJS(
				"http://localhost:8080/sockjs/webSocketServer");
	}
	websocket.onopen = function(evnt) {
	};
	websocket.onmessage = function(evnt) {
		$('#messages').html(evnt.data);
	};
	websocket.onerror = function(evnt) {
	};
	websocket.onclose = function(evnt) {
	}
</script>
</head>
<body>
	<!-- #TopPane -->

	<!-- TOP HEADER -->
	<div class="top-header">
		<div class="container">
			<div class="row">
				<div class="col-md-6 col-sm-6 col-xs-12" style="width: 20%">
					<p class="phone-info">
						<a href="#" style="font-size: 2.2em">Activiti</a>
					</p>
				</div>
				<!--下拉列表  -->
				<div class="col-md-6 col-sm-6 col-xs-12"
					style="width: 80%; text-align: right">
					<span style="padding-left: 10px;">欢迎：</span> <span
						title="角色：${groupNames }">${user.firstName }
						${user.lastName }/${user.id }</span><a href="#" id="loginOut"
						style="margin-left: 15px">安全退出</a>
				</div>
				<!-- 下拉列表结束 -->

			</div>
		</div>
	</div>
	<!-- .top-header -->

	<!-- .top-header -->
	<div id="topPane" class="ui-layout-north"></div>



	<!-- RightPane -->
	<div id="centerPane"
		class="ui-layout-center ui-helper-reset ui-widget-content">
		<div id="tabs">
			<ul>
				<li><a class="tabs-title" href="#tab-index">首页</a><span
					class='ui-icon ui-icon-close' title='关闭标签页'></span></li>
			</ul>
			<div id="newMessages" style="color: #000000;">

				<div id="messages" class="alert alert-success"
					style="margin-bottom: 0px"></div>

			</div>
			<div id="tab-index">
				<iframe id="mainIframe" name="mainIframe" src="welcome"
					class="module-iframe" scrolling="auto" frameborder="0"
					style="width: 100%; height: 100%;"></iframe>
			</div>
		</div>
	</div>

	<!-- #BottomPane -->
	<!-- SITE-FOOTER -->
	<div class="site-footer"
		style="position: absolute; margin: 0px; top: auto; bottom: 0px; left: 0px; right: 0px; width: auto; z-index: 0; height: 45px; display: block; visibility: visible;">
		<div class="container">
			<div class="row">
				<div class="col-md-12 text-center">
					<p>
						Copyright &copy; 上海交通大学 | SJTU <a href="#" target="_blank">软件学院</a>
						- 实验室 <a href="#" title="网页模板" target="_blank">信息系统实验室</a>
					</p>
				</div>
			</div>
		</div>
	</div>
	<!-- .site-footer -->
	<%@ include file="menu.jsp"%>
	<div id="themeswitcherDialog">
		<div id="themeSwitcher"></div>
	</div>
</body>
</html>
