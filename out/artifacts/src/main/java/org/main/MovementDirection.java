package org.main;

import java.util.EnumSet;
import java.util.List;

public enum MovementDirection {
    NONE,
    UP,
    DOWN,
    UP_LEFT,
    DOWN_RIGHT,
    UP_RIGHT,
    DOWN_LEFT,
    RIGHT,
    LEFT;

    static public MovementDirection toSingleDirection(List<MovementDirection> directions)
    {
        switch (directions.size())
        {
            case 0:
            case 4: return MovementDirection.NONE;
            case 1: return directions.get(0);
            case 3: removeOpposites(directions); return directions.get(0);
            case 2 :{
                removeOpposites(directions);
                if(directions.isEmpty())
                    return MovementDirection.NONE;

                return mergeTwoDirections(directions.get(0), directions.get(1));
            }
        }

        return MovementDirection.NONE;
    }

    static private void removeOpposites(List<MovementDirection> directions)
    {
        EnumSet<MovementDirection> directionSet = EnumSet.copyOf(directions);

        if (directionSet.contains(MovementDirection.UP) && directionSet.contains(MovementDirection.DOWN)) {
            directionSet.remove(MovementDirection.UP);
            directionSet.remove(MovementDirection.DOWN);
        }
        if (directionSet.contains(MovementDirection.LEFT) && directionSet.contains(MovementDirection.RIGHT)) {
            directionSet.remove(MovementDirection.LEFT);
            directionSet.remove(MovementDirection.RIGHT);
        }
    }

    static private MovementDirection mergeTwoDirections(MovementDirection direction1, MovementDirection direction2)
    {
        if(direction1 == MovementDirection.UP || direction2 == MovementDirection.UP)
        {
            if(direction1 == MovementDirection.RIGHT || direction2 == MovementDirection.RIGHT)
                return MovementDirection.UP_RIGHT;
            else
                return MovementDirection.UP_LEFT;
        }

        if(direction1 == MovementDirection.DOWN || direction2 == MovementDirection.DOWN)
        {
            if(direction1 == MovementDirection.RIGHT || direction2 == MovementDirection.RIGHT)
                return MovementDirection.DOWN_RIGHT;
            else
                return MovementDirection.DOWN_LEFT;
        }

        return MovementDirection.NONE;
    }
}
