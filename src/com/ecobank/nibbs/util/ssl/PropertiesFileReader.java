package com.ecobank.nibbs.util.ssl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Properties;

public class PropertiesFileReader
{
  private static Properties props;
  private static String applicationPropFile;
  
  private static void loadPropertyFile()
    throws IOException
  {
    props = new Properties();
    System.out.println("22");
    System.out.println("inside loadPropertyFile" + getApplicationPropFile());
    System.out.println("23");
    URL configFileURL = PropertiesFileReader.class.getResource(getApplicationPropFile());
    System.out.println("24");
    String filePath = URLDecoder.decode(configFileURL.getPath(), "UTF-8");
    System.out.println("25");
    FileInputStream configFile = new FileInputStream(filePath);
    System.out.println("26");
    props.load(configFile);
    System.out.println("27");
    configFile.close();
  }
  
  public static String getPropertyFilePath(String propFile)
    throws IOException
  {
    System.out.println("27-1");
    URL configFileURL = PropertiesFileReader.class.getResource(propFile);
    System.out.println("28");
    String filePath = URLDecoder.decode(configFileURL.getPath(), "UTF-8");
    System.out.println("29");
    return filePath;
  }
  
  public static Properties getProperties(String propFile)
    throws Exception
  {
    System.out.println("30");
    setApplicationPropFile(propFile);
    System.out.println("31");
    System.out.println("inside getProperties" + propFile);
    System.out.println("32");
    loadPropertyFile();
    System.out.println("33");
    return props;
  }
  
  public static URL getResourceURL(String file)
  {
    System.out.println("34");
    return PropertiesFileReader.class.getResource(file);
  }
  
  private static void setApplicationPropFile(String appPropFile)
  {
    System.out.println("35");
    applicationPropFile = appPropFile;
    System.out.println("36");
  }
  
  private static String getApplicationPropFile()
  {
    System.out.println("37");
    return applicationPropFile;
  }
}
