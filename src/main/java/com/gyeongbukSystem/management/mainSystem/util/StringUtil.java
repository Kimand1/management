package com.gyeongbukSystem.management.mainSystem.util;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.safety.Whitelist;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nimbusds.jose.shaded.json.JSONObject;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 문자열관련 기능을 모은 클래스
 *
 * @author 김흥욱
 * @since 2014.03.11
 * @version 1.0
 * @see
 *
 *      <pre>
 * << 개정이력(Modification Information) >>
 *
 *  수정일           수정자     수정내용
 *  ------------- -------- ---------------------------
 *  2014.03.11 김흥욱     최초 생성
 *  2014.06.05 김흥욱     배열에 key값이 존재 여부 확인 추가, html을 text로 변환 추가
 *  2014.06.07 김흥욱     cutOffUTF8String, availibleByteNum 메소드 추가
 *  2014.06.09 김흥욱     removeTag 메소드 추가
 *  2014.06.19 김흥욱     htmlSpecialChars 메소드 추가
 *  2015.04.21 김흥욱     nvlNum(String str), nvlNum(Object str) 메소드 추가
 *
 *      </pre>
 */
public class StringUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(StringUtil.class);

	 /**
	  * 모바일 체크
	  * @param request
	  * @return
	  */
	 public static boolean isMobile(HttpServletRequest request) {
	        String userAgent = request.getHeader("user-agent");
	        boolean mobile1 = userAgent.matches(".*(iPhone|iPod|Android|Windows CE|BlackBerry|Symbian|Windows Phone|webOS|Opera Mini|Opera Mobi|POLARIS|IEMobile|lgtelecom|nokia|SonyEricsson).*");
	        boolean mobile2 = userAgent.matches(".*(LG|SAMSUNG|Samsung).*");
	        if(mobile1 || mobile2) {
	            return true;
	        }
	        return false;
	}

	 /**
		 * XSS 방지 처리 @param String @return String @exception
		 */
		public static String unscript(String data) {
			if (data == null || "".equals(data.trim())) {
				return "";
			}

			String ret = data;

			ret = ret.replaceAll("<(S|s)(C|c)(R|r)(I|i)(P|p)(T|t)", "&lt;script");
			ret = ret.replaceAll("</(S|s)(C|c)(R|r)(I|i)(P|p)(T|t)", "&lt;/script");

			ret = ret.replaceAll("<(O|o)(B|b)(J|j)(E|e)(C|c)(T|t)", "&lt;object");
			ret = ret.replaceAll("</(O|o)(B|b)(J|j)(E|e)(C|c)(T|t)", "&lt;/object");

			ret = ret.replaceAll("<(A|a)(P|p)(P|p)(L|l)(E|e)(T|t)", "&lt;applet");
			ret = ret.replaceAll("</(A|a)(P|p)(P|p)(L|l)(E|e)(T|t)", "&lt;/applet");

			ret = ret.replaceAll("<(E|e)(M|m)(B|b)(E|e)(D|d)", "&lt;embed");
			ret = ret.replaceAll("</(E|e)(M|m)(B|b)(E|e)(D|d)", "&lt;embed");

			ret = ret.replaceAll("<(F|f)(O|o)(R|r)(M|m)", "&lt;form");
			ret = ret.replaceAll("</(F|f)(O|o)(R|r)(M|m)", "&lt;form");

			return ret;

		}

		/**
		 * 리턴문자를 br태그로 치환하여 돌려줌. @param String @return String @exception
		 */
		public static String nl2br(String str) {
			if (str == null)
				return "";
			str = str.replaceAll("\r\n", "<br/>");
			str = str.replaceAll("\n", "<br/>");
			str = str.replaceAll("\r", "<br/>");
			return str;
		}

		/**
		 * XSS 방지와 리턴문자 br 치환 @param String @return String @exception
		 */
		public static String xssbr(String str) {

			str = unscript(str);
			str = nl2br(str);

			return str;

		}

		/**
		 * 데이터가 비어있는지 유무를 판단하여 돌려줌 - isZeroCheck ( true : 0일때 return true ) @param
		 * Integer, boolean @return boolean @exception
		 */
		public static boolean isEmpty(Integer num, boolean isZeroCheck) {
			if (isZeroCheck) {
				if (num == null) {
					return true;
				} else {
					return num == 0 ? true : isEmpty(num);
				}
			} else {
				return isEmpty(num);
			}
		}

		/**
		 * 데이터가 비어있는지 유무를 판단하여 돌려줌 @param Integer @return boolean @exception
		 */
		public static boolean isEmpty(Integer num) {
			return isEmpty(String.valueOf(num));
		}

		/**
		 * 데이터가 비어있는지 유무를 판단하여 돌려줌 @param String @return boolean @exception
		 */
		public static boolean isEmpty(String str) {
			if (str == null)
				return true;
			return str.trim().length() > 0 ? false : true;
		}

		/**
		 * fill문자를 len의 길이만큼 앞에 붙여 돌려줌 @param int, int, String @return
		 * String @exception
		 */
		public static String zerofill(int num, int len, String fill) {
			return zerofill(String.valueOf(num), len, fill);
		}

		/**
		 * fill문자를 len의 길이만큼 앞에 붙여 돌려줌 @param String, int, String @return
		 * String @exception
		 */
		public static String zerofill(String str, int len, String fill) {
			int strLen = str.length();
			StringBuffer tmp = new StringBuffer();
			for (int LoopI = 0; LoopI < len - strLen; LoopI++) {
				tmp.append(fill);
			}
			tmp.append(str);
			return tmp.toString();
		}

		/**
		 * 응용어플리케이션에서 고유값을 사용하기 위해 시스템에서17자리의TIMESTAMP값을 구하는 기능
		 *
		 * @param
		 * @return Timestamp 값
		 * @exception MyException
		 * @see
		 */
		public static String getTimeStamp() {
			String rtnStr = null;
			// 문자열로 변환하기 위한 패턴 설정(년도-월-일 시:분:초:초(자정이후 초))
			String pattern = "yyyyMMddHHmmssSSS";
			try {
				SimpleDateFormat sdfCurrent = new SimpleDateFormat(pattern, Locale.KOREA);
				Timestamp ts = new Timestamp(System.currentTimeMillis());
				rtnStr = sdfCurrent.format(ts.getTime());
			}
			catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
			return rtnStr;
		}

		/**
		 * 스트링이 비어있으면 "" 있으면 입력 str 리턴
		 *
		 * @param str
		 * @return "" or str
		 */
		public static String nvl(String str) {
			if (isEmpty(str)) {
				return "";
			} else {
				return str;
			}
		}

		/**
		 * 스트링이 비어있다면 0을 리턴 스트링 값이 있다면 Integer형으로 변환해서 리턴 Integer로 변환중
		 * NumberFormatException이 발생되면 0을 리턴
		 *
		 * @param str
		 * @return 0 or Integer str
		 */
		public static int nvlNum(String str) {
			if (isEmpty(str)) {
				return 0;
			} else {
				try {
					return Integer.parseInt(str);
				} catch (NumberFormatException e) {
					e.printStackTrace();
					return 0;
				}
			}
		}

		/**
		 * 스트링이 비어있다면 0을 리턴 스트링 값이 있다면 Integer형으로 변환해서 리턴 Integer로 변환중
		 * NumberFormatException이 발생되면 0을 리턴
		 *
		 * @param obj
		 * @return 0 or Integer str
		 */
		public static int nvlNum(Object obj) {
			if (isEmpty(String.valueOf(obj))) {
				return 0;
			} else {
				try {
					return Integer.parseInt(String.valueOf(obj));
				} catch (NumberFormatException e) {
					e.printStackTrace();
					return 0;
				}
			}
		}

		/**
		 * 배열에 key값이 존재 여부 확인
		 *
		 * @param key
		 * @param array
		 * @return
		 */
		public static boolean isExistArray(String key, String[] array) {

			boolean result = false;

			if (array == null)
				return result;

			int arrayCnt = array.length;

			for (int i = 0; i < arrayCnt; i++) {
				if (key.equals(array[i])) {
					result = true;
					break;
				}
			}

			return result;

		}

		/**
		 * 배열에 key값이 존재 여부 확인
		 *
		 * @param key
		 * @param array
		 * @return
		 */
		public static String isExistArray(int key, String[] array) {

			return isExistArray(String.valueOf(key), array) ? "1" : "0";

		}

		/**
		 * html 을 text로 변환
		 *
		 * @param str
		 * @return
		 */
		public static String html2text(String str) {

			if (isEmpty(str)) {
				return str;
			} else {
				// HTML = HTML.replaceAll("&", "&amp;");
				str = str.replaceAll("<", "&lt;");
				str = str.replaceAll(">", "&gt;");
				// HTML = HTML.replaceAll("\"", "&quot;");
				// HTML = HTML.replaceAll("'", "&#39;");
				str = str.replaceAll("\n", "<br/>");
				// HTML = HTML.replaceAll(" ", "&nbsp;");
				return str;
			}

		}

		public static String htmlSpecialChars(String s) {

			if (StringUtils.isNotEmpty(s)) {
				s = s.replaceAll("&amp;", "&");
				s = s.replaceAll("&nbsp;", " ");
				s = s.replaceAll("&quot;", "\"");
				s = s.replaceAll("&#039;", "'");
				s = s.replaceAll("&lt;", "<");
				s = s.replaceAll("&gt;", ">");
				s = s.replaceAll("'", "&#039;");
				s = s.replaceAll("\"", "&quot;");
				s = s.replaceAll("<", "&lt;");
				s = s.replaceAll(">", "&gt;");
				s = s.replaceAll(" ", "&nbsp;");
				return s;
			} else {
				return new String();
			}

		}

		/**
		 * 문자열을 특정 byteSize에 맞게 자르고 뒤에 문자열(trail)을 붙여줌
		 *
		 * @param str
		 * @param maxByteSize
		 * @param trail
		 * @return
		 */
		public static String cutOffUTF8String(String str, int maxByteSize, String trail) {

			try {

				// 널일 경우에는 그냥 리턴
				if (str == null)
					return null;
				if (str.length() == 0)
					return str;

				byte strByte[] = str.getBytes("UTF-8");

				if (maxByteSize < 1 || strByte.length <= maxByteSize)
					return str;

				// 마지막 줄임말
				int trailByteSize = 0;

				// 줄임말의 바이트 수 계산
				if (trail != null)
					trailByteSize = trail.getBytes("UTF-8").length;

				// 실질적으로 포함되는 최대 바이트 수는 trailByte를 뺀 것이다.
				maxByteSize = maxByteSize - trailByteSize;

				int endPos = 0; // 마지막 바이트 위치
				int currByte = 0; // 현재까지 조사한 바이트 수

				for (int i = 0; i < str.length(); i++) {
					// 순차적으로 문자들을 가져옴.
					char ch = str.charAt(i);

					// 이 문자가 몇 바이트로 구성된 UTF-8 코드인지를 검사하여 currByte에 누적 시킨다.
					currByte = currByte + availibleByteNum(ch);

					// 현재까지 조사된 바이트가 maxSize를 넘는다면 이전 단계 까지 누적된 바이트 까지를 유효한 바이트로
					// 간주한다.
					if (currByte > maxByteSize) {
						endPos = currByte - availibleByteNum(ch);
						break;
					}
				}

				// 원래 문자열을 바이트로 가져와서 유효한 바이트 까지 배열 복사를 한다.
				byte newStrByte[] = new byte[endPos];

				System.arraycopy(strByte, 0, newStrByte, 0, endPos);

				String newStr = new String(newStrByte, "UTF-8");

				newStr += trail;

				return newStr;

			}
			catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			catch(IllegalArgumentException e) {
				e.printStackTrace();
			}
			catch(IndexOutOfBoundsException e) {
				e.printStackTrace();
			}
			return "";
		}

		/**
		 * 바이트 검사
		 *
		 * @param c
		 * @return
		 */
		public static int availibleByteNum(char c) {

			// UTF-8은 최대 4바이트를 사용하고 ASCII는 1바이트 그외의 문자들은 2~3바이트 까지 조합하여 사용한다.
			// 즉, 어느 나라 문자이냐에 따라서 몇 바이트를 사용하는지 모르기 때문에 하나의 charater가 몇 바이트 대역에
			// 있는지 조사하여 한문자의 바이트를 조사... 이를 더해 나가면 문자 단위로 몇 바이트를 차지 하는지 정확하게 조사할 수
			// 있다.
			int ONE_BYTE_MIN = 0x0000;
			int ONE_BYTE_MAX = 0x007F;

			int TWO_BYTE_MIN = 0x0800;
			int TWO_BYTE_MAX = 0x07FF;

			int THREE_BYTE_MIN = 0x0800;
			int THREE_BYTE_MAX = 0xFFFF;

			int SURROGATE_MIN = 0x10000;
			int SURROGATE_MAX = 0x10FFFF;

			int digit = (int) c;

			if (ONE_BYTE_MIN <= digit && digit <= ONE_BYTE_MAX)
				return 1;
			else if (TWO_BYTE_MIN <= digit && digit <= TWO_BYTE_MAX)
				return 2;
			else if (THREE_BYTE_MIN <= digit && digit <= THREE_BYTE_MAX)
				return 3;
			else if (SURROGATE_MIN <= digit && digit <= SURROGATE_MAX)
				return 4;

			return -1;

		}

		/**
		 * 태그를 제거한다
		 *
		 * @param str
		 * @return
		 */
		public static String removeTag(String str) {
			if (str == null) {
				return "";
			}

			int lt = str.indexOf("<");

			if (lt != -1) {
				int gt = str.indexOf(">", lt);
				if ((gt != -1)) {
					str = str.substring(0, lt) + str.substring(gt + 1);
					str = removeTag(str);
				}
			}

			StringUtils.replace(str, "&amp;nbsp;", " ");
			return str;

		}

		public static String urlEncode(String str, String enc) {
			String rt = str;
			if (str != null && enc != null) {
				try {
					rt = java.net.URLEncoder.encode(str, enc);
				}
				catch(UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				catch (NullPointerException e) {
					e.printStackTrace();
				}
			}
			return rt;
		}

		public static String urlEncode(String str) {
			String enc = PropResource.getString("site.charset", "utf8");
			return urlEncode(str, enc);
		}

		public static String urlDecode(String str) {
			String enc = PropResource.getString("site.charset", "utf8");
			return urlDecode(str, enc);
		}

		public static String urlDecode(String str, String enc) {
			if (isEmpty(str))
				return "";
			String rt = str;
			try {
				rt = URLDecoder.decode(str, enc);
			}
			catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			catch (NullPointerException e) {
				e.printStackTrace();
			}
			return rt;
		}

		public static String encodeURIComponent(String str, String enc) {
			String result = null;
			try {
				result = urlEncode(str, enc).replaceAll("\\+", "%20").replaceAll("\\%21", "!")
						.replaceAll("\\%27", "'").replaceAll("\\%28", "(").replaceAll("\\%29", ")")
						.replaceAll("\\%7E", "~");
			} catch (Exception e) {
				result = str;
			}
			
			return result;
		}

		public static boolean isHangul(char ch) {
			Character.UnicodeBlock block = Character.UnicodeBlock.of(ch);
			if (block == Character.UnicodeBlock.HANGUL_SYLLABLES || block == Character.UnicodeBlock.HANGUL_JAMO
					|| block == Character.UnicodeBlock.HANGUL_COMPATIBILITY_JAMO) {
				return true;
			}
			return false;
		}

		public static boolean isIncludeHangul(String s) {
			if (s == null)
				return false;

			char c;
			for (int i = 0, n = s.length(); i < n; i++) {
				c = s.charAt(i);
				if (isHangul(c)) {
					return true;
				}
			}
			return false;
		}

		public static DecimalFormat NUMBER_FORMAT = new DecimalFormat("#,###");

		public static String getCommaNumberFormat(Integer l) {
			String r = "";
			if (l != null) {
				r = NUMBER_FORMAT.format(l);
			}
			return r;
		}

		public static String getCommaNumberFormat(Double l) {
			String r = "";
			if (l != null) {
				r = NUMBER_FORMAT.format(l);
			}
			return r;
		}

		public static String getCommaNumberFormat(Long l) {
			String r = "";
			if (l != null) {
				r = NUMBER_FORMAT.format(l);
			}
			return r;
		}

		public static String b64Encode(String str) {
			String enc = PropResource.getString("site.charset", "utf8");

			return b64Encode(str, enc);
		}

		public static String b64Encode(String str, String enc) {
			String b64 = "";
			if (str != null) {
				try {
					b64 = new String(Base64.encodeBase64(str.getBytes(enc)));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			return b64;
		}

		public static String b64Decode(String str) {
			String enc = PropResource.getString("site.charset", "utf8");
			;

			return b64Decode(str, enc);
		}

		public static String b64Decode(String str, String enc) {
			String b64 = "";
			if (str != null) {
				try {
					b64 = new String(Base64.decodeBase64(str.getBytes(enc)));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			return b64;
		}

		public static String fileSizeUnit(String bytes) {
			String result = "";
			try {
				long lBytes = Long.valueOf(bytes);
				result = fileSizeUnit(lBytes);
			}
			catch (IllegalArgumentException e) {
				e.printStackTrace();
			}

			return result;
		}

		public static String fileSizeUnit(Long bytes) {

			String result = "";

			try {
				double lengthbyUnit = (double) bytes;
				int Unit = 0;
				while (lengthbyUnit > 1024 && Unit < 5) {
					// 단위 숫자로 나누고 한번 나눌 때마다 Unit 증가
					lengthbyUnit = lengthbyUnit / 1024;
					Unit++;
				}

				DecimalFormat df = new DecimalFormat("#,##0.00");

				result = df.format(lengthbyUnit);

				switch (Unit) {
				case 0:
					result += "Bytes";
					break;
				case 1:
					result += "KB";
					break;
				case 2:
					result += "MB";
					break;
				case 3:
					result += "GB";
					break;
				case 4:
					result += "TB";
				}
			}
			catch (IllegalArgumentException e) {
				e.printStackTrace();
			}


			return result;

		}


		public static String toSha256(String str) {
			String SHA = "";
			try{
				MessageDigest sh = MessageDigest.getInstance("SHA-256");
				sh.update(str.getBytes());
				byte byteData[] = sh.digest();
				StringBuffer sb = new StringBuffer();
				for(int i = 0 ; i < byteData.length ; i++){
					sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
				}
				SHA = sb.toString();

			}
			catch(NoSuchAlgorithmException e){
				e.printStackTrace();
			}
			catch(IndexOutOfBoundsException e) {
				e.printStackTrace();
			}
			return SHA;
		}

		public static String encrypt(String str) {
			String es = "";
			String crypto = str;
			try {
				//crypto = DBCryptUtil.Encrypt(str);
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			es = StringUtil.toSha256(crypto);
			return es;
		}

		/**
		 * 날짜계산(두 날짜의 차이)
		 *
		 * @param beginDate
		 * @param endDate
		 * @return
		 * @throws Exception
		 */
		public static long diffOfDate(String beginDate, String endDate) throws Exception {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

			Date beginDate1 = formatter.parse(beginDate);
			Date endDate1 = formatter.parse(endDate);

			long diff = endDate1.getTime() - beginDate1.getTime();
			long diffDays = diff / (24 * 60 * 60 * 1000);

			return diffDays;
		}

		public static String encodeUrl(String m) {
			return encodeUrl(m, "utf-8");
		}

		public static String encodeUrl(String m, String enc) {
			StringBuffer encMenuUrl = new StringBuffer();
			try {
				String u = StringUtils.substringBefore(m, "?");
				String p = StringUtils.substringAfter(m, "?");
				String[] pa = StringUtils.splitByWholeSeparator(p, "&");

				encMenuUrl.append(u).append("?");

				for (String prs : pa) {
					String k = StringUtils.substringBefore(prs, "=");
					String v = StringUtils.substringAfter(prs, "=");
					if (StringUtils.isNotBlank(k) && StringUtils.isNotEmpty(v)) {
						encMenuUrl.append(k).append("=").append(StringUtil.urlEncode(v, enc)).append("&");
					}
				}
			}
			catch(NullPointerException e) {
				//LoggingUtil.ignore(e);
				e.printStackTrace();
			}

			return encMenuUrl.toString();
		}

		public static String stripBody(String s) {
			String rtn = s;
			if (rtn == null) {
				return "";
			}

			if (rtn.indexOf("<BODY>") != -1) {
				rtn = StringUtils.substringAfter(rtn, "<BODY>");
			}
			if (rtn.indexOf("</BODY>") != -1) {
				rtn = StringUtils.substringBefore(rtn, "</BODY>");
			}
			if (rtn.indexOf("<body>") != -1) {
				rtn = StringUtils.substringAfter(rtn, "<body>");
			}
			if (rtn.indexOf("</body>") != -1) {
				rtn = StringUtils.substringBefore(rtn, "</body>");
			}
			return rtn;
		}

		public static Pattern SPECIAL_SYMBOL_PATTERN = Pattern.compile(
				"[\u2000-\u2bff\u3000-\u303f\u3200-\u337f\u33de-\u33ff\uff00-\uffff]", Pattern.DOTALL | Pattern.MULTILINE);

		public static String removeSpecialSymbols(String s) {
			String r = s;
			r = SPECIAL_SYMBOL_PATTERN.matcher(s).replaceAll(" ");
			return r;
		}

		public static String getPrecisionNumber(Double num, Integer precision) {
			String formatted = String.valueOf(num);
			try {
				String frm = "##";
				if (precision > 0) {
					frm += ".";
					for (int i = 0; i < precision; i++) {
						frm += "#";
					}
				}

				LOGGER.debug("num = " + num + ", frm = " + frm);

				DecimalFormat df = new DecimalFormat(frm);
				formatted = df.format(num);
			}
			catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
			return formatted;
		}


		public static String removeCRLF(String s) {
			return StringUtils.defaultIfEmpty(s, "").replaceAll("\r", "").replaceAll("\n", "");
		}


		public static final Whitelist myWhitelist = Whitelist.relaxed()
				.preserveRelativeLinks(true)
				.addAttributes("div", "class", "style")
				.addAttributes("dl", "class", "style")
				.addAttributes("ul", "class", "style")
				.addAttributes("ol", "class", "style")
				.addAttributes("li", "class", "style")
				.addAttributes("table", "class", "style")
				.addAttributes("p", "class", "style")
				.addAttributes("span", "class", "style")
				.addAttributes("img", "class", "style")
				.addAttributes("a", "class", "style", "target")
				.addAttributes("h1", "class", "style")
				.addAttributes("h2", "class", "style")
				.addAttributes("h3", "class", "style")
				.addAttributes("h4", "class", "style")
				.addAttributes("h5", "class", "style")
				.addAttributes("h6", "class", "style")
				.addProtocols("img", "src", "data", "cid")
				;

		public static final String[] whitelistOfStyle = new String[] { "color", "font-size", "font-weight", "line-height", "letter-spacing", "text-align", "padding-left"  };


		public static String makeCleanCn(String html) {
			return html;/*
			String source = Jsoup.clean(html.toString(), "", myWhitelist);
			Document doc = Jsoup.parse(source, "", Parser.htmlParser());

			// style 속성을 가지고 있는 element를 select
			Elements elements = doc.select("*[style]");
			Elements emptyStyleElements = new Elements();
			if(elements != null) {
				for(Element e : elements) {

					// style속성을 map으로
					String[] styles = e.attr("style").split(";");

					Map<String, String> styleMap = new HashMap<String, String>();
					for(String s : styles) {
						String[] sv = s.split(":");
						// key/value 쌍으로 값이 적절하며 whitelistOfStyle에 지정된 값만 map에 put
						if(sv.length == 2) {
							String key = sv[0].trim();
							String val = sv[1].trim();
							if(!"".equals(key) && !"".equals(val)) {
								if(ArrayUtils.contains(whitelistOfStyle, key)) {
									styleMap.put(key, val);
								}
							}
						}
					}

					// 적법한 style 이 없는 경우 style속성 삭제 대상 element에 추가
					if(styleMap.size() < 1) {
						emptyStyleElements.add(e);
					}
					// map을 style string으로 join하여 attribute에 재설정
					else {
						StringBuffer cleanStyles = new StringBuffer();
						Iterator<String> keys = styleMap.keySet().iterator();
				        while (keys.hasNext()) {
				            String key = keys.next();
				            cleanStyles.append(key).append(":").append(styleMap.get(key)).append(";");
				        }
						e.attr("style", cleanStyles.toString());
					}
				}
			}

			// style 속성 삭제 대상 element 의 style속성을 제거
			if(emptyStyleElements.size() > 0) {
				for(Element e : emptyStyleElements) {
					try {
						e.removeAttr("style");
					}
					catch(java.util.ConcurrentModificationException ex) {
						LoggingUtil.ignore(ex);
					}
				}
			}

			//return doc.toString();
			return doc.body().children().toString();
			*/
		}


		public static String replacePhoneNum(String phoneNumber) {
			if(phoneNumber == null) {
				return "";
			}
			String regEx = "(\\d{3})(\\d{3,4})(\\d{4})";
			if(!Pattern.matches(regEx, phoneNumber)) {
				return phoneNumber;
			}
			return phoneNumber.replaceAll(regEx, "$1-$2-$3");
		}

		public static String parsePhoneNumber(String phoneNumber) {
			return replacePhoneNum(phoneNumber);
	    }

		/**
	     * 전화번호 형태로 변경한다
	     * 0212341234  -> 02-1234-1234  10
	     * 021234567   -> 02-123-4567   9
	     * 04312341234 -> 043-1234-1234 11
	     * 0431234567  -> 043-123-4567  10
	     * @param telNo
	     * @return
	     */
	    public static String parseTelNumber(String telNo) {
	        String regEx = "(\\d{2,3})(\\d{3,4})(\\d{4})";
	        if(!Pattern.matches(regEx, telNo)) return "";

	        if(telNo.length() == 11) {
	            return String.format("%s-%s-%s", telNo.substring(0,3),telNo.substring(3,7),telNo.substring(7));
	        }else if(telNo.length() == 9) {
	            return String.format("%s-%s-%s", telNo.substring(0,2),telNo.substring(2,5),telNo.substring(5));
	        }else if(telNo.length() == 10) {
	            // 02로 시작할경우와 3글자로 시작하는경우가 있다
	            // 위에 예제를 보면 알 수 있다.
	            if(telNo.startsWith("02")) {
	                return String.format("%s-%s-%s", telNo.substring(0,2),telNo.substring(2,6),telNo.substring(6));
	            }else {
	                return String.format("%s-%s-%s", telNo.substring(0,3),telNo.substring(3,6),telNo.substring(6));
	            }
	        }


	        return telNo;
	    }
	    public static enum PatternType {NUMBER, EMAIL, TEL, MOBILE};
	    public static final String REGEX_NUMBER = "^[0-9]*$";
	    public static final String REGEX_EMAIL = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
	    public static final String REGEX_TEL = "^\\d{2,3}-\\d{3,4}-\\d{4}$";
	    public static final String REGEX_MOBILE = "^01(?:0|1[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$";

		public static boolean checkPattern(PatternType ty, String str) {
			boolean okPattern = false;

			String regex = null;

			if (PatternType.NUMBER == ty) {
				regex = REGEX_NUMBER;
			}
			else if (PatternType.EMAIL == ty) {
				regex = REGEX_EMAIL;
			}
			else if (PatternType.TEL == ty) {
				regex = REGEX_TEL;
			}
			else if (PatternType.MOBILE == ty) {
				regex = REGEX_MOBILE;
			}


			if (regex != null && str != null) {
				okPattern = Pattern.matches(regex, str);
			}

			return okPattern;
		}
	    /**
		  * Client IP 확인
		  * @param request
		  * @return
		  */
		 public static String getClientIpAddr(HttpServletRequest request) {
			    String ip = request.getHeader("X-Forwarded-For");

			    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			        ip = request.getHeader("Proxy-Client-IP");
			    }
			    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			        ip = request.getHeader("WL-Proxy-Client-IP");
			    }
			    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			        ip = request.getHeader("HTTP_CLIENT_IP");
			    }
			    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			        ip = request.getHeader("HTTP_X_FORWARDED_FOR");
			    }
			    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			        ip = request.getRemoteAddr();
			    }

			    return ip;
		}

		public static String intToStr(int cnt){
			String rtn = "";

			try{
				rtn = Integer.toString(cnt);
			}catch(Exception e){
				return "";
			}

			return rtn;
		}

		/**
		  * 문자 암호화
		  * @param val
		  * @return
		  */
		 public static String encryptCEV(String val){
			 String rtn = "";
			 val = StringUtil.strTrim(val);

			 try{
				 if(!"".equals(val)){
					 	// 암호화 함수 사용시 대비용
					 	//System.out.println("암호화===>" + val);
					 	//rtn = SDBCrypto.encryptCEV("dbsec","api_key","aria256",val);
				 }
			 }catch(Exception e){
				 //e.printStackTrace();
				 rtn = val;
			 }
			 return rtn;
		 }

		 /**
		  * 문자 복호화
		  * @param val
		  * @return
		  */
		 public static String decryptCEV(String val){
			 String rtn = "";
			 val = StringUtil.strTrim(val);

			 try{
				 if(!"".equals(val)){
					 // 복호화 함수 사용시 대비용
					 // rtn = SDBCrypto.decryptCEV("dbsec","api_key","aria256",val);
				 }
			 }catch(Exception e){
				 rtn = val;
			 }

			 return rtn;
		 }

		 /**
		     * 문자열에 공백 제거
		     * @param val
		     * @return
		     */
		    public static String strTrim(String val){
		    	String value = "";

		    	val = StringUtil.isNull(val);

		    	if(val != null && !"null".equals(val)){
		    		value = val.trim();
		    	}else{
		    		value = "";
		    	}

		    	return value;
		    }

			 /**
		     * 문자열 공백 확인 후 Replace
		     * @param val
		     * @return
		     */
		    public static String strReplaceALL(String val, String val2, String val3){
		    	String value = "";

		    	try{
			    	value = StringUtil.strTrim(val);
			    	if(!"".equals(value)){
			    		value = value.replaceAll(val2, val3);
			    	}
		    	}catch (Exception e) {
		    		value = val;
				}

		    	return value;
		    }

		    /**
		     * 문자열이 널일 경우 공백으로 처리
		     * @param val
		     * @return
		     */
		    public static String isNull(String val){

				String value = val;

				if(value == null){
					return "";
				}

				return value;
			}
		    
		    /**
		     * 문자열이 널일 경우 대체문자 제공
		     * @param val
		     * @return
		     */
		    public static String convertNullStr(String val , String str){

				String value = val;

				if(value == null || "".equals(strTrim(value)  ) ||   "null".equals(strTrim(value).toLowerCase()  )  ){
					return str ;
				}

				return value;
			}

		    /**
		     * Integer 형 널 체크 하여 널일때 0 반환
		     * @param val
		     * @return
		     */
		    public static int intNullCheck(Integer val){
		    	if(val == null){
		    		return 0;
		    	}else{
		    		return val;
		    	}
		    }


		    /**
		   	 * 천단위 콤마찍어주는 포맷
		   	 * @param val
		   	 * @return
		   	 */
		   	@SuppressWarnings("finally")
		   	public static String getFormatData(String val){

		   		if("".equals(StringUtil.strTrim(val))){
		   			return "0";
		   		}

		   		String formatVal = "";
		   		try {
		   			DecimalFormat df = new DecimalFormat("###,###");
		   			formatVal = df.format(Double.parseDouble(val));
		   		} catch (Exception e) {
		   			formatVal = val;
		   		} finally {
		   			return formatVal;
		   		}
		   	}


			/**
			 * 천단위 콤마찍어주는 포맷
			 * @param val (int 형)
			 * @return String
			 */
			public static String getFormatComma(int val){
				String formatVal = "";
				String rtn = "";

				try {
					rtn = intToStr(val);

					DecimalFormat df = new DecimalFormat("###,###");
					formatVal = df.format(Double.parseDouble(rtn));

					return formatVal;
				} catch (Exception e) {
					return "0";
				}
			}

			public static String getFormatCommaIngeger(Integer val){
				String formatVal = "";
				String rtn = "";

				try {
					rtn = intToStr(val);

					DecimalFormat df = new DecimalFormat("###,###");
					formatVal = df.format(Double.parseDouble(rtn));

					return formatVal;
				} catch (Exception e) {
					return "0";
				}
			}

			/**
			  * 암호화 함수
			  * @param text
			  * @param key
			  * @return
			  * @throws Exception
			  */
		     public static String Encrypt(String text, String key) throws Exception{
		               Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		               byte[] keyBytes= new byte[16];
		               byte[] b= key.getBytes("UTF-8");
		               int len= b.length;

		               if (len > keyBytes.length) len = keyBytes.length;

		               System.arraycopy(b, 0, keyBytes, 0, len);
		               SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
		               IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);

		               cipher.init(Cipher.ENCRYPT_MODE,keySpec,ivSpec);

		               byte[] results = cipher.doFinal(text.getBytes("UTF-8"));
		               BASE64Encoder encoder = new BASE64Encoder();
		               return encoder.encode(results);
		     }

		     /**
			  * 복호화 함수
			  * @param text
			  * @param key
			  * @return
			  * @throws Exception
			  */
			 public static String Decrypt(String text, String key) throws Exception{
		           Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		           byte[] keyBytes= new byte[16];
		           byte[] b= key.getBytes("UTF-8");
		           int len= b.length;

		           if (len > keyBytes.length) len = keyBytes.length;

		           System.arraycopy(b, 0, keyBytes, 0, len);
		           SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
		           IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
		           cipher.init(Cipher.DECRYPT_MODE,keySpec,ivSpec);

		           BASE64Decoder decoder = new BASE64Decoder();
		           byte [] results = cipher.doFinal(decoder.decodeBuffer(text));

		           return new String(results,"UTF-8");
		     }

			 /**
				 * 문자열을 숫자로 파싱
				 * @param cnt
				 * @return
				 */
			public static int strToInt(String cnt){
					try{
						if(!"".equals(StringUtil.strTrim(cnt))){
							return Integer.parseInt(cnt);
						}else{
							return 0;
						}

					}catch(Exception e){
						return 0;
					}
			}

			public static boolean isNaN(String value) {
		    	if(value == null) {
		    		return true;
		    	}

		    	String reg = "^[0-9]*$";
		        Matcher matcher = Pattern.compile(reg).matcher(value);
		        boolean result = true;

		        if(!matcher.matches()) {
		            result = false;
		        }

		        // 공백이 존재하는지 체크
		        if(value.indexOf(" ") > 0) {
		            result = false;
		        }

		        return result;
		    }

			/**
			 * md5으로 암호화 한다
			 *
			 * @param str
			 * @return
			 * @throws NoSuchAlgorithmException
			 * @throws InvalidKeyException
			 * @throws BadPaddingException
			 * @throws IllegalBlockSizeException
			 */
			public static String toMd5(String str)
					throws NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

				MessageDigest di = MessageDigest.getInstance("MD5");
				di.update(new String(str).getBytes());
				byte[] md5Code = di.digest();
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < md5Code.length; i++) {
					String md5Char = String.format("%02x", 0xff & (char) md5Code[i]);
					sb.append(md5Char);
				}

				return sb.toString();

			}

			/**
			 * 문자열 더블로 파싱
			 *
			 * @param cnt
			 * @return
			 */
			public static double strToDouble(String cnt) {
				try {
					if (!"".equals(StringUtil.strTrim(cnt))) {
						return Double.parseDouble(cnt);
					} else {
						return 0;
					}

				} catch (Exception e) {
					return 0;
				}
			}

			/**
			 * 문자열 자르기 널일 경우 공백 리턴
			 *
			 * @param val
			 * @param startLength
			 * @param endLength
			 * @return
			 */
			public static String strSubString(String val, int startLength, int endLength) {
				String rtn = "";

				try {
					val = StringUtil.isNull(val);
					if (!"".equals(StringUtil.strTrim(val))) {
						int valLen = val.length();

						if (valLen >= endLength) {
							rtn = val.substring(startLength, endLength);
						} else {
							rtn = val;
						}
					} else {
						rtn = "";
					}
				} catch (Exception e) {
					rtn = "";
				}

				return rtn;
			}

			/**
			 * String Byte 체크
			 *
			 * @param str
			 * @return
			 */
			public static int getStringByte(String str) {
				int rtnInt = 0;

				try {
					if (!"".equals(StringUtil.strTrim(str))) {
						rtnInt = str.getBytes("euc-kr").length;
					} else {
						rtnInt = -99;
					}
				} catch (Exception e) {
					rtnInt = -99;
				}

				return rtnInt;
			}

			/**
			 * 나이 계산 프로그램
			 *
			 * @param birthYear
			 * @param birthMonth
			 * @param birthDay
			 * @param gubun
			 *            (1:만 나이, 2: 나이)
			 * @return
			 */
			public static int getAge(int birthYear, int birthMonth, int birthDay, String gubun) {
				Calendar current = Calendar.getInstance();
				int currentYear = current.get(Calendar.YEAR);
				int currentMonth = current.get(Calendar.MONTH) + 1;
				int currentDay = current.get(Calendar.DAY_OF_MONTH);

				int age = currentYear - birthYear;
				// 생일 안 지난 경우 -1
				if ("1".equals(gubun)) {
					if (birthMonth * 100 + birthDay > currentMonth * 100 + currentDay)
						age--;
				}

				return age;
			}

			/**
			 * 생일로 나이구하기
			 *
			 * @param day
			 * @param gubun
			 *            (1:만나이, 2:나이)
			 * @return
			 */
			public static int getAge(String day, String gubun) {
				int age = 0;

				day = StringUtil.strReplaceALL(day, "-", "");
				day = StringUtil.strReplaceALL(day, "\\.", "");

				String birthYearStr = StringUtil.strSubString(day, 0, 4);
				String birthMonthStr = StringUtil.strSubString(day, 4, 6);
				String birthDayStr = StringUtil.strSubString(day, 6, 8);

				int birthYear = StringUtil.strToInt(birthYearStr);
				int birthMonth = StringUtil.strToInt(birthMonthStr);
				int birthDay = StringUtil.strToInt(birthDayStr);

				Calendar current = Calendar.getInstance();
				int currentYear = current.get(Calendar.YEAR);
				int currentMonth = current.get(Calendar.MONTH) + 1;
				int currentDay = current.get(Calendar.DAY_OF_MONTH);

				age = currentYear - birthYear;

				// 생일 안 지난 경우 -1
				if ("1".equals(gubun)) {
					if (birthMonth * 100 + birthDay > currentMonth * 100 + currentDay)
						age--;
				}

				return age;
			}

			/**
			 * 나이로 학년 구하기
			 *
			 * @param day
			 * @param gubun
			 * @return
			 */
			public static int getSchoolYear(String day, String gubun) {
				int age = StringUtil.getAge(day, gubun);

				if (age == 8) {
					age = 1; // 초등학교 1학년
				} else if (age == 9) {
					age = 2; // 초등학교 2학년
				} else if (age == 10) {
					age = 3; // 초등학교 3학년
				} else if (age == 11) {
					age = 4; // 초등학교 4학년
				} else if (age == 12) {
					age = 5; // 초등학교 5학년
				} else if (age == 13) {
					age = 6; // 초등학교 6학년
				} else if (age == 14) {
					age = 7; // 중학교 1학년
				} else if (age == 15) {
					age = 8; // 중학교 2학년
				} else if (age == 16) {
					age = 9; // 중학교 3학년
				} else if (age == 17) {
					age = 10; // 고등학교 1학년
				} else if (age == 18) {
					age = 11; // 고등학교 2학년
				} else if (age == 19) {
					age = 12; // 고등학교 3학년
				}

				return age;
			}

			/**
			 * 전화번호 자리수에 따른 포맷팅
			 *
			 * @param str
			 *            ("xxx-xxx-xxxx or xxx-xxxx-xxxx")
			 * @return
			 */
			public static String getTelNoMask(String StringNum) {

				String sumStr = "";
				String resultNum = StringUtil.strTrim(StringNum);
				resultNum = StringUtil.strReplaceALL(resultNum, "-", "");
				resultNum = StringUtil.strReplaceALL(resultNum, "\\.", "");

				try {
					if (resultNum.length() == 9) {
						sumStr = resultNum.substring(0, 2) + "-" + resultNum.substring(2, 5) + "-" + resultNum.substring(5, 9);
					} else if (resultNum.length() == 10) {
						sumStr = resultNum.substring(0, 3) + "-" + resultNum.substring(3, 6) + "-" + resultNum.substring(6, 10);
					} else if (resultNum.length() == 11) {
						sumStr = resultNum.substring(0, 3) + "-" + resultNum.substring(3, 7) + "-" + resultNum.substring(7, 11);
					} else {
						sumStr = StringNum;
					}
				} catch (Exception e) {
					sumStr = StringNum;
				}

				return sumStr;
			}

			/**
			 * List<?> jsonList 형태로 변환
			 *
			 * @param List<?>
			 *            resultList
			 * @return List<JSONObject>
			 */
			@SuppressWarnings({ "unchecked", "rawtypes" })
			public static List<JSONObject> jsonParserList(List<?> resultList) {

				JSONObject tmpJson;
				List<JSONObject> jsonList = new ArrayList();

				try {
					for (Object obj : resultList) {
						tmpJson = new JSONObject();
						for (Field field : obj.getClass().getDeclaredFields()) {
							field.setAccessible(true);
							tmpJson.put(field.getName(), field.get(obj));
						}
						jsonList.add(tmpJson);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				return jsonList;
			}

			/**
			 * TextArea 에 입력한 데로 화면 보이기
			 *
			 * @param str
			 * @return
			 */
			public static String newLineReplace(String str) {
				String rtn = "";

				try {
					if (!"".equals(StringUtil.strTrim(str))) {
						rtn = str.replaceAll("\n", "<br/>").replaceAll("\u0020", "&nbsp;");
					} else {
						rtn = str;
					}
				} catch (Exception e) {
					rtn = str;
				}

				return rtn;
			}

			/**
			 * 엔터 리플레이스
			 *
			 * @param str
			 * @return
			 */
			public static String enterReplace(String str) {
				String rtn = "";

				try {
					if (!"".equals(StringUtil.strTrim(str))) {
						rtn = StringUtil.strReplaceALL(str, System.getProperty("line.separator"), "");
						rtn = StringUtil.strReplaceALL(str, "\\n", "");
						rtn = StringUtil.strReplaceALL(str, "\\r\\n", "");
					}
				} catch (Exception e) {
					rtn = str;
				}

				return rtn;
			}

			public static String enterReplace2(String str) {
				try {
					if (!"".equals(StringUtil.strTrim(str))) {
						str = str.replaceAll("\\\\n\\\\n", "<br />");
						str = str.replaceAll("\\\n\\\n", "<br />");
						str = str.replaceAll("\\n\\n", "<br />");
						str = str.replaceAll("\n\n", "<br />");

						str = str.replaceAll("\\\\r\\\\n", "<br />");
						str = str.replaceAll("\\\r\\\n", "<br />");
						str = str.replaceAll("\\r\\n", "<br />");
						str = str.replaceAll("\r\n", "<br />");

						str = str.replaceAll("\\\\n", "<br />");
						str = str.replaceAll("\\\\r", "<br />");
						str = str.replaceAll("\\\n", "<br />");
						str = str.replaceAll("\\\r", "<br />");
						str = str.replaceAll("\\n", "<br />");
						str = str.replaceAll("\\r", "<br />");
						str = str.replaceAll("\n", "<br />");
						str = str.replaceAll("\r", "<br />");

						 str = str.replaceAll(System.getProperty("line.separator") +
						 System.getProperty("line.separator"), "<br />");
						 str = str.replaceAll(System.getProperty("line.separator"), "<br />");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}


				return str;
			}

			public static String enterReplace3(String str) {
				try {
					if (!"".equals(StringUtil.strTrim(str))) {
						str = str.replaceAll("\\\\n\\\\n", "");
						str = str.replaceAll("\\\n\\\n", "");
						str = str.replaceAll("\\n\\n", "");
						str = str.replaceAll("\n\n", "");

						str = str.replaceAll("\\\\r\\\\n", "");
						str = str.replaceAll("\\\r\\\n", "");
						str = str.replaceAll("\\r\\n", "");
						str = str.replaceAll("\r\n", "");

						str = str.replaceAll("\\\\n", "");
						str = str.replaceAll("\\\\r", "");
						str = str.replaceAll("\\\n", "");
						str = str.replaceAll("\\\r", "");
						str = str.replaceAll("\\n", "");
						str = str.replaceAll("\\r", "");
						str = str.replaceAll("\n", "");
						str = str.replaceAll("\r", "");
						str = str.replaceAll("\\'", "&#39;");

						 str = str.replaceAll(System.getProperty("line.separator") +
						 System.getProperty("line.separator"), "");
						 str = str.replaceAll(System.getProperty("line.separator"), "");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}


				return str;
			}

			/**
			 * html 태그 변환 치환 (&quot; &amp; &lt; &gt; &nbsp; 등)
			 *
			 * @param str
			 * @return
			 */
			public static String unescapeHtml3(String str) {
				String rtn = "";

				try {
					if (!"".equals(StringUtil.strTrim(str))) {
						rtn = StringEscapeUtils.unescapeHtml3(str);
					}
				} catch (Exception e) {
					rtn = str;
				}

				return rtn;
			}

			// DEXT5 에디터 전용 문자 치환
			public static String htmlDext5Char(String s) {

				if (StringUtils.isNotEmpty(s)) {
					s = s.replaceAll("'", "&#39;");

					return s;
				} else {
					return new String();
				}

			}
			
			/**
     * 구분자로 구분된 컬럼명을 카멜 표기법으로 바꿈   예)BBS_REF -> bbsRef
     * @param str 
     * @param seperator
     * @return
     */
    public static String getCamelNotation(String str, String seperator) {
        StringBuffer retVal = new StringBuffer();
        if(!StringUtils.isBlank(str)) {
            String[] strArr = str.split(seperator);
            for(int idx = 0 ; idx < strArr.length ; idx++) {
                if(idx == 0) {
                    retVal.append(strArr[idx].toLowerCase());
                }
                else {
                    retVal.append(strArr[idx].toUpperCase().charAt(0) + strArr[idx].toLowerCase().substring(1));
                }
            }
        }
        return retVal.toString();
    }
}
