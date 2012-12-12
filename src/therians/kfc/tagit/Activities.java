package therians.kfc.tagit;

import java.util.ArrayList;
import java.util.List;
import therians.kfc.tagit.db.Func;
import therians.kfc.tagit.db.Log;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Activities extends Activity {

	private Func func = null;
	private ListView lv = null;
	private myAdapter adapter = null;
	private LayoutInflater li = null;
	private int UserId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		func = new Func(this, this);
		setContentView(R.layout.activity);
		Intent intent = this.getIntent();
		UserId = intent.getIntExtra("UserId", -1);
		lv = (ListView) findViewById(R.id.listActivity);
		initList();
	}

	private void initList() {
		li = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		List<Log> list = null, tmp = new ArrayList<Log>();
		list = func.getLogs();
		for (int x = list.size() - 1; x > 0; x--) {
			Log i = list.get(x);
			if (i.getUid() == UserId) {
				tmp.add(i);
			}
		}
		adapter = new myAdapter(tmp);
		lv.setAdapter(adapter);
	}

	private class myAdapter extends BaseAdapter {

		private List<Log> list = null;

		public myAdapter(List<Log> list) {
			this.list = list;
			if (list.size() == 0) {
				list.add(new Log());
			}
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

		public View getView(int position, View v, ViewGroup parent) {
			// TODO Auto-generated method stub
			if (v == null) {
				v = li.inflate(R.layout.item_activity, null);
			}
			TextView dt = (TextView) v.findViewById(R.id.txtActivityItemDT), desc = (TextView) v
					.findViewById(R.id.txtActivityItemDesc), type = (TextView) v
					.findViewById(R.id.txtActivityItemType);
			Log i = list.get(position);
			if (i.getDt() != null) {
				dt.setText(i.getDt());
				desc.setText(i.getDesc());
				type.setText(i.getType());
			}
			return v;
		}

	}
}
