<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html>
<head>
<%@ include file="/common/global.jsp"%>


<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="author" content="leipi.org">
<link href="${ctx }/css/form-design/bootstrap/css/bootstrap.css?2024"
	rel="stylesheet" type="text/css" />
<!--[if lte IE 6]>
    <link rel="stylesheet" type="text/css" href="${ctx }/css/form-design/bootstrap/css/bootstrap-ie6.css?2024">
    <![endif]-->
<!--[if lte IE 7]>
    <link rel="stylesheet" type="text/css" href="${ctx }/css/form-design/bootstrap/css/ie.css?2024">
    <![endif]-->
<link href="${ctx }/css/form-design/site.css?2024" rel="stylesheet"
	type="text/css" />


<style>
#components {
	min-height: 600px;
}

#target {
	min-height: 200px;
	border: 1px solid #ccc;
	padding: 5px;
}

#target .component {
	border: 1px solid #fff;
}

#temp {
	width: 500px;
	background: white;
	border: 1px dotted #ccc;
	border-radius: 10px;
}

.popover-content form {
	margin: 0 auto;
	width: 213px;
}

.popover-content form .btn {
	margin-right: 10px
}

#source {
	min-height: 500px;
}

.read_only {
	line-height: 30px;
	vertical-align: middle;
}

a {
	color: #5cb48e;
	-webkit-transition: all 150ms ease-in-out;
	-moz-transition: all 150ms ease-in-out;
	-ms-transition: all 150ms ease-in-out;
	-o-transition: all 150ms ease-in-out;
	transition: all 150ms ease-in-out;
	text-decoration: none !important;
}
</style>


