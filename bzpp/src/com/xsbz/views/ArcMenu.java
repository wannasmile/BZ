package com.xsbz.views;

import java.util.List;

import com.xsbz.R;
import com.xsbz.SavedActivity;

import MyBZ.ActivitiesCollector;
import MyBZ.SaveInfo;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

public class ArcMenu extends ViewGroup implements OnClickListener {

	private static final int POS_LEFT_TOP = 0;
	private static final int POS_LEFT_BOTTOM = 1;
	private static final int POS_RIGHT_TOP = 2;
	private static final int POS_RIGHT_BOTTOM = 3;

	// 进行保存信息时需要获取到各个控件的值
	private EditText etName;
	private Spinner YearSpinner, MonthSpinner, DaySpinner, HourSpinner, MinuteSpinner;
	private RadioButton isMan;

	private Position mPosition = Position.RIGHT_TOP;
	private int mRadius;
	/**
	 * 菜单的状态
	 */
	private Status mCurrentStatus = Status.CLOSE;
	/**
	 * 菜单的主按钮
	 */
	private View mCButton;
	private OnMenuItemClickListener mMenuItemClickListener;

	/**
	 * 点击主菜单项的回调接口
	 */
	public interface OnMenuItemClickListener {
		void onClick(View view, int pos);
	}

	public void setOnMenuItemClickListener(OnMenuItemClickListener mMenuItemClickListener) {
		this.mMenuItemClickListener = mMenuItemClickListener;
	}

	public enum Status {
		OPEN, CLOSE
	}

	/**
	 * 菜单的位置枚举类
	 */
	public enum Position {
		LEFT_TOP, LEFT_BOTTOM, RIGHT_TOP, RIGHT_BOTTOM
	}

	public ArcMenu(Context context) {
		this(context, null);
	}

	public ArcMenu(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ArcMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());

