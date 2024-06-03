package WordGameInternals;

/**
 * Represents a configurable setting for the word game.
 *
 * @param <T> the type of the setting's value
 */
public class Setting<T> {
    private String name;
    private String description;
    private T value;
    private ValueValidator<T> valueValidator;

    /**
     * Functional interface for validating the setting's value.
     *
     * @param <T> the type of the value to validate
     */
    @FunctionalInterface
    interface ValueValidator<T> {
        /**
         * Checks if the provided value is valid.
         *
         * @param value the value to check
         * @return true if the value is valid, false otherwise
         */
        boolean check_valid(T value);
    }

    /**
     * Checks if the provided value is legal according to the validator.
     *
     * @param value the value to check
     * @return true if the value is legal, false otherwise
     */
    public boolean valueIsLegal(T value) {
        return valueValidator.check_valid(value);
    }

    /**
     * Constructs a new Setting with a default value and no specific validation.
     *
     * @param name the name of the setting
     * @param description a description of the setting
     * @param default_value the default value of the setting
     */
    public Setting(String name, String description, T default_value) {
        this.name = name;
        this.description = description;
        this.value = default_value;
        this.valueValidator = (value) -> true;
    }

    /**
     * Constructs a new Setting with a default value and a custom validator.
     *
     * @param name the name of the setting
     * @param description a description of the setting
     * @param default_value the default value of the setting
     * @param valueValidator a custom validator for the setting's value
     */
    public Setting(String name, String description, T default_value, ValueValidator<T> valueValidator) {
        this(name, description, default_value);
        this.valueValidator = valueValidator;
    }

    public String getName() { return name; }

    public String get_description() { return description; }

    public T getValue() { return value; }

    /**
     * Sets a new value for the setting. Checks if the value is legal.
     *
     * @param value the new value to set
     * @throws IllegalArgumentException if the new value is not legal
     */
    public void setValue(T value) throws IllegalArgumentException {
        if (!valueIsLegal(value)) {
            throw new IllegalArgumentException("Value is forbidden to be set to the passed argument.");
        }
        this.value = value;
    }
}
