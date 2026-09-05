package com.sanedge.gateway.service.impl;

import com.sanedge.gateway.dto.MerchantBusinessDto.CreateMerchantBusinessRequest;
import com.sanedge.gateway.dto.MerchantBusinessDto.CreateMerchantBusinessResponse;
import com.sanedge.gateway.dto.MerchantBusinessDto.FindAllMerchantBusinessResponse;
import com.sanedge.gateway.dto.MerchantBusinessDto.FindByIdMerchantBusinessResponse;
import com.sanedge.gateway.dto.MerchantBusinessDto.SimpleStatusMessageResponse;
import com.sanedge.gateway.dto.MerchantBusinessDto.UpdateMerchantBusinessRequest;
import com.sanedge.gateway.dto.MerchantBusinessDto.UpdateMerchantBusinessResponse;
import com.sanedge.gateway.service.MerchantBusinessService;
import com.sanedge.gateway.telemetry.TelemetryHelper;
import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class MerchantBusinessServiceImpl implements MerchantBusinessService {

    @Inject
    TelemetryHelper telemetryHelper;

    @GrpcClient("merchant")
    pb.merchant_business.MutinyMerchantBusinessQueryServiceGrpc.MutinyMerchantBusinessQueryServiceStub merchantBusinessQueryService;

    @GrpcClient("merchant")
    pb.merchant_business.MutinyMerchantBusinessCommandServiceGrpc.MutinyMerchantBusinessCommandServiceStub merchantBusinessCommandService;

    @Override
    public Uni<FindAllMerchantBusinessResponse> listMerchantBusinesses(int page, int size, String search) {
        return telemetryHelper.traceAndMetric("merchantBusiness.listMerchantBusinesses", () -> merchantBusinessQueryService.findAll(pb.merchant.MerchantQuery.FindAllMerchantRequest.newBuilder()
                .setPage(page)
                .setPageSize(size)
                .setSearch(search == null ? "" : search)
                .build())
                .map(FindAllMerchantBusinessResponse::from));
    }

    @Override
    public Uni<FindAllMerchantBusinessResponse> listActiveMerchantBusinesses(int page, int size, String search) {
        return telemetryHelper.traceAndMetric("merchantBusiness.listActiveMerchantBusinesses", () -> merchantBusinessQueryService.findByActive(pb.merchant.MerchantQuery.FindAllMerchantRequest.newBuilder()
                .setPage(page)
                .setPageSize(size)
                .setSearch(search == null ? "" : search)
                .build())
                .map(FindAllMerchantBusinessResponse::from));
    }

    @Override
    public Uni<FindAllMerchantBusinessResponse> listTrashedMerchantBusinesses(int page, int size, String search) {
        return telemetryHelper.traceAndMetric("merchantBusiness.listTrashedMerchantBusinesses", () -> merchantBusinessQueryService.findByTrashed(pb.merchant.MerchantQuery.FindAllMerchantRequest.newBuilder()
                .setPage(page)
                .setPageSize(size)
                .setSearch(search == null ? "" : search)
                .build())
                .map(FindAllMerchantBusinessResponse::from));
    }

    @Override
    public Uni<FindByIdMerchantBusinessResponse> getMerchantBusiness(int id) {
        return telemetryHelper.traceAndMetric("merchantBusiness.getMerchantBusiness", () -> merchantBusinessQueryService.findById(pb.merchant_business.MerchantBusinessCommon.FindByIdMerchantBusinessRequest.newBuilder()
                .setId(id)
                .build())
                .map(FindByIdMerchantBusinessResponse::from));
    }

    @Override
    public Uni<CreateMerchantBusinessResponse> createMerchantBusiness(CreateMerchantBusinessRequest body) {
        return telemetryHelper.traceAndMetric("merchantBusiness.createMerchantBusiness", () -> merchantBusinessCommandService.create(pb.merchant_business.MerchantBusinessCommand.CreateMerchantBusinessRequest.newBuilder()
                .setMerchantId(body.merchantId())
                .setBusinessType(body.businessType() == null ? "" : body.businessType())
                .setTaxId(body.taxId() == null ? "" : body.taxId())
                .setEstablishedYear(body.establishedYear())
                .setNumberOfEmployees(body.numberOfEmployees())
                .setWebsiteUrl(body.websiteUrl() == null ? "" : body.websiteUrl())
                .build())
                .map(CreateMerchantBusinessResponse::from));
    }

    @Override
    public Uni<UpdateMerchantBusinessResponse> updateMerchantBusiness(int id, UpdateMerchantBusinessRequest body) {
        return telemetryHelper.traceAndMetric("merchantBusiness.updateMerchantBusiness", () -> merchantBusinessCommandService.update(pb.merchant_business.MerchantBusinessCommand.UpdateMerchantBusinessRequest.newBuilder()
                .setMerchantBusinessInfoId(id)
                .setBusinessType(body.businessType() == null ? "" : body.businessType())
                .setTaxId(body.taxId() == null ? "" : body.taxId())
                .setEstablishedYear(body.establishedYear())
                .setNumberOfEmployees(body.numberOfEmployees())
                .setWebsiteUrl(body.websiteUrl() == null ? "" : body.websiteUrl())
                .build())
                .map(UpdateMerchantBusinessResponse::from));
    }

    @Override
    public Uni<FindByIdMerchantBusinessResponse> deleteMerchantBusiness(int id) {
        return telemetryHelper.traceAndMetric("merchantBusiness.deleteMerchantBusiness", () -> merchantBusinessCommandService.trashedMerchantBusiness(pb.merchant_business.MerchantBusinessCommon.FindByIdMerchantBusinessRequest.newBuilder()
                .setId(id)
                .build())
                .map(FindByIdMerchantBusinessResponse::from));
    }

    @Override
    public Uni<FindByIdMerchantBusinessResponse> restoreMerchantBusiness(int id) {
        return telemetryHelper.traceAndMetric("merchantBusiness.restoreMerchantBusiness", () -> merchantBusinessCommandService.restoreMerchantBusiness(pb.merchant_business.MerchantBusinessCommon.FindByIdMerchantBusinessRequest.newBuilder()
                .setId(id)
                .build())
                .map(FindByIdMerchantBusinessResponse::from));
    }

    @Override
    public Uni<SimpleStatusMessageResponse> deleteMerchantBusinessPermanent(int id) {
        return telemetryHelper.traceAndMetric("merchantBusiness.deleteMerchantBusinessPermanent", () -> merchantBusinessCommandService.deleteMerchantBusinessPermanent(pb.merchant_business.MerchantBusinessCommon.FindByIdMerchantBusinessRequest.newBuilder()
                .setId(id)
                .build())
                .map(proto -> new SimpleStatusMessageResponse(proto.getStatus(), proto.getMessage())));
    }

    @Override
    public Uni<SimpleStatusMessageResponse> restoreAllMerchantBusinesses() {
        return telemetryHelper.traceAndMetric("merchantBusiness.restoreAllMerchantBusinesses", () -> merchantBusinessCommandService.restoreAllMerchantBusiness(com.google.protobuf.Empty.getDefaultInstance())
                .map(proto -> new SimpleStatusMessageResponse(proto.getStatus(), proto.getMessage())));
    }

    @Override
    public Uni<SimpleStatusMessageResponse> deleteAllMerchantBusinessesPermanent() {
        return telemetryHelper.traceAndMetric("merchantBusiness.deleteAllMerchantBusinessesPermanent", () -> merchantBusinessCommandService.deleteAllMerchantBusinessPermanent(com.google.protobuf.Empty.getDefaultInstance())
                .map(proto -> new SimpleStatusMessageResponse(proto.getStatus(), proto.getMessage())));
    }
}
