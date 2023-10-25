package persistence.sql.meta;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import persistence.dialect.h2.H2Dialect;
import persistence.meta.MetaDataColumn;
import persistence.sql.fixture.PersonFixture;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("1.2 MetaDataColumn을 Clazz에서 추출합니다.")
public class MetaDataColumnTest {
  private static Class<PersonFixture> person;
  private static H2Dialect h2Dialect;
  final private String id = "id";
  final private String name = "name";
  final private String age = "age";
  final private String email = "email";

  @BeforeAll
  static void setup() {
    person = PersonFixture.class;
    h2Dialect = new H2Dialect();
  }

  @Test
  @DisplayName("1.2.1 @Id 애노테이션 Column 을 생성한다.")
  public void createWithIdAnnotation() throws NoSuchFieldException {
    Field idField = person.getDeclaredField(id);
    String type = h2Dialect.convertToColumn(idField);

    MetaDataColumn column = MetaDataColumn.of(idField, type);

    assertAll(
            () -> assertThat(column.getColumn()).isEqualTo("id BIGINT generated by default as identity PRIMARY KEY")
    );

  }

  @Test
  @DisplayName("1.2.2 @Column 으로 Column 을 생성한다.")
  public void createWithoutColumnAnnotation() throws NoSuchFieldException {

    Field emailField = person.getDeclaredField(email);
    String emailType = h2Dialect.convertToColumn(emailField);

    MetaDataColumn emailColumn = MetaDataColumn.of(emailField, emailType);

    assertAll(
            () -> assertThat(emailColumn.getColumn()).isEqualTo("email CHARACTER VARYING NOT NULL")
    );

  }

  @Test
  @DisplayName("1.2.2 @Column 애노테이션의 name 값으로 Column 을 생성한다.")
  public void createWithNameValueinColumnAnnotation() throws NoSuchFieldException {
    Field nameField = person.getDeclaredField(name);
    Field ageField = person.getDeclaredField(age);
    String idType = h2Dialect.convertToColumn(nameField);
    String ageType = h2Dialect.convertToColumn(ageField);

    MetaDataColumn nameColumn = MetaDataColumn.of(nameField, idType);
    MetaDataColumn ageColumn = MetaDataColumn.of(ageField, ageType);

    assertAll(
            () -> assertThat(nameColumn.getColumn()).isEqualTo("nick_name CHARACTER VARYING "),
            () -> assertThat(ageColumn.getColumn()).isEqualTo("old INTEGER ")
    );
  }

  @Test
  @DisplayName("1.2.2 @Column 애노테이션의 nullable 값으로 Column 을 생성한다.")
  public void createWithNullableColumnAnnotation() throws NoSuchFieldException {
    Field emailField = person.getDeclaredField(email);
    String emailType = h2Dialect.convertToColumn(emailField);

    MetaDataColumn emailColumn = MetaDataColumn.of(emailField, emailType);

    assertAll(
            () -> assertThat(emailColumn.getColumn()).isEqualTo("email CHARACTER VARYING NOT NULL")
    );
  }

  @Test
  @DisplayName("1.2.2 @GeneratedValue 애노테이션의 값으로 Column 을 생성한다.")
  public void createWithGeneratedValueAnnotation() throws NoSuchFieldException {
    Field idField = person.getDeclaredField(id);
    String type = h2Dialect.convertToColumn(idField);
    MetaDataColumn column = MetaDataColumn.of(idField, type);

    assertAll(
            () -> assertThat(column.getColumn()).isEqualTo("id BIGINT generated by default as identity PRIMARY KEY")
    );
  }
}
