// generated with ast extension for cup
// version 0.8
// 7/1/2021 23:56:33


package rs.ac.bg.etf.pp1.ast;

public class ReturnStatement extends Statement {

    private ReturnStmt ReturnStmt;
    private ExprOptional ExprOptional;

    public ReturnStatement (ReturnStmt ReturnStmt, ExprOptional ExprOptional) {
        this.ReturnStmt=ReturnStmt;
        if(ReturnStmt!=null) ReturnStmt.setParent(this);
        this.ExprOptional=ExprOptional;
        if(ExprOptional!=null) ExprOptional.setParent(this);
    }

    public ReturnStmt getReturnStmt() {
        return ReturnStmt;
    }

    public void setReturnStmt(ReturnStmt ReturnStmt) {
        this.ReturnStmt=ReturnStmt;
    }

    public ExprOptional getExprOptional() {
        return ExprOptional;
    }

    public void setExprOptional(ExprOptional ExprOptional) {
        this.ExprOptional=ExprOptional;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ReturnStmt!=null) ReturnStmt.accept(visitor);
        if(ExprOptional!=null) ExprOptional.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ReturnStmt!=null) ReturnStmt.traverseTopDown(visitor);
        if(ExprOptional!=null) ExprOptional.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ReturnStmt!=null) ReturnStmt.traverseBottomUp(visitor);
        if(ExprOptional!=null) ExprOptional.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ReturnStatement(\n");

        if(ReturnStmt!=null)
            buffer.append(ReturnStmt.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ExprOptional!=null)
            buffer.append(ExprOptional.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ReturnStatement]");
        return buffer.toString();
    }
}
