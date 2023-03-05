// generated with ast extension for cup
// version 0.8
// 7/1/2021 23:56:33


package rs.ac.bg.etf.pp1.ast;

public class VarArray extends VarPart {

    private String arrayName;

    public VarArray (String arrayName) {
        this.arrayName=arrayName;
    }

    public String getArrayName() {
        return arrayName;
    }

    public void setArrayName(String arrayName) {
        this.arrayName=arrayName;
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
        buffer.append("VarArray(\n");

        buffer.append(" "+tab+arrayName);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarArray]");
        return buffer.toString();
    }
}
