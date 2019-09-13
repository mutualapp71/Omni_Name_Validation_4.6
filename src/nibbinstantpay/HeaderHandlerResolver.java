package nibbinstantpay;

import java.util.ArrayList;
import java.util.List;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.handler.PortInfo;

public class HeaderHandlerResolver
  implements HandlerResolver
{
  public List<Handler> getHandlerChain(PortInfo portInfo)
  {
    List<Handler> handlerChain = new ArrayList();
    
    MessageHandler messageHandler = new MessageHandler();
    HeaderHandler headerHandler = new HeaderHandler();
    
    handlerChain.add(messageHandler);
    handlerChain.add(headerHandler);
    
    return handlerChain;
  }
}
