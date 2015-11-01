package me.kafeitu.demo.activiti.web.form.formkey;

import me.kafeitu.demo.activiti.service.Parser.DOMParser;
import me.kafeitu.demo.activiti.util.Page;
import me.kafeitu.demo.activiti.util.PageUtil;
import me.kafeitu.demo.activiti.util.UserUtil;
import me.kafeitu.demo.activiti.util.WorkflowUtils;
import me.kafeitu.demo.activiti.util.ZipUtils;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.persistence.entity.SuspensionState;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.NativeTaskQuery;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.ZipInputStream;
import java.util.Set;

/**
 * 外置表单Controller
 * 了解不同表单请访问：http://www.kafeitu.me/activiti/2012/08/05/diff-activiti
 * -workflow-forms.html
 * 
 * @author HenryYan
 */
@Controller
@RequestMapping(value = "/form/formkey")
public class FormKeyController {

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

	@Autowired
	private ManagementService managementService;

	/**
	 * form流程列表
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "process-list", "" })
	public ModelAndView processDefinitionList(Model model,
			HttpServletRequest request) {
		ModelAndView mav = new ModelAndView(
				"/form/formkey/formkey-process-list");
		Page<ProcessDefinition> page = new Page<ProcessDefinition>(
				PageUtil.PAGE_SIZE);
		int[] pageParams = PageUtil.init(page, request);
		/*
		 * 表单：leave-formkey
		 */
		ProcessDefinitionQuery query = repositoryService
				.createProcessDefinitionQuery()
				.processDefinitionKey("leave-formkey").active()
				.orderByDeploymentId().desc();
		List<ProcessDefinition> list = query.listPage(pageParams[0],
				pageParams[1]);

		page.setResult(list);
		page.setTotalCount(query.count());
		mav.addObject("page", page);
		return mav;
	}

	/**
	 * form流程列表
	 * 
	 * @param model
	 * @return
	 */
	// @RequestMapping(value = { "form-design-list", "" })
	// public ModelAndView processDesignList(Model model,
	// HttpServletRequest request) {
	// ModelAndView mav = new ModelAndView("/form/formkey/form-design-list");
	// Page<ProcessDefinition> page = new Page<ProcessDefinition>(
	// PageUtil.PAGE_SIZE);
	// int[] pageParams = PageUtil.init(page, request);
	// /*
	// * 表单：leave-formkey
	// */
	// ProcessDefinitionQuery query = repositoryService
	// .createProcessDefinitionQuery()
	// .processDefinitionKey("leave-formkey").active()
	// .orderByDeploymentId().desc();
	// List<ProcessDefinition> list = query.listPage(pageParams[0],
	// pageParams[1]);
	//
	// page.setResult(list);
	// page.setTotalCount(query.count());
	// mav.addObject("page", page);
	// return mav;
	// }

	/**
	 * form表单设计
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "form-design", "" })
	public ModelAndView formDesign(Model model, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("/form/formkey/form-design");

		return mav;
	}

	/**
	 * 根据processId得到外置表单中的form数量
	 * 
	 */
