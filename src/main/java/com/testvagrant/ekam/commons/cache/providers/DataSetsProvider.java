package com.testvagrant.ekam.commons.cache.providers;

import com.google.common.io.Files;
import com.testvagrant.ekam.commons.cache.DataSetsCache;
import com.testvagrant.ekam.commons.io.DirectoryFinder;
import com.testvagrant.ekam.commons.io.FileFinder;
import com.testvagrant.ekam.commons.io.GsonParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static com.testvagrant.ekam.commons.constants.ResourcesConfigKeys.DATASETS;

@SuppressWarnings({"rawtypes", "unchecked"})
public class DataSetsProvider {

  private static DataSetsCache<Object> cacheProvider;

  private DataSetsProvider() {}

  public static DataSetsCache<Object> dataSetsProvider() {
    if (cacheProvider == null) {
      synchronized (DataSetsProvider.class) {
        if (cacheProvider == null) {
          cacheProvider = new DataSetsProvider().get();
        }
      }
    }
    return cacheProvider;
  }

  public DataSetsCache load() {
    DataSetsCache dataSetsCache = new DataSetsCache();
    String env = System.getProperty(DATASETS.DATASETS_ENV, System.getProperty("env", ""));
    Optional<String> dataSetsPathOptional = new DirectoryFinder().find(System.getProperty(DATASETS.DIR), "json");
    String dataSetsPath = dataSetsPathOptional.orElse("");
    List<File> fileList = dataSetsPath.isEmpty()? new ArrayList<>() : new FileFinder(dataSetsPath, env)
            .findWithExtension(".json");

    fileList
            .stream().parallel()
            .forEach(file -> transform(dataSetsCache, file));

    return dataSetsCache;
  }

  private DataSetsCache get() {
    return load();
  }

  private void transform(DataSetsCache dataSetsCache, File file) {
    try {
      GsonParser gsonParser = new GsonParser();
      String dataSet =
          String.valueOf(new GsonParser().deserialize(new FileReader(file), Object.class));

      if (dataSet.trim().startsWith("[")) {
        List dataSetList = gsonParser.deserialize(new FileReader(file), List.class);
        String fileName =
            file.getName().replaceAll("." + Files.getFileExtension(file.getName()), "");
        dataSetsCache.put(fileName, dataSetList);
      } else {
        transformAsMap(dataSetsCache, file);
      }
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  private <T> void transformAsMap(DataSetsCache dataSetsCache, File file)
      throws FileNotFoundException {
    Map<String, T> dataSetMap = new GsonParser().deserialize(new FileReader(file), Map.class);
    AtomicInteger listValueCounter = new AtomicInteger();

    dataSetMap.entrySet().stream()
        .flatMap(
            entry -> {
              String key = entry.getKey().trim().toLowerCase();
              T value = entry.getValue();

              if (value instanceof List) {
                dataSetsCache.put(key, value);
                listValueCounter.set(0);
                return ((List) value)
                    .stream()
                        .map(
                            val -> {
                              String formattedKey =
                                  String.format("%s_%s", key, listValueCounter.getAndIncrement());
                              return new AbstractMap.SimpleEntry<>(formattedKey, val);
                            });
              } else {
                return Stream.of(value).map(v -> new AbstractMap.SimpleEntry<>(key, v));
              }
            })
        .forEach(
            item -> {
              Map.Entry entry = (Map.Entry) item;
              dataSetsCache.put(entry.getKey().toString(), entry.getValue());
            });
  }
}
