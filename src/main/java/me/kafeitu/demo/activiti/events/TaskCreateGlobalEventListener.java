package me.kafeitu.demo.activiti.events;

import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.event.ActivitiEntityEvent;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;

import me.kafeitu.demo.activiti.web.websocket.MyWebSocket;
import me.kafeitu.demo.activiti.web.websocket.MyWebSocketServlet;

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
			
			ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

			TaskService taskService = processEngine.getTaskService();

			TaskQuery tasks = taskService.createTaskQuery().taskCandidateOrAssigned(assignee).active();
			List<MyWebSocket> sockets = MyWebSocketServlet.getSocketList();
			for (MyWebSocket socket : sockets) {
				if (socket.getUsername().equals(assignee)) {

					String message = "���յ�һ��������   Ŀǰ����������" + tasks.count() + "�   ������Ϣ������";
					System.out.println("###########################-------->" + (tasks.count()+1));
					socket.onMessage(message);
				}
			}

		}

	}

	@Override
	public boolean isFailOnException() {
		// TODO Auto-generated method stub
		return false;
	}

}
