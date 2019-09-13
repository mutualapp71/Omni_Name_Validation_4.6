/*
 * Class Name:   Main
 *
 * Date:         March-2008
 *
 *  Copyright 2008      Fasyl Nigeria Ltd.
 *                       Amazing Grace Plaza
 *                       3rd Floor
 *                       2e-4e ,Ligali Ayorinde Street
 *                       Victoria Island Lagos
 *                       Nigeria.
 * This source is part of the FASYL FLEXCUBE INTERFACE  Software System and is copyrighted
 * by Fasyl Nigeria Ltd. All rights reserved.  No part of this work
 * may be reproduced, stored in a retrieval system, adopted or transmitted
 * in any form or by any means, electronic, mechanical, photographic, graphic,
 * optic recording or otherwise, translated in any language or computer
 * language, without the prior written permission of Fasyl Nigeria Ltd.
 *
 */
package main;

import java.util.HashMap;
import java.util.Properties;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.*;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.pool.OracleDataSource;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;
import java.io.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;
import requesthandlers.ArielProcessor;

import requesthandlers.ProcessorBase;
import utility.XPathReader;
import utility.XMLRequestParser;
import org.jasypt.util.*;
import requesthandlers.PolarisProcessor;
import utility.DatabaseFactory;

/**
 *
 * @author fasyl
 */
public class MainNonThread {

    /**
     * Get the error Message at the Main level
     *
     * @return
     */
    public static String getErrMsg() {
        return errMsg;
    }

    /**
     * Set the error Message at the Main level
     *
     * @param aErrMsg The error message to set
     *
     */
    public static void setErrMsg(String aErrMsg) {
        errMsg = aErrMsg;
    }
    /**
     * @param Pass the Initialization.properties file as the main paramter to
     * the application.
     */
    static String xmlMsg = "";
    static String requestQueue = "";
    static String replyQueue = "";
    static String cifQueue = "";
    static String FXRATERESQUEUE = "";
    static String BENACCRESQUEUE = "";
    static String errorQueue = "";
    static int portErr = 0;
    static int portRequest = 0;
    static int portResponse = 0;
    static String server = "";
    static String queManager = "";
    static String uid = "";
    static String sid = "";
    static String xmlSchemaConfig = "";
    static String dbServer = "";
    static Logger logger = Logger.getLogger(MainNonThread.class);
    static long timeInterval = 0;
    private static Properties paramProp = new Properties();
    private static Properties paramPropSchema = new Properties();
    private static Properties paramPropXMLPath = new Properties();
    private static Properties paramClassMapping = new Properties();
    static jms.QueueUtility xmlMsgChecker = null;
    static jms.QueueUtility xmlMsgReceiver = null;
    static jms.QueueUtility xmlMsgSender = null;
    static jms.QueueUtility XMLFXRATEQUEUE = null;
    static jms.QueueUtility XMLBENACCRESQUEUE = null;
    static jms.QueueUtility xmlMsgSenderCif = null;
    static jms.QueueUtility xmlMsgError = null;
    static String url = "jdbc:oracle:thin:@localhost:1521:orcl";
    static String pwd = "";
    static String schemaConfig = "";
    static String application = "";
    static String xmlPathConfig = "";
    static String classmapping = "";
    static String sampleXmlFolder = "";
    static XPathReader xmlReader = null;
    private boolean err = false;
    private static HashMap hashMap = null;
    private static String errMsg = "";
    private static String channel = "";
    private static String charSet = "";
    private static String encoding = "";
    private static String dbConfig = "";
    private static String SSLPROPERTY = "";
    private static String KEY_STORE_URL = "";
    private static String KEY_STORE_PASSWORD = "";
    private static String TRUST_STORE_URL = "";
    private static String TRUST_STORE_PASSWORD = "";
    private static String SHOW_DEBUG_INFO = "";
    private static String USE_PROXY = "";
    private static String PROXY_HOST = "";
    private static String PROXY_PORT = "";
    private static int msgRecCt = 0;
    private static int msgSentCt = 0;
    private static int msgErrorCt = 0;
    private static int msgSentCt1 = 0;
    private static OracleDataSource ods = null;
    private static TextEncryptor te = new TextEncryptor();
    private static String genKey = "";
    private static int dbTimeout = 0;
    private static ArrayList<HashMap<String, Object>> failMsgList = new ArrayList<HashMap<String, Object>>();
    private static HashMap<String, OracleConnection> listConn = new HashMap<String, OracleConnection>();
    //World Bank Stuff
    private static String affCodeDest;
    private static String affCodeSrc;
    private static String xref;
    static OracleConnection interfacecon;

    /**
     * The main application entry point.
     *
     * @param args The Initialization properties file as the argument
     * @throws org.xml.sax.SAXException
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws SAXException, IOException {
        initParameter(args[0]);
        System.out.println("Service Up and Running");

//        if(te.decrypt(genKey).compareTo(getMacAddress()) != 0){
//            logger.error("Proper Installation not done, contact system Administrator. System Exiting");
//            System.exit(0);
//        }
        while (true) {
            receiveMessage();
            try {
                Thread.sleep(timeInterval);
            } catch (Exception ex) {
                logger.error(ex);
            }
        }
    }
    /**
     * This is the main method that handles requests. After the request has been
     * read as byte array
     *
     * @param requestCode
     * @param appCode
     * @param request
     */
    private static Thread dt;
    private static long startTime = 0;
    private static long stopTime = 0;
    private static DatabaseFactory df;
    private static HashMap affDb;

    private static void getDbConfig() {
        File affDbConfig = new File(dbConfig);
        XMLRequestParser xmlParser = null;
        try {
            if (affDbConfig.exists()) {
                xmlParser = new XMLRequestParser(affDbConfig);
                affDb = xmlParser.getHashMapValue();
            }
        } catch (Exception ex) {
            logger.error(ex);
        }
    }