//	@RequestMapping(value = "form-design/getFormNames")
//	@ResponseBody
//	public ArrayList<String> getFormNames(
//			@RequestParam("processDefinitionId") String processDefinitionId,
//			HttpServletResponse response) throws Exception {
//		ArrayList<String> res = new ArrayList<String>();
//		// ArrayList<String> res2 = new ArrayList<String>();
//		ProcessDefinition processDefinition = repositoryService
//				.createProcessDefinitionQuery()
//				.processDefinitionId(processDefinitionId).singleResult();
//		String resourceName = "";
//		resourceName = processDefinition.getResourceName();
//		InputStream resourceAsStream = repositoryService.getResourceAsStream(
//				processDefinition.getDeploymentId(), resourceName);
//		DOMParser parser = new DOMParser();
//		ArrayList<String> searchNode = new ArrayList<String>();
//		searchNode.add("startEvent");
//		searchNode.add("userTask");
//		res = parser.getNodeContent(resourceAsStream, searchNode,
//				"activiti:formKey");
//		// res2 = parser.getNodeContent(resourceAsStream, "startEvent",
//		// "activiti:formKey");
//		// res1.add(res2.get(0).toString());
//		return res;
//	}

	/**
	 * 根据model id得到外置表单中的form数量
	 * 
	 */
	@RequestMapping(value = "form-design/getFormNamesByModel")
	@ResponseBody
	public ArrayList<String> getFormNamesbyModel(
			@RequestParam("modelId") String modelId,
			HttpServletResponse response) throws Exception {
		ArrayList<String> res = new ArrayList<String>();

		try {
			org.activiti.engine.repository.Model modelData = repositoryService
					.getModel(modelId);
			BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
			JsonNode editorNode = new ObjectMapper().readTree(repositoryService
					.getModelEditorSource(modelData.getId()));
			BpmnModel bpmnModel = jsonConverter.convertToBpmnModel(editorNode);
			BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
			byte[] bpmnBytes = xmlConverter.convertToXML(bpmnModel);

			InputStream in = new ByteArrayInputStream(bpmnBytes);
			DOMParser parser = new DOMParser();
			ArrayList<String> searchNode = new ArrayList<String>();
			searchNode.add("startEvent");
			searchNode.add("userTask");
			res = parser.getNodeContent(in, searchNode, "activiti:formKey");

		} catch (Exception e) {
			logger.error("导出model的xml文件失败：modelId={}", modelId, e);
		}

		// res2 = parser.getNodeContent(resourceAsStream, "startEvent",
		// "activiti:formKey");
		// res1.add(res2.get(0).toString());
		return res;
	}

	/**
	 * 上传文件到系统 processDefinitionId
	 * 
	 */
