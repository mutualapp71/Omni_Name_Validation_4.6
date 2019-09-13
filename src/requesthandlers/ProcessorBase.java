package requesthandlers;

import java.math.BigDecimal;
import java.nio.CharBuffer;
import java.sql.Clob;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import oracle.jdbc.OracleCallableStatement;
import org.apache.log4j.Logger;

public class ProcessorBase
{
  private boolean bStatus = false;
  private String errCode = "";
  private String errMsg = "";
  static Logger logger = Logger.getLogger(ProcessorBase.class);
  
  public boolean isBStatus()
  {
    return bStatus;
  }
  
  public byte[] getResponse(HashMap hashMap, String requestCode)
  {
    return null;
  }
  
  private byte[] checkUncompletedTask(String transNumber, String requestCode)
  {
    return null;
  }
  
  public void setBStatus(boolean bStatus)
  {
    this.bStatus = bStatus;
  }
  
  public String getErrCode()
  {
    return errCode;
  }
  
  public void setErrCode(String errCode)
  {
    this.errCode = errCode;
  }
  
  public String getErrMsg()
  {
    return errMsg;
  }
  
  public void setErrMsg(String errMsg)
  {
    this.errMsg = errMsg;
  }
  
  public void setStmtString(OracleCallableStatement stmt, HashMap values, String key, int index)
  {
    try
    {
      if (values.get(key) != null) {
        stmt.setString(index, values.get(key).toString());
      } else {
        stmt.setNull(index, 12);
      }
    }
    catch (Exception ex)
    {
      logger.error(ex);
      try
      {
        stmt.setNull(index, 12);
      }
      catch (Exception exc)
      {
        logger.error(exc);
      }
    }
  }
  
  public void setStmtClob(OracleCallableStatement stmt, HashMap values, String key, int index)
  {
    try
    {
      Clob cl;
      if (values.get(key) != null)
      {
        StringBuffer buffer = new StringBuffer(50000);
        
        buffer.append(values.get(key));
        cl = null;
      }
      else
      {
        stmt.setNull(index, 2005);
      }
    }
    catch (Exception ex)
    {
      logger.error(ex);
      try
      {
        stmt.setNull(index, 2005);
      }
      catch (Exception exc)
      {
        logger.error(exc);
      }
    }
  }
  
  public void setStmtString(OracleCallableStatement stmt, int index, Object values)
  {
    try
    {
      if (values != null) {
        stmt.setString(index, (String)values);
      } else {
        stmt.setNull(index, 12);
      }
    }
    catch (Exception ex)
    {
      logger.error(ex);
      try
      {
        stmt.setNull(index, 12);
      }
      catch (Exception exc)
      {
        logger.error(exc);
      }
    }
  }
  
  public void setStmtString2(OracleCallableStatement stmt, HashMap values, String key, int index)
  {
    try
    {
      if (values.get(key) != null) {
        stmt.setString(index, (String)values.get(key));
      } else {
        stmt.setNull(index, 1);
      }
    }
    catch (Exception ex)
    {
      logger.error(ex);
      try
      {
        stmt.setNull(index, 1);
      }
      catch (Exception exc)
      {
        logger.error(exc);
      }
    }
  }
  
  public void setStmtString2(OracleCallableStatement stmt, int index, Object values)
  {
    try
    {
      if (values != null) {
        stmt.setString(index, (String)values);
      } else {
        stmt.setNull(index, 1);
      }
    }
    catch (Exception ex)
    {
      logger.error(ex);
      try
      {
        stmt.setNull(index, 1);
      }
      catch (Exception exc)
      {
        logger.error(exc);
      }
    }
  }
  
  public void setStmtNumeric(OracleCallableStatement stmt, HashMap values, String key, int index)
  {
    try
    {
      if (values.get(key) != null) {
        stmt.setBigDecimal(index, new BigDecimal((String)values.get(key)));
      } else {
        stmt.setNull(index, 8);
      }
    }
    catch (Exception ex)
    {
      logger.error(ex);
      try
      {
        stmt.setNull(index, 2);
      }
      catch (Exception exc)
      {
        logger.error(exc);
      }
    }
  }
  
