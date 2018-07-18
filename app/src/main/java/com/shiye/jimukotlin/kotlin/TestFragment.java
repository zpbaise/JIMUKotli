package com.shiye.jimukotlin.kotlin;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shiye.baselibrary.net.ApiServiceFactory;
import com.shiye.baselibrary.net.callback.CallBack;
import com.shiye.baselibrary.net.data.BaseResponse;
import com.shiye.baselibrary.net.exception.ResponseException;
import com.shiye.jimukotlin.R;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import io.reactivex.disposables.Disposable;

import static com.shiye.baselibrary.net.utils.ResponseParseUtils.parseResponseConstrOuter;

/**
 * Created by issuser on 2018/7/18.
 */
public class TestFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("type","1");
        MainActivity.APIService apiService = ApiServiceFactory.INSTANCE.defaultCreateApiService(getContext(), "http://www.meandy.com/", hashMap,
                MainActivity.APIService.class);
        parseResponseConstrOuter(
                getContext(),
                apiService.getObject(),
                AutoDispose.<BaseResponse<ObjTestData>>autoDisposable(AndroidLifecycleScopeProvider.from(this)),
                new CallBack<BaseResponse<ObjTestData>>() {
                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onFailed(@NotNull ResponseException e) {
                        Log.e("AAA", "onFailed: " + e.getMessage());
                    }

                    @Override
                    public void onSucceed(BaseResponse<ObjTestData> result) {
                        Log.e("AAA", "onFailed: " + result.toString());
                    }

                    @Override
                    public void onBefore(@NotNull Disposable disposable) {

                    }
                });
        super.onViewCreated(view, savedInstanceState);
    }
}
