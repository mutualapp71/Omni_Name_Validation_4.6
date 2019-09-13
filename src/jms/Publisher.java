package jms;

import com.ibm.mq.jms.MQTopicConnectionFactory;
import java.io.File;
import java.io.FileInputStream;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.StreamMessage;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import org.apache.log4j.Logger;

class Publisher
{
  TopicConnectionFactory connectionFactory = null;
  TopicConnection connection = null;
  TopicSession session = null;
  Topic topic = null;
  MessageProducer msgProducer = null;
  TopicPublisher topicPublisher = null;
  static Logger logger = Logger.getLogger(Publisher.class);
  String tName = null;
  int _port = 0;
  String _server = "";
  String _queueManager = "";
  
  public Publisher(String topicName, int port, String server, String queueManager, String channel)
    throws Exception
  {
    try
    {
      _port = port;
      tName = topicName;
      _server = server;
      _queueManager = queueManager;
      setUp(port, server, queueManager, channel);
      topic = session.createTopic(topicName);
      topicPublisher = session.createPublisher(topic);
      setUp(port, server, queueManager, channel);
    }
    catch (Exception e)
    {
      e.printStackTrace();
      close();
      throw e;
    }
  }
  
  public boolean checkForMessage()
  {
    try
    {
      return true;
    }
    catch (Exception ex) {}
    return false;
  }
  
  protected void setUp(int port, String server, String queueManager, String channel)
    throws Exception
  {
    connectionFactory = new MQTopicConnectionFactory();
    ((MQTopicConnectionFactory)connectionFactory).setHostName(server);
    ((MQTopicConnectionFactory)connectionFactory).setPort(port);
    ((MQTopicConnectionFactory)connectionFactory).setChannel(channel);
    ((MQTopicConnectionFactory)connectionFactory).setTransportType(1);
    ((MQTopicConnectionFactory)connectionFactory).setQueueManager(queueManager);
    connection = connectionFactory.createTopicConnection();
    connection.start();
    session = connection.createTopicSession(false, 1);
  }
  
  public void close()
  {
    if (connection != null) {
      try
      {
        connection.close();
        connection = null;
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
  
  public void sendXMLMsg(String xmlMsg, String unicode)
    throws Exception
  {
    try
    {
      StreamMessage streamMessage = null;
      streamMessage = session.createStreamMessage();
      streamMessage.writeObject(xmlMsg.getBytes());
      streamMessage.setStringProperty("UniqueCode", unicode);
      topicPublisher.setDeliveryMode(2);
      topicPublisher.publish(streamMessage);
    }
    catch (Exception ex)
    {
      logger.error(ex);
    }
  }
  
  public void send(byte[] xmlByte, String unicode)
    throws Exception
  {
    try
    {
      StreamMessage streamMessage = null;
      streamMessage = session.createStreamMessage();
      streamMessage.writeObject(xmlByte);
      streamMessage.setStringProperty("UniqueCode", unicode);
      topicPublisher.setDeliveryMode(2);
      topicPublisher.publish(streamMessage);
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
      topicPublisher.setDeliveryMode(2);
      topicPublisher.publish(streamMessage);
    }
    catch (Exception ex)
    {
      logger.error(ex);
    }
  }
  
  public void sendTextMsg(String xmlMsg, String unicode)
    throws Exception
  {
    try
    {
      TextMessage txtMessage = null;
      txtMessage = session.createTextMessage();
      txtMessage.setText(xmlMsg);
      txtMessage.setStringProperty("UniqueCode", unicode);
      topicPublisher.setDeliveryMode(2);
      topicPublisher.publish(txtMessage);
    }
    catch (Exception ex)
    {
      logger.error(ex);
    }
  }
}