    public static void handleRequest(String requestCode, String appCode, byte[] request) {
        //Kastel Section
        String s = "";
        byte[] retByte = null;
        XMLRequestParser xmlParser = new XMLRequestParser(new ByteArrayInputStream(request));
        hashMap = xmlParser.getHashMapValue();
        logger.info("hashmap " + hashMap);
        ProcessorBase kasProc = null;
        df = DatabaseFactory.getDBFactory();
        if (appCode.compareToIgnoreCase("KASTEL") == 0) {
            kasProc = new ArielProcessor(requestCode, df.getDBInstance(affCodeDest, (HashMap) affDb.get(affCodeDest)));
            try {
                startTime = System.currentTimeMillis();
                DatabaseThread databaseThread = new DatabaseThread();
                databaseThread.kasProc = kasProc;
                databaseThread.requestCode = requestCode;
                dt = new Thread(databaseThread);
                dt.setName("DatabaseThread");
                dt.start();
                while (dt.isAlive()) {
                    dt.join(dbTimeout);
                    retByte = databaseThread.retByte;
                    if (dt.isAlive() && (System.currentTimeMillis() - startTime > dbTimeout)) {
                        logger.info("Database Execution Thread taken too long more than " + dbTimeout / 1000 + " sec.");
                        dt.interrupt();
                        dt.stop();
                        break;
                    }
                }
            } catch (Exception ex) {
                logger.trace(ex);
            }
            if (retByte == null) {
                xmlMsgError.closeProducer();
                if (kasProc.isBStatus()) {
                    logXMLError(kasProc.getErrMsg(), requestCode);
                    sendTxtErrorMsgKastle(kasProc.getErrMsg(), (String) hashMap.get("transNumber"));
                    return;
                }
                xmlMsgError.closeProducer();
            } else {
                //Added for SMS banking
//                if (xmlMsgReceiver.getCorrID() != null) {
//                    xmlMsgSender.setCorrID(xmlMsgReceiver.getCorrID());
//                    s = new String(retByte);
//                    logXMLResponse(s, requestCode);
//                    processResponse(requestCode, s, hashMap);
//                    xmlMsgSender.closeProducer();
//                    return;
//                }
                xmlMsgSender.connectProducer();
                s = new String(retByte);

                logXMLResponse(s, requestCode);
                processResponse(requestCode, s, hashMap);
                xmlMsgSender.closeProducer();
                return;
            }
        } else if (appCode.compareToIgnoreCase("ECIB+") == 0) {
            //kasProc = new ArielProcessor(requestCode, getOracleConnection());
            // kasProc = new ArielProcessor(requestCode, df.getDBInstance(uid, pwd, url, ""),xmlMsgReceiver);

            if (requestCode.equals("1021")) {

                interfacecon = df.getDBInstance(uid, pwd, url, "");

                if (interfacecon == null) {
                    logger.error(" Flexcube Connection DOWN 1");
                    String err = buildErrorMsg("Flexcube Core Banking Cannot be Accessed", requestCode, hashMap.get("MsgBody"), hashMap.get("MsgHeader"));
                    sendTxtErrorMsg(err, "", xmlMsgReceiver.getCorrID());
                    //logXMLResponse(err, requestCode);
                    return;
                }

                try {

                    // logger.info("keep alive");
                    interfacecon.prepareStatement("select * from dual").execute();
                    System.out.println("connection to DB success");
                    logger.info("connection to DB success");

                } catch (Exception ex) {
                    logger.error(ex.getMessage());
                    logger.info("reconnecting ");
                    interfacecon = df.getDBInstance(uid, pwd, url, "");

                }

            }

            kasProc = new ArielProcessor(requestCode, interfacecon, xmlMsgReceiver);

//            logger.info("am here "+xmlMsgReceiver.getCorrID());
//            logger.info("am here "+xmlMsgReceiver.getMessID());
            try {
                startTime = System.currentTimeMillis();
                DatabaseThread databaseThread = new DatabaseThread();
                databaseThread.kasProc = kasProc;
                databaseThread.requestCode = requestCode;
                dt = new Thread(databaseThread);
                dt.setName("DatabaseThread");
                dt.start();
                while (dt.isAlive()) {
                    dt.join(dbTimeout);
                    retByte = databaseThread.retByte;
                    if (dt.isAlive() && (System.currentTimeMillis() - startTime > dbTimeout)) {

                        logger.info("Database Execution Thread taken too long more than " + dbTimeout / 1000 + " sec.");
                        dt.interrupt();
                        dt.stop();
                        break;
                    }
                }

            } catch (Exception ex) {
                logger.trace(ex);

            }
            //retByte = kasProc.getResponse(hashMap, requestCode);

            if (xmlMsgReceiver.getCorrID() != null) {
                xmlMsgSender.setCorrID(xmlMsgReceiver.getCorrID());

            }
            if (retByte != null && kasProc.isBStatus()) {
                xmlMsgError.connectProducer();
                String g = new String(retByte);
                sendTxtErrorMsg(g, "", xmlMsgReceiver.getCorrID());
                xmlMsgError.closeProducer();
                logger.info("Reply sent");
                return;
            } else if (retByte != null && !kasProc.isBStatus()) {
                xmlMsgSender.connectProducer();
                s = new String(retByte);
                logXMLResponse(s, requestCode);
                processResponse(requestCode, s, hashMap);
                xmlMsgSender.closeProducer();
                logger.info("Reply sent");
                return;
            } else if (kasProc.isBStatus()) {
                System.out.println("here " + xmlMsgReceiver.getCorrID());
                logger.error(" Flexcube Connection DOWN");
                String err = buildErrorMsg("Flexcube Core Banking Cannot be Accessed", requestCode, hashMap.get("MsgBody"), hashMap.get("MsgHeader"));
                sendTxtErrorMsg(err, "", xmlMsgReceiver.getCorrID());
                logXMLResponse(err, requestCode);
                logger.info("Reply sent");
                return;
            }
        } else if (appCode.compareToIgnoreCase("POLARIS") == 0) {
            kasProc = new PolarisProcessor(requestCode, df.getDBInstance(uid, pwd, url, ""));
            try {
                startTime = System.currentTimeMillis();
                DatabaseThread databaseThread = new DatabaseThread();
                databaseThread.kasProc = kasProc;
                databaseThread.requestCode = requestCode;
                dt = new Thread(databaseThread);
                dt.setName("DatabaseThread");
                dt.start();
                while (dt.isAlive()) {
                    dt.join(dbTimeout);
                    retByte = databaseThread.retByte;
                    if (dt.isAlive() && (System.currentTimeMillis() - startTime > dbTimeout)) {
                        logger.info("Database Execution Thread taken too long more than " + dbTimeout / 1000 + " sec.");
                        dt.interrupt();
                        dt.stop();
                        break;
                    }
                }
            } catch (Exception ex) {
                logger.trace(ex);
            }
            if (retByte == null) {
                xmlMsgError.closeProducer();
                if (kasProc.isBStatus()) {
                    logXMLError(kasProc.getErrMsg(), requestCode);
                    sendTxtErrorMsgKastle(kasProc.getErrMsg(), (String) hashMap.get("transNumber"));
                    return;
                }
                xmlMsgError.closeProducer();
            } else {
                xmlMsgSender.connectProducer();
                s = new String(retByte);
                s = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + s;
                logXMLResponse(s, requestCode);
                processResponse(requestCode, s, hashMap);
                xmlMsgSender.closeProducer();
                return;
            }
        }
    }

