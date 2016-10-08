package com.king.mytennis.model;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;


public class MyDataOutputStream extends DataOutputStream{

	public MyDataOutputStream(OutputStream out) {
		super(out);
	}

	public void writeString(String str){
		if (str==null) {
			str="";
		}
		try {
			byte[] b=str.getBytes();//由于中英文编码关系，先获得其字节码可保证不出错
			writeInt(b.length);
			if (b.length!=0) {
				write(b);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
