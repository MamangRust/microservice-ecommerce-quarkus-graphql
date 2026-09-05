package com.sanedge.gateway.service;

import com.sanedge.gateway.dto.ShippingAddressDto.CreateShippingAddressRequest;
import com.sanedge.gateway.dto.ShippingAddressDto.CreateShippingAddressResponse;
import com.sanedge.gateway.dto.ShippingAddressDto.FindAllShippingResponse;
import com.sanedge.gateway.dto.ShippingAddressDto.FindByIdShippingResponse;
import com.sanedge.gateway.dto.ShippingAddressDto.SimpleStatusMessageResponse;
import com.sanedge.gateway.dto.ShippingAddressDto.UpdateShippingAddressRequest;
import com.sanedge.gateway.dto.ShippingAddressDto.UpdateShippingAddressResponse;
import io.smallrye.mutiny.Uni;

public interface ShippingAddressService {
    Uni<FindAllShippingResponse> listShippingAddresses(int page, int size, String search);
    Uni<FindByIdShippingResponse> getShippingAddressByOrder(int orderId);
    Uni<FindAllShippingResponse> listActiveShippingAddresses(int page, int size, String search);
    Uni<FindAllShippingResponse> listTrashedShippingAddresses(int page, int size, String search);
    Uni<FindByIdShippingResponse> getShippingAddress(int id);
    Uni<CreateShippingAddressResponse> createShippingAddress(CreateShippingAddressRequest body);
    Uni<UpdateShippingAddressResponse> updateShippingAddress(int id, UpdateShippingAddressRequest body);
    Uni<FindByIdShippingResponse> deleteShippingAddress(int id);
    Uni<FindByIdShippingResponse> restoreShippingAddress(int id);
    Uni<SimpleStatusMessageResponse> deleteShippingAddressPermanent(int id);
    Uni<SimpleStatusMessageResponse> restoreAllShippingAddresses();
    Uni<SimpleStatusMessageResponse> deleteAllShippingAddressesPermanent();
}
