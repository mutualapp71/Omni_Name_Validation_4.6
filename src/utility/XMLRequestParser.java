package utility;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Vector;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLRequestParser
{
  private HashMap hashMapValue = new HashMap();
  private HashMap hashMapType = new HashMap();
  static Logger logger = Logger.getLogger(XMLRequestParser.class);
  
  public XMLRequestParser(ByteArrayInputStream xmlValue)
  {
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    try
    {
      DocumentBuilder db = dbf.newDocumentBuilder();
      Document doc = db.parse(xmlValue);
      for (int i = 0; i <= doc.getChildNodes().getLength() - 1; i++)
      {
        Node n = doc.getChildNodes().item(i);
        getNodeValue(n);
      }
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      logger.error(ex);
    }
  }
  
  public XMLRequestParser(File xmlValue)
  {
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    try
    {
      DocumentBuilder db = dbf.newDocumentBuilder();
      Document doc = db.parse(xmlValue);
      for (int i = 0; i <= doc.getChildNodes().getLength() - 1; i++)
      {
        Node n = doc.getChildNodes().item(i);
        
        getNodeValue(n);
      }
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }
  
  public XMLRequestParser(File inFile, String functionName)
  {
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    try
    {
      DocumentBuilder db = dbf.newDocumentBuilder();
      Document doc = db.parse(inFile);
      Node n = doc.getElementsByTagName(functionName).item(0);
      getAttributes(n);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }
  
  public static void main(String[] args)
    throws SAXException, IOException
  {
    File file = new File("X:\\INT2.xml");
    System.out.println(" file " + file);
    XMLRequestParser xrp = new XMLRequestParser(file);
    
    HashMap hm = xrp.getHashMapValue();
    System.out.println(hm);
  }
  
  public HashMap getAttributes(Node node)
  {
    HashMap hm = new HashMap();
    if (node.hasAttributes()) {
      node.getAttributes().getNamedItem("name");
    }
    return hm;
  }
  
  private void getNodeValue(Node n)
  {
    HashMap hm = new HashMap();
    if (n.hasChildNodes()) {
      for (int i = 0; i <= n.getChildNodes().getLength() - 1; i++)
      {
        Node node = n.getChildNodes().item(i);
        if ((node.hasChildNodes()) && (node.getChildNodes().getLength() > 1))
        {
          HashMap h = new HashMap();
          h = getNodeChildrenValues(node);
          
          hm.put(node.getNodeName(), h);
        }
        else if ((node.hasChildNodes()) && (node.getChildNodes().getLength() == 1))
        {
          HashMap h = new HashMap();
          HashMap h1 = new HashMap();
          
          h = getNodeChildrenValues(node);
          if (h.isEmpty())
          {
            hm.put(node.getNodeName(), getNodeValueHM(node));
          }
          else
          {
            Node node1 = node.getChildNodes().item(0);
            h1 = getNodeChildrenValues(node1);
            if (h.isEmpty()) {
              hm.put(node1.getNodeName(), getNodeValueHM(node1));
            }
            h.put(node1.getNodeName(), h1);
            hm.put(node.getNodeName(), h);
          }
        }
        else if (node.hasChildNodes())
        {
          HashMap h = new HashMap();
          h = getNodeChildrenValues(node);
          if (h.isEmpty()) {
            hm.put(node.getNodeName(), getNodeValueHM(node));
          } else {
            hm.put(node.getNodeName(), h);
          }
        }
        else if (node.getNodeName().compareTo("#text") != 0)
        {
          hm.put(node.getNodeName(), getNodeValueHM(node));
        }
      }
    } else if ((n.getNodeValue() != null) && (n.getNodeValue().trim().length() > 0)) {
      hm.put(n.getParentNode().getNodeName(), n.getNodeValue());
    } else if ((n.getNodeValue() != null) && (n.getNodeValue().trim().length() == 0)) {
      hm.put(n.getParentNode().getNodeName(), "");
    }
    setHashMapValue(hm);
  }
  
  private String getNodeValueHM(Node n)
  {
    String hm = "";
    if ((n.hasChildNodes()) && (n.getChildNodes().getLength() == 1))
    {
      Node node = n.getChildNodes().item(0);
      return getNodeValueHM(node);
    }
    if ((n.getNodeValue() != null) && (n.getNodeValue().trim().length() > 0)) {
      hm = n.getNodeValue();
    } else if (n.getNodeValue() == null) {
      hm = "";
    } else if ((n.getNodeValue() != null) && (n.getNodeValue().trim().length() == 0)) {
      hm = "";
    }
    return hm;
  }
  
  private HashMap getNodeChildrenValues(Node n)
  {
    Vector<HashMap> v = new Vector();
    HashMap contentMap = new HashMap();
    for (int i = 0; i <= n.getChildNodes().getLength(); i++)
    {
      Node node = n.getChildNodes().item(i);
      if (node != null) {
        if ((node.hasChildNodes()) && (node.getChildNodes().getLength() > 1))
        {
          HashMap child = new HashMap();
          child = getNodeChildrenValues(node);
          if (contentMap.containsKey(node.getNodeName()))
          {
            v.add(child);
            contentMap.put(node.getNodeName(), v);
          }
          else
          {
            v = new Vector();
            v.add(child);
            contentMap.put(node.getNodeName(), v);
          }
        }
        else if ((node.hasChildNodes()) && (node.getNextSibling() == null))
        {
          contentMap.put(node.getNodeName(), getNodeValueHM(node));
        }
        else if ((node.hasChildNodes()) && (node.getNextSibling().getNodeValue() != null))
        {
          contentMap.put(node.getNodeName(), getNodeValueHM(node));
        }
        else if ((!node.hasChildNodes()) && (node.getNodeName().compareToIgnoreCase("#text") > 0))
        {
          contentMap.put(node.getNodeName(), "");
        }
        else if ((node.hasChildNodes()) && (node.getNodeName().compareToIgnoreCase("#text") > 0) && (node.getNextSibling().getNodeValue() != null) && (node.getNextSibling().getNodeValue().trim().length() == 0))
        {
          contentMap.put(node.getNodeName(), "");
        }
        else if ((node.hasChildNodes()) && (node.getNodeName().compareToIgnoreCase("#text") > 0) && (node.getNextSibling().getNodeValue() == null))
        {
          contentMap.put(node.getNodeName(), getNodeValueHM(node));
        }
        else if ((node.hasChildNodes()) && (node.getNextSibling().getNodeValue() != null) && (node.getNextSibling().getNodeValue().trim().length() == 0))
        {
          contentMap.put(node.getNodeName(), "");
        }
      }
    }
    return contentMap;
  }
  
  public HashMap getHashMapValue()
  {
    return hashMapValue;
  }
  
  private void setHashMapValue(HashMap hashMapValue)
  {
    this.hashMapValue = hashMapValue;
  }
  
  public HashMap getHashMapType()
  {
    return hashMapType;
  }
  
  public void setHashMapType(HashMap hashMapType)
  {
    this.hashMapType = hashMapType;
  }
}
