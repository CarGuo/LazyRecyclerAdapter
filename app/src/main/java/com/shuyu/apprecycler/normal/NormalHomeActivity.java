package com.shuyu.apprecycler.normal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.shuyu.apprecycler.R;
import com.shuyu.apprecycler.normal.activity.CustomXRecyclerRefreshActivity;
import com.shuyu.apprecycler.normal.activity.GridXRecyclerActivity;
import com.shuyu.apprecycler.normal.activity.NormalActivity;
import com.shuyu.apprecycler.normal.activity.NormalEmptyActivity;
import com.shuyu.apprecycler.normal.activity.NormalSystemRefreshActivity;
import com.shuyu.apprecycler.normal.activity.NormalXRecyclerActivity;
import com.shuyu.apprecycler.normal.activity.RefreshXRecyclerActivity;
import com.shuyu.apprecycler.normal.activity.StaggeredSystemRefreshActivity;
import com.shuyu.apprecycler.normal.activity.StaggeredXRecyclerActivity;
import com.shuyu.apprecycler.normal.activity.SystemRefreshActivity;
import com.shuyu.apprecycler.normal.activity.XRecyclerEmptyActivity;
import com.shuyu.apprecycler.normal.utils.DataUtils;

import butterknife.ButterKnife;

/**
 * Created by guoshuyu on 2017/8/29.
 */

public class NormalHomeActivity extends AppCompatActivity implements ListView.OnItemClickListener {


    ListView homeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        homeList = (ListView) findViewById(R.id.home_list);

        homeList.setOnItemClickListener(this);

        ListAdapter listAdapter = new ArrayAdapter<>(this, R.layout.home_item, DataUtils.getHomeList());

        homeList.setAdapter(listAdapter);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0: {
                Intent intent = new Intent(NormalHomeActivity.this, NormalActivity.class);
                startActivity(intent);
                break;
            }
            case 1: {
                Intent intent = new Intent(NormalHomeActivity.this, SystemRefreshActivity.class);
                startActivity(intent);
                break;
            }
            case 2: {
                Intent intent = new Intent(NormalHomeActivity.this, StaggeredSystemRefreshActivity.class);
                startActivity(intent);
                break;
            }
            case 3: {
                Intent intent = new Intent(NormalHomeActivity.this, NormalXRecyclerActivity.class);
                startActivity(intent);
                break;
            }
            case 4: {
                Intent intent = new Intent(NormalHomeActivity.this, RefreshXRecyclerActivity.class);
                startActivity(intent);
                break;
            }
            case 5: {
                Intent intent = new Intent(NormalHomeActivity.this, StaggeredXRecyclerActivity.class);
                startActivity(intent);
                break;
            }
            case 6: {
                Intent intent = new Intent(NormalHomeActivity.this, CustomXRecyclerRefreshActivity.class);
                startActivity(intent);
                break;
            }
            case 7: {
                Intent intent = new Intent(NormalHomeActivity.this, NormalEmptyActivity.class);
                startActivity(intent);
                break;
            }
            case 8: {
                Intent intent = new Intent(NormalHomeActivity.this, XRecyclerEmptyActivity.class);
                startActivity(intent);
                break;
            }
            case 9: {
                Intent intent = new Intent(NormalHomeActivity.this, GridXRecyclerActivity.class);
                startActivity(intent);
                break;
            }
            case 10: {
                Intent intent = new Intent(NormalHomeActivity.this, NormalSystemRefreshActivity.class);
                startActivity(intent);
                break;
            }
        }
    }
}
