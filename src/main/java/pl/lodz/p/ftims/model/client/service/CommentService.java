package pl.lodz.p.ftims.model.client.service;


import pl.lodz.p.ftims.model.client.model.Comment;
import pl.lodz.p.ftims.model.client.repository.CommentRepository;
import pl.lodz.p.ftims.model.client.repository.ICommentRepository;

import java.util.List;

public class CommentService implements ICommentService{
    private ICommentRepository commentRepository = new CommentRepository();

    @Override
    public void addComment(Comment comment) {
        commentRepository.addComment(comment);
    }

    @Override
    public void removeComment(String id) {
        commentRepository.removeComment(id);
    }

    @Override
    public void modifyComment(Comment comment) {
        commentRepository.modifyComment(comment);
    }

    @Override
    public Comment getComment(String id) {
        return commentRepository.getComment(id);
    }

    @Override
    public List<Comment> getAllComment() {
        return commentRepository.getAllComment();
    }
}
