package therians.kfc.tagit;

import therians.kfc.tagit.db.Attr;
import therians.kfc.tagit.db.Func;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Settings extends Activity implements OnClickListener {

	private Func func = null;
	private TextView txtName, txtCurPw, txtNewPw, txtConPw;
	private ImageView imgAvatar;
	private int[] ids = { R.id.imgSetIcon1, R.id.imgSetIcon2, R.id.imgSetIcon3,
			R.id.imgSetIcon4, R.id.imgSetIcon5, R.id.imgSetIcon6,
			R.id.imgSetIcon7, R.id.imgSetIcon8, R.id.btnSetSave,
			R.id.btnSetLogout, R.id.btnSetFB, R.id.btnSetTw };
	private int curAvatar, newAvatar;
	private boolean hasChange;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		func = new Func(this, this);
		init();
	}

	private void init() {
		txtName = (TextView) this.findViewById(R.id.txtSetName);
		txtCurPw = (TextView) this.findViewById(R.id.txtSetPW);
		txtNewPw = (TextView) this.findViewById(R.id.txtSetNewPW);
		txtConPw = (TextView) this.findViewById(R.id.txtSetConfPW);
		imgAvatar = (ImageView) this.findViewById(R.id.imgSetAvatar);
		for (int i : ids) {
			this.findViewById(i).setOnClickListener(this);
		}
		hasChange = false;
		curAvatar = func.getSettings().getInt("UserAvatar", 1);
		newAvatar = curAvatar;
		imgAvatar = func.setAvatar(imgAvatar, curAvatar);
		String name = func.getSettings().getString("UserName", "New Name");
		txtName.setHint(name);
	}

	private void signout() {
		func.remSetting("UserId");
		Intent intent = new Intent();
		intent.putExtra("action", 2);
		setResult(RESULT_OK, intent);
		Settings.this.finish();
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent();
		intent.putExtra("action", 1);
		setResult(RESULT_OK, intent);
		if (hasChange == true) {
			func.sync(false, 2);
		} else {
			Settings.this.finish();
		}
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgSetIcon1:
			newAvatar = 1;
			imgAvatar.setImageResource(R.drawable.b1);
			break;
		case R.id.imgSetIcon2:
			newAvatar = 2;
			imgAvatar.setImageResource(R.drawable.b2);
			break;
		case R.id.imgSetIcon3:
			newAvatar = 3;
			imgAvatar.setImageResource(R.drawable.b3);
			break;
		case R.id.imgSetIcon4:
			newAvatar = 4;
			imgAvatar.setImageResource(R.drawable.b4);
			break;
		case R.id.imgSetIcon5:
			newAvatar = 5;
			imgAvatar.setImageResource(R.drawable.b5);
			break;
		case R.id.imgSetIcon6:
			newAvatar = 6;
			imgAvatar.setImageResource(R.drawable.b6);
			break;
		case R.id.imgSetIcon7:
			newAvatar = 7;
			imgAvatar.setImageResource(R.drawable.b7);
			break;
		case R.id.imgSetIcon8:
			newAvatar = 8;
			imgAvatar.setImageResource(R.drawable.b8);
			break;
		case R.id.btnSetSave:
			String name = this.txtName.getText().toString(),
			pw = this.txtCurPw.getText().toString(),
			pw2 = this.txtNewPw.getText().toString(),
			pw3 = this.txtConPw.getText().toString();
			if (name.length() + pw.length() > 0 || curAvatar != newAvatar) {
				boolean cont = true;
				if (name.length() > 16) {
					Toast.makeText(this,
							"Please have your name in 16 characters or less.",
							Toast.LENGTH_SHORT).show();
					cont = false;
				}
				if (cont
						&& pw.length() > 0
						&& !pw.equals(func.getSettings()
								.getString("UserPw", ""))) {
					Toast.makeText(this, "Password incorrect",
							Toast.LENGTH_SHORT).show();
					cont = false;
				}
				if (cont
						&& pw.length() > 0
						&& (pw2.length() + pw3.length() < 8 || !pw2.equals(pw3))) {
					Toast.makeText(
							this,
							"Please make new password length more than 4 characters and match the new password and confirm password",
							Toast.LENGTH_SHORT).show();
					cont = false;
				}
				if (cont == true) {
					Object[] res = func.updateUser(name, pw2, newAvatar);
					String msg = "";
					if (res == null) {
						msg = Attr.NO_NET;
					} else {
						hasChange = true;
						msg = Boolean.valueOf(res[0].toString()) ? "Saved"
								: res[1].toString();
					}
					Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
				}
			} else {
				Toast.makeText(this, "No change is made", Toast.LENGTH_SHORT)
						.show();
			}
			break;
		case R.id.btnSetLogout:
			signout();
			break;
		case R.id.btnSetFB:
			break;
		case R.id.btnSetTw:
			break;
		}
	}

}
// -init:void
// =set name, avatar
// -matchPassword:boolean
// =compare new password and confirm password
// -save:void
// =sync changes, toast result (req net)
// -signout:void
// =remove preference