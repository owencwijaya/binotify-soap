package com.binotify;

import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
 
@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC) 
public class TesServlet {
   public String ping() {
       return "pong";
   }
}

