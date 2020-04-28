package data.lab.ongdb.util;
/*
 *
 * Data Lab - graph database organization.
 *
 */import java.io.UnsupportedEncodingException;
import java.util.Base64;

/**
 * @author Yc-Ma 
 * @PACKAGE_NAME: data.lab.ongdb.util
 * @Description: TODO(BASE64)
 * @date 2019/7/9 10:48
 */
public class Base64Digest {

    private static String CHARACTER = "UTF-8";

    private static Base64.Encoder encoder = Base64.getEncoder();

    private static Base64.Decoder decoder = Base64.getDecoder();

    /**
     * @param text:当前文本
     * @return
     * @Description: TODO(base64编码)
     */
    public static String encoder(String text) {
        if (text != null && !"".equals(text)) {
            try {
                return encoder.encodeToString(text.getBytes(CHARACTER));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * @param text:当前文本
     * @return
     * @Description: TODO(base64解码)
     */
    public static String dncoder(String text) {
        if (text != null && !"".equals(text)) {
            try {
                return new String(decoder.decode(text),CHARACTER);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}

