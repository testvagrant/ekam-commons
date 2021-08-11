package com.testvagrant.ekam.commons.cache;

import com.google.common.cache.LoadingCache;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SharedDataCache<Value> extends DataCache<Value> {

  protected LoadingCache<String, Value> availableCache;
  protected LoadingCache<String, Value> engagedCache;

  public SharedDataCache() {
    availableCache = build((key) -> availableCache.getIfPresent(key));
    engagedCache = build((key) -> engagedCache.getIfPresent(key));
  }

  public synchronized void put(String key, Value value) {
    super.put(key, value);
    availableCache.put(key, value);
  }

  public synchronized Optional<Value> get(String key, boolean lockRecord) {
    Value value = getValue(availableCache, key);
    if (Objects.isNull(value)) return Optional.empty();
    if (lockRecord) lock(key);
    return Optional.of(value);
  }

  protected synchronized void lock(String key) {
    String finalKey = getFinalKey(availableCache, key);
    Value value = getValue(availableCache, finalKey);
    engagedCache.put(finalKey, value);
    availableCache.invalidate(finalKey);
  }

  public synchronized void release(String key, Predicate<Map.Entry<String, Value>> predicate) {
    String finalKey = getFinalKey(engagedCache, key, predicate);
    Value value = getValue(engagedCache, finalKey);
    release(finalKey, value);
  }

  public synchronized void release(Predicate<Map.Entry<String, Value>> predicate) {
    String finalKey = getFinalKey(engagedCache, predicate);
    Value value = getValue(engagedCache, finalKey);
    release(finalKey, value);
  }

  public synchronized void release(String key) {
    String finalKey = getFinalKey(engagedCache, key);
    Value value = getValue(engagedCache, finalKey);
    release(finalKey, value);
  }

  public boolean isAvailable(String key) {
    return anyMatch(availableCache, key);
  }

  public boolean isEngaged(String key) {
    return anyMatch(engagedCache, key);
  }

  protected <T> String getFinalKey(
      LoadingCache<String, Value> cache,
      String key,
      Predicate<Map.Entry<String, Value>> predicate) {
    boolean fullFledgedKey = cache.asMap().containsKey(key);
    if (!fullFledgedKey) {
      String finalKey = key;
      Optional<Map.Entry<String, Value>> finalEntry =
          cache.asMap().entrySet().stream()
              .filter(entry -> entry.getKey().startsWith(finalKey))
              .filter(predicate)
              .findFirst();
      if (finalEntry.isPresent()) {
        key = finalEntry.get().getKey();
      }
    }
    return key;
  }

  protected <T> String getFinalKey(
      LoadingCache<String, Value> cache, Predicate<Map.Entry<String, Value>> predicate) {
    Optional<Map.Entry<String, Value>> finalEntry =
        cache.asMap().entrySet().stream().filter(predicate).findFirst();
    return finalEntry.isPresent() ? finalEntry.get().getKey() : "";
  }

  protected String getFinalKey(LoadingCache<String, Value> cache, String key) {
    List<String> matchingKeys =
        cache.asMap().keySet().stream()
            .filter(k -> k.toLowerCase().startsWith(key + "_"))
            .collect(Collectors.toList());

    try {
      return cache.asMap().containsKey(key) && !(cache.get(key) instanceof List)
          ? key
          : matchingKeys.stream().findFirst().orElse(key);
    } catch (Exception ex) {
      throw new RuntimeException(ex.getMessage());
    }
  }

  protected Value getValue(LoadingCache<String, Value> cache, String key) {
    String finalKey = getFinalKey(cache, key);
    return cache.getIfPresent(finalKey);
  }

  @Override
  public long size() {
    return availableCache.size();
  }

  private void release(String key, Value value) {
    if (key.isEmpty()) return;
    availableCache.put(key, value);
    engagedCache.invalidate(key);
  }
}
