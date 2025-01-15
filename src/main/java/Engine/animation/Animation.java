package Engine.animation;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Animation {

    public final PositionAnimationComponent positionAnimationComponent;
    public final FrameAnimationComponent frameAnimationComponent;
    public final BlendAnimationComponent blendAnimationComponent;

    public Animation(PositionAnimationComponent positionAnimationComponent, FrameAnimationComponent frameAnimationComponent, BlendAnimationComponent blendAnimationComponent) {
        this.positionAnimationComponent = positionAnimationComponent;
        this.frameAnimationComponent = frameAnimationComponent;
        this.blendAnimationComponent = blendAnimationComponent;
    }

    public void update(float deltaTime) {
        if (positionAnimationComponent != null)
            positionAnimationComponent.update(deltaTime);

        if (blendAnimationComponent != null)
            blendAnimationComponent.update(deltaTime);

        if (frameAnimationComponent != null)
            frameAnimationComponent.update(deltaTime);
    }

    public boolean isFinished() {
        boolean positonFinished = true;
        boolean blendFinished = true;
        boolean frameFinished = true;

        if (positionAnimationComponent != null)
            positonFinished = positionAnimationComponent.isFinished();
        if (frameAnimationComponent != null)
            frameFinished = frameAnimationComponent.isFinished();
        if (blendAnimationComponent != null)
            blendFinished = blendAnimationComponent.isFinished();

        return positonFinished && blendFinished && frameFinished;
    }

    public void reset() {
        if (positionAnimationComponent != null)
            positionAnimationComponent.reset();
        if (frameAnimationComponent != null)
            frameAnimationComponent.reset();
        if (blendAnimationComponent != null)
            blendAnimationComponent.reset();
    }
}
