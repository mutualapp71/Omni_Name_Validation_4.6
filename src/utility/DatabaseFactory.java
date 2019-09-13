package utility;

import java.util.HashMap;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.pool.OracleDataSource;
import org.apache.log4j.Logger;
import org.jasypt.util.TextEncryptor;

public class DatabaseFactory
{
  private static OracleDataSource ods = null;
  private static TextEncryptor te = new TextEncryptor();
  private Logger logger = Logger.getLogger(DatabaseFactory.class);
  private static HashMap<String, OracleConnection> listConn = new HashMap();
  private static DatabaseFactory df;
  
  public static synchronized DatabaseFactory getDBFactory()
  {
    if (df == null)
    {
      df = new DatabaseFactory();
      te.setPassword("fasylqwe123!@#");
    }
    return df;
  }
  
  private OracleConnection getOracleConnection(String uid, String pwd, String url)
  {
    OracleConnection conn = null;
    try
    {
      ods = new OracleDataSource();
      ods.setPassword(te.decrypt(pwd));
      ods.setUser(uid);
      ods.setURL(url);
      
      conn = (OracleConnection)ods.getConnection();
    }
    catch (Exception ex)
    {
      logger.error(ex);
    }
    return conn;
  }
  
  public OracleConnection getDBInstance(String uid, String pwd, String url, String affCode)
  {
    if (listConn.get(affCode) == null) {
      listConn.put(affCode, getOracleConnection(uid, pwd, url));
    } else {
      try
      {
        ods = new OracleDataSource();
        ods.setPassword(te.decrypt(pwd));
        ods.setUser(uid);
        ods.setURL(url);
        
        listConn.put(affCode, (OracleConnection)ods.getConnection());
      }
      catch (Exception ex)
      {
        logger.error(ex);
      }
    }
    return (OracleConnection)listConn.get(affCode);
  }
  
  public OracleConnection getDBInstance(String affCode, HashMap connDetail)
  {
    String uid = (String)connDetail.get("uid");
    String pwd = (String)connDetail.get("password");
    String url = (String)connDetail.get("url");
    if (listConn.get(affCode) == null) {
      listConn.put(affCode, getOracleConnection(uid, pwd, url));
    }
    return (OracleConnection)listConn.get(affCode);
  }
}
