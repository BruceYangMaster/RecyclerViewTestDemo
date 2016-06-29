package com.deepblue.recyclerviewtestdemo;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by gerry on 2016/3/30.
 *
 */
public interface IBaseView {
    void onBindView();

    View onCreateView(ViewGroup parent);
}
