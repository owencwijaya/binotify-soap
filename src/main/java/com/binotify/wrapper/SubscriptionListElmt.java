package com.binotify.wrapper;

import com.binotify.models.*;

import jakarta.xml.bind.annotation.*;


@XmlRootElement(name = "subscription")
@XmlAccessorType(XmlAccessType.FIELD)
public class SubscriptionListElmt {
    @XmlElement(name = "creator-id", required = true)
    private String creator_id;

    @XmlElement(name = "subscriber-id", required = true)
    private int subscriber_id;

    @XmlElement(name = "status", required = true)
    private Status status;
   
    public SubscriptionListElmt(){
    }
    
    public SubscriptionListElmt(String creator_id, int subscriber_id, Status status) {
        this.creator_id = creator_id;
        this.subscriber_id = subscriber_id;
        this.status = status;
    }

    public SubscriptionListElmt(Subscription sub){
        this.creator_id = sub.getCreatorId();
        this.subscriber_id = sub.getSubscriberId();
        this.status = sub.getStatus();
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
