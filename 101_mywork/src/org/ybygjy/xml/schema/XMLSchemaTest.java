package org.ybygjy.xml.schema;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.SAXValidator;
import org.dom4j.io.XMLWriter;
import org.dom4j.util.XMLErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

/**
 * ʵ��XML Schema
 * <p>1��ͨ��DOM4J����XSD��֤XML
 * <p>2��http://lavasoft.blog.51cto.com/62575/97597/
 * @author WangYanCheng
 * @version 2014-5-27
 */
public class XMLSchemaTest {
    private File xmlFile = null;
    private File xsdFile = null;
    public XMLSchemaTest(File xmlFile, File xsdFile) {
        this.xmlFile = xmlFile;
        this.xsdFile = xsdFile;
    }
    public void validateByDTD() {
    }
    public void validateByXSD() {
        //XML��������
        XMLErrorHandler errorHandler = new XMLErrorHandler();
        //����SAX�Ľ�����
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        //ָ���������ڽ����ĵ�ʱ��֤�ĵ�����
        saxParserFactory.setValidating(true);
        //ָ����������XML���ƿռ�֧��
        saxParserFactory.setNamespaceAware(true);
        //ʹ�ù�������������SAXParserʵ��
        SAXParser saxParser = null;
        try {
            saxParser = saxParserFactory.newSAXParser();
        } catch (ParserConfigurationException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (SAXException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        //�����ĵ���ȡʵ��
        SAXReader saxReader = new SAXReader();
        //����xml�ĵ�ʵ��
        Document xmlDocument = null;
        try {
            xmlDocument = saxReader.read(xmlFile);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        try {
            //����XMLReader�Ļ���ʵ���е��ض�����ֵ��
            //���Ĺ��ܺ������б����http://sax.sourceforge.net/?selected=get-set���ҵ�
            saxParser.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
            System.out.println("xsdFile.toURI().toString()==>" + xsdFile.toURI().toString());
            saxParser.setProperty("http://java.sun.com/xml/jaxp/properties/schemaSource", xsdFile.toURI().toString());
        } catch (SAXNotRecognizedException e) {
            e.printStackTrace();
        } catch (SAXNotSupportedException e) {
            e.printStackTrace();
        }
        SAXValidator validator = null;
        try {
            //ʵ�����ļ����ѹ���
            validator = new SAXValidator(saxParser.getXMLReader());
            //ע���������
            validator.setErrorHandler(errorHandler);
            //У��
            validator.validate(xmlDocument);
        } catch (SAXException e) {
            e.printStackTrace();
        }
        XMLWriter xmlWriter = null;
        try {
            xmlWriter = new XMLWriter(OutputFormat.createPrettyPrint());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (errorHandler.getErrors().hasContent()) {
            System.out.println("XML�ļ�ͨ��XSD�ļ�У��ʧ�ܣ�");
            try {
                xmlWriter.write(errorHandler.getErrors());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("XML�ļ�ͨ��XSD�ļ�У��ɹ���");
        }
    }
    /**
     * �������
     * @param args �����б�
     */
    public static void main(String[] args) {
        File xmlFile = new File(XMLSchemaTest.class.getResource("schema-data-content.xml").getFile());
        File xsdFile = new File(XMLSchemaTest.class.getResource("schema-data-content.xsd").getFile());
        XMLSchemaTest xmlSchemaTest = new XMLSchemaTest(xmlFile, xsdFile);
        xmlSchemaTest.validateByXSD();
    }
}
