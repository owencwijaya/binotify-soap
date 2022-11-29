package com.binotify.services.subscription;

import com.binotify.db.SQLi;
import com.binotify.models.Status;
import com.binotify.models.Subscription;
import com.binotify.wrapper.SubscriptionList;
import com.binotify.wrapper.SubscriptionListElmt;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.List;
import java.util.ArrayList;

import jakarta.annotation.Resource;
import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import jakarta.jws.WebParam;
import jakarta.jws.soap.SOAPBinding;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.xml.ws.WebServiceContext;
import jakarta.xml.ws.handler.MessageContext;
import jakarta.xml.ws.RequestWrapper;


@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public class GetSubs {

    @Resource
    private WebServiceContext context;

    @WebMethod
    @RequestWrapper(className = "com.binotify.wrapper.SubscriptionList",
    targetNamespace = "http://subscription.services.binotify.com/")
    public SubscriptionList getSubs(
        @WebParam(name = "api_key") String api_key,
        @WebParam(name = "user_id") String user_id,
        @WebParam(name = "page") String page,
        @WebParam(name = "limit") String limit
    ) throws Exception {
        MessageContext mc = context.getMessageContext();
        HttpServletRequest req = (HttpServletRequest) mc.get(MessageContext.SERVLET_REQUEST);
        
        SQLi instance = SQLi.getInstance();

        try{
            // masukin ke log
            instance.insertLog(req);
            
            // cek API key nya sesuai sama user_id ato ngga
            if (!instance.checkAPIKey(api_key, user_id)){
                throw new Exception("Invalid API key");
            }
        } catch (Exception e) {
            throw e;
        }

        
        try {
            Connection conn = SQLi.getConn();

            List<SubscriptionListElmt> subs = new ArrayList<SubscriptionListElmt>();
            
            String query = "SELECT * FROM subscription LIMIT " + limit + " OFFSET " + page;
            
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet res = statement.executeQuery();
            
            while (res.next()){
                String creator_id = res.getString("creator_id");
                int subscriber_id = res.getInt("subscriber_id");
                Status status = Status.valueOf(res.getString("status"));

                SubscriptionListElmt sub = new SubscriptionListElmt(
                    new Subscription(creator_id, subscriber_id, status)
                );
                System.out.println(sub);

                subs.add(sub);
            }

            SubscriptionList subList = new SubscriptionList(subs);
            return subList;
        } catch (SQLException e) {
            throw e;
        }
    }
}
