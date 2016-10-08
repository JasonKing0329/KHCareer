package com.king.mytennis.view;

import com.king.mytennis.interfc.SafeInforDAO;
import com.king.mytennis.model.SafeInfor;
import com.king.mytennis.model.SafeInforDAOImp;

import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SafeSetDialog extends Builder implements OnClickListener {

	private Context context;
	private Button bt_alterpwd, bt_alterqstans, bt_submit, bt_exit;
	private TextView tv_newpwd, tv_question, tv_answer;
	private EditText et_newpwd, et_question, et_answer, et_oldpwd;
	private boolean pwdClickCount = true, qaClickCount = true;
	private Dialog dialogID;//用于关闭该对话框
	private boolean needOldPassword;
	private LinearLayout oldPWDLayout;

	public SafeSetDialog(Context context, boolean needOldPWD) {
		super(context);
		this.context = context;
		needOldPassword = needOldPWD;
		init();
	}

	private void init() {

		View view=LayoutInflater.from(context).inflate(R.layout.dialog_safeset, null);

		tv_newpwd = (TextView) view.findViewById(R.id.safeset_tv_newpwd);
		tv_question = (TextView) view.findViewById(R.id.safeset_tv_question);
		tv_answer = (TextView) view.findViewById(R.id.safeset_tv_answer);

		et_newpwd = (EditText) view.findViewById(R.id.safeset_edit_newpwd);
		et_question = (EditText) view.findViewById(R.id.safeset_edit_question);
		et_answer = (EditText) view.findViewById(R.id.safeset_edit_answer);
		et_oldpwd = (EditText) view.findViewById(R.id.safeset_edit_oldpwd);
		oldPWDLayout = (LinearLayout) view.findViewById(R.id.safeset_layout_oldpwd);
		if (!needOldPassword) {
			oldPWDLayout.setVisibility(View.GONE);
			view.findViewById(R.id.safeset_submit_warning).setVisibility(View.GONE);
		}

		bt_alterpwd = (Button) view.findViewById(R.id.safeset_button_setpwd);
		bt_alterqstans = (Button) view.findViewById(R.id.safeset_button_setqstans);
		bt_submit = (Button) view.findViewById(R.id.safeset_button_submit);
		bt_exit = (Button) view.findViewById(R.id.safeset_button_exit);
		bt_alterpwd.setOnClickListener(this);
		bt_alterqstans.setOnClickListener(this);
		bt_submit.setOnClickListener(this);
		bt_exit.setOnClickListener(this);

		setView(view);
		setTitle(R.string.menu_safeset);
		dialogID = show();
	}

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.safeset_button_setpwd) {
			if (pwdClickCount) {
				tv_newpwd.setVisibility(View.VISIBLE);
				et_newpwd.setVisibility(View.VISIBLE);
			}
			else {
				tv_newpwd.setVisibility(View.GONE);
				et_newpwd.setVisibility(View.GONE);
			}
			pwdClickCount = !pwdClickCount;
		}
		else if (v.getId() == R.id.safeset_button_setqstans) {
			if (qaClickCount) {
				tv_question.setVisibility(View.VISIBLE);
				et_question.setVisibility(View.VISIBLE);
				tv_answer.setVisibility(View.VISIBLE);
				et_answer.setVisibility(View.VISIBLE);
			}
			else {
				tv_question.setVisibility(View.GONE);
				et_question.setVisibility(View.GONE);
				tv_answer.setVisibility(View.GONE);
				et_answer.setVisibility(View.GONE);
			}
			qaClickCount = !qaClickCount;
		}
		else if (v.getId() == R.id.safeset_button_submit) {
			SafeInfor safeInfor = SafeInfor.getInstance();
			if (needOldPassword) {
				if (et_oldpwd.getText().toString().equals(safeInfor.getPassword())) {
					if (!pwdClickCount) {
						safeInfor.setPassword(et_newpwd.getText().toString());
					}
					if (!qaClickCount) {
						safeInfor.setQuestion(et_question.getText().toString());
						safeInfor.setAnswer(et_answer.getText().toString());
					}
					SafeInforDAO dao = new SafeInforDAOImp(context);
					dao.update(safeInfor);
					Toast.makeText(context, R.string.safeset_alterok, Toast.LENGTH_LONG).show();
					dialogID.dismiss();
				}
				else {
					et_oldpwd.setError(getContext().getResources().getString(R.string.safeset_wrongpwd));
				}
			}
			else {
				SafeInforDAO dao = new SafeInforDAOImp(context);
				dao.update(safeInfor);
				Toast.makeText(context, R.string.safeset_alterok, Toast.LENGTH_LONG).show();
				dialogID.dismiss();
			}
		}
		else if (v.getId() == R.id.safeset_button_exit) {
			dialogID.dismiss();
		}
	}

}
