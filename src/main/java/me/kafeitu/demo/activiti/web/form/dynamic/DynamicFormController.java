package me.kafeitu.demo.activiti.web.form.dynamic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import me.kafeitu.demo.activiti.util.Page;
import me.kafeitu.demo.activiti.util.PageUtil;
import me.kafeitu.demo.activiti.util.UserUtil;
import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.form.StartFormDataImpl;
import org.activiti.engine.impl.form.TaskFormDataImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.DelegationState;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 动态表单Controller
 * 了解不同表单请访问：http://www.kafeitu.me/activiti/2012/08/05/diff-activiti
 * -workflow-forms.html
 *
 * @author HenryYan
 */
@Controller
@RequestMapping(value = "/form/dynamic")
public class DynamicFormController {

	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private FormService formService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private IdentityService identityService;
	@Autowired
	private HistoryService historyService;
	@Autowired
	private RuntimeService runtimeService;

	/**
	 * 动态form流程列表
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "process-list/{allType}" })
	public ModelAndView processDefinitionList(Model model, @PathVariable("allType") String allType,
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("/form/dynamic/dynamic-form-process-list");
		Page<ProcessDefinition> page = new Page<ProcessDefinition>(PageUtil.PAGE_SIZE);
		int[] pageParams = PageUtil.init(page, request);

		if (!allType.equals("allType")) {
			/*
			 * 只读取动态表单的流程
			 */
			ProcessDefinitionQuery query1 = repositoryService.createProcessDefinitionQuery()
					.processDefinitionKey("leave-dynamic-from").active().orderByDeploymentId().desc();
			List<ProcessDefinition> list = query1.listPage(pageParams[0], pageParams[1]);

			ProcessDefinitionQuery query2 = repositoryService.createProcessDefinitionQuery()
					.processDefinitionKey("dispatch").active().orderByDeploymentId().desc();
			List<ProcessDefinition> dispatchList = query2.listPage(pageParams[0], pageParams[1]);

			ProcessDefinitionQuery query3 = repositoryService.createProcessDefinitionQuery()
					.processDefinitionKey("leave-jpa").active().orderByDeploymentId().desc();
			List<ProcessDefinition> list3 = query3.listPage(pageParams[0], pageParams[1]);

			list.addAll(list3);
			list.addAll(dispatchList);

