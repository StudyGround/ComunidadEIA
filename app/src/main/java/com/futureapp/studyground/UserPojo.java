package com.futureapp.studyground;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.List;

public class UserPojo {


    String programa,email,uid,name;
    List materias[];


    public UserPojo() { }

    public String getPrograma() {
        return programa;
    }

    public void setPrograma(String programa) {
        this.programa = programa;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List[] getMaterias() {
        return materias;
    }

    public void setMaterias(List[] materias) {
        this.materias = materias;
    }

    @Override
    public String toString() {
        return "UserPojo{" +
                "programa='" + programa + '\'' +
                ", email='" + email + '\'' +
                ", uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                ", materias=" + Arrays.toString(materias) +
                '}';
    }
}
