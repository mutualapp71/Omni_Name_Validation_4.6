package jms;

import java.io.PrintStream;
import org.apache.log4j.Logger;

public class TopicUtility
{
  Publisher producer = null;
  Subscriber consumer = null;
  String xmlPath = null;
  String qName = null;
  long _port = 0L;
  String _server = "";
  String _queueManager = "";
  private int _noOfMsgSend = 0;
  private int _noOfMsgRecv = 0;
  static Logger logger = Logger.getLogger(TopicUtility.class);
  
  public TopicUtility(String queueName, String type, int port, String server, String queueManager, String channel)
  {
    qName = queueName;
    _port = port;
    _server = server;
    _queueManager = queueManager;
    try
    {
      if (type.compareTo("send") == 0) {
        producer = new Publisher(queueName, port, server, queueManager, channel);
      } else if (type.compareTo("receive") == 0) {
        consumer = new Subscriber(queueName, port, server, queueManager, channel);
      }
    }
    catch (Exception e)
    {
      System.out.println("Exception occurred : " + e.toString());
      e.printStackTrace();
    }
  }
  
  public void sendMessage(String xmlfile, String unicode)
  {
    try
    {
      if (_noOfMsgSend >= 250)
      {
        closeProducer();
        _noOfMsgSend = 0;
      }
      producer.send(xmlfile, unicode);
      _noOfMsgSend += 1;
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }
  
  public void sendTxtMessage(String xmlfile, String unicode)
  {
    try
    {
      producer.sendTextMsg(xmlfile, unicode);
      _noOfMsgSend += 1;
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }
  
  public void sendMessage(byte[] xmlfile, String unicode)
  {
    try
    {
      producer.send(xmlfile, unicode);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }
  
  public byte[] receiveMessage()
  {
    byte[] bais = null;
    
    return bais;
  }
  
  public void closeProducer()
  {
    producer.close();
  }
  
  public void closeConsumer() {}
  
  public boolean checkMessage()
  {
    return false;
  }
}
