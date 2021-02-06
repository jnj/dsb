package dsb;

import java.util.Objects;

class Target {
    public final String name;

    Target(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final var target = (Target) o;
        return name.equals(target.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
