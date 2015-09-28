<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html lang="en">
<head>
<%@ include file="/common/global.jsp"%>
<title>请假申请</title>
<%@ include file="/common/meta.jsp"%>
<%@ include file="/common/include-base-styles.jsp"%>
<%@ include file="/common/include-jquery-ui-theme.jsp"%>
<link
	href="${ctx }/js/common/plugins/jui/extends/timepicker/jquery-ui-timepicker-addon.css"
	type="text/css" rel="stylesheet" />

<script src="${ctx }/js/common/jquery-1.8.3.js" type="text/javascript"></script>
<script
	src="${ctx }/js/common/plugins/jui/jquery-ui-${themeVersion }.min.js"
	type="text/javascript"></script>
<script
	src="${ctx }/js/common/plugins/jui/extends/timepicker/jquery-ui-timepicker-addon.js"
	type="text/javascript"></script>
<script
	src="${ctx }/js/common/plugins/jui/extends/i18n/jquery-ui-date_time-picker-zh-CN.js"
	type="text/javascript"></script>
<script type="text/javascript">
	$(function() {
		$('#startTime,#endTime').datetimepicker({
			stepMinute : 5
		});
	});
</script>
<style type="text/css">
</style>
</head>

<body style="background-color:#EAEAEA;">
	<div class="container showgrid">
		<c:if test="${not empty message}">
			<div id="message" class="alert alert-success">${message}</div>
			<!-- 自动隐藏提示信息 -->
			<script type="text/javascript">
				setTimeout(function() {
					$('#message').hide('slow');
				}, 5000);
			</script>
		</c:if>
		<c:if test="${not empty error}">
			<div id="error" class="alert alert-error">${error}</div>
			<!-- 自动隐藏提示信息 -->
			<script type="text/javascript">
				setTimeout(function() {
					$('#error').hide('slow');
				}, 5000);
			</script>
		</c:if>
		<form:form id="inputForm" action="${ctx}/oa/leave/start" method="post"
			style="background-color:#ffffff;margin: 0 auto;min-height:1000px">
			<fieldset>
				<legend style="padding-top: 15px; padding-bottom: 15px;">
					<h1 style="font-size: 2em; text-align: center; color: #218868">请假申请</h1>
				</legend>
				<table border="1" class="table"
					style="width: 60%; margin: 0 auto; font-size: 1.2em">
					<tr>
						<td
							style="color: #218868; width: 40%; text-align: right; vertical-align: middle">请假类型：</td>
						<td><select id="leaveType" name="leaveType"
							class="form-control" style="width: 200px">
								<option>公休</option>
								<option>病假</option>
								<option>调休</option>
								<option>事假</option>
								<option>婚假</option>
						</select></td>
					</tr>
					<tr>
						<td
							style="color: #218868; width: 40%; text-align: right; vertical-align: middle">开始时间：</td>
						<td><input type="text" id="startTime" name="startTime"
							class="form-control" style="width: 200px" /></td>
					</tr>
					<tr>
						<td
							style="color: #218868; width: 40%; text-align: right; vertical-align: middle">结束时间：</td>
						<td><input type="text" id="endTime" name="endTime"
							class="form-control" style="width: 200px" /></td>
					</tr>
					<tr>
						<td
							style="color: #218868; width: 40%; text-align: right; vertical-align: middle">请假原因：</td>
						<td><textarea name="reason"
								style="color: #000000; width: 300px; resize: none"></textarea></td>
					</tr>
					<tr>
						<td colspan=2 style="text-align:center">
							<button class="btn btn-success" style="width: 150px;"
								type="submmit">申请</button>
						</td>
					</tr>
				</table>
			</fieldset>
		</form:form>
	</div>

</body>
</html>
