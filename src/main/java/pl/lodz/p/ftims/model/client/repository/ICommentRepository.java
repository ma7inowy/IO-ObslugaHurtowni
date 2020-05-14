package pl.lodz.p.ftims.model.client.repository;

import pl.lodz.p.ftims.model.client.model.Comment;

import java.util.List;

public interface ICommentRepository {
    void addComment(Comment comment);
    void removeComment(String id);
    Comment getComment(String id);
    void modifyComment(Comment comment);
    List<Comment> getAllComment();
}
