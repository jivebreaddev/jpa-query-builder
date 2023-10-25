package persistence.meta;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Predicate;

public enum ConstraintType {
  EMPTY((ConstraintGroupType) -> false, "", Map.of()),
  GENERATED_VALUE((annotation) -> annotation.annotationType() == GeneratedValue.class,
          "strategy",
          Map.of(GenerationType.IDENTITY.name(), "generated by default as identity")),
  ID((annotation -> annotation.annotationType() == Id.class), "", Map.of()),
  COLUMN((annotation -> annotation.annotationType() == Column.class), "nullable", Map.of("true", "NOT NULL", "false", ""));


  private Predicate<Annotation> predicate;
  private String optionParam;
  private Map<String, String> constraintMapping;
  private String EMPTY_STRING = "";

  ConstraintType(Predicate<Annotation> predicate, String optionParam, Map<String, String> constraintMapping) {
    this.predicate = predicate;
    this.optionParam = optionParam;
    this.constraintMapping = constraintMapping;
  }

  public boolean hasConstraint(Annotation annotation) {
    return predicate.test(annotation);
  }

  public String getConstraintMappingToConstraint(Annotation annotation) {
    String value = Arrays.stream(annotation.annotationType().getDeclaredMethods())
            .filter(method -> method.getName().equals(optionParam))
            .map(val -> {
              try {
                return val.invoke(annotation);
              } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
              }
            }).findFirst()
            .orElseGet(() -> "")
            .toString();

    return constraintMapping.getOrDefault(value, EMPTY_STRING);
  }

}
