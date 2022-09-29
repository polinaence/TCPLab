public class ServerStart {
    public static void main(String[] args) {
        int PORT = Integer.parseInt(args[0]);
        ServerService TransferService = new ServerService(PORT);
        TransferService.run();
    }
}