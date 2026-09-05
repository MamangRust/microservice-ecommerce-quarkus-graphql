package com.sanedge.gateway.service;

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
import io.smallrye.mutiny.Uni;
import org.jboss.resteasy.reactive.multipart.FileUpload;

public interface CategoryService {
    Uni<FindAllCategoryResponse> listCategories(int page, int size, String search);
    Uni<FindAllCategoryResponse> listActiveCategories(int page, int size, String search);
    Uni<FindAllCategoryResponse> listTrashedCategories(int page, int size, String search);
    Uni<FindByIdCategoryResponse> getCategory(int id);
    Uni<CreateCategoryResponse> createCategory(CreateCategoryRequest body);
    Uni<UpdateCategoryResponse> updateCategory(int id, UpdateCategoryRequest body);
    Uni<UpdateCategoryResponse> uploadCategory(int id, FileUpload file);
    Uni<FindByIdCategoryResponse> deleteCategory(int id);
    Uni<FindByIdCategoryResponse> restoreCategory(int id);
    Uni<SimpleStatusMessageResponse> deleteCategoryPermanent(int id);
    Uni<SimpleStatusMessageResponse> restoreAllCategories();
    Uni<SimpleStatusMessageResponse> deleteAllCategoriesPermanent();
    
    // Stats
    Uni<CategoriesMonthlyTotalPriceResponse> getCategoryMonthTotalPrice(int year, int month);
    Uni<CategoriesYearlyTotalPriceResponse> getCategoryYearTotalPrice(int year);
    Uni<CategoriesMonthlyTotalPriceResponse> getCategoryMonthTotalPriceByMerchant(int merchantId, int year, int month);
    Uni<CategoriesYearlyTotalPriceResponse> getCategoryYearTotalPriceByMerchant(int merchantId, int year);
    Uni<CategoriesMonthlyTotalPriceResponse> getCategoryMonthTotalPriceById(int categoryId, int year, int month);
    Uni<CategoriesYearlyTotalPriceResponse> getCategoryYearTotalPriceById(int categoryId, int year);
    Uni<CategoryMonthPriceResponse> getCategoryMonthPrice(int year);
    Uni<CategoryYearPriceResponse> getCategoryYearPrice(int year);
    Uni<CategoryMonthPriceResponse> getCategoryMonthPriceByMerchant(int merchantId, int year);
    Uni<CategoryYearPriceResponse> getCategoryYearPriceByMerchant(int merchantId, int year);
    Uni<CategoryMonthPriceResponse> getCategoryMonthPriceById(int categoryId, int year);
    Uni<CategoryYearPriceResponse> getCategoryYearPriceById(int categoryId, int year);
}
