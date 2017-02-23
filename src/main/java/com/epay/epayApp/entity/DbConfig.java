package com.epay.epayApp.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;


@Entity
public class DbConfig implements Serializable {

    @Id
    private String propertyName;

    private String propertyValue;

    private Date createdTime;

    private Date updatedTime;

    @PrePersist
    public void persistDate(){
        createdTime=new Date();
        updatedTime=new Date();
    }

    @PreUpdate
    public void update(){
        updatedTime=new Date();
    }


    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
    }
}
