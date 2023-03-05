// generated with ast extension for cup
// version 0.8
// 7/1/2021 23:56:33


package rs.ac.bg.etf.pp1.ast;

public class NumberFactor extends Factor {

    private Integer fact;

    public NumberFactor (Integer fact) {
        this.fact=fact;
    }

    public Integer getFact() {
        return fact;
    }

    public void setFact(Integer fact) {
        this.fact=fact;
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
        buffer.append("NumberFactor(\n");

        buffer.append(" "+tab+fact);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [NumberFactor]");
        return buffer.toString();
    }
}
