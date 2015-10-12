<?xml version="1.0" encoding="UTF-8" ?>
<%@page import="me.kafeitu.demo.activiti.util.PropertyFileUtil"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!doctype html>
<html lang="en">
<head>
<%@ include file="/common/global.jsp"%>
<%@ include file="/common/meta.jsp"%>

<%@ include file="/common/include-base-styles.jsp"%>
<%@ include file="/common/include-jquery-ui-theme.jsp"%>
<link
	href="${ctx }/js/common/plugins/jui/extends/portlet/jquery.portlet.min.css?v=1.1.2"
	type="text/css" rel="stylesheet" />
<link href="${ctx }/js/common/plugins/qtip/jquery.qtip.css?v=1.1.2"
	type="text/css" rel="stylesheet" />
<%@ include file="/common/include-custom-styles.jsp"%>
<style type="text/css">
.template {
	display: none;
}

.version {
	margin-left: 0.5em;
	margin-right: 0.5em;
}

.trace {
	margin-right: 0.5em;
}

.center {
	width: 1200px;
	margin-left: auto;
	margin-right: auto;
}
th, td {
	text-align: center;
}

td {
	color: #000000;
	vertical-align: middle;
}

</style>

<script src="${ctx }/js/common/jquery-1.8.3.js" type="text/javascript"></script>
<script
	src="${ctx }/js/common/plugins/jui/jquery-ui-${themeVersion }.min.js"
	type="text/javascript"></script>
<script
	src="${ctx }/js/common/plugins/jui/extends/portlet/jquery.portlet.pack.js?v=1.1.2"
	type="text/javascript"></script>
<script src="${ctx }/js/common/plugins/qtip/jquery.qtip.pack.js"
	type="text/javascript"></script>
<script src="${ctx }/js/common/plugins/html/jquery.outerhtml.js"
	type="text/javascript"></script>
<script src="${ctx }/js/module/activiti/workflow.js"
	type="text/javascript"></script>
<script src="${ctx }/js/module/main/welcome-portlet.js"
	type="text/javascript"></script>
</head>
<body style="background-color: #EAEAEA;width:100%">
	<div
		style="width: 100%; background-color: #ffffff; margin: 0 auto; text-align: center">
		<table class="table table-bordered" id="todoTasks" style="color: #000000">

		</table>
	</div>
</body>
</html>
