package com.example.demo;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class DataHandlerService {
    Map<String, Data> dataMap;

    public DataHandlerService() {
        this.dataMap = new HashMap<>();
    }

    public Data getData(String key) {
        dataMap.get(key).reschedule();
        return dataMap.get(key);
    }

    public String setData(String key, String content) {
        if (dataMap.containsKey(key)) {
            this.remove(key);
        }
        dataMap.put(key, new Data(key, content, this));
        return "Data successfully added";
    }

    public String setData(String key, String content, long ttl) {
        if (dataMap.containsKey(key)) {
            this.remove(key);
        }
        dataMap.put(key, new Data(key, content, ttl, this));
        return "Data successfully added";
    }

    public Data remove(String key) {
        Data temp = dataMap.get(key);
        dataMap.get(key).earlyExpire();
        dataMap.remove(key);
        return temp;
    }

    public void dump() throws IOException {
        File file = new File("dump.txt");
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(dataMap);
        oos.close();
        fos.close();
    }

    public void load() throws IOException, ClassNotFoundException {
        File file = new File("dump.txt");
        FileInputStream fis = new FileInputStream(file);
        ObjectInputStream ois = new ObjectInputStream(fis);
        dataMap = (HashMap<String, Data>) ois.readObject();
        for (var entry : dataMap.entrySet()) {
            Data entryData = entry.getValue();
            entryData.setDataHandler(this);
            entryData.reschedule();
        }
        ois.close();
        fis.close();
    }
}
