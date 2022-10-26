package Application;

import java.math.BigInteger;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Digite o endereço de ip no formato: (255.255.255.255 / 32)");
        Scanner input = new Scanner(System.in);
        String address = input.nextLine();
        System.out.println();

        String[] separatedAddress = address.split("/");

        if (separatedAddress.length != 2 || separatedAddress[0].split("\\.").length != 4)
            throw new Exception("Endereço digitado inválido");

        Integer mask = Integer.parseInt(separatedAddress[1].trim());
        String ip = separatedAddress[0].trim();
        Integer diffInBits = 32 - mask;
        Double hostNumber = Math.pow(2, diffInBits) - 1;

        long ipLong = ipToLong(ip);
        ipLong += Math.pow(2, diffInBits) - 1;

        
        System.out.println("IP Inicial:");
        System.out.println(ip);
        System.out.println();
        System.out.println("Numero de hosts");
        System.out.println(hostNumber.intValue());
        System.out.println();
        System.out.println("IP Final:");
        System.out.print((ipLong & 0xFF000000) >> 24);
        System.out.print(".");
        System.out.print((ipLong & 0xFF0000) >> 16);
        System.out.print(".");
        System.out.print((ipLong & 0xFF00) >> 8);
        System.out.print(".");
        System.out.print(ipLong & 0xFF);
    }

    static long ipToLong(String ipAddress) {
        String[] ipAddressInArray = ipAddress.split("\\.");
    
        long result = 0;
        for (int i = 0; i < ipAddressInArray.length; i++) {
            int power = 3 - i;
            int ip = Integer.parseInt(ipAddressInArray[i]);
            result += ip * Math.pow(256, power);
        }
    
        return result;
    }
}
