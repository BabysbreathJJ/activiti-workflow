package me.kafeitu.demo.activiti.webservice;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * ���WebService�ӿ�
 * @author: Henry Yan
 */
@WebService
public interface LeaveWebService {

    /**
     * �Ƿ���Ҫ�ܾ�������
     * @param startDate ��ٿ�ʼʱ��
     * @param endDate   ��ٽ���ʱ��
     * @return  true|false
     */
    @WebResult(name="needed")
    boolean generalManagerAudit(@WebParam(name = "startDate") XMLGregorianCalendar startDate, @WebParam(name = "endDate") XMLGregorianCalendar endDate) throws Exception;

}
