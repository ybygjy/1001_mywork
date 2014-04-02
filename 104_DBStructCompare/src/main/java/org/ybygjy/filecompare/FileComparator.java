package org.ybygjy.filecompare;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * �ļ����ݱȽ�
 * @author SunYuJia
 * @author WangYanCheng
 * @version 2011-10-20
 */
public class FileComparator {
	/** ����Ŀ¼*/
	private String[] excludeDirExp = new String[] {".SVN"/*, "TABLE", "TRIGGER", "TYPE", "SEQUENCES"*/};
	/** �����ļ���չ��*/
	private String[] includeFileExp = new String[]{"PDC", "FNC", "PRC", "SQL", "TRG", "TPS", "VW", "TAB"};
	/** �Ѽ���ļ�*/
	private List<String> checkedFiles = new ArrayList<String>();
	/** �ļ����ݲ���*/
	private List<InnerClass> unEqualsArr = new ArrayList<InnerClass>();
	/** δ������Чӳ��*/
	private List<InnerClass> missedFileArr = new ArrayList<InnerClass>();
	/** �����*/
	private OutputStreamWriter ous;
	/**
	 * ��ӡ�쳣��Ϣ
	 */
	public void showMsgs() {
		innerShowMsgs2("�Ҳ�����Ӧ���ļ� " + missedFileArr.size(), missedFileArr);
		innerShowMsgs2("�ļ����� " + unEqualsArr.size(), unEqualsArr);
		//innerShowMsgs("�������ļ�", checkedFiles);
	}
	private void innerShowMsgs2(String title, List<InnerClass> tmpList) {
		System.out.println(title);
		writeOus(title);
		for (Iterator<InnerClass> iterator = tmpList.iterator(); iterator.hasNext();) {
			InnerClass icInst = iterator.next();
			System.out.println("\t\t".concat(icInst.srcStr).concat("\t\t").concat(icInst.tarStr));
			writeOus("\t\t".concat(icInst.srcStr).concat("\t\t").concat(icInst.tarStr));
		}
	}

	private void writeOus(String str) {
		if (null == ous) {
			return;
		}
		try {
			ous.write(str);
			ous.write("\r\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * ��ѯ�ļ�
	 * @param dir Ŀ¼ʵ��
	 * @param fileName �ļ�����
	 * @return rtnField/null
	 */
	public File searchFile(File dir, final String fileName) {
		File[] fileArr =  dir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return fileName.toUpperCase().equals(name.toUpperCase());
			}
		});
		if (fileArr.length > 0) {
			return fileArr[0];
		} else {
			return null;
		}
	}

	/**
	 * �ļ��Ƚ����
	 * @param patchs �ļ�·����
	 * @param trunk ����·��
	 * @throws IOException IO�쳣
	 */
	public void comparator(String[] patchs, String trunk) throws IOException {
		File trunkDir = new File(trunk);
		for (String tmpPath : patchs) {
			comparator(new File(tmpPath), trunkDir);
		}
		showMsgs();
	}

	/**
	 * �ļ��Ƚ����
	 * @param patch Դ·��ʵ��
	 * @param trunk Ŀ��·��ʵ��
	 */
	protected void comparator(File patch, File trunk) {
		File[] file = patch.listFiles();
		if (file == null) {
			System.out.println(patch);
			return;
		}
		for (int i = 0; i < file.length; i++) {
			File srcFile = file[i];
			if (checkedFiles.contains(srcFile.getName())) {
				continue;
			}
			if (srcFile.isDirectory() && !filterDir(srcFile)) {
				File f = new File(trunk.getPath() + "/" + srcFile.getName());
				if (!f.exists()) {
					f = trunk;
				}
				comparator(srcFile, f);
			} else if (srcFile.isFile() && filterFile(srcFile)) {
				File trunkFile = searchFile(trunk, srcFile.getName());
				if (trunkFile == null) {
					missedFileArr.add(new InnerClass(srcFile.getPath(), new File(trunk, srcFile.getName()).getPath(), 0));
					continue;
				}
				if (!isEquals(srcFile, trunkFile)) {
					unEqualsArr.add(new InnerClass(srcFile.getPath(), trunkFile.getPath(), 0));
				} else {
//					checkedFiles.add(srcFile.getName());
					checkedFiles.add(srcFile.getAbsolutePath());
				}
			}
		}
	}

