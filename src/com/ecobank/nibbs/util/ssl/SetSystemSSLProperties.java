package com.ecobank.nibbs.util.ssl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Writer;
import java.net.Socket;
import java.net.URL;
import java.util.Properties;
import javax.net.SocketFactory;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import main.MainNonThread;

public class SetSystemSSLProperties
{
  private static SetSystemSSLProperties instance;
  MainNonThread MNT = new MainNonThread();
  private static Properties props = new Properties();
  
  static
  {
    HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier()
    {
      public boolean verify(String hostname, SSLSession sslSession)
      {
        return true;
      }
    });
  }
  
  public static SetSystemSSLProperties getInstance()
  {
    if (instance == null) {
      instance = new SetSystemSSLProperties();
    }
    return instance;
  }
  
  public void process()
  {
    try
    {
      System.out.println("1");
      URL trustStoreURL = SetSystemSSLProperties.class.getResource(MainNonThread.getTRUST_STORE_URL());
      System.out.println("2");
      System.out.println(MainNonThread.getTRUST_STORE_URL());
      System.out.println("3");
      
      URL keyStoreURL = SetSystemSSLProperties.class.getResource(MainNonThread.getKEY_STORE_URL());
      System.out.println("4");
      String trustStorePassword = MainNonThread.getTRUST_STORE_PASSWORD();
      System.out.println("5");
      String keyStorePassword = MainNonThread.getKEY_STORE_PASSWORD();
      System.out.println("6");
      boolean showDebugInfo = "Y".equalsIgnoreCase(MainNonThread.getSHOW_DEBUG_INFO());
      System.out.println("7");
      boolean useProxy = "Y".equalsIgnoreCase(MainNonThread.getUSE_PROXY());
      System.out.println("8");
      
      System.out.println("9");
      
      String trustStoreFilePath = "//usr//app//INTERFACE//ECIBNigeria//InterfaceService2//NIPS//nibbs.jks";
      String keyStoreFilePath = "//usr//app//INTERFACE//ECIBNigeria//InterfaceService2//NIPS//nibbs.jks";
      
      System.out.println("keyStoreFilePath:" + keyStoreFilePath);
      System.out.println("10");
      if (useProxy)
      {
        System.setProperty("http.proxyHost", MainNonThread.getPROXY_HOST());
        System.setProperty("http.proxyPort", MainNonThread.getPROXY_PORT());
      }
      if (showDebugInfo)
      {
        System.setProperty("javax.net.debug", "all");
        System.out.println("11");
      }
      System.setProperty("javax.net.ssl.trustStore", trustStoreFilePath);
      System.out.println("12");
      System.setProperty("javax.net.ssl.trustStorePassword", trustStorePassword);
      System.out.println("13");
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  
  public static void main(String[] args)
  {
    String TARGET_HTTPS_SERVER = "10.12.224.8";
    int TARGET_HTTPS_PORT = 8077;
    
    System.out.println("14");
    getInstance().process();
    try
    {
      System.out.println("15");
      Socket socket = SSLSocketFactory.getDefault().createSocket("10.12.224.8", 8077);
      
      System.out.println("16");
      Writer out = new OutputStreamWriter(socket.getOutputStream(), "ISO-8859-1");
      
      System.out.println("17");
      out.write("GET / HTTP/1.1\r\n");
      System.out.println("18");
      out.write("Host: 10.12.224.8:8077\r\n");
      
      out.write("Agent: SSL-TEST\r\n");
      out.write("\r\n");
      out.flush();
      System.out.println("19");
      BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "ISO-8859-1"));
      
      System.out.println("20");
      String line = null;
      while ((line = in.readLine()) != null)
      {
        System.out.println("21");
        System.out.println(line);
      }
      socket.close();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}
