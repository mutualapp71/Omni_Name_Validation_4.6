/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ESB;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

/**
 *
 * @author PAGBEBLEWU
 */
public class RestPost {

    public static String post(String endpoint, String postData, String method, String protocol) throws MalformedURLException, IOException, KeyManagementException, NoSuchAlgorithmException {

        String resp = "";

        if (protocol.equalsIgnoreCase("HTTPS") & method.contains("POST")) {

            sslConfig();

            URL url = new URL(endpoint);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod(method);
            conn.setRequestProperty("Content-Type", "application/xml");
            conn.setRequestProperty("charset", "utf-8");

            conn.setUseCaches(false);

            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.write(postData.getBytes());
//            try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
//                wr.write(postData.getBytes());
//            }

            Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            for (int c; (c = in.read()) >= 0;) {

                resp += String.valueOf((char) c);
            }

            System.out.println("Response >>> " + resp);

        }

        //HTTP
        if (protocol.equalsIgnoreCase("HTTP") & method.contains("POST")) {

            URL url = new URL(endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod(method);
            conn.setRequestProperty("Content-Type", "application/xml");
            conn.setRequestProperty("charset", "utf-8");

            conn.setUseCaches(false);

            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.write(postData.getBytes());
//            try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
//                wr.write(postData.getBytes());
//            }

            Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            for (int c; (c = in.read()) >= 0;) {

                resp += String.valueOf((char) c);
            }

            System.out.println("Response >>> " + resp);
        }

        return resp;
    }

    public static void sslConfig() throws KeyManagementException, NoSuchAlgorithmException {

        TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }

            }
        };

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

    }

}
