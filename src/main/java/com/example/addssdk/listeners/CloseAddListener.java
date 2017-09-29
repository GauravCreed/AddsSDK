package com.example.addssdk.listeners;

import android.content.Context;

import com.example.addssdk.common.Utils;
import com.example.addssdk.interfaces.CloseAddsInterface;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pc5 on 9/23/2017.
 */

public class CloseAddListener {
    CloseAddsInterface champingInterface;

    public void sendCloseAddResponse(String response, Context mContext) {
        champingInterface = (CloseAddsInterface) mContext;

        if (!response.matches("") && !response.contains("error")) {
            try {
                JSONObject object = new JSONObject(response);
                JSONObject dataobject = object.getJSONObject("data");

                Utils.champignBean.id = dataobject.getString("id");
                Utils.champignBean.brand_id = dataobject.getString("brand_id");
                Utils.champignBean.title = dataobject.getString("title");
                Utils.champignBean.description = dataobject.getString("description");
                Utils.champignBean.start_date = dataobject.getString("start_date");
                Utils.champignBean.end_date = dataobject.getString("end_date");
                Utils.champignBean.budget_amount = dataobject.getString("budget_amount");
                Utils.champignBean.cpe = dataobject.getString("cpe");
                Utils.champignBean.status = dataobject.getString("status");
                Utils.champignBean.coupon_type = dataobject.getString("coupon_type");
                Utils.champignBean.coupon_file = dataobject.getString("coupon_file");
                Utils.champignBean.age_group = dataobject.getString("age_group");
                Utils.champignBean.image_file = dataobject.getString("image_file");
                Utils.champignBean.radius = dataobject.getString("radius");
                Utils.champignBean.created_at = dataobject.getString("created_at");
                Utils.champignBean.updated_at = dataobject.getString("updated_at");

                if (!Utils.champignBean.image_file.matches("")) {
                    Utils.champignBean.image_file = Utils.champignBean.image_file.replace("\\", "");
                    Utils.champignBean.image_file = Utils.champignBean.image_file.replace(" ", "%20");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            champingInterface.getCloseAppResponse(response);
        } else {
            champingInterface.getCloseAppResponse(response);
        }


    }
}
