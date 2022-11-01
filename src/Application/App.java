package Application;

import java.lang.reflect.ParameterizedType;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.google.gson.Gson;

public class App {
    private static DecimalFormat decimalFormatter = new DecimalFormat("#");
    private static Gson gson = new Gson();
    
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
                firstQuestion();
                break;
            case 2:
                secondQuestion();
                break;
            case 3:
                thirdQuestion();
                break;
            case 4:
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
        // NetworkList<NetworkList<NetworkList<String>>> networkList = gson.fromJson("{\"initialIP\":\"10.10.10.0\",\"finalIP\":\"10.10.10.255\",\"list\":[{\"initialIP\":\"10.10.10.0\",\"finalIP\":\"10.10.10.100\",\"list\":[{\"initialIP\":\"10.10.10.0\",\"finalIP\":\"10.10.10.50\",\"list\":[\"\"]},{\"initialIP\":\"10.10.10.51\",\"finalIP\":\"10.10.10.100\",\"list\":[\"\"]}]},{\"initialIP\":\"10.10.10.101\",\"finalIP\":\"10.10.10.200\",\"list\":[{\"initialIP\":\"10.10.10.101\",\"finalIP\":\"10.10.10.150\",\"list\":[\"\"]},{\"initialIP\":\"10.10.10.151\",\"finalIP\":\"10.10.10.200\",\"list\":[\"\"]}]},{\"initialIP\":\"10.10.10.201\",\"finalIP\":\"10.10.10.255\",\"list\":[{\"initialIP\":\"10.10.10.201\",\"finalIP\":\"10.10.10.210\",\"list\":[\"\"]},{\"initialIP\":\"10.10.10.211\",\"finalIP\":\"10.10.10.230\",\"list\":[\"\"]},{\"initialIP\":\"10.10.10.231\",\"finalIP\":\"10.10.10.255\",\"list\":[\"\"]}]}]}"
        // , NetworkList.class);

        NetworkList<NetworkList<NetworkList>> networkList = new NetworkList<NetworkList<NetworkList>>();
        networkList.initialIP = "10.10.10.0";
        networkList.finalIP = "10.10.10.255";
        networkList.list.add(new NetworkList<NetworkList>());
        networkList.list.add(new NetworkList<NetworkList>());
        networkList.list.add(new NetworkList<NetworkList>());

        networkList.list.get(0).initialIP = "10.10.10.0";
        networkList.list.get(0).finalIP = "10.10.10.100";
        networkList.list.get(0).list.add(new NetworkList());
        networkList.list.get(0).list.add(new NetworkList());
        networkList.list.get(0).list.get(0).initialIP = "10.10.10.0";
        networkList.list.get(0).list.get(0).finalIP = "10.10.10.50";
        networkList.list.get(0).list.get(1).initialIP = "10.10.10.51";
        networkList.list.get(0).list.get(1).finalIP = "10.10.10.100";

        networkList.list.get(1).initialIP = "10.10.10.101";
        networkList.list.get(1).finalIP = "10.10.10.200";
        networkList.list.get(1).list.add(new NetworkList());
        networkList.list.get(1).list.add(new NetworkList());
        networkList.list.get(1).list.get(0).initialIP = "10.10.10.101";
        networkList.list.get(1).list.get(0).finalIP = "10.10.10.150";
        networkList.list.get(1).list.get(1).initialIP = "10.10.10.151";
        networkList.list.get(1).list.get(1).finalIP = "10.10.10.200";

        networkList.list.get(2).initialIP = "10.10.10.201";
        networkList.list.get(2).finalIP = "10.10.10.255";
        networkList.list.get(2).list.add(new NetworkList());
        networkList.list.get(2).list.add(new NetworkList());
        networkList.list.get(2).list.add(new NetworkList());
        networkList.list.get(2).list.get(0).initialIP = "10.10.10.201";
        networkList.list.get(2).list.get(0).finalIP = "10.10.10.210";
        networkList.list.get(2).list.get(1).initialIP = "10.10.10.211";
        networkList.list.get(2).list.get(1).finalIP = "10.10.10.230";
        networkList.list.get(2).list.get(2).initialIP = "10.10.10.231";
        networkList.list.get(2).list.get(2).finalIP = "10.10.10.255";

        System.out.println("O JSON a seguir representa nossa lista de endereços de rede:");
        System.out.println();
        System.out.println(gson.toJson(networkList));

        System.out.println("Digite o endereço IP entre o range 10.10.10.0 e 10.10.10.255:");
        Scanner input = new Scanner(System.in);
        String ip = input.nextLine();
        System.out.println();

        long ipLong = Helper.ipToLong(ip);

        if (ip.split("\\.").length != 4 || ipLong < networkList.getLongInitialIP() || ipLong > networkList.getLongFinalIP())
            throw new Exception("Endereço digitado é inválido ou fora do range solicitado.");
        
        networkList.list.forEach(x1 -> {
            if (x1.list != null) {
                x1.list.forEach(x2 -> {
                    if (ipLong >= x2.getLongInitialIP() && ipLong <= x2.getLongFinalIP())
                    {
                        System.out.println("Pertence ao:");
                        System.out.println(gson.toJson(x2));
                        return;
                    }
                });
            }
        });
    }
}
