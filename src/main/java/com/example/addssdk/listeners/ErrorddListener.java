package com.example.addssdk.listeners;

import android.content.Context;

import com.example.addssdk.interfaces.ErrorInterface;
import com.example.addssdk.interfaces.ResponseInterface;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pc5 on 9/9/2017.
 */

public class ErrorddListener {

    ErrorInterface responseInterface;

    public void sendError(String response, Context mContext) {
        responseInterface = (ErrorInterface) mContext;

        if (response != null) {
            responseInterface.getErrorMessage(response);
        } else {
            responseInterface.getErrorMessage("Sorry.....");
        }

    }

}
