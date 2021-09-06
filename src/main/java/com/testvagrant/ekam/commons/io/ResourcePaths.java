package com.testvagrant.ekam.commons.io;

import com.testvagrant.ekam.commons.path.PathBuilder;

public class ResourcePaths {

  public static final String ROOT = String.valueOf(new PathBuilder(System.getProperty("user.dir")));

  public static final String SRC_ROOT =
      String.valueOf(new PathBuilder(System.getProperty("user.dir")).append("src"));

  public static final String TEST_RESOURCES =
      String.valueOf(new PathBuilder(SRC_ROOT).append("test").append("resources"));

  public static final String MAIN_RESOURCES =
      String.valueOf(new PathBuilder(SRC_ROOT).append("main").append("resources"));

  public static final String[] RESOURCES = {TEST_RESOURCES, MAIN_RESOURCES};

  public static final String APP_DIR =
      String.valueOf(new PathBuilder(System.getProperty("user.dir")).append("app"));

  public static final String EKAM_EXECUTION_TIMELINE =
      String.valueOf(new PathBuilder(ROOT).append("build").append("ekam-execution-timeline"));

  public static String getPath(String property) {
    String value = System.getProperty(property, "");
    return String.valueOf(
        value.isEmpty()
            ? new PathBuilder(ResourcePaths.TEST_RESOURCES)
            : new PathBuilder(ResourcePaths.TEST_RESOURCES).append(value));
  }

  public static String getPath(String rootPath, String property) {
    String value = System.getProperty(property, "");
    return String.valueOf(
        value.isEmpty() ? new PathBuilder(rootPath) : new PathBuilder(rootPath).append(value));
  }

  public static String getTestPath(String className, String testName) {
    return String.valueOf(
        new PathBuilder(EKAM_EXECUTION_TIMELINE).append(className).append(testName));
  }
}
