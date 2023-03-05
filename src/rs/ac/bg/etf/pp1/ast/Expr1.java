// generated with ast extension for cup
// version 0.8
// 7/1/2021 23:56:33


package rs.ac.bg.etf.pp1.ast;

public class Expr1 implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    public rs.etf.pp1.symboltable.concepts.Struct struct = null;

    private TermOrMinusTerm TermOrMinusTerm;
    private ExprList ExprList;

    public Expr1 (TermOrMinusTerm TermOrMinusTerm, ExprList ExprList) {
        this.TermOrMinusTerm=TermOrMinusTerm;
        if(TermOrMinusTerm!=null) TermOrMinusTerm.setParent(this);
        this.ExprList=ExprList;
        if(ExprList!=null) ExprList.setParent(this);
    }

    public TermOrMinusTerm getTermOrMinusTerm() {
        return TermOrMinusTerm;
    }

    public void setTermOrMinusTerm(TermOrMinusTerm TermOrMinusTerm) {
        this.TermOrMinusTerm=TermOrMinusTerm;
    }

    public ExprList getExprList() {
        return ExprList;
    }

    public void setExprList(ExprList ExprList) {
        this.ExprList=ExprList;
    }

    public SyntaxNode getParent() {
        return parent;
    }

    public void setParent(SyntaxNode parent) {
        this.parent=parent;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line=line;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(TermOrMinusTerm!=null) TermOrMinusTerm.accept(visitor);
        if(ExprList!=null) ExprList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(TermOrMinusTerm!=null) TermOrMinusTerm.traverseTopDown(visitor);
        if(ExprList!=null) ExprList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(TermOrMinusTerm!=null) TermOrMinusTerm.traverseBottomUp(visitor);
        if(ExprList!=null) ExprList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("Expr1(\n");

        if(TermOrMinusTerm!=null)
            buffer.append(TermOrMinusTerm.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ExprList!=null)
            buffer.append(ExprList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [Expr1]");
        return buffer.toString();
    }
}
