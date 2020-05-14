package pl.lodz.p.ftims.model.client.model;

import pl.lodz.p.ftims.model.client.model.Client;
import pl.lodz.p.ftims.model.user.model.User;

public class Comment {

    private String commentID;
    private String comment;
    private Client client;
    private User user;

    public Comment(String commentID, String comment, Client client, User user) {
        this.commentID = commentID;
        this.comment = comment;
        this.client = client;
        this.user = user;
    }

    public String getCommentID() {
        return commentID;
    }

    public String getComment() {
        return comment;
    }

    public void setCommentID(String commentID) {
        this.commentID = commentID;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
