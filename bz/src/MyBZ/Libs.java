package MyBZ;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

public class Libs {

	private static int BASEYEAR = 1900;
	private static int ALLYEAR = 201;
	final static String[] Gan = new String[] { "甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸" };
	final static String[] Zhi = new String[] { "子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥" };
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy HH:mm");

	public Libs() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 输入传来的Intent，返回相应的日期
	 * 
	 * @param intent
	 * @return
	 */
	public static Calendar getCalendar(Intent intent) {

		String date=intent.getStringExtra("date");
		Calendar calInput = Calendar.getInstance();
		try {
			calInput.setTime(sdf.parse(date));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return calInput;
	}

	/**
	 * 制作生日的下拉数据集
	 * 
	 * @param isSolar
	 * @param year
	 * @param month
	 * @return
	 */
	final public static List<String> getDayAdapterData(Boolean isSolar, int year, int month) {

		List<String> listDay = new ArrayList<String>();

		if (isSolar) {// 产生阳历日数据集
			if (isLeapYear(year)) {
				if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
					for (int i = 1; i < 32; i++)
						listDay.add(String.valueOf(i));
				} else if (month == 4 || month == 6 || month == 9 || month == 11) {
					for (int i = 1; i < 31; i++)
						listDay.add(String.valueOf(i));
				} else {
					for (int i = 1; i < 30; i++)
						listDay.add(String.valueOf(i));
				}
			} else {
				if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
					for (int i = 1; i < 32; i++)
						listDay.add(String.valueOf(i));
				} else if (month == 4 || month == 6 || month == 9 || month == 11) {
					for (int i = 1; i < 31; i++)
						listDay.add(String.valueOf(i));
				} else {
					for (int i = 1; i < 29; i++)
						listDay.add(String.valueOf(i));
				}
			}

		} else {// 产生阴历日的数据集
			SolarTerm solarTerm = new SolarTerm();
			String[] days = solarTerm.getLunarDaysOfTheMonth(year, month);
			for (int i = 0; i < days.length && days[i] != null; i++) {
				listDay.add(days[i]);
			}
		}
		// Log.e("yueLong", listDay.toArray().length+"");
		return listDay;
	}

	/**
	 * 输入具体生日，返回起运生日
	 * 
	 * @param cal
	 * @param gender
	 * @param yeargan
	 * @return
	 */
	public static Calendar getDaYunStartCalendar(Calendar cal, int gender, String yeargan) {

		PaiPan paiPan = new PaiPan(cal);
		Calendar calendar = Calendar.getInstance();
		long result;//获取毫秒数

		String[] jieqis = paiPan.getWholeYearJieQis(paiPan.getCheckedYear(cal));

		int jieqiPos = paiPan.getPostionOfTheYear(cal, jieqis);

		int dir = paiPan.getPaiDaYunDir(gender, yeargan);

		if (dir == 1) {

			try {
				calendar.setTime(sdf.parse(jieqis[jieqiPos + 1]));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			result = calendar.getTimeInMillis() - cal.getTimeInMillis();
		} else {
			try {
				calendar.setTime(sdf.parse(jieqis[jieqiPos]));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			result = cal.getTimeInMillis() - calendar.getTimeInMillis();
		}
		result = (result / 7200000L) / 3;

		cal.add(Calendar.MONTH, (int) result);
		return cal;
	}

	/**
	 * @param dayun
	 * @param liunian
	 * @param liunianLabelString
	 * @param dayunLabel
	 */
	public static void initLiuNian(ArrayList<TextView> dayun, ArrayList<TextView> liunian, TextView liunianLabelString,
			String[] dayunLabel) {
		dayun.get(0).setTextColor(0xFFFF0000);

		liunianLabelString.setText((Integer.parseInt(dayunLabel[0])) + "    " + (Integer.parseInt(dayunLabel[0]) + 1)
				+ "    " + (Integer.parseInt(dayunLabel[0]) + 2) + "    " + (Integer.parseInt(dayunLabel[0]) + 3)
				+ "    " + (Integer.parseInt(dayunLabel[0]) + 4) + "    " + (Integer.parseInt(dayunLabel[0]) + 5)
				+ "    " + (Integer.parseInt(dayunLabel[0]) + 6) + "    " + (Integer.parseInt(dayunLabel[0]) + 7)
				+ "    " + (Integer.parseInt(dayunLabel[0]) + 8) + "    " + (Integer.parseInt(dayunLabel[0]) + 9));

		// 填充每个TextView
		Libs.setTextViewText(liunian, getLiuNianStrings(Integer.parseInt(dayunLabel[0])));
	}

	/**
	 * @param liunian
	 * @param dayunLabel
	 * @param liuyueStrings
	 * @param liuyue
	 */
	public static void initLiuYue(ArrayList<TextView> liunian, String[] dayunLabel, String[] liuyueStrings,
			ArrayList<TextView> liuyue) {

		liunian.get(0).setTextColor(0xFFFF0000);
		liuyueStrings = Libs
				.getLiuYueStrings(String.valueOf(getLiuNianStrings(Integer.parseInt(dayunLabel[0]))[0].charAt(0)));
		// 填充每个TextView
		setTextViewText(liuyue, liuyueStrings);
	}

	/**
	 * 输入年份，返回该大运中的十年
	 * 
	 * @param year
	 * @return
	 */
	public static String[] getLiuNianStrings(int year) {

		int y = year - 1864;
		String[] outPut = new String[10];

		for (int i = 0; i < 10; i++, y++) {
			outPut[i] = (Gan[y % 10] + Zhi[y % 12]);
		}
		return outPut;
	}

	/**
	 * 输入年份，返回该年中的十二个月
	 * 
	 * @param mGan
	 * @return
	 */
	public static String[] getLiuYueStrings(String mGan) {

		// 创建当年的所有月的干支字符数组monthGanZhis
		String[] outPut = new String[12];

		Calendar calendar = Calendar.getInstance();
		PaiPan paiPan = new PaiPan(calendar);

		String monthGan = "丙戊庚壬甲";
		// 获得的位置对5求余来源于：甲己对应丙，乙庚对应戊，丙辛对应庚，，，以此类推
		String monBeginGan = String.valueOf(monthGan.charAt(paiPan.getGanPosition(mGan) % 5));
		int gan = paiPan.getGanPosition(monBeginGan);
		int zhi = paiPan.getZhiPosition("寅");

		for (int i = 0; i < 12; i++) {
			outPut[i] = Gan[(gan++) % 10] + Zhi[(zhi++) % 12];
		}
		return outPut;
	}

	/**
	 * 制作生时的下拉数据集
	 * 
	 * @return
	 */
	final public static List<String> getHourAdapterData() {
		List<String> listHour = new ArrayList<String>();
		int baseHour = 0;
		for (int i = 0; i < 24; i++) {
			String aa = String.valueOf(baseHour++);
			listHour.add(aa);
		}
		return listHour;
	}

	/**
	 * 返回获取的输入时间
	 * 
	 * @param selectYear
	 * @param selectMonth
	 * @param selectDay
	 * @param selectHour
	 * @return
	 */
	public static Calendar getInputCalendar(String selectYear, String selectMonth, String selectDay, String selectHour,
			String selectMinute) {
		String birthday = selectYear + "-" + selectMonth + "-" + selectDay + " " + selectHour + ":" + selectMinute
				+ ":00";

		Calendar calInput = Calendar.getInstance();
		try {
			calInput.setTime(sdf.parse(birthday));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return calInput;
	}

	/**
	 * 输入阴历日期，返回对应的阳历日期
	 * 
	 * @param selectYear
	 * @param selectMonth
	 * @param selectDay
	 * @param selectHour
	 * @return
	 */
	public static Calendar getInputSolarCalendar(String selectYear, String selectMonth, String selectDay,
			String selectHour, String selectMinute) {

		Calendar calInput = Calendar.getInstance();
		String dateInput = selectYear + "年" + selectMonth + selectDay;
		boolean same = true;
		try {
			calInput.setTime(sdf1.parse(selectYear + " " + selectHour + ":" + selectMinute));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		while (same) {
			SolarMode solarMode = new SolarMode(calInput);

			// Log.e("calInput", solarMode.getLunarString());
			// Log.e("dateInput", dateInput);

			if (solarMode.getLunarString().equals(dateInput)) {
				same = false;
			} else {
				calInput.add(Calendar.DATE, 1);
			}
		}
		return calInput;
	}

	/**
	 * 制作生分的下拉数据集
	 * 
	 * @return
	 */
	public static List<String> getMinuteAdapterData() {
		// TODO Auto-generated method stub
		List<String> listHour = new ArrayList<String>();
		int baseMinute = 0;
		for (int i = 0; i < 60; i++) {
			String aa = String.valueOf(baseMinute++);
			listHour.add(aa);
		}
		return listHour;
	}

	/**
	 * 制作月份的下拉数据集,只有农历的时候才用得上第二个参数
	 * 
	 * @param isSolar
	 * @param year
	 * @return
	 */
	final public static List<String> getMonthAdapterData(Boolean isSolar, int year) {
		List<String> listMonth = new ArrayList<String>();
		if (isSolar) {
			int baseMonth = 1;
			for (int i = 0; i < 12; i++) {
				String aa = String.valueOf(baseMonth++);
				listMonth.add(aa);
			}
		} else {
			SolarTerm solarTerm = new SolarTerm();
			String[] months = solarTerm.getLunarMonthsOfTheYear(year);
			for (int i = 0; i < months.length && months[i] != null; i++) {
				listMonth.add(months[i]);
			}
		}

		return listMonth;
	}

	/**
	 * 制作年份的下拉数据集
	 * 
	 * @return
	 */
	final public static List<String> getYearAdapterData() {
		List<String> listYear = new ArrayList<String>();
		int baseYear = BASEYEAR;
		for (int i = 0; i < ALLYEAR; i++) {
			String aa = String.valueOf(baseYear++);
			listYear.add(aa);
		}
		return listYear;
	}

	/**
	 * 传入日期和传来的Intent，打印相应的基本信息
	 * 
	 * @param calendar
	 * @param intent
	 */
	public static void LoadBasicInfo(Calendar calendar, Intent intent, TextView tvName, TextView baseInfo) {

		PaiPan paipan = new PaiPan(calendar);
		SolarMode solarMode = new SolarMode(calendar);

		String name = intent.getStringExtra("name");
		int gender = intent.getIntExtra("gender", 0);
		String nayin = paipan.getNaYinString(calendar);

		String genderString = (gender == 1) ? "男" : "女";
		String solarString = calendar.get(Calendar.YEAR) + "年" + (calendar.get(Calendar.MONTH) + 1) + "月"
				+ calendar.get(Calendar.DATE) + "日" + calendar.get(Calendar.HOUR_OF_DAY) + "时"+calendar.get(Calendar.MINUTE)+"分";
		String lunarString = solarMode.getLunarString() + calendar.get(Calendar.HOUR_OF_DAY) + "时"+calendar.get(Calendar.MINUTE)+"分";
		tvName.setText("姓名：" + name + "  性别：" + genderString);
		baseInfo.setText("阳历为：" + solarString + "\n" + "阴历为：" + lunarString + "\n" + "纳音为：" + nayin);
	}

	/**
	 * 返回是否是闰年
	 * 
	 * @param year
	 * @return 返回是否为闰年
	 */
	final public static Boolean isLeapYear(int year) {

		return (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) ? true : false;
	}

	/**
	 * 排八字函数（含空亡）
	 * 
	 * @param cal
	 * @param sizhu
	 */
	public static void LoadBaZi(Calendar cal, ArrayList<TextView> sizhu) {

		PaiPan paipan = new PaiPan(cal);

		String sizhuString = paipan.getSiZhuString();

		sizhu.get(0).setText(paipan.getSiZhuString().charAt(0) + "" + paipan.getSiZhuString().charAt(1));
		sizhu.get(1).setText(paipan.getSiZhuString().charAt(2) + "" + paipan.getSiZhuString().charAt(3));
		sizhu.get(2).setText(paipan.getSiZhuString().charAt(4) + "" + paipan.getSiZhuString().charAt(5));
		sizhu.get(3).setText(paipan.getSiZhuString().charAt(6) + "" + paipan.getSiZhuString().charAt(7));
		sizhu.get(4).setText("（" + paipan.getKWString(sizhuString.charAt(4) + "" + sizhuString.charAt(5)) + "空）");
	}

	/**
	 * 加载大运
	 * 
	 * @param calendar
	 * @param intent
	 * @param dayunLabel
	 * @param dayun
	 */
	public static void LoadDaYun(Calendar calendar, Intent intent, TextView dayunLabelString, String[] dayunLabel,
			ArrayList<TextView> dayun) {

		PaiPan paipan = new PaiPan(calendar);
		String sizhu = paipan.getSiZhuString();

		int gender = intent.getIntExtra("gender", 0);
		// 起运时间
		Calendar cal_for_dayun=Libs.getDaYunStartCalendar(calendar, gender, String.valueOf(paipan.getSiZhuString().charAt(0)));
		//int DAYUNLABEL = cal_for_dayun.get(Calendar.YEAR);
		int DAYUNSTART=cal_for_dayun.get(Calendar.YEAR);//+"."+(cal_for_dayun.get(Calendar.MONTH)+1);
		
		//初始化大运标签时，标记起运的月份
		String DAYUN_FIRST=cal_for_dayun.get(Calendar.YEAR)+"."+(cal_for_dayun.get(Calendar.MONTH)+1);
		dayunLabel[0] = String.valueOf(DAYUNSTART);
		for (int k = 1; k < 8; k++) {
			dayunLabel[k] = String.valueOf(Integer.parseInt(dayunLabel[k - 1]) + 10);
		}
		// 显示大运
		dayunLabelString.setText(DAYUN_FIRST + "       " + dayunLabel[1] + "       " + dayunLabel[2] + "        "
				+ dayunLabel[3] + "       " + dayunLabel[4] + "       " + dayunLabel[5] + "      " + dayunLabel[6]
				+ "       " + dayunLabel[7]);
		String[] dayuns = paipan.getDaYunString(gender, sizhu.charAt(0) + "" + sizhu.charAt(1),
				sizhu.charAt(2) + "" + sizhu.charAt(3));
		int i = 0;
		for (TextView t : dayun)
			t.setText(dayuns[i++].toString());
	}

	/**
	 * 传入对象数组，将其中所有的数组设置为无色
	 * 
	 * @param tv
	 */
	public static void removeColor(ArrayList<TextView> textViews) {
		for (TextView t : textViews)
			t.setTextColor(0xFF000000);
	}

	/**
	 * @param dayun
	 * @param liunian
	 * @param liunianLabelString
	 * @param dayunLabel
	 */
	public static void setClickDaYun(ArrayList<TextView> dayun, ArrayList<TextView> liunian,
			TextView liunianLabelString, String[] dayunLabel) {
		// TODO Auto-generated method stub
		int i = 0;
		for (TextView t : dayun)
			t.setOnClickListener(new ClickDaYun(dayun, liunian, t, liunianLabelString, dayunLabel, i++));
	}

	/**
	 * 填充文字
	 * 
	 * @param textViews
	 *            对象数组
	 * @param strings
	 *            文字数组
	 */
	public static void setTextViewText(ArrayList<TextView> textViews, String[] strings) {
		int i = 0;
		for (TextView t : textViews)
			t.setText(strings[i++]);
	}

	public static void setClickLiuNian(ArrayList<TextView> liunian, ArrayList<TextView> liuyue, String[] liunianStrings,
			String[] dayunLabel) {
		// TODO Auto-generated method stub

		liunianStrings = getLiuNianStrings(Integer.parseInt(dayunLabel[0]));
		int i = 0;
		for (TextView t : liunian)
			t.setOnClickListener(new ClickLiuNian(t, liunian, liuyue, liunianStrings, i++));
	}
}
