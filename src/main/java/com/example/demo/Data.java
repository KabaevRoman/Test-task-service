package com.example.demo;

import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;

public class Data implements Serializable {

    transient DataHandler dataHandler;
    transient Timer timer;
    String content;
    String key;
    long ttl;

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

    //если кто-то получает доступ к записи то ее время жизни пролонгируется становится базовым
    public void reschedule() {
        if (this.timer != null) {
            this.timer.cancel();
        }
        this.timer = new Timer();
        this.timer.schedule(new TimeToLive(), ttl * 1000);
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
