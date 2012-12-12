package therians.kfc.tagit;

import therians.kfc.tagit.db.Attr;
import therians.kfc.tagit.db.Func;
import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TagIt extends Activity implements OnClickListener {

	private Button btnSignUp;
	private TextView txtName, txtEmail, txtPword, txtPword2;
	private Func func;

	@TargetApi(9)
	@Override
	public void onCreate(Bundle savedInstanceState) {
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

		setContentView(R.layout.activity_tag_it);
		btnSignUp = (Button) findViewById(R.id.btnSignUp);
		btnSignUp.setOnClickListener(this);
		View v = findViewById(R.id.txtLogin);
		v.setOnClickListener(this);
		txtName = (TextView) findViewById(R.id.txtSignName);
		txtEmail = (TextView) findViewById(R.id.txtSignEmail);
		txtPword = (TextView) findViewById(R.id.txtSignPwd);
		txtPword2 = (TextView) findViewById(R.id.txtSignConfPwd);
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.txtLogin:
			Intent intent = new Intent(TagIt.this, Login.class);
			startActivity(intent);
			this.finish();
			break;
		case R.id.btnSignUp:
			String name = txtName.getText().toString(),
			email = txtEmail.getText().toString(),
			password = txtPword.getText().toString(),
			password2 = txtPword2.getText().toString();
			if (name.length() > 0 && email.length() > 0
					&& password.length() > 0 && password.equals(password2)) {
				Object[] res = func.signup(name, email, password);
				if (res == null) {
					Toast.makeText(TagIt.this, Attr.NO_NET, Toast.LENGTH_LONG)
							.show();
				} else if (res[0] == Boolean.valueOf(true)) {
					int user_id = (Integer) res[1];
					func.putSetting('n', "UserId", String.valueOf(user_id));
					func.putSetting('s', "UserPw", password);
					func.sync(false, 0);

					intent = new Intent(TagIt.this,
							therians.kfc.tagit.Home.class);
					startActivity(intent);
					this.finish();
				} else {
					Toast.makeText(TagIt.this, res[1].toString(),
							Toast.LENGTH_LONG).show();
				}
			} else {
				if (password.equals(password2)) {
					Toast.makeText(TagIt.this, "Please complete all fields",
							Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(TagIt.this, "Passwords do not match",
							Toast.LENGTH_LONG).show();
				}
			}
			break;
		}
	}
}
