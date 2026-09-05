package com.sanedge.gateway.service.impl;

import com.sanedge.gateway.dto.ProductDto.CreateProductRequest;
import com.sanedge.gateway.dto.ProductDto.CreateProductResponse;
import com.sanedge.gateway.dto.ProductDto.FindAllProductResponse;
import com.sanedge.gateway.dto.ProductDto.FindByIdProductResponse;
import com.sanedge.gateway.dto.ProductDto.SimpleStatusMessageResponse;
import com.sanedge.gateway.dto.ProductDto.UpdateProductRequest;
import com.sanedge.gateway.dto.ProductDto.UpdateProductResponse;
import com.sanedge.gateway.service.FileService;
import com.sanedge.gateway.service.ProductService;
import com.sanedge.gateway.telemetry.TelemetryHelper;
import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.resteasy.reactive.multipart.FileUpload;

@ApplicationScoped
public class ProductServiceImpl implements ProductService {

    @Inject
    FileService fileService;

    @Inject
    TelemetryHelper telemetryHelper;

    @GrpcClient("product")
    pb.product.MutinyProductQueryServiceGrpc.MutinyProductQueryServiceStub productQueryService;

    @GrpcClient("product")
    pb.product.MutinyProductCommandServiceGrpc.MutinyProductCommandServiceStub productCommandService;

    @Override
    public Uni<FindAllProductResponse> listProducts(int page, int size, String search) {
        return telemetryHelper.traceAndMetric("product.listProducts", () -> productQueryService.findAll(pb.product.ProductQuery.FindAllProductRequest.newBuilder()
                .setPage(page)
                .setPageSize(size)
                .setSearch(search == null ? "" : search)
                .build())
                .map(FindAllProductResponse::from));
    }

    @Override
    public Uni<FindAllProductResponse> listActiveProducts(int page, int size, String search) {
        return telemetryHelper.traceAndMetric("product.listActiveProducts", () -> productQueryService.findByActive(pb.product.ProductQuery.FindAllProductRequest.newBuilder()
                .setPage(page)
                .setPageSize(size)
                .setSearch(search == null ? "" : search)
                .build())
                .map(FindAllProductResponse::from));
    }

    @Override
    public Uni<FindAllProductResponse> listTrashedProducts(int page, int size, String search) {
        return telemetryHelper.traceAndMetric("product.listTrashedProducts", () -> productQueryService.findByTrashed(pb.product.ProductQuery.FindAllProductRequest.newBuilder()
                .setPage(page)
                .setPageSize(size)
                .setSearch(search == null ? "" : search)
                .build())
                .map(FindAllProductResponse::from));
    }

    @Override
    public Uni<FindByIdProductResponse> getProduct(int id) {
        return telemetryHelper.traceAndMetric("product.getProduct", () -> productQueryService.findById(pb.product.ProductCommon.FindByIdProductRequest.newBuilder()
                .setId(id)
                .build())
                .map(FindByIdProductResponse::from));
    }

    @Override
    public Uni<CreateProductResponse> createProduct(CreateProductRequest body) {
        return telemetryHelper.traceAndMetric("product.createProduct", () -> productCommandService.create(pb.product.ProductCommand.CreateProductRequest.newBuilder()
                .setMerchantId(body.merchantId())
                .setCategoryId(body.categoryId())
                .setName(body.name() == null ? "" : body.name())
                .setDescription(body.description() == null ? "" : body.description())
                .setPrice(body.price())
                .setCountInStock(body.countInStock())
                .setBrand(body.brand() == null ? "" : body.brand())
                .setWeight(body.weight())
                .setSlugProduct(body.slugProduct() == null ? "" : body.slugProduct())
                .setImageProduct(body.imageProduct() == null ? "" : body.imageProduct())
                .build())
                .map(CreateProductResponse::from));
    }

