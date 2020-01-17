package com.dark.pro.jimsradioadmin;

/**
 * Created by Chetan & Rahul on 21-Mar-18.
 */

public class ProgramHelper {
    public int audio_id;
    String prog_name;
    String show_code;
    String show_name;
    String prog_desc;
    String link;
    String prog_date;

    ProgramHelper(){
        this.audio_id=0;
        this.prog_name="";
        this.show_code="";
        this.show_name="";
        this.prog_desc="";
        this.link="";
        this.prog_date="";
    }
    public void  get_audio_id(int id) {
        this.audio_id = id;
    }
    public void get_prog_name(String name) {
        this.prog_name = name;
    }
    public void get_show_code(String scode) {
        this.show_code = scode;
    }
    public void get_show_name(String sname) {
        this.show_name = sname;
    }
    public void get_prog_desc(String desc) {
        this.prog_desc = desc;
    }
    public void get_prog_link(String link) {
        this.link = link;
    }
    public void get_prog_date(String date) {
        this.prog_date = date;
    }

    public int put_audio_id() {
        return this.audio_id;
    }
    public String put_prog_name() {
        return this.prog_name;
    }
    public String put_show_code() {
        return this.show_code;
    }
    public String put_show_name() {
        return this.show_name;
    }
    public String put_prog_desc() {
        return this.prog_desc;
    }
    public String put_prog_link() {
        return this.link;
    }
    public String put_prog_date() {
        return this.prog_date;
    }
}
