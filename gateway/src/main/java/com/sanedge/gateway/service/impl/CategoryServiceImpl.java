package com.sanedge.gateway.service.impl;

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
import com.sanedge.gateway.service.FileService;
import com.sanedge.gateway.telemetry.TelemetryHelper;
import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.resteasy.reactive.multipart.FileUpload;

@ApplicationScoped
public class CategoryServiceImpl implements CategoryService {

    @Inject
    FileService fileService;

    @Inject
    TelemetryHelper telemetryHelper;

    @GrpcClient("category")
    pb.category.MutinyCategoryQueryServiceGrpc.MutinyCategoryQueryServiceStub categoryQueryService;

    @GrpcClient("category")
    pb.category.MutinyCategoryCommandServiceGrpc.MutinyCategoryCommandServiceStub categoryCommandService;

    @GrpcClient("statsreader")
    pb.category.stats.MutinyCategoryPriceServiceGrpc.MutinyCategoryPriceServiceStub categoryPriceService;

    @GrpcClient("statsreader")
    pb.category.stats.MutinyCategoryPriceByIdServiceGrpc.MutinyCategoryPriceByIdServiceStub categoryPriceByIdService;

    @GrpcClient("statsreader")
    pb.category.stats.MutinyCategoryPriceByMerchantGrpc.MutinyCategoryPriceByMerchantStub categoryPriceByMerchantService;

    @GrpcClient("statsreader")
    pb.category.stats.MutinyCategoryTotalPriceServiceGrpc.MutinyCategoryTotalPriceServiceStub categoryTotalPriceService;

    @GrpcClient("statsreader")
    pb.category.stats.MutinyCategoryTotalPriceByIdGrpc.MutinyCategoryTotalPriceByIdStub categoryTotalPriceByIdService;

    @GrpcClient("statsreader")
    pb.category.stats.MutinyCategoryTotalPriceByMerchantGrpc.MutinyCategoryTotalPriceByMerchantStub categoryTotalPriceByMerchantService;

    @Override
    public Uni<FindAllCategoryResponse> listCategories(int page, int size, String search) {
        return telemetryHelper.traceAndMetric("category.listCategories", () -> categoryQueryService.findAll(pb.category.CategoryQuery.FindAllCategoryRequest.newBuilder()
                .setPage(page)
                .setPageSize(size)
                .setSearch(search == null ? "" : search)
                .build())
                .map(FindAllCategoryResponse::from));
    }

    @Override
    public Uni<FindAllCategoryResponse> listActiveCategories(int page, int size, String search) {
        return telemetryHelper.traceAndMetric("category.listActiveCategories", () -> categoryQueryService.findByActive(pb.category.CategoryQuery.FindAllCategoryRequest.newBuilder()
                .setPage(page)
                .setPageSize(size)
                .setSearch(search == null ? "" : search)
                .build())
                .map(FindAllCategoryResponse::from));
    }

    @Override
    public Uni<FindAllCategoryResponse> listTrashedCategories(int page, int size, String search) {
        return telemetryHelper.traceAndMetric("category.listTrashedCategories", () -> categoryQueryService.findByTrashed(pb.category.CategoryQuery.FindAllCategoryRequest.newBuilder()
                .setPage(page)
                .setPageSize(size)
                .setSearch(search == null ? "" : search)
                .build())
                .map(FindAllCategoryResponse::from));
    }

    @Override
    public Uni<FindByIdCategoryResponse> getCategory(int id) {
        return telemetryHelper.traceAndMetric("category.getCategory", () -> categoryQueryService.findById(pb.category.CategoryCommon.FindByIdCategoryRequest.newBuilder()
                .setId(id)
                .build())
                .map(FindByIdCategoryResponse::from));
    }

    @Override
    public Uni<CreateCategoryResponse> createCategory(CreateCategoryRequest body) {
        return telemetryHelper.traceAndMetric("category.createCategory", () -> categoryCommandService.create(pb.category.CategoryCommand.CreateCategoryRequest.newBuilder()
                .setName(body.name() == null ? "" : body.name())
                .setDescription(body.description() == null ? "" : body.description())
                .setSlugCategory(body.slugCategory() == null ? "" : body.slugCategory())
                .setImageCategory(body.imageCategory() == null ? "" : body.imageCategory())
                .build())
                .map(CreateCategoryResponse::from));
    }

    @Override
    public Uni<UpdateCategoryResponse> updateCategory(int id, UpdateCategoryRequest body) {
        return telemetryHelper.traceAndMetric("category.updateCategory", () -> categoryCommandService.update(pb.category.CategoryCommand.UpdateCategoryRequest.newBuilder()
                .setCategoryId(id)
                .setName(body.name() == null ? "" : body.name())
                .setDescription(body.description() == null ? "" : body.description())
                .setSlugCategory(body.slugCategory() == null ? "" : body.slugCategory())
                .setImageCategory(body.imageCategory() == null ? "" : body.imageCategory())
                .build())
                .map(UpdateCategoryResponse::from));
    }

