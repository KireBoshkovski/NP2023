 package K2.ex37;

import java.util.*;


public class PostTester {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String postAuthor = sc.nextLine();
        String postContent = sc.nextLine();

        Post p = new Post(postAuthor, postContent);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split(";");
            String testCase = parts[0];

            if (testCase.equals("addComment")) {
                String author = parts[1];
                String id = parts[2];
                String content = parts[3];
                String replyToId = null;
                if (parts.length == 5) {
                    replyToId = parts[4];
                }
                p.addComment(author, id, content, replyToId);
            } else if (testCase.equals("likes")) { //likes;1;2;3;4;1;1;1;1;1 example
                for (int i = 1; i < parts.length; i++) {
                    p.likeComment(parts[i]);
                }
            } else {
                System.out.println(p);
            }

        }
    }
}

class Comment {
    private final String author;
    private final String commentId;
    private final String content;
    private int likes;
    private final List<Comment> replies;

    public Comment(String author, String commendId, String content) {
        this.author = author;
        this.commentId = commendId;
        this.content = content;
        this.likes = 0;
        this.replies = new ArrayList<>();
    }

    public void addComment(Comment comment) {
        replies.add(comment);
    }

    public String getCommentId() {
        return commentId;
    }

    public List<Comment> getReplies() {
        return replies;
    }

    public int getLikes() {
        int c = likes;
        for (Comment reply : replies) {
            c += reply.getLikes();
        }
        return c;
    }

    public void like() {
        likes++;
    }

    public String display(int indent) {
        StringBuilder sb = new StringBuilder();
        sb.append("    ".repeat(Math.max(0, indent)));
        sb.append("Comment: ").append(content).append("\n");
        sb.append("    ".repeat(Math.max(0, indent)));
        sb.append("Written by: ").append(author).append("\n");
        sb.append("    ".repeat(Math.max(0, indent)));
        sb.append("Likes: ").append(likes).append("\n");

        replies.sort(Comparator.comparing(Comment::getLikes).reversed());
        for (Comment reply : replies) {
            sb.append(reply.display(indent + 1));
        }

        return sb.toString();
    }
}

class Post {
    private final String author;
    private final String content;
    private final List<Comment> comments;

    public Post(String author, String content) {
        this.author = author;
        this.content = content;
        this.comments = new ArrayList<>();
    }

    public void addComment(String author, String id, String content, String replyToId) {
        Comment comment = new Comment(author, id, content);
        if (replyToId == null) {
            comments.add(comment);
        } else {
            Comment parent = findCommentById(comments, replyToId);
            if (parent != null) {
                parent.addComment(comment);
            }
        }
    }

    public void likeComment(String commentId) {
        Comment comment = findCommentById(comments, commentId);
        if (comment != null) {
            comment.like();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(String.format("Post: %s\nWritten by: %s\nComments:\n", content, author));
        comments.sort(Comparator.comparing(Comment::getLikes).reversed());
        for (Comment comment : comments) {
            sb.append(comment.display(2));
        }
        return sb.toString();
    }

    private Comment findCommentById(List<Comment> comments, String commentId) {
        for (Comment comment : comments) {
            if (comment.getCommentId().equals(commentId)) {
                return comment;
            }
            Comment found = findCommentById(comment.getReplies(), commentId);
            if (found != null) {
                return found;
            }
        }
        return null;
    }
}