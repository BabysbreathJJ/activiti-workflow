/**
 * Copyright (c) 2005-2012 springside.org.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package me.kafeitu.modules.test.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import javax.sql.DataSource;

/**
 * Spring��֧�����ݿ����, ������ƺ�����ע���JUnit4 ���ɲ��Ի���.
 * ���Springԭ�������ָ��̲�������dataSource����.
 * <p/>
 * ������Ҫ����applicationContext�ļ���λ��, ��:
 *
 * @author calvin
 * @ContextConfiguration(locations = { "/applicationContext-test.xml" })
 */
@ActiveProfiles("test")
public abstract class SpringTransactionalTestCase extends AbstractTransactionalJUnit4SpringContextTests {

    protected DataSource dataSource;

    @Override
    @Autowired
    public void setDataSource(DataSource dataSource) {
        super.setDataSource(dataSource);
        this.dataSource = dataSource;
    }
}
