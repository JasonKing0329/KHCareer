/**
 * 一个困扰了很久的大问题，一定要引起注意。
 * 在用FileInputStream和FileOutputStream时，明明目录文件是一样的，但总是Input行，而Output就
 * 抛FileNotFoundException
 * 原因是Output以及创建文件的操作需要在manifest文件中指定权限！！！！！！！！！！
 * 以后遇到类似的问题一定要往这方面想，不然真是浪费好多时间
 */
package com.king.khcareer.model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.king.khcareer.common.multiuser.MultiUserManager;
import com.king.khcareer.score.RankBean;
import com.king.khcareer.common.config.Configuration;
import com.king.khcareer.model.sql.player.bean.Record;
import com.king.khcareer.settings.AutoFillItem;

public class FileIO {

	public ArrayList<Record> queryAllFrom(String path) {

		ArrayList<Record> list = new ArrayList<Record>();
		FileInputStream stream = null;
		ObjectInputStream oin = null;
		try {
			stream = new FileInputStream(path);
			oin = new ObjectInputStream(stream);
			int size=oin.readInt();
			Record record=null;
			for (int i=0; i<size; i++){
				//for (int i=0; i<10; i++){//调试用加快速度
				record=(Record) oin.readObject();
				list.add(record);
			}
			oin.close();
			stream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (oin != null) {
				try {
					oin.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return list;
	}

	public String saveAll(ArrayList<Record> list, String path) {

		FileOutputStream stream = null;
		ObjectOutputStream oout = null;
		try {
			stream = new FileOutputStream(path);//调试用
			oout = new ObjectOutputStream(stream);
			oout.writeInt(list.size());
			for (Record record:list) {
				oout.writeObject(record);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return e.toString();
		} finally {
			if (oout != null) {
				try {
					oout.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return "ok";
	}

	public Configuration readConfigInfor() {

		FileInputStream stream = null;
		ObjectInputStream oin = null;
		Configuration conf = null;
		try {
			stream = new FileInputStream(Configuration.CONF_DIR + Configuration.CONF_FILE);
			oin = new ObjectInputStream(stream);
			conf = (Configuration) oin.readObject();
		} catch (IOException e1) {
			e1.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (oin != null) {
				try {
					oin.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return conf;
	}

	public String saveConfigInfor(Configuration conf) {

		FileOutputStream stream = null;
		ObjectOutputStream oout = null;
		try {
			stream = new FileOutputStream(Configuration.CONF_DIR + Configuration.CONF_FILE);
			oout = new ObjectOutputStream(stream);
			oout.writeObject(conf);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return e.toString();
		} catch (IOException e) {
			return e.toString();
		} finally {
			if (oout != null) {
				try {
					oout.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return "ok";
	}

	public List<HashMap<String, String>> readSeasonScoreList() {

		List<HashMap<String, String>> list = null;
		FileInputStream stream = null;
		ObjectInputStream oin = null;
		try {
			stream = new FileInputStream(Configuration.CONF_DIR
					+ MultiUserManager.getInstance().getTargetScoreFile());
			oin = new ObjectInputStream(stream);
			int size = oin.readInt();
			if (size > 0) {
				list = new ArrayList<HashMap<String,String>>();
			}
			HashMap<String, String> map = null;
			for (int i = 0; i < size; i ++) {
				map = new HashMap<String, String>();
				map.put("season_score_match", (String) oin.readObject());
				map.put("season_score_score", (String) oin.readObject());
				list.add(map);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (oin != null) {
				try {
					oin.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}

	public void saveSeasonScoreList(List<HashMap<String, String>> list) {

		if (list == null || list.size() == 0) {
			return;
		}
		FileOutputStream stream = null;
		ObjectOutputStream oout = null;
		try {
			stream = new FileOutputStream(Configuration.CONF_DIR
					+ MultiUserManager.getInstance().getTargetScoreFile());
			oout = new ObjectOutputStream(stream);
			oout.writeInt(list.size());
			HashMap<String, String> map = null;
			for (int i = 0; i < list.size(); i ++) {
				map = list.get(i);
				oout.writeObject(map.get("season_score_match"));
				oout.writeObject(map.get("season_score_score"));
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			if (oout != null) {
				try {
					oout.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public List<HashMap<String, String>> readMatchOrderList() {

		List<HashMap<String, String>> list = null;
		FileInputStream stream = null;
		ObjectInputStream oin = null;
		try {
			stream = new FileInputStream(Configuration.CONF_DIR + Configuration.MATCH_ORDER_FILE);
			oin = new ObjectInputStream(stream);
			int size = oin.readInt();
			if (size > 0) {
				list = new ArrayList<HashMap<String,String>>();
			}
			HashMap<String, String> map = null;
			for (int i = 0; i < size; i ++) {
				map = new HashMap<String, String>();
				map.put("order_flag", (String) oin.readObject());
				map.put("match_name", (String) oin.readObject());
				list.add(map);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (oin != null) {
				try {
					oin.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}

	public void saveMatchOrderList(List<HashMap<String, String>> list) {

		if (list == null || list.size() == 0) {
			return;
		}
		FileOutputStream stream = null;
		ObjectOutputStream oout = null;
		try {
			stream = new FileOutputStream(Configuration.CONF_DIR + Configuration.MATCH_ORDER_FILE);
			oout = new ObjectOutputStream(stream);
			oout.writeInt(list.size());
			HashMap<String, String> map = null;
			for (int i = 0; i < list.size(); i ++) {
				map = list.get(i);
				oout.writeObject(map.get("order_flag"));
				oout.writeObject(map.get("match_name"));
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			if (oout != null) {
				try {
					oout.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public HashMap<String, Integer> readRankData(String dataFilePath) {
		HashMap<String, Integer> data = new HashMap<String, Integer>();

		FileInputStream stream = null;
		DataInputStream din = null;
		try {
			stream = new FileInputStream(Configuration.CONF_DIR
					+ dataFilePath);
			din = new DataInputStream(stream);

			data.put("rank", din.readInt());
			data.put("score", din.readInt());
			data.put("top1week", din.readInt());
			data.put("matchNumber", din.readInt());
		} catch (IOException e1) {
			e1.printStackTrace();

			data.put("rank", 0);
			data.put("score", 0);
			data.put("top1week", 0);
			data.put("matchNumber", 0);
			return null;
		} finally {
			if (din != null) {
				try {
					din.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return data;
	}

	public void saveRankBean(RankBean data, String dataFilePath) {
		if (data == null) {
			return;
		}
		FileOutputStream stream = null;
		DataOutputStream dout = null;
		try {
			stream = new FileOutputStream(Configuration.CONF_DIR
					+ dataFilePath);
			dout = new DataOutputStream(stream);
			dout.writeInt(data.getRank());
			dout.writeInt(data.getHighestRank());
			dout.writeInt(data.getTop1Week());
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			if (dout != null) {
				try {
					dout.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public RankBean readRankBean(String dataFilePath) {
		RankBean bean = new RankBean();

		FileInputStream stream = null;
		DataInputStream din = null;
		try {
			stream = new FileInputStream(Configuration.CONF_DIR
					+ dataFilePath);
			din = new DataInputStream(stream);

			bean.setRank(din.readInt());
			bean.setHighestRank(din.readInt());
			bean.setTop1Week(din.readInt());
		} catch (IOException e1) {
			e1.printStackTrace();
			return null;
		} finally {
			if (din != null) {
				try {
					din.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return bean;
	}

	public void saveAutoFillForm(AutoFillItem item) {
		FileOutputStream stream = null;
		ObjectOutputStream oout = null;
		try {
			stream = new FileOutputStream(Configuration.AUTOFILL_DIR + item.getMatch() + ".aff");
			oout = new ObjectOutputStream(stream);

			oout.writeObject(item);
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			if (oout != null) {
				try {
					oout.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public List<AutoFillItem> getAllAutoFillForms() {
		List<AutoFillItem> list = null;
		FileInputStream stream = null;
		ObjectInputStream oin = null;
		try {
			File dir = new File(Configuration.AUTOFILL_DIR);
			File[] forms = dir.listFiles();
			for (File file:forms) {
				stream = new FileInputStream(file.getPath());
				oin = new ObjectInputStream(stream);

				if (list == null) {
					list = new ArrayList<AutoFillItem>();
				}
				list.add((AutoFillItem) oin.readObject());
				oin.close();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (oin != null) {
				try {
					oin.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}

	public void deleteAutoFillForm(AutoFillItem itemToUpdate) {
		File file = new File(Configuration.AUTOFILL_DIR + itemToUpdate.getMatch() + ".aff");
		if (file.exists()) {
			file.delete();
		}
	}
}
