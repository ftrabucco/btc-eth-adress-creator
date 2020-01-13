package console.coin;

import coin.btc.BitcoinAccountCreatorSimple;
import coin.eth.EthereumAccountCreatorConsole;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Console {

    public static void main(String[] args) throws Exception {

        BufferedReader reader =
                new BufferedReader(new InputStreamReader(System.in));

        System.out.print("\nType 1 for BTC (Bitcoin) or 2 for ETH (Ethereum) address: ");

        String userCoinSelection = reader.readLine();

        if(userCoinSelection.equals("") || !(userCoinSelection.equals("1") || userCoinSelection.equals("2"))){
            System.out.println("\nPlease enter 1 for BTC (Bitcoin) or 2 for ETH (Ethereum)!");
            System.exit(-1);
        }

        System.out.println("\nYou have selected " + (userCoinSelection.equals("1") ? "BTC." : "ETH."));

        if(userCoinSelection.equals("1")) { //BTC
             System.out.println("\nPlease wait while your Bitcoin address is being created...\n");
             BitcoinAccountCreatorSimple.createBTC();
             System.out.println("\nBitcoin address has been successfully created.");
        }else { //ETH
             System.out.println("\nPlease wait while your Ethereum address is being created...\n");
             EthereumAccountCreatorConsole.createETH();
             System.out.println("\nEthereum address has been successfully created.");
        }
    }
}
