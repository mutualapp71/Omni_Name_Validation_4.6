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
public class GhipssRequest {

    public static String start(String requestId, String requestToken,
            String destinationBankCode, String accountNo) {

        return "{\n"
                + "  \"hostHeaderInfo\":{\n"
                + "    \"sourceCode\": \"OBDX\",\n"
                + "    \"affiliateCode\": \"EGH\",\n"
                + "    \"requestId\":  " + '"' + requestId + '"' + ",\n"
                + "    \"requestToken\":  " + '"' + requestToken + '"' + ",\n"
                + "    \"requestType\": \"IBTNAMEINQUIRY\",\n"
                + "    \"ipAddress\": \"1.1.1.1\",\n"
                + "    \"sourceChannelId\": \"WEB\"\n"
                + "    },\n"
                + "  \"sendingBankCode\": \"300312\",\n"
                + "  \"destinationBankCode\": " + '"' + destinationBankCode + '"' + ",\n"
                + "  \"accountNo\": " + '"' + accountNo + '"' + ",\n"
                + "  \"accountCode\": \"999032\"\n"
                + "}";

    }

}
