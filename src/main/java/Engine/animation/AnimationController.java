package Engine.animation;

import Engine.renderer.Texture;
import org.main.Game;
import org.main.MovementDirection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class AnimationController {

    private Map<String, Map<MovementDirection, Animation>> animations = new HashMap<>();
    private Map<String, List<Transition>> transitions = new HashMap<>();
    private String currentAnimationName;
    private MovementDirection currentDirection;

    public class Transition
    {
        public Transition(String destinationAnimationName, Supplier<Boolean> condition) {
            this.destinationAnimationName = destinationAnimationName;
            this.condition = condition;
        }

        public final String destinationAnimationName;
        public final Supplier<Boolean> condition;
    }

    public AnimationController(AnimationController animationController) {
        this.transitions = new HashMap<>(animationController.transitions);
        this.animations = new HashMap<>(animationController.animations);
        this.currentAnimationName = animationController.currentAnimationName;
        this.currentDirection = animationController.currentDirection;
    }

    public AnimationController()
    {
        Animation defaultAnim = new Animation();
        addAnimation("default", MovementDirection.NONE, defaultAnim);
        currentAnimationName = "default";
        currentDirection = MovementDirection.NONE;
    }

    public AnimationController(List<Texture> frames)
    {
        Animation defaultAnim = new Animation();
        defaultAnim.addFrameAnimation(1.0f, false, frames);
        addAnimation("default", MovementDirection.NONE, defaultAnim);
        currentAnimationName = "default";
        currentDirection = MovementDirection.NONE;
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
            transitions.put(fromAnimationName, new ArrayList<>());

        transitions.get(fromAnimationName).add(new Transition(toAnimationName, condition));
    }

    public void update() {

        float deltaTime = (float)Game.getWindow().getLastFrameDuration();

        if (!transitions.containsKey(currentAnimationName)) {
            getCurrentAnimation().update(deltaTime);
            return;
        }

        transitions.get(currentAnimationName).forEach((transition) -> {
            if (transition.condition.get()) {
                getCurrentAnimation().reset();
                currentAnimationName = transition.destinationAnimationName;
            }
        });

        getCurrentAnimation().update(deltaTime);
    }

    public Animation getCurrentAnimation() {
        return animations.get(currentAnimationName).get(currentDirection);
    }

    public void setDirection(MovementDirection direction) {
        currentDirection = direction;
    }
}
