package Application;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {
    private static DecimalFormat decimalFormatter = new DecimalFormat("#");

    public static void main(String[] args) throws Exception {
        decimalFormatter.setRoundingMode(RoundingMode.DOWN);

        System.out.println("Escolha a questão que deseja executar: ");
        System.out.println("1 - Primeiro e ultimo endereço ip");
        System.out.println("2 - Faixas de IP para subredes");
        System.out.println("3 - Pertence à rede");
        System.out.println("4 - Mostrar subrede a qual o endereço IP pertence");
        Scanner sc = new Scanner(System.in);
        Integer x = sc.nextInt();
        switch(x){
            case 1:
                System.out.println();
                firstQuestion();
                break;
            case 2:
                System.out.println();
                secondQuestion();
                break;
            case 3:
                System.out.println();
                thirdQuestion();
                break;
            case 4:
                System.out.println();
                fourthQuestion();
                break;
            default:
            System.out.println("Questão invalida.");
        }
    }


    public static void firstQuestion() throws Exception {
        System.out.println("Digite o endereço de rede no formato: (255.255.255.255 / 32)");
        Scanner input = new Scanner(System.in);
        String address = input.nextLine();
        System.out.println();

        String[] separatedAddress = address.split("/");

        if (separatedAddress.length != 2 || separatedAddress[0].split("\\.").length != 4)
            throw new Exception("Endereço digitado é inválido.");

        Integer mask = Integer.parseInt(separatedAddress[1].trim());
        String ip = separatedAddress[0].trim();
        Integer diffInBits = 32 - mask;
        Double hostNumber = Math.pow(2, diffInBits) - 1;

        long ipLong = Helper.ipToLong(ip);
        ipLong += Math.pow(2, diffInBits) - 1;

        System.out.println("Endereço IP inicial:");
        System.out.println(ip);
        System.out.println();
        System.out.println("Numero de hosts");
        System.out.println(hostNumber.intValue());
        System.out.println();
        System.out.println("Endereço IP final:");
        System.out.print((ipLong & 0xFF000000) >> 24);
        System.out.print(".");
        System.out.print((ipLong & 0xFF0000) >> 16);
        System.out.print(".");
        System.out.print((ipLong & 0xFF00) >> 8);
        System.out.print(".");
        System.out.print(ipLong & 0xFF);
    }


    public static void secondQuestion() throws Exception {
        System.out.println("Digite o endereço de rede no formato: (255.255.255.255 / 32)");
        Scanner input = new Scanner(System.in);
        String address = input.nextLine();
        System.out.println();

        System.out.println("Digite quantas subredes você quer criar com o ip informado:");
        Integer n = input.nextInt();
        System.out.println();

        String[] separatedAddress = address.split("/");

        if (separatedAddress.length != 2 
                || separatedAddress[0].split("\\.").length != 4 
                || Math.pow(2, (32 - Integer.parseInt(separatedAddress[1].trim())) - 1) < n)
            throw new Exception("Endereço digitado é inválido ou impossível dividir a faixa de IPs entre o número de subredes informado.");

        String ip = separatedAddress[0].trim();
        Integer mask = Integer.parseInt(separatedAddress[1].trim());
        Integer diffInBits = 32 - mask;
        Double hostNumber = Math.pow(2, diffInBits);

        List<Integer> listaSubredes = new ArrayList<>();

        Integer resultadoDivisao = Integer.valueOf(decimalFormatter.format(hostNumber / n));
        Integer resto = hostNumber.intValue() % n;

        for(Integer i = 0; i < n; i++) {
            listaSubredes.add(resultadoDivisao + (i < resto ? 1 : 0));
        }

        for(Integer i = 0; i < listaSubredes.size(); i++) {
            long ipLong = Helper.ipToLong(ip);
            ipLong += listaSubredes.get(i) - 1;

            String ipFinal = String.format("%d.%d.%d.%d", ((ipLong & 0xFF000000) >> 24), ((ipLong & 0xFF0000) >> 16), ((ipLong & 0xFF00) >> 8), (ipLong & 0xFF));
            
            System.out.println(String.format("Endereço IP inicial da sub-rede nº%d:", i+1));
            System.out.println(ip);
            System.out.println();
            System.out.println("Numero de hosts");
            System.out.println(listaSubredes.get(i));
            System.out.println();
            System.out.println(String.format("Endereço IP final da sub-rede nº%d:", i+1));
            System.out.println(ipFinal);
            System.out.println();

            ipLong += 1;
            ipFinal = String.format("%d.%d.%d.%d", ((ipLong & 0xFF000000) >> 24), ((ipLong & 0xFF0000) >> 16), ((ipLong & 0xFF00) >> 8), (ipLong & 0xFF));
            ip = ipFinal;
        }
    }


    public static void thirdQuestion() throws Exception {
        System.out.println("Digite o endereço de rede no formato: (255.255.255.255 / 32)");
        Scanner input = new Scanner(System.in);
        String address = input.nextLine();
        System.out.println();

        System.out.println("Digite o endereço IP no formato: (255.255.255.255)");
        String ip = input.nextLine();
        System.out.println();

        String[] separatedAddress = address.split("/");

        if (separatedAddress.length != 2 || separatedAddress[0].split("\\.").length != 4 || ip.split("\\.").length != 4)
            throw new Exception("Endereços digitados são inválidos.");
        
        Integer mask = Integer.parseInt(separatedAddress[1].trim());
        Integer diffInBits = 32 - mask;
        Double hostNumber = Math.pow(2, diffInBits);

        String ipNetwork = separatedAddress[0].trim();

        long ipLongNetwork = Helper.ipToLong(ipNetwork);
        long ipLong = Helper.ipToLong(ip);
        long ipLongNetworkFinal = ipLongNetwork + hostNumber.longValue() - 1;
        
        if (ipLong >= ipLongNetwork && ipLong <= ipLongNetworkFinal)
            System.out.println("O endereço IP informado, pertence à rede.");
        else
            System.out.println("O endereço IP informado, não pertence à rede.");
    }
    
    public static void fourthQuestion() throws Exception {
        System.out.println("A seguir, será pedido para que você digite uma lista de endereços de rede, para parar o input da lista de endereços de rede e passar para o input do endereço de ip, digite \"*\".");
        System.out.println();

        Scanner input = new Scanner(System.in);
        List<NetworkAddress> addresses = new ArrayList<>();

        String aux = "";

        while (!aux.equals("*")) {
            System.out.println("Digite um endereço de rede no formato: (255.255.255.255 / 32). Para parar o input da lista digite \"*\".");

            aux = input.nextLine();

            if (!aux.equals("*")) {
                addresses.add(new NetworkAddress(aux.split("/")));

                if (aux.split("/").length != 2 || aux.split("/")[0].split("\\.").length != 4)
                    throw new Exception("Endereço de rede digitado é inválido.");
            }

            System.out.println();
        }

        System.out.println("Digite o endereço IP no formato: (255.255.255.255)");
        String ip = input.nextLine();
        System.out.println();

        if (ip.split("\\.").length != 4)
            throw new Exception("Endereço de IP digitado é inválido.");

        long ipLong = Helper.ipToLong(ip);
        NetworkAddress result = new NetworkAddress(new String[]{"", "1"});

        for(NetworkAddress address : addresses) {
            Integer diffInBits = 32 - address.mask;
            Double hostNumber = Math.pow(2, diffInBits);
            long ipLongNetwork = Helper.ipToLong(address.ip);
            long ipLongNetworkFinal = ipLongNetwork + hostNumber.longValue() - 1;
            
            if (ipLong >= ipLongNetwork && ipLong <= ipLongNetworkFinal)
                if (address.mask > result.mask)
                    result = address;
        }
        
        System.out.println("Pertence ao endereço de rede:");
        System.out.println(String.format("%s/%d", result.ip, result.mask));
    }
}
