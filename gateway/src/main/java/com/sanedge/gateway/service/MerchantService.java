package com.sanedge.gateway.service;

import com.sanedge.gateway.dto.MerchantDto.CreateMerchantRequest;
import com.sanedge.gateway.dto.MerchantDto.CreateMerchantResponse;
import com.sanedge.gateway.dto.MerchantDto.FindAllMerchantResponse;
import com.sanedge.gateway.dto.MerchantDto.FindByIdMerchantResponse;
import com.sanedge.gateway.dto.MerchantDto.SimpleStatusMessageResponse;
import com.sanedge.gateway.dto.MerchantDto.TrashedMerchantResponse;
import com.sanedge.gateway.dto.MerchantDto.UpdateMerchantRequest;
import com.sanedge.gateway.dto.MerchantDto.UpdateMerchantResponse;
import io.smallrye.mutiny.Uni;

public interface MerchantService {
    Uni<FindAllMerchantResponse> listMerchants(int page, int size, String search);
    Uni<FindByIdMerchantResponse> getMerchant(int id);
    Uni<FindAllMerchantResponse> listActiveMerchants(int page, int size, String search);
    Uni<FindAllMerchantResponse> listTrashedMerchants(int page, int size, String search);
    Uni<CreateMerchantResponse> createMerchant(CreateMerchantRequest body);
    Uni<UpdateMerchantResponse> updateMerchant(int id, UpdateMerchantRequest body);
    Uni<TrashedMerchantResponse> deleteMerchant(int id);
    Uni<TrashedMerchantResponse> restoreMerchant(int id);
    Uni<SimpleStatusMessageResponse> deleteMerchantPermanent(int id);
    Uni<SimpleStatusMessageResponse> restoreAllMerchants();
    Uni<SimpleStatusMessageResponse> deleteAllMerchantsPermanent();
}
