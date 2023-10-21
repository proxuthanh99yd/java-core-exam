package ra.presentation;

import ra.business.utils.GFG;
import ra.business.utils.Messages;

import java.util.Scanner;

public class ShopManagement {
    public static void main(String[] args) {
        while (true) {
            System.out.print(GFG.BLACK_BACKGROUND + GFG.GREEN);
            System.out.println("________________ Shop Management __________________");
            System.out.println("[1]. Quản lý danh mục");
            System.out.println("[2]. Quản lý sản phẩm");
            System.out.println("[0]. Thoát");
            System.out.println("__________________________________________________________");
            System.out.print(GFG.ANSI_RESET);
            try {
                switch (Integer.parseInt(new Scanner(System.in).nextLine())) {
                    case 0:
                        System.exit(0);
                    case 1:
                        CategoriesManagement categoriesManagement = new CategoriesManagement();
                        categoriesManagement.run();
                        break;
                    case 2:
                        ProductsManagement productsManagement = new ProductsManagement();
                        productsManagement.run();
                        break;
                    default:
                        Messages.error("Giá trị nhập vào không đúng vui lòng nhập lại!");
                }
            } catch (NumberFormatException ex) {
                Messages.error("Giá trị nhập vào không đúng vui lòng nhập lại!");
            }
        }
    }
}
