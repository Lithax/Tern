import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MonoHandler {
    public static final byte[] HEADER_START = {33, 33, 33, 33, 37, 37, 37, 37, 37, 37, 37, 37, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 36, 38, 38, 38, 38, 38, 38, 38, 38, 38, 38, 38, 38, 38, 38, 38};
    public static final byte[] HEADER_END = {38, 38, 38, 38, 37, 37, 37, 37, 37, 37, 37, 37, 36, 36, 36, 36, 36, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40,37, 37, 37, 37, 37, 37, 37, 37, 36, 36, 36, 36, 36, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40};
    public static final byte[] HASHSTART = {48, 48, 48, 47, 47, 47, 46, 46, 46, 46, 46, 34, 34, 35, 34, 34, 46, 47, 48, 43, 43, 43, 43};
    public static final byte[] HASHEND = {47, 47, 47, 46, 46, 46, 46, 46, 34, 34, 35, 34, 34, 46, 47, 47, 48, 42, 48, 46, 46, 46, 46, 43};
    public static final int BUFFER_SIZE = 4096;

    public static void processMonoInward(File[] files, String key) {
        System.out.println("Compressing Files to Mono");
        try (ByteArrayOutputStream mono = new ByteArrayOutputStream()) {
            if (files.length == 0) {
                System.err.println("Error: No files were found");
                return;
            }
            for (File file : files) {
                System.out.println(file.getName());
                try (BufferedInputStream fileInputStream = new BufferedInputStream(new FileInputStream(file))) {
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    byte[] buffer = new byte[BUFFER_SIZE];
                    int bytesRead;
                    while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                        byte[] data = Arrays.copyOf(buffer, bytesRead);
                        outputStream.write(data); // Append data to the output stream
                    }
                    byte[] hash = HashingHandler.hash(outputStream.toByteArray());
                    byte[] filename = (file.getName()).getBytes(Charset.forName("ASCII"));
                    mono.write(HEADER_START);
                    mono.write(filename);
                    mono.write(HASHSTART);
                    mono.write(hash);
                    mono.write(HASHEND);
                    mono.write(outputStream.toByteArray());
                    mono.write(HEADER_END);
                }
            }

            File f = new File("mono_" + new Random().nextInt(10000000));
            System.out.println("All Data was written to: " + f.getName());
            f.createNewFile();
            byte[] b = mono.toByteArray();
            b = CompressionHandler.compress(b);
            b = CryptoHandler.encrypt(b, CryptoHandler.generateAESKeyFromString(key));
            new FileProcessor(f).writeb(b);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void processMonoOutward(File[] files, String key, boolean verify) {
        System.out.println("Unwrapping Files from Mono");
        for (File file : files) {
            try {
                FileProcessor fileProcessor = new FileProcessor(file);
                byte[] b = fileProcessor.readb();
                b = CryptoHandler.decrypt(b, CryptoHandler.generateAESKeyFromString(key));
                b = CompressionHandler.decompress(b);
                for (byte[] f : splitMono(b)) {
                    int idxHASHSTART = -1;
                    int idxHASHEND = -1;
                    for(int i = 0; i < f.length; i++) {
                        if(f[i] == 48 && f.length > i + 2 && f[i+1] == 48 && f[i+2] == 48 && checkIfHashStart(f, i)) {
                            idxHASHSTART=i;
                        }
                        if(f[i] == 47 && f.length > i + 2 && f[i+1] == 47 && f[i+2] == 47 && checkIfHashEnd(f, i)) {
                            idxHASHEND = i;
                        }
                    }
                    byte[] filename = byteSubstring(f, HEADER_START.length, idxHASHSTART);
                    if(!verify) System.out.print("\u001B[31mUnwrapping "+new String(filename, "ASCII")+"..."+ "\u001B[0m");
                    print("FILENAME: " + new String(filename, "ASCII"), verify);
                    byte[] hash = byteSubstring(f, idxHASHSTART+HASHSTART.length, idxHASHEND);
                    byte[] data = byteSubstring(f, idxHASHEND+HASHEND.length, f.length);
                    print("\u001B[34mREADHASH: " + Arrays.toString(hash) + "\u001B[0m", verify);
                    print("\u001B[34mNEWHASH: "+Arrays.toString(HashingHandler.hash(data))+ "\u001B[0m", verify);
                    if (Arrays.equals(hash, HashingHandler.hash(data))) {
                        if(!verify) {
                            System.out.println("\r\u001B[32mUnwrapping "+new String(filename, "ASCII")+"..."+ "\u001B[0m");
                            File l = new File(new String(filename, "ASCII"));
                            l.createNewFile();
                            FileProcessor s = new FileProcessor(l);
                            s.writeb(data);
                        }
                        System.out.println("\u001B[32mHash matches\u001B[0m");
                    } else {
                        System.err.println("\u001B[31mError: Hashes do not match at file: '" + new String(filename) + "'\u001B[0m");
                    }
                    System.out.println("--------------------");
                }
            } catch (Exception e) {
                System.out.println("Unexpected Error: " + e.getMessage()+ "\nMono File may be corrupted or has been tampared with");
            }
        }
    }

    public static byte[] byteSubstring(byte[] data, int start, int end) {
        if (start < 0 || end > data.length || start > end) {
            throw new IllegalArgumentException("Invalid start or end indices: start=" + start + ", end=" + end + ", data.length=" + data.length);
        }
        byte[] result = new byte[end - start];
        System.arraycopy(data, start, result, 0, end - start);
        return result;
    }

    public static int lastIndexOf(byte[] data, int ch) {
        for (int i = data.length - 1; i >= 0; i--) {
            if (data[i] == ch) {
                return i;
            }
        }
        return -1;
    }

    public static boolean checkIfHeaderStart(byte[] data, int ch) {
        if(data.length < HEADER_START.length) {
            return false;
        }
        for (int i = ch; i < HEADER_START.length; i++) {
            if(data[i] != HEADER_START[i]) {
                return false;
            }
        }
        return true;
    }

    public static boolean checkIfHeaderEnd(byte[] data, int ch) {
        if (data.length < HEADER_END.length) {
            return false;
        }
        for (int i = ch; i < HEADER_END.length; i++) {
            if (data[i] != HEADER_END[i]) {
                return false;
            }
        }
        return true;
    }

    public static boolean checkIfHashStart(byte[] data, int ch) {
        if (data.length - ch < HASHSTART.length) {
            return false; // Ensure enough bytes remain
        }
        for (int i = 0; i < HASHSTART.length; i++) {
            if (data[ch + i] != HASHSTART[i]) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean checkIfHashEnd(byte[] data, int ch) {
        if (data.length - ch < HASHEND.length) {
            return false; // Ensure enough bytes remain
        }
        for (int i = 0; i < HASHEND.length; i++) {
            if (data[ch + i] != HASHEND[i]) {
                return false;
            }
        }
        return true;
    }

    public static List<byte[]> splitMono(byte[] data) {
        List<byte[]> result = new ArrayList<>();
        int start = 0;
        while (start < data.length) {
            int headerStartIdx = indexOf(data, HEADER_START, start);
            if (headerStartIdx == -1) break; // No more headers
            int headerEndIdx = indexOf(data, HEADER_END, headerStartIdx + HEADER_START.length);
            if (headerEndIdx == -1) break; // No closing HEADER_END
    
            // Extract the chunk excluding HEADER_END
            result.add(byteSubstring(data, headerStartIdx, headerEndIdx));
            start = headerEndIdx + HEADER_END.length; // Move past this chunk
        }
        return result;
    }    
    
    public static int indexOf(byte[] data, byte[] pattern, int start) {
        outer:
        for (int i = start; i <= data.length - pattern.length; i++) {
            for (int j = 0; j < pattern.length; j++) {
                if (data[i + j] != pattern[j]) continue outer;
            }
            return i;
        }
        return -1;
    }
    
    public static void print(String e, boolean f) {
        if(f) {
            System.out.println(e);
        }
    }
}