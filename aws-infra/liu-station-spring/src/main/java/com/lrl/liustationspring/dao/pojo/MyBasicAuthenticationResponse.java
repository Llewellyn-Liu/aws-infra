package com.lrl.liustationspring.dao.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MyBasicAuthenticationResponse {

    @JsonProperty
    private String response;

    private String info;

    public MyBasicAuthenticationResponse(String response, String info) {
        this.response = response;
        this.info = info;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "MyBasicAuthenticationResponse{" +
                "response='" + response + '\'' +
                ", info='" + info + '\'' +
                '}';
    }
}
