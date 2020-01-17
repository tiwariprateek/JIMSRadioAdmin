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

public class FragmentEditProgram extends Fragment implements View.OnClickListener{
    RequestQueue requestQueue;
    ProgressBar prog_progbar;
    LinearLayout layout_edit_program,layoutedit_guest2,layoutedit_anchor2;

    //edit layout
    Spinner spedit_select_show,spedit_select_program,spedit_choose_show,
            spedit_select_g1,spedit_select_g2,spedit_select_a1,spedit_select_a2;
    String show_code_edit,prog_code_edit,show_code_edit_choose,show_name_edit_choose;
    int edit_prog_chosen_show_index=0;
    String gid[],aid[],arr_guest[][],arr_anchor[][];
    Button btn_edit_prog,btnedit_add_guest,btnedit_add_anchor,btnedit_rem_guest1,btnedit_rem_guest2,
            btnedit_rem_anchor1,btnedit_rem_anchor2;
    EditText edtedit_prog_name,edtedit_prog_desc,edtedit_date;
    TextView txtedit_g1_name,txtedit_g1_detail,txtedit_g2_name,txtedit_g2_detail,
            txtedit_a1_name,txtedit_a1_detail,txtedit_a2_name,txtedit_a2_detail;
    EditText edtedit_prog_link;

