package MyBZ;

import java.util.ArrayList;

import android.view.View;
import android.widget.TextView;

public class ClickDaYun implements View.OnClickListener {

	private ArrayList<TextView> dayun, liunian;
	private TextView tv, liunianLabelString;
	private String[] dayunLabel;
	private int i;

	public ClickDaYun(ArrayList<TextView> dayun, ArrayList<TextView> liunian, TextView tv,
			TextView liunianLabelString, String[] dayunLabel, int i) {
		// TODO Auto-generated constructor stub
		this.dayun = dayun;
		this.liunian = liunian;
		this.tv = tv;
		this.liunianLabelString = liunianLabelString;
		this.dayunLabel = dayunLabel;
		this.i = i;

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Libs.removeColor(dayun);
		tv.setTextColor(0xFFFF0000);
		int k = Integer.parseInt(dayunLabel[i]);
		liunianLabelString.setText(k + "    " + (k + 1) + "    " + (k + 2) + "    " + (k + 3) + "    " + (k + 4)
				+ "    " + (k + 5) + "    " + (k + 6) + "    " + (k + 7) + "    " + (k + 8) + "    " + (k + 9));

		// Ìî³äÃ¿¸öTextView
		Libs.setTextViewText(liunian, Libs.getLiuNianStrings(k));
	}

}
