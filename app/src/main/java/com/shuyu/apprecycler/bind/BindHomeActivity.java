package com.shuyu.apprecycler.bind;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.shuyu.apprecycler.R;
import com.shuyu.apprecycler.bind.activity.BindEmptyActivity;
import com.shuyu.apprecycler.bind.activity.BindHorizontalRefreshLoadActivity;
import com.shuyu.apprecycler.bind.activity.BindNormalActivity;
import com.shuyu.apprecycler.bind.activity.BindRefreshLoadActivity;
import com.shuyu.apprecycler.bind.activity.BindStaggeredRefreshLoadActivity;
import com.shuyu.apprecycler.bind.activity.BindGridActivity;
import com.shuyu.apprecycler.bind.utils.BindDataUtils;

/**
 * 主页
 * Created by guoshuyu on 2017/8/29.
 */

public class BindHomeActivity extends AppCompatActivity implements ListView.OnItemClickListener {


    ListView homeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        homeList = (ListView) findViewById(R.id.home_list);

        homeList.setOnItemClickListener(this);

        ListAdapter listAdapter = new ArrayAdapter<>(this, R.layout.home_item, BindDataUtils.getHomeList());

        homeList.setAdapter(listAdapter);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0: {
                Intent intent = new Intent(BindHomeActivity.this, BindNormalActivity.class);
                startActivity(intent);
                break;
            }
            case 1: {
                Intent intent = new Intent(BindHomeActivity.this, BindRefreshLoadActivity.class);
                startActivity(intent);
                break;
            }
            case 2: {
                Intent intent = new Intent(BindHomeActivity.this, BindStaggeredRefreshLoadActivity.class);
                startActivity(intent);
                break;
            }
            case 3: {
                Intent intent = new Intent(BindHomeActivity.this, BindEmptyActivity.class);
                startActivity(intent);
                break;
            }
            case 4: {
                Intent intent = new Intent(BindHomeActivity.this, BindGridActivity.class);
                startActivity(intent);
                break;
            }
            case 5: {
                Intent intent = new Intent(BindHomeActivity.this, BindHorizontalRefreshLoadActivity.class);
                startActivity(intent);
                break;
            }
        }
    }
}
