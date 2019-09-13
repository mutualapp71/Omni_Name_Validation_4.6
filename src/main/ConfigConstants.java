package main;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import jms.QueueUtility;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.pool.OracleDataSource;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import utility.XPathReader;

public class ConfigConstants
{
  private static String xmlMsg = "";
  private static String requestQueue = "";
  private static String replyQueue = "";
  private static String errorQueue = "";
  private static int portErr = 0;
  private static int portRequest = 0;
  private static int portResponse = 0;
  private static String server = "";
  private static String queManager = "";
  private static String uid = "";
  private static String sid = "";
  private static String xmlSchemaConfig = "";
  private static String dbServer = "";
  private static String channel = "";
  private static Logger logger = Logger.getLogger(Main.class);
  private static int timeInterval = 0;
  private static Properties paramProp = new Properties();
  private static Properties paramPropSchema = new Properties();
  private static Properties paramPropXMLPath = new Properties();
  private static Properties paramClassMapping = new Properties();
  private static QueueUtility xmlMsgReceiver = null;
  private static QueueUtility xmlMsgSender = null;
  private static QueueUtility xmlMsgError = null;
  private static String url = "jdbc:oracle:thin:@localhost:1521:orcl";
  private static String pwd = "";
  private static String schemaConfig = "";
  private static String application = "";
  private static String xmlPathConfig = "";
  private static String classmapping = "";
  private static String sampleXmlFolder = "";
  private static XPathReader xmlReader = null;
  private static String errMsg = "";
  private static QueueUtility xmlMsgSender1 = null;
  private static String xsddir = "";
  private static OracleDataSource ods = null;
  
  private static void xmlPathProp(String propFile)
  {
    File paramFile = new File(propFile);
    if (paramFile.exists()) {
      try
      {
        getParamPropXMLPath().load(new FileInputStream(paramFile));
      }
      catch (Exception ex)
      {
        getLogger().error(ex);
      }
    }
  }
  
  public static String getXmlMsg()
  {
    return xmlMsg;
  }
  
  public static void setXmlMsg(String aXmlMsg)
  {
    xmlMsg = aXmlMsg;
  }
  
  public static String getRequestQueue()
  {
    return requestQueue;
  }
  
  public static void setRequestQueue(String aRequestQueue)
  {
    requestQueue = aRequestQueue;
  }
  
  public static String getReplyQueue()
  {
    return replyQueue;
  }
  
  public static void setReplyQueue(String aReplyQueue)
  {
    replyQueue = aReplyQueue;
  }
  
  public static String getErrorQueue()
  {
    return errorQueue;
  }
  
  public static void setErrorQueue(String aErrorQueue)
  {
    errorQueue = aErrorQueue;
  }
  
  public static int getPortErr()
  {
    return portErr;
  }
  
  public static void setPortErr(int aPortErr)
  {
    portErr = aPortErr;
  }
  
  public static int getPortRequest()
  {
    return portRequest;
  }
  
  public static void setPortRequest(int aPortRequest)
  {
    portRequest = aPortRequest;
  }
  
  public static int getPortResponse()
  {
    return portResponse;
  }
  
  public static void setPortResponse(int aPortResponse)
  {
    portResponse = aPortResponse;
  }
  
  public static String getServer()
  {
    return server;
  }
  
  public static void setServer(String aServer)
  {
    server = aServer;
  }
  
  public static String getQueManager()
  {
    return queManager;
  }
  
  public static void setQueManager(String aQueManager)
  {
    queManager = aQueManager;
  }
  
  public static String getUid()
  {
    return uid;
  }
  
  public static void setUid(String aUid)
  {
    uid = aUid;
  }
  
  public static String getSid()
  {
    return sid;
  }
  
  public static void setSid(String aSid)
  {
    sid = aSid;
  }
  
  public static String getXmlSchemaConfig()
  {
    return xmlSchemaConfig;
  }
  
  public static void setXmlSchemaConfig(String aXmlSchemaConfig)
  {
    xmlSchemaConfig = aXmlSchemaConfig;
  }
  
