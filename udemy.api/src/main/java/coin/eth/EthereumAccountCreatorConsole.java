package coin.eth;

import org.bouncycastle.util.encoders.Hex;
import org.ethereum.crypto.ECKey;

import java.security.SecureRandom;

public class EthereumAccountCreatorConsole {

    public static void main(String[] args) throws Exception {
        EthereumAccountCreatorConsole.createETH();
    }

    public static void createETH() throws Exception {

        ECKey key = new ECKey(SecureRandom.getInstanceStrong());

        byte[] addressKey =  key.getAddress();
        byte[] pubKey = key.getPubKey();
        byte[] privKey = key.getPrivKeyBytes();

        String ETHAddress = Hex.toHexString(addressKey);
        String publicKey = Hex.toHexString(pubKey);
        String privateKey = Hex.toHexString(privKey);

        System.out.println("-------------------------------------------------------------");

        System.out.println("Ethereum Address: 0x" + ETHAddress.toUpperCase());
        System.out.println("Public key: " + publicKey);
        System.out.println("Private key: " + privateKey);

        System.out.println("-------------------------------------------------------------");

        System.out.println("See the address on the Ethereum blockchain: "
                + "https://etherscan.io/address/0x" + ETHAddress.toUpperCase());
    }
}
