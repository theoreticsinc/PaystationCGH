/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api;

import misc.DataBaseHandler;
import misc.XMLreader;

/**
 *
 * @author Angelo
 */
public class CashierAPI {

    public String getCashierID() throws Exception {
        DataBaseHandler dbh = new DataBaseHandler();
//        XMLreader xr = new XMLreader();
//        String CID = xr.getElementValue("C://JTerminals/ginH.xml", "cashier_id");
        String CID = dbh.getCashierID();
        return CID;
    }

    public String getCashierName() throws Exception {
        DataBaseHandler dbh = new DataBaseHandler();
//        XMLreader xr = new XMLreader();
//        String CN = xr.getElementValue("C://JTerminals/ginH.xml", "cashier_name");
        String CN = dbh.getCashierName();
        return CN;
    }
}
