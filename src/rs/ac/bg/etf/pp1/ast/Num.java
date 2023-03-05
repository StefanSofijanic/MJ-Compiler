// generated with ast extension for cup
// version 0.8
// 7/1/2021 23:56:32


package rs.ac.bg.etf.pp1.ast;

public class Num extends Const {

    private Integer number;

    public Num (Integer number) {
        this.number=number;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number=number;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("Num(\n");

        buffer.append(" "+tab+number);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [Num]");
        return buffer.toString();
    }
}
