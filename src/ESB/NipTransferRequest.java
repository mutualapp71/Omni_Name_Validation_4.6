/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ESB;

/**
 *
 * @author Admin
 */
public class NipTransferRequest {

    public static String start(String sourceCode, String requestId,String requestToken, String sendingBankCode,String destinationBankCode, String senderAccountNo,
            String senderName,String beneficiaryAccountNo, String beneficiaryName, String narration,String transferNo, String amount,
            String transferReferenceNo, String paymentReferenceNo ) {

        return "<IBTPostTransferRequest>\n"
                + "            <hostHeaderInfo>\n"
                + "               <sourceCode>"+sourceCode+"</sourceCode>\n"
                + "               <requestId>"+requestId+"</requestId>\n"
                + "               <requestToken>"+requestToken+"</requestToken>\n"
                + "               <requestType>IBTTRANPOST</requestType>\n"
                + "               <affiliateCode>ENG</affiliateCode>\n"
                + "               <ipAddress>10.2.100.3</ipAddress>\n"
                + "                <sourceChannelId>567777</sourceChannelId>\n"
                + "            </hostHeaderInfo> \n"
                + "               <sendingBankCode>"+sendingBankCode+"</sendingBankCode>\n"
                + "               <destinationBankCode>"+destinationBankCode+"</destinationBankCode>\n"
                + "               <senderAccountNo>"+senderAccountNo+"</senderAccountNo>\n"
                + "               <senderName>"+senderName+"</senderName>\n"
                + "               <senderAddress>Ecobank</senderAddress>\n"
                + "               <senderID></senderID>\n"
                + "               <senderIDType></senderIDType>\n"
                + "               <senderPhone>08045321786</senderPhone>\n"
                + "               <beneficiaryAccountNo>"+beneficiaryAccountNo+"</beneficiaryAccountNo>\n"
                + "               <beneficiaryName>"+beneficiaryName+"</beneficiaryName>\n"
                + "               <beneficiaryID></beneficiaryID>\n"
                + "               <beneficiaryPhone></beneficiaryPhone>\n"
                + "               <transferType>ACIBT</transferType>\n"
                + "               <narration>"+narration+"</narration>\n"
                + "               <transferNo>"+transferNo+"</transferNo>\n"
                + "               <nameInquiryRef></nameInquiryRef>\n"
                + "               <amount>"+amount+"</amount>\n"
                + "               <ccy>NGN</ccy>\n"
                + "               <transferReferenceNo>"+transferReferenceNo+"</transferReferenceNo>\n"
                + "               <paymentReferenceNo>"+paymentReferenceNo+"</paymentReferenceNo>\n"
                + "               <FormDataValue></FormDataValue>\n"
                + "</IBTPostTransferRequest>";

    }

}
