package com.medi360.utils;

import org.ksoap2.transport.HttpTransportSE;

/**
 * Created by jegan on 3/26/2016.
 */
public class Config {
    public static String BASE_URI;
   // public static String BASE_URI = "https://www.medi360.in/WebServices/";
  //  public static String BASE_URI = "https://ec2-52-66-69-18.ap-south-1.compute.amazonaws.com:4500/webservices/";

    public static enum API{

        LOGIN ("Hospitalservices.asmx"),
        REGISTERML("api/addmldata/smldetails");

        private final String endApi;

        API(String endApi){
            this.endApi = endApi;
        }

        public String get(){
            if(Configuration.server.equals("LIVE")) {
                BASE_URI= "https://www.medi360.in/WebServices/";

            } else {
                BASE_URI= "https://ec2-52-66-69-18.ap-south-1.compute.amazonaws.com:4500/webservices/";
            }
            return BASE_URI + this.endApi;
        }

    }
}
