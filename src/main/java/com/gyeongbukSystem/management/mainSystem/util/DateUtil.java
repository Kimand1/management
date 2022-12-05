package com.gyeongbukSystem.management.mainSystem.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
//import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;

import com.ibm.icu.util.ChineseCalendar;

/**
 * 날짜관련 기능을 모은 클래스
 *
 * @author 김흥욱
 * @since 2014.03.10
 * @version 1.0
 * @see
 *
 *      <pre>
 * << 개정이력(Modification Information) >>
 *
 *  수정일           수정자     수정내용
 *  ------------- -------- ---------------------------
 *  2014.03.10 김흥욱     최초 생성
 *  2014.06.05 김흥욱     현재 날짜에서 입력된 날짜를 더하는 메소드 추가
 *  2014.06.08 김흥욱     getDateTimeFormat 메소드 추가
 *
 * </pre>
 */
public class DateUtil {
	/**
	 * 현재 날짜와 시간을 param의 포멧 형태로 돌려준다.
	 *
	 * @param String
	 * @return String
	 * @exception
	 */

	public static String getNowDateTime() {
		return getNowDateTime("yyyyMMddHHmmss");
	}

	public static String getNowDateTime(String format) {
		String ndt = "";
		try {
			DateTime dt = DateTime.now();
			ndt = dt.toString(format);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return ndt;
	}

	public static int getThisMonthLastDay() {
		int lastDay = getYMDLastDay(DateTime.now().toString("yyyyMMdd"));
		return lastDay;
	}

	public static int getYMDLastDay(String dt) {
		int lastDay = 30;
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(toDate(dt, "yyyyMMdd").toDate());
			lastDay = cal.getActualMaximum(Calendar.DATE);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return lastDay;
	}


	public static DateTime toDate(String strDate, String strType) {
		DateTime resultDate = toDate(strDate, strType, Locale.KOREAN);
		return resultDate;
	}

	public static DateTime toDate(String strDate, String strType, Locale locale) {
		DateTime resultDate = null;
		try {
			boolean hasZone = strType.indexOf('Z') != -1 && !strType.matches("'([\\s]?)Z([\\s]?)'");
			int commaCnt = StringUtils.countMatches(strType, "'");
			String pattern = StringUtils.substring(strType, 0, StringUtils.length(strDate)+commaCnt);
			int length = StringUtils.replace(strType, "'", "").length();
			if(hasZone) {
				length = length + 5;
			}
			strDate = StringUtils.substring(strDate, 0, length);
			org.joda.time.format.DateTimeFormatter fmt = org.joda.time.format.DateTimeFormat.forPattern(pattern).withLocale(locale);
			resultDate = DateTime.parse(strDate, fmt);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return resultDate;
	}

	public static boolean isValidDate(String strDate, String strFormat) {
		boolean res = false;
		if(StringUtils.isNotBlank(strDate)) {
			try {
				SimpleDateFormat dateFormat = new SimpleDateFormat(strFormat);

	            // 날짜가 잘못들어오면 자동으로 잡아주는것을 무시함
	            dateFormat.setLenient(false);
	            dateFormat.parse(strDate);
				res = true;
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		return res;
	}

	public static String toDateFormat(String strDate, String strType1, String strType2) {
		return formatDispDate(strDate, strType1, strType2);
	}

	public static final String YMD_FMT = "yyyyMMdd";
	public static final String YMDHMS_FMT = "yyyyMMddHHmmss";

	public static String getDateAddDay(int day) {
		return getDateAddDay(getNowDateTime(YMDHMS_FMT), day);
	}

	public static String getDateAddDay(String dt, int day) {
		DateTime base = toDate(dt, YMDHMS_FMT);
		return getDateAddDayYMD(base, day, YMDHMS_FMT);
	}

	public static String getDateFmtAddDay(String dt, int day,String fmt) {
		DateTime base = toDate(dt.replace("-", "") , YMDHMS_FMT);
		return getDateAddDayYMD(base, day, fmt);
	}

	public static String getDateAddDay(String bgnde, int addDay, String gubun) {

		gubun = (gubun==null?"":gubun);
		int y = Integer.parseInt(bgnde.substring(0, 4));
		int m = Integer.parseInt(bgnde.substring(4, 6));
		int d = Integer.parseInt(bgnde.substring(6, 8));

		Calendar calendar = Calendar.getInstance();
		calendar.set(y, m-1, d);
		calendar.add(Calendar.DATE, addDay);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy" +gubun + "MM" +gubun+"dd");
		return sdf.format(calendar.getTime()).toString();

	}

	public static String getDateAddDayYMD(int day) {
		return getDateAddDayYMD(DateTime.now(), day, YMD_FMT);
	}

	public static String getDateAddDayYMD(String dt, int day) {
		DateTime base = toDate(dt, YMD_FMT);
		return getDateAddDayYMD(base, day, YMD_FMT);
	}

	public static String getDateAddDayYMD(DateTime dt, int day, String fmt) {
		return dt.plusDays(day).toString(fmt);
	}
	
	public static String getDateAddMonth(int month) {
		return getDateAddMonth(month,YMDHMS_FMT) ;
	}
	
	/**
	 *  현재시간에서 입력한 개월 수를 더함. 
	 * @param month  더해질 개월 수 
	 * @param fmt	 리턴 날짜형식
	 * @return
	 */
	public static String getDateAddMonth(int month , String fmt) {
		DateTime base = DateTime.now();
		return getDateAddMonth(base , month,fmt) ;
	}
	
	/**
	 * 기준일자에서 입력한 개월수를 더함
	 * @param dt  기준일
	 * @param month  더해질 개월 수 
	 * @param fmt 리턴 날짜형식
	 * @return
	 */
	public static String getDateAddMonth(DateTime dt, int month , String fmt) {
		return dt.plusMonths(month).toString(fmt);
	}
	
	/**
	 * 기준일자에서 입력한 개월수를 더함
	 * @param dt  기준일 
	 * @param month  더해질 개월 수 
	 * @param fmt 리턴 날짜형식
	 * @return
	 */
	public static String getStrDateAddMonth(String strDate, int month , String fmt) {
		DateTime  dt = toDate(strDate , fmt);		
		return getDateAddMonth(dt,month,fmt);
	}
	
	/**
	 *  기준일자에서 입력한 주 수를 더함. 
	 * @param weeks  더해질 주 수 
	 * @param fmt	 리턴 날짜형식
	 * @return
	 */
	public static String getDateAddWeek(DateTime dt, int weeks , String fmt) {	
		return   getDateAddDayYMD(dt , weeks*7, fmt); 
	}
	
	/**
	 *  현재시간에서 입력한 주 수를 더함. 
	 * @param weeks  더해질 주 수 
	 * @param fmt	 리턴 날짜형식
	 * @return
	 */
	public static String getDateAddWeek(int weeks , String fmt) {
		DateTime base = DateTime.now();
		return   getDateAddWeek(base, weeks , fmt); 
	}
	
	public static final int J_MONDAY = 1;
	public static final int J_TUESDAY = 2;
	public static final int J_WEDNESDAY = 3;
	public static final int J_THURSDAY = 4;
	public static final int J_FRIDAY = 5;
	public static final int J_SATURDAY = 6;
	public static final int J_SUNDAY = 7;

	public static final int SUNDAY = 1;
	public static final int MONDAY = 2;
	public static final int TUESDAY = 3;
	public static final int WEDNESDAY = 4;
	public static final int THURSDAY = 5;
	public static final int FRIDAY = 6;
	public static final int SATURDAY = 7;

	public static int getDayOfTheWeekYMD(String dt) {
		return getDayOfTheWeekYMD(dt, YMD_FMT);
	}

	public static int getDayOfTheWeekYMD(String dt, String fmt) {
		DateTime base = toDate(dt, fmt);
		int jDow = base.getDayOfWeek();
		int dow = jDow;
		switch(jDow) {
			case J_MONDAY : dow = MONDAY; break;
			case J_TUESDAY : dow = TUESDAY; break;
			case J_WEDNESDAY : dow = WEDNESDAY; break;
			case J_THURSDAY : dow = THURSDAY; break;
			case J_FRIDAY : dow = FRIDAY; break;
			case J_SATURDAY : dow = SATURDAY; break;
			case J_SUNDAY : dow = SUNDAY; break;
		}
		return dow;
	}

	public static String getFirstDateOfTheWeekYMD(String dt) {
		int day = getDayOfTheWeekYMD(dt) - 1;
		return getDateAddDayYMD(dt, -1 * day);
	}

	public static String getDateTimeFormat(Date date, String format) {
		return getDateTimeFormat(date, format, Locale.KOREAN);
	}

	public static String getDateTimeFormat(Date date, String format, Locale locale) {
		return getDateTimeFormat(new DateTime(date), format, locale);
	}

	public static String getDateTimeFormat(DateTime date, String format) {
		return getDateTimeFormat(date, format, Locale.KOREAN);
	}

	public static String getDateTimeFormat(DateTime date, String format, Locale locale) {
		return date.toString(format, locale);
	}

	public static String formatDispDate(String dt, String origFormat, String returnFormat) {
		return formatDispDate(dt, origFormat, returnFormat,	Locale.KOREAN, Locale.KOREAN);
	}

	public static String formatDispDate(String dt, String origFormat, String returnFormat, Locale ol) {
		return formatDispDate(dt, origFormat, returnFormat, ol,	Locale.KOREAN);
	}

	public static String formatDispDate(String dt, String origFormat, String returnFormat, Locale ol, Locale rl) {
		String rtn = dt;
		try {
			DateTime o = toDate(dt, origFormat, ol);
			rtn = getDateTimeFormat(o, returnFormat, rl);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return rtn;
	}


	/**
     * 날짜비교
     *  -> 시작일자 - 종료일자
     *  -> + 값일 경우 시작일자가 큼
     *  -> - 값일 경우 시작일자가 작음
     * @param pattern
     * @param startValue
     * @param endValue
     * @return
     */
    public static Long getDateDiff(String pattern, String startValue, String endValue) {
        // 변수선언 및 값 설정
        Long diffValue = null;

        try {
            SimpleDateFormat simpleDateFormat    = new SimpleDateFormat(pattern);
            Date startDate      = simpleDateFormat.parse(startValue);
            Date endDate        = simpleDateFormat.parse(endValue);

            diffValue   = startDate.getTime() - endDate.getTime();
        } catch(Exception e) {
            // 에러출력
            e.printStackTrace();

            return null;
        }

        return diffValue;
    }

	public static int getDiffDayCount(String fromDate, String toDate) {
		DateTime start = toDate(fromDate, "yyyyMMdd");
		DateTime end = toDate(toDate, "yyyyMMdd");
		return getDiffDay(start, end);
	}

	public static int getDiffDay(DateTime date1, DateTime date2){
		return Days.daysBetween(date1, date2).getDays();
	}

	public static String getDiffTimeString(DateTime date1, DateTime date2, int cnt){
		String value="";
		try {
			long diffSec = date1.getMillis() - date2.getMillis();
			boolean isMinus = (diffSec < 0);
			diffSec = Math.abs(diffSec) / 1000 * cnt;

			long hour = diffSec / 3600;
			long minute = (diffSec - (hour * 3600)) / 60;
			long second = (diffSec - (hour * 3600) - (minute * 60));
			if(hour>0){
				value = value + StringUtil.getCommaNumberFormat(hour) + "시간 ";
			}
			if(minute>0){
				value =value + minute + "분 ";
			}
			if(second>0){
				value =value + second + "초";
			}

			if(isMinus) {
				value = "-" + value;
			}
		}
		catch(RuntimeException e) {
			e.printStackTrace();
		}
		return value;
	}



	public static String getDiffTimeString(String date1, String date2) {
		DateTime d1 = toDate(date1, YMDHMS_FMT);
		DateTime d2 = toDate(date2, YMDHMS_FMT);
		return getDiffTimeString(d1, d2, 1);
	}

	public static String[] getDayList(String date1, String date2) {
		return getDayList(date1, date2, true, true);
	}

	public static String[] getDayList(String date1, String date2, boolean includeStart, boolean includeEnd) {
		DateTime d1 = toDate(date1, YMD_FMT);
		DateTime d2 = toDate(date2, YMD_FMT);
		return getDayList(d1, d2, includeStart, includeEnd);
	}

	public static String[] getDayList(Date date1, Date date2){
		return getDayList(new DateTime(date1), new DateTime(date2), true, true);
	}

	public static String[] getDayList(Date date1, Date date2, boolean includeStart, boolean includeEnd) {
		return getDayList(new DateTime(date1), new DateTime(date2), includeStart, includeEnd);
	}

	public static String[] getDayList(DateTime date1, DateTime date2, boolean includeStart, boolean includeEnd){
		int diffDay = getDiffDay(date1,date2)+1;
		int arrSize = diffDay - (includeStart?0:1) - (includeEnd?0:1);
		String[] result = new String[arrSize];
		for(int i=0, j=0; i<diffDay;i++){
			if(i+1 == diffDay && !includeEnd) {
				break;
			}
			result[j] = date1.plusDays(i).toString(YMD_FMT, Locale.KOREAN);
			if(i == 0) {
				if(includeStart) {
					j++;
				}
			}
			else {
				j++;
			}
		}
		return result;
	}


	public static int getDayOfWeek(Date date){
		return getDayOfTheWeekYMD(new DateTime(date).toString(YMD_FMT));
	}

	public static String getDayOfKrWeek(int week){
		String[] krWeek= {"일","월","화","수","목","금","토"};
		return krWeek[week-1];
	}


    /**
     * 특정 날짜에 대하여 요일을 구함(일요일 ~ 토요일)
     * @param date
     * @param dateType
     * @return
     * @throws Exception
     */
    public static String getDateDay(String dt, String ty) {
    	int dayNum = getDayOfTheWeekYMD(dt, ty);
    	String day = "요일";
    	try {
    		day = getDayOfKrWeek(dayNum) + "요일";
    	}
    	catch(IndexOutOfBoundsException e) {
    		e.printStackTrace();
    	}
        return day;
    }



	private static String[] solarArr = new String[] { "0101"/* 양력설 */
	, "0301"/* 삼일절 */
	, "0505"/* 어린이날 */
	, "0606"/* 현충일 */
	, "0815"/* 광복절 */
	, "1003"/* 개천절 */
	, "1009"/* 한글날 */
	, "1225"/* 성탄절 */
	};

	private static String[] lunarArr = new String[] { "0101"/* 설날당일 */
	, "0102"/* 설날다음날 */
	, "0408"/* 부처님오신날 */
	, "0814"/* 추석전날 */
	, "0815"/* 추석당일 */
	, "0816"/* 추석다음날 */
	};


	public static String getLunarFromSolar(String date) {
		Calendar cal = Calendar.getInstance();
		ChineseCalendar chinaCal = new ChineseCalendar();

		cal.set(Calendar.YEAR, Integer.parseInt(date.substring(0, 4)));
		cal.set(Calendar.MONTH, Integer.parseInt(date.substring(4, 6)) - 1);
		cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date.substring(6)));
		chinaCal.setTimeInMillis(cal.getTimeInMillis());

		int yy = chinaCal.get(ChineseCalendar.EXTENDED_YEAR) - 2637;
		int mm = chinaCal.get(ChineseCalendar.MONTH) + 1;
		int dd = chinaCal.get(ChineseCalendar.DAY_OF_MONTH);

		StringBuilder sb = new StringBuilder();
		sb.append(yy);
		if (mm < 10) {
			sb.append("0");
		}
		sb.append(mm);

		if (dd < 10) {
			sb.append("0");
		}
		sb.append(dd);

		return sb.toString();
	}

	public static String getSolarFromLunar(String date) {
		ChineseCalendar chinaCal = new ChineseCalendar();
		chinaCal.set(com.ibm.icu.util.Calendar.EXTENDED_YEAR, Integer.parseInt(date.substring(0, 4))+2637);
		chinaCal.set(com.ibm.icu.util.Calendar.MONTH, Integer.parseInt(date.substring(4, 6)) - 1);
		chinaCal.set(com.ibm.icu.util.Calendar.DATE, Integer.parseInt(date.substring(6)));
		chinaCal.set(com.ibm.icu.util.Calendar.HOUR_OF_DAY, 0);

		Calendar cal = Calendar.getInstance();
		cal.setTime(chinaCal.getTime());

		int yy = cal.get(Calendar.YEAR);
		int mm = cal.get(Calendar.MONTH) + 1;
		int dd = cal.get(Calendar.DAY_OF_MONTH);

		StringBuilder sb = new StringBuilder();
		sb.append(yy);

		if (mm < 10) {
			sb.append("0");
		}
		sb.append(mm);

		if (dd < 10) {
			sb.append("0");
		}
		sb.append(dd);

		return sb.toString();
	}

	public static boolean isHolidayLunar(String date) {
		boolean result = false;
		Calendar cal = Calendar.getInstance();
		ChineseCalendar chinaCal = new ChineseCalendar();

		cal.set(Calendar.YEAR, Integer.parseInt(date.substring(0, 4)));
		cal.set(Calendar.MONTH, Integer.parseInt(date.substring(4, 6)) - 1);
		cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date.substring(6)));
		chinaCal.setTimeInMillis(cal.getTimeInMillis());

		/** 음력으로 변환된 월과 일자 **/
		int mm = chinaCal.get(ChineseCalendar.MONTH) + 1;
		int dd = chinaCal.get(ChineseCalendar.DAY_OF_MONTH);

		StringBuilder sb = new StringBuilder();
		if (mm < 10) {
			sb.append("0");
		}
		sb.append(mm);

		if (dd < 10) {
			sb.append("0");
		}
		sb.append(dd);

		/** 음력 12월의 마지막날 (설날 첫번째 휴일)인지 확인 **/

		if (mm == 12) {
			int lastDate = chinaCal
					.getActualMaximum(ChineseCalendar.DAY_OF_MONTH);
			if (dd == lastDate) {
				return true;
			}
		}
		for (String d : lunarArr) {
			if (sb.toString().equals(d)) {
				return true;
			}
		}

		return result;
	}

	public static boolean isHolidaySolar(String date) {
		return isHolidaySolar(date, YMD_FMT);
	}

	public static boolean isHolidaySolar(String date, String fmt) {
		boolean result = false;
		DateTime dt = toDate(date, fmt);
		String md = dt.toString("MMdd");

		for (String d : solarArr) {
			if (md.equals(d)) {
				return true;
			}
		}
		return result;
	}


	public static boolean isDayOfWeek(String date, int dayOfWeek) {
		boolean result = false;
		if (getDayOfTheWeekYMD(date) == dayOfWeek) {
			result = true;
		}
		return result;
	}

	public static boolean isSunday(String date) {
		return isDayOfWeek(date, SUNDAY);
	}

	public static boolean isSaterday(String date) {
		return isDayOfWeek(date, SATURDAY);
	}

	public static boolean isFriday(String date) {
		return isDayOfWeek(date, FRIDAY);
	}

	public static boolean isWeekdayIncludeSunday(String date) {
		boolean result = false;
		if (getDayOfTheWeekYMD(date) < SATURDAY) {
			result = true;
		}
		return result;
	}

	public static boolean isWeekday(String date) {
		boolean result = false;
		int dayOfWeek = getDayOfTheWeekYMD(date);
		if (dayOfWeek > SUNDAY && dayOfWeek < SATURDAY) {
			result = true;
		}
		return result;
	}

	public static boolean isPrevdayOfHoliday(String date) {
		date = getDateAddDayYMD(date, 1);
		boolean result = true;
		result = isHoliday(date);
		return result;
	}

	public static boolean isHoliday(String date) {
		boolean result = true;
		/** 양력 법정공휴일부터 확인 **/
		result = isHolidaySolar(date);
		/** 양력 법정공휴일에 해당하지 않으면 **/
		if (!result) {
			/** 음력 법정공휴일에 해당하는 지 확인 **/
			result = isHolidayLunar(date);
		}
		return result;
	}

	public static String[] getHoliday(String yyyymmdd) {
		// 검사년도
		int yyyy = Integer.parseInt(yyyymmdd.substring( 0, 4 ));
		try {
			for (String[] hd :  getYearHoliday(yyyy) ) {
				if (yyyymmdd.equals(hd[0])) {
					return hd;
				}
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}


	public static List<String[]> getYearHoliday(int yyyy) {
		// 음력 공휴일을 양력으로 바꾸어서 입력
		List<String[]> solarHolidays = new ArrayList<String[]>();
		solarHolidays.add(new String[] { yyyy + "0101",  "신정"});
		solarHolidays.add(new String[] { yyyy + "0301",  "삼일절"});
		solarHolidays.add(new String[] { yyyy + "0505",  "어린이날"});
		solarHolidays.add(new String[] { yyyy + "0606",  "현충일"});
		solarHolidays.add(new String[] { yyyy + "0815",  "광복절"});
		solarHolidays.add(new String[] { yyyy + "1003",  "개천절"});
		solarHolidays.add(new String[] { yyyy + "1009",  "한글날"});
		solarHolidays.add(new String[] { yyyy + "1225",  "성탄절"});

		List<String[]> lunarHolidays = new ArrayList<String[]>();

		try {
			String tmp01 = DateUtil.lunarTranse( yyyy + "0101", false);// 음력설날
			String tmp02 = DateUtil.lunarTranse( yyyy + "0815", false);// 음력추석
			String tmp03 = DateUtil.lunarTranse( yyyy + "0408", false);// 석가탄신일
			lunarHolidays.add(new String[] { DateUtil.getDateAdd(tmp01, "yyyyMMdd", -1, Calendar.DATE), "설연휴"});
			lunarHolidays.add(new String[] { tmp01,  "설날"});
			lunarHolidays.add(new String[] { DateUtil.getDateAdd(tmp01, "yyyyMMdd", 1, Calendar.DATE), "설연휴"});
			lunarHolidays.add(new String[] { DateUtil.getDateAdd(tmp02, "yyyyMMdd", -1, Calendar.DATE), "추석연휴"});
			lunarHolidays.add(new String[] { tmp02,  "추석"});
			lunarHolidays.add(new String[] { DateUtil.getDateAdd(tmp02, "yyyyMMdd", 1, Calendar.DATE), "추석연휴"});
			lunarHolidays.add(new String[] { tmp03,  "석가탄신일"});
		}
		catch(IllegalArgumentException e) {
			e.printStackTrace();
		}
		catch(Exception e) {
			e.printStackTrace();
		}

		List<String[]> holidays = new ArrayList<String[]>();
		holidays.addAll(lunarHolidays);
		holidays.addAll(solarHolidays);

		return holidays;
	}

	public static String getDateAdd(String strDate, String type, int add, int field) {
		if(StringUtil.isEmpty(strDate)){
			return "";
		}
		Calendar cal = DateUtil.toDate(strDate, type).toCalendar(Locale.KOREAN);
		cal.add(field, add);
		DateTime dt = new DateTime(cal);
		return getDateTimeFormat(dt, type);
	}

	/**
	 * 음력을 약력으로
	 *
	 * @param       String    음력일('yyyyMMdd')
	 * @param       boolean   윤달 여부
	 * @return      String    처리결과 양력일 엔티티
	 * @throws      java.lang.Exception
	 */
	public static String lunarTranse(String TranseDay,boolean leapyes) throws Exception{
		int lyear = Integer.parseInt(TranseDay.substring(0,4));
		int lmonth = Integer.parseInt(TranseDay.substring(4,6));
		int lday = Integer.parseInt(TranseDay.substring(6,8));

		if(!leapyes && !verifyDate(lyear, lmonth, lday, "solar-"))
		{
			return "";
		}
		if(leapyes && !verifyDate(lyear, lmonth, lday, "solar+"))
		{
			return "";
		}
		int m1 = -1;
		long td = 0L;
		if(lyear != 1881)
		{
			m1 = lyear - 1882;
			for(int i = 0; i <= m1; i++)
			{
				for(int j = 0; j < 13; j++)
					td = td + (long)KK[i * 13 + j];
				if(KK[i * 13 + 12] == 0)
					td = td + 336L;
				else
					td = td + 362L;
			}
		}
		m1++;
		int n2 = lmonth - 1;
		int m2 = -1;
		do
		{
			m2++;
			if(KK[m1 * 13 + m2] > 2)
			{
				td = td + 26L + (long)KK[m1 * 13 + m2];
				n2++;
				continue;
			}
			if(m2 == n2)
				break;
			td = td + 28L + (long)KK[m1 * 13 + m2];
		} while(true);
		if(leapyes)
			td = td + 28L + (long)KK[m1 * 13 + m2];
		td = td + (long)lday + 29L;
		m1 = 1880;
		do
		{
			m1++;
			boolean leap = m1 % 400 == 0 || m1 % 100 != 0 && m1 % 4 == 0;
			if(leap)
				m2 = 366;
			else
				m2 = 365;
			if(td < (long)m2)
				break;
			td = td - (long)m2;
		} while(true);
		int syear = m1;
		MDayCnt[1] = m2 - 337;
		m1 = 0;
		do
		{
			m1++;
			if(td <= (long)MDayCnt[m1 - 1])
				break;
			td = td - (long)MDayCnt[m1 - 1];
		} while(true);
		int smonth = m1;
		int sday = (int)td;
		long y = (long)syear - 1L;
		td = ((y * 365L + y / 4L) - y / 100L) + y / 400L;
		boolean leap = syear % 400 == 0 || syear % 100 != 0 && syear % 4 == 0;
		if(leap)
			MDayCnt[1] = 29;
		else
			MDayCnt[1] = 28;
		for(int i = 0; i < smonth - 1; i++)
			td = td + (long)MDayCnt[i];
		td = td + (long)sday;
		int i = (int)(td % 10L);
		i = (i + 4) % 10;
		int j = (int)(td % 12L);
		j = (j + 2) % 12;
		String sValue=String.valueOf(syear);
		if(smonth<10)
			sValue+="0";
		sValue+=String.valueOf(smonth);
		if(sday<10)
			sValue+="0";
		sValue+=String.valueOf(sday);
		return sValue;
	}

	private static boolean verifyDate(int k, int l, int l1, String s)
	{
		if(k < 1881 || k > 2043 || l < 1 || l > 12)
			return false;
		if("lunar".equals(s) && l1 > MDayCnt[l - 1])
			return false;
		if("solar+".equals(s))
		{
			if(KK[(k - 1881) * 13 + 12] < 1)
				return false;
			if(KK[(k - 1881) * 13 + l] < 3)
				return false;
			if(KK[(k - 1881) * 13 + l] + 26 < l1)
				return false;
		}
		if("solar-".equals(s))
		{
			int j = l - 1;
			for(int i = 1; i <= 12; i++)
				if(KK[((k - 1881) * 13 + i) - 1] > 2)
					j++;
			if(l1 > KK[(k - 1881) * 13 + j] + 28)
				return false;
		}
		return true;
	}

	private static final int KK[] = {
		1, 2, 1, 2, 1, 2, 2, 3, 2, 2,
		1, 2, 1, 1, 2, 1, 2, 1, 2, 1,
		2, 2, 1, 2, 2, 0, 1, 1, 2, 1,
		1, 2, 1, 2, 2, 2, 1, 2, 0, 2,
		1, 1, 2, 1, 3, 2, 1, 2, 2, 1,
		2, 2, 2, 1, 1, 2, 1, 1, 2, 1,
		2, 1, 2, 2, 0, 2, 1, 2, 1, 2,
		1, 1, 2, 1, 2, 1, 2, 0, 2, 2,
		1, 2, 3, 2, 1, 1, 2, 1, 2, 1,
		2, 2, 1, 2, 2, 1, 2, 1, 1, 2,
		1, 2, 1, 0, 2, 1, 2, 2, 1, 2,
		1, 2, 1, 2, 1, 2, 0, 1, 2, 3,
		2, 1, 2, 2, 1, 2, 1, 2, 1, 2,
		1, 2, 1, 2, 1, 2, 1, 2, 2, 1,
		2, 2, 0, 1, 1, 2, 1, 1, 2, 3,
		2, 2, 1, 2, 2, 2, 1, 1, 2, 1,
		1, 2, 1, 2, 1, 2, 2, 2, 0, 1,
		2, 1, 2, 1, 1, 2, 1, 2, 1, 2,
		2, 0, 2, 1, 2, 1, 2, 3, 1, 2,
		1, 2, 1, 2, 1, 2, 2, 2, 1, 2,
		1, 1, 2, 1, 2, 1, 2, 0, 1, 2,
		2, 1, 2, 1, 2, 1, 2, 1, 2, 1,
		0, 2, 1, 2, 3, 2, 2, 1, 2, 1,
		2, 1, 2, 1, 2, 1, 2, 1, 2, 1,
		2, 2, 1, 2, 1, 2, 0, 1, 2, 1,
		1, 2, 1, 2, 2, 3, 2, 2, 1, 2,
		1, 2, 1, 1, 2, 1, 2, 1, 2, 2,
		2, 1, 0, 2, 1, 2, 1, 1, 2, 1,
		2, 1, 2, 2, 2, 0, 1, 2, 1, 2,
		1, 3, 2, 1, 1, 2, 2, 1, 2, 2,
		2, 1, 2, 1, 1, 2, 1, 1, 2, 2,
		1, 0, 2, 2, 1, 2, 2, 1, 1, 2,
		1, 2, 1, 2, 0, 1, 2, 2, 1, 4,
		1, 2, 1, 2, 1, 2, 1, 2, 1, 2,
		1, 2, 1, 2, 2, 1, 2, 1, 2, 1,
		0, 2, 1, 1, 2, 2, 1, 2, 1, 2,
		2, 1, 2, 0, 1, 2, 3, 1, 2, 1,
		2, 1, 2, 2, 2, 1, 2, 1, 2, 1,
		1, 2, 1, 2, 1, 2, 2, 2, 1, 0,
		2, 1, 2, 1, 1, 2, 3, 1, 2, 2,
		1, 2, 2, 2, 1, 2, 1, 1, 2, 1,
		1, 2, 2, 1, 2, 0, 2, 2, 1, 2,
		1, 1, 2, 1, 1, 2, 1, 2, 0, 2,
		2, 1, 2, 2, 3, 1, 2, 1, 2, 1,
		1, 2, 2, 1, 2, 2, 1, 2, 1, 2,
		1, 2, 1, 2, 0, 1, 2, 1, 2, 1,
		2, 2, 1, 2, 1, 2, 1, 0, 2, 1,
		3, 2, 1, 2, 2, 1, 2, 2, 1, 2,
		1, 2, 1, 1, 2, 1, 2, 1, 2, 2,
		2, 1, 2, 0, 1, 2, 1, 1, 2, 1,
		2, 3, 2, 2, 1, 2, 2, 1, 2, 1,
		1, 2, 1, 1, 2, 2, 1, 2, 2, 0,
		2, 1, 2, 1, 1, 2, 1, 1, 2, 1,
		2, 2, 0, 2, 1, 2, 2, 1, 3, 2,
		1, 1, 2, 1, 2, 2, 1, 2, 2, 1,
		2, 1, 2, 1, 2, 1, 1, 2, 0, 2,
		1, 2, 1, 2, 2, 1, 2, 1, 2, 1,
		1, 0, 2, 1, 2, 2, 3, 2, 1, 2,
		2, 1, 2, 1, 2, 1, 1, 2, 1, 2,
		1, 2, 2, 1, 2, 2, 1, 0, 2, 1,
		1, 2, 1, 2, 1, 2, 2, 1, 2, 2,
		0, 1, 2, 3, 1, 2, 1, 1, 2, 2,
		1, 2, 2, 2, 1, 2, 1, 1, 2, 1,
		1, 2, 1, 2, 2, 2, 0, 1, 2, 2,
		1, 1, 2, 3, 1, 2, 1, 2, 2, 1,
		2, 2, 2, 1, 1, 2, 1, 1, 2, 1,
		2, 1, 0, 2, 2, 2, 1, 2, 1, 2,
		1, 1, 2, 1, 2, 0, 1, 2, 2, 1,
		2, 4, 1, 2, 1, 2, 1, 1, 2, 1,
		2, 1, 2, 2, 1, 2, 2, 1, 2, 1,
		2, 0, 1, 1, 2, 1, 2, 1, 2, 2,
		1, 2, 2, 1, 0, 2, 1, 1, 4, 1,
		2, 1, 2, 1, 2, 2, 2, 1, 2, 1,
		1, 2, 1, 1, 2, 1, 2, 2, 2, 1,
		0, 2, 2, 1, 1, 2, 1, 1, 4, 1,
		2, 2, 1, 2, 2, 2, 1, 1, 2, 1,
		1, 2, 1, 2, 1, 2, 0, 2, 2, 1,
		2, 1, 2, 1, 1, 2, 1, 2, 1, 0,
		2, 2, 1, 2, 2, 1, 4, 1, 1, 2,
		1, 2, 1, 2, 1, 2, 2, 1, 2, 2,
		1, 2, 1, 1, 2, 0, 1, 2, 1, 2,
		1, 2, 2, 1, 2, 2, 1, 2, 0, 1,
		1, 2, 1, 4, 1, 2, 1, 2, 2, 1,
		2, 2, 1, 1, 2, 1, 1, 2, 1, 2,
		2, 2, 1, 2, 0, 2, 1, 1, 2, 1,
		1, 2, 1, 2, 2, 1, 2, 0, 2, 2,
		3, 1, 2, 1, 1, 2, 1, 2, 1, 2,
		2, 2, 1, 2, 1, 2, 1, 1, 2, 1,
		2, 1, 2, 0, 2, 2, 1, 2, 1, 2,
		1, 3, 2, 1, 2, 1, 2, 2, 1, 2,
		2, 1, 2, 1, 1, 2, 1, 2, 1, 0,
		2, 1, 2, 2, 1, 2, 1, 2, 1, 2,
		1, 2, 0, 1, 2, 1, 2, 1, 4, 2,
		1, 2, 1, 2, 1, 2, 1, 2, 1, 1,
		2, 2, 1, 2, 2, 1, 2, 2, 0, 1,
		1, 2, 1, 1, 2, 1, 2, 2, 1, 2,
		2, 0, 2, 1, 1, 4, 1, 1, 2, 1,
		2, 1, 2, 2, 2, 1, 2, 1, 2, 1,
		1, 2, 1, 2, 1, 2, 2, 0, 2, 1,
		2, 1, 2, 1, 1, 2, 3, 2, 1, 2,
		2, 1, 2, 2, 1, 2, 1, 1, 2, 1,
		2, 1, 2, 0, 1, 2, 2, 1, 2, 1,
		2, 1, 2, 1, 2, 1, 0, 2, 1, 2,
		1, 2, 2, 3, 2, 1, 2, 1, 2, 1,
		2, 1, 2, 1, 2, 1, 2, 2, 1, 2,
		1, 2, 0, 1, 2, 1, 1, 2, 1, 2,
		2, 1, 2, 2, 1, 0, 2, 1, 2, 1,
		3, 2, 1, 2, 1, 2, 2, 2, 1, 2,
		1, 2, 1, 1, 2, 1, 2, 1, 2, 2,
		2, 0, 1, 2, 1, 2, 1, 1, 2, 1,
		1, 2, 2, 1, 0, 2, 2, 2, 3, 2,
		1, 1, 2, 1, 1, 2, 2, 1, 2, 2,
		1, 2, 2, 1, 1, 2, 1, 2, 1, 2,
		0, 1, 2, 2, 1, 2, 1, 2, 3, 2,
		1, 2, 1, 2, 1, 2, 1, 2, 1, 2,
		2, 1, 2, 1, 2, 1, 0, 2, 1, 1,
		2, 2, 1, 2, 1, 2, 2, 1, 2, 0,
		1, 2, 1, 1, 2, 3, 2, 1, 2, 2,
		2, 1, 2, 1, 2, 1, 1, 2, 1, 2,
		1, 2, 2, 2, 1, 0, 2, 1, 2, 1,
		1, 2, 1, 1, 2, 2, 2, 1, 0, 2,
		2, 1, 2, 3, 1, 2, 1, 1, 2, 2,
		1, 2, 2, 2, 1, 2, 1, 1, 2, 1,
		1, 2, 1, 2, 0, 2, 2, 1, 2, 1,
		2, 1, 2, 3, 2, 1, 1, 2, 2, 1,
		2, 2, 1, 2, 1, 2, 1, 2, 1, 1,
		0, 2, 2, 1, 2, 1, 2, 2, 1, 2,
		1, 2, 1, 0, 2, 1, 1, 2, 1, 2,
		4, 1, 2, 2, 1, 2, 1, 2, 1, 1,
		2, 1, 2, 1, 2, 2, 1, 2, 2, 0,
		1, 2, 1, 1, 2, 1, 1, 2, 2, 1,
		2, 2, 0, 2, 1, 2, 1, 3, 2, 1,
		1, 2, 2, 1, 2, 2, 2, 1, 2, 1,
		1, 2, 1, 1, 2, 1, 2, 2, 0, 2,
		1, 2, 2, 1, 1, 2, 1, 1, 2, 3,
		2, 2, 1, 2, 2, 1, 2, 1, 2, 1,
		1, 2, 1, 2, 0, 1, 2, 2, 1, 2,
		2, 1, 2, 1, 2, 1, 1, 0, 2, 1,
		2, 2, 1, 2, 3, 2, 2, 1, 2, 1,
		2, 1, 1, 2, 1, 2, 1, 2, 2, 1,
		2, 2, 1, 0, 2, 1, 1, 2, 1, 2,
		1, 2, 2, 1, 2, 2, 0, 1, 2, 1,
		1, 2, 3, 1, 2, 1, 2, 2, 2, 2,
		1, 2, 1, 1, 2, 1, 1, 2, 1, 2,
		2, 2, 0, 1, 2, 2, 1, 1, 2, 1,
		1, 2, 1, 2, 2, 0, 1, 2, 2, 3,
		2, 1, 2, 1, 1, 2, 1, 2, 1, 2,
		2, 2, 1, 2, 1, 2, 1, 1, 2, 1,
		2, 0, 1, 2, 2, 1, 2, 2, 1, 2,
		3, 2, 1, 1, 2, 1, 2, 1, 2, 2,
		1, 2, 1, 2, 2, 1, 2, 0, 1, 1,
		2, 1, 2, 1, 2, 2, 1, 2, 2, 1,
		0, 2, 1, 1, 2, 1, 3, 2, 2, 1,
		2, 2, 2, 1, 2, 1, 1, 2, 1, 1,
		2, 1, 2, 2, 2, 1, 0, 2, 2, 1,
		1, 2, 1, 1, 2, 1, 2, 2, 1, 0,
		2, 2, 2, 1, 3, 2, 1, 1, 2, 1,
		2, 1, 2, 2, 2, 1, 2, 1, 2, 1,
		1, 2, 1, 2, 1, 0, 2, 2, 1, 2,
		2, 1, 2, 1, 1, 2, 1, 2, 0, 1,
		2, 3, 2, 2, 1, 2, 1, 2, 2, 1,
		1, 2, 1, 2, 1, 2, 1, 2, 2, 1,
		2, 2, 1, 2, 0, 1, 1, 2, 1, 2,
		1, 2, 3, 2, 2, 1, 2, 2, 1, 1,
		2, 1, 1, 2, 1, 2, 2, 2, 1, 2,
		0, 2, 1, 1, 2, 1, 1, 2, 1, 2,
		2, 1, 2, 0, 2, 2, 1, 1, 2, 3,
		1, 2, 1, 2, 1, 2, 2, 2, 1, 2,
		1, 2, 1, 1, 2, 1, 2, 1, 2, 0,
		2, 1, 2, 2, 1, 2, 1, 1, 2, 1,
		2, 1, 0, 2, 1, 2, 4, 2, 1, 2,
		1, 1, 2, 1, 2, 1, 2, 1, 2, 2,
		1, 2, 1, 2, 1, 2, 1, 2, 0, 1,
		2, 1, 2, 1, 2, 1, 2, 2, 3, 2,
		1, 2, 1, 2, 1, 1, 2, 1, 2, 2,
		2, 1, 2, 2, 0, 1, 1, 2, 1, 1,
		2, 1, 2, 2, 1, 2, 2, 0, 2, 1,
		1, 2, 1, 3, 2, 1, 2, 1, 2, 2,
		2, 1, 2, 1, 2, 1, 1, 2, 1, 2,
		1, 2, 2, 0, 2, 1, 2, 1, 2, 1,
		1, 2, 1, 2, 1, 2, 0, 2, 1, 2,
		2, 3, 2, 1, 1, 2, 1, 2, 1, 2,
		1, 2, 2, 1, 2, 1, 2, 1, 2, 1,
		2, 1, 0, 2, 1, 2, 1, 2, 2, 1,
		2, 1, 2, 1, 2, 0, 1, 2, 3, 2,
		1, 2, 1, 2, 2, 1, 2, 1, 2, 1,
		2, 1, 1, 2, 1, 2, 2, 1, 2, 2,
		1, 0, 2, 1, 2, 1, 1, 2, 3, 2,
		1, 2, 2, 2, 1, 2, 1, 2, 1, 1,
		2, 1, 2, 1, 2, 2, 2, 0, 1, 2,
		1, 2, 1, 1, 2, 1, 1, 2, 2, 2,
		0, 1, 2, 2, 1, 2, 3, 1, 2, 1,
		1, 2, 2, 1, 2, 2, 1, 2, 2, 1,
		1, 2, 1, 1, 2, 2, 0, 1, 2, 1,
		2, 2, 1, 2, 1, 2, 1, 2, 1, 0,
		2, 1, 2, 3, 2, 1, 2, 2, 1, 2,
		1, 2, 1, 2, 1, 1, 2, 1, 2, 2,
		1, 2, 2, 1, 2, 0, 1, 2, 1, 1,
		2, 1, 2, 3, 2, 2, 2, 1, 2, 1,
		2, 1, 1, 2, 1, 2, 1, 2, 2, 2,
		1, 0, 2, 1, 2, 1, 1, 2, 1, 1,
		2, 2, 1, 2, 0, 2, 2, 1, 2, 1,
		1, 4, 1, 1, 2, 1, 2, 2, 2, 2,
		1, 2, 1, 1, 2, 1, 1, 2, 1, 2,
		0, 2, 2, 1, 2, 1, 2, 1, 2, 1,
		1, 2, 1, 0, 2, 2, 1, 2, 2, 3,
		2, 1, 2, 1, 2, 1, 1, 2, 1, 2,
		2, 1, 2, 2, 1, 2, 1, 2, 1, 0,
		2, 1, 1, 2, 1, 2, 2, 1, 2, 2,
		1, 2, 0, 1, 2, 3, 1, 2, 1, 2,
		1, 2, 2, 2, 1, 2, 1, 2, 1, 1,
		2, 1, 1, 2, 2, 1, 2, 2, 0
	};
	private static final int MDayCnt[] = {31, 0, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

	/**
	 * 해당월의 마지막날을 구한다
	 * @param year
	 * @param month
	 * @return
	 */
	public static int getLastDay(Integer year,Integer month) {
		Calendar cal = Calendar.getInstance();
		cal.set(year, month-1,1);
		int lastDay = cal.getActualMaximum(Calendar.DATE);
		return lastDay;
	}



	/**
     * 날짜 형식이 맞는지 체크한다
     * yyyyMMdd 100만개 기준으로
     * 맞는 식 / 틀리는 식 전체 1.8초 내로 끝남.
     * @param date
     * @param format
     * @return  true    : 날짜형태일경우
     *          false   : 날짜 형태가 아닌경우
     */
    public static boolean validateDateFormat(String date,String format) {
        return isValidDate(date, format);
    }






	public static String getTermDay(String bgnde, String endde, String timeSe, String bgnTime, String endTime) {
		String bd = DateUtil.formatDispDate(bgnde, "yyyyMMdd", "yyyy.MM.dd(E)", Locale.KOREA);
		String ed = DateUtil.formatDispDate(endde, "yyyyMMdd", "yyyy.MM.dd(E)", Locale.KOREA);
		String bh = StringUtils.substring(bgnTime, 0, 2);
		String bm = StringUtils.substring(bgnTime, 2, 4);
		String eh = StringUtils.substring(endTime, 0, 2);
		String em = StringUtils.substring(endTime, 2, 4);

		StringBuffer bterm = new StringBuffer(bd);
		if ("WORK".equals(timeSe)) {
			if (StringUtils.isNotBlank(ed) && !bd.equals(ed)) {
				bterm.append(" ~ ").append(ed);
			}

			if (StringUtils.isNotBlank(bgnTime) || StringUtils.isNotBlank(endTime)) {
				if (StringUtils.isNotBlank(bh)) {
					bterm.append(" / ");
					bterm.append(bh).append("시");
					if (StringUtils.isNotBlank(bm)) {
						bterm.append(bm).append("분");
					}
				}
				if (StringUtils.isNotBlank(eh)) {
					bterm.append("~").append(eh).append("시");
					if (StringUtils.isNotBlank(em)) {
						bterm.append(em).append("분");
					}
				}
			}
		} else if ("NA".equals(timeSe)) {
			if (StringUtils.isNotBlank(ed) && !bd.equals(ed)) {
				bterm.append(" ~ ").append(ed);
			}
		} else {
			if (StringUtils.isNotBlank(bh)) {
				bterm.append(" ").append(bh).append("시");
				if (StringUtils.isNotBlank(bm)) {
					bterm.append(" ").append(bm).append("분");
				}
			}
			if (StringUtils.isNotBlank(ed) && !bd.equals(ed)) {
				bterm.append("/ ~ ").append(ed).append(" ");
			}

			if (bd.equals(ed)) {
				bterm.append(" ~ ");
			}

			if (StringUtils.isNotBlank(eh)) {
				bterm.append(eh).append("시");
				if (StringUtils.isNotBlank(em)) {
					bterm.append(em).append("분");
				}
			}
		}

		return bterm.toString();
	}

	public static String getTermTime(String bgnTime, String endTime) {
		String bh = StringUtils.substring(bgnTime, 0, 2);
		String bm = StringUtils.substring(bgnTime, 2, 4);
		String eh = StringUtils.substring(endTime, 0, 2);
		String em = StringUtils.substring(endTime, 2, 4);

		StringBuffer bterm = new StringBuffer();
		if (StringUtils.isNotBlank(bgnTime) || StringUtils.isNotBlank(endTime)) {
			if (StringUtils.isNotBlank(bh)) {
				bterm.append(bh).append(":");
				if (StringUtils.isNotBlank(bm)) {
					bterm.append(bm);
				}
				else {
					bterm.append("00");
				}
			}
			if (StringUtils.isNotBlank(eh)) {
				bterm.append("~").append(eh).append(":");
				if (StringUtils.isNotBlank(em)) {
					bterm.append(em);
				}
				else {
					bterm.append("00");
				}
			}
		}
		return bterm.toString();
	}

	public static String getNextMonthYM(String ym) {
		String res = getNowDateTime("yyyyMM");
		if(!isValidDate(ym, "yyyyMM")) {
			ym = res;
		}
		try {
			int year = Integer.parseInt(ym.substring(0, 4));
			int month = Integer.parseInt(ym.substring(4, 6));
			int[] yma = getNextMonth(year, month);
			res = String.format("%s%s", yma[0], StringUtils.leftPad(String.valueOf(yma[1]), 2,"0"));
		}
		catch(IllegalArgumentException e) {
			e.printStackTrace();
		}
		return res;
	}

	public static String getPrevMonthYM(String ym) {
		String res = getNowDateTime("yyyyMM");
		if(!isValidDate(ym, "yyyyMM")) {
			ym = res;
		}
		try {
			int year = Integer.parseInt(ym.substring(0, 4));
			int month = Integer.parseInt(ym.substring(4, 6));
			int[] yma = getPrevMonth(year, month);
			res = String.format("%s%s", yma[0], StringUtils.leftPad(String.valueOf(yma[1]), 2,"0"));
		}
		catch(IllegalArgumentException e) {
			e.printStackTrace();
		}
		return res;
	}



	/**
     * 다음 달을 구해온다
     * @param year
     * @param month
     * @return [0] = 년 [1] = 월
     */
    public static int[] getNextMonth(Integer year,Integer month) {
        if(month < 12) {
            return new int[] {year, month+1};
        }else {
            return new int[] {year + 1, 1};
        }
    }
    /**
     * 전달을 구해온다
     * @param year
     * @param month
     * @return [0] = 년 [1] = 월
     */
    public static int[] getPrevMonth(Integer year,Integer month) {

        if(month == 1) {
            return new int[] {year - 1, 12};
        }else {
            return new int[] {year, month - 1};
        }
    }

    public static boolean betweenYMD(String compde, String bgnde, String endde) {
		return between(compde, bgnde, endde, "yyyyMMdd");
	}

	public static boolean between(String compde, String bgnde, String endde, String fmt) {
		if(!isValidDate(compde, fmt)) {
			return false;
		}
		if(!isValidDate(bgnde, fmt)) {
			return false;
		}
		if(!isValidDate(endde, fmt)) {
			return false;
		}
		return compde.compareTo(bgnde) >= 0 && compde.compareTo(endde) <= 0;
	}

	public static String getAddDateTIme(int field, int amount) throws Exception {

		Calendar now = Calendar.getInstance();
		now.add(field, amount);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(now.getTime()).toString();

	}

	public static String getDateAddDayFromNow(int day) {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DATE, day);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(now.getTime()).toString();
    }

	public static String getDateAddDayFromNow(int day,String format) {

        Calendar now = Calendar.getInstance();
        now.add(Calendar.DATE, day);

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(now.getTime()).toString();

    }

	/**
	 * 날짜 SubString
	 * @param strDate
	 * @param gubun
	 * @return
	 */
	public static String getDateFormat(String strDate, String gubun){
		String rtn = "";

		try{
			strDate = StringUtil.strTrim(strDate);

			if(strDate.length() >= 12){
				if("yyyy.mm.dd hh:mi:ss sss".equals(gubun)){
					// 2017.11.11 11:11:11
					rtn = strDate.substring(0, 4) + "." + strDate.substring(4,6) + "." + strDate.substring(6, 8) + " " + strDate.substring(8, 10) + ":" + strDate.substring(10, 12) + ":" + strDate.substring(12, 14);
				}else if("yyyy.mm.dd hh:mi:ss".equals(gubun)){
					// 2017.11.11 11:11:11
					rtn = strDate.substring(0, 4) + "." + strDate.substring(4,6) + "." + strDate.substring(6, 8) + " " + strDate.substring(8, 10) + ":" + strDate.substring(10, 12) + ":" + strDate.substring(12);
				}else if("yyyy-mm-dd hh:mi:ss".equals(gubun)){
					// 2017-11-11 11:11:11
					rtn = strDate.substring(0, 4) + "-" + strDate.substring(4,6) + "-" + strDate.substring(6, 8) + " " + strDate.substring(8, 10) + ":" + strDate.substring(10, 12) + ":" + strDate.substring(12);
				}else if("yyyy-mm-dd hh:mi".equals(gubun)){
					// 2017-11-11 11:11
					rtn = strDate.substring(0, 4) + "-" + strDate.substring(4,6) + "-" + strDate.substring(6, 8) + " " + strDate.substring(8, 10) + ":" + strDate.substring(10, 12);
				}else if("yyyy/mm/dd hh:mi:ss".equals(gubun)){
					// 2017/11/11 11:11:11
					rtn = strDate.substring(0, 4) + "/" + strDate.substring(4,6) + "/" + strDate.substring(6, 8) + " " + strDate.substring(8, 10) + ":" + strDate.substring(10, 12) + ":" + strDate.substring(12);
				}else if("yyyy.mm.dd hh:mi".equals(gubun)){
					// 17.11.11 11:11
					rtn = strDate.substring(0, 4) + "." + strDate.substring(4,6) + "." + strDate.substring(6, 8) + " " + strDate.substring(8, 10) + ":" + strDate.substring(10, 12);
				}else if("yy.mm.dd hh:mi".equals(gubun)){
					// 17.11.11 11:11
					rtn = strDate.substring(2, 4) + "." + strDate.substring(4,6) + "." + strDate.substring(6, 8) + " " + strDate.substring(8, 10) + ":" + strDate.substring(10, 12);
				}else if("yy.mm.dd".equals(gubun)){
					// 17.11.11
					rtn = strDate.substring(2, 4) + "." + strDate.substring(4,6) + "." + strDate.substring(6, 8);
				}else if("yyyy.mm.dd".equals(gubun)){
					// 2017.11.11
					rtn = strDate.substring(0, 4) + "." + strDate.substring(4,6) + "." + strDate.substring(6, 8);
				}else if("yyyy-mm-dd".equals(gubun)){
					// 2017-11-11
					rtn = strDate.substring(0, 4) + "-" + strDate.substring(4,6) + "-" + strDate.substring(6, 8);
				}else if("mm.dd".equals(gubun)){
					rtn = strDate.substring(4,6) + "." + strDate.substring(6, 8);
				}
			}else if(strDate.length() >= 8){
				if("yyyy.mm.dd".equals(gubun)){
					// 2017.11.11
					rtn = strDate.substring(0, 4) + "." + strDate.substring(4,6) + "." + strDate.substring(6, 8);
				}else if("yyyy-mm-dd".equals(gubun)){
					// 2017-11-11
					rtn = strDate.substring(0, 4) + "-" + strDate.substring(4,6) + "-" + strDate.substring(6, 8);
				}else if("mm.dd".equals(gubun)){
					// 11.11
					rtn = strDate.substring(4,6) + "." + strDate.substring(6, 8);
				}
			}else{
				rtn = "";
			}
		}catch(Exception e){
			rtn = "";
		}

		return rtn;
	}

	public static List<String> getDayOfWeekFindByDateRange(String sde, String ede, String[] dateOfWeek) throws Exception {

		List<String> list = new ArrayList<String>();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = null, endDate = null;
        startDate = dateFormat.parse(sde); // or 2010-05-01
        endDate = dateFormat.parse(ede); // or 2010-06-01

        Calendar start = Calendar.getInstance();
        start.setTime(startDate);
        Calendar end = Calendar.getInstance();
        end.setTime(endDate);
        int i =0;
        while(!start.after(end))
        {
            int year = start.get(Calendar.YEAR);
            int month = start.get(Calendar.MONTH) + 1;
            int day = start.get(Calendar.DAY_OF_MONTH);
            String date = String.format("%02d%02d%02d", year, month, day);

            boolean foundDayOfWeek = Arrays.asList( dateOfWeek ).contains(DateUtil.getDayOfWeek(date));
            if(foundDayOfWeek) {
            	list.add(String.format("%02d-%02d-%02d", year, month, day));
            }
            start.add(Calendar.DATE, 1);
        }
        return list;
	}

	public static String getDayOfWeek(String strDate) {

		int year = Integer.parseInt(strDate.substring(0, 4));
		int month = Integer.parseInt(strDate.substring(4, 6));
		int day = Integer.parseInt(strDate.substring(6, 8));

		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month-1, day);

		// 해당 년월일에 대한 요일 값
		String dayOfWeek = String.valueOf(calendar.get(Calendar.DAY_OF_WEEK));

		return dayOfWeek;
	}

	public static String getWeek() {
		Calendar c = Calendar.getInstance();
		String week = String.valueOf(c.get(Calendar.WEEK_OF_MONTH));
		return week;
	}

	/**
	 * 특정 날짜에 주차를 구한다(1주차, 2주차....)
	 * @param strDate
	 * @return
	 * @throws Exception
	 */
	public static String getWeek(String strDate) throws Exception{
		String week = "";
		try{
			strDate = StringUtil.strReplaceALL(strDate, "\\-", "");
			strDate = StringUtil.strReplaceALL(strDate, "\\.", "");

			int y = Integer.parseInt(strDate.substring(0, 4));
			int m = Integer.parseInt(strDate.substring(4, 6));
			int d = Integer.parseInt(strDate.substring(6, 8));

			Calendar calendar = Calendar.getInstance();
			calendar.set(y, m-1, d);

			week = String.valueOf(calendar.get(Calendar.WEEK_OF_MONTH));
		}catch (Exception e) {
			week = "9";
		}
		return week;

	}

	/**
	 * 현재 시간
	 * @return String 형식( yyyyMMddHHmmssSSS)
	 */
	public static String getCurrentTimeStamp() {
	    return new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
	}

	/**
	 * 몇일 후의 날짜구하기
	 * @param yyyy
	 * @param mm
	 * @param day
	 * @param addDay
	 * @return
	 */
	public static String getDateAddDay(int yyyy, int mm, int day, int addDay) {
		Calendar now = Calendar.getInstance();
		now.set(yyyy, mm-1, day);
		now.add(Calendar.DATE, addDay);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(now.getTime()).toString();
	}

	/**
	 * 현재 날짜에서 입력된 날짜를 더함
	 * @param day
	 * @param format	 ("yyyy-mm-dd")
	 * @return
	 */
	public static String getDateAddDay(int day, String format) {
		Calendar now = Calendar.getInstance();
		now.add(Calendar.DATE, day);

		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(now.getTime()).toString();

	}

	/**
	 * 요일로 변환
	 * @param dayNum
	 * @return
	 */
	public static String replaceDay(String dayNum, String gubun, String changeGubun){
		String dayStr = dayNum;

		try{
			dayStr = StringUtil.strReplaceALL(dayStr, gubun, changeGubun);
			dayStr = StringUtil.strReplaceALL(dayStr, "1", "일");
			dayStr = StringUtil.strReplaceALL(dayStr, "2", "월");
			dayStr = StringUtil.strReplaceALL(dayStr, "3", "화");
			dayStr = StringUtil.strReplaceALL(dayStr, "4", "수");
			dayStr = StringUtil.strReplaceALL(dayStr, "5", "목");
			dayStr = StringUtil.strReplaceALL(dayStr, "6", "금");
			dayStr = StringUtil.strReplaceALL(dayStr, "7", "토");
		}catch (Exception e) {
			dayStr = "";
		}

		return dayStr;
	}

	/**
	 * 년도 더하기, 빼기 함수
	 * 		ex) DateUtil.getDateAddYear(-5)
	 * @param year
	 * @return
	 * @throws Exception
	 */
	public static String getDateAddYear(int year) throws Exception {
		String dt = getNowDateTime("yyyyMMdd");
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");

        Calendar cal = Calendar.getInstance();
        Date date = format.parse(dt);

        cal.setTime(date);
        cal.add(Calendar.YEAR, year);      //년 더하기

        return format.format(cal.getTime());
	}

	/**
	 * 날짜 분기 확인
	 * @param day
	 * @return
	 */
	public static String getDateQuarter(String day){
		String rtnStr="";

		if("01".equals(day) || "02".equals(day) || "03".equals(day)){
			rtnStr = "1";
		}else if("04".equals(day) || "05".equals(day) || "06".equals(day)){
			rtnStr = "2";
		}else if("07".equals(day) || "08".equals(day) || "09".equals(day)){
			rtnStr = "3";
		}else if("10".equals(day) || "11".equals(day) || "12".equals(day)){
			rtnStr = "4";
		}

		return rtnStr;
	}

	/**
	 * 두 날짜의 차이  diffDateCnt("20031028", "20031102")
	 * @param begin
	 * @param end
	 * @return
	 * @throws Exception
	 */
	public static long diffDateCnt(String begin, String end) throws Exception{
		begin = StringUtil.strReplaceALL(begin, "\\-", "");
		begin = StringUtil.strReplaceALL(begin, "\\.", "");
		end = StringUtil.strReplaceALL(end, "\\-", "");
		end = StringUtil.strReplaceALL(end, "\\.", "");

		long diffDays = 0;
		try{
		    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

		    Date beginDate = formatter.parse(begin);
		    Date endDate = formatter.parse(end);

		    long diff = endDate.getTime() - beginDate.getTime();
		    diffDays = diff / (24 * 60 * 60 * 1000);
		}catch(Exception e){
			diffDays = 0;
		}

	    return diffDays;
	  }

	/**
	 * 날짜를 입력 받아 해당 요일 숫자를 리턴 한다.
	 * @param date
	 * @param dateType
	 * @return
	 * @throws Exception
	 */
	public static int getDateDayInt(String date, String dateType) throws Exception {
	    String day = "" ;

	    SimpleDateFormat dateFormat = new SimpleDateFormat(dateType) ;
	    Date nDate = dateFormat.parse(date) ;

	    Calendar cal = Calendar.getInstance() ;
	    cal.setTime(nDate);

	    int dayNum = cal.get(Calendar.DAY_OF_WEEK) ;

	    return dayNum;
	}

	/**
	 * 교육 요일 시간 정보 표시
	 * @param dayNum
	 * @param edcTime
	 * @param gubun
	 * @param changeGubun
	 * @return
	 */
	public static String replaceDayTime(String dayNum, String edcTime, String gubun, String changeGubun){
		String dayStr = "";
		String[] edcTimeArr = edcTime.split("\\|");

		try{
			if(dayNum.contains("1"))
				dayStr = dayStr + "(일) " + edcTimeArr[0].substring(0, 2)+":"+edcTimeArr[0].substring(2, 4) + "~" + edcTimeArr[0].substring(4, 6)+":"+edcTimeArr[0].substring(6, 8) + "<br />";

			if(dayNum.contains("2"))
				dayStr = dayStr + "(월) " + edcTimeArr[1].substring(0, 2)+":"+edcTimeArr[1].substring(2, 4) + "~" + edcTimeArr[1].substring(4, 6)+":"+edcTimeArr[1].substring(6, 8) + "<br />";

			if(dayNum.contains("3"))
				dayStr = dayStr + "(화) " + edcTimeArr[2].substring(0, 2)+":"+edcTimeArr[2].substring(2, 4) + "~" + edcTimeArr[2].substring(4, 6)+":"+edcTimeArr[2].substring(6, 8) + "<br />";

			if(dayNum.contains("4"))
				dayStr = dayStr + "(수) " + edcTimeArr[3].substring(0, 2)+":"+edcTimeArr[3].substring(2, 4) + "~" + edcTimeArr[3].substring(4, 6)+":"+edcTimeArr[3].substring(6, 8) + "<br />";

			if(dayNum.contains("5"))
				dayStr = dayStr + "(목) " + edcTimeArr[4].substring(0, 2)+":"+edcTimeArr[4].substring(2, 4) + "~" + edcTimeArr[4].substring(4, 6)+":"+edcTimeArr[4].substring(6, 8) + "<br />";

			if(dayNum.contains("6"))
				dayStr = dayStr + "(금) " + edcTimeArr[5].substring(0, 2)+":"+edcTimeArr[5].substring(2, 4) + "~" + edcTimeArr[5].substring(4, 6)+":"+edcTimeArr[5].substring(6, 8) + "<br />";

			if(dayNum.contains("7"))
				dayStr = dayStr + "(토) " + edcTimeArr[6].substring(0, 2)+":"+edcTimeArr[6].substring(2, 4) + "~" + edcTimeArr[6].substring(4, 6)+":"+edcTimeArr[6].substring(6, 8) + "<br />";

			if(dayStr.length() > 1){
				dayStr = dayStr.substring(0, dayStr.length() - 6);
			}

		}catch (Exception e) {
			dayStr = DateUtil.replaceDay(dayNum, gubun, changeGubun);
			dayStr = "("+dayStr+")";
		}

		return dayStr;
	}

}
