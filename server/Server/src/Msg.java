public class Msg {
    private String From;
    private String To;
    private String Message;

    public String getFrom() {
        return From;
    }

    public void setFrom(String from) {
        this.From = from;
    }

    public String getTo() {
        return To;
    }

    public void setTo(String to) {
        To = to;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        this.Message = message;
    }
    public Msg(String From, String To, String message){
        this.From = From;
        this.To = To;
        this.Message = message;
    }
    public Msg (String From,String message){
        this.From = From;
        this.Message = message;
    }
    public Msg(){}
}
