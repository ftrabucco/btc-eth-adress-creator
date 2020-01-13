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
 * Output: Sample Bitcoin Address Generation steps starting from 0 to 9 - Step 5 and 6 uses SHA-256 hash function:
 * ----------------------------------------------------------------------------------------------------------------------------
 * 0: Private Key: a4f228d49910e8ecb53ba6f23f33fbfd2bad442e902ea20b8cf89c473237bf9f
 * 0: Private Key Base58: C6t2iJ7AXA2X1KQVnqw3r7NKtKaERdLnaGmbDZNqxXjk
 * 1: public key: 03564213318d739994e4d9785bf40eac4edbfa21f0546040ce7e6859778dfce5d4
 * 2: SHA-256 public key: 482c77b119e47024d00b38a256a3a83cbc716ebb4d684a0d30b8ea1af12d42d9
 * 3: RIPEMD-160 hashing on the result of SHA-256 public key: 0c2c910a661178ef63e276dd0e239883b862f58c
 * 4: RIPEMD-160 hash with version byte: 000c2c910a661178ef63e276dd0e239883b862f58c
 * 5-6: 2 * SHA-256 hash for RIPEMD-160 hash with version byte: c3c0439f33dc4cf4d66d3dd37900fc12597938a64817306b542a75b9223213e0
 * 7: CheckSum: c3c0439f
 * 8: 25 Byte Binary Bitcoin Address: 000c2c910a661178ef63e276dd0e239883b862f58cc3c0439f
 * 9: Bitcoin Address: 127NVqnjf8gB9BFAW2dnQeM6wqmy1gbGtv
 * ----------------------------------------------------------------------------------------------------------------------------
 */
public class BitcoinAccountCreatorConsole {

    public static void main(String[] args) throws Exception{
        BitcoinAccountCreatorConsole.createBTC();
    }

    public static void createBTC() throws Exception {

        Security.addProvider(new BouncyCastleProvider());

        ECKey key = new ECKey(SecureRandom.getInstanceStrong());

        System.out.println("--------------------------------------------------------------------------");

        //0 - Having a private ECDSA Key
        System.out.println("O: Private Key: " + key.getPrivateKeyAsHex());

        //1 - Take the corresponding public key generated with it
        System.out.println("1: Public Key: " + Hex.toHexString(key.getPubKey()));

        //2 - Perform SHA-256 hashing on the public key
        MessageDigest sha256MessageDigest = MessageDigest.getInstance("SHA-256");
        byte[] SHA256PublicKey =  sha256MessageDigest.digest(key.getPubKey());
        System.out.println("2: SHA-256 public key: " + Hex.toHexString(SHA256PublicKey));

        //3 - Perform RIPEMD-160 hashing on the result of SHA-256
        MessageDigest ripeMID160Digest = MessageDigest.getInstance("RipeMD160", "BC");
        byte[] ripemdPublicKeySha256 = ripeMID160Digest.digest(SHA256PublicKey);
        System.out.println("3: RIPEMD-160 hashing on the result of SHA-256 public key: " + Hex.toHexString(ripemdPublicKeySha256));

        //4 - Add version byte in front of RIPEMD-160 hash (0x00 for Main Network)
        byte versionByte[] = new byte[1]; versionByte[0] = 0;
        byte[] ripemd160HashWithVersionByte = Bytes.concat(versionByte, ripemdPublicKeySha256);
        System.out.println("4: RIPEMD-160 hash with version byte: " + Hex.toHexString(ripemd160HashWithVersionByte));

        //5 - Perform SHA-256 hash on the extended RIPEMD-160 result
        //6 - Perform SHA-256 hash on the result of the previous SHA-256 result
        byte[] sha256HashForRipem160WithVersionByte = sha256MessageDigest.digest(
                sha256MessageDigest.digest(ripemd160HashWithVersionByte));
        System.out.println("5-6: SHA-256 hash for RIPEM-160 hash with version byte: " + Hex.toHexString(sha256HashForRipem160WithVersionByte));

        //7 - Take the first 4 bytes of the second SHA-256 hash. This is the address checksum
        byte[] checkSum = Arrays.copyOfRange(sha256HashForRipem160WithVersionByte, 0 ,4);
        System.out.println("7: CheckSum: " + Hex.toHexString(checkSum));

        //8 - Add the 4 byte checksum from stage 7 at the end of extended RIPEMD-160 hash from stage 4
        byte[] binaryBitcoinAddress = Bytes.concat(ripemd160HashWithVersionByte, checkSum);
        System.out.println("8: 25 Byte Binary Bitcoin Address: " + Hex.toHexString(binaryBitcoinAddress));

        //9 - Convert the result from a byte string into a base58 string using Base58cHECK ENCODING.
        //This is the most commonly used Bitcoin Address format
        System.out.println("9: Bitcoin Address: " + Base58.encode(binaryBitcoinAddress));

        System.out.println("See the address on the Bitcoin blockchain: " +
                "https://blockchain.com/btc/address/" + Base58.encode(binaryBitcoinAddress));
        System.out.println("-------------------------------------------------------------------------------");

    }

}
