package com.testvagrant.ekam.commons.cache.clients;

import com.testvagrant.ekam.commons.cache.LocaleCache;

import static com.testvagrant.ekam.commons.cache.providers.LocaleProvider.localeProvider;

@SuppressWarnings({"unchecked", "rawtypes"})
public class LocaleCacheClient {

  private final LocaleCache localCache;

  public LocaleCacheClient() {
    this.localCache = localeProvider();
  }

  public synchronized <T> T getLocale(String key, Class<T> tClass) {
    return (T) localCache.get(key, tClass);
  }
}
