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
        @WebParam(name = "page") String page,
        @WebParam(name = "limit") String limit
    ) throws Exception {
        MessageContext mc = context.getMessageContext();
        HttpServletRequest req = (HttpServletRequest) mc.get(MessageContext.SERVLET_REQUEST);
        
        SQLi instance = SQLi.getInstance();

        try{
            // masukin ke log
            instance.insertLog(req);
            System.out.println("Limit: "+limit);
            System.out.println("Page: "+ page);
            String restAPIKey = System.getenv("REST_API_KEY");
            String appAPIKey = System.getenv("APP_API_KEY");
            System.out.println("REST API key: " + restAPIKey);
            System.out.println("App API key: " + appAPIKey);

            Boolean isFromREST = api_key.equals(restAPIKey);
            Boolean isFromApp = api_key.equals(appAPIKey);

            // karena ini hanya untuk REST, jadi kalau bukan dari REST, langsung throw exception
            if (!isFromREST){
                throw new Exception("API key is invalid");
            }

        } catch (Exception e) {
            throw e;
        }

        
        try {
            Connection conn = SQLi.getConn();

            List<SubscriptionListElmt> subs = new ArrayList<SubscriptionListElmt>();
            
            String query = "SELECT * FROM subscription WHERE status = 'PENDING' LIMIT " + limit + ", " + page;
            System.out.println(query);
            
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
