package persistence.meta;

import org.h2.table.MetaTable;
import persistence.dialect.Dialect;

import java.util.List;


// 1) Columns 받고
// 2) columns를 data type에 맞게 받고 sql로 넘겨주기 (여기서 mapper 가 dialect에서 가져오면될듯)
//   a) nullable 값 받기 --> 값받아서  이거
//   b) transient 도 -> annotation 읽고 db 만 안잡기
// 3) value 값도 맵핑하기
// 4) table 네임은 가볍게 가져오기
// 5) annotation으로 id, column 값 가져오기 name(db 값 가져오기)
//   a) generatedValue(Identity 관련해서 어떻게 해야할지 생각해보기)
//   b)

public class MetaData {
  private final MetaDataTable metaDataTable;
  private final MetaDataColumns metaDataColumns;
  private static final String CREATE_TABLE_CLAUSE = "%s (%s)";

  private MetaData(MetaDataTable metaDataTable, MetaDataColumns metaDataColumns) {
    this.metaDataTable = metaDataTable;
    this.metaDataColumns = metaDataColumns;
  }

  public static MetaData of(Class<?> clazz, Dialect dialect) {
    return new MetaData(MetaDataTable.of(clazz), MetaDataColumns.of(clazz, dialect));
  }

  public String getCreateClause() {
    return String.format(CREATE_TABLE_CLAUSE, metaDataTable.getName(), metaDataColumns.getColumns());
  }

  public String getDropClause() {
    return metaDataTable.getName();
  }
}

