package dev.lyze.projectTrianglePlatforming;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TiledTileCollisionToBox2dOptions {
    private float scale;
    @Builder.Default
    private boolean throwOnInvalidObject = true;
    @Builder.Default
    private boolean triangulateInsteadOfThrow = true;
    @Builder.Default
    private boolean combineTileCollisions = true;
}
