package com.example.addssdk;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.addssdk.common.CustomRequest;
import com.example.addssdk.common.Utils;
import com.example.addssdk.listeners.AddListener;
import com.example.addssdk.listeners.ChampignListener;
import com.example.addssdk.listeners.CloseAddListener;
import com.example.addssdk.listeners.EmailChampignListener;
import com.example.addssdk.listeners.ErrorddListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


/**
 * Created by pc5 on 9/9/2017.
 */

public class ApiCallAdds {

    Context mContext;
    String appId, countryName, deviceId, deviceName, deviceOs, deviceVersion;

    public ApiCallAdds(Context context) {
        mContext = context;

        TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        Locale loc = new Locale("", tm.getSimCountryIso());

        appId = mContext.getResources().getString(R.string.appId_value);
        countryName = loc.getDisplayCountry();
        deviceId = Utils.getDeviceId(mContext);
        deviceName = Utils.getDeviceName();
        deviceOs = Build.VERSION.RELEASE;
        deviceVersion = (Build.VERSION.SDK_INT) + "";


        if (Utils.checkInternetConnection(context)) {
            callInitApp();
        } else {
            getErrorListener(context.getResources().getString(R.string.no_connection_err));
        }

    }

    private void getErrorListener(String errorMsg) {
        ErrorddListener listener = new ErrorddListener();
        listener.sendError(errorMsg, mContext);
    }

