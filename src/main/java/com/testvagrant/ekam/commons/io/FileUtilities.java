package com.testvagrant.ekam.commons.io;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.util.Optional;

public class FileUtilities {

  private static FileUtilities fileUtils;

  private FileUtilities() {}

  public static FileUtilities fileUtils() {
    if (fileUtils == null) {
      synchronized (FileUtilities.class) {
        if (fileUtils == null) {
          fileUtils = new FileUtilities();
        }
      }
    }

    return fileUtils;
  }

  public synchronized String createDirectory(String dirPath, boolean deleteIfExists) {
    File file = new File(dirPath);
    if (file.exists() && file.isDirectory() && deleteIfExists) {
      try {
        FileUtils.deleteDirectory(file);
      } catch (Exception ex) {
        throw new RuntimeException(ex.getMessage());
      }
    }
    if (!file.exists()) {
      boolean directoryCreated = file.mkdirs();
      if (!directoryCreated) {
        throw new RuntimeException(String.format("'%s' directory couldn't be created.", dirPath));
      }
    }

    return file.getAbsolutePath();
  }

  public synchronized String createDirectory(String dirPath) {
    return createDirectory(dirPath, true);
  }

  public synchronized void writeFile(String filePath, String data) {
    try {
      FileWriter file = new FileWriter(filePath);
      file.write(data);
      file.close();
    } catch (Exception ex) {
      throw new RuntimeException(
          String.format("Error writing to file: '%s'.\nError:\n%s", filePath, ex.getMessage()));
    }
  }

  public Optional<File> findResource(String resourceName, String env) {
    File tempFile = new FileFinder("", env).find(resourceName);
    return Optional.ofNullable(tempFile);
  }
}
