package ex00;

public class Program {

  public static void main(String[] args) throws InterruptedException {
    if (args.length == 1) {
      if (args[0].substring(0, 8).equals("--count=")) {
        int count = Integer.parseInt(args[0].substring(8));

        Runnable egg = () -> {
          for (int i = 0; i < count; i++)
            System.out.println("Egg");
        };

        Runnable hen = () -> {
          for (int i = 0; i < count; i++)
            System.out.println("Hen");
        };

        Thread e = new Thread(egg);
        Thread h = new Thread(hen);

        e.start();
        h.start();

        e.join();
        h.join();

        for (int i = 0; i < count; i++)
          System.out.println("Human");
      }
    }
  }
}