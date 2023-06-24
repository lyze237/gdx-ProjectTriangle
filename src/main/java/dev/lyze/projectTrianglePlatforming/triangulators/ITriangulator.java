package dev.lyze.projectTrianglePlatforming.triangulators;

import com.badlogic.gdx.utils.ShortArray;

public interface ITriangulator {
    public ShortArray triangulate(float[] vertices);
}
