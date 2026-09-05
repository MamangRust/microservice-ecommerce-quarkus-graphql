package com.sanedge.gateway.service;

import com.sanedge.gateway.dto.SliderDto.CreateSliderRequest;
import com.sanedge.gateway.dto.SliderDto.CreateSliderResponse;
import com.sanedge.gateway.dto.SliderDto.FindAllSliderResponse;
import com.sanedge.gateway.dto.SliderDto.FindByIdSliderResponse;
import com.sanedge.gateway.dto.SliderDto.SimpleStatusMessageResponse;
import com.sanedge.gateway.dto.SliderDto.UpdateSliderRequest;
import com.sanedge.gateway.dto.SliderDto.UpdateSliderResponse;
import io.smallrye.mutiny.Uni;
import org.jboss.resteasy.reactive.multipart.FileUpload;

public interface SliderService {
    Uni<FindAllSliderResponse> listSliders(int page, int size, String search);
    Uni<FindAllSliderResponse> listActiveSliders(int page, int size, String search);
    Uni<FindAllSliderResponse> listTrashedSliders(int page, int size, String search);
    Uni<FindByIdSliderResponse> getSlider(int id);
    Uni<CreateSliderResponse> createSlider(CreateSliderRequest body);
    Uni<UpdateSliderResponse> updateSlider(int id, UpdateSliderRequest body);
    Uni<UpdateSliderResponse> uploadSlider(int id, FileUpload file);
    Uni<FindByIdSliderResponse> deleteSlider(int id);
    Uni<FindByIdSliderResponse> restoreSlider(int id);
    Uni<SimpleStatusMessageResponse> deleteSliderPermanent(int id);
    Uni<SimpleStatusMessageResponse> restoreAllSliders();
    Uni<SimpleStatusMessageResponse> deleteAllSlidersPermanent();
}
