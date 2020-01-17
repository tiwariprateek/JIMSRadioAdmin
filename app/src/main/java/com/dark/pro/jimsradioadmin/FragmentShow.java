package com.dark.pro.jimsradioadmin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FragmentShow extends Fragment implements View.OnClickListener{

    RequestQueue requestQueue;
    Button btn_add_show_layout,btn_edit_show_layout,btn_remove_show_layout;
    Button btn_add_show,btn_edit_show,btn_remove_show;
    LinearLayout layout_add_show,layout_edit_show,layout_remove_show;
    EditText edt_add_show_name,edt_add_show_desc,edt_add_show_code,
            edt_edit_show_name,edt_edit_show_desc,edt_edit_show_code;
    TextView txt_remove_show_name,txt_remove_show_code,txt_remove_show_desc;
    String arr_show[][];
    String show_code_old,show_name_old,show_desc_old,show_code_del,show_name_del;
    int flag_active_option=0;
    //adapter items
    ArrayList array_edit_list;
    ArrayAdapter array_edit_adapter;
    Spinner spinner_modify_show_select,spinner_remove_show_select;
    ProgressBar show_progbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_show, container, false);

        show_progbar=(ProgressBar)v.findViewById(R.id.progbar_show_options);
        show_progbar.setVisibility(View.GONE);
        //Layout buttons
        btn_add_show_layout=(Button)v.findViewById(R.id.btn_set_add_show);
        btn_add_show_layout.setOnClickListener(this);
        btn_edit_show_layout=(Button)v.findViewById(R.id.btn_set_edit_show);
        btn_edit_show_layout.setOnClickListener(this);
        btn_remove_show_layout=(Button)v.findViewById(R.id.btn_set_remove_show);
        btn_remove_show_layout.setOnClickListener(this);
        btn_add_show_layout.setBackgroundResource(R.drawable.button_background);
        btn_edit_show_layout.setBackgroundResource(R.drawable.button_background);
        btn_remove_show_layout.setBackgroundResource(R.drawable.button_background);

        //Action buttons for queries
        btn_add_show=(Button)v.findViewById(R.id.btn_add_show);
        btn_add_show.setOnClickListener(this);
        btn_edit_show=(Button)v.findViewById(R.id.btn_edit_show);
        btn_edit_show.setOnClickListener(this);
        btn_remove_show=(Button)v.findViewById(R.id.btn_remove_show);
        btn_remove_show.setOnClickListener(this);

        //adapter for fetching shows
        array_edit_list=new ArrayList();
        array_edit_adapter=new ArrayAdapter(getActivity(),
                android.R.layout.simple_dropdown_item_1line,array_edit_list);

        //layouts for each show operation
        layout_add_show=(LinearLayout)v.findViewById(R.id.layout_add_show);
        layout_edit_show=(LinearLayout)v.findViewById(R.id.layout_edit_show);
        layout_remove_show=(LinearLayout)v.findViewById(R.id.layout_remove_show);

        //add show layout elements
        edt_add_show_name=(EditText)v.findViewById(R.id.edt_add_showname);
        edt_add_show_desc=(EditText)v.findViewById(R.id.edt_add_showdesc);
        edt_add_show_code=(EditText)v.findViewById(R.id.edt_add_show_code);

        //edit show layout elements
        edt_edit_show_name=(EditText)v.findViewById(R.id.edt_edit_showname);
        edt_edit_show_desc=(EditText)v.findViewById(R.id.edt_edit_showdesc);
        edt_edit_show_code=(EditText)v.findViewById(R.id.edt_edit_show_code);

        spinner_modify_show_select=(Spinner)v.findViewById(R.id.spinner_edit_show);
        spinner_modify_show_select.setAdapter(array_edit_adapter);
        spinner_modify_show_select.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                show_code_old=arr_show[position][0];
                show_name_old=arr_show[position][1];
                show_desc_old=arr_show[position][2];
                edt_edit_show_code.setText(arr_show[position][0]);
                edt_edit_show_name.setText(arr_show[position][1]);
                edt_edit_show_desc.setText(arr_show[position][2]);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //remove show layout elements
        spinner_remove_show_select=(Spinner)v.findViewById(R.id.spinner_remove_show);
        spinner_remove_show_select.setAdapter(array_edit_adapter);
        txt_remove_show_code=(TextView)v.findViewById(R.id.txt_remove_show_code);
        txt_remove_show_name=(TextView)v.findViewById(R.id.txt_remove_show_name);
        txt_remove_show_desc=(TextView)v.findViewById(R.id.txt_remove_show_desc);
        spinner_remove_show_select.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                show_code_del=arr_show[position][0];
                show_name_del=arr_show[position][1];
                txt_remove_show_code.setText("Code: "+arr_show[position][0]);
                txt_remove_show_name.setText("Name: "+arr_show[position][1]);
                txt_remove_show_desc.setText("Description: \n"+arr_show[position][2]);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_add_show_layout.setBackgroundResource(R.drawable.button_background);
        layout_add_show.setVisibility(View.VISIBLE);
        layout_edit_show.setVisibility(View.GONE);
        layout_remove_show.setVisibility(View.GONE);
        array_edit_adapter.clear();
        return v;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_set_add_show:
                btn_add_show_layout.setBackgroundResource(R.drawable.button_background);
                btn_edit_show_layout.setBackgroundResource(R.drawable.button_background);
                btn_remove_show_layout.setBackgroundResource(R.drawable.button_background);
                layout_add_show.setVisibility(View.VISIBLE);
                layout_edit_show.setVisibility(View.GONE);
                layout_remove_show.setVisibility(View.GONE);
                array_edit_adapter.clear();
                break;

            case R.id.btn_set_edit_show:
                btn_edit_show_layout.setBackgroundResource(R.drawable.button_background);
                btn_add_show_layout.setBackgroundResource(R.drawable.button_background);
                btn_remove_show_layout.setBackgroundResource(R.drawable.button_background);
                show_progbar.setVisibility(View.VISIBLE);
                layout_add_show.setVisibility(View.GONE);
                layout_edit_show.setVisibility(View.GONE);
                layout_remove_show.setVisibility(View.GONE);
                flag_active_option=1;
                array_edit_adapter.clear();
                fetchShow();
                break;

            case R.id.btn_set_remove_show:
                btn_remove_show_layout.setBackgroundResource(R.drawable.button_background);
                btn_add_show_layout.setBackgroundResource(R.drawable.button_background);
                btn_edit_show_layout.setBackgroundResource(R.drawable.button_background);
                show_progbar.setVisibility(View.VISIBLE);
                layout_add_show.setVisibility(View.GONE);
                layout_edit_show.setVisibility(View.GONE);
                layout_remove_show.setVisibility(View.GONE);
                flag_active_option=2;
                array_edit_adapter.clear();
                fetchShow();
                break;

            case R.id.btn_add_show:
                addShow();
                break;

            case R.id.btn_edit_show:
                updateShow();
                break;

            case R.id.btn_remove_show:
                new AlertDialog.Builder(getActivity())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Exit..?")
                        .setMessage("Are you sure you want to remove show '"+show_name_del+"'..?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getActivity(),"Removing show...",Toast.LENGTH_SHORT).show();
                                removeShow(show_code_del);
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
                break;
        }
    }

    @Override
    public void onDestroyView() {

        try {
            requestQueue.cancelAll(new RequestQueue.RequestFilter() {
                @Override
                public boolean apply(Request<?> request) {
                    return true;
                }
            });
        }catch(Exception e){

        }
        super.onDestroyView();
    }

    private void addShow(){
        final String show_name =edt_add_show_name.getText().toString().trim();
        final String show_desc =edt_add_show_desc.getText().toString().trim();
        final String show_code =edt_add_show_code.getText().toString().trim();

        if(show_name.equals("")||show_desc.equals("")||show_code.equals("")){
            Toast.makeText(getActivity(), "Invalid data", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getActivity(),"Adding show...",Toast.LENGTH_SHORT).show();
            String url = "https://jimsd.org/jimsradio/add_show.php?";
            requestQueue = Volley.newRequestQueue(getActivity());
            StringRequest request = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(getActivity(), ""+response, Toast.LENGTH_SHORT).show();
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.frag_main, new FragmentShow()).commit();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Toast.makeText(getActivity(), "Cannot connect to the server, check your Internet conection and try again...", Toast.LENGTH_SHORT).show();
                            Toast.makeText(getActivity(),""+error,Toast.LENGTH_SHORT).show();
                            error.printStackTrace();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> param = new HashMap<>();
                    param.put("show_name", show_name);
                    param.put("show_desc", show_desc);
                    param.put("show_code", show_code);
                    return param;
                }
            };
            requestQueue.add(request);
        }
    }

    private void fetchShow(){
        show_code_old="";
        show_name_old="";
        show_desc_old="";
        show_code_del="";
        show_name_del="";
        String url="https://jimsd.org/jimsradio/fetch_show.php?";
        requestQueue= Volley.newRequestQueue(getActivity());
        StringRequest request =new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONArray json =new JSONArray(response);
                            JSONObject obj;
                            int size=json.length();
                            array_edit_adapter.clear();
                            arr_show=new String[size][3];
                            for(int i=0;i<json.length();i++){
                                //adding to drawer code
                                obj=json.getJSONObject(i);
                                arr_show[i][0]=obj.getString("show_code");
                                arr_show[i][1]=obj.getString("show_name");
                                arr_show[i][2]=obj.getString("description");
                                array_edit_list.add( arr_show[i][1] );
                                array_edit_adapter.notifyDataSetChanged();
                            }
                            show_progbar.setVisibility(View.GONE);
                            if(flag_active_option==1){
                                layout_edit_show.setVisibility(View.VISIBLE);
                            }else if(flag_active_option==2){
                                layout_remove_show.setVisibility(View.VISIBLE);
                            }
                            else{
                                Toast.makeText(getActivity(),"Active view error...",Toast.LENGTH_SHORT).show();
                            }
                        }catch(Exception e){
                            Toast.makeText(getActivity(),"An Error has Occurred"+e,Toast.LENGTH_SHORT).show();
                            show_progbar.setVisibility(View.GONE);
                        }
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
                //param.put("code",code);
                return param;
            }
        };
        requestQueue.add(request);
    }

    private void updateShow() {
        final String show_name =edt_edit_show_name.getText().toString().trim();
        final String show_desc =edt_edit_show_desc.getText().toString().trim();
        final String show_code =edt_edit_show_code.getText().toString().trim();

        if(show_name.equals("")||show_desc.equals("")||show_code.equals("")){
            Toast.makeText(getActivity(), "Invalid data", Toast.LENGTH_SHORT).show();
        }else if(show_code.equals(show_code_old)&&show_name.equals(show_name_old)&&show_desc.equals(show_desc_old)){
            Toast.makeText(getActivity(), "No changes detected..", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getActivity(),"Modifying show...",Toast.LENGTH_SHORT).show();
            String url = "https://jimsd.org/jimsradio/edit_show.php?";
            requestQueue = Volley.newRequestQueue(getActivity());
            StringRequest request = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(getActivity(), ""+response, Toast.LENGTH_SHORT).show();
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.frag_main, new FragmentShow()).commit();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getActivity(), "Cannot connect to the server, check your Internet conection and try again...", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> param = new HashMap<>();
                    param.put("show_code_old",show_code_old);
                    param.put("show_name", show_name);
                    param.put("show_desc", show_desc);
                    param.put("show_code", show_code);
                    return param;
                }
            };
            requestQueue.add(request);
       }
    }

    private void removeShow(final String code){
        String url="https://jimsd.org/jimsradio/remove_show.php?";
        requestQueue= Volley.newRequestQueue(getActivity());
        StringRequest request =new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getActivity(), ""+response, Toast.LENGTH_SHORT).show();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frag_main, new FragmentShow()).commit();
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
                param.put("show_code",show_code_del);
                return param;
            }
        };
        requestQueue.add(request);
    }

}
