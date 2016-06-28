package MyBZ;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.util.Log;

public class PaiPan {

	private int DAYUN_COUNT = 8;// 需要的大运的数目
	// ==========干支数组===================
	final String[] Gan = new String[] { "甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸" };
	final String[] Zhi = new String[] { "子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥" };
	final String[] NaYin = new String[] { "海中金", "海中金", "炉中火", "炉中火", "大林木", "大林木", "路旁土", "路旁土", "剑锋金", "剑锋金", "山头火",
			"山头火", "涧下水", "涧下水", "城头土", "城头土", "白腊金", "白腊金", "杨柳木", "杨柳木", "泉中水", "泉中水", "屋上土", "屋上土", "霹雳火", "霹雳火",
			"松柏木", "松柏木", "长流水", "长流水", "沙中金", "沙中金", "山下火", "山下火", "平地木", "平地木", "壁上土", "壁上土", "金箔金", "金箔金", "佛灯火",
			"佛灯火", "天河水", "天河水", "大驿土", "大驿土", "钗钏金", "钗钏金", "桑松木", "桑松木", "大溪水", "大溪水", "沙中土", "沙中土", "天上火", "天上火",
			"石榴木", "石榴木", "大海水", "大海水" };

	// 计算干支的偏移值
	private int yearCyl, dayCyl, hourCyl;
	private String monthGanZhi = null;
	// 三种时间格式化格式
	static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
	static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH");
	static SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	static SimpleDateFormat sdf4 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * 构造函数
	 * 
	 * @param cal
	 */
	public PaiPan(Calendar cal) {

		Date baseDate = null;
		try {
			baseDate = sdf1.parse("1900-1-31");
		} catch (ParseException e) {
			e.printStackTrace();
		}

		yearCyl = getCheckedYear(cal) - 1864;

		// 求出月的干支
		monthGanZhi = getMonthGanZhiString(cal, getCheckedYear(cal));
		// 求出日和时的偏移值
		int offset = (int) ((cal.getTime().getTime() - baseDate.getTime()) / 86400000L);
		dayCyl = offset + 40;
		hourCyl = (int) ((cal.getTime().getTime() - baseDate.getTime() + 3300000L) / 7200000L);

	}

	/**
	 * 输出四柱
	 * 
	 * @return
	 */
	public String getSiZhuString() {// 返回四柱
		return cyclicalm(yearCyl) + monthGanZhi + cyclicalm(dayCyl) + cyclicalm(hourCyl);
	}

	/**
	 * 传入 月日的offset 传回干支, 0=甲子
	 * 
	 * @param num
	 * @return
	 */
	final private String cyclicalm(int num) {
		return (Gan[num % 10] + Zhi[num % 12]);
	}

	/**
	 * 输入日干支，返回空亡
	 * 
	 * @param rigan
	 * @return
	 */
	public String getKWString(String rigan) {
		int gan = 0, zhi = 0, kong = 0;
		gan = getGanPosition(String.valueOf(rigan.charAt(0)));
		zhi = getZhiPosition(String.valueOf(rigan.charAt(1)));

		kong = (10 - gan - (12 - zhi) + 12) % 12;
		String kw = Zhi[kong] + Zhi[kong + 1];
		return kw;
	}

	/**
	 * 输入性别和年干，返回排大运的方向
	 * 
	 * @param gender
	 * @param yeargan
	 * @return
	 */
	public int getPaiDaYunDir(int gender, String yeargan) {

		int dir = 1;
		int yg = getGanPosition(yeargan);
		if ((yg % 2 == 0 && gender == 1) || (yg % 2 == 1 && gender == 0)) {// 男阳/女阴
			dir = 1;
		} else if ((yg % 2 == 1 && gender == 1) || (yg % 2 == 0 && gender == 0)) {// 男阴/女阳
			dir = -1;
		}
		return dir;
	}

	/**
	 * 排大运方法
	 * 
	 * @param gender
	 * @param yearganzhi
	 * @param monthganzhi
	 * @return
	 */
	public String[] getDaYunString(int gender, String yearganzhi, String monthganzhi) {

		int dir = 1, monthgan = 0, i, monthzhi = 0;
		String[] daYun = new String[DAYUN_COUNT];

		dir = getPaiDaYunDir(gender, String.valueOf(yearganzhi.charAt(0)));

		monthgan = getGanPosition(String.valueOf(monthganzhi.charAt(0)));
		monthzhi = getZhiPosition(String.valueOf(monthganzhi.charAt(1)));
		// Log.e("tag", "transfer-->"+monthzhi);
		// Log.e("tag", "transfer-->"+monthganzhi.charAt(1));

		if (dir == 1) {
			for (i = 0; i < DAYUN_COUNT; i++) {
				daYun[i] = Gan[(++monthgan) % 10].toString() + (Zhi[(++monthzhi) % 12].toString());
			}
		} else {
			for (i = 0; i < DAYUN_COUNT; i++) {
				daYun[i] = Gan[((--monthgan) + 10) % 10].toString() + (Zhi[((--monthzhi) + 12) % 12].toString());
				// Log.e("tag", "dayundizhi-->"+(Zhi[((monthzhi) + 12) %
				// 12].toString()));
			}
		}

		return daYun;
	}

