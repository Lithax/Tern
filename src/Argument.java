import java.io.File;
import java.io.RandomAccessFile;

public enum Argument { //EVERYTHING IN BRACKETS IS OPTIONAL AS A PARAMETER, <> PARAMETERS ARE NEEDED
    ENCRYPT("--enc", (fileData, key) -> CryptoHandler.encrypt(fileData, CryptoHandler.generateAESKeyFromString(key))),
    DECRYPT("--denc", (fileData, key) -> CryptoHandler.decrypt(fileData, CryptoHandler.generateAESKeyFromString(key))),
    COMPRESS("--com", (fileData, key) -> CompressionHandler.compress(fileData)),
    DECOMPRESS("--dcom", (fileData, key) -> CompressionHandler.decompress(fileData))
    ;

    public final String command;
    private final FileProcessorFunction operation;

    Argument(String command, FileProcessorFunction operation) {
        this.command = command;
        this.operation = operation;
    }

    public String getCommand() {
        return command;
    }

    public static void checkForProperLength(String[] args, int idx) {
        if(args.length-1<idx) {
            System.out.println(args.length + " " +idx);
            System.err.println("Error: Not Enough Arguments were given, could not continue");
            System.exit(0);
        }
    }

    public static void findAndExecuteArgument(String s, String[] args, int idx) {
        for(Argument arg : Argument.values()) {
            if(arg.getCommand().equals(s)) {
                arg.executeFileProcessing(args, idx);
            }
        }
    }

    public void executeFileProcessing(String[] args, int idx) {
        try {
            boolean e = args[idx].equals("--enc") || args[idx].equals("--denc");
            System.out.println(args[idx]);
            System.err.println(idx);
            Argument.checkForProperLength(args, idx+ (e ? 2 : 1));
            String arg1 = args[idx+1];
            System.out.println(e);
            String arg2 = e ? args[idx+2] : null;

            File[] files = Wildcard.getFilesFromIdx(args, arg2 == null ? idx + 1 : idx + 2);
            for(File file : files) {
                System.out.println("Processing file: " + file.getName());
                byte[] fileData = new FileProcessor(file).readb();
            byte[] processedData = operation.apply(fileData, arg1);
            boolean noCopy = false;
            if (args.length-1 >= idx + (e ? 3 : 2) && "-nocopy".equals(args[idx + (e ? 3 : 2)])) {
                noCopy = true;
            }
            long fileSize = file.length();
            if (noCopy) {
                try (RandomAccessFile outputFile = new RandomAccessFile(file, "rw")) {
                    long bytesProcessed = 0;
                    int bufferSize = 1024 * 1024; // 1MB chunks
                    byte[] buffer = new byte[bufferSize];

                    for (int i = 0; i < processedData.length; i += bufferSize) {
                        int chunkSize = Math.min(bufferSize, processedData.length - i);
                        System.arraycopy(processedData, i, buffer, 0, chunkSize);

                        outputFile.write(buffer, 0, chunkSize);
                        bytesProcessed += chunkSize;

                        // Update progress
                        double progress = (double) bytesProcessed / fileSize;
                        printProgressBar(progress);
                    }
                }
            } else {
            String newFileName = Argument.generateNewFileName(file.getName(), this);
            File newFile = new File(newFileName);
            if (newFile.createNewFile()) {
                // Same logic as above for progress during file write
                try (RandomAccessFile outputFile = new RandomAccessFile(newFile, "rw")) {
                    long bytesProcessed = 0;
                    int bufferSize = 1024 * 1024; // 1MB chunks
                    byte[] buffer = new byte[bufferSize];

                    for (int i = 0; i < processedData.length; i += bufferSize) {
                        int chunkSize = Math.min(bufferSize, processedData.length - i);
                        System.arraycopy(processedData, i, buffer, 0, chunkSize);

                        outputFile.write(buffer, 0, chunkSize);
                        bytesProcessed += chunkSize;

                        // Update progress
                        double progress = (double) bytesProcessed / fileSize;
                        printProgressBar(progress);
                    }
                }
            }
            printProgressBar(100.0);
        }
            }
        } catch (Exception e) {
            System.err.println("Error occurred while processing file: " + e.getMessage());
            System.err.println("File might Already be (en/de)-crypted or (de)-compressed");
            e.printStackTrace();
        }
    }

    private static String generateNewFileName(String originalName, Argument arg) {
        String extension = switch (arg) {
            case ENCRYPT -> ".enc";
            case DECRYPT -> ".denc";
            case COMPRESS -> ".com";
            case DECOMPRESS -> ".dcom";
            default -> "";
        };
        try {
            return originalName.substring(0, originalName.lastIndexOf('.')) + extension;
        } catch (Exception e) {
            return originalName + extension;
        }
    }

    @FunctionalInterface
    private interface FileProcessorFunction {
        byte[] apply(byte[] fileData, String key) throws Exception;
    }

    private void printProgressBar(double progress) {
        // Ensure progress is between 0.0 and 1.0
        progress = Math.min(1.0, Math.max(0.0, progress));
    
        int totalBars = 50;  // Total number of characters for the progress bar
        int progressBars = (int) Math.round(progress * totalBars);
    
        StringBuilder bar = new StringBuilder("[");
        for (int i = 0; i < totalBars; i++) {
            if (i < progressBars) {
                bar.append("#");
            } else {
                bar.append(" ");
            }
        }
        bar.append("]");
    
        // Print the progress bar and the percentage
        System.out.print("\r" + bar.toString() + " " + (int) Math.round(progress * 100) + "%");
    }
    
}