    private static void processResponse(String requestCode, String xmlMsg, HashMap map) {
        //String schemaFile = paramProp.getProperty("XSDDIR") + "\\Response\\" + requestCode + "_RS.xsd";

        String schemaFile = paramProp.getProperty("XSDDIR") + File.separator + "Response" + File.separator + requestCode + "_RS.xsd";
        try {
            if (validateXMLRequest(xmlMsg, schemaFile, requestCode)) {
                if (application.compareToIgnoreCase("KASTEL") == 0) {
                    sendResponseMsg(xmlMsg, (String) map.get("transNumber"), requestCode);
                    //sendResponseMsg(xmlMsg, (String) map.get("transNumber"), requestCode, xmlMsgReceiver.getCorrID(), charSet, encoding);
                } else if (application.compareToIgnoreCase("ECIB+") == 0) {
                    sendResponseMsg(xmlMsg, (String) map.get("transNumber"), requestCode, xmlMsgReceiver.getMessID(), charSet, encoding);
                    logger.info("Response CorrID ::: " + xmlMsgReceiver.getMessID());
                    
                } else if (application.compareToIgnoreCase("POLARIS") == 0) {
                    sendResponseMsg(xmlMsg, (String) map.get("transNumber"), requestCode, xmlMsgReceiver.getCorrID(), charSet, encoding);
                } else if (application.compareToIgnoreCase("WORLDBANK") == 0) {
                    sendResponseMsg(xmlMsg, (String) map.get("transNumber"), requestCode, xmlMsgReceiver.getCorrID(), charSet, encoding);
                }
            } else {
                if (application.compareToIgnoreCase("KASTEL") == 0) {
                    String err = buildErrorMsg(application + "RESPONSE SCHEMA VALIDATION ERROR", map, requestCode);
                    sendTxtErrorMsg(err, (String) map.get("transNumber"));
                } else if (application.compareToIgnoreCase("ECIB+") == 0) {
                    sendTxtErrorMsg(buildErrorMsg(getErrMsg(), requestCode, (String) map.get("UniqueCode"), (String) map.get("BranchName"), (String) map.get("TxnDtTime")), requestCode, charSet, encoding);
                } else if (application.compareToIgnoreCase("POLARIS") == 0) {
                    String err = buildErrorMsg(application + "RESPONSE SCHEMA VALIDATION ERROR", map, requestCode);
                    sendTxtErrorMsg(err, (String) map.get("transNumber"));
                } else if (application.compareToIgnoreCase("WORLDBANK") == 0) {
                    sendTxtErrorMsg(buildErrorMsg(getErrMsg(), requestCode, (String) map.get("UniqueCode"), (String) map.get("BranchName"), (String) map.get("TxnDtTime")), requestCode, charSet, encoding);
                }
            }
        } catch (Exception ex) {
            logger.error(ex);
        }
    }

