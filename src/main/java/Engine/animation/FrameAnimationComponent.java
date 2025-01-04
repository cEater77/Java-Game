package Engine.animation;

import Engine.renderer.Texture;

import java.util.List;

public class FrameAnimationComponent extends AnimationComponent {

    private List<Texture> frames;
    private int currentFrameIndex = 0;
    private float frameDuration = 0.0f;

    public FrameAnimationComponent(List<Texture> frames, float duration, boolean shouldLoop) {
        super(duration, shouldLoop);
        this.frames = frames;
        if (!frames.isEmpty())
            frameDuration = 1.0f / (float) frames.size();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        currentFrameIndex = (int)Math.floor(progress / frameDuration);
        if(currentFrameIndex == frames.size())
            currentFrameIndex--;
    }

    public Texture getCurrentFrame()
    {
        return frames.get(currentFrameIndex);
    }
}