  public void setStmtNumeric(OracleCallableStatement stmt, int index, Object values)
  {
    try
    {
      if (values != null) {
        stmt.setBigDecimal(index, new BigDecimal((String)values));
      } else {
        stmt.setNull(index, 8);
      }
    }
    catch (Exception ex)
    {
      logger.error(ex);
      try
      {
        stmt.setNull(index, 2);
      }
      catch (Exception exc)
      {
        logger.error(exc);
      }
    }
  }
  
  public void setStmtDate(OracleCallableStatement stmt, HashMap values, String key, int index)
  {
    try
    {
      if (values.get(key) != null) {
        stmt.setDate(index, parseDate((String)values.get(key)));
      } else {
        stmt.setNull(index, 91);
      }
    }
    catch (Exception ex)
    {
      logger.error(ex);
      try
      {
        stmt.setNull(index, 91);
      }
      catch (Exception exc)
      {
        logger.error(exc);
      }
    }
  }
  
  public void setStmtInt(OracleCallableStatement stmt, HashMap values, String key, int index)
  {
    try
    {
      if (values.get(key) != null) {
        stmt.setInt(index, Integer.parseInt((String)values.get(key)));
      } else {
        stmt.setNull(index, -5);
      }
    }
    catch (Exception ex)
    {
      logger.error(ex);
      try
      {
        stmt.setNull(index, -5);
      }
      catch (Exception exc)
      {
        logger.error(exc);
      }
    }
  }
  
  public void setStmtInt(OracleCallableStatement stmt, int index, Object values)
  {
    try
    {
      if (values != null) {
        stmt.setInt(index, Integer.parseInt((String)values));
      } else {
        stmt.setNull(index, -5);
      }
    }
    catch (Exception ex)
    {
      logger.error(ex);
      try
      {
        stmt.setNull(index, -5);
      }
      catch (Exception exc)
      {
        logger.error(exc);
      }
    }
  }
  
  public void setStmtLong(OracleCallableStatement stmt, HashMap values, String key, int index)
  {
    try
    {
      if (values.get(key) != null) {
        stmt.setLong(index, Long.parseLong((String)values.get(key)));
      } else {
        stmt.setNull(index, -5);
      }
    }
    catch (Exception ex)
    {
      logger.error(ex);
      try
      {
        stmt.setNull(index, -5);
      }
      catch (Exception exc)
      {
        logger.error(exc);
      }
    }
  }
  
  public void setStmtLong(OracleCallableStatement stmt, int index, Object values)
  {
    try
    {
      if (values != null) {
        stmt.setLong(index, Long.parseLong((String)values));
      } else {
        stmt.setNull(index, -5);
      }
    }
    catch (Exception ex)
    {
      logger.error(ex);
      try
      {
        stmt.setNull(index, -5);
      }
      catch (Exception exc)
      {
        logger.error(exc);
      }
    }
  }
  
  public java.sql.Date parseDate(String dateVal)
  {
    SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD");
    try
    {
      char[] chArray2 = { '/' };
      CharBuffer cb1 = CharBuffer.wrap(chArray2);
      if (dateVal.contains(cb1)) {
        sdf.applyPattern("dd-MMM-YYYY");
      }
      return new java.sql.Date(sdf.parse(dateVal).getTime());
    }
    catch (Exception ex)
    {
      logger.error(ex);
    }
    return null;
  }
  
  public java.sql.Date parseDate2(String dateVal)
  {
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-YYYY");
    try
    {
      char[] chArray2 = { '/' };
      CharBuffer cb1 = CharBuffer.wrap(chArray2);
      if (dateVal.contains(cb1)) {
        sdf.applyPattern("dd-MMM-YYYY");
      }
      return new java.sql.Date(sdf.parse(dateVal).getTime());
    }
    catch (Exception ex)
    {
      logger.error(ex);
    }
    return null;
  }
}
