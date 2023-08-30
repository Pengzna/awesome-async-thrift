package com.timecho.awesome.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.*;
import java.util.function.Supplier;
import java.util.stream.IntStream;

/**
 * JDK 协程 feature 说明：<a href="https://openjdk.org/jeps/425">...</a>
 *
 * <p>协程环境配置 参考： <a href="https://blog.csdn.net/weixin_45819587/article/details/119940717">...</a>
 * <a
 * href="https://www.imlc.me/zh/Intellij-IDEA-%E5%BC%80%E5%90%AF-JDK-%E9%A2%84%E8%A7%88%E7%89%B9%E6%80%A7">...</a>
 * https://www.jetbrains.com/idea/guide/tips/turn-on-preview-features/
 *
 * <p>开源协程框架：<a href="https://github.com/puniverse/quasar/">...</a>
 *
 * <p>其他使用 Ref：<a href="https://zhuanlan.zhihu.com/p/547863785">...</a> <a
 * href="https://zhuanlan.zhihu.com/p/579732019">...</a>
 *
 * <p>业务协程化改造思路： 1. 线程创建改为协程创建 2. 线程池替换为协程池
 *
 * <p>理想协程模型：主线程分派任务协程发起 RPC，任务协程同步阻塞，此时主线程可以分配其他协程处理其他任务，从而达到异步化效果
 *
 * <p>TODO：1. 协程效果测试（验证业务正确性）2. 协程线程性能测试（验证 IO 和 CPU 任务收益）
 */
public class LoomFiberUtils {

  private static Logger logger = LoggerFactory.getLogger(LoomFiberUtils.class);

  // JDK 官方不推荐池化协程，因为每个虚拟线程在其生存期内只能运行一个任务。相反，模型是无约束地创建虚拟线程。
  // 该 executor 将 unbound 的创建协程
  private static ExecutorService localFiberExecutor =
      Executors.newThreadPerTaskExecutor(Thread.ofVirtual().factory());

  public static <T> Future<T> submitCallable(Callable<T> fiberTask) {
    if (fiberTask == null) {
      logger.warn("Fiber supplier invalid");
      return null;
    }
    RunnableFuture<T> runnableFuture = new FutureTask<>(fiberTask);
    localFiberExecutor.execute(runnableFuture);
    return runnableFuture;
  }

  public static <T> Future<T> submitSupplier(Supplier<T> fiberTask) {
    if (fiberTask == null) {
      logger.warn("Fiber supplier invalid");
      return null;
    }
    return CompletableFuture.supplyAsync(fiberTask, localFiberExecutor);
  }

  public static ExecutorService asyncFiberExecutor() {
    ThreadFactory threadFactory = Thread.ofVirtual().factory();
    return Executors.newThreadPerTaskExecutor(threadFactory);

    // 传统的池化方法
    //    return Executors.newFixedThreadPool(fiberCount, threadFactory);
  }

  public static boolean isFiber() {
    return Thread.currentThread().isVirtual();
  }

  public static void main(String[] args) throws InterruptedException {
    try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
      IntStream.range(0, 1)
          .forEach(
              i -> {
                executor.submit(
                    () -> {
                      System.out.println(i);
                      return i;
                    });
              });
    }
  }
}
