package com.example.hbhri;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import org.w3c.dom.Text;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {

    private View content;

    private List<Integer> mList = new ArrayList<>();
    private List<ImageView> mImageList = new ArrayList<>();
    private LinearLayout container;
    private ViewPager pager;
    private MyHandler mHandler;
    private int currentPosition;

    private ListView mListView;
    private String[] mtitles;
    private String[] mContent1;
    private String[] mContent2;
    private String[] mContent3;
    private static int MESSAGE_IN_QUEUE = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mList.add(R.drawable.satelite_one);
        mList.add(R.drawable.satelite_two);
        mList.add(R.drawable.satelite_three);
        mList.add(R.drawable.satelite_four);

        for (int i = 0; i < mList.size(); i++) {
            ImageView iv = new ImageView(getActivity());
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            iv.setImageResource(mList.get(i));
            mImageList.add(iv);
        }

       String[] titles ={getContext().getResources().getString(R.string.gaofen_title_1),getContext().getResources().getString(R.string.gaofen_title_2),getContext().getResources().getString(R.string.gaofen_title_3),getContext().getResources().getString(R.string.gaofen_title_4)};
       String[] content1 ={getContext().getResources().getString(R.string.gaofen_content_1_1),getContext().getResources().getString(R.string.gaofen_content_2_1),getContext().getResources().getString(R.string.gaofen_content_3_1),getContext().getResources().getString(R.string.gaofen_content_4_1)};
       String[] content2 ={getContext().getResources().getString(R.string.gaofen_content_1_2),getContext().getResources().getString(R.string.gaofen_content_2_2),getContext().getResources().getString(R.string.gaofen_content_3_2),getContext().getResources().getString(R.string.gaofen_content_4_2)};
       String[] content3 ={getContext().getResources().getString(R.string.gaofen_content_1_3),getContext().getResources().getString(R.string.gaofen_content_2_3),getContext().getResources().getString(R.string.gaofen_content_3_3),getContext().getResources().getString(R.string.gaofen_content_4_3)};

        mtitles = titles;
        mContent1 = content1;
        mContent2 = content2;
        mContent3 = content3;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.mainfragment_one, container, false);
        initIndicator(view);

        pager = (ViewPager) view.findViewById(R.id.satelite_pager);

        pager.setAdapter(new PagerAdapter() {
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = mImageList.get(position % mList.size());
                if(view.getParent()!=null){
                   ViewGroup parent = (ViewGroup) view.getParent();
                    parent.removeView(view);
                }
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {

            }

            @Override
            public int getCount() {
                return Integer.MAX_VALUE;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
        });

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeIndicatorImage(position);
                currentPosition = position;
            }



            @Override
            public void onPageScrollStateChanged(int state) {
                /**
                 * 如果当前页面将滑动到下一页 清空队列中的消息（如果队列中有消息的话 说明这个页面跳转由滑动触发） 并重新发送一个消息
                 */
                if(state==ViewPager.SCROLL_STATE_SETTLING){
                    if (mHandler.hasMessages(MESSAGE_IN_QUEUE)) {
                        mHandler.removeMessages(MESSAGE_IN_QUEUE);
                    }
                    Message msg = Message.obtain();
                    msg.what = MESSAGE_IN_QUEUE;
                    mHandler.sendMessageDelayed(msg,2000);
                }
            }
        });
        content = view;
        return view;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initListView(content);

        pager.setCurrentItem(Integer.MAX_VALUE/2-(Integer.MAX_VALUE/2)%mList.size());
        currentPosition = Integer.MAX_VALUE/2-(Integer.MAX_VALUE/2)%mList.size();

        mHandler = new MyHandler(this);
        Message msg = Message.obtain();
        msg.what = MESSAGE_IN_QUEUE;
        mHandler.sendMessageDelayed(msg,2000);


    }

    private void changeIndicatorImage(int position) {
            position = position%mList.size();
        for(int i=0;i<mList.size();i++){
            View view = container.getChildAt(i);
            if(view instanceof  ImageView && i==position){
                ((ImageView) view).setImageResource(R.drawable.page_indicator_focused);
            }else if(view instanceof  ImageView){
                ((ImageView) view).setImageResource(R.drawable.page_indicator_unfocused);
            }
        }
    }

    private void initIndicator(View view) {
        container = (LinearLayout) view.findViewById(R.id.indicatorContainer);
        for (int i=0;i<mList.size();i++){
            ImageView iv = new ImageView(getActivity());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.rightMargin =5;
            iv.setLayoutParams(params);
            if(i==0){
                iv.setImageResource(R.drawable.page_indicator_focused);
            }else{
                iv.setImageResource(R.drawable.page_indicator_unfocused);
            }
            container.addView(iv);
        }
    }

    private void initListView(View view) {
        mListView = (ListView) view.findViewById(R.id.lv_gf);
        mListView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return mtitles.length;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ViewHolder holder = null;
                if(convertView==null){
                    View view = View.inflate(getContext(),R.layout.gf_item,null);
                    holder = new ViewHolder();
                    holder.title = (TextView)view.findViewById(R.id.gf_text_title);
                    holder.content1 = (TextView)view.findViewById(R.id.gf_text_content1);
                    holder.content2 =(TextView)view.findViewById(R.id.gf_text_content2);
                    holder.content3 = (TextView)view.findViewById(R.id.gf_text_content3);
                    holder.iv = (ImageView)view.findViewById(R.id.gf_iv);
                    convertView = view;
                    convertView.setTag(holder);
                }else{
                    holder = (ViewHolder) convertView.getTag();
                }
                holder.title.setText(mtitles[position]);
                holder.content1.setText(mContent1[position]);
                holder.content2.setText(mContent2[position]);
                holder.content3.setText(mContent3[position]);

                return convertView;
            }
        });

    }

    private static class ViewHolder {
        TextView title;
        TextView content1;
        TextView content2;
        TextView content3;
        ImageView iv;
    }

    private static class MyHandler extends Handler{
        private WeakReference<MainFragment> mFragment;
        public MyHandler(MainFragment fragment){
            mFragment = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mFragment.get().changePage();

        }
    }

    public void changePage(){
        currentPosition++;
        pager.setCurrentItem(currentPosition);
    }
}
