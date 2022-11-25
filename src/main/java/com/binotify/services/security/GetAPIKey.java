package com.binotify.services.security;

import com.binotify.db.SQLi;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.Timestamp;

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
public class GetAPIKey {

    @Resource
    private WebServiceContext context;

    @WebMethod
    public String getAPIKey(
        @WebParam(name = "user_id") int user_id
    ) throws Exception {

        MessageContext mc = context.getMessageContext();
        System.out.println(mc.get(MessageContext.HTTP_REQUEST_HEADERS));
        HttpServletRequest req = (HttpServletRequest) mc.get(MessageContext.SERVLET_REQUEST);
        
        SQLi instance = SQLi.getInstance();
        instance.insertLog(req);

        String token = "fortinaiti ila babaji " + user_id;

        try{
            String query = "INSERT INTO api_key VALUES(?, ?)";
            
            Connection conn = SQLi.getConn();
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, token);
            statement.setInt(2, user_id);
            
            statement.executeUpdate();
        } catch (SQLException e){
            return e.getMessage();
        }

        return "fortinaiti ila babaji " + user_id;
    }
    
}
