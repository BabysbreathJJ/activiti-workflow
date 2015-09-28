package me.kafeitu.modules.web.servlet;

import me.kafeitu.demo.activiti.util.LinkedProperties;
import me.kafeitu.demo.activiti.util.PropertyFileUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Set;

/**
 * classpath��������������ļ���ȡ��ʼ����
 */
public class PropertiesServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    protected Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * @see HttpServlet#HttpServlet()
     */
    public PropertiesServlet() {
        super();
    }

    /**
     * @see Servlet#init(ServletConfig)
     */
    public void init(ServletConfig config) throws ServletException {
        try {
            String profile = config.getInitParameter("profile");
            if (PropertyFileUtil.INITIALIZED) {
                logger.info("---- �ѳ�ʼ�������Ա��γ�ʼ������ ----");
                setParameterToServerContext(config.getServletContext());
                return;
            }
            if (StringUtils.isNotBlank(profile)) {
                logger.info("�����ض�Profile=" + profile);
                PropertyFileUtil.init(profile);
            } else {
                PropertyFileUtil.init();
                logger.info("����Ĭ��Profile");
            }
            setParameterToServerContext(config.getServletContext());
            logger.info("++++ ��ʼ��[classpath��������������ļ�]��� ++++");
        } catch (IOException e) {
            logger.error("��ʼ��classpath�µ������ļ�ʧ��", e);
        }
    }

    /**
     * �󶨲�����ServletContext
     *
     * @param servletContext
     */
    private void setParameterToServerContext(ServletContext servletContext) {
        servletContext.setAttribute("prop", PropertyFileUtil.getKeyValueMap());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = StringUtils.defaultString(req.getParameter("action"));
        resp.setContentType("text/plain;charset=UTF-8");
        if ("reload".equals(action)) { // ����
            try {
                String profile = StringUtils.defaultString(req.getParameter("profile"), PropertyFileUtil.getProfile());
                if (StringUtils.isNotBlank(profile)) {
                    logger.info("�������ã�ʹ���ض�Profile=" + profile);
                }
                PropertyFileUtil.init(profile);

                setParameterToServerContext(req.getSession().getServletContext());
                logger.info("++++ ��������������ļ��������� ++++��{IP={}}", req.getRemoteAddr());
                resp.getWriter().print("<b>�����ļ����سɹ���</b><br/>");
                writeProperties(resp);
            } catch (IOException e) {
                logger.error("���������ļ�ʧ��", e);
            }
        } else if ("getprop".equals(action)) { // ��ȡ����
            String key = StringUtils.defaultString(req.getParameter("key"));
            resp.getWriter().print(ObjectUtils.toString(PropertyFileUtil.get(key)));
        } else if ("list-all".equals(action)) { // ��ȡȫ������
            writeProperties(resp);
        } else if ("list-split".equals(action)) { // ���ļ���ȡȫ������
            writePropertiesBySplit(resp);
        } else if ("files".equals(action)) {
            writeActiveFiles(resp);
        } else if ("save".equals(action)) {
            String parameterName = StringUtils.defaultString(req.getParameter("parameterName"));
            String parameterValue = StringUtils.defaultString(req.getParameter("parameterValue"));
            saveParameter(parameterName, parameterValue, resp);
        } else if ("delete".equals(action)) {
            String parameterKey = StringUtils.defaultString(req.getParameter("parameterKey"));
            deleteParameter(parameterKey, resp);
        } else {
            writeNav(req, resp);
        }
    }

    /**
     * ����/�޸ı��������ļ�������
     *
     * @param parameterName
     * @param parameterValue
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void saveParameter(String parameterName, String parameterValue, HttpServletResponse resp) throws ServletException, IOException {
        InputStream inputStream = null;
        InputStream cInputStream = null;
        OutputStream out = null;
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            inputStream = loader.getResourceAsStream("application-files.properties");
            Properties props = new Properties();
            props.load(inputStream);
            String cFilePath = props.getProperty("C").split(":")[1];

            File file = new File(cFilePath);
            if (file.getParentFile() != null && !file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            Properties cProps = new Properties();
            cInputStream = new FileInputStream(file);
            cProps.load(cInputStream);
            out = new FileOutputStream(cFilePath);
            cProps.setProperty(parameterName, parameterValue);
            cProps.store(out, "Update:'" + parameterName + "' value:" + parameterValue);
            out.flush();
            out.close();
            cInputStream.close();
            inputStream.close();
            PropertyFileUtil.init();
            resp.getWriter().print("success");
        } catch (Exception e) {
            logger.error("����/�޸�����:", e);
            resp.getWriter().print("error:" + e.getMessage());
        } finally {
            if (null != inputStream) {
                inputStream.close();
            }
            if (null != cInputStream) {
                cInputStream.close();
            }
            if (null != out) {
                out.close();
            }
        }
    }

    /**
     * ɾ�����������ļ�������
     *
     * @param parameterKey
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void deleteParameter(String parameterKey, HttpServletResponse resp) throws ServletException, IOException {
        InputStream inputStream = null;
        InputStream cInputStream = null;
        OutputStream out = null;
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            inputStream = loader.getResourceAsStream("application-files.properties");
            Properties props = new Properties();
            props.load(inputStream);
            String cFilePath = props.getProperty("C").split(":")[1];

            File file = new File(cFilePath);
            if (file.getParentFile() != null && !file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            Properties cProps = new Properties();
            cInputStream = new FileInputStream(file);
            cProps.load(cInputStream);
            cProps.remove(parameterKey);
            out = new FileOutputStream(cFilePath);
            cProps.store(out, "delete:'" + parameterKey);
            out.flush();
            out.close();
            cInputStream.close();
            inputStream.close();
            PropertyFileUtil.init();
            resp.getWriter().print("success");
        } catch (Exception e) {
            logger.error("ɾ�����������ļ�������:", e);
            resp.getWriter().print("error:" + e.getMessage());
        } finally {
            if (null != inputStream) {
                inputStream.close();
            }
            if (null != cInputStream) {
                cInputStream.close();
            }
            if (null != out) {
                out.close();
            }
        }
    }

    /**
     * ��������Լ�ֵ�б�ҳ��
     *
     * @param resp
     * @throws IOException
     */
    protected void writeProperties(HttpServletResponse resp) throws IOException {
        Set<String> keys = PropertyFileUtil.getKeys();
        StringBuilder sb = new StringBuilder();
        for (String key : keys) {
            sb.append(key + "<span style='color:red;font-weight:bold;'>=</span>" + PropertyFileUtil.get(key) + "<br/>");
        }
        resp.setContentType("text/html");
        resp.getWriter().print("<html><body>" + sb.toString() + "</body></html>");
    }

    /**
     * ���ļ���ȡȫ������
     *
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    protected void writePropertiesBySplit(HttpServletResponse resp) throws ServletException, IOException {
        InputStream inputStream = null;
        InputStream cInputStream = null;
        StringBuilder sb = new StringBuilder();
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            inputStream = loader.getResourceAsStream("application-files.properties");
            Properties props = new LinkedProperties();
            props.load(inputStream);
            Set<Object> fileKeySet = props.keySet();
            for (Object obj : fileKeySet) {
                logger.debug("��ȡ�ļ�:key={}, value={}", obj, props.getProperty(obj.toString()));
                sb.append("<span style='color:red;font-weight:bold;'>" + props.getProperty(obj.toString()) + "</span><br/>");
                if (props.getProperty(obj.toString()).startsWith("file:")) {
                    File file = new File(props.getProperty(obj.toString()).split(":")[1]);
                    if (file.getParentFile() != null && !file.getParentFile().exists()) {
                        continue;
                    }
                    if (!file.exists()) {
                        continue;
                    }
                    cInputStream = new FileInputStream(file);
                } else {
                    cInputStream = loader.getResourceAsStream(props.getProperty(obj.toString()));
                }

                Properties cProps = new LinkedProperties();
                cProps.load(cInputStream);
                Set<Object> cFileKeySet = cProps.keySet();
                for (Object cObj : cFileKeySet) {
                    sb.append(cObj.toString() + "<span style='color:red;font-weight:bold;'>=</span>" + cProps.get(cObj.toString()) + "<br/>");
                }
                cInputStream.close();
            }
            inputStream.close();
            resp.setContentType("text/html");
            resp.getWriter().print("<html><body>" + sb.toString() + "</body></html>");
        } catch (Exception e) {
            logger.error("ɾ�����������ļ�������:", e);
            resp.getWriter().print("error:" + e.getMessage());
        } finally {
            if (null != inputStream) {
                inputStream.close();
            }
            if (null != cInputStream) {
                cInputStream.close();
            }
        }
    }

    /**
     * ������õ������ļ��б�ҳ��
     *
     * @param resp
     * @throws IOException
     */
    protected void writeActiveFiles(HttpServletResponse resp) throws IOException {
        Properties activePropertyFiles = PropertyFileUtil.getActivePropertyFiles();
        Enumeration<Object> keys = activePropertyFiles.keys();
        StringBuilder sb = new StringBuilder();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement().toString();
            sb.append(key + "<span style='color:red;font-weight:bold;'>=</span>" + activePropertyFiles.get(key) + "<br/>");
        }
        resp.setContentType("text/html");
        resp.getWriter().print("<html><body><h4>���ζ�ȡ���������ļ���Profile=" + PropertyFileUtil.getProfile() + "����</h4>" + sb.toString() + "</body></html>");
    }

    protected void writeNav(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        String contextPath = req.getContextPath();
        PrintWriter pw = resp.getWriter();
        String elementformat = "<li><a href='" + contextPath + "/servlet/properties?action=%1s' target='_blank'>%2s</a></li>";
        pw.println("<ul>");
        pw.println(String.format(elementformat, "files", "�����ļ��б�"));
        pw.println(String.format(elementformat, "list-all", "�����б�ȫ����"));
        pw.println(String.format(elementformat, "list-split", "�����б����ļ���"));
        pw.println(String.format(elementformat, "reload", "���¼���"));
        pw.println(String.format(elementformat, "getprop&key=sample", "��ȡ����"));
        pw.println("</ul>");
    }
}