// generated with ast extension for cup
// version 0.8
// 7/1/2021 23:56:33


package rs.ac.bg.etf.pp1.ast;

public class DivopExpr extends Term {

    private Term Term;
    private Divop Divop;
    private Factors Factors;

    public DivopExpr (Term Term, Divop Divop, Factors Factors) {
        this.Term=Term;
        if(Term!=null) Term.setParent(this);
        this.Divop=Divop;
        if(Divop!=null) Divop.setParent(this);
        this.Factors=Factors;
        if(Factors!=null) Factors.setParent(this);
    }

    public Term getTerm() {
        return Term;
    }

    public void setTerm(Term Term) {
        this.Term=Term;
    }

    public Divop getDivop() {
        return Divop;
    }

    public void setDivop(Divop Divop) {
        this.Divop=Divop;
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
        if(Term!=null) Term.accept(visitor);
        if(Divop!=null) Divop.accept(visitor);
        if(Factors!=null) Factors.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Term!=null) Term.traverseTopDown(visitor);
        if(Divop!=null) Divop.traverseTopDown(visitor);
        if(Factors!=null) Factors.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Term!=null) Term.traverseBottomUp(visitor);
        if(Divop!=null) Divop.traverseBottomUp(visitor);
        if(Factors!=null) Factors.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DivopExpr(\n");

        if(Term!=null)
            buffer.append(Term.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Divop!=null)
            buffer.append(Divop.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Factors!=null)
            buffer.append(Factors.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DivopExpr]");
        return buffer.toString();
    }
}
