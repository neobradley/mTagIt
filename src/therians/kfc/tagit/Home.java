package therians.kfc.tagit;

import java.text.SimpleDateFormat;
import java.util.Date;

import therians.kfc.tagit.db.Func;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class Home extends Activity implements OnClickListener {

	private Func func;
	private int[] ids = { R.id.btnHomeSync, R.id.btnHomeSetting,
			R.id.btnHomeAbout, R.id.btnHome, R.id.btnHomeAnP,
			R.id.btnHomeFriends, R.id.btnHomeScan, R.id.btnHomeOrder,
			R.id.btnHomeActivities, R.id.btnHomeShare };

	@TargetApi(9)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		func = new Func(this, this);

		for (int i : ids) {
			findViewById(i).setOnClickListener(this);
		}
		func.updateInfo();
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = null, scanner = null, intent2 = null, setting = null;
		switch (v.getId()) {
		case R.id.btnHomeSync:
			func.sync(true, 1);
			break;
		case R.id.btnHomeSetting:
			setting = new Intent(Home.this, Settings.class);
			startActivityForResult(setting, 2);
			break;
		case R.id.btnHomeAbout:
			func.about();
			break;
		case R.id.btnHomeAnP:
			intent = new Intent(Home.this, AnP.class);
			break;
		case R.id.btnHomeFriends:
			intent = new Intent(Home.this, Friends.class);
			break;
		case R.id.btnHomeScan:
			scanner = new Intent("com.google.zxing.client.android.SCAN");
			scanner.putExtra("SCAN_MODE", "QR_CODE_MODE");
			startActivityForResult(scanner, 1);
			break;
		case R.id.btnHomeOrder:
			intent2 = new Intent(Home.this, Order.class);
			break;
		case R.id.btnHomeActivities:
			intent2 = new Intent(Home.this, Activities.class);
			intent2.putExtra("UserId", func.getSettings().getInt("UserId", 0));
			break;
		case R.id.btnHomeShare:
			share();
			break;

		}
		if (intent != null) {
			startActivity(intent);
			overridePendingTransition(0, 0);
			Home.this.finish();
		}
		if (intent2 != null) {
			startActivity(intent2);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 1:
			if (resultCode == RESULT_OK) {
				String contents = data.getStringExtra("SCAN_RESULT");
				storeScan(contents);
				new AlertDialog.Builder(Home.this)
				.setTitle("Point fetched!")
				.setMessage("This action will require network access. Sync data?")
				.setPositiveButton("Sync now", new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						func.sync(false, 1);
					}
				})
				.setNegativeButton("Sync later", null).show();
				
				Toast.makeText(Home.this, contents, Toast.LENGTH_LONG).show();
			}
			break;
		case 2:
			if(data.getIntExtra("action", 1)==1){
				func.updateInfo();	
			}else{
				Intent intent = new Intent(this, Login.class);
				startActivity(intent);
				Home.this.finish();
			}
			break;
		}
	}
	
	private void storeScan(String contents){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/M/dd H:m:s");
		String now = sdf.format(new Date());
		Object[] o = {contents, now};
		func.updateReceipt(o);
	}

	private void share() {
		LayoutInflater li = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View v = li.inflate(R.layout.dialog_share, null);
		new AlertDialog.Builder(this)
		.setTitle("Share")
		.setView(v)
		.show();
	}
}

//-submitScan:void
//=prompt dialog if submit scan, sync if confirmed
//-storeScan:void
//=store scan string into db
