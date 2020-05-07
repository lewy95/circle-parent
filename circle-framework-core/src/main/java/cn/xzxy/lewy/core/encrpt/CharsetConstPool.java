package cn.xzxy.lewy.core.encrpt;

/**
 * 字符集
 */
public class CharsetConstPool {

    public static final String CHAR_SET_US_ASCII = "US-ASCII";
    public static final String CHAR_SET_ISO_8859_1 = "ISO-8859-1";
    public static final String CHAR_SET_UTF_8 = "UTF-8";
    public static final String CHAR_SET_UTF_16BE = "UTF-16BE";
    public static final String CHAR_SET_UTF_16LE = "UTF-16LE";
    public static final String CHAR_SET_UTF_16 = "UTF-16";
    public static final String CHAR_SET_GBK = "GBK";

    public static final String[] SEED_POOL = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d",
            "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y",
            "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z"};
    public static final String[] SYMBOL_POOL = {"!", "#", "$", "%", "&", "'", "(", ")", "*", "+", ",", "-", ".", "/",
            "[", "{", "|", "}", "~", "]", "^", "_", "`",};

    public static final String BOOLEAN_STRING_FALSE = "false";
    public static final String BOOLEAN_STRING_TRUE = "true";
}
