package com.sanedge.gateway.service;

import com.sanedge.gateway.dto.MerchantBusinessDto.CreateMerchantBusinessRequest;
import com.sanedge.gateway.dto.MerchantBusinessDto.CreateMerchantBusinessResponse;
import com.sanedge.gateway.dto.MerchantBusinessDto.FindAllMerchantBusinessResponse;
import com.sanedge.gateway.dto.MerchantBusinessDto.FindByIdMerchantBusinessResponse;
import com.sanedge.gateway.dto.MerchantBusinessDto.SimpleStatusMessageResponse;
import com.sanedge.gateway.dto.MerchantBusinessDto.UpdateMerchantBusinessRequest;
import com.sanedge.gateway.dto.MerchantBusinessDto.UpdateMerchantBusinessResponse;
import io.smallrye.mutiny.Uni;

public interface MerchantBusinessService {
    Uni<FindAllMerchantBusinessResponse> listMerchantBusinesses(int page, int size, String search);
    Uni<FindAllMerchantBusinessResponse> listActiveMerchantBusinesses(int page, int size, String search);
    Uni<FindAllMerchantBusinessResponse> listTrashedMerchantBusinesses(int page, int size, String search);
    Uni<FindByIdMerchantBusinessResponse> getMerchantBusiness(int id);
    Uni<CreateMerchantBusinessResponse> createMerchantBusiness(CreateMerchantBusinessRequest body);
    Uni<UpdateMerchantBusinessResponse> updateMerchantBusiness(int id, UpdateMerchantBusinessRequest body);
    Uni<FindByIdMerchantBusinessResponse> deleteMerchantBusiness(int id);
    Uni<FindByIdMerchantBusinessResponse> restoreMerchantBusiness(int id);
    Uni<SimpleStatusMessageResponse> deleteMerchantBusinessPermanent(int id);
    Uni<SimpleStatusMessageResponse> restoreAllMerchantBusinesses();
    Uni<SimpleStatusMessageResponse> deleteAllMerchantBusinessesPermanent();
}
