package ex01;

public class Program {
  public static void main(String[] args) {
    if (args.length == 1) {
      if (args[0].substring(0, 8).equals("--count=")) {
        int count = Integer.parseInt(args[0].substring(8));

        Spor spor = new Spor(count);

        Runnable egg = () -> {
          try {
            spor.Egg();
          } catch (InterruptedException e) {
          }
        };

        Runnable hen = () -> {
          try {
            spor.Hen();
          } catch (InterruptedException e) {
          }
        };

        Thread e = new Thread(egg);
        Thread h = new Thread(hen);

        e.start();
        h.start();
      }
    }
  }
}

class Spor {
  static boolean run = true;
  int count;

  public Spor(int c) { this.count = c; }

  public void Egg() throws InterruptedException {
    for (int i = 0; i < count; i++) {
      synchronized (this) {
        while (!run)
          wait();
        System.out.println("Egg");
        run = false;
        notify();
      }
    }
  }

  public void Hen() throws InterruptedException {
    for (int i = 0; i < count; i++) {
      synchronized (this) {
        while (run)
          wait();
        System.out.println("Hen");
        run = true;
        notify();
      }
    }
  }
}
