package com.testvagrant.ekam.commons.dataclients;

import com.testvagrant.ekam.commons.cache.clients.DataSetsCacheClient;

public class DataClient extends DataSetsCacheClient {

  public <T> T get(String key, Class<T> tClass) {
    return getValue(key, tClass, false);
  }
}
