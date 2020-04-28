package data.lab.ongdb.compose.pack;/*
 *
 * Data Lab - graph database organization.
 *
 */import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @PACKAGE_NAME: data.lab.ongdb.compose.pack
 * @Description: TODO(Describe the role of this JAVA class)
 * @author Yc-Ma 
 * @date 2019/8/2 13:19
 *
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Foo {
    String value();
}
