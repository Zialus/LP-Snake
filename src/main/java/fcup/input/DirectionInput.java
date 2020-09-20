package fcup.input;

public class DirectionInput implements InputAction {

    public final Directions direction;

    public DirectionInput(Directions direction) {
        this.direction = direction;
    }

    @Override
    public InputActionType getType() {
        return InputActionType.DIRECTION_INPUT;
    }
}
