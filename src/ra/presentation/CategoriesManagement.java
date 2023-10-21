package ra.presentation;

import ra.business.models.Category;
import ra.business.serviceInterface.ICategoryServices;
import ra.business.services.CategoryServices;
import ra.business.utils.CommandLineTable;
import ra.business.utils.FormatDate;
import ra.business.utils.GFG;
import ra.business.utils.Messages;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class CategoriesManagement {
    public ICategoryServices categoryServices = new CategoryServices();

    public void run() {
        while (true) {
            System.out.print(GFG.BLACK_BACKGROUND+GFG.GREEN);
            System.out.println("________________ Categories Management __________________");
            System.out.println("[1]. Hiển thị danh sách danh mục sản phẩm");
            System.out.println("[2]. Tạo mới danh mục");
            System.out.println("[3]. Tìm kiếm danh mục theo tên");
            System.out.println("[4]. Chỉnh sửa thông tin danh mục");
            System.out.println("[5]. Ẩn/hiện danh mục theo mã danh mục");
            System.out.println("[0]. Trở về menu chính");
            System.out.println("__________________________________________________________");
            System.out.print(GFG.ANSI_RESET);
            try {
                switch (Integer.parseInt(new Scanner(System.in).nextLine())) {
                    case 0:
                        return;
                    case 1:
                        showCategory();
                        break;
                    case 2:
                        createCategory();
                        break;
                    case 3:
                        searchCategory();
                        break;
                    case 4:
                        updateCategory();
                        break;
                    case 5:
                        changeStatusCategory();
                        break;
                    default:
                        Messages.error("Giá trị nhập vào không đúng vui lòng nhập lại!");
                }
            } catch (NumberFormatException ex) {

                Messages.error("Giá trị nhập vào không đúng vui lòng nhập lại!");
            }

        }
    }

    public void showCategory() {
        System.out.println("CATEGORIES LIST");
        CommandLineTable st = new CommandLineTable();
//        st.setRightAlign(true);//if true then cell text is right aligned
        st.setShowVerticalLines(true);//if false (default) then no vertical lines are shown
        st.setHeaders("ID", "NAME", "DESCRIPTION", "STATUS", "CREATED_AT", "UPDATED_AT");//optional - if not used then there will be no header and horizontal lines
        categoryServices.findAll().forEach(category -> {
            st.addRow(String.valueOf(category.getId()), category.getName(), category.getDescription(), String.valueOf(category.getStatus()), FormatDate.formatter(category.getCreatedAt()), FormatDate.formatter(category.getUpdatedAt()));
        });
        st.print();
        System.out.println(GFG.ANSI_RESET);
    }

    public void createCategory() {
        Scanner sc = new Scanner(System.in);
        System.out.println("CATEGORIES CREATE");
        Category category = new Category();
        category.setId(categoryServices.autoIncrementId(categoryServices.findAll()));
        category.setStatus(true);
        category.setCreatedAt(LocalDateTime.now());
        category.setUpdatedAt(LocalDateTime.now());
        while (true) {
            System.out.println("Name: ");
            String name = sc.nextLine().trim();
            if (name.isEmpty()) {
                Messages.error("Tên danh mục không được để trống !");
                continue;
            }
            if (categoryServices.existName(name)) {
                category.setName(name);
                break;
            }
            Messages.error("Danh mục sản phẩm đã tồn tại vui lòng nhập lại!");
        }
        while (true) {
            System.out.println("Description: ");
            String desc = sc.nextLine().trim();
            if (!desc.isEmpty()) {
                category.setDescription(desc);
                break;
            }
            Messages.error("Mô tả danh mục không được để trống !");
        }
        categoryServices.save(category);
        Messages.success("Danh mục  " + category.getName() + " đã thêm thành công!");
        System.out.println();
    }

    public void searchCategory() {
        Scanner sc = new Scanner(System.in);
        System.out.println("CATEGORIES SEARCH");
        System.out.println("Name:");
        String q = sc.nextLine().trim();
        System.out.println("RESULTS");
        CommandLineTable st = new CommandLineTable();
//        st.setRightAlign(true);//if true then cell text is right aligned
        st.setShowVerticalLines(true);//if false (default) then no vertical lines are shown
        st.setHeaders("ID", "NAME", "DESCRIPTION", "STATUS", "CREATED_AT", "UPDATED_AT");//optional - if not used then there will be no header and horizontal lines
        categoryServices.searchByName(q).forEach(category -> {
            st.addRow(String.valueOf(category.getId()), category.getName(), category.getDescription(), String.valueOf(category.getStatus()), FormatDate.formatter(category.getCreatedAt()), FormatDate.formatter(category.getUpdatedAt()));
        });
        st.print();
        System.out.println(GFG.ANSI_RESET);
    }

    public void updateCategory() {
        Scanner sc = new Scanner(System.in);
        System.out.println("CATEGORIES UPDATE");
        Category category;
        while (true) {
            System.out.println("Nhap id danh muc : ");
            Long id = Long.parseLong(sc.nextLine());
            if (categoryServices.findById(id) != null) {
                category = categoryServices.findById(id);
                break;
            }
            Messages.error("id danh muc khong ton tai : ");
        }

        while (true) {
            System.out.println("Name: ");
            String name = sc.nextLine().trim();
            if (name.isEmpty()) {
                break;
            }
            if (categoryServices.existName(name)) {
                category.setName(name);
                break;
            }
            Messages.error("Danh mục sản phẩm đã tồn tại vui lòng nhập lại!");
        }

        System.out.println("Description: ");
        String desc = sc.nextLine().trim();
        if (!desc.isEmpty()) {
            category.setDescription(desc);
        }

        category.setUpdatedAt(LocalDateTime.now());
        categoryServices.save(category);
        Messages.success("Danh mục  " + category.getName() + " đã cập nhật thành công!");
        System.out.println();
    }

    public void changeStatusCategory() {
        Scanner sc = new Scanner(System.in);
        System.out.println("CATEGORIES CHANGE STATUS");
        Category category;
        while (true) {
            System.out.println("Nhap id danh muc : ");
            Long id = Long.parseLong(sc.nextLine());
            if (categoryServices.findById(id) != null) {
                category = categoryServices.findById(id);
                category.setStatus(!category.getStatus());
                break;
            }
            Messages.error("id danh muc khong ton tai : ");
        }
        category.setUpdatedAt(LocalDateTime.now());
        categoryServices.save(category);
        Messages.success("Danh mục  " + category.getName() + " đã thay đổi trạng thái thành " + category.getStatus());
        System.out.println();
    }
}
