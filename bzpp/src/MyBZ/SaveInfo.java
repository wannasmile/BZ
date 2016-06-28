package MyBZ;

import java.util.Calendar;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

public class SaveInfo {

	private Calendar cal;
	private EditText etName;
	private Spinner YearSpinner, MonthSpinner, DaySpinner, HourSpinner, MinuteSpinner;
	private MyDataBase dbHelper;
	private SQLiteDatabase db;
	private Context context;
	private RadioButton isMan;
	private int gender = 0;
	private String name;
	private String bir_date;
	private int status = 0;// 默认选择第一种构造方法

	public SaveInfo(Context context, EditText etName, Spinner YearSpinner, Spinner MonthSpinner, Spinner DaySpinner,
			Spinner HourSpinner, Spinner MinuteSpinner, RadioButton isMan) {
		Log.i("tag", "2");
		cal = Calendar.getInstance();
		this.context = context;
		this.etName = etName;
		this.YearSpinner = YearSpinner;
		this.MonthSpinner = MonthSpinner;
		this.DaySpinner = DaySpinner;
		this.HourSpinner = HourSpinner;
		this.MinuteSpinner = MinuteSpinner;
		this.isMan = isMan;
		dbHelper = new MyDataBase(context, "bzdb.db", null, 1);
		db = dbHelper.getWritableDatabase();
		gender = (isMan.isChecked()) ? 1 : 0;
		status = 0;
	}

	public SaveInfo(Context context, String name, String bir_date, int gender) {
		Log.i("tag", "3");
		this.context = context;
		this.name = name;
		this.bir_date = bir_date;
		this.gender = gender;
		cal = Calendar.getInstance();
		dbHelper = new MyDataBase(context, "bzdb.db", null, 1);
		db = dbHelper.getWritableDatabase();
		status = 1;
	}

	public void Save() {
		if (status == 0) {

			String name = etName.getText().toString().length() == 0 ? "姓名" : etName.getText().toString();
			String bir_date = YearSpinner.getSelectedItem().toString() + "-" + MonthSpinner.getSelectedItem().toString()
					+ "-" + DaySpinner.getSelectedItem().toString() + " " + HourSpinner.getSelectedItem().toString()
					+ ":" + MinuteSpinner.getSelectedItem().toString() + ":00";
			String now_date = cal.get(Calendar.YEAR) + "-" + cal.get(Calendar.MONTH) + "-" + cal.get(Calendar.DATE)
					+ " " + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + ":"
					+ cal.get(Calendar.SECOND);
			String sql = "insert into saved(name,gender,bir_date,save_date) values('" + name + "'," + gender + ",'"
					+ bir_date + "','" + now_date + "')";
			db.execSQL(sql);
		} else {
			String now_date = cal.get(Calendar.YEAR) + "-" + cal.get(Calendar.MONTH) + "-" + cal.get(Calendar.DATE)
					+ " " + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + ":"
					+ cal.get(Calendar.SECOND);
			String sql = "insert into saved(name,gender,bir_date,save_date) values('" + name + "'," + gender + ",'"
					+ bir_date + "','" + now_date + "')";
			db.execSQL(sql);
		}
		Toast.makeText(context, "保存成功", Toast.LENGTH_SHORT).show();

	}
}
