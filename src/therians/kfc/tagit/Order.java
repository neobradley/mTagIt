package therians.kfc.tagit;

import java.util.ArrayList;
import java.util.List;
import therians.kfc.tagit.db.Menu;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Order extends Activity {

	private static int GROUP_NUMBER = 1, CURRENT_PAGE = 0;
	private static List<Menu> CART = new ArrayList<Menu>();
	private LayoutInflater li = null;
	private int[] menu_ids = { R.id.btnOrderMenu, R.id.btnOrderCart, R.id.btnOrderGet };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initCategory();
		li = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE); 
	}

	private void setGroup() {
		View v = li.inflate(R.layout.dialog_input, null);
		final EditText input = (EditText) v.findViewById(R.id.txtInput);
		input.setInputType(InputType.TYPE_CLASS_NUMBER);
		new AlertDialog.Builder(this)
				.setTitle("Group Number")
				.setView(v)
				.setPositiveButton("Enter",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								int val = Integer.parseInt(input.getText()
										.toString());
								GROUP_NUMBER = val > 0 ? val : 1;
								List<Menu> tmp = new ArrayList<Menu>();
								if(GROUP_NUMBER>=CART.size()){
									for(int i=0;i<CART.size();i++){
										tmp.add(CART.get(i));
									}
									CART = tmp;
								}else{
									CART = new ArrayList<Menu>();
									Toast.makeText(Order.this, "Group has been reset", Toast.LENGTH_SHORT).show();
								}
							}
						})
						.setNegativeButton("Cancel", null)
						.show();
	}

	private void initOption(){
		for(int id:menu_ids){
			findViewById(id).setOnClickListener(new DefaultListener());
		}
	}
	
	private void initCategory() {
		int[] ids = { R.id.imgOrderBowlMeals, R.id.imgOrderBoxMeals,
				R.id.imgOrderBuckets, R.id.imgOrderSandwiches,
				R.id.imgOrderChicken, R.id.imgOrderDesserts,
				R.id.imgOrderKrushers, R.id.imgOrderSalads,
				R.id.imgOrderSnacks, R.id.imgOrderTwisters, R.id.imgOrderFixins };
		setContentView(R.layout.order);
		for (int id : ids) {
			findViewById(id).setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					initMenu(v.getId());
				}

			});
		}
		initOption();
		findViewById(R.id.btnOrderKKB).setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				setGroup();
			}
			
		});
		CURRENT_PAGE = 0;
	}

	private void initCart(){
		setContentView(R.layout.order_cart);
		ListView lv = (ListView) findViewById(R.id.listOrderCart);
		lv.setAdapter(new BaseAdapter(){

			public int getCount() {
				return CART.size();
			}

			public Object getItem(int position) {
				return CART.get(position);
			}

			public long getItemId(int position) {
				return position;
			}

			public View getView(final int pos, View v, ViewGroup parent) {
				if(v==null){
					v = li.inflate(R.layout.item_menu, null);
				}
				Menu m = CART.get(pos);
				final int[] owners = m.getOwners();
				TextView txtName = (TextView) v.findViewById(R.id.txtName),
						txtQty = (TextView) v.findViewById(R.id.txtQty);
				txtName.setText(m.getName());
				txtQty.setText("Qty : "+m.getQty());
				v.findViewById(R.id.btnRemove).setOnClickListener(new OnClickListener(){

					public void onClick(View v) {
						// TODO Auto-generated method stub
						new AlertDialog.Builder(Order.this)
						.setTitle("Remove Item?")
						.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								CART.remove(pos);
								initCart();
							}
						})
						.setNegativeButton("Cancel", null)
						.show();
					}
					
				});
				v.setOnLongClickListener(new OnLongClickListener(){

					public boolean onLongClick(View v) {
						String str = "";
						for(int i:owners){
							str+="User "+(i+1)+"\n";
						}
						TextView tv = new TextView(Order.this);
						tv.setTextColor(getResources().getColor(R.color.white));
						tv.setText(str);
						new AlertDialog.Builder(Order.this)
						.setTitle("Buyers")
						.setView(tv)
						.setPositiveButton("Close", null)
						.show();
						return false;
					}
					
				});
				return v;
			}
			
		});
		initOption();
		findViewById(R.id.btnOrderClear).setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				CART = new ArrayList<Menu>();
				initCart();
			}
			
		});
		CURRENT_PAGE = 0;
	}
	
	private void initGet(){
		setContentView(R.layout.order_get);
		TextView tv = (TextView) findViewById(R.id.txtOrderGet);
		String str = "";
		if(CART.size()>0){
			str="ORDER LIST :\n";
			double[] ind_price = new double[GROUP_NUMBER];
			for(int i=0;i<ind_price.length;i++){
				ind_price[i]=0;
			}
			double total_price = 0;
			for(Menu m:CART){
				double sub = m.getPrice()*m.getQty();
				double ind = sub/m.getOwners().length;
				str+=m.getName()+"\nQty : "+m.getQty()+"\t"+sub+"\n";
				total_price += sub;
				for(int i:m.getOwners()){
					ind_price[i]+=ind;
				}
			}
			str+="\nTOTAL PRICE : "+total_price+"\n";
			str+="TOTAL PRICE w/ delivery: "+Math.rint((total_price*1.1))+"\n";
			if(GROUP_NUMBER>1){
				str+="\nIndividual Payments : \n";
				for(int i=0;i<ind_price.length;i++){
					str+="User "+(i+1)+" : "+(ind_price[i])+"\n";
				}
			}
			if(GROUP_NUMBER>1){
				str+="\nIndividual Payments w/ delivery : \n";
				for(int i=0;i<ind_price.length;i++){
					str+="User "+(i+1)+" : "+Math.rint(ind_price[i]*1.1)+"\n";
				}
			}
			str+="\n* Note that minimum delivery amount of P200.00 and standard 10% delivery charge for delivery";
			tv.setText(str);
		}
		initOption();
		findViewById(R.id.btnOrderSend).setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				new AlertDialog.Builder(Order.this)
				.setTitle("Send Order?")
				.setMessage("You will be sending SMS to KFC for ordering. Proceed?")
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(Order.this, "Order sent. Please wait for confirmation reply", Toast.LENGTH_LONG).show();
					}
				})
				.setNegativeButton("No", null)
				.show();
			}
			
		});
		CURRENT_PAGE = 0;
	}
	
	private void initMenu(int category) {
		int[] ids = {};
		switch (category) {
		case R.id.imgOrderBowlMeals:
			setContentView(R.layout.order_bowls);
			ids = new int[] { R.id.btnChickenAlaKing, R.id.btnKungPaoChicken,
					R.id.btnPastaBowl };
			break;
		case R.id.imgOrderBoxMeals:
			setContentView(R.layout.order_box);
			ids = new int[] { R.id.btnFullyLoadedMeal,
					R.id.btnTwisterFullyLoaded };
			break;
		case R.id.imgOrderBuckets:
			setContentView(R.layout.order_buckets);
			ids = new int[] { R.id.btnBucketMeal, R.id.btnStreetwiseBucketMeal,
					R.id.btnHolloweenBucketMeal };
			break;
		case R.id.imgOrderChicken:
			setContentView(R.layout.order_chicken);
			ids = new int[] { R.id.btnBarrel21Chicken, R.id.btnBucket15Chicken,
					R.id.btn2Chicken, R.id.btn1Chicken, R.id.btnBoxOfSix,
					R.id.btnChickenSpagCombo, R.id.btn3WingSale,
					R.id.btnBBQRods };
			break;
		case R.id.imgOrderDesserts:
			setContentView(R.layout.order_desserts);
			ids = new int[] { R.id.btnBrownie,
					R.id.btnMangoCheesecakeSpoonfuls,
					R.id.btnChocolateTiramisuSpoonful,
					R.id.btnChocolateMousseSpoonfuls, R.id.btnChocoCheeseTorte };
			break;
		case R.id.imgOrderKrushers:
			setContentView(R.layout.order_krushers);
			ids = new int[] { R.id.btnKrushedsCoffee, R.id.btnMixedBerries,
					R.id.btnMiniKrushers, R.id.btnStrawberryLush,
					R.id.btnRockinRoad, R.id.btnMangoMania,
					R.id.btnKookiesNCream };
			break;
		case R.id.imgOrderSalads:
			setContentView(R.layout.order_salad);
			ids = new int[] { R.id.btnGardenSalad, R.id.btnChickenSalad };
			break;
		case R.id.imgOrderSandwiches:
			setContentView(R.layout.order_sandwich);
			ids = new int[] { R.id.btnZinger, R.id.btnChickenNFillet,
					R.id.btnChickenBurger, R.id.btnCheeseTopBurger };
			break;
		case R.id.imgOrderSnacks:
			setContentView(R.layout.order_snacks);
			ids = new int[] { R.id.btnSpaghetti, R.id.btnBucketOfFries,
					R.id.btnFunshots1, R.id.btnFunshots2, R.id.btnHotShots1,
					R.id.btnHotShots2, R.id.btnSnackbox,
					R.id.btnOriginalRecipeBites, R.id.btnMacNCheese };
			break;
		case R.id.imgOrderTwisters:
			setContentView(R.layout.order_toasted_line);
			ids = new int[] { R.id.btnCaliMakiTwister };
			break;
		case R.id.imgOrderFixins:
			setContentView(R.layout.order_fixins);
			ids = new int[] { R.id.btnCrispyFries1, R.id.btnCrispyFries2,
					R.id.btnMacaroniSalad1, R.id.btnMacaroniSalad2,
					R.id.btnColeslaw1, R.id.btnColeslaw2,
					R.id.btnMashedPotato1, R.id.btnMashedPotato2,
					R.id.btnMushroomSoup };
			break;
		}
		if (ids.length > 0) {
			for (int id : ids) {
				findViewById(id).setOnClickListener(new MenuListener());
			}
		}
		initOption();
		CURRENT_PAGE = 1;
	}

	@Override
	public void onBackPressed() {
		switch (CURRENT_PAGE) {
		case 0:
			Order.this.finish();
		case 1:
			initCategory();
			break;
		default:
			super.onBackPressed();
		}
	}

	private void initKKBDialog(final Menu m) {
		View v = li.inflate(R.layout.dialog_input, null);
		final EditText input = (EditText) v.findViewById(R.id.txtInput);
		input.setInputType(InputType.TYPE_CLASS_NUMBER);
		new AlertDialog.Builder(this)
		.setTitle(m.getName())
		.setView(v)
		.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				final int qty = Integer.parseInt(input.getText().toString());
				if(GROUP_NUMBER==1){
					addCartItem(m, qty, new int[]{0});
					Toast.makeText(Order.this, "Item added", Toast.LENGTH_SHORT).show();
				}else{
					final boolean[] checked = new boolean[GROUP_NUMBER];
					ListView lv = (ListView) li.inflate(R.layout.dialog_listview, null);
					lv.setAdapter(new BaseAdapter(){
						
						public int getCount() {
							return GROUP_NUMBER;
						}

						public Object getItem(int pos) {
							return null;
						}

						public long getItemId(int position) {
							return position;
						}

						public View getView(final int pos, View v,
								ViewGroup parent) {
							LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
							LinearLayout ll = new LinearLayout(Order.this);
							ll.setOrientation(LinearLayout.HORIZONTAL);
							CheckBox cb = new CheckBox(Order.this);
							if(pos==0){
								cb.setChecked(true);
								checked[pos]=true;
							}else{
								checked[pos]=false;
							}
							cb.setOnCheckedChangeListener(new OnCheckedChangeListener(){

								public void onCheckedChanged(
										CompoundButton buttonView,
										boolean isChecked) {
									checked[pos]=isChecked;
								}
								
							});
							TextView tv = new TextView(Order.this);
							tv.setTextColor(getResources().getColor(R.color.white));
							tv.setText("User "+ (pos+1));
							ll.addView(cb, params);
							ll.addView(tv, params);
							return ll;
						}
						
					});
					new AlertDialog.Builder(Order.this)
					.setTitle("Choose Buyer")
					.setView(lv)
					.setPositiveButton("Buy", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							int count =  0;
							for(boolean b:checked){
								if(b){
									count++;
								}
							}
							if(count==0){
								Toast.makeText(Order.this, "Item not added", Toast.LENGTH_SHORT).show();
							}else{
								int[] owner = new int[count];
								for(int i=0,j=0;i<checked.length;i++){
									if(checked[i]==true){
										owner[j++]=i;
									}
								}
								addCartItem(m,qty,owner);
								Toast.makeText(Order.this, "Item added", Toast.LENGTH_SHORT).show();
							}
						}
					})
					.setNegativeButton("Cancel", null)
					.show();
				}
			}
		})
		.setNegativeButton("Cancel", null)
		.show();
	}

	private void addCartItem(Menu m, int qty, int[] owner) {
		m.setQty(qty);
		m.setOwners(owner);
		CART.add(m);
	}
	
	private class MenuListener implements OnClickListener {

		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btnChickenAlaKing:
				initKKBDialog(new Menu("Chicken Ala King", "Bowls", 90));
				break;
			case R.id.btnKungPaoChicken:
				initKKBDialog(new Menu("Kung Pao Chicken", "Bowls", 90));
				break;
			case R.id.btnPastaBowl:
				initKKBDialog(new Menu("Pasta Bowl", "Bowls", 80));
				break;
			case R.id.btnFullyLoadedMeal:
				initKKBDialog(new Menu("Fully Loaded Meal", "Box Meals", 146));
				break;
			case R.id.btnTwisterFullyLoaded:
				initKKBDialog(new Menu("Twister Fully Loaded", "Box Meals", 165));
				break;
			case R.id.btnBucketMeal:
				initKKBDialog(new Menu("Bucket Meal", "Bucket Meals", 540));
				break;
			case R.id.btnStreetwiseBucketMeal:
				initKKBDialog(new Menu("Streetwise Bucket Meal",
						"Bucket Meals", 399));
				break;
			case R.id.btnHolloweenBucketMeal:
				initKKBDialog(new Menu("Holloween Bucket Meal", "Bucket Meals",
						620));
				break;
			case R.id.btnBarrel21Chicken:
				initKKBDialog(new Menu("Barrel 21 Chicken", "Chicken", 1245));
				break;
			case R.id.btnBucket15Chicken:
				initKKBDialog(new Menu("Bucket 15 Chicken", "Chicken", 900));
				break;
			case R.id.btn2Chicken:
				initKKBDialog(new Menu("2 pcs Chicken", "Chicken", 121));
				break;
			case R.id.btn1Chicken:
				initKKBDialog(new Menu("1 pc Chicken", "Chicken", 75));
				break;
			case R.id.btnBoxOfSix:
				initKKBDialog(new Menu("Box Of Six", "Chicken", 360));
				break;
			case R.id.btnChickenSpagCombo:
				initKKBDialog(new Menu("Chicken Spagetti Combo", "Chicken", 112));
				break;
			case R.id.btn3WingSale:
				initKKBDialog(new Menu("3-Wing Sale", "Chicken", 110));
				break;
			case R.id.btnBBQRods:
				initKKBDialog(new Menu("BBQ Rods", "Chicken", 75));
				break;
			case R.id.btnBrownie:
				initKKBDialog(new Menu("Brownie", "Desserts", 21));
				break;
			case R.id.btnMangoCheesecakeSpoonfuls:
				initKKBDialog(new Menu("Mango Cheesecake Spoonfuls",
						"Desserts", 29));
				break;
			case R.id.btnChocolateTiramisuSpoonful:
				initKKBDialog(new Menu("Chocolate Tiramisu Spoonful",
						"Desserts", 29));
				break;
			case R.id.btnChocolateMousseSpoonfuls:
				initKKBDialog(new Menu("Chocolate Mousse Spoonfuls",
						"Desserts", 29));
				break;
			case R.id.btnChocoCheeseTorte:
				initKKBDialog(new Menu("Choco Cheese Torte", "Desserts", 29));
				break;
			case R.id.btnKrushedsCoffee:
				initKKBDialog(new Menu("Krushers Coffee", "Krushers", 65));
				break;
			case R.id.btnMixedBerries:
				initKKBDialog(new Menu("Mixed Berries", "Krushers", 75));
				break;
			case R.id.btnMiniKrushers:
				initKKBDialog(new Menu("Mini Krushers", "Krushers", 47));
				break;
			case R.id.btnStrawberryLush:
				initKKBDialog(new Menu("Strawberry Lush", "Krushers", 75));
				break;
			case R.id.btnRockinRoad:
				initKKBDialog(new Menu("Rockin' Road", "Krushers", 65));
				break;
			case R.id.btnMangoMania:
				initKKBDialog(new Menu("Mango Mania", "Krushers", 75));
				break;
			case R.id.btnKookiesNCream:
				initKKBDialog(new Menu("Kookies n' Cream", "Krushers", 65));
				break;
			case R.id.btnGardenSalad:
				initKKBDialog(new Menu("Garden Salad", "Salad", 45));
				break;
			case R.id.btnChickenSalad:
				initKKBDialog(new Menu("Chicken Salad", "Salad", 95));
				break;
			case R.id.btnZinger:
				initKKBDialog(new Menu("Zinger", "Sandwiches", 106));
				break;
			case R.id.btnChickenNFillet:
				initKKBDialog(new Menu("Chicken'N Fillet", "Sandwiches", 48));
				break;
			case R.id.btnChickenBurger:
				initKKBDialog(new Menu("Chicken Burger", "Sandwiches", 29));
				break;
			case R.id.btnCheeseTopBurger:
				initKKBDialog(new Menu("Cheese Top Burger", "Sandwiches", 48));
				break;
			case R.id.btnCaliMakiTwister:
				initKKBDialog(new Menu("Cali Maki Twister", "Toasted Line", 80));
				break;
			case R.id.btnCrispyFries1:
				initKKBDialog(new Menu("Crispy Fries", "Fixins", 29));
				break;
			case R.id.btnCrispyFries2:
				initKKBDialog(new Menu("Crispy Fries", "Fixins", 46));
				break;
			case R.id.btnMacaroniSalad1:
				initKKBDialog(new Menu("Macaroni Salad", "Fixins", 29));
				break;
			case R.id.btnMacaroniSalad2:
				initKKBDialog(new Menu("Macaroni Salad", "Fixins", 46));
				break;
			case R.id.btnColeslaw1:
				initKKBDialog(new Menu("Coleslaw", "Fixins", 29));
				break;
			case R.id.btnColeslaw2:
				initKKBDialog(new Menu("Coleslaw", "Fixins", 46));
				break;
			case R.id.btnMashedPotato1:
				initKKBDialog(new Menu("Mashed Potato", "Fixins", 29));
				break;
			case R.id.btnMashedPotato2:
				initKKBDialog(new Menu("Mashed Potato", "Fixins", 46));
				break;
			case R.id.btnMushroomSoup:
				initKKBDialog(new Menu("Mushroom Soup", "Fixins", 29));
				break;
			case R.id.btnSpaghetti:
				initKKBDialog(new Menu("Spaghetti", "Snacks", 45));
				break;
			case R.id.btnBucketOfFries:
				initKKBDialog(new Menu("Bucket of Fries", "Snacks", 58));
				break;
			case R.id.btnFunshots1:
				initKKBDialog(new Menu("KFC Funshots", "Snacks", 55));
				break;
			case R.id.btnFunshots2:
				initKKBDialog(new Menu("KFC Funshots", "Snacks", 100));
				break;
			case R.id.btnHotShots1:
				initKKBDialog(new Menu("Hot Shots", "Snacks", 55));
				break;
			case R.id.btnHotShots2:
				initKKBDialog(new Menu("Hot Shots", "Snacks", 100));
				break;
			case R.id.btnSnackbox:
				initKKBDialog(new Menu("Snackbox", "Snacks", 50));
				break;
			case R.id.btnOriginalRecipeBites:
				initKKBDialog(new Menu("Original Recipe Bites", "Snacks", 70));
				break;
			case R.id.btnMacNCheese:
				initKKBDialog(new Menu("Mac and Cheese", "Snacks", 49));
				break;
			}
		}

	}
	
	private class DefaultListener implements OnClickListener{

		public void onClick(View v) {
			switch(v.getId()){
			case R.id.btnOrderMenu:
				initCategory();
				break;
			case R.id.btnOrderCart:
				initCart();
				break;
			case  R.id.btnOrderGet:
				initGet();
				break;
			}
		}
		
	}
}