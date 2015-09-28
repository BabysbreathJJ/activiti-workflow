package me.kafeitu.demo.activiti.webservice;

import javax.xml.ws.Endpoint;

import org.apache.cxf.endpoint.Server;

/**
 * ���Webservice����
 * @author: Henry Yan
 */
public class LeaveWebserviceUtil {

    public static final String WEBSERVICE_URL = "http://localhost:8088/leave";
    public static final String WEBSERVICE_WSDL_URL = WEBSERVICE_URL + "?wsdl";
    public static final String WEBSERVICE_URI = "http://webservice.kafeitu.me/";

    private static Server server = null;

    /**
     * ����Webservice
     */
    public static void startServer() {
        if (server != null) {
            System.out.println("WebService�����������С�����");
            return;
        }
        LeaveWebService leaveWebService = new LeaveWebServiceImpl();
        /*JaxWsServerFactoryBean svrFactory = new JaxWsServerFactoryBean();
        svrFactory.setServiceClass(LeaveWebService.class);
        svrFactory.setAddress(WEBSERVICE_URL);
        svrFactory.setServiceBean(leaveWebService);
        svrFactory.getInInterceptors().add(new LoggingInInterceptor());
        svrFactory.getOutInterceptors().add(new LoggingOutInterceptor());
        server = svrFactory.create();
        server.start();*/
        Endpoint.publish(WEBSERVICE_URL, leaveWebService);
        System.out.println("���Webservice�ѷ�����" + WEBSERVICE_URL + "?wsdl");
    }

    /**
     * ֹͣWebservice����
     */
    public static void stopServer() {
        if (server != null) {
            server.destroy();
        }
    }

    public static void main(String[] args) {
        startServer();
    }

}
