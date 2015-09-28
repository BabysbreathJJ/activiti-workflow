package me.kafeitu.demo.activiti.events.TaskAutoRedirectGlobalEventListener;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.delegate.event.ActivitiEntityEvent;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.apache.commons.lang3.StringUtils;

public class TaskAutoRedirectGlobalEventListener implements ActivitiEventListener {

	private static Map<String, String> userMap = new HashMap<String, String>();

	static {
		userMap.put("admin", "leaderuser");
	}

	@Override
	public void onEvent(ActivitiEvent event) {
		// TODO Auto-generated method stub
		ActivitiEntityEvent entityEvent = (ActivitiEntityEvent) event;
		Object entity = entityEvent.getEntity();
		if(entity instanceof TaskEntity){
			TaskEntity task = (TaskEntity) entity;
//		    task.get
			String originUserId = task.getAssignee();
			String newUserId = userMap.get(originUserId);
			if(StringUtils.isNotBlank(newUserId)){
				task.setAssignee(newUserId);
			}
		}

	}

	@Override
	public boolean isFailOnException() {
		// TODO Auto-generated method stub
		return false;
	}

}
