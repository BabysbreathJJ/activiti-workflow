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
 * ��̬��Controller
 * �˽ⲻͬ������ʣ�http://www.kafeitu.me/activiti/2012/08/05/diff-activiti
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
	 * ��̬form�����б�
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
			 * ֻ��ȡ��̬��������
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
			// ��ȡ��������
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
	 * ��ʼ���������̣���ȡ�������̵ı��ֶ�����Ⱦstart form
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
		 * ��ȡenum�������ݣ�����������
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
	 * ��ȡTask�ı�
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "get-form/task/{taskId}/processInstanceId/{processInstanceId}")
	@ResponseBody
	public Map<String, Object> findTaskForm(@PathVariable("taskId") String taskId,
			@PathVariable("processInstanceId") String processInstanceId) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		TaskFormDataImpl taskFormData = (TaskFormDataImpl) formService.getTaskFormData(taskId);

		// ����taskΪnull���������json��ʱ��ᱨ��
		taskFormData.setTask(null);

		result.put("taskFormData", taskFormData);

		/*
		 * ��ȡenum�������ݣ�����������
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
	 * ���������ύtask�Ĳ�����form
	 */
	@RequestMapping(value = "task/complete/{taskId}/{allType}")
	@SuppressWarnings("unchecked")
	public String completeTask(@PathVariable("taskId") String taskId, @PathVariable("allType") String allType,
			RedirectAttributes redirectAttributes, HttpServletRequest request) {
		Map<String, String> formProperties = new HashMap<String, String>();

		// ��request�ж�ȡ����Ȼ��ת��
		Map<String, String[]> parameterMap = request.getParameterMap();
		Set<Entry<String, String[]>> entrySet = parameterMap.entrySet();
		for (Entry<String, String[]> entry : entrySet) {
			String key = entry.getKey();

			// fp_����˼��form paremeter
			if (StringUtils.defaultString(key).startsWith("fp_")) {
				formProperties.put(key.split("_")[1], entry.getValue()[0]);
			}
		}

		logger.debug("start form parameters: {}", formProperties);

		User user = UserUtil.getUserFromSession(request.getSession());

		// �û�δ��¼���ܲ�����ʵ��Ӧ��ʹ��Ȩ�޿��ʵ�֣�����Spring Security��Shiro��
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

		redirectAttributes.addFlashAttribute("message", "������ɣ�taskId=" + taskId);
		if (allType.equals("allType"))
			return "redirect:/form/dynamic/task/list/allType";
		else
			return "redirect:/form/dynamic/task/list/singleType";

	}

	/**
	 * �ύ��������
	 */
	@RequestMapping(value = "start-process/{processDefinitionId}/{allType}")
	@SuppressWarnings("unchecked")
	public String submitStartFormAndStartProcessInstance(
			@PathVariable("processDefinitionId") String processDefinitionId, @PathVariable("allType") String allType,
			RedirectAttributes redirectAttributes, HttpServletRequest request) {
		Map<String, String> formProperties = new HashMap<String, String>();

		// ��request�ж�ȡ����Ȼ��ת��
		Map<String, String[]> parameterMap = request.getParameterMap();
		Set<Entry<String, String[]>> entrySet = parameterMap.entrySet();
		for (Entry<String, String[]> entry : entrySet) {
			String key = entry.getKey();

			// fp_����˼��form paremeter
			if (StringUtils.defaultString(key).startsWith("fp_")) {
				formProperties.put(key.split("_")[1], entry.getValue()[0]);
			}
		}

		logger.debug("start form parameters: {}", formProperties);

		User user = UserUtil.getUserFromSession(request.getSession());
		// �û�δ��¼���ܲ�����ʵ��Ӧ��ʹ��Ȩ�޿��ʵ�֣�����Spring Security��Shiro��
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
		redirectAttributes.addFlashAttribute("message", "�����ɹ�������ID��" + processInstance.getId());
		if (allType.equals("allType"))
			return "redirect:/form/dynamic/process-list/allType";
		else
			return "redirect:/form/dynamic/process-list/singleType";
	}

	/**
	 * �ύ��Ϣ��������
	 */
	@RequestMapping(value = "start-process/message/{processDefinitionKey}/{allType}")
	public String startMessageProcessInstance(@PathVariable("processDefinitionKey") String processDefinitionKey,
			@PathVariable("allType") String allType, RedirectAttributes redirectAttributes,
			HttpServletRequest request) {

		logger.debug("start message process: {}", processDefinitionKey);

		User user = UserUtil.getUserFromSession(request.getSession());
		// �û�δ��¼���ܲ�����ʵ��Ӧ��ʹ��Ȩ�޿��ʵ�֣�����Spring Security��Shiro��
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
					      .messageEventSubscriptionName("msg") // ����msg message�Ķ������ڱ�������һ��intermediate message catch event
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
		redirectAttributes.addFlashAttribute("message", "�����ɹ�������ID��" + processInstance.getId());
		if (allType.equals("allType"))
			return "redirect:/form/dynamic/process-list/allType";
		else
			return "redirect:/form/dynamic/process-list/singleType";
	}

	/**
	 * task�б�
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
			 * ����Ϊ����ʾ���ֿ��Զ������������̣�ֵ��ȡleave-dynamic-from
			 * ��FormKeyController����ʹ��native��ʽ��ѯ������
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
	 * ǩ������
	 */
	@RequestMapping(value = "task/claim/{id}/{allType}")
	public String claim(@PathVariable("id") String taskId, HttpSession session, HttpServletRequest request,
			@PathVariable("allType") String allType, RedirectAttributes redirectAttributes) {

		String userId = UserUtil.getUserFromSession(session).getId();
		taskService.claim(taskId, userId);
		redirectAttributes.addFlashAttribute("message", "������ǩ��");
		return "redirect:/form/dynamic/task/list/" + allType;
	}

	/**
	 * �����е�����ʵ��
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
	 * �ѽ���������ʵ��
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

	// ********************** ����ί�� ************************************
	@RequestMapping(value = "delegate/task/{taskId}/{allType}")
	public String delegateTask(@PathVariable("taskId") String taskId, @PathVariable("allType") String allType,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {

		String userId = request.getParameter("delegateUser");

		User user = UserUtil.getUserFromSession(request.getSession());

		// �û�δ��¼���ܲ�����ʵ��Ӧ��ʹ��Ȩ�޿��ʵ�֣�����Spring Security��Shiro��
		if (user == null || StringUtils.isBlank(user.getId())) {
			return "redirect:/login?timeout=true";
		}

		taskService.delegateTask(taskId, userId);
		redirectAttributes.addFlashAttribute("message", "����IDΪ[" + taskId + "]�������Ѿ�ί�ɸ�" + userId);

		return "redirect:/form/dynamic/task/list/" + allType;

	}

}
