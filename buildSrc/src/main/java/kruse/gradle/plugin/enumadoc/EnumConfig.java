package kruse.gradle.plugin.enumadoc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnumConfig {

  private final ArrayList<String> columnNames;
  private final Map<String, String> columnMap;

  public EnumConfig(String columns) {
    List<String> columnPairs = Arrays.asList(columns.split("&"));
    columnNames = new ArrayList<>();
    columnMap = new HashMap<>();
    columnPairs.forEach(pair -> {
      String[] tuple = pair.split(":");
      columnNames.add(tuple[1]);
      columnMap.put(tuple[1], tuple[0]);
    });
  }

  public ArrayList<String> getColumnNames() {
    return columnNames;
  }

  public String get(String key) {
    return columnMap.get(key);
  }
}
