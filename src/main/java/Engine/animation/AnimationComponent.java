package Engine.animation;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class AnimationComponent {

    private float elapsedTime = 0;
    private boolean shouldLoop;
    protected float progress = 0;
    private float duration;

    public AnimationComponent(float duration, boolean shouldLoop) {
        this.duration = duration;
        this.shouldLoop = shouldLoop;
    }

    public AnimationComponent()
    {

    }

    public void update(float deltaTime) {
        elapsedTime += deltaTime;
        progress = elapsedTime / duration;
        if (progress >= 1) {
            if (shouldLoop)
                elapsedTime = 0;

            progress = 1.0f;
        }
    }

    public boolean isFinished() {
        if (shouldLoop)
            return false;
        else
            return progress == 1.0f;
    }

    public void reset()
    {
        elapsedTime = 0;
    }
}