    @Override
    public Uni<UpdateCategoryResponse> uploadCategory(int id, FileUpload file) {
        return telemetryHelper.traceAndMetric("category.uploadCategory", () -> categoryQueryService.findById(pb.category.CategoryCommon.FindByIdCategoryRequest.newBuilder()
                .setId(id)
                .build())
                .flatMap(res -> {
                    if (!res.hasData()) {
                        return Uni.createFrom().failure(new Exception("Category not found"));
                    }
                    pb.category.CategoryCommon.CategoryResponse data = res.getData();
                    String filepath = "uploads/categories/" + System.currentTimeMillis() + "_" + file.fileName();
                    String savedPath = fileService.createFileImage(file, filepath);
                    if (savedPath == null) {
                        return Uni.createFrom().failure(new Exception("Failed to save image file"));
                    }
                    return categoryCommandService.update(pb.category.CategoryCommand.UpdateCategoryRequest.newBuilder()
                            .setCategoryId(id)
                            .setName(data.getName())
                            .setDescription(data.getDescription())
                            .setSlugCategory(data.getSlugCategory())
                            .setImageCategory(savedPath)
                            .build());
                })
                .map(UpdateCategoryResponse::from));
    }

    @Override
    public Uni<FindByIdCategoryResponse> deleteCategory(int id) {
        return telemetryHelper.traceAndMetric("category.deleteCategory", () -> categoryCommandService.trashedCategory(pb.category.CategoryCommon.FindByIdCategoryRequest.newBuilder()
                .setId(id)
                .build())
                .map(FindByIdCategoryResponse::from));
    }

    @Override
    public Uni<FindByIdCategoryResponse> restoreCategory(int id) {
        return telemetryHelper.traceAndMetric("category.restoreCategory", () -> categoryCommandService.restoreCategory(pb.category.CategoryCommon.FindByIdCategoryRequest.newBuilder()
                .setId(id)
                .build())
                .map(FindByIdCategoryResponse::from));
    }

    @Override
    public Uni<SimpleStatusMessageResponse> deleteCategoryPermanent(int id) {
        return telemetryHelper.traceAndMetric("category.deleteCategoryPermanent", () -> categoryCommandService.deleteCategoryPermanent(pb.category.CategoryCommon.FindByIdCategoryRequest.newBuilder()
                .setId(id)
                .build())
                .map(SimpleStatusMessageResponse::from));
    }

    @Override
    public Uni<SimpleStatusMessageResponse> restoreAllCategories() {
        return telemetryHelper.traceAndMetric("category.restoreAllCategories", () -> categoryCommandService.restoreAllCategory(com.google.protobuf.Empty.getDefaultInstance())
                .map(SimpleStatusMessageResponse::from));
    }

    @Override
    public Uni<SimpleStatusMessageResponse> deleteAllCategoriesPermanent() {
        return telemetryHelper.traceAndMetric("category.deleteAllCategoriesPermanent", () -> categoryCommandService.deleteAllCategoryPermanent(com.google.protobuf.Empty.getDefaultInstance())
                .map(SimpleStatusMessageResponse::from));
    }

    @Override
    public Uni<CategoriesMonthlyTotalPriceResponse> getCategoryMonthTotalPrice(int year, int month) {
        return telemetryHelper.traceAndMetric("category.getCategoryMonthTotalPrice", () -> categoryTotalPriceService.findMonthlyTotalPrices(pb.category.CategoryCommon.FindYearMonthTotalPrices.newBuilder()
                .setYear(year)
                .setMonth(month)
                .build())
                .map(CategoriesMonthlyTotalPriceResponse::from));
    }

    @Override
    public Uni<CategoriesYearlyTotalPriceResponse> getCategoryYearTotalPrice(int year) {
        return telemetryHelper.traceAndMetric("category.getCategoryYearTotalPrice", () -> categoryTotalPriceService.findYearlyTotalPrices(pb.category.CategoryCommon.FindYearTotalPrices.newBuilder()
                .setYear(year)
                .build())
                .map(CategoriesYearlyTotalPriceResponse::from));
    }

    @Override
    public Uni<CategoriesMonthlyTotalPriceResponse> getCategoryMonthTotalPriceByMerchant(int merchantId, int year, int month) {
        return telemetryHelper.traceAndMetric("category.getCategoryMonthTotalPriceByMerchant", () -> categoryTotalPriceByMerchantService.findMonthlyTotalPricesByMerchant(pb.category.CategoryCommon.FindYearMonthTotalPriceByMerchant.newBuilder()
                .setMerchantId(merchantId)
                .setYear(year)
                .setMonth(month)
                .build())
                .map(CategoriesMonthlyTotalPriceResponse::from));
    }

