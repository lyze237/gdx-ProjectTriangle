package dev.lyze.projectTrianglePlatforming;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TiledObjectLayerToBox2dOptions {
    private float scale;
    @Builder.Default
    private boolean throwOnInvalidObject = true;
    @Builder.Default
    private boolean triangulateInsteadOfThrow = true;
}
