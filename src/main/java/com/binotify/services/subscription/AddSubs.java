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
        String appAPIKey = System.getenv("APP_API_KEY");
        Boolean isFromApp = api_key.equals(appAPIKey);

        // karena ini hanya untuk REST, jadi kalau bukan dari REST, langsung throw exception
        if (!isFromApp){
            return ("API key is invalid");
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

            
            if (count > 0){
                return "User already sent a subscription";
            }


            query = "INSERT INTO subscription VALUES(?, ?, \'PENDING\')";
        
            
            statement = conn.prepareStatement(query);
            statement.setString(1, creator_id);
            statement.setInt(2, subscriber_id);

            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return e.getMessage();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return e.getMessage();
        }

        return "Success";
    }
}
