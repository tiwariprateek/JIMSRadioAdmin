package com.dark.pro.jimsradioadmin;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FragmentRating extends Fragment {
    RequestQueue requestQueue;
    ProgressBar progressBar;
    LinearLayout rating_layout;
    TextView rating_detail;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_rating, container, false);
        progressBar=(ProgressBar)v.findViewById(R.id.progbar_rating);
        rating_layout=(LinearLayout)v.findViewById(R.id.layout_rating);
        rating_layout.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        rating_detail=(TextView)v.findViewById(R.id.txt_ratings);
        loadRatings();
        return v;
    }

    private void loadRatings() {
        String url="https://jimsd.org/jimsradio/fetch_rating.php?";
        requestQueue= Volley.newRequestQueue(getActivity());
        StringRequest request =new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONArray json =new JSONArray(response);
                            JSONObject obj;
                            int size=json.length();
                            int r1,r2,r3,r4,r5;
                            r1=r2=r3=r4=r5=0;
                            int item;
                            float total=0;
                            for(int i=0;i<size;i++){
                                //adding to drawer code
                                obj=json.getJSONObject(i);
                                item=obj.getInt("rating_value");
                                switch(item){
                                    case 1:
                                        r1++;
                                        total+=1;
                                        break;
                                    case 2:
                                        r2++;
                                        total+=2;
                                        break;
                                    case 3:
                                        r3++;
                                        total+=3;
                                        break;
                                    case 4:
                                        r4++;
                                        total+=4;
                                        break;
                                    case 5:
                                        r5++;
                                        total+=5;
                                        break;
                                }
                            }
                            total/=size;

                            String data="Out of "+size+" ratings...\n"+
                                    "\n1 Star: "+r1+"\n2 Stars: "+r2+"\n3 Stars: "+r3+
                                    "\n4 Stars: "+r4+"\n5 Stars: "+r5+
                                    "\n\nAverage rating: "+total;

                            rating_detail.setText(data);

                            progressBar.setVisibility(View.GONE);
                            rating_layout.setVisibility(View.VISIBLE);
                        }catch(JSONException e) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getActivity(),"No records present..",Toast.LENGTH_SHORT).show();
                        }catch(Exception e){
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getActivity(),"An Error has Occurred"+e,Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        //Toast.makeText(getActivity(),"Cannot connect to the server, check your Internet conection and try again...",Toast.LENGTH_SHORT).show();
                        Toast.makeText(getActivity(),""+error,Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param=new HashMap<>();
                //param.put("code",code);
                return param;
            }
        };
        requestQueue.add(request);
    }
}
