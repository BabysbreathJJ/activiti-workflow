<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html lang="en">
<head>
<%@ include file="/common/global.jsp"%>
<title>activiti-demo</title>
<script>
		var logon = ${not empty user};
		if (logon) {
			location.href = '${ctx}/main/index';
		}
	</script>
<%@ include file="/common/meta.jsp"%>
<%@ include file="/common/include-jquery-ui-theme.jsp"%>
<%@ include file="/common/include-base-styles.jsp"%>
<style type="text/css">
.login-center {
	width: 600px;
	margin-left: auto;
	margin-right: auto;
}

#loginContainer {
	margin-top: 3em;
}

.login-input {
	padding: 4px 6px;
	font-size: 14px;
	height: 2.5em;
	margin-left: 20px;
	vertical-align: middle;
	color: #000000;
}

.login-text {
	font-size: 1.5em;
	text-align: right;
}
</style>

<script src="${ctx }/js/common/jquery-1.8.3.js" type="text/javascript"></script>
<script
	src="${ctx }/js/common/plugins/jui/jquery-ui-${themeVersion }.min.js"
	type="text/javascript"></script>
<script type="text/javascript">
	$(function() {
		$('button').button({
			icons: {
				primary: 'ui-icon-key'
			}
		});
	});
	</script>
</head>

<body>
	<div class="site-bg"></div>
	<div class="site-bg-overlay"></div>

	<!-- TOP HEADER -->
	<div class="top-header">
		<div class="container">
			<div class="row">
				<div class="col-md-6 col-sm-6 col-xs-12">
					<p class="phone-info" style="font-size: 1.5em">上海交通大学软件学院</p>
				</div>
				<div class="col-md-6 col-sm-6 col-xs-12">
					<div class="social-icons">
						<ul>
							<li style="color: #5cb48e">信息系统实验室</li>

						</ul>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- .top-header -->
	<div class="container" id="page-content">
		<div class="homepage home-section text-center">
			<div id="loginContainer" class="login-center welcome-text">
				<c:if test="${not empty param.error}">
					<h2 id="error" class="alert alert-error">用户名或密码错误！！！</h2>
				</c:if>
				<c:if test="${not empty param.timeout}">
					<h2 id="error" class="alert alert-error">未登录或超时！！！</h2>
				</c:if>

				<div class="welcome-text">
					<h2>工作流引擎Activiti演示项目</h2>
				</div>
				<hr />
				<form action="${ctx }/user/logon" method="get"
					class="subscribe-form">
					<table>
						<tr style="margin-bottom: 20px">
							<td width="40%" class="login-text">用户名：</td>
							<td><input id="username" name="username" class="login-input"
								placeholder="用户名" /></td>
						</tr>
						<tr>
							<td class="login-text" style="padding-top: 30px">密码：</td>
							<td><input id="password" name="password" type="password"
								class="login-input" style="margin-left: 20px"
								placeholder="默认为：000000" /></td>
						</tr>
						<tr>
							<td>&nbsp;</td>
							<td><input name="submit" type="submit"
								class="button default"
								style="font-size: 17px; margin-top: 20px; margin-left: 60px"
								id="submit" value="登录"></td>
						</tr>
					</table>
				</form>
				<hr />
			</div>
		</div>
	</div>
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
</body>
</html>
