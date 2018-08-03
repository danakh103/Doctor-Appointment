package com.medi360.utils;

/**
 * Created by TPS5 on 15-Apr-16.
 */

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
//import org.apache.http.client.ClientProtocolException;

/**
 * Created by Sourabh on 7-8-15.
 */
public class WebService {


    private static String NAMESPACE = "https://tempuri.org/";
    // private static String URL = "http://ec2-52-35-50-68.us-west-2.compute.amazonaws.com:4500/webservices/userlogin.asmx";
    //private static String URL ="http://192.168.1.6:4300/WebServices/Hospitalservices.asmx";
 //Test
       private static String TEST_URL ="https://ec2-52-66-69-18.ap-south-1.compute.amazonaws.com:4500/webservices/Hospitalservices.asmx";
// Live
private static String LIVE_URL ="https://www.medi360.in/WebServices/Hospitalservices.asmx";
    //private static String URL ="http://ec2-52-66-69-18.ap-south-1.compute.amazonaws.com/WebServices/Hospitalservices.asmx";


    private static String SOAP_ACTION = "https://tempuri.org/";

    public static String invokeWS(String jsonObjSend, String webMethName) {
        String resTxt = null;
       // System.out.println(" url is : "+URL);

        // Create request
        SoapObject request = new SoapObject(NAMESPACE, webMethName);
        // Property which holds input parameters
        PropertyInfo jsonObj = new PropertyInfo();
        // Set Name
        jsonObj.setName("jsdata");
        // Set Value
        jsonObj.setValue(jsonObjSend.toString());
        // Set dataType
        // jsonObj.setType(JSONObject.class);
        jsonObj.setType(String.class);
        // Add the property to request object
        request.addProperty(jsonObj);


        // Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        new MarshalBase64().register(envelope);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        allowAllSSL();
        HttpTransportSE androidHttpTransport;
        if(Configuration.server.equals("LIVE")) {
            androidHttpTransport = new HttpTransportSE(LIVE_URL);
            System.out.println("Ganesh, live url is being called: " + LIVE_URL);

        } else {
            androidHttpTransport = new HttpTransportSE(TEST_URL);
            System.out.println("Ganesh, TEST url is being called: " + TEST_URL);
        }
        try {
            // Invole web service
            androidHttpTransport.call(SOAP_ACTION+webMethName, envelope);
            // Get the response

            SoapPrimitive  response = (SoapPrimitive ) envelope.getResponse();
            // Assign it to fahren static variable
            resTxt = response.toString();

            System.out.println("Response is: " + resTxt);

        } catch (Exception e) {
            e.printStackTrace();
            resTxt = "Null";
        }

        return resTxt;
    }


    private static TrustManager[] trustManagers;

    public static class _FakeX509TrustManager implements
            javax.net.ssl.X509TrustManager {
        private static final X509Certificate[] _AcceptedIssuers = new X509Certificate[] {};

        public void checkClientTrusted(X509Certificate[] arg0, String arg1)
                throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] arg0, String arg1)
                throws CertificateException {
        }

        public boolean isClientTrusted(X509Certificate[] chain) {
            return (true);
        }

        public boolean isServerTrusted(X509Certificate[] chain) {
            return (true);
        }

        public X509Certificate[] getAcceptedIssuers() {
            return (_AcceptedIssuers);
        }
    }
    public static void allowAllSSL() {

        javax.net.ssl.HttpsURLConnection
                .setDefaultHostnameVerifier(new HostnameVerifier() {
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });

        javax.net.ssl.SSLContext context = null;

        if (trustManagers == null) {
            trustManagers = new javax.net.ssl.TrustManager[] { new _FakeX509TrustManager() };
        }

        try {
            context = javax.net.ssl.SSLContext.getInstance("TLS");
            context.init(null, trustManagers, new SecureRandom());
        } catch (NoSuchAlgorithmException e) {
            Log.e("allowAllSSL", e.toString());
        } catch (KeyManagementException e) {
            Log.e("allowAllSSL", e.toString());
        }
        javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(context
                .getSocketFactory());
    }
}

