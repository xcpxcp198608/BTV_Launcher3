package com.px.common.http.configuration;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Parameters {
    public Map<String ,String> stringMap = new ConcurrentHashMap<>();
    public Map<String ,File> fileMap = new ConcurrentHashMap<>();
    public Map<String ,Object> objectMap = new ConcurrentHashMap<>();

    public Parameters() {

    }

    public Parameters(String key ,String value) {
        put(key ,value);
    }

    public Parameters(String key , File value){
        put(key ,value);
    }

    public Parameters(String key , Object value){
        put(key ,value);
    }

    public void put (String key ,String value){
        stringMap.put(key ,value);
    }

    public void put (String key ,File value){
        fileMap.put(key ,value);
    }

    public void put (String key ,Object value){
        objectMap.put(key ,value);
    }

    public String getStringValue (String key){
        return stringMap.get(key);
    }


    public File getFileValue (String key){
        return fileMap.get(key);
    }

    public Object getObjectValue (String key){
        return objectMap.get(key);
    }

}
