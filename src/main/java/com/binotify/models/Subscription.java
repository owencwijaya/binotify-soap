package com.binotify.models;


public class Subscription {
    private String creator_id;
    private int subscriber_id;
    private Status status;
   
    public Subscription(){
    }

    public Subscription(String creator_id, int subscriber_id, Status status) {
        this.creator_id = creator_id;
        this.subscriber_id = subscriber_id;
        this.status = status;
    }

    public String getCreatorId() {
        return creator_id;
    }

    public int getSubscriberId(){
        return subscriber_id;
    }

    public Status getStatus(){
        return status;
    }
}