  public static String getDbServer()
  {
    return dbServer;
  }
  
  public static void setDbServer(String aDbServer)
  {
    dbServer = aDbServer;
  }
  
  public static Logger getLogger()
  {
    return logger;
  }
  
  public static void setLogger(Logger aLogger)
  {
    logger = aLogger;
  }
  
  public static int getTimeInterval()
  {
    return timeInterval;
  }
  
  public static void setTimeInterval(int aTimeInterval)
  {
    timeInterval = aTimeInterval;
  }
  
  public static Properties getParamProp()
  {
    return paramProp;
  }
  
  public static void setParamProp(Properties aParamProp)
  {
    paramProp = aParamProp;
  }
  
  public static Properties getParamPropSchema()
  {
    return paramPropSchema;
  }
  
  public static void setParamPropSchema(Properties aParamPropSchema)
  {
    paramPropSchema = aParamPropSchema;
  }
  
  public static Properties getParamPropXMLPath()
  {
    return paramPropXMLPath;
  }
  
  public static void setParamPropXMLPath(Properties aParamPropXMLPath)
  {
    paramPropXMLPath = aParamPropXMLPath;
  }
  
  public static Properties getParamClassMapping()
  {
    return paramClassMapping;
  }
  
  public static void setParamClassMapping(Properties aParamClassMapping)
  {
    paramClassMapping = aParamClassMapping;
  }
  
  public static QueueUtility getXmlMsgReceiver()
  {
    return xmlMsgReceiver;
  }
  
  public static void setXmlMsgReceiver(QueueUtility aXmlMsgReceiver)
  {
    xmlMsgReceiver = aXmlMsgReceiver;
  }
  
  public static QueueUtility getXmlMsgSender()
  {
    return xmlMsgSender;
  }
  
  public static void setXmlMsgSender(QueueUtility aXmlMsgSender)
  {
    xmlMsgSender = aXmlMsgSender;
  }
  
  public static QueueUtility getXmlMsgError()
  {
    return xmlMsgError;
  }
  
  public static void setXmlMsgError(QueueUtility aXmlMsgError)
  {
    xmlMsgError = aXmlMsgError;
  }
  
  public static String getUrl()
  {
    return url;
  }
  
  public static void setUrl(String aUrl)
  {
    url = aUrl;
  }
  
  public static String getPwd()
  {
    return pwd;
  }
  
  public static void setPwd(String aPwd)
  {
    pwd = aPwd;
  }
  
  public static String getSchemaConfig()
  {
    return schemaConfig;
  }
  
  public static void setSchemaConfig(String aSchemaConfig)
  {
    schemaConfig = aSchemaConfig;
  }
  
  public static String getApplication()
  {
    return application;
  }
  
  public static void setApplication(String aApplication)
  {
    application = aApplication;
  }
  
  public static String getXmlPathConfig()
  {
    return xmlPathConfig;
  }
  
  public static void setXmlPathConfig(String aXmlPathConfig)
  {
    xmlPathConfig = aXmlPathConfig;
  }
  
  public static String getClassmapping()
  {
    return classmapping;
  }
  
  public static void setClassmapping(String aClassmapping)
  {
    classmapping = aClassmapping;
  }
  
  public static String getSampleXmlFolder()
  {
    return sampleXmlFolder;
  }
  
  public static void setSampleXmlFolder(String aSampleXmlFolder)
  {
    sampleXmlFolder = aSampleXmlFolder;
  }
  
  public static XPathReader getXmlReader()
  {
    return xmlReader;
  }
  
  public static void setXmlReader(XPathReader aXmlReader)
  {
    xmlReader = aXmlReader;
  }
  
  public static String getErrMsg()
  {
    return errMsg;
  }
  
  public static void setErrMsg(String aErrMsg)
  {
    errMsg = aErrMsg;
  }
  
  public static QueueUtility getXmlMsgSender1()
  {
    return xmlMsgSender1;
  }
  
  public static void setXmlMsgSender1(QueueUtility aXmlMsgSender1)
  {
    xmlMsgSender1 = aXmlMsgSender1;
  }
  
