package com.testvagrant.ekam.commons.tests;

import com.testvagrant.ekam.commons.io.DirectoryFinder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class DirectoryFinderTest {

  @Test
  public void findDirectoryAnyWhere() {
    Optional<String> data_sets_main = new DirectoryFinder().find("data_sets_main", "json");
    Assertions.assertTrue(data_sets_main.isPresent());
  }
}
