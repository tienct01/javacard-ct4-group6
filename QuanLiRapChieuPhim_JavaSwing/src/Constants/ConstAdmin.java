/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Constants;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Admin
 */
public class  ConstAdmin {
    public static final HashMap<String, Integer> role = new HashMap<>();

    static {
        role.put("giamDoc", 0);
        role.put("banHang", 1);
        role.put("nhanSu", 2);
        role.put("quanKho", 3);
    }
}
