package Engine;

import org.joml.Vector2f;

public class AABB {
    public Vector2f min = new Vector2f();
    public Vector2f max = new Vector2f();

    public AABB(Vector2f min, Vector2f max) {
        this.min = min;
        this.max = max;
    }


    public AABB(float x, float y, float width, float height) {
        this.min.x = x;
        this.min.y = y;
        this.max.x = width;
        this.max.y = height;
    }

    public AABB(AABB aabb)
    {
        this.min.x = aabb.min.x;
        this.min.y = aabb.min.y;
        this.max.x = aabb.max.x;
        this.max.y = aabb.max.y;
    }

    public AABB()
    {

    }

    public boolean isIntersecting(AABB other) {
        return (this.min.x < other.max.x && this.max.x > other.min.x) &&
                (this.min.y < other.max.y && this.max.y > other.min.y);
    }

    public Vector2f getMinTranslationVector(AABB other) {
        
        float overlapX = Math.min(this.max.x, other.max.x) - Math.max(this.min.x, other.min.x);
        float overlapY = Math.min(this.max.y, other.max.y) - Math.max(this.min.y, other.min.y);

        if (overlapX < 0 || overlapY < 0) {
            return null;
        }

        if (overlapX < overlapY) {
            if (this.max.x > other.min.x && other.min.x > this.min.x) {
                return new Vector2f(overlapX, 0);
            } else {
                return new Vector2f(-overlapX, 0);
            }
        } else {
           
            if (this.max.y > other.min.y && other.min.y > this.min.y) {
                return new Vector2f(0, overlapY); 
            } else {
                return new Vector2f(0, -overlapY);
            }
        }
    }
}
