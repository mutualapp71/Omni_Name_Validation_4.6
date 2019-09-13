package utility;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.StringReader;
import java.util.Hashtable;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class XPathReader
{
  InputSource inputSource = null;
  XPathFactory factory = null;
  XPath xPath = null;
  Document doc;
  static Logger logger = Logger.getLogger(XPathReader.class);
  private String xmlFileName;
  
  public XPathReader(FileInputStream requestStream)
  {
    try
    {
      factory = XPathFactory.newInstance();
      xPath = factory.newXPath();
      inputSource = new InputSource(requestStream);
    }
    catch (Exception e)
    {
      logger.error(e);
    }
  }
  
  public XPathReader(String xmlDocument)
  {
    try
    {
      factory = XPathFactory.newInstance();
      xPath = factory.newXPath();
      ByteArrayInputStream bis = new ByteArrayInputStream(xmlDocument.getBytes());
      DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
      doc = docBuilder.parse(bis);
      inputSource = new InputSource(bis);
    }
    catch (Exception e)
    {
      logger.error(e);
    }
  }
  
  public XPathReader(byte[] xmlBytes)
  {
    try
    {
      factory = XPathFactory.newInstance();
      xPath = factory.newXPath();
      inputSource = new InputSource(new StringReader(new String(xmlBytes)));
    }
    catch (Exception e)
    {
      logger.error(e);
    }
  }
  
  public XPathReader()
  {
    try
    {
      factory = XPathFactory.newInstance();
      xPath = factory.newXPath();
    }
    catch (Exception ex)
    {
      logger.error(ex);
    }
  }
  
  public Hashtable getValueAsArrayList(String xmlExpression)
  {
    Hashtable retValue = new Hashtable();
    int t = 0;
    try
    {
      if (inputSource == null) {
        inputSource = new InputSource(new FileInputStream(xmlFileName));
      }
      NodeList nodeList = (NodeList)doc.getElementsByTagName(xmlExpression).item(0);
      for (int i = 0; i <= nodeList.getLength() - 1; i++)
      {
        Object o = nodeList.item(i);
        retValue.put(nodeList.item(i).getNodeName(), nodeList.item(i).getFirstChild().getNodeValue());
      }
    }
    catch (Exception e)
    {
      logger.error(e);
    }
    return retValue;
  }
  
  public String getValue(String xmlExpression, byte[] xmlBytes)
  {
    String retValue = "";
    try
    {
      if (inputSource == null) {
        inputSource = new InputSource(new StringReader(new String(xmlBytes)));
      }
      retValue = xPath.evaluate(xmlExpression, inputSource);
      inputSource = null;
    }
    catch (Exception e)
    {
      logger.error(e.getMessage());
    }
    return retValue;
  }
  
  public String getValue(String xmlExpression)
  {
    String retValue = "";
    try
    {
      if (inputSource == null) {
        inputSource = new InputSource(new FileInputStream(xmlFileName));
      }
      retValue = xPath.evaluate(xmlExpression, inputSource);
      inputSource = null;
    }
    catch (Exception e)
    {
      logger.error(e.getMessage());
    }
    return retValue;
  }
  
  public String getXmlFileName()
  {
    return xmlFileName;
  }
  
  public void setXmlFileName(String xmlFileName)
  {
    this.xmlFileName = xmlFileName;
  }
}
