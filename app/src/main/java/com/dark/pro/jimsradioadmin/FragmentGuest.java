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

public class FragmentGuest extends Fragment implements View.OnClickListener {
    RequestQueue requestQueue;
    ProgressBar progressBar;
    Button btnset_view_guest,btnset_add_guest,btnset_edit_guest,btnset_remove_guest;
    LinearLayout layout_view_guest,layout_add_guest,layout_edit_guest,layout_remove_guest;

    Button btn_add_guest,btn_edit_guest,btn_remove_guest;
    EditText edt_add_guest_name,edt_add_guest_desc,
            edt_edit_guest_name,edt_edit_guest_desc;
    TextView txt_remove_guest_name,txt_remove_guest_desc;

    ArrayList array_guest_list;
    ArrayAdapter array_guest_adapter;

    ArrayList<HashMap<String,String>> guestList;
    HashMap<String,String> guestItem;
    SimpleAdapter guestAdapter;

    int flag_active_option;

    String edit_guest_code_choice,edit_guest_name_old,edit_guest_desc_old
            ,guest_name_del,guest_code_del;
    Spinner spinner_modify_guest_select,spinner_remove_guest_select;
    ListView list_guest;

    String arr_guest[][];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_guest, container, false);
        progressBar=(ProgressBar)v.findViewById(R.id.progbar_guest_options);
        //layout buttons
        btnset_view_guest=(Button)v.findViewById(R.id.btn_set_view_guest);
        btnset_view_guest.setOnClickListener(this);
        btnset_add_guest=(Button)v.findViewById(R.id.btn_set_add_guest);
        btnset_add_guest.setOnClickListener(this);
        btnset_edit_guest=(Button)v.findViewById(R.id.btn_set_edit_guest);
        btnset_edit_guest.setOnClickListener(this);
        btnset_remove_guest=(Button)v.findViewById(R.id.btn_set_remove_guest);
        btnset_remove_guest.setOnClickListener(this);
        btnset_view_guest.setBackgroundResource(android.R.drawable.btn_default);
        btnset_add_guest.setBackgroundResource(android.R.drawable.btn_default);
        btnset_edit_guest.setBackgroundResource(android.R.drawable.btn_default);
        btnset_remove_guest.setBackgroundResource(android.R.drawable.btn_default);

        //Action buttons for queries
        btn_add_guest=(Button)v.findViewById(R.id.btn_add_guest);
        btn_add_guest.setOnClickListener(this);
        btn_edit_guest=(Button)v.findViewById(R.id.btn_edit_guest);
        btn_edit_guest.setOnClickListener(this);
        btn_remove_guest=(Button)v.findViewById(R.id.btn_remove_guest);
        btn_remove_guest.setOnClickListener(this);

        //adapter for operations
        guestList=new ArrayList<>();
        guestAdapter = new SimpleAdapter(getActivity(), guestList,
                R.layout.layout_show,
                new String[] { "line1","line2" },
                new int[] {R.id.list_txt_show_name, R.id.list_txt_show_description});

        array_guest_list=new ArrayList();
        array_guest_adapter=new ArrayAdapter(getActivity(),
                android.R.layout.simple_dropdown_item_1line,array_guest_list);

        //layouts for each guest operation
        layout_view_guest=(LinearLayout)v.findViewById(R.id.layout_view_guest);
        layout_add_guest=(LinearLayout)v.findViewById(R.id.layout_add_guest);
        layout_edit_guest=(LinearLayout)v.findViewById(R.id.layout_edit_guest);
        layout_remove_guest=(LinearLayout)v.findViewById(R.id.layout_remove_guest);

        //view guest layout elements
        list_guest=(ListView)v.findViewById(R.id.list_guest);
        list_guest.setAdapter(guestAdapter);
        list_guest.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                new AlertDialog.Builder(getActivity())
                        .setIcon(R.drawable.alert)
                        .setTitle("Guest details")
                        .setMessage("Name:\n"+arr_guest[position][1]+
                                "\n\nDescription:\n"+arr_guest[position][2])
                        .setNeutralButton("OK",null)
                        .show();
            }
        });

        //add guest layout elements
        edt_add_guest_name=(EditText)v.findViewById(R.id.edt_add_guestname);
        edt_add_guest_desc=(EditText)v.findViewById(R.id.edt_add_guestdesc);

        //edit guest layout elements
        edt_edit_guest_name=(EditText)v.findViewById(R.id.edt_edit_guestname);
        edt_edit_guest_desc=(EditText)v.findViewById(R.id.edt_edit_guestdesc);

        spinner_modify_guest_select=(Spinner)v.findViewById(R.id.sp_edit_guest);
        spinner_modify_guest_select.setAdapter(array_guest_adapter);
        spinner_modify_guest_select.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                edit_guest_code_choice=arr_guest[position][0];
                edit_guest_name_old=arr_guest[position][1];
                edit_guest_desc_old=arr_guest[position][2];
                edt_edit_guest_name.setText(arr_guest[position][1]);
                edt_edit_guest_desc.setText(arr_guest[position][2]);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        edit_guest_code_choice=null;

        //remove show layout elements
        spinner_remove_guest_select=(Spinner)v.findViewById(R.id.sp_remove_guest);
        spinner_remove_guest_select.setAdapter(array_guest_adapter);
        txt_remove_guest_name=(TextView)v.findViewById(R.id.txt_remove_guestname);
        txt_remove_guest_desc=(TextView)v.findViewById(R.id.txt_remove_guestdesc);
        spinner_remove_guest_select.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                guest_code_del=arr_guest[position][0];
                guest_name_del=arr_guest[position][1];
                txt_remove_guest_name.setText("Name: "+arr_guest[position][1]);
                txt_remove_guest_desc.setText("Description: \n"+arr_guest[position][2]);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        flag_active_option=0;
        progressBar.setVisibility(View.VISIBLE);
        btnset_view_guest.setBackgroundColor(Color.parseColor("#00ff00"));
        fetchGuest();
        return v;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_set_view_guest:
                flag_active_option = 0;
                btnset_view_guest.setBackgroundColor(Color.parseColor("#00ff00"));
                btnset_add_guest.setBackgroundResource(android.R.drawable.btn_default);
                btnset_edit_guest.setBackgroundResource(android.R.drawable.btn_default);
                btnset_remove_guest.setBackgroundResource(android.R.drawable.btn_default);
                layout_view_guest.setVisibility(View.GONE);
                layout_add_guest.setVisibility(View.GONE);
                layout_edit_guest.setVisibility(View.GONE);
                layout_remove_guest.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                guestList.clear();
                fetchGuest();
                break;
            case R.id.btn_set_add_guest:
                flag_active_option = 1;
                btnset_add_guest.setBackgroundColor(Color.parseColor("#00ff00"));
                btnset_view_guest.setBackgroundResource(android.R.drawable.btn_default);
                btnset_edit_guest.setBackgroundResource(android.R.drawable.btn_default);
                btnset_remove_guest.setBackgroundResource(android.R.drawable.btn_default);
                layout_add_guest.setVisibility(View.VISIBLE);
                layout_view_guest.setVisibility(View.GONE);
                layout_edit_guest.setVisibility(View.GONE);
                layout_remove_guest.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                break;
            case R.id.btn_set_edit_guest:
                flag_active_option = 2;
                btnset_edit_guest.setBackgroundColor(Color.parseColor("#00ff00"));
                btnset_view_guest.setBackgroundResource(android.R.drawable.btn_default);
                btnset_add_guest.setBackgroundResource(android.R.drawable.btn_default);
                btnset_remove_guest.setBackgroundResource(android.R.drawable.btn_default);
                layout_view_guest.setVisibility(View.GONE);
                layout_add_guest.setVisibility(View.GONE);
                layout_edit_guest.setVisibility(View.GONE);
                layout_remove_guest.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                array_guest_adapter.clear();
                fetchGuest();
                break;
            case R.id.btn_set_remove_guest:
                flag_active_option = 3;
                btnset_remove_guest.setBackgroundColor(Color.parseColor("#00ff00"));
                btnset_view_guest.setBackgroundResource(android.R.drawable.btn_default);
                btnset_add_guest.setBackgroundResource(android.R.drawable.btn_default);
                btnset_edit_guest.setBackgroundResource(android.R.drawable.btn_default);
                layout_view_guest.setVisibility(View.GONE);
                layout_add_guest.setVisibility(View.GONE);
                layout_edit_guest.setVisibility(View.GONE);
                layout_remove_guest.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                array_guest_adapter.clear();
                fetchGuest();
                break;
            case R.id.btn_add_guest:
                addGuest();
                break;
            case R.id.btn_edit_guest:
                updateGuest();
                break;
            case R.id.btn_remove_guest:
                new AlertDialog.Builder(getActivity())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Exit..?")
                        .setMessage("Are you sure you want to remove guest '"+guest_name_del+"'..?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getActivity(),"Removing guest...",Toast.LENGTH_SHORT).show();
                                removeGuest(guest_code_del);
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
                break;
        }
    }

    private void addGuest(){
        final String guest_name =edt_add_guest_name.getText().toString().trim();
        final String guest_desc =edt_add_guest_desc.getText().toString().trim();

        if(guest_name.equals("")||guest_desc.equals("")){
            Toast.makeText(getActivity(), "Invalid data", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getActivity(),"Adding guest...",Toast.LENGTH_SHORT).show();
            String url = "https://jimsd.org/jimsradio/add_guest.php?";
            requestQueue = Volley.newRequestQueue(getActivity());
            StringRequest request = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(getActivity(), ""+response, Toast.LENGTH_SHORT).show();
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.frag_main, new FragmentGuest()).commit();
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
                    param.put("g_name", guest_name);
                    param.put("g_desc", guest_desc);
                    return param;
                }
            };
            requestQueue.add(request);
        }
    }

    private void fetchGuest() {
        String url = "https://jimsd.org/jimsradio/fetch_guest.php?";
        requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONArray json =new JSONArray(response);
                            JSONObject obj;
                            array_guest_adapter.clear();
                            arr_guest=new String[json.length()][3];
                            for(int i=0;i<json.length();i++){
                                //adding to drawer code
                                obj=json.getJSONObject(i);
                                arr_guest[i][0]=obj.getString("guest_id");
                                arr_guest[i][1]=obj.getString("guest_name");
                                arr_guest[i][2]=obj.getString("guest_desc");
                                array_guest_list.add( arr_guest[i][1] );
                                array_guest_adapter.notifyDataSetChanged();
                                if(flag_active_option==0) {
                                    guestItem=new HashMap<String,String>();
                                    guestItem.put( "line1", arr_guest[i][1]);
                                    guestItem.put( "line2", arr_guest[i][2]);
                                    guestList.add(guestItem);
                                    guestAdapter.notifyDataSetChanged();
                                }
                            }
                            progressBar.setVisibility(View.GONE);
                            if(flag_active_option==0){
                                list_guest.setVisibility(View.VISIBLE);
                                layout_view_guest.setVisibility(View.VISIBLE);
                            }else if(flag_active_option==2){
                                layout_edit_guest.setVisibility(View.VISIBLE);
                            }else if(flag_active_option==3){
                                layout_remove_guest.setVisibility(View.VISIBLE);
                            }
                            else{
                                Toast.makeText(getActivity(),"Active view error...",Toast.LENGTH_SHORT).show();
                            }
                        }catch(JSONException e) {
                            Toast.makeText(getActivity(), "No valid guest records..", Toast.LENGTH_SHORT).show();
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
                param.put("g_code", "empty");
                return param;
            }
        };
        requestQueue.add(request);
    }

    private void updateGuest() {
        if(edit_guest_code_choice==null){
            Toast.makeText(getActivity(), "Select a guest first...", Toast.LENGTH_SHORT).show();
            return;
        }
        final String guest_name =edt_edit_guest_name.getText().toString().trim();
        final String guest_desc =edt_edit_guest_desc.getText().toString().trim();

        if(guest_name.equals("")||guest_desc.equals("")){
            Toast.makeText(getActivity(), "Invalid data", Toast.LENGTH_SHORT).show();
        }
        else if(edit_guest_name_old.equals(guest_name)&&edit_guest_desc_old.equals(guest_desc)){
            Toast.makeText(getActivity(), "No changes recorded", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getActivity(),"Updating guest...",Toast.LENGTH_SHORT).show();
            String url = "https://jimsd.org/jimsradio/edit_guest.php?";
            requestQueue = Volley.newRequestQueue(getActivity());
            StringRequest request = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(getActivity(), ""+response, Toast.LENGTH_SHORT).show();
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.frag_main, new FragmentGuest()).commit();
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
                    param.put("g_code", edit_guest_code_choice);
                    param.put("g_name", guest_name);
                    param.put("g_desc", guest_desc);
                    return param;
                }
            };
            requestQueue.add(request);
        }
    }

    private void removeGuest(final String guest_rem_code){
        String url="https://jimsd.org/jimsradio/remove_guest.php?";
        requestQueue= Volley.newRequestQueue(getActivity());
        StringRequest request =new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getActivity(), ""+response, Toast.LENGTH_SHORT).show();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frag_main, new FragmentGuest()).commit();
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
                param.put("g_code",guest_rem_code);
                return param;
            }
        };
        requestQueue.add(request);
    }

}
