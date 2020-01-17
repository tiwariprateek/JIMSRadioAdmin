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
import android.widget.Button;
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

public class FragmentRemoveProgram extends Fragment implements View.OnClickListener{
    RequestQueue requestQueue;
    ProgressBar prog_progbar;
    LinearLayout layout_remove_program;

    //remove layout
    Spinner sprem_select_show,sprem_select_program;
    String show_code_rem,prog_code_rem,prog_name_rem;
    TextView txtrem_prog_name,txtrem_show_name,txtrem_progdesc;
    Button btn_remove_prog;

    //common items
    String arr_show[][];
    ProgramHelper[] programHelper;
    ArrayList array_show_list,array_prog_list;
    ArrayAdapter array_show_adapter,array_prog_adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_remove_program, container, false);
        prog_progbar=(ProgressBar)v.findViewById(R.id.progbar_remove_program);
        layout_remove_program=(LinearLayout)v.findViewById(R.id.layout_remove_program);

        //adapter for operations
        array_show_list=new ArrayList();
        array_show_adapter=new ArrayAdapter(getActivity(),
                android.R.layout.simple_dropdown_item_1line,array_show_list);
        array_prog_list=new ArrayList();
        array_prog_adapter=new ArrayAdapter(getActivity(),
                android.R.layout.simple_dropdown_item_1line,array_prog_list);

        //remove program layout elements-----------------------------------------
        txtrem_prog_name=(TextView) v.findViewById(R.id.txt_remove_prog_name);
        txtrem_show_name=(TextView) v.findViewById(R.id.txt_remove_prog_show);
        txtrem_progdesc = (TextView) v.findViewById(R.id.txt_remove_prog_desc);
        btn_remove_prog=(Button)v.findViewById(R.id.btn_remove_program);
        btn_remove_prog.setOnClickListener(this);
        sprem_select_show=(Spinner)v.findViewById(R.id.sp_removeprog_get_show);
        sprem_select_show.setAdapter(array_show_adapter);
        sprem_select_program=(Spinner)v.findViewById(R.id.sp_removeprog_get_prog);
        sprem_select_program.setAdapter(array_prog_adapter);

        sprem_select_show.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                show_code_rem=arr_show[position][0];
                fetchProgram(show_code_rem);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        sprem_select_program.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                del_prog_values_update(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        //-----------------------------------------------------------------------

        fetchShow();
        return v;
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_remove_program:
                new AlertDialog.Builder(getActivity())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Exit..?")
                        .setMessage("Are you sure you want to remove program '"+prog_name_rem+"'..?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getActivity(),"Removing show...",Toast.LENGTH_SHORT).show();
                                removeProgram();
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
                break;
        }
    }

    private void del_prog_values_update(int position) {
        prog_code_rem=""+programHelper[position].put_audio_id();
        prog_name_rem=programHelper[position].put_prog_name();

        Toast.makeText(getActivity(), "Selected program to be deleted:\n"+prog_name_rem
                , Toast.LENGTH_SHORT).show();
        txtrem_prog_name.setText("Name: "+programHelper[position].put_prog_name());
        txtrem_show_name.setText("Show: "+programHelper[position].put_show_name());
        txtrem_progdesc.setText("Description:\n"+programHelper[position].put_prog_desc());
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
                            layout_remove_program.setVisibility(View.VISIBLE);

                        }catch(JSONException e){
                            Toast.makeText(getActivity(),"No records found"+e,Toast.LENGTH_SHORT).show();
                            prog_progbar.setVisibility(View.GONE);
                        }catch(Exception e){
                            Toast.makeText(getActivity(),"An Error has Occurred"+e,Toast.LENGTH_SHORT).show();
                            prog_progbar.setVisibility(View.GONE);
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

    private void fetchProgram(final String code) {
        String url="https://jimsd.org/jimsradio/fetch_program.php?";
        requestQueue= Volley.newRequestQueue(getActivity());
        StringRequest request =new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONArray json =new JSONArray(response);
                            JSONObject obj;
                            programHelper=new ProgramHelper[json.length()];
                            ProgramHelper programHelper_=new ProgramHelper();
                            array_prog_adapter.clear();
                            for(int i=0;i<json.length();i++){
                                //adding to drawer code
                                obj=json.getJSONObject(i);
                                programHelper[i]=new ProgramHelper();
                                programHelper[i].get_audio_id(Integer.parseInt(obj.getString("audio_id")));
                                programHelper[i].get_prog_name(obj.getString("name"));
                                programHelper[i].get_show_name((obj.getString("show_name")));
                                programHelper[i].get_prog_desc(obj.getString("prog_desc"));
                                programHelper[i].get_prog_date(obj.getString("date"));
                                programHelper[i].get_prog_link(obj.getString("link"));
                                array_prog_list.add(programHelper[i].put_prog_name());
                                array_prog_adapter.notifyDataSetChanged();

                            }
                            if(json.length()>0)
                                del_prog_values_update(0);
                            else
                                Toast.makeText(getActivity(), "No program present", Toast.LENGTH_SHORT).show();
                        }catch(JSONException e){
                            Toast.makeText(getActivity(),"No records found..\n"+e,Toast.LENGTH_SHORT).show();
                        }catch(Exception e){
                            Toast.makeText(getActivity(),"An error has occured..\n"+e,Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(),"Cannot connect to the server, check your Internet conection and try again...",Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param=new HashMap<>();
                param.put("code",code);
                param.put("sort","name_a2z");
                return param;
            }
        };
        requestQueue.add(request);
    }

    private void removeProgram() {
        if(prog_code_rem.equals("")){
            Toast.makeText(getActivity(), "A selection is required", Toast.LENGTH_SHORT).show();
            return;
        }
        final String url = "https://jimsd.org/jimsradio/remove_program.php?";
        requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getActivity(), ""+response, Toast.LENGTH_LONG).show();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frag_main, new FragmentProgram()).commit();
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
                param.put("prog_code", prog_code_rem);
                return param;
            }
        };
        requestQueue.add(request);
    }


}
