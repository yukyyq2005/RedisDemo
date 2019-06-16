package com.kfit;

import java.io.Serializable;

//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.Id;

//@Entity
public class demoinfo implements Serializable {
    private static final long serialVersionUID = 1L;
  //  @Id
    //@GeneratedValue
    private int id;
    private String name;
    private String pwd;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    @Override
    public String toString() {
        return "DemoInfo [id=" + id + ", name=" + name + ", pwd=" + pwd + "]";
    }
}
