package Engine.animation;

import Engine.renderer.Texture;
import org.joml.Vector2f;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class FrameAnimationComponent extends AnimationComponent {

    private List<Texture> frames = new ArrayList<>();
    private int currentFrameIndex = 0;

    public FrameAnimationComponent(List<Texture> frames, float duration, boolean shouldLoop) {
        super(duration, shouldLoop);
        this.frames = frames;
    }

    public FrameAnimationComponent() {
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
