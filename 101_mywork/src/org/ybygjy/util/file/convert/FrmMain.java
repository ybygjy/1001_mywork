package org.ybygjy.util.file.convert;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
public class FrmMain extends JFrame implements ActionListener {
    private static final long serialVersionUID = 8906317933020631699L;
    /** ��ť ת�� */
    JButton btn_change;
    /** ��ť �˳� */
    JButton btn_exit;
    /** ��ǩ �ļ����ļ��� */
    JLabel lab_file;
    /** �����ı� �ļ�·�� */
    JTextField txt_file;
    /** ��ť "." ѡ���ļ����ļ��� */
    JButton btn_selectFile;
    
    /** ��ǩ �����ļ��� */
    JLabel lab_dir;
    /** �����ı� ����·�� */
    JTextField txt_dir;
    /** ��ť "." ѡ�񱣴��ļ��� */
    JButton btn_selectDir;
    
    /** ����� ת����Ϣ */
    JTextArea txt_show;
    
    /** ��ǩ ѡ�����: */
    JLabel lab_encoding;
    /** �����б� Դ���� */
    JComboBox jbx_source;
    /** �����б� Ŀ����� */
    JComboBox jbx_target;
    /** ��ǩ �ļ�����: */
    JLabel lab_type;
    /** �����ı� ��������,��".java" */
    JTextField txt_type;
    /** ����汾 */
    private String version = "V1.50base";
    /** ������� */
    private String title = 
        "JAVA�ļ�����ת������" + version;
    
    /** �����б�ֵ:Ĭ�ϱ��� */
    private String[] encoding =
        "gbk,utf-8,gb2312,utf-16".split(",");
    
    private Console console;
    
    public FrmMain(){
        init();
    };
    public FrmMain(Console console) {
        this.setConsole(console);
        init();
    }
    private void init() {
        this.setTitle(title);
        this.setContentPane(createContent());
        this.setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE);
        this.setSize(600, 500);
        this.setLocationRelativeTo(null);
//      this.setVisible(true);
    }
    /** ����� */
    private Container createContent() {
        JPanel pane = new JPanel(new BorderLayout());
        pane.setBorder(new EmptyBorder(8, 8, 8, 8));
        pane.add(BorderLayout.NORTH, createSelect());
        pane.add(BorderLayout.CENTER, createShowPane());
        pane.add(BorderLayout.SOUTH, createBtnPane());
        return pane;
    }
    
    /** ѡ�����  */
    private JPanel createSelect(){
        JPanel pane = new JPanel(new BorderLayout());
//      pane.setBorder(new EmptyBorder(8, 0, 8, 0));
        pane.add(BorderLayout.NORTH, 
                        createSelectFile());
        pane.add(BorderLayout.CENTER, 
                        createSelectSaveDir());
        pane.add(BorderLayout.SOUTH, 
                        createSelectEncoding());
        return pane;
    }
    
    /** ѡ���ļ���� */
    private JPanel createSelectFile() {
        JPanel pane = new JPanel(new BorderLayout());
        lab_file = new JLabel("�ļ����ļ���: ");
        txt_file = new JTextField();
        txt_file.setEditable(false);
        btn_selectFile = new JButton(".");
        btn_selectFile.addActionListener(this);
        pane.add(BorderLayout.WEST, lab_file);
        pane.add(BorderLayout.CENTER, txt_file);
        pane.add(BorderLayout.EAST, btn_selectFile);
        return pane;
    }
    
    /** ѡ�񱣴�·�� */
    private JPanel createSelectSaveDir() {
        JPanel pane = new JPanel(new BorderLayout());
        pane.setBorder(new EmptyBorder(8, 0, 0, 0));
        lab_dir = new JLabel("�����ļ���:");
        txt_dir = new JTextField();
        txt_dir.setEditable(false);
        btn_selectDir = new JButton(".");
        btn_selectDir.addActionListener(this);
        pane.add(BorderLayout.WEST, lab_dir);
        pane.add(BorderLayout.CENTER, txt_dir);
        pane.add(BorderLayout.EAST, btn_selectDir);
        return pane;
    }
    /** ����,������� */
    private JPanel createSelectEncoding() {
        JPanel pane = new JPanel(
                new FlowLayout(FlowLayout.LEFT));
        pane.setBorder(new EmptyBorder(8, 0, 0, 0));
        lab_encoding = new JLabel("ѡ�����:");
        jbx_source = new JComboBox(encoding);
        jbx_source.setSelectedIndex(0);// Ĭ����ʾ��0������ֵ
        jbx_source.addActionListener(this);
        jbx_target = new JComboBox(encoding);
        jbx_target.setSelectedIndex(1);
        jbx_target.addActionListener(this);
        lab_type = new JLabel("�ļ�����:");
        txt_type = new JTextField();
        txt_type.setColumns(10);// �����г���,������ʾ������
        txt_type.setText(".java");
        pane.add(lab_encoding);
        pane.add(jbx_source);
        pane.add(jbx_target);
        pane.add(lab_type);
        pane.add(txt_type);
        return pane;
    }
    /** �����Ϣ��� */
    private JScrollPane createShowPane() {
        JScrollPane pane = new JScrollPane();
        pane.setBorder(new TitledBorder("��Ϣ"));
        txt_show = new JTextArea(10, 10);
        txt_show.setEnabled(false);
        txt_show.setLineWrap(true);// �����Զ�����
        txt_show.setFont(new Font("����", Font.BOLD, 13));
        txt_show.setDisabledTextColor(Color.red);
        pane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        pane.getViewport().add(txt_show);
        return pane;
    }
    /** ת���˳���ť��� */
    private Component createBtnPane() {
        JPanel pane = new JPanel();
        btn_change = new JButton("ת��");
        btn_change.addActionListener(this);
        btn_exit = new JButton("�˳�");
        btn_exit.addActionListener(this);
        pane.add(btn_change);
        pane.add(btn_exit);
        this.getRootPane().
            setDefaultButton(btn_change);
        return pane;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object temp = e.getSource();
        
        if(temp == btn_change){
            console.change();
        }else if(temp == btn_selectFile){
            console.selectFile();
        }else if(temp == jbx_source){
            console.sourceEncodingChoose();
        }else if(temp == jbx_target){
            console.targetEncodingChoose();
        }else if(temp == btn_selectDir){
            console.selectDir();
        }else if(temp == btn_exit){
            console.exit();
        }
    }
    public void setConsole(Console console) {
        this.console = console;
    }
    public Console getConsole() {
        return console;
    }
}