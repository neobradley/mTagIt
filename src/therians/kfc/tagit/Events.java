package therians.kfc.tagit;

import java.util.List;

import therians.kfc.tagit.db.Event;
import therians.kfc.tagit.db.Func;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Events extends Activity {

	private ListView lv = null;
	private Func func = null;
	private myAdapter adapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		func = new Func(this, this);
		setContentView(R.layout.event);
		lv = (ListView) findViewById(R.id.listEvent);
		initList();
	}

	private void initList() {
		adapter = new myAdapter(func.getEvent());
		lv.setAdapter(adapter);
	}

	private class myAdapter extends BaseAdapter {

		private List<Event> list = null;

		public myAdapter(List<Event> list) {
			this.list = list;
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		public Object getItem(int pos) {
			// TODO Auto-generated method stub
			return list.get(pos);
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public View getView(int position, View v, ViewGroup parent) {
			if (v == null) {
				LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = li.inflate(R.layout.item_event, null);
			}
			TextView name = (TextView) v.findViewById(R.id.txtEventItemName), desc = (TextView) v
					.findViewById(R.id.txtEventItemDesc), date = (TextView) v
					.findViewById(R.id.txtEventItemDate), point = (TextView) v
					.findViewById(R.id.txtEventItemPoint);

			Event i = list.get(position);
			name.setText(i.getName());
			desc.setText(i.getDesc());
			date.setText(i.getStart()+" to "+i.getEnd());
			point.setText(i.getPoint()+" pts");
			return v;
		}

	}

}
