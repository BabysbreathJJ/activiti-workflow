package me.kafeitu.demo.activiti.service.activiti;

import me.kafeitu.demo.activiti.util.WorkflowUtils;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 宸ヤ綔娴佽窡韪浉鍏砈ervice
 *
 * @author HenryYan
 */
@Component
public class WorkflowTraceService {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    protected RuntimeService runtimeService;

    @Autowired
    protected TaskService taskService;

    @Autowired
    protected RepositoryService repositoryService;

    @Autowired
    protected IdentityService identityService;

    /**
     * 娴佺▼璺熻釜鍥�
     *
     * @param processInstanceId 娴佺▼瀹炰緥ID
     * @return 灏佽浜嗗悇绉嶈妭鐐逛俊鎭�
     */
    public List<Map<String, Object>> traceProcess(String processInstanceId) throws Exception {
        Execution execution = runtimeService.createExecutionQuery().executionId(processInstanceId).singleResult();//鎵ц瀹炰緥
        Object property = PropertyUtils.getProperty(execution, "activityId");
        String activityId = "";
        if (property != null) {
            activityId = property.toString();
        }
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId)
                .singleResult();
        ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                .getDeployedProcessDefinition(processInstance.getProcessDefinitionId());
        List<ActivityImpl> activitiList = processDefinition.getActivities();//鑾峰緱褰撳墠浠诲姟鐨勬墍鏈夎妭鐐�

        List<Map<String, Object>> activityInfos = new ArrayList<Map<String, Object>>();
        for (ActivityImpl activity : activitiList) {

            boolean currentActiviti = false;
            String id = activity.getId();

            // 褰撳墠鑺傜偣
            if (id.equals(activityId)) {
                currentActiviti = true;
            }

            Map<String, Object> activityImageInfo = packageSingleActivitiInfo(activity, processInstance, currentActiviti);

            activityInfos.add(activityImageInfo);
        }

        return activityInfos;
    }

    /**
     * 灏佽杈撳嚭淇℃伅锛屽寘鎷細褰撳墠鑺傜偣鐨刋銆乊鍧愭爣銆佸彉閲忎俊鎭�佷换鍔＄被鍨嬨�佷换鍔℃弿杩�
     *
     * @param activity
     * @param processInstance
     * @param currentActiviti
     * @return
     */
    private Map<String, Object> packageSingleActivitiInfo(ActivityImpl activity, ProcessInstance processInstance,
                                                          boolean currentActiviti) throws Exception {
        Map<String, Object> vars = new HashMap<String, Object>();
        Map<String, Object> activityInfo = new HashMap<String, Object>();
        activityInfo.put("currentActiviti", currentActiviti);
        setPosition(activity, activityInfo);
        setWidthAndHeight(activity, activityInfo);

        Map<String, Object> properties = activity.getProperties();
        vars.put("浠诲姟绫诲瀷", WorkflowUtils.parseToZhType(properties.get("type").toString()));

        ActivityBehavior activityBehavior = activity.getActivityBehavior();
        logger.debug("activityBehavior={}", activityBehavior);
        if (activityBehavior instanceof UserTaskActivityBehavior) {

            Task currentTask = null;

			/*
             * 褰撳墠鑺傜偣鐨則ask
			 */
            if (currentActiviti) {
                currentTask = getCurrentTaskInfo(processInstance);
            }

			/*
			 * 褰撳墠浠诲姟鐨勫垎閰嶈鑹�
			 */
            UserTaskActivityBehavior userTaskActivityBehavior = (UserTaskActivityBehavior) activityBehavior;
            TaskDefinition taskDefinition = userTaskActivityBehavior.getTaskDefinition();
            Set<Expression> candidateGroupIdExpressions = taskDefinition.getCandidateGroupIdExpressions();
            if (!candidateGroupIdExpressions.isEmpty()) {

                // 浠诲姟鐨勫鐞嗚鑹�
                setTaskGroup(vars, candidateGroupIdExpressions);

                // 褰撳墠澶勭悊浜�
                if (currentTask != null) {
                    setCurrentTaskAssignee(vars, currentTask);
                }
            }
        }

        vars.put("鑺傜偣璇存槑", properties.get("documentation"));

        String description = activity.getProcessDefinition().getDescription();
        vars.put("鎻忚堪", description);

        logger.debug("trace variables: {}", vars);
        activityInfo.put("vars", vars);
        return activityInfo;
    }

    private void setTaskGroup(Map<String, Object> vars, Set<Expression> candidateGroupIdExpressions) {
        String roles = "";
        for (Expression expression : candidateGroupIdExpressions) {
            String expressionText = expression.getExpressionText();
            String roleName = identityService.createGroupQuery().groupId(expressionText).singleResult().getName();
            roles += roleName;
        }
        vars.put("浠诲姟鎵�灞炶鑹�", roles);
    }

    /**
     * 璁剧疆褰撳墠澶勭悊浜轰俊鎭�
     *
     * @param vars
     * @param currentTask
     */
    private void setCurrentTaskAssignee(Map<String, Object> vars, Task currentTask) {
        String assignee = currentTask.getAssignee();
        if (assignee != null) {
            User assigneeUser = identityService.createUserQuery().userId(assignee).singleResult();
            String userInfo = assigneeUser.getFirstName() + " " + assigneeUser.getLastName();
            vars.put("褰撳墠澶勭悊浜�", userInfo);
        }
    }

    /**
     * 鑾峰彇褰撳墠鑺傜偣淇℃伅
     *
     * @param processInstance
     * @return
     */
    private Task getCurrentTaskInfo(ProcessInstance processInstance) {
        Task currentTask = null;
        try {
            String activitiId = (String) PropertyUtils.getProperty(processInstance, "activityId");
            logger.debug("current activity id: {}", activitiId);

            currentTask = taskService.createTaskQuery().processInstanceId(processInstance.getId()).taskDefinitionKey(activitiId)
                    .singleResult();
            logger.debug("current task for processInstance: {}", ToStringBuilder.reflectionToString(currentTask));

        } catch (Exception e) {
            logger.error("can not get property activityId from processInstance: {}", processInstance);
        }
        return currentTask;
    }

    /**
     * 璁剧疆瀹藉害銆侀珮搴﹀睘鎬�
     *
     * @param activity
     * @param activityInfo
     */
    private void setWidthAndHeight(ActivityImpl activity, Map<String, Object> activityInfo) {
        activityInfo.put("width", activity.getWidth());
        activityInfo.put("height", activity.getHeight());
    }

    /**
     * 璁剧疆鍧愭爣浣嶇疆
     *
     * @param activity
     * @param activityInfo
     */
    private void setPosition(ActivityImpl activity, Map<String, Object> activityInfo) {
        activityInfo.put("x", activity.getX());
        activityInfo.put("y", activity.getY());
    }
}
