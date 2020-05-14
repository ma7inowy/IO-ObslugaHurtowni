package pl.lodz.p.ftims.model.client.repository;

import pl.lodz.p.ftims.database.communicationinterface.ICommentDatabase;
import pl.lodz.p.ftims.database.managers.CommentDatabaseManager;
import pl.lodz.p.ftims.model.client.model.Comment;

import java.util.List;

public class CommentRepository implements ICommentRepository {
    private ICommentDatabase commentDatabase = new CommentDatabaseManager();

    public CommentRepository(){

    }

    @Override
    public void addComment(Comment comment) {
        commentDatabase.addComment(comment);
    }

    @Override
    public void removeComment(String id) {
        commentDatabase.removeComment(id);
    }

    @Override
    public Comment getComment(String id) {
        return commentDatabase.getComment(id);
    }

    @Override
    public void modifyComment(Comment comment) {
        commentDatabase.modifyComment(comment);
    }

    @Override
    public List<Comment> getAllComment() {
        return commentDatabase.getComments();
    }
}