</head>
<body>






	<div class="container">
		<div class="row clearfix">
			<div class="span6">
				<div class="clearfix">

					<h2>我的表单</h2>

					<div>
						<div style="float: left; width: 80%" id="formNames"></div>
						<a style="float: right;line-height:35px; width: 20%"
							href="${ctx}/workflow/model/list">返回模型列表</a>
					</div>
					<hr>
					<div id="build">
						<form id="target" class="form-horizontal">
							<fieldset></fieldset>

						</form>
					</div>
				</div>
				<div style="float: right">
					<button class='btn btn-success' id="uploadFile">确定</button>
					<button class='btn btn-success' id="deployFile">部署</button>
				</div>
			</div>

			<div class="span6">
				<h2>拖拽下面的控件到左侧</h2>
				<hr>
				<div class="tabbable">
					<ul class="nav nav-tabs" id="navtab">
						<li class="active"><a href="#1" data-toggle="tab">常用控件</a></li>
						<li class><a href="#2" data-toggle="tab">定制控件</a></li>
						<li class><a id="sourcetab" href="#5" data-toggle="tab">源代码</a></li>
					</ul>
					<form class="form-horizontal" id="components">
						<fieldset>
							<div class="tab-content">

								<div class="tab-pane active" id="1">

									<!-- Text start -->
									<div class="control-group component" rel="popover" title="文本控件"
										trigger="manual"
										data-content="
  <form class='form'>
    <div class='controls'>
      <label class='control-label'>控件名称</label> <input type='text' id='orgname' placeholder='必填项'>
      <label class='control-label'>默认值</label> <input type='text' id='orgvalue' placeholder='默认值'>
      <hr/>
      <button class='btn btn-info' type='button'>确定</button><button class='btn btn-danger' type='button'>取消</button>
    </div>
  </form>">
										<!-- Text -->
										<label class="control-label leipiplugins-orgname">文本域</label>
										<div class="controls designText">
											<span class="leipiplugins read_only" title="文本域"
												leipiplugins="text">默认值</span>
										</div>

									</div>
									<!-- Text end -->

									<!-- Text input start -->
									<div class="control-group component" rel="popover"
										title="文本框控件" trigger="manual"
										data-content="
  <form class='form'>
    <div class='controls'>
      <label class='control-label'>控件名称</label> <input type='text' id='orgname' placeholder='必填项'>
      <label class='control-label'>默认值</label> <input type='text' id='orgvalue' placeholder='默认值'>
      <label class='control-label'>数据库字段名</label> <input type='text' id='orgdataname' placeholder='默认值'>
      <hr/>
      <button class='btn btn-info' type='button'>确定</button><button class='btn btn-danger' type='button'>取消</button>
    </div>
  </form>">
										<!-- Text -->
										<label class="control-label leipiplugins-orgname">文本框</label>
										<div class="controls">
											<input type="text" title="文本框" value="" class="leipiplugins"
												leipiplugins="text_input" />
										</div>

									</div>
									<!-- Text end -->


									<!-- Textarea start -->
									<div class="control-group component" rel="popover"
										title="多行文本控件" trigger="manual"
										data-content="
  <form class='form'>
    <div class='controls'>
      <label class='control-label'>控件名称</label> <input type='text' id='orgname' placeholder='必填项'>
      <label class='control-label'>默认值</label> <input type='text' id='orgvalue' placeholder='默认值'>
      <label class='control-label'>数据库字段名</label> <input type='text' id='orgdataname' placeholder='默认值'>
      <hr/>
      <button class='btn btn-info' type='button'>确定</button><button class='btn btn-danger' type='button'>取消</button>
    </div>
  </form>">
										<!-- Textarea -->
										<label class="control-label leipiplugins-orgname">多行文本</label>
										<div class="controls">
											<div class="textarea">
												<textarea title="多行文本" class="leipiplugins"
													leipiplugins="textarea" />
												</textarea>
											</div>
										</div>
									</div>
									<!-- Textarea end -->

									<!-- Select start -->
									<div class="control-group component" rel="popover" title="下拉控件"
										trigger="manual"
										data-content="
  <form class='form'>
    <div class='controls'>
      <label class='control-label'>控件名称</label> <input type='text' id='orgname' placeholder='必填项'>
      <label class='control-label'>下拉选项</label>
      <textarea style='min-height: 70px' id='orgvalue'></textarea>
      <label class='control-label'>数据库字段名</label> <input type='text' id='orgdataname' placeholder='默认值'>
      <p class='help-block'>一行一个选项</p>
      <hr/>
      <button class='btn btn-info' type='button'>确定</button><button class='btn btn-danger' type='button'>取消</button>
    </div>
  </form>">
										<!-- Select -->
										<label class="control-label leipiplugins-orgname">下拉菜单</label>
										<div class="controls">
											<select title="下拉菜单" class="leipiplugins"
												leipiplugins="select">
												<option>选项一</option>
												<option>选项二</option>
												<option>选项三</option>
											</select>
										</div>

									</div>
									<!-- Select end -->


									<!-- Select start -->
									<div class="control-group component" rel="popover"
										title="多选下拉控件" trigger="manual"
										data-content="
  <form class='form'>
    <div class='controls'>
      <label class='control-label'>控件名称</label> <input type='text' id='orgname' placeholder='必填项'>
      <label class='control-label'>下拉选项</label>
      <textarea style='min-height: 70px' id='orgvalue'></textarea>
      <label class='control-label'>数据库字段名</label> <input type='text' id='orgdataname' placeholder='默认值'>
      <p class='help-block'>一行一个选项</p>
      <hr/>
      <button class='btn btn-info' type='button'>确定</button><button class='btn btn-danger' type='button'>取消</button>
    </div>
  </form>">
										<!-- Select -->
										<label class="control-label leipiplugins-orgname">下拉菜单</label>
										<div class="controls">
											<select multiple="multiple" title="下拉菜单" class="leipiplugins"
												leipiplugins="select">
												<option>选项一</option>
												<option>选项二</option>
												<option>选项三</option>
												<option>选项四</option>
											</select>
										</div>

									</div>
									<!-- Select end -->


									<!-- Multiple Checkboxes start -->
									<div class="control-group component" rel="popover" title="复选控件"
										trigger="manual"
										data-content="
  <form class='form'>
    <div class='controls'>
      <label class='control-label'>控件名称</label> <input type='text' id='orgname' placeholder='必填项'>
      <label class='control-label'>复选框</label>
      <textarea style='min-height: 70px' id='orgvalue'></textarea>
       <label class='control-label'>数据库字段名</label> <input type='text' id='orgdataname' placeholder='默认值'>
      <p class='help-block'>一行一个选项</p>
      <hr/>
      <button class='btn btn-info' type='button'>确定</button><button class='btn btn-danger' type='button'>取消</button>
    </div>
  </form>">
										<label class="control-label leipiplugins-orgname">复选框</label>
										<div class="controls leipiplugins-orgvalue">
											<!-- Multiple Checkboxes -->
											<label class="checkbox inline"> <input
												type="checkbox" title="复选框" value="选项1" class="leipiplugins"
												leipiplugins="checkbox" orginline="inline"> 选项1
											</label> <label class="checkbox inline"> <input
												type="checkbox" title="复选框" value="选项2" class="leipiplugins"
												leipiplugins="checkbox" orginline="inline"> 选项2
											</label>
										</div>

									</div>

									<div class="control-group component" rel="popover" title="复选控件"
										trigger="manual"
										data-content="
  <form class='form'>
    <div class='controls'>
      <label class='control-label'>控件名称</label> <input type='text' id='orgname' placeholder='必填项'>
      <label class='control-label'>复选框</label>
      <textarea style='min-height: 70px' id='orgvalue'></textarea>
       <label class='control-label'>数据库字段名</label> <input type='text' id='orgdataname' placeholder='默认值'>
      <p class='help-block'>一行一个选项</p>
      <hr/>
      <button class='btn btn-info' type='button'>确定</button><button class='btn btn-danger' type='button'>取消</button>
    </div>
  </form>">
										<label class="control-label leipiplugins-orgname">复选框</label>
										<div class="controls leipiplugins-orgvalue">
											<!-- Multiple Checkboxes -->
											<label class="checkbox"> <input type="checkbox"
												title="复选框" value="选项1" class="leipiplugins"
												leipiplugins="checkbox"> 选项1
											</label> <label class="checkbox"> <input type="checkbox"
												title="复选框" value="选项2" class="leipiplugins"
												leipiplugins="checkbox"> 选项2
											</label>
										</div>
									</div>
									<!-- Multiple Checkboxes end -->

									<!-- Multiple radios start -->
									<div class="control-group component" rel="popover" title="单选控件"
										trigger="manual"
										data-content="
  <form class='form'>
    <div class='controls'>
      <label class='control-label'>控件名称</label> <input type='text' id='orgname' placeholder='必填项'>
      <label class='control-label'>单选框</label>
      <textarea style='min-height: 70px' id='orgvalue'></textarea>
      <label class='control-label'>数据库字段名</label> <input type='text' id='orgdataname' placeholder='默认值'>
      <p class='help-block'>一行一个选项</p>
      <hr/>
      <button class='btn btn-info' type='button'>确定</button><button class='btn btn-danger' type='button'>取消</button>
    </div>
  </form>">
										<label class="control-label leipiplugins-orgname">单选</label>
										<div class="controls leipiplugins-orgvalue">
											<!-- Multiple Checkboxes -->
											<label class="radio inline"> <input type="radio"
												title="单选框" value="选项1" class="leipiplugins"
												leipiplugins="radio" orginline="inline"> 选项1
											</label> <label class="radio inline"> <input type="radio"
												title="单选框" value="选项2" class="leipiplugins"
												leipiplugins="radio" orginline="inline"> 选项2
											</label>
										</div>
									</div>

									<div class="control-group component" rel="popover" title="单选控件"
										trigger="manual"
										data-content="
  <form class='form'>
    <div class='controls'>
      <label class='control-label'>控件名称</label> <input type='text' id='orgname' placeholder='必填项'>
      <label class='control-label'>单选框</label>
      <textarea style='min-height: 70px' id='orgvalue'></textarea>
      <label class='control-label'>数据库字段名</label> <input type='text' id='orgdataname' placeholder='默认值'>
      <p class='help-block'>一行一个选项</p>
      <hr/>
      <button class='btn btn-info' type='button'>确定</button><button class='btn btn-danger' type='button'>取消</button>
    </div>
  </form>">
										<label class="control-label leipiplugins-orgname">单选</label>
										<div class="controls leipiplugins-orgvalue">
											<!-- Multiple Checkboxes -->
											<label class="radio"> <input type="radio" title="单选框"
												value="选项1" class="leipiplugins" leipiplugins="radio">
												选项1
											</label> <label class="radio"> <input type="radio"
												title="单选框" value="选项2" class="leipiplugins"
												leipiplugins="radio"> 选项2
											</label>
										</div>
									</div>
									<!-- Multiple radios end -->


								</div>

								<div class="tab-pane" id="2">



									<div class="control-group component" rel="popover" title="文件上传"
										trigger="manual"
										data-content="
                  <form class='form'>
                    <div class='controls'>
                      <label class='control-label'>控件名称</label> <input type='text' id='orgname' placeholder='必填项'>
                      <hr/>
                      <button class='btn btn-info' type='button'>确定</button><button class='btn btn-danger' type='button'>取消</button>
                    </div>
                  </form>">
										<label class="control-label leipiplugins-orgname">文件上传</label>

										<!-- File Upload -->
										<div>
											<input type="file" name="leipiNewField" title="文件上传"
												class="leipiplugins" leipiplugins="uploadfile">
										</div>
									</div>


								</div>



								<div class="tab-pane" id="5">
									<textarea id="source" class="span6"></textarea>
								</div>
						</fieldset>
					</form>
				</div>
				<!--tab-content-->
			</div>
			<!---tabbable-->
		</div>
		<!-- row -->

	</div>
	<!-- /container -->


	<script type="text/javascript" charset="utf-8"
		src="${ctx }/js/Public/js/jquery-1.7.2.min.js?2024"></script>
	<script type="text/javascript"
		src="${ctx }/js/Public/js/formbuild/bootstrap/js/bootstrap.min.js?2024"></script>
	<script type="text/javascript" charset="utf-8"
		src="${ctx }/js/Public/js/formbuild/leipi.form.build.core.js?2024"></script>
	<script type="text/javascript" charset="utf-8"
		src="${ctx }/js/Public/js/formbuild/leipi.form.build.plugins.js?2024"></script>
	<script type="text/javascript">
		$(document).ready(function() {

					$.get(ctx+ '/form/formkey/form-design/getFormNamesByModel'+ window.location.search,
											function(data) {
												console.log(data.length);
												var content = "选择表单名称：<select id='selectName'>";
												for ( var i = 0; i < data.length; i++) {
													content += "<option value="+data[i]+">"
															+ data[i]
															+ "</option>";
												}
												content += "</select>";
												/* $(dialog).html(content); */
												$("#formNames").html(content);

											});

							$("#uploadFile").click(function() {
								
											$.ajax({
												url : ctx + "/form/formkey/form-design/uploadFileByModel"
															  + window.location.search
															  + "&formName="
															  + $("#selectName").val(),
														data : $("#source").val(),
														type : 'POST',
														dataType : "text",
														contentType : "text/plain",
														success : function(data) {
																
																if(data == "success")
																	alert("表单设置成功！");
																$("#build").find("fieldset").html(""); 
															},
														error:function(data){
																alert("error");
																alert(JSON.stringify(data));
																}
														});

											});

							$("#deployFile").click(function() {
												$.ajax({
															url : ctx
																	+ "/form/formkey/form-design/deployFile"
																	+ window.location.search,
															type : 'GET',
															success : function(data) {
																window.location.href="${ctx}/workflow/model/list";
															}
														});

											});

						});
	</script>



</body>
</html>