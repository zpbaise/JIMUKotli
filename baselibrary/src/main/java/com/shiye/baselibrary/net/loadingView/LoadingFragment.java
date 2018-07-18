package com.shiye.baselibrary.net.loadingView;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shiye.baselibrary.R;


/**
 * Created by issuser on 2018/7/9.
 * 网络请求的Loading动画
 */

public class LoadingFragment extends DialogFragment {
    private View mLoadingView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, R.style.Loading_Dialog);
    }

    @Override
    public void onStart() {
        super.onStart();
        //设置宽度顶满屏幕,无左右留白
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout(dm.widthPixels, getDialog().getWindow().getAttributes().height);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (mLoadingView == null){
            mLoadingView = inflater.inflate(R.layout.loading_dialog, container, false);
        }
        return mLoadingView;
    }

    public void setLoadingView(View loadingView){
        mLoadingView = loadingView;
    }
}
