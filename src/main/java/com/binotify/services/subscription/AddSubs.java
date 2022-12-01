package com.binotify.services.subscription;

import com.binotify.db.SQLi;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.annotation.Resource;
import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import jakarta.jws.WebParam;
import jakarta.jws.soap.SOAPBinding;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.xml.ws.WebServiceContext;
import jakarta.xml.ws.handler.MessageContext;


@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public class AddSubs {

    @Resource
    private WebServiceContext context;

    @WebMethod
    public String addSubs(
        @WebParam(name = "creator_id") String creator_id,
        @WebParam(name = "subscriber_id") int subscriber_id,
        @WebParam(name = "api_key") String api_key
    ) throws Exception {

        MessageContext mc = context.getMessageContext();
        HttpServletRequest req = (HttpServletRequest) mc.get(MessageContext.SERVLET_REQUEST);
        SQLi instance = SQLi.getInstance();
        instance.insertLog(req);

        // cek API key nya valid ato ngga
        String restAPIKey = System.getenv("REST_API_KEY");
        String appAPIKey = System.getenv("APP_API_KEY");
        System.out.println("REST API key: " + restAPIKey);
        System.out.println("App API key: " + appAPIKey);

        Boolean isFromREST = api_key.equals(restAPIKey);
        Boolean isFromApp = api_key.equals(appAPIKey);

        // karena ini hanya untuk REST, jadi kalau bukan dari REST, langsung throw exception
        if (!isFromApp){
            throw new Exception("API key is invalid");
        }
    

        try {
            Connection conn = SQLi.getConn();

            String query = "SELECT COUNT(*) FROM subscription WHERE creator_id = ? AND subscriber_id = ? AND status != 'REJECTED'";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, creator_id);
            statement.setInt(2, subscriber_id);

            ResultSet res = statement.executeQuery();
            int count = 0;

            while (res.next()){
                count = res.getInt(1);
            }

            System.out.println(count);
            
            if (count > 0){
                return "User already sent a subscription";
            }


            query = "INSERT INTO subscription VALUES(?, ?, \'PENDING\')";
        
            
            statement = conn.prepareStatement(query);
            statement.setString(1, creator_id);
            statement.setInt(2, subscriber_id);

            statement.executeUpdate();
        } catch (SQLException e) {
            return e.getMessage();
        } catch (Exception e) {
            throw e;
        }

        return "Successfully added subscription";
    }
}
