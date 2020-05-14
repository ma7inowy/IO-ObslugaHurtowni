package pl.lodz.p.ftims.database.managers;

import pl.lodz.p.ftims.database.Database;
import pl.lodz.p.ftims.database.DatabaseSingleton;
import pl.lodz.p.ftims.database.communicationinterface.IClientDatabase;
import pl.lodz.p.ftims.model.client.model.Client;
import pl.lodz.p.ftims.model.client.model.Comment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ClientDatabaseManager implements IClientDatabase {

    private Database database = DatabaseSingleton.getInstance();

    public ClientDatabaseManager() {

    }

    @Override
    public boolean addClient(Client client) {
        try {
            PreparedStatement prepStmt = database.getConn().prepareStatement(
                    "insert into clients (name, state, isBanned) values (?, ?, ?);");
            prepStmt.setString(1, client.getName());
            prepStmt.setString(2, client.getState());
            prepStmt.setInt(3, client.getIsBanned() ? 1 : 0);
            prepStmt.execute();
            prepStmt.close();
            database.closeConnection();
        } catch (SQLException e) {
            System.err.println("Error while adding client to databse: " + client.toString());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean modifyClient(Client client) {
        try {
            PreparedStatement prepStmt = database.getConn().prepareStatement(
                    "update clients " +
                            "set name=?, state=?, isBanned=?" +
                            "where clientID=?;");
            prepStmt.setString(1, client.getName());
            prepStmt.setString(2, client.getState());
            prepStmt.setInt(3, client.getIsBanned() ? 1 : 0);
            prepStmt.setInt(4, Integer.parseInt(client.getId()));
            prepStmt.execute();
            prepStmt.close();
            database.closeConnection();
            return getClient(client.getId()).getId().equals(client.getId());
        } catch (SQLException e) {
            System.err.println("Error while modifying client: " + client.toString());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean removeClient(String id) {
        try {
            if (getClient(id) == null)
                return false;
            PreparedStatement prepStmt = database.getConn().prepareStatement("delete FROM clients WHERE clientID=" + id);
            prepStmt.execute();
            prepStmt.close();
            database.closeConnection();
            return true;
        } catch (SQLException e) {
            System.err.println("Error while removing client with id=" + id);
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Client> getClients() {
        List<Client> clients = new ArrayList<>(1);
        try {
            Statement stat = database.getConn().createStatement();
            ResultSet result = stat.executeQuery("SELECT * FROM clients");
            while (result.next()) {
                String id = String.valueOf(result.getInt("clientID"));
                String name = result.getString("name");
                String state = result.getString("state");
                int isBanned = result.getInt("isBanned");
                clients.add(new Client(id, name, state, isBanned));
            }
            result.close();
            stat.close();
            database.closeConnection();
        } catch (SQLException e) {
            System.err.println("Error while getting clients from database");
            e.printStackTrace();
            return null;
        }
        return clients;
    }

    @Override
    public Client getClient(String id) {
        try {
            Statement stat = database.getConn().createStatement();
            ResultSet result = stat.executeQuery("SELECT * FROM clients where clientID=" + id);
            if (result.next()) {
                String name = result.getString("name");
                String state = result.getString("state");
                int isBanned = result.getInt("isBanned");
                result.close();
                stat.close();
                database.closeConnection();
                return new Client(id, name, state, isBanned);
            }
            result.close();
            stat.close();
            database.closeConnection();
            return null;
        } catch (SQLException e) {
            System.err.println("Error while getting client with clientID=" + id);
            e.printStackTrace();
            return null;
        }
    }

    private List<Comment> getClientsComments(String userId) {
        CommentDatabaseManager commentDatabaseManager = new CommentDatabaseManager();
        List<Comment> comments = commentDatabaseManager.getComments();
        return comments.stream().filter(comment -> comment.getClient().getId().equals(userId)).collect(Collectors.toList());
    }
}
