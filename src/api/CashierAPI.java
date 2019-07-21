/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package api;

import misc.XMLreader;

/**
 *
 * @author Angelo
 */
public class CashierAPI {

    public String getCashierID() throws Exception {
        XMLreader xr = new XMLreader();
        String CID = xr.getElementValue("C://JTerminals/ginH.xml", "cashier_id");
        return CID;
    }

    public String getCashierName() throws Exception {
        XMLreader xr = new XMLreader();
        String CN = xr.getElementValue("C://JTerminals/ginH.xml", "cashier_name");
        return CN;
    }
}
