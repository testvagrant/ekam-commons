package com.testvagrant.ekam.commons.dataclients;

import com.google.gson.reflect.TypeToken;
import com.testvagrant.ekam.commons.cache.clients.DataSetsCacheClient;

import java.util.List;

public class ListDataClient extends DataSetsCacheClient {

  public <T> List<T> getList(String fileName) {
    return getValue(fileName, new TypeToken<List<T>>() {}.getType());
  }
}
