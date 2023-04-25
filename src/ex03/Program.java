package ex03;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class Program {

  public static void main(String[] args)
      throws InterruptedException, IOException {

    int threadsCount = 3;
    String filename = "files_urls.txt";

    ArrayList<URL> urls = new ArrayList<>();

    BufferedReader fa = new BufferedReader(new FileReader(filename));
    String input = null;
    while ((input = fa.readLine()) != null) {
      urls.add(new URL(input));
    }
    fa.close();

    URLFiles u = new URLFiles(urls);

    ArrayList<DownloadThread> threads = new ArrayList<>();
    for (int i = 0; i < threadsCount; i++) {
      threads.add(new DownloadThread(i + 1, u));
    }

    for (int i = 0; i < threadsCount; i++) {
      threads.get(i).start();
    }
  }
}

class URLFiles {
  public ArrayList<URL> arr;
  public int index;

  public URLFiles(ArrayList<URL> a) {
    arr = a;
    index = 0;
  }

  public synchronized NumUrl getNextUrl() {
    URL res = null;
    if (index < arr.size()) {
      res = arr.get(index);
      index++;
    }
    return new NumUrl(index, res);
  }
}

class NumUrl {
  URL url;
  int num;

  NumUrl(int n, URL u) {
    num = n;
    url = u;
  }
}

class DownloadThread extends Thread {
  URLFiles urls;
  int num;

  public DownloadThread(int n, URLFiles u) {
    this.num = n;
    this.urls = u;
  }

  public void run() {
    NumUrl temp = urls.getNextUrl();
    while (temp.url != null) {
      String name = temp.url.toString();
      name = name.substring(name.lastIndexOf('/') + 1, name.length());
      try (BufferedInputStream in =
               new BufferedInputStream(temp.url.openStream())) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(name)) {
          System.out.printf("Thread-%d start download file number %d\n", num,
                            temp.num);
          byte dataBuffer[] = new byte[1024];
          int bytesRead;
          while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
            fileOutputStream.write(dataBuffer, 0, bytesRead);
          }
          System.out.printf("Thread-%d finish download file number %s\n", num,
                            temp.num);
          in.close();
          fileOutputStream.close();
        } catch (IOException e) {
          System.out.println("Output!");
        }
      } catch (IOException e) {

        System.out.println("Input!");
      }
      temp = urls.getNextUrl();
    }
  }
}
