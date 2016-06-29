package com.deepblue.recyclerviewtestdemo;

/**
 * Created by gerry on 2016/3/30.
 * 自定义加载更多的view
 */
public interface IloadMoreView extends IBaseView {

    void onLoading();

    void onFinish();

    void onBindView(boolean isEnd);
}
