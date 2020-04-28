package data.lab.ongdb.util;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author YanchaoMa yanchaoma@foxmail.com
 * @PACKAGE_NAME: casia.isi.neo4j.util.FileUtil
 * @Description: TODO(文件操作)
 * @date 2019/7/22 11:19
 */
public class FileUtil {

    /**
     * @param
     * @return
     * @Description: TODO(获取文件内容)
     */
    public static String getFileContent(String catalog, String lastIdFileName) {
        File dir = new File(catalog);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, lastIdFileName);
        if (!file.getAbsoluteFile().exists()) {
            return null;
        }
        return getFileContent(file);
    }

    /**
     * @param data:数据
     * @param path:生成CSV文件的路径
     * @param fileName:CSV文件名
     * @param append:是否追加写入   - false覆盖写 true追加写入
     * @return
     * @Description: TODO(将数据写入CSV文件)
     */
    public static void writeDataToCSV(String data, String path, String fileName, boolean append) {
        try {
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, fileName);
            FileWriter writer = new FileWriter(file, append);
            writer.write(data);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param path:生成CSV文件的路径
     * @param fileName:CSV文件名
     * @return
     * @Description: TODO(将数据写入CSV文件)
     */
    public static void deleteFile(String path, String fileName) {
        try {
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, fileName);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param path:需要删除的文件路径
     * @return
     * @Description: TODO(删除文件夹)
     */
    public static void delete(String path) {
        try {
            File dir = new File(path);
            if (dir.exists()) {
                dir.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新最新处理的id
     *
     * @param filaname
     * @param autoId
     */
    public static void updateCursor(String filaname, String autoId) {

        File dir = new File("cursor");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(dir, filaname);
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file, false));
            writer.write(autoId);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeWriter(writer);
        }
    }

    /**
     * 读取文件
     *
     * @param file
     * @return
     */
    public static String getFileContent(File file) {
        StringBuffer sb = new StringBuffer();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line.trim()).append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeReader(reader);
        }
        return sb.toString();
    }

    /**
     * 读取文件
     *
     * @param file
     * @return
     */
    public static Set<String> getFileContentByLine(File file) {
        Set<String> set = new HashSet<String>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                if ("".equals(line.trim()))
                    continue;
                set.add(line.trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeReader(reader);
        }
        return set;
    }

    /**
     * 读取文件首行
     *
     * @param filePath
     * @return
     */
    public static String getFirstLine(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
            String line;
            if ((line = reader.readLine()) != null) {
                return line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeReader(reader);
        }
        return null;
    }


    /**
     * 按行读取文件
     *
     * @param fileName 文件名
     * @return List<String> 行列表
     * @throws IOException
     */
    @SuppressWarnings("resource")
    public static List<String> readFileByLine(String fileName) throws IOException {
        List<String> lineList = new ArrayList<String>();

        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
        String line = null;
        while ((line = reader.readLine()) != null) {
            lineList.add(line.trim());
        }

        return lineList;
    }

    /**
     * 追加文件：使用FileOutputStream，在构造FileOutputStream时，把第二个参数设为true
     *
     * @param fileName 文件名
     * @param content  文件内容
     */
    public static void writeFileByAddNew(String fileName, String content) {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(fileName, true), "UTF-8"));
            out.write(content + "\r\n");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 写一个新文件
     *
     * @param fileName 文件名
     * @param content  文件内容
     */
    public static void writeFileByNewFile(String fileName, String content) {
        BufferedWriter out = null;

        try {
            out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(fileName, false), "UTF-8"));
            out.write(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param
     * @return
     * @Description: TODO(Read one line)
     */
    public static String readOneLine(String filePath) {
        File file = new File(filePath);
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String str = br.readLine();
            return str;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 从文件中读取最后处理的id
     *
     * @param lastIdFileName
     * @return
     */
    public static long getLastId(String lastIdFileName) {
        File dir = new File("cursor");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File lastIdFile = new File(dir, lastIdFileName);
        if (!lastIdFile.getAbsoluteFile().exists()) {
            try {
                lastIdFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return 0;
        }
        FileReader reader = null;
        String id = null;
        try {
            reader = new FileReader(lastIdFile);
            char[] buffer = new char[12];
            int read = reader.read(buffer);
            if (read == -1) {
                id = "0";
            } else {
                char[] realBuffer = new char[read];
                System.arraycopy(buffer, 0, realBuffer, 0, read);
                id = new String(realBuffer).replace("\r\n", "");
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeReader(reader);
        }
        if (id != null && !"".equals(id)) {
            return Long.parseLong(id);
        }
        return 0;
    }

    public static String getLastUpdateTime(String last_timeFile) {
        String defaultTime = "1970-01-01 00:00:00";
        File dir = new File("cursor");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File lastIdFile = new File(dir, last_timeFile);
        if (!lastIdFile.getAbsoluteFile().exists()) {
            try {
                lastIdFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // DEFAULT UPDATE TIME
            return defaultTime;
        }
        String line = readOneLine("cursor" + File.separator + last_timeFile);
        if (line != null && !"".equals(line)) {
            return line;
        }
        return defaultTime;
    }

    /**
     * @param @param ids
     * @param @param filename    参数
     * @return void    返回类型
     * @throws
     * @Title: writeIDSToFile
     * @Description: TODO(将ids写入文件)
     */
    public static void writeIDSToFile(String ids, String filename) {
        try {
            File dir = new File("cursor");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, filename);
            FileWriter writer = new FileWriter(file, true);
            writer.write(ids + "\r\n");
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param @param ids
     * @param @param filename    参数 false覆盖 true追加
     * @return void    返回类型
     * @throws
     * @Title: writeIDSToFile
     * @Description: TODO(将ids写入文件)
     */
    public static void writeIDSToFile(String ids, String filename, boolean bool) {
        try {
            File dir = new File("cursor");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, filename);
            FileWriter writer = new FileWriter(file, bool);
            writer.write(ids + "\r\n");
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param @param ids
     * @param @param filename    参数 false覆盖 true追加
     * @return void    返回类型
     * @throws
     * @Title: writeIDSToFile
     * @Description: TODO(将ids写入文件)
     */
    public static void writeIDSToFile(String ids, String pathName, String filename, boolean bool) {
        try {
            File dir = new File(pathName);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, filename);
            FileWriter writer = new FileWriter(file, bool);
            writer.write(ids + "\r\n");
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param reader:文件读取器
     * @return
     * @Description: TODO(关闭文件READER)
     */
    public static void closeReader(FileReader reader) {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param writer:文件写入器
     * @return
     * @Description: TODO(关闭文件WRITER)
     */
    private static void closeWriter(BufferedWriter writer) {
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param reader:文件读取器
     * @return
     * @Description: TODO(关闭文件READER)
     */
    private static void closeReader(BufferedReader reader) {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

