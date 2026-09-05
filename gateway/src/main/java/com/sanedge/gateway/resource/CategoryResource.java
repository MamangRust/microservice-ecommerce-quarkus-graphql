package com.sanedge.gateway.resource;

import org.eclipse.microprofile.graphql.DefaultValue;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Name;
import org.eclipse.microprofile.graphql.Query;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import com.sanedge.gateway.dto.CategoryDto.CategoriesMonthlyTotalPriceResponse;
import com.sanedge.gateway.dto.CategoryDto.CategoriesYearlyTotalPriceResponse;
import com.sanedge.gateway.dto.CategoryDto.CategoryMonthPriceResponse;
import com.sanedge.gateway.dto.CategoryDto.CategoryYearPriceResponse;
import com.sanedge.gateway.dto.CategoryDto.CreateCategoryRequest;
import com.sanedge.gateway.dto.CategoryDto.CreateCategoryResponse;
import com.sanedge.gateway.dto.CategoryDto.FindAllCategoryResponse;
import com.sanedge.gateway.dto.CategoryDto.FindByIdCategoryResponse;
import com.sanedge.gateway.dto.CategoryDto.SimpleStatusMessageResponse;
import com.sanedge.gateway.dto.CategoryDto.UpdateCategoryRequest;
import com.sanedge.gateway.dto.CategoryDto.UpdateCategoryResponse;
import com.sanedge.gateway.service.CategoryService;

import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@GraphQLApi
@Singleton
public class CategoryResource {

        @Inject
        CategoryService categoryService;

        @Query("listCategories")
        @Description("List all categories")
        @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER" })
        public Uni<FindAllCategoryResponse> listCategories(
                        @Name("page") @DefaultValue("1") int page,
                        @Name("size") @DefaultValue("20") int size,
                        @Name("search") String search) {
                return categoryService.listCategories(page, size, search);
        }

        @Query("listActiveCategories")
        @Description("List active categories")
        @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER" })
        public Uni<FindAllCategoryResponse> listActiveCategories(
                        @Name("page") @DefaultValue("1") int page,
                        @Name("size") @DefaultValue("20") int size,
                        @Name("search") String search) {
                return categoryService.listActiveCategories(page, size, search);
        }

        @Query("listTrashedCategories")
        @Description("List trashed categories")
        @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
        public Uni<FindAllCategoryResponse> listTrashedCategories(
                        @Name("page") @DefaultValue("1") int page,
                        @Name("size") @DefaultValue("20") int size,
                        @Name("search") String search) {
                return categoryService.listTrashedCategories(page, size, search);
        }

        @Query("getCategory")
        @Description("Get category by ID")
        @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF", "ROLE_USER" })
        public Uni<FindByIdCategoryResponse> getCategory(@Name("id") int id) {
                return categoryService.getCategory(id);
        }

        @Mutation("createCategory")
        @Description("Create a new category")
        @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
        public Uni<CreateCategoryResponse> createCategory(@Name("body") CreateCategoryRequest body) {
                return categoryService.createCategory(body);
        }

        @Mutation("updateCategory")
        @Description("Update category")
        @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
        public Uni<UpdateCategoryResponse> updateCategory(@Name("id") int id,
                        @Name("body") UpdateCategoryRequest body) {
                return categoryService.updateCategory(id, body);
        }

        @Mutation("uploadCategory")
        @Description("Upload category image")
        @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
        public Uni<UpdateCategoryResponse> uploadCategory(
                        @Name("id") int id,
                        @Name("file") FileUpload file) {
                return categoryService.uploadCategory(id, file);
        }

        @Mutation("deleteCategory")
        @Description("Soft-delete category")
        @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
        public Uni<FindByIdCategoryResponse> deleteCategory(@Name("id") int id) {
                return categoryService.deleteCategory(id);
        }

        @Mutation("restoreCategory")
        @Description("Restore soft-deleted category")
        @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
        public Uni<FindByIdCategoryResponse> restoreCategory(@Name("id") int id) {
                return categoryService.restoreCategory(id);
        }

        @Mutation("deleteCategoryPermanent")
        @Description("Permanently delete category")
        @RolesAllowed({ "ROLE_ADMIN" })
        public Uni<SimpleStatusMessageResponse> deleteCategoryPermanent(@Name("id") int id) {
                return categoryService.deleteCategoryPermanent(id);
        }

