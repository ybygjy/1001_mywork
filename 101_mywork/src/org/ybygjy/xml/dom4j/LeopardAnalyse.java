package org.ybygjy.xml.dom4j;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/**
 * dom4j ѧϰ
 * @author WangYanCheng
 * @version 2010-8-3
 */
public class LeopardAnalyse {
    /**
     * �������
     * @param args �����б�
     */
    public static void main(String[] args) {
        try {
            Dom4JBasic dom4JInst =
                new Dom4JBasic(new File("D:\\work\\workspace\\mywork\\src\\org\\ybygjy\\xml\\leopard.xml"));
            dom4JInst.setAopImpInst(new AOPListener() {
                public void beforeParseElement(Element elemInst) {
                }
                public void afterParseElement(Element elemInst) {
                    if (elemInst.isRootElement()) {
                        return;
                    }
                    Iterator iterator = elemInst.nodeIterator();
                    for (; iterator.hasNext();) {
                        Node nodeInst = (Node) iterator.next();
                        System.out.println(nodeInst.asXML());
                    }
                }
            });
            dom4JInst.parseXml4Iterator();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
}

/**
 * ѧϰdom4j����
 * @author WangYanCheng
 * @version 2010-8-3
 */
class Dom4JBasic {
    /**�����¼�AOP*/
    private AOPListener aopImpInst = null;
    /**inner document instance*/
    private Document docInst = null;
    /**
     * Constructor
     */
    public Dom4JBasic() {
    }
    /**
     * Constructor
     * @param fileInst fileInst
     * @throws DocumentException DocumentException
     */
    public Dom4JBasic(File fileInst) throws DocumentException {
        docInst = parse(fileInst);
    }
    /**
     * Constructor
     * @param xmlStr ����xml��ʽ���ַ���
     * @throws DocumentException DocumentException
     */
    public Dom4JBasic(String xmlStr) throws DocumentException {
        docInst = DocumentHelper.parseText(xmlStr);
    }
    /**
     * ��������xmlģʽ���ļ�
     * @param fileInst xmlģʽ���ļ�
     * @return docInst docInst
     * @throws DocumentException DocumentException
     */
    public Document parse(File fileInst) throws DocumentException {
        Document docInst = null;
        SAXReader saxRInst = new SAXReader();
        saxRInst.setIgnoreComments(true);
        saxRInst.setMergeAdjacentText(true);
        saxRInst.setStringInternEnabled(false);
        saxRInst.setStripWhitespaceText(true);
        docInst = saxRInst.read(fileInst);
        return docInst;
    }
    /**
     * ��������xml�ļ�����
     * @param docInst �ļ�ʵ��
     */
    public void parseXml4Iterator(Document docInst) {
        boolean aopListFlag = null == this.aopImpInst ? false : true;
        StringBuilder sbuStr = new StringBuilder();
        Element rootElemInst = docInst.getRootElement();
        if (aopListFlag) {
            this.aopImpInst.beforeParseElement(rootElemInst);
        }
        for (Iterator iterator = rootElemInst.elementIterator(); iterator.hasNext();) {
            Element tmpElem = (Element) iterator.next();
            if (aopListFlag) {
                this.aopImpInst.beforeParseElement(tmpElem);
            }
            sbuStr
            .append("getName:" + tmpElem.getName())
            .append("\tQName:" + tmpElem.getQName().getName())
            .append("\tattributeCount:" + tmpElem.attributeCount())
            .append("\tnodeCount:" + tmpElem.nodeCount())
            .append("\n");
            if (aopListFlag) {
                this.aopImpInst.afterParseElement(tmpElem);
            }
        }
        System.out.println(sbuStr);
    }
    /**
     * ����xml�ļ�ʵ������
     */
    public void parseXml4Iterator() {
        if (null != this.docInst) {
            this.parseXml4Iterator(docInst);
        }
    }
    /**
     * ��������xml�ļ�����,ʹ��XPath
     * @param docInst xml�ļ�ʵ��
     */
    public void parseXml4Xpath(Document docInst) {
        StringBuilder sbuStr = new StringBuilder();
        List<Node> listElem = docInst.selectNodes("//leopard/config-item");
        for (Node nodeInst : listElem) {
            Node itemName = nodeInst.selectSingleNode("item-name");
            sbuStr
            .append("getName:" + itemName.getName())
            .append("getText:" + itemName.getText())
            .append("\n");
        }
        System.out.println(sbuStr);
    }
    /**
     * ��������xml�ļ�����,ʹ��XPath
     */
    public void parseXml4Xpath() {
        if (null != this.docInst) {
            this.parseXml4Xpath(this.docInst);
        }
    }
    /**
     * �����ĵ�ʵ��
     * @return docInst docInst
     */
    public Document createDocument() {
        Document docInst = DocumentHelper.createDocument();
        Element rootElem = docInst.addElement("leopard");
        Element itemElem = rootElem.addElement("conf-item");
        itemElem.addElement("item-name").addText("nTimeOut");
        itemElem.addElement("item-value").addText("15");
        itemElem = rootElem.addElement("conf-item");
        itemElem.addElement("item-name").addText("nRetry");
        itemElem.addElement("item-value").addText("5");
        return docInst;
    }
    /**
     * �־ô洢xml�ĵ�
     * @param docInst xml�ĵ�
     */
    public void saveDocument(Document docInst) {
        try {
            XMLWriter xmlWriteInst = new XMLWriter(new FileWriter("Dom4jFormatSave.xml"));
            xmlWriteInst.write(docInst);
            xmlWriteInst.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * prettyPrint
     * @param docInst �ĵ�ʵ��
     */
    public void prettyPrint(Document docInst) {
        OutputFormat formatInst = OutputFormat.createPrettyPrint();
        XMLWriter xmlWInst;
        try {
            xmlWInst = new XMLWriter(System.out, formatInst);
            xmlWInst.write(docInst);
            xmlWInst.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    /**
     * compactPrint
     * @param docInst �ĵ�ʵ��
     */
    public void compactPrint(Document docInst) {
        OutputFormat formatInst = OutputFormat.createCompactFormat();
        XMLWriter xmlWInst;
        try {
            xmlWInst = new XMLWriter(System.out, formatInst);
            xmlWInst.write(docInst);
            xmlWInst.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    /**
     * ���ٳ־ô洢xml�ĵ�
     * @param docInst xml�ĵ�ʵ��
     */
    public void quickSaveDocument(Document docInst) {
        FileWriter fwInst;
        try {
            fwInst = new FileWriter("Dom4jQuickSave.xml");
            docInst.write(fwInst);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Converting to String
     * @return rtnStr xmlStr
     */
    public String convertDomInst2Str() {
        return this.docInst.asXML();
    }
    /**
     * װ��aop����ʵ��ʵ��
     * @param aopImpInst aopʵ��ʵ��
     */
    public void setAopImpInst(AOPListener aopImpInst) {
        this.aopImpInst = aopImpInst;
    }
}
