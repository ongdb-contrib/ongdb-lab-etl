package data.lab.ongdb.model;
/*
 *
 * Data Lab - graph database organization.
 *
 *//**
 * @author Yc-Ma 
 * @PACKAGE_NAME: data.lab.ongdb.model
 * @Description: TODO(标签接口)
 * @date 2019/7/9 14:40
 */
public interface Label {
    /**
     * Returns the name of the label. The name uniquely identifies a
     * label, i.e. two different Label instances with different object identifiers
     * (and possibly even different classes) are semantically equivalent if they
     * have {@link String#equals(Object) equal} names.
     *
     * @return the name of the label
     */
    String name();

    /**
     * Instantiates a new {@linkplain Label} with the given name.
     *
     * @param name the name of the label
     * @return a {@link Label} instance for the given name
     * @throws IllegalArgumentException if name is {@code null}
     */
    static Label label(String name) {
        if (name == null) {
            throw new IllegalArgumentException("A label cannot have a null name");
        }
        return new Label() {
            @Override
            public String name() {
                return name;
            }

            @Override
            public String toString() {
                return name;
            }

            @Override
            public boolean equals(Object that) {
                if (this == that) {
                    return true;
                }
                if (that == null || that.getClass() != getClass()) {
                    return false;
                }
                return name.equals(((Label) that).name());
            }

            @Override
            public int hashCode() {
                return name.hashCode();
            }
        };
    }
}
