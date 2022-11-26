package com.binotify.wrapper;

import jakarta.xml.bind.annotation.*;

import java.util.List;
import java.util.ArrayList;

@XmlRootElement(name = "subscription-list")
@XmlSeeAlso({ SubscriptionListElmt.class })
public class SubscriptionList {
    private List<SubscriptionListElmt> subscriptions;

    public SubscriptionList(){
        subscriptions = new ArrayList<>();
    }

    public SubscriptionList(List<SubscriptionListElmt> subscriptions){
        this.subscriptions = subscriptions;
    }

    @XmlElementWrapper(name = "subscription-lists")
    @XmlElement(name = "subscription")
    public List<SubscriptionListElmt> getList(){
        return subscriptions;
    }
}
