package data.lab.ongdb.algo.simhash;
/**
 * 　　　　　　　 ┏┓       ┏┓+ +
 * 　　　　　　　┏┛┻━━━━━━━┛┻┓ + +
 * 　　　　　　　┃　　　　　　 ┃
 * 　　　　　　　┃　　　━　　　┃ ++ + + +
 * 　　　　　　 █████━█████  ┃+
 * 　　　　　　　┃　　　　　　 ┃ +
 * 　　　　　　　┃　　　┻　　　┃
 * 　　　　　　　┃　　　　　　 ┃ + +
 * 　　　　　　　┗━━┓　　　 ┏━┛
 * ┃　　  ┃
 * 　　　　　　　　　┃　　  ┃ + + + +
 * 　　　　　　　　　┃　　　┃　Code is far away from     bug with the animal protecting
 * 　　　　　　　　　┃　　　┃ +
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃　　+
 * 　　　　　　　　　┃　 　 ┗━━━┓ + +
 * 　　　　　　　　　┃ 　　　　　┣┓
 * 　　　　　　　　　┃ 　　　　　┏┛
 * 　　　　　　　　　┗┓┓┏━━━┳┓┏┛ + + + +
 * 　　　　　　　　　 ┃┫┫　 ┃┫┫
 * 　　　　　　　　　 ┗┻┛　 ┗┻┛+ + + +
 */

import java.util.Objects;

/**
 * @author YanchaoMa yanchaoma@foxmail.com
 * @PACKAGE_NAME: casia.isi.neo4j.algo.simhash
 * @Description: TODO(新闻指纹)
 * @date 2019/8/29 9:42
 */
public class NewsFingerPrint {

    // TITLE SIM-HASH-VALUE
    private String titleSimHash;

    // CONTENT SIM-HASH-VALUE
    private String contentSimHash;

    public NewsFingerPrint(String titleSimHash, String contentSimHash) {
        this.titleSimHash = titleSimHash;
        this.contentSimHash = contentSimHash;
    }

    public NewsFingerPrint(String titleOrContentSimHash) {
        this.contentSimHash = titleOrContentSimHash;
    }

    public String getTitleSimHash() {
        return titleSimHash;
    }

    public void setTitleSimHash(String titleSimHash) {
        this.titleSimHash = titleSimHash;
    }

    public String getContentSimHash() {
        return contentSimHash;
    }

    public void setContentSimHash(String contentSimHash) {
        this.contentSimHash = contentSimHash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewsFingerPrint that = (NewsFingerPrint) o;
        return Objects.equals(titleSimHash, that.titleSimHash) &&
                Objects.equals(contentSimHash, that.contentSimHash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(titleSimHash, contentSimHash);
    }

    @Override
    public String toString() {
        return "NewsFingerPrint{" +
                "titleSimHash='" + titleSimHash + '\'' +
                ", contentSimHash='" + contentSimHash + '\'' +
                '}';
    }
}
