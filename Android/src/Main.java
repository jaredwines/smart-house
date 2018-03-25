public class Main {

    public static void main(String[] args) {
        System.out.println("sendCommand");

        /**
         * YOU MUST CHANGE THE FOLLOWING
         * FILE_NAME: A FILE IN THE DIRECTORY
         * USER: LOGIN USER NAME
         * PASSWORD: PASSWORD FOR THAT USER
         * HOST: IP ADDRESS OF THE SSH SERVER
         **/
        String userName = "pi";
        String password = "powell";
        String connectionIP = "192.168.0.200";
        SSHManager instance = new SSHManager(userName, password, connectionIP, "");
        instance.connect();

        // call sendCommand for each command and the output
        //(without prompts) is returned
        //instance.sendCommand("irsend SEND_ONCE TV KEY_VOLUMEUP");
        instance.sendCommand("irsend SEND_ONCE TV KEY_POWER");
        // close only after all commands are sent
        instance.close();
    }
}
