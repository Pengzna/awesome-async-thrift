package com.timecho.awesome.utils;

import co.paralleluniverse.common.monitoring.MonitorType;
import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.FiberForkJoinScheduler;
import co.paralleluniverse.fibers.FiberScheduler;
import co.paralleluniverse.strands.SuspendableRunnable;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class QuasarFiberUtils {
  private static final FiberScheduler scheduler =
      new FiberForkJoinScheduler("IoTDB-Fiber-Pool", 20, null, MonitorType.JMX, false);
  private static final Logger logger = LoggerFactory.getLogger(QuasarFiberUtils.class);

  public static boolean submit(
      List<SuspendableRunnable> fiberTasks, long maxWaitTimeMs, String taskName) {
    if (fiberTasks == null || fiberTasks.size() == 0) {
      return false;
    }
    if (maxWaitTimeMs <= 0) {
      throw new RuntimeException("maxWaitSeconds is invalid.");
    }

    final CountDownLatch countDownLatch = new CountDownLatch(fiberTasks.size());
    List<Fiber> fibers = Lists.newArrayList();
    for (int i = 0; i < fiberTasks.size(); i++) {
      Fiber fiber =
          new Fiber<Void>(taskName + (i + 1), scheduler, fiberTasks.get(0)) {
            @Override
            protected void onCompletion() {
              countDownLatch.countDown();
              super.onCompletion();
            }

            @Override
            protected void onException(Throwable t) {
              countDownLatch.countDown();
              logger.error(t.getMessage(), t);
              super.onException(t);
            }
          }.start();
      fibers.add(fiber);
    }

    try {
      countDownLatch.await(maxWaitTimeMs, TimeUnit.MILLISECONDS);
    } catch (InterruptedException e) {
      logger.error("Fiber tasks exceed time limit: " + e.getMessage(), e);
    } finally {
      fibers.forEach(
          fiber -> {
            if (fiber.isAlive()) {
              try {
                fiber.cancel(true);
              } catch (Exception e) {
                logger.error("Fiber cancel failed: " + e.getMessage(), e);
              }
            }
          });
    }
    return true;
  }
}
