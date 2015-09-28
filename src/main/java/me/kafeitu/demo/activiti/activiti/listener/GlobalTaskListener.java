package me.kafeitu.demo.activiti.activiti.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;

import me.kafeitu.demo.activiti.web.websocket.SystemWebSocketHandler;

/**
 * User: henryyan
 */
public class GlobalTaskListener implements TaskListener {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	

	@Override
	public void notify(DelegateTask delegateTask) {
		logger.debug("������ȫ�ּ�����, pid={}, tid={}, event={}", new Object[] { delegateTask.getProcessInstanceId(),
				delegateTask.getId(), delegateTask.getEventName() });
		
	}
}
