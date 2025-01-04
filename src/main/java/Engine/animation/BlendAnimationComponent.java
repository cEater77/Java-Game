package Engine.animation;

public class BlendAnimationComponent extends AnimationComponent{
    float fromAlpha;
    float toAlpha;
    float currentAlpha = 0;

    public BlendAnimationComponent(float fromAlpha, float toAlpha, float duration, boolean shouldLoop)
    {
        super(duration, shouldLoop);
        this.fromAlpha = fromAlpha;
        this.toAlpha = toAlpha;
    }

    @Override
    public void update(float deltaTime)
    {
        super.update(deltaTime);
        currentAlpha = (1 - progress) * fromAlpha + progress * toAlpha;
    }

    public float getCurrentAlpha()
    {
        return currentAlpha;
    }
}
