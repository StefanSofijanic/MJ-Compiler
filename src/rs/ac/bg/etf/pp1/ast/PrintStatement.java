// generated with ast extension for cup
// version 0.8
// 7/1/2021 23:56:33


package rs.ac.bg.etf.pp1.ast;

public class PrintStatement extends Statement {

    private Print Print;
    private Expr Expr;
    private CommaNumConstOptional CommaNumConstOptional;

    public PrintStatement (Print Print, Expr Expr, CommaNumConstOptional CommaNumConstOptional) {
        this.Print=Print;
        if(Print!=null) Print.setParent(this);
        this.Expr=Expr;
        if(Expr!=null) Expr.setParent(this);
        this.CommaNumConstOptional=CommaNumConstOptional;
        if(CommaNumConstOptional!=null) CommaNumConstOptional.setParent(this);
    }

    public Print getPrint() {
        return Print;
    }

    public void setPrint(Print Print) {
        this.Print=Print;
    }

    public Expr getExpr() {
        return Expr;
    }

    public void setExpr(Expr Expr) {
        this.Expr=Expr;
    }

    public CommaNumConstOptional getCommaNumConstOptional() {
        return CommaNumConstOptional;
    }

    public void setCommaNumConstOptional(CommaNumConstOptional CommaNumConstOptional) {
        this.CommaNumConstOptional=CommaNumConstOptional;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Print!=null) Print.accept(visitor);
        if(Expr!=null) Expr.accept(visitor);
        if(CommaNumConstOptional!=null) CommaNumConstOptional.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Print!=null) Print.traverseTopDown(visitor);
        if(Expr!=null) Expr.traverseTopDown(visitor);
        if(CommaNumConstOptional!=null) CommaNumConstOptional.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Print!=null) Print.traverseBottomUp(visitor);
        if(Expr!=null) Expr.traverseBottomUp(visitor);
        if(CommaNumConstOptional!=null) CommaNumConstOptional.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("PrintStatement(\n");

        if(Print!=null)
            buffer.append(Print.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Expr!=null)
            buffer.append(Expr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(CommaNumConstOptional!=null)
            buffer.append(CommaNumConstOptional.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [PrintStatement]");
        return buffer.toString();
    }
}
