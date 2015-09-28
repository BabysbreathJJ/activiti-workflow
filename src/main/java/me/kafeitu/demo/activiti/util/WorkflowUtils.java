package me.kafeitu.demo.activiti.util;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author HenryYan
 */
public class WorkflowUtils {

    private static Logger logger = LoggerFactory.getLogger(WorkflowUtils.class);

    /**
     * ת�����̽ڵ�����Ϊ����˵��
     *
     * @param type Ӣ������
     * @return ��������������
     */
    public static String parseToZhType(String type) {
        Map<String, String> types = new HashMap<String, String>();
        types.put("userTask", "�û�����");
        types.put("serviceTask", "ϵͳ����");
        types.put("startEvent", "��ʼ�ڵ�");
        types.put("endEvent", "�����ڵ�");
        types.put("exclusiveGateway", "�����жϽڵ�(ϵͳ�Զ�������������)");
        types.put("inclusiveGateway", "���д�������");
        types.put("callActivity", "������");
        return types.get(type) == null ? type : types.get(type);
    }

    /**
     * ����ͼƬ�ļ���Ӳ��
     *
     * @return �ļ���ȫ·��
     */
    public static String exportDiagramToFile(RepositoryService repositoryService, ProcessDefinition processDefinition, String exportDir) throws IOException {
        String diagramResourceName = processDefinition.getDiagramResourceName();
        String key = processDefinition.getKey();
        int version = processDefinition.getVersion();
        String diagramPath = "";

        InputStream resourceAsStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), diagramResourceName);
        byte[] b = new byte[resourceAsStream.available()];

        @SuppressWarnings("unused")
        int len = -1;
        resourceAsStream.read(b, 0, b.length);

        // create file if not exist
        String diagramDir = exportDir + "/" + key + "/" + version;
        File diagramDirFile = new File(diagramDir);
        if (!diagramDirFile.exists()) {
            diagramDirFile.mkdirs();
        }
        diagramPath = diagramDir + "/" + diagramResourceName;
        File file = new File(diagramPath);

        // �ļ������˳�
        if (file.exists()) {
            // �ļ���С��ͬʱֱ�ӷ��ط������´����ļ�(������)
            logger.debug("diagram exist, ignore... : {}", diagramPath);
            return diagramPath;
        } else {
            file.createNewFile();
        }

        logger.debug("export diagram to : {}", diagramPath);

        // wirte bytes to file
        FileUtils.writeByteArrayToFile(file, b, true);
        return diagramPath;
    }

}