    @Override
    public Uni<CategoriesYearlyTotalPriceResponse> getCategoryYearTotalPriceByMerchant(int merchantId, int year) {
        return telemetryHelper.traceAndMetric("category.getCategoryYearTotalPriceByMerchant", () -> categoryTotalPriceByMerchantService.findYearlyTotalPricesByMerchant(pb.category.CategoryCommon.FindYearTotalPriceByMerchant.newBuilder()
                .setMerchantId(merchantId)
                .setYear(year)
                .build())
                .map(CategoriesYearlyTotalPriceResponse::from));
    }

    @Override
    public Uni<CategoriesMonthlyTotalPriceResponse> getCategoryMonthTotalPriceById(int categoryId, int year, int month) {
        return telemetryHelper.traceAndMetric("category.getCategoryMonthTotalPriceById", () -> categoryTotalPriceByIdService.findMonthlyTotalPricesById(pb.category.CategoryCommon.FindYearMonthTotalPriceById.newBuilder()
                .setCategoryId(categoryId)
                .setYear(year)
                .setMonth(month)
                .build())
                .map(CategoriesMonthlyTotalPriceResponse::from));
    }

    @Override
    public Uni<CategoriesYearlyTotalPriceResponse> getCategoryYearTotalPriceById(int categoryId, int year) {
        return telemetryHelper.traceAndMetric("category.getCategoryYearTotalPriceById", () -> categoryTotalPriceByIdService.findYearlyTotalPricesById(pb.category.CategoryCommon.FindYearTotalPriceById.newBuilder()
                .setCategoryId(categoryId)
                .setYear(year)
                .build())
                .map(CategoriesYearlyTotalPriceResponse::from));
    }

    @Override
    public Uni<CategoryMonthPriceResponse> getCategoryMonthPrice(int year) {
        return telemetryHelper.traceAndMetric("category.getCategoryMonthPrice", () -> categoryPriceService.findMonthPrice(pb.category.CategoryCommon.FindYearCategory.newBuilder()
                .setYear(year)
                .build())
                .map(CategoryMonthPriceResponse::from));
    }

    @Override
    public Uni<CategoryYearPriceResponse> getCategoryYearPrice(int year) {
        return telemetryHelper.traceAndMetric("category.getCategoryYearPrice", () -> categoryPriceService.findYearPrice(pb.category.CategoryCommon.FindYearCategory.newBuilder()
                .setYear(year)
                .build())
                .map(CategoryYearPriceResponse::from));
    }

    @Override
    public Uni<CategoryMonthPriceResponse> getCategoryMonthPriceByMerchant(int merchantId, int year) {
        return telemetryHelper.traceAndMetric("category.getCategoryMonthPriceByMerchant", () -> categoryPriceByMerchantService.findMonthPriceByMerchant(pb.category.CategoryCommon.FindYearCategoryByMerchant.newBuilder()
                .setMerchantId(merchantId)
                .setYear(year)
                .build())
                .map(CategoryMonthPriceResponse::from));
    }

    @Override
    public Uni<CategoryYearPriceResponse> getCategoryYearPriceByMerchant(int merchantId, int year) {
        return telemetryHelper.traceAndMetric("category.getCategoryYearPriceByMerchant", () -> categoryPriceByMerchantService.findYearPriceByMerchant(pb.category.CategoryCommon.FindYearCategoryByMerchant.newBuilder()
                .setMerchantId(merchantId)
                .setYear(year)
                .build())
                .map(CategoryYearPriceResponse::from));
    }

    @Override
    public Uni<CategoryMonthPriceResponse> getCategoryMonthPriceById(int categoryId, int year) {
        return telemetryHelper.traceAndMetric("category.getCategoryMonthPriceById", () -> categoryPriceByIdService.findMonthPriceById(pb.category.CategoryCommon.FindYearCategoryById.newBuilder()
                .setCategoryId(categoryId)
                .setYear(year)
                .build())
                .map(CategoryMonthPriceResponse::from));
    }

    @Override
    public Uni<CategoryYearPriceResponse> getCategoryYearPriceById(int categoryId, int year) {
        return telemetryHelper.traceAndMetric("category.getCategoryYearPriceById", () -> categoryPriceByIdService.findYearPriceById(pb.category.CategoryCommon.FindYearCategoryById.newBuilder()
                .setCategoryId(categoryId)
                .setYear(year)
                .build())
                .map(CategoryYearPriceResponse::from));
    }
}
