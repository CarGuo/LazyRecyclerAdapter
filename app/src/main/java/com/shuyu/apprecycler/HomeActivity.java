package com.shuyu.apprecycler;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.shuyu.apprecycler.activity.NormalActivity;
import com.shuyu.apprecycler.activity.SystemRefreshActivity;
import com.shuyu.apprecycler.utils.DataUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by guoshuyu on 2017/1/7.
 */

public class HomeActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.home_list)
    ListView homeList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        homeList.setOnItemClickListener(this);

        ListAdapter listAdapter = new ArrayAdapter<>(this, R.layout.home_item, DataUtils.getHomeList());

        homeList.setAdapter(listAdapter);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0: {
                Intent intent = new Intent(HomeActivity.this, NormalActivity.class);
                startActivity(intent);
                break;
            }
            case 1: {
                Intent intent = new Intent(HomeActivity.this, SystemRefreshActivity.class);
                startActivity(intent);
                break;
            }
        }
    }
}
