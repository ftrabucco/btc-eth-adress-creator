package coin.btc;

import com.google.common.primitives.Bytes;
import org.bitcoinj.core.Base58;
import org.bitcoinj.core.ECKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Arrays;

/**
 * Created by tuna tore on 13/01/2020.
 * see https://mycrypto.tools/blog_how_to_generate_bitcoin_addresses.html
 * -------------------------------------------------------------------------------
 * Private Key: e5895bcc787b526116498a352c3bd508c50caf46a2bfc7ec2b5f0f17f6bbf7e3
 * Public key: 021974b88265aa6a1b4217c2ce9f252e82d90dc673ebd7391e07db14182d19fb54
 * Bitcoin Address: 16xGiz8tVLmxEePYjksgNkewassTrrGbvj
 * See the address on the Bitcoin blockchain: https://blockchain.com/btc/address/16xGiz8tVLmxEePYjksgNkewassTrrGbvj
 * -------------------------------------------------------------------------------
 */
public class BitcoinAccountCreatorSimple {

    public static void main(String[] args) throws Exception {
        BitcoinAccountCreatorSimple.createBTC();
    }

    public static void createBTC() throws Exception {

        Security.addProvider(new BouncyCastleProvider());

        ECKey key = new ECKey(SecureRandom.getInstanceStrong());

        System.out.println("-------------------------------------------------------------------------------");

        System.out.println("Private Key: " + key.getPrivateKeyAsHex());
        //System.out.println("Private Key Base58: " + Base58.encode(key.getPrivKeyBytes()));

        //1 - Take the corresponding public key generated with it (33 bytes, 1 byte 0x02 (y-coord is even),
        // and 32 bytes corresponding to X coordinate)
        String publicKeyHex = key.getPublicKeyAsHex();
        System.out.println("Public key: " + Hex.toHexString(key.getPubKey()));

        //2 - Perform SHA-256 hashing on the public key
        MessageDigest sha256MessageDigest = MessageDigest.getInstance("SHA-256");
        byte[] SHA256PublicKey = sha256MessageDigest.digest(Hex.decode(publicKeyHex));

        //3 - Perform RIPEMD-160 hashing on the result of SHA-256
        MessageDigest ripeMD160Digest = MessageDigest.getInstance("RipeMD160", "BC");
        byte[] ripemdPublicKeySha256 = ripeMD160Digest.digest(SHA256PublicKey);

        //4 - Add version byte in front of RIPEMD-160 hash (0x00 for Main Network)
        byte versionByte[] = new byte[1]; versionByte[0] = 0;
        byte[] ripemd160HashWithVersionByte = Bytes.concat(versionByte, ripemdPublicKeySha256);

        //5 - Perform SHA-256 hash on the extended RIPEMD-160 result
        //6 - Perform SHA-256 hash on the result of the previous SHA-256 hash
        byte[] sha256HashForRipem160WithVersionByte =
                sha256MessageDigest.digest(
                sha256MessageDigest.digest(ripemd160HashWithVersionByte));

        //7 - Take the first 4 bytes of the second SHA-256 hash. This is the address checksum
        byte[] checkSum = Arrays.copyOfRange(sha256HashForRipem160WithVersionByte, 0, 4);

        //8 - Add the 4 checksum bytes from stage 7 at the end of extended RIPEMD-160 hash from stage 4.
        // This is the 25-byte binary Bitcoin Address.
        byte[] binaryBitcoinAddress = Bytes.concat(ripemd160HashWithVersionByte, checkSum);

        //9 - Convert the result from a byte string into a base58 string using Base58Check encoding.
        // This is the most commonly used Bitcoin Address format
        System.out.println("Bitcoin Address: " + Base58.encode(binaryBitcoinAddress));

        System.out.println("See the address on the Bitcoin blockchain: " + "https://blockchain.com/btc/address/" + Base58.encode(binaryBitcoinAddress));
        System.out.println("-------------------------------------------------------------------------------");

    }
}
