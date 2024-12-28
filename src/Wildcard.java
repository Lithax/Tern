import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Wildcard {

    public static File[] getFilesFromIdx(String[] args, int idx) {
        boolean start = false;
        boolean end = false;
        List<File> files = new ArrayList<>();
        for (int i = idx; i < args.length && !end; i++) {
            if(!args[i].startsWith("-") && !(args[i].length() < 6)) {
                if(new File(args[i]).exists()) {
                    files.add(new File(args[i]));
                } else {
                    System.out.println("Error file does not exist: " + args[i]);
                }
                start = true;
            } else {
                if(start) {
                    end = true;
                }
            }
        }
        return files.toArray(new File[0]);
    }

    public static File[] getMatchingFiles(String wildcard) {
        String regex = wildcardToRegex(wildcard);
        File dir = new File(".");
        return dir.listFiles(new FilenameFilter() {
            private final Pattern pattern = Pattern.compile(regex);

            @Override
            public boolean accept(File dir, String name) {
                return pattern.matcher(name).matches();
            }
        });
    }

    private static String wildcardToRegex(String wildcard) {
        StringBuilder s = new StringBuilder(wildcard.length());
        s.append('^');
        for (int i = 0, is = wildcard.length(); i < is; i++) {
            char c = wildcard.charAt(i);
            switch (c) {
                case '*':
                    s.append(".*");
                    break;
                case '?':
                    s.append(".");
                    break;
                case '(': case ')': case '[': case ']': case '$': case '^':
                case '.': case '{': case '}': case '|': case '\\':
                    s.append("\\");
                    s.append(c);
                    break;
                default:
                    s.append(c);
                    break;
            }
        }
        s.append('$');
        return s.toString();
    }

    public static File[] listFilesByPatternOrNames(String expression) throws Exception {
        List<File> allMatchedFiles = new ArrayList<>();
            if (expression.contains("*")) {
                Path path = Paths.get(expression).getParent();
                if (path == null) {
                    path = Paths.get(".");
                }
                String pattern = Paths.get(expression).getFileName().toString();
                File directory = path.toFile();
                File[] matchedFiles = matchFilesWithPattern(pattern, directory);
                for (File file : matchedFiles) {
                    allMatchedFiles.add(file);
                }
            } else {
                File file = new File(expression);
                allMatchedFiles.add(file);
            }
        return allMatchedFiles.toArray(new File[0]);
    }

    public static File[] matchFilesWithPattern(String pattern, File directory) {
        if (pattern.equals("*.*")) {
            return directory.listFiles(File::isFile);
        } else if (pattern.startsWith("*.")) {
            String extension = pattern.substring(2);
            return directory.listFiles((dir, name) -> name.endsWith("." + extension));
        } else if (pattern.endsWith(".*")) {
            String baseName = pattern.substring(0, pattern.length() - 2);
            return directory.listFiles((dir, name) -> name.startsWith(baseName + "."));
        } else if (pattern.contains("*")) {
            return directory.listFiles((dir, name) -> name.matches(pattern.replace("*", ".*")));
        }
        return new File[0];
    }

    public static void main(String[] args) {
        args  = new String[]{"--mono", "-in", "key", "file1.png", "file2.png", "--enc", "secret", "file3.png", "file4.png", "--com", "file5.png", "file6.png"};
        try {
            for(File f : getFilesFromIdx(args, 9)) {
                System.out.println(f.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}