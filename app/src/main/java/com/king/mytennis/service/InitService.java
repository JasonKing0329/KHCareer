package com.king.mytennis.service;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;

import com.king.mytennis.model.Configuration;
import com.king.mytennis.model.ImageFactory;
import com.king.mytennis.model.Record;
import com.king.mytennis.model.FileIO;

public class InitService {

	private FileIO daoFile;
	
	public InitService() {
		daoFile = new FileIO();
	}

	public Bitmap loadBackgound() {
		return new ImageFactory().getDefBackground();
	}

	public void loadConfiguration() {

		Configuration.setInstance(daoFile.readConfigInfor());
	}

	public void loadDatabase(Context context, ArrayList<Record> list) {

		RecordService service = new RecordService(context);
		service.initTableRecord(list);
		service.initTableID(list.size());
	}
	
}
