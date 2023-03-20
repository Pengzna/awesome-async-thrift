/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.timecho.awesome.thrift;

import org.apache.thrift.TProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.CountDownLatch;

public abstract class ThriftService implements IService {

  private static final Logger LOGGER = LoggerFactory.getLogger(ThriftService.class);

  public static final String STATUS_UP = "UP";
  public static final String STATUS_DOWN = "DOWN";

  protected String mbeanName;
  protected AbstractThriftServiceThread thriftServiceThread;
  protected TProcessor processor;

  private CountDownLatch stopLatch;

  public String getRPCServiceStatus() {
    if (thriftServiceThread == null) {
      LOGGER.debug("Start latch is null when getting status");
    } else {
      LOGGER.debug("Start status is {} when getting status", thriftServiceThread.isServing());
    }
    if (stopLatch == null) {
      LOGGER.debug("Stop latch is null when getting status");
    } else {
      LOGGER.debug("Stop latch is {} when getting status", stopLatch.getCount());
    }

    if (thriftServiceThread != null && thriftServiceThread.isServing()) {
      return STATUS_UP;
    } else {
      return STATUS_DOWN;
    }
  }

  @Override
  public void start() {
    JMXService.registerMBean(this, mbeanName);
    startService();
  }

  @Override
  public void stop() {
    stopService();
    JMXService.deregisterMBean(mbeanName);
  }

  boolean setSyncImpl = false;
  boolean setAsyncImpl = false;

  public void initSyncServiceImpl(Object serviceImpl) {
    setSyncImpl = true;
  }

  public void initAsyncServiceImpl(Object serviceImpl) {
    setAsyncImpl = true;
  }

  public abstract void initTProcessor()
    throws ClassNotFoundException, IllegalAccessException, InstantiationException,
    NoSuchMethodException, InvocationTargetException;

  public abstract void initThriftServiceThread()
    throws IllegalAccessException, InstantiationException, ClassNotFoundException;

  public abstract String getBindIP();

  public abstract int getBindPort();

  @SuppressWarnings("squid:S2276")
  public void startService() throws StartupException {
    if (STATUS_UP.equals(getRPCServiceStatus())) {
      LOGGER.info(
        "{}: {} has been already running now",
        IoTDBConstant.GLOBAL_DB_NAME,
        this.getID().getName());
      return;
    }
    LOGGER.info("{}: start {}...", IoTDBConstant.GLOBAL_DB_NAME, this.getID().getName());
    try {
      reset();
      initTProcessor();
      if (!setSyncImpl && !setAsyncImpl) {
        throw new StartupException(
          getID().getName(), "At least one service implementataion should be set.");
      }
      initThriftServiceThread();
      thriftServiceThread.setThreadStopLatch(stopLatch);
      thriftServiceThread.start();

      while (!thriftServiceThread.isServing()) {
        // sleep 100ms for waiting the rpc server start.
        Thread.sleep(100);
      }
    } catch (InterruptedException
             | ClassNotFoundException
             | IllegalAccessException
             | InstantiationException
             | NoSuchMethodException
             | InvocationTargetException e) {
      Thread.currentThread().interrupt();
      throw new StartupException(this.getID().getName(), e.getMessage());
    }

    LOGGER.info(
      "{}: start {} successfully, listening on ip {} port {}",
      IoTDBConstant.GLOBAL_DB_NAME,
      this.getID().getName(),
      getBindIP(),
      getBindPort());
  }

  private void reset() {
    thriftServiceThread = null;
    stopLatch = new CountDownLatch(1);
  }

  public void restartService() {
    stopService();
    startService();
  }

  public void stopService() {
    if (STATUS_DOWN.equals(getRPCServiceStatus())) {
      LOGGER.info("{}: {} isn't running now", IoTDBConstant.GLOBAL_DB_NAME, this.getID().getName());
      return;
    }
    LOGGER.info("{}: closing {}...", IoTDBConstant.GLOBAL_DB_NAME, this.getID().getName());
    if (thriftServiceThread != null) {
      thriftServiceThread.close();
    }
    try {
      stopLatch.await();
      reset();
      LOGGER.info(
        "{}: close {} successfully", IoTDBConstant.GLOBAL_DB_NAME, this.getID().getName());
    } catch (InterruptedException e) {
      LOGGER.error(
        "{}: close {} failed because: ", IoTDBConstant.GLOBAL_DB_NAME, this.getID().getName(), e);
      Thread.currentThread().interrupt();
    }
  }
}
