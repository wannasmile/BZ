package com.xsbz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import MyBZ.ActivitiesCollector;
import MyBZ.MyDataBase;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class SavedActivity extends Activity {

	private ListView listView;
	private List<Map<String, String>> list;
	private MyDataBase dbHelper;
	private SQLiteDatabase db;
	private Cursor cursor;
	private String SAVED_INFO = "select * from saved";
	private List<Integer> idList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.show_saved);
		ActivitiesCollector.addActivity(this);

		listView = (ListView) this.findViewById(R.id.lvShowSaved);
		dbHelper = new MyDataBase(this, "bzdb.db", null, 1);
		db = dbHelper.getReadableDatabase();
		cursor = db.rawQuery(SAVED_INFO, null);
		list = new ArrayList<Map<String, String>>();
		idList = new ArrayList<Integer>();

		SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.show_saved_lv,
				new String[] { "name", "gender", "bir_date", "save_date" },
				new int[] { R.id.tvName, R.id.tvGender, R.id.tvBirDate, R.id.tvNowDate });

		while (cursor.moveToNext()) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("name", cursor.getString(1));
			String gender = Integer.parseInt(cursor.getString(2)) == 1 ? "ÄÐ" : "Å®";
			map.put("gender", "£¨" + gender + "£©");
			map.put("bir_date", cursor.getString(4));
			map.put("now_date", cursor.getString(5));
			list.add(0, map);
			idList.add(0, cursor.getInt(0));
		}
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				int ID = idList.get((int) id);
				String sql = "select name,gender,bir_date from saved where id=" + ID + "";
				dbHelper = new MyDataBase(SavedActivity.this, "bzdb.db", null, 1);
				db = dbHelper.getReadableDatabase();
				Cursor cursor=db.rawQuery(sql, null);
				String date = null;
				String gender = null;
				String name = null;
				if (cursor.moveToFirst()) {
					date = cursor.getString(2).toString();
					name = cursor.getString(0).toString();
					gender = cursor.getString(1).toString();
				}
				cursor.close();
				Intent intent = new Intent(SavedActivity.this, PaiPanActivity.class);
				intent.putExtra("name", name);
				intent.putExtra("gender", Integer.parseInt(gender));
				intent.putExtra("date", date);
				startActivity(intent);
			}
		});
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, final long id) {

				AlertDialog.Builder builder = new AlertDialog.Builder(SavedActivity.this);
				builder.setMessage("É¾³ý").setCancelable(false).setPositiveButton("ÊÇ", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						int ID = idList.get((int) id);
						String delete_sql = "delete from saved where id=" + ID + "";
						MyDataBase myDataBase = new MyDataBase(SavedActivity.this, "bzdb.db", null, 1);
						SQLiteDatabase database = myDataBase.getWritableDatabase();
						database.execSQL(delete_sql);
						SavedActivity.this.onCreate(null);
					}
				}).setNegativeButton("·ñ", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						dialog.cancel();
					}
				}).show();
				builder.create();

				return false;
			}
		});

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			ActivitiesCollector.removeActivity(this);
		}
		return super.onKeyDown(keyCode, event);
	}
}
