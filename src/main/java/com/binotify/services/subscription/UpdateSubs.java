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
public class UpdateSubs {
    
    @Resource
    private WebServiceContext context;

    @WebMethod
    public String updateSubs(
        @WebParam(name = "api_key") String api_key,
        @WebParam(name = "user_id") String user_id,
        @WebParam(name = "creator_id") String creator_id,
        @WebParam(name = "subscriber_id") int subscriber_id,
        @WebParam(name = "new_status") String new_status
    ) throws Exception{     
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
            
            String query = "SELECT * FROM subscription WHERE creator_id = ? AND subscriber_id = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, creator_id);
            statement.setInt(2, subscriber_id);

            ResultSet res = statement.executeQuery();
            String status = "";

            while (res.next()){
                status = res.getString("status");
            }

            if (status != "PENDING"){
                return "Subscription is not on pending";
            }

            query = "UPDATE subscription SET status = ? WHERE creator_id = ? AND subscriber_id = ? AND status = 'PENDING'";
                        
            statement = conn.prepareStatement(query);
            statement.setString(1, new_status);
            statement.setString(2, creator_id);
            statement.setInt(3, subscriber_id);

            statement.executeUpdate();
        } catch (SQLException e) {
            return e.getMessage();
        }

        return "Successfully updated subscription";

        // habis ini kudu ngabarin si app dulu
    }
}
