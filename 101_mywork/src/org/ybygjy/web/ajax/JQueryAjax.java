package org.ybygjy.web.ajax;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ����JQuery Ajax
 * <p>
 * ���Ե�ַ:http://localhost:8080/org.ybygjy.web.ajax.JQueryAjax?recType=[xml|json]
 * </p>
 * <p>
 * ������������:
 * </p>
 * <p>
 * 1���ṩajax�������ݵ����ָ�ʽ(xml/json)
 * </p>
 * <p>
 * 2���ṩ������CRUDģ�������֧��ajax��������
 * </p>
 * @author WangYanCheng
 * @version 2011-7-11
 */
public class JQueryAjax extends HttpServlet {
    /** serialUID */
    private static final long serialVersionUID = -5890816541793988156L;
    /** ��ɫ */
    private static String[] ROLEMGR = {"�ܽ���", "�о�Ա", "��Ա"};
    /** ��ʼ���������� */
    private static List<JQueryAjaxPersonModel> objArray = new ArrayList<JQueryAjaxPersonModel>();
    /** XML��ʽ */
    private String xmlTmpl = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><users count=\"@C\">@U</users>";
    /** JSON��ʽ */
    private String jsonTmpl = "{\"count\":\"@C\",\"users\":[@U]}";
    
    @Override
    public void init() throws ServletException {
        initTestData();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
        IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
        IOException {
        String act = req.getParameter("act");
        int startRow = 0;
        int endRow = 0;
        if ("qryAll".equals(act)) {
            startRow = Integer.parseInt(req.getParameter("startRow"));
            endRow = Integer.parseInt(req.getParameter("endRow"));
            sendData(req, resp, buildTestData(startRow, endRow));
        } else if ("add".equals(act)) {
            responseData(req, resp, doAddUser(req));
        } else if ("remove".equals(act)) {
        } else if ("modify".equals(act)) {
        }
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
        IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        super.service(req, resp);
    }

    /**
     * ��������
     * @param req ����ʵ��
     * @param resp ��Ӧʵ��
     * @throws ServletException ServletException
     * @throws IOException IOException
     */
    private void sendData(HttpServletRequest req, HttpServletResponse resp,List<JQueryAjaxPersonModel> objArray) throws ServletException,
        IOException {
        String recType = req.getParameter("recType");
        byte[] strBuff = null;
        if ("xml".equals(recType)) {
            // text/xml��application/xml
            resp.setContentType("text/xml");
            strBuff = generalXML(objArray).getBytes("UTF-8");
        } else if ("json".equals(recType)) {
            resp.setContentType("text/json");
            strBuff = generalJSON(objArray).getBytes("UTF-8");
        }
        resp.setCharacterEncoding("UTF-8");
        resp.setContentLength(strBuff.length);
        resp.getOutputStream().write(strBuff);
    }
    /**
     * ������Ӧ
     * @param req ������
     * @param resp ��Ӧ��
     * @param dataContent ��Ӧ��������
     * @throws ServletException ServletException
     * @throws IOException IOException
     */
    private void responseData(HttpServletRequest req, HttpServletResponse resp, String dataContent) throws ServletException, IOException {
        String recType = req.getParameter("recType");
        byte[] strBuff = null;
        if ("xml".equals(recType)) {
            // text/xml��application/xml
            resp.setContentType("text/xml");
            strBuff = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><response><code>@C</code></response>".replace("@C", dataContent).getBytes("UTF-8");
        } else if ("json".equals(recType)) {
            resp.setContentType("text/json");
            strBuff = ("{\"code\":\"" + dataContent + "\"}").getBytes("UTF-8");
        }
        resp.setCharacterEncoding("UTF-8");
        resp.setContentLength(strBuff.length);
        resp.getOutputStream().write(strBuff);
    }
    /**
     * ����XML��ʽ��Ϣ
     * @return xml��
     */
    private String generalXML(List<JQueryAjaxPersonModel> objArray) {
        String rtnStr = xmlTmpl.replace("@C", String.valueOf(objArray.size()));
        StringBuilder sbud = new StringBuilder();
        for (JQueryAjaxPersonModel japm : objArray) {
            sbud.append(japm.generalXML());
        }
        return rtnStr.replace("@U", sbud.toString());
    }

    /**
     * ����JSON��ʽ��Ϣ
     * @return json��
     */
    private String generalJSON(List<JQueryAjaxPersonModel> objArray) {
        String rtnStr = jsonTmpl.replace("@C", String.valueOf(objArray.size()));
        StringBuilder sbud = new StringBuilder();
        for (JQueryAjaxPersonModel japm : objArray) {
            sbud.append(japm.generalJSON()).append(",");
        }
        return rtnStr.replace("@U", sbud.substring(0, sbud.length() - 1));
    }
    /**
     * ������������
     * @param start ��ʼ�±�
     * @param end �����±�
     * @return ʵ�����ݼ�
     */
    private List<JQueryAjaxPersonModel> buildTestData(int start, int end) {
        List<JQueryAjaxPersonModel> rtnList = new ArrayList<JQueryAjaxPersonModel>();
        int totalSize = objArray.size();
        if (start < end && (end - start) <= totalSize) {
            for (;start < end; start++) {
                rtnList.add(objArray.get(start));
            }
        }
        return rtnList;
    }
    /**
     * ���
     * @param req ���ݶ���
     * @return rtnValue pkCode
     */
    private String doAddUser(HttpServletRequest req) {
        String rtnStr = (String.valueOf((int)Math.random() * 100));
        String userRole = req.getParameter("role");
        objArray.add(0, new JQueryAjaxPersonModel(rtnStr, req.getParameter("name"), req.getParameter("age"), userRole));
        return rtnStr;
    }
    /**
     * ��ʼ����������
     */
    private void initTestData() {
        for (int start = 1, end = 101;start <= end; start ++) {
            objArray.add(new JQueryAjaxPersonModel(String.valueOf(start), String.valueOf(start), String.valueOf(start), ROLEMGR[((int)(Math.random()*(ROLEMGR.length)))]));
        }
    }
}

/**
 * ��Աʵ��
 * @author WangYanCheng
 * @version 2011-7-20
 */
class JQueryAjaxPersonModel {
    /** ���� */
    private String code;
    /** ���� */
    private String name;
    /** ���� */
    private String age;
    /** ��ɫ */
    private String role;
    /** xml��ʽ */
    private String xmlTmpl = "<user><code>@C</code><name>@N</name><age>@A</age><role>@R</role></user>";
    /** json��ʽ */
    private String jsonTmpl = "{\"code\":\"@C\",\"name\":\"@N\",\"age\":\"@A\",\"role\":\"@R\"}";

    /**
     * ��Աʵ��
     * @param code ����
     * @param name ����
     * @param age ����
     * @param role ��ɫ
     */
    public JQueryAjaxPersonModel(String code, String name, String age, String role) {
        this.code = code;
        this.name = name;
        this.age = age;
        this.role = role;
    }

    /**
     * ����XML��ʽ��Ϣ
     */
    public String generalXML() {
        return xmlTmpl.replaceAll("@C", this.code).replaceAll("@N", this.name).replaceAll("@A", this.age)
            .replaceAll("@R", this.role);
    }

    /**
     * ����JSON��ʽ��Ϣ
     * @return JSON��ʽ
     */
    public String generalJSON() {
        return jsonTmpl.replaceAll("@C", this.code).replaceAll("@N", this.name).replaceAll("@A", this.age)
            .replaceAll("@R", this.role);
    }
}
