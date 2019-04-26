package com.steven.module.androidcomponentdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.steven.module.androidcomponentdemo.network.HQNetworking;
import com.steven.module.androidcomponentdemo.network.example.MessageService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * 请求示例
     */
    private void getMessage() {
        MessageService.getMessage(1, 10, new HQNetworking.ResultCallback<MessageService.MessageBean>() {
            @Override
            public void onResult(MessageService.MessageBean messageBean) {

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }
}
