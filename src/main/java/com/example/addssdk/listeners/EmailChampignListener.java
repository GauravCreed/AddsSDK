package com.example.addssdk.listeners;

import android.content.Context;
import android.widget.Toast;

import com.example.addssdk.R;
import com.example.addssdk.common.Utils;
import com.example.addssdk.interfaces.ChampingInterface;
import com.example.addssdk.interfaces.EmailInterface;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pc5 on 9/9/2017.
 */

public class EmailChampignListener {
    EmailInterface champingInterface;

    public void sendEmailChampignResponse(String response, Context mContext) {
        champingInterface = (EmailInterface) mContext;
        if (!response.matches("") && !response.contains("error")) {
            try {
                JSONObject object = new JSONObject(response);
                JSONObject dataobject = object.getJSONObject("data");

                Utils.emailChampignBean.id = dataobject.getString("id");
                Utils.emailChampignBean.brand_id = dataobject.getString("brand_id");
                Utils.emailChampignBean.title = dataobject.getString("title");
                Utils.emailChampignBean.description = dataobject.getString("description");
                Utils.emailChampignBean.start_date = dataobject.getString("start_date");
                Utils.emailChampignBean.end_date = dataobject.getString("end_date");
                Utils.emailChampignBean.budget_amount = dataobject.getString("budget_amount");
                Utils.emailChampignBean.cpe = dataobject.getString("cpe");
                Utils.emailChampignBean.status = dataobject.getString("status");
                Utils.emailChampignBean.coupon_type = dataobject.getString("coupon_type");
                Utils.emailChampignBean.coupon_file = dataobject.getString("coupon_file");
                Utils.emailChampignBean.age_group = dataobject.getString("age_group");
                Utils.emailChampignBean.image_file = dataobject.getString("image_file");
                Utils.emailChampignBean.radius = dataobject.getString("radius");
                Utils.emailChampignBean.created_at = dataobject.getString("created_at");
                Utils.emailChampignBean.updated_at = dataobject.getString("updated_at");

                if (!Utils.emailChampignBean.image_file.matches("")) {
                    Utils.emailChampignBean.image_file = Utils.emailChampignBean.image_file.replace("\\", "");
                    Utils.emailChampignBean.image_file = Utils.emailChampignBean.image_file.replace(" ", "%20");
                }

                Toast.makeText(mContext, mContext.getResources().getString(R.string.sent_coupon_msg), Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            champingInterface.getEmailChampignRespons(response);
        } else {
            champingInterface.getEmailChampignRespons(response);
        }


    }
}
