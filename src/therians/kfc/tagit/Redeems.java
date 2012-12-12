package therians.kfc.tagit;

import java.util.List;

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

public class Redeems extends Activity {

	private ListView lv = null;
	private Func func = null;
	private myAdapter adapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		func = new Func(this, this);
		setContentView(R.layout.redeem);
		lv = (ListView) findViewById(R.id.listRedeem);
		initList();
	}

	private void initList() {
		adapter = new myAdapter(func.getRedeem());
		lv.setAdapter(adapter);
	}

	private class myAdapter extends BaseAdapter {

		private List<Object[]> list = null;

		public myAdapter(List<Object[]> list) {
			this.list = list;
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
				LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = li.inflate(R.layout.item_redeem, null);
			}
			TextView name = (TextView) v.findViewById(R.id.txtItemRedeemName), point = (TextView) v
					.findViewById(R.id.txtItemRedeemPoint);
			Object[] i = list.get(position);
			name.setText(i[0].toString());
			point.setText(i[1].toString()+" pts required");
			return v;
		}

	}
}