	/**
	 * �ļ��й���
	 * @param dirFile �ļ���ʵ��
	 * @return rtnFlag true/false
	 */
	private boolean filterDir(File dirFile) {
		String dirName = dirFile.getName();
		boolean rtnFlag = false;
		for (String tmpS : excludeDirExp) {
			if (dirName.toUpperCase().endsWith(tmpS)) {
				rtnFlag = true;
				break;
			}
		}
		return rtnFlag;
	}

	/**
	 * �ļ�����
	 * @param patchFile �ļ�ʵ��
	 * @return true/false
	 */
	private boolean filterFile(File patchFile) {
		boolean rtnFlag = false;
		String fileName = patchFile.getName();
		for (String tmpS : includeFileExp) {
			if ((fileName.toUpperCase()).endsWith(tmpS)) {
				rtnFlag = true;
				break;
			}
		}
		return rtnFlag;
	}

	/**
	 * �ļ����ݱȽ�
	 * @param file1 Դ�ļ�
	 * @param file2 Ŀ���ļ�
	 * @return rtnFlag true/false
	 */
	public boolean isEquals(File file1, File file2) {
		try {
			String s1 = org.apache.commons.io.FileUtils.readFileToString(file1, Charset.defaultCharset().displayName());
			String s2 = org.apache.commons.io.FileUtils.readFileToString(file2, Charset.defaultCharset().displayName());
			return s1.trim().equals(s2.trim());
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * ����SVN
	 * @author WangYanCheng
	 * @version 2011-10-20
	 */
	class SvnFileFilter implements FilenameFilter {
		public boolean accept(File dir, String name) {
			return !(name.toUpperCase().endsWith(".SVN"));
		}
	}
	/**
	 * ����ת����
	 * @param ous
	 */
	public void setOutputStream(OutputStreamWriter ous) {
		this.ous = ous;
	}

	/**
	 * �洢�ļ��й����趨
	 * @param excludeDirExp
	 */
	public void setExcludeDirExp(String[] excludeDirExp) {
		if (null != excludeDirExp) {
			this.excludeDirExp = excludeDirExp;
		}
	}
	
	/**
	 * �洢�ļ������趨
	 * @param includeFileExp
	 */
	public void setIncludeFileExp(String[] includeFileExp) {
		if (null != includeFileExp) {
			this.includeFileExp = includeFileExp;
		}
	}

	/**
	 * �߼�ʵ��
	 * @author WangYanCheng
	 * @version 2011-10-20
	 */
	protected class InnerClass {
		private String srcStr;
		private String tarStr;
		private int typeFlag;
		public InnerClass(String srcStr, String tarStr, int typeFlag) {
			this.srcStr = srcStr;
			this.tarStr = tarStr;
			this.typeFlag = typeFlag;
		}
		public String getSrcStr() {
			return srcStr;
		}
		public void setSrcStr(String srcStr) {
			this.srcStr = srcStr;
		}
		public String getTarStr() {
			return tarStr;
		}
		public void setTarStr(String tarStr) {
			this.tarStr = tarStr;
		}
		public int getTypeFlag() {
			return typeFlag;
		}
		public void setTypeFlag(int typeFlag) {
			this.typeFlag = typeFlag;
		}
	}
	/**
	 * �������
	 * @param args �����б�
	 * @throws IOException �׳��쳣��Log
	 */
	public static void main(String[] args) throws IOException {
		String[] patchs = new String[1];
		String trunk = "F:\\work\\nstc\\2_����\\002_N9���Ͻ��ڲ���\\sql\\7_BDG";
		patchs[0] = "F:\\work\\nstc\\2_����\\002_N9���Ͻ��ڲ���\\sql\\1_3-SAM";
		FileComparator sqlComp = new FileComparator();
		sqlComp.comparator(patchs, trunk);
	}
}
