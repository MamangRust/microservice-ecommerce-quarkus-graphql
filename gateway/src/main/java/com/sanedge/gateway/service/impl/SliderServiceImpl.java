package com.sanedge.gateway.service.impl;

import com.sanedge.gateway.dto.SliderDto.CreateSliderRequest;
import com.sanedge.gateway.dto.SliderDto.CreateSliderResponse;
import com.sanedge.gateway.dto.SliderDto.FindAllSliderResponse;
import com.sanedge.gateway.dto.SliderDto.FindByIdSliderResponse;
import com.sanedge.gateway.dto.SliderDto.SimpleStatusMessageResponse;
import com.sanedge.gateway.dto.SliderDto.UpdateSliderRequest;
import com.sanedge.gateway.dto.SliderDto.UpdateSliderResponse;
import com.sanedge.gateway.service.FileService;
import com.sanedge.gateway.service.SliderService;
import com.sanedge.gateway.telemetry.TelemetryHelper;
import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.resteasy.reactive.multipart.FileUpload;

@ApplicationScoped
public class SliderServiceImpl implements SliderService {

    @Inject
    FileService fileService;

    @Inject
    TelemetryHelper telemetryHelper;

    @GrpcClient("slider")
    pb.slider.MutinySliderQueryServiceGrpc.MutinySliderQueryServiceStub sliderQueryService;

    @GrpcClient("slider")
    pb.slider.MutinySliderCommandServiceGrpc.MutinySliderCommandServiceStub sliderCommandService;

    @Override
    public Uni<FindAllSliderResponse> listSliders(int page, int size, String search) {
        return telemetryHelper.traceAndMetric("slider.listSliders", () -> sliderQueryService.findAll(pb.slider.SliderQuery.FindAllSliderRequest.newBuilder()
                .setPage(page)
                .setPageSize(size)
                .setSearch(search == null ? "" : search)
                .build())
                .map(FindAllSliderResponse::from));
    }

    @Override
    public Uni<FindAllSliderResponse> listActiveSliders(int page, int size, String search) {
        return telemetryHelper.traceAndMetric("slider.listActiveSliders", () -> sliderQueryService.findByActive(pb.slider.SliderQuery.FindAllSliderRequest.newBuilder()
                .setPage(page)
                .setPageSize(size)
                .setSearch(search == null ? "" : search)
                .build())
                .map(FindAllSliderResponse::from));
    }

    @Override
    public Uni<FindAllSliderResponse> listTrashedSliders(int page, int size, String search) {
        return telemetryHelper.traceAndMetric("slider.listTrashedSliders", () -> sliderQueryService.findByTrashed(pb.slider.SliderQuery.FindAllSliderRequest.newBuilder()
                .setPage(page)
                .setPageSize(size)
                .setSearch(search == null ? "" : search)
                .build())
                .map(FindAllSliderResponse::from));
    }

    @Override
    public Uni<FindByIdSliderResponse> getSlider(int id) {
        return telemetryHelper.traceAndMetric("slider.getSlider", () -> sliderQueryService.findById(pb.slider.SliderCommon.FindByIdSliderRequest.newBuilder()
                .setId(id)
                .build())
                .map(FindByIdSliderResponse::from));
    }

    @Override
    public Uni<CreateSliderResponse> createSlider(CreateSliderRequest body) {
        return telemetryHelper.traceAndMetric("slider.createSlider", () -> sliderCommandService.create(pb.slider.SliderCommand.CreateSliderRequest.newBuilder()
                .setName(body.name() == null ? "" : body.name())
                .setImage(body.image() == null ? "" : body.image())
                .build())
                .map(CreateSliderResponse::from));
    }

    @Override
    public Uni<UpdateSliderResponse> updateSlider(int id, UpdateSliderRequest body) {
        return telemetryHelper.traceAndMetric("slider.updateSlider", () -> sliderCommandService.update(pb.slider.SliderCommand.UpdateSliderRequest.newBuilder()
                .setId(id)
                .setName(body.name() == null ? "" : body.name())
                .setImage(body.image() == null ? "" : body.image())
                .build())
                .map(UpdateSliderResponse::from));
    }

    @Override
    public Uni<UpdateSliderResponse> uploadSlider(int id, FileUpload file) {
        return telemetryHelper.traceAndMetric("slider.uploadSlider", () -> sliderQueryService.findById(pb.slider.SliderCommon.FindByIdSliderRequest.newBuilder().setId(id).build())
                .flatMap(res -> {
                    if (!res.hasData()) {
                        return Uni.createFrom().failure(new Exception("Slider not found"));
                    }
                    pb.slider.SliderCommon.SliderResponse data = res.getData();
                    String filepath = "uploads/sliders/" + System.currentTimeMillis() + "_" + file.fileName();
                    String savedPath = fileService.createFileImage(file, filepath);
                    if (savedPath == null) {
                        return Uni.createFrom().failure(new Exception("Failed to save image file"));
                    }
                    return sliderCommandService.update(pb.slider.SliderCommand.UpdateSliderRequest.newBuilder()
                            .setId(id)
                            .setName(data.getName())
                            .setImage(savedPath)
                            .build());
                })
                .map(UpdateSliderResponse::from));
    }

    @Override
    public Uni<FindByIdSliderResponse> deleteSlider(int id) {
        return telemetryHelper.traceAndMetric("slider.deleteSlider", () -> sliderCommandService.trashedSlider(pb.slider.SliderCommon.FindByIdSliderRequest.newBuilder()
                .setId(id)
                .build())
                .map(FindByIdSliderResponse::from));
    }

    @Override
    public Uni<FindByIdSliderResponse> restoreSlider(int id) {
        return telemetryHelper.traceAndMetric("slider.restoreSlider", () -> sliderCommandService.restoreSlider(pb.slider.SliderCommon.FindByIdSliderRequest.newBuilder()
                .setId(id)
                .build())
                .map(FindByIdSliderResponse::from));
    }

    @Override
    public Uni<SimpleStatusMessageResponse> deleteSliderPermanent(int id) {
        return telemetryHelper.traceAndMetric("slider.deleteSliderPermanent", () -> sliderCommandService.deleteSliderPermanent(pb.slider.SliderCommon.FindByIdSliderRequest.newBuilder()
                .setId(id)
                .build())
                .map(proto -> new SimpleStatusMessageResponse(proto.getStatus(), proto.getMessage())));
    }

    @Override
    public Uni<SimpleStatusMessageResponse> restoreAllSliders() {
        return telemetryHelper.traceAndMetric("slider.restoreAllSliders", () -> sliderCommandService.restoreAllSlider(com.google.protobuf.Empty.getDefaultInstance())
                .map(proto -> new SimpleStatusMessageResponse(proto.getStatus(), proto.getMessage())));
    }

    @Override
    public Uni<SimpleStatusMessageResponse> deleteAllSlidersPermanent() {
        return telemetryHelper.traceAndMetric("slider.deleteAllSlidersPermanent", () -> sliderCommandService.deleteAllSliderPermanent(com.google.protobuf.Empty.getDefaultInstance())
                .map(proto -> new SimpleStatusMessageResponse(proto.getStatus(), proto.getMessage())));
    }
}
