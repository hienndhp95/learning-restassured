package model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostBody {

    private int userId;
    private int id;
    private String title;
    private String body;

    public PostBody() {
    }

    public PostBody(int userId, int id, String title, String body) {
        this.userId = userId;
        this.id = id;
        this.title = title;
        this.body = body;
    }

    @Override
    public String toString() {
        return "PostBody{" +
                "userId=" + userId +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
