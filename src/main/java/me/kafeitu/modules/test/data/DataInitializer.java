package me.kafeitu.modules.test.data;

import org.springframework.beans.factory.InitializingBean;

import javax.sql.DataSource;

/**
 * �������ݳ�ʼ������
 *
 * @author calvin
 */
public class DataInitializer implements InitializingBean {

    private DataSource dataSource;

    private String dataFile;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setDataFile(String dataFile) {
        this.dataFile = dataFile;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        DataFixtures.reloadData(dataSource, dataFile);
    }
}
