package org.ybygjy.basic.thinking.thread.testframework;

import org.ybygjy.basic.thinking.thread.testframework.state.InvariantState;

/**
 * ������Թ�������
 * @author WangYanCheng
 * @version 2010-9-30
 */
public interface Invariant {
    /**
     * ���Թ���
     * @return {@link InvariantState}
     */
    InvariantState invariant();
}
