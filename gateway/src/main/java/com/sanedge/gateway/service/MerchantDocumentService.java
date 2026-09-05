package com.sanedge.gateway.service;

import com.sanedge.gateway.dto.MerchantDocumentDto.CreateMerchantDocumentBody;
import com.sanedge.gateway.dto.MerchantDocumentDto.CreateMerchantDocumentResponse;
import com.sanedge.gateway.dto.MerchantDocumentDto.FindAllMerchantDocumentsResponse;
import com.sanedge.gateway.dto.MerchantDocumentDto.FindByIdMerchantDocumentResponse;
import com.sanedge.gateway.dto.MerchantDocumentDto.SimpleStatusMessageResponse;
import com.sanedge.gateway.dto.MerchantDocumentDto.TrashedMerchantDocumentResponse;
import com.sanedge.gateway.dto.MerchantDocumentDto.UpdateMerchantDocumentBody;
import com.sanedge.gateway.dto.MerchantDocumentDto.UpdateMerchantDocumentResponse;
import com.sanedge.gateway.dto.MerchantDocumentDto.UpdateMerchantDocumentStatusBody;
import io.smallrye.mutiny.Uni;

public interface MerchantDocumentService {
    Uni<FindAllMerchantDocumentsResponse> listMerchantDocuments(int page, int size, String search);
    Uni<FindAllMerchantDocumentsResponse> listActiveMerchantDocuments(int page, int size, String search);
    Uni<FindAllMerchantDocumentsResponse> listTrashedMerchantDocuments(int page, int size, String search);
    Uni<FindByIdMerchantDocumentResponse> getMerchantDocument(int id);
    Uni<CreateMerchantDocumentResponse> createMerchantDocument(CreateMerchantDocumentBody body);
    Uni<UpdateMerchantDocumentResponse> updateMerchantDocument(int id, UpdateMerchantDocumentBody body);
    Uni<UpdateMerchantDocumentResponse> updateMerchantDocumentStatus(int id, UpdateMerchantDocumentStatusBody body);
    Uni<TrashedMerchantDocumentResponse> trashMerchantDocument(int id);
    Uni<CreateMerchantDocumentResponse> restoreMerchantDocument(int id);
    Uni<SimpleStatusMessageResponse> deleteMerchantDocumentPermanent(int id);
    Uni<SimpleStatusMessageResponse> restoreAllMerchantDocuments();
    Uni<SimpleStatusMessageResponse> deleteAllMerchantDocuments();
}
