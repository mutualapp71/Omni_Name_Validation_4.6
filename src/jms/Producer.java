package jms;

import com.ibm.mq.MQEnvironment;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.jms.MQQueueConnectionFactory;
import com.ibm.mq.jms.MQQueueSender;
import com.ibm.mq.jms.MQQueueSession;
import java.io.File;
import java.io.FileInputStream;
import java.util.Enumeration;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.QueueBrowser;
import javax.jms.QueueConnection;
import javax.jms.StreamMessage;
import javax.jms.TextMessage;
import javax.jms.Topic;
import org.apache.log4j.Logger;

class Producer
{
  MQQueueConnectionFactory connectionFactory = null;
  QueueConnection connection = null;
  MQQueueSession session = null;
  MQEnvironment mqenv = null;
  MQQueueManager mqManager = null;
  com.ibm.mq.jms.MQQueue queue = null;
  com.ibm.mq.MQQueue mqueue = null;
  Topic topic = null;
  QueueBrowser qbrowser = null;
  MessageProducer msgProducer = null;
  MQQueueSender queueSender = null;
  static Logger logger = Logger.getLogger(Producer.class);
  String qName = null;
  int _port = 0;
  String _server = "";
  String _queueManager = "";
  String _channel = "";
  
  public Producer(String queueName, int port, String server, String queueManager, String channel)
    throws Exception
  {
    try
    {
      _port = port;
      qName = queueName;
      _server = server;
      _queueManager = queueManager;
      _channel = channel;
    }
    catch (Exception e)
    {
      logger.error(e);
      
      throw e;
    }
  }
  
  public boolean checkForMessage()
  {
    try
    {
      return qbrowser.getEnumeration().hasMoreElements();
    }
    catch (Exception ex)
    {
      logger.error(ex);
    }
    return false;
  }
  
  protected void setUp()
    throws Exception
  {
    if (connectionFactory == null) {
      connectionFactory = new MQQueueConnectionFactory();
    }
    connectionFactory.setHostName(_server);
    connectionFactory.setPort(_port);
    connectionFactory.setClientId("");
    connectionFactory.setChannel(_channel);
    connectionFactory.setTransportType(1);
    connectionFactory.setQueueManager(_queueManager);
    connection = connectionFactory.createQueueConnection();
    
    connection.start();
    session = ((MQQueueSession)connection.createQueueSession(false, 1));
    queue = ((com.ibm.mq.jms.MQQueue)session.createQueue(qName));
    
    qbrowser = session.createBrowser(queue);
    queueSender = ((MQQueueSender)session.createSender(queue));
  }
  
  public void close()
  {
    if (connection != null) {
      try
      {
        connection.close();
      }
      catch (JMSException e)
      {
        logger.error(e);
      }
      catch (Exception ex)
      {
        logger.error(ex);
      }
    }
  }
  
  public boolean connect()
  {
    try
    {
      setUp();
      return true;
    }
    catch (Exception e)
    {
      logger.error(e);
    }
    return false;
  }
  
  public void sendXMLMsg(String xmlMsg, String unicode)
    throws Exception
  {
    try
    {
      StreamMessage streamMessage = null;
      streamMessage = session.createStreamMessage();
      streamMessage.writeObject(xmlMsg.getBytes());
      streamMessage.setStringProperty("UniqueCode", unicode);
      queueSender.setDeliveryMode(2);
      queueSender.send(streamMessage);
    }
    catch (Exception ex)
    {
      logger.error(ex);
    }
  }
  
  public void sendXMLMsg(String xmlMsg, String unicode, String corrID)
    throws Exception
  {
    try
    {
      StreamMessage streamMessage = null;
      streamMessage = session.createStreamMessage();
      streamMessage.writeObject(xmlMsg.getBytes());
      streamMessage.setJMSCorrelationID(corrID);
      
      queueSender.setDeliveryMode(2);
      queueSender.send(streamMessage);
    }
    catch (Exception ex)
    {
      logger.error(ex);
    }
  }
  
