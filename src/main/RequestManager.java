package main;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Properties;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import jms.QueueUtility;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;
import requesthandlers.ArielProcessor;
import requesthandlers.ProcessorBase;
import utility.XMLRequestParser;
import utility.XPathReader;

class RequestManager
  extends Thread
{
  static Logger logger = Logger.getLogger(RequestManager.class);
  private static HashMap hashMap = null;
  private static String errMsg = "";
  private static ConfigConstants configConstants = null;
  
  public static String getErrMsg()
  {
    return errMsg;
  }
  
  public static void setErrMsg(String aErrMsg)
  {
    errMsg = aErrMsg;
  }
  
  public void run()
  {
    try
    {
      receiveMessage();
    }
    catch (Exception ex)
    {
      logger.error(ex);
    }
  }
  
  public RequestManager(ConfigConstants configCon)
  {
    configConstants = configCon;
  }
  
  public void receiveMessage()
    throws SAXException, IOException
  {
    String xmlread = "";
    byte[] msgArray = null;
    String reqCode = "";
    XPathReader xmlReader = null;
    try
    {
      msgArray = ConfigConstants.getXmlMsgReceiver().receiveMessage();
      if (msgArray.length > 0)
      {
        xmlReader = new XPathReader(msgArray);
        
        reqCode = xmlReader.getValue(ConfigConstants.getParamPropXMLPath().getProperty(ConfigConstants.getApplication()));
        String schemaFile = ConfigConstants.getXsddir() + "\\Request\\" + reqCode + "_RQ.xsd";
        xmlread = new String(msgArray);
        if (validateXMLRequest(xmlread, schemaFile, reqCode))
        {
          handleRequest(reqCode, ConfigConstants.getApplication(), msgArray);
          return;
        }
      }
    }
    catch (Exception ex)
    {
      logger.error(ex);
      setErrMsg(ex.getMessage());
      if (msgArray != null) {
        buildErrorMsg(ConfigConstants.getApplication() + " SCHEMA VALIDATION: " + getErrMsg(), hashMap, reqCode);
      }
    }
  }
  
  private String buildErrorMsg(String errCode, HashMap param, String request)
  {
    StringBuilder sb = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    sb.append("<kastelResponse><header>");
    sb.append("<msgType>" + (String)param.get("msgType") + "</msgType>");
    sb.append("<transCode>" + request + "</transCode>");
    sb.append("<transNumber>" + (String)param.get("transNumber") + "</transNumber>");
    sb.append("<originator>" + (String)param.get("originator") + "</originator>");
    sb.append("<receiver>" + (String)param.get("receiver") + "</receiver>");
    sb.append("<timestamp>" + (String)param.get("timestamp") + "</timestamp>");
    sb.append("<legacyCompanyCode>" + (String)param.get("legacyCompanyCode") + "</legacyCompanyCode>");
    sb.append("</header>");
    if (request.compareToIgnoreCase("BLKCUST") == 0) {
      sb.append("<blacklistCustomer><msgDesc>" + errCode + ": " + getErrMsg() + "</msgDesc></blacklistCustomer>");
    } else if (request.compareToIgnoreCase("CIFSRCH") == 0) {
      sb.append("<cifSearch><msgDesc>" + errCode + ": " + getErrMsg() + "</msgDesc></cifSearch>");
    } else if (request.compareToIgnoreCase("LNACCRT") == 0) {
      sb.append("<loanAccountSetup><msgDesc>" + errCode + ": " + getErrMsg() + "</msgDesc></loanAccountSetup>");
    } else if (request.compareToIgnoreCase("EXPDTLS") == 0) {
      sb.append("<exposureEnquiry><msgDesc>" + errCode + ": " + getErrMsg() + "</msgDesc></exposureEnquiry>");
    } else if (request.compareToIgnoreCase("DSBADV") == 0) {
      sb.append("<disbursementAdvice><msgDesc>" + errCode + ": " + getErrMsg() + "</msgDesc></disbursementAdvice>");
    } else if (request.compareToIgnoreCase("CIFUPD") == 0) {
      sb.append("<cifInsertUpdate><msgDesc>" + errCode + ": " + getErrMsg() + "</msgDesc></cifInsertUpdate>");
    } else if (request.compareToIgnoreCase("CIFINS") == 0) {
      sb.append("<cifInsertUpdate><msgDesc>" + errCode + ": " + getErrMsg() + "</msgDesc></cifInsertUpdate>");
    } else if (request.compareToIgnoreCase("CIFENQ") == 0) {
      sb.append("<cifEnquiry><msgDesc>" + errCode + ": " + getErrMsg() + "</msgDesc></cifEnquiry>");
    } else if (request.compareToIgnoreCase("CIFENQ") == 0) {
      sb.append("<cifEnquiry><msgDesc>" + errCode + ": " + getErrMsg() + "</msgDesc></cifEnquiry>");
    }
    sb.append("</kastelResponse>");
    return sb.toString();
  }
  
  private void processResponse(String requestCode, String xmlMsg, HashMap map)
  {
    String schemaFile = ConfigConstants.getXsddir() + "\\Response\\" + requestCode + "_RS.xsd";
    try
    {
      if (validateXMLRequest(xmlMsg, schemaFile, requestCode))
      {
        sendResponseMsg(xmlMsg.getBytes(), (String)map.get("transNumber"));
      }
      else
      {
        String err = buildErrorMsg(ConfigConstants.getApplication() + "RESPONSE SCHEMA VALIDATION ERROR", map, requestCode);
        sendErrorMsg(err.getBytes(), (String)map.get("transNumber"));
      }
    }
    catch (Exception ex)
    {
      logger.error(ex);
    }
  }
  
  public void handleRequest(String requestCode, String appCode, byte[] request)
  {
    String s = "";
    byte[] retByte = null;
    XMLRequestParser xmlParser = new XMLRequestParser(new ByteArrayInputStream(request));
    hashMap = xmlParser.getHashMapValue();
    ProcessorBase kasProc = null;
    if (appCode.compareToIgnoreCase("KASTEL") == 0)
    {
      kasProc = new ArielProcessor(requestCode, ConfigConstants.getOracleConnection());
      retByte = kasProc.getResponse(hashMap, requestCode);
    }
    else if (appCode.compareToIgnoreCase("ARIEL") == 0)
    {
      kasProc = new ArielProcessor(requestCode, ConfigConstants.getOracleConnection());
    }
    if ((retByte == null) && (kasProc.isBStatus()))
    {
      sendErrorMsg(kasProc.getErrMsg().getBytes(), (String)hashMap.get("transNumber"));
      
      return;
    }
    s = new String(retByte);
    processResponse(requestCode, s, hashMap);
  }
  
  private boolean validateXMLRequest(String xmlMsgReq, String xsdFile, String reqCode)
    throws SAXException, IOException
  {
    if (validateXML(xmlMsgReq, xsdFile, reqCode)) {
      return true;
    }
    return false;
  }
  
  public void sendResponseMsg(byte[] xmlMsg, String transNum)
  {
    try
    {
      ConfigConstants.getXmlMsgSender().sendMessage(xmlMsg, transNum);
      ConfigConstants.getXmlMsgSender().closeProducer();
    }
    catch (Exception ex)
    {
      logger.error(ex);
    }
  }
  
  public void sendErrorMsg(byte[] xmlMsg, String uniqueCode)
  {
    try
    {
      ConfigConstants.getXmlMsgError().sendMessage(xmlMsg, uniqueCode);
    }
    catch (Exception ex)
    {
      logger.error(ex);
    }
  }
  
  private boolean validateXML(String xml, String xsd, String requestCode)
    throws SAXException, IOException
  {
    SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
    File schemaLocation = new File(xsd);
    Schema schema = factory.newSchema(schemaLocation);
    Validator validator = schema.newValidator();
    Source source = new StreamSource(new StringReader(xml));
    try
    {
      validator.validate(source);
      return true;
    }
    catch (SAXException ex)
    {
      logger.error(requestCode + ":" + ex);
      setErrMsg(ex.getMessage());
    }
    return false;
  }
}
