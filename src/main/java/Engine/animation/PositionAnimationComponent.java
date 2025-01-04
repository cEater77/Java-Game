package Engine.animation;

import org.joml.Vector3f;

public class PositionAnimationComponent extends AnimationComponent{
    private Vector3f fromPosition;
    private Vector3f toPosition;
    private Vector3f currentPositon = new Vector3f(0);
    
    public PositionAnimationComponent(Vector3f fromPosition, Vector3f toPosition, float duration, boolean shouldLoop)
    {
        super(duration, shouldLoop);
        this.fromPosition = fromPosition;
        this.toPosition = toPosition;
    }
    
    @Override
    public void update(float deltaTime)
    {
        super.update(deltaTime);
        currentPositon.x = (1 - progress) * fromPosition.x + progress * toPosition.x;
        currentPositon.y = (1 - progress) * fromPosition.y + progress * toPosition.y;
        currentPositon.z = (1 - progress) * fromPosition.z + progress * toPosition.z;
    }
    
    public Vector3f getCurrentPositon()
    {
        return currentPositon;
    }
}
