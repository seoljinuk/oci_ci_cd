package com.shopping.controller;

import com.shopping.entity.Product;
import com.shopping.service.ObjectStorageService;
import com.shopping.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/*
    http://localhost:9000/products
    http://localhost:9000/products/1
*/

@Controller
@RequestMapping({"/products", "/"})
public class ProductController {

    private final ObjectStorageService objectStorageService;
    private final ProductService productService;

    public ProductController(
            ProductService productService,
            ObjectStorageService objectStorageService) {

        this.productService = productService;
        this.objectStorageService = objectStorageService;
    }

    // 상품 목록
    @GetMapping
    public String list(Model model) {
        model.addAttribute(
                "products",
                productService.getAllProducts()
        );

        return "productList";
    }

    // 상품 상세
    @GetMapping("/{id:\\d+}")
    public String detail(
            @PathVariable Long id,
            Model model) {

        Product product =
                productService.getProductById(id);

        if (product == null) {
            return "redirect:/products";
        }

        model.addAttribute("product", product);

        return "productDetail";
    }

    // 상품 등록 화면
    @GetMapping("/insert")
    public String insertForm() {
        return "productInsertForm";
    }

    // ==========================================================
    // 상품 등록 처리
    // ==========================================================

    @PostMapping("/insert")
    public String createProduct(
            @RequestParam("name") String name,
            @RequestParam("price") int price,
            @RequestParam("description") String description,
            @RequestParam("image") MultipartFile image,
            Model model)
            throws IOException {

        // ==========================================================
        // 1. OCI Bucket 확인
        // ==========================================================

//        try {
//
//            objectStorageService.checkBucket();
//
//            System.out.println("OCI Bucket 확인 성공!");
//
//        } catch (Exception e) {
//
//            System.out.println("OCI Bucket 확인 실패!");
//            e.printStackTrace();
//
//            model.addAttribute(
//                    "errorMessage",
//                    "OCI Object Storage Bucket에 접근할 수 없습니다."
//            );
//
//            // Bucket 확인에 실패하면 여기서 종료
//            return "productInsertForm";
//        }

        // ==========================================================
        // 2. 이미지 파일명 생성
        // ==========================================================

        String originalFilename =
                image.getOriginalFilename();

        String extension = "";

        if (originalFilename != null
                && originalFilename.contains(".")) {

            extension =
                    originalFilename.substring(
                            originalFilename.lastIndexOf(".")
                    );
        }

        String objectName =
                UUID.randomUUID() + extension;

        // ==========================================================
        // 3. OCI Object Storage에 이미지 업로드
        // ==========================================================

        String imageUrl =
                objectStorageService.uploadImage(
                        image,
                        objectName
                );

        // ==========================================================
        // 4. Product 객체 생성
        // ==========================================================

        Product product = new Product();

        product.setName(name);
        product.setPrice(price);
        product.setDescription(description);
        product.setImageUrl(imageUrl);

        // ==========================================================
        // 5. DB 저장
        // ==========================================================

        productService.saveProduct(product);

        // ==========================================================
        // 6. 상품 목록으로 이동
        // ==========================================================

        return "redirect:/products";
    }
}