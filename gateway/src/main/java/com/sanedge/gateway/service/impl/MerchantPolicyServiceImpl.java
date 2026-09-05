package com.sanedge.gateway.service.impl;

import com.sanedge.gateway.dto.MerchantPolicyDto.CreateMerchantPolicyRequest;
import com.sanedge.gateway.dto.MerchantPolicyDto.CreateMerchantPolicyResponse;
import com.sanedge.gateway.dto.MerchantPolicyDto.FindAllMerchantPolicyResponse;
import com.sanedge.gateway.dto.MerchantPolicyDto.FindByIdMerchantPolicyResponse;
import com.sanedge.gateway.dto.MerchantPolicyDto.SimpleStatusMessageResponse;
import com.sanedge.gateway.dto.MerchantPolicyDto.UpdateMerchantPolicyRequest;
import com.sanedge.gateway.dto.MerchantPolicyDto.UpdateMerchantPolicyResponse;
import com.sanedge.gateway.service.MerchantPolicyService;
import com.sanedge.gateway.telemetry.TelemetryHelper;
import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class MerchantPolicyServiceImpl implements MerchantPolicyService {

    @Inject
    TelemetryHelper telemetryHelper;

    @GrpcClient("merchant")
    pb.merchant_policy.MutinyMerchantPolicyQueryServiceGrpc.MutinyMerchantPolicyQueryServiceStub merchantPolicyQueryService;

    @GrpcClient("merchant")
    pb.merchant_policy.MutinyMerchantPolicyCommandServiceGrpc.MutinyMerchantPolicyCommandServiceStub merchantPolicyCommandService;

    @Override
    public Uni<FindAllMerchantPolicyResponse> listMerchantPolicies(int page, int size, String search) {
        return telemetryHelper.traceAndMetric("merchantPolicy.listMerchantPolicies", () -> merchantPolicyQueryService.findAll(pb.merchant.MerchantQuery.FindAllMerchantRequest.newBuilder()
                .setPage(page)
                .setPageSize(size)
                .setSearch(search == null ? "" : search)
                .build())
                .map(FindAllMerchantPolicyResponse::from));
    }

    @Override
    public Uni<FindAllMerchantPolicyResponse> listActiveMerchantPolicies(int page, int size, String search) {
        return telemetryHelper.traceAndMetric("merchantPolicy.listActiveMerchantPolicies", () -> merchantPolicyQueryService.findByActive(pb.merchant.MerchantQuery.FindAllMerchantRequest.newBuilder()
                .setPage(page)
                .setPageSize(size)
                .setSearch(search == null ? "" : search)
                .build())
                .map(FindAllMerchantPolicyResponse::from));
    }

    @Override
    public Uni<FindAllMerchantPolicyResponse> listTrashedMerchantPolicies(int page, int size, String search) {
        return telemetryHelper.traceAndMetric("merchantPolicy.listTrashedMerchantPolicies", () -> merchantPolicyQueryService.findByTrashed(pb.merchant.MerchantQuery.FindAllMerchantRequest.newBuilder()
                .setPage(page)
                .setPageSize(size)
                .setSearch(search == null ? "" : search)
                .build())
                .map(FindAllMerchantPolicyResponse::from));
    }

    @Override
    public Uni<FindByIdMerchantPolicyResponse> getMerchantPolicy(int id) {
        return telemetryHelper.traceAndMetric("merchantPolicy.getMerchantPolicy", () -> merchantPolicyQueryService.findById(pb.merchant_policy.MerchantPolicyCommon.FindByIdMerchantPoliciesRequest.newBuilder()
                .setId(id)
                .build())
                .map(FindByIdMerchantPolicyResponse::from));
    }

    @Override
    public Uni<CreateMerchantPolicyResponse> createMerchantPolicy(CreateMerchantPolicyRequest body) {
        return telemetryHelper.traceAndMetric("merchantPolicy.createMerchantPolicy", () -> merchantPolicyCommandService.create(pb.merchant_policy.MerchantPolicyCommand.CreateMerchantPoliciesRequest.newBuilder()
                .setMerchantId(body.merchantId())
                .setPolicyType(body.policyType() == null ? "" : body.policyType())
                .setTitle(body.title() == null ? "" : body.title())
                .setDescription(body.description() == null ? "" : body.description())
                .build())
                .map(CreateMerchantPolicyResponse::from));
    }

    @Override
    public Uni<UpdateMerchantPolicyResponse> updateMerchantPolicy(int id, UpdateMerchantPolicyRequest body) {
        return telemetryHelper.traceAndMetric("merchantPolicy.updateMerchantPolicy", () -> merchantPolicyCommandService.update(pb.merchant_policy.MerchantPolicyCommand.UpdateMerchantPoliciesRequest.newBuilder()
                .setMerchantPolicyId(id)
                .setPolicyType(body.policyType() == null ? "" : body.policyType())
                .setTitle(body.title() == null ? "" : body.title())
                .setDescription(body.description() == null ? "" : body.description())
                .build())
                .map(UpdateMerchantPolicyResponse::from));
    }

    @Override
    public Uni<FindByIdMerchantPolicyResponse> deleteMerchantPolicy(int id) {
        return telemetryHelper.traceAndMetric("merchantPolicy.deleteMerchantPolicy", () -> merchantPolicyCommandService.trashedMerchantPolicies(pb.merchant_policy.MerchantPolicyCommon.FindByIdMerchantPoliciesRequest.newBuilder()
                .setId(id)
                .build())
                .map(FindByIdMerchantPolicyResponse::from));
    }

    @Override
    public Uni<FindByIdMerchantPolicyResponse> restoreMerchantPolicy(int id) {
        return telemetryHelper.traceAndMetric("merchantPolicy.restoreMerchantPolicy", () -> merchantPolicyCommandService.restoreMerchantPolicies(pb.merchant_policy.MerchantPolicyCommon.FindByIdMerchantPoliciesRequest.newBuilder()
                .setId(id)
                .build())
                .map(FindByIdMerchantPolicyResponse::from));
    }

    @Override
    public Uni<SimpleStatusMessageResponse> deleteMerchantPolicyPermanent(int id) {
        return telemetryHelper.traceAndMetric("merchantPolicy.deleteMerchantPolicyPermanent", () -> merchantPolicyCommandService.deleteMerchantPoliciesPermanent(pb.merchant_policy.MerchantPolicyCommon.FindByIdMerchantPoliciesRequest.newBuilder()
                .setId(id)
                .build())
                .map(proto -> new SimpleStatusMessageResponse(proto.getStatus(), proto.getMessage())));
    }

    @Override
    public Uni<SimpleStatusMessageResponse> restoreAllMerchantPolicies() {
        return telemetryHelper.traceAndMetric("merchantPolicy.restoreAllMerchantPolicies", () -> merchantPolicyCommandService.restoreAllMerchantPolicies(com.google.protobuf.Empty.getDefaultInstance())
                .map(proto -> new SimpleStatusMessageResponse(proto.getStatus(), proto.getMessage())));
    }

    @Override
    public Uni<SimpleStatusMessageResponse> deleteAllMerchantPoliciesPermanent() {
        return telemetryHelper.traceAndMetric("merchantPolicy.deleteAllMerchantPoliciesPermanent", () -> merchantPolicyCommandService.deleteAllMerchantPoliciesPermanent(com.google.protobuf.Empty.getDefaultInstance())
                .map(proto -> new SimpleStatusMessageResponse(proto.getStatus(), proto.getMessage())));
    }
}
