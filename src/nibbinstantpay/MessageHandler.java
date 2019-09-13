package nibbinstantpay;

import java.util.HashSet;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

public class MessageHandler
  implements SOAPHandler<SOAPMessageContext>
{
  public boolean handleMessage(SOAPMessageContext messageContext)
  {
    return true;
  }
  
  public Set<QName> getHeaders()
  {
    QName securityHeader = new QName("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "Security", "wsse");
    
    HashSet headers = new HashSet();
    headers.add(securityHeader);
    
    return headers;
  }
  
  public boolean handleFault(SOAPMessageContext messageContext)
  {
    return true;
  }
  
  public void close(MessageContext context) {}
}
