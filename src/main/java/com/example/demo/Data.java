package com.example.demo;

import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;

public class Data implements Serializable {
    private transient Timer timer;
    private transient DataHandler dataHandler;
    private String content;
    private String key;
    private long ttl;

    public Data(String key, String content, DataHandler dataHandler) {
        this.key = key;
        this.dataHandler = dataHandler;
        this.ttl = 5;//basic ttl is 5 seconds
        this.content = content;
        this.timer = new Timer();
        this.timer.schedule(new TimeToLive(), ttl * 1000);
    }

    public Data(String key, String content, long ttl, DataHandler dataHandler) {
        this.key = key;
        this.ttl = ttl;
        this.dataHandler = dataHandler;
        this.content = content;
        this.timer = new Timer();
        this.timer.schedule(new TimeToLive(), ttl * 1000);
    }

    public void setDataHandler(DataHandler dataHandler) {
        this.dataHandler = dataHandler;
    }

    //если кто-то получает доступ к записи то ее время жизни пролонгируется становится базовым
    public void reschedule() {
        if (this.timer != null) {
            this.timer.cancel();
        }
        this.timer = new Timer();
        this.timer.schedule(new TimeToLive(), ttl * 1000);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getTtl() {
        return ttl;
    }

    public void setTtl(long ttl) {
        this.ttl = ttl;
    }

    public void earlyExpire() {
        timer.cancel();
    }

    class TimeToLive extends TimerTask {
        @Override
        public void run() {
            dataHandler.remove(key);
            System.out.println("Element with key " + key + " expired");
            timer.cancel();
        }
    }
}
