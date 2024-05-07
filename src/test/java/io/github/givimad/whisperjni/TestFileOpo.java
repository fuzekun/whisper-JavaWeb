package io.github.givimad.whisperjni;

import java.io.File;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * @author: Zekun Fu
 * @date: 2024/5/7 21:24
 * @Description:
 */
public class TestFileOpo {

    /** 不能自动递归或者循环创建，需要手动一层一层的创建 */
    @Test
    public void testMkDir() throws IOException {
        String path = "d:\\data\\speak\\speak\\txt";
        File pat = new File(path);
        if (!pat.exists()) {
            assert mkDir(path);
        }
    }

    @Test
    public void testRename() throws IOException {
        String sourceFile = "d:\\password.txt";
        String targetFile = "d:\\data\\speak\\txt\\password.txt";
//        assert new File(sourceFile).renameTo(new File(targetFile));
        saveFile(new File(sourceFile), targetFile);
    }
    public boolean mkDir(String path) {
        String[]paths = path.split("\\\\");
        if (paths.length == 0) paths = new String[]{path};
//        return mkDirHepler(paths, 0, new StringBuilder());
        return mkDirHepler(paths);
    }
    private boolean mkDirHepler(String[] paths) {
        StringBuilder curPath = new StringBuilder();
        for (String path : paths) {
            curPath.append("\\").append(path);
            File file = new File(curPath.toString());
            if (!file.exists()) {
                if (!file.mkdir())
                    return false;
            }
        }
        return true;
    }
    private boolean mkDirHepler(String[] paths, int cur, StringBuilder curPath) {
        if (cur == paths.length) {
            return true;
        }
        curPath.append("\\").append(paths[cur]);
        File pathFile = new File(curPath.toString());
        if (!pathFile.exists()) {
            if (!pathFile.mkdir())
                return false;
        }
        return mkDirHepler(paths, cur + 1, curPath);
    }
    private boolean saveFile(File sourceFile, String filePath) throws IOException {
        // 首先创建文件夹
        if (filePath.contains("\\"))  {
            String[]paths = filePath.split("\\\\");
            if(!mkDirHepler(paths, paths.length - 1, new StringBuilder()))
                return false;
        }
        // 创建文件
        File targetFile = new File(filePath);
        if (!targetFile.exists()) {
            if (targetFile.createNewFile())
                return false;
        }
        // 移动文件
        return sourceFile.renameTo(new File(filePath));
    }
}
