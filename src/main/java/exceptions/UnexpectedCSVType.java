package exceptions;

public class UnexpectedCSVType extends Exception {
    public UnexpectedCSVType(){
        super();
    }
    public UnexpectedCSVType(String s){
        super(s);
    }
}
