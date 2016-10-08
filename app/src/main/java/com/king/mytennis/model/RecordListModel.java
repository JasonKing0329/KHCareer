package com.king.mytennis.model;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.king.mytennis.multiuser.MultiUserManager;

public class RecordListModel {

	public RecordListModel() {

	}
	////////////在listview下面显示的统计信息
	public String getCountInfor(List<Record> list) {

		int count=list.size();
		int win=0,lose=0;
		int yearcount=0;
		int yearwin=0, yearlose=0;

		long yearDateStart=0;
		int lastyear=Calendar.getInstance().get(Calendar.YEAR)-1;
		try {
			yearDateStart=new SimpleDateFormat("yyyy-MM-dd").parse(lastyear+"-12-31").getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		boolean isWinner = false;
		for (Record record:list){
			isWinner = MultiUserManager.USER_DB_FLAG.equals(record.getWinner());
			if (record.getLongDate() > yearDateStart) {
				yearcount++;
				if (isWinner) {
					win++;
					yearwin++;
				}
				else {
					lose++;
					yearlose++;
				}
			}
			else {
				if (isWinner) {
					win++;
				}
				else {
					lose++;
				}
			}
		}

		float rate=(float)win/(float)count;
		float yrate=(float)yearwin/(float)yearcount;
		DecimalFormat format = new DecimalFormat(".00");

		StringBuilder builder = new StringBuilder("Total ");
		builder.append("(").append(win).append("/").append(lose)
				.append(")  rate:").append(format.format(rate*100)).append("%\n").append(lastyear+1)
				.append(" ").append("(").append(yearwin)
				.append("/").append(yearlose).append(")  rate:").append(format.format(yrate*100)).append("%");

		return builder.toString();
	}

}
