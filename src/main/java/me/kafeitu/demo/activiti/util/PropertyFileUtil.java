package me.kafeitu.demo.activiti.util;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.DefaultPropertiesPersister;
import org.springframework.util.PropertiesPersister;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * ϵͳ���Թ�����
 *
 * @author HenryYan
 */
public class PropertyFileUtil {


    private static final String DEFAULT_ENCODING = "UTF-8";
    private static Logger logger = LoggerFactory.getLogger(PropertyFileUtil.class);
    private static Properties properties;
    private static PropertiesPersister propertiesPersister = new DefaultPropertiesPersister();
    private static ResourceLoader resourceLoader = new DefaultResourceLoader();
    private static Properties activePropertyFiles = null;
    private static String PROFILE_ID = StringUtils.EMPTY;
    public static boolean INITIALIZED = false; // �Ƿ��ѳ�ʼ��

    /**
     * ��ʼ����ȡ�����ļ�����ȡ���ļ��б�λ��classpath�����application-files.properties<br/>
     * <p/>
     * ��������ļ����������ĸ�����ͬ����ֵ
     *
     * @throws IOException ��ȡ�����ļ�ʱ
     */
    public static void init() throws IOException {
        String fileNames = "application-files.properties";
        PROFILE_ID = StringUtils.EMPTY;
        innerInit(fileNames);
        activePropertyFiles(fileNames);
        INITIALIZED = true;
    }

    /**
     * ��ʼ����ȡ�����ļ�����ȡ���ļ��б�λ��classpath�����application-[type]-files.properties<br/>
     * <p/>
     * ��������ļ����������ĸ�����ͬ����ֵ
     *
     * @param profile �����ļ����ͣ�application-[profile]-files.properties
     * @throws IOException ��ȡ�����ļ�ʱ
     */
    public static void init(String profile) throws IOException {
        if (StringUtils.isBlank(profile)) {
            init();
        } else {
            PROFILE_ID = profile;
            String fileNames = "application-" + profile + "-files.properties";
            innerInit(fileNames);
        }
        INITIALIZED = true;
    }

    /**
     * �ڲ�����
     *
     * @param fileName
     * @throws IOException
     */
    private static void innerInit(String fileName) throws IOException {
        String[] propFiles = activePropertyFiles(fileName);
        logger.debug("��ȡ�����ļ���{}", ArrayUtils.toString(propFiles));
        properties = loadProperties(propFiles);
        Set<Object> keySet = properties.keySet();
        for (Object key : keySet) {
            logger.debug("property: {}, value: {}", key, properties.getProperty(key.toString()));
        }
    }

    /**
     * ��ȡ��ȡ����Դ�ļ��б�
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    private static String[] activePropertyFiles(String fileName) throws IOException {
        logger.info("��ȡ" + fileName);
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream resourceAsStream = loader.getResourceAsStream(fileName);
        // Ĭ�ϵ�Propertiesʵ��ʹ��HashMap�㷨��Ϊ�˱���ԭ��˳��ʹ������Map
        activePropertyFiles = new LinkedProperties();
        activePropertyFiles.load(resourceAsStream);

        Set<Object> fileKeySet = activePropertyFiles.keySet();
        String[] propFiles = new String[fileKeySet.size()];
        List<Object> fileList = new ArrayList<Object>();

        fileList.addAll(activePropertyFiles.keySet());
        for (int i = 0; i < propFiles.length; i++) {
            String fileKey = fileList.get(i).toString();
            propFiles[i] = activePropertyFiles.getProperty(fileKey);
        }
        return propFiles;
    }

    /**
     * ������properties�ļ�, ��ͬ�����������������ļ��е�ֵ���Ḳ��֮ǰ������.
     * �ļ�·��ʹ��Spring Resource��ʽ, �ļ�����ʹ��UTF-8.
     *
     * @see org.springframework.beans.factory.config.PropertyPlaceholderConfigurer
     */
    public static Properties loadProperties(String... resourcesPaths) throws IOException {
        Properties props = new Properties();

        for (String location : resourcesPaths) {

            logger.debug("Loading properties file from:" + location);

            InputStream is = null;
            try {
                Resource resource = resourceLoader.getResource(location);
                is = resource.getInputStream();
                propertiesPersister.load(props, new InputStreamReader(is, DEFAULT_ENCODING));
            } catch (IOException ex) {
                logger.info("Could not load properties from classpath:" + location + ": " + ex.getMessage());
            } finally {
                if (is != null) {
                    is.close();
                }
            }
        }
        return props;
    }

    /**
     * ��ȡ���е�key
     *
     * @return
     */
    public static Set<String> getKeys() {
        return properties.stringPropertyNames();
    }

    /**
     * ��ȡ��ֵ��Map
     *
     * @return
     */
    public static Map<String, String> getKeyValueMap() {
        Set<String> keys = getKeys();
        Map<String, String> values = new HashMap<String, String>();
        for (String key : keys) {
            values.put(key, get(key));
        }
        return values;
    }

    /**
     * ��ȡ����ֵ
     *
     * @param key ��
     * @return ֵ
     */
    public static String get(String key) {
        String propertyValue = properties.getProperty(key);
        logger.debug("��ȡ���ԣ�{}��ֵ��{}", key, propertyValue);
        return propertyValue;
    }

    /**
     * ��ȡ����ֵ
     *
     * @param key          ��
     * @param defaultValue Ĭ��ֵ
     * @return ֵ
     */
    public static String get(String key, String defaultValue) {
        String propertyValue = properties.getProperty(key);
        String value = StringUtils.defaultString(propertyValue, defaultValue);
        logger.debug("��ȡ���ԣ�{}��ֵ��{}", key, value);
        return value;
    }

    /**
     * ���ڴ��������
     *
     * @param key   ��
     * @param value ֵ
     */
    public static void add(String key, String value) {
        properties.put(key, value);
        logger.debug("ͨ������������Ե��ڴ棺{}��ֵ��{}", key, value);
    }

    public static Properties getActivePropertyFiles() {
        return activePropertyFiles;
    }

    public static String getProfile() {
        return PROFILE_ID;
    }
}