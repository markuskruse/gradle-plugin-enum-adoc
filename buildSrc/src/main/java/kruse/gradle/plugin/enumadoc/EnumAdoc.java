package kruse.gradle.plugin.enumadoc;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import org.gradle.internal.impldep.aQute.bnd.build.Run;

public class EnumAdoc extends DefaultTask {

  private String enumPath;
  private String outputFile;
  private String columns;

  public static void main(String[] args) {
    new EnumAdoc().generateDocFromEnum();
  }

  @TaskAction
  void generateDocFromEnum() {
    try {

      EnumConfig enumConfig = new EnumConfig(columns);
      List<List<String>> enumData = analyzeEnum(enumPath, enumConfig);

      try (FileWriter resultOutput = new FileWriter(getFileToWrite())) {
        resultOutput.write("|=======\n");
        for (List<String> enumField : enumData) {
          for (String value : enumField) {
            resultOutput.write("| " + value);
          }
          resultOutput.write(" |\n");
        }
        resultOutput.write("|=======\n");
      } catch (IOException e) {
        throw new RuntimeException("Error writing file: " + e.getMessage(), e);
      }

    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  private List<List<String>> analyzeEnum(String enumPath, EnumConfig enumConfig) throws ClassNotFoundException {
    List<List<String>> enumData = new ArrayList<>();
    Class<?> clazz = Class.forName(enumPath);
    if (!clazz.isEnum()) {
      throw new RuntimeException("Not an enum");
    }
    List<Object> enums = Arrays.asList(clazz.getEnumConstants());
    for (Object field : enums) {
      ArrayList<String> enumList = new ArrayList<>();
      enumData.add(enumList);
      enumList.add(callMethod(clazz, field, "name"));
      for (String name : enumConfig.getColumnNames()) {
        String methodName = enumConfig.get(name);
        enumList.add(callMethod(clazz, field, methodName));
      }
    }
    return enumData;
  }

  private File getFileToWrite() throws IOException {
    File file = new File(outputFile);
    if (!file.exists()) {
      if (!file.createNewFile()) {
        throw new RuntimeException("Couldn't create file to write");
      }
    }
    if (!file.exists() || !file.canWrite()) {
      throw new RuntimeException("File not writable");
    }
    return file;
  }

  private String callMethod(Class clazz, Object object, String methodName) {
    try {
      Method method = clazz.getMethod(methodName);
      Object result = method.invoke(object);
      return result.toString();
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }
    return "";
  }

  public String getEnumPath() {
    return enumPath;
  }

  public void setEnumPath(String enumPath) {
    this.enumPath = enumPath;
  }

  public String getOutputFile() {
    return outputFile;
  }

  public void setOutputFile(String outputFile) {
    this.outputFile = outputFile;
  }

  public String getColumns() {
    return columns;
  }

  public void setColumns(String columns) {
    this.columns = columns;
  }
}