package str;

public class StrToMass {
    public String mas[];

    public String[] getMas() {
        return mas;
    }

    public StrToMass(String msg){
        this.mas = msg.split(";");
    }
}