  public void send(byte[] xmlByte, String unicode)
    throws Exception
  {
    int retry = 0;
    try
    {
      StreamMessage streamMessage = null;
      streamMessage = session.createStreamMessage();
      streamMessage.writeObject(xmlByte);
      streamMessage.setStringProperty("UniqueCode", unicode);
      queueSender.setDeliveryMode(2);
      queueSender.send(streamMessage);
    }
    catch (JMSException jms)
    {
      try
      {
        logger.error(jms.getMessage() + "Message Sending Failed: Retrying " + String.valueOf(retry + 1));
        setUp();
        send(xmlByte, unicode);
      }
      catch (Exception e)
      {
        logger.error(e);
      }
    }
    catch (Exception ex)
    {
      logger.error(ex);
    }
  }
  
  public void send(byte[] xmlByte, String unicode, String corrID)
    throws Exception
  {
    int retry = 0;
    try
    {
      StreamMessage streamMessage = null;
      streamMessage = session.createStreamMessage();
      streamMessage.writeObject(xmlByte);
      streamMessage.setJMSCorrelationID(corrID);
      queueSender.setDeliveryMode(2);
      queueSender.send(streamMessage);
    }
    catch (JMSException jms)
    {
      try
      {
        logger.error(jms.getMessage() + "Message Sending Failed: Retrying " + String.valueOf(retry + 1));
        setUp();
        send(xmlByte, unicode, corrID);
      }
      catch (Exception e)
      {
        logger.error(e);
      }
    }
    catch (Exception ex)
    {
      logger.error(ex);
    }
  }
  
  public void sendTextMsg(String xmlMsg, String unicode)
    throws Exception
  {
    int retry = 0;
    try
    {
      TextMessage txtMessage = null;
      txtMessage = session.createTextMessage();
      txtMessage.setText(xmlMsg);
      txtMessage.setStringProperty("UniqueCode", unicode);
      queueSender.setDeliveryMode(2);
      queueSender.send(txtMessage);
    }
    catch (JMSException jms)
    {
      try
      {
        logger.error(jms.getMessage() + "Message Sending Failed: Retrying " + String.valueOf(retry + 1));
        setUp();
        sendTextMsg(xmlMsg, unicode);
      }
      catch (Exception e)
      {
        logger.error(e);
      }
    }
    catch (Exception ex)
    {
      logger.error(ex);
    }
  }
  
  public void sendTextMsg(String xmlMsg, String unicode, String corrID)
    throws Exception
  {
    int retry = 0;
    try
    {
      TextMessage txtMessage = null;
      txtMessage = session.createTextMessage();
      txtMessage.setText(xmlMsg);
      txtMessage.setJMSCorrelationID(corrID);
      queueSender.setDeliveryMode(2);
      queueSender.send(txtMessage);
    }
    catch (JMSException jms)
    {
      try
      {
        logger.error(jms.getMessage() + "Message Sending Failed: Retrying " + String.valueOf(retry++));
        setUp();
        sendTextMsg(xmlMsg, unicode, corrID);
      }
      catch (Exception e)
      {
        logger.error(e);
      }
    }
    catch (Exception ex)
    {
      logger.error(ex);
    }
  }
  
  public void sendTextMsgCharSetEncoding(String xmlMsg, String unicode, String corrID, String format, String encoding)
    throws Exception
  {
    int retry = 0;
    int count = 1;
    try
    {
      TextMessage txtMessage = null;
      txtMessage = session.createTextMessage();
      txtMessage.setText(xmlMsg);
      txtMessage.setJMSCorrelationID(corrID);
      txtMessage.setStringProperty("JMS_IBM_Character_Set", format);
      txtMessage.setIntProperty("JMS_IBM_Encoding", Integer.parseInt(encoding));
      
      queue.setTargetClient(1);
      
      queueSender.setDeliveryMode(2);
      queueSender.send(txtMessage);
    }
    catch (JMSException jms)
    {
      try
      {
        logger.error(jms.getMessage() + "Message Sending Failed: Retrying " + String.valueOf(retry + 1));
        setUp();
        
        boolean result = sendTextMsgCharSetEncodingRetry(xmlMsg, unicode, corrID, format, encoding, count);
        logger.info("Result = " + result);
        while ((!result) && (count <= 3))
        {
          setUp();
          result = sendTextMsgCharSetEncodingRetry(xmlMsg, unicode, corrID, format, encoding, count);
          count++;
        }
      }
      catch (Exception e)
      {
        logger.error(e);
      }
    }
    catch (Exception ex)
    {
      logger.error(ex);
    }
  }
  
