import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.strands.Strand;
import co.paralleluniverse.strands.SuspendableRunnable;
import com.google.common.collect.Maps;
import com.timecho.awesome.utils.LoomFiberUtils;
import com.timecho.awesome.utils.QuasarFiberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 测试类，结果如下： M1 max 16g 512g
 *
 * <p>10_0000: LoomFiber：488；LoomFiber2：223；Thread：6029
 *
 * <p>100_0000: LoomFiber：3667；LoomFiber2：2498；Thread：61296
 */
public class FiberTest {
  private static final Logger logger = LoggerFactory.getLogger(FiberTest.class);
  private static final AtomicInteger result = new AtomicInteger(0);
  private static int cycleCount = 1;
  private static Map<String, Runnable> testFuncMap = Maps.newHashMap();

  static {
    testFuncMap.put(
        "Thread",
        () -> {
          try {
            testThread();
          } catch (InterruptedException e) {
            throw new RuntimeException(e);
          }
        });
    testFuncMap.put(
        "LoomFiber",
        () -> {
          try {
            testLoomFiber();
          } catch (InterruptedException e) {
            throw new RuntimeException(e);
          }
        });
    testFuncMap.put(
        "LoomFiber2",
        () -> {
          try {
            testLoomFiber2();
          } catch (InterruptedException e) {
            throw new RuntimeException(e);
          }
        });
    //        testFuncMap.put(
    //            "Raw QuasarFiber",
    //            () -> {
    //              try {
    //                testRawQuasarFiber();
    //              } catch (InterruptedException e) {
    //                throw new RuntimeException(e);
    //              }
    //            });
    //        testFuncMap.put("QuasarFiberUtil", FiberTest::testQuasarFiberUtil);
  }

  public static void main(String[] args) {
    // -javaagent:/Users/pengjunzhi/.m2/repository/co/paralleluniverse/quasar-core/0.8.0/quasar-core-0.8.0.jar
    // 模拟业务逻辑：调用耗时方式 1W 次，然后将结果累加；
    // --enable-preview
    testFuncMap.forEach(
        (k, v) -> {
          logger.warn(k + " test begin");
          logger.warn("模拟循环调用次数：{}", cycleCount);
          long start = System.currentTimeMillis();
          v.run();
          long end = System.currentTimeMillis();
          logger.warn("耗时 = {} Ms, 计算结果 = {}", (end - start), result.get());
          logger.warn(k + " test end");
        });
  }

  private static void testThread() throws InterruptedException {
    final CountDownLatch latch = new CountDownLatch(cycleCount);
    // 一般线程池的 max size 为 200 左右，这里假设线程打满。
    ExecutorService executorService = Executors.newFixedThreadPool(200);
    for (int i = 0; i < cycleCount; i++) {
      Thread t =
          new Thread() {
            @Override
            public void run() {
              try {
                int invokeResult = mockInvoke();
                result.addAndGet(invokeResult);
                latch.countDown();
              } catch (Exception e) {
                logger.error(e.getMessage(), e);
              }
            }
          };
      executorService.execute(t);
    }
    executorService.shutdown();
    latch.await();
  }

  private static void testLoomFiber() throws InterruptedException {
    final CountDownLatch latch = new CountDownLatch(cycleCount);
    for (int i = 0; i < cycleCount; i++) {
      LoomFiberUtils.submitCallable(
          () -> {
            try {

              if (Thread.currentThread().isVirtual()) {
                System.out.println(Thread.currentThread().getName());
                System.out.println(Thread.currentThread().threadId());
                logger.info("Virtual!");
              }
              // 线程使用情况
              ThreadMXBean threads = ManagementFactory.getThreadMXBean();
              System.out.println("========begin=========");
              for (Long threadId : threads.getAllThreadIds()) {
                System.out.printf(
                    "threadId: %d | threadName: %s%n",
                    threadId, threads.getThreadInfo(threadId).getThreadName());
              }
              System.out.println("========end=========");
              int invokeResult = mockInvoke();
              result.addAndGet(invokeResult);
              latch.countDown();
            } catch (Exception e) {
              logger.error(e.getMessage(), e);
            }
            return null;
          });
    }
    latch.await();
  }

  private static void testLoomFiber2() throws InterruptedException {
    final CountDownLatch latch = new CountDownLatch(cycleCount);
    for (int i = 0; i < cycleCount; i++) {
      LoomFiberUtils.submitSupplier(
          () -> {
            try {
              int invokeResult = mockInvoke();
              result.addAndGet(invokeResult);
              latch.countDown();
            } catch (Exception e) {
              logger.error(e.getMessage(), e);
            }
            return null;
          });
    }
    latch.await();
  }

  private static void testRawQuasarFiber() throws InterruptedException {
    final CountDownLatch latch = new CountDownLatch(cycleCount);
    for (int i = 0; i < cycleCount; i++) {
      Fiber fiber =
          new Fiber<>(
                  "Caller",
                  new SuspendableRunnable() {
                    @Override
                    public void run() throws SuspendExecution, InterruptedException {
                      int invokeResult = mockInvoke();
                      result.addAndGet(invokeResult);
                      latch.countDown();
                    }
                  })
              .start();
    }
    latch.await();
  }

  private static void testQuasarFiberUtil() {
    List<SuspendableRunnable> fiberTaskList = new ArrayList<>();
    for (int i = 0; i < cycleCount; i++) {
      fiberTaskList.add(
          new SuspendableRunnable() {
            @Override
            public void run() throws SuspendExecution, InterruptedException {
              int invokeResult = mockInvoke();
              result.addAndGet(invokeResult);
            }
          });
    }
    QuasarFiberUtils.submit(fiberTaskList, 30, "PerformanceTest-QuasarFiber");
  }

  /** 该方法在调用后，将会主动 sleep 10 ms，然后返回数据； */
  private static int mockInvoke() throws SuspendExecution, InterruptedException {
    Strand.sleep(10);
    return 1;
  }
}
