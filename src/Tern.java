import java.io.File;
import java.util.Random;

public class Tern {
    public static final String TERN = "████████╗███████╗██████╗ ███╗   ██╗\n" + //
                                      "╚══██╔══╝██╔════╝██╔══██╗████╗  ██║\n" + //
                                      "   ██║   █████╗  ██████╔╝██╔██╗ ██║\n" + //
                                      "   ██║   ██╔══╝  ██╔══██╗██║╚██╗██║\n" + //
                                      "   ██║   ███████╗██║  ██║██║ ╚████║\n" + //
                                      "   ╚═╝   ╚══════╝╚═╝  ╚═╝╚═╝  ╚═══╝";
    public static void main(String[] args) {
        if(args == null || args.length < 1) {
            System.err.println("Error: Not enough Arguments, use --help for help");
            System.exit(0);
        }
        int idx = 0;
        for(String arg : args) {
            checkStatic(arg);
            checkMono(args, arg, idx);
            Argument.findAndExecuteArgument(arg, args, idx);
            checkArgsValidity(args);
            idx++;
        }
    }

    public static void checkMono(String[] args, String arg, int idx) { //tern --mono -in/out key files
        try {
            if(arg.equals("--mono")) {
                if(args.length > idx+3) {
                    File[] files = Wildcard.getFilesFromIdx(args, idx+3);
                    if(args[idx+1].equals("-in")) {
                        MonoHandler.processMonoInward(files, args[idx+2]);
                    } else if(args[idx+1].equals("-out")) {
                        MonoHandler.processMonoOutward(files, args[idx+2], false);
                    } else if(args[idx+1].equals("-verify")) {
                        MonoHandler.processMonoOutward(files, args[idx+2], true);
                    } else {
                        System.err.println("Error: Invalid Argument");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void checkStatic(String arg) {
        switch (arg) {
            case "--help":
            System.out.println("\u001B[1mUsage:\u001B[0m tern [options] [arguments]\n");
            System.out.println("\u001B[1mOptions:\u001B[0m");
            System.out.println("\u001B[32m--help\u001B[0m\t\t\t\tDisplays this help message");
            System.out.println("\u001B[32m--version\u001B[0m\t\t\tDisplays the Current Version and Author");
            System.out.println("\u001B[32m--enc\u001B[0m\t[key]\t[files]\t-nocopy\tEncrypts files");
            System.out.println("\u001B[32m--denc\u001B[0m\t[key]\t[files]\t-nocopy\tDecrypts files");
            System.out.println("\u001B[32m--com\u001B[0m\t\t[files]\t-nocopy\tCompresses files");
            System.out.println("\u001B[32m--dcom\u001B[0m\t\t[files]\t-nocopy\tDecompresses files");
            System.out.println("\u001B[32m--mono\u001B[0m\t[arg]\t[key]\t[files]\tSpecial Monofile Encryption, compresses and encrypts files to a mono File,\n\t\t\t\tstores Hash and filenames, when unwrapping from mono, files will return like they were before\n");
            System.out.println("\u001B[1mArguments:\u001B[0m");
            System.out.println("\u001B[32mkey\u001B[0m\t\t\t\tThe Key to use for Encryption/Decryption");
            System.out.println("\u001B[32mfiles\u001B[0m\t\t\t\tThe Files to process");
            System.out.println("\u001B[32mnocopy\u001B[0m\t\t\t\t\u001B[31m\u001B[1mOPTIONAL\u001B[0m\u001B[0m: Use if file should be processed directly (no copying)\n");
            System.out.println("\u001B[1mMono-Arguments:\u001B[0m");
            System.out.println("\u001B[32m-in\u001B[0m\t\t\t\tEncrypts Files to Mono");
            System.out.println("\u001B[32m-out\u001B[0m\t\t\t\tDecrypts Files from Mono");
            System.out.println("\u001B[32m-verify\u001B[0m\t\t\t\tVerifies Hashes of Files in Mono\n");
            System.out.println("\u001B[1mExamples:\u001B[0m");
            System.out.println("tern \u001B[32m--enc\u001B[0m mykey file1.txt");
            System.out.println("tern \u001B[32m--dcom\u001B[0m file1.denc");
            System.out.println("tern \u001B[32m--mono\u001B[0m -in mykey hello*.txt");
            System.out.println("tern \u001B[32m--mono\u001B[0m -out mykey mono*");
                break;
            case "--version":
            System.out.println(PrintMethods.printRandom(TERN));
            System.out.println("\u001B[1mTern - Lightweight CLI Encryption Tool\u001B[0m");
            System.out.println("by "+PrintMethods.toRainbow("@Lithax")+" -> (https://www.github.com/Lithax)");
            System.out.println("\u001B[1mv1.02\u001B[0m");
            System.out.println("Press Ctrl + C to Exit...");
            String p = "\n"+"\u001B[1mTern - Lightweight CLI Encryption Tool\u001B[0m"+PrintMethods.toRainbow("@Lithax")+" -> (https://www.github.com/Lithax)"+"\n\u001B[1mv1.02\u001B[0m"+"\nPress Ctrl + C to Exit...";	
            switch (new Random().nextInt(5)) {
                case 0:
                    PrintMethods.flashTERN(TERN);
                    break;
                case 1:
                    PrintMethods.updateTERN(TERN);
                    break;
                case 2:
                    PrintMethods.transTERN(TERN);
                    break;
                case 3:
                    PrintMethods.waveTERN(TERN);
                    break;
                case 4:
                    PrintMethods.flickerTERN(TERN);
                    break;
            } 
                break;
        }
    }

    public static boolean checkArgsValidity(String[] args) {
        int idx = 0;
        while (idx < args.length) {
            String arg = args[idx];
    
            switch (arg) {
                case "--help":
                case "--version":
                    // These require no additional arguments
                    idx++;
                    break;
    
                case "--enc":
                case "--denc":
                    // Validate --enc and --denc: Requires [key] [files]
                    if (idx + 2 >= args.length || args[idx + 1].startsWith("-") || args[idx + 2].startsWith("-")) {
                        System.err.println("Error: Invalid arguments for " + arg + ". Expected: [key] [files]");
                        return false;
                    }
                    idx += 3; // Skip [key] and [files]
                    break;
    
                case "--com":
                case "--dcom":
                    // Validate --com and --dcom: Requires [files]
                    if (idx + 1 >= args.length || args[idx + 1].startsWith("-")) {
                        System.err.println("Error: Invalid arguments for " + arg + ". Expected: [files]");
                        return false;
                    }
                    idx += 2; // Skip [files]
                    break;
    
                case "--mono":
                    // Validate --mono: Requires [arg] [key] [files] and optionally -nocopy
                    if (idx + 3 >= args.length) {
                        System.err.println("Error: Invalid arguments for " + arg + ". Expected: [arg] [key] [files]");
                        return false;
                    }
                    String monoArg = args[idx + 1];
                    if (!monoArg.equals("-in") && !monoArg.equals("-out") && !monoArg.equals("-verify")) {
                        System.err.println("Error: Invalid mono argument " + monoArg + ". Expected: -in, -out, or -verify");
                        return false;
                    }
                    if (args[idx + 2].startsWith("-")) {
                        System.err.println("Error: Invalid key for " + arg + ". Expected: [key]");
                        return false;
                    }
                    if (args[idx + 3].startsWith("-")) {
                        System.err.println("Error: Invalid files for " + arg + ". Expected: [files]");
                        return false;
                    }
                    // Check for optional -nocopy
                    if (idx + 4 < args.length && args[idx + 4].equals("-nocopy")) {
                        idx++; // Skip -nocopy
                    }
                    idx += 4; // Skip [arg], [key], [files]
                    break;
            }
        }
        return true; // All arguments are valid
    }
    
}