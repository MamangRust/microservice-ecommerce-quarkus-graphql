package com.sanedge.gateway.service.impl;

import com.sanedge.gateway.dto.MerchantDocumentDto.CreateMerchantDocumentBody;
import com.sanedge.gateway.dto.MerchantDocumentDto.CreateMerchantDocumentResponse;
import com.sanedge.gateway.dto.MerchantDocumentDto.FindAllMerchantDocumentsResponse;
import com.sanedge.gateway.dto.MerchantDocumentDto.FindByIdMerchantDocumentResponse;
import com.sanedge.gateway.dto.MerchantDocumentDto.SimpleStatusMessageResponse;
import com.sanedge.gateway.dto.MerchantDocumentDto.TrashedMerchantDocumentResponse;
import com.sanedge.gateway.dto.MerchantDocumentDto.UpdateMerchantDocumentBody;
import com.sanedge.gateway.dto.MerchantDocumentDto.UpdateMerchantDocumentResponse;
import com.sanedge.gateway.dto.MerchantDocumentDto.UpdateMerchantDocumentStatusBody;
import com.sanedge.gateway.service.MerchantDocumentService;
import com.sanedge.gateway.telemetry.TelemetryHelper;
import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class MerchantDocumentServiceImpl implements MerchantDocumentService {

    @Inject
    TelemetryHelper telemetryHelper;

    @GrpcClient("merchant")
    pb.merchant_document.MutinyMerchantDocumentQueryServiceGrpc.MutinyMerchantDocumentQueryServiceStub merchantDocumentQueryService;

    @GrpcClient("merchant")
    pb.merchant_document.MutinyMerchantDocumentCommandServiceGrpc.MutinyMerchantDocumentCommandServiceStub merchantDocumentCommandService;

    @Override
    public Uni<FindAllMerchantDocumentsResponse> listMerchantDocuments(int page, int size, String search) {
        return telemetryHelper.traceAndMetric("merchantDocument.listMerchantDocuments", () -> merchantDocumentQueryService.findAll(pb.merchant_document.MerchantDocumentQuery.FindAllMerchantDocumentsRequest.newBuilder()
                .setPage(page)
                .setPageSize(size)
                .setSearch(search == null ? "" : search)
                .build())
                .map(FindAllMerchantDocumentsResponse::from));
    }

    @Override
    public Uni<FindAllMerchantDocumentsResponse> listActiveMerchantDocuments(int page, int size, String search) {
        return telemetryHelper.traceAndMetric("merchantDocument.listActiveMerchantDocuments", () -> merchantDocumentQueryService.findAllActive(pb.merchant_document.MerchantDocumentQuery.FindAllMerchantDocumentsRequest.newBuilder()
                .setPage(page)
                .setPageSize(size)
                .setSearch(search == null ? "" : search)
                .build())
                .map(FindAllMerchantDocumentsResponse::from));
    }

    @Override
    public Uni<FindAllMerchantDocumentsResponse> listTrashedMerchantDocuments(int page, int size, String search) {
        return telemetryHelper.traceAndMetric("merchantDocument.listTrashedMerchantDocuments", () -> merchantDocumentQueryService.findAllTrashed(pb.merchant_document.MerchantDocumentQuery.FindAllMerchantDocumentsRequest.newBuilder()
                .setPage(page)
                .setPageSize(size)
                .setSearch(search == null ? "" : search)
                .build())
                .map(FindAllMerchantDocumentsResponse::from));
    }

    @Override
    public Uni<FindByIdMerchantDocumentResponse> getMerchantDocument(int id) {
        return telemetryHelper.traceAndMetric("merchantDocument.getMerchantDocument", () -> merchantDocumentQueryService.findById(pb.merchant_document.MerchantDocumentQuery.FindMerchantDocumentByIdRequest.newBuilder()
                .setDocumentId(id)
                .build())
                .map(FindByIdMerchantDocumentResponse::from));
    }

    @Override
    public Uni<CreateMerchantDocumentResponse> createMerchantDocument(CreateMerchantDocumentBody body) {
        return telemetryHelper.traceAndMetric("merchantDocument.createMerchantDocument", () -> merchantDocumentCommandService.create(pb.merchant_document.MerchantDocumentCommand.CreateMerchantDocumentRequest.newBuilder()
                .setMerchantId(body.merchantId())
                .setDocumentType(body.documentType() == null ? "" : body.documentType())
                .setDocumentUrl(body.documentUrl() == null ? "" : body.documentUrl())
                .build())
                .map(CreateMerchantDocumentResponse::from));
    }

    @Override
    public Uni<UpdateMerchantDocumentResponse> updateMerchantDocument(int id, UpdateMerchantDocumentBody body) {
        return telemetryHelper.traceAndMetric("merchantDocument.updateMerchantDocument", () -> merchantDocumentCommandService.update(pb.merchant_document.MerchantDocumentCommand.UpdateMerchantDocumentRequest.newBuilder()
                .setDocumentId(id)
                .setMerchantId(body.merchantId())
                .setDocumentType(body.documentType() == null ? "" : body.documentType())
                .setDocumentUrl(body.documentUrl() == null ? "" : body.documentUrl())
                .setNote(body.note() == null ? "" : body.note())
                .setStatus(body.status() == null ? "" : body.status())
                .build())
                .map(UpdateMerchantDocumentResponse::from));
    }

    @Override
    public Uni<UpdateMerchantDocumentResponse> updateMerchantDocumentStatus(int id, UpdateMerchantDocumentStatusBody body) {
        return telemetryHelper.traceAndMetric("merchantDocument.updateMerchantDocumentStatus", () -> merchantDocumentCommandService.updateStatus(pb.merchant_document.MerchantDocumentCommand.UpdateMerchantDocumentStatusRequest.newBuilder()
                .setDocumentId(id)
                .setMerchantId(body.merchantId())
                .setNote(body.note() == null ? "" : body.note())
                .setStatus(body.status() == null ? "" : body.status())
                .build())
                .map(UpdateMerchantDocumentResponse::from));
    }

    @Override
    public Uni<TrashedMerchantDocumentResponse> trashMerchantDocument(int id) {
        return telemetryHelper.traceAndMetric("merchantDocument.trashMerchantDocument", () -> merchantDocumentCommandService.trashed(pb.merchant_document.MerchantDocumentCommand.TrashedMerchantDocumentRequest.newBuilder()
                .setDocumentId(id)
                .build())
                .map(TrashedMerchantDocumentResponse::from));
    }

    @Override
    public Uni<CreateMerchantDocumentResponse> restoreMerchantDocument(int id) {
        return telemetryHelper.traceAndMetric("merchantDocument.restoreMerchantDocument", () -> merchantDocumentCommandService.restore(pb.merchant_document.MerchantDocumentCommand.RestoreMerchantDocumentRequest.newBuilder()
                .setDocumentId(id)
                .build())
                .map(CreateMerchantDocumentResponse::from));
    }

    @Override
    public Uni<SimpleStatusMessageResponse> deleteMerchantDocumentPermanent(int id) {
        return telemetryHelper.traceAndMetric("merchantDocument.deleteMerchantDocumentPermanent", () -> merchantDocumentCommandService.deletePermanent(pb.merchant_document.MerchantDocumentCommand.DeleteMerchantDocumentPermanentRequest.newBuilder()
                .setDocumentId(id)
                .build())
                .map(SimpleStatusMessageResponse::from));
    }

    @Override
    public Uni<SimpleStatusMessageResponse> restoreAllMerchantDocuments() {
        return telemetryHelper.traceAndMetric("merchantDocument.restoreAllMerchantDocuments", () -> merchantDocumentCommandService.restoreAll(com.google.protobuf.Empty.getDefaultInstance())
                .map(SimpleStatusMessageResponse::from));
    }

    @Override
    public Uni<SimpleStatusMessageResponse> deleteAllMerchantDocuments() {
        return telemetryHelper.traceAndMetric("merchantDocument.deleteAllMerchantDocuments", () -> merchantDocumentCommandService.deleteAllPermanent(com.google.protobuf.Empty.getDefaultInstance())
                .map(SimpleStatusMessageResponse::from));
    }
}