    public static void receiveMessage() throws SAXException, IOException {
        String xmlread = "";
        byte[] msgArray = null;
        String reqCode = "";
        xmlMsgChecker.connectConsumer();
        //if (!xmlMsgChecker.checkMessage()) {
       if (xmlMsgChecker.checkMessage()) {

            System.out.println("Inside here..");
            try {
                xmlMsgReceiver.connectConsumer();
                if (application.compareTo("WORLDBANK") == 0) {
                    msgArray = xmlMsgReceiver.receiveMessageWB();
                } else {

                    System.out.println("ECIB+");
                    logger.info("ECIB+");

                    msgArray = xmlMsgReceiver.receiveMessage();
                    //msgArray = "<Msg><MsgHeader><ServiceId>2019062410007</ServiceId><TxnCode>1050</TxnCode></MsgHeader><MsgBody><BeneAcctDtl><AppCode>EGHGCP</AppCode><CMSTransID>CMS1561396714448</CMSTransID><BeneAccountNmbr>2062179221018</BeneAccountNmbr><BankCode>GH020100</BankCode></BeneAcctDtl></MsgBody></Msg>".getBytes();
                    //msgArray="<Msg><MsgHeader><ServiceId>0000000000372716</ServiceId><TxnCode>1021</TxnCode></MsgHeader><MsgBody><BeneAcctDtl><AppCode>EGHGCP</AppCode><CMSTransID>0000000000000001</CMSTransID><BeneAccountNmbr>01600444470976601</BeneAccountNmbr><AcctCCY>GHS</AcctCCY></BeneAcctDtl><BeneAcctDtl><AppCode>EGHGCP</AppCode><CMSTransID>0000000000000002</CMSTransID><BeneAccountNmbr>0300034656032301</BeneAccountNmbr><AcctCCY>GHS</AcctCCY></BeneAcctDtl><BeneAcctDtl><AppCode>EGHGCP</AppCode><CMSTransID>0000000000000003</CMSTransID><BeneAccountNmbr>03000346556191010</BeneAccountNmbr><AcctCCY>GHS</AcctCCY></BeneAcctDtl><BeneAcctDtl><AppCode>EGHGCP</AppCode><CMSTransID>0000000000000004</CMSTransID><BeneAccountNmbr>0300034656047801</BeneAccountNmbr><AcctCCY>GHS</AcctCCY></BeneAcctDtl></MsgBody></Msg>".getBytes();
                    //msgArray="<Msg><MsgHeader><ServiceId>2019091110015</ServiceId><TxnCode>1050</TxnCode></MsgHeader><MsgBody><BeneAcctDtl><AppCode>GCPENG</AppCode><CMSTransID>PAY1568207310519</CMSTransID><BeneAccountNmbr>0003525240</BeneAccountNmbr><BankCode>058152052</BankCode></BeneAcctDtl></MsgBody></Msg>".getBytes();

                }

                msgRecCt++;
                if (msgRecCt >= 250) {
                    xmlMsgReceiver = null;
                    xmlMsgReceiver = new jms.QueueUtility(requestQueue, "receive", portResponse, server, queManager, channel);
                    msgRecCt = 0;
                }
                if (msgArray.length > 0) {
                    xmlReader = new XPathReader(msgArray);
                    System.out.println("here 2");
                    reqCode = xmlReader.getValue(paramPropXMLPath.getProperty(application), msgArray);

                    System.out.println("reqcode >>> " + reqCode);

                    //String schemaFile = paramProp.getProperty("XSDDIR") + "\\Request\\" + reqCode + "_RQ.xsd";
                    String schemaFile = paramProp.getProperty("XSDDIR") + File.separator + "Request" + File.separator + reqCode + "_RQ.xsd";

                    xmlread = new String(msgArray);
                    TextEncryptor te1 = new TextEncryptor();
                    te1.setPassword("fasylqwe123!@#");
                    // XML message encryption
                    if (reqCode.equalsIgnoreCase("1039")) {
                        logger.info(" 1039:  Customer Account Search Request");
                        logger.info("*****************************************************************");
                        logXMLRequest(xmlread, reqCode);
                    } else if (reqCode.equalsIgnoreCase("1015")) {
                        logger.info("1015:FX Contract Request");
                        logger.info("*****************************************************************");
                        logXMLRequest(xmlread, reqCode);
                    } else if (reqCode.equalsIgnoreCase("1021")) {
                        logger.info("1021: Beneficary Account verification");
                        logger.info("*****************************************************************");
                        logXMLRequest(xmlread, reqCode);
                    } else {
                        logXMLRequest(xmlread, reqCode);
                    }

                    if (validateXMLRequest(xmlread, schemaFile, reqCode)) {

                        System.out.println("Validation success");
                        handleRequest(reqCode, application, msgArray);
                        xmlMsgReceiver.closeConsumer();
                        return;
                    } else {
                        XMLRequestParser xmlParser = new XMLRequestParser(new ByteArrayInputStream(msgArray));
                        hashMap = xmlParser.getHashMapValue();
                        if (application.compareToIgnoreCase("KASTEL") == 0) {
                            String err = buildErrorMsg(application + "RESPONSE SCHEMA VALIDATION ERROR", hashMap, reqCode);
                            sendTxtErrorMsg(err, (String) hashMap.get("transNumber"));
                        } else if (application.compareToIgnoreCase("ECIB+") == 0) {
                            sendTxtErrorMsg(buildErrorMsg(getErrMsg(), reqCode, hashMap.get("MsgBody"), hashMap.get("MsgHeader")), "", xmlMsgReceiver.getCorrID());
                            //sendTxtErrorMsg(g, "", xmlMsgReceiver.getCorrID());
                        } else if (application.compareToIgnoreCase("WORLDBANK") == 0) {
                            sendTxtErrorMsgWB(buildErrorMsg(getErrMsg(), reqCode, (String) hashMap.get("UniqueCode"), (String) hashMap.get("BranchName"), (String) hashMap.get("TxnDtTime")), "", xmlMsgReceiver.getCorrID());
                            //sendTxtErrorMsg(g, "", xmlMsgReceiver.getCorrID());
                        } else if (application.compareToIgnoreCase("POLARIS") == 0) {
                            String err = buildErrorMsg(application + "RESPONSE SCHEMA VALIDATION ERROR", hashMap, reqCode);
                            sendTxtErrorMsg(err, (String) hashMap.get("transNumber"));
                        }
                        xmlMsgReceiver.closeConsumer();
                    }
                }
            } catch (Exception ex) {
                logger.error(ex);
                setErrMsg(ex.getMessage());
                if (msgArray != null) {
                    XMLRequestParser xmlParser = new XMLRequestParser(new ByteArrayInputStream(msgArray));
                    buildErrorMsg(application + " SCHEMA VALIDATION: " + getErrMsg(), xmlParser.getHashMapValue(), reqCode);
                }
            }
        }
        xmlMsgChecker.closeConsumer();
    }

