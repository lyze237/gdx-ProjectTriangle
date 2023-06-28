package dev.lyze.projectTrianglePlatforming;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TiledTileLayerToBox2dOptions {
    private float scale;
    @Builder.Default
    private boolean throwOnInvalidObject = true;
}
