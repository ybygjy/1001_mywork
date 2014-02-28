package org.ybygjy.pattern.proxy.rmistate.server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import org.ybygjy.pattern.proxy.rmistate.GumballMachine;
import org.ybygjy.pattern.proxy.rmistate.IGumballMachine;

/**
 * Server<br>
 * <p>
 * 1��Java�����ṩ��һ���������Ե�RMIע����ƣ��������뵥�����ԣ��޷�������������ͨ���ԡ�
 * </p>
 * 2����ʹ��Java�ṩ��rmiregistry�ն�ģ����ʵ����<br>
 * <ol>
 * <li>����remote�ӿ�����</li>
 * <li>�����̳�UnicastRemoteObject���Ҹ���ʵ��ҵ��ӿڵľ���ʵ���� </li>
 * <li>ʹ��rmic&nbsp;MyRemoteImpl ����Stub</li>
 * </ol>
 * 3����дClient��ע��
 * <ol>
 * <li>����RemoteInterface.class</li>
 * <li>����RemoteImpl_Stub.class</li>
 * <li>��������RemoteImpl.class</li>
 * </ol>
 * 4������
 * <ol>
 * <li>�����classesĿ¼��ִ��rmiregistry����</li>
 * <li>ִ��Server�˰�</li>
 * <li>ִ��Զ��Client�˲���</li>
 * </ol>
 * @author WangYanCheng
 * @version 2010-12-24
 */
public class Server {
    /***/
    public static String rmiURL = "Gumball";

    /**
     * �������
     * @param args args
     * @throws RemoteException RemoteException
     */
    public static void main(String[] args) throws RemoteException {
        String url = "//" + args[0] + "/Gumball";
        IGumballMachine gumball = new GumballMachine(url, Integer.parseInt(args[1]));
        try {
            Naming.rebind(url, gumball);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        // innerRebind("Gumball", gumball);
    }

    /**
     * rebind
     * @param url url
     * @param obj obj
     */
    static void innerRebind(String url, Remote obj) {
        try {
            createRegistry(java.rmi.registry.Registry.REGISTRY_PORT);
            Naming.rebind(url, obj);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * doCreateRegistry
     * @param port port
     */
    static void createRegistry(int port) {
        try {
            LocateRegistry.createRegistry(port);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
