package me.kafeitu.demo.activiti.service;

import java.util.Calendar;
import java.util.Date;

import org.activiti.engine.IdentityService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.identity.User;

/**
 * �������--�ʼ�������������������÷����ʼ�ʱ��һЩ����
 *
 * @author 
 */
public class SetMailInfo implements ExecutionListener {

	private static final long serialVersionUID = 1L;

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		IdentityService identityService = execution.getEngineServices().getIdentityService();
		String applyUserId = (String) execution.getVariable("applyUserId");
		User user = identityService.createUserQuery().userId(applyUserId).singleResult();
		String to = user.getEmail();
		execution.setVariableLocal("to", to);
		String userName = user.getFirstName() + " " + user.getLastName();
		execution.setVariableLocal("name", userName);

		// ��ʱ����ʱ�����ã�Ϊ�˷�����ԣ�����Ϊ�������񵽴�һ���Ӻ�����
		// Date endDate = (Date) execution.getVariable("endDate");
		Calendar ca = Calendar.getInstance();
		// ca.setTime(endDate);
		ca.add(Calendar.MINUTE, 1);
		System.out.println("ʱ�䣺������������������������" + ca.toString());
		execution.setVariableLocal("reportBackTimeout", ca.getTime());
	}

}