import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class CompressionHandler {
    public static byte[] decompress(byte[] compressedData) throws Exception {
        Inflater inflater = new Inflater();
        inflater.setInput(compressedData);
        byte[] buffer = new byte[compressedData.length * 4];
        int decompressedDataLength = inflater.inflate(buffer);
        inflater.end();
        byte[] decompressedData = new byte[decompressedDataLength];
        System.arraycopy(buffer, 0, decompressedData, 0, decompressedDataLength);
        return decompressedData;
    }

    public static byte[] compress(byte[] data) throws Exception {
        Deflater deflater = new Deflater(Deflater.BEST_COMPRESSION);
        deflater.setInput(data);
        deflater.finish();
        byte[] buffer = new byte[data.length];
        int compressedDataLength = deflater.deflate(buffer);
        deflater.end();
        byte[] compressedData = new byte[compressedDataLength];
        System.arraycopy(buffer, 0, compressedData, 0, compressedDataLength);
        return compressedData;
    }
}