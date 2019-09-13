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
@XmlRootElement(name = "IBTNameInquiryResponse")
public class IBTNameInquiryResponse {
    
     HeaderResponse hostHeaderInfo;
     String accountNo;
     String accountName;
     String accountType;
     String accountStatus;

    @XmlElement(name="hostHeaderInfo")
    public HeaderResponse getHostHeaderInfo() {
        return hostHeaderInfo;
    }

    public void setHostHeaderInfo(HeaderResponse hostHeaderInfo) {
        this.hostHeaderInfo = hostHeaderInfo;
    }
    
    @XmlElement(name="accountNo")
    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    @XmlElement(name="accountName")
    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    @XmlElement(name="accountType")
    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    @XmlElement(name="accountStatus")
    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }
                              

    
}
