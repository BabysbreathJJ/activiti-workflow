$(function() {
	function getAutoDelegateTasks() {

		$.ajax({
					url : ctx + '/workflow/task/todo/list',
					type : "get",
					contentType : 'application/json',
					dataType : 'json',
					success : function(data) {
						var ct;
						for (var i = 0; i < data.length; i++) {
							ct += "<tr>";
							ct += "<td>"
									+ data[i].pdname
									+ "</td>"
									+ "<td>"
									+ data[i].pid
									+ "</td><td><span class='ui-state-highlight ui-corner-all'>"
									+ data[i].name + "</span></td>";
							ct += "<td><span class='version' title='流程定义版本："
									+ data[i].pdversion + "'><b>V:</b>"
									+ data[i].pdversion + "</span></td>";
							ct += "<td><a class='trace' href='#' pid='"
									+ data[i].pid
									+ "' title='点击查看流程图'>跟踪</a></td>";
							ct += "<td><span class='status' title='任务状态'>"
									+ (data[i].status == 'claim' ? '未签收' : '')
									+ "</span></td>";
							ct += "</tr>";

							$("#todoTasks").html(ct);
						}
					}
				});

		/* setTimeout(getAutoDelegateTasks(), 50); */
	}
	/* getAutoDelegateTasks(); */
	setTimeout(getAutoDelegateTasks(), 50);
});