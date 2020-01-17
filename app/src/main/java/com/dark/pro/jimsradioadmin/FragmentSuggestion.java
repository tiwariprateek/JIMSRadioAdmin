package com.dark.pro.jimsradioadmin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FragmentSuggestion extends Fragment {

    RequestQueue requestQueue;
    ProgressBar progressBar;
    ListView listView;
    ArrayList<HashMap<String,String>> suggList;
    HashMap<String,String> item;
    SimpleAdapter simpleAdapter;
    String arr_suggest[][];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_suggestion, container, false);

        progressBar=(ProgressBar)v.findViewById(R.id.progbar_suggest_select);

        suggList=new ArrayList();
        simpleAdapter = new SimpleAdapter(getActivity(), suggList,
                R.layout.layout_show,
                new String[] { "line1","line2" },
                new int[] {R.id.list_txt_show_name, R.id.list_txt_show_description});

        listView=(ListView)v.findViewById(R.id.list_suggestion);
        listView.setAdapter(simpleAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(getActivity())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Suggestion from "+arr_suggest[position][2])
                        .setMessage("Suggestion:\n"+arr_suggest[position][4]+
                                "\n\nEmail:\n"+arr_suggest[position][1]+
                                "\n\nContact:\n"+arr_suggest[position][3])
                        .setNeutralButton("OK",null)
                        .setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getActivity(), "Removing", Toast.LENGTH_SHORT).show();
                                removeSuggestion(arr_suggest[position][0]);
                            }

                        })
                        .show();
            }
        });


        loadSuggestion();
        return v;
    }

    private void loadSuggestion() {
        String url="https://jimsd.org/jimsradio/fetch_suggestion.php?";
        requestQueue= Volley.newRequestQueue(getActivity());
        StringRequest request =new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONArray json =new JSONArray(response);
                            JSONObject obj;
                            int size=json.length();
                            arr_suggest=new String[size][5];
                            for(int i=0;i<json.length();i++){
                                //adding to drawer code
                                obj=json.getJSONObject(i);
                                arr_suggest[i][0]=obj.getString("suggestion_id");
                                arr_suggest[i][1]=obj.getString("user_mail");
                                arr_suggest[i][2]=obj.getString("user_name");
                                arr_suggest[i][3]=obj.getString("user_contact");
                                arr_suggest[i][4]=obj.getString("suggestion_txt");
                                item = new HashMap<String,String>();
                                item.put( "line1", arr_suggest[i][4]);
                                item.put( "line2", arr_suggest[i][2]);
                                suggList.add(item);
                                simpleAdapter.notifyDataSetChanged();
                            }
                            progressBar.setVisibility(View.GONE);
                            listView.setVisibility(View.VISIBLE);
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

    private void removeSuggestion(final String s_id) {
        String url="https://jimsd.org/jimsradio/remove_suggestion.php?";
        requestQueue= Volley.newRequestQueue(getActivity());
        StringRequest request =new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getActivity(), ""+response, Toast.LENGTH_SHORT).show();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frag_main, new FragmentSuggestion()).commit();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(getActivity(),"Cannot connect to the server, check your Internet conection and try again...",Toast.LENGTH_SHORT).show();
                        Toast.makeText(getActivity(),""+error,Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param=new HashMap<>();
                param.put("sugg_code",s_id);
                return param;
            }
        };
        requestQueue.add(request);
    }

}
