package org.ybygjy.util.svn;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;

import org.tmatesoft.svn.core.ISVNLogEntryHandler;
import org.tmatesoft.svn.core.SVNCommitInfo;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNErrorCode;
import org.tmatesoft.svn.core.SVNErrorMessage;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNProperties;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.wc.ISVNOptions;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNCommitClient;
import org.tmatesoft.svn.core.wc.SVNCopyClient;
import org.tmatesoft.svn.core.wc.SVNCopySource;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNStatus;
import org.tmatesoft.svn.core.wc.SVNStatusType;
import org.tmatesoft.svn.core.wc.SVNWCClient;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

public final class SvnHandler {
    private String svnRoot;
    private String userName;
    private String password;
    
    private SvnHandler(String svnRoot, String userName, String password) {
        this.svnRoot = svnRoot;
        this.userName = userName;
        this.password = password;
    }

    public void update(String dir) throws SVNException {
        SVNClientManager svnClientManager = getSVNClientManager();
        try {
            svnClientManager.getUpdateClient().doUpdate(new File(dir), SVNRevision.HEAD, SVNDepth.INFINITY,
                true, true);
        } finally {
            svnClientManager.dispose();
        }
    }

    public SVNClientManager getSVNClientManager() {
        FSRepositoryFactory.setup();
        DAVRepositoryFactory.setup();
        SVNRepositoryFactoryImpl.setup();
        ISVNOptions options = SVNWCUtil.createDefaultOptions(true);
        // String decryptName = SecurityUtil.decrypt(SvnConstants.NAME);
        // String decryptPassword = SecurityUtil.decrypt(SvnConstants.PASSWORD);
        // SVNClientManager cm = SVNClientManager.newInstance((DefaultSVNOptions) options, decryptName, decryptPassword);
        SVNClientManager cm = SVNClientManager.newInstance((DefaultSVNOptions) options, this.userName, this.password);
        return cm;
    }

    public boolean isExist(String dir) {
        if (!StringUtil.hasLengthAfterTrim(dir)) {
            throw new NullPointerException("dir" + "��������Ϊ�գ�");
        }

        SVNClientManager svnClientManager = getSVNClientManager();
        try {
            SVNWCClient svnwcClient = svnClientManager.getWCClient();
            SVNURL svnUrl = SVNURL.parseURIDecoded(dir);
            svnwcClient.doInfo(svnUrl, SVNRevision.HEAD, SVNRevision.HEAD);
            return true;
        } catch (SVNException e) {
            e.printStackTrace();
            System.out.println(dir + "�����ڣ�");
            return false;
        } finally {
            svnClientManager.dispose();
        }
    }

    public void deleteDir(String dir, String commitMessage) throws SVNException {
        if (!StringUtil.hasLengthAfterTrim(dir)) {
            throw new NullPointerException("dir" + "��������Ϊ�գ�");
        }

        if (!isExist(dir)) {
            return;
        }
        // ʵ��ɾ��Ŀ¼�Ĺ���
        SVNClientManager svnClientManager = getSVNClientManager();
        try {
            SVNCommitClient svnCommitClient = svnClientManager.getCommitClient();

            String message = commitMessage;
            if (!StringUtil.hasLengthAfterTrim(message)) {
                message = "�Զ�ɾ��" + dir;
            }

            SVNURL svnUrl = SVNURL.parseURIDecoded(dir);
            SVNCommitInfo svnCommitInfo = svnCommitClient.doDelete(new SVNURL[] { svnUrl }, message);
            SVNErrorMessage svnErrorMessage = svnCommitInfo.getErrorMessage();
            if (svnErrorMessage != null) {
                throw new RuntimeException(svnErrorMessage.getFullMessage());
            }

        } finally {
            svnClientManager.dispose();
        }
    }

