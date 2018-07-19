package com.shiye.baselibrary.net.loadingView;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.shiye.baselibrary.R;

/**
 * Created by issuser on 2018/7/19.
 */
public class LoadingDialog extends AlertDialog {
    private View mLoadingView;

    public LoadingDialog(Context context) {
        this(context, -1);
    }

    public LoadingDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mLoadingView != null){
            setContentView(mLoadingView);
        }else {
            setContentView(R.layout.loading_dialog);
        }
    }
    public void setLoadingView(View loadingView){
        mLoadingView = loadingView;
    }
}
