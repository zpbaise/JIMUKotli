package com.shiye.jimukotlin.kotlin;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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

    private Button mButton2;
    private MainActivity.APIService mApiService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test, container, false);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("type","1");
        mApiService = ApiServiceFactory.INSTANCE.defaultCreateApiService(getContext(), "http://www.meandy.com/", hashMap,
                MainActivity.APIService.class);
        Button button1 = view.findViewById(R.id.interface1);
        mButton2 = view.findViewById(R.id.interface2);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parseResponseConstrOuter(
                        getContext(),
                        mApiService.getObject(),
                        AutoDispose.<BaseResponse<ObjTestData>>autoDisposable(AndroidLifecycleScopeProvider.from(getLifecycle())),
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
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        super.onViewCreated(view, savedInstanceState);
        final FragmentActivity activity = getActivity();
        mButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parseResponseConstrOuter(
                        activity,
                        true,
                        mApiService.getObject(),
                        AutoDispose.<BaseResponse<ObjTestData>>autoDisposable(AndroidLifecycleScopeProvider.from(getLifecycle())),
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
            }
        });
    }
}
