package org.ybygjy.filecompare.adapter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.ybygjy.filecompare.FileComparator;



/**
 * �ļ��Ƚ�Ant����ӿ�
 * @author WangYanCheng
 * @version 2011-10-20
 */
public class FileComparator4Ant extends org.apache.tools.ant.Task  {
	/**Դ�ļ���*/
	private List<FilePath> srcFilePathArr = new ArrayList<FilePath>();
	/**���յ�ַ*/
	private String targetPath;
	/**�����ļ���*/
	private String excludeDir;
	/**�����ļ�*/
	private String includeFile;
	/**��־�ļ�*/
	private String logFile;
	/**�����*/
	private OutputStreamWriter ous;
	@Override
	public void execute() throws BuildException {
		if (srcFilePathArr.size() == 0) {
			throw new BuildException("ȱ�ٱ�Ҫ·����Ϣ��������������<FilePath path=\"...\"/>��ǡ�����");
		}
		ous = getLogOutputStream();
		if (null != ous) {
			outputMsg("ת���ļ�·����".concat(getLogFile()));
		}
		outputMsg("��ʼ���񡣡���");
		String[] srcFilePathes = toArray(srcFilePathArr);
		outputMsg("Դ��ַ��".concat(Arrays.toString(srcFilePathes)));
		outputMsg("Ŀ���ַ��".concat(getTargetPath()));
		outputMsg(80);
		FileComparator fcInst = new FileComparator();
		try {
			fcInst.setOutputStream(ous);
			if (excludeDir != null) {
				outputMsg("�����ļ��У�".concat(excludeDir));
				fcInst.setExcludeDirExp(excludeDir.split(","));
			}
			if (includeFile != null) {
				outputMsg("�����ļ���".concat(includeFile));
				fcInst.setIncludeFileExp(includeFile.split(","));
			}
			fcInst.comparator(srcFilePathes, getTargetPath());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			outputMsg("���������������");
			if (null != ous) {
				outputMsg("�ر�ת����������");
				outputMsg("ת���ļ���ַ��" + getLogFile());
				try {
					ous.flush();
					ous.close();
					ous = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * ȡ��־��
	 * @return rtnOus/null
	 */
	private OutputStreamWriter getLogOutputStream() {
		if (null == getLogFile()) {
			return null;
		}
		try {
			return new OutputStreamWriter(new FileOutputStream(getLogFile(), false));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * ȡ����·��
	 * @return rtnTargetPath ����Ŀ��·��
	 */
	public String getTargetPath() {
		return targetPath;
	}

	/**
	 * ���ò���·��
	 * @param targetPath targetPath
	 */
	public void setTargetPath(String targetPath) {
		this.targetPath = targetPath;
	}
	/**
	 * ȡ��־�ļ�·��
	 * @return rtnLogFile �ļ�·��
	 */
	public String getLogFile() {
		return logFile;
	}
	/**
	 * ������־�ļ�·��
	 * @param logFile logFile
	 */
	public void setLogFile(String logFile) {
		this.logFile = logFile;
	}
	/**
	 * NestedǶ�׶���ʵ��
	 * @return rtnFilePath rtnFilePath
	 */
	public FilePath createFilePath() {
		FilePath filePath = new FilePath();
		srcFilePathArr.add(filePath);
		return filePath;
	}
	/**
	 * �洢�����ļ����趨
	 * @param excludeDir �����ļ���
	 */
	public void setExcludeDir(String excludeDir) {
		this.excludeDir = excludeDir;
	}
	/**
	 * �洢�����ļ���׺�趨
	 * @param includeFile �����ļ�
	 */
	public void setIncludeFile(String includeFile) {
		this.includeFile = includeFile;
	}

	/**
	 * �����Ϣ
	 * @param str ��Ϣ����
	 */
	private void outputMsg(String str) {
		System.out.println(str);
		try {
			if (ous != null) {
				ous.write(str);
				ous.write("\r\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * �����Ƿ�
	 * @param flagCount ��Ƿ�����
	 */
	private void outputMsg(int flagCount) {
		StringBuilder sbud = new StringBuilder();
		for (int i = 0; i < flagCount; i++) {
			sbud.append("=");
		}
		outputMsg(sbud.toString());
	}
	/**
	 * Nestedʵ��(�ļ�·��)
	 * @author WangYanCheng
	 * @version 2011-10-20
	 */
	public class FilePath {
		private String path;
		public FilePath() {}
		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			if ("".equals(path)) {
				throw new BuildException("������Ч���ļ���ַ������<FilePath.../>��ǩֵ������");
			}
			this.path = path;
		}
		@Override
		public String toString() {
			return this.path;
		}
	}
	/**
	 * �ļ�·��ʵ��ת��������
	 * @param filePathes �ļ�·����
	 * @return rtnArr rtnArr
	 */
	private String[] toArray(List<FilePath> filePathes) {
		String[] rtnArr = new String[filePathes.size()];
		int i = 0;
		for (Iterator<FilePath> iterator = filePathes.iterator(); iterator.hasNext();) {
			rtnArr[i++] = iterator.next().path;
		}
		return rtnArr;
	}
	public static void main(String[] args) {
		FileComparator4Ant fc4Ant = new FileComparator4Ant();
		fc4Ant.setTargetPath("F:\\work\\nstc\\2_����\\003_1�ļ��Ƚ�\\sql-oracle-trunk\\BDG");
		FilePath filePath = fc4Ant.new FilePath();
		filePath.setPath("F:\\work\\nstc\\2_����\\003_1�ļ��Ƚ�\\TEST\\BDG");
		fc4Ant.srcFilePathArr.add(filePath);
		fc4Ant.execute();
	}
}
