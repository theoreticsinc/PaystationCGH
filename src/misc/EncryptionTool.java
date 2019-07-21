package misc;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import org.apache.log4j.LogManager;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class EncryptionTool {
    
    static Logger log = LogManager.getLogger(EncryptionTool.class.getName());

    byte[] i_v = {(byte) 0xcb, (byte) 0x53, (byte) 0x03, (byte) 0x0f, (byte) 0xe0,
        (byte) 0x79, (byte) 0x9d, (byte) 0xdc, (byte) 0x80, (byte) 0xa9, (byte) 0x83,
        (byte) 0xf1, (byte) 0x03, (byte) 0xb6, (byte) 0x59, (byte) 0x83};

    byte[] key_Bytes = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};

    public String encrypt(String text, String key) {
        try {
            StringBuffer pKey = new StringBuffer(text);
            return (new String(encrypt_act(text.getBytes(), pKey)));
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return (null);
    }

    ;

    public String decrypt(String text, String key) {
        try {
            StringBuffer pKey = new StringBuffer(text);
            return (new String(decrypt_act(text, pKey)));
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return (null);
    }

    ;

    private String encrypt_act(byte[] plainText, StringBuffer private_key) {
        try {
            IvParameterSpec ivSpec = new IvParameterSpec(i_v);
            SecretKey key = new SecretKeySpec(key_Bytes, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
            byte[] cipherText = cipher.doFinal(plainText);

            BASE64Encoder base64encoder = new BASE64Encoder();

            return base64encoder.encode(cipherText);
        } catch (Exception ex) {
            //  log.error(ex.getMessage());
        }
        return null;
    }

    ;

    private String decrypt_act(String encryptedStr, StringBuffer private_key) {
        try {

            if (encryptedStr == null || encryptedStr.trim().length() <= 0) {
                throw new IllegalArgumentException("encrypted string was null or empty");
            }

            BASE64Decoder base64decoder = new BASE64Decoder();
            byte[] cipherText = base64decoder.decodeBuffer(encryptedStr);

            IvParameterSpec ivSpec = new IvParameterSpec(i_v);
            SecretKey key = new SecretKeySpec(key_Bytes, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
            byte[] plainText = cipher.doFinal(cipherText);

            return bytes2String(plainText);
        } catch (Exception ex) {
            //  log.error(ex.getMessage());
        }
        return null;
    }

    ;


   private static String bytes2String(byte[] bytes) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            stringBuffer.append((char) bytes[i]);
        }
        return stringBuffer.toString();
    }

    public boolean process(String[] args, boolean toFile) {
        try {
            if (args.length == 2) {
                if (args[0].equals("-e")) {
                    //int size = args[1].length()/9;
                    //	int excess = args[1].length() % 9;
                    //int factor = 0;
                    //String testVal = "";
                    java.util.Calendar cal = java.util.Calendar.getInstance();
                    String ext = String.valueOf(cal.getTimeInMillis());
                    java.io.Writer writer = new java.io.FileWriter(new java.io.File("encrypted_" + ext + ".egt"));
                    writer.write(encrypt(args[1], null));
                    writer.flush();
                    writer.close();
                    log.info("Encrypted password is being saved in encrypted_" + ext + ".egt");
                    return (true);

                } else if (args[0].equals("-d")) {
                    java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(new java.io.File(args[1])));
                    int c = -1;
                    String buff = "";
                    while ((c = reader.read()) != -1) {
                        buff += (char) c;
                    }

                    reader.close();
                    log.info("Your decrypted password: " + decrypt(buff, null));
                    return (true);
                }
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }

        return (false);
    }

    ;

    /** how to use this module **/

    @SuppressWarnings("empty-statement")
    public static void main(String[] args) {
        try {
            EncryptionTool instance = new EncryptionTool();
            String temp = instance.encrypt("angelo dizonangelo dizonangelo dizonangelo dizonangelo dizonangelo dizonangelo dizonangelo dizonangelo dizonangelo dizonangelo dizonangelo dizon", "theoretics");
            log.info(temp.length() + "   " + temp.toString());
            log.info(instance.decrypt(temp, "theoretics"));
            //instance.process(args, true);
        } catch (Exception ex) {
        };
    }
;
};