    //common items
    String arr_show[][];
    ProgramHelper[] programHelper;
    ArrayList array_show_list,array_prog_list,array_guest_list,array_anchor_list;
    ArrayAdapter array_show_adapter,array_prog_adapter,array_guest_adapter,array_anchor_adapter;
    boolean flag_g1,flag_g2,flag_a1,flag_a2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_edit_program, container, false);
        prog_progbar=(ProgressBar)v.findViewById(R.id.progbar_edit_program);

        //adapter for shows and programs
        array_show_list=new ArrayList();
        array_show_adapter=new ArrayAdapter(getActivity(),
                android.R.layout.simple_dropdown_item_1line,array_show_list);
        array_prog_list=new ArrayList();
        array_prog_adapter=new ArrayAdapter(getActivity(),
                android.R.layout.simple_dropdown_item_1line,array_prog_list);

        //adapters for guest and anchors
        array_guest_list=new ArrayList();
        array_guest_adapter=new ArrayAdapter(getActivity(),
                android.R.layout.simple_dropdown_item_1line,array_guest_list);
        array_anchor_list=new ArrayList();
        array_anchor_adapter=new ArrayAdapter(getActivity(),
                android.R.layout.simple_dropdown_item_1line,array_anchor_list);

        //layouts for each program operation
        layout_edit_program=(LinearLayout)v.findViewById(R.id.layout_edit_program);

        //edit program layout elements-------------------------------------------
        btn_edit_prog=(Button)v.findViewById(R.id.btn_edit_program);
        btn_edit_prog.setOnClickListener(this);
        edtedit_prog_name=(EditText)v.findViewById(R.id.edt_edit_progname);
        edtedit_prog_desc=(EditText)v.findViewById(R.id.edt_edit_progdesc);
        edtedit_prog_link=(EditText)v.findViewById(R.id.edt_edit_proglink);
        edtedit_date=(EditText)v.findViewById(R.id.edit_prog_date);

        //edit guest elements
        layoutedit_guest2=(LinearLayout)v.findViewById(R.id.editguest_layout_2);
        txtedit_g1_name=(TextView)v.findViewById(R.id.txtedit_guest_name1);
        txtedit_g1_detail=(TextView)v.findViewById(R.id.txtedit_guest_desc1);
        txtedit_g2_name=(TextView)v.findViewById(R.id.txtedit_guest_name2);
        txtedit_g2_detail=(TextView)v.findViewById(R.id.txtedit_guest_desc2);
        btnedit_add_guest=(Button)v.findViewById(R.id.btnedit_add_guest2);
        btnedit_add_guest.setOnClickListener(this);
        btnedit_rem_guest1=(Button)v.findViewById(R.id.btnedit_remove_guest1);
        btnedit_rem_guest1.setOnClickListener(this);
        btnedit_rem_guest2=(Button)v.findViewById(R.id.btnedit_remove_guest2);
        btnedit_rem_guest2.setOnClickListener(this);
        //edit anchor elements
        layoutedit_anchor2=(LinearLayout)v.findViewById(R.id.editanchor_layout_2);
        txtedit_a1_name=(TextView)v.findViewById(R.id.txtedit_anchor_name1);
        txtedit_a1_detail=(TextView)v.findViewById(R.id.txtedit_anchor_desc1);
        txtedit_a2_name=(TextView)v.findViewById(R.id.txtedit_anchor_name2);
        txtedit_a2_detail=(TextView)v.findViewById(R.id.txtedit_anchor_desc2);
        btnedit_add_anchor=(Button)v.findViewById(R.id.btnedit_add_anchor2);
        btnedit_add_anchor.setOnClickListener(this);
        btnedit_rem_anchor1=(Button)v.findViewById(R.id.btnedit_remove_anchor1);
        btnedit_rem_anchor1.setOnClickListener(this);
        btnedit_rem_anchor2=(Button)v.findViewById(R.id.btnedit_remove_anchor2);
        btnedit_rem_anchor2.setOnClickListener(this);

        spedit_select_show=(Spinner)v.findViewById(R.id.sp_editprog_get_show);
        spedit_select_show.setAdapter(array_show_adapter);
        spedit_select_program=(Spinner)v.findViewById(R.id.sp_editprog_get_prog);
        spedit_select_program.setAdapter(array_prog_adapter);
        spedit_choose_show=(Spinner)v.findViewById(R.id.sp_edit_show_to_prog);
        spedit_choose_show.setAdapter(array_show_adapter);

        spedit_select_a1=(Spinner)v.findViewById(R.id.sp_edit_anchor1);
        spedit_select_a1.setAdapter(array_anchor_adapter);
        spedit_select_a2=(Spinner)v.findViewById(R.id.sp_edit_anchor2);
        spedit_select_a2.setAdapter(array_anchor_adapter);
        spedit_select_g1=(Spinner)v.findViewById(R.id.sp_edit_guest1);
        spedit_select_g1.setAdapter(array_guest_adapter);
        spedit_select_g2=(Spinner)v.findViewById(R.id.sp_edit_guest2);
        spedit_select_g2.setAdapter(array_guest_adapter);


        spedit_select_show.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                edit_prog_chosen_show_index=position;
                show_code_edit=arr_show[position][0];
                fetchProgram(show_code_edit);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spedit_select_program.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                display_edit_values_prog(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spedit_choose_show.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                show_code_edit_choose=arr_show[position][0];
                show_name_edit_choose=arr_show[position][1];
                Toast.makeText(getActivity(), "Selected show: "+ show_name_edit_choose,
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spedit_select_g1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    txtedit_g1_name.setText("");
                    txtedit_g1_detail.setText("");
                    gid[0]="empty";
                }else{
                    txtedit_g1_name.setText("Name: "+arr_guest[position-1][1]);
                    txtedit_g1_detail.setText("Description: "+arr_guest[position-1][2]);
                    gid[0]=arr_guest[position-1][0];
                }
                if(gid[0].equals(gid[1])&&!gid[0].equals("empty")){
                    spedit_select_g2.setSelection(0);
                    Toast.makeText(getActivity(), "Cannot select same guest twice..", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spedit_select_g2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    txtedit_g2_name.setText("");
                    txtedit_g2_detail.setText("");
                    gid[0]="empty";
                }else{
                    txtedit_g2_name.setText("Name: "+arr_guest[position-1][1]);
                    txtedit_g2_detail.setText("Description: "+arr_guest[position-1][2]);
                    gid[1]=arr_guest[position-1][0];
                }
                if(gid[1].equals(gid[0])&&!gid[0].equals("empty")){
                    spedit_select_g2.setSelection(0);
                    Toast.makeText(getActivity(), "Cannot select same guest twice..", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spedit_select_a1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    txtedit_a1_name.setText("");
                    txtedit_a1_detail.setText("");
                    aid[0]="empty";
                }else{
                    txtedit_a1_name.setText("Name: "+arr_anchor[position-1][1]);
                    txtedit_a1_detail.setText("Description: "+arr_anchor[position-1][2]);
                    aid[0]=arr_anchor[position-1][0];
                }
                if(aid[0].equals(aid[1])&&!aid[0].equals("empty")){
                    spedit_select_a2.setSelection(0);
                    Toast.makeText(getActivity(), "Cannot select same anchor twice..", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spedit_select_a2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    txtedit_a2_name.setText("");
                    txtedit_a2_detail.setText("");
                    aid[1]="empty";
                }else{
                    txtedit_a2_name.setText("Name: "+arr_anchor[position-1][1]);
                    txtedit_a2_detail.setText("Description: "+arr_anchor[position-1][2]);
                    aid[1]=arr_anchor[position-1][0];
                }
                if(aid[1].equals(aid[0])&&!aid[0].equals("empty")){
                    spedit_select_a2.setSelection(0);
                    Toast.makeText(getActivity(), "Cannot select same anchor twice..", Toast.LENGTH_SHORT).show();
                }
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_edit_program:
                updateProgram(prog_code_edit);
                break;
            case R.id.btnedit_add_guest2:
                layoutedit_guest2.setVisibility(View.VISIBLE);
                btnedit_add_guest.setVisibility(View.GONE);
                break;
            case R.id.btnedit_remove_guest1:
                spedit_select_g1.setSelection(0);
                break;
            case R.id.btnedit_remove_guest2:
                spedit_select_g2.setSelection(0);
                layoutedit_guest2.setVisibility(View.GONE);
                btnedit_add_guest.setVisibility(View.VISIBLE);
                break;
            case R.id.btnedit_add_anchor2:
                layoutedit_anchor2.setVisibility(View.VISIBLE);
                btnedit_add_anchor.setVisibility(View.GONE);
                break;
            case R.id.btnedit_remove_anchor1:
                spedit_select_a1.setSelection(0);
                break;
            case R.id.btnedit_remove_anchor2:
                spedit_select_a2.setSelection(0);
                layoutedit_anchor2.setVisibility(View.GONE);
                btnedit_add_anchor.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void display_edit_values_prog(int position) {
        prog_code_edit=""+programHelper[position].put_audio_id();
        //String show_name_edit = programHelper[position].put_show_name();
        String prog_name_edit=programHelper[position].put_prog_name();
        String prog_desc_edit =programHelper[position].put_prog_desc();
        String prog_date_edit = programHelper[position].put_prog_date();
        String prog_link_edit = programHelper[position].put_prog_link();

        edtedit_prog_name.setText(prog_name_edit);
        edtedit_prog_desc.setText(prog_desc_edit);
        edtedit_prog_link.setText(prog_link_edit);
        edtedit_date.setText(prog_date_edit);
        spedit_choose_show.setSelection(edit_prog_chosen_show_index);

        update_edit_guest(prog_code_edit);

    }

    private void update_edit_anchor(final String prog_code) {
        aid = new String[2];
        aid[0] = new String();
        aid[1] = new String();
        String url="https://jimsd.org/jimsradio/fetch_prog_anchor.php?";
        requestQueue= Volley.newRequestQueue(getActivity());
        StringRequest request =new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            int count=0;
                            JSONArray json =new JSONArray(response);
                            JSONObject obj;
                            int size=json.length();
                            array_anchor_adapter.clear();
                            array_anchor_list.add(0,"No anchor selected");
                            arr_anchor=new String[size][3];
                            int index=0;
                            for(int i=0;i<json.length();i++){
                                obj=json.getJSONObject(i);
                                if(obj.getString("anchor_id").equals("empty")){
                                    count = i;
                                }
                                else {
                                    arr_anchor[index][0] = obj.getString("anchor_id");
                                    arr_anchor[index][1] = obj.getString("anchor_name");
                                    arr_anchor[index][2] = obj.getString("anchor_desc");
                                    array_anchor_list.add(arr_anchor[index][1]);
                                    array_anchor_adapter.notifyDataSetChanged();
                                    index++;
                                }
                            }
                            assignAnchor(count);
                        }catch(JSONException e){
                            Toast.makeText(getActivity(),"No valid anchor records..",Toast.LENGTH_SHORT).show();
                        }
                        catch(Exception e){
                            Toast.makeText(getActivity(),"An error has occured : "+e,Toast.LENGTH_SHORT).show();
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
                param.put("audio_id",prog_code);
                return param;
            }
        };
        requestQueue.add(request);
    }

    private void update_edit_guest(final String prog_code) {
        gid = new String[2];
        gid[0] = new String();
        gid[1] = new String();
        String url="https://jimsd.org/jimsradio/fetch_prog_guest.php?";
        requestQueue= Volley.newRequestQueue(getActivity());
        StringRequest request =new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            int count=0;
                            JSONArray json =new JSONArray(response);
                            JSONObject obj;
                            int size=json.length();
                            array_guest_adapter.clear();
                            array_guest_list.add(0,"No guest selected");
                            arr_guest=new String[size][3];
                            int index=0;
                            for(int i=0;i<json.length();i++){
                                obj=json.getJSONObject(i);
                                if(obj.getString("guest_id").equals("empty")){
                                    count = i;
                                }
                                else {
                                    arr_guest[index][0] = obj.getString("guest_id");
                                    arr_guest[index][1] = obj.getString("guest_name");
                                    arr_guest[index][2] = obj.getString("guest_desc");
                                    array_guest_list.add(arr_guest[index][1]);
                                    array_guest_adapter.notifyDataSetChanged();
                                    index++;
                                }
                            }
                            assignGuest(count);
                            update_edit_anchor(prog_code_edit);
                        }catch(JSONException e) {
                            Toast.makeText(getActivity(), "No valid guest records..", Toast.LENGTH_SHORT).show();
                            update_edit_anchor(prog_code_edit);
                        }catch(Exception e){
                            Toast.makeText(getActivity(),"An error has occured : "+e,Toast.LENGTH_SHORT).show();
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
                param.put("audio_id",prog_code);
                return param;
            }
        };
        requestQueue.add(request);
    }


    private void assignAnchor(int count){
        Spinner sp_select[]=new Spinner[2];
        sp_select[0]=spedit_select_a1;
        sp_select[1]=spedit_select_a2;
        sp_select[0].setSelection(0);
        sp_select[1].setSelection(0);
        for(int i=0;i<count;i++){
            sp_select[i].setSelection(i+1);
        }
        if(count == 2){
            layoutedit_anchor2.setVisibility(View.VISIBLE);
            btnedit_add_anchor.setVisibility(View.GONE);
        }
        else{
            layoutedit_anchor2.setVisibility(View.GONE);
            btnedit_add_anchor.setVisibility(View.VISIBLE);
        }
    }

    private void assignGuest(int count){
        Spinner sp_select[]=new Spinner[2];
        sp_select[0]=spedit_select_g1;
        sp_select[1]=spedit_select_g2;
        sp_select[0].setSelection(0);
        sp_select[1].setSelection(0);
        for(int i=0;i<count;i++){
            sp_select[i].setSelection(i+1);
        }
        if(count == 2){
            layoutedit_guest2.setVisibility(View.VISIBLE);
            btnedit_add_guest.setVisibility(View.GONE);
        }
        else{
            layoutedit_guest2.setVisibility(View.GONE);
            btnedit_add_guest.setVisibility(View.VISIBLE);
        }
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
                            layout_edit_program.setVisibility(View.VISIBLE);
                        }catch(JSONException e){
                            prog_progbar.setVisibility(View.GONE);
                            Toast.makeText(getActivity(),"No shows found..",Toast.LENGTH_SHORT).show();
                        }catch(Exception e){
                            prog_progbar.setVisibility(View.GONE);
                            Toast.makeText(getActivity(),"An Error has Occurred : "+e,Toast.LENGTH_SHORT).show();
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
                                    display_edit_values_prog(0);
                        }catch(JSONException e){
                            Toast.makeText(getActivity(),"No programs found..",Toast.LENGTH_SHORT).show();
                        }catch(Exception e){
                            Toast.makeText(getActivity(),"An error has occured : "+e,Toast.LENGTH_SHORT).show();
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

    private void updateProgram(final String audio_id) {
        final String prog_name = edtedit_prog_name.getText().toString().trim();
        final String prog_desc = edtedit_prog_desc.getText().toString().trim();
        final String prog_link = edtedit_prog_link.getText().toString().trim();
        if(prog_name.equals("")||prog_desc.equals("")||prog_link.equals("")){
            Toast.makeText(getActivity(), "Program fields cannot be empty..", Toast.LENGTH_SHORT).show();
            return;
        }

        final String add_date=edtedit_date.getText().toString().trim();
        if(add_date.equals("")) {
            Toast.makeText(getActivity(), "Date cannot be empty..", Toast.LENGTH_SHORT).show();
            return;
        }

        //guest checks
        String guestmsg="";
        if(gid[0].equals("empty"))
            guestmsg="Guest 1 empty..";
        else
            guestmsg="Guest 1 not empty..";

        if(gid[1].equals("empty")){
            guestmsg+="\nGuest 2 empty..";
            Toast.makeText(getActivity(), guestmsg, Toast.LENGTH_SHORT).show();
        }
        else{
            guestmsg+="\nGuest 2 not empty..";
            Toast.makeText(getActivity(), guestmsg, Toast.LENGTH_SHORT).show();
        }

        //anchor checks
        String anchormsg="";
        if(aid[0].equals("empty"))
            anchormsg="Anchor 1 empty..";
        else
            anchormsg="Anchor 1 not empty..";

        if(aid[1].equals("empty")){
            anchormsg+="\nAnchor 2 empty..";
            Toast.makeText(getActivity(), anchormsg, Toast.LENGTH_SHORT).show();
        }
        else{
            anchormsg+="\nAnchor 2 not empty..";
            Toast.makeText(getActivity(), anchormsg, Toast.LENGTH_SHORT).show();
        }

        final String url = "https://jimsd.org/jimsradio/edit_program.php?";
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
                        Toast.makeText(getActivity(),"An error has occured : "+error,Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("audio_id",audio_id);
                param.put("prog_name", prog_name);
                param.put("prog_date", add_date);
                param.put("prog_desc", prog_desc);
                param.put("show_code", show_code_edit_choose);
                param.put("prog_link",prog_link);

                //selecting which values to be passed
                param.put("g1_id",gid[0]);
                param.put("g2_id",gid[1]);
                param.put("a1_id",aid[0]);
                param.put("a2_id",aid[1]);
                return param;
            }
        };
        requestQueue.add(request);
    }

}
