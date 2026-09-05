package com.sanedge.gateway.service;

import com.sanedge.gateway.dto.MerchantAwardDto.CreateMerchantAwardRequest;
import com.sanedge.gateway.dto.MerchantAwardDto.CreateMerchantAwardResponse;
import com.sanedge.gateway.dto.MerchantAwardDto.FindAllMerchantAwardResponse;
import com.sanedge.gateway.dto.MerchantAwardDto.FindByIdMerchantAwardResponse;
import com.sanedge.gateway.dto.MerchantAwardDto.SimpleStatusMessageResponse;
import com.sanedge.gateway.dto.MerchantAwardDto.UpdateMerchantAwardRequest;
import com.sanedge.gateway.dto.MerchantAwardDto.UpdateMerchantAwardResponse;
import io.smallrye.mutiny.Uni;

public interface MerchantAwardService {
    Uni<FindAllMerchantAwardResponse> listMerchantAwards(int page, int size, String search);
    Uni<FindAllMerchantAwardResponse> listActiveMerchantAwards(int page, int size, String search);
    Uni<FindAllMerchantAwardResponse> listTrashedMerchantAwards(int page, int size, String search);
    Uni<FindByIdMerchantAwardResponse> getMerchantAward(int id);
    Uni<CreateMerchantAwardResponse> createMerchantAward(CreateMerchantAwardRequest body);
    Uni<UpdateMerchantAwardResponse> updateMerchantAward(int id, UpdateMerchantAwardRequest body);
    Uni<FindByIdMerchantAwardResponse> deleteMerchantAward(int id);
    Uni<FindByIdMerchantAwardResponse> restoreMerchantAward(int id);
    Uni<SimpleStatusMessageResponse> deleteMerchantAwardPermanent(int id);
    Uni<SimpleStatusMessageResponse> restoreAllMerchantAwards();
    Uni<SimpleStatusMessageResponse> deleteAllMerchantAwardsPermanent();
}
