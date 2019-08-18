/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import misc.DataBaseHandler;
import misc.RawFileHandler;
import org.apache.log4j.LogManager;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author Administrator
 */
public class CRDPLTcheckDupAPI {
    
    static Logger log = LogManager.getLogger(CRDPLTcheckDupAPI.class.getName());

    Statement stmt = null;
    Connection conn = null;

    public boolean isCRDDuplicate(String card2check) throws IOException {
        try {
            DataBaseHandler DB = new DataBaseHandler();
            conn = DB.getConnection(true);
            stmt = conn.createStatement();
            //RawFileHandler rfh = new RawFileHandler();
            //boolean found = rfh.FindFileFolder("/SYSTEMS/", card2check + ".crd");
            //String SQL = "SELECT * FROM crdplt.main WHERE cardNumber = '" +card2check + "'";
            String SQL = "SELECT * FROM unidb.timeindb WHERE CardCode = '" +card2check + "'";
            ResultSet found = stmt.executeQuery(SQL);
                if (found.next()) {
                    //Cardno = found.getString(2);
                    return true;
                }
            return false;
        } catch (SQLException ex) {
            log.error(ex.getMessage());
        }
        return false;
    }

    public boolean isCRDDuplicate2(String card2check) {
        String Cardno = "";
        try {
            this.openDB(false);
            try {
                String SQL = "select * from crdplt.main where CardNo = '" + card2check + "'";
                ResultSet found = stmt.executeQuery(SQL);
                if (found.next()) {
                    Cardno = found.getString(2);
                    return true;
                }
                if (Cardno.compareTo("") == 0) {
                    this.close();
                    this.openDB(true);
                    try {
                        SQL = "select * from crdplt.main where CardNo = '" + card2check + "'";
                        found = stmt.executeQuery(SQL);
                        if (found.next()) {
                            Cardno = found.getString(2);
                            return true;
                        }

                    } catch (Exception ex) {
                        log.error(ex.getMessage());
                    }
                    return false;
                }
            } catch (SQLException ex) {
                log.error(ex.getMessage());
            }
            this.close();
        } catch (Exception ex) {
            this.openDB(true);
            try {
                String SQL = "select * from crdplt.main where CardNo = '" + card2check + "'";
                ResultSet found = stmt.executeQuery(SQL);
                while (found.next()) {
                    Cardno = found.getString(2);
                }
                if (Cardno.compareTo("") == 0) {
                    this.close();
                    this.openDB(false);
                    try {
                        SQL = "select * from crdplt.main where CardNo = '" + card2check + "'";
                        found = stmt.executeQuery(SQL);
                        while (found.next()) {
                            Cardno = found.getString(2);
                        }
                    } catch (Exception ex2) {
                        log.error(ex2.getMessage());
                    }
                    return false;
                }
            } catch (SQLException e) {
                log.error(ex.getMessage());
            }
            this.close();
        }
        return true;
    }

    public boolean isPLTDuplicate(String plate2check) throws IOException {
        RawFileHandler rfh = new RawFileHandler();
        boolean found = rfh.FindFileFolder("/SYSTEMS/", plate2check + ".plt");
        return found;
    }

    public boolean isPLTDuplicate2(String plate2check) {
        String Cardno = "";
        try {
            this.openDB(false);
            try {
                String SQL = "select * from crdplt.main where PlateNo = '" + plate2check + "'";
                ResultSet found = stmt.executeQuery(SQL);
                while (found.next()) {
                    Cardno = found.getString(2);
                }
                if (Cardno.compareTo("") == 0) {
                    this.close();
                    this.openDB(true);
                    try {
                        SQL = "select * from crdplt.main where PlateNo = '" + plate2check + "'";
                        found = stmt.executeQuery(SQL);
                        while (found.next()) {
                            Cardno = found.getString(2);
                        }
                    } catch (Exception ex) {
                        log.error(ex.getMessage());
                    }
                    return false;
                }
            } catch (SQLException ex) {
                log.error(ex.getMessage());
            }
            this.close();
        } catch (Exception ex) {
            log.error(ex.getMessage());
            this.openDB(true);
            try {
                String SQL = "select * from crdplt.main where PlateNo = '" + plate2check + "'";
                ResultSet found = stmt.executeQuery(SQL);
                while (found.next()) {
                    Cardno = found.getString(2);
                }
                if (Cardno.compareTo("") == 0) {
                    this.close();
                    this.openDB(true);
                    try {
                        SQL = "select * from crdplt.main where PlateNo = '" + plate2check + "'";
                        found = stmt.executeQuery(SQL);
                        while (found.next()) {
                            Cardno = found.getString(2);
                        }
                    } catch (Exception ex2) {
                        log.error(ex2.getMessage());
                    }
                    return false;
                }
            } catch (SQLException e) {
                log.error(ex.getMessage());
            }
            this.close();
        }
        return true;
    }

    public void openDB(boolean mainDB) {
        try {
            DataBaseHandler DBH = new DataBaseHandler();
            conn = DBH.getConnection(mainDB);
            stmt = conn.createStatement();
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    public void close() {
        try {
            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            log.error(ex.getMessage());
        }
    }
}
