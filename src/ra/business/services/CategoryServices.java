package ra.business.services;

import ra.business.config.IOFile;
import ra.business.models.Category;
import ra.business.serviceInterface.ICategoryServices;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class CategoryServices implements ICategoryServices {
    private final List<Category> categoryList;

    public CategoryServices() {
        this.categoryList = IOFile.readFromFile(IOFile.CATEGORIES_PATH);
    }

    @Override
    public List<Category> findAll() {
        categoryList.sort(Comparator.comparing(Category::getUpdatedAt).reversed());
        return categoryList;
    }

    @Override
    public Category findById(Long id) {
        return categoryList.stream()
                .filter(cate -> Objects.equals(cate.getId(), id))
                .findFirst().orElse(null);
    }

    @Override
    public boolean save(Category category) {
        Category oldCategory = findById(category.getId());
        if (oldCategory != null) {
            // chức nănng cập nhật
            categoryList.set(categoryList.indexOf(oldCategory), category);
        } else {
            // chức năng thêm mới
            category.setId(autoIncrementId(categoryList));
            categoryList.add(category);
        }
        // lưu vào file
        IOFile.writeToFile(IOFile.CATEGORIES_PATH, categoryList);
        return true;
    }

    @Override
    public void changeStatusById(Long id) {
        Category category = findById(id);
        if (category != null) {
            category.setStatus(!category.getStatus());
        } else {
            System.err.println("ID nhập vào không khớp với cơ sở dữ liệu, vui lòng nhập lại!");
        }
    }

    @Override
    public Long autoIncrementId(List<Category> categories) {
        return categories.stream().mapToLong(Category::getId).max().orElse(0) + 1;
    }

    @Override
    public Stream<Category> searchByName(String name) {
        return categoryList.stream().filter(cate -> cate.getName().trim().toLowerCase().contains(name.toLowerCase()));
    }

    @Override
    public boolean existName(String name) {
        Category category = categoryList.stream()
                .filter(cate -> Objects.equals(cate.getName().trim().toLowerCase(), name.trim().toLowerCase()))
                .findFirst().orElse(null);
        return category == null;
    }
}
