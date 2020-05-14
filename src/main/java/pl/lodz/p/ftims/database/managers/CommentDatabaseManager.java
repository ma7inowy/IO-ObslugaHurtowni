package pl.lodz.p.ftims.database.managers;

import pl.lodz.p.ftims.database.Database;
import pl.lodz.p.ftims.database.DatabaseSingleton;
import pl.lodz.p.ftims.database.communicationinterface.ICommentDatabase;
import pl.lodz.p.ftims.model.client.model.Comment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CommentDatabaseManager implements ICommentDatabase {

    private Database database = DatabaseSingleton.getInstance();

    public CommentDatabaseManager() {

    }


    @Override
    public boolean addComment(Comment comment) {
        try {
            PreparedStatement prepStmt = database.getConn().prepareStatement(
                    "INSERT INTO comments (comment, clientID, userID) VALUES (?, ?, ?);");
            prepStmt.setString(1, comment.getComment());
            prepStmt.setInt(2, Integer.parseInt(comment.getClient().getId()));
            prepStmt.setInt(3,Integer.parseInt(comment.getUser().getId()));
            prepStmt.execute();
            prepStmt.close();
            database.closeConnection();
        } catch (SQLException e) {
            System.err.println("Error while adding comment to database: " + comment.toString());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean removeComment(String id) {
        try {
            if (getComment(id) == null)
                return false;
            PreparedStatement prepStmt = database.getConn().prepareStatement("delete FROM comments WHERE commentID=" + id);
            prepStmt.execute();
            prepStmt.close();
            database.closeConnection();
            return true;
        } catch (SQLException e) {
            System.err.println("Error while removing comment with id=" + id + " from database");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean modifyComment(Comment comment) {
        try {
            PreparedStatement prepStmt = database.getConn().prepareStatement(
                    "UPDATE comments " +
                            "SET comment=?, clientID=?, userID=? " +
                            "WHERE commentID=?;");
            prepStmt.setString(1, comment.getComment());
            prepStmt.setInt(2, Integer.parseInt(comment.getClient().getId()));
            prepStmt.setInt(3, Integer.parseInt(comment.getUser().getId()));
            prepStmt.setInt(4, Integer.parseInt(comment.getCommentID()));
            prepStmt.execute();
            prepStmt.close();
            database.closeConnection();
            return getComment(comment.getCommentID()).getCommentID().equals(comment.getCommentID());
        } catch (SQLException e) {
            System.err.println("Error while modifying comment: " + comment.toString());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Comment> getComments() {
        ClientDatabaseManager clientDatabaseManager = new ClientDatabaseManager();
        UserDatabaseManager userDatabaseManager = new UserDatabaseManager();
        List<Comment> comments = new ArrayList<>();
        try {
            Statement stat = database.getConn().createStatement();
            ResultSet result = stat.executeQuery("SELECT * FROM comments");
            while (result.next()) {
                int commentID = result.getInt("commentID");
                String comment = result.getString("comment");
                int clientID = result.getInt("clientID");
                int userID = result.getInt("userID");
                comments.add(new Comment(String.valueOf(commentID), comment,
                        clientDatabaseManager.getClient(String.valueOf(clientID)),
                        userDatabaseManager.getUser(String.valueOf(userID)))
                );
            }
            result.close();
            stat.close();
            database.closeConnection();
        } catch (SQLException e) {
            System.err.println("Error while getting comments from database");
            e.printStackTrace();
            return null;
        }
        return comments;
    }

    @Override
    public Comment getComment(String id) {
        ClientDatabaseManager clientDatabaseManager = new ClientDatabaseManager();
        UserDatabaseManager userDatabaseManager = new UserDatabaseManager();
        try {
            Statement stat = database.getConn().createStatement();
            ResultSet result = stat.executeQuery("SELECT * FROM comments where commentID=" + id);
            if (result.next()) {
                int commentID = result.getInt("commentID");
                String comment = result.getString("comment");
                int clientID = result.getInt("clientID");
                int userID = result.getInt("userID");
                result.close();
                stat.close();
                database.closeConnection();
                return new Comment(id,
                        comment,
                        clientDatabaseManager.getClient(String.valueOf(clientID)),
                        userDatabaseManager.getUser(String.valueOf(userID)));
            }
            result.close();
            stat.close();
            database.closeConnection();
            return null;
        } catch (SQLException e) {
            System.err.println("Error while getting comment with id=" + id + " from database");
            e.printStackTrace();
            return null;
        }
    }
}