    private static String getHashmapValue(Object hasp, String tag) {
        int t = 0;
        int t2 = 0;
        int t3 = 0;
        int t4 = 0;
        //Object hasp = hashMap.get(value);

        t = hasp.toString().indexOf(tag);
        if (t > 0) {
            t2 = hasp.toString().indexOf("=", t);
            t3 = hasp.toString().indexOf(",", t2);
            t4 = hasp.toString().indexOf("}", t);
            if (t3 < 0) {
                t3 = t4 - t2;
                logger.info(hasp.toString().substring(t2 + 1, t3 + t2));
                return (hasp.toString().substring(t2 + 1, t3 + t2));
            }

            logger.info(hasp.toString().substring(t2 + 1, t3));
            return (hasp.toString().substring(t2 + 1, t3));
        }
        return " ";
    }

    private static boolean validateXMLRequest(String xmlMsgReq, String xsdFile, String reqCode) throws SAXException, IOException {
        if (validateXML(xmlMsgReq, xsdFile, reqCode)) {
            return true;
        }
        //disable validation for polaris to go thru
        return false;
    }
    private static jms.QueueUtility xmlMsgSender1 = null;

    public static void sendMessage() {
        String unicode = null;
        utility.XPathReader xmlreader = new utility.XPathReader();
        File file = new File(sampleXmlFolder);
        for (String s : file.list()) {
            String fName = file.getPath() + "\\" + s;
            File f = new File(fName);
            if (!f.isDirectory()) {
                xmlreader.setXmlFileName(fName);
                unicode = xmlreader.getValue(paramPropXMLPath.getProperty(application));
                if (unicode == null || unicode.length() <= 0) {
                    unicode = xmlreader.getValue(paramPropXMLPath.getProperty(application));
                }
                xmlMsgSender1.sendMessage(fName, unicode);
                msgSentCt++;

                unicode = null;
            }
        }
    }

    public static void sendResponseMsg(byte[] xmlMsg, String transNum) {
        try {
            xmlMsgSender.sendMessage(xmlMsg, transNum);
            msgSentCt1++;

        } catch (Exception ex) {
            logger.error(ex);
        }
    }

    public static void sendResponseMsg(byte[] xmlMsg, String transNum, String corrID) {
        try {
            xmlMsgSender.sendMessage(xmlMsg, transNum, corrID);
            msgSentCt1++;

        } catch (Exception ex) {
            logger.error(ex);
        }
    }

    public static void sendResponseMsg(String xmlMsg, String transNum, String requestCode) {
        try {

            if (requestCode.compareToIgnoreCase("CIFSRCH") == 0 && application.compareToIgnoreCase("KASTEL") == 0) {
                xmlMsgSenderCif.connectProducer();
                xmlMsgSenderCif.sendTxtMessage(xmlMsg, transNum);
                xmlMsgSenderCif.closeProducer();
            } else {
                xmlMsgSender.sendTxtMessage(xmlMsg, transNum);
            }
            msgSentCt1++;

        } catch (Exception ex) {
            logger.error(ex);
        }
    }

    public static void sendResponseMsg(String xmlMsg, String transNum, String requestCode, String corrID) {
        try {
            if (requestCode.compareToIgnoreCase("CIFSRCH") == 0 && application.compareToIgnoreCase("KASTEL") == 0) {
                xmlMsgSenderCif.sendTxtMessage(xmlMsg, transNum);
            } else {
                xmlMsgSender.sendTxtMessage(xmlMsg, transNum, corrID);
            }
            msgSentCt1++;

        } catch (Exception ex) {
            logger.error(ex);
        }
    }

    public static void sendResponseMsg(String xmlMsg, String transNum, String requestCode, String corrID, String _charSet, String _encoding) {
        try {
            if (requestCode.compareToIgnoreCase("CIFSRCH") == 0 && application.compareToIgnoreCase("KASTEL") == 0) {
                //xmlMsgSenderCif.sendTxtMessage(xmlMsg, transNum);
                xmlMsgSenderCif.connectProducer();
                //xmlMsgSenderCif.sendTxtMessage(xmlMsg, transNum, corrID, charSet, encoding);
                xmlMsgSenderCif.closeProducer();
            } else if (requestCode.compareToIgnoreCase("1015") == 0 && application.compareToIgnoreCase("ECIB+") == 0) {
                //xmlMsgSenderCif.sendTxtMessage(xmlMsg, transNum);
                XMLFXRATEQUEUE.connectProducer();
                XMLFXRATEQUEUE.sendTxtMessage(xmlMsg, transNum, corrID, charSet, encoding);
                XMLFXRATEQUEUE.closeProducer();
            } else if (requestCode.compareToIgnoreCase("1021") == 0 && application.compareToIgnoreCase("ECIB+") == 0) {
                //xmlMsgSenderCif.sendTxtMessage(xmlMsg, transNum);
                XMLBENACCRESQUEUE.connectProducer();
                System.out.println("main class:" + corrID);
                XMLBENACCRESQUEUE.sendTxtMessage(xmlMsg, transNum, corrID, charSet, encoding);

                XMLBENACCRESQUEUE.closeProducer();
            } else {
                xmlMsgSender.sendTxtMessage(xmlMsg, transNum, corrID, charSet, encoding);
            }
            msgSentCt1++;

        } catch (Exception ex) {
            logger.error(ex);
        }
    }

    public static void sendErrorMsg(byte[] xmlMsg, String uniqueCode) {
        try {
            xmlMsgError.sendMessage(xmlMsg, uniqueCode);
            msgErrorCt++;

        } catch (Exception ex) {
            logger.error(ex);
        }
    }

    public static void sendTxtErrorMsg(String xmlMsg, String uniqueCode) {
        try {
            xmlMsgError.sendTxtMessage(xmlMsg, uniqueCode);
            msgErrorCt++;

        } catch (Exception ex) {
            logger.error(ex);
        }
    }

