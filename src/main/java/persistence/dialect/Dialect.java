package persistence.dialect;

import org.h2.value.DataType;
import org.h2.value.Value;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Dialect {
  private final Map<Class, Integer> defaults = new ConcurrentHashMap<>();

  protected void registerColumnType(Class clazz, Integer type) {
    defaults.put(clazz, type);
  }

  public Integer getJavaSqlType(Field field) {
    Class<?> type = field.getType();
    Optional<Integer> dbType = Optional.ofNullable(defaults.get(type));

    return dbType.orElseThrow(() -> new RuntimeException(String.format("%s is not in the mappings", type)));
  }

  public String convertToColumn(Field field) {
    Integer type = getJavaSqlType(field);
    return Value.getTypeName(DataType.convertSQLTypeToValueType(type));
  }
}