  public static String getXsddir()
  {
    return xsddir;
  }
  
  public static void setXsddir(String aXsddir)
  {
    xsddir = aXsddir;
  }
  
  public static String getChannel()
  {
    return channel;
  }
  
  public static void setChannel(String aChannel)
  {
    channel = aChannel;
  }
  
  public ConfigConstants(String propFile)
  {
    initParameter(propFile);
  }
  
  private static void xmlSchemaProp(String propFile)
  {
    File paramFile = new File(propFile);
    if (paramFile.exists()) {
      try
      {
        getParamPropSchema().load(new FileInputStream(paramFile));
      }
      catch (Exception ex)
      {
        getLogger().error(ex);
      }
    }
  }
  
  public static OracleConnection getOracleConnection()
  {
    OracleConnection conn = null;
    try
    {
      if (ods == null)
      {
        ods = new OracleDataSource();
        ods.setPassword(pwd);
        ods.setUser(uid);
        ods.setURL(url);
      }
      conn = (OracleConnection)ods.getConnection();
    }
    catch (Exception ex)
    {
      logger.error(ex);
    }
    return conn;
  }
  
  private static void initParameter(String propFile)
  {
    File paramFile = new File(propFile);
    if (paramFile.exists()) {
      try
      {
        getParamProp().load(new FileInputStream(paramFile));
      }
      catch (Exception ex)
      {
        getLogger().error(ex);
      }
    }
    try
    {
      setRequestQueue(getParamProp().getProperty("REQUESTQUEUE"));
      setReplyQueue(getParamProp().getProperty("REPLYQUEUE"));
      setErrorQueue(getParamProp().getProperty("ERRORQUEUE"));
      setPortErr(Integer.parseInt(getParamProp().getProperty("ERRPORT")));
      setPortRequest(Integer.parseInt(getParamProp().getProperty("ERRPORT")));
      setPortResponse(Integer.parseInt(getParamProp().getProperty("RESPORT")));
      setServer(getParamProp().getProperty("QUEUESERVER"));
      setQueManager(getParamProp().getProperty("QUEUEMANAGER"));
      setUid(getParamProp().getProperty("SCHEMA"));
      setPwd(getParamProp().getProperty("PASSWORD"));
      setSchemaConfig(getParamProp().getProperty("XMLSCHEMACONFIG"));
      setApplication(getParamProp().getProperty("APPLICATION"));
      setXmlPathConfig(getParamProp().getProperty("XMLPATHCONFIG"));
      setClassmapping(getParamProp().getProperty("CLASSMAPPING"));
      setDbServer(getParamProp().getProperty("DBSERVER"));
      setSid(getParamProp().getProperty("SID"));
      setChannel(getParamProp().getProperty("CHANNEL"));
      setSampleXmlFolder(getParamProp().getProperty("SAMPLEXML"));
      setXsddir(getParamProp().getProperty("XSDDIR"));
      setTimeInterval(Integer.parseInt(getParamProp().getProperty("TIMEINTERVAL")));
      PropertyConfigurator.configure(getParamProp().getProperty("LOG4JPROP"));
      xmlSchemaProp(getSchemaConfig());
      xmlPathProp(getXmlPathConfig());
      
      setUrl("jdbc:oracle:thin:@" + getDbServer() + ":1521:" + getSid());
      setXmlMsgReceiver(new QueueUtility(getRequestQueue(), "receive", getPortResponse(), getServer(), getQueManager(), getChannel()));
      setXmlMsgSender(new QueueUtility(getReplyQueue(), "send", getPortRequest(), getServer(), getQueManager(), getChannel()));
      setXmlMsgError(new QueueUtility(getErrorQueue(), "send", getPortErr(), getServer(), getQueManager(), getChannel()));
      setXmlMsgSender1(new QueueUtility(getRequestQueue(), "send", getPortResponse(), getServer(), getQueManager(), getChannel()));
    }
    catch (Exception ex)
    {
      getLogger().error(ex);
    }
  }
}
