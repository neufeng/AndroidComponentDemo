package com.steven.module.androidcomponentdemo.network.example;

import com.steven.module.androidcomponentdemo.network.BaseResult;
import com.steven.module.androidcomponentdemo.network.HQNetworking;

import java.util.HashMap;
import java.util.Map;

public class MessageService {
    private final static String BASE_URL = "http://www.baidu.com";
    private final static String MESSAGE_URL= BASE_URL + "/api/message";

    public static void getMessage(int PageNo, int PageSize, HQNetworking.ResultCallback<MessageBean> callback) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("PageNo", PageNo);
        paramMap.put("PageSize", PageSize);
        HQNetworking.post(MESSAGE_URL, paramMap, callback);
    }

    public class MessageBean extends BaseResult {
        private String code;
        private String msg;
        private String data;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }
}