	/**
	 * 返回输入的地支所对应的位置
	 * 
	 * @param gan
	 * @return
	 */
	public int getGanPosition(String gan) {// 返回输入的天干所对应的位置

		int ganBack = 0, i = 0;
		for (i = 0; i < 10; i++) {
			if (Gan[i].toString().equals(gan)) {
				ganBack = i;
				break;
			} else {
				continue;
			}
		}
		return ganBack;
	}

	/**
	 * 返回输入的地支所对应的位置
	 * 
	 * @param zhi
	 * @return
	 */
	public int getZhiPosition(String zhi) {

		int zhiBack = 0, i = 0;
		for (i = 0; i < 12; i++) {
			if (Zhi[i].toString().equals(zhi)) {
				zhiBack = i;
				break;
			} else {
				continue;
			}
		}
		return zhiBack;
	}

	/**
	 * 输入日期，返回月干支
	 * 
	 * @param cal
	 * @param year
	 * @return
	 */
	public String getMonthGanZhiString(Calendar cal, int year) {

		String monthGan = "丙戊庚壬甲";
		String monBeginGan = String.valueOf(monthGan.charAt((year - 1864) % 5));

		int gan = getGanPosition(monBeginGan);
		int zhi = getZhiPosition("寅");

		String[] monthDate = getWholeYearJieQis(year);

		// 创建当年的所有月的干支字符数组monthGanZhis
		String[] monthGanZhis = new String[12];
		for (int i = 0; i < 12; i++) {
			monthGanZhis[i] = Gan[(gan++) % 10] + Zhi[(zhi++) % 12];
		}

		return monthGanZhis[getPostionOfTheYear(cal, monthDate)];

	}

	/**
	 * 输入校对后的年份，返回当年的所有节气的日期（立春~大寒）
	 * 
	 * @param year
	 * @return
	 */
	public String[] getWholeYearJieQis(int year) {

		SolarTerm solarTerm = new SolarTerm();
		String[] nowYear = solarTerm.getLunarJieQisDateOfTheYear(year);
		String[] nextYear = solarTerm.getLunarJieQisDateOfTheYear(year + 1);

		String[] monthDate = new String[12];
		for (int i = 2; i < 12; i++) {
			monthDate[i - 2] = nowYear[i];
		}
		monthDate[10] = nextYear[0];
		monthDate[11] = nextYear[1];

		return monthDate;
	}

	/**
	 * 返回立春日历时间
	 * 
	 * @param year
	 * @return
	 */
	public Calendar getLiChunCalendar(int year) {
		SolarTerm stLiChun = new SolarTerm();
		Calendar calLiChun = Calendar.getInstance();
		try {
			calLiChun.setTime(sdf3.parse(stLiChun.getLiChunString(year)));// 找出当年立春时间

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return calLiChun;
	}

	/**
	 * 输入输入日期和当年节气的数组，返回所在节气的位置
	 * 
	 * @param calInput
	 * @param jqStrings
	 * @return
	 */
	public int getPostionOfTheYear(Calendar calInput, String[] jqStrings) {

		int outPut = 0, i = 0;
		String timeString = null;
		Calendar calTime = Calendar.getInstance();// 一个一个取出节气数组中的日期进行比较
		long inputTime = calInput.getTimeInMillis();
		long jqTime = 0;// 一个一个取出节气数组中的日期变为长整形数据进行比较

		for (i = 0; i < jqStrings.length; i++) {

			timeString = jqStrings[i];
			try {
				calTime.setTime(sdf4.parse(timeString));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			jqTime = calTime.getTimeInMillis();

			if (jqTime < inputTime) {
				continue;
			} else {
				break;
			}
		}
		outPut = i - 1;

		return outPut;

	}

	/**
	 * 输入日期，返回校正后的年份
	 * 
	 * @param cal
	 * @return
	 */
	final public int getCheckedYear(Calendar cal) {

		Calendar calLiChun = getLiChunCalendar(cal.get(Calendar.YEAR));
		return (cal.getTimeInMillis() > calLiChun.getTimeInMillis()) ? calLiChun.get(Calendar.YEAR)
				: (calLiChun.get(Calendar.YEAR) - 1);
	}

	/**
	 * 输入日期返回相应的纳音
	 * 
	 * @param cal
	 * @return
	 */
	final public String getNaYinString(Calendar cal) {

		Calendar calLiChun = getLiChunCalendar(cal.get(Calendar.YEAR));
		int year = (cal.getTimeInMillis() > calLiChun.getTimeInMillis()) ? calLiChun.get(Calendar.YEAR)
				: (calLiChun.get(Calendar.YEAR) - 1);

		return NaYin[(year - 1864) % 60];
	}

	// //=============================TestArea=========================================//
	// public static void main(String[] arg) {
	//
	// String birthday = 2014 + "-" + 12 + "-" + 12 + " " + 12;
	// dataCalc.SolarTerm stLiChun = new SolarTerm();
	//
	// Calendar calInput = Calendar.getInstance();
	// try {
	// calInput.setTime(sdf2.parse(birthday));
	// } catch (ParseException e) {
	// e.printStackTrace();
	// }
	//
	// PaiPan pp=new PaiPan(calInput);
	//
	// SolarTerm solarTerm=new SolarTerm();
	// String [] test=solarTerm.getLunarMonthsDateOfTheYear(2014);
	// for(int j=0;j<test.length;j++){
	// System.out.println("Here:"+test[j]);
	// }
	// }

}
