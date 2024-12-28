import java.util.Random;

public class PrintMethods {


        public static String toRainbow(String input) {
        String[] colors = {
            "\u001B[31m",
            "\u001B[33m",
            "\u001B[32m",
            "\u001B[36m",
            "\u001B[34m",
            "\u001B[35m"
        };
        StringBuilder rainbowString = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            rainbowString.append(colors[i % colors.length]).append(input.charAt(i));
        }
        rainbowString.append("\u001B[0m");
        return rainbowString.toString();
    }

    public static void printWithGradient(String text) {
        String[] gradientColors = {
            "\u001B[38;5;34m",
            "\u001B[38;5;35m",
            "\u001B[38;5;36m",
            "\u001B[38;5;37m",
            "\u001B[38;5;38m",
            "\u001B[38;5;39m"
        };

        String[] lines = text.split("\n");
        String reset = "\u001B[0m";

        for (int i = 0; i < lines.length; i++) {
            String color = gradientColors[i % gradientColors.length];
            System.out.println(color + lines[i] + reset);
        }
    }

    public static void printWithGradientAndBorders(String text) {
        String[] gradientColors = {
            "\u001B[38;5;34m", 
            "\u001B[38;5;35m",
            "\u001B[38;5;36m", 
            "\u001B[38;5;37m", 
            "\u001B[38;5;38m",
            "\u001B[38;5;39m"
        };
        String white = "\u001B[97m";

        String[] lines = text.split("\n");
        String reset = "\u001B[0m";

        for (int i = 0; i < lines.length; i++) {
            String color = gradientColors[i % gradientColors.length];
            for (int j = 0; j < lines[i].length(); j++) {
                char c = lines[i].charAt(j);
                if (isBorderCharacter(c)) {
                    System.out.print(white + c);
                } else {
                    System.out.print(color + c);
                }
            }
            System.out.println(reset);
        }
    }

    public static String printRandom(String txt) {
        int r = new Random().nextInt(256);
        int g = new Random().nextInt(256);
        int b = new Random().nextInt(256);

        String e = "";
        for(char c : txt.toCharArray()) {
            if(isBorderCharacter(c)) {
                e += rgbToAnsi(255, 255, 255) + c;
            } else {
                e += rgbToAnsi(r, g, b) + c;
            }
        }
        return e;
    }

    public static String printTern(String txt, int r, int g, int b) {
        String e = "";
        for(char c : txt.toCharArray()) {
            if(isBorderCharacter(c)) {
                e += rgbToAnsi(255, 255, 255) + c;
            } else {
                e += rgbToAnsi(r, g, b) + c;
            }
        }
        return e;
    }

    public static boolean isBorderCharacter(char c) {
        return c == '╚' || c == '═' || c == '╗' || c == '╝' || c == '║' || c == '╔';
    }

    public static String rgbToAnsi(int r, int g, int b) {
        if (r < 0) r = 0;
        if (r > 255) r = 255;
        if (g < 0) g = 0;
        if (g > 255) g = 255;
        if (b < 0) b = 0;
        if (b > 255) b = 255;
        return "\u001B[38;2;" + r + ";" + g + ";" + b + "m";
    }

    public static void updateTERN(String txt) {
        String up = "\u001B[10A";
        for(int i = 0; i < 1000 || true; i++) {
            System.out.print(up+"\r"+printRandom(txt));
            try {
                Thread.sleep(800);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            up = "\u001B[5A";
        }
    }

    public static void flashTERN(String txt) {
        String up = "\u001B[10A";
        for(int i = 0; i < 1000 || true; i++) {
            if(i % 2 == 0) {
                System.out.print(up+"\r"+printRandom(txt));
            } else {
                System.out.print(up+"\r"+printEmpty());
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            up = "\u001B[5A";
        }
    }

    public static void transTERN(String txt) {
        String up = "\u001B[10A";
        float hue = 0.0f;
        while (true) {
            java.awt.Color color = java.awt.Color.getHSBColor(hue, 1.0f, 1.0f);
            int r = color.getRed();
            int g = color.getGreen();
            int b = color.getBlue();
            System.out.print(up + "\r" + printTern(txt, r, g, b));
            hue += 0.002f;
            if (hue > 1.0f) hue = 0.0f;
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    
            up = "\u001B[5A";
        }
    }

    public static void waveTERN(String txt) {
        String up = "\u001B[10A";
        float baseHue = 0.0f;
    
        while (true) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < txt.length(); i++) {
                float hue = (baseHue + i * 0.03f) % 1.0f;
                java.awt.Color color = java.awt.Color.getHSBColor(hue, 1.0f, 1.0f);
                int r = color.getRed();
                int g = color.getGreen();
                int b = color.getBlue();
                sb.append(printTern(String.valueOf(txt.charAt(i)), r, g, b));
            }
            System.out.print(up + "\r" + sb);
            up = "\u001B[5A";
            baseHue += 0.01f;
            if (baseHue > 1.0f) baseHue = 0.0f;
    
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void flickerTERN(String txt) {
        String up = "\u001B[10A";
    
        while (true) {
            StringBuilder sb = new StringBuilder();
            for (char c : txt.toCharArray()) {
                int r = (int) (Math.random() * 256);
                int g = (int) (Math.random() * 256);
                int b = (int) (Math.random() * 256);
                sb.append(printTern(String.valueOf(c), r, g, b));
            }
            System.out.print(up + "\r" + sb);
            up = "\u001B[5A";
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    
    
    public static void randomTERN(String txt) {
        String up = "\u001B[10A";
    
        while (true) {
            StringBuilder sb = new StringBuilder();
            for (char c : txt.toCharArray()) {
                // Generate random colors
                int r = (int) (Math.random() * 256);
                int g = (int) (Math.random() * 256);
                int b = (int) (Math.random() * 256);
                sb.append(printTern(String.valueOf(c), r, g, b));
            }
            System.out.print(up + "\r" + sb);
    
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }    

    public static String printEmpty() {
        return "                                      \n                                      \n                                      \n                                      \n                                      \n                                      ";
    }
}