    public void callInitApp() {
        // TODO Auto-generated method stub


        String URL = mContext.getResources().getString(R.string.init_url);

       /* System.out.println("=== appId : " + appId);
        System.out.println("=== countryName : " + countryName);
        System.out.println("=== deviceId : " + deviceId);
        System.out.println("=== deviceName : " + deviceName);
        System.out.println("=== deviceOs : " + deviceOs);
        System.out.println("=== deviceVersion : " + deviceVersion);*/


        Map<String, String> params = new HashMap<String, String>();
        params.put(mContext.getResources().getString(R.string.appId), appId);
        params.put(mContext.getResources().getString(R.string.device_id), deviceId);
        params.put(mContext.getResources().getString(R.string.device_model), deviceName);
        params.put(mContext.getResources().getString(R.string.os), deviceOs);
        params.put(mContext.getResources().getString(R.string.version), deviceVersion);
        params.put(mContext.getResources().getString(R.string.country), countryName);

        RequestQueue queue = Volley.newRequestQueue(mContext);
        CustomRequest jsObjRequest = new CustomRequest(Request.Method.POST,
                URL, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.toString() != null) {
                    getResponse(response.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        queue.add(jsObjRequest);
    }


    private void getResponse(String response) {
        AddListener listener = new AddListener();
        listener.sendResponse(response, mContext);
    }

    public void loadData() {
        if (Utils.initBean.isInitialized) {
            if (Utils.checkInternetConnection(mContext)) {
                callChampignApi();
            } else {
                getErrorListener(mContext.getResources().getString(R.string.no_connection_err));
            }
        } else {
            getErrorListener(mContext.getResources().getString(R.string.app_not_initialize));
        }
    }

    private void callChampignApi() {

        String URL = mContext.getResources().getString(R.string.champign_url);

        Map<String, String> params = new HashMap<String, String>();
        params.put(mContext.getResources().getString(R.string.latitude), 28.7041 + "");
        params.put(mContext.getResources().getString(R.string.longitude), 77.1025 + "");
        params.put(mContext.getResources().getString(R.string.radius), "1");

        RequestQueue queue = Volley.newRequestQueue(mContext);
        CustomRequest jsObjRequest = new CustomRequest(Request.Method.POST,
                URL, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.toString() != null) {
                    getChampignResponse(response.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                getErrorListener(error.getMessage());
            }
        });

        queue.add(jsObjRequest);
    }

    private void getChampignResponse(String response) {
        ChampignListener listener = new ChampignListener();
        listener.sendChampignResponse(response, mContext);
    }

    public void showAdd() {
        final Dialog dialog = new Dialog(mContext);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.test);

        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        final LinearLayout layoutMain = dialog.findViewById(R.id.layoutMain);
        final EditText edtEmailAddress = dialog.findViewById(R.id.edtEmailAddress);
        final ProgressBar imgProgress = dialog.findViewById(R.id.imgProgress);
        final ImageView imgAdds = dialog.findViewById(R.id.imgAdds);
        final ImageView imgClose = dialog.findViewById(R.id.imgClose);
        final RelativeLayout layoutEmailAdd = dialog.findViewById(R.id.layoutEmailAdd);
        final TextView btnSend = dialog.findViewById(R.id.btnSend);
        final LinearLayout layoutEnterMail = dialog.findViewById(R.id.layoutEnterMail);
        TextView txtTitle = dialog.findViewById(R.id.txtTitle);
        TextView txtDescription = dialog.findViewById(R.id.txtDescription);

        imgProgress.setVisibility(View.VISIBLE);

        if (Utils.champignBean.image_file != null && Utils.champignBean.image_file.length() > 0) {

            txtTitle.setText(Utils.champignBean.title);
            txtDescription.setText(Utils.champignBean.description);

            Picasso.with(mContext).load(Utils.champignBean.image_file).into(imgAdds, new Callback() {
                @Override
                public void onSuccess() {
                    imgProgress.setVisibility(View.GONE);
                    imgAdds.setVisibility(View.VISIBLE);
                    dialog.show();
                }

                @Override
                public void onError() {

                }
            });
        }


        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                YoYo.with(Techniques.RubberBand)
                        .duration(400)
                        .repeat(1)
                        .withListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animator) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animator) {
                                dialog.dismiss();
                                if (Utils.checkInternetConnection(mContext)) {
                                    callCloseAddApi(Utils.champignBean.id);
                                } else {
                                    getErrorListener(mContext.getResources().getString(R.string.no_connection_err));
                                }
                            }

                            @Override
                            public void onAnimationCancel(Animator animator) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animator) {

                            }
                        })
                        .playOn(imgClose);
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                YoYo.with(Techniques.Shake)
                        .duration(400)
                        .repeat(1)
                        .withListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animator) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animator) {
                                String strEmail = edtEmailAddress.getText().toString();
                                if (!strEmail.matches("")) {

                                    if (Utils.isValidEmail(strEmail)) {
                                        dialog.dismiss();

                                        if (Utils.checkInternetConnection(mContext)) {
                                            callEmailApi(strEmail);
                                        } else {
                                            getErrorListener(mContext.getResources().getString(R.string.no_connection_err));
                                        }

                                    } else {
                                        Toast.makeText(mContext, mContext.getResources().getString(R.string.err_msg_email), Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(mContext, mContext.getResources().getString(R.string.err_msg), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onAnimationCancel(Animator animator) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animator) {

                            }
                        })
                        .playOn(view);


            }
        });

        layoutEnterMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutEmailAdd.setVisibility(View.VISIBLE);
                layoutEnterMail.setVisibility(View.GONE);
            }
        });
        layoutEmailAdd.setVisibility(View.GONE);

    }


    private void callCloseAddApi(String id) {
        String URL = mContext.getResources().getString(R.string.close_adds_url);

       /* System.out.println("=== appId : " + appId);
        System.out.println("=== countryName : " + countryName);
        System.out.println("=== deviceId : " + deviceId);
        System.out.println("=== deviceName : " + deviceName);
        System.out.println("=== deviceOs : " + deviceOs);
        System.out.println("=== deviceVersion : " + deviceVersion);*/

        Map<String, String> params = new HashMap<String, String>();
        params.put(mContext.getResources().getString(R.string.campaignId), id);
        params.put(mContext.getResources().getString(R.string.device_id), deviceId);
        params.put(mContext.getResources().getString(R.string.device_model), deviceName);
        params.put(mContext.getResources().getString(R.string.os), deviceOs);
        params.put(mContext.getResources().getString(R.string.version), deviceVersion);
        params.put(mContext.getResources().getString(R.string.country), countryName);

        RequestQueue queue = Volley.newRequestQueue(mContext);
        CustomRequest jsObjRequest = new CustomRequest(Request.Method.POST,
                URL, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.toString() != null) {
                    getCloseAddResponse(response.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        queue.add(jsObjRequest);
    }

    private void getCloseAddResponse(String response) {
        CloseAddListener listener = new CloseAddListener();
        listener.sendCloseAddResponse(response, mContext);
    }

    private void callEmailApi(String strEmail) {
        String URL = mContext.getResources().getString(R.string.email_champign_url);
        Map<String, String> params = new HashMap<String, String>();
        params.put(mContext.getResources().getString(R.string.email), strEmail);
        params.put(mContext.getResources().getString(R.string.campaignId), Utils.champignBean.id);

        RequestQueue queue = Volley.newRequestQueue(mContext);
        CustomRequest jsObjRequest = new CustomRequest(Request.Method.POST,
                URL, params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.toString() != null) {
                    getEmailChampignResponse(response.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                getErrorListener(error.getMessage());
            }
        });

        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(jsObjRequest);
    }

    private void getEmailChampignResponse(String strMessage) {
        EmailChampignListener listener = new EmailChampignListener();
        listener.sendEmailChampignResponse(strMessage, mContext);
    }
}
