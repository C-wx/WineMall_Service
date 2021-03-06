package winemall.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import winemall.bean.Comment;
import winemall.bean.CommentExample;
import winemall.mapper.CommentMapper;

import java.util.List;

/**
 * @Explain: 评论处理器
 */
@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    public List<Comment> queryList(Comment comment) {
        CommentExample commentExample = new CommentExample();
        CommentExample.Criteria criteria = commentExample.createCriteria();
        if (comment.getParentId() != null) {
            criteria.andParentIdEqualTo(comment.getParentId());
        }
        criteria.andStatusEqualTo("E");
        commentExample.setOrderByClause("id desc");
        return commentMapper.selectByExample(commentExample);
    }

    public Comment queryDetail(Long id) {
        return commentMapper.selectByPrimaryKey(id);
    }

    public int doAdd(Comment comment) {
        return commentMapper.insertSelective(comment);
    }

    public int doEdit(Comment comment) {
        return commentMapper.updateByPrimaryKeySelective(comment);
    }
}
