package com.example.hbhri;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;

public class MainActivity extends AppCompatActivity {

	private Toolbar mToolbar;
	private DrawerLayout mDrawer;
	private ListView mList;
	private PagerSlidingTabStrip tabs;
	private ViewPager pager;

	private boolean closeFlag = false;

	private FragmentManager fm;

	private String[] strs ={"登录","浏览","设置","关于"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);



		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		mDrawer = (DrawerLayout) findViewById(R.id.drawer);
		mList = (ListView) findViewById(R.id.drawer_list);
		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		pager = (ViewPager)findViewById(R.id.pager);

		initDrawer();

		initPager();

//		Intent intent = new Intent(this,TileViewTest.class);
//		startActivity(intent);

	}

	private void initPager() {
		pager.setAdapter(new FragmentPagerAdapter(fm) {

			private String[] titles = {"高分一号","待定","待定"};
			@Override
			public Fragment getItem(int position) {
				MainFragment fragment = null;
				switch (position){
					case 0:
						fragment = new MainFragment();
						break;
					default:
						fragment = new MainFragment();
						break;
				}
				return fragment;
			}

			@Override
			public int getCount() {
				return 3;
			}

			@Override
			public CharSequence getPageTitle(int position) {
				return titles[position];
			}
		});
		tabs.setViewPager(pager);
	}

	private void initDrawer() {
		fm = getSupportFragmentManager();


		mToolbar.setTitle("首页");
		setSupportActionBar(mToolbar);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,mDrawer,mToolbar, R.string.open_string,R.string.close_string){

			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
				closeFlag = false;
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				closeFlag = true;
			}

		};
		mDrawer.setDrawerListener(toggle);
		toggle.syncState();

		mList.setAdapter(new MyAdapter());
		mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				switch (position){
					case 0:

						break;
					case 1:
						Intent intent = new Intent(MainActivity.this,MapActivity.class);

						startActivity(intent);
						break;

				}
			}
		});
	}


	private class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return strs.length;
		}

		@Override
		public Object getItem(int position) {
			return strs[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(getBaseContext(), R.layout.drawer_list_item, null);
			ImageView iv = (ImageView) view.findViewById(R.id.list_iv);
			TextView tv = (TextView) view.findViewById(R.id.list_tv);
			iv.setImageResource(R.drawable.ic_visibility_black_24dp);
			tv.setText(strs[position]);
			return view;

		}

	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(closeFlag){
			mDrawer.closeDrawers();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.activity_open,R.anim.activity_close);
	}
}
