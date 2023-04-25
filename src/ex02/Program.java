package ex02;

import java.util.ArrayList;
import java.util.Random;

public class Program {

  public static void main(String[] args) throws InterruptedException {
    if (args.length == 2) {
      if (args[0].substring(0, 12).equals("--arraySize=") &&
          args[1].substring(0, 15).equals("--threadsCount=")) {
        int arraySize = 0;
        int threadsCount = 0;
        try {
          arraySize = Integer.parseInt(args[0].substring(12));
          threadsCount = Integer.parseInt(args[1].substring(15));
        } catch (Exception e) {
          System.out.println("Wrong Input");
        }

        if (arraySize > 0 && threadsCount > 0 && arraySize >= threadsCount &&
            arraySize <= 2000000) {
          Random random = new Random();

          int[] arr = new int[arraySize];
          int res = 0;
          for (int i = 0; i < arraySize; i++) {
            arr[i] = random.nextInt(0, 1000);
            System.out.println(arr[i]);
            res = res + arr[i];
          }
          System.out.printf("Sum: %d\n", res);
          int part = arraySize / threadsCount;
          ArrayList<CountThread> threads = new ArrayList<>();
          int zapolneno = 0;
          for (int j = 0; j < threadsCount - 1; j++) {
            threads.add(
                new CountThread(j + 1, arr, j * part, (j + 1) * part - 1));
            zapolneno = zapolneno + part;
          }
          threads.add(
              new CountThread(threadsCount, arr, zapolneno, arraySize - 1));
          for (int i = 0; i < threadsCount; i++) {
            threads.get(i).run();
            threads.get(i).join();
          }
          res = 0;
          for (int i = 0; i < threadsCount; i++)
            res = res + threads.get(i).res;
          System.out.printf("Sum by threads: %d\n", res);
        }
      }
    }
  }
}

class CountThread extends Thread {
  int num;
  int from;
  int to;
  int[] arr;

  public int res;

  public CountThread(int n, int[] a, int f, int t) {
    this.from = f;
    this.to = t;
    this.arr = a;
    this.num = n;
    res = 0;
  }

  public void run() {

    for (int i = from; i <= to; i++)
      res = res + arr[i];

    System.out.printf("Thread %d: from %d to %d sum is %d\n", num, from, to,
                      res);
  }
}
