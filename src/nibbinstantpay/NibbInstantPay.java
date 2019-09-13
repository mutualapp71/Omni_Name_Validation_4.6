package nibbinstantpay;

import com.ecobank.esb.interbanktransfer.GetIBTransferNameInquiry;
import com.ecobank.esb.interbanktransfer.GetIBTransferNameInquiry.InterBankTransferNameInquiry;
import com.ecobank.esb.interbanktransfer.GetIBTransferNameInquiry.InterBankTransferNameInquiry.NameInquiryRequest;
import com.ecobank.esb.interbanktransfer.GetIBTransferNameInquiryResponse;
import com.ecobank.esb.interbanktransfer.GetIBTransferNameInquiryResponse.Return;
import com.ecobank.esb.interbanktransfer.GetIBTransferNameInquiryResponse.Return.NameInquiryResponse;
import com.ecobank.esb.interbanktransfer.IInterBankTransferService;
import com.ecobank.esb.interbanktransfer.InterBankTransferService;
import com.ecobank.esb.interbanktransfer._1.IBTransferNameInquiryRequest;
import com.ecobank.esb.interbanktransfer._1.TransferHeaderType;
import com.ecobank.esb.interbanktransfer._1.TransferResponseType;
import com.ecobank.nibbs.util.ssl.SetSystemSSLProperties;
import java.io.PrintStream;
import java.security.MessageDigest;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.log4j.Logger;

public class NibbInstantPay
{
  Format formatter = new SimpleDateFormat("yymmddHHmmss");
  Date newDate = new Date();
  static Logger logger = Logger.getLogger(NibbInstantPay.class);
  
  public static void main(String[] args)
  {
    NibbInstantPay Japp = new NibbInstantPay();
    
    Japp.schedulerJob();
  }
  
  public void schedulerJob()
  {
    SetSystemSSLProperties.getInstance().process();
    
    String fileBody = "";
    ArrayList txnlt = new ArrayList();
    ArrayList txnlist = new ArrayList();
    try
    {
      GetIBTransferNameInquiry.InterBankTransferNameInquiry tt = new GetIBTransferNameInquiry.InterBankTransferNameInquiry();
      GetIBTransferNameInquiryResponse.Return result = new GetIBTransferNameInquiryResponse.Return();
      
      IBTransferNameInquiryRequest NameEquiryRQ = new IBTransferNameInquiryRequest();
      TransferHeaderType transHT = new TransferHeaderType();
      
      String scode = "RIBNIP";
      String spwd = "ribnipuat";
      transHT.setSourceCode(scode);
      transHT.setSourceChannel("RIB");
      
      String SenderBankCode = "050";
      String DestinationBankCode = "214";
      
      String datetime = formatter.format(newDate);
      
      String sequence = "123456789016";
      String requestID = SenderBankCode + DestinationBankCode + datetime + sequence;
      
      transHT.setRequestId(requestID);
      
      String acc = "0418563022";
      
      String data = acc + requestID + scode + spwd;
      MessageDigest digest = MessageDigest.getInstance("SHA-512");
      
      byte[] hashBytes = data.getBytes("UTF-8");
      
      byte[] messageDigest = digest.digest(hashBytes);
      
      StringBuffer sb = new StringBuffer();
      for (int i = 0; i < messageDigest.length; i++)
      {
        String h = Integer.toHexString(0xFF & messageDigest[i]);
        while (h.length() < 2) {
          h = "0" + h;
        }
        sb.append(h);
      }
      String requestToken = sb.toString();
      
      transHT.setRequestToken(requestToken);
      
      GregorianCalendar gcal = new GregorianCalendar();
      XMLGregorianCalendar xgcal = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
      System.out.println(xgcal);
      
      transHT.setRequestDate(xgcal);
      transHT.setAffiliateCode("ENG");
      transHT.setIPAddress("23");
      transHT.setSourceChannelId("10");
      
      tt.setTransferHeader(transHT);
      NameEquiryRQ.setTransferHeader(transHT);
      
      GetIBTransferNameInquiry.InterBankTransferNameInquiry.NameInquiryRequest EnquiryNamRq = new GetIBTransferNameInquiry.InterBankTransferNameInquiry.NameInquiryRequest();
      
      EnquiryNamRq.setSendingBankCode("050");
      EnquiryNamRq.setDestinationBankCode("214");
      EnquiryNamRq.setAccountNo(acc);
      EnquiryNamRq.setAccountCode(acc);
      
      tt.setNameInquiryRequest(EnquiryNamRq);
      
      HeaderHandlerResolver res = new HeaderHandlerResolver();
      
      IInterBankTransferService rrr = new IInterBankTransferService();
      
      rrr.setHandlerResolver(res);
      
      InterBankTransferService resp = rrr.getInterBankTransferServicePort();
      
      System.out.println(resp.getIBTransferNameInquiry(tt).getNameInquiryResponse().getAccountName());
    }
    catch (Exception ex)
    {
      System.out.println(ex);
      ex.printStackTrace();
    }
  }
  
