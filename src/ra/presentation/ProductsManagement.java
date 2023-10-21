package ra.presentation;

import ra.business.models.Product;
import ra.business.serviceInterface.ICategoryServices;
import ra.business.serviceInterface.IProductServices;
import ra.business.services.CategoryServices;
import ra.business.services.ProductServices;
import ra.business.utils.CommandLineTable;
import ra.business.utils.FormatDate;
import ra.business.utils.GFG;
import ra.business.utils.Messages;

import java.time.LocalDateTime;
import java.util.Scanner;

public class ProductsManagement {
    public IProductServices productServices = new ProductServices();
    public ICategoryServices categoryServices = new CategoryServices();

    public void run() {
        while (true) {
            System.out.print(GFG.BLACK_BACKGROUND + GFG.GREEN);
            System.out.println("________________ Product Management __________________");
            System.out.println("[1]. Hiển thị danh sách sản phẩm");
            System.out.println("[2]. Tạo mới sản phẩm");
            System.out.println("[3]. Tìm kiếm sảnh phẩm theo tên");
            System.out.println("[4]. Chỉnh sửa thông tin sản phẩm");
            System.out.println("[5]. Ẩn/hiện sản phẩm theo mã danh mục");
            System.out.println("[0]. Trở về menu chính");
            System.out.println("__________________________________________________________");
            System.out.print(GFG.ANSI_RESET);
            try {
                switch (Integer.parseInt(new Scanner(System.in).nextLine())) {
                    case 0:
                        return;
                    case 1:
                        showProduct();
                        break;
                    case 2:
                        createProduct();
                        break;
                    case 3:
                        searchProduct();
                        break;
                    case 4:
                        updateProduct();
                        break;
                    case 5:
                        changeStatusProduct();
                        break;
                    default:
                        Messages.error("Giá trị nhập vào không đúng vui lòng nhập lại!");
                }
            } catch (NumberFormatException ex) {

                Messages.error("Giá trị nhập vào không đúng vui lòng nhập lại!");
            }

        }
    }

    public void showProduct() {
        System.out.println("CATEGORIES LIST");
        CommandLineTable st = new CommandLineTable();
//        st.setRightAlign(true);//if true then cell text is right aligned
        st.setShowVerticalLines(true);//if false (default) then no vertical lines are shown
        st.setHeaders("ID", "NAME", "CATEGORY", "DESCRIPTION", "UNIT_PRICE", "STOCK", "STATUS", "CREATED_AT", "UPDATED_AT");//optional - if not used then there will be no header and horizontal lines
        productServices.findAll().forEach(product -> {
            st.addRow(String.valueOf(product.getId()),
                    product.getName(),
                    categoryServices.findById(product.getCategoryId()).getName(),
                    product.getDescription(),
                    String.valueOf(product.getUnitPrice()),
                    String.valueOf(product.getStock()),
                    String.valueOf(product.getStatus()),
                    FormatDate.formatter(product.getCreatedAt()),
                    FormatDate.formatter(product.getUpdatedAt()));
        });
        st.print();
        System.out.println(GFG.ANSI_RESET);
    }

    public void createProduct() {
        if (categoryServices.findAll().isEmpty()) {
            Messages.warning("Chưa có danh mục nào được thêm, vui lòng thêm mới một danh mục");
            return;
        }
        Scanner sc = new Scanner(System.in);
        System.out.println("PRODUCT CREATE");
        System.out.println("Nhập số lượng sản phẩm muốn thêm: ");
        int prInt;
        while (true) {
            try {
                int i = Integer.parseInt(sc.nextLine());
                if (i > 0) {
                    prInt = i;
                    break;
                }
            } catch (NumberFormatException ex) {
                Messages.error("Số lượng không đúng!");
            }
        }
        for (int i = 0; i < prInt; i++) {
            Product product = new Product();
            product.setId(productServices.autoIncrementId(productServices.findAll()));
            product.setStatus(true);
            product.setCreatedAt(LocalDateTime.now());
            product.setUpdatedAt(LocalDateTime.now());
            while (true) {
                System.out.println("Name: ");
                String name = sc.nextLine().trim();
                if (name.isEmpty()) {
                    Messages.error("Tên sản phẩm không được để trống!");
                    continue;
                }
                if (name.trim().length() < 6) {
                    Messages.error("Tên sản phẩm ít nhất 6 kí tự!");
                    continue;
                }
                if (productServices.existName(name)) {
                    product.setName(name);
                    break;
                }
                Messages.error("Sản phẩm đã tồn tại vui lòng nhập lại!");
            }
            while (true) {
                try {
                    categoryServices.findAll().forEach(category -> {
                        System.out.println(GFG.BLACK_BACKGROUND + GFG.WHITE + "[" + category.getId() + "]. " + category.getName() + " " + GFG.ANSI_RESET);
                    });
                    System.out.println("Chọn danh mục: ");
                    Long categoryId = Long.parseLong(sc.nextLine());
                    if (categoryServices.findById(categoryId) != null) {
                        product.setCategoryId(categoryId);
                        break;
                    }
                    Messages.error("Id danh mục không tồn tại!");
                } catch (NumberFormatException ex) {
                    Messages.error("Id danh mục không đúng!");
                }

            }
            while (true) {
                System.out.println("Description: ");
                String desc = sc.nextLine().trim();
                if (!desc.isEmpty()) {
                    product.setDescription(desc);
                    break;
                }
                Messages.error("Mô tả danh mục không được để trống !");
            }
            while (true) {
                try {
                    System.out.println("UnitPrice: ");
                    double price = Double.parseDouble(sc.nextLine());
                    if (price > 0) {
                        product.setUnitPrice(price);
                        break;
                    }
                    Messages.error("Đơn giá lớn hơn 0!");
                } catch (NumberFormatException ex) {
                    Messages.error("Đơn giá không đúng!");
                }

            }
            while (true) {
                try {
                    System.out.println("Stock: ");
                    int stock = Integer.parseInt(sc.nextLine());
                    if (stock >= 0) {
                        product.setStock(stock);
                        break;
                    }
                    Messages.error("Số lượng lớn hơn hoặc bằng 0!");
                } catch (NumberFormatException ex) {
                    Messages.error("Số lượng không đúng!");
                }

            }
            productServices.save(product);
            Messages.success("Sẩn phẩm " + product.getName() + " đã thêm thành công!");
            System.out.println();
        }

    }

