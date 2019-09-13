package jms;

import javax.jms.JMSException;
import org.apache.log4j.Logger;

public class QueueUtility
{
  Producer producer = null;
  Consumer consumer = null;
  String xmlPath = null;
  String qName = null;
  int _port = 0;
  String _server = "";
  String _queueManager = "";
  String _channel = "";
  private int _noOfMsgSend = 0;
  private int _noOfMsgRecv = 0;
  private String corrID = "";
  private String messID = "";
  static Logger logger = Logger.getLogger(QueueUtility.class);
  
  public QueueUtility(String queueName, String type, int port, String server, String queueManager, String channel)
  {
    qName = queueName;
    _port = port;
    _server = server;
    _queueManager = queueManager;
    _channel = channel;
    try
    {
      if (type.compareTo("send") == 0) {
        producer = new Producer(queueName, port, server, queueManager, channel);
      } else if (type.compareTo("receive") == 0) {
        consumer = new Consumer(queueName, port, server, queueManager, channel);
      }
    }
    catch (Exception e)
    {
      logger.error(e);
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
      logger.error(ex);
    }
  }
  
  public boolean connectConsumer()
  {
    try
    {
      consumer.connect();
      return true;
    }
    catch (Exception e)
    {
      logger.error(e);
    }
    return false;
  }
  
  public boolean connectProducer()
  {
    try
    {
      producer.connect();
      return true;
    }
    catch (Exception e)
    {
      logger.error(e);
    }
    return false;
  }
  
  public void sendMessage(String xmlfile, String unicode, String corrID)
  {
    try
    {
      if (_noOfMsgSend >= 250)
      {
        closeProducer();
        _noOfMsgSend = 0;
      }
      producer.send(xmlfile, unicode, corrID);
      _noOfMsgSend += 1;
    }
    catch (Exception ex)
    {
      logger.error(ex);
    }
  }
  
  public void sendTxtMessage(String xmlfile, String unicode)
  {
    try
    {
      if (_noOfMsgSend >= 250)
      {
        closeProducer();
        _noOfMsgSend = 0;
      }
      producer.sendTextMsg(xmlfile, unicode);
      _noOfMsgSend += 1;
    }
    catch (Exception ex)
    {
      logger.error(ex);
    }
  }
  
  public void sendTxtMessage(String xmlfile, String unicode, String corrID)
  {
    try
    {
      if (_noOfMsgSend >= 250)
      {
        closeProducer();
        _noOfMsgSend = 0;
      }
      producer.sendTextMsg(xmlfile, unicode, corrID);
      _noOfMsgSend += 1;
    }
    catch (Exception ex)
    {
      logger.error(ex);
    }
  }
  
  public void sendTxtMessage(String xmlfile, String unicode, String corrID, String charSet, String encoding)
  {
    try
    {
      if (_noOfMsgSend >= 250)
      {
        closeProducer();
        _noOfMsgSend = 0;
      }
      producer.sendTextMsgCharSetEncoding(xmlfile, unicode, corrID, charSet, encoding);
      _noOfMsgSend += 1;
    }
    catch (Exception ex)
    {
      logger.error(ex);
    }
  }
  
  public void sendTxtMessageWB(String xmlfile, String unicode, String corrID, String charSet, String encoding)
  {
    try
    {
      if (_noOfMsgSend >= 250)
      {
        closeProducer();
        _noOfMsgSend = 0;
      }
      producer.sendTextMsgCharSetEncodingWB(xmlfile, unicode, corrID, charSet, encoding);
      _noOfMsgSend += 1;
    }
    catch (Exception ex)
    {
      logger.error(ex);
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
      logger.error(ex);
    }
  }
  
  public void sendMessage(byte[] xmlfile, String unicode, String corrID)
  {
    try
    {
      producer.send(xmlfile, unicode, corrID);
    }
    catch (Exception ex)
    {
      logger.error(ex);
    }
  }
  
  public byte[] receiveMessage()
  {
    byte[] bais = null;
    try
    {
      bais = consumer.receive();
      if (consumer.getCorrID() != null)
      {
          
        System.out.println("CorrID >>> " + consumer.getCorrID());
        System.out.println("MessID >>> " + consumer.getMessID());
        
        logger.info("CorrID >>> " + consumer.getCorrID());
        logger.info("MessID >>> " + consumer.getMessID());
        
        setCorrID(consumer.getCorrID());
        setMessID(consumer.getMessID());
      }
    }
    catch (JMSException jms)
    {
      try
      {
        consumer.session = null;
        consumer.setUp();
        receiveMessage();
      }
      catch (Exception e)
      {
        logger.error(e);
      }
      logger.error(jms);
    }
    catch (Exception ex)
    {
      logger.error(ex);
    }
    return bais;
  }
  
  public byte[] receiveMessageWB()
  {
    byte[] bais = null;
    try
    {
      bais = consumer.receiveWB();
      if (consumer.getCorrID() != null) {
        setCorrID(consumer.getCorrID());
      }
    }
    catch (JMSException jms)
    {
      try
      {
        consumer.session = null;
        consumer.setUp();
        receiveMessage();
      }
      catch (Exception e)
      {
        logger.error(e);
      }
      logger.error(jms);
    }
    catch (Exception ex)
    {
      logger.error(ex);
    }
    return bais;
  }
  
  public void closeProducer()
  {
    producer.close();
  }
  
  public void closeConsumer()
  {
    consumer.close();
  }
  
  public boolean checkMessage()
  {
    return consumer.checkForMessage();
  }
  
  public String getMessID()
  {
    corrID = consumer.getCorrID();
    return messID;
  }
  
  public void setMessID(String messID)
  {
    this.messID = messID;
  }
  
  public String getCorrID()
  {
    return corrID;
  }
  
  public void setCorrID(String corrID)
  {
    this.corrID = corrID;
  }
}
