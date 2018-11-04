package com.smartdevsolutions.ilottoandroid.Model;

/**
 * Created by teecodez on 10/25/2018.
 */

public class Configuration {
    private String Description;
    private String Key;
    private String Value;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    private int Id;

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }
}
