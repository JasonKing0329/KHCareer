package com.king.mytennis.model;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;


public class MyDataInputStream extends DataInputStream{

	public MyDataInputStream(InputStream in) {
		super(in);
	}
	public String readString(){

		try {
			int length=readInt();
			if (length==0) {
				return "";
			}
			byte[] b=new byte[length];
			for (int i=0;i<length;i++){
				b[i]=readByte();
			}
			/**
			 * 通过测试可用看出，普通的java工程(OperateExcel)在存文件时，对汉字是进行的gb2312编码
			 * 而android工程(本工程)存文件时是用utf-8进行编码
			 * 	     所以，如果读取D:\android_work\test_file下的match.txt(OperateExcel工程所创建),则
			 * 应使用return new String(b,"gb2312");
			 *    若是matchsave.txt(本工程创建),则直接是return new String(b);
			 */
			//return new String(b,"gb2312");
			return new String(b);
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}

}
