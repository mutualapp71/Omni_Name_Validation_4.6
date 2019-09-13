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
public class Strip {

    public static String getStrip(String val) {

        val = val.substring(1);
        val = val.substring(0, val.length() - 1);

        return val;
    }

}
