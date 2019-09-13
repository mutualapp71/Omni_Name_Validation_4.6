/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ESB;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author PAGBEBLEWU
 */
@XmlRootElement(name = "IBTPostTransferResponse")
public class IBTPostTransferResponse {

    HeaderResponse hostHeaderInfo;
    String transactionRef;
    String paymentRef;
    String cbaReferenceNo;
    String transactionId;
    String amount;

    @XmlElement(name = "transactionRef")
    public String getTransactionRef() {
        return transactionRef;
    }

    public void setTransactionRef(String transactionRef) {
        this.transactionRef = transactionRef;
    }

    @XmlElement(name = "paymentRef")
    public String getPaymentRef() {
        return paymentRef;
    }

    public void setPaymentRef(String paymentRef) {
        this.paymentRef = paymentRef;
    }

    @XmlElement(name = "cbaReferenceNo")
    public String getCbaReferenceNo() {
        return cbaReferenceNo;
    }

    public void setCbaReferenceNo(String cbaReferenceNo) {
        this.cbaReferenceNo = cbaReferenceNo;
    }

    @XmlElement(name = "transactionId")
    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    @XmlElement(name = "amount")
    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    @XmlElement(name = "hostHeaderInfo")
    public HeaderResponse getHostHeaderInfo() {
        return hostHeaderInfo;
    }

    public void setHostHeaderInfo(HeaderResponse hostHeaderInfo) {
        this.hostHeaderInfo = hostHeaderInfo;
    }

}
