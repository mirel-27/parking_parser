package mm.parking.cli;

import java.util.Objects;

public final class Argument {
    private final String name;
    private final boolean flag;
    private final boolean required;

    public Argument(String name, boolean flag, boolean required) {
        this.name = name;
        this.flag = flag;
        this.required = required;
    }

    public String getName() {
        return name;
    }

    public boolean isFlag() {
        return flag;
    }

    public boolean isRequired() {
        return required;
    }

    @Override
    public String toString() {
        return "Argument{" +
                "name='" + name + '\'' +
                ", flag=" + flag +
                ", required=" + required +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Argument)) return false;
        Argument argument = (Argument) o;
        return flag == argument.flag &&
                required == argument.required &&
                Objects.equals(name, argument.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, flag, required);
    }
}
