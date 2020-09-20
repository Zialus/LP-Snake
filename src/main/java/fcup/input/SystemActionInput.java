package fcup.input;

public class SystemActionInput implements InputAction {

    public final SystemActions systemActions;

    public SystemActionInput(SystemActions systemActions) {
        this.systemActions = systemActions;
    }

    @Override
    public InputActionType getType() {
        return InputActionType.SYSTEM_INPUT;
    }
}