  public String NameEnquiry(String DestinationBankCode, String acc)
  {
    SetSystemSSLProperties.getInstance().process();
    String Name = "";
    try
    {
      GetIBTransferNameInquiry.InterBankTransferNameInquiry tt = new GetIBTransferNameInquiry.InterBankTransferNameInquiry();
      GetIBTransferNameInquiryResponse.Return result = new GetIBTransferNameInquiryResponse.Return();
      
      IBTransferNameInquiryRequest NameEquiryRQ = new IBTransferNameInquiryRequest();
      TransferHeaderType transHT = new TransferHeaderType();
      
      String scode = "RIBNIP";
      String spwd = "ribnipuat";
      transHT.setSourceCode(scode);
      transHT.setSourceChannel("RIB");
      
      String SenderBankCode = "050";
      
      String datetime = formatter.format(newDate);
      String sequence = "123456789016";
      String requestID = SenderBankCode + DestinationBankCode + datetime + sequence;
      
      transHT.setRequestId(requestID);
      
      String data = acc + requestID + scode + spwd;
      MessageDigest digest = MessageDigest.getInstance("SHA-512");
      
      byte[] hashBytes = data.getBytes("UTF-8");
      
      byte[] messageDigest = digest.digest(hashBytes);
      
      StringBuffer sb = new StringBuffer();
      for (int i = 0; i < messageDigest.length; i++)
      {
        String h = Integer.toHexString(0xFF & messageDigest[i]);
        while (h.length() < 2) {
          h = "0" + h;
        }
        sb.append(h);
      }
      String requestToken = sb.toString();
      
      transHT.setRequestToken(requestToken);
      
      GregorianCalendar gcal = new GregorianCalendar();
      XMLGregorianCalendar xgcal = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
      System.out.println(xgcal);
      
      transHT.setRequestDate(xgcal);
      transHT.setAffiliateCode("ENG");
      transHT.setIPAddress("23");
      transHT.setSourceChannelId("10");
      
      tt.setTransferHeader(transHT);
      NameEquiryRQ.setTransferHeader(transHT);
      
      GetIBTransferNameInquiry.InterBankTransferNameInquiry.NameInquiryRequest EnquiryNamRq = new GetIBTransferNameInquiry.InterBankTransferNameInquiry.NameInquiryRequest();
      
      EnquiryNamRq.setSendingBankCode("050");
      EnquiryNamRq.setDestinationBankCode(DestinationBankCode);
      EnquiryNamRq.setAccountNo(acc);
      EnquiryNamRq.setAccountCode(acc);
      
      tt.setNameInquiryRequest(EnquiryNamRq);
      HeaderHandlerResolver res = new HeaderHandlerResolver();
      IInterBankTransferService rrr = new IInterBankTransferService();
      rrr.setHandlerResolver(res);
      InterBankTransferService resp = rrr.getInterBankTransferServicePort();
      
      result = resp.getIBTransferNameInquiry(tt);
      if (result.getResponseData().getResponseCode().compareToIgnoreCase("00") == 0)
      {
        Name = result.getNameInquiryResponse().getAccountName();
        logger.info("ACC>>>>>:" + acc + "  " + "Response code>>>>>>" + result.getResponseData().getResponseCode() + " " + "NameResponse>>>>" + result.getNameInquiryResponse().getAccountName());
        System.out.println(resp.getIBTransferNameInquiry(tt).getNameInquiryResponse().getAccountName());
      }
      else
      {
        logger.info("ACC>>>>>:" + acc + "  " + "Response code>>>>>>" + result.getResponseData().getResponseCode() + " " + "NameResponse>>>>" + result.getNameInquiryResponse().getAccountName());
        Name = "Name Not Found";
      }
    }
    catch (Exception ex)
    {
      System.out.println(ex);
      ex.printStackTrace();
      logger.info(ex);
      Name = "Name Not Found";
    }
    return Name;
  }
}
