package org.ybygjy.ui2.demo2;

/**
 * ����Bean
 * @author WangYanCheng
 * @version 2012-11-1
 */
public class FibBean {
    /**�������Key*/
    private String inputKey;
    /**�������Key*/
    private String outputKey;
    /**Fibonacci��*/
    private long fibTerm;
    /**Fibonacciֵ*/
    private long fibValue;
    /**
     * @return the inputKey
     */
    public String getInputKey() {
        return inputKey;
    }
    /**
     * @param inputKey the inputKey to set
     */
    public void setInputKey(String inputKey) {
        this.inputKey = inputKey;
    }
    /**
     * @return the outputKey
     */
    public String getOutputKey() {
        return outputKey;
    }
    /**
     * @param outputKey the outputKey to set
     */
    public void setOutputKey(String outputKey) {
        this.outputKey = outputKey;
    }
    /**
     * @return the fibTerm
     */
    public long getFibTerm() {
        return fibTerm;
    }
    /**
     * @param fibTerm the fibTerm to set
     */
    public void setFibTerm(long fibTerm) {
        this.fibTerm = fibTerm;
    }
    /**
     * @return the fibValue
     */
    public long getFibValue() {
        return fibValue;
    }
    /**
     * @param fibValue the fibValue to set
     */
    public void setFibValue(long fibValue) {
        this.fibValue = fibValue;
    }
}
