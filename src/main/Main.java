package main;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import jms.QueueUtility;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

public class Main
{
  static Logger logger = Logger.getLogger(Main.class);
  
  public static void main(String[] args)
    throws SAXException, IOException
  {
    ThreadPoolExecutor tpe = null;
    ConfigConstants configConstants = new ConfigConstants(args[0]);
    tpe = (ThreadPoolExecutor)Executors.newCachedThreadPool();
    tpe.setMaximumPoolSize(1000);
    for (;;)
    {
      if (ConfigConstants.getXmlMsgReceiver().checkMessage()) {
        try
        {
          RequestManager rm = new RequestManager(configConstants);
          rm.receiveMessage();
          tpe.execute(rm);
          if (tpe.getActiveCount() >= 500) {
            Runtime.getRuntime().gc();
          }
        }
        catch (Exception ex)
        {
          tpe.shutdown();
          Runtime.getRuntime().gc();
          logger.error(ex);
        }
      } else {
        try
        {
          Thread.sleep(ConfigConstants.getTimeInterval());
        }
        catch (Exception ex)
        {
          logger.error(ex);
        }
      }
    }
  }
}
