package ra.business.services;

import ra.business.config.IOFile;
import ra.business.models.Product;
import ra.business.serviceInterface.IProductServices;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class ProductServices implements IProductServices {
    private final List<Product> productList;

    public ProductServices() {
        this.productList = IOFile.readFromFile(IOFile.PRODUCTS_PATH);
    }

    @Override
    public List<Product> findAll() {
        productList.sort(Comparator.comparing(Product::getUpdatedAt).reversed());
        return productList;
    }

    @Override
    public Product findById(Long id) {
        return productList.stream()
                .filter(pro -> Objects.equals(pro.getId(), id))
                .findFirst().orElse(null);
    }

    @Override
    public boolean save(Product product) {
        Product oldProduct = findById(product.getId());
        if (oldProduct != null) {
            // chức nănng cập nhật
            productList.set(productList.indexOf(oldProduct), product);
        } else {
            // chức năng thêm mới
            product.setId(autoIncrementId(productList));
            productList.add(product);
        }
        // lưu vào file
        IOFile.writeToFile(IOFile.PRODUCTS_PATH, productList);
        return true;
    }

    @Override
    public void changeStatusById(Long id) {
        Product product = findById(id);
        if (product != null) {
            product.setStatus(!product.getStatus());
        } else {
            System.err.println("ID nhập vào không khớp với cơ sở dữ liệu, vui lòng nhập lại!");
        }
    }

    @Override
    public Long autoIncrementId(List<Product> products) {
        return products.stream().mapToLong(Product::getId).max().orElse(0) + 1;
    }

    @Override
    public Stream<Product> searchByName(String name) {
        return productList.stream().filter(pro ->pro.getName().trim().toLowerCase().contains(name.trim().toLowerCase()));
    }

    @Override
    public boolean existName(String name) {
        Product product = productList.stream()
                .filter(pro -> Objects.equals(pro.getName().trim().toLowerCase(), name.trim().toLowerCase()))
                .findFirst().orElse(null);
        return product == null;
    }
}
