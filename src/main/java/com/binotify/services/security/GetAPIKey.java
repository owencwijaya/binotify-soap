package com.binotify.services.security;

import jakarta.annotation.Resource;
import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
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
    public String getAPIKey(String username) throws Exception {
        // MessageContext mc = context.getMessageContext();
        return "fortinaiti ila babaji" + username;
    }
    
}
