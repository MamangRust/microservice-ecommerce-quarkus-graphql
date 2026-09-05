package com.sanedge.gateway.service;

import com.sanedge.gateway.dto.MerchantPolicyDto.CreateMerchantPolicyRequest;
import com.sanedge.gateway.dto.MerchantPolicyDto.CreateMerchantPolicyResponse;
import com.sanedge.gateway.dto.MerchantPolicyDto.FindAllMerchantPolicyResponse;
import com.sanedge.gateway.dto.MerchantPolicyDto.FindByIdMerchantPolicyResponse;
import com.sanedge.gateway.dto.MerchantPolicyDto.SimpleStatusMessageResponse;
import com.sanedge.gateway.dto.MerchantPolicyDto.UpdateMerchantPolicyRequest;
import com.sanedge.gateway.dto.MerchantPolicyDto.UpdateMerchantPolicyResponse;
import io.smallrye.mutiny.Uni;

public interface MerchantPolicyService {
    Uni<FindAllMerchantPolicyResponse> listMerchantPolicies(int page, int size, String search);
    Uni<FindAllMerchantPolicyResponse> listActiveMerchantPolicies(int page, int size, String search);
    Uni<FindAllMerchantPolicyResponse> listTrashedMerchantPolicies(int page, int size, String search);
    Uni<FindByIdMerchantPolicyResponse> getMerchantPolicy(int id);
    Uni<CreateMerchantPolicyResponse> createMerchantPolicy(CreateMerchantPolicyRequest body);
    Uni<UpdateMerchantPolicyResponse> updateMerchantPolicy(int id, UpdateMerchantPolicyRequest body);
    Uni<FindByIdMerchantPolicyResponse> deleteMerchantPolicy(int id);
    Uni<FindByIdMerchantPolicyResponse> restoreMerchantPolicy(int id);
    Uni<SimpleStatusMessageResponse> deleteMerchantPolicyPermanent(int id);
    Uni<SimpleStatusMessageResponse> restoreAllMerchantPolicies();
    Uni<SimpleStatusMessageResponse> deleteAllMerchantPoliciesPermanent();
}
