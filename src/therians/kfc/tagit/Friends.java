package therians.kfc.tagit;

import java.util.List;
import therians.kfc.tagit.db.Attr;
import therians.kfc.tagit.db.Func;
import therians.kfc.tagit.db.User;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Friends extends Activity implements OnClickListener {

	private static String STATUS = "Accepted";
	private Func func = null;
	private int[] ids = { R.id.btnFriendAbout, R.id.btnFriendAnP,
			R.id.btnFriendHome, R.id.btnFriendSetting, R.id.btnFriendSync,
			R.id.btnFriendAll, R.id.btnFriendByMe, R.id.btnFriendByOther,
			R.id.btnFriendAdd };
	private myAdapter adapter = null;
	private ListView lv = null;
	private LayoutInflater li = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		func = new Func(this, this);
		setContentView(R.layout.friend);
		for (int i : ids) {
			findViewById(i).setOnClickListener(this);
		}
		lv = (ListView) findViewById(R.id.listFriend);
		initList();
		li = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
	}

	private void initList() {
		List<User> tmp = func.initList(STATUS);
		adapter = new myAdapter(this, tmp);
		lv.setAdapter(adapter);
	}

	public void onClick(View v) {
		Intent intent = null, setting = null;
		switch (v.getId()) {
		case R.id.btnFriendAbout:
			func.about();
			break;
		case R.id.btnFriendAnP:
			intent = new Intent(this, AnP.class);
			break;
		case R.id.btnFriendHome:
			intent = new Intent(this, Home.class);
			break;
		case R.id.btnFriendSetting:
			setting = new Intent(this, Settings.class);
			startActivityForResult(setting, 2);
			break;
		case R.id.btnFriendSync:
			func.sync(true, 3);
			break;
		case R.id.btnFriendAll:
			STATUS = "Accepted";
			initList();
			break;
		case R.id.btnFriendByMe:
			STATUS = "PendingByMe";
			initList();
			break;
		case R.id.btnFriendByOther:
			STATUS = "PendingByOther";
			initList();
			break;
		case R.id.btnFriendAdd:
			searchUser();
			break;
		}
		if (intent != null) {
			startActivity(intent);
			overridePendingTransition(0, 0);
			this.finish();
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 2:
			if (data.getIntExtra("action", 1) == 1) {
				func.updateInfo();
			} else {
				Intent intent = new Intent(this, Login.class);
				startActivity(intent);
				Friends.this.finish();
			}
			break;
		}
	}

	AlertDialog searchFriend = null;

	private void searchUser() {
		View v = li.inflate(R.layout.dialog_input, null);
		final EditText input = (EditText) v.findViewById(R.id.txtInput);
		input.setInputType(InputType.TYPE_CLASS_TEXT);
		new AlertDialog.Builder(this)
				.setView(v)
				.setPositiveButton("Enter",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								String val = input.getText().toString();
								List<User> list = func.searchUser(val);
								if (list.size() > 0) {
									LinearLayout ll = new LinearLayout(
											Friends.this);
									ll.setOrientation(LinearLayout.HORIZONTAL);
									LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
											LinearLayout.LayoutParams.MATCH_PARENT,
											LinearLayout.LayoutParams.MATCH_PARENT);
									ListView lv = new ListView(Friends.this);
									ll.addView(lv, params);
									lv.setAdapter(new viewFriendAdapter(
											Friends.this, list));
									searchFriend = new AlertDialog.Builder(
											Friends.this)
											.setTitle("Search Result")
											.setView(ll).show();
								} else {
									Toast.makeText(Friends.this,
											"No user found", Toast.LENGTH_SHORT)
											.show();
								}
							}
						}).show();
	}

	private class myAdapter extends BaseAdapter {

		private Context context = null;
		private List<User> list = null;

		public myAdapter(Context context, List<User> list) {
			this.context = context;
			this.list = list;
			if (list.size() == 0) {
				list.add(new User());
			}
		}

		public int getCount() {
			return list.size();
		}

		public Object getItem(int pos) {
			return list.get(pos);
		}

		public long getItemId(int pos) {
			return pos;
		}

		public View getView(int pos, View v, ViewGroup parent) {
			if (v == null) {
				LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = li.inflate(R.layout.item_friend, null);
			}
			ImageView avatar = (ImageView) v.findViewById(R.id.imgFriendAvatar);
			TextView name = (TextView) v.findViewById(R.id.txtFriendItemName), ach = (TextView) v
					.findViewById(R.id.txtFriendItemAch), rank = (TextView) v
					.findViewById(R.id.txtFriendItemRank);
			User u = list.get(pos);
			final int u_id = u.getId();
			if (u.getName() == null) {
				avatar.setVisibility(View.INVISIBLE);
				name.setText("No User in This List");
				ach.setText("");
				rank.setText("");
			} else {
				func.setAvatar(avatar, u.getAvatar());
				name.setText(u.getName());
				ach.setText(u.getAch() + " title achieved");
				rank.setText(u.getRank());
				if (STATUS.equals("Accepted")) {
					v.setOnClickListener(new OnClickListener() {

						public void onClick(View v) {
							Intent intent = new Intent(Friends.this,FriendProfiles.class);
							intent.putExtra("UserId", u_id);
							startActivity(intent);
						}

					});
				} else if (STATUS.equals("PendingByMe")) {
					v.setOnClickListener(new OnClickListener() {

						public void onClick(View v) {
							new AlertDialog.Builder(context)
									.setTitle("Cancel Request?")
									.setPositiveButton(
											"Cancel",
											new DialogInterface.OnClickListener() {

												public void onClick(
														DialogInterface dialog,
														int which) {
													Object[] o = func
															.manageFriend(u_id,
																	3);
													if(o!=null){
														if (Boolean.valueOf(o[0]
																.toString()) == true) {
															Toast.makeText(
																	context,
																	"Ignored",
																	Toast.LENGTH_SHORT)
																	.show();
															func.sync(false, 3);
														} else {
															Toast.makeText(
																	context,
																	o[1].toString(),
																	Toast.LENGTH_SHORT)
																	.show();
														}
													}else{
														Toast.makeText(Friends.this, Attr.NO_NET, Toast.LENGTH_SHORT).show();
													}
												}
											}).show();
						}

					});
				} else if (STATUS.equals("PendingByOther")) {
					v.setOnClickListener(new OnClickListener() {

						public void onClick(View v) {
							new AlertDialog.Builder(context)
									.setTitle("Accept Request?")
									.setPositiveButton(
											"Accept",
											new DialogInterface.OnClickListener() {

												public void onClick(
														DialogInterface dialog,
														int which) {
													Object[] o = func
															.manageFriend(u_id,
																	2);
													if(o!=null){
														if (Boolean.valueOf(o[0]
																.toString()) == true) {
															Toast.makeText(
																	context,
																	"Accepted",
																	Toast.LENGTH_SHORT)
																	.show();
															func.sync(false, 3);
														} else {
															Toast.makeText(
																	context,
																	o[1].toString(),
																	Toast.LENGTH_SHORT)
																	.show();
														}
													}else{
														Toast.makeText(Friends.this, Attr.NO_NET, Toast.LENGTH_SHORT).show();
													}
													
												}
											})
									.setNegativeButton(
											"Ignore",
											new DialogInterface.OnClickListener() {

												public void onClick(
														DialogInterface dialog,
														int which) {
													Object[] o = func
															.manageFriend(u_id,
																	4);
													if(o!=null){
														if (Boolean.valueOf(o[0]
																.toString()) == true) {
															Toast.makeText(
																	context,
																	"Ignored",
																	Toast.LENGTH_SHORT)
																	.show();
															func.sync(false, 3);
														} else {
															Toast.makeText(
																	context,
																	o[1].toString(),
																	Toast.LENGTH_SHORT)
																	.show();
														}														
													}else{
														Toast.makeText(Friends.this, Attr.NO_NET, Toast.LENGTH_SHORT).show();
													}
												}
											}).show();
						}

					});
				}
			}
			return v;
		}

	}

	private class viewFriendAdapter extends BaseAdapter {

		private Context context = null;
		private List<User> list = null;

		public viewFriendAdapter(Context context, List<User> list) {
			this.context = context;
			this.list = list;
		}

		public int getCount() {
			return list.size();
		}

		public Object getItem(int pos) {
			return list.get(pos);
		}

		public long getItemId(int pos) {
			return pos;
		}

		public View getView(int pos, View v, ViewGroup parent) {
			if (v == null) {
				LinearLayout tmp = new LinearLayout(context);
				tmp.setBackgroundResource(R.color.white);
				tmp.setOrientation(LinearLayout.HORIZONTAL);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				User u = list.get(pos);
				final String name = u.getName();
				final int u_id = u.getId();
				Button btn = new Button(context);
				btn.setText("Add");
				tmp.addView(btn, params);
				TextView tv = new TextView(context);
				tv.setText(name + " (" + u.getEmail() + ")");
				tv.setTextSize(12);
				tmp.addView(tv, params);
				btn.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						Object[] o = func.manageFriend(u_id, 1);
						if(o!=null){
							if (Boolean.valueOf(o[0].toString()) == true) {
								Toast.makeText(context,
										"Add " + name + " request sent",
										Toast.LENGTH_SHORT).show();
								func.sync(false, 3);
							} else {
								Toast.makeText(context, o[1].toString(),
										Toast.LENGTH_SHORT).show();
							}	
						}else{
							Toast.makeText(Friends.this, Attr.NO_NET, Toast.LENGTH_SHORT).show();
						}
						searchFriend.cancel();
					}

				});
				v = tmp;
			}
			return v;
		}

	}

}
