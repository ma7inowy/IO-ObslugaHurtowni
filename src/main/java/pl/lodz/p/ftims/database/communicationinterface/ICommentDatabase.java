package pl.lodz.p.ftims.database.communicationinterface;

import pl.lodz.p.ftims.model.client.model.Comment;

import java.util.List;

public interface ICommentDatabase {

    boolean addComment(Comment comment);

    boolean removeComment(String id);

    boolean modifyComment(Comment comment);

    List<Comment> getComments();

    Comment getComment(String id);
}
