package zctang.here;

/**
 * Created by wangxiaoyang01 on 2017/5/19.
 */

class Msg {
    private String msg;
    private String time;
    private String upvote;

    public Msg(String content, String time, String upvote) {
        this.msg = content;
        this.time = time;
        this.upvote = upvote;
    }

    public String getMsg() {
        return msg;
    }

    public String getTime() {
        return time;
    }

    public String getUpvote() {
        return upvote;
    }
}