    public void searchProduct() {
        Scanner sc = new Scanner(System.in);
        System.out.println("PRODUCT SEARCH");
        System.out.println("Name:");
        String q = sc.nextLine().trim();
        System.out.println("RESULTS");
        CommandLineTable st = new CommandLineTable();
//        st.setRightAlign(true);//if true then cell text is right aligned
        st.setShowVerticalLines(true);//if false (default) then no vertical lines are shown
        st.setHeaders("ID", "NAME", "CATEGORY", "DESCRIPTION", "UNIT_PRICE", "STOCK", "STATUS", "CREATED_AT", "UPDATED_AT");//optional - if not used then there will be no header and horizontal lines
        productServices.searchByName(q).forEach(product -> {
            st.addRow(String.valueOf(product.getId()),
                    product.getName(),
                    categoryServices.findById(product.getCategoryId()).getName(),
                    product.getDescription(),
                    String.valueOf(product.getUnitPrice()),
                    String.valueOf(product.getStock()),
                    String.valueOf(product.getStatus()),
                    FormatDate.formatter(product.getCreatedAt()),
                    FormatDate.formatter(product.getUpdatedAt()));
        });
        st.print();
        System.out.println(GFG.ANSI_RESET);
    }

    public void updateProduct() {
        Scanner sc = new Scanner(System.in);
        System.out.println("PRODUCT UPDATE");
        Product product;

        while (true) {
            System.out.println("Nhâp id sản phẩm : ");
            Long id = Long.parseLong(sc.nextLine());
            if (productServices.findById(id) != null) {
                product = productServices.findById(id);
                break;
            }
            Messages.error("id sản phẩm không tồn tại : ");
        }

        while (true) {
            System.out.println("Name: ");
            String name = sc.nextLine().trim();
            if (name.isEmpty()) {
                break;
            }
            if (name.trim().length() < 6) {
                Messages.error("Tên sản phẩm ít nhất 6 kí tự!");
                continue;
            }
            if (productServices.existName(name)) {
                product.setName(name);
                break;
            }
            Messages.error("Sản phẩm đã tồn tại vui lòng nhập lại!");
        }
        while (true) {
            try {
                categoryServices.findAll().forEach(category -> {
                    System.out.println(GFG.BLACK_BACKGROUND + GFG.WHITE + "[" + category.getId() + "]. " + category.getName() + " " + GFG.ANSI_RESET);
                });
                System.out.println("Chọn danh mục: ");
                String cateStr = sc.nextLine();
                if (cateStr.isEmpty()) {
                    break;
                }
                Long categoryId = Long.parseLong(cateStr);
                if (categoryServices.findById(categoryId) != null) {
                    product.setCategoryId(categoryId);
                    break;
                }
                Messages.error("Id danh mục không tồn tại!");
            } catch (NumberFormatException ex) {
                Messages.error("Id danh mục không đúng!");
            }

        }
        System.out.println("Description: ");
        String desc = sc.nextLine().trim();
        if (!desc.isEmpty()) {
            product.setDescription(desc);
        }

        while (true) {
            try {
                System.out.println("UnitPrice: ");
                String priceStr = sc.nextLine();
                if (priceStr.isEmpty()) {
                    break;
                }
                double price = Double.parseDouble(priceStr);
                if (price > 0) {
                    product.setUnitPrice(price);
                    break;
                }
                Messages.error("Giá trị nhập vào phải lớn hơn 0!");
            } catch (NumberFormatException ex) {
                Messages.error("Giá trị nhập vào không đúng!");
            }

        }
        while (true) {
            try {
                System.out.println("Stock: ");
                String stockStr = sc.nextLine();
                if (stockStr.isEmpty()) {
                    break;
                }
                int stock = Integer.parseInt(stockStr);
                if (stock >= 0) {
                    product.setStock(stock);
                    break;
                }
                Messages.error("Số lượng lớn hơn hoặc bằng 0!");
            } catch (NumberFormatException ex) {
                Messages.error("Số lượng không đúng!");
            }

        }

        product.setUpdatedAt(LocalDateTime.now());
        productServices.save(product);
        Messages.success("Sẩn phẩm " + product.getName() + " đã cập nhật thành công!");
        System.out.println();
    }

    public void changeStatusProduct() {
        Scanner sc = new Scanner(System.in);
        System.out.println("PRODUCT CHANGE STATUS");
        Product product;
        while (true) {
            System.out.println("Nhập id sản phẩm : ");
            Long id = Long.parseLong(sc.nextLine());
            if (productServices.findById(id) != null) {
                product = productServices.findById(id);
                product.setStatus(!product.getStatus());
                break;
            }
            Messages.error("id sản phẩm không tồn tại : ");
        }
        product.setUpdatedAt(LocalDateTime.now());
        productServices.save(product);
        Messages.success("Sẩn phẩm " + product.getName() + " đã thay đổi trạng thái thành " + product.getStatus());
        System.out.println();
    }
}
