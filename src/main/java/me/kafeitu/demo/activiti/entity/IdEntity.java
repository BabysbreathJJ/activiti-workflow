package me.kafeitu.demo.activiti.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * ͳһ����id��entity����.
 * <p/>
 * ����ͳһ����id���������ơ��������͡�����ӳ�估���ɲ���.
 * ���������getId()�����ض���id������ӳ������ɲ���.
 *
 * @author calvin
 */
//JPA ����ı�ʶ
@MappedSuperclass
public abstract class IdEntity {

    protected Long id;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@GeneratedValue(strategy = GenerationType.SEQUENCE)
    //@GeneratedValue(generator = "system-uuid")
    //@GenericGenerator(name = "system-uuid", strategy = "uuid")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
