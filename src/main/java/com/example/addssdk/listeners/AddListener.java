package com.example.addssdk.listeners;

import android.content.Context;

import com.example.addssdk.bean.InitBean;
import com.example.addssdk.common.Utils;
import com.example.addssdk.interfaces.ResponseInterface;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pc5 on 9/9/2017.
 */

public class AddListener {

    String strData;
    ResponseInterface responseInterface;

    public void sendResponse(String response, Context mContext) {
        strData = response;
        responseInterface = (ResponseInterface) mContext;

        if (strData != null) {
            try {

                JSONObject obj = new JSONObject(strData);
                Utils.initBean.status = obj.getString("status");
                Utils.initBean.message = obj.getString("message");
                Utils.initBean.isInitialized = obj.getBoolean("isInitialized");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            responseInterface.getApiResponse(strData);
        } else {
            responseInterface.getApiResponse("Sorry.....");
        }
    }
}
