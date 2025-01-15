package Engine.animation;

import Engine.renderer.Texture;
import org.joml.Vector3f;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

public class Animation {

    private PositionAnimationComponent positionAnimationComponent;
    private FrameAnimationComponent frameAnimationComponent;
    private BlendAnimationComponent blendAnimationComponent;

    public Animation(PositionAnimationComponent positionAnimationComponent, FrameAnimationComponent frameAnimationComponent, BlendAnimationComponent blendAnimationComponent) {
        this.positionAnimationComponent = positionAnimationComponent;
        this.frameAnimationComponent = frameAnimationComponent;
        this.blendAnimationComponent = blendAnimationComponent;
    }

    public Animation()
    {

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

    public void addFrameAnimation(float duration, boolean shouldLoop, List<Texture> frames)
    {
        frameAnimationComponent = new FrameAnimationComponent(frames, duration, shouldLoop);
    }

    public void addBlendAnimation(float duration, boolean shouldLoop, float fromAlpha, float toAlpha)
    {

    }

    public void addPositionAnimation(float duration, boolean shouldLoop, Vector3f fromPosition, Vector3f toPosition)
    {

    }

    public Texture getCurrentFrameAnimationData()
    {
        return frameAnimationComponent.getCurrentFrame();
    }

    public float getCurrentBlendAnimationData()
    {
        return blendAnimationComponent.currentAlpha;
    }

    public Vector3f getCurrentPositionAnimationData()
    {
        return positionAnimationComponent.getCurrentPositon();
    }
}
