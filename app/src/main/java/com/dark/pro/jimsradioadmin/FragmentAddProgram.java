package com.dark.pro.jimsradioadmin;

import android.content.Context;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FragmentAddProgram extends Fragment implements View.OnClickListener{

    Button btnadd_prog,btnadd_add_guest,btnadd_add_anchor,btnadd_rem_guest1,btnadd_rem_guest2,
            btnadd_rem_anchor1,btnadd_rem_anchor2;
    EditText edtadd_prog_name,edtadd_prog_desc,edtadd_dd,edtadd_mm,edtadd_yyyy;
    TextView txtadd_g1_name,txtadd_g1_detail,txtadd_g2_name,txtadd_g2_detail,
            txtadd_a1_name,txtadd_a1_detail,txtadd_a2_name,txtadd_a2_detail;
    EditText edtadd_prog_link;
    Spinner spadd_select_show,spadd_select_g1,spadd_select_g2,spadd_select_a1,spadd_select_a2;
    String show_code_add,show_name_add,g1_id,g2_id,a1_id,a2_id;
    LinearLayout layoutadd_guest2,layoutadd_anchor2,layoutedit_guest2,layoutedit_anchor2;

    //common items
    LinearLayout layout_add_program;
    ProgressBar prog_progbar;
    RequestQueue requestQueue;
    String arr_show[][],arr_guest[][],arr_anchor[][];
    ArrayList array_show_list,array_guest_list,array_anchor_list;
    ArrayAdapter array_show_adapter,array_guest_adapter,array_anchor_adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_add_program, container, false);
        prog_progbar=(ProgressBar)v.findViewById(R.id.progbar_add_program);
        layout_add_program=(LinearLayout)v.findViewById(R.id.layout_add_program);

        //adapter for operations
        array_show_list=new ArrayList();
        array_show_adapter=new ArrayAdapter(getActivity(),
                android.R.layout.simple_dropdown_item_1line,array_show_list);
        array_guest_list=new ArrayList();
        array_guest_adapter=new ArrayAdapter(getActivity(),
                android.R.layout.simple_dropdown_item_1line,array_guest_list);
        array_anchor_list=new ArrayList();
        array_anchor_adapter=new ArrayAdapter(getActivity(),
                android.R.layout.simple_dropdown_item_1line,array_anchor_list);

        edtadd_prog_name=(EditText)v.findViewById(R.id.edt_add_progname);
        edtadd_prog_desc=(EditText)v.findViewById(R.id.edt_add_progdesc);
        edtadd_prog_link=(EditText)v.findViewById(R.id.edt_add_proglink);
        edtadd_dd=(EditText)v.findViewById(R.id.add_prog_date_dd);
        edtadd_mm=(EditText)v.findViewById(R.id.add_prog_date_mm);
        edtadd_yyyy=(EditText)v.findViewById(R.id.add_prog_date_yyyy);
        btnadd_prog=(Button)v.findViewById(R.id.btn_add_program);
        btnadd_prog.setOnClickListener(this);

        layoutadd_guest2=(LinearLayout)v.findViewById(R.id.guest_layout_2);
        txtadd_g1_name=(TextView)v.findViewById(R.id.txtadd_guest_name1);
        txtadd_g1_detail=(TextView)v.findViewById(R.id.txtadd_guest_desc1);
        txtadd_g2_name=(TextView)v.findViewById(R.id.txtadd_guest_name2);
        txtadd_g2_detail=(TextView)v.findViewById(R.id.txtadd_guest_desc2);
        btnadd_add_guest=(Button)v.findViewById(R.id.btnadd_add_guest2);
        btnadd_add_guest.setOnClickListener(this);
        btnadd_rem_guest1=(Button)v.findViewById(R.id.btnadd_remove_guest1);
        btnadd_rem_guest1.setOnClickListener(this);
        btnadd_rem_guest2=(Button)v.findViewById(R.id.btnadd_remove_guest2);
        btnadd_rem_guest2.setOnClickListener(this);

        layoutadd_anchor2=(LinearLayout)v.findViewById(R.id.anchor_layout_2);
        txtadd_a1_name=(TextView)v.findViewById(R.id.txtadd_anchor_name1);
        txtadd_a1_detail=(TextView)v.findViewById(R.id.txtadd_anchor_desc1);
        txtadd_a2_name=(TextView)v.findViewById(R.id.txtadd_anchor_name2);
        txtadd_a2_detail=(TextView)v.findViewById(R.id.txtadd_anchor_desc2);
        btnadd_add_anchor=(Button)v.findViewById(R.id.btnadd_add_anchor2);
        btnadd_add_anchor.setOnClickListener(this);
        btnadd_rem_anchor1=(Button)v.findViewById(R.id.btnadd_remove_anchor1);
        btnadd_rem_anchor1.setOnClickListener(this);
        btnadd_rem_anchor2=(Button)v.findViewById(R.id.btnadd_remove_anchor2);
        btnadd_rem_anchor2.setOnClickListener(this);

        spadd_select_show=(Spinner)v.findViewById(R.id.sp_add_show_to_prog);
        spadd_select_show.setAdapter(array_show_adapter);
        spadd_select_show.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                show_code_add=arr_show[position][0];
                show_name_add=arr_show[position][1];
                Toast.makeText(getActivity(), "Selected show: "+ show_name_add, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        g1_id = g2_id = "empty";
        a1_id = a2_id = "empty";
        spadd_select_g1= (Spinner)v.findViewById(R.id.sp_add_guest1);
        spadd_select_g1.setAdapter(array_guest_adapter);
        spadd_select_g2= (Spinner)v.findViewById(R.id.sp_add_guest2);
        spadd_select_g2.setAdapter(array_guest_adapter);
        spadd_select_a1=(Spinner)v.findViewById(R.id.sp_add_anchor1);
        spadd_select_a1.setAdapter(array_anchor_adapter);
        spadd_select_a2=(Spinner)v.findViewById(R.id.sp_add_anchor2);
        spadd_select_a2.setAdapter(array_anchor_adapter);

        spadd_select_g1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    txtadd_g1_name.setText("");
                    txtadd_g1_detail.setText("");
                    g1_id="empty";
                }else{
                    txtadd_g1_name.setText("Name: "+arr_guest[position-1][1]);
                    txtadd_g1_detail.setText("Description: "+arr_guest[position-1][2]);
                    g1_id=arr_guest[position-1][0];
                }
                if(g1_id.equals(g2_id)&&!g1_id.equals("empty")){
                    spadd_select_g2.setSelection(0);
                    Toast.makeText(getActivity(), "Cannot select same guest twice..", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spadd_select_g2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    txtadd_g2_name.setText("");
                    txtadd_g2_detail.setText("");
                    g2_id="empty";
                }else{
                    txtadd_g2_name.setText("Name: "+arr_guest[position-1][1]);
                    txtadd_g2_detail.setText("Description: "+arr_guest[position-1][2]);
                    g2_id=arr_guest[position-1][0];
                }
                if(g1_id.equals(g2_id)&&!g1_id.equals("empty")){
                    spadd_select_g2.setSelection(0);
                    Toast.makeText(getActivity(), "Cannot select same guest twice..", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spadd_select_a1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    txtadd_a1_name.setText("");
                    txtadd_a1_detail.setText("");
                    a1_id="empty";
                }else{
                    txtadd_a1_name.setText("Name: "+arr_anchor[position-1][1]);
                    txtadd_a1_detail.setText("Description: "+arr_anchor[position-1][2]);
                    a1_id=arr_anchor[position-1][0];
                }
                if(a1_id.equals(a2_id)&&!a1_id.equals("empty")){
                    spadd_select_a2.setSelection(0);
                    Toast.makeText(getActivity(), "Cannot select same anchor twice..", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spadd_select_a2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    txtadd_a2_name.setText("");
                    txtadd_a2_detail.setText("");
                    a2_id="empty";
                }else{
                    txtadd_a2_name.setText("Name: "+arr_anchor[position-1][1]);
                    txtadd_a2_detail.setText("Description: "+arr_anchor[position-1][2]);
                    a2_id=arr_anchor[position-1][0];
                }
                if(a1_id.equals(a2_id)&&!a1_id.equals("empty")){
                    spadd_select_a2.setSelection(0);
                    Toast.makeText(getActivity(), "Cannot select same anchor twice..", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        fetchShow();
        return v;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_add_program:
                addProgram();
                break;
            case R.id.btnadd_add_guest2:
                layoutadd_guest2.setVisibility(View.VISIBLE);
                btnadd_add_guest.setVisibility(View.GONE);
                break;
            case R.id.btnadd_remove_guest1:
                spadd_select_g1.setSelection(0);
                break;
            case R.id.btnadd_remove_guest2:
                spadd_select_g2.setSelection(0);
                layoutadd_guest2.setVisibility(View.GONE);
                btnadd_add_guest.setVisibility(View.VISIBLE);
                break;
            case R.id.btnadd_add_anchor2:
                layoutadd_anchor2.setVisibility(View.VISIBLE);
                btnadd_add_anchor.setVisibility(View.GONE);
                break;
            case R.id.btnadd_remove_anchor1:
                spadd_select_a1.setSelection(0);
                break;
            case R.id.btnadd_remove_anchor2:
                spadd_select_a2.setSelection(0);
                layoutadd_anchor2.setVisibility(View.GONE);
                btnadd_add_anchor.setVisibility(View.VISIBLE);
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

    private void fetchShow(){
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
                            array_show_adapter.clear();
                            arr_show=new String[size][2];
                            for(int i=0;i<json.length();i++){
                                //adding to drawer code
                                obj=json.getJSONObject(i);
                                arr_show[i][0]=obj.getString("show_code");
                                arr_show[i][1]=obj.getString("show_name");
                                array_show_list.add( arr_show[i][1] );
                                array_show_adapter.notifyDataSetChanged();
                            }
                            prog_progbar.setVisibility(View.GONE);
                            layout_add_program.setVisibility(View.VISIBLE);
                            fetchGuest();
                        }catch(JSONException e){
                            Toast.makeText(getActivity(),"No records found:"+e,Toast.LENGTH_SHORT).show();
                        }
                        catch(Exception e){
                            Toast.makeText(getActivity(),"An Error has Occurred"+e,Toast.LENGTH_SHORT).show();
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

    private void fetchGuest() {
        String url="https://jimsd.org/jimsradio/fetch_all_guest.php?";
        requestQueue= Volley.newRequestQueue(getActivity());
        StringRequest request =new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{

                            JSONArray json =new JSONArray(response);
                            JSONObject obj;
                            int size=json.length();
                            array_guest_adapter.clear();
                            array_guest_list.add(0,"No guest selected");
                            arr_guest=new String[size][3];
                            for(int i=0;i<json.length();i++){
                                //adding to drawer code
                                obj=json.getJSONObject(i);
                                arr_guest[i][0]=obj.getString("guest_id");
                                arr_guest[i][1]=obj.getString("guest_name");
                                arr_guest[i][2]=obj.getString("guest_desc");
                                array_guest_list.add( arr_guest[i][1] );
                                array_guest_adapter.notifyDataSetChanged();
                            }
                            fetchAnchor();
                        }catch(JSONException e){
                            Toast.makeText(getActivity(),"No records found:"+e,Toast.LENGTH_SHORT).show();
                        }
                        catch(Exception e){
                            Toast.makeText(getActivity(),"An Error has Occurred"+e,Toast.LENGTH_SHORT).show();
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

    private void fetchAnchor() {
        String url="https://jimsd.org/jimsradio/fetch_all_anchor.php?";
        requestQueue= Volley.newRequestQueue(getActivity());
        StringRequest request =new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONArray json =new JSONArray(response);
                            JSONObject obj;
                            int size=json.length();
                            array_anchor_adapter.clear();
                            array_anchor_list.add(0,"No anchor selected");
                            arr_anchor=new String[size][3];
                            for(int i=0;i<json.length();i++){
                                //adding to drawer code
                                obj=json.getJSONObject(i);
                                arr_anchor[i][0]=obj.getString("anchor_id");
                                arr_anchor[i][1]=obj.getString("anchor_name");
                                arr_anchor[i][2]=obj.getString("anchor_desc");
                                array_anchor_list.add( arr_anchor[i][1] );
                                array_anchor_adapter.notifyDataSetChanged();
                            }

                        }catch(JSONException e){
                            Toast.makeText(getActivity(),"No records found:"+e,Toast.LENGTH_SHORT).show();
                        }
                        catch(Exception e){
                            Toast.makeText(getActivity(),"An Error has Occurred"+e,Toast.LENGTH_SHORT).show();
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

    private void addProgram() {
        final String prog_name = edtadd_prog_name.getText().toString().trim();
        final String prog_desc = edtadd_prog_desc.getText().toString().trim();
        final String prog_link = edtadd_prog_link.getText().toString().trim();
        if(prog_name.equals("")||prog_desc.equals("")||prog_link.equals("")){
            Toast.makeText(getActivity(), "Program fields cannot be empty..", Toast.LENGTH_SHORT).show();
            return;
        }

        final String prog_date_mm = edtadd_mm.getText().toString().trim();
        final String prog_date_dd = edtadd_dd.getText().toString().trim();
        final String prog_date_yyyy = edtadd_yyyy.getText().toString().trim();
        if(prog_date_dd.equals("")||prog_date_mm.equals("")||prog_date_yyyy.equals("")){
            Toast.makeText(getActivity(), "Date values cannot be empty..", Toast.LENGTH_SHORT).show();
            return;
        }
        try{
            Integer.parseInt(prog_date_dd);
            Integer.parseInt(prog_date_mm);
            Integer.parseInt(prog_date_yyyy);
        }catch (Exception e){
            Toast.makeText(getActivity(), "Invalid date", Toast.LENGTH_SHORT).show();
            return;
        }
        final String add_date=prog_date_yyyy+"/"+prog_date_mm+"/"+prog_date_dd;

        //guest checks
        String guestmsg="";
        if(g1_id.equals("empty"))
            guestmsg="Guest 1 empty..";
        else
            guestmsg="Guest 1 not empty..";

        if(g2_id.equals("empty")){
            guestmsg+="\nGuest 2 empty..";
            Toast.makeText(getActivity(), guestmsg, Toast.LENGTH_SHORT).show();
        }
        else{
            guestmsg+="\nGuest 2 not empty..";
            Toast.makeText(getActivity(), guestmsg, Toast.LENGTH_SHORT).show();
        }

        //anchor checks
        String anchormsg="";
        if(a1_id.equals("empty"))
            anchormsg="Anchor 1 empty..";
        else
            anchormsg="Anchor 1 not empty..";

        if(a2_id.equals("empty")){
            anchormsg+="\nAnchor 2 empty..";
            Toast.makeText(getActivity(), anchormsg, Toast.LENGTH_SHORT).show();
        }
        else{
            anchormsg+="\nAnchor 2 not empty..";
            Toast.makeText(getActivity(), anchormsg, Toast.LENGTH_SHORT).show();
        }
        
        final String url = "https://jimsd.org/jimsradio/add_program.php?";
        requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getActivity(), ""+response, Toast.LENGTH_SHORT).show();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frag_main, new FragmentAddProgram()).commit();
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
                param.put("prog_name", prog_name);
                param.put("prog_date", add_date);
                param.put("prog_desc", prog_desc);
                param.put("show_code", show_code_add);
                param.put("prog_link",prog_link);

                param.put("g1_id",g1_id);
                param.put("g2_id",g2_id);
                param.put("a1_id",a1_id);
                param.put("a2_id",a2_id);

                return param;
            }
        };
        requestQueue.add(request);
    }

}
