package nibbinstantpay;

import java.util.HashSet;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

public class HeaderHandler
  implements SOAPHandler<SOAPMessageContext>
{
  public boolean handleMessage(SOAPMessageContext smc)
  {
    Boolean outboundProperty = (Boolean)smc.get("javax.xml.ws.handler.message.outbound");
    if (outboundProperty.booleanValue())
    {
      SOAPMessage message = smc.getMessage();
      try
      {
        SOAPEnvelope envelope = smc.getMessage().getSOAPPart().getEnvelope();
        
        String namespace = "http://www.w3.org/2005/08/addressing";
        
        QName wsaFromQName = new QName(namespace, "From", "wsa");
        
        SOAPElement wsaFromElement = message.getSOAPHeader().addChildElement(wsaFromQName);
        wsaFromElement.addTextNode("eco-kenya");
      }
      catch (Exception e) {}
    }
    return outboundProperty.booleanValue();
  }
  
  public Set<QName> getHeaders()
  {
    QName securityHeader = new QName("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "Security", "wsse");
    
    HashSet headers = new HashSet();
    headers.add(securityHeader);
    
    return headers;
  }
  
  public boolean handleFault(SOAPMessageContext context)
  {
    return true;
  }
  
  public void close(MessageContext context) {}
}
