package com.king.mytennis.glory.model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.util.Log;

import com.king.khcareer.common.config.Configuration;
import com.king.khcareer.common.multiuser.MultiUser;
import com.king.khcareer.common.multiuser.MultiUserManager;

public class GrandSlame {

	private final String TAG = "GrandSlame";
	private HashMap<String, List<GrandSlameItem>> map;

	public GrandSlame() {
		map = new HashMap<String, List<GrandSlameItem>>();
	}

	/**
	 * 保证list不为空，因为GrandSlameAdapter持有的list是从AchieveParentView注入的，GrandSlameAdapter不能再new，直接用的该list
	 * @param user
	 * @return
	 */
	public List<GrandSlameItem> getGrandSlameItems(MultiUser user) {
		List<GrandSlameItem> list = map.get(user.getId());
		if (list == null) {
			list = new ArrayList<GrandSlameItem>();
			map.put(user.getId(), list);
		}
		return list;
	}

	public void putGrandSlameItems(MultiUser user, List<GrandSlameItem> list) {
		map.put(user.getId(), list);
	}

	public void readData() {

		FileInputStream stream = null;
		ObjectInputStream oin = null;
		try {
			stream = new FileInputStream(Configuration.CONF_DIR + Configuration.GRANDSLAME_FILE);
			oin = new ObjectInputStream(stream);

			int size = oin.readInt();
			List<GrandSlameItem> list = null;
			for (int n = 0; n < size; n ++) {
				String userId = oin.readUTF();
				int items = oin.readInt();
				if (items > 0) {
					list = new ArrayList<GrandSlameItem>();
					for (int i = 0; i < items; i ++) {
						list.add((GrandSlameItem) oin.readObject());
					}
					map.put(userId, list);
				}

			}
		} catch (IOException e1) {
			e1.printStackTrace();
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
	}

	public void saveData() {
		Log.d(TAG, "saveData");
		FileOutputStream stream = null;
		ObjectOutputStream oout = null;
		try {
			stream = new FileOutputStream(Configuration.CONF_DIR + Configuration.GRANDSLAME_FILE);
			oout = new ObjectOutputStream(stream);

			MultiUser[] users = MultiUserManager.getInstance().getUsers();
			oout.writeInt(users.length);
			List<GrandSlameItem> list = null;
			for (int n = 0; n < users.length; n++) {
				oout.writeUTF(users[n].getId());
				list = map.get(users[n].getId());
				if (list != null) {
					oout.writeInt(list.size());
					for (int i = 0; i < list.size(); i ++) {
						oout.writeObject(list.get(i));
					}
				}
				else {
					oout.writeInt(0);
				}
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

	public void converToNewVersion(List<GrandSlameItem> data) {
		map.put(MultiUserManager.getInstance().getUsers()[0].getId(), data);
		saveData();
	}

}
