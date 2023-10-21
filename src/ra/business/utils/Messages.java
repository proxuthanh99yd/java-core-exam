package ra.business.utils;

public class Messages {
    public static void error(String str){
        System.out.println(GFG.BLACK_BACKGROUND + GFG.RED + str + GFG.ANSI_RESET);
    }
    public static void success(String str){
        System.out.println(GFG.BLACK_BACKGROUND + GFG.GREEN + str + GFG.ANSI_RESET);
    }
    public static void warning(String str){
        System.out.println(GFG.BLACK_BACKGROUND + GFG.YELLOW + str + GFG.ANSI_RESET);
    }
}
