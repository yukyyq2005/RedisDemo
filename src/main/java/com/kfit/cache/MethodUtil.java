package com.kfit.cache;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 方法工具类，包含绝大多数公共方法
 * 
 * @author Josh
 * @date 2017年2月10日 下午5:33:18
 */
public class MethodUtil {

	private static final Logger logger = LoggerFactory
			.getLogger(MethodUtil.class);

	/**
	 * 判断字符是否为空(包含去空格)
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		if (str == null || "".equals(str.trim())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断对象是否为空
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isEmpty(Object obj) {
		if (obj == null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断字符是否不为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNotEmpty(String str) {
		if (str != null && !"".equals(str.trim())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断对象是否不为空
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isNotEmpty(Object obj) {
		if (obj != null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * unicode 转换成 中文
	 * 
	 * @return
	 */
	public static String decodeUnicode(String theString) {
		char aChar;
		int len = theString.length();
		StringBuffer outBuffer = new StringBuffer(len);
		for (int x = 0; x < len;) {
			aChar = theString.charAt(x++);
			if (aChar == '\\') {
				aChar = theString.charAt(x++);
				if (aChar == 'u') {
					int value = 0;
					for (int i = 0; i < 4; i++) {
						aChar = theString.charAt(x++);
						switch (aChar) {
						case '0':
						case '1':
						case '2':
						case '3':
						case '4':
						case '5':
						case '6':
						case '7':
						case '8':
						case '9':
							value = (value << 4) + aChar - '0';
							break;
						case 'a':
						case 'b':
						case 'c':
						case 'd':
						case 'e':
						case 'f':
							value = (value << 4) + 10 + aChar - 'a';
							break;
						case 'A':
						case 'B':
						case 'C':
						case 'D':
						case 'E':
						case 'F':
							value = (value << 4) + 10 + aChar - 'A';
							break;
						default:
							throw new IllegalArgumentException(
									"Malformed      encoding.");
						}
					}
					outBuffer.append((char) value);
				} else {
					if (aChar == 't') {
						aChar = '\t';
					} else if (aChar == 'r') {
						aChar = '\r';
					} else if (aChar == 'n') {
						aChar = '\n';
					} else if (aChar == 'f') {
						aChar = '\f';
					}
					outBuffer.append(aChar);
				}
			} else {
				outBuffer.append(aChar);
			}
		}
		return outBuffer.toString();
	}
	
	public static String getMD5(String str){
		return getMD5(str, "utf-8", 1);
	}

