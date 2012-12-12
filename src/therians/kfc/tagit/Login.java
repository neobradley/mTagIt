package therians.kfc.tagit;

import therians.kfc.tagit.db.Attr;
import therians.kfc.tagit.db.Func;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends Activity implements OnClickListener {

	private Button btnLogin;
	private TextView txtEmail, txtPword;
	private Func func;

	@TargetApi(9)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());
		super.onCreate(savedInstanceState);

		func = new Func(this, this);
		if (func.getSettings().getInt("UserId", -1) > 0) {
			Intent intent = new Intent(this, therians.kfc.tagit.Home.class);
			startActivity(intent);
			this.finish();
		}

		setContentView(R.layout.login);

		btnLogin = (Button) findViewById(R.id.btnLogin);
		txtEmail = (TextView) findViewById(R.id.txtEmail);
		txtPword = (TextView) findViewById(R.id.txtPassword);
		findViewById(R.id.txtSignUp).setOnClickListener(this);
		btnLogin.setOnClickListener(this);
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnLogin:
			String email = txtEmail.getText().toString().trim(),
			pword = txtPword.getText().toString().trim();
			if (email.length() > 0 && pword.length() > 0) {
				Object[] res = func.login(email, pword);
				if (res == null) {
					Toast.makeText(Login.this, Attr.NO_NET, Toast.LENGTH_LONG)
							.show();
				} else if (res[0] == Boolean.valueOf(true)) {
					int user_id = (Integer) res[1];
					func.putSetting('n', "UserId", String.valueOf(user_id));
					func.putSetting('s', "UserPw", pword);
					func.sync(false, 0);
				} else {
					Toast.makeText(Login.this, res[1].toString(),
							Toast.LENGTH_LONG).show();
				}
			} else {
				Toast.makeText(Login.this, "Please enter email and password",
						Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.txtSignUp:
			Intent intent = new Intent(Login.this, TagIt.class);
			startActivity(intent);
			this.finish();
			break;
		}
	}
}