    public SVNCommitInfo copyToDir(String src, String des, String commitMessage) throws SVNException {
        SVNCommitInfo svnCommitInfo = null;
        if (!StringUtil.hasLengthAfterTrim(src)) {
            throw new NullPointerException("src" + "��������Ϊ�գ�");
        }

        if (!StringUtil.hasLengthAfterTrim(des)) {
            throw new NullPointerException("des" + "��������Ϊ�գ�");
        }

        // ʵ�ֿ���Ŀ¼�Ĺ���
        SVNClientManager svnClientManager = getSVNClientManager();
        try {
            String message = commitMessage;
            if (!StringUtil.hasLengthAfterTrim(message)) {
                message = "�Զ���" + src + "������" + des;
            }

            SVNURL srcSVNURL = SVNURL.parseURIDecoded(src);
            SVNCopySource svnCopySource = new SVNCopySource(SVNRevision.HEAD, SVNRevision.HEAD, srcSVNURL);
            SVNURL desSVNURL = SVNURL.parseURIDecoded(des);
            SVNCopyClient svnCopyClient = svnClientManager.getCopyClient();
            svnCommitInfo = svnCopyClient.doCopy(new SVNCopySource[] { svnCopySource }, desSVNURL, false,
                true, true, message, new SVNProperties());
            SVNErrorMessage svnErrorMessage = svnCommitInfo.getErrorMessage();
            if (svnErrorMessage != null) {
                throw new RuntimeException(svnErrorMessage.getFullMessage());
            }
        } finally {
            svnClientManager.dispose();
        }
        return svnCommitInfo;
    }

    public SVNCommitInfo deleteAndCopyTo(String src, String des, String commitMessage) throws SVNException {
        if (!StringUtil.hasLengthAfterTrim(src)) {
            throw new NullPointerException("src" + "��������Ϊ�գ�");
        }

        if (!StringUtil.hasLengthAfterTrim(des)) {
            throw new NullPointerException("des" + "��������Ϊ�գ�");
        }
        if (des.toLowerCase().indexOf("trunk") > 0) {
            throw new RuntimeException(des + "�з��� trunk�ؼ���,������Ĳ���!");
        }
        if (des.indexOf("/branches/") <= 0 && des.indexOf("/tags/") <= 0 && des.indexOf("/AchV/") <= 0) {
            throw new RuntimeException(des + "��û�з��� branches��tags�ؼ���,������Ĳ���!");
        }
        deleteDir(des, commitMessage);
        System.out.println("ɾ��" + des + "���");
        // ��ԴĿ¼������Ŀ��Ŀ¼
        SVNCommitInfo svnCommitInfo = copyToDir(src, des, commitMessage);
        System.out.println("����\r\n" + src + "\r\n" + des + "���");
        return svnCommitInfo;
    }

    public boolean isExistSvnUrl(String url) throws SVNException {
        SVNClientManager svnClientManager = getSVNClientManager();
        SVNURL svnUrl = SVNURL.parseURIDecoded(url);
        SVNRepository repository = svnClientManager.createRepository(svnUrl, false);
        if (SVNNodeKind.NONE.equals(repository.checkPath(".", -1))) {
            return false;
        } else {
            return true;
        }
    }

    public void importDir(File dir, String url, String memo) throws SVNException {
        SVNClientManager svnClientManager = getSVNClientManager();
        try {
            SVNURL svnUrl = SVNURL.parseURIDecoded(url);
            if (!isExistSvnUrl(url)) {
                svnClientManager.getCommitClient().doMkDir(new SVNURL[] { svnUrl }, memo);
            }
            svnClientManager.getCommitClient().doImport(dir, svnUrl, memo, false, false);
            //.doImport(dir, svnUrl, memo, false);
            //.doImport(dir, svnUrl, memo, true);
        } finally {
            svnClientManager.dispose();
        }
    }

    public void commitFile(File commitFile, String memo) throws SVNException {
        SVNClientManager svnClientManager = getSVNClientManager();
        try {
            SVNStatus status = svnClientManager.getStatusClient().doStatus(commitFile, true);
            // ������ļ��������ӵ����ȰѴ��ļ���ӵ��汾�⣬Ȼ���ύ��
            if (status.getContentsStatus().toString()
                .equalsIgnoreCase(SVNStatusType.STATUS_UNVERSIONED.toString())) {
                // �Ѵ��ļ����ӵ��汾����
                svnClientManager.getWCClient().doAdd(commitFile, false, false, false, SVNDepth.INFINITY,
                    false, false);
                // �ύ���ļ�
                svnClientManager.getCommitClient().doCommit(new File[] { commitFile }, true, memo, null,
                    null, true, false, SVNDepth.INFINITY);
                System.out.println("add");
            } else if (status.getContentsStatus().toString()
                .equalsIgnoreCase(SVNStatusType.CHANGED.toString())
                || status.getContentsStatus().toString()
                    .equalsIgnoreCase(SVNStatusType.STATUS_ADDED.toString())
                || status.getContentsStatus().toString()
                    .equalsIgnoreCase(SVNStatusType.STATUS_MODIFIED.toString())) {// ������ļ����������ӵģ�ֱ���ύ��
                svnClientManager.getCommitClient().doCommit(new File[] { commitFile }, true, memo, null,
                    null, true, false, SVNDepth.INFINITY);
                System.out.println("commit");
            }
        } finally {
            svnClientManager.dispose();
        }
    }

    public void commitFiles(File f, String memo) throws SVNException {
        if (f.isFile()) {
            commitFile(f, memo);
        } else {
            commitFile(f, memo);
            File[] list = f.listFiles();
            for (int i = 0; i < list.length; i++) {
                File fl = list[i];
                if (!fl.getName().startsWith(".svn")) {
                    commitFiles(fl, memo);
                }
            }
        }
    }

    public void commitFilesToUrl(File f, String url, String memo) throws SVNException {
        SVNClientManager svnClientManager = getSVNClientManager();
        try {
            if (!f.exists()) {
                f.mkdir();
            }
            SVNURL svnUrl = SVNURL.parseURIDecoded(url);
            if (isExistSvnUrl(url)) {
                if (new File(f.getAbsoluteFile() + File.separator + ".svn").exists()) {
                    commitFiles(f, memo);
                    svnClientManager.getUpdateClient().doUpdate(f, SVNRevision.HEAD, SVNDepth.INFINITY,
                        true, true);
                } else {
                    svnClientManager.getUpdateClient().doCheckout(svnUrl, f, SVNRevision.HEAD,
                        SVNRevision.HEAD, SVNDepth.INFINITY, false);
                    commitFiles(f, memo);
                }
            } else {
                svnClientManager.getCommitClient().doMkDir(new SVNURL[] { svnUrl }, memo);
                svnClientManager.getUpdateClient().doCheckout(svnUrl, f, SVNRevision.HEAD,
                    SVNRevision.HEAD, SVNDepth.INFINITY, false);
                commitFiles(f, memo);
            }
        } finally {
            svnClientManager.dispose();
        }
    }

    public void commitFileToUrl(File file, String url, String memo, SVNClientManager parentSVNM) throws SVNException {
        SVNClientManager svnCM = parentSVNM == null ? getSVNClientManager() : parentSVNM;
        if (!file.exists()) {
            throw new SVNException(SVNErrorMessage.create(SVNErrorCode.FS_NOT_FOUND, file.getPath()));
        }
        try {
            if (file.isDirectory()) {
                File[] fileArr = file.listFiles();
                for (int i = 0; i < fileArr.length; i++) {
                    commitFileToUrl(fileArr[i], url, memo, svnCM);
                }
            } else {
                SVNURL svnUrl = SVNURL.parseURIEncoded(url);
                SVNRepository svnRepository = svnCM.createRepository(svnUrl, false);
                SVNNodeKind svnNodeKind = svnRepository.checkPath(file.getName(), -1);
                String filePath = svnRepository.getRepositoryRoot(false).toString().concat("/").concat(svnRepository.getFullPath(file.getName()));
                SVNURL fileSvnUrl = SVNURL.parseURIDecoded(filePath);
                if (SVNNodeKind.FILE == svnNodeKind) {
                    svnCM.getCommitClient().doDelete(new SVNURL[]{fileSvnUrl}, memo);
                    svnNodeKind = SVNNodeKind.NONE;
                }
                if (SVNNodeKind.NONE == svnNodeKind) {
                    svnCM.getCommitClient().doImport(file, fileSvnUrl, memo, null, true, true, SVNDepth.fromRecurse(false));
                    //this.showLog(svnCM, svnUrl, new String[]{file.getName()});
                }
            }
        } finally {
            if (parentSVNM != svnCM) {
                svnCM.dispose();
            }
        }
    }

