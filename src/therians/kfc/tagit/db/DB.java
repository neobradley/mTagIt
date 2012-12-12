package therians.kfc.tagit.db;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DB extends SQLiteOpenHelper {

	// private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private final static String[] tbls = {
			"CREATE TABLE User (UserId int, UserName text, UserEmail text, UserAch int, UserRank text, UserPoint int, UserAvatar int, UserLastUpdate text)",
			"CREATE TABLE Menu (MenuName text, MenuPrice double, MenuType text)",
			"CREATE TABLE UserAch (UAUId int, UAAId int)",
			"CREATE TABLE Ach (AId int, AName text, ADesc text, AType text, AReq int, APoint int)",
			"CREATE TABLE Friend (FId int, FStatus text)",
			"CREATE TABLE Receipt (RNumber int, RDC text)",
			"CREATE TABLE Redeem (REqv text, RPoint int)",
			"CREATE TABLE Event (EName text, EStart text, EEnd text, EPoint int, EDesc text, EStatus text)",
			"CREATE TABLE Log (LogId int, LogUId int, LogDesc text, LogDT text, LogType text)" };

	public DB(Context context) {
		super(context, Attr.DBN, null, Attr.DBV);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		for (String tbl : tbls) {
			db.execSQL(tbl);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}

	public List<User> getUser() {
		List<User> list = new ArrayList<User>();
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cur = db.rawQuery("SELECT * from User", null);
		if (cur.moveToFirst()) {
			do {
				list.add(new User(
						cur.getString(cur.getColumnIndex("UserName")), cur
								.getString(cur.getColumnIndex("UserEmail")),
						cur.getString(cur.getColumnIndex("UserRank")),
						cur.getString(cur.getColumnIndex("UserLastUpdate")),
						cur.getInt(cur.getColumnIndex("UserId")), cur
								.getInt(cur.getColumnIndex("UserAch")), cur
								.getInt(cur.getColumnIndex("UserPoint")), cur
								.getInt(cur.getColumnIndex("UserAvatar"))));
			} while (cur.moveToNext());
		}
		cur.close();
		db.close();
		return list;
	}

	public void updateUser(List<User> list) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete("User", null, null);
		ContentValues cv = new ContentValues();
		for (User i : list) {
			cv.put("UserId", i.getId());
			cv.put("UserName", i.getName());
			cv.put("UserEmail", i.getEmail());
			cv.put("UserAch", i.getAch());
			cv.put("UserRank", i.getRank());
			cv.put("UserPoint", i.getPoint());
			cv.put("UserAvatar", i.getAvatar());
			cv.put("UserLastUpdate", i.getLastUpdate());
			db.insert("User", null, cv);
		}
		db.close();
	}

	public List<Menu> getMenu() {
		List<Menu> list = new ArrayList<Menu>();
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cur = db.rawQuery("SELECT * from Menu", null);
		if (cur.moveToFirst()) {
			do {
				list.add(new Menu(cur.getString(1), cur.getString(2), cur
						.getDouble(3)));
			} while (cur.moveToNext());
		}
		cur.close();
		db.close();
		return list;
	}

	public void updateMenu(List<Menu> list) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete("Menu", null, null);
		ContentValues cv = new ContentValues();
		for (Menu i : list) {
			cv.put("MenuName", i.getName());
			cv.put("MenuType", i.getType());
			cv.put("MenuPrice", i.getPrice());
			db.insert("Menu", null, cv);
		}
		db.close();
	}

	public List<int[]> getUserAch() {
		List<int[]> list = new ArrayList<int[]>();
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cur = db.rawQuery("SELECT * from UserAch", null);
		if (cur.moveToFirst()) {
			do {
				// UserAch (UAUId int, UAAId int
				int[] val = { cur.getInt(cur.getColumnIndex("UAUId")),
						cur.getInt(cur.getColumnIndex("UAAId")) };
				list.add(val);
			} while (cur.moveToNext());
		}
		cur.close();
		db.close();
		return list;
	}

	public void updateUserAch(List<int[]> list) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete("UserAch", null, null);
		ContentValues cv = new ContentValues();
		for (int[] i : list) {
			cv.put("UAUId", i[0]);
			cv.put("UAAId", i[1]);
			db.insert("UserAch", null, cv);
		}
		db.close();
	}

	public List<Achievement> getAch() {
		List<Achievement> list = new ArrayList<Achievement>();
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cur = db.rawQuery("SELECT * from Ach", null);
		if (cur.moveToFirst()) {
			do {
				list.add(new Achievement(cur.getInt(cur.getColumnIndex("AId")),
						cur.getString(cur.getColumnIndex("AName")), cur
								.getString(cur.getColumnIndex("ADesc")), cur
								.getString(cur.getColumnIndex("AType")), cur
								.getInt(cur.getColumnIndex("AReq")), cur.getInt(cur
								.getColumnIndex("APoint"))));
			} while (cur.moveToNext());
		}
		cur.close();
		db.close();
		return list;
	}

	public void updateAch(List<Achievement> list) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete("Ach", null, null);
		ContentValues cv = new ContentValues();
		for (Achievement i : list) {
			cv.put("AId", i.getId());
			cv.put("AName", i.getName());
			cv.put("ADesc", i.getDesc());
			cv.put("AType", i.getType());
			cv.put("AReq", i.getReq());
			cv.put("APoint", i.getPoint());
			db.insert("Ach", null, cv);
		}
		db.close();
	}

	public List<Object[]> getFriend() {
		List<Object[]> list = new ArrayList<Object[]>();
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cur = db.rawQuery("SELECT * from Friend", null);
		if (cur.moveToFirst()) {
			do {
				Object[] val = { cur.getInt(cur.getColumnIndex("FId")),
						cur.getString(cur.getColumnIndex("FStatus")) };
				list.add(val);
			} while (cur.moveToNext());
		}
		cur.close();
		db.close();
		return list;
	}

	public void updateFriend(List<Object[]> list) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete("Friend", null, null);
		ContentValues cv = new ContentValues();
		for (Object[] i : list) {
			cv.put("FId", Integer.parseInt(i[0].toString()));
			cv.put("FStatus", i[1].toString());
			db.insert("Friend", null, cv);
		}
		db.close();
	}

	public List<Object[]> getReceipt() {
		List<Object[]> list = new ArrayList<Object[]>();
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cur = db.rawQuery("SELECT * from Receipt", null);
		if (cur.moveToFirst()) {
			do {
				Object[] val = { cur.getInt(cur.getColumnIndex("RNumber")),
						cur.getString(cur.getColumnIndex("RDC")) };
				list.add(val);
			} while (cur.moveToNext());
		}
		cur.close();
		db.close();
		return list;
	}

	public void deleteReceipt() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete("Receipt", null, null);
		db.close();
	}

	public void updateReceipt(Object[] i) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("RNumber", Integer.parseInt(i[0].toString()));
		cv.put("RDC", i[1].toString());
		db.insert("Receipt", null, cv);
		db.close();
	}

	public List<Object[]> getRedeem() {
		List<Object[]> list = new ArrayList<Object[]>();
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cur = db.rawQuery("SELECT * from Redeem", null);
		if (cur.moveToFirst()) {
			do {
				Object[] val = { cur.getString(cur.getColumnIndex("REqv")),
						cur.getInt(cur.getColumnIndex("RPoint")) };
				list.add(val);
			} while (cur.moveToNext());
		}
		cur.close();
		db.close();
		return list;
	}

	public void updateRedeem(List<Object[]> list) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete("Redeem", null, null);
		ContentValues cv = new ContentValues();
		for (Object[] i : list) {
			cv.put("REqv", i[0].toString());
			cv.put("RPoint", Integer.parseInt(i[1].toString()));
			db.insert("Redeem", null, cv);
		}
		db.close();
	}

	public List<Event> getEvent() {
		List<Event> list = new ArrayList<Event>();
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cur = db.rawQuery("SELECT * from Event", null);
		if (cur.moveToFirst()) {
			do {
				list.add(new Event(cur.getString(cur.getColumnIndex("EName")),
						cur.getString(cur.getColumnIndex("EStart")), cur
								.getString(cur.getColumnIndex("EEnd")), cur
								.getString(cur.getColumnIndex("EDesc")), cur
								.getString(cur.getColumnIndex("EStatus")), cur
								.getInt(cur.getColumnIndex("EPoint"))));
			} while (cur.moveToNext());
		}
		cur.close();
		db.close();
		return list;
	}

	public void updateEvent(List<Event> list) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete("Event", null, null);
		ContentValues cv = new ContentValues();
		for (Event i : list) {
			cv.put("EName", i.getName());
			cv.put("EStart", i.getStart());
			cv.put("EEnd", i.getEnd());
			cv.put("EPoint", i.getPoint());
			cv.put("EDesc", i.getDesc());
			cv.put("EStatus", i.getStatus());
			db.insert("Event", null, cv);
		}
		db.close();
	}

	public List<Log> getLog() {
		List<Log> list = new ArrayList<Log>();
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cur = db.rawQuery("SELECT * from Log", null);
		if (cur.moveToFirst()) {
			do {
				list.add(new Log(cur.getInt(cur.getColumnIndex("LogId")), cur
						.getInt(cur.getColumnIndex("LogUId")), cur
						.getString(cur.getColumnIndex("LogDesc")), cur
						.getString(cur.getColumnIndex("LogDT")), cur
						.getString(cur.getColumnIndex("LogType"))));
			} while (cur.moveToNext());
		}
		cur.close();
		db.close();
		return list;
	}

	public void updateLog(List<Log> list) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete("Log", null, null);
		ContentValues cv = new ContentValues();
		for (Log i : list) {
			cv.put("LogId", i.getId());
			cv.put("LogUId", i.getUid());
			cv.put("LogDesc", i.getDesc());
			cv.put("LogDT", i.getDt());
			cv.put("LogType", i.getType());
			db.insert("Log", null, cv);
		}
		db.close();
	}

	public String[] getSetting() {
		SQLiteDatabase db = this.getReadableDatabase();
		try {

		} finally {
			db.close();
		}
		return null;
	}
}
