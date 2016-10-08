package com.king.mytennis.service;

public class FormatFactory {

	/**
	 * up to MB
	 * @param size
	 * @return
	 */
	public static String formatFileSize(long size) {

		String result = null;
		if (size == 0) {
			return "0KB";
		}

		long temp = size;
		long kb = temp >> 10;
		long mb = temp >> 20;
		if (mb == 0) {
			/*
			 * 方法1:用Math.round计算,这里返回的数字格式的.
				float price=89.89;
				int itemNum=3;
				float totalPrice=price*itemNum;
				float num=(float)(Math.round(totalPrice*100)/100);//如果要求精确4位就*10000然后/10000

				 方法2:用DecimalFormat 返回的是String格式的.该类对十进制进行全面的封装.像%号,千分位,小数精度.科学计算.
				float price=1.2;
				DecimalFormat decimalFormat=new DecimalFormat(".00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
				String p=decimalFomat.format(price);//format 返回的是字符串
			 */
			if (kb == 0) {
				result = size + "B";
			}
			else {
				float point = (float)(temp%1024)/1024f;
				int nPoint = (int) (100 * point);
				if (nPoint < 10) {
					result = kb + ".0" + nPoint + "KB";
				}
				else {
					result = kb + "." + nPoint + "KB";
				}
			}
		}
		else {
			float point = (float)(temp%(mb<<20))/(float)(2<<20);
			int nPoint = (int) (100 * point);
			if (nPoint < 10) {
				result = mb + ".0" + nPoint + "MB";
			}
			else {
				result = mb + "." + nPoint + "MB";
			}
		}
		return result;
	}
}
