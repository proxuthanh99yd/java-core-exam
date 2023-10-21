package ra.presentation;

import ra.business.utils.GFG;

import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        System.out.println(GFG.BLACK_BACKGROUND+GFG.BLUE  +"test color"+GFG.ANSI_RESET);
        System.out.println(GFG.BLACK_BACKGROUND  +"test color"+GFG.ANSI_RESET);
//        Scanner sc = new Scanner(System.in);
//        while (true){
//            try {
//                System.out.println("UnitPrice: ");
//                String priceStr = sc.nextLine();
//                if (priceStr.isEmpty()){
//                    break;
//                }
//                double price = Double.parseDouble(priceStr);
//                if (price > 0) {
//                    System.out.println("price"+price);
//                    break;
//                }
//                System.err.println("Đơn giá lớn hơn 0!");
//            } catch (NumberFormatException ex) {
//                System.err.println("Đơn giá không đúng!");
//            }
//        }
    }
}
