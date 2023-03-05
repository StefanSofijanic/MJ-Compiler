// generated with ast extension for cup
// version 0.8
// 7/1/2021 23:56:33


package rs.ac.bg.etf.pp1.ast;

public class FactorTerm extends Term {

    private Factors Factors;

    public FactorTerm (Factors Factors) {
        this.Factors=Factors;
        if(Factors!=null) Factors.setParent(this);
    }

    public Factors getFactors() {
        return Factors;
    }

    public void setFactors(Factors Factors) {
        this.Factors=Factors;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Factors!=null) Factors.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Factors!=null) Factors.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Factors!=null) Factors.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FactorTerm(\n");

        if(Factors!=null)
            buffer.append(Factors.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FactorTerm]");
        return buffer.toString();
    }
}
