package org.ybygjy.util.file.convert;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
/**
 *@author Hj, Date: 2011-5-17
 * Email: Hj-545@qq.com
 */
public class Console {
    
    private FrmMain frm;
    
    private boolean flag = true;
    private String sourceEncoding = "gbk";
    private String targetEncoding = "utf-8";
    
    private String type;
    
    private String filepath;
    private File selectFile;
    
    private String saveFilepath;
    private File selectSaveDir;
    
    public Console(){}
    public void exit() {
        int choose = 
            JOptionPane.showConfirmDialog(
                null, 
                "��ȷ��Ҫ�˳���",
                "ȷ���˳�", 
                JOptionPane.YES_NO_OPTION);
        if (choose == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
    /** ѡ���ļ����ļ���  */
    public void selectFile() {
        JFileChooser jf = new JFileChooser();
        jf.setDialogTitle("ѡ���ļ����ļ���");
        jf.setFileSelectionMode(
                JFileChooser.FILES_AND_DIRECTORIES);
//      FileNameExtensionFilter filter = 
//              new FileNameExtensionFilter(
//              "java&php&txt&jsp&sql", 
//              "java", "php", "txt", "sql", "jsp");
//      jf.setFileFilter(filter);
        int result = jf.showOpenDialog(frm);
        jf.setVisible(true);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectFile = jf.getSelectedFile();
            todoChooser(selectFile);
        }
    }
    
    /** ѡ�񱣴�·��  */
    public void selectDir() {
        JFileChooser jf = new JFileChooser();
        jf.setDialogTitle("ѡ�񱣴��ļ���");
        jf.setFileSelectionMode(
                JFileChooser.DIRECTORIES_ONLY);
        int result = jf.showOpenDialog(frm);
        jf.setVisible(true);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectSaveDir = jf.getSelectedFile();
            todoChooserDir(selectSaveDir);
        }
    }
    
    /** ѡ�񱣴��ļ��к���������  */
    private void todoChooserDir(File selectSaveDir) {
        if (selectSaveDir.exists()) {
            saveFilepath = 
                selectSaveDir.getAbsolutePath();
            frm.txt_dir.setText(saveFilepath);
            frm.txt_show.append("\n\r");
            frm.txt_show.append(
                        "����·��: " + saveFilepath);
        } else {
            this.selectSaveDir = null;
            JOptionPane.showMessageDialog(
                    null, 
                    "�ļ��в�����!");
        }
    }
    
    /**  ѡ���ļ����ļ��к��������� */
    private void todoChooser(File selectFile) {
        if (selectFile.exists()) {
            filepath = 
                selectFile.getAbsolutePath();
            frm.txt_file.setText(filepath);
            frm.txt_show.append("\n\r");
            
            if (selectFile.isFile()) {
                flag = true;
                frm.txt_show.append(
                        "�ļ�: " + filepath);
            } else {
                flag = false;
                frm.txt_show.append(
                        "Ŀ¼: " + filepath);
            }
        } else {
            this.selectFile = null;
            JOptionPane.showMessageDialog(
                    null, 
                    "�ļ����ļ��в�����!");
        }
    }
    public void setFrm(FrmMain frm) {
        this.frm = frm;
    }
    public FrmMain getFrm() {
        return frm;
    }
    
    /** ��ʾ����  */
    public void show() {
        frm.setVisible(true);
    }
    
    /** ����Դ����  */
    public void sourceEncodingChoose() {
        sourceEncoding =    (String) 
            frm.jbx_source.getSelectedItem();
//      System.out.println(sourceEncoding);
    }
    
    /** ����Ŀ�����  */
    public void targetEncodingChoose() {
        targetEncoding = (String) 
            frm.jbx_target.getSelectedItem();
//      System.out.println(targetEncoding);
    }
    /** ִ�а�ťת���ķ���  */
    public void change() {
        if(selectFile == null){
            JOptionPane.showMessageDialog(
                    null, 
                    "��ѡ���ļ����ļ���!");
        }
        
        String sourcePath = frm.txt_file.getText();
        String targetPath = sourcePath + ".bak";
        
        if(flag){
            todoChange(sourcePath, targetPath);
        }else{
            todoAllChage();
        }
    }   
    
    /** �ļ���ת������  */
    private void todoAllChage() {
        if((type = frm.txt_type.getText()) == null){
            JOptionPane.showMessageDialog(
                    frm,
            "��ѡ������ļ���,��ָ���ļ�����,��:.java");
            return;
        }
        List<File> list = 
            IOCVUtils.listAll(filepath, type);
        if(list.size() == 0){
            JOptionPane.showMessageDialog(frm,
            "���ļ�������û������Ҫ���ļ�!");
        }else{
            frm.txt_show.append("\n\r");
            frm.txt_show.append("��ѡ�е��ļ��а���" + list.size() + "���ļ�:");
            for(File ls:list){
                frm.txt_show.append("\n\r");
                frm.txt_show.append(ls.getAbsolutePath());
            }
            int choose = JOptionPane.showConfirmDialog(frm, 
                    "��ȷ����Ҫת����Щ�ļ���?",
                    "Message", JOptionPane.YES_NO_OPTION);
            if (choose == JOptionPane.YES_OPTION) {
                frm.txt_show.append("\n\r");
                frm.txt_show.append("ִ������ת��.....");
                IOCVUtils.sourceEncoding = sourceEncoding;
                IOCVUtils.targetEncoding = targetEncoding;
                for(File ls:list){
                    String sourcePath = ls.getAbsolutePath();
                    String targetPath = sourcePath + ".bak";
                    todoChange(sourcePath, targetPath);
                }
            }else{
                return;
            }
        }
        
    }
    
    /** �ļ�ת������  */
    private void todoChange(String sourcePath, String targetPath) {
        IOCVUtils.rename(sourcePath, targetPath);
        try {
            IOCVUtils.changeEncoding(targetPath, 
                    sourcePath, 
                    sourceEncoding, 
                    targetEncoding);
            frm.txt_show.append("\n\r");
            frm.txt_show.append("�ļ�ת���ɹ�: " + sourcePath);
            frm.txt_show.append("\n\r");
            frm.txt_show.append(targetPath + "ΪԴ�ļ�����!");
        } catch (IOException e) {
//          e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }   
}