package com.testvagrant.ekam.commons.io;

import com.google.common.io.Files;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class FileFinder {

  private final String path;
  private final List<File> allFiles;
  private final List<File> matchingFiles;
  private final String env;
  private boolean fileFound;
  private File fileToSearch;

  public FileFinder() {
    this(System.getProperty("user.dir"));
  }

  public FileFinder(String path) {
    this.path = getPath(path);
    allFiles = new ArrayList<>();
    env = System.getProperty("env", "");
    matchingFiles = new ArrayList<>();
  }

  public FileFinder(String path, String env) {
    this.path = getPath(path);
    this.env = env;
    allFiles = new ArrayList<>();
    matchingFiles = new ArrayList<>();
  }

  private String getPath(String path) {
    return Objects.isNull(path) || path.isEmpty() ? ResourcePaths.ROOT : path;
  }

  public List<File> findWithExtension(String fileExtension) {
    collectFiles(new File(path), fileExtension);
    if (!env.isEmpty()) {
      return allFiles.stream()
          .filter(file -> file.getPath().contains(env))
          .collect(Collectors.toList());
    }
    return allFiles;
  }

  public File find(String fileName, String fileExtension) {
    if (env.isEmpty()) {
      collectFile(new File(path), fileName, fileExtension);
    } else {
      collectFiles(new File(path), fileName, fileExtension);
      Optional<File> first =
          matchingFiles.stream().filter(file -> file.getPath().contains(env)).findFirst();
      fileToSearch =
          first.orElseThrow(
              () ->
                  new RuntimeException(
                      String.format(
                          "File %s%s not found in %s env and path %s",
                          fileName, fileExtension, env, path)));
    }
    return fileToSearch;
  }

  public File find(String fileNameWithExtension) {
    String fileExtension = Files.getFileExtension(fileNameWithExtension);
    String fileName = fileNameWithExtension.replaceAll("." + fileExtension, "");
    return find(fileName, "." + fileExtension);
  }

  private void collectFiles(File rootFile, String fileExtensionToSearch) {
    if (rootFile.exists() && rootFile.isDirectory()) {
      File[] files = rootFile.listFiles();
      assert files != null;
      for (File file : files) {
        if (!file.isDirectory() || isFileFound(fileExtensionToSearch, file)) {
          fileFound = isFileFound(fileExtensionToSearch, file);
          if (fileFound) {
            allFiles.add(file);
          }
        } else {
          collectFiles(file, fileExtensionToSearch);
        }
      }
    }
  }

  private boolean isFileFound(String fileExtensionToSearch, File file) {
    return file.getName().endsWith(fileExtensionToSearch);
  }

  private boolean isFileFound(File file, String fileName, String fileExtensionToSearch) {
    return file.getName().equals(String.format("%s%s", fileName, fileExtensionToSearch));
  }

  private void collectFile(File rootFile, String fileName, String fileExtensionToSearch) {
    if (rootFile.exists() && rootFile.isDirectory()) {
      File[] files = rootFile.listFiles();
      assert files != null;
      for (File file : files) {
        if (!file.isDirectory() || isFileFound(file, fileName, fileExtensionToSearch)) {
          fileFound = isFileFound(file, fileName, fileExtensionToSearch);
          if (fileFound) {
            fileToSearch = file;
            break;
          }
        } else {
          collectFile(file, fileName, fileExtensionToSearch);
        }
      }
    }
  }

  private void collectFile(File rootFile, String fileName) {
    if (rootFile.exists() && rootFile.isDirectory()) {
      File[] files = rootFile.listFiles();
      assert files != null;
      for (File file : files) {
        if (!file.isDirectory()) {
          fileFound = file.getName().equalsIgnoreCase(fileName);
          if (fileFound) {
            fileToSearch = file;
            break;
          }
        } else {
          collectFile(file, fileName);
        }
      }
    }
  }

  private void collectFiles(File rootFile, String fileName, String fileExtensionToSearch) {
    if (rootFile.exists() && rootFile.isDirectory()) {
      File[] files = rootFile.listFiles();
      assert files != null;
      for (File file : files) {
        if (!file.isDirectory()) {
          fileFound = file.getName().equals(String.format("%s%s", fileName, fileExtensionToSearch));
          if (fileFound) matchingFiles.add(file);
        } else {
          collectFiles(file, fileName, fileExtensionToSearch);
        }
      }
    }
  }
}
