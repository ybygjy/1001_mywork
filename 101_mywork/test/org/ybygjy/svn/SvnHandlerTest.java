package org.ybygjy.svn;

import static org.junit.Assert.fail;

import java.io.File;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.ybygjy.util.svn.SvnConstants;
import org.ybygjy.util.svn.SvnHandler;

/**
 * Svn������������
 * @author WangYanCheng
 * @version 2013-1-24
 */
public class SvnHandlerTest {
    private String src = "trunk/HelloSVN";
    private String des = "tags/HelloSVN";
    private SvnHandler svnHandler;
    @Before
    public void setUp() throws Exception {
        svnHandler = SvnHandler.getInstance(SvnConstants.ROOT_URL, "harry", "harry");
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testUpdate() {
        fail("Not yet implemented");
    }

    @Test
    public void testGetSVNClientManager() {
        Assert.assertNotNull(svnHandler.getSVNClientManager());
    }

    @Test
    public void testIsExist() {
        Assert.assertTrue(svnHandler.isExist(SvnConstants.ROOT_URL));
    }

    @Test
    public void testIsExistSvnUrl() {
        try {
            svnHandler.isExistSvnUrl(SvnConstants.ROOT_URL);
        } catch (SVNException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDeleteDir() {
        try {
            svnHandler.deleteDir(SvnConstants.ROOT_URL.concat(src), "org.ybygjy.util.svn.SvnHandlerTest.testDeleteDir()");
            //svnHandler.deleteDir("J://svnClient//trunk//HelloSVN", "org.ybygjy.util.svn.SvnHandlerTest.testDeleteDir()");
        } catch (SVNException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCopyToDir() {
        try {
            svnHandler.copyToDir(SvnConstants.ROOT_URL.concat(src), SvnConstants.ROOT_URL.concat(des), "org.ybygjy.util.svn.SvnHandlerTest.testCopyToDir()");
        } catch (SVNException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDeleteAndCopyTo() {
        //SvnHandler#copyToDir�ķ������
        //���Ƚ�desɾ��=>Ȼ��src����copy��des
        try {
            svnHandler.deleteAndCopyTo(SvnConstants.ROOT_URL.concat(src), SvnConstants.ROOT_URL.concat(des), "org.ybygjy.util.svn.SvnHandlerTest.testDeleteAndCopyTo()");
        } catch (SVNException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testImportDir() {
        String url = SvnConstants.ROOT_URL.concat("trunk/TestImport");
        File targetFile = new File(this.getClass().getClassLoader().getResource(getCurrClassFile()).getFile());
        try {
            svnHandler.importDir(targetFile.getParentFile(), url, "org.ybygjy.util.svn.SvnHandlerTest.testImportDir()");
        } catch (SVNException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCommitFile() {
        //�˹��ܲ�֧����Ŀ¼���ް汾���Ƶ���Դ�Զ��ύ
        File commitFile = new File("J://svnClient//trunk//HelloSVN");
        try {
            svnHandler.commitFile(commitFile, "org.ybygjy.util.svn.SvnHandlerTest.testCommitFile()");
        } catch (SVNException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCommitFiles() {
        //�˹��ܿ�֧����Ŀ¼���ް汾���Ƶ���Դ�Զ��ύ
        File commitFile = new File("J://svnClient//trunk//HelloSVN");
        try {
            svnHandler.commitFiles(commitFile, "org.ybygjy.util.svn.SvnHandlerTest.testCommitFiles()");
        } catch (SVNException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCommitFilesToUrl() {
        File f = new File("J://svnClient//trunk//CommitFilesToUrl");
        String url = SvnConstants.ROOT_URL.concat("trunk/CommitFilesToUrl");
        try {
            svnHandler.commitFilesToUrl(f, url, "org.ybygjy.util.svn.SvnHandlerTest.testCommitFilesToUrl()");
        } catch (SVNException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDelete() {
        //�˹���ģʽ�ߵ���ֱ��ɾ��Զ�˷�������ʽ��Ҳ���ع�������û���κι�ϵ
        String remotePath = "trunk/TestImport";
        try {
            SVNURL svnUrl = SVNURL.parseURIEncoded(SvnConstants.ROOT_URL);
            SVNRepository svnRepository = svnHandler.getSVNClientManager().createRepository(svnUrl, false);
            svnHandler.delete(svnRepository, remotePath, "org.ybygjy.util.svn.SvnHandlerTest.testDelete()");
        } catch (SVNException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCheckOutFileToDir() {
        //���
        File localDir = new File("J:\\svnTest\\checkoutDir");
        try {
            svnHandler.checkOutFileToDir(localDir, SvnConstants.ROOT_URL.concat(src));
        } catch (SVNException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDoExportFileToDir() {
        //����
        File localDir = new File("J:\\svnTest\\ExportDir");
        try {
            svnHandler.doExportFileToDir(localDir, SvnConstants.ROOT_URL);
        } catch (SVNException e) {
            e.printStackTrace();
        }
    }

    /**
     * ȡ��ǰClass����ļ�·��
     * @return pathName
     */
    private String getCurrClassFile() {
        String pathName = this.getClass().getName().replaceAll("\\.", "/").concat(".class");
        return pathName;
    }
}
