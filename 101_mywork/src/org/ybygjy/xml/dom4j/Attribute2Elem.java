package org.ybygjy.xml.dom4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/**
 * �������[���ϲ�ѯ.xml]Ԫ������ת��Ϊelement
 * @author WangYanCheng
 * @version 2010-11-18
 */
public class Attribute2Elem {
    /** docInst */
    public Document doc;
    /** outDoc */
    public Document outDoc;
    /** rootElem */
    public Element rootElem;
    /** saveXML */
    public File saveXml;

    /**
     * Constructor
     * @param fileInst fileInst
     * @param saveXML saveXML
     * @throws DocumentException DocumentException
     */
    public Attribute2Elem(File fileInst, File saveXML) throws DocumentException {
        this.saveXml = saveXML;
        doc = parse(fileInst);
        outDoc = createDom();
    }

    /**
     * ��������xmlģʽ���ļ�
     * @param fileInst xmlģʽ���ļ�
     * @return docInst docInst
     * @throws DocumentException DocumentException
     */
    private Document parse(File fileInst) throws DocumentException {
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
     * �����ĵ�ʵ��
     * @return domInst domInst
     */
    public Document createDom() {
        Document docInst = DocumentHelper.createDocument();
        this.rootElem = docInst.addElement("rootElem");
        return docInst;
    }

    /**
     * �־ô洢xml�ĵ�
     */
    public void saveDocument() {
        try {
            XMLWriter xmlWriteInst = new XMLWriter(new FileOutputStream(saveXml));
            xmlWriteInst.write(outDoc);
            xmlWriteInst.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ת��
     */
    public void doConvert() {
        Element elem = doc.getRootElement();
        Iterator<Element> iterator = elem.elementIterator();
        innerConvert(iterator);
        doGetColumn();
        doGetColumn4Path();
        saveDocument();
    }

    /**
     * innerConvert
     * @param iterator iterator
     */
    private void innerConvert(Iterator<Element> iterator) {
        for (; iterator.hasNext();) {
            Element tmpElem = iterator.next();
            Iterator attIterator = tmpElem.attributeIterator();
            createElement(tmpElem.getName(), attIterator);
        }
    }
    /**
     * ��ȡ��
     */
    private void doGetColumn() {
        List<Element> elemArr = rootElem.elements("ROW");
        Element elem = elemArr.get(0);
        List<Element> childElemArr = elem.elements();
        for (Iterator<Element> iterator = childElemArr.iterator(); iterator.hasNext();) {
            Element tmpElem = iterator.next();
            System.out.println(tmpElem.getName() + "\t\t\t" + tmpElem.getTextTrim());
        }
    }
    /**
     * ��ȡ�ڵ㣬ʹ��XPATH��ʽ
     */
    private void doGetColumn4Path() {
        Element element = (Element) rootElem.selectSingleNode("ROW");
        Element tmpElem = (Element) element.selectSingleNode("���ϱ��");
System.out.println(tmpElem.getName() + ":" + tmpElem.getStringValue());
    }
    /**
     * createElement
     * @param tmpElem elementName
     * @param attIterator attributeIterator
     */
    private void createElement(String tmpElem, Iterator attIterator) {
        Element elem = DocumentHelper.createElement(tmpElem);
        for (; attIterator.hasNext();) {
            Attribute tmpAtt = (Attribute) attIterator.next();
            elem.addElement(tmpAtt.getName()).add(DocumentHelper.createCDATA(tmpAtt.getValue()));
        }
        rootElem.add(elem);
    }
    /**
     * �������
     * @param args �����б�
     */
    public static void main(String[] args) {
        File outFile = new File("src\\org\\ybygjy\\xml\\������Ϣ.xml");
        File inFile = new File("src\\org\\ybygjy\\xml\\���ϲ�ѯ.xml");
        try {
            Attribute2Elem att2Elem = new Attribute2Elem(inFile, outFile);
            att2Elem.doConvert();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
}
