package jms;

import com.ibm.mq.jms.MQTopicConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.StreamMessage;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import org.apache.log4j.Logger;

class Subscriber
  implements MessageListener
{
  TopicConnectionFactory connectionFactory = null;
  TopicConnection connection = null;
  TopicSession session = null;
  Topic topic = null;
  MessageProducer msgProducer = null;
  TopicSubscriber topicSubscriber = null;
  static Logger logger = Logger.getLogger(Subscriber.class);
  String tName = null;
  int _port = 0;
  String _server = "";
  String _queueManager = "";
  
  public boolean checkForMessage()
  {
    try
    {
      return true;
    }
    catch (Exception ex)
    {
      logger.error(ex);
    }
    return false;
  }
  
  public Subscriber(String topicName, int port, String server, String queueManager, String channel)
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
      topicSubscriber = session.createSubscriber(topic);
      topicSubscriber.setMessageListener(this);
    }
    catch (Exception e)
    {
      logger.error(e);
      close();
      throw e;
    }
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
  
  public void onMessage(Message message)
  {
    try
    {
      receive(message);
    }
    catch (Exception ex) {}
  }
  
  public byte[] receive(Message message)
    throws Exception
  {
    StreamMessage streamMessage = null;
    TextMessage msg = null;
    byte[] bytes = null;
    try
    {
      if ((message instanceof TextMessage))
      {
        msg = (TextMessage)message;
        bytes = msg.getText().getBytes();
      }
      if ((message instanceof StreamMessage))
      {
        streamMessage = (StreamMessage)message;
        bytes = (byte[])streamMessage.readObject();
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
}
