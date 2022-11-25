package com.binotify.services.subscription;

import com.binotify.Status;
import com.binotify.db.SQLi;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;

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
        @WebParam(name = "subscriber_id") int subscriber_id
    ) throws Exception {

        MessageContext mc = context.getMessageContext();
        HttpServletRequest req = (HttpServletRequest) mc.get(MessageContext.SERVLET_REQUEST);
        SQLi instance = SQLi.getInstance();
        instance.insertLog(req);

        try {
            String query = "INSERT INTO subscription VALUES(?, ?, \'PENDING\')";

            Connection conn = SQLi.getConn();
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, creator_id);
            statement.setInt(2, subscriber_id);

            statement.executeUpdate();
        } catch (SQLException e) {
            return e.getMessage();
        }

        return "Successfully added subscription";
    }
}
