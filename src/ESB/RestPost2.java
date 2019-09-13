/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ESB;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.Logger;

public class RestPost2 {

    static Logger logger;
    public static JsonObject post(String endpoint, String postData, String method, String protocol)
            throws MalformedURLException, IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
        
         logger = Logger.getLogger(RestPost2.class.getName());
        String resp = "";
        JsonObject result = null;

        HttpPost httpPost = new HttpPost(endpoint);

        CloseableHttpClient client = HttpClients.custom().
                setHostnameVerifier(new AllowAllHostnameVerifier()).
                setSslcontext(new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                    public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                        return true;
                    }
                }).build()).build();

        if ((protocol.equalsIgnoreCase("HTTPS") & method.contains("POST"))) {

            StringEntity entity = new StringEntity(postData);
            httpPost.setEntity(entity);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            CloseableHttpResponse response = client.execute(httpPost);

            HttpEntity responseEntity = response.getEntity();

            String res = EntityUtils.toString(responseEntity);
            
            System.out.println("************************************************************************************");
            System.out.println("** Response From GHIPSS-Gateway >> " + res);
            System.out.println("************************************************************************************");
            
            logger.info("*******************************************************************************************");
            logger.info("** Response From GHIPSS-Gateway >> " + res);
            logger.info("*******************************************************************************************");

            result = (JsonObject) new JsonParser().parse(res);

           

            client.close();

        }

        return result;

    }

}
