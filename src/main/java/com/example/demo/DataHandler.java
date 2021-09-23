package com.example.demo;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class DataHandler {
    Map<String, Data> dataMap;

    public DataHandler() {
        this.dataMap = new HashMap<>();
    }

    public String getData(String key) {
        dataMap.get(key).reschedule();
        return dataMap.get(key).content;
    }

    public void setData(String key, String content) {
        dataMap.put(key, new Data(key, content, this));
    }

    public void setData(String key, String content, long ttl) {
        dataMap.put(key, new Data(key, content, ttl, this));
    }

    public void remove(String key) {
        dataMap.remove(key);
    }

    public boolean dump() throws IOException {
        File file = new File("filename.txt");
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(dataMap);
        oos.close();
        fos.close();
        return true;
    }

    public void load() throws IOException, ClassNotFoundException {
        File file = new File("filename.txt");
        FileInputStream fis = new FileInputStream(file);
        ObjectInputStream ois = new ObjectInputStream(fis);
        dataMap = (HashMap<String, Data>) ois.readObject();
        for (var entry : dataMap.entrySet()) {
            Data entryData = entry.getValue();
            entryData.dataHandler = this;
            entryData.reschedule();
        }
        ois.close();
        fis.close();
    }
}
