import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
public class Client {
    private final static int BUFF_SIZE = 4096;

    public static void main(String[] args) {
        Socket socket = new Socket();
        if (args.length < 3) {
            System.out.println("usage: file_name server_address server_port");
            return;
        }
        String filePath = args[0];
        String serverName = args[1];
        int serverPort = Integer.parseInt(args[2]);


        try {
            Path file = Paths.get(filePath);
            socket.connect(new InetSocketAddress(serverName, serverPort));
            OutputStream outputStream = socket.getOutputStream();

            FileInfo fileInfo = new FileInfo(filePath, Files.size(file), calculateHash(file));
            Protocol.writeFileInfo(outputStream, fileInfo);
            uploadFile(outputStream, Files.newInputStream(file));
            boolean isSuccessTransfer = Protocol.readACK(socket.getInputStream()); // wait while all file was transmitted
            if (isSuccessTransfer) {
                System.out.println("successfully transmitted file " + file + " to server c:");
            } else {
                System.out.println("there is some error on server side while transferring file :c");
            }
            socket.close();
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private static byte[] calculateHash(Path file) throws IOException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        try (InputStream is = Files.newInputStream(file);
             DigestInputStream dis = new DigestInputStream(is, md)) {
            while (dis.read(new byte[4096]) > 0) ;
        }
        return md.digest();
    }


    private static void uploadFile(OutputStream outputStream, InputStream fileStream) throws IOException {
        byte[] buff = new byte[BUFF_SIZE];
        int readBytes;
        while ((readBytes = fileStream.read(buff)) > 0) {
            outputStream.write(buff, 0, readBytes);
        }
    }
}