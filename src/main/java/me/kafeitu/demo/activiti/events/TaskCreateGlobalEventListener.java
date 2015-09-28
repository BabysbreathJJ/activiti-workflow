package me.kafeitu.demo.activiti.events;

import java.util.Map;

import org.activiti.engine.delegate.event.ActivitiEntityEvent;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.impl.persistence.entity.TaskEntity;

public class TaskCreateGlobalEventListener implements ActivitiEventListener {

	@Override
	public void onEvent(ActivitiEvent event) {
		// TODO Auto-generated method stub

		ActivitiEntityEvent entityEvent = (ActivitiEntityEvent) event;
		Object entity = entityEvent.getEntity();
		if (entity instanceof TaskEntity) {
			TaskEntity task = (TaskEntity) entity;
			String assignee = task.getAssignee();
			if (assignee != null && !assignee.equals("")) {
				System.out.println("���񴴽���������������������������������������������������" + assignee);
			}

		}

	}

	@Override
	public boolean isFailOnException() {
		// TODO Auto-generated method stub
		return false;
	}

}