  public boolean sendTextMsgCharSetEncodingRetry(String xmlMsg, String unicode, String corrID, String format, String encoding, int count)
    throws Exception
  {
    int retry = 0;
    try
    {
      logger.info(" Trying to Resend Message " + count + " " + xmlMsg);
      TextMessage txtMessage = null;
      txtMessage = session.createTextMessage();
      txtMessage.setText(xmlMsg);
      txtMessage.setJMSCorrelationID(corrID);
      txtMessage.setStringProperty("JMS_IBM_Character_Set", format);
      txtMessage.setIntProperty("JMS_IBM_Encoding", Integer.parseInt(encoding));
      
      queue.setTargetClient(1);
      
      queueSender.setDeliveryMode(2);
      queueSender.send(txtMessage);
    }
    catch (JMSException jms)
    {
      try
      {
        logger.error(jms.getMessage() + " Message Sending Failed: Retrying " + count);
        setUp();
        return false;
      }
      catch (Exception e)
      {
        logger.error(e);
        return false;
      }
    }
    catch (Exception ex)
    {
      logger.error(ex);
      return false;
    }
    return true;
  }
  
  public void sendTextMsgCharSetEncodingWB(String xmlMsg, String unicode, String corrID, String format, String encoding)
    throws Exception
  {
    int retry = 0;
    try
    {
      TextMessage txtMessage = null;
      txtMessage = session.createTextMessage();
      txtMessage.setText(xmlMsg);
      txtMessage.setJMSCorrelationID(corrID);
      txtMessage.setStringProperty("JMS_IBM_Character_Set", format);
      txtMessage.setIntProperty("JMS_IBM_Encoding", Integer.parseInt(encoding));
      
      queueSender.setDeliveryMode(2);
      queueSender.send(txtMessage);
    }
    catch (JMSException jms)
    {
      try
      {
        logger.error(jms.getMessage() + "Message Sending Failed: Retrying " + String.valueOf(retry + 1));
        setUp();
        send(xmlMsg, unicode, corrID);
      }
      catch (Exception e)
      {
        logger.error(e);
      }
    }
    catch (Exception ex)
    {
      logger.error(ex);
    }
  }
  
  public void send(String xmlfile, String unicode)
    throws Exception
  {
    try
    {
      StreamMessage streamMessage = null;
      File f = new File(xmlfile);
      int length = (int)f.length();
      FileInputStream inStream = new FileInputStream(f);
      byte[] buf = new byte[length];
      inStream.read(buf);
      inStream.close();
      streamMessage = session.createStreamMessage();
      streamMessage.writeObject(buf);
      streamMessage.setStringProperty("UniqueCode", unicode);
      queueSender.setDeliveryMode(2);
      queueSender.send(streamMessage);
    }
    catch (Exception ex)
    {
      logger.error(ex);
    }
  }
  
  public void send(String xmlfile, String unicode, String corrID)
    throws Exception
  {
    try
    {
      StreamMessage streamMessage = null;
      File f = new File(xmlfile);
      int length = (int)f.length();
      FileInputStream inStream = new FileInputStream(f);
      byte[] buf = new byte[length];
      inStream.read(buf);
      inStream.close();
      
      streamMessage = session.createStreamMessage();
      streamMessage.writeObject(buf);
      
      streamMessage.setJMSCorrelationID(corrID);
      queueSender.setDeliveryMode(2);
      queueSender.send(streamMessage);
    }
    catch (Exception ex)
    {
      logger.error(ex);
    }
  }
}
