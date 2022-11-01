package Application;

import java.util.ArrayList;
import java.util.List;

public class NetworkList<T> {
    public String initialIP;
    public String finalIP;
    
    public List<T> list = new ArrayList<T>();

    public long getLongInitialIP() {
        return Helper.ipToLong(initialIP);
    }
    
    public long getLongFinalIP() {
        return Helper.ipToLong(finalIP);
    }
}