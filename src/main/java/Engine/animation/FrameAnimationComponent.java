package Engine.animation;

import Engine.renderer.Texture;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

public class FrameAnimationComponent extends AnimationComponent {

    private List<Texture> frames;
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

    @Override
    public void serialize(DataOutputStream stream) throws IOException {
        super.serialize(stream);
        stream.writeInt(frames.size());
        for(Texture frame : frames)
        {
            stream.writeFloat(frame.uvPosition.x);
            stream.writeFloat(frame.uvPosition.y);
            stream.writeFloat(frame.uvSize.x);
            stream.writeFloat(frame.uvSize.y);

        }
    }

    @Override
    public void deserialize(DataInputStream stream)
    {

    }
}
