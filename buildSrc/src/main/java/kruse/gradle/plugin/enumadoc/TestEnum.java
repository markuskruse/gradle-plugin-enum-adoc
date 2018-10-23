package kruse.gradle.plugin.enumadoc;

public enum TestEnum {

  TESTERS("Quality assurance", "QA", 1),
  DESIGN("Desgin", "AD", 1),
  DEVELOPERS("System developers", "Dev", 3);

  private final String longTitle;
  private final String abbreviation;
  private final int coefficient;

  public String getLongTitle() {
    return longTitle;
  }

  public String getAbbreviation() {
    return abbreviation;
  }

  public int getCoefficient() {
    return coefficient;
  }

  TestEnum(String longTitle, String abbreviation, int coefficient) {

    this.longTitle = longTitle;
    this.abbreviation = abbreviation;
    this.coefficient = coefficient;
  }
}
