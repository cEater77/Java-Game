package Engine.animation;

import org.main.MovementDirection;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class AnimationController {

    private Map<String, Map<MovementDirection, Animation>> animations = new HashMap<>();
    private Map<String, Map<String, Supplier<Boolean>>> transitions = new HashMap<>();
    private String currentAnimationName;
    private MovementDirection currentDirection;

    private float deltaTime = 0.0f;
    private long lastTime = 0;

    public AnimationController() {

    }

    public AnimationController(String defaultAnimationName, MovementDirection defaultDirection, Animation defaultAnimation) {
        currentAnimationName = defaultAnimationName;
        currentDirection = defaultDirection;
        addAnimation(defaultAnimationName, defaultDirection, defaultAnimation);
    }

    public void addAnimation(String animationName, MovementDirection direction, Animation animation) {
        if (!animations.containsKey(animationName))
            animations.put(animationName, new HashMap<>());

        animations.get(animationName).put(direction, animation);
    }

    public void addTransition(String fromAnimationName, String toAnimationName, Supplier<Boolean> condition) {
        if (!transitions.containsKey(fromAnimationName))
            transitions.put(fromAnimationName, new HashMap<>());

        transitions.get(fromAnimationName).put(toAnimationName, condition);
    }

    public void update() {

        if (lastTime == 0) {
            deltaTime = 0;
        } else {
            deltaTime = (System.nanoTime() - lastTime) / 1_000_000_000.0f;
        }

        lastTime = System.nanoTime();

        if (!transitions.containsKey(currentAnimationName)) {
            animations.get(currentAnimationName).get(currentDirection).update(deltaTime);
            return;
        }

        transitions.get(currentAnimationName).forEach((toAnimationName, condition) -> {
            if (condition.get()) {
                animations.get(currentAnimationName).get(currentDirection).reset();
                currentAnimationName = toAnimationName;
            }
        });

        animations.get(currentAnimationName).get(currentDirection).update(deltaTime);
    }

    public Animation getCurrentAnimation() {
        return animations.get(currentAnimationName).get(currentDirection);
    }

    public void setDirection(MovementDirection direction) {
        currentDirection = direction;
    }
}
