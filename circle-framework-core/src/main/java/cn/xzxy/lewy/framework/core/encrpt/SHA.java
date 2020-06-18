package cn.xzxy.lewy.framework.core.encrpt;

import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 * 提供SHA安全散列算法
 */
@Slf4j
public class SHA {

    public static boolean checkSHA(String signdata, String chkList, String encName) {
        log.info("init checkSHA ...");
        String encryptData = getSignStr(chkList, encName);
        if (!encryptData.equals(signdata)) {
            return false;
        }
        return true;
    }

    public static String signSHA(String chkList, String encName) {
        log.info("init signSHA ...");
        return getSignStr(chkList, encName);
    }

    public static String getSignStr(String listStr, String encName) {
        log.info("init getSignStr ...");
        String visadata = "";
        String[] sourceStrArray = listStr.split("[|]");
        ArrayList list = new ArrayList<>();

        for (int i = 0; i < sourceStrArray.length; i++) {
            list.add(sourceStrArray[i].toString());
            if (i == sourceStrArray.length - 1) {
                visadata = visadata + sourceStrArray[i].toString();
            } else {
                visadata = visadata + sourceStrArray[i].toString() + "|";
            }
        }

        return encrypt(visadata, encName);
    }

    /**
     * sha-256加密算法
     */
    public static String encrypt(String strSrc, String encName) {
        log.info("init Encrypt ...");
        MessageDigest md = null;
        String strDes = null;
        byte[] bt = strSrc.getBytes();
        try {
            if (encName == null || encName.equals("")) {
                encName = "SHA-256";
            }
            md = MessageDigest.getInstance(encName);
            md.update(bt);
            strDes = bytes2Hex(md.digest());

        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        return strDes;
    }

    public static String bytes2Hex(byte[] bts) {
        log.info("init bytes2Hex ...");
        String des = "";
        String tmp = null;
        for (int i = 0; i < bts.length; i++) {
            tmp = (Integer.toHexString(bts[i] & 0xFF));
            if (tmp.length() == 1) {
                des += "0";
            }
            des += tmp;
        }
        return des;
    }
}
