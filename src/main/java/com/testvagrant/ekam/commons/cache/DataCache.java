package com.testvagrant.ekam.commons.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableMap;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

public class DataCache<Value> {

  protected LoadingCache<String, Value> cache;

  public DataCache() {
    this.cache = build(cacheLoader());
  }

  protected LoadingCache<String, Value> build(
      CacheLoaderCondition<String, Value> cacheLoaderCondition) {
    return CacheBuilder.newBuilder()
        .maximumSize(getCacheSize())
        .expireAfterWrite(4, TimeUnit.HOURS)
        .build(buildCacheLoader(cacheLoaderCondition));
  }

  private long getCacheSize() {
    try {
      return Long.parseLong(System.getProperty("cache.size", "10000"));
    } catch (NumberFormatException e) {
      return 10000;
    }
  }

  public void put(String key, Value value) {
    cache.put(key, value);
  }

  public Optional<Value> get(String key) {
    Value value = cache.getIfPresent(key);
    return Optional.ofNullable(value);
  }

  public ImmutableMap<String, Value> getAll(List<String> keys) {
    return cache.getAllPresent(keys);
  }

  public ImmutableMap<String, Value> getAll(String... keys) {
    return cache.getAllPresent(Arrays.asList(keys));
  }

  public void invalidate(String key) {
    cache.invalidate(key);
  }

  public void invalidateAll() {
    cache.invalidateAll();
  }

  public synchronized boolean isPresent(Predicate<Value> predicate) {
    return anyMatch(cache, predicate);
  }

  public synchronized boolean isPresent(String key) {
    return anyMatch(cache, key);
  }

  public Value get(Predicate<Value> predicate) throws ExecutionException {
    throw new UnsupportedOperationException();
  }

  public long size() {
    return cache.size();
  }

  protected boolean anyMatch(LoadingCache<String, Value> cache, Predicate<Value> predicate) {
    return cache.asMap().values().stream().anyMatch(predicate);
  }

  protected boolean anyMatch(LoadingCache<String, Value> cache, String key) {
    return cache.asMap().containsKey(key);
  }

  protected CacheLoaderCondition<String, Value> cacheLoader() {
    return (key) -> cache.getIfPresent(key);
  }

  private CacheLoader<String, Value> buildCacheLoader(
      CacheLoaderCondition<String, Value> cacheLoaderCondition) {
    return new CacheLoader<String, Value>() {
      @Override
      public Value load(String key) {
        return cacheLoaderCondition.condition(key);
      }
    };
  }
}
