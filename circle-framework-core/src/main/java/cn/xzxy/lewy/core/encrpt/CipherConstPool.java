package cn.xzxy.lewy.core.encrpt;

/**
 * 加密算法静态资源池
 */
public class CipherConstPool {
	// ====================分组算法工作模式=============================
	/** 分组算法工作模式 - 电子密码本模式 **/
	public static final String WORK_PATTERN_ECB = "ECB";
	/** 分组算法工作模式 - 密文链接模式 **/
	public static final String WORK_PATTERN_CBC = "CBC";
	/** 分组算法工作模式 - 密文反馈模式 **/
	public static final String WORK_PATTERN_CFB = "CFB";
	/** 分组算法工作模式 - 输出反馈模式 **/
	public static final String WORK_PATTERN_OFB = "OFB";
	/** 分组算法工作模式 - 计数器模式 **/
	public static final String WORK_PATTERN_CTR = "CTR";
	// ====================RSA 分组算法填充模式 =============================
	/** RSA 分组算法填充模式 - PKCS1 默认 jdk默认 **/
	public static final String RSA_PADDING_PATTERN_PKCS1 = "PKCS1Padding";
	/** RSA 分组算法填充模式 - NO_PADDING **/
	public static final String RSA_PADDING_PATTERN_NO_PADDING = "NoPadding";
	/** RSA 分组算法填充模式 - OAEP_MD5 **/
	public static final String RSA_PADDING_PATTERN_OAEP_MD5 = "OAEPWITHMD5AndMGF1Padding";
	/** RSA 分组算法填充模式 - OAEP_SHA1 **/
	public static final String RSA_PADDING_PATTERN_OAEP_SHA1 = "OAEPWITHSHA1AndMGF1Padding";
	/** RSA 分组算法填充模式 - OAEP_SHA256 **/
	public static final String RSA_PADDING_PATTERN_OAEP_SHA256 = "OAEPWITHSHA256AndMGF1Padding";
	/** RSA 分组算法填充模式 - OAEP_SHA384 **/
	public static final String RSA_PADDING_PATTERN_OAEP_SHA384 = "OAEPWITHSHA384AndMGF1Padding";
	/** RSA 分组算法填充模式 - OAEP_SHA512 **/
	public static final String RSA_PADDING_PATTERN_OAEP_SHA512 = "OAEPWITHSHA512AndMGF1Padding";
	/** RSA 分组算法填充模式 - ISO_9796_1 **/
	public static final String RSA_PADDING_PATTERN_ISO_9796_1 = "ISO9796-1Padding";
	// ====================AES 分组算法填充模式 =============================
	/** AES 分组算法填充模式 - No-Padding **/
	public static final String AES_PADDING_PATTERN_NO_PADDING = "NoPadding";
	/** RSA 分组算法填充模式 - PKCS5 **/
	public static final String AES_PADDING_PATTERN_PKCS5 = "PKCS5Padding";
	/** RSA 分组算法填充模式 - ISO_10126 **/
	public static final String AES_PADDING_PATTERN_ISO_10126 = "ISO10126Padding";
	// =======================in use===========================================
	/** JDK默认为RSA/ECB/PKCS1Padding **/
	public static final String CIPHER_RSA = "RSA";
	/** AES 算法 **/
	public static final String CIPHER_AES = "AES";
	/** 算法全称 **/
	public static final String CIPHER_AES_NAME = "AES/ECB/PKCS5Padding";
	// ==========================session=================================
	/** 初始化rsa公钥 **/
	@Deprecated
	public static final String SESSION_KEY_INIT_RSA_PUBLIC = "init-rsa-public";
	/** 初始化rsa私钥 **/
	@Deprecated
	public static final String SESSION_KEY_INIT_RSA_PRIVATE = "init-rsa-private";
	/** 初始化aes秘钥 **/
	@Deprecated
	public static final String SESSION_KEY_INIT_AES_DEFAULT = "init-aes";
	/** 工作rsa公钥 **/
	@Deprecated
	public static final String SESSION_KEY_WORK_RSA_PUBLIC = "work-rsa-public";
	/** 工作rsa私钥 **/
	@Deprecated
	public static final String SESSION_KEY_WORK_RSA_PRIVATE = "work-rsa-private";
	/** 工作aes秘钥 **/
	@Deprecated
	public static final String SESSION_KEY_WORK_AES_DEFAULT = "work-aes";

}