        @Mutation("restoreAllCategories")
        @Description("Restore all soft-deleted categories")
        @RolesAllowed({ "ROLE_ADMIN" })
        public Uni<SimpleStatusMessageResponse> restoreAllCategories() {
                return categoryService.restoreAllCategories();
        }

        @Mutation("deleteAllCategoriesPermanent")
        @Description("Permanently delete all soft-deleted categories")
        @RolesAllowed({ "ROLE_ADMIN" })
        public Uni<SimpleStatusMessageResponse> deleteAllCategoriesPermanent() {
                return categoryService.deleteAllCategoriesPermanent();
        }

        // STATS QUERIES
        @Query("getCategoryMonthTotalPrice")
        @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
        public Uni<CategoriesMonthlyTotalPriceResponse> getCategoryMonthTotalPrice(@Name("year") int year,
                        @Name("month") int month) {
                return categoryService.getCategoryMonthTotalPrice(year, month);
        }

        @Query("getCategoryYearTotalPrice")
        @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
        public Uni<CategoriesYearlyTotalPriceResponse> getCategoryYearTotalPrice(@Name("year") int year) {
                return categoryService.getCategoryYearTotalPrice(year);
        }

        @Query("getCategoryMonthTotalPriceByMerchant")
        @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
        public Uni<CategoriesMonthlyTotalPriceResponse> getCategoryMonthTotalPriceByMerchant(
                        @Name("merchantId") int merchantId, @Name("year") int year, @Name("month") int month) {
                return categoryService.getCategoryMonthTotalPriceByMerchant(merchantId, year, month);
        }

        @Query("getCategoryYearTotalPriceByMerchant")
        @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
        public Uni<CategoriesYearlyTotalPriceResponse> getCategoryYearTotalPriceByMerchant(
                        @Name("merchantId") int merchantId, @Name("year") int year) {
                return categoryService.getCategoryYearTotalPriceByMerchant(merchantId, year);
        }

        @Query("getCategoryMonthTotalPriceById")
        @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
        public Uni<CategoriesMonthlyTotalPriceResponse> getCategoryMonthTotalPriceById(
                        @Name("categoryId") int categoryId, @Name("year") int year, @Name("month") int month) {
                return categoryService.getCategoryMonthTotalPriceById(categoryId, year, month);
        }

        @Query("getCategoryYearTotalPriceById")
        @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
        public Uni<CategoriesYearlyTotalPriceResponse> getCategoryYearTotalPriceById(@Name("categoryId") int categoryId,
                        @Name("year") int year) {
                return categoryService.getCategoryYearTotalPriceById(categoryId, year);
        }

        @Query("getCategoryMonthPrice")
        @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
        public Uni<CategoryMonthPriceResponse> getCategoryMonthPrice(@Name("year") int year) {
                return categoryService.getCategoryMonthPrice(year);
        }

        @Query("getCategoryYearPrice")
        @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
        public Uni<CategoryYearPriceResponse> getCategoryYearPrice(@Name("year") int year) {
                return categoryService.getCategoryYearPrice(year);
        }

        @Query("getCategoryMonthPriceByMerchant")
        @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
        public Uni<CategoryMonthPriceResponse> getCategoryMonthPriceByMerchant(@Name("merchantId") int merchantId,
                        @Name("year") int year) {
                return categoryService.getCategoryMonthPriceByMerchant(merchantId, year);
        }

        @Query("getCategoryYearPriceByMerchant")
        @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
        public Uni<CategoryYearPriceResponse> getCategoryYearPriceByMerchant(@Name("merchantId") int merchantId,
                        @Name("year") int year) {
                return categoryService.getCategoryYearPriceByMerchant(merchantId, year);
        }

        @Query("getCategoryMonthPriceById")
        @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
        public Uni<CategoryMonthPriceResponse> getCategoryMonthPriceById(@Name("categoryId") int categoryId,
                        @Name("year") int year) {
                return categoryService.getCategoryMonthPriceById(categoryId, year);
        }

        @Query("getCategoryYearPriceById")
        @RolesAllowed({ "ROLE_ADMIN", "ROLE_STAFF" })
        public Uni<CategoryYearPriceResponse> getCategoryYearPriceById(@Name("categoryId") int categoryId,
                        @Name("year") int year) {
                return categoryService.getCategoryYearPriceById(categoryId, year);
        }
}
