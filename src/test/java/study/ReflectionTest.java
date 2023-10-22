package study;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;

public class ReflectionTest {

  private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);
  private static Class<Car> carClass;

  @BeforeAll
  static void setup() {
    carClass = Car.class;
  }

  @Test
  @DisplayName("1. Car 객체 정보 가져오기")
  void showClass() {

    logger.debug("클래스 이름");
    logger.debug(carClass.getName());

    logger.debug("객체 필드 목록");
    Field[] declaredFileds = carClass.getDeclaredFields();

    for (Field field : declaredFileds) {
      logger.debug(String.valueOf(field));
    }

    logger.debug("객체 생성자 목록");
    Constructor[] constructors = carClass.getConstructors();

    for (Constructor constructor : constructors) {
      logger.debug(constructor.getName());
      logger.debug(Arrays.toString(constructor.getParameterTypes()));
    }

    logger.debug("객체 메서드 목록");
    Method[] methods = carClass.getDeclaredMethods();

    for (Method method : methods) {
      logger.debug(method.getName());
      logger.debug(Arrays.toString(method.getParameterTypes()));
    }
  }

  @Test
  @DisplayName("2. test로 시작하는 메소드 시작하기")
  void runAutomaticRun() throws Exception {
    Car car = carClass.getConstructor().newInstance();

    List<Method> methods = Arrays.stream(carClass.getDeclaredMethods())
            .filter(method -> method.getName().startsWith("test"))
            .collect(Collectors.toList());

    for (Method method : methods) {
      String output = String.valueOf(method.invoke(car));

      assertThat(method.getName()).startsWith("test");
      assertThat(output).startsWith("test :");
    }
  }

  @Test
  @DisplayName("3. @PrintView 애노테이션 메소드 실행")
  void testAnnotationMethodRun() throws Exception {
    Car car = null;
    car = carClass.getConstructor().newInstance();
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStream));

    List<Method> methods = Arrays.stream(carClass.getDeclaredMethods())
            .filter(method -> method.isAnnotationPresent(PrintView.class))
            .collect(Collectors.toList());

    for (Method method : methods) {
      method.invoke(car);
      assertThat(outputStream.toString()).startsWith("자동차 정보를 출력 합니다.");
    }
  }


  @Test
  @DisplayName("4. private field에 값 할당")
  public void privateFieldAccess() throws Exception {
    Car car = carClass.getConstructor().newInstance();
    final String price = "price";

    Field field = carClass.getDeclaredField(price);
    field.setAccessible(true);
    int value = 30;
    field.set(car, value);

    assertThat(car.testGetPrice()).isEqualTo("test : 30");
  }

  @Test
  @DisplayName("5. 인자를 가진 생성자의 인스턴스 생성")
  void constructorWithArgs() throws Exception {
    final String sonata = "sonata";
    int value = 300;

    Car car = carClass.getConstructor(String.class, int.class).newInstance(sonata, value);

    assertThat(car.testGetPrice()).isEqualTo("test : " + value);
  }

}
