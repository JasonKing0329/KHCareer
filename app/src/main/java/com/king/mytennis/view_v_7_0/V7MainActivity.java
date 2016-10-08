package com.king.mytennis.view_v_7_0;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.king.lib.tool.ui.RippleFactory;
import com.king.mytennis.multiuser.MultiUser;
import com.king.mytennis.multiuser.MultiUserManager;
import com.king.mytennis.service.Application;
import com.king.mytennis.service.InitService;
import com.king.mytennis.service.MenuService;
import com.king.mytennis.view.BaseActivity;
import com.king.mytennis.view.R;
import com.king.mytennis.view.RecordEditorActivity;
import com.king.mytennis.view_v_7_0.controller.BasicOperation;
import com.king.mytennis.view_v_7_0.controller.FolderManagerAction;
import com.king.mytennis.view_v_7_0.view.CircleImageView;
import com.king.mytennis.view_v_7_0.view.DragLayout;
import com.king.mytennis.view_v_7_0.view.FoldableLayout;
import com.king.mytennis.view_v_7_0.view.DragLayout.DragListener;

/**
 * @author JingYang
 * @version create time：2016-3-7 上午10:59:03
 *
 */
public class V7MainActivity extends BaseActivity implements OnClickListener
		, DragListener, OnMenuItemClickListener{

	private DragLayout dragLayout;
	private CircleImageView dragSideHead, mainSideHead;
	private TextView dragSideName;
	private TextView kingTextView, flamencoTextView, henryTextView, qiTextView;

	private FoldableLayout timeFolder;
	private FoldableLayout playerFolder;
	private FoldableLayout matchFolder;
	private FoldableLayout moreFolder;
	private View initLayout;

	private FolderManagerAction timeManager;
	private FolderManagerAction playerManager;
	private FolderManagerAction matchManager;
	private FolderManagerAction moreManager;

	private MenuService menuService;
	private PopupMenu popupMenu;

	private Bitmap mBgBitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//check default user
		MultiUserManager.getInstance().loadUsers(this);
		MultiUserManager.getInstance().loadFromPreference(this);

		setContentView(R.layout.activity_view7_0_main);

		dragLayout = (DragLayout) findViewById(R.id.view7_main_draglayout);
		mainSideHead = (CircleImageView) findViewById(R.id.view7_actionbar_img);
		mainSideHead.setVisibility(View.VISIBLE);
		dragSideHead = (CircleImageView) findViewById(R.id.view7_main_side_user_head);
		dragSideName = (TextView) findViewById(R.id.view7_main_side_user_name);
		kingTextView = (TextView) findViewById(R.id.view7_main_side_user_king);
		flamencoTextView = (TextView) findViewById(R.id.view7_main_side_user_flamenco);
		henryTextView = (TextView) findViewById(R.id.view7_main_side_user_henry);
		qiTextView = (TextView) findViewById(R.id.view7_main_side_user_qi);

		timeFolder = (FoldableLayout) findViewById(R.id.foldercard_time);
		playerFolder = (FoldableLayout) findViewById(R.id.foldercard_player);
		matchFolder = (FoldableLayout) findViewById(R.id.foldercard_match);
		moreFolder = (FoldableLayout) findViewById(R.id.foldercard_more);
		initLayout = findViewById(R.id.layout_init);

		timeManager = UiFactory.createTimeManager();
		playerManager = UiFactory.createPlayerManager();
		matchManager = UiFactory.createMatchManager();
		moreManager = UiFactory.createMoreManager();

		timeManager.initFolderView(this, timeFolder);
		playerManager.initFolderView(this, playerFolder);
		matchManager.initFolderView(this, matchFolder);
		moreManager.initFolderView(this, moreFolder);

		mainSideHead.setOnClickListener(this);
		kingTextView.setOnClickListener(this);
		flamencoTextView.setOnClickListener(this);
		henryTextView.setOnClickListener(this);
		qiTextView.setOnClickListener(this);
		findViewById(R.id.view7_actionbar_menu).setOnClickListener(this);
		findViewById(R.id.view7_main_add).setOnClickListener(this);

		dragLayout.setDragListener(this);

		if (Application.isLollipop()) {
			findViewById(R.id.view7_actionbar_menu).setBackground(
					RippleFactory.getRippleBackground(Color.TRANSPARENT,
							getResources().getColor(R.color.ripple_material_light)));
			findViewById(R.id.view7_main_add).setBackground(
					RippleFactory.getRippleBackground(Color.TRANSPARENT
							, getResources().getColor(R.color.ripple_material_light)));
			kingTextView.setBackground(RippleFactory.getRippleBackground(Color.TRANSPARENT
					, getResources().getColor(R.color.ripple_material_light), true));
			flamencoTextView.setBackground(RippleFactory.getRippleBackground(Color.TRANSPARENT
					, getResources().getColor(R.color.ripple_material_light), true));
			henryTextView.setBackground(RippleFactory.getRippleBackground(Color.TRANSPARENT
					, getResources().getColor(R.color.ripple_material_light), true));
			qiTextView.setBackground(RippleFactory.getRippleBackground(Color.TRANSPARENT
					, getResources().getColor(R.color.ripple_material_light), true));
		}

		menuService = new MenuService();
		initUserInfor();

		new InitAppThread().start();
	}

	private void initUserInfor() {
		MultiUser user = MultiUserManager.getInstance().getCurrentUser();
		int imgRes = MultiUserManager.getInstance().getUserSelectorResId(this, user);
		dragSideHead.setImageResource(imgRes);
		mainSideHead.setImageResource(imgRes);
		dragSideName.setText(user.getId());

		MultiUser[] users = MultiUserManager.getInstance().getUsers();
		kingTextView.setText(users[0].getId());
		kingTextView.setTag(users[0]);
		flamencoTextView.setText(users[1].getId());
		flamencoTextView.setTag(users[1]);
		henryTextView.setText(users[2].getId());
		henryTextView.setTag(users[2]);
		qiTextView.setText(users[3].getId());
		qiTextView.setTag(users[3]);
	}

	/**
	 * 登录后首次进入首页初始化菜单背景以及Configuration信息
	 */
	private class InitAppThread extends Thread implements Callback {

		private Handler handler;

		public InitAppThread () {
			handler = new Handler(this);
		}

		public void run() {
			InitService initService = new InitService();
			initService.loadConfiguration();
			mBgBitmap = initService.loadBackgound();

			Message msg = new Message();
			msg.what = 1;
			handler.sendMessage(msg);
		}

		@Override
		public boolean handleMessage(Message msg) {

			if (msg.what == 1) {
				initLayout.setVisibility(View.GONE);
				if (mBgBitmap != null) {
					//只支持修改侧边菜单栏背景
					updateSideBackground(mBgBitmap);
				}
			}
			return true;
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.view7_actionbar_menu:
				if (popupMenu == null) {
					createMenu();
				}
				popupMenu.show();
				break;
			case R.id.view7_main_add:
				Intent intent = new Intent();
				intent.setClass(this, RecordEditorActivity.class);
				startActivity(intent);
				break;

			case R.id.view7_actionbar_img:
				dragLayout.open(true);
				break;
			default:
				break;
		}

		if (v instanceof TextView) {
			MultiUser user = (MultiUser) v.getTag();
			if (!MultiUserManager.getInstance().getCurrentUser().getId().equals(user.getId())) {
				MultiUserManager.getInstance().setCurrentUser(user);
				MultiUserManager.getInstance().saveToPreference(this, user);
				onUserChanged(user);
			}
		}
	}

	public void updateSideBackground(Bitmap bitmap) {
		//必须设置在dragLayout上，不能设置在侧边栏所在的RelativeLayout上，会有bug
		dragLayout.setBackground(new BitmapDrawable(getResources(), bitmap));
	}

	private void createMenu() {
		popupMenu = new PopupMenu(this, findViewById(R.id.view7_actionbar_menu));
		popupMenu.getMenuInflater().inflate(R.menu.view7_main, popupMenu.getMenu());
		popupMenu.setOnMenuItemClickListener(this);
	}

	private void onUserChanged(MultiUser user) {
		dragSideName.setText(user.getId());
		int imgRes = MultiUserManager.getInstance().getUserSelectorResId(this, user);
		dragSideHead.setImageResource(imgRes);
		mainSideHead.setImageResource(imgRes);
	}

	@Override
	public void onOpen() {

	}

	@Override
	public void onClose() {

	}

	@Override
	public void onDrag(float percent) {

	}

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_view7_main_load:
				BasicOperation.showLoadFromDialog(this, new BasicOperation.DialogCallback() {

					@Override
					public void onOk(Object result) {
						Toast.makeText(V7MainActivity.this, R.string.loading_ok, Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onCancel(Object result) {

					}
				});
				break;
			case R.id.menu_view7_main_save:
				String folder = menuService.saveDatabases();
				String message = getString(R.string.save_db_success);
				message = message.replace("%s", folder);
				Toast.makeText(V7MainActivity.this, message, Toast.LENGTH_LONG).show();
				break;
			case R.id.menu_view7_main_saveas:
				BasicOperation.showSaveAsDialog(this, null);
				break;
			case R.id.menu_view7_main_exit:
				finish();
				break;

			default:
				break;
		}
		return false;
	}
}
