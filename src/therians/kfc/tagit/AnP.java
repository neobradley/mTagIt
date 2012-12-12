package therians.kfc.tagit;

import therians.kfc.tagit.db.Func;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class AnP extends Activity implements OnClickListener {

	private Func func;
	private int[] ids = { R.id.btnANPSync, R.id.btnANPSetting,
			R.id.btnANPAbout, R.id.btnANPHome, R.id.btnANPFriends,
			R.id.btnANPAch, R.id.btnANPEvent, R.id.btnANPRedeem,
			R.id.btnANPTransfer };

	@TargetApi(9)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.anp);
		func = new Func(this, this);
		for (int i : ids) {
			findViewById(i).setOnClickListener(this);
		}
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = null, intent2 = null, setting = null;
		switch (v.getId()) {
		case R.id.btnANPSync:
			func.sync(true, 2);
			break;
		case R.id.btnANPSetting:
			setting = new Intent(this, Settings.class);
			startActivityForResult(setting, 2);
			break;
		case R.id.btnANPAbout:
			func.about();
			break;
		case R.id.btnANPHome:
			intent = new Intent(AnP.this, Home.class);
			break;
		case R.id.btnANPFriends:
			intent = new Intent(AnP.this, Friends.class);
			break;
		case R.id.btnANPAch:
			intent2 = new Intent(AnP.this, Achievements.class);
			intent2.putExtra("UserId", func.getSettings().getInt("UserId", 0));
			break;
		case R.id.btnANPEvent:
			intent2 = new Intent(AnP.this, Events.class);
			break;
		case R.id.btnANPRedeem:
			intent2 = new Intent(AnP.this, Redeems.class);
			break;
		case R.id.btnANPTransfer:
			func.transferPoint(-1);
			break;

		}
		if (intent != null) {
			startActivity(intent);
			overridePendingTransition(0, 0);
			AnP.this.finish();
		}
		if (intent2 != null) {
			startActivity(intent2);
		}
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 2:
			if(data.getIntExtra("action", 1)==1){
				func.updateInfo();	
			}else{
				Intent intent = new Intent(this, Login.class);
				startActivity(intent);
				AnP.this.finish();
			}
			break;
		}
	}
	
}
