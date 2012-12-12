package therians.kfc.tagit;

import therians.kfc.tagit.db.Func;
import therians.kfc.tagit.db.User;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class FriendProfiles extends Activity implements OnClickListener{

	private Func func = null;
	private User user = null;
	private TextView txtName, txtPoint, txtRank, txtAch;
	private ImageView imgAvatar;
	private int[] ids = {R.id.btnFriendActivities,R.id.btnFriendAchievements,R.id.btnFriendTransfer,R.id.btnFriendUnfollow};
	private int id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friend_profile);
		func = new Func(this,this);
		Intent intent = getIntent();
		id = intent.getIntExtra("UserId", 0);
		user = func.getUser(id);
		
		imgAvatar = (ImageView) findViewById(R.id.imgFriendAvatar);
		txtName = (TextView) findViewById(R.id.txtFriendName);
		txtPoint = (TextView) findViewById(R.id.txtFriendPoint);
		txtRank = (TextView) findViewById(R.id.txtFriendRank);
		txtAch = (TextView) findViewById(R.id.txtFriendAch);
		func.setAvatar(imgAvatar, user.getAvatar());
		txtName.setText(user.getName());
		txtPoint.setText("Point : "+user.getPoint());
		txtRank.setText("Rank : "+user.getRank());
		txtAch.setText("Total Achievement : "+user.getAch()+"/"+func.getSettings().getInt("AchCount", 0));
		
		for(int i:ids){
			findViewById(i).setOnClickListener(this);
		}
	}

	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btnFriendAchievements:
			Intent intent = new Intent(FriendProfiles.this, Achievements.class);
			intent.putExtra("UserId", id);
			startActivity(intent);
			break;
		case R.id.btnFriendActivities:
			intent = new Intent(FriendProfiles.this, Activities.class);
			intent.putExtra("UserId", id);
			startActivity(intent);
			break;
		case R.id.btnFriendTransfer:
			func.transferPoint(id);
			break;
		case R.id.btnFriendUnfollow:
			new AlertDialog.Builder(this)
			.setTitle("Remove friend?")
			.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					Object[] o = func.manageFriend(id, 5);
					if(Boolean.valueOf(o[0].toString())==true){
						Toast.makeText(FriendProfiles.this, "Friend Removed", Toast.LENGTH_SHORT).show();
						func.sync(false, 3);
					}else{
						Toast.makeText(FriendProfiles.this, o[1].toString(), Toast.LENGTH_SHORT).show();
					}
				}
			})
			.setNegativeButton("Cancel", null)
			.show();
			break;
		}
	}
	
	
}
