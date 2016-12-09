import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.io.*;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Arrays;
import java.util.*;
/**
 * Created by danielbraithwt on 10/4/16.
 */
public class GenerateClass {

    public static void main(String[] args) {
        int numBytes = 4;
        BigInteger n = new BigInteger(args[0]);

        byte[] key = Blowfish.asByteArray(n, numBytes);
        Blowfish.setKey(key);

        String plaintextStr = "May good flourish; Kia hua ko te pai";
        byte[] plaintext = plaintextStr.getBytes();
        byte[] cyphertext = Blowfish.encrypt(plaintext);
    }
}
