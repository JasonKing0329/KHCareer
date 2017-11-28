package com.king.khcareer.home.v7;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.king.khcareer.common.helper.BackgroundProvider;
import com.king.lib.tool.ui.RippleFactory;
import com.king.khcareer.common.config.Configuration;
import com.king.khcareer.model.FileIO;
import com.king.khcareer.common.image.ImageFactory;
import com.king.khcareer.common.multiuser.MultiUser;
import com.king.khcareer.common.multiuser.MultiUserManager;
import com.king.khcareer.base.KApplication;
import com.king.khcareer.common.helper.MenuService;
import com.king.khcareer.base.BaseActivity;
import com.king.khcareer.common.viewsys.ChooseBkDialog;
import com.king.khcareer.base.CustomDialog;
import com.king.mytennis.view.R;
import com.king.khcareer.record.editor.RecordEditorActivity;
import com.king.khcareer.common.helper.BasicOperation;
import com.king.khcareer.pubview.CircleImageView;
import com.king.khcareer.pubview.DragLayout;
import com.king.khcareer.pubview.FoldableLayout;
import com.king.khcareer.pubview.DragLayout.DragListener;

import java.util.HashMap;

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
	private View changeBkView;

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
		MultiUserManager.getInstance().loadUsers();
		MultiUserManager.getInstance().loadFromPreference();

		setContentView(R.layout.activity_view7_0_main);

		dragLayout = (DragLayout) findViewById(R.id.view7_main_draglayout);
		changeBkView = findViewById(R.id.view7_main_side_change_bk);
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
		changeBkView.setOnClickListener(this);
		findViewById(R.id.view7_actionbar_menu).setOnClickListener(this);
		findViewById(R.id.view7_main_add).setOnClickListener(this);

		dragLayout.setDragListener(this);

		if (KApplication.isLollipop()) {
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

		mBgBitmap = new BackgroundProvider().loadBackgound();
		initLayout.setVisibility(View.GONE);
		if (mBgBitmap != null) {
			//只支持修改侧边菜单栏背景
			updateSideBackground(mBgBitmap);
		}
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

			case R.id.view7_main_side_change_bk:
				chooseBackground();
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

	private void chooseBackground() {
		ChooseBkDialog bkDialog = new ChooseBkDialog(this, new CustomDialog.OnCustomDialogActionListener() {

			@Override
			public boolean onSave(Object object) {
				@SuppressWarnings("unchecked")
				HashMap<String, Object> map = (HashMap<String, Object>) object;
				int kind = (Integer) map.get(ChooseBkDialog.BK_KIND_KEY);
				if (kind == ChooseBkDialog.BK_KIND_MAINVIEW) {
					String path = (String) map.get("path");
					Bitmap bitmap = new ImageFactory().getBackground(path);
					notifyBackgroundChanged(bitmap, path);
				}
				return true;
			}

			@Override
			public void onLoadData(HashMap<String, Object> data) {

			}

			@Override
			public boolean onCancel() {
				return false;
			}
		});
		bkDialog.show();
	}

	/**
	 * @param bitmap
	 * @param newDEF_BK
	 */
	private void notifyBackgroundChanged(Bitmap bitmap, String newDEF_BK) {
		updateSideBackground(bitmap);
		Configuration.getInstance().DEF_BK = newDEF_BK;
		FileIO dao = new FileIO();
		dao.saveConfigInfor(Configuration.getInstance());
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
