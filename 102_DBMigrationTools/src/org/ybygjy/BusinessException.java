package org.ybygjy;

/**
 * �߼������쳣
 * <p>
 * ���ǵ��ֲ�ṹ������쳣��ʧ���쳣��Ϣ��ȫ�������
 * <p>
 * ���ฺ����ҵ���߼����쳣�� ������ϵͳ��Ҫ����{@link Exception}�ļ̳й�ϵ������ض�������ת��
 * <p>
 * ʾ������UI���������´���
 * 
 * <pre>
 * try {
 *  DBUtils.testConnection(dbInfo);
 * } catch(Exception e) {
 *  throws new UIException("���ݿ�����ʧ��", e);
 * }
 * </pre>
 * @author WangYanCheng
 * @version 2012-11-15
 */
public class BusinessException extends Exception {
    /**
     * serial number
     */
    private static final long serialVersionUID = 2796913021739651254L;

    public BusinessException(String message) {
        super(message);
    }
    public BusinessException(Throwable cause) {
        this("", cause);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
