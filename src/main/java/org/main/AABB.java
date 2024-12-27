package org.main;

import org.joml.Vector2f;

public class AABB {
    Vector2f center = new Vector2f();
    Vector2f offset = new Vector2f();

    public AABB(Vector2f center, Vector2f offset) {
        this.center = center;
        this.offset = offset;
    }

    public AABB(float x, float y, float width, float height) {
        this.center.x = x;
        this.center.y = y;
        this.offset.x = width;
        this.offset.y = height;
    }

    public boolean isIntersect(AABB other) {
        boolean xOverlap = Math.abs(this.center.x - other.center.x) < (this.offset.x + other.offset.x);
        boolean yOverlap = Math.abs(this.center.y - other.center.y) < (this.offset.y + other.offset.y);
        return xOverlap && yOverlap;
    }
}
