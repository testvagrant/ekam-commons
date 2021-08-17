package com.testvagrant.ekam.commons.cache.clients;

import com.testvagrant.ekam.commons.cache.DataStoreCache;

import static com.testvagrant.ekam.commons.cache.providers.DataStoreProvider.dataStoreProvider;

@SuppressWarnings({"unchecked", "rawtypes"})
public class DataStoreCacheClient {

  private final DataStoreCache dataStoreCache;

  public DataStoreCacheClient() {
    this.dataStoreCache = dataStoreProvider();
  }

  public <T> T getValue(String key) {
    return (T) dataStoreCache.get(key);
  }
}