		// 获取自定义属性的值
		TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ArcMenu, defStyle, 0);

		int pos = array.getInt(R.styleable.ArcMenu_position, POS_RIGHT_TOP);

		switch (pos) {
		case POS_LEFT_TOP:
			mPosition = Position.LEFT_TOP;
			break;
		case POS_LEFT_BOTTOM:
			mPosition = Position.LEFT_BOTTOM;
			break;
		case POS_RIGHT_TOP:
			mPosition = Position.RIGHT_TOP;
			break;
		case POS_RIGHT_BOTTOM:
			mPosition = Position.RIGHT_BOTTOM;
			break;
		}
		mRadius = (int) array.getDimension(R.styleable.ArcMenu_radius,
				TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics()));

		// Log.i("TAG", "position=" + mPosition + ",radius=" + mRadius);

		array.recycle();

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int count = getChildCount();
		for (int i = 0; i < count; i++) {
			// 测量child
			measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if (changed) {
			layoutCButton();

			int count = getChildCount();

			for (int i = 0; i < count - 1; i++) {

				View child = getChildAt(i + 1);

				child.setVisibility(View.GONE);

				int cl = (int) (mRadius * Math.sin(Math.PI / 2 / (count - 2) * i));
				int ct = (int) (mRadius * Math.cos(Math.PI / 2 / (count - 2) * i));

				int cWidth = child.getMeasuredWidth();
				int cHeight = child.getMeasuredHeight();

				// 如果菜单位置在底部 左下、右下
				if (mPosition == Position.LEFT_BOTTOM || mPosition == Position.RIGHT_BOTTOM) {

					ct = getMeasuredHeight() - cHeight - ct;
				}

				// 如果菜单位置在底部右上、右下
				if (mPosition == Position.RIGHT_TOP || mPosition == Position.RIGHT_BOTTOM) {

					cl = getMeasuredWidth() - cWidth - cl;
				}

				child.layout(cl, ct, cl + cWidth, ct + cHeight);

			}
		}

	}

	/**
	 * 定位主菜单按钮
	 */
	private void layoutCButton() {

		mCButton = getChildAt(0);
		mCButton.setOnClickListener(this);

		int l = 0;
		int t = 0;

		int width = mCButton.getMeasuredWidth();
		int height = mCButton.getMeasuredHeight();
		switch (mPosition) {
		case LEFT_TOP:
			l = 0;
			t = 0;
			break;
		case LEFT_BOTTOM:
			l = 0;
			t = getMeasuredHeight() - height;
			break;
		case RIGHT_TOP:
			l = getMeasuredWidth() - width;
			t = 0;
			break;
		case RIGHT_BOTTOM:
			l = getMeasuredWidth() - width;
			t = getMeasuredHeight() - height;
			break;
		}

		mCButton.layout(l, t, l + width, t + height);
	}

	@Override
	public void onClick(View v) {

		rotateCButton(v, 0f, -720f, 300);

		toggleMenu(300);

	}

	/**
	 * 切换菜单
	 */
	public void toggleMenu(int duration) {

		// 为menuItem添加动画和旋转动画
		int count = getChildCount();
		for (int i = 0; i < count - 1; i++) {
			final View childView = getChildAt(i + 1);
			childView.setVisibility(View.VISIBLE);

			// end 0,0
			// start
			int cl = (int) (mRadius * Math.sin(Math.PI / 2 / (count - 2) * i));
			int ct = (int) (mRadius * Math.cos(Math.PI / 2 / (count - 2) * i));

			int xflag = 1;
			int yflag = 1;

			if (mPosition == Position.LEFT_TOP || mPosition == Position.LEFT_BOTTOM) {
				xflag = -1;
			}

			if (mPosition == Position.LEFT_TOP || mPosition == Position.RIGHT_TOP) {
				yflag = -1;
			}

			AnimationSet animset = new AnimationSet(true);
			Animation tranAnim = null;

			// to open
			if (mCurrentStatus == Status.CLOSE) {
				tranAnim = new TranslateAnimation(xflag * cl, 0, yflag * ct, 0);
				childView.setClickable(true);
				childView.setFocusable(true);
			} else {// to close
				tranAnim = new TranslateAnimation(0, xflag * cl, 0, yflag * ct);
				childView.setClickable(false);
				childView.setFocusable(false);
			}
			tranAnim.setFillAfter(true);
			tranAnim.setDuration(duration);
			tranAnim.setStartOffset((i * 240) / count);

			tranAnim.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {

				}

				@Override
				public void onAnimationRepeat(Animation animation) {

				}

				@Override
				public void onAnimationEnd(Animation animation) {

					if (mCurrentStatus == Status.CLOSE) {
						childView.setVisibility(View.GONE);
					}

				}
			});

			// 旋转动画
			RotateAnimation rotateAnimation = new RotateAnimation(0, -720, Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			rotateAnimation.setDuration(duration);
			rotateAnimation.setFillAfter(true);

			animset.addAnimation(rotateAnimation);
			animset.addAnimation(tranAnim);

			childView.startAnimation(animset);

			final int pos = i + 1;
			childView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (mMenuItemClickListener != null)
						mMenuItemClickListener.onClick(childView, pos);
					menuItemAnim(pos - 1);
					changeMenuStatus();

				}
			});

		}
		// 切换菜单状态
		changeMenuStatus();

	}

	/**
	 * 添加menuItem的点击动画
	 */
	private void menuItemAnim(int pos) {

		for (int i = 0; i < getChildCount() - 1; i++) {

			View childView = getChildAt(i + 1);
			if (i == pos) {
				childView.startAnimation(scaleBigAnim(300));
				if (childView.getTag().toString().equals("save")) {

					// 获取所有的Activity
					List<Activity> activities = ActivitiesCollector.getActivityList();

					Activity activity = (Activity) childView.getContext();// 获取当前的Context

					// 如果当前activity是IndexActivity,则使用控件的值来保存
					if (activities.get(activities.size() - 1).getLocalClassName().equals("IndexActivity")) {
						Log.i("tag", "1");
						// 获得控件
						etName = (EditText) activity.findViewById(R.id.etName);
						isMan = (RadioButton) activity.findViewById(R.id.rbMen);
						YearSpinner = (Spinner) activity.findViewById(R.id.sYear);
						MonthSpinner = (Spinner) activity.findViewById(R.id.sMonth);
						DaySpinner = (Spinner) activity.findViewById(R.id.sDay);
						HourSpinner = (Spinner) activity.findViewById(R.id.sHour);
						MinuteSpinner = (Spinner) activity.findViewById(R.id.sMinute);

						// 保存数据
						SaveInfo saveInfo = new SaveInfo(activity, etName, YearSpinner, MonthSpinner, DaySpinner,
								HourSpinner, MinuteSpinner, isMan);
						saveInfo.Save();

					} else if (activities.get(activities.size() - 1).getLocalClassName().equals("PaiPanActivity")) {

						// 如果当前的Activity是PaiPanActivity则使用SaveInfo的另一个构造方法进行保存
						Intent intent = (Intent) activities.get(activities.size() - 1).getIntent();
						String name = intent.getStringExtra("name");
						String bir_date = intent.getStringExtra("date");
						int gender = intent.getIntExtra("gender", 0);
						SaveInfo saveInfo = new SaveInfo(activity, name, bir_date, gender);
						saveInfo.Save();

					}

				} else if (childView.getTag().toString().equals("history")) {
					Activity activity = (Activity) childView.getContext();
					Intent intent = new Intent(activity, SavedActivity.class);
					activity.startActivity(intent);
				} else {
					ActivitiesCollector.finishAll();
				}
			} else {
				childView.startAnimation(scaleSmallAnim(300));
			}
			childView.setClickable(false);
			childView.setFocusable(false);
		}

	}

	private Animation scaleSmallAnim(int duration) {
		AnimationSet animationSet = new AnimationSet(true);

		ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);

		AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0.0f);
		animationSet.addAnimation(scaleAnimation);
		animationSet.addAnimation(alphaAnimation);

		animationSet.setDuration(duration);
		animationSet.setFillAfter(true);

		return animationSet;
	}

	/**
	 * 为当前点击的Item设置变大和透明度降低的动画
	 * 
	 * @param duration
	 * @return
	 */
	private Animation scaleBigAnim(int duration) {

		AnimationSet animationSet = new AnimationSet(true);

		ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 4.0f, 1.0f, 4.0f, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);

		AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0.0f);
		animationSet.addAnimation(scaleAnimation);
		animationSet.addAnimation(alphaAnimation);

		animationSet.setDuration(duration);
		animationSet.setFillAfter(true);

		return animationSet;
	}

	/**
	 * 切换菜单状态
	 */
	private void changeMenuStatus() {

		mCurrentStatus = (mCurrentStatus == Status.CLOSE ? Status.OPEN : Status.CLOSE);

	}

	public boolean isOpen() {

		return mCurrentStatus == Status.OPEN;
	}

	private void rotateCButton(View v, float start, float end, int duration) {

		RotateAnimation animation = new RotateAnimation(start, end, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);

		animation.setDuration(duration);
		animation.setFillAfter(true);
		v.startAnimation(animation);
	}

}
