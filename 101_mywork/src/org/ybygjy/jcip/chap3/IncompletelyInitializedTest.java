package org.ybygjy.jcip.chap3;

/**
 * δ��ȫ��ʼ��
 * 1.���Ȳ����˼̳л���
 * 2.��������඼��getName����
 * 3.����override����getName����
 * 4.������ι��캯�����ø�����ι��캯��
 * 5.����Ҫ��һ����ǣ���ʵ���ڽ��г�ʼ�������б��ⲿ����ʵ�����ʣ������ⲿʵ������������״̬
 * @author WangYanCheng
 * @version 2014-7-22
 */
public class IncompletelyInitializedTest {
    public static void main(String[] args) {
        Leaker leaker = new Leaker();
        Node node = new NamingNode(leaker, "Hello World!");
        System.out.println(node.getName());
    }
}
/**
 * ����
 */
class Node {
	private String nodeName;
	/**
	 * ���캯��
	 * @param leaker {@link Leaker}
	 */
    public Node(Leaker leaker) {
        leaker.leak(this);
    }
    /**
     * ���캯��
     * @param nodeName nodeName
     */
    public Node(String nodeName) {
    	this.nodeName = nodeName;
    }
    /**
     * �ɱ����า�ǵķ���
     * @return nameStr
     */
    protected String getName() {
        return "BaseNode";
    }
}
/**
 * ����
 */
class NamingNode extends Node {
    private String nodeName;
    /**
     * ���캯��
     * @param leaker {@link Leaker}
     * @param nodeName nameStr
     */
    public NamingNode(Leaker leaker, String nodeName) {
        super(leaker);
        this.nodeName = nodeName;
    }
    @Override
    public String getName() {
        return nodeName;
    }
}
/**
 * ����ʵ��������{@link NamingNode}δ��ȫ��ʼ�������з���{@link NamingNode}ʵ��
 * @author WangYanCheng
 * @version 2014-07-22
 */
class Leaker {
    public void leak(Node node) {
        System.out.println("node.getName()=>" + node.getName());
    }
}