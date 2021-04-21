package com.example.firebasecrud;

public class User {

    private String key;
    private String user_name;
    private String user_age;
    private String phone_number;
    private String id;


    public User() {

    }

    public User(String namaBarang, String merkBarang, String hargaBarang, String user_id) {
        user_name = namaBarang;
        user_age = merkBarang;
        phone_number = hargaBarang;
        id = user_id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_age() {
        return user_age;
    }

    public void setUser_age(String user_age) {
        this.user_age = user_age;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}