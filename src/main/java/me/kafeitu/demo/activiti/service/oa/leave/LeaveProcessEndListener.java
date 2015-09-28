package me.kafeitu.demo.activiti.service.oa.leave;

import me.kafeitu.demo.activiti.dao.ActivitiDao;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ������̽���������
 *
 * @author: Henry Yan
 */
@Service
@Transactional
public class LeaveProcessEndListener implements ExecutionListener {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    ActivitiDao activitiDao;

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        String processInstanceId = execution.getProcessInstanceId();

        int i = activitiDao.deleteFormPropertyByProcessInstanceId(processInstanceId);
        logger.debug("������ {} ����ʷ������", i);
    }
}
