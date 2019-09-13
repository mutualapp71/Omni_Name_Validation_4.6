package requesthandlers;

import java.math.BigDecimal;
import java.nio.CharBuffer;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import oracle.jdbc.OracleCallableStatement;
import org.apache.log4j.Logger;

public class DBUtility
{
  static Logger logger = Logger.getLogger(DBUtility.class);
  private static DBUtility dbUtil;
  
  private DBUtility()
  {
    if (dbUtil == null) {
      dbUtil = new DBUtility();
    }
  }
  
  public void setStmtString(OracleCallableStatement stmt, HashMap values, String key, int index)
  {
    try
    {
      if (values.get(key) != null) {
        stmt.setString(index, (String)values.get(key));
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
