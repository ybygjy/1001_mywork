package org.ybygjy.exp;

import org.ybygjy.ExpInterface;
import org.ybygjy.TestEnv;

public abstract class AbstractExpInterface implements ExpInterface {
    /**��Դ������*/
    protected TestEnv testEnvInst;
    /**
     * Constructor
     * @param testEnvInst {@link TestEnv}
     */
    public AbstractExpInterface(TestEnv testEnvInst) {
        this.testEnvInst = testEnvInst;
    }
}