    public static void sendTxtErrorMsgKastle(String xmlMsg, String uniqueCode) {
        try {
            xmlMsgSender.sendTxtMessage(xmlMsg, uniqueCode);
            msgErrorCt++;

        } catch (Exception ex) {
            logger.error(ex);
        }
    }

    public static void sendTxtErrorMsg(String xmlMsg, String uniqueCode, String charSet, String encoding) {
        try {
            xmlMsgError.sendTxtMessage(xmlMsg, xmlMsgReceiver.getCorrID(), uniqueCode, charSet, encoding);
            msgErrorCt++;

        } catch (Exception ex) {
            logger.error(ex);
        }
    }

    public static void sendTxtErrorMsg(String xmlMsg, String uniqueCode, String corrID) {
        try {
            xmlMsgError.connectProducer();
            xmlMsgError.sendTxtMessage(xmlMsg, uniqueCode, corrID, charSet, encoding);
            msgErrorCt++;
            xmlMsgError.closeProducer();
        } catch (Exception ex) {
            logger.error(ex);
        }
    }

    public static void sendTxtErrorMsgWB(String xmlMsg, String uniqueCode, String corrID) {
        try {
            xmlMsgSender.connectProducer();
            xmlMsgSender.sendTxtMessageWB(xmlMsg, uniqueCode, corrID, charSet, encoding);
            msgErrorCt++;

            xmlMsgSender.closeProducer();
        } catch (Exception ex) {
            logger.error(ex);
        }
    }

    private static void xmlSchemaProp(String propFile) {
        File paramFile = new File(propFile);
        if (paramFile.exists()) {
            try {
                paramPropSchema.load(new FileInputStream(paramFile));
            } catch (Exception ex) {
                logger.error(ex);
            }
        }
    }

    private static void xmlClassMapping(String propFile) {
        File paramFile = new File(propFile);
        if (paramFile.exists()) {
            try {
                paramClassMapping.load(new FileInputStream(paramFile));
            } catch (Exception ex) {
                logger.error(ex);
            }
        }
    }

    private static void xmlPathProp(String propFile) {
        File paramFile = new File(propFile);
        if (paramFile.exists()) {
            try {
                paramPropXMLPath.load(new FileInputStream(paramFile));
            } catch (Exception ex) {
                logger.error(ex);
            }
        }
    }

    private static void initParameter(String propFile) {
        File paramFile = new File(propFile);
        te.setPassword("fasylqwe123!@#");
        if (paramFile.exists()) {
            try {
                paramProp.load(new FileInputStream(paramFile));
            } catch (Exception ex) {
                logger.error(ex);
            }
        }
        try {
            requestQueue = paramProp.getProperty("REQUESTQUEUE");
            replyQueue = paramProp.getProperty("REPLYQUEUE");
            cifQueue = paramProp.getProperty("CIFSEARCH");
            FXRATERESQUEUE = paramProp.getProperty("FXRATERESQUEUE");
            BENACCRESQUEUE = paramProp.getProperty("BENACCRESQUEUE");
            errorQueue = paramProp.getProperty("ERRORQUEUE");
            portErr = Integer.parseInt(paramProp.getProperty("ERRPORT"));
            portRequest = Integer.parseInt(paramProp.getProperty("ERRPORT"));
            portResponse = Integer.parseInt(paramProp.getProperty("RESPORT"));
            server = paramProp.getProperty("QUEUESERVER");
            queManager = paramProp.getProperty("QUEUEMANAGER");
            uid = paramProp.getProperty("SCHEMA");
            pwd = paramProp.getProperty("PASSWORD");
            //schemaConfig = paramProp.getProperty("XMLSCHEMACONFIG");
            application = paramProp.getProperty("APPLICATION");
            xmlPathConfig = paramProp.getProperty("XMLPATHCONFIG");
            classmapping = paramProp.getProperty("CLASSMAPPING");
            dbServer = paramProp.getProperty("DBSERVER");
            sid = paramProp.getProperty("SID");
            // sampleXmlFolder = paramProp.getProperty("SAMPLEXML");
            timeInterval = Long.parseLong(paramProp.getProperty("TIMEINTERVAL"));
            channel = paramProp.getProperty("CHANNEL");
            charSet = paramProp.getProperty("CHARSET");
            encoding = paramProp.getProperty("ENCODING");
            dbTimeout = Integer.parseInt(paramProp.getProperty("DBTIMEOUT"));
            dbConfig = paramProp.getProperty("DBCONFIG");
            genKey = paramProp.getProperty("GENKEY");
            org.apache.log4j.PropertyConfigurator.configure(paramProp.getProperty("LOG4JPROP"));

            setSSLPROPERTY(paramProp.getProperty("SSLPROPERTY"));

            setKEY_STORE_URL(paramProp.getProperty("KEY_STORE_URL"));
            setKEY_STORE_PASSWORD(paramProp.getProperty("KEY_STORE_PASSWORD"));
            setTRUST_STORE_URL(paramProp.getProperty("TRUST_STORE_URL"));
            setTRUST_STORE_PASSWORD(paramProp.getProperty("TRUST_STORE_PASSWORD"));
            setSHOW_DEBUG_INFO(paramProp.getProperty("SHOW_DEBUG_INFO"));

            setUSE_PROXY(paramProp.getProperty("USE_PROXY"));
            setPROXY_HOST(paramProp.getProperty("PROXY_HOST"));
            setPROXY_PORT(paramProp.getProperty("PROXY_PORT"));

            xmlSchemaProp(schemaConfig);
            xmlPathProp(xmlPathConfig);
            url = "jdbc:oracle:thin:@" + dbServer + ":1521:" + sid;
            //xmlMsgReceiver = new jms.QueueUtility(requestQueue, "receive", portResponse, server, queManager, channel);
            xmlMsgReceiver = new jms.QueueUtility(requestQueue, "receive", portResponse, server, queManager, channel);
            xmlMsgChecker = new jms.QueueUtility(requestQueue, "receive", portResponse, server, queManager, channel);
            xmlMsgSender = new jms.QueueUtility(replyQueue, "send", portRequest, server, queManager, channel);
            XMLFXRATEQUEUE = new jms.QueueUtility(FXRATERESQUEUE, "send", portRequest, server, queManager, channel);
            XMLBENACCRESQUEUE = new jms.QueueUtility(BENACCRESQUEUE, "send", portRequest, server, queManager, channel);
            xmlMsgError = new jms.QueueUtility(errorQueue, "send", portErr, server, queManager, channel);
            xmlMsgSender1 = new jms.QueueUtility(requestQueue, "send", portResponse, server, queManager, channel);
            getDbConfig();
        } catch (Exception ex) {
            logger.error(ex);
        }
    }

