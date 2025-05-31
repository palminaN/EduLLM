package com.example.edullm.Models;

public class User {

    public int id;
    public String email;
    public int parent_id;
    public boolean is_parent;


    public User(int id, boolean is_parent, String email, int parent_id) {
        this.id = id;
        this.is_parent = is_parent;
        this.email = email;
        this.parent_id = parent_id;
    }


    public int getId(){
    return id;}

    public boolean getIs_parent() {
        return is_parent;
    }

    public String getEmail() {
        return email;
    }

    public int getParent_id() {
        return parent_id;
    }
}
