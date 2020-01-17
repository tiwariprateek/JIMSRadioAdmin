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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FragmentSchedule extends Fragment implements View.OnClickListener{
    LinearLayout layout_add_schedule,layout_edit_schedule,layout_remove_schedule;
    Button btnset_add_schedule,btnset_edit_schedule,btnset_remove_schedule,
            btn_add_schedule,btn_edit_schedule,btn_remove_schedule;
    EditText edt_schedule_prog,edt_schedule_txt,
            edt_date_dd,edt_date_mm,edt_date_yyyy,
            edt_date_to_dd,edt_date_to_mm,edt_date_to_yyyy,
            edt_date_from_dd,edt_date_from_mm,edt_date_from_yyyy,
            edtedit_schedule_prog,edtedit_schedule_txt,edtedit_schedule_date,
            edtedit_schedule_from_yyyymmdd,edtedit_schedule_to_yyyymmdd;

    TextView schedule_txt;
    Spinner sp_edit_schedule,sp_remove_schedule;
    ArrayList arrayList;
    ArrayAdapter arrayAdapter;
    String arr_schedule[][],schedule_rem_code,schedule_rem_name,schedule_edt_code;
    ProgressBar progBar;
    int flag_active_option;

    RequestQueue requestQueue;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_schedule, container, false);
        progBar= (ProgressBar)v.findViewById(R.id.progbar_schedule);
        //layouts for each show schedule operation
        layout_add_schedule=(LinearLayout)v.findViewById(R.id.layout_add_schedule);
        layout_edit_schedule=(LinearLayout)v.findViewById(R.id.layout_edit_schedule);
        layout_remove_schedule=(LinearLayout)v.findViewById(R.id.layout_remove_schedule);

        //Layout buttons
        btnset_add_schedule=(Button)v.findViewById(R.id.btn_set_add_schedule);
        btnset_add_schedule.setOnClickListener(this);
        btnset_edit_schedule=(Button)v.findViewById(R.id.btn_set_edit_schedule);
        btnset_edit_schedule.setOnClickListener(this);
        btnset_remove_schedule=(Button)v.findViewById(R.id.btn_set_remove_schedule);
        btnset_remove_schedule.setOnClickListener(this);
        btnset_add_schedule.setBackgroundResource(R.drawable.button_background);
        btnset_edit_schedule.setBackgroundResource(R.drawable.button_background);
        btnset_remove_schedule.setBackgroundResource(R.drawable.button_background);

        //adapters
        arrayList= new ArrayList();
        arrayAdapter= new ArrayAdapter(getActivity(),
                android.R.layout.simple_dropdown_item_1line,arrayList);

        //add schedule data
        edt_schedule_prog=(EditText)v.findViewById(R.id.edtadd_schedule_progname);
        edt_schedule_txt=(EditText)v.findViewById(R.id.edtadd_schedule_txt);

        edt_date_dd=(EditText)v.findViewById(R.id.add_schedule_date_dd);
        edt_date_mm=(EditText)v.findViewById(R.id.add_schedule_date_mm);
        edt_date_yyyy=(EditText)v.findViewById(R.id.add_schedule_date_yyyy);

        edt_date_from_dd=(EditText)v.findViewById(R.id.add_schedule_from_date_dd);
        edt_date_from_mm=(EditText)v.findViewById(R.id.add_schedule_from_date_mm);
        edt_date_from_yyyy=(EditText)v.findViewById(R.id.add_schedule_from_date_yyyy);

        edt_date_to_dd=(EditText)v.findViewById(R.id.add_schedule_to_date_dd);
        edt_date_to_mm=(EditText)v.findViewById(R.id.add_schedule_to_date_mm);
        edt_date_to_yyyy=(EditText)v.findViewById(R.id.add_schedule_to_date_yyyy);

        btn_add_schedule=(Button)v.findViewById(R.id.btn_add_schedule);
        btn_add_schedule.setOnClickListener(this);

        //edit schedule data
        edtedit_schedule_prog=(EditText)v.findViewById(R.id.edtedit_schedule_progname);
        edtedit_schedule_txt=(EditText)v.findViewById(R.id.edtedit_schedule_txt);
        edtedit_schedule_date=(EditText)v.findViewById(R.id.edtedit_schedule_date);
        edtedit_schedule_from_yyyymmdd=(EditText)v.findViewById(R.id.edtedit_schedule_fromdate);
        edtedit_schedule_to_yyyymmdd=(EditText)v.findViewById(R.id.edtedit_schedule_todate);

        btn_edit_schedule=(Button)v.findViewById(R.id.btn_update_schedule);
        btn_edit_schedule.setOnClickListener(this);

        sp_edit_schedule=(Spinner)v.findViewById(R.id.sp_edit_schedule);
        sp_edit_schedule.setAdapter(arrayAdapter);
        sp_edit_schedule.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                schedule_edt_code=arr_schedule[position][0];
                Toast.makeText(getActivity(), "Selected Item: "+arr_schedule[position][1],
                        Toast.LENGTH_SHORT).show();
                edtedit_schedule_prog.setText(arr_schedule[position][1]);
                edtedit_schedule_txt.setText(arr_schedule[position][2]);
                edtedit_schedule_date.setText(arr_schedule[position][3]);
                edtedit_schedule_from_yyyymmdd.setText(arr_schedule[position][4]);
                edtedit_schedule_to_yyyymmdd.setText(arr_schedule[position][5]);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        //remove schedule data
        schedule_txt= (TextView)v.findViewById(R.id.txt_remove_schedule_desc);
        sp_remove_schedule= (Spinner)v.findViewById(R.id.sp_remove_schedule);
        sp_remove_schedule.setAdapter(arrayAdapter);
        sp_remove_schedule.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String data;
                schedule_rem_code=arr_schedule[position][0];
                schedule_rem_name=arr_schedule[position][1];
                Toast.makeText(getActivity(), "Selected Item: "+arr_schedule[position][1],
                        Toast.LENGTH_SHORT).show();
                data="Program name: "+arr_schedule[position][1]+
                        "\nDescription: "+arr_schedule[position][2]+
                        "\nSchedule date: "+arr_schedule[position][3]+
                        "\n\nTo be displayed\nFrom: "+arr_schedule[position][4]+
                        "\nTo: "+arr_schedule[position][5];
                schedule_txt.setText(data);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btn_remove_schedule=(Button)v.findViewById(R.id.btn_remove_schedule);
        btn_remove_schedule.setOnClickListener(this);

        flag_active_option=1;

        btnset_add_schedule.setBackgroundResource(R.drawable.button_background);
        layout_add_schedule.setVisibility(View.VISIBLE);
        layout_edit_schedule.setVisibility(View.GONE);
        layout_remove_schedule.setVisibility(View.GONE);
        arrayAdapter.clear();

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_set_add_schedule:
                layout_add_schedule.setVisibility(View.VISIBLE);
                layout_edit_schedule.setVisibility(View.GONE);
                layout_remove_schedule.setVisibility(View.GONE);
                progBar.setVisibility(View.GONE);
                btnset_add_schedule.setBackgroundResource(R.drawable.button_background);
                btnset_edit_schedule.setBackgroundResource(R.drawable.button_background);
                btnset_remove_schedule.setBackgroundResource(R.drawable.button_background);
                arrayAdapter.clear();
                break;
            case R.id.btn_set_edit_schedule:
                layout_edit_schedule.setVisibility(View.GONE);
                layout_add_schedule.setVisibility(View.GONE);
                layout_remove_schedule.setVisibility(View.GONE);
                progBar.setVisibility(View.VISIBLE);
                btnset_edit_schedule.setBackgroundResource(R.drawable.button_background);
                btnset_add_schedule.setBackgroundResource(R.drawable.button_background);
                btnset_remove_schedule.setBackgroundResource(R.drawable.button_background);
                arrayAdapter.clear();
                flag_active_option=1;
                fetch_schedule();
                break;
            case R.id.btn_set_remove_schedule:
                layout_remove_schedule.setVisibility(View.GONE);
                layout_add_schedule.setVisibility(View.GONE);
                layout_edit_schedule.setVisibility(View.GONE);
                progBar.setVisibility(View.VISIBLE);
                btnset_remove_schedule.setBackgroundResource(R.drawable.button_background);
                btnset_add_schedule.setBackgroundResource(R.drawable.button_background);
                btnset_edit_schedule.setBackgroundResource(R.drawable.button_background);
                arrayAdapter.clear();
                flag_active_option=2;
                fetch_schedule();
                break;
            case R.id.btn_add_schedule:
                add_schedule();
                break;
            case R.id.btn_update_schedule:
                if(schedule_edt_code.equals("")){
                    Toast.makeText(getActivity(),"No schedule selected",Toast.LENGTH_SHORT).show();
                    return;
                }
                update_schedule();
                break;
            case R.id.btn_remove_schedule:
                if(schedule_rem_code.equals("")){
                    Toast.makeText(getActivity(), "No schedule selected for removing", Toast.LENGTH_SHORT).show();
                    return;
                }
                new AlertDialog.Builder(getActivity())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Exit..?")
                        .setMessage("Are you sure you want to remove schedule for '"+schedule_rem_name+"'..?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getActivity(),"Removing show...",Toast.LENGTH_SHORT).show();
                                remove_schedule();
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

    private void add_schedule() {
        String url = "https://jimsd.org/jimsradio/add_schedule.php?";
        
        final String prog_name=edt_schedule_prog.getText().toString().trim();
        final String schedule_txt=edt_schedule_txt.getText().toString().trim();
        if(schedule_txt.equals("")||prog_name.equals("")){
            Toast.makeText(getActivity(), "Cannot add empty fields..!", Toast.LENGTH_SHORT).show();
            return;
        }
        final String dd = edt_date_dd.getText().toString().trim();
        final String mm = edt_date_mm.getText().toString().trim();
        final String yyyy = edt_date_yyyy.getText().toString().trim();
        final String from_dd = edt_date_from_dd.getText().toString().trim();
        final String from_mm = edt_date_from_mm.getText().toString().trim();
        final String from_yyyy = edt_date_from_yyyy.getText().toString().trim();
        final String to_dd = edt_date_to_dd.getText().toString().trim();
        final String to_mm = edt_date_to_mm.getText().toString().trim();
        final String to_yyyy = edt_date_to_yyyy.getText().toString().trim();
        if(dd.equals("")||mm.equals("")||yyyy.equals("")){
            Toast.makeText(getActivity(), "Schedule Date values cannot be empty..", Toast.LENGTH_SHORT).show();
            return;
        }
        if(from_dd.equals("")||from_mm.equals("")||from_yyyy.equals("")){
            Toast.makeText(getActivity(), "From Date values cannot be empty..", Toast.LENGTH_SHORT).show();
            return;
        }
        if(to_dd.equals("")||to_mm.equals("")||to_yyyy.equals("")){
            Toast.makeText(getActivity(), "To Date values cannot be empty..", Toast.LENGTH_SHORT).show();
            return;
        }

        try{
            if(Integer.parseInt(dd)>31||Integer.parseInt(mm)>12||yyyy.length()!=4){
            Toast.makeText(getActivity(), "Check the scheduled date format carefully..", Toast.LENGTH_SHORT).show();
            return;
            }

            if(Integer.parseInt(from_dd)>31||Integer.parseInt(from_mm)>12||from_yyyy.length()!=4){
                Toast.makeText(getActivity(), "Check the from date format carefully..", Toast.LENGTH_SHORT).show();
                return;
            }
            if(Integer.parseInt(to_dd)>31||Integer.parseInt(to_mm)>12||to_yyyy.length()!=4){
                Toast.makeText(getActivity(), "Check the to date format carefully..", Toast.LENGTH_SHORT).show();
                return;
            }
            Integer.parseInt(yyyy);
            Integer.parseInt(from_yyyy);
            Integer.parseInt(to_yyyy);
        }catch (Exception e){
            Toast.makeText(getActivity(), "Invalid date", Toast.LENGTH_SHORT).show();
        }
        final String date=yyyy+"/"+mm+"/"+dd;
        final String from_date=from_yyyy+"/"+from_mm+"/"+from_dd;
        final String to_date=to_yyyy+"/"+to_mm+"/"+to_dd;

        requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getActivity(), ""+response, Toast.LENGTH_SHORT).show();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frag_main, new FragmentSchedule()).commit();
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
                param.put("prog",prog_name);
                param.put("schedule_txt", schedule_txt);
                param.put("date", date);
                param.put("from_date", from_date);
                param.put("to_date", to_date);
                return param;
            }
        };
        requestQueue.add(request);
    }

    private void fetch_schedule(){
        schedule_rem_code="";
        schedule_edt_code="";
        String url="https://jimsd.org/jimsradio/fetch_all_schedule.php?";
        requestQueue= Volley.newRequestQueue(getActivity());
        StringRequest request =new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONArray json =new JSONArray(response);
                            JSONObject obj;
                            int size=json.length();
                            arrayAdapter.clear();
                            arr_schedule=new String[size][6];
                            for(int i=0;i<json.length();i++){
                                //adding to drawer code
                                obj=json.getJSONObject(i);
                                arr_schedule[i][0]=obj.getString("schedule_id");
                                arr_schedule[i][1]=obj.getString("schedule_prog");
                                arr_schedule[i][2]=obj.getString("schedule_txt");
                                arr_schedule[i][3]=obj.getString("schedule_date");
                                arr_schedule[i][4]=obj.getString("from_date");
                                arr_schedule[i][5]=obj.getString("to_date");
                                arrayList.add( arr_schedule[i][1] );
                                arrayAdapter.notifyDataSetChanged();
                            }
                            progBar.setVisibility(View.GONE);
                            if(flag_active_option==1){
                                layout_edit_schedule.setVisibility(View.VISIBLE);
                            }else if(flag_active_option==2){
                                layout_remove_schedule.setVisibility(View.VISIBLE);
                            }
                            else{
                                Toast.makeText(getActivity(),"Active view error...",Toast.LENGTH_SHORT).show();
                            }
                        }catch(JSONException e){
                            Toast.makeText(getActivity(),"No records found..\n"+e,Toast.LENGTH_SHORT).show();
                            progBar.setVisibility(View.GONE);
                        }
                        catch(Exception e){
                            Toast.makeText(getActivity(),"An Error has Occurred"+e,Toast.LENGTH_SHORT).show();
                            progBar.setVisibility(View.GONE);
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


    private void update_schedule() {
        String url = "https://jimsd.org/jimsradio/edit_schedule.php?";

        final String prog_name=edtedit_schedule_prog.getText().toString().trim();
        final String schedule_txt=edtedit_schedule_txt.getText().toString().trim();
        if(schedule_txt.equals("")||prog_name.equals("")){
            Toast.makeText(getActivity(), "Cannot update empty fields..!", Toast.LENGTH_SHORT).show();
            return;
        }
        final String date = edtedit_schedule_date.getText().toString().trim();
        final String from_date = edtedit_schedule_from_yyyymmdd.getText().toString().trim();
        final String to_date = edtedit_schedule_to_yyyymmdd.getText().toString().trim();

        if(date.equals("")){
            Toast.makeText(getActivity(), "Schedule Date value cannot be empty..", Toast.LENGTH_SHORT).show();
            return;
        }
        if(from_date.equals("")){
            Toast.makeText(getActivity(), "From Date value cannot be empty..", Toast.LENGTH_SHORT).show();
            return;
        }
        if(to_date.equals("")){
            Toast.makeText(getActivity(), "To Date value cannot be empty..", Toast.LENGTH_SHORT).show();
            return;
        }

        requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getActivity(), ""+response, Toast.LENGTH_SHORT).show();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frag_main, new FragmentSchedule()).commit();
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
                param.put("schedule_id",schedule_edt_code);
                param.put("prog",prog_name);
                param.put("schedule_txt", schedule_txt);
                param.put("date", date);
                param.put("from_date", from_date);
                param.put("to_date", to_date);
                return param;
            }
        };
        requestQueue.add(request);
    }

    private void remove_schedule(){
        String url="https://jimsd.org/jimsradio/remove_schedule.php?";
        requestQueue= Volley.newRequestQueue(getActivity());
        StringRequest request =new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getActivity(), ""+response, Toast.LENGTH_SHORT).show();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frag_main, new FragmentSchedule()).commit();
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
                param.put("schedule_id",schedule_rem_code);
                return param;
            }
        };
        requestQueue.add(request);
    }

}
