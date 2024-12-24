package dk.sunepoulsen.tes.features.model;

import dk.sunepoulsen.tes.rest.models.BaseModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

@Builder
@Schema(name = "Feature", description = "Defines a feature")
public class Feature implements BaseModel {
    @Schema(
        description = "Unique key of this feature",
        requiredMode = Schema.RequiredMode.REQUIRED,
        accessMode = Schema.AccessMode.READ_ONLY
    )
    @NotNull
    private String key;

    @Schema(
        description = "Name of this feature",
        requiredMode = Schema.RequiredMode.REQUIRED,
        accessMode = Schema.AccessMode.READ_WRITE
    )
    @NotNull
    private String name;

    @Schema(
        description = "Description of this feature",
        requiredMode = Schema.RequiredMode.REQUIRED,
        accessMode = Schema.AccessMode.READ_WRITE
    )
    private String description;

    @Schema(
        description = "Activations of this feature",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        accessMode = Schema.AccessMode.READ_WRITE
    )
    @Valid
    private List<FeatureActivation> activations;
}
