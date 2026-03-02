package com._team._project.domain.region.api.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateRegionRequest {

        @NotBlank
        private String name;

        @NotBlank
        private String geomWkt;

        private Boolean isActive;
}