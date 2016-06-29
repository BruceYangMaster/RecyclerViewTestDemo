package com.deepblue.recyclerviewtestdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private View headView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWidget();
    }

    private void initWidget() {
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));//listview样式
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));//gridview样式 两行

//        View view = LayoutInflater.from(this).inflate(R.layout.tv, null);
//        mRecyclerView.addView(view);
        //
        ArrayList<String> arrayList = getData();
        //
        MyAdapter myAdapter = new MyAdapter(this, arrayList);
        //添加头headview
        final RecyclerViewAdapterPaker recyclerViewAdapterPaker = new RecyclerViewAdapterPaker(myAdapter, MainActivity.this);
        recyclerViewAdapterPaker.setLoadMore(new RecyclerViewAdapterPaker.ILoadMore() {
            @Override
            public void loadMore() {
//                initData();
                Toast.makeText(MainActivity.this, "loadMore", Toast.LENGTH_LONG).show();
                recyclerViewAdapterPaker.setLoadMoreComplete();
//                recyclerViewAdapterPaker.setIsEnd(true);//no_more
            }
        });
        recyclerViewAdapterPaker.setHead(new IBaseView() {
            @Override
            public void onBindView() {

            }

            @Override
            public View onCreateView(ViewGroup parent) {
                headView = LayoutInflater.from(parent.getContext()).inflate(R.layout.head_test, parent, false);
                headView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, "头", Toast.LENGTH_LONG).show();
                    }
                });
                return headView;
            }
        });
        //
        mRecyclerView.setAdapter(myAdapter);
        mRecyclerView.setAdapter(recyclerViewAdapterPaker);
    }

    private ArrayList<String> getData() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("环境监控的沙发了客家话");
        arrayList.add("没的说了奶粉了看见审单发货看电视剧");
        arrayList.add("了肯德基风格算了可费劲了");
        arrayList.add("是丹佛文件分类看身份你们了深刻的");
        arrayList.add("jflkjsldkfj");
        return arrayList;
    }
}
