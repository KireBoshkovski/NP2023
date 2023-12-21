package K2.ex37;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


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
    private String username;
    private String id;
    private String content;
    private ArrayList<Comment> replies;
    private int likes;

    public Comment(String username, String id, String content) {
        this.id = id;
        this.username = username;
        this.content = content;
        this.replies = new ArrayList<>();
        this.likes = 0;
    }

    public void addReply(Comment reply){
        this.replies.add(reply);
    }

    public void addLike(){
        likes++;
    }

    public int getLikes(){
        int total = this.likes;

        for (Comment reply : this.replies){
            total += reply.getLikes();
        }

        return total;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("        Comment: ").append(this.content).append("\n");
        sb.append("        Written by: ").append(this.username).append("\n");
        sb.append("        Likes: ").append(this.getLikes()).append("\n");
        for (Comment reply : this.replies) {
            sb.append(reply.toString()).append("\n");
        }
        return sb.toString();
    }
}

class Post{
    private String username;
    private String postContent;
    private Map<String, Comment> comments;
    public Post(String username, String postContent) {
        this.username = username;
        this.postContent = postContent;
        this.comments = new HashMap<>();
    }

    public void addComment(String username, String commentId, String content, String replyToId){
        Comment comment = new Comment(username, commentId, content);
        if(replyToId == null){
            this.comments.put(commentId, comment);
        } else {
            Comment parent = comments.get(replyToId);
            if (parent != null) {
                parent.addReply(comment);
            }
        }
    }

    public void likeComment(String commentId){
        Comment comment = this.comments.get(commentId);
        if(comment != null){
            comment.addLike();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Post: ").append(this.postContent).append("\n");
        sb.append("Written by: ").append(this.username).append("\n");
        sb.append("Comments:\n");
        List<Comment> sortedComments = new ArrayList<>(this.comments.values());
        sortedComments.sort((c1, c2) -> Integer.compare(c2.getLikes(), c1.getLikes()));
        for (Comment comment : sortedComments) {
            sb.append(comment.toString());
        }
        return sb.toString();
    }
}