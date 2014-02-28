package org.ybygjy.ibatis;

import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.log4j.PropertyConfigurator;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;
import com.ibatis.sqlmap.client.event.RowHandler;

/**
 * �������
 * @author WangYanCheng
 * @version 2010-11-26
 */
public class TestUser {
    /** sqlMap */
    private SqlMapClient sqlMap = null;

    /**
     * Constructor
     */
    public TestUser() {
        doInitLogSys();
        Reader reader = null;
        try {
            reader = Resources.getResourceAsReader("org/ybygjy/ibatis/SqlMapConfig.xml");
            sqlMap = SqlMapClientBuilder.buildSqlMapClient(reader);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
    }

    /**
     * doPrint
     * @param instList instList
     */
    static void print(List instList) {
        for (Iterator iterator = instList.iterator(); iterator.hasNext();) {
            System.out.println(iterator.next());
        }
    }

    /**
     * ��ʼLog
     */
    protected void doInitLogSys() {
        URL url = TestUser.class.getResource("log4j.properties");
        PropertyConfigurator.configure(url);
    }

    /**
     * queryAllUser��ע�����ֲ�ѯ��ʽ��
     * <li>ֱ�ӵ���List</li>
     * <li>ʹ���м�������</li>
     * @return rtnList rtnList
     * @throws SQLException SQLException
     */
    private List<User> queryAllUser() throws SQLException {
        final List<User> rtnList = new ArrayList<User>();
//            sqlMap.queryForList("getUser");
        User paramUser = new User();
        paramUser.setUserId(37);
        sqlMap.queryWithRowHandler("getUser", paramUser, new RowHandler() {
            public void handleRow(Object valueObject) {
                rtnList.add((User) valueObject);
            }
        });
        return rtnList;
    }

    /**
     * queryUser
     * @param id id
     * @return rtnUser {@link User}
     * @throws SQLException SQLException
     */
    public User queryUser(int id) throws SQLException {
        User user = new User();
        user.setUserId(id);
        user = (User) sqlMap.queryForObject("getUserById", user);
        return user;
    }

    /**
     * startTrans
     * @throws SQLException SQLException
     */
    public void doBeginTrans() throws SQLException {
        sqlMap.startTransaction();
    }

    /**
     * endTrans
     * @throws SQLException SQLException
     */
    public void doEndTrans() throws SQLException {
        sqlMap.commitTransaction();
        sqlMap.endTransaction();
    }

    /**
     * updateUserName
     * @param user {@link User}
     * @throws SQLException SQLException
     */
    public void updateUserName(User user) throws SQLException {
        int rtnId = sqlMap.update("updateUserName", user);
        System.out.println(rtnId);
    }
    /**
     * updateUserNo
     * @param user {@link User}
     * @throws SQLException SQLException
     */
    public void updateUserNo(User user) throws SQLException {
        sqlMap.update("updateUserNo", user);
    }
    /**
     * �������
     * @param args args
     * @throws SQLException SQLException
     */
    public static void main(String[] args) throws SQLException {
        String[] userNames = {"����", "����", "ũ�", "����", "̶��", "����", "����", "Ƶ��", "����", "����", "��ѧ", "����", "����",
                "����", "����", "����"};
        Random rand = new Random();
        TestUser tuInst = new TestUser();
        List<User> userList = tuInst.queryAllUser();
        tuInst.doBeginTrans();
        for (Iterator<User> iterator = userList.iterator(); iterator.hasNext();) {
            User user = iterator.next();
            System.out.println(user.getLoanLoges().size() + "\t" + user.getCompanyName());
//            user.setUserName(userNames[rand.nextInt(userNames.length)]);
//            user.setUserNo(String.valueOf(((int) (Math.random() * 9000 + 1000))));
//            tuInst.updateUserName(user);
//            tuInst.updateUserNo(user);
        }
        tuInst.doEndTrans();
    }
}
