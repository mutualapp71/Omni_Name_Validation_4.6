package jms;

import com.ibm.jms.JMSBytesMessage;
import com.ibm.mq.jms.MQQueueConnectionFactory;
import java.util.Enumeration;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.QueueConnection;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.StreamMessage;
import javax.jms.TextMessage;
import javax.jms.Topic;
import org.apache.log4j.Logger;

class Consumer
{
  MQQueueConnectionFactory connectionFactory = null;
  QueueConnection connection = null;
  QueueSession session = null;
  Queue queue = null;
  Topic topic = null;
  QueueBrowser qbrowser = null;
  QueueReceiver queueReceiver = null;
  MessageConsumer msgConsumer = null;
  static Logger logger = Logger.getLogger(Consumer.class);
  String qName = "";
  int _port = 0;
  String _server = "";
  String _queueManager = "";
  String _channel = "";
  private String corrID = "";
  private String messID = "";
  
  public boolean checkForMessage()
  {
    int retry = 0;
    try
    {
      System.gc();
      return qbrowser.getEnumeration().hasMoreElements();
    }
    catch (JMSException jms)
    {
      try
      {
        setUp();
        return checkForMessage();
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
    }
    return false;
  }
  
  public Consumer(String queueName, int port, String server, String queueManager, String channel)
    throws Exception
  {
    _port = port;
    qName = queueName;
    _server = server;
    _queueManager = queueManager;
    _channel = channel;
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
  
  protected void setUp()
    throws Exception
  {
    try
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
      session = connection.createQueueSession(false, 1);
      queue = session.createQueue(qName);
      qbrowser = session.createBrowser(queue);
      queueReceiver = session.createReceiver(queue);
    }
    catch (Exception ex) {}
  }
  
  public void close()
  {
    if (connection != null) {
      try
      {
        connection.close();
      }
      catch (JMSException ee)
      {
        logger.error(ee);
      }
      catch (Exception ex)
      {
        logger.error(ex);
      }
    }
  }
  
  public byte[] receive()
    throws Exception
  {
    StreamMessage streamMessage = null;
    TextMessage txtMessage = null;
    JMSBytesMessage bMsg = null;
    Message message = null;
    byte[] bytes = null;
    int retry = 0;
    try
    {
      message = queueReceiver.receiveNoWait();
      if ((message instanceof TextMessage))
      {
        txtMessage = (TextMessage)message;
        bytes = (byte[])txtMessage.getText().getBytes();
        setCorrID(txtMessage.getJMSMessageID());
        setMessID(txtMessage.getJMSCorrelationID());
      }
      else if ((message instanceof JMSBytesMessage))
      {
        bMsg = (JMSBytesMessage)message;
        bMsg.readBytes(bytes);
        setCorrID(bMsg.getJMSMessageID());
        setMessID(bMsg.getJMSCorrelationID());
      }
      else
      {
        streamMessage = (StreamMessage)message;
        streamMessage.readBytes(bytes);
        setCorrID(streamMessage.getJMSMessageID());
        setMessID(streamMessage.getJMSCorrelationID());
      }
    }
    catch (JMSException jms)
    {
      try
      {
        logger.error(jms.getMessage() + "Message Checking Failed: Retrying " + String.valueOf(retry + 1));
        setUp();
        return receive();
      }
      catch (Exception e)
      {
        logger.error(e);
      }
    }
    catch (NullPointerException e)
    {
      for (;;)
      {
        logger.error(e);
      }
    }
    catch (Exception e)
    {
      logger.error(e);
    }
    return bytes;
  }
  
  public byte[] receiveWB()
    throws Exception
  {
    StreamMessage streamMessage = null;
    TextMessage txtMessage = null;
    JMSBytesMessage bMsg = null;
    Message message = null;
    byte[] bytes = null;
    int retry = 0;
    try
    {
      message = queueReceiver.receiveNoWait();
      if ((message instanceof TextMessage))
      {
        txtMessage = (TextMessage)message;
        bytes = (byte[])txtMessage.getText().getBytes();
        setCorrID(txtMessage.getJMSCorrelationID());
      }
      else if ((message instanceof JMSBytesMessage))
      {
        bMsg = (JMSBytesMessage)message;
        bMsg.readBytes(bytes);
        setCorrID(bMsg.getJMSCorrelationID());
      }
      else
      {
        streamMessage = (StreamMessage)message;
        streamMessage.readBytes(bytes);
        setCorrID(streamMessage.getJMSCorrelationID());
      }
    }
    catch (JMSException jms)
    {
      try
      {
        logger.error(jms.getMessage() + "Message Checking Failed: Retrying " + String.valueOf(retry + 1));
        setUp();
        return receiveWB();
      }
      catch (Exception e)
      {
        logger.error(e);
      }
    }
    catch (NullPointerException e)
    {
      for (;;)
      {
        logger.error(e);
      }
    }
    catch (Exception e)
    {
      logger.error(e);
    }
    return bytes;
  }
  
  public String getCorrID()
  {
    return corrID;
  }
  
  public String getMessID()
  {
    return messID;
  }
  
  public void setCorrID(String corrID)
  {
    this.corrID = corrID;
  }
  
  public void setMessID(String messID)
  {
    this.messID = messID;
  }
}
