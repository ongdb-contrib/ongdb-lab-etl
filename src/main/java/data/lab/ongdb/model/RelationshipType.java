package data.lab.ongdb.model;

/*
*
 * Data Lab - graph database organization.
*
 */


/**
 * @author Yc-Ma
 * @PACKAGE_NAME: data.lab.ongdb.model
 * @Description: TODO(关系类型接口)
 * @date 2019/7/9 14:40
 */

public interface RelationshipType {
    /**
     * Returns the name of the relationship type. The name uniquely identifies a
     * relationship type, i.e. two different RelationshipType instances with
     * different object identifiers (and possibly even different classes) are
     * semantically equivalent if they have {@link String#equals(Object) equal}
     * names.
     *
     * @return the name of the relationship type
     */
    String name();

    /**
     * Instantiates a new {@linkplain RelationshipType} with the given name.
     *
     * @param name the name of the dynamic relationship type
     * @return a {@link RelationshipType} with the given name
     * @throws IllegalArgumentException if name is {@code null}
     */
    static RelationshipType withName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("A relationship type cannot have a null name");
        }
        return new RelationshipType() {
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
                return name.equals(((RelationshipType) that).name());
            }

            @Override
            public int hashCode() {
                return name.hashCode();
            }
        };
    }
}
