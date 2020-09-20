package fcup.input;

public interface InputAction {
    static InputAction newDirectionInput(Directions d) {
        return new DirectionInput(d);
    }

    static InputAction newSystemInput(SystemActions s) {
        return new SystemActionInput(s);
    }

    InputActionType getType();
}

