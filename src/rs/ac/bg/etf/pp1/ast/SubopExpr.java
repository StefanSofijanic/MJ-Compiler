// generated with ast extension for cup
// version 0.8
// 7/1/2021 23:56:33


package rs.ac.bg.etf.pp1.ast;

public class SubopExpr extends ExprList {

    private ExprList ExprList;
    private Subop Subop;
    private Term Term;

    public SubopExpr (ExprList ExprList, Subop Subop, Term Term) {
        this.ExprList=ExprList;
        if(ExprList!=null) ExprList.setParent(this);
        this.Subop=Subop;
        if(Subop!=null) Subop.setParent(this);
        this.Term=Term;
        if(Term!=null) Term.setParent(this);
    }

    public ExprList getExprList() {
        return ExprList;
    }

    public void setExprList(ExprList ExprList) {
        this.ExprList=ExprList;
    }

    public Subop getSubop() {
        return Subop;
    }

    public void setSubop(Subop Subop) {
        this.Subop=Subop;
    }

    public Term getTerm() {
        return Term;
    }

    public void setTerm(Term Term) {
        this.Term=Term;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ExprList!=null) ExprList.accept(visitor);
        if(Subop!=null) Subop.accept(visitor);
        if(Term!=null) Term.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ExprList!=null) ExprList.traverseTopDown(visitor);
        if(Subop!=null) Subop.traverseTopDown(visitor);
        if(Term!=null) Term.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ExprList!=null) ExprList.traverseBottomUp(visitor);
        if(Subop!=null) Subop.traverseBottomUp(visitor);
        if(Term!=null) Term.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("SubopExpr(\n");

        if(ExprList!=null)
            buffer.append(ExprList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Subop!=null)
            buffer.append(Subop.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Term!=null)
            buffer.append(Term.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [SubopExpr]");
        return buffer.toString();
    }
}