			page.setResult(list);
			page.setType(allType);
			page.setTotalCount(query1.count() + query2.count() + query3.count());
		} else {
			// 读取所有流程
			ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery().active()
					.orderByDeploymentId().desc();
			List<ProcessDefinition> list = query.list();
			page.setResult(list);
			page.setType(allType);
			page.setTotalCount(query.count());
		}

		mav.addObject("page", page);
		return mav;
	}

	/**
	 * 初始化启动流程，读取启动流程的表单字段来渲染start form
	 */
	@RequestMapping(value = "get-form/start/{processDefinitionId}")
	@ResponseBody
	@SuppressWarnings("unchecked")
	public Map<String, Object> findStartForm(@PathVariable("processDefinitionId") String processDefinitionId,
			HttpServletRequest request) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.processDefinitionId(processDefinitionId).singleResult();
				// boolean hasStartFormKey =
				// processDefinition.hasStartFormKey();

		// if (hasStartFormKey) {
		// Object startForm =
		// formService.getRenderedStartForm(processDefinitionId);
		// result.put("form", startForm);
		// } else {

		StartFormDataImpl startFormData = (StartFormDataImpl) formService.getStartFormData(processDefinitionId);
		startFormData.setProcessDefinition(null);

		/*
		 * 读取enum类型数据，用于下拉框
		 */
		List<FormProperty> formProperties = startFormData.getFormProperties();
		for (FormProperty formProperty : formProperties) {
			Map<String, String> values = (Map<String, String>) formProperty.getType().getInformation("values");
			if (values != null) {
				for (Entry<String, String> enumEntry : values.entrySet()) {
					logger.debug("enum, key: {}, value: {}", enumEntry.getKey(), enumEntry.getValue());
				}
				result.put("enum_" + formProperty.getId(), values);
			}
		}

		result.put("form", startFormData);
		// }

		return result;
	}

	/**
	 * 读取Task的表单
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "get-form/task/{taskId}/processInstanceId/{processInstanceId}")
	@ResponseBody
	public Map<String, Object> findTaskForm(@PathVariable("taskId") String taskId,
			@PathVariable("processInstanceId") String processInstanceId) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		TaskFormDataImpl taskFormData = (TaskFormDataImpl) formService.getTaskFormData(taskId);

		// 设置task为null，否则输出json的时候会报错
		taskFormData.setTask(null);

		result.put("taskFormData", taskFormData);

		/*
		 * 读取enum类型数据，用于下拉框
		 */
		List<FormProperty> formProperties = taskFormData.getFormProperties();

		for (FormProperty formProperty : formProperties) {
			Map<String, String> values = (Map<String, String>) formProperty.getType().getInformation("values");
			if (values != null) {
				for (Entry<String, String> enumEntry : values.entrySet()) {
					logger.debug("enum, key: {}, value: {}", enumEntry.getKey(), enumEntry.getValue());

				}

				result.put(formProperty.getId(), values);

			}
		}

		HistoricProcessInstance applicant = historyService.createHistoricProcessInstanceQuery()
				.processInstanceId(processInstanceId).singleResult();
		// System.out.println("****************" + applicant.getStartUserId());

		result.put("applicant", applicant.getStartUserId());

		return result;
	}

	/**
	 * 办理任务，提交task的并保存form
	 */
	@RequestMapping(value = "task/complete/{taskId}/{allType}")
	@SuppressWarnings("unchecked")
	public String completeTask(@PathVariable("taskId") String taskId, @PathVariable("allType") String allType,
			RedirectAttributes redirectAttributes, HttpServletRequest request) {
		Map<String, String> formProperties = new HashMap<String, String>();

		// 从request中读取参数然后转换
		Map<String, String[]> parameterMap = request.getParameterMap();
		Set<Entry<String, String[]>> entrySet = parameterMap.entrySet();
		for (Entry<String, String[]> entry : entrySet) {
			String key = entry.getKey();

			// fp_的意思是form paremeter
			if (StringUtils.defaultString(key).startsWith("fp_")) {
				formProperties.put(key.split("_")[1], entry.getValue()[0]);
			}
		}

		logger.debug("start form parameters: {}", formProperties);

		User user = UserUtil.getUserFromSession(request.getSession());

		// 用户未登录不能操作，实际应用使用权限框架实现，例如Spring Security、Shiro等
		if (user == null || StringUtils.isBlank(user.getId())) {
			return "redirect:/login?timeout=true";
		}
		try {
			Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

			if (task.getOwner() != null)
				taskService.resolveTask(taskId);
			else
				identityService.setAuthenticatedUserId(user.getId());
			formService.submitTaskFormData(taskId, formProperties);

		} finally {
			identityService.setAuthenticatedUserId(null);
		}

		redirectAttributes.addFlashAttribute("message", "任务完成：taskId=" + taskId);
		if (allType.equals("allType"))
			return "redirect:/form/dynamic/task/list/allType";
		else
			return "redirect:/form/dynamic/task/list/singleType";

	}

	/**
	 * 提交启动流程
	 */
	@RequestMapping(value = "start-process/{processDefinitionId}/{allType}")
	@SuppressWarnings("unchecked")
	public String submitStartFormAndStartProcessInstance(
			@PathVariable("processDefinitionId") String processDefinitionId, @PathVariable("allType") String allType,
			RedirectAttributes redirectAttributes, HttpServletRequest request) {
		Map<String, String> formProperties = new HashMap<String, String>();

		// 从request中读取参数然后转换
		Map<String, String[]> parameterMap = request.getParameterMap();
		Set<Entry<String, String[]>> entrySet = parameterMap.entrySet();
		for (Entry<String, String[]> entry : entrySet) {
			String key = entry.getKey();

			// fp_的意思是form paremeter
			if (StringUtils.defaultString(key).startsWith("fp_")) {
				formProperties.put(key.split("_")[1], entry.getValue()[0]);
			}
		}

		logger.debug("start form parameters: {}", formProperties);

		User user = UserUtil.getUserFromSession(request.getSession());
		// 用户未登录不能操作，实际应用使用权限框架实现，例如Spring Security、Shiro等
		if (user == null || StringUtils.isBlank(user.getId())) {
			return "redirect:/login?timeout=true";
		}
		ProcessInstance processInstance = null;
		try {
			identityService.setAuthenticatedUserId(user.getId());
			formProperties.put("applyUser", user.getId());
			processInstance = formService.submitStartFormData(processDefinitionId, formProperties);
			logger.debug("start a processinstance: {}", processInstance);
		} finally {
			identityService.setAuthenticatedUserId(null);
		}
		redirectAttributes.addFlashAttribute("message", "启动成功，流程ID：" + processInstance.getId());
		if (allType.equals("allType"))
			return "redirect:/form/dynamic/process-list/allType";
		else
			return "redirect:/form/dynamic/process-list/singleType";
	}

	/**
	 * 提交消息启动流程
	 */
	@RequestMapping(value = "start-process/message/{processDefinitionKey}/{allType}")
	public String startMessageProcessInstance(@PathVariable("processDefinitionKey") String processDefinitionKey,
			@PathVariable("allType") String allType, RedirectAttributes redirectAttributes,
			HttpServletRequest request) {

		logger.debug("start message process: {}", processDefinitionKey);

		User user = UserUtil.getUserFromSession(request.getSession());
		// 用户未登录不能操作，实际应用使用权限框架实现，例如Spring Security、Shiro等
		if (user == null || StringUtils.isBlank(user.getId())) {
			return "redirect:/login?timeout=true";
		}
		ProcessInstance processInstance = null;
		try {
			identityService.setAuthenticatedUserId(user.getId());
			if (processDefinitionKey.indexOf("intermediate") >= 0) {
				processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey);
						
				List<Execution> executions = runtimeService
					      .createExecutionQuery()
					      .processDefinitionKey(processDefinitionKey)
					      .messageEventSubscriptionName("msg") // 监听msg message的东西，在本例里是一个intermediate message catch event
					      .list();
					  for(Execution execution : executions) {
					    runtimeService.messageEventReceived("msg", execution.getId());
					  }

				
			} else
				System.out.println("");
			System.out.println("********************msg-text**************");
				processInstance = runtimeService.startProcessInstanceByMessage("msg-test");
				System.out.println("");
			logger.debug("**************message************: {}", processInstance);
		} finally {
			identityService.setAuthenticatedUserId(null);
		}
		redirectAttributes.addFlashAttribute("message", "启动成功，流程ID：" + processInstance.getId());
		if (allType.equals("allType"))
			return "redirect:/form/dynamic/process-list/allType";
		else
			return "redirect:/form/dynamic/process-list/singleType";
	}

	/**
	 * task列表
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "task/list/{allType}")
	public ModelAndView taskList(@PathVariable("allType") String allType, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("/form/dynamic/dynamic-form-task-list");
		User user = UserUtil.getUserFromSession(request.getSession());

		// Page<ProcessDefinition> page = new
		// Page<ProcessDefinition>(PageUtil.PAGE_SIZE);
		// int[] pageParams = PageUtil.init(page, request);

		Page<Task> page = new Page<Task>(PageUtil.PAGE_SIZE);
		int[] pageParams = PageUtil.init(page, request);

		List<Task> tasks = new ArrayList<Task>();

		if (!allType.equals("allType")) {
			/**
			 * 这里为了演示区分开自定义表单的请假流程，值读取leave-dynamic-from
			 * 在FormKeyController中有使用native方式查询的例子
			 */
			TaskQuery dynamicForm = taskService.createTaskQuery().processDefinitionKey("leave-dynamic-from")
					.taskCandidateOrAssigned(user.getId()).active().orderByTaskId().desc();

			TaskQuery dispatch = taskService.createTaskQuery().processDefinitionKey("dispatch")
					.taskCandidateOrAssigned(user.getId()).active().orderByTaskId().desc();
			TaskQuery leaveJpa = taskService.createTaskQuery().processDefinitionKey("leave-jpa")
					.taskCandidateOrAssigned(user.getId()).active().orderByTaskId().desc();

			List<Task> dynamicFormTasks = dynamicForm.listPage(pageParams[0], pageParams[1]);

			List<Task> dispatchTasks = dispatch.listPage(pageParams[0], pageParams[1]);

			List<Task> leaveJpaTasks = leaveJpa.listPage(pageParams[0], pageParams[1]);

			tasks.addAll(dynamicFormTasks);
			tasks.addAll(dispatchTasks);
			tasks.addAll(leaveJpaTasks);

			page.setResult(tasks);
			page.setTotalCount(dynamicForm.count() + dispatch.count() + leaveJpa.count());
			page.setType(allType);
		} else {
			TaskQuery pageTasks = taskService.createTaskQuery().taskCandidateOrAssigned(user.getId()).active();

			tasks = pageTasks.listPage(pageParams[0], pageParams[1]);

			page.setResult(tasks);
			page.setTotalCount(pageTasks.count());
			page.setType(allType);
		}

		mav.addObject("page", page);
		return mav;
	}

	/**
	 * 签收任务
	 */
	@RequestMapping(value = "task/claim/{id}/{allType}")
	public String claim(@PathVariable("id") String taskId, HttpSession session, HttpServletRequest request,
			@PathVariable("allType") String allType, RedirectAttributes redirectAttributes) {

		String userId = UserUtil.getUserFromSession(session).getId();
		taskService.claim(taskId, userId);
		redirectAttributes.addFlashAttribute("message", "任务已签收");
		return "redirect:/form/dynamic/task/list/" + allType;
	}

	/**
	 * 运行中的流程实例
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "process-instance/running/list/{allType}")
	public ModelAndView running(Model model, @PathVariable("allType") String allType, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("/form/running-list");
		Page<ProcessInstance> page = new Page<ProcessInstance>(PageUtil.PAGE_SIZE);
		int[] pageParams = PageUtil.init(page, request);

		if (!allType.equals("allType")) {
			ProcessInstanceQuery leaveDynamicQuery = runtimeService.createProcessInstanceQuery()
					.processDefinitionKey("leave-dynamic-from").orderByProcessInstanceId().desc().active();
			List<ProcessInstance> list = leaveDynamicQuery.listPage(pageParams[0], pageParams[1]);

			ProcessInstanceQuery dispatchQuery = runtimeService.createProcessInstanceQuery()
					.processDefinitionKey("dispatch").active().orderByProcessInstanceId().desc();
			List<ProcessInstance> list2 = dispatchQuery.listPage(pageParams[0], pageParams[1]);
			list.addAll(list2);

			ProcessInstanceQuery leaveJpaQuery = runtimeService.createProcessInstanceQuery()
					.processDefinitionKey("leave-jpa").active().orderByProcessInstanceId().desc();
			List<ProcessInstance> list3 = leaveJpaQuery.listPage(pageParams[0], pageParams[1]);
			list.addAll(list3);

			page.setResult(list);
			page.setTotalCount(leaveDynamicQuery.count() + dispatchQuery.count());
		} else {
			ProcessInstanceQuery dynamicQuery = runtimeService.createProcessInstanceQuery().orderByProcessInstanceId()
					.desc().active();
			List<ProcessInstance> list = dynamicQuery.listPage(pageParams[0], pageParams[1]);
			page.setResult(list);
			page.setTotalCount(dynamicQuery.count());
		}
		mav.addObject("page", page);
		return mav;
	}

	/**
	 * 已结束的流程实例
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "process-instance/finished/list/{allType}")
	public ModelAndView finished(Model model, @PathVariable("allType") String allType, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("/form/finished-list");
		Page<HistoricProcessInstance> page = new Page<HistoricProcessInstance>(PageUtil.PAGE_SIZE);
		int[] pageParams = PageUtil.init(page, request);

		if (!allType.equals("allType")) {
			HistoricProcessInstanceQuery leaveDynamicQuery = historyService.createHistoricProcessInstanceQuery()
					.processDefinitionKey("leave-dynamic-from").finished().orderByProcessInstanceEndTime().desc();
			List<HistoricProcessInstance> list = leaveDynamicQuery.listPage(pageParams[0], pageParams[1]);

			HistoricProcessInstanceQuery dispatchQuery = historyService.createHistoricProcessInstanceQuery()
					.processDefinitionKey("dispatch").finished().orderByProcessInstanceEndTime().desc();
			List<HistoricProcessInstance> list2 = dispatchQuery.listPage(pageParams[0], pageParams[1]);

			HistoricProcessInstanceQuery leaveJpaQuery = historyService.createHistoricProcessInstanceQuery()
					.processDefinitionKey("leave-jpa").finished().orderByProcessInstanceEndTime().desc();
			List<HistoricProcessInstance> list3 = leaveJpaQuery.listPage(pageParams[0], pageParams[1]);

			list.addAll(list2);
			list.addAll(list3);

			page.setResult(list);
			page.setTotalCount(leaveDynamicQuery.count() + dispatchQuery.count());
		} else {
			HistoricProcessInstanceQuery dynamicQuery = historyService.createHistoricProcessInstanceQuery().finished()
					.orderByProcessInstanceEndTime().desc();
			List<HistoricProcessInstance> list = dynamicQuery.listPage(pageParams[0], pageParams[1]);
			page.setResult(list);
			page.setTotalCount(dynamicQuery.count());
		}

		mav.addObject("page", page);
		return mav;
	}

	// ********************** 任务委派 ************************************
	@RequestMapping(value = "delegate/task/{taskId}/{allType}")
	public String delegateTask(@PathVariable("taskId") String taskId, @PathVariable("allType") String allType,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {

		String userId = request.getParameter("delegateUser");

		User user = UserUtil.getUserFromSession(request.getSession());

		// 用户未登录不能操作，实际应用使用权限框架实现，例如Spring Security、Shiro等
		if (user == null || StringUtils.isBlank(user.getId())) {
			return "redirect:/login?timeout=true";
		}

		taskService.delegateTask(taskId, userId);
		redirectAttributes.addFlashAttribute("message", "任务ID为[" + taskId + "]的任务已经委派给" + userId);

		return "redirect:/form/dynamic/task/list/" + allType;

	}

}
