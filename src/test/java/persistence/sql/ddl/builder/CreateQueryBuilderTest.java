package persistence.sql.ddl.builder;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import persistence.ddl.Person;
import persistence.ddl.builder.CreateQueryBuilder;
import persistence.dialect.h2.H2Dialect;
import persistence.meta.MetaData;
import persistence.sql.fixture.PersonFixture;
import persistence.sql.fixture.PersonFixture2;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("1.2 Entity를 CREATE DLL로 변환합니다.")
public class CreateQueryBuilderTest {
  private static Class<PersonFixture> person;
  private static Class<PersonFixture2> person2;
  private static Class<Person> original;
  private static H2Dialect h2Dialect;
  private CreateQueryBuilder createQueryBuilder = new CreateQueryBuilder();
  ;
  @BeforeAll
  static void setup() {
    person = PersonFixture.class;
    original = Person.class;
    person2 = PersonFixture2.class;
    h2Dialect = new H2Dialect();
  }
  @Test
  @DisplayName("1.2.1.1 @Entity, @id가 표기된 class의 TABLE CREATE DDL을 생성합니다.")
  public void createDdlFromEntityClass() {
    MetaData meta = MetaData.of(original, h2Dialect);

    String query = createQueryBuilder.createCreateQuery(meta);

    assertThat(query).isEqualTo("CREATE TABLE PERSON (id BIGINT PRIMARY KEY,name CHARACTER VARYING,age INTEGER);");

  }

  @Test
  @DisplayName("1.2.2.1 @GeneratedValue와 @Column의 표기된 정보를 이용해 TABLE CREATE DDL을 생성합니다.")
  public void createDdlFromPkAndColumnAnnotations() {
    MetaData meta = MetaData.of(person, h2Dialect);

    String query = createQueryBuilder.createCreateQuery(meta);

    assertThat(query).isEqualTo("CREATE TABLE USERS (id BIGINT generated by default as identity PRIMARY KEY,nick_name CHARACTER VARYING ,old INTEGER ,email CHARACTER VARYING NOT NULL);");
  }

  @Test
  @DisplayName("1.2.3.1 @Table과 @Transient에 표기된 정보를 이용해 TABLE CREATE DDL을 생성합니다.")
  public void createDdlFromTableAndTransientAnnotations() {
    MetaData meta = MetaData.of(person, h2Dialect);

    String query = createQueryBuilder.createCreateQuery(meta);

    assertThat(query).isEqualTo("CREATE TABLE USERS (id BIGINT generated by default as identity PRIMARY KEY,nick_name CHARACTER VARYING ,old INTEGER ,email CHARACTER VARYING NOT NULL);");
  }

  @Test
  @DisplayName("1.2.2.2 @GeneratedValue의 PK 전략과 @Column의 Not Null, name에 맞게 DDL을 생성합니다.")
  public void createDdlFromGeneratedValueAndColumnAnnotations() {
    MetaData meta = MetaData.of(person2, h2Dialect);

    String query = createQueryBuilder.createCreateQuery(meta);

    assertThat(query).isEqualTo("CREATE TABLE PERSONFIXTURE2 (id BIGINT generated by default as identity PRIMARY KEY,nick_name CHARACTER VARYING ,old INTEGER ,email CHARACTER VARYING NOT NULL,index INTEGER );");

  }



}
