package com.futureapp.studyground;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.List;

public class UserPojo {


    String programa,email,uid,name,tutor,telefono,token;
    List materias[];


    public UserPojo() { }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

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

    public String getTutor() { return tutor; }

    public void setTutor(String tutor) { this.tutor = tutor; }


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
