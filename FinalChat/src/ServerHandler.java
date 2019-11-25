public class ServerHandler {

    public String createName(String username) {

        try {
            if (!username.matches("^[a-zA-Z0-9]-_*$")) {
                return "Illegal characters. Username can only have characters, - and _";
            }
            if (username.length() > 12) {
                return "Username is to long. Must be between 1-12 characters...";
            }
            if (Server.clientUsernames.contains(username)) {
                return "Username already taken. Try another one...";
            }
        } catch (IndexOutOfBoundsException e) {
            return "Something went wrong...";
        }
        Server.clientUsernames.add(username);
        return "J_OK";
    }

}

