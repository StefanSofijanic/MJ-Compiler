// generated with ast extension for cup
// version 0.8
// 7/1/2021 23:56:33


package rs.ac.bg.etf.pp1.ast;

public class ModopExpr extends Term {

    private Term Term;
    private Modop Modop;
    private Factors Factors;

    public ModopExpr (Term Term, Modop Modop, Factors Factors) {
        this.Term=Term;
        if(Term!=null) Term.setParent(this);
        this.Modop=Modop;
        if(Modop!=null) Modop.setParent(this);
        this.Factors=Factors;
        if(Factors!=null) Factors.setParent(this);
    }

    public Term getTerm() {
        return Term;
    }

    public void setTerm(Term Term) {
        this.Term=Term;
    }

    public Modop getModop() {
        return Modop;
    }

    public void setModop(Modop Modop) {
        this.Modop=Modop;
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
        if(Modop!=null) Modop.accept(visitor);
        if(Factors!=null) Factors.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Term!=null) Term.traverseTopDown(visitor);
        if(Modop!=null) Modop.traverseTopDown(visitor);
        if(Factors!=null) Factors.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Term!=null) Term.traverseBottomUp(visitor);
        if(Modop!=null) Modop.traverseBottomUp(visitor);
        if(Factors!=null) Factors.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ModopExpr(\n");

        if(Term!=null)
            buffer.append(Term.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Modop!=null)
            buffer.append(Modop.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Factors!=null)
            buffer.append(Factors.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ModopExpr]");
        return buffer.toString();
    }
}
