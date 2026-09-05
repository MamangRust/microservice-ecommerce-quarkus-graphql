package com.sanedge.gateway.service;

import com.sanedge.gateway.dto.MerchantDetailDto.CreateMerchantDetailRequest;
import com.sanedge.gateway.dto.MerchantDetailDto.CreateMerchantDetailResponse;
import com.sanedge.gateway.dto.MerchantDetailDto.FindAllMerchantDetailResponse;
import com.sanedge.gateway.dto.MerchantDetailDto.FindByIdMerchantDetailResponse;
import com.sanedge.gateway.dto.MerchantDetailDto.SimpleStatusMessageResponse;
import com.sanedge.gateway.dto.MerchantDetailDto.UpdateMerchantDetailRequest;
import com.sanedge.gateway.dto.MerchantDetailDto.UpdateMerchantDetailResponse;
import io.smallrye.mutiny.Uni;

public interface MerchantDetailService {
    Uni<FindAllMerchantDetailResponse> listMerchantDetails(int page, int size, String search);
    Uni<FindAllMerchantDetailResponse> listActiveMerchantDetails(int page, int size, String search);
    Uni<FindAllMerchantDetailResponse> listTrashedMerchantDetails(int page, int size, String search);
    Uni<FindByIdMerchantDetailResponse> getMerchantDetail(int id);
    Uni<CreateMerchantDetailResponse> createMerchantDetail(CreateMerchantDetailRequest body);
    Uni<UpdateMerchantDetailResponse> updateMerchantDetail(int id, UpdateMerchantDetailRequest body);
    Uni<FindByIdMerchantDetailResponse> deleteMerchantDetail(int id);
    Uni<FindByIdMerchantDetailResponse> restoreMerchantDetail(int id);
    Uni<SimpleStatusMessageResponse> deleteMerchantDetailPermanent(int id);
    Uni<SimpleStatusMessageResponse> restoreAllMerchantDetails();
    Uni<SimpleStatusMessageResponse> deleteAllMerchantDetailsPermanent();
}
