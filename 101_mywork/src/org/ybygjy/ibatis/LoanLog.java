package org.ybygjy.ibatis;

/**
 * POJO �����¼
 * @author WangYanCheng
 * @version 2010-11-27
 */
public class LoanLog {
    /** logId */
    private Integer logId;
    /** bookId */
    private Integer bookId;
    /** ���ʱ�� */
    private String loanTime;
    /** ����ʱ�� */
    private String returnTime;
    /** ������ */
    private int readerId;

    /**
     * @return the logId
     */
    public Integer getLogId() {
        return logId;
    }

    /**
     * @param logId the logId to set
     */
    public void setLogId(Integer logId) {
        this.logId = logId;
    }

    /**
     * @return the bookId
     */
    public Integer getBookId() {
        return bookId;
    }

    /**
     * @param bookId the bookId to set
     */
    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    /**
     * @return the loanTime
     */
    public String getLoanTime() {
        return loanTime;
    }

    /**
     * @param loanTime the loanTime to set
     */
    public void setLoanTime(String loanTime) {
        this.loanTime = loanTime;
    }

    /**
     * @return the returnTime
     */
    public String getReturnTime() {
        return returnTime;
    }

    /**
     * @param returnTime the returnTime to set
     */
    public void setReturnTime(String returnTime) {
        this.returnTime = returnTime;
    }

    /**
     * @return the readerId
     */
    public int getReaderId() {
        return readerId;
    }

    /**
     * @param readerId the readerId to set
     */
    public void setReaderId(int readerId) {
        this.readerId = readerId;
    }
}
