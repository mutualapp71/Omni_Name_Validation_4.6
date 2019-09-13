package requesthandlers;

import java.io.InputStream;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleConnection;
import oracle.sql.CLOB;
import org.apache.log4j.Logger;

public class PolarisProcessor
  extends ProcessorBase
{
  private OracleConnection _conn;
  private String _requestCode;
  static Logger logger = Logger.getLogger(PolarisProcessor.class);
  
  public PolarisProcessor(String requestCode, OracleConnection conn)
  {
    _conn = conn;
    _requestCode = requestCode;
  }
  
  public byte[] getResponse(HashMap hashMap, String requestCode)
  {
    byte[] retByte = null;
    if (requestCode.compareToIgnoreCase("FXDEAL") == 0) {
      retByte = buildFXUPLOADResponse((HashMap)hashMap.get("UPLOAD_FX_DEAL"));
    } else if (requestCode.compareToIgnoreCase("FXREVS") == 0) {
      retByte = buildFXREVERSEResponse((HashMap)hashMap.get("UPLOAD_FX_REVERSE"));
    } else if (requestCode.compareToIgnoreCase("FXSWAP") == 0) {
      retByte = buildFXSWAPResponse((HashMap)hashMap.get("UPLOAD_FXSWAP_DEAL"));
    } else if (requestCode.compareToIgnoreCase("FXAMND") == 0) {
      retByte = buildFXAMENDResponse((HashMap)hashMap.get("UPLOAD_FX_AMND"));
    } else if (requestCode.compareToIgnoreCase("FXAMSP") == 0) {
      retByte = buildFXSWAPAMENDResponse((HashMap)hashMap.get("UPLOAD_FXSWAP_AMND"));
    } else if (requestCode.compareToIgnoreCase("FXRVSP") == 0) {
      retByte = buildFXSWAPREVResponse((HashMap)hashMap.get("UPLOAD_FXSWAP_REVERSE"));
    } else if (requestCode.compareToIgnoreCase("MMDEAL") == 0) {
      retByte = buildFXMMResponse((HashMap)hashMap.get("UPLOAD_MM_DEAL"));
    }
    return retByte;
  }
  
  public byte[] buildFXMMResponse(HashMap values)
  {
    byte[] retval = null;
    setBStatus(false);
    try
    {
      CLOB clob = null;
      int success = 0;
      OracleCallableStatement stmt = (OracleCallableStatement)_conn.prepareCall("{? = call PKG_POLARIS_DEAL.fn_REVERSE2_swap(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
      
      stmt.registerOutParameter(1, -5);
      stmt.registerOutParameter(83, 2005);
      stmt.registerOutParameter(84, 2005);
      setStmtString(stmt, values, "XREF", 2);
      setStmtString(stmt, values, "SCODE", 3);
      setStmtString(stmt, values, "BRN", 4);
      setStmtString(stmt, values, "PRD", 5);
      setStmtNumeric(stmt, values, "CPTY", 6);
      setStmtString(stmt, values, "CPTYCONFSTAT", 7);
      setStmtNumeric(stmt, values, "AMT", 8);
      setStmtString(stmt, values, "CCY", 9);
      setStmtString(stmt, values, "ORGL_STDT", 10);
      setStmtString(stmt, values, "TRNDT", 11);
      setStmtString(stmt, values, "VALDT", 12);
      setStmtString2(stmt, values, "MAT_TYPE", 13);
      setStmtString(stmt, values, "MATDT", 14);
      setStmtNumeric(stmt, values, "NOTC_DAYS", 15);
      setStmtNumeric(stmt, values, "TENOR", 16);
      setStmtString(stmt, values, "DFLT_ACC", 17);
      setStmtString(stmt, values, "DFLT_ACC_BRN", 18);
      setStmtString(stmt, values, "LINE", 19);
      setStmtString(stmt, values, "DEALER", 20);
      setStmtString(stmt, values, "BRK", 21);
      setStmtString(stmt, values, "BRKCONFSTAT", 22);
      setStmtString(stmt, values, "TDESC", 23);
      setStmtString(stmt, values, "AUTHSTAT", 24);
      setStmtString(stmt, values, "AMORTYPE", 25);
      setStmtString(stmt, values, "ANNLOAN", 26);
      setStmtString(stmt, values, "APPCHG", 27);
      setStmtString(stmt, values, "APPROLLTAX", 28);
      setStmtString(stmt, values, "ASSREF", 29);
      setStmtString(stmt, values, "AUTCRECRACC", 30);
      setStmtString(stmt, values, "AUTCREDRACC", 31);
      setStmtString(stmt, values, "AUTPROVREQD", 32);
      setStmtString(stmt, values, "CASSCHD", 33);
      setStmtString(stmt, values, "CLUSID", 34);
      setStmtString(stmt, values, "CLUSSIZE", 35);
      setStmtString(stmt, values, "DEDTAXCAP", 36);
      setStmtString(stmt, values, "DEMBASIS", 37);
      setStmtString(stmt, values, "DFLTSETTCCY", 38);
      setStmtString(stmt, values, "DRSETLAC", 39);
      setStmtString(stmt, values, "DRSETLACBR", 40);
      setStmtString(stmt, values, "DRSETLCCY", 41);
      setStmtString(stmt, values, "EXPCAT", 42);
      setStmtString(stmt, values, "HOLMTH", 43);
      setStmtString(stmt, values, "HOLMTHFLG", 44);
      setStmtString(stmt, values, "IGHOL", 45);
      setStmtString(stmt, values, "LIQBVSCH", 46);
      setStmtString(stmt, values, "LIQODSCH", 47);
      setStmtString(stmt, values, "LNSTMTCYC", 48);
      setStmtString(stmt, values, "LNSTMTTYPE", 49);
      setStmtNumeric(stmt, values, "MAXDRWDOWNAMT", 50);
      setStmtString(stmt, values, "MVACRMTH", 51);
      setStmtString(stmt, values, "NEWCOMP", 52);
      setStmtString(stmt, values, "NOTCREQD", 53);
      setStmtString(stmt, values, "PAREXTREFNO", 54);
      setStmtString(stmt, values, "PARPYMTMLIQ", 55);
      setStmtString(stmt, values, "PRINLIQN", 56);
      setStmtString(stmt, values, "PROCID", 57);
      setStmtString(stmt, values, "RELREF", 58);
      setStmtString(stmt, values, "REVCOMM", 59);
      setStmtNumeric(stmt, values, "RFEXPAMT", 60);
      setStmtString(stmt, values, "ROLLALLW", 61);
      setStmtString(stmt, values, "ROLLAMTTYP", 62);
      setStmtString(stmt, values, "ROLLAMT", 63);
      setStmtString(stmt, values, "ROLLICCFFROM", 64);
      setStmtString(stmt, values, "ROLLMATDT", 65);
      setStmtString(stmt, values, "ROLLMATTYPE", 66);
      setStmtNumeric(stmt, values, "ROLLNOTDAYS", 67);
      setStmtString(stmt, values, "ROLLTYPE", 68);
      setStmtString(stmt, values, "SCHDDEFNBAS", 69);
      setStmtString(stmt, values, "SCHDMVMT", 70);
      setStmtString(stmt, values, "SGENREQD", 71);
      setStmtNumeric(stmt, values, "STMTDAY", 72);
      setStmtString(stmt, values, "STATCONT", 73);
      setStmtString(stmt, values, "TRCKRECVALIQ", 74);
      setStmtString(stmt, values, "TRCKRECVMLIQ", 75);
      setStmtString(stmt, values, "TRESPLAMT", 76);
      setStmtString(stmt, values, "UPDUTLROLL", 77);
      setStmtString(stmt, values, "UDSTAT", 78);
      setStmtString(stmt, values, "VRFYFUND", 79);
      setStmtString(stmt, values, "VRFYFUNDINT", 80);
      setStmtString(stmt, values, "VRFYFUNDPLTY", 81);
      setStmtString(stmt, values, "VRFYFUNDPRIN", 82);
      stmt.execute();
      success = stmt.getInt(1);
      if (success == 0)
      {
        clob = stmt.getCLOB(84);
        retval = new byte[(int)clob.length()];
        InputStream reader = clob.getAsciiStream();
        reader.read(retval);
        logRequest(values, "MMDEAL", "Successully Processed");
      }
      else
      {
        clob = stmt.getCLOB(83);
        retval = new byte[(int)clob.length()];
        InputStream reader = clob.getAsciiStream();
        reader.read(retval);
        setBStatus(true);
        logger.error(new String(retval));
        logRequest(values, "MMDEAL", "Failed Processing");
      }
    }
    catch (Exception ex)
    {
      logger.error(ex);
      setBStatus(true);
      logRequest(values, "MMDEAL", "Failed Processing");
    }
    return retval;
  }
  
  public byte[] buildFXSWAPResponse(HashMap values)
  {
    byte[] retval = null;
    setBStatus(false);
    try
    {
      CLOB clob = null;
      int success = 0;
      OracleCallableStatement stmt = (OracleCallableStatement)_conn.prepareCall("{? = call PKG_POLARIS_DEAL.fn_SWAP(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
      
      stmt.registerOutParameter(1, -5);
      stmt.registerOutParameter(61, 2005);
      stmt.registerOutParameter(62, 2005);
      setStmtString(stmt, values, "SCODE", 2);
      setStmtString(stmt, values, "XREF", 3);
      setStmtString(stmt, values, "XREF2", 4);
      setStmtString(stmt, values, "UREF", 5);
      setStmtString(stmt, values, "UREF2", 6);
      setStmtString(stmt, values, "BRN", 7);
      setStmtString(stmt, values, "PRD", 8);
      setStmtString(stmt, values, "CPTY", 9);
      setStmtString(stmt, values, "BKDT", 10);
      setStmtString(stmt, values, "BS", 11);
      setStmtNumeric(stmt, values, "BOTAMT", 12);
      setStmtNumeric(stmt, values, "BOTAMT2", 13);
      setStmtString(stmt, values, "BOTCCY", 14);
      setStmtString(stmt, values, "BOTVALDT", 15);
      setStmtString(stmt, values, "BOTVALDT2", 16);
      setStmtString(stmt, values, "BOTACCBRN", 17);
      setStmtString(stmt, values, "BOTACCBRN2", 18);
      setStmtString(stmt, values, "BOTACC", 19);
      setStmtString(stmt, values, "BOTACC2", 20);
      setStmtNumeric(stmt, values, "SOLDAMT", 21);
      setStmtNumeric(stmt, values, "SOLDAMT2", 22);
      setStmtString(stmt, values, "SOLDCCY", 23);
      setStmtString(stmt, values, "SOLDVALDT", 24);
      setStmtString(stmt, values, "SOLDVALDT2", 25);
      setStmtString(stmt, values, "SOLDACCBRN", 26);
      setStmtString(stmt, values, "SOLDACCBRN2", 27);
      setStmtString(stmt, values, "SOLDACC", 28);
      setStmtString(stmt, values, "SOLDACC2", 29);
      setStmtNumeric(stmt, values, "XRATE", 30);
      setStmtNumeric(stmt, values, "XRATE2", 31);
      setStmtString(stmt, values, "DEALER", 32);
      setStmtString(stmt, values, "BRK", 33);
      setStmtString(stmt, values, "BRKAMT", 34);
      setStmtString(stmt, values, "BRKCCY", 35);
      setStmtString(stmt, values, "BRK2", 36);
      setStmtString(stmt, values, "BRKAMT2", 37);
      setStmtString(stmt, values, "BRKCCY2", 38);
      setStmtString(stmt, values, "TDESC", 39);
      setStmtString(stmt, values, "LINE", 40);
      setStmtString(stmt, values, "RECV_FRM", 41);
      setStmtString(stmt, values, "RECV_FRM2", 42);
      setStmtString(stmt, values, "PAYTO", 43);
      setStmtString(stmt, values, "PAYTO2", 44);
      setStmtString(stmt, values, "OPTDT", 45);
      setStmtString(stmt, values, "SPOTDT", 46);
      setStmtString(stmt, values, "SPOTDT2", 47);
      setStmtString(stmt, values, "SPOTRATE", 48);
      setStmtString(stmt, values, "SPOTRATE2", 49);
      setStmtString(stmt, values, "AUTOLIQD", 50);
      setStmtString(stmt, values, "AUTOLIQD2", 51);
      setStmtString(stmt, values, "AUTHSTAT", 52);
      setStmtString(stmt, values, "NETTYPE", 53);
      setStmtString(stmt, values, "PAREXTREF", 54);
      setStmtString(stmt, values, "SWAPREF", 55);
      setStmtString(stmt, values, "TRACKSETRISK", 56);
      setStmtString(stmt, values, "PSETRISKLINE", 57);
      setStmtString(stmt, values, "TRACKWHTRISK", 58);
      setStmtString(stmt, values, "WHTRISKLINE", 59);
      setStmtString(stmt, values, "NETSETRISKFLG", 60);
      stmt.execute();
      success = stmt.getInt(1);
      if (success == 0)
      {
        clob = stmt.getCLOB(61);
        retval = new byte[(int)clob.length()];
        InputStream reader = clob.getAsciiStream();
        reader.read(retval);
        logRequest(values, "FXSWAP", "Successully Processed");
      }
      else
      {
        clob = stmt.getCLOB(62);
        retval = new byte[(int)clob.length()];
        InputStream reader = clob.getAsciiStream();
        reader.read(retval);
        setBStatus(true);
        logger.error(new String(retval));
        logRequest(values, "FXSWAP", "Failed Processing");
      }
    }
    catch (Exception ex)
    {
      logger.error(ex);
      setBStatus(true);
      logRequest(values, "FXSWAP", "Failed Processing");
    }
    return retval;
  }
  
  public byte[] buildFXSWAPAMENDResponse(HashMap values)
  {
    byte[] retval = null;
    setBStatus(false);
    try
    {
      CLOB clob = null;
      int success = 0;
      OracleCallableStatement stmt = (OracleCallableStatement)_conn.prepareCall("{? = call PKG_POLARIS_DEAL.fn_amendswap(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
      
      stmt.registerOutParameter(1, -5);
      stmt.registerOutParameter(61, 2005);
      stmt.registerOutParameter(62, 2005);
      setStmtString(stmt, values, "SCODE", 2);
      setStmtString(stmt, values, "XREF", 3);
      setStmtString(stmt, values, "XREF2", 4);
      setStmtString(stmt, values, "UREF", 5);
      setStmtString(stmt, values, "UREF2", 6);
      setStmtString(stmt, values, "BRN", 7);
      setStmtString(stmt, values, "PRD", 8);
      setStmtString(stmt, values, "CPTY", 9);
      setStmtString(stmt, values, "BKDT", 10);
      setStmtString(stmt, values, "BS", 11);
      setStmtNumeric(stmt, values, "BOTAMT", 12);
      setStmtNumeric(stmt, values, "BOTAMT2", 13);
      setStmtString(stmt, values, "BOTCCY", 14);
      setStmtString(stmt, values, "BOTVALDT", 15);
      setStmtString(stmt, values, "BOTVALDT2", 16);
      setStmtString(stmt, values, "BOTACCBRN", 17);
      setStmtString(stmt, values, "BOTACCBRN2", 18);
      setStmtString(stmt, values, "BOTACC", 19);
      setStmtString(stmt, values, "BOTACC2", 20);
      setStmtNumeric(stmt, values, "SOLDAMT", 21);
      setStmtNumeric(stmt, values, "SOLDAMT2", 22);
      setStmtString(stmt, values, "SOLDCCY", 23);
      setStmtString(stmt, values, "SOLDVALDT", 24);
      setStmtString(stmt, values, "SOLDVALDT2", 25);
      setStmtString(stmt, values, "SOLDACCBRN", 26);
      setStmtString(stmt, values, "SOLDACCBRN2", 27);
      setStmtString(stmt, values, "SOLDACC", 28);
      setStmtString(stmt, values, "SOLDACC2", 29);
      setStmtNumeric(stmt, values, "XRATE", 30);
      setStmtNumeric(stmt, values, "XRATE2", 31);
      setStmtString(stmt, values, "DEALER", 32);
      setStmtString(stmt, values, "BRK", 33);
      setStmtString(stmt, values, "BRKAMT", 34);
      setStmtString(stmt, values, "BRKCCY", 35);
      setStmtString(stmt, values, "BRK2", 36);
      setStmtString(stmt, values, "BRKAMT2", 37);
      setStmtString(stmt, values, "BRKCCY2", 38);
      setStmtString(stmt, values, "TDESC", 39);
      setStmtString(stmt, values, "LINE", 40);
      setStmtString(stmt, values, "RECV_FRM", 41);
      setStmtString(stmt, values, "RECV_FRM2", 42);
      setStmtString(stmt, values, "PAYTO", 43);
      setStmtString(stmt, values, "PAYTO2", 44);
      setStmtString(stmt, values, "OPTDT", 45);
      setStmtString(stmt, values, "SPOTDT", 46);
      setStmtString(stmt, values, "SPOTDT2", 47);
      setStmtString(stmt, values, "SPOTRATE", 48);
      setStmtString(stmt, values, "SPOTRATE2", 49);
      setStmtString(stmt, values, "AUTOLIQD", 50);
      setStmtString(stmt, values, "AUTOLIQD2", 51);
      setStmtString(stmt, values, "AUTHSTAT", 52);
      setStmtString(stmt, values, "NETTYPE", 53);
      setStmtString(stmt, values, "PAREXTREF", 54);
      setStmtString(stmt, values, "SWAPREF", 55);
      setStmtString(stmt, values, "TRACKSETRISK", 56);
      setStmtString(stmt, values, "PSETRISKLINE", 57);
      setStmtString(stmt, values, "TRACKWHTRISK", 58);
      setStmtString(stmt, values, "WHTRISKLINE", 59);
      setStmtString(stmt, values, "NETSETRISKFLG", 60);
      stmt.execute();
      success = stmt.getInt(1);
      if (success == 0)
      {
        clob = stmt.getCLOB(61);
        retval = new byte[(int)clob.length()];
        InputStream reader = clob.getAsciiStream();
        reader.read(retval);
        logRequest(values, "FXAMEND", "Successully Processed");
      }
      else
      {
        clob = stmt.getCLOB(62);
        retval = new byte[(int)clob.length()];
        InputStream reader = clob.getAsciiStream();
        reader.read(retval);
        setBStatus(true);
        logger.error(new String(retval));
        logRequest(values, "FXAMEND", "Failed Processing");
      }
    }
    catch (Exception ex)
    {
      logger.error(ex);
      setBStatus(true);
      logRequest(values, "FXAMEND", "Failed Processing");
    }
    return retval;
  }
  
  public byte[] buildFXAMENDResponse(HashMap values)
  {
    byte[] retval = null;
    setBStatus(false);
    try
    {
      CLOB clob = null;
      int success = 0;
      OracleCallableStatement stmt = (OracleCallableStatement)_conn.prepareCall("{? = call PKG_POLARIS_DEAL.fn_amend2(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
      
      stmt.registerOutParameter(1, -5);
      stmt.registerOutParameter(54, 2005);
      stmt.registerOutParameter(55, 2005);
      setStmtString(stmt, values, "SCODE", 2);
      setStmtString(stmt, values, "XREF", 3);
      setStmtString(stmt, values, "UREF", 4);
      setStmtString(stmt, values, "BRN", 5);
      setStmtString(stmt, values, "PRD", 6);
      setStmtString(stmt, values, "CPTY", 7);
      setStmtString(stmt, values, "BKDT", 8);
      setStmtString(stmt, values, "BS", 9);
      setStmtNumeric(stmt, values, "BOTAMT", 10);
      setStmtString(stmt, values, "BOTCCY", 11);
      setStmtString(stmt, values, "BOTVALDT", 12);
      setStmtString2(stmt, values, "BOTACCBRN", 13);
      setStmtString(stmt, values, "BOTACC", 14);
      setStmtNumeric(stmt, values, "SOLDAMT", 15);
      setStmtString(stmt, values, "SOLDCCY", 16);
      setStmtString(stmt, values, "SOLDVALDT", 17);
      setStmtString2(stmt, values, "SOLDACCBRN", 18);
      setStmtString(stmt, values, "SOLDACC", 19);
      setStmtNumeric(stmt, values, "XRATE", 20);
      setStmtNumeric(stmt, values, "LCY_EQV", 21);
      setStmtString(stmt, values, "DEALER", 22);
      setStmtString(stmt, values, "BRK", 23);
      setStmtNumeric(stmt, values, "BRKAMT", 24);
      setStmtString(stmt, values, "BRKCCY", 25);
      setStmtString(stmt, values, "TDESC", 26);
      setStmtString(stmt, values, "LINE", 27);
      setStmtString(stmt, values, "RECV_FRM", 28);
      setStmtString(stmt, values, "PAYTO", 29);
      setStmtString(stmt, values, "OPTDT", 30);
      setStmtString(stmt, values, "SPOTDT", 31);
      setStmtNumeric(stmt, values, "SPOTRATE", 32);
      setStmtString(stmt, values, "TAXSCHM", 33);
      setStmtNumeric(stmt, values, "SPOTLCYE", 34);
      setStmtString2(stmt, values, "AUTOLIQD", 35);
      setStmtString2(stmt, values, "AUTHSTAT", 36);
      setStmtNumeric(stmt, values, "BOTPREMDISC", 37);
      setStmtNumeric(stmt, values, "SOLPREMDIS", 38);
      setStmtNumeric(stmt, values, "INTRTBOT", 39);
      setStmtNumeric(stmt, values, "INTRTSOLD", 40);
      setStmtString(stmt, values, "NETTYPE", 41);
      setStmtString(stmt, values, "PAREXTREF", 42);
      setStmtString(stmt, values, "RELREF", 43);
      setStmtString(stmt, values, "TRACKSETRISK", 44);
      setStmtString(stmt, values, "TRACKPSETRISK", 45);
      setStmtString(stmt, values, "PSETRISKLINE", 46);
      setStmtString(stmt, values, "TRACKWHTRISK", 47);
      setStmtString(stmt, values, "WHTRISKLINE", 48);
      setStmtString2(stmt, values, "NETSETRISKFLG", 49);
      setStmtString2(stmt, values, "NETSETRISKFLG", 50);
      setStmtString2(stmt, values, "NETWHTRISKFLG", 51);
      setStmtString(stmt, values, "EXTSWAPREF", 52);
      setStmtString(stmt, values, "STTLMENT", 53);
      stmt.execute();
      success = stmt.getInt(1);
      if (success == 0)
      {
        clob = stmt.getCLOB(55);
        retval = new byte[(int)clob.length()];
        InputStream reader = clob.getAsciiStream();
        reader.read(retval);
        logRequest(values, "FXSWAPAMEND", "Successully Processed");
      }
      else
      {
        clob = stmt.getCLOB(54);
        retval = new byte[(int)clob.length()];
        InputStream reader = clob.getAsciiStream();
        reader.read(retval);
        setBStatus(true);
        logger.error(new String(retval));
        logRequest(values, "FXSWAPAMEND", "Failed Processing");
      }
    }
    catch (Exception ex)
    {
      logger.error(ex);
      setBStatus(true);
      logRequest(values, "FXSWAPAMEND", "Failed Processing");
    }
    return retval;
  }
  
  public byte[] buildFXSWAPREVResponse(HashMap values)
  {
    byte[] retval = null;
    setBStatus(false);
    try
    {
      CLOB clob = null;
      int success = 0;
      OracleCallableStatement stmt = (OracleCallableStatement)_conn.prepareCall("{? = call PKG_POLARIS_DEAL.fn_REVERSE2_swap(?,?,?,?,?)}");
      
      stmt.registerOutParameter(1, -5);
      stmt.registerOutParameter(5, 2005);
      stmt.registerOutParameter(6, 2005);
      setStmtString(stmt, values, "SCODE", 2);
      setStmtString(stmt, values, "XREF", 3);
      setStmtString(stmt, values, "SWAPREF", 4);
      stmt.execute();
      success = stmt.getInt(1);
      if (success == 0)
      {
        clob = stmt.getCLOB(6);
        retval = new byte[(int)clob.length()];
        InputStream reader = clob.getAsciiStream();
        reader.read(retval);
        logRequest(values, "FXRVSP", "Successully Processed");
      }
      else
      {
        clob = stmt.getCLOB(5);
        retval = new byte[(int)clob.length()];
        InputStream reader = clob.getAsciiStream();
        reader.read(retval);
        setBStatus(true);
        logger.error(new String(retval));
        logRequest(values, "FXRVSP", "Failed Processing");
      }
    }
    catch (Exception ex)
    {
      logger.error(ex);
      setBStatus(true);
      logRequest(values, "FXRVSP", "Failed Processing");
    }
    return retval;
  }
  
  public byte[] buildFXREVERSEResponse(HashMap values)
  {
    byte[] retval = null;
    setBStatus(false);
    try
    {
      CLOB clob = null;
      int success = 0;
      OracleCallableStatement stmt = (OracleCallableStatement)_conn.prepareCall("{? = call PKG_POLARIS_DEAL.fn_REVERSE(?,?,?,?,?,?)}");
      
      stmt.registerOutParameter(1, -5);
      stmt.registerOutParameter(6, 2005);
      stmt.registerOutParameter(7, 2005);
      setStmtString(stmt, values, "SCODE", 2);
      setStmtString(stmt, values, "XREF", 3);
      setStmtString(stmt, values, "FCCREF", 4);
      setStmtString(stmt, values, "AUTHSTAT", 5);
      stmt.execute();
      success = stmt.getInt(1);
      if (success == 0)
      {
        clob = stmt.getCLOB(7);
        retval = new byte[(int)clob.length()];
        InputStream reader = clob.getAsciiStream();
        reader.read(retval);
        logRequest(values, "FXREVERSE", "Successully Processed");
      }
      else
      {
        clob = stmt.getCLOB(6);
        retval = new byte[(int)clob.length()];
        InputStream reader = clob.getAsciiStream();
        reader.read(retval);
        setBStatus(true);
        logger.error(new String(retval));
        logRequest(values, "FXREVERSE", "Failed Processing");
      }
    }
    catch (Exception ex)
    {
      logger.error(ex);
      setBStatus(true);
      logRequest(values, "FXREVERSE", "Failed Processing");
    }
    return retval;
  }
  
  public byte[] buildFXUPLOADResponse(HashMap values)
  {
    byte[] retval = null;
    setBStatus(false);
    try
    {
      CLOB clob = null;
      int success = 0;
      OracleCallableStatement stmt = (OracleCallableStatement)_conn.prepareCall("{? = call PKG_POLARIS_DEAL.fn_new_deal(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
      
      stmt.registerOutParameter(1, -5);
      stmt.registerOutParameter(54, 2005);
      stmt.registerOutParameter(55, 2005);
      setStmtString(stmt, values, "SCODE", 2);
      setStmtString(stmt, values, "XREF", 3);
      setStmtString(stmt, values, "UREF", 4);
      setStmtString2(stmt, values, "BRN", 5);
      setStmtString2(stmt, values, "PRD", 6);
      setStmtString(stmt, values, "CPTY", 7);
      setStmtString(stmt, values, "BKDT", 8);
      setStmtString(stmt, values, "BS", 9);
      setStmtNumeric(stmt, values, "BOTAMT", 10);
      setStmtString2(stmt, values, "BOTCCY", 11);
      setStmtString(stmt, values, "BOTVALDT", 12);
      setStmtString(stmt, values, "BOTACCBRN", 13);
      setStmtString(stmt, values, "BOTACC", 14);
      setStmtNumeric(stmt, values, "SOLDAMT", 15);
      setStmtString2(stmt, values, "SOLDCCY", 16);
      setStmtString(stmt, values, "SOLDVALDT", 17);
      setStmtString2(stmt, values, "SOLDACCBRN", 18);
      setStmtString(stmt, values, "SOLDACC", 19);
      setStmtNumeric(stmt, values, "XRATE", 20);
      setStmtNumeric(stmt, values, "LCY_EQV", 21);
      setStmtString(stmt, values, "DEALER", 22);
      setStmtString(stmt, values, "BRK", 23);
      setStmtNumeric(stmt, values, "BRKAMT", 24);
      setStmtString(stmt, values, "BRKCCY", 25);
      setStmtString(stmt, values, "TDESC", 26);
      setStmtString(stmt, values, "LINE", 27);
      setStmtString(stmt, values, "RECV_FRM", 28);
      setStmtString(stmt, values, "PAYTO", 29);
      setStmtString(stmt, values, "OPTDT", 30);
      setStmtString(stmt, values, "SPOTDT", 31);
      setStmtNumeric(stmt, values, "SPOTRATE", 32);
      setStmtString(stmt, values, "TAXSCHM", 33);
      setStmtNumeric(stmt, values, "SPOTLCYE", 34);
      setStmtString2(stmt, values, "AUTOLIQD", 35);
      setStmtString2(stmt, values, "AUTHSTAT", 36);
      setStmtNumeric(stmt, values, "BOTPREMDISC", 37);
      setStmtNumeric(stmt, values, "SOLPREMDIS", 38);
      setStmtNumeric(stmt, values, "INTRTBOT", 39);
      setStmtString(stmt, values, "INTRTSOLD", 40);
      setStmtString(stmt, values, "NETTYPE", 41);
      setStmtString(stmt, values, "PAREXTREF", 42);
      setStmtString(stmt, values, "RELREF", 43);
      setStmtString(stmt, values, "TRACKSETRISK", 44);
      setStmtString(stmt, values, "TRACKPSETRISK", 45);
      setStmtString(stmt, values, "PSETRISKLINE", 46);
      setStmtString(stmt, values, "TRACKWHTRISK", 47);
      setStmtString(stmt, values, "WHTRISKLINE", 48);
      setStmtString2(stmt, values, "NETSETRISKFLG", 49);
      setStmtString2(stmt, values, "NETWHTRISKFLG", 50);
      setStmtString2(stmt, values, "NETPSETRISKFLG", 51);
      setStmtString(stmt, values, "EXTSWAPREF", 52);
      setStmtString(stmt, values, "STTLMENT", 53);
      stmt.execute();
      success = stmt.getInt(1);
      if (success == 0)
      {
        clob = stmt.getCLOB(55);
        retval = new byte[(int)clob.length()];
        InputStream reader = clob.getAsciiStream();
        reader.read(retval);
        logRequest(values, "FXDEAL", "Successully Processed");
      }
      else
      {
        clob = stmt.getCLOB(54);
        retval = new byte[(int)clob.length()];
        InputStream reader = clob.getAsciiStream();
        reader.read(retval);
        setBStatus(true);
        logger.error(new String(retval));
        logRequest(values, "FXDEAL", "Failed Processing");
      }
    }
    catch (Exception ex)
    {
      logger.error(ex);
      setBStatus(true);
      logRequest(values, "FXDEAL", "Failed Processing");
    }
    return retval;
  }
  
  private void logRequest(HashMap values, String requestCode, String status)
  {
    Calendar cal = Calendar.getInstance();
    String logMsg = "Polaris Request " + requestCode + " Date/Time" + cal.getTime().toString() + "\r\n";
    Object[] valArray = values.values().toArray();
    Object[] keyArray = values.keySet().toArray();
    try
    {
      for (int i = 0; i <= valArray.length - 1; i++) {
        if ((valArray[i] instanceof String)) {
          logMsg = logMsg + " " + (String)keyArray[i] + " :" + (String)valArray[i] + "\r\n";
        }
      }
      logMsg = logMsg + "Status Code: " + status;
      logger.info(logMsg);
    }
    catch (Exception ex)
    {
      logger.error(ex);
    }
  }
}
