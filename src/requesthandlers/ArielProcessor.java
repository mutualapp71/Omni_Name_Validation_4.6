package requesthandlers;

import ESB.GhipssRequest;
import ESB.IBTNameInquiryResponse;
import ESB.NipRequest;
import ESB.RestPost;
import ESB.RestPost2;
import ESB.Strip;
import com.ecobank.nibbs.util.ssl.SetSystemSSLProperties;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.security.MessageDigest;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import jms.QueueUtility;
import nibbinstantpay.HeaderHandlerResolver;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.sql.CLOB;
import org.apache.log4j.Logger;
import java.io.Writer;
import java.util.Properties;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

public class ArielProcessor
        extends ProcessorBase {

    private OracleConnection _conn;
    private String _requestCode;
    static Logger logger = Logger.getLogger(ArielProcessor.class);
    private boolean bStatus = false;
    private String errCode = "";
    private String errMsg = "";
    private String corrID = "";
    static QueueUtility xmlMsgReceiver1 = null;
    Format formatter = new SimpleDateFormat("yyMMddHHmmss");
    Date newDate = new Date();
    String returnCode = "";

    public ArielProcessor(String requestCode, OracleConnection conn) {
        _conn = conn;
        _requestCode = requestCode;
    }

    public ArielProcessor(String requestCode, OracleConnection conn, QueueUtility xmlMsgReceiver) {
        _conn = conn;
        _requestCode = requestCode;
        xmlMsgReceiver1 = xmlMsgReceiver;
    }

    public byte[] getResponse(HashMap hashMap, String requestCode) {
        byte[] retByte = null;
        if ((requestCode.compareToIgnoreCase("1027") == 0) || (requestCode.compareToIgnoreCase("1028") == 0)) {
            retByte = buildBalanceResponse(hashMap);
        } else if (requestCode.compareToIgnoreCase("1029") == 0) {
            retByte = buildBalanceResponseEOD(hashMap);
        } else if ((requestCode.compareToIgnoreCase("1035") == 0) || (requestCode.compareToIgnoreCase("1036") == 0)) {
            retByte = buildActivityResponse(hashMap);
        } else if (requestCode.compareToIgnoreCase("1009") == 0) {
            retByte = buildAccountingHandoffResponse(hashMap);
        } else if (requestCode.compareToIgnoreCase("1007") == 0) {
            retByte = buildScheduleDownloadResponse(hashMap);
        } else if (requestCode.compareToIgnoreCase("1010") == 0) {
            retByte = buildIssuanceUpdateResponse(hashMap);
        } else if ((requestCode.compareToIgnoreCase("1001") == 0) || (requestCode.compareToIgnoreCase("1003") == 0) || (requestCode.compareToIgnoreCase("1025") == 0) || (requestCode.compareToIgnoreCase("1005") == 0)) {
            retByte = buildGLTransactionResponse(hashMap);
        } else if ((requestCode.compareToIgnoreCase("1012") == 0) || (requestCode.compareToIgnoreCase("1023") == 0)) {
            retByte = buildStopPayResponse(hashMap);
        } else if (requestCode.compareToIgnoreCase("1015") == 0) {
            retByte = buildFxContractRate(hashMap);
        } else if (requestCode.compareToIgnoreCase("1021") == 0) {

            System.out.println("1021 Ariel Processor Call");
            logger.info("1021 Ariel Processor Call");
            retByte = buildBeneAcctVerf2(hashMap);

        } else if (requestCode.compareToIgnoreCase("1050") == 0) {
            retByte = buildBeneNameVerf(hashMap);
        }
        return retByte;
    }

    public byte[] buildARLACTHResponse(HashMap values) {
        byte[] retval = null;
        setBStatus(false);
        try {
            CLOB clob = null;
            int success = 0;
            OracleCallableStatement stmt = (OracleCallableStatement) _conn.prepareCall("{? = call pkariel.fn_ARLACTH(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");

            stmt.registerOutParameter(1, -5);
            stmt.registerOutParameter(14, 2005);
            stmt.registerOutParameter(15, 2005);
            setStmtString(stmt, values, "MessageType", 2);
            setStmtString(stmt, values, "ProcCode", 3);
            setStmtString(stmt, values, "UniqueCode", 4);
            setStmtString(stmt, values, "BranchName", 5);
            setStmtString(stmt, values, "TxnDtTime", 6);
            setStmtString(stmt, values, "AcctNum", 7);
            setStmtString(stmt, values, "AcctCompCode", 8);
            setStmtString(stmt, values, "FromDate", 9);
            setStmtString(stmt, values, "ToDate", 10);
            setStmtInt(stmt, values, "RecordFromIndex", 11);
            setStmtInt(stmt, values, "RecordToIndex", 12);
            setStmtString(stmt, values, "IncludeHeader", 13);
            logger.info("Cal to db start");
            stmt.execute();
            logger.info("Cal to db return");
            success = stmt.getInt(1);
            if (success == 0) {
                clob = stmt.getCLOB(15);
                retval = new byte[(int) clob.length()];
                InputStream reader = clob.getAsciiStream();
                reader.read(retval);
                logRequest(values, "ARLACTH", "Successully Processed");
            } else {
                clob = stmt.getCLOB(14);
                retval = new byte[(int) clob.length()];
                InputStream reader = clob.getAsciiStream();
                reader.read(retval);
                setBStatus(true);
                logRequest(values, "ARLACTH", "Failed Processing");
            }
        } catch (Exception ex) {
            logger.error(ex);

            setBStatus(true);
            logRequest(values, "ARLACTH", "Failed Processing");
        }
        return retval;
    }

    public byte[] buildARLLTXHResponse(HashMap values) {
        byte[] retval = null;
        setBStatus(false);
        try {
            CLOB clob = null;
            int success = 0;
            OracleCallableStatement stmt = (OracleCallableStatement) _conn.prepareCall("{? = call pkariel.fn_ARLLTXH(?,?,?,?,?,?,?,?,?,?,?,?,?)}");

            stmt.registerOutParameter(1, -5);
            stmt.registerOutParameter(13, 2005);
            stmt.registerOutParameter(14, 2005);
            setStmtString(stmt, values, "MessageType", 2);
            setStmtString(stmt, values, "ProcCode", 3);
            setStmtString(stmt, values, "UniqueCode", 4);
            setStmtString(stmt, values, "BranchName", 5);
            setStmtString(stmt, values, "TxnDtTime", 6);
            setStmtString(stmt, values, "LoanRefNo", 7);
            setStmtString(stmt, values, "LoanAcctCompCode", 8);
            setStmtString(stmt, values, "FromDate", 9);
            setStmtString(stmt, values, "ToDate", 10);
            setStmtInt(stmt, values, "RecordFromIndex", 11);
            setStmtInt(stmt, values, "RecordToIndex", 12);
            stmt.execute();

            success = stmt.getInt(1);
            if (success == 0) {
                clob = stmt.getCLOB(14);
                retval = new byte[(int) clob.length()];
                InputStream reader = clob.getAsciiStream();
                reader.read(retval);
            } else {
                clob = stmt.getCLOB(13);
                retval = new byte[(int) clob.length()];
                InputStream reader = clob.getAsciiStream();
                reader.read(retval);
                setBStatus(true);
            }
        } catch (Exception ex) {
            logger.error(ex);

            setBStatus(true);
        }
        return retval;
    }

    public byte[] buildARLEXRLResponse(HashMap values) {
        byte[] retval = null;
        setBStatus(false);
        try {
            CLOB clob = null;
            int success = 0;
            OracleCallableStatement stmt = (OracleCallableStatement) _conn.prepareCall("{? = call pkariel.fn_ARLEXRL(?,?,?,?,?,?,?)}");

            stmt.registerOutParameter(1, -5);
            stmt.registerOutParameter(7, 2005);
            stmt.registerOutParameter(8, 2005);
            setStmtString(stmt, values, "MessageType", 2);
            setStmtString(stmt, values, "ProcCode", 3);
            setStmtString(stmt, values, "UniqueCode", 4);
            setStmtString(stmt, values, "BranchName", 5);
            setStmtString(stmt, values, "TxnDtTime", 6);
            stmt.execute();

            success = stmt.getInt(1);
            if (success == 0) {
                clob = stmt.getCLOB(8);
                retval = new byte[(int) clob.length()];
                InputStream reader = clob.getAsciiStream();
                reader.read(retval);
                logRequest(values, "ARLEXR", "Successfully Processed");
            } else {
                clob = stmt.getCLOB(7);
                retval = new byte[(int) clob.length()];
                InputStream reader = clob.getAsciiStream();
                reader.read(retval);
                setBStatus(true);
                logRequest(values, "ARLEXR", "Failed Processing");
            }
        } catch (Exception ex) {
            logger.error(ex);

            setBStatus(true);
            logRequest(values, "ARLEXR", "Failed Processing");
        }
        return retval;
    }

    public byte[] buildARLSPEXRResponse(HashMap values) {
        byte[] retval = null;
        setBStatus(false);
        try {
            CLOB clob = null;
            int success = 0;
            OracleCallableStatement stmt = (OracleCallableStatement) _conn.prepareCall("{? = call pkariel.fn_ARLSPEXR(?,?,?,?,?,?,?,?,?,?)}");

            stmt.registerOutParameter(1, -5);
            stmt.registerOutParameter(10, 2005);
            stmt.registerOutParameter(11, 2005);
            setStmtString(stmt, values, "MessageType", 2);
            setStmtString(stmt, values, "ProcCode", 3);
            setStmtString(stmt, values, "UniqueCode", 4);
            setStmtString(stmt, values, "BranchName", 5);
            setStmtString(stmt, values, "TxnDtTime", 6);
            setStmtString(stmt, values, "BaseCurr", 7);
            setStmtString(stmt, values, "SourceCurr", 8);
            setStmtString(stmt, values, "TargetCurr", 9);
            stmt.execute();

            success = stmt.getInt(1);
            if (success == 0) {
                clob = stmt.getCLOB(11);
                retval = new byte[(int) clob.length()];
                InputStream reader = clob.getAsciiStream();
                reader.read(retval);
                logRequest(values, "ARLSPEXR", "Successfully Processed");
            } else {
                clob = stmt.getCLOB(10);
                retval = new byte[(int) clob.length()];
                InputStream reader = clob.getAsciiStream();
                reader.read(retval);
                setBStatus(true);
                logRequest(values, "ARLSPEXR", "Failed Processing");
            }
        } catch (Exception ex) {
            logger.error(ex);
            setBStatus(true);
            logRequest(values, "ARLSPEXR", "Failed Processing");
        }
        return retval;
    }

    public byte[] buildARLLACDResponse(HashMap values) {
        byte[] retval = null;
        setBStatus(false);
        try {
            CLOB clob = null;
            int success = 0;
            OracleCallableStatement stmt = (OracleCallableStatement) _conn.prepareCall("{? = call pkariel.fn_ARLLACD(?,?,?,?,?,?,?,?,?)}");

            stmt.registerOutParameter(1, -5);
            stmt.registerOutParameter(9, 2005);
            stmt.registerOutParameter(10, 2005);
            setStmtString(stmt, values, "MessageType", 2);
            setStmtString(stmt, values, "ProcCode", 3);
            setStmtString(stmt, values, "UniqueCode", 4);
            setStmtString(stmt, values, "BranchName", 5);
            setStmtString(stmt, values, "TxnDtTime", 6);
            setStmtString(stmt, values, "LoanRefNo", 7);
            setStmtString(stmt, values, "LoanAcctCompCode", 8);
            stmt.execute();

            success = stmt.getInt(1);
            if (success == 0) {
                clob = stmt.getCLOB(10);
                retval = new byte[(int) clob.length()];
                InputStream reader = clob.getAsciiStream();
                reader.read(retval);
                logRequest(values, "ARLLACD", "Successfully Processed");
            } else {
                clob = stmt.getCLOB(9);
                retval = new byte[(int) clob.length()];
                InputStream reader = clob.getAsciiStream();
                reader.read(retval);
                setBStatus(true);
                logRequest(values, "ARLLACD", "Failed Processing");
            }
        } catch (Exception ex) {
            logger.error(ex);
            setBStatus(true);
            logRequest(values, "ARLLACD", "Failed Processing");
        }
        return retval;
    }

    public byte[] build1021Response(HashMap values) {
        byte[] retval = null;
        setBStatus(false);
        try {
            CLOB clob = null;
            int success = 0;
            OracleCallableStatement stmt = (OracleCallableStatement) _conn.prepareCall("{? = call pkariel.fn_ARLACDL(?,?,?,?,?,?,?,?,?)}");

            stmt.registerOutParameter(1, -5);
            stmt.registerOutParameter(9, 2005);
            stmt.registerOutParameter(10, 2005);
            setStmtString(stmt, values, "MessageType", 2);
            setStmtString(stmt, values, "ProcCode", 3);
            setStmtString(stmt, values, "UniqueCode", 4);
            setStmtString(stmt, values, "BranchName", 5);
            setStmtString(stmt, values, "TxnDtTime", 6);
            setStmtString(stmt, values, "AcctNum", 7);
            setStmtString(stmt, values, "AcctCompCode", 8);
            stmt.execute();

            success = stmt.getInt(1);
            if (success == 0) {
                clob = stmt.getCLOB(10);
                retval = new byte[(int) clob.length()];
                InputStream reader = clob.getAsciiStream();
                reader.read(retval);
                logRequest(values, "ARLACDL", "Successully Processed");
            } else {
                clob = stmt.getCLOB(9);
                retval = new byte[(int) clob.length()];
                InputStream reader = clob.getAsciiStream();
                reader.read(retval);

                setBStatus(true);
                logRequest(values, "ARLACDL", "Failed Processing");
            }
        } catch (Exception ex) {
            logger.error(ex);
            logRequest(values, "ARLACDL", "Failed Processing");
            setBStatus(true);
        }
        return retval;
    }

    public byte[] buildBalanceResponse(HashMap values) {
        logger.info(values.get("MsgBody"));
        logger.info(values.get("MsgHeader"));
        logRequest(values, "BalanceReport", " values");
        byte[] retval = null;
        setBStatus(false);
        try {
            CLOB clob = null;
            int success = 0;
            OracleCallableStatement stmt = (OracleCallableStatement) _conn.prepareCall("{? = call GCPPROCESSOR.BalanceReporting(?,?,?)}");

            stmt.registerOutParameter(1, -5);
            stmt.registerOutParameter(4, 2005);

            setStmtString(stmt, values, "MsgHeader", 2);
            setStmtClob(stmt, values, "MsgBody", 3);

            stmt.execute();

            success = stmt.getInt(1);
            if (success == 0) {
                clob = stmt.getCLOB(4);
                retval = new byte[(int) clob.length()];
                InputStream reader = clob.getAsciiStream();
                reader.read(retval);
                logRequest(values, "BalanceReport", "Successully Processed");
            } else {
                clob = stmt.getCLOB(4);
                retval = new byte[(int) clob.length()];
                InputStream reader = clob.getAsciiStream();
                reader.read(retval);
                setBStatus(true);
                logger.error(new String(retval));
                logRequest(values, "BalanceReport", "Failed Processing");
            }
        } catch (Exception ex) {
            logger.error(ex);

            setBStatus(true);
            logRequest(values, "BalanceReport", "Failed Processing");
        }
        return retval;
    }

    public CLOB stringtoClob(String stringData) {
        CLOB tempClob = null;
        try {
            //clob = CLOB.createTemporary(_conn, false, 10);
            //clob.setString(1, stringData);

            tempClob = CLOB.createTemporary(_conn, true, CLOB.DURATION_SESSION);
            tempClob.open(CLOB.MODE_READWRITE); // open the temporary CLOB in readwrite mode to enable writing
            Writer tempClobWriter = tempClob.getCharacterOutputStream();    // get the output stream to write
            tempClobWriter.write(stringData);
        } catch (SQLException sqlException) {

            logger.info("Exception thrown in stringtoClob function : SQL Exception : " + sqlException);
            System.out.println("Exception thrown in stringtoClob function : SQL Exception : " + sqlException);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tempClob;
    }

    public byte[] buildBeneAcctVerf3(HashMap values) {
        logger.info(values.get("MsgHeader"));

        PreparedStatement prepstmt = null;
        HashMap resultH = (HashMap) values.get("MsgHeader");
        HashMap result = (HashMap) values.get("MsgBody");
        byte[] retval = null;

        Vector result4 = (Vector) result.get("BeneAcctDtl");

        Iterator iterator = result4.iterator();

        int success = 0;
        int t;
        while (iterator.hasNext()) {
            t = 0;
        }
        setBStatus(false);
        try {
            CLOB clob = null;
            OracleCallableStatement stmt = (OracleCallableStatement) _conn.prepareCall("{? = call GCPPROCESSOR.BeneAcctVerification(?,?,?)}");

            stmt.registerOutParameter(1, -5);
            stmt.registerOutParameter(4, 2005);

            setStmtString(stmt, values, "MsgHeader", 2);
            stmt.setCLOB(3, stringtoClob(values.get("MsgBody").toString()));

            stmt.execute();

            success = stmt.getInt(1);
            if (success == 0) {
                clob = stmt.getCLOB(4);
                retval = new byte[(int) clob.length()];
                InputStream reader = clob.getAsciiStream();
                reader.read(retval);
                logRequest(values, "Beneficairy Account Verification", "Successully Processed");
            } else {
                clob = stmt.getCLOB(4);
                retval = new byte[(int) clob.length()];
                InputStream reader = clob.getAsciiStream();
                reader.read(retval);
                setBStatus(true);
                logger.error(new String(retval));
                logRequest(values, "Beneficairy Account Verification", "Failed Processing");
            }
        } catch (SQLException ex) {
            logger.error(ex);
            setBStatus(true);
            logRequest(values, "Beneficairy Account Verification", "Failed Processing");
        } catch (Exception ex) {
            logger.error(ex);

            setBStatus(true);
            logRequest(values, "Beneficairy Account Verification", "Failed Processing");
        }
        return retval;
    }

    public byte[] buildBeneNameVerf(HashMap values) {

        logger.info(values.get("MsgBody"));
        logger.info(values.get("MsgHeader"));
        logRequest(values, "Beneficairy Name Verification", " values");

        HashMap MsgHeader = (HashMap) values.get("MsgHeader");
        String TxnCode = MsgHeader.get("TxnCode").toString();
        String ServiceId = MsgHeader.get("ServiceId").toString();
        String CMSTransID = "";
        String BankCode = "";
        String BeneAccountNmbr = "";
        String AppCode = "";

        //System.out.println("application >>> " + prop.getProperty("db.url"));
        HashMap result = (HashMap) values.get("MsgBody");
        byte[] retval = null;

        logger.info(result.get("BeneAcctDtl"));

        StringBuilder sb = new StringBuilder();
        sb.append("<Msg><MsgHeader><ServiceId>").append(ServiceId);
        sb.append("</ServiceId><TxnCode>1051</TxnCode></MsgHeader><MsgBody>");

        try {
            Vector result4 = (Vector) result.get("BeneAcctDtl");

            Iterator iterator = result4.iterator();

            int t = 0;
            while (iterator.hasNext()) {

                logger.info(result4.get(t));
                HashMap result1 = (HashMap) result4.get(t);

                CMSTransID = result1.get("CMSTransID").toString();
                BankCode = result1.get("BankCode").toString();
                BeneAccountNmbr = result1.get("BeneAccountNmbr").toString();
                AppCode = result1.get("AppCode").toString();

                if (AppCode.contains("ENG")) {

                    sb.append("<BeneAcctDtl><AppCode>GCPENG</AppCode><CMSTransID>");
                }
                if (AppCode.contains("EGH")) {

                    sb.append("<BeneAcctDtl><AppCode>GCPEGH</AppCode><CMSTransID>");
                }

                sb.append(CMSTransID).append("</CMSTransID><BeneAccountNmbr>").append(BeneAccountNmbr).append("</BeneAccountNmbr><BeneAccountName>");

                logger.info("CMSTransID :" + CMSTransID);
                logger.info("BankCode :" + BankCode);
                logger.info("BeneAccountNmbr :" + BeneAccountNmbr);
                iterator.next();
                t++;

                String BenName = NameEnquiry(BankCode, BeneAccountNmbr, AppCode).replaceAll("&", "AND").replaceAll(",", " ");
                
                logger.info("<<< NIPPS/Ghips Beneficiary Name >>> " + BenName);

                if (returnCode.equals("00")) {
                    sb.append(BenName).append("</BeneAccountName><BankCode>").append(BankCode).append("</BankCode><StatusCode>").append(returnCode).append("</StatusCode><ErrorCode></ErrorCode><ErrorReason></ErrorReason>");
                    sb.append("</BeneAcctDtl>");
                } else {
                    sb.append("</BeneAccountName><BankCode>").append(BankCode).append("</BankCode><StatusCode>99</StatusCode><ErrorCode>").append(returnCode).append("</ErrorCode><ErrorReason>").append(BenName).append("</ErrorReason>");
                    sb.append("</BeneAcctDtl>");
                }
            }
        } catch (ClassCastException ex) {

            System.out.println("***************************");
            System.out.println("** SINGLE VALIDATION CALL**");
            System.out.println("***************************");

            logger.info("error --------------------");
            HashMap result4 = (HashMap) result.get("BeneAcctDtl");

            AppCode = result4.get("AppCode").toString();

            System.out.println("AppCode >>> " + AppCode);

            if (AppCode.contains("ENG")) {
                sb.append("<BeneAcctDtl><AppCode>GCPENG</AppCode><CMSTransID>");

            }
            if (AppCode.contains("EGH")) {
                sb.append("<BeneAcctDtl><AppCode>GCPEGH</AppCode><CMSTransID>");

            }

            CMSTransID = result4.get("CMSTransID").toString();
            BankCode = result4.get("BankCode").toString();
            BeneAccountNmbr = result4.get("BeneAccountNmbr").toString();

            sb.append(CMSTransID).append("</CMSTransID><BeneAccountNmbr>").append(BeneAccountNmbr).append("</BeneAccountNmbr><BeneAccountName>");

            logger.info("CMSTransID :" + CMSTransID);
            logger.info("BankCode :" + BankCode);
            logger.info("BeneAccountNmbr :" + BeneAccountNmbr);

            sb.append(NameEnquiry(BankCode, BeneAccountNmbr, AppCode).replaceAll("&", "AND").replaceAll(",", "")).append("</BeneAccountName><BankCode>").append(BankCode).append("</BankCode><StatusCode>").append(returnCode).append("</StatusCode><ErrorCode></ErrorCode><ErrorReason></ErrorReason>");
            sb.append("</BeneAcctDtl>");
        }
        sb.append("</MsgBody></Msg>");

        System.out.println("Validation Response >>> " + sb.toString());
        logger.info("Validation Response >>> " + sb.toString());

        retval = sb.toString().getBytes();

        return retval;
    }

    public String NameEnquiry(String DestinationBankCode, String acc, String AppCode) {

        String nips_validation_url = "";
        String ghipss_validation_url = "";

        try (InputStream input = new FileInputStream("Initialization.properties")) {

            Properties prop = new Properties();

            // load a properties file
            prop.load(input);

            // get the property value and print it out
            nips_validation_url = prop.getProperty("NIPS_URL");
            ghipss_validation_url = prop.getProperty("GHIPSS_URL");

            System.out.println("GHIPSS_URL >>> " + ghipss_validation_url);
            System.out.println("NIPS_URL >>> " + prop.getProperty("NIPS_URL"));

            logger.info("GHIPSS_URL >>> " + ghipss_validation_url);
            logger.info("NIPS_URL >>> " + prop.getProperty("NIPS_URL"));

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        SetSystemSSLProperties.getInstance().process();
        String Name = "";
        String NIPDesBankCode = "";

        String wsResult = "";
        JsonObject wsResult2 = null;
        String response_code = "";
        String response_msg = "";

        try {

            String scode = "RIBNIP";
            String spwd = "ribnipuat";

            String SenderBankCode = "000010";

            String datetime = formatter.format(newDate);
            String sequence = "123456789016";

            String requestID = SenderBankCode + datetime + sequence;

            System.out.println("========================================================");
            System.out.println("====== Session ID  >>> " + requestID);
            System.out.println("========================================================");

            logger.info("=======================================================");
            logger.info("====== Session ID  >>> " + requestID);
            logger.info("=======================================================");

            String data = acc + requestID + scode + spwd;
            MessageDigest digest = MessageDigest.getInstance("SHA-512");

            byte[] hashBytes = data.getBytes("UTF-8");

            byte[] messageDigest = digest.digest(hashBytes);

            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2) {
                    h = "0" + h;
                }
                sb.append(h);
            }
            String requestToken = sb.toString();

            //if (AppCode.equals("ENGGCP")) {
            if (AppCode.contains("ENG")) {

                System.out.println("*****************************");
                System.out.println("***NIPS NAME VALIDATION CALL*");
                System.out.println("*****************************");

                logger.info("******************************");
                logger.info("***NIPS NAME VALIDATION CALL**");
                logger.info("******************************");

//                transHT.setRequestToken(requestToken);
                GregorianCalendar gcal = new GregorianCalendar();
                XMLGregorianCalendar xgcal = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
                System.out.println(xgcal);

                if (DestinationBankCode.substring(0, 3).compareToIgnoreCase("044") == 0) {
                    NIPDesBankCode = "000014";
                } else if (DestinationBankCode.substring(0, 3).compareToIgnoreCase("023") == 0) {
                    NIPDesBankCode = "000009";
                } else if (DestinationBankCode.substring(0, 3).compareToIgnoreCase("084") == 0) {
                    NIPDesBankCode = "000019";
                } else if (DestinationBankCode.substring(0, 3).compareToIgnoreCase("214") == 0) {
                    NIPDesBankCode = "000003";
                } else if (DestinationBankCode.substring(0, 3).compareToIgnoreCase("070") == 0) {
                    NIPDesBankCode = "000007";
                } else if (DestinationBankCode.substring(0, 3).compareToIgnoreCase("011") == 0) {
                    NIPDesBankCode = "000016";
                } else if (DestinationBankCode.substring(0, 3).compareToIgnoreCase("058") == 0) {
                    NIPDesBankCode = "000013";
                } else if (DestinationBankCode.substring(0, 3).compareToIgnoreCase("030") == 0) {
                    NIPDesBankCode = "000020";
                } else if (DestinationBankCode.substring(0, 3).compareToIgnoreCase("301") == 0) {
                    NIPDesBankCode = "000006";
                } else if (DestinationBankCode.substring(0, 3).compareToIgnoreCase("082") == 0) {
                    NIPDesBankCode = "000002";
                } else if (DestinationBankCode.substring(0, 3).compareToIgnoreCase("076") == 0) {
                    NIPDesBankCode = "000008";
                } else if (DestinationBankCode.substring(0, 3).compareToIgnoreCase("221") == 0) {
                    NIPDesBankCode = "000012";
                } else if (DestinationBankCode.substring(0, 3).compareToIgnoreCase("068") == 0) {
                    NIPDesBankCode = "000021";
                } else if (DestinationBankCode.substring(0, 3).compareToIgnoreCase("232") == 0) {
                    NIPDesBankCode = "000001";
                } else if (DestinationBankCode.substring(0, 3).compareToIgnoreCase("033") == 0) {
                    NIPDesBankCode = "000004";
                } else if (DestinationBankCode.substring(0, 3).compareToIgnoreCase("215") == 0) {
                    NIPDesBankCode = "000011";
                } else if (DestinationBankCode.substring(0, 3).compareToIgnoreCase("035") == 0) {
                    NIPDesBankCode = "000017";
                } else if (DestinationBankCode.substring(0, 3).compareToIgnoreCase("057") == 0) {
                    NIPDesBankCode = "000015";
                } else if (DestinationBankCode.substring(0, 3).compareToIgnoreCase("032") == 0) {
                    NIPDesBankCode = "000018";
                } else if (DestinationBankCode.substring(0, 3).compareToIgnoreCase("063") == 0) {
                    NIPDesBankCode = "000005";
                }
                logger.info("NIPDesBankCode :" + NIPDesBankCode);

                String RQ = NipRequest.start(requestID, requestToken, SenderBankCode, NIPDesBankCode, acc);

                logger.info("NEW ESB NIPSS Name Inquiry Request");
                logger.info("NEW ESB NIPSS Name Inquiry Request >>> " + RQ);

                System.out.println("NEW ESB NIPSS Name Inquiry Request >>> " + RQ);

                wsResult = RestPost.post(nips_validation_url, RQ, "POST", "HTTPS");

                logger.info("wsResult >>> " + wsResult);

                //convert string to object
                JAXBContext jaxbContext2 = JAXBContext.newInstance(IBTNameInquiryResponse.class);
                Unmarshaller unmarshaller2 = jaxbContext2.createUnmarshaller();
                StringReader reader2 = new StringReader(wsResult);
                IBTNameInquiryResponse resp2 = (IBTNameInquiryResponse) unmarshaller2.unmarshal(reader2);

                System.out.println("*** NIPS ResponseCode >>> " + resp2.getHostHeaderInfo().getResponseCode());
                logger.info("*** NIPS ResponseCode >>> " + resp2.getHostHeaderInfo().getResponseCode());

                if (resp2.getHostHeaderInfo().getResponseCode().compareToIgnoreCase("000") == 0) {

                    Name = resp2.getAccountName();
                    logger.info("Bene NAme >>> " + Name);

                    returnCode = "00";  ///resp2.getHostHeaderInfo().getResponseCode();
                    logger.info("ACC>>>>>:" + acc + "  " + "Response code>>>>>>" + resp2.getHostHeaderInfo().getResponseCode() + " " + "NameResponse>>>> " + Name);

                    logger.info("*****************************************************");
                    logger.info("*** NIPS NAME >> " + resp2.getAccountName());
                    logger.info("*****************************************************");

                    System.out.println("*****************************************************");
                    System.out.println("*** NIPS NAME >> " + resp2.getAccountName());
                    System.out.println("*****************************************************");
                } else {

                    logger.info("ACC>>>>>:" + acc + "  " + "Response code>>>>>>" + resp2.getHostHeaderInfo().getResponseCode() + " " + "NameResponse>>>>" + resp2.getAccountName());
                    Name = "Name Not Found";
                    returnCode = resp2.getHostHeaderInfo().getResponseCode();

                }
            }
            else if (AppCode.contains("EGH")) {

                System.out.println("********************************");
                System.out.println("***GHIPSS NAME VALIDATION CALL**");
                System.out.println("********************************");

                logger.info("********************************");
                logger.info("***GHIPSS NAME VALIDATION CALL**");
                logger.info("********************************");

                // MAPPING ACH CODES FROM OMNI TO GHIPSS BANKCODES    
                if ((DestinationBankCode.compareToIgnoreCase("GH280100")) == 0) {
                    NIPDesBankCode = "300329";  //Access Bank
                } else if ((DestinationBankCode.compareToIgnoreCase("GH030100")) == 0) {
                    NIPDesBankCode = "300303"; //Barclays Bank
                } else if ((DestinationBankCode.compareToIgnoreCase("GH050100")) == 0) {
                    NIPDesBankCode = "300305"; //NIB (National Investment Bank)
                } else if ((DestinationBankCode.compareToIgnoreCase("GH080100")) == 0) {
                    NIPDesBankCode = "300307"; //ADB
                } else if ((DestinationBankCode.compareToIgnoreCase("GH110100")) == 0) {
                    NIPDesBankCode = "300310"; //HFC
                } else if ((DestinationBankCode.compareToIgnoreCase("GH120100")) == 0) {
                    NIPDesBankCode = "300311"; //Zenith Bank
                } else if ((DestinationBankCode.compareToIgnoreCase("GH190100")) == 0) {
                    NIPDesBankCode = "300318"; //Stanbic Bank
                } else if ((DestinationBankCode.compareToIgnoreCase("GH180100")) == 0) {
                    NIPDesBankCode = "300317"; //Prudential Bank
                } else if ((DestinationBankCode.compareToIgnoreCase("GH170100")) == 0) {
                    NIPDesBankCode = "300316"; //FAB
                } else if ((DestinationBankCode.compareToIgnoreCase("GH140100")) == 0) {
                    NIPDesBankCode = "300313"; //Cal Bank
                } else if ((DestinationBankCode.compareToIgnoreCase("GH060100")) == 0) {
                    NIPDesBankCode = "300325"; //United Bank for Afrcia (UBA)
                } else if ((DestinationBankCode.compareToIgnoreCase("GH320100")) == 0) {
                    NIPDesBankCode = "300348"; //GN BANK
                } else if ((DestinationBankCode.compareToIgnoreCase("GH340100")) == 0) {
                    NIPDesBankCode = "300357"; //SOVEREIGN BANK
                } else if ((DestinationBankCode.compareToIgnoreCase("GH330100")) == 0) {
                    NIPDesBankCode = "300334"; // First National Bank
                } else if ((DestinationBankCode.compareToIgnoreCase("290100")) == 0) {
                    NIPDesBankCode = "300330"; //ENERGY BANK
                } else if ((DestinationBankCode.compareToIgnoreCase("GH300100")) == 0) {
                    NIPDesBankCode = "300331"; //THE ROYAL BANK 
                } else if ((DestinationBankCode.compareToIgnoreCase("GH270100")) == 0) {
                    NIPDesBankCode = "300324"; //BSIC
                } else if ((DestinationBankCode.compareToIgnoreCase("GH240100")) == 0) {
                    NIPDesBankCode = "300323"; //FIDELITY BANK LTD
                } else if ((DestinationBankCode.compareToIgnoreCase("GH070100")) == 0) {
                    NIPDesBankCode = "300306"; //Apex Bank
                } else if ((DestinationBankCode.compareToIgnoreCase("GH090100")) == 0) {
                    NIPDesBankCode = "300308"; // SG Bank
                } else if ((DestinationBankCode.compareToIgnoreCase("GH020100")) == 0) {
                    NIPDesBankCode = "300302"; //  Standard Chartered Bank
                } else if ((DestinationBankCode.compareToIgnoreCase("GH300348")) == 0) {
                    NIPDesBankCode = "300348"; //  Standard Chartered Bank
                }

                if (NIPDesBankCode.equals("")) {

                    NIPDesBankCode = DestinationBankCode; //NIPDesBankCode Not Found
                }

                String gipsReq = GhipssRequest.start(requestID, requestToken, NIPDesBankCode, acc);

                System.out.println("Ghipss Request >>> " + gipsReq);
                logger.info("Ghipss Request >>> " + gipsReq);

                wsResult2 = RestPost2.post(ghipss_validation_url, gipsReq, "POST", "HTTPS");
                
                System.out.println("Ghipss Reponse >>> " + wsResult2);

                String hdr = wsResult2.get("hostHeaderInfo").toString();
                JsonObject hd = (JsonObject) new JsonParser().parse(hdr);
                response_code = Strip.getStrip(hd.get("responseCode").toString());//responseMessage
                response_msg = Strip.getStrip(hd.get("responseMessage").toString());
                Name = Strip.getStrip(wsResult2.get("accountName").toString());

                System.out.println("********************************");
                System.out.println("***GHIPSS NAME >>> " + Name);
                System.out.println("********************************");

                logger.info("********************************");
                logger.info("***GHIPSS NAME >>> " + Name);
                logger.info("********************************");

                System.out.println("ResponseCode >>> " + response_code);
                logger.info("ResponseCode >>> " + response_code);

                System.out.println("ResponseMSg >>> " + response_msg);
                logger.info("ResponseMSg >>> " + response_msg);

                if (response_code.equals("000")) {

                    Name = Strip.getStrip(wsResult2.get("accountName").toString());
                    returnCode = "00";

                } else {

                    returnCode = "99";
                    Name = "Name Not Found";
                }

            }
        } catch (Exception ex) {
            System.out.println(ex);
            ex.printStackTrace();
            logger.info(ex);
            returnCode = "99";
            Name = "Name Not Found";
        }
        return Name;
    }

    public byte[] buildBeneAcctVerf2(HashMap values) {
        System.out.println("Inside Flexcube Validation");
        logger.info("Flexcube Validation");
        logger.info("msg_header" + values.get("MsgHeader"));

        logger.info("msg_body" + values.get("MsgBody"));

        System.out.println("msg_header" + values.get("MsgHeader"));
        System.out.println("msg_body" + values.get("MsgBody"));

        byte[] retval = null;
        setBStatus(false);
        try {
            CLOB clob = null;

            int success = 0;
            // OracleCallableStatement stmt = (OracleCallableStatement)_conn.prepareCall("{? = call GCPPROCESSOR.BeneAcctVerification(?,?,?)}");
            OracleCallableStatement stmt = (OracleCallableStatement) _conn.prepareCall("{? = call GCPPROCESSORPLUS.BeneAcctVerification(?,?,?)}");

            stmt.registerOutParameter(1, -5);
            stmt.registerOutParameter(4, 2005);

            setStmtString(stmt, values, "MsgHeader", 2);
            stmt.setCLOB(3, stringtoClob(values.get("MsgBody").toString()));
            setStmtString(stmt, values, "MsgBody", 3);

            stmt.execute();

            success = stmt.getInt(1);
            System.out.println("result >>> " + success);

            if (success == 0) {
                clob = stmt.getCLOB(4);
                retval = new byte[(int) clob.length()];
                InputStream reader = clob.getAsciiStream();
                reader.read(retval);

                logRequest(values, "Beneficairy Account Verification", "Successully Processed");
                System.out.println("Beneficairy Account Verification Successully Processed");
            } else {
                clob = stmt.getCLOB(4);
                retval = new byte[(int) clob.length()];
                InputStream reader = clob.getAsciiStream();
                reader.read(retval);
                setBStatus(true);
                logger.error(new String(retval));
                logRequest(values, "Beneficairy Account Verification", "Failed Processing");
            }
        } catch (SQLException ex) {
            logger.error(ex);
            setBStatus(true);
            logRequest(values, "Beneficairy Account Verification", "Failed Processing");
        } catch (Exception ex) {
            logger.error(ex);

            setBStatus(true);
            logRequest(values, "Beneficairy Account Verification", "Failed Processing");
        }

        System.out.println("CBA Response >>> " + new String(retval));

        return retval;
    }

    //changed on 27-06-2019 Percy
//    public byte[] buildBeneAcctVerf2(HashMap values) {
//        logger.info(values.get("MsgHeader"));
//        
//        System.out.println("*********************6666666666");
//
//        byte[] retval = null;
//        setBStatus(false);
//        try {
//            CLOB clob = null;
//            int success = 0;
//            OracleCallableStatement stmt = (OracleCallableStatement) _conn.prepareCall("{? = call GCPPROCESSOR.BeneAcctVerification(?,?,?)}");
//
//            stmt.registerOutParameter(1, -5);
//            stmt.registerOutParameter(4, 2005);
//
//            setStmtString(stmt, values, "MsgHeader", 2);
//            //stmt.setCLOB(3, stringtoClob(values.get("MsgBody").toString()));
//            setStmtString(stmt, values, "MsgBody", 3);
//
//            stmt.execute();
//
//            success = stmt.getInt(1);
//            logger.info("result >> " + success);
//             System.out.println("result >> " + success);
//            if (success == 0) {
//                clob = stmt.getCLOB(4);
//                retval = new byte[(int) clob.length()];
//                InputStream reader = clob.getAsciiStream();
//                reader.read(retval);
//                logRequest(values, "Beneficairy Account Verification", "Successully Processed");
//                System.out.println ("Beneficairy Account Verification");
//                
//                System.out.println("response >>> " + new String(retval));
//                
//            } else {
//                clob = stmt.getCLOB(4);
//                retval = new byte[(int) clob.length()];
//                InputStream reader = clob.getAsciiStream();
//                reader.read(retval);
//                setBStatus(true);
//                logger.error(new String(retval));
//                logRequest(values, "Beneficairy Account Verification", "Failed Processing");
//            }
//        } catch (SQLException ex) {
//            logger.error(ex);
//            setBStatus(true);
//            logRequest(values, "Beneficairy Account Verification", "Failed Processing");
//        } catch (Exception ex) {
//            logger.error(ex);
//            ex.printStackTrace();
//
//            setBStatus(true);
//            logRequest(values, "Beneficairy Account Verification", "Failed Processing");
//        }
//
//        System.out.println("<< CBA >> "+ new String(retval));
//        return retval;
//    }
//     public byte[] buildBeneAcctVerf2(HashMap values) {
//
//        System.out.println("Inside Flexcube Validation");
//        logger.info("Flexcube Validation");
//        logger.info("msg_header" + values.get("MsgHeader"));
//
//        logger.info("msg_body" + values.get("MsgBody"));
//
//        System.out.println("msg_header" + values.get("MsgHeader"));
//        System.out.println("msg_body" + values.get("MsgBody"));
//
//        byte[] retval = null;
//        setBStatus(false);
//        try {
//            CLOB clob = null;
////            int success = 0;
////            OracleCallableStatement stmt = (OracleCallableStatement) _conn.prepareCall("{? = call GCPPROCESSOR.BeneAcctVerification(?,?,?)}");
////
////            stmt.registerOutParameter(1, -5);
////            stmt.registerOutParameter(4, 2005);
////
////            setStmtString(stmt, values, "MsgHeader", 2);
////            stmt.setCLOB(3, stringtoClob(values.get("MsgBody").toString()));
////
////            stmt.execute();
//
//            int success = 0;
//            oracle.jdbc.OracleCallableStatement stmt = (OracleCallableStatement) _conn.prepareCall("{? = call GCPPROCESSOR.BeneAcctVerification("
//                    + "?,"
//                    + "?,"
//                    + "?"
//                    + ")}");
//            stmt.registerOutParameter(1, java.sql.Types.BIGINT);
//            stmt.registerOutParameter(4, java.sql.Types.CLOB);
//            //stmt.registerOutParameter(10, java.sql.Types.CLOB);
//
//            setStmtString(stmt, values, "MsgHeader", 2);
//            stmt.setCLOB(3, stringtoClob(values.get("MsgBody").toString()));
//            setStmtString(stmt, values, "MsgBody", 3);
//
//            stmt.execute();
//
//            success = stmt.getInt(1);
//            System.out.println("result >>> " + success);
//
//            if (success == 0) {
//                clob = stmt.getCLOB(4);
//                retval = new byte[(int) clob.length()];
//                InputStream reader = clob.getAsciiStream();
//                reader.read(retval);
//
//                logRequest(values, "Beneficairy Account Verification", "Successully Processed");
//                System.out.println("Beneficairy Account Verification Successully Processed");
//
//            } else {
//                clob = stmt.getCLOB(4);
//                retval = new byte[(int) clob.length()];
//                InputStream reader = clob.getAsciiStream();
//                reader.read(retval);
//                setBStatus(true);
//                logger.error(new String(retval));
//                logRequest(values, "Beneficairy Account Verification", "Failed Processing");
//            }
//        } catch (SQLException ex) {
//            logger.error(ex);
//            setBStatus(true);
//            logRequest(values, "Beneficairy Account Verification", "Failed Processing");
//        } catch (Exception ex) {
//            logger.error(ex);
//
//            setBStatus(true);
//            logRequest(values, "Beneficairy Account Verification", "Failed Processing");
//        }
//
//        System.out.println("<<< CBA Response >>> " + new String(retval));
//
//        return retval;
//    }
    public byte[] buildBeneAcctVerf(HashMap values) {
        logger.info(values.get("MsgBody"));
        logger.info(values.get("MsgHeader"));
        logRequest(values, "Beneficairy Account Verification", " values");
        byte[] retval = null;
        setBStatus(false);
        try {
            CLOB clob = null;
            int success = 0;
            OracleCallableStatement stmt = (OracleCallableStatement) _conn.prepareCall("{? = call GCPPROCESSOR.BeneAcctVerification(?,?,?)}");

            stmt.registerOutParameter(1, -5);
            stmt.registerOutParameter(4, 2005);

            setStmtString(stmt, values, "MsgHeader", 2);
            setStmtString(stmt, values, "MsgBody", 3);

            stmt.execute();

            success = stmt.getInt(1);
            if (success == 0) {
                clob = stmt.getCLOB(4);
                retval = new byte[(int) clob.length()];
                InputStream reader = clob.getAsciiStream();
                reader.read(retval);
                logRequest(values, "Beneficairy Account Verification", "Successully Processed");
            } else {
                clob = stmt.getCLOB(4);
                retval = new byte[(int) clob.length()];
                InputStream reader = clob.getAsciiStream();
                reader.read(retval);
                setBStatus(true);
                logger.error(new String(retval));
                logRequest(values, "Beneficairy Account Verification", "Failed Processing");
            }
        } catch (SQLException ex) {
            logger.error(ex);
            setBStatus(true);
            logRequest(values, "Beneficairy Account Verification", "Failed Processing");
        } catch (Exception ex) {
            logger.error(ex);

            setBStatus(true);
            logRequest(values, "Beneficairy Account Verification", "Failed Processing");
        }
        return retval;
    }

    public byte[] buildFxContractRate(HashMap values) {
        logger.info(values.get("MsgBody"));
        logger.info(values.get("MsgHeader"));
        logRequest(values, " FX Contract Rate Request", " values");
        byte[] retval = null;
        setBStatus(false);
        try {
            CLOB clob = null;
            int success = 0;
            OracleCallableStatement stmt = (OracleCallableStatement) _conn.prepareCall("{? = call GCPPROCESSOR.FxContractRate(?,?,?)}");

            stmt.registerOutParameter(1, -5);
            stmt.registerOutParameter(4, 2005);

            setStmtString(stmt, values, "MsgHeader", 2);
            setStmtString(stmt, values, "MsgBody", 3);

            stmt.execute();

            success = stmt.getInt(1);
            if (success == 0) {
                clob = stmt.getCLOB(4);
                retval = new byte[(int) clob.length()];
                InputStream reader = clob.getAsciiStream();
                reader.read(retval);
                logRequest(values, " FX Contract Rate Request", "Successully Processed");
            } else {
                logRequest(values, " FX Contract Rate Request", "Not Found ");
            }
        } catch (SQLException ex) {
            logger.error(ex);
            setBStatus(true);
            logRequest(values, "FX Contract Rate Request", "Failed Processing");
        } catch (Exception ex) {
            logger.error(ex);

            setBStatus(true);
            logRequest(values, " FX Contract Rate Request", "Failed Processing");
        }
        return retval;
    }

    public byte[] buildBalanceResponseEOD(HashMap values) {
        logger.info(values.get("MsgBody"));
        logger.info(values.get("MsgHeader"));
        logger.info(xmlMsgReceiver1.getCorrID());
        logger.info(xmlMsgReceiver1.getMessID());
        logRequest(values, "End Of Day Balance Request", " values");
        byte[] retval = null;
        setBStatus(false);
        try {
            CLOB clob = null;
            int success = 0;
            OracleCallableStatement stmt = (OracleCallableStatement) _conn.prepareCall("{? = call GCPPROCESSOR.BalanceReportingtanked(?,?,?,?,?)}");

            stmt.registerOutParameter(1, -5);
            stmt.registerOutParameter(6, 2005);

            setStmtString(stmt, values, "MsgHeader", 2);
            setStmtString(stmt, values, "MsgBody", 3);
            stmt.setString(4, xmlMsgReceiver1.getCorrID());
            stmt.setString(5, xmlMsgReceiver1.getMessID());

            stmt.execute();

            success = stmt.getInt(1);
            if (success == 0) {
                logRequest(values, " End Of Day Balance Request", "Successully Cached");
            } else {
                clob = stmt.getCLOB(6);
                retval = new byte[(int) clob.length()];
                InputStream reader = clob.getAsciiStream();
                reader.read(retval);
                setBStatus(true);
                logger.error(new String(retval));
                logRequest(values, " End Of Day Balance Request", "Failed Caching");
            }
        } catch (Exception ex) {
            logger.error(ex);

            setBStatus(true);
            logRequest(values, " End Of Day Balance Requestscassa", "Failed Caching");
        }
        return retval;
    }

    public byte[] buildActivityResponse(HashMap values) {
        logger.info(values.get("MsgBody"));
        logger.info(values.get("MsgHeader"));
        logRequest(values, "BalanceReport", " values");
        byte[] retval = null;
        setBStatus(false);
        try {
            CLOB clob = null;
            int success = 0;
            OracleCallableStatement stmt = (OracleCallableStatement) _conn.prepareCall("{? = call GCPPROCESSOR.ActivityReport(?,?,?)}");

            stmt.registerOutParameter(1, -5);
            stmt.registerOutParameter(4, 2005);

            setStmtString(stmt, values, "MsgHeader", 2);
            setStmtString(stmt, values, "MsgBody", 3);

            stmt.execute();

            success = stmt.getInt(1);
            if (success == 0) {
                clob = stmt.getCLOB(4);
                retval = new byte[(int) clob.length()];
                InputStream reader = clob.getAsciiStream();
                reader.read(retval);
                logRequest(values, "ActivityReport", "Successully Processed");
            } else {
                clob = stmt.getCLOB(4);
                retval = new byte[(int) clob.length()];
                InputStream reader = clob.getAsciiStream();
                reader.read(retval);
                setBStatus(true);
                logger.error(new String(retval));
                logRequest(values, "ActivityReport", "Failed Processing");
            }
        } catch (Exception ex) {
            logger.error(ex);

            setBStatus(true);
            logRequest(values, "ActivityReport", "Failed Processing");
        }
        return retval;
    }

    public byte[] buildAccountingHandoffResponse(HashMap values) {
        logger.info(values.get("MsgBody"));
        logger.info(values.get("MsgHeader"));
        logRequest(values, "AccountingHandoff", " values");
        byte[] retval = null;
        setBStatus(false);
        try {
            CLOB clob = null;
            int success = 0;
            OracleCallableStatement stmt = (OracleCallableStatement) _conn.prepareCall("{? = call GCPPROCESSOR.AccountingHandoff(?,?,?)}");

            stmt.registerOutParameter(1, -5);
            stmt.registerOutParameter(4, 2005);

            setStmtString(stmt, values, "MsgHeader", 2);
            setStmtString(stmt, values, "MsgBody", 3);

            stmt.execute();

            success = stmt.getInt(1);
            if (success == 0) {
                clob = stmt.getCLOB(4);
                retval = new byte[(int) clob.length()];
                InputStream reader = clob.getAsciiStream();
                reader.read(retval);
                logRequest(values, "AccountingHandoff", "Successully Processed");
            } else {
                clob = stmt.getCLOB(4);
                retval = new byte[(int) clob.length()];
                InputStream reader = clob.getAsciiStream();
                reader.read(retval);
                setBStatus(true);
                logger.error(new String(retval));
                logRequest(values, "AccountingHandoff", "Failed Processing");
            }
        } catch (Exception ex) {
            logger.error(ex);

            setBStatus(true);
            logRequest(values, "AccountingHandoff", "Failed Processing");
        }
        return retval;
    }

    public byte[] buildScheduleDownloadResponse(HashMap values) {
        logger.info(values.get("MsgBody"));
        logger.info(values.get("MsgHeader"));

        byte[] retval = null;
        setBStatus(false);
        try {
            CLOB clob = null;
            int success = 0;
            OracleCallableStatement stmt = (OracleCallableStatement) _conn.prepareCall("{? = call GCPPROCESSOR.ScheduleDownload(?,?,?)}");

            stmt.registerOutParameter(1, -5);
            stmt.registerOutParameter(4, 2005);

            setStmtString(stmt, values, "MsgHeader", 2);
            setStmtString(stmt, values, "MsgBody", 3);

            stmt.execute();

            success = stmt.getInt(1);
            if (success == 0) {
                clob = stmt.getCLOB(4);
                retval = new byte[(int) clob.length()];
                InputStream reader = clob.getAsciiStream();
                reader.read(retval);
                logRequest(values, "ScheduleDownload", "Successully Processed");
            } else {
                clob = stmt.getCLOB(4);
                retval = new byte[(int) clob.length()];
                InputStream reader = clob.getAsciiStream();
                reader.read(retval);
                setBStatus(true);
                logger.error(new String(retval));
                logRequest(values, "ScheduleDownload", "Failed Processing");
            }
        } catch (Exception ex) {
            logger.error(ex);

            setBStatus(true);
            logRequest(values, "ScheduleDownload", "Failed Processing");
        }
        return retval;
    }

    public byte[] buildStopPayResponse(HashMap values) {
        logger.info(values.get("MsgBody"));
        logger.info(values.get("MsgHeader"));

        byte[] retval = null;
        setBStatus(false);
        try {
            CLOB clob = null;
            int success = 0;
            OracleCallableStatement stmt = (OracleCallableStatement) _conn.prepareCall("{? = call GCPPROCESSOR.Stoppay(?,?,?)}");

            stmt.registerOutParameter(1, -5);
            stmt.registerOutParameter(4, 2005);

            setStmtString(stmt, values, "MsgHeader", 2);
            setStmtString(stmt, values, "MsgBody", 3);

            stmt.execute();

            success = stmt.getInt(1);
            if (success == 0) {
                clob = stmt.getCLOB(4);
                retval = new byte[(int) clob.length()];
                InputStream reader = clob.getAsciiStream();
                reader.read(retval);
                logRequest(values, "StopPay Confirmation", "Successully Processed");
            } else {
                clob = stmt.getCLOB(4);
                retval = new byte[(int) clob.length()];
                InputStream reader = clob.getAsciiStream();
                reader.read(retval);
                setBStatus(true);
                logger.error(new String(retval));
                logRequest(values, "StopPay Confirmation", "Failed Processing");
            }
        } catch (Exception ex) {
            logger.error(ex);

            setBStatus(true);
            logRequest(values, "StopPay Confirmation", "Failed Processing");
        }
        return retval;
    }

    public byte[] buildGLTransactionResponse(HashMap values) {
        logger.info(values.get("MsgBody"));
        logger.info(values.get("MsgHeader"));

        byte[] retval = null;
        setBStatus(false);
        try {
            CLOB clob = null;
            int success = 0;
            OracleCallableStatement stmt = (OracleCallableStatement) _conn.prepareCall("{? = call GCPPROCESSOR.TransactionRequest(?,?,?)}");

            stmt.registerOutParameter(1, -5);
            stmt.registerOutParameter(4, 2005);

            setStmtString(stmt, values, "MsgHeader", 2);
            setStmtString(stmt, values, "MsgBody", 3);

            stmt.execute();

            success = stmt.getInt(1);
            if (success == 0) {
                clob = stmt.getCLOB(4);
                retval = new byte[(int) clob.length()];
                InputStream reader = clob.getAsciiStream();
                reader.read(retval);
                logRequest(values, "GL Transaction", "Successully Processed");
            } else {
                clob = stmt.getCLOB(4);
                retval = new byte[(int) clob.length()];
                InputStream reader = clob.getAsciiStream();
                reader.read(retval);
                setBStatus(true);
                logger.error(new String(retval));
                logRequest(values, "GL Transaction", "Failed Processing");
            }
        } catch (Exception ex) {
            logger.error(ex);

            setBStatus(true);
            logRequest(values, "GL Transaction", "Failed Processing");
        }
        return retval;
    }

    public byte[] buildIssuanceUpdateResponse(HashMap values) {
        logger.info(values.get("MsgBody"));
        logger.info(values.get("MsgHeader"));

        byte[] retval = null;
        setBStatus(false);
        try {
            CLOB clob = null;
            int success = 0;
            OracleCallableStatement stmt = (OracleCallableStatement) _conn.prepareCall("{? = call GCPPROCESSOR.IssuanceUpdate(?,?,?)}");

            stmt.registerOutParameter(1, -5);
            stmt.registerOutParameter(4, 2005);

            setStmtString(stmt, values, "MsgHeader", 2);
            setStmtString(stmt, values, "MsgBody", 3);

            stmt.execute();

            success = stmt.getInt(1);
            if (success == 0) {
                clob = stmt.getCLOB(4);
                retval = new byte[(int) clob.length()];
                InputStream reader = clob.getAsciiStream();
                reader.read(retval);
                logRequest(values, "IssuanceUpdate", "Successully Processed");
            } else {
                clob = stmt.getCLOB(4);
                retval = new byte[(int) clob.length()];
                InputStream reader = clob.getAsciiStream();
                reader.read(retval);
                setBStatus(true);
                logger.error(new String(retval));
                logRequest(values, "IssuanceUpdate", "Failed Processing");
            }
        } catch (Exception ex) {
            logger.error(ex);

            setBStatus(true);
            logRequest(values, "IssuanceUpdate", "Failed Processing");
        }
        return retval;
    }

    public byte[] buildARLTDPLResponse(HashMap values) {
        byte[] retval = null;
        setBStatus(false);
        try {
            CLOB clob = null;
            String retError = "";
            int success = 0;
            OracleCallableStatement stmt = (OracleCallableStatement) _conn.prepareCall("{? = call pkariel.fn_ARLTDPL(?,?,?,?,?,?,?,?)}");

            stmt.registerOutParameter(1, -5);
            stmt.registerOutParameter(8, 2005);
            stmt.registerOutParameter(9, 2005);
            setStmtString(stmt, values, "MessageType", 2);
            setStmtString(stmt, values, "ProcCode", 3);
            setStmtString(stmt, values, "UniqueCode", 4);
            setStmtString(stmt, values, "BranchName", 5);
            setStmtString(stmt, values, "TxnDtTime", 6);
            setStmtString(stmt, values, "CIN", 7);
            stmt.execute();
            success = stmt.getInt(1);
            if (success == 0) {
                clob = stmt.getCLOB(9);
                retval = new byte[(int) clob.length()];
                InputStream reader = clob.getAsciiStream();
                reader.read(retval);
                logRequest(values, "ARLTDPL", "Successfully Processed");
            } else {
                clob = stmt.getCLOB(8);
                retval = new byte[(int) clob.length()];
                InputStream reader = clob.getAsciiStream();
                reader.read(retval);
                setBStatus(true);
                logger.error(new String(retval));
                logRequest(values, "ARLTDPL", "Failed Processing");
            }
        } catch (Exception ex) {
            logger.error(ex);
            setBStatus(true);
            logRequest(values, "ARLTDPL", "Failed Processing");
        }
        return retval;
    }

    public byte[] buildARLCHGSResponse(HashMap values) {
        byte[] retval = null;
        setBStatus(false);
        try {
            CLOB clob = null;
            CLOB errClob = null;
            String retError = "";
            int success = 0;
            OracleCallableStatement stmt = (OracleCallableStatement) _conn.prepareCall("{? = call pkariel.fn_ARLCHGS(?,?,?,?,?,?,?,?,?,?,?,?)}");

            stmt.registerOutParameter(1, -5);
            stmt.registerOutParameter(12, 2005);
            stmt.registerOutParameter(13, 2005);
            setStmtString(stmt, values, "MessageType", 2);
            setStmtString(stmt, values, "ProcCode", 3);
            setStmtString(stmt, values, "UniqueCode", 4);
            setStmtString(stmt, values, "BranchName", 5);
            setStmtString(stmt, values, "TxnDtTime", 6);
            setStmtString(stmt, values, "AcctNum", 7);
            setStmtString(stmt, values, "AcctCompCode", 8);
            setStmtNumeric(stmt, values, "Amount", 9);
            setStmtString(stmt, values, "Currency", 10);
            setStmtString(stmt, values, "TxnType", 11);
            stmt.execute();
            success = stmt.getInt(1);
            if (success == 0) {
                clob = stmt.getCLOB(13);
                retval = new byte[(int) clob.length()];
                InputStream reader = clob.getAsciiStream();
                reader.read(retval);
                logRequest(values, "ARLCHGS", "Successfully Processed");
            } else {
                clob = stmt.getCLOB(12);
                retval = new byte[(int) clob.length()];
                InputStream reader = clob.getAsciiStream();
                reader.read(retval);
                setBStatus(true);
                logger.error(new String(retval));
                logRequest(values, "ARLCHGS", "Failed Processing");
            }
        } catch (Exception ex) {
            logger.error(ex);
            setBStatus(true);
            logRequest(values, "ARLCHGS", "Failed Processing");
        }
        return retval;
    }

    public byte[] buildARLSACDResponse(HashMap values) {
        byte[] retval = null;
        setBStatus(false);
        try {
            CLOB clob = null;
            CLOB errClob = null;
            String retError = "";
            int success = 0;
            OracleCallableStatement stmt = (OracleCallableStatement) _conn.prepareCall("{? = call gcpprocessor.fn_ARLSACD(?,?,?,?,?,?,?,?,?)}");

            stmt.registerOutParameter(1, -5);
            stmt.registerOutParameter(9, 2005);
            stmt.registerOutParameter(10, 2005);
            setStmtString(stmt, values, "MessageType", 2);
            setStmtString(stmt, values, "ProcCode", 3);
            setStmtString(stmt, values, "UniqueCode", 4);
            setStmtString(stmt, values, "BranchName", 5);
            setStmtString(stmt, values, "TxnDtTime", 6);
            setStmtString(stmt, values, "AcctNum", 7);
            setStmtString(stmt, values, "AcctCompCode", 8);
            stmt.execute();
            success = stmt.getInt(1);
            if (success == 0) {
                clob = stmt.getCLOB(10);
                retval = new byte[(int) clob.length()];
                InputStream reader = clob.getAsciiStream();
                reader.read(retval);
                logRequest(values, "ARLSACD", "Successfully Processed");
            } else {
                clob = stmt.getCLOB(9);
                retval = new byte[(int) clob.length()];
                InputStream reader = clob.getAsciiStream();
                reader.read(retval);
                setBStatus(true);
                logger.error(new String(retval));
                logRequest(values, "ARLSACD", "Failed Processing");
            }
        } catch (Exception ex) {
            logger.error(ex);
            setBStatus(true);
            logRequest(values, "ARLSACD", "Failed Processing");
        }
        return retval;
    }

    public byte[] buildARLVALACTResponse(HashMap values) {
        byte[] retval = null;
        setBStatus(false);
        try {
            CLOB clob = null;
            String retError = "";
            int success = 0;
            OracleCallableStatement stmt = (OracleCallableStatement) _conn.prepareCall("{? = call pkariel.fn_ARLVALACT(?,?,?,?,?,?,?,?,?)}");

            stmt.registerOutParameter(1, -5);
            stmt.registerOutParameter(9, 2005);
            stmt.registerOutParameter(10, 2005);
            stmt.setString(2, (String) values.get("MessageType"));
            stmt.setString(3, (String) values.get("ProcCode"));
            stmt.setString(4, (String) values.get("UniqueCode"));
            stmt.setString(5, (String) values.get("BranchName"));
            stmt.setString(6, (String) values.get("TxnDtTime"));
            stmt.setString(7, (String) values.get("AcctNum"));
            stmt.setString(8, (String) values.get("AcctCompCode"));
            stmt.execute();
            success = stmt.getInt(1);
            if (success == 0) {
                clob = stmt.getCLOB(10);
                retval = new byte[(int) clob.length()];
                InputStream reader = clob.getAsciiStream();
                reader.read(retval);
                logRequest(values, "ARLVALACT", "Successfully Processed");
            } else {
                clob = stmt.getCLOB(9);
                retval = new byte[(int) clob.length()];
                InputStream reader = clob.getAsciiStream();
                reader.read(retval);
                setBStatus(true);
                logger.error(new String(retval));
                logRequest(values, "ARLVALACT", "Failed Processing");
            }
        } catch (Exception ex) {
            logger.error(ex);
            setBStatus(true);
            logRequest(values, "ARLVALACT", "Failed Processing");
        }
        return retval;
    }

    public byte[] buildARLSTOLResponse(HashMap values) {
        byte[] retval = null;
        setBStatus(false);
        try {
            CLOB clob = null;
            String retError = "";
            int success = 0;
            OracleCallableStatement stmt = (OracleCallableStatement) _conn.prepareCall("{? = call pkariel.fn_ARLSTOL(?,?,?,?,?,?,?,?,?)}");

            stmt.registerOutParameter(1, -5);
            stmt.registerOutParameter(9, 2005);
            stmt.registerOutParameter(10, 2005);
            stmt.setString(2, (String) values.get("MessageType"));
            stmt.setString(3, (String) values.get("ProcCode"));
            stmt.setString(4, (String) values.get("UniqueCode"));
            stmt.setString(5, (String) values.get("BranchName"));
            stmt.setString(6, (String) values.get("TxnDtTime"));
            stmt.setString(7, (String) values.get("AcctNum"));
            stmt.setString(8, (String) values.get("AcctCompCode"));
            stmt.execute();
            success = stmt.getInt(1);
            if (success == 0) {
                clob = stmt.getCLOB(10);
                retval = new byte[(int) clob.length()];
                InputStream reader = clob.getAsciiStream();
                reader.read(retval);
                logRequest(values, "ARLSTOL", "Successfully Processed");
            } else {
                clob = stmt.getCLOB(9);
                retval = new byte[(int) clob.length()];
                InputStream reader = clob.getAsciiStream();
                reader.read(retval);
                setBStatus(true);
                logger.error(new String(retval));
                logRequest(values, "ARLSTOL", "Failed Processing");
            }
        } catch (Exception ex) {
            logger.error(ex);
            setBStatus(true);
            logRequest(values, "ARLSTOL", "Failed Processing");
        }
        return retval;
    }

    public byte[] buildARLSTODResponse(HashMap values) {
        byte[] retval = null;
        setBStatus(false);
        try {
            CLOB clob = null;
            String retError = "";
            int success = 0;
            OracleCallableStatement stmt = (OracleCallableStatement) _conn.prepareCall("{? = call pkariel.fn_ARLSTOD(?,?,?,?,?,?,?,?,?)}");

            stmt.registerOutParameter(1, -5);
            stmt.registerOutParameter(9, 2005);
            stmt.registerOutParameter(10, 2005);
            stmt.setString(2, (String) values.get("MessageType"));
            stmt.setString(3, (String) values.get("ProcCode"));
            stmt.setString(4, (String) values.get("UniqueCode"));
            stmt.setString(5, (String) values.get("BranchName"));
            stmt.setString(6, (String) values.get("TxnDtTime"));
            stmt.setString(7, (String) values.get("SORefNo"));
            stmt.setString(8, (String) values.get("AcctCompCode"));
            stmt.execute();
            success = stmt.getInt(1);
            if (success == 0) {
                clob = stmt.getCLOB(10);
                retval = new byte[(int) clob.length()];
                InputStream reader = clob.getAsciiStream();
                reader.read(retval);
                logRequest(values, "ARLSTOD", "Successfully Processed");
            } else {
                clob = stmt.getCLOB(9);
                retval = new byte[(int) clob.length()];
                InputStream reader = clob.getAsciiStream();
                reader.read(retval);
                setBStatus(true);
                logger.error(new String(retval));
                logRequest(values, "ARLSTOD", "Failed Processing");
            }
        } catch (Exception ex) {
            logger.error(ex);
            setBStatus(true);
            logRequest(values, "ARLSTOD", "Failed Processing");
        }
        return retval;
    }

    public byte[] buildARLACTRResponse(HashMap values) {
        byte[] retval = null;
        setBStatus(false);
        try {
            CLOB clob = null;
            CLOB errClob = null;
            String retError = "";
            int success = 0;
            OracleCallableStatement stmt = (OracleCallableStatement) _conn.prepareCall("{? = call PKARIEL_INSTRUCTIONS.fn_ARLACTR(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");

            stmt.registerOutParameter(1, -5);
            stmt.registerOutParameter(17, 2005);
            stmt.registerOutParameter(18, 2005);

            setStmtString(stmt, values, "MessageType", 2);
            setStmtString(stmt, values, "ProcCode", 3);
            setStmtString(stmt, values, "UniqueCode", 4);
            setStmtString(stmt, values, "BranchName", 5);
            setStmtString(stmt, values, "TxnDtTime", 6);
            setStmtString(stmt, values, "DebitAcct", 7);
            setStmtString(stmt, values, "DebitAcctCompCode", 8);
            setStmtString(stmt, values, "CreditAcct", 9);
            setStmtString(stmt, values, "CreditAcctCompCode", 10);
            setStmtNumeric(stmt, values, "CreditAmount", 11);
            setStmtString(stmt, values, "ValueDate", 12);
            setStmtString(stmt, values, "PaymentDetails", 13);
            setStmtString(stmt, values, "CustomerRef", 14);
            setStmtString(stmt, values, "Charges", 15);
            setStmtString(stmt, values, "TransactionNo", 16);
            stmt.execute();

            success = stmt.getInt(1);
            if (success == 0) {
                clob = stmt.getCLOB(18);
                retval = new byte[(int) clob.length()];
                InputStream reader = clob.getAsciiStream();
                reader.read(retval);
                logRequest(values, "ARLACTR", "Successully Processed");
            } else {
                clob = stmt.getCLOB(17);
                retval = new byte[(int) clob.length()];
                InputStream reader = clob.getAsciiStream();
                reader.read(retval);
                setBStatus(true);
                logger.error(new String(retval));
                logRequest(values, "ARLACTR", "Failed Processing");
            }
        } catch (Exception ex) {
            logger.error(ex);
            setBStatus(true);
            logRequest(values, "ARLACTR", "Failed Processing");
        }
        return retval;
    }

    public byte[] buildARLCHBRResponse(HashMap values) {
        byte[] retval = null;
        setBStatus(false);
        try {
            CLOB clob = null;
            int success = 0;
            OracleCallableStatement stmt = (OracleCallableStatement) _conn.prepareCall("{? = call pkariel_instructions.fn_ARLCHBR(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");

            stmt.registerOutParameter(1, -5);
            stmt.registerOutParameter(15, 2005);
            stmt.registerOutParameter(16, 2005);
            setStmtString(stmt, values, "MessageType", 2);
            setStmtString(stmt, values, "ProcCode", 3);
            setStmtString(stmt, values, "UniqueCode", 4);
            setStmtString(stmt, values, "BranchName", 5);
            setStmtString(stmt, values, "TxnDtTime", 6);
            setStmtString(stmt, values, "AcctNum", 7);
            setStmtString(stmt, values, "AcctCompCode", 8);
            setStmtNumeric(stmt, values, "NoOfLeaves", 9);
            setStmtString(stmt, values, "ToBeCollectedAt", 10);
            setStmtString2(stmt, values, "RegisteredMail", 11);
            setStmtString(stmt, values, "CustomerRef", 12);
            setStmtInt(stmt, values, "Charges", 13);
            setStmtString(stmt, values, "TransactionNo", 14);
            stmt.execute();

            success = stmt.getInt(1);
            if (success == 0) {
                clob = stmt.getCLOB(16);
                retval = new byte[(int) clob.length()];
                InputStream reader = clob.getAsciiStream();
                reader.read(retval);
                logRequest(values, "ARLCHBR", "Successully Processed");
            } else {
                clob = stmt.getCLOB(15);
                retval = new byte[(int) clob.length()];
                InputStream reader = clob.getAsciiStream();
                reader.read(retval);
                setBStatus(true);
                logger.error(new String(retval));
                logRequest(values, "ARLCHBR", "Failed Processing");
            }
        } catch (Exception ex) {
            logger.error(ex);
            setBStatus(true);
            logRequest(values, "ARLCHBR", "Failed Processing");
        }
        return retval;
    }

    public byte[] buildARLDDTRBResponse(HashMap values) {
        byte[] retval = null;
        setBStatus(false);
        try {
            CLOB clob = null;
            CLOB errClob = null;
            String retError = "";
            int success = 0;
            OracleCallableStatement stmt = (OracleCallableStatement) _conn.prepareCall("{? = call pkariel_instructions.fn_ARLDDTRB(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");

            stmt.registerOutParameter(1, -5);
            stmt.registerOutParameter(22, 2005);
            stmt.registerOutParameter(23, 2005);
            setStmtString(stmt, values, "MessageType", 2);
            setStmtString(stmt, values, "ProcCode", 3);
            setStmtString(stmt, values, "UniqueCode", 4);
            setStmtString(stmt, values, "BranchName", 5);
            setStmtString(stmt, values, "TxnDtTime", 6);
            setStmtString(stmt, values, "DebitAcct", 7);
            setStmtString(stmt, values, "DebitAcctCompCode", 8);
            setStmtNumeric(stmt, values, "DraftAmount", 9);
            setStmtString(stmt, values, "BenName", 10);
            setStmtString(stmt, values, "OrderingPartyName", 11);
            setStmtString(stmt, values, "DraftCollectedAt", 12);
            setStmtString(stmt, values, "DealerReference", 13);
            setStmtNumeric(stmt, values, "ExchangeRate", 14);
            setStmtString(stmt, values, "AdditionalInfo", 15);
            setStmtString(stmt, values, "CustomerRef", 16);
            setStmtString(stmt, values, "ValueDate", 17);
            setStmtNumeric(stmt, values, "Charges", 18);
            setStmtString(stmt, values, "TransactionNo", 19);
            setStmtString(stmt, values, "CreditAcct", 20);
            setStmtString(stmt, values, "CreditAcctCompCode", 21);
            stmt.execute();

            success = stmt.getInt(1);
            if (success == 0) {
                clob = stmt.getCLOB(23);
                retval = new byte[(int) clob.length()];
                InputStream reader = clob.getAsciiStream();
                reader.read(retval);
                logRequest(values, "ARLDDTRB", "Successully Processed");
            } else {
                clob = stmt.getCLOB(22);
                retval = new byte[(int) clob.length()];
                InputStream reader = clob.getAsciiStream();
                reader.read(retval);
                setBStatus(true);
                logger.error(new String(retval));
                logRequest(values, "ARLDDTRB", "Failed Processing");
            }
        } catch (Exception ex) {
            logger.error(ex);
            setBStatus(true);
            logRequest(values, "ARLDDTRB", "Failed Processing");
        }
        return retval;
    }

    public byte[] buildARLFBTRSResponse(HashMap values) {
        byte[] retval = null;
        setBStatus(false);
        try {
            CLOB clob = null;
            CLOB errClob = null;
            String retError = "";
            int success = 0;
            OracleCallableStatement stmt = (OracleCallableStatement) _conn.prepareCall("{? = call pkariel_instructions.fn_ARLFBTRS(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");

            stmt.registerOutParameter(1, -5);
            stmt.registerOutParameter(21, 2005);
            stmt.registerOutParameter(22, 2005);
            setStmtString(stmt, values, "MessageType", 2);
            setStmtString(stmt, values, "ProcCode", 3);
            setStmtString(stmt, values, "UniqueCode", 4);
            setStmtString(stmt, values, "BranchName", 5);
            setStmtString(stmt, values, "TxnDtTime", 6);
            setStmtString(stmt, values, "DebitAcct", 7);
            setStmtString(stmt, values, "DebitAcctCompCode", 8);
            setStmtString(stmt, values, "DebitCurr", 9);
            setStmtString(stmt, values, "CreditAcct", 10);
            setStmtString(stmt, values, "CreditAcctCompCode", 11);
            setStmtString(stmt, values, "CreditCurr", 12);
            setStmtNumeric(stmt, values, "CreditAmount", 13);
            setStmtString(stmt, values, "ValueDate", 14);
            setStmtString(stmt, values, "ExchangeRate", 15);
            setStmtString(stmt, values, "DealerRef", 16);
            setStmtString(stmt, values, "PaymentDetails", 17);
            setStmtString(stmt, values, "CustomerRef", 18);
            setStmtString(stmt, values, "Charges", 19);
            setStmtString(stmt, values, "TransactionNo", 20);
            stmt.execute();

            success = stmt.getInt(1);
            if (success == 0) {
                clob = stmt.getCLOB(22);
                retval = new byte[(int) clob.length()];
                InputStream reader = clob.getAsciiStream();
                reader.read(retval);
                logRequest(values, "ARLFBTRS", "Successully Processed");
            } else {
                clob = stmt.getCLOB(21);
                retval = new byte[(int) clob.length()];
                InputStream reader = clob.getAsciiStream();
                reader.read(retval);
                setBStatus(true);
                logger.error(new String(retval));
                logRequest(values, "ARLFBTRS", "Failed Processing");
            }
        } catch (Exception ex) {
            logger.error(ex);
            setBStatus(true);
            logRequest(values, "ARLFBTRS", "Failed Processing");
        }
        return retval;
    }

    public byte[] buildARLFBTRXResponse(HashMap values) {
        byte[] retval = null;
        setBStatus(false);
        try {
            CLOB clob = null;
            int success = 0;
            OracleCallableStatement stmt = (OracleCallableStatement) _conn.prepareCall("{? = call pkariel_instructions.fn_ARLFBTRX(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");

            stmt.registerOutParameter(1, -5);
            stmt.registerOutParameter(21, 2005);
            stmt.registerOutParameter(22, 2005);
            setStmtString(stmt, values, "MessageType", 2);
            setStmtString(stmt, values, "ProcCode", 3);
            setStmtString(stmt, values, "UniqueCode", 4);
            setStmtString(stmt, values, "BranchName", 5);
            setStmtString(stmt, values, "TxnDtTime", 6);
            setStmtString(stmt, values, "DebitAcct", 7);
            setStmtString(stmt, values, "DebitAcctCompCode", 8);
            setStmtString(stmt, values, "DebitCurr", 9);
            setStmtString(stmt, values, "CreditAcct", 10);
            setStmtString(stmt, values, "CreditAcctCompCode", 11);
            setStmtString(stmt, values, "CreditCurr", 12);
            setStmtNumeric(stmt, values, "CreditAmount", 13);
            setStmtString(stmt, values, "ValueDate", 14);
            setStmtString(stmt, values, "ExchangeRate", 15);
            setStmtString(stmt, values, "DealerRef", 16);
            setStmtString(stmt, values, "PaymentDetails", 17);
            setStmtString(stmt, values, "CustomerRef", 18);
            setStmtString(stmt, values, "Charges", 19);
            setStmtString(stmt, values, "TransactionNo", 20);
            stmt.execute();

            success = stmt.getInt(1);
            if (success == 0) {
                clob = stmt.getCLOB(22);
                retval = new byte[(int) clob.length()];
                InputStream reader = clob.getAsciiStream();
                reader.read(retval);
                logRequest(values, "ARLFBTRX", "Successully Processed");
            } else {
                clob = stmt.getCLOB(21);
                retval = new byte[(int) clob.length()];
                InputStream reader = clob.getAsciiStream();
                reader.read(retval);
                setBStatus(true);
                logger.error(new String(retval));
                logRequest(values, "ARLFBTRX", "Failed Processing");
            }
        } catch (Exception ex) {
            logger.error(ex);
            setBStatus(true);
            logRequest(values, "ARLFBTRX", "Failed Processing");
        }
        return retval;
    }

    public byte[] buildARLFDDTRSResponse(HashMap values) {
        byte[] retval = null;
        setBStatus(false);
        try {
            CLOB clob = null;
            int success = 0;
            OracleCallableStatement stmt = (OracleCallableStatement) _conn.prepareCall("{? = call pkariel_instructions.fn_ARLFDDTRS(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");

            stmt.registerOutParameter(1, -5);
            stmt.registerOutParameter(22, 2005);
            stmt.registerOutParameter(23, 2005);
            setStmtString(stmt, values, "MessageType", 2);
            setStmtString(stmt, values, "ProcCode", 3);
            setStmtString(stmt, values, "UniqueCode", 4);
            setStmtString(stmt, values, "BranchName", 5);
            setStmtString(stmt, values, "TxnDtTime", 6);
            setStmtString(stmt, values, "DebitAcct", 7);
            setStmtString(stmt, values, "DebitAcctCompCode", 8);
            setStmtNumeric(stmt, values, "DraftAmount", 9);
            setStmtString(stmt, values, "BenName", 10);
            setStmtString(stmt, values, "OrderingPartyName", 11);
            setStmtString(stmt, values, "DraftCollectedAt", 12);
            setStmtString(stmt, values, "DealerReference", 13);
            setStmtString(stmt, values, "ExchangeRate", 14);
            setStmtString(stmt, values, "AdditionalInfo", 15);
            setStmtString(stmt, values, "CustomerRef", 16);
            setStmtString(stmt, values, "ValueDate", 17);
            setStmtNumeric(stmt, values, "Charges", 18);
            setStmtString(stmt, values, "TransactionNo", 19);
            setStmtString(stmt, values, "CreditAcct", 20);
            setStmtString(stmt, values, "CreditAcctCompCode", 21);
            stmt.execute();

            success = stmt.getInt(1);
            if (success == 0) {
                clob = stmt.getCLOB(23);
                retval = new byte[(int) clob.length()];
                InputStream reader = clob.getAsciiStream();
                reader.read(retval);
                logRequest(values, "ARLFDDTRS", "Successully Processed");
            } else {
                clob = stmt.getCLOB(22);
                retval = new byte[(int) clob.length()];
                InputStream reader = clob.getAsciiStream();
                reader.read(retval);
                setBStatus(true);
                logger.error(new String(retval));
                logRequest(values, "ARLFDDTRS", "Failed Processing");
            }
        } catch (Exception ex) {
            logger.error(ex);
            setBStatus(true);
            logRequest(values, "ARLFDDTRS", "Failed Processing");
        }
        return retval;
    }

    public byte[] buildARLFDDTRXResponse(HashMap values) {
        byte[] retval = null;
        setBStatus(false);
        try {
            CLOB clob = null;
            int success = 0;
            OracleCallableStatement stmt = (OracleCallableStatement) _conn.prepareCall("{? = call pkariel_instructions.fn_ARLFDDTRX(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");

            stmt.registerOutParameter(1, -5);
            stmt.registerOutParameter(22, 2005);
            stmt.registerOutParameter(23, 2005);
            setStmtString(stmt, values, "MessageType", 2);
            setStmtString(stmt, values, "ProcCode", 3);
            setStmtString(stmt, values, "UniqueCode", 4);
            setStmtString(stmt, values, "BranchName", 5);
            setStmtString(stmt, values, "TxnDtTime", 6);
            setStmtString(stmt, values, "DebitAcct", 7);
            setStmtString(stmt, values, "DebitAcctCompCode", 8);
            setStmtNumeric(stmt, values, "DraftAmount", 9);
            setStmtString(stmt, values, "BenName", 10);
            setStmtString(stmt, values, "OrderingPartyName", 11);
            setStmtString(stmt, values, "DraftCollectedAt", 12);
            setStmtString(stmt, values, "DealerReference", 13);
            setStmtNumeric(stmt, values, "ExchangeRate", 14);
            setStmtString(stmt, values, "AdditionalInfo", 15);
            setStmtString(stmt, values, "CustomerRef", 16);
            setStmtString(stmt, values, "ValueDate", 17);
            setStmtInt(stmt, values, "Charges", 18);
            setStmtString(stmt, values, "TransactionNo", 19);
            setStmtString(stmt, values, "CreditAcct", 20);
            setStmtString(stmt, values, "CreditAcctCompCode", 21);
            stmt.execute();
            success = stmt.getInt(1);
            if (success == 0) {
                clob = stmt.getCLOB(23);
                retval = new byte[(int) clob.length()];
                InputStream reader = clob.getAsciiStream();
                reader.read(retval);
                logRequest(values, "ARLFDDTRX", "Successully Processed");
            } else {
                clob = stmt.getCLOB(22);
                retval = new byte[(int) clob.length()];
                InputStream reader = clob.getAsciiStream();
                reader.read(retval);
                setBStatus(true);
                logger.error(new String(retval));
                logRequest(values, "ARLFDDTRX", "Failed Processing");
            }
        } catch (Exception ex) {
            logger.error(ex);
            setBStatus(true);
            logRequest(values, "ARLFDDTRX", "Failed Processing");
        }
        return retval;
    }

    public byte[] buildARLSOACRResponse(HashMap values) {
        byte[] retval = null;
        setBStatus(false);
        try {
            CLOB clob = null;
            int success = 0;
            OracleCallableStatement stmt = (OracleCallableStatement) _conn.prepareCall("{? = call pkariel_instructions.fn_ARLSOACR(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");

            stmt.registerOutParameter(1, -5);
            stmt.registerOutParameter(16, 2005);
            stmt.registerOutParameter(17, 2005);
            setStmtString(stmt, values, "MessageType", 2);
            setStmtString(stmt, values, "ProcCode", 3);
            setStmtString(stmt, values, "UniqueCode", 4);
            setStmtString(stmt, values, "BranchName", 5);
            setStmtString(stmt, values, "TxnDtTime", 6);
            setStmtString(stmt, values, "AcctNum", 7);
            setStmtString(stmt, values, "AcctCompCode", 8);
            setStmtString(stmt, values, "FromDate", 9);
            setStmtString(stmt, values, "ToDate", 10);
            setStmtString(stmt, values, "DeliveryBranchCode", 11);
            setStmtString2(stmt, values, "RegisteredMail", 12);
            setStmtString(stmt, values, "CustomerRef", 13);
            setStmtNumeric(stmt, values, "Charges", 14);
            setStmtString(stmt, values, "TransactionNo", 15);
            stmt.execute();
            success = stmt.getInt(1);
            if (success == 0) {
                clob = stmt.getCLOB(17);
                retval = new byte[(int) clob.length()];
                InputStream reader = clob.getAsciiStream();
                reader.read(retval);
                logRequest(values, "ARLSOACR", "Successully Processed");
            } else {
                clob = stmt.getCLOB(16);
                retval = new byte[(int) clob.length()];
                InputStream reader = clob.getAsciiStream();
                reader.read(retval);
                setBStatus(true);
                logger.error(new String(retval));
                logRequest(values, "ARLSOACR", "Failed Processing");
            }
        } catch (Exception ex) {
            logger.error(ex);
            setBStatus(true);
            logRequest(values, "ARLSOACR", "Failed Processing");
        }
        return retval;
    }

    public byte[] buildARLTETRSRXResponse(HashMap values) {
        byte[] retval = null;
        setBStatus(false);
        try {
            CLOB clob = null;
            int success = 0;
            OracleCallableStatement stmt = (OracleCallableStatement) _conn.prepareCall("{? = call pkariel.fn_ARLTETRS(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");

            stmt.registerOutParameter(1, -5);
            stmt.registerOutParameter(38, 2005);
            stmt.registerOutParameter(39, 2005);
            setStmtString(stmt, values, "MessageType", 2);
            setStmtString(stmt, values, "ProcCode", 3);
            setStmtString(stmt, values, "UniqueCode", 4);
            setStmtString(stmt, values, "BranchName", 5);
            setStmtString(stmt, values, "TxnDtTime", 6);
            setStmtString(stmt, values, "BenName", 7);
            setStmtString(stmt, values, "BenAddress", 8);
            setStmtString(stmt, values, "BenCountry", 9);
            setStmtString(stmt, values, "CreditCurr", 10);
            setStmtNumeric(stmt, values, "CreditAmount", 11);
            setStmtNumeric(stmt, values, "ExchangeRate", 12);
            setStmtString(stmt, values, "DealerRef", 13);
            setStmtString(stmt, values, "DebitAcct", 14);
            setStmtString(stmt, values, "DebitAcctCompCode", 15);
            setStmtString(stmt, values, "ValueDate", 16);
            setStmtString(stmt, values, "ReasonOfPayment", 17);
            setStmtString(stmt, values, "BenBankName", 18);
            setStmtNumeric(stmt, values, "BenBankAddress", 19);
            setStmtString(stmt, values, "BenBankCountry", 20);
            setStmtString(stmt, values, "BenBankChipSortCode", 21);
            setStmtString(stmt, values, "BenBankSwiftCode", 22);
            setStmtString(stmt, values, "BenAcctNo", 25);
            setStmtString(stmt, values, "BenBankABANo", 26);
            setStmtString(stmt, values, "BenBankIBAN", 27);
            setStmtString(stmt, values, "InterBankName", 28);
            setStmtString(stmt, values, "InterBankAddress", 29);
            setStmtString(stmt, values, "InterBankCountry", 30);
            setStmtString(stmt, values, "InterBankSwiftCode", 31);
            setStmtInt(stmt, values, "Charges", 32);
            setStmtString(stmt, values, "PaymentDetails", 33);
            setStmtString(stmt, values, "CustomerRef", 34);
            setStmtString(stmt, values, "CreditAcct", 35);
            setStmtString(stmt, values, "CreditAcctCompCode", 36);
            setStmtString(stmt, values, "TransactionNo", 37);
            stmt.execute();

            success = stmt.getInt(1);
            if (success == 0) {
                clob = stmt.getCLOB(39);
                retval = new byte[(int) clob.length()];
                InputStream reader = clob.getAsciiStream();
                reader.read(retval);
                logRequest(values, "ARLTETRS", "Successully Processed");
            } else {
                clob = stmt.getCLOB(38);
                retval = new byte[(int) clob.length()];
                InputStream reader = clob.getAsciiStream();
                reader.read(retval);
                setBStatus(true);
                logger.error(new String(retval));
                logRequest(values, "ARLTETRS", "Failed Processing");
            }
        } catch (Exception ex) {
            logger.error(ex);
            setBStatus(true);
            logRequest(values, "ARLTETRS", "Failed Processing");
        }
        return retval;
    }

    public byte[] buildARLTETRXResponse(HashMap values) {
        byte[] retval = null;
        setBStatus(false);
        try {
            CLOB clob = null;
            int success = 0;
            OracleCallableStatement stmt = (OracleCallableStatement) _conn.prepareCall("{? = call pkariel_instructions.fn_ARLTETRX(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");

            stmt.registerOutParameter(1, -5);
            stmt.registerOutParameter(36, 2005);
            stmt.registerOutParameter(37, 2005);
            setStmtString(stmt, values, "MessageType", 2);
            setStmtString(stmt, values, "ProcCode", 3);
            setStmtString(stmt, values, "UniqueCode", 4);
            setStmtString(stmt, values, "BranchName", 5);
            setStmtString(stmt, values, "TxnDtTime", 6);
            setStmtString(stmt, values, "BenName", 7);
            setStmtString(stmt, values, "BenAddress", 8);
            setStmtString(stmt, values, "BenCountry", 9);
            setStmtString(stmt, values, "CreditCurr", 10);
            setStmtNumeric(stmt, values, "CreditAmount", 11);
            setStmtNumeric(stmt, values, "ExchangeRate", 12);
            setStmtString(stmt, values, "DealerRef", 13);
            setStmtString(stmt, values, "DebitAcct", 14);
            setStmtString(stmt, values, "DebitAcctCompCode", 15);
            setStmtString(stmt, values, "ValueDate", 16);
            setStmtString(stmt, values, "ReasonOfPayment", 17);
            setStmtString(stmt, values, "BenBankName", 18);
            setStmtString(stmt, values, "BenBankAddress", 19);
            setStmtString(stmt, values, "BenBankCountry", 20);
            setStmtString(stmt, values, "BenBankChipSortCode", 21);
            setStmtString(stmt, values, "BenBankSwiftCode", 22);
            setStmtString(stmt, values, "BenAccountNo", 23);
            setStmtString(stmt, values, "BenBankABANo", 24);
            setStmtString(stmt, values, "BenBankIBAN", 25);
            setStmtString(stmt, values, "InterBankName", 26);
            setStmtString(stmt, values, "InterBankAddress", 27);
            setStmtString(stmt, values, "InterBankCountry", 28);
            setStmtString(stmt, values, "InterBankSwiftCode", 29);
            setStmtInt(stmt, values, "Charges", 30);
            setStmtString(stmt, values, "PaymentDetails", 31);
            setStmtString(stmt, values, "CustomerRef", 32);
            setStmtString(stmt, values, "CreditAcct", 33);
            setStmtString(stmt, values, "CreditAcctCompCode", 34);
            setStmtString(stmt, values, "TransactionNo", 35);
            stmt.execute();
            success = stmt.getInt(1);
            if (success == 0) {
                clob = stmt.getCLOB(37);
                retval = new byte[(int) clob.length()];
                InputStream reader = clob.getAsciiStream();
                reader.read(retval);
                logRequest(values, "ARLTETRX", "Successully Processed");
            } else {
                clob = stmt.getCLOB(36);
                retval = new byte[(int) clob.length()];
                InputStream reader = clob.getAsciiStream();
                reader.read(retval);
                setBStatus(true);
                logger.error(new String(retval));
                logRequest(values, "ARLTETRX", "Failed Processing");
            }
        } catch (Exception ex) {
            logger.error(ex);
            setBStatus(true);
            logRequest(values, "ARLTETRX", "Failed Processing");
        }
        return retval;
    }

    public byte[] buildARLTPTRFXResponse(HashMap values) {
        byte[] retval = null;
        setBStatus(false);
        try {
            CLOB clob = null;
            int success = 0;
            OracleCallableStatement stmt = (OracleCallableStatement) _conn.prepareCall("{? = call pkariel_instructions.fn_ARLTPTRFX(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");

            stmt.registerOutParameter(1, -5);
            stmt.registerOutParameter(24, 2005);
            stmt.registerOutParameter(25, 2005);

            setStmtString(stmt, values, "MessageType", 2);
            setStmtString(stmt, values, "ProcCode", 3);
            setStmtString(stmt, values, "UniqueCode", 4);
            setStmtString(stmt, values, "BranchName", 5);
            setStmtString(stmt, values, "TxnDtTime", 6);
            setStmtString(stmt, values, "DebitAcct", 7);
            setStmtString(stmt, values, "DebitAcctCompCode", 8);
            setStmtString(stmt, values, "BenName", 9);
            setStmtString(stmt, values, "BenAcctNo", 10);
            setStmtString(stmt, values, "BenAddress", 11);
            setStmtString(stmt, values, "CreditAcct", 12);
            setStmtString(stmt, values, "CreditAccCompCode", 13);
            setStmtString(stmt, values, "BenBankCode", 14);
            setStmtString(stmt, values, "BenBankName", 15);
            setStmtString(stmt, values, "CreditCurr", 16);
            setStmtNumeric(stmt, values, "CreditAmount", 17);
            setStmtString(stmt, values, "ValueDate", 18);
            setStmtString(stmt, values, "AdditionalInfo", 19);
            setStmtString(stmt, values, "CustomerRef", 20);
            setStmtNumeric(stmt, values, "Charges", 21);
            setStmtString(stmt, values, "ApplicantName", 22);
            setStmtString(stmt, values, "TransactionNo", 23);
            stmt.execute();
            success = stmt.getInt(1);
            if (success == 0) {
                clob = stmt.getCLOB(25);
                retval = new byte[(int) clob.length()];
                InputStream reader = clob.getAsciiStream();
                reader.read(retval);
                logRequest(values, "ARLTETRX", "Successully Processed");
            } else {
                clob = stmt.getCLOB(24);
                retval = new byte[(int) clob.length()];
                InputStream reader = clob.getAsciiStream();
                reader.read(retval);
                setBStatus(true);
                logger.error(new String(retval));
                logRequest(values, "ARLTETRX", "Failed Processing");
            }
        } catch (Exception ex) {
            logger.error(ex);
            setBStatus(true);
            logRequest(values, "ARLTETRX", "Failed Processing");
        }
        return retval;
    }

    public byte[] buildARLTETRSResponse(HashMap values) {
        byte[] retval = null;
        setBStatus(false);
        try {
            CLOB clob = null;
            int success = 0;
            OracleCallableStatement stmt = (OracleCallableStatement) _conn.prepareCall("{? = call pkariel_instructions.fn_ARLTETRS(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");

            stmt.registerOutParameter(1, -5);
            stmt.registerOutParameter(36, 2005);
            stmt.registerOutParameter(37, 2005);
            setStmtString(stmt, values, "MessageType", 2);
            setStmtString(stmt, values, "ProcCode", 3);
            setStmtString(stmt, values, "UniqueCode", 4);
            setStmtString(stmt, values, "BranchName", 5);
            setStmtString(stmt, values, "TxnDtTime", 6);
            setStmtString(stmt, values, "BenName", 7);
            setStmtString(stmt, values, "BenAddress", 8);
            setStmtString(stmt, values, "BenCountry", 9);
            setStmtString(stmt, values, "CreditCurr", 10);
            setStmtNumeric(stmt, values, "CreditAmount", 11);
            setStmtNumeric(stmt, values, "ExchangeRate", 12);
            setStmtString(stmt, values, "DealerRef", 13);
            setStmtString(stmt, values, "DebitAcct", 14);
            setStmtString(stmt, values, "DebitAcctCompCode", 15);
            setStmtString(stmt, values, "ValueDate", 16);
            setStmtString(stmt, values, "ReasonOfPayment", 17);
            setStmtString(stmt, values, "BenBankName", 18);
            setStmtString(stmt, values, "BenBankAddress", 19);
            setStmtString(stmt, values, "BenBankCountry", 20);
            setStmtString(stmt, values, "BenBankChipSortCode", 21);
            setStmtString(stmt, values, "BenBankSwiftCode", 22);
            setStmtString(stmt, values, "BenAccountNo", 23);
            setStmtString(stmt, values, "BenBankABANo", 24);
            setStmtString(stmt, values, "BenBankIBAN", 25);
            setStmtString(stmt, values, "InterBankName", 26);
            setStmtString(stmt, values, "InterBankAddress", 27);
            setStmtString(stmt, values, "InterBankCountry", 28);
            setStmtString(stmt, values, "InterBankSwiftCode", 29);
            setStmtInt(stmt, values, "Charges", 30);
            setStmtString(stmt, values, "PaymentDetails", 31);
            setStmtString(stmt, values, "CustomerRef", 32);
            setStmtString(stmt, values, "CreditAcct", 33);
            setStmtString(stmt, values, "CreditAcctCompCode", 34);
            setStmtString(stmt, values, "TransactionNo", 35);
            stmt.execute();
            success = stmt.getInt(1);
            if (success == 0) {
                clob = stmt.getCLOB(37);
                retval = new byte[(int) clob.length()];
                InputStream reader = clob.getAsciiStream();
                reader.read(retval);
                logRequest(values, "ARLTETRS", "Successully Processed");
            } else {
                clob = stmt.getCLOB(36);
                retval = new byte[(int) clob.length()];
                InputStream reader = clob.getAsciiStream();
                reader.read(retval);
                setBStatus(true);
                logger.error(new String(retval));
                logRequest(values, "ARLTETRS", "Failed Processing");
            }
        } catch (Exception ex) {
            logger.error(ex);
            setBStatus(true);
            logRequest(values, "ARLTETRX", "Failed Processing");
        }
        return retval;
    }

    public byte[] buildARLTPTRBResponse(HashMap values) {
        byte[] retval = null;
        setBStatus(false);
        try {
            CLOB clob = null;
            int success = 0;
            OracleCallableStatement stmt = (OracleCallableStatement) _conn.prepareCall("{? = call pkariel_instructions.fn_ARLTPTRB(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");

            stmt.registerOutParameter(1, -5);
            stmt.registerOutParameter(24, 2005);
            stmt.registerOutParameter(25, 2005);
            setStmtString(stmt, values, "MessageType", 2);
            setStmtString(stmt, values, "ProcCode", 3);
            setStmtString(stmt, values, "UniqueCode", 4);
            setStmtString(stmt, values, "BranchName", 5);
            setStmtString(stmt, values, "TxnDtTime", 6);
            setStmtString(stmt, values, "DebitAcct", 7);
            setStmtString(stmt, values, "DebitAcctCompCode", 8);
            setStmtString(stmt, values, "BenName", 9);
            setStmtString(stmt, values, "BenAcctNo", 10);
            setStmtString(stmt, values, "BenAddress", 11);
            setStmtString(stmt, values, "CreditAcct", 12);
            setStmtString(stmt, values, "CreditAccCompCode", 13);
            setStmtString(stmt, values, "BenBankCode", 14);
            setStmtNumeric(stmt, values, "BenBankName", 15);
            setStmtInt(stmt, values, "CreditCurr", 16);
            setStmtNumeric(stmt, values, "CreditAmount", 17);
            setStmtString(stmt, values, "ValueDate", 18);
            setStmtString(stmt, values, "AdditionalInfo", 19);
            setStmtNumeric(stmt, values, "CustomerRef", 20);
            setStmtInt(stmt, values, "Charges", 21);
            setStmtString(stmt, values, "ApplicantName", 22);
            setStmtString(stmt, values, "TransactionNo", 23);
            stmt.execute();
            success = stmt.getInt(1);
            if (success == 0) {
                clob = stmt.getCLOB(25);
                retval = new byte[(int) clob.length()];
                InputStream reader = clob.getAsciiStream();
                reader.read(retval);
                logRequest(values, "ARLTPTRB", "Successully Processed");
            } else {
                clob = stmt.getCLOB(24);
                retval = new byte[(int) clob.length()];
                InputStream reader = clob.getAsciiStream();
                reader.read(retval);
                setBStatus(true);
                logger.error(new String(retval));
                logRequest(values, "ARLTPTRB", "Failed Processing");
            }
        } catch (Exception ex) {
            logger.error(ex);
            setBStatus(true);
            logRequest(values, "ARLTPTRB", "Failed Processing");
        }
        return retval;
    }

    public byte[] buildARLTPTRFSResponse(HashMap values) {
        byte[] retval = null;
        setBStatus(false);
        try {
            CLOB clob = null;
            int success = 0;
            OracleCallableStatement stmt = (OracleCallableStatement) _conn.prepareCall("{? = call pkariel_instructions.fn_ARLTPTRFS(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");

            stmt.registerOutParameter(1, -5);
            stmt.registerOutParameter(24, 2005);
            stmt.registerOutParameter(25, 2005);
            setStmtString(stmt, values, "MessageType", 2);
            setStmtString(stmt, values, "ProcCode", 3);
            setStmtString(stmt, values, "UniqueCode", 4);
            setStmtString(stmt, values, "BranchName", 5);
            setStmtString(stmt, values, "TxnDtTime", 6);
            setStmtString(stmt, values, "DebitAcct", 7);
            setStmtString(stmt, values, "DebitAcctCompCode", 8);
            setStmtString(stmt, values, "BenName", 9);
            setStmtString(stmt, values, "BenAcctNo", 10);
            setStmtString(stmt, values, "BenAddress", 11);
            setStmtString(stmt, values, "CreditAcct", 12);
            setStmtString(stmt, values, "CreditAccCompCode", 13);
            setStmtString(stmt, values, "BenBankCode", 14);
            setStmtNumeric(stmt, values, "BenBankName", 15);
            setStmtInt(stmt, values, "CreditCurr", 16);
            setStmtNumeric(stmt, values, "CreditAmount", 17);
            setStmtString(stmt, values, "ValueDate", 18);
            setStmtString(stmt, values, "AdditionalInfo", 19);
            setStmtNumeric(stmt, values, "CustomerRef", 20);
            setStmtInt(stmt, values, "Charges", 21);
            setStmtString(stmt, values, "ApplicantName", 22);
            setStmtString(stmt, values, "TransactionNo", 23);
            stmt.execute();
            success = stmt.getInt(1);
            if (success == 0) {
                clob = stmt.getCLOB(25);
                retval = new byte[(int) clob.length()];
                InputStream reader = clob.getAsciiStream();
                reader.read(retval);
                logRequest(values, "ARLTPTRFS", "Successully Processed");
            } else {
                clob = stmt.getCLOB(24);
                retval = new byte[(int) clob.length()];
                InputStream reader = clob.getAsciiStream();
                reader.read(retval);
                setBStatus(true);
                logger.error(new String(retval));
                logRequest(values, "ARLTPTRFS", "Failed Processing");
            }
        } catch (Exception ex) {
            logger.error(ex);
            setBStatus(true);
            logRequest(values, "ARLTPTRFS", "Failed Processing");
        }
        return retval;
    }

    public void setBStatus(boolean bStatus) {
        this.bStatus = bStatus;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public boolean isBStatus() {
        return bStatus;
    }

    private void logRequest(HashMap values, String requestCode, String status) {
        Calendar cal = Calendar.getInstance();
        String logMsg = "GCP Request " + requestCode + " Date/Time" + cal.getTime().toString() + "\r\n";
        Object[] valArray = values.values().toArray();
        Object[] keyArray = values.keySet().toArray();
        try {
            for (int i = 0; i <= valArray.length - 1; i++) {
                if ((valArray[i] instanceof String)) {
                    logMsg = logMsg + " " + (String) keyArray[i] + " :" + (String) valArray[i] + "\r\n";
                }
            }
            logMsg = logMsg + "Status Code: " + status;
            logger.info(logMsg);
        } catch (Exception ex) {
            logger.error(ex);
        }
    }

    private byte[] checkUncompletedTask(String transNumber, String requestCode) {
        byte[] retByte = null;

        return retByte;
    }
}