    @Override
    public Uni<UpdateProductResponse> updateProduct(int id, UpdateProductRequest body) {
        return telemetryHelper.traceAndMetric("product.updateProduct", () -> productCommandService.update(pb.product.ProductCommand.UpdateProductRequest.newBuilder()
                .setProductId(id)
                .setMerchantId(body.merchantId())
                .setCategoryId(body.categoryId())
                .setName(body.name() == null ? "" : body.name())
                .setDescription(body.description() == null ? "" : body.description())
                .setPrice(body.price())
                .setCountInStock(body.countInStock())
                .setBrand(body.brand() == null ? "" : body.brand())
                .setWeight(body.weight())
                .setSlugProduct(body.slugProduct() == null ? "" : body.slugProduct())
                .setImageProduct(body.imageProduct() == null ? "" : body.imageProduct())
                .build())
                .map(UpdateProductResponse::from));
    }

    @Override
    public Uni<UpdateProductResponse> uploadProduct(int id, FileUpload file) {
        return telemetryHelper.traceAndMetric("product.uploadProduct", () -> productQueryService.findById(pb.product.ProductCommon.FindByIdProductRequest.newBuilder().setId(id)
                .build())
                .flatMap(res -> {
                    if (!res.hasData()) {
                        return Uni.createFrom().failure(new Exception("Product not found"));
                    }
                    pb.product.ProductCommon.ProductResponse data = res.getData();
                    String filepath = "uploads/products/" + System.currentTimeMillis() + "_" + file.fileName();
                    String savedPath = fileService.createFileImage(file, filepath);
                    if (savedPath == null) {
                        return Uni.createFrom().failure(new Exception("Failed to save image file"));
                    }
                    return productCommandService.update(pb.product.ProductCommand.UpdateProductRequest.newBuilder()
                            .setProductId(id)
                            .setMerchantId(data.getMerchantId())
                            .setCategoryId(data.getCategoryId())
                            .setName(data.getName())
                            .setDescription(data.getDescription())
                            .setPrice(data.getPrice())
                            .setCountInStock(data.getCountInStock())
                            .setBrand(data.getBrand())
                            .setWeight(data.getWeight())
                            .setSlugProduct(data.getSlugProduct())
                            .setImageProduct(savedPath)
                            .build());
                })
                .map(UpdateProductResponse::from));
    }

    @Override
    public Uni<FindByIdProductResponse> deleteProduct(int id) {
        return telemetryHelper.traceAndMetric("product.deleteProduct", () -> productCommandService.trashedProduct(pb.product.ProductCommon.FindByIdProductRequest.newBuilder()
                .setId(id)
                .build())
                .map(FindByIdProductResponse::from));
    }

    @Override
    public Uni<FindByIdProductResponse> restoreProduct(int id) {
        return telemetryHelper.traceAndMetric("product.restoreProduct", () -> productCommandService.restoreProduct(pb.product.ProductCommon.FindByIdProductRequest.newBuilder()
                .setId(id)
                .build())
                .map(FindByIdProductResponse::from));
    }

    @Override
    public Uni<SimpleStatusMessageResponse> deleteProductPermanent(int id) {
        return telemetryHelper.traceAndMetric("product.deleteProductPermanent", () -> productCommandService.deleteProductPermanent(pb.product.ProductCommon.FindByIdProductRequest.newBuilder()
                .setId(id)
                .build())
                .map(SimpleStatusMessageResponse::from));
    }

    @Override
    public Uni<SimpleStatusMessageResponse> restoreAllProducts() {
        return telemetryHelper.traceAndMetric("product.restoreAllProducts", () -> productCommandService.restoreAllProduct(com.google.protobuf.Empty.getDefaultInstance())
                .map(SimpleStatusMessageResponse::from));
    }

    @Override
    public Uni<SimpleStatusMessageResponse> deleteAllProductsPermanent() {
        return telemetryHelper.traceAndMetric("product.deleteAllProductsPermanent", () -> productCommandService.deleteAllProductPermanent(com.google.protobuf.Empty.getDefaultInstance())
                .map(SimpleStatusMessageResponse::from));
    }
}
