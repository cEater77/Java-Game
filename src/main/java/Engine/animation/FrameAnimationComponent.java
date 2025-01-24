package Engine.animation;

import Engine.renderer.Texture;

import java.util.ArrayList;
import java.util.List;

public class FrameAnimationComponent extends AnimationComponent {

    private List<Texture> frames = new ArrayList<>();
    private int currentFrameIndex = 0;

    public FrameAnimationComponent(List<Texture> frames, float duration, boolean shouldLoop) {
        super(duration, shouldLoop);
        this.frames = frames;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        currentFrameIndex = (int)Math.min(Math.floor(progress * frames.size()), frames.size() - 1);
    }

    public Texture getCurrentFrame()
    {
        return frames.get(currentFrameIndex);
    }
}
