package com.king.mytennis.service;

import com.king.mytennis.model.ImageFactory;

import android.graphics.Bitmap;

public class RecordEditorService {

	public RecordEditorService() {

	}

	public Bitmap loadBackgound() {
		return new ImageFactory().getDefBackground();
	}
}
