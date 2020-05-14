package pl.lodz.p.ftims.model.client.service;

import pl.lodz.p.ftims.model.client.model.Comment;

import java.util.List;

public interface ICommentService {
    void addComment(Comment comment);

    void removeComment(String id);

    void modifyComment(Comment comment);

    Comment getComment(String id);

    List<Comment> getAllComment();
}