//	@RequestMapping(value = "form-design/uploadFile")
//	@ResponseBody
//	public HashMap<String, String> uploadFile(
//			@RequestParam("processDefinitionId") String processDefinitionId,
//			@RequestParam("formName") String formName,
//			HttpServletResponse response, HttpServletRequest request,
//			@RequestBody String fileContent) throws Exception {
//		HashMap<String, String> res = new HashMap<String, String>();
//
//		String path = request.getRealPath("/") + "/../resources/form-design/";
//
//		// System.out.println("@@@@@@@@@@@@" + path);
//		File dir = new File(path + processDefinitionId);
//		// if file doesnt exists, then create it
//		if (!dir.exists()) {
//			dir.mkdirs();
//		}
//
//		String processXml = path + processDefinitionId + "/"
//				+ processDefinitionId + ".bpmn";
//		File pxml = new File(processXml);
//		// if file doesnt exists, then create it
//		if (!pxml.exists()) {// 如果没有流程定义文件，在文件夹下添加
//			pxml.createNewFile();
//			ProcessDefinition processDefinition = repositoryService
//					.createProcessDefinitionQuery()
//					.processDefinitionId(processDefinitionId).singleResult();
//			String resourceName = "";
//			resourceName = processDefinition.getResourceName();
//			InputStream resourceAsStream = repositoryService
//					.getResourceAsStream(processDefinition.getDeploymentId(),
//							resourceName);
//			FileOutputStream pxmlWriter = new FileOutputStream(pxml);
//			final ReadableByteChannel inputChannel = Channels
//					.newChannel(resourceAsStream);
//			final WritableByteChannel outputChannel = Channels
//					.newChannel(pxmlWriter);
//
//			final ByteBuffer buffer = ByteBuffer.allocateDirect(16 * 1024);
//
//			while (inputChannel.read(buffer) != -1) {
//				buffer.flip();
//				outputChannel.write(buffer);
//				buffer.compact();
//			}
//
//			buffer.flip();
//
//			while (buffer.hasRemaining()) {
//				outputChannel.write(buffer);
//			}
//
//		}
//		String fileDir = path + processDefinitionId + "/" + formName;
//
//		FileWriter fw = new FileWriter(fileDir);
//		BufferedWriter bw = new BufferedWriter(fw);
//		bw.write(fileContent);
//		bw.close();
//
//		res.put("result", fileContent);
//
//		return res;
//	}

	/**
	 * 上传文件到系统 modelId
	 * 
	 */
	@RequestMapping(value = "form-design/uploadFileByModel")
	@ResponseBody
	public String uploadFilebyModel(
			@RequestParam("modelId") String modelId,
			@RequestParam("formName") String formName,
			@RequestParam("modelKey") String modelKey,
			HttpServletResponse response, HttpServletRequest request,
			@RequestBody String fileContent) throws Exception {
		HashMap<String, String> res = new HashMap<String, String>();

		String path = request.getRealPath("/") + "/../resources/form-design/";

		File dir = new File(path + modelKey);
		// if file doesnt exists, then create it
		if (!dir.exists()) {
			dir.mkdirs();
		}
		
		String processXml = path + modelKey+"/"+ "formkey" + modelId + ".bpmn20.xml";
		File pxml = new File(processXml);
		// if file doesnt exists, then create it
		if (!pxml.exists()) {// 如果没有流程定义文件，在文件夹下添加
			pxml.createNewFile();
			org.activiti.engine.repository.Model modelData = repositoryService
					.getModel(modelId);
			BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
			JsonNode editorNode = new ObjectMapper().readTree(repositoryService
					.getModelEditorSource(modelData.getId()));
			BpmnModel bpmnModel = jsonConverter.convertToBpmnModel(editorNode);
			BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
			byte[] bpmnBytes = xmlConverter.convertToXML(bpmnModel);

			InputStream in = new ByteArrayInputStream(bpmnBytes);

			FileOutputStream pxmlWriter = new FileOutputStream(pxml);
			final ReadableByteChannel inputChannel = Channels
					.newChannel(in);
			final WritableByteChannel outputChannel = Channels
					.newChannel(pxmlWriter);

			final ByteBuffer buffer = ByteBuffer.allocateDirect(16 * 1024);

			while (inputChannel.read(buffer) != -1) {
				buffer.flip();
				outputChannel.write(buffer);
				buffer.compact();
			}

			buffer.flip();

			while (buffer.hasRemaining()) {
				outputChannel.write(buffer);
			}

		}
		String fileDir="" ;
		if(formName.indexOf("/")>0){
			String[] fileName = formName.split("/");
			fileDir = path + modelKey + "/" + fileName[1];
		}
	

		FileWriter fw = new FileWriter(fileDir);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(fileContent);
		bw.close();

//		res.put("result", "success");

		return "success";
	}

	/**
	 * 压缩文件并且上传部署
	 * 
	 */
	@RequestMapping(value = "form-design/deployFile")
	@ResponseBody
	public String deployFile(
			@RequestParam("modelId") String modelId,
			@RequestParam("modelKey") String modelKey,
			HttpServletResponse response, HttpServletRequest request,
			@RequestBody String fileContent) throws Exception {
		HashMap<String, String> res = new HashMap<String, String>();

		String spath = request.getRealPath("/") + "/../resources/form-design/"
				+ modelKey;
		String dpath = request.getRealPath("/") + "/../resources/form-design/"
				+ modelKey + ".bar";
		ZipUtils.createZip(spath, dpath);
		try {
			Deployment deployment = null;
			File deployFile = new File(dpath);
			InputStream in = new FileInputStream(deployFile);
			ZipInputStream zip = new ZipInputStream(in);
			deployment = repositoryService.createDeployment()
					.addZipInputStream(zip).deploy();

		} catch (Exception e) {
			logger.error(
					"error on deploy process, because of file input stream", e);
		}

		// res.put("result", "success");

		return "success";
	}

	/**
	 * 初始化启动流程，读取启动流程的表单内容来渲染start form
	 */
	@RequestMapping(value = "get-form/start/{processDefinitionId}")
	@ResponseBody
	public Object findStartForm(
			@PathVariable("processDefinitionId") String processDefinitionId)
			throws Exception {

		// 根据流程定义ID读取外置表单
		Object startForm = formService
				.getRenderedStartForm(processDefinitionId);
//		StartFormData startForm = formService.getStartFormData(processDefinitionId);

		return startForm;
	}

	/**
	 * 读取Task的表单
	 */
	@RequestMapping(value = "get-form/task/{taskId}/processInstanceId/{processInstanceId}")
	@ResponseBody
	public Map<String, Object> findTaskForm(
			@PathVariable("taskId") String taskId,
			@PathVariable("processInstanceId") String processInstanceId)
			throws Exception {
		Object renderedTaskForm = formService.getRenderedTaskForm(taskId);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("formData", renderedTaskForm);
		HistoricProcessInstance applicant = historyService
				.createHistoricProcessInstanceQuery()
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
	public String completeTask(@PathVariable("taskId") String taskId,
			@PathVariable("allType") String allType,
			RedirectAttributes redirectAttributes, HttpServletRequest request) {
		Map<String, String> formProperties = new HashMap<String, String>();

		// 从request中读取参数然后转换
		Map<String, String[]> parameterMap = request.getParameterMap();
		Set<Entry<String, String[]>> entrySet = parameterMap.entrySet();
		for (Entry<String, String[]> entry : entrySet) {
			String key = entry.getKey();

			/*
			 * 参数结构：fq_reason，用_分割 fp的意思是form paremeter 最后一个是属性名称
			 */
			if (StringUtils.defaultString(key).startsWith("fp_")) {
				String[] paramSplit = key.split("_");
				formProperties.put(paramSplit[1], entry.getValue()[0]);
			}

			formProperties.put(key, entry.getValue()[0]);

		}

		logger.debug("start form parameters: {}", formProperties);

		User user = UserUtil.getUserFromSession(request.getSession());

		// 用户未登录不能操作，实际应用使用权限框架实现，例如Spring Security、Shiro等
		if (user == null || StringUtils.isBlank(user.getId())) {
			return "redirect:/login?timeout=true";
		}
		try {
			Task task = taskService.createTaskQuery().taskId(taskId)
					.singleResult();

			if (task.getOwner() != null)
				taskService.resolveTask(taskId);
			else
				identityService.setAuthenticatedUserId(user.getId());
			formService.submitTaskFormData(taskId, formProperties);
		} finally {
			identityService.setAuthenticatedUserId(null);
		}

		redirectAttributes
				.addFlashAttribute("message", "任务完成：taskId=" + taskId);
		if (allType.equals("allType"))
			return "redirect:/form/dynamic/task/list/allType";
		else
			return "redirect:/form/formkey/task/list";
	}

	/**
	 * 读取启动流程的表单字段
	 */
	@RequestMapping(value = "start-process/{processDefinitionId}/{allType}")
	@SuppressWarnings("unchecked")
	public String submitStartFormAndStartProcessInstance(
			@PathVariable("processDefinitionId") String processDefinitionId,
			@PathVariable("allType") String allType,
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
			else
				formProperties.put(key, entry.getValue()[0]); 
				
		}

		logger.debug("start form parameters: {}", formProperties);

		User user = UserUtil.getUserFromSession(request.getSession());
		// 用户未登录不能操作，实际应用使用权限框架实现，例如Spring Security、Shiro等
		if (user == null || StringUtils.isBlank(user.getId())) {
			return "redirect:/login?timeout=true";
		}
		try {
			identityService.setAuthenticatedUserId(user.getId());

			ProcessInstance processInstance = formService.submitStartFormData(
					processDefinitionId, formProperties);
			logger.debug("start a processinstance: {}", processInstance);

			redirectAttributes.addFlashAttribute("message", "启动成功，流程ID："
					+ processInstance.getId());
		} finally {
			identityService.setAuthenticatedUserId(null);
		}
		if (allType.equals("allType"))
			return "redirect:/form/dynamic/process-list/allType";
		else
			return "redirect:/form/formkey/process-list";
	}

	/**
	 * task列表
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "task/list")
	public ModelAndView taskList(Model model, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("/form/formkey/formkey-task-list");
		User user = UserUtil.getUserFromSession(request.getSession());
		Page<Task> page = new Page<Task>(PageUtil.PAGE_SIZE);
		int[] pageParams = PageUtil.init(page, request);

		/**
		 * 这里为了演示区分开自定义表单的请假流程，值读取leave-formkey
		 */

		// 已经签收的或者直接分配到当前人的任务
		// String asigneeSql = "select distinct RES.* from ACT_RU_TASK RES inner
		// join ACT_RE_PROCDEF D on RES.PROC_DEF_ID_ = D.ID_ WHERE RES.ASSIGNEE_
		// = #{userId}"
		// + " and D.KEY_ = #{processDefinitionKey} and RES.SUSPENSION_STATE_ =
		// #{suspensionState}";
		//
		// // 当前人在候选人或者候选组范围之内
		// String needClaimSql = "select distinct RES1.* from ACT_RU_TASK RES1
		// inner join ACT_RU_IDENTITYLINK I on I.TASK_ID_ = RES1.ID_ inner join
		// ACT_RE_PROCDEF D1 on RES1.PROC_DEF_ID_ = D1.ID_ WHERE"
		// + " D1.KEY_ = #{processDefinitionKey} and RES1.ASSIGNEE_ is null and
		// I.TYPE_ = 'candidate'"
		// + " and ( I.USER_ID_ = #{userId} or I.GROUP_ID_ IN (select
		// g.GROUP_ID_ from ACT_ID_MEMBERSHIP g where g.USER_ID_ = #{userId} )
		// )"
		// + " and RES1.SUSPENSION_STATE_ = #{suspensionState}";
		// String sql = asigneeSql + " union all " + needClaimSql;
		// NativeTaskQuery query = taskService.createNativeTaskQuery().sql(sql)
		// .parameter("processDefinitionKey", "leave-formkey")
		// .parameter("suspensionState",
		// SuspensionState.ACTIVE.getStateCode()).parameter("userId",
		// user.getId());
		// List<Task> tasks = query.listPage(pageParams[0], pageParams[1]);

		TaskQuery leaveFormKey = taskService.createTaskQuery()
				.processDefinitionKey("leave-formkey")
				.taskCandidateOrAssigned(user.getId()).active().orderByTaskId()
				.desc();

		List<Task> tasks = leaveFormKey.listPage(pageParams[0], pageParams[1]);

		page.setResult(tasks);
		// page.setTotalCount(query.sql("select count(*) from (" + sql + ") as
		// CT").count());
		page.setTotalCount(leaveFormKey.count());
		mav.addObject("page", page);
		return mav;
	}

	/**
	 * 签收任务
	 */
	@RequestMapping(value = "task/claim/{id}")
	public String claim(@PathVariable("id") String taskId, HttpSession session,
			RedirectAttributes redirectAttributes) {
		String userId = UserUtil.getUserFromSession(session).getId();
		taskService.claim(taskId, userId);
		redirectAttributes.addFlashAttribute("message", "任务已签收");
		return "redirect:/form/formkey/task/list";
	}

	/**
	 * 运行中的流程实例
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "process-instance/running/list")
	public ModelAndView running(Model model, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("/form/running-list");
		Page<ProcessInstance> page = new Page<ProcessInstance>(
				PageUtil.PAGE_SIZE);
		int[] pageParams = PageUtil.init(page, request);
		ProcessInstanceQuery query = runtimeService
				.createProcessInstanceQuery()
				.processDefinitionKey("leave-formkey").active()
				.orderByProcessInstanceId().desc();
		List<ProcessInstance> list = query.listPage(pageParams[0],
				pageParams[1]);
		page.setResult(list);
		page.setTotalCount(query.count());
		mav.addObject("page", page);
		return mav;
	}

	/**
	 * 已结束的流程实例
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "process-instance/finished/list")
	public ModelAndView finished(Model model, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("/form/finished-list");
		Page<HistoricProcessInstance> page = new Page<HistoricProcessInstance>(
				PageUtil.PAGE_SIZE);
		int[] pageParams = PageUtil.init(page, request);
		HistoricProcessInstanceQuery query = historyService
				.createHistoricProcessInstanceQuery()
				.processDefinitionKey("leave-formkey")
				.orderByProcessInstanceEndTime().desc().finished();
		List<HistoricProcessInstance> list = query.listPage(pageParams[0],
				pageParams[1]);

		page.setResult(list);
		page.setTotalCount(query.count());
		mav.addObject("page", page);
		return mav;
	}

}
