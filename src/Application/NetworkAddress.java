package Application;

public class NetworkAddress {
    public String ip;
    public Integer mask;

    public NetworkAddress(String[] address) {
        this.ip = address[0].trim();
        this.mask = Integer.parseInt(address[1].trim());
    }
}