	/**
	 * MD5加密方法
	 * 
	 * @param str
	 * @param encoding
	 *            default UTF-8
	 * @param no_Lower_Upper
	 *            0,1,2 0：不区分大小写，1：小写，2：大写
	 * @return MD5Str
	 */
	public static String getMD5(String str, String encoding, int no_Lower_Upper) {
		if (null == encoding)
			encoding = "utf-8";
		StringBuffer sb = new StringBuffer();
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] array = md.digest(str.getBytes(encoding));
			for (int i = 0; i < array.length; i++) {
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100)
						.toUpperCase().substring(1, 3));
			}
		} catch (Exception e) {
			logger.error(
					convertLog(Thread.currentThread().getStackTrace()[1]
							.getMethodName(), "exception"), e);
		}
		if (no_Lower_Upper == 0) {
			return sb.toString();
		}
		if (no_Lower_Upper == 1) {
			return sb.toString().toLowerCase();
		}
		if (no_Lower_Upper == 2) {
			return sb.toString().toUpperCase();
		}
		return null;
	}

	/**
	 * 生成随机数字与字母
	 * 
	 * @param length
	 *            随机数长度
	 * @return
	 */
	public static String getStringRandom(int length) {

		String val = "";
		Random random = new Random();

		// 参数length，表示生成几位随机数
		for (int i = 0; i < length; i++) {

			String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
			// 输出字母还是数字
			if ("char".equalsIgnoreCase(charOrNum)) {
				// 输出是大写字母还是小写字母
				int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
				val += (char) (random.nextInt(26) + temp);
			} else if ("num".equalsIgnoreCase(charOrNum)) {
				val += String.valueOf(random.nextInt(10));
			}
		}

		return val.toLowerCase();
	}

	/**
	 * 获取随机数
	 * 
	 * @param min
	 *            最小数
	 * @param max
	 *            最大数
	 */
	public static int getRandom(int min, int max) {
		return (int) (Math.random() * (max - min) + min);
	}

	/**
	 * 获取随机数从1开始,格式10000000-99999999
	 * 
	 * @param number
	 *            随机数长度
	 * @return
	 */
	public static int getRandom(int number) {
		int max = 9;
		int min = 1;
		for (int i = 1; i < number; i++) {
			min = min * 10;
			max = max * 10 + 9;
		}
		return getRandom(min, max);
	}

	/**
	 * 20位可用于UUID
	 * 
	 * @return String
	 */
	public static String getUid() {
		return new SimpleDateFormat("yyMMddHHmmss").format(new Date())
				+ getRandom(8);
	}

	/**
	 * 32位可用于UUID(去除-)
	 * 
	 * @return String
	 */
	public static String getUUID() {
		return getUUID36().replaceAll("-", "");
	}
	
	/**
	 * 36位可用于UUID
	 * 
	 * @return String
	 */
	public static String getUUID36() {
		return UUID.randomUUID().toString();
	}

	/**
	 * 12位时间加上number位数
	 * 
	 * @param number
	 * @return Long
	 */
	public static Long getUid(int number) {
		return Long.parseLong(new SimpleDateFormat("yyMMddHHmmss")
				.format(new Date()) + getRandom(number));
	}

	/**
	 * 当前时间纳秒数 + 生成的 bits随机数
	 * 
	 * @param bits
	 * @return
	 */
	public static String getRandomNum(int bits) {
		// 纳秒级时间量
		long s = System.nanoTime();
		StringBuffer sb = new StringBuffer("");
		sb.append(s);
		for (int i = 0; i < bits; i++) {
			sb.append((int) (Math.random() * 10));
		}
		return sb.toString();
	}

	/**
	 * 下载图片
	 * 
	 * @param urlString
	 *            url地址
	 * @param filename
	 *            图片名称
	 * @param savePath
	 *            保存路径
	 * @throws Exception
	 */
	public static boolean downloadImg(String urlString, String filename,
			String savePath) throws Exception {
		boolean isSuccess = false;
		OutputStream os = null;
		InputStream is = null;
		try {
			logger.info("downloadImg,url= {}",urlString);
			// 构造URL
			URL url = new URL(urlString);
			// 打开连接
			URLConnection con = url.openConnection();
			// 输入流
			is = con.getInputStream();

			// 1K的数据缓冲
			byte[] bs = new byte[1024];
			// 读取到的数据长度
			int len;
			// 输出的文件流
			File sf = new File(savePath);
			if (!sf.exists()) {
				sf.mkdirs();
			}
			os = new FileOutputStream(sf.getPath() + "/" + filename);
			// 开始读取
			while ((len = is.read(bs)) != -1) {
				os.write(bs, 0, len);
			}
			// 完毕，关闭所有链接
			os.flush();
			os.close();
			is.close();
			os = null;
			is = null;
			isSuccess = true;
			logger.info("下载文件成功,保存在: {}",sf.getPath() + "/" + filename);
		}catch (Exception ee){
			logger.info("downloadImg exception: {}",ee.toString());
		}finally {
			if(os != null){
				os.close();
			}

			if(is != null){
				is.close();
			}
		}
		return isSuccess;
	}

	public static String getFixedRandomStr(int len) {
		Random rm = new Random();
		double pross = (1 + rm.nextDouble()) * Math.pow(10, len);
		String fixLenthString = String.valueOf(pross);
		return fixLenthString.substring(1, len + 1);
	}

	/**
	 * 缓存KEY配置
	 * 
	 * @param keyTemplate
	 * @param key
	 * @return
	 */
	public static String getKeyOfCachedData(String keyTemplate, String... key) {
		if (isEmpty(keyTemplate) || isEmpty(key)) {
			return "";
		}
		String[] keySection = keyTemplate.split(":");
		StringBuilder build = new StringBuilder();
		int j = 0;
		for (int i = 0; i < keySection.length; ++i) {
			String section = keySection[i].trim();
			if (section.startsWith("{") && section.endsWith("}")) {
				if (j + 1 > key.length) {
					return null;
				}
				section = key[j];
				j++;
			}
			if (i == 0) {
				build.append(section);
			} else {
				build.append(":").append(section);
			}
		}
		return build.toString();
	}

	/**
	 * 功能：驼峰格式字符串转换为下划线格式字符串
	 * 
	 * @param name
	 * @return
	 */
	public static String underscoreName(String name) {
		StringBuilder result = new StringBuilder();
		if ((name != null) && (name.length() > 0)) {
			result.append(name.substring(0, 1).toLowerCase());
			for (int i = 1; i < name.length(); ++i) {
				String s = name.substring(i, i + 1);
				if (s.equals(s.toUpperCase()) && !("_".equals(s))) {
					result.append("_");
					result.append(s.toLowerCase());
				} else {
					result.append(s);
				}
			}
		}
		return result.toString();
	}

	/**
	 * 转换日志
	 * 
	 * @param methodName
	 *            调用方法名
	 * @param msg
	 *            输出信息
	 * @param values
	 *            值
	 * @return
	 */
	public static String convertLog(String methodName, String msg,
			Object... values) {
		if (msg == null) {
			msg = "";
		}
		StringBuffer bf = new StringBuffer();
		bf.append(methodName);
		bf.append(":");
		bf.append(msg);
		bf.append("[");
		if (values.length > 0) {
			for (int i = 0; i < values.length; i++) {
				bf.append(JSON.toJSONString(values[i]));
				if (i != values.length - 1) {
					bf.append(",");
				}
			}
		}
		bf.append("]");
		return bf.toString();
	}

	public static String getIpAddress(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	/**
	 * 比较版本号的大小,前者大则返回一个正数,后者大返回一个负数,相等则返回0
	 * 
	 * @param version1
	 * @param version2
	 * @return
	 */
	public static int compareVersion(String version1, String version2) {
		if (version1 == null || version2 == null) {
			return 0;
		}
		String[] versionArray1 = version1.split("\\.");// 注意此处为正则匹配，不能用"."；
		String[] versionArray2 = version2.split("\\.");
		int idx = 0;
		int minLength = Math.min(versionArray1.length, versionArray2.length);// 取最小长度值
		int diff = 0;
		while (idx < minLength
				&& (diff = versionArray1[idx].length()
						- versionArray2[idx].length()) == 0// 先比较长度
				&& (diff = versionArray1[idx].compareTo(versionArray2[idx])) == 0) {// 再比较字符
			++idx;
		}
		// 如果已经分出大小，则直接返回，如果未分出大小，则再比较位数，有子版本的为大；
		diff = (diff != 0) ? diff : versionArray1.length - versionArray2.length;
		return diff;
	}

	private static Field[] getBeanFields(Class<?> cls, Field[] fs) {
		fs = (Field[]) ArrayUtils.addAll(fs, cls.getDeclaredFields());
		if (cls.getSuperclass() != null) {
			Class<?> clsSup = cls.getSuperclass();
			fs = getBeanFields(clsSup, fs);
		}
		return fs;
	}

	private static Method getMethodValue(String str, Object obj, Class<?> cls,
			Class<?> clsType) throws Exception {
		Method method = null;
		if (cls.getSuperclass() != null) {
			try {
				Class<?> clsSup = cls.getSuperclass();
				if (clsType != null) {
					method = clsSup.getDeclaredMethod(str, clsType);
				} else {
					method = clsSup.getDeclaredMethod(str);
				}
			} catch (NoSuchMethodException e) {
			}
		}
		if (method == null) {
			if (clsType != null) {
				method = cls.getDeclaredMethod(str, clsType);
			} else {
				method = cls.getDeclaredMethod(str);
			}
		}
		return method;
	}

	/**
	 * 返回由对象的属性为key,值为map的value的Map集合
	 * 
	 * @param obj
	 *            Object
	 * @return mapValue Map<String,String>
	 * @throws Exception
	 */
	public static Map<String, Object> objectToMap(Object obj) {
		Map<String, Object> mapValue = new HashMap<String, Object>();
		Class<?> cls = obj.getClass();
		Field[] fields = getBeanFields(cls, new Field[0]);
		for (Field field : fields) {
			try {
				String name = field.getName();
				String strGet = "get" + name.substring(0, 1).toUpperCase()
						+ name.substring(1, name.length());
				Method method = getMethodValue(strGet, obj, cls, null);
				Object object = method.invoke(obj);
				if (object == null) {
					continue;
				}
				String value = object.toString();
				mapValue.put(name, value);
			} catch (Exception e) {
				continue;
			}
		}
		return mapValue;
	}

	/**
	 * 返回由Map的key对属性，value对应值组成的对应
	 * 
	 * @param map
	 *            Map<String,String>
	 * @param cls
	 *            Class
	 * @return obj Object
	 * @throws Exception
	 */
	public static Object mapToObject(Map<String, Object> map, Class<?> cls)
			throws Exception {
		Field[] fields = getBeanFields(cls, new Field[0]);
		Object obj = cls.newInstance();
		for (Field field : fields) {
			try {
				Class<?> clsType = field.getType();
				String name = field.getName();
				String strSet = "set" + name.substring(0, 1).toUpperCase()
						+ name.substring(1, name.length());

				Method method = getMethodValue(strSet, obj, cls, clsType);
				if (map.containsKey(name)) {
					Object value = map.get(name);
					if (value==null) {
						continue;
					}
					Object objValue = typeConversion(clsType, value.toString());
					if (objValue == null) {
						continue;
					}
					method.invoke(obj, objValue);
				}
			} catch (Exception e) {
				continue;
			}
		}
		return obj;
	}

	@SuppressWarnings("deprecation")
	private static Object typeConversion(Class<?> cls, String str) {
		Object obj = null;
		String nameType = cls.getSimpleName();
		if (!"String".equals(nameType)
				&& (str == null || str.trim().equals(""))) {
			return null;
		}
		if ("String".equals(nameType)) {
			obj = str;
		} else if ("Integer".equalsIgnoreCase(nameType)) {
			obj = Integer.valueOf(str);
		} else if ("int".equalsIgnoreCase(nameType)) {
			obj = Integer.valueOf(str);
		} else if ("Float".equalsIgnoreCase(nameType)) {
			obj = Float.valueOf(str);
		} else if ("Double".equalsIgnoreCase(nameType)) {
			obj = Double.valueOf(str);
		} else if ("Boolean".equalsIgnoreCase(nameType)) {
			obj = Boolean.valueOf(str);
		} else if ("Long".equalsIgnoreCase(nameType)) {
			obj = Long.valueOf(str);
		} else if ("Short".equalsIgnoreCase(nameType)) {
			obj = Short.valueOf(str);
		} else if ("Character".equalsIgnoreCase(nameType)) {
			obj = str.charAt(1);
		} else if ("BigInteger".equalsIgnoreCase(nameType)) {
			obj = BigInteger.valueOf(Long.valueOf(str));
		} else if ("BigDecimal".equalsIgnoreCase(nameType)) {
			obj = new BigDecimal(str);
		} else if ("Byte".equalsIgnoreCase(nameType)) {
			obj = Byte.valueOf(str);
		} else if ("Date".equalsIgnoreCase(nameType)) {
			obj = new Date(Date.parse(str));
		} else {
			System.out.println(nameType + "=" + str);
		}
		return obj;
	}
	
	/**
	 * byte数组转base64
	 * @param data
	 * @return
	 */
	public static String byteToBase64(byte[] data){
		try {
			return Base64.getEncoder().encodeToString(data);
		} catch (Exception e) {
			logger.error(convertLog(Thread.currentThread().getStackTrace()[1].getMethodName(), "exception"),e);
		}
		return null;
	}
	
	/**
	 * base64转byte数组
	 * @param data
	 * @return
	 */
	public static byte[] base64ToByte(String data){
		try {
			return Base64.getDecoder().decode(data);
		} catch (Exception e) {
			logger.error(convertLog(Thread.currentThread().getStackTrace()[1].getMethodName(), "exception"),e);
		}
		return null;
	}
	/**
	 * @Description: 判断String类型数字是否大于零
	 * @auther: huangsx
	 * @date:  2018/12/19 9:28
	 * @param: [num]
	 * @return: java.lang.Boolean
	 */
	public static Boolean isMoreThanZero(String n) {
		int num = new BigDecimal(n).compareTo(BigDecimal.ZERO);
		if (num == 1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 将数字转换百分比
	 * @param n
	 * @return
	 */
	public static String NumToPercentStr(double n,int num) {
		NumberFormat percent = NumberFormat.getPercentInstance();
		percent.setMaximumFractionDigits(num);//设置数值的【小数部分】允许的最大位数
		return  percent.format(n);
	}

	public static String getClassPathDir(){
		String pathDir = "";
		try {
			String path = System.getProperty("java.class.path");
			int firstIndex = path.lastIndexOf(System.getProperty("path.separator")) + 1;
			int lastIndex = path.lastIndexOf(File.separator) + 1;
			pathDir = path.substring(firstIndex, lastIndex);
		}catch (Exception e){

		}
		return pathDir;
	}


	public static byte[] readFileData(String filePath){
		byte[] fileData = null;
		File fin;
		FileInputStream fis = null;
		try {
			fin = new File(filePath);
			if (fin != null && fin.exists() && fin.isFile()) {
				fis = new FileInputStream(fin);
				int fileSize = (int)fin.length();
				if(fileSize > 0) {
					fileData = new byte[fileSize];
					fis.read(fileData);
				}
			}else {
				logger.info("fin is {}, fin.exists()={}, fin.isFile()={}",fin != null ? "not null":"null",fin.exists(), fin.isFile());
			}
		}catch (Exception e) {
			logger.info(e.toString());
		}finally {
			if(fis != null) {
				try{
					fis.close();
				} catch(Exception e2) {

				}
			}
		}
		return  fileData;
	}

	public static String readBase64FileData(String filePath){
		//logger.info("readBase64FileData: {}",filePath);
		byte[] fileData = readFileData(filePath);
		if(fileData != null){
			return Base64.getEncoder().encodeToString(fileData);
		}
		return null;
	}

	public static void deleteFile(String filePath)
	{
		File file = new File(filePath);
		file.delete();
	}
}
