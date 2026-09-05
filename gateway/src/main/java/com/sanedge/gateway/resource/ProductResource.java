package com.sanedge.gateway.resource;

import org.eclipse.microprofile.graphql.DefaultValue;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Name;
import org.eclipse.microprofile.graphql.Query;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import com.sanedge.gateway.dto.ProductDto.CreateProductRequest;
import com.sanedge.gateway.dto.ProductDto.CreateProductResponse;
import com.sanedge.gateway.dto.ProductDto.FindAllProductResponse;
import com.sanedge.gateway.dto.ProductDto.FindByIdProductResponse;
import com.sanedge.gateway.dto.ProductDto.SimpleStatusMessageResponse;
import com.sanedge.gateway.dto.ProductDto.UpdateProductRequest;
import com.sanedge.gateway.dto.ProductDto.UpdateProductResponse;
import com.sanedge.gateway.service.ProductService;

import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@GraphQLApi
@Singleton
public class ProductResource {

        @Inject
        ProductService productService;

        @Query("listProducts")
        @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER" })
        public Uni<FindAllProductResponse> listProducts(
                        @Name("page") @DefaultValue("1") int page,
                        @Name("size") @DefaultValue("20") int size,
                        @Name("search") String search) {
                return productService.listProducts(page, size, search);
        }

        @Query("listActiveProducts")
        @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER" })
        public Uni<FindAllProductResponse> listActiveProducts(
                        @Name("page") @DefaultValue("1") int page,
                        @Name("size") @DefaultValue("20") int size,
                        @Name("search") String search) {
                return productService.listActiveProducts(page, size, search);
        }

        @Query("listTrashedProducts")
        @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
        public Uni<FindAllProductResponse> listTrashedProducts(
                        @Name("page") @DefaultValue("1") int page,
                        @Name("size") @DefaultValue("20") int size,
                        @Name("search") String search) {
                return productService.listTrashedProducts(page, size, search);
        }

        @Query("getProduct")
        @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER" })
        public Uni<FindByIdProductResponse> getProduct(@Name("id") int id) {
                return productService.getProduct(id);
        }

        @Mutation("createProduct")
        @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
        public Uni<CreateProductResponse> createProduct(@Name("body") CreateProductRequest body) {
                return productService.createProduct(body);
        }

        @Mutation("updateProduct")
        @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
        public Uni<UpdateProductResponse> updateProduct(@Name("id") int id, @Name("body") UpdateProductRequest body) {
                return productService.updateProduct(id, body);
        }

        @Mutation("uploadProduct")
        @Description("Upload product image")
        @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
        public Uni<UpdateProductResponse> uploadProduct(
                        @Name("id") int id,
                        @Name("file") FileUpload file) {
                return productService.uploadProduct(id, file);
        }

        @Mutation("deleteProduct")
        @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
        public Uni<FindByIdProductResponse> deleteProduct(@Name("id") int id) {
                return productService.deleteProduct(id);
        }

        @Mutation("restoreProduct")
        @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
        public Uni<FindByIdProductResponse> restoreProduct(@Name("id") int id) {
                return productService.restoreProduct(id);
        }

        @Mutation("deleteProductPermanent")
        @RolesAllowed({ "ROLE_ADMIN" })
        public Uni<SimpleStatusMessageResponse> deleteProductPermanent(@Name("id") int id) {
                return productService.deleteProductPermanent(id);
        }

        @Mutation("restoreAllProducts")
        @RolesAllowed({ "ROLE_ADMIN" })
        public Uni<SimpleStatusMessageResponse> restoreAllProducts() {
                return productService.restoreAllProducts();
        }

        @Mutation("deleteAllProductsPermanent")
        @RolesAllowed({ "ROLE_ADMIN" })
        public Uni<SimpleStatusMessageResponse> deleteAllProductsPermanent() {
                return productService.deleteAllProductsPermanent();
        }
}
