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
public class CheckUserSubbed {
    @Resource
    private WebServiceContext context;

    @WebMethod
    public Boolean checkUserSubbed(
        @WebParam(name = "creator_id") String creator_id,
        @WebParam(name = "subscriber_id") int subscriber_id,
        @WebParam(name = "api_key") String api_key
    ) throws Exception{   
        MessageContext mc = context.getMessageContext();
        HttpServletRequest req = (HttpServletRequest) mc.get(MessageContext.SERVLET_REQUEST);
        
        SQLi instance = SQLi.getInstance();

        try{
            // masukin ke log
            instance.insertLog(req);

            String restAPIKey = System.getenv("REST_API_KEY");
            Boolean isFromREST = api_key.equals(restAPIKey);


            // karena ini hanya untuk REST, jadi kalau bukan dari REST, langsung throw exception
            if (!isFromREST){
                throw new Exception("API key is invalid");
            }
        
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }

        try {
            Connection conn = SQLi.getConn();
            String query = "SELECT status FROM subscription WHERE creator_id = ? AND subscriber_id = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, creator_id);
            statement.setInt(2, subscriber_id);

            ResultSet res = statement.executeQuery();

            String status = "";
            while (res.next()){
                status = res.getString("status");
            }


            if (status.equals("ACCEPTED")){
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
