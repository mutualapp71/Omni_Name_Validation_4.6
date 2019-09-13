/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ESB;

/**
 *
 * @author PAGBEBLEWU
 */
public class NipRequest {

    public static String start(String requestId, String requestToken,String sendingBankCode,
            String destinationBankCode,String accountNo ) {

        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
                + "<IBTNameInquiryRequest>\n"
                + "            <hostHeaderInfo>\n"
                + "               <sourceCode>ECOBANKMOBILE</sourceCode>\n"
                + "               <requestId>"+requestId+"</requestId>\n"
                + "               <requestToken>"+requestToken+"</requestToken>\n"
                + "               <requestType>GETACCNAME</requestType>\n"
                + "               <affiliateCode>ENG</affiliateCode>\n"
                + "               <sourceChannelId>567777</sourceChannelId>\n"
                + "               <ipAddress>ENG</ipAddress>\n"
                + "               </hostHeaderInfo>    \n"
                + "               <sendingBankCode>"+sendingBankCode+"</sendingBankCode>\n"
                + "               <destinationBankCode>"+destinationBankCode+"</destinationBankCode> \n"
                + "               <accountNo>"+accountNo+"</accountNo>\n"
                + "               <accountCode></accountCode>\n"
                + "</IBTNameInquiryRequest>";

    }

}
