package com.timecho.awesome.client;

import org.apache.thrift.async.AsyncMethodCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmptyAsyncHandler implements AsyncMethodCallback<Void>  {

  private static final Logger LOGGER = LoggerFactory.getLogger(EmptyAsyncHandler.class);

  @Override
  public void onComplete(Void unused) {
    // Do nothing
  }

  @Override
  public void onError(Exception e) {
    LOGGER.error("Error when executing activateDNode", e);
  }
}
