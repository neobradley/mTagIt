package therians.kfc.tagit.db;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import therians.kfc.tagit.Achievements;
import therians.kfc.tagit.R;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Func {

	private Context context = null;
	private Activity activity = null;
	private DB db = null;
	private SharedPreferences settings = null;

	@TargetApi(9)
	public Func(Context context, Activity activity) {
		this.context = context;
		this.activity = activity;
		db = new DB(context);
		settings = context.getSharedPreferences(Attr.PREFS_NAME, 0);
		initElem();
	}

	// setting functions
	public SharedPreferences getSettings() {
		return settings;
	}

	public void putSetting(char type, String name, String val) {
		SharedPreferences.Editor editor = settings.edit();
		switch (type) {
		case 'n':
			editor.putInt(name, Integer.parseInt(val));
			break;
		case 's':
			editor.putString(name, val);
			break;
		case 'b':
			editor.putBoolean(name, Boolean.valueOf(val));
			break;
		case 'f':
			editor.putFloat(name, Float.parseFloat(val));
			break;
		case 'l':
			editor.putLong(name, Long.parseLong(val));
			break;
		}
		editor.commit();
	}

	public void remSetting(String name) {
		SharedPreferences.Editor editor = settings.edit();
		editor.remove(name);
		editor.commit();
	}

	// end setting functions

	// front-end functions
	private LayoutInflater li;
	private ProgressDialog progDialog;
	private Handler h;

	private void initElem() {
		progDialog = new ProgressDialog(context);
		progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progDialog.setMessage("Syncing user data...");
		h = new Handler();
		li = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void sync(boolean hasDialog, final int mode) {
		final int current = getSettings().getInt("UserAch", 0);
		if (hasDialog) {
			View view = li.inflate(R.layout.dialog_sync, null);
			TextView last = (TextView) view
					.findViewById(R.id.txtDialogLastUpdate);
			last.setText("Last Update : "
					+ getSettings()
							.getString("UserLastUpdate", "No update yet"));
			new AlertDialog.Builder(context)
					.setView(view)
					.setTitle("Sync")
					.setPositiveButton("Sync",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									progDialog.show();
									new Thread(new Runnable() {
										String msg = "";

										public void run() {
											msg = doSync();
											if (msg == null) {
												progDialog.dismiss();
												h.post(new Runnable() {

													public void run() {
														Toast.makeText(
																context,
																Attr.NO_NET,
																Toast.LENGTH_LONG)
																.show();
													}

												});
											} else {
												progDialog.dismiss();
												if(getSettings().getInt("UserAch", 0)>current){
													h.post(new Runnable(){

														public void run() {
															new AlertDialog.Builder(context)
															.setTitle("Congradulations!!!")
															.setMessage("You have unlocked a new achievement")
															.setPositiveButton("View Achievements", new DialogInterface.OnClickListener() {
																
																public void onClick(DialogInterface dialog, int which) {
																	Intent intent = new Intent(context,Achievements.class);
																	intent.putExtra("UserId", getSettings().getInt("UserId", 0));
																	activity.startActivity(intent);
																}
															})
															.setNegativeButton("Close", null)
															.show();
														}
														
													});
												}
												h.post(new Runnable() {

													public void run() {
														Toast.makeText(
																context,
																"Sync completed",
																Toast.LENGTH_SHORT)
																.show();
													}

												});
												switch (mode) {
												case 1:
													h.post(new Runnable() {

														public void run() {
															updateInfo();
														}

													});
													break;
												case 2:
													break;
												case 3:
													h.post(new Runnable() {

														public void run() {
															initList(curStatus);
														}

													});
													break;
												}
											}

										}
									}).start();
								}
							}).setNegativeButton("Cancel", null).show();
		} else {
			progDialog.setCancelable(false);
			progDialog.show();
			new Thread(new Runnable() {
				String msg = "";

				public void run() {
					msg = doSync();
					if (msg == null) {
						progDialog.dismiss();
						h.post(new Runnable() {

							public void run() {
								Toast.makeText(context, Attr.NO_NET,
										Toast.LENGTH_LONG).show();
							}
						});
					} else {
						progDialog.dismiss();
						if(getSettings().getInt("UserAch", 0)>current){
							h.post(new Runnable(){

								public void run() {
									new AlertDialog.Builder(context)
									.setTitle("Congradulations!!!")
									.setMessage("You have unlocked a new achievement")
									.setPositiveButton("View Achievements", new DialogInterface.OnClickListener() {
										
										public void onClick(DialogInterface dialog, int which) {
											Intent intent = new Intent(context,Achievements.class);
											intent.putExtra("UserId", getSettings().getInt("UserId", 0));
											activity.startActivity(intent);
										}
									})
									.setNegativeButton("Close", null)
									.show();
								}
								
							});
						}
						if (mode > 0) {
							switch (mode) {
							case 1:// scan point
								h.post(new Runnable() {

									public void run() {
										Toast.makeText(context,
												"Points updated",
												Toast.LENGTH_SHORT).show();
									}

								});
								break;
							case 2:// setting
								activity.finish();
								break;
							case 3:
								break;
							}
						} else {
							Intent intent = new Intent(context,
									therians.kfc.tagit.Home.class);
							activity.startActivity(intent);
							activity.finish();
						}

					}
				}
			}).start();
		}
	}

	public void about() {
		LayoutInflater vi = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = vi.inflate(R.layout.dialog_about, null);
		new AlertDialog.Builder(context).setView(v)
				.setPositiveButton("OK", null).show();
	}

	public void updateInfo() {
		try{
			TextView tv = (TextView) activity.findViewById(R.id.txtHomeName);
			tv.setText(getSettings().getString("UserName", "Annonymous"));
			tv = (TextView) activity.findViewById(R.id.txtHomePoint);
			tv.setText("Point : " + getSettings().getInt("UserPoint", 0));
			tv = (TextView) activity.findViewById(R.id.txtHomeRank);
			tv.setText("Rank : " + getSettings().getString("UserRank", "Unknown"));
			tv = (TextView) activity.findViewById(R.id.txtHomeAch);
			tv.setText("Total Achievement : " + getSettings().getInt("UserAch", 0)
					+ "/" + getSettings().getInt("AchCount", 0));
			ImageView imgAvatar = (ImageView) activity
					.findViewById(R.id.imgHomeAvatar);
			imgAvatar = setAvatar(imgAvatar, getSettings().getInt("UserAvatar", 1));			
		}catch(Exception e){
			
		}
		
	}

	public User getUser(int id) {
		User u = null;
		List<User> list = db.getUser();
		for (User i : list) {
			if (i.getId() == id) {
				u = i;
				break;
			}
		}
		return u;
	}

	public ImageView setAvatar(ImageView imgAvatar, int avatar) {
		switch (avatar) {
		case 0:
		case 1:
			imgAvatar.setImageResource(R.drawable.b1);
			break;
		case 2:
			imgAvatar.setImageResource(R.drawable.b2);
			break;
		case 3:
			imgAvatar.setImageResource(R.drawable.b3);
			break;
		case 4:
			imgAvatar.setImageResource(R.drawable.b4);
			break;
		case 5:
			imgAvatar.setImageResource(R.drawable.b5);
			break;
		case 6:
			imgAvatar.setImageResource(R.drawable.b6);
			break;
		case 7:
			imgAvatar.setImageResource(R.drawable.b7);
			break;
		case 8:
			imgAvatar.setImageResource(R.drawable.b8);
			break;
		}
		return imgAvatar;
	}

	public void transferPoint(int id) {
		if (id < 0) {
			transferFriend();
		} else {
			transferInput(id);
		}
	}

	private AlertDialog dialog_transferFriend = null;

	private void transferFriend() {
		View v = li.inflate(R.layout.dialog_listview, null);
		ListView lv = (ListView) v.findViewById(R.id.listview);
		lv.setAdapter(new transferAdapter(initList("Accepted")));
		dialog_transferFriend = new AlertDialog.Builder(context)
				.setTitle("Select Friend").setView(v)
				.setPositiveButton("Close", null).show();
	}

	private void transferInput(final int id) {
		View v = li.inflate(R.layout.dialog_input, null);
		final EditText et = (EditText) v.findViewById(R.id.txtInput);
		et.setInputType(InputType.TYPE_CLASS_NUMBER);
		final int curPoint = getSettings().getInt("UserPoint", 0);
		new AlertDialog.Builder(context)
				.setTitle("Available Point : " + curPoint)
				.setView(v)
				.setPositiveButton("Send",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								int pt = Integer.parseInt(et.getText()
										.toString().trim());
								if (pt <= curPoint) {
									Object[] o = sendPoint(id, pt);
									if (o != null) {
										if (Boolean.valueOf(o[0].toString()) == true) {
											Toast.makeText(context,
													"Point transferred",
													Toast.LENGTH_SHORT).show();
											sync(false, 3);
										} else {
											Toast.makeText(context,
													o[1].toString(),
													Toast.LENGTH_SHORT).show();
										}
									} else {
										Toast.makeText(context, Attr.NO_NET,
												Toast.LENGTH_SHORT).show();
									}
								} else {
									Toast.makeText(context,
											"Invalid point input",
											Toast.LENGTH_SHORT).show();
									transferInput(id);
								}

							}
						})
				.setNegativeButton("Back",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								transferFriend();
							}
						}).show();
	}

	private String curStatus = null;

	public List<User> initList(String status) {
		curStatus = status;
		List<Object[]> friend = db.getFriend();
		Log.d("TagIt", friend.size() + " is size of friend");
		List<User> tmp = new ArrayList<User>();
		int me = getSettings().getInt("UserId", -1);
		for (Object[] o : friend) {
			int x = Integer.parseInt(o[0].toString());
			if (x == me || !o[1].toString().equals(curStatus)) {
				continue;
			}
			tmp.add(getUser(x));
		}
		Log.d("TagIt", tmp.size() + " is size tmp");
		return tmp;
	}

	// end front-end functions

	// back-end db functions
	public Object[] signup(String name, String email, String password) {
		Object[] res = new Object[2];
		String no = getMyPhoneNumber();
		String url = Attr.JURL + "?action=addUser" + "&name=" + name
				+ "&email=" + email + "&password=" + password
				+ "&mobile_number=" + no;
		JSONObject obj = fetch(url);
		if (obj == null) {
			return null;
		}
		res = new Object[2];
		String msg = "";
		try {
			msg = obj.get("result").toString();
			res[0] = true;
			res[1] = Integer.parseInt(msg);
		} catch (NumberFormatException e) {
			res[0] = false;
			res[1] = msg;
		} catch (Exception e) {
			Log.e("TagIt Error", e.toString());
			return null;
		}
		return res;
	}

	public Object[] login(String email, String password) {
		Object[] res = new Object[2];
		String no = getMyPhoneNumber();
		String url = Attr.JURL + "?action=getUser" + "&email=" + email
				+ "&password=" + password + "&mobilenumberused=" + no;
		JSONObject obj = fetch(url);
		if (obj == null) {
			return null;
		}
		String msg = "";
		try {
			msg = obj.getString("result");
			Log.d("TagIt", msg);
			res[0] = true;
			res[1] = Integer.parseInt(msg);
		} catch (NumberFormatException e) {
			res[0] = false;
			res[1] = msg;
		} catch (Exception e) {
			Log.e("TagIt Error", e.toString());
			return null;
		}
		return res;
	}

	public Object[] updateUser(String name, String pw, int avatar) {
		Object[] res = null;
		int id = this.getSettings().getInt("UserId", 0);
		String no = getMyPhoneNumber();
		String url = Attr.JURL + "?action=updateUserInfo" + "&id=" + id
				+ "&avatar=" + avatar + "&mobile_number=" + no;
		if (name.length() > 0) {
			url += "&name=" + name;
		}
		if (pw.length() > 0) {
			url += "&password=" + pw;
		}
		JSONObject obj = fetch(url);
		if (obj == null) {
			return null;
		}

		String msg = "";
		try {
			msg = obj.getString("result");
			Log.d("TagIt", msg);
			res = getMsg(msg);
		} catch (Exception e) {
			Log.e("TagIt Error", e.toString());
			return null;
		}

		return res;
	}

	public void updateReceipt(Object[] i) {
		db.updateReceipt(i);
	}

	public List<User> searchUser(String word) {
		List<User> list = new ArrayList<User>();
		String url = Attr.JURL + "?action=searchUser" + "&keyword=" + word;
		JSONObject obj = fetch(url);
		if (obj != null) {
			try {
				String avatar, current_points, email, last_update, rank, total_number_achievement, id, name;
				JSONArray a = obj.getJSONArray("result");
				for (int i = 0; i < a.length(); i++) {
					JSONObject o = a.getJSONObject(i);
					avatar = o.getString("avatar");
					if (avatar.equals("null") || avatar.length() == 0) {
						avatar = "0";
					}
					current_points = o.getString("current_points");
					email = o.getString("email");
					id = o.getString("id");
					last_update = o.getString("last_update");
					name = o.getString("name");
					rank = o.getString("rank");
					total_number_achievement = o
							.getString("total_number_achievement");
					if (Integer.parseInt(id) == getSettings().getInt("UserId",
							-1)) {
						continue;
					}
					list.add(new User(name, email, rank, last_update, Integer
							.parseInt(id), Integer
							.parseInt(total_number_achievement), Integer
							.parseInt(current_points), Integer.parseInt(avatar)));
				}
			} catch (Exception e) {
				Log.d("TagIt Error search user", e.toString());
			}
		}
		return list;
	}

	public Object[] manageFriend(int id, int function) {
		Object[] res = null;
		int me = this.getSettings().getInt("UserId", 0);
		String no = getMyPhoneNumber();
		String url = Attr.JURL + "?mobilenumberused=" + no + "&id=" + me
				+ "&id2=" + id;
		switch (function) {
		case 1:// add
			url += "&action=addFriend";
			break;
		case 2:// accept
			url += "&action=acceptFriend";
			break;
		case 3:// cancel
			url += "&action=cancelFriend";
			break;
		case 4:// ignore
			url += "&action=ignoreFriend";
			break;
		case 5:// remove
			url += "&action=removeFriend";
			break;
		}
		JSONObject obj = fetch(url);
		if (obj == null) {
			return null;
		}
		String msg = "";
		try {
			msg = obj.getString("result");
			Log.d("TagIt", msg);
			res = getMsg(msg);
		} catch (Exception e) {
			Log.e("TagIt Error", e.toString());
			return null;
		}
		return res;
	}

	private Object[] sendPoint(int id, int point) {
		Object[] res = null;
		String no = getMyPhoneNumber();
		int me = getSettings().getInt("UserId", -1);
		String url = Attr.JURL + "?mobilenumberused=" + no + "&id=" + me
				+ "&id2=" + id + "&point=" + point + "&action=transferPoint";
		JSONObject obj = fetch(url);
		if (obj == null) {
			return null;
		}
		String msg = "";
		try {
			msg = obj.getString("result");
			Log.d("TagIt", msg);
			res = getMsg(msg);
		} catch (Exception e) {
			Log.e("TagIt Error", e.toString());
			return null;
		}
		return res;
	}

	public void submitScan() {
		List<Object[]> list = db.getReceipt();
		if (list.size() == 0) {
			return;
		}
		String no = getMyPhoneNumber();
		int id = getSettings().getInt("UserId", -1);
		String url = "";
		JSONObject obj = null;
		for (Object[] o : list) {
			url = Attr.JURL + "?action=scanPoints" + "&id=" + id
					+ "&receiptnumber=" + o[0] + "&mobilenumberused=" + no;
			obj = fetch(url);
			if (obj == null) {
				break;
			}
			try{
				String msg = obj.getString("result");
				Log.d("TagIt", msg);
				Object[] res = getMsg(msg);
				final String str = res[1].toString();
				if(Boolean.valueOf(res[0].toString())==false){
					h.post(new Runnable(){

						public void run() {
							Toast.makeText(context, str, Toast.LENGTH_LONG).show();
						}
						
					});
				}
			}catch(Exception e){
				
			}
		}
	}

	public List<therians.kfc.tagit.db.Log> getLogs() {
		return db.getLog();
	}

	public List<Achievement> getAchievements() {
		return db.getAch();
	}

	public List<Event> getEvent() {
		return db.getEvent();
	}

	public List<Object[]> getRedeem() {
		return db.getRedeem();
	}

	public List<int[]> getUserAch() {
		return db.getUserAch();
	}

	private String doSync() {
		submitScan();
		String msg = "";
		String no = getMyPhoneNumber();
		String url = Attr.JURL + "?action=syncUser&id="
				+ settings.getInt("UserId", -1) + "&mobilenumberused=" + no;
		JSONObject obj = fetch(url);
		if (obj == null) {
			return null;
		}
		try {
			JSONObject arr = obj.getJSONObject("result");

			String description, id, name, point, required_qty, type;
			List<Achievement> ach = new ArrayList<Achievement>();
			JSONArray a = arr.getJSONArray("achievement");
			for (int i = 0; i < a.length(); i++) {

				JSONObject o = a.getJSONObject(i);
				description = o.getString("description");
				id = o.getString("id");
				name = o.getString("name");
				point = o.getString("point");
				required_qty = o.getString("required_qty");
				type = o.getString("type");
				ach.add(new Achievement(Integer.parseInt(id), name,
						description, type, Integer.parseInt(required_qty),
						Integer.parseInt(point)));
			}

			String achievement_id, date_end, date_start, status = "";
			List<Event> event = new ArrayList<Event>();
			a = arr.getJSONArray("event");
			for (int i = 0; i < a.length(); i++) {
				JSONObject o = a.getJSONObject(i);
				achievement_id = o.getString("achievement_id");
				date_end = o.getString("date_end");
				date_start = o.getString("date_start");
				description = o.getString("description");
				name = o.getString("name");
				point = o.getString("point");
				status = o.getString("status");
				event.add(new Event(name, date_start, date_end, description,
						getValue("event", Integer.parseInt(status)), Integer
								.parseInt(point)));
			}

			String user_id;
			List<Object[]> friend = new ArrayList<Object[]>();
			a = arr.getJSONArray("friend");
			for (int i = 0; i < a.length(); i++) {
				JSONObject o = a.getJSONObject(i);
				if (o.has("user_res")) {
					user_id = o.getString("user_res");
				} else {
					user_id = o.getString("user_req");
				}
				int nStatus = Integer.parseInt(o.getString("status"));
				switch (nStatus) {
				case 3:
					status = o.has("user_res") ? "PendingByMe"
							: "PendingByOther";
					break;
				case 4:
					status = "Accepted";
					break;
				}

				Object[] val = { user_id, status };
				friend.add(val);
			}

			String action, action_id, date_created;
			List<therians.kfc.tagit.db.Log> log = new ArrayList<therians.kfc.tagit.db.Log>();
			a = arr.getJSONArray("log");
			for (int i = 0; i < a.length(); i++) {
				JSONObject o = a.getJSONObject(i);
				action = o.getString("action_description");
				action_id = o.getString("action");
				date_created = o.getString("date_created");
				id = o.getString("id");
				user_id = o.getString("user_id");
				log.add(new therians.kfc.tagit.db.Log(Integer.parseInt(id),
						Integer.parseInt(user_id), action, date_created,
						action_id));
			}

			String avatar, current_points, email, last_update, rank, total_number_achievement;
			List<User> user = new ArrayList<User>();
			a = arr.getJSONArray("user");
			for (int i = 0; i < a.length(); i++) {
				JSONObject o = a.getJSONObject(i);
				avatar = o.getString("avatar");
				if (avatar.equals("null") || avatar.length() == 0) {
					avatar = "0";
				}
				current_points = o.getString("current_points");
				email = o.getString("email");
				id = o.getString("id");
				last_update = o.getString("last_update");
				name = o.getString("name");
				rank = o.getString("rank");
				total_number_achievement = o
						.getString("total_number_achievement");
				if (Integer.parseInt(id) == getSettings().getInt("UserId", -1)) {
					putSetting('s', "UserName", name);
					putSetting('s', "UserRank", rank);
					putSetting('s', "UserLastUpdate", last_update);
					putSetting('n', "UserAch", total_number_achievement);
					putSetting('n', "UserPoint", current_points);
					putSetting('n', "UserAvatar", avatar);
				}
				user.add(new User(name, email, rank, last_update, Integer
						.parseInt(id), Integer
						.parseInt(total_number_achievement), Integer
						.parseInt(current_points), Integer.parseInt(avatar)));
			}

			List<int[]> user_ach = new ArrayList<int[]>();
			a = arr.getJSONArray("user_achievement");
			for (int i = 0; i < a.length(); i++) {
				JSONObject o = a.getJSONObject(i);
				achievement_id = o.getString("achievement_id");
				user_id = o.getString("user_id");
				int[] val = { Integer.parseInt(user_id),
						Integer.parseInt(achievement_id) };
				user_ach.add(val);
			}

			String equiv;
			List<Object[]> redeem = new ArrayList<Object[]>();
			a = arr.getJSONArray("redeem");
			for (int i = 0; i < a.length(); i++) {
				JSONObject o = a.getJSONObject(i);
				equiv = o.getString("equivalent");
				point = o.getString("point");
				Object[] val = { equiv, Integer.parseInt(point) };
				redeem.add(val);
			}

			putSetting('n', "AchCount", String.valueOf(ach.size()));
			db.updateAch(ach);
			db.updateEvent(event);
			db.updateFriend(friend);
			db.updateLog(log);
			db.updateUser(user);
			db.updateUserAch(user_ach);
			db.updateRedeem(redeem);
			db.deleteReceipt();
			msg = "200";
		} catch (Exception e) {
			Log.e("TagIt Error", e.toString());
		}
		return msg;
	}

	// end back-end db functions

	// back-end misc functions
	private Object[] getMsg(String msg) {
		Object[] res = new Object[2];
		if (msg.trim().equals("200")) {
			res[0] = true;
		} else {
			res[0] = false;
			res[1] = msg;
		}
		return res;
	}

	private String getMyPhoneNumber() {
		TelephonyManager mTelephonyMgr;
		mTelephonyMgr = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return mTelephonyMgr.getLine1Number();
	}

	private JSONObject fetch(String url) {
		InputStream is = null;
		JSONObject jObj = null;
		String json = "";
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "n");
			}
			Log.d("TagIt", sb.toString());
			is.close();
			json = sb.toString();
			jObj = new JSONObject(new String(json));
		} catch (Exception e) {
			Log.e("TagIt Error", "Error parsing data " + e.toString());
		}
		return jObj;
	}

	private String getValue(String cls, int val) {
		String res = "";
		if (cls.equals("event")) {
			switch (val) {
			case 1:
				res = "Open";
				break;
			case 2:
				res = "Closed";
				break;
			case 3:
				res = "Contact KFC";
				break;
			}
		}
		return res;
	}

	private class transferAdapter extends BaseAdapter {

		private List<User> list = null;
		private LayoutParams params = null;

		public transferAdapter(List<User> list) {
			super();
			this.list = list;
			if (list.size() == 0) {
				list.add(new User());
			}
			params = new LayoutParams(LayoutParams.MATCH_PARENT, 60);
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public View getView(int pos, View v, ViewGroup parent) {
			TextView tv = new TextView(context);
			tv.setBackgroundResource(R.color.white);
			tv.setTextSize(12);
			tv.setLayoutParams(params);
			User i = list.get(pos);
			final int user_id = i.getId();
			if (i.getName() != null) {
				tv.setText(i.getName() + "\n(" + i.getEmail() + ")");
				tv.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						dialog_transferFriend.cancel();
						transferInput(user_id);
					}

				});
			} else {
				tv.setText("No friend in list");
			}
			return tv;
		}

	}
	// end back-end misc functions
}
