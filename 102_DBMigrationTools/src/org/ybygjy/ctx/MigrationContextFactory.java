package org.ybygjy.ctx;

import org.ybygjy.MigrationContext;

/**
 * {@link MigrationContext}����
 * @author WangYanCheng
 * @version 2012-10-17
 */
public class MigrationContextFactory {
    /**singleton pattern*/
    private static MigrationContextFactory mcfInst = new MigrationContextFactory();
    /**{@link MigrationContext}ʵ��*/
    private static MigrationContext mcInst;
    /**
     * Constructor
     */
    public MigrationContextFactory() {
        //TODO �˴����ڶ��߳����⣬�����̰߳汾�У����ǲ��ö��̻߳��ƴ���_�ȶԺ�����
        mcInst = new MigrationContextImpl();
    }
    /**
     * ȡ{@link MigrationContext} ʵ��
     * @return mcfInst {@link MigrationContextFactory}
     */
    public static final MigrationContextFactory getInstance() {
        return mcfInst;
    }
    /**
     * ȡ{@link MigrationContext} ʵ��
     * @return mcInst {@link MigrationContext}
     */
    public MigrationContext getCtx() {
        return mcInst;
    }
}
