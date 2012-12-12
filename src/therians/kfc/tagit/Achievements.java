package therians.kfc.tagit;

import java.util.ArrayList;
import java.util.List;
import therians.kfc.tagit.db.*;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class Achievements extends Activity {

	private Func func = null;
	private int UserId;
	private LayoutInflater li = null;
	private ListView lv = null;
	private myAdapter adapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.achivement);
		func = new Func(this, this);
		Intent intent = this.getIntent();
		UserId = intent.getIntExtra("UserId", -1);
		initList();
	}

	private void initList() {
		li = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		lv = (ListView) this.findViewById(R.id.listAchievement);
		List<Achievement> list = func.getAchievements(), tmp = new ArrayList<Achievement>();
		List<int[]> userAch = func.getUserAch();
		for (int x = list.size() - 1; x >= 0; x--) {
			Achievement i = list.get(x);
			for (int is[] : userAch) {
				if (is[0] == UserId && is[1] == i.getId()) {
					i.setAch(true);
					break;
				}
			}
			tmp.add(0,i);
		}
		adapter = new myAdapter(tmp, this);
		lv.setAdapter(adapter);

	}

	private class myAdapter extends BaseAdapter {

		private List<Achievement> list = null;
		@SuppressWarnings("unused")
		private Context context = null;

		public myAdapter(List<Achievement> list, Context context) {
			super();
			this.list = list;
			this.context = context;
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
			if (v == null) {
				v = li.inflate(R.layout.item_achievement, null);
			}
			TextView title = (TextView) v.findViewById(R.id.txtAchTitle), desc = (TextView) v
					.findViewById(R.id.txtAchDesc), req = (TextView) v
					.findViewById(R.id.txtAchReq), point = (TextView) v
					.findViewById(R.id.txtAchPoint);
			Achievement i = list.get(pos);
			title.setText(i.getType()+" : "+i.getName());
			desc.setText(i.getDesc());
			req.setText("Req : "+i.getReq());
			point.setText(i.getPoint()+" pts");
			ImageView imgAch = (ImageView) v.findViewById(R.id.imgAchieved);
			if(i.isAch()==true){
				imgAch.setImageResource(R.drawable.stamp_light);
			}else{
				imgAch.setImageResource(R.drawable.stamp_black);
			}
			return v;
		}

	}

}
