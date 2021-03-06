package com.testvagrant.ekam.commons.io;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

  private void collectDirectories(
      File rootFile, String directoryName, String validFileTypeInDirectory) {
    if (rootFile.exists() && rootFile.isDirectory()) {
      File[] files = rootFile.listFiles();
      assert files != null;
      for (File file : files) {
        if (file.isDirectory() && file.getName().equalsIgnoreCase(directoryName)) {
          List<File> filesWithExtension =
              new FileFinder(file.getPath()).findWithExtension(validFileTypeInDirectory);
          if (!filesWithExtension.isEmpty()) {
            directories.add(file.getPath());
          }
        } else {
          collectDirectories(file, directoryName, validFileTypeInDirectory);
        }
      }
    }
  }
}
