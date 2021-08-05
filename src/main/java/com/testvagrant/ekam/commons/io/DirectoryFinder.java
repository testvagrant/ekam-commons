package com.testvagrant.ekam.commons.io;

import com.google.common.io.Files;

import java.io.File;
import java.util.*;

public class DirectoryFinder {

  List<String> directories;

  public DirectoryFinder() {
    this.directories = new ArrayList<>();
  }

  public Optional<String> find(String directoryName, String validFileType) {
    File rootFile = new File(System.getProperty("user.dir"));
    collectDirectories(rootFile, directoryName, validFileType);
    return directories.stream()
        .filter(directoryPath -> directoryPath.toLowerCase().contains(directoryName))
        .findFirst();
  }

  private void collectDirectories(File rootFile, String directoryName, String validFileTypeInDirectory) {
    if (rootFile.exists() && rootFile.isDirectory()) {
      File[] files = rootFile.listFiles();
      assert files != null;
      for (File file : files) {
        if (file.isDirectory() && file.getName().equalsIgnoreCase(directoryName)) {
          boolean isValidDirectory = Arrays
                  .stream(Objects.requireNonNull(file.listFiles()))
                  .anyMatch(file1 -> Files.getFileExtension(file1.getName()).contains(validFileTypeInDirectory));
          if(isValidDirectory) {
            directories.add(file.getPath());
          }
        } else {
          collectDirectories(file, directoryName, validFileTypeInDirectory);
        }
      }
    }
  }
}
