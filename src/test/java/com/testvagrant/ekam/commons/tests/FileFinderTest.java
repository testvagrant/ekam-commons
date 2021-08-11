package com.testvagrant.ekam.commons.tests;

import com.google.gson.reflect.TypeToken;
import com.testvagrant.ekam.commons.io.FileFinder;
import com.testvagrant.ekam.commons.io.GsonParser;
import com.testvagrant.ekam.commons.io.ResourcePaths;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.SetSystemProperty;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class FileFinderTest {

  @Test
  @SetSystemProperty(key = "env", value = "")
  public void shouldFindFileFromEnvIfSpecified() {
    File hosts = new FileFinder(ResourcePaths.TEST_RESOURCES, "qa").find("hosts.json");
    Map<String, String> maps =
        new GsonParser()
            .deserialize(hosts.getPath(), new TypeToken<Map<String, String>>() {}.getType());
    Assertions.assertEquals(maps.get("host"), "qa");
  }

  @Test
  @SetSystemProperty(key = "env", value = "")
  public void shouldFindFileWhenRootFileIsNotSpecified() {
    File hosts = new FileFinder("", "qa").find("hosts.json");
    Map<String, String> maps =
        new GsonParser()
            .deserialize(hosts.getPath(), new TypeToken<Map<String, String>>() {}.getType());
    Assertions.assertEquals(maps.get("host"), "qa");
  }

  @Test
  @SetSystemProperty(key = "env", value = "")
  public void validateFirstFoundFileReturnedWhenEnvIsNotSpecified() {
    File hosts = new FileFinder(ResourcePaths.TEST_RESOURCES).find("hosts", ".json");
    Map<String, String> maps =
        new GsonParser()
            .deserialize(hosts.getPath(), new TypeToken<Map<String, String>>() {}.getType());
    Assertions.assertEquals(maps.get("host"), "abc");
  }

  @Test
  public void shouldFindFileByExtension() {
    String fileDirectory =
        Paths.get(ResourcePaths.TEST_RESOURCES, "file_finder/find_by_extensions").toString();
    List<File> files = new FileFinder(fileDirectory).findWithExtension(".json");
    List<File> xmlFiles = new FileFinder(fileDirectory).findWithExtension(".xml");
    Assertions.assertEquals(files.size(), 2);
    Assertions.assertEquals(xmlFiles.size(), 1);
  }

  @Test
  public void shouldFindFileWhenFullPathSpecified() {
    String filePath = "file_name_with_extension.json";
    File file = new FileFinder(ResourcePaths.TEST_RESOURCES).find(filePath);
    Map<String, String> map =
        new GsonParser()
            .deserialize(file.getPath(), new TypeToken<Map<String, String>>() {}.getType());

    Assertions.assertEquals(map.get("message"), "I'm found!");
  }
}
