package com.xsbz;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import com.xsbz.R;
import com.xsbz.views.ArcMenu;

import MyBZ.ActivitiesCollector;
import MyBZ.Libs;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.TextView;

public class PaiPanActivity extends Activity {

	private static int DAYUN_COUNT = 8;// 设置大运的数量为：8
	TextView baseInfo, yearGanZhi, monthGanZhi, dayGanZhi, hourGanZhi, kongwang, dayunLabelString, liunianLabelString,
			tvName;
	private String[] dayunLabel = new String[DAYUN_COUNT];// 存放各个大运的年数
	private ArrayList<TextView> sizhu;
	private ArrayList<TextView> dayun;
	private ArrayList<TextView> liunian = new ArrayList<TextView>();
	private ArrayList<TextView> liuyue = new ArrayList<TextView>();
	private String[] liunianStrings = null;
	private String[] liuyueStrings = null;

	private ArcMenu mArcMenu;

	static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH");
	static SimpleDateFormat sdf4 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static SimpleDateFormat sdf5 = new SimpleDateFormat("yyyy HH");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.paipan);
		ActivitiesCollector.addActivity(this);

		mArcMenu = (ArcMenu) this.findViewById(R.id.id_menu);
		// 获取对象
		sizhu = getSiZhuID();
		dayun = getDaYunID();
		liunian = getLiuNianID();
		liuyue = getLiuYueID();

		liunianLabelString = (TextView) this.findViewById(R.id.tvLiuNianLabel);

		tvName = (TextView) this.findViewById(R.id.tvName);
		baseInfo = (TextView) this.findViewById(R.id.tvBaseInfo);
		dayunLabelString = (TextView) this.findViewById(R.id.tvDaYunLabel);

		Intent intent = (Intent) this.getIntent();

		Calendar calInput = Libs.getCalendar(intent);// 返回相应的日期

		Libs.LoadBasicInfo(calInput, intent, tvName, baseInfo);// 加载基本信息
		Libs.LoadBaZi(calInput, sizhu);// 加载八字以及空亡
		Libs.LoadDaYun(calInput, intent, dayunLabelString, dayunLabel, dayun);// 加载大运

		// 初始化流年、流月
		Libs.initLiuNian(dayun, liunian, liunianLabelString, dayunLabel);
		Libs.initLiuYue(liunian, dayunLabel, liuyueStrings, liuyue);
		// 给每个大运和流年设置点击事件
		Libs.setClickDaYun(dayun, liunian, liunianLabelString, dayunLabel);
		Libs.setClickLiuNian(liunian, liuyue, liunianStrings, dayunLabel);

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			ActivitiesCollector.removeActivity(this);
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (mArcMenu.isOpen())
			mArcMenu.toggleMenu(200);
		return super.onTouchEvent(event);
	}

	private final ArrayList<TextView> getSiZhuID() {
		ArrayList<TextView> sizhu = new ArrayList<TextView>();
		sizhu.add((TextView) this.findViewById(R.id.tvNianGanZhi));
		sizhu.add((TextView) this.findViewById(R.id.tvYueGanZhi));
		sizhu.add((TextView) this.findViewById(R.id.tvRiGanZhi));
		sizhu.add((TextView) this.findViewById(R.id.tvShiGanZhi));
		sizhu.add((TextView) this.findViewById(R.id.tvKongWang));
		return sizhu;
	}

	private final ArrayList<TextView> getDaYunID() {
		ArrayList<TextView> dayun = new ArrayList<TextView>();
		dayun.add((TextView) this.findViewById(R.id.tvDaYun_01));
		dayun.add((TextView) this.findViewById(R.id.tvDaYun_02));
		dayun.add((TextView) this.findViewById(R.id.tvDaYun_03));
		dayun.add((TextView) this.findViewById(R.id.tvDaYun_04));
		dayun.add((TextView) this.findViewById(R.id.tvDaYun_05));
		dayun.add((TextView) this.findViewById(R.id.tvDaYun_06));
		dayun.add((TextView) this.findViewById(R.id.tvDaYun_07));
		dayun.add((TextView) this.findViewById(R.id.tvDaYun_08));
		return dayun;
	}

	private final ArrayList<TextView> getLiuNianID() {
		ArrayList<TextView> liunian = new ArrayList<TextView>();
		liunian.add((TextView) this.findViewById(R.id.tvLiuNian_01));
		liunian.add((TextView) this.findViewById(R.id.tvLiuNian_02));
		liunian.add((TextView) this.findViewById(R.id.tvLiuNian_03));
		liunian.add((TextView) this.findViewById(R.id.tvLiuNian_04));
		liunian.add((TextView) this.findViewById(R.id.tvLiuNian_05));
		liunian.add((TextView) this.findViewById(R.id.tvLiuNian_06));
		liunian.add((TextView) this.findViewById(R.id.tvLiuNian_07));
		liunian.add((TextView) this.findViewById(R.id.tvLiuNian_08));
		liunian.add((TextView) this.findViewById(R.id.tvLiuNian_09));
		liunian.add((TextView) this.findViewById(R.id.tvLiuNian_10));
		return liunian;
	}

	private final ArrayList<TextView> getLiuYueID() {
		ArrayList<TextView> liuyue = new ArrayList<TextView>();
		liuyue.add((TextView) this.findViewById(R.id.tvLiuYue_01));
		liuyue.add((TextView) this.findViewById(R.id.tvLiuYue_02));
		liuyue.add((TextView) this.findViewById(R.id.tvLiuYue_03));
		liuyue.add((TextView) this.findViewById(R.id.tvLiuYue_04));
		liuyue.add((TextView) this.findViewById(R.id.tvLiuYue_05));
		liuyue.add((TextView) this.findViewById(R.id.tvLiuYue_06));
		liuyue.add((TextView) this.findViewById(R.id.tvLiuYue_07));
		liuyue.add((TextView) this.findViewById(R.id.tvLiuYue_08));
		liuyue.add((TextView) this.findViewById(R.id.tvLiuYue_09));
		liuyue.add((TextView) this.findViewById(R.id.tvLiuYue_10));
		liuyue.add((TextView) this.findViewById(R.id.tvLiuYue_11));
		liuyue.add((TextView) this.findViewById(R.id.tvLiuYue_12));
		return liuyue;
	}

}