    public static void setSSLPROPERTY(String SSLPROPERTY) {
        MainNonThread.SSLPROPERTY = SSLPROPERTY;
        System.out.println("setting the ssl property " + SSLPROPERTY);
    }

    public static String getSSLPROPERTY() {
        return SSLPROPERTY;
    }

    public static void setKEY_STORE_URL(String KEY_STORE_URL) {
        MainNonThread.KEY_STORE_URL = KEY_STORE_URL;

    }

    public static String getKEY_STORE_URL() {
        return KEY_STORE_URL;
    }

    public static void setPROXY_PORT(String PROXY_HOST) {
        MainNonThread.PROXY_PORT = PROXY_PORT;

    }

    public static String getPROXY_PORT() {
        return PROXY_PORT;
    }

    public static void setPROXY_HOST(String PROXY_HOST) {
        MainNonThread.PROXY_HOST = PROXY_HOST;

    }

    public static String getPROXY_HOST() {
        return PROXY_HOST;
    }

    public static void setUSE_PROXY(String USE_PROXY) {
        MainNonThread.USE_PROXY = USE_PROXY;

    }

    public static String getUSE_PROXY() {
        return USE_PROXY;
    }

    public static void setSHOW_DEBUG_INFO(String SHOW_DEBUG_INFO) {
        MainNonThread.SHOW_DEBUG_INFO = SHOW_DEBUG_INFO;

    }

    public static String getSHOW_DEBUG_INFO() {
        return SHOW_DEBUG_INFO;
    }

    public static void setTRUST_STORE_PASSWORD(String TRUST_STORE_PASSWORD) {
        MainNonThread.TRUST_STORE_PASSWORD = TRUST_STORE_PASSWORD;

    }

    public static String getTRUST_STORE_PASSWORD() {
        return TRUST_STORE_PASSWORD;
    }

    public static void setTRUST_STORE_URL(String TRUST_STORE_URL) {
        MainNonThread.TRUST_STORE_URL = TRUST_STORE_URL;

    }

    public static String getTRUST_STORE_URL() {
        return TRUST_STORE_URL;
    }

    public static void setKEY_STORE_PASSWORD(String KEY_STORE_PASSWORD) {
        MainNonThread.KEY_STORE_PASSWORD = KEY_STORE_PASSWORD;

    }

    public static String getKEY_STORE_PASSWORD() {
        return KEY_STORE_PASSWORD;
    }

    private static boolean validateXML(String xml, String xsd, String requestCode) throws SAXException, IOException {
        SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
        File schemaLocation = new File(xsd);
        Schema schema = factory.newSchema(schemaLocation);
        Validator validator = schema.newValidator();
        Source source = new StreamSource(new StringReader(xml));
        try {
            validator.validate(source);
            return true;
        } catch (SAXException ex) {
            logger.error(requestCode + ":" + ex);
            setErrMsg(ex.getMessage());
        }

        return false;
    }
    //Kastle Error Response

    private static String buildErrorMsg(String errDesc, String request, Object MsgBody, Object MsgHeader) {

        if ((request.compareToIgnoreCase("1021") == 0)) {
            String TnxCode = "";
            String Result = "";
            if (request.compareToIgnoreCase("1021") == 0) {
                TnxCode = "1022";
            }

            Result = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                    + "<Msg>"
                    + "<MsgHeader>"
                    + "<ServiceId>" + getHashmapValue(MsgHeader, "ServiceId") + "</ServiceId>"
                    + "<TxnCode>" + TnxCode + "</TxnCode>"
                    + "</MsgHeader>"
                    + "<MsgBody>"
                    + "<BeneAcctDtl>"
                    + "<AppCode>" + request + "</AppCode>"
                    + "<CMSTransID>" + getHashmapValue(MsgBody, "CMSTransID") + "</CMSTransID>"
                    + "<BeneAccountNmbr>" + getHashmapValue(MsgBody, "BeneAccountNmbr") + "</BeneAccountNmbr>"
                    + "<AcctCCY>" + getHashmapValue(MsgBody, "AcctCCY") + "</AcctCCY>"
                    + "<StatusCode>99</StatusCode>"
                    + "<ErrorCode>ERR-9999</ErrorCode>"
                    + "<ErrorReason>" + errDesc + "</ErrorReason>"
                    + "</BeneAcctDtl>"
                    + "</MsgBody>"
                    + "</Msg>";
            logger.info(Result);
            return Result;
        }
        return " ";

    }

