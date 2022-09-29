import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerService implements Runnable {

    private final int port;

    public ServerService(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        String UPLOADS_PATH = "./uploads";
        try {
            Files.createDirectory(Paths.get(UPLOADS_PATH));
        } catch (FileAlreadyExistsException e1) {
            //все ок
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ExecutorService connectionsPool = Executors.newCachedThreadPool();
        //Метод newCachedThreadPool создает исполнителя с расширяемым пулом потоков.
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Сервер старт");
            while (!Thread.interrupted()) {
                Socket socket = serverSocket.accept();
                System.out.println("Accepted new connection: " + socket);
                connectionsPool.execute(new ClientConnection(socket, UPLOADS_PATH));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        connectionsPool.shutdown();
    }
}