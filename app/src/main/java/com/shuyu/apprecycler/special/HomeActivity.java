package com.shuyu.apprecycler.special;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.shuyu.apprecycler.R;
import com.shuyu.apprecycler.special.activity.CustomXRecyclerRefreshActivity;
import com.shuyu.apprecycler.special.activity.GridSystemRefreshActivity;
import com.shuyu.apprecycler.special.activity.GridXRecyclerActivity;
import com.shuyu.apprecycler.special.activity.NormalActivity;
import com.shuyu.apprecycler.special.activity.NormalEmptyActivity;
import com.shuyu.apprecycler.special.activity.NormalXRecyclerActivity;
import com.shuyu.apprecycler.special.activity.RefreshXRecyclerActivity;
import com.shuyu.apprecycler.special.activity.StaggeredSystemRefreshActivity;
import com.shuyu.apprecycler.special.activity.StaggeredXRecyclerActivity;
import com.shuyu.apprecycler.special.activity.SystemRefreshActivity;
import com.shuyu.apprecycler.special.activity.ViewPagerActivity;
import com.shuyu.apprecycler.special.activity.XRecyclerEmptyActivity;
import com.shuyu.apprecycler.special.utils.DataUtils;

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
            case 2: {
                Intent intent = new Intent(HomeActivity.this, StaggeredSystemRefreshActivity.class);
                startActivity(intent);
                break;
            }
            case 3: {
                Intent intent = new Intent(HomeActivity.this, NormalXRecyclerActivity.class);
                startActivity(intent);
                break;
            }
            case 4: {
                Intent intent = new Intent(HomeActivity.this, RefreshXRecyclerActivity.class);
                startActivity(intent);
                break;
            }
            case 5: {
                Intent intent = new Intent(HomeActivity.this, StaggeredXRecyclerActivity.class);
                startActivity(intent);
                break;
            }
            case 6: {
                Intent intent = new Intent(HomeActivity.this, CustomXRecyclerRefreshActivity.class);
                startActivity(intent);
                break;
            }
            case 7: {
                Intent intent = new Intent(HomeActivity.this, ViewPagerActivity.class);
                startActivity(intent);
                break;
            }
            case 8: {
                Intent intent = new Intent(HomeActivity.this, NormalEmptyActivity.class);
                startActivity(intent);
                break;
            }
            case 9: {
                Intent intent = new Intent(HomeActivity.this, XRecyclerEmptyActivity.class);
                startActivity(intent);
                break;
            }
            case 10: {
                Intent intent = new Intent(HomeActivity.this, GridXRecyclerActivity.class);
                startActivity(intent);
                break;
            }
            case 11: {
                Intent intent = new Intent(HomeActivity.this, GridSystemRefreshActivity.class);
                startActivity(intent);
                break;
            }
        }
    }
}
