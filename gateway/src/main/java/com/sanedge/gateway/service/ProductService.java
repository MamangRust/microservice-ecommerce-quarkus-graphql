package com.sanedge.gateway.service;

import com.sanedge.gateway.dto.ProductDto.CreateProductRequest;
import com.sanedge.gateway.dto.ProductDto.CreateProductResponse;
import com.sanedge.gateway.dto.ProductDto.FindAllProductResponse;
import com.sanedge.gateway.dto.ProductDto.FindByIdProductResponse;
import com.sanedge.gateway.dto.ProductDto.SimpleStatusMessageResponse;
import com.sanedge.gateway.dto.ProductDto.UpdateProductRequest;
import com.sanedge.gateway.dto.ProductDto.UpdateProductResponse;
import io.smallrye.mutiny.Uni;
import org.jboss.resteasy.reactive.multipart.FileUpload;

public interface ProductService {
    Uni<FindAllProductResponse> listProducts(int page, int size, String search);
    Uni<FindAllProductResponse> listActiveProducts(int page, int size, String search);
    Uni<FindAllProductResponse> listTrashedProducts(int page, int size, String search);
    Uni<FindByIdProductResponse> getProduct(int id);
    Uni<CreateProductResponse> createProduct(CreateProductRequest body);
    Uni<UpdateProductResponse> updateProduct(int id, UpdateProductRequest body);
    Uni<UpdateProductResponse> uploadProduct(int id, FileUpload file);
    Uni<FindByIdProductResponse> deleteProduct(int id);
    Uni<FindByIdProductResponse> restoreProduct(int id);
    Uni<SimpleStatusMessageResponse> deleteProductPermanent(int id);
    Uni<SimpleStatusMessageResponse> restoreAllProducts();
    Uni<SimpleStatusMessageResponse> deleteAllProductsPermanent();
}
