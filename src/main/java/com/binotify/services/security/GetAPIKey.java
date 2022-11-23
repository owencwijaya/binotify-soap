package com.binotify.services.security;

import com.binotify.db.SQL;

import java.sql.SQLException;

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

    // MessageContext mc = context.getMessageContext();

        String token = "fortinaiti ila babaji " + user_id;
        SQL SQLInstance = new SQL();

        try{
            SQLInstance.Execute("INSERT INTO api_key VALUES(\"" + token + "\", " + user_id + ")");
        } catch (SQLException e){
            return e.getMessage();
        }

        return "fortinaiti ila babaji " + user_id;
    }
    
}