    /*
     * �˺����ݹ�Ļ�ȡ�汾����ĳһĿ¼�µ�������Ŀ
     */
    public void delete(SVNRepository repository, String path, String memo) throws SVNException {
        Collection entries = repository.getDir(path, -1, null, (Collection) null);// ��ȡ�汾���pathĿ¼�µ�������Ŀ��������1��ʾ�����°汾��
        Iterator iterator = entries.iterator();
        while (iterator.hasNext()) {
            SVNDirEntry entry = (SVNDirEntry) iterator.next();
            // ��ǰEntry���ļ���->recurse delete
            if (entry.getKind() == SVNNodeKind.DIR) {
                delete(repository, entry.getRelativePath(), memo);
            }
            // ��ǰ���ļ� delete
            getSVNClientManager().getCommitClient().doDelete(new SVNURL[] { entry.getURL() }, memo);
        }
        SVNDirEntry svnDirEntry = repository.getDir(path, -1, false, null);
        if (svnDirEntry.getKind() == SVNNodeKind.DIR) {
            getSVNClientManager().getCommitClient().doDelete(new SVNURL[] { svnDirEntry.getURL() }, memo);
            System.out.println("ɾ��=>".concat(svnDirEntry.getRelativePath()));
        }
    }

    public void checkOutFileToDir(File f, String url) throws SVNException {
        SVNClientManager svnClientManager = getSVNClientManager();
        try {
            if (!f.exists()) {
                f.mkdir();
            }
            SVNURL svnUrl = SVNURL.parseURIDecoded(url);
            if (isExistSvnUrl(url)) {
                if (new File(f.getAbsoluteFile() + File.separator + ".svn").exists()) {
                    svnClientManager.getUpdateClient().doUpdate(f, SVNRevision.HEAD, SVNDepth.INFINITY,
                        true, true);
                } else {
                    svnClientManager.getUpdateClient().doCheckout(svnUrl, f, SVNRevision.HEAD,
                        SVNRevision.HEAD, SVNDepth.INFINITY, false);
                }
            }

        } finally {
            svnClientManager.dispose();
        }
    }

    public void doExportFileToDir(File f, String url) throws SVNException {
        SVNClientManager svnClientManager = getSVNClientManager();
        try {
            if (!f.exists()) {
                f.mkdir();
            }
            SVNURL svnUrl = SVNURL.parseURIEncoded(url);
            if (isExistSvnUrl(url)) {
                svnClientManager.getUpdateClient().doExport(svnUrl, f, SVNRevision.HEAD, SVNRevision.HEAD, "LF", true, true);
            }
        } finally {
            svnClientManager.dispose();
        }
    }
    public static final SvnHandler getInstance(String svnRoot, String userCode, String password) {
        return new SvnHandler(svnRoot, userCode, password);
    }

    public void mkDir(String destPath, String message) throws SVNException {
        SVNClientManager svnClientManager = getSVNClientManager();
        try {
            SVNURL svnUrl = SVNURL.parseURIEncoded(destPath);
            SVNRepository repository = svnClientManager.createRepository(svnUrl, false);
            SVNNodeKind svnNodeKind = repository.checkPath(".", -1l);
            //��֤Ŀ¼�Ƿ����_ֻ��Ŀ¼�����ڵ�����´���
            if (SVNNodeKind.NONE == svnNodeKind) {
                System.out.println("repository.checkPath>>" + svnNodeKind);
                System.out.println("repository.getFullPath(\".\")>>" + repository.getFullPath("."));
                System.out.println("repository.getLocation()>>" + repository.getLocation());
                svnClientManager.getCommitClient().doMkDir(new SVNURL[]{svnUrl}, message, new SVNProperties(), true);
            }
        } finally {
            svnClientManager.dispose();
        }
    }
    public void showLog(SVNClientManager svnCM, SVNURL svnUrl, String[] paths) {
        try {
            svnCM.getLogClient().doLog(svnUrl, paths, SVNRevision.HEAD, SVNRevision.HEAD, SVNRevision.HEAD, true, false, 5, new ISVNLogEntryHandler() {
                public void handleLogEntry(SVNLogEntry arg0) throws SVNException {
                    System.out.println(arg0);
                }
            });
        } catch(SVNException e) {
            e.printStackTrace();
        }
    }
}
