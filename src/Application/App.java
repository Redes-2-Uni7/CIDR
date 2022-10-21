package Application;

import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Digite o endereço de ip no formato: (255.255.255.255 / 32)");
        Scanner input = new Scanner(System.in);
        String address = input.nextLine();

        String[] separatedAddress = address.split("/");

        if (separatedAddress.length != 2)
            throw new Exception("Endereço digitado inválido");

        Integer mascara = Integer.parseInt(separatedAddress[1].trim());
        String ip = separatedAddress[0].trim();
        byte[] ipHex = ip.getBytes();
        Long ipLong = ConvertToLong(ipHex); //ver a possibilidade de usar inteiro em vez de long
        Integer diffInBytes = 32 - mascara; //http://mauda.com.br/?p=1271

        System.out.println(ip);
        System.out.println(mascara);
        System.out.println(ipHex);
        System.out.println(ipLong >> mascara);
        System.out.println(diffInBytes);
    }

    static long ConvertToLong(byte[] bytes)
    {
        long value = 0l;
 
        for (byte b : bytes) {
            value = (value << 8) + (b & 255);
        }
 
        return value;
    }
}