    private static String buildErrorMsg(String errCode, HashMap param, String request) {
        StringBuilder sb = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sb.append("<kastelResponse><header>");
        sb.append("<msgType>" + (String) param.get("msgType") + "</msgType>");
        sb.append("<transCode>" + request + "</transCode>");
        sb.append("<transNumber>" + (String) param.get("transNumber") + "</transNumber>");
        sb.append("<originator>" + (String) param.get("originator") + "</originator>");
        sb.append("<receiver>" + (String) param.get("receiver") + "</receiver>");
        sb.append("<timestamp>" + (String) param.get("timestamp") + "</timestamp>");
        sb.append("<legacyCompanyCode>" + (String) param.get("legacyCompanyCode") + "</legacyCompanyCode>");
        sb.append("</header>");
        if (request.compareToIgnoreCase("BLKCUST") == 0) {
            sb.append("<blacklistCustomer><msgDesc>" + errCode + ": " + getErrMsg() + "</msgDesc></blacklistCustomer>");
        } else if (request.compareToIgnoreCase("CIFSRCH") == 0) {
            sb.append("<cifSearch><msgDesc>" + errCode + ": " + getErrMsg() + "</msgDesc></cifSearch>");
        } else if (request.compareToIgnoreCase("LNACCRT") == 0) {
            sb.append("<loanAccountSetup><msgDesc>" + errCode + ": " + getErrMsg() + "</msgDesc></loanAccountSetup>");
        } else if (request.compareToIgnoreCase("EXPDTLS") == 0) {
            sb.append("<exposureEnquiry><msgDesc>" + errCode + ": " + getErrMsg() + "</msgDesc></exposureEnquiry>");
        } else if (request.compareToIgnoreCase("DSBADV") == 0) {
            sb.append("<disbursementAdvice><msgDesc>" + errCode + ": " + getErrMsg() + "</msgDesc></disbursementAdvice>");
        } else if (request.compareToIgnoreCase("CIFUPD") == 0) {
            sb.append("<cifInsertUpdate><msgDesc>" + errCode + ": " + getErrMsg() + "</msgDesc></cifInsertUpdate>");
        } else if (request.compareToIgnoreCase("CIFINS") == 0) {
            sb.append("<cifInsertUpdate><msgDesc>" + errCode + ": " + getErrMsg() + "</msgDesc></cifInsertUpdate>");
        } else if (request.compareToIgnoreCase("CIFENQ") == 0) {
            sb.append("<cifEnquiry><msgDesc>" + errCode + ": " + getErrMsg() + "</msgDesc></cifEnquiry>");
        } else if (request.compareToIgnoreCase("CIFENQ") == 0) {
            sb.append("<cifEnquiry><msgDesc>" + errCode + ": " + getErrMsg() + "</msgDesc></cifEnquiry>");
        }
        //CIFINS
        sb.append("</kastelResponse>");

        return sb.toString();
    }

    private static String buildErrorMsg(String errDesc, String request, String uniqueCode, String branchName, String txnDtTime) {
        StringBuilder sb = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sb.append("<ArielResponse>");
        sb.append("<MessageType>2</MessageType>");
        sb.append("<MessageDesc>" + errDesc + "</MessageDesc>");
        sb.append("<ProcCode>" + request + "</ProcCode>");
        sb.append("<ProcCode>" + uniqueCode + "</ProcCode>");
        sb.append("<BranchName>" + branchName + "</BranchName>");
        sb.append("<TxnDtTime>" + txnDtTime + "</TxnDtTime>");
        sb.append("</ArielResponse>");
        return sb.toString();
    }

    private static String buildErrorMsg(String errDesc, String request, String xref) {
        StringBuilder sb = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sb.append("<UPLOAD_FX_ACKMESSAGE>");
        sb.append("<STATUS>FAILURE</STATUS>");
        sb.append("<XREF>" + xref + "</XREF>");
        sb.append("<ERRORMESSAGE>" + request + " " + errDesc + "</ERRORMESSAGE>");
        sb.append("</UPLOAD_FX_ACKMESSAGE>");
        return sb.toString();
    }

    private static OracleConnection getOracleConnection(String uid, String pwd, String url, String affCode) {
        //PasswordEncryptor pwde = new PasswordEncryptor();

        OracleConnection conn = null;
        try {
            if (ods == null) {
                ods = new OracleDataSource();
                ods.setPassword(te.decrypt(pwd));
                ods.setUser(uid);
                ods.setURL(url);
            }
            conn = (OracleConnection) ods.getConnection();
            if (listConn.get(affCode) == null) {
                listConn.put(affCode, conn);
            }
        } catch (Exception ex) {
            logger.error(ex);
        }
        return conn;
    }
    //Just to return something, normal db package function shld be called

    private static String getMacAddress() {
        String retVal = "";
        try {
            InetAddress address = InetAddress.getLocalHost();


            /*
             * Get NetworkInterface for the current host and then read the
             * hardware address.
             */
            NetworkInterface ni = NetworkInterface.getByInetAddress(address);
            byte[] mac = ni.getHardwareAddress();

            /*
             * Extract each array of mac address and convert it to hexa with the
             * following format 08-00-27-DC-4A-9E.
             */
            for (int i = 0; i < mac.length; i++) {
                retVal = retVal + String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : "");
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        }

        return retVal;
    }

    private static void logXMLRequest(String xml, String requestCode) {
        logger.info("Raw XML Request:  " + requestCode + "  " + xml);
    }

    private static void logXMLResponse(String xml, String requestCode) {
        logger.info("Raw XML Response:  " + requestCode + "  " + xml);
    }

    private static void logXMLError(String xml, String requestCode) {
        logger.info("Raw XML Error:  " + requestCode + "  " + xml);
    }

    static class DatabaseThread implements Runnable {

        public ProcessorBase kasProc = null;
        public byte[] retByte = null;
        public String requestCode = null;

        public void run() {
            retByte = kasProc.getResponse(hashMap, requestCode);
        }
    }

    static class QueueThread implements Runnable {

        public ProcessorBase kasProc = null;
        public byte[] retByte = null;
        public String requestCode = null;

        //@Override
        public void run() {
            retByte = kasProc.getResponse(hashMap, requestCode);
        }
    }
}
