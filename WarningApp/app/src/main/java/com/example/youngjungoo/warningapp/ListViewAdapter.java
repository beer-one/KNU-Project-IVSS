package com.example.youngjungoo.warningapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.net.URL;
import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>();


    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();


        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView) ;
        TextView fileNameText = (TextView) convertView.findViewById(R.id.fileNameText) ;
        TextView timeText = (TextView) convertView.findViewById(R.id.timeText) ;

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        ListViewItem listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        //imageView.setImageURI(listViewItem.getIcon());

        // Glide를 통한 url iamge Get 네트워킹
        Glide.with(context).load(listViewItem.getIcon().toString()).into(imageView);
        fileNameText.setText(listViewItem.getTitle());
        timeText.setText(listViewItem.getDesc());

        //   Glide.with(getBaseContext())
        //                .load(url.toString()).into(imageView);

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(URL url, String title, String desc) {
        ListViewItem item = new ListViewItem();

        item.setIcon(url);
        item.setTitle(title);
        item.setDesc(desc);

        listViewItemList.add(item);
    }

    public void refreshAdapter(ListViewAdapter items) {
        this.listViewItemList.clear();
        this.listViewItemList.addAll(items.listViewItemList);
        notifyDataSetChanged();
    }


}