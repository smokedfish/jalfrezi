package org.jalfrezi.datamodel.id;

import com.google.common.base.Objects;

public class Id<T> {

    private final T id;

    public Id(T id) {
        this.id = id;
    }

    public T getId() {
        return id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public boolean equals (Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }

        final Id<?> other = (Id<?>) obj;
        return Objects.equal(getClass(), other.getClass()) && Objects.equal(this.id, other.id);
    }

    @Override
    public String toString() {
        return id.toString();
    }
}
