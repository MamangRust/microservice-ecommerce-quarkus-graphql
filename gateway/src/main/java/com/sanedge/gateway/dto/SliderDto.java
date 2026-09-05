package com.sanedge.gateway.dto;

import java.util.List;

public class SliderDto {
    @org.eclipse.microprofile.graphql.Name("SliderSliderResponse")
    public record SliderResponse(
            int id,
            String name,
            String image,
            String createdAt,
            String updatedAt) {
        public static SliderResponse from(pb.slider.SliderCommon.SliderResponse proto) {
            return new SliderResponse(
                    proto.getId(),
                    proto.getName(),
                    proto.getImage(),
                    proto.getCreatedAt(),
                    proto.getUpdatedAt()
            );
        }
        public static SliderResponse from(pb.slider.SliderCommon.SliderResponseDeleteAt proto) {
            return new SliderResponse(
                    proto.getId(),
                    proto.getName(),
                    proto.getImage(),
                    proto.getCreatedAt(),
                    proto.getUpdatedAt()
            );
        }
    }

    @org.eclipse.microprofile.graphql.Name("SliderFindAllSliderResponse")
    public record FindAllSliderResponse(
            List<SliderResponse> data,
            String status,
            String message) {
        public static FindAllSliderResponse from(pb.slider.SliderCommon.ApiResponsePaginationSlider proto) {
            return new FindAllSliderResponse(
                    proto.getDataList().stream().map(SliderResponse::from).toList(),
                    proto.getStatus(),
                    proto.getMessage()
            );
        }
        public static FindAllSliderResponse from(pb.slider.SliderCommon.ApiResponsePaginationSliderDeleteAt proto) {
            return new FindAllSliderResponse(
                    proto.getDataList().stream().map(SliderResponse::from).toList(),
                    proto.getStatus(),
                    proto.getMessage()
            );
        }
    }

    @org.eclipse.microprofile.graphql.Name("SliderFindByIdSliderResponse")
    public record FindByIdSliderResponse(
            SliderResponse data,
            String status,
            String message) {
        public static FindByIdSliderResponse from(pb.slider.SliderCommon.ApiResponseSlider proto) {
            return new FindByIdSliderResponse(
                    proto.hasData() ? SliderResponse.from(proto.getData()) : null,
                    proto.getStatus(),
                    proto.getMessage()
            );
        }
        public static FindByIdSliderResponse from(pb.slider.SliderCommon.ApiResponseSliderDeleteAt proto) {
            return new FindByIdSliderResponse(
                    proto.hasData() ? SliderResponse.from(proto.getData()) : null,
                    proto.getStatus(),
                    proto.getMessage()
            );
        }
    }

    @org.eclipse.microprofile.graphql.Name("SliderCreateSliderRequest")
    public record CreateSliderRequest(
            String name,
            String image) {}

    @org.eclipse.microprofile.graphql.Name("SliderCreateSliderResponse")
    public record CreateSliderResponse(
            SliderResponse data,
            String status,
            String message) {
        public static CreateSliderResponse from(pb.slider.SliderCommon.ApiResponseSlider proto) {
            return new CreateSliderResponse(
                    proto.hasData() ? SliderResponse.from(proto.getData()) : null,
                    proto.getStatus(),
                    proto.getMessage()
            );
        }
    }

    @org.eclipse.microprofile.graphql.Name("SliderUpdateSliderRequest")
    public record UpdateSliderRequest(
            String name,
            String image) {}

    @org.eclipse.microprofile.graphql.Name("SliderUpdateSliderResponse")
    public record UpdateSliderResponse(
            SliderResponse data,
            String status,
            String message) {
        public static UpdateSliderResponse from(pb.slider.SliderCommon.ApiResponseSlider proto) {
            return new UpdateSliderResponse(
                    proto.hasData() ? SliderResponse.from(proto.getData()) : null,
                    proto.getStatus(),
                    proto.getMessage()
            );
        }
    }

    @org.eclipse.microprofile.graphql.Name("SliderSimpleStatusMessageResponse")
    public record SimpleStatusMessageResponse(
            String status,
            String message) {
        public static SimpleStatusMessageResponse from(pb.slider.SliderCommon.ApiResponseSliderDeleteAt proto) {
            return new SimpleStatusMessageResponse(proto.getStatus(), proto.getMessage());
        }
        public static SimpleStatusMessageResponse from(pb.slider.SliderCommon.ApiResponseSliderDelete proto) {
            return new SimpleStatusMessageResponse(proto.getStatus(), proto.getMessage());
        }
    }
}
