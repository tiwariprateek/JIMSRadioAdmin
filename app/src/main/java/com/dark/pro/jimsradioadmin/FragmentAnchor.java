package com.dark.pro.jimsradioadmin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FragmentAnchor extends Fragment implements View.OnClickListener {
    RequestQueue requestQueue;
    ProgressBar progressBar;
    Button btnset_view_anchor,btnset_add_anchor,btnset_edit_anchor,btnset_remove_anchor;
    LinearLayout layout_view_anchor,layout_add_anchor,layout_edit_anchor,layout_remove_anchor;

    Button btn_add_anchor,btn_edit_anchor,btn_remove_anchor;
    EditText edt_add_anchor_name,edt_add_anchor_desc,
            edt_edit_anchor_name,edt_edit_anchor_desc;
    TextView txt_remove_anchor_name,txt_remove_anchor_desc;

    ArrayList array_anchor_list;
    ArrayAdapter array_anchor_adapter;
    int flag_active_option;

    ArrayList<HashMap<String,String>> anchorList;
    HashMap<String,String> anchorItem;
    SimpleAdapter anchorAdapter;

    String edit_anchor_code_choice,edit_anchor_name_old,edit_anchor_desc_old
            ,anchor_name_del,anchor_code_del;
    Spinner spinner_modify_anchor_select,spinner_remove_anchor_select;
    ListView list_anchor;

    String arr_anchor[][];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_anchor, container, false);
        progressBar=(ProgressBar)v.findViewById(R.id.progbar_anchor_options);
        //layout buttons
        btnset_view_anchor=(Button)v.findViewById(R.id.btn_set_view_anchor);
        btnset_view_anchor.setOnClickListener(this);
        btnset_add_anchor=(Button)v.findViewById(R.id.btn_set_add_anchor);
        btnset_add_anchor.setOnClickListener(this);
        btnset_edit_anchor=(Button)v.findViewById(R.id.btn_set_edit_anchor);
        btnset_edit_anchor.setOnClickListener(this);
        btnset_remove_anchor=(Button)v.findViewById(R.id.btn_set_remove_anchor);
        btnset_remove_anchor.setOnClickListener(this);
        btnset_view_anchor.setBackgroundResource(R.drawable.button_background);
        btnset_add_anchor.setBackgroundResource(R.drawable.button_background);
        btnset_edit_anchor.setBackgroundResource(R.drawable.button_background);
        btnset_remove_anchor.setBackgroundResource(R.drawable.button_background);

        //Action buttons for queries
        btn_add_anchor=(Button)v.findViewById(R.id.btn_add_anchor);
        btn_add_anchor.setOnClickListener(this);
        btn_edit_anchor=(Button)v.findViewById(R.id.btn_edit_anchor);
        btn_edit_anchor.setOnClickListener(this);
        btn_remove_anchor=(Button)v.findViewById(R.id.btn_remove_anchor);
        btn_remove_anchor.setOnClickListener(this);

        //adapter for operations
        anchorList=new ArrayList<>();
        anchorAdapter = new SimpleAdapter(getActivity(), anchorList,
                R.layout.layout_show,
                new String[] { "line1","line2" },
                new int[] {R.id.list_txt_show_name, R.id.list_txt_show_description});

        array_anchor_list=new ArrayList();
        array_anchor_adapter=new ArrayAdapter(getActivity(),
                android.R.layout.simple_dropdown_item_1line,array_anchor_list);

        //layouts for each anchor operation
        layout_view_anchor=(LinearLayout)v.findViewById(R.id.layout_view_anchor);
        layout_add_anchor=(LinearLayout)v.findViewById(R.id.layout_add_anchor);
        layout_edit_anchor=(LinearLayout)v.findViewById(R.id.layout_edit_anchor);
        layout_remove_anchor=(LinearLayout)v.findViewById(R.id.layout_remove_anchor);

        //view anchor layout elements
        list_anchor=(ListView)v.findViewById(R.id.list_anchor);
        list_anchor.setAdapter(anchorAdapter);
        list_anchor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                new AlertDialog.Builder(getActivity())
                        .setIcon(R.drawable.alert)
                        .setTitle("Anchor details")
                        .setMessage("Name:\n"+arr_anchor[position][1]+
                                "\n\nDescription:\n"+arr_anchor[position][2])
                        .setNeutralButton("OK",null)
                        .show();
            }
        });

        //add anchor layout elements
        edt_add_anchor_name=(EditText)v.findViewById(R.id.edt_add_anchorname);
        edt_add_anchor_desc=(EditText)v.findViewById(R.id.edt_add_anchordesc);

        //edit anchor layout elements
        edt_edit_anchor_name=(EditText)v.findViewById(R.id.edt_edit_anchorname);
        edt_edit_anchor_desc=(EditText)v.findViewById(R.id.edt_edit_anchordesc);

        spinner_modify_anchor_select=(Spinner)v.findViewById(R.id.sp_edit_anchor);
        spinner_modify_anchor_select.setAdapter(array_anchor_adapter);
        spinner_modify_anchor_select.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                edit_anchor_code_choice=arr_anchor[position][0];
                edit_anchor_name_old=arr_anchor[position][1];
                edit_anchor_desc_old=arr_anchor[position][2];
                edt_edit_anchor_name.setText(arr_anchor[position][1]);
                edt_edit_anchor_desc.setText(arr_anchor[position][2]);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        edit_anchor_code_choice=null;

        //remove show layout elements
        spinner_remove_anchor_select=(Spinner)v.findViewById(R.id.sp_remove_anchor);
        spinner_remove_anchor_select.setAdapter(array_anchor_adapter);
        txt_remove_anchor_name=(TextView)v.findViewById(R.id.txt_remove_anchorname);
        txt_remove_anchor_desc=(TextView)v.findViewById(R.id.txt_remove_anchordesc);
        spinner_remove_anchor_select.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                anchor_code_del=arr_anchor[position][0];
                anchor_name_del=arr_anchor[position][1];
                txt_remove_anchor_name.setText("Name: "+arr_anchor[position][1]);
                txt_remove_anchor_desc.setText("Description: \n"+arr_anchor[position][2]);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        flag_active_option=0;
        progressBar.setVisibility(View.VISIBLE);
        btnset_view_anchor.setBackgroundResource(R.drawable.button_background);
        fetchAnchor();
        return v;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_set_view_anchor:
                flag_active_option = 0;
                btnset_view_anchor.setBackgroundResource(R.drawable.activated_btn);
                btnset_add_anchor.setBackgroundResource(R.drawable.button_background);
                btnset_edit_anchor.setBackgroundResource(R.drawable.button_background);
                btnset_remove_anchor.setBackgroundResource(R.drawable.button_background);
                layout_view_anchor.setVisibility(View.GONE);
                layout_add_anchor.setVisibility(View.GONE);
                layout_edit_anchor.setVisibility(View.GONE);
                layout_remove_anchor.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                anchorList.clear();
                fetchAnchor();
                break;
            case R.id.btn_set_add_anchor:
                flag_active_option = 1;
                btnset_add_anchor.setBackgroundResource(R.drawable.activated_btn);
                btnset_view_anchor.setBackgroundResource(R.drawable.button_background);
                btnset_edit_anchor.setBackgroundResource(R.drawable.button_background);
                btnset_remove_anchor.setBackgroundResource(R.drawable.button_background);
                layout_view_anchor.setVisibility(View.GONE);
                layout_add_anchor.setVisibility(View.VISIBLE);
                layout_edit_anchor.setVisibility(View.GONE);
                layout_remove_anchor.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                break;
            case R.id.btn_set_edit_anchor:
                flag_active_option = 2;
                btnset_edit_anchor.setBackgroundResource(R.drawable.activated_btn);
                btnset_view_anchor.setBackgroundResource(R.drawable.button_background);
                btnset_add_anchor.setBackgroundResource(R.drawable.button_background);
                btnset_remove_anchor.setBackgroundResource(R.drawable.button_background);
                layout_view_anchor.setVisibility(View.GONE);
                layout_add_anchor.setVisibility(View.GONE);
                layout_edit_anchor.setVisibility(View.GONE);
                layout_remove_anchor.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                array_anchor_adapter.clear();
                fetchAnchor();
                break;
            case R.id.btn_set_remove_anchor:
                flag_active_option = 3;
                btnset_remove_anchor.setBackgroundResource(R.drawable.activated_btn);
                btnset_view_anchor.setBackgroundResource(R.drawable.button_background);
                btnset_add_anchor.setBackgroundResource(R.drawable.button_background);
                btnset_edit_anchor.setBackgroundResource(R.drawable.button_background);
                layout_view_anchor.setVisibility(View.GONE);
                layout_add_anchor.setVisibility(View.GONE);
                layout_edit_anchor.setVisibility(View.GONE);
                layout_remove_anchor.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                array_anchor_adapter.clear();
                fetchAnchor();
                break;
            case R.id.btn_add_anchor:
                addAnchor();
                break;
            case R.id.btn_edit_anchor:
                updateAnchor();
                break;
            case R.id.btn_remove_anchor:
                new AlertDialog.Builder(getActivity())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Exit..?")
                        .setMessage("Are you sure you want to remove anchor '"+anchor_name_del+"'..?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getActivity(),"Removing anchor...",Toast.LENGTH_SHORT).show();
                                removeAnchor(anchor_code_del);
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
                break;
        }
    }

    private void addAnchor(){
        final String anchor_name =edt_add_anchor_name.getText().toString().trim();
        final String anchor_desc =edt_add_anchor_desc.getText().toString().trim();

        if(anchor_name.equals("")||anchor_desc.equals("")){
            Toast.makeText(getActivity(), "Invalid data", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getActivity(),"Adding anchor...",Toast.LENGTH_SHORT).show();
            String url = "https://jimsd.org/jimsradio/add_anchor.php?";
            requestQueue = Volley.newRequestQueue(getActivity());
            StringRequest request = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(getActivity(), ""+response, Toast.LENGTH_SHORT).show();
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.frag_main, new FragmentAnchor()).commit();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Toast.makeText(getActivity(), "Cannot connect to the server, check your Internet conection and try again...", Toast.LENGTH_SHORT).show();
                            Toast.makeText(getActivity(),""+error,Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> param = new HashMap<>();
                    param.put("a_name", anchor_name);
                    param.put("a_desc", anchor_desc);
                    return param;
                }
            };
            requestQueue.add(request);
        }
    }

    private void fetchAnchor() {
        String url = "https://jimsd.org/jimsradio/fetch_anchor.php?";
        requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONArray json =new JSONArray(response);
                            JSONObject obj;
                            array_anchor_adapter.clear();
                            arr_anchor=new String[json.length()][3];
                            for(int i=0;i<json.length();i++){
                                //adding to drawer code
                                obj=json.getJSONObject(i);
                                arr_anchor[i][0]=obj.getString("anchor_id");
                                arr_anchor[i][1]=obj.getString("anchor_name");
                                arr_anchor[i][2]=obj.getString("anchor_desc");
                                array_anchor_list.add( arr_anchor[i][1] );
                                array_anchor_adapter.notifyDataSetChanged();
                                if(flag_active_option==0) {
                                    anchorItem=new HashMap<String,String>();
                                    anchorItem.put( "line1", arr_anchor[i][1]);
                                    anchorItem.put( "line2", arr_anchor[i][2]);
                                    anchorList.add(anchorItem);
                                    anchorAdapter.notifyDataSetChanged();
                                }
                            }
                            progressBar.setVisibility(View.GONE);
                            if(flag_active_option==0){
                                list_anchor.setVisibility(View.VISIBLE);
                                layout_view_anchor.setVisibility(View.VISIBLE);
                            }else if(flag_active_option==2){
                                layout_edit_anchor.setVisibility(View.VISIBLE);
                            }else if(flag_active_option==3){
                                layout_remove_anchor.setVisibility(View.VISIBLE);
                            }
                            else{
                                Toast.makeText(getActivity(),"Active view error...",Toast.LENGTH_SHORT).show();
                            }
                        }catch(JSONException e) {
                            Toast.makeText(getActivity(), "No valid anchor records..", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }catch(Exception e){
                            Toast.makeText(getActivity(),"An Error has Occurred"+e,Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(getActivity(), "Cannot connect to the server, check your Internet conection and try again...", Toast.LENGTH_SHORT).show();
                        Toast.makeText(getActivity(),""+error,Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("a_code", "empty");
                return param;
            }
        };
        requestQueue.add(request);
    }


    private void updateAnchor() {
        if(edit_anchor_code_choice==null){
            Toast.makeText(getActivity(), "Select a anchor first...", Toast.LENGTH_SHORT).show();
            return;
        }
        final String anchor_name =edt_edit_anchor_name.getText().toString().trim();
        final String anchor_desc =edt_edit_anchor_desc.getText().toString().trim();

        if(anchor_name.equals("")||anchor_desc.equals("")){
            Toast.makeText(getActivity(), "Invalid data", Toast.LENGTH_SHORT).show();
        }
        else if(edit_anchor_name_old.equals(anchor_name)&&edit_anchor_desc_old.equals(anchor_desc)){
            Toast.makeText(getActivity(), "No changes recorded", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getActivity(),"Updating anchor...",Toast.LENGTH_SHORT).show();
            String url = "https://jimsd.org/jimsradio/edit_anchor.php?";
            requestQueue = Volley.newRequestQueue(getActivity());
            StringRequest request = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(getActivity(), ""+response, Toast.LENGTH_SHORT).show();
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.frag_main, new FragmentAnchor()).commit();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Toast.makeText(getActivity(), "Cannot connect to the server, check your Internet conection and try again...", Toast.LENGTH_SHORT).show();
                            Toast.makeText(getActivity(),""+error,Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> param = new HashMap<>();
                    param.put("a_code", edit_anchor_code_choice);
                    param.put("a_name", anchor_name);
                    param.put("a_desc", anchor_desc);
                    return param;
                }
            };
            requestQueue.add(request);
        }
    }

    private void removeAnchor(final String anchor_rem_code){
        String url="https://jimsd.org/jimsradio/remove_anchor.php?";
        requestQueue= Volley.newRequestQueue(getActivity());
        StringRequest request =new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getActivity(), ""+response, Toast.LENGTH_SHORT).show();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frag_main, new FragmentAnchor()).commit();
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
                param.put("a_code",anchor_rem_code);
                return param;
            }
        };
        requestQueue.add(request);
    }

}

