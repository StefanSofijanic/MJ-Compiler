// generated with ast extension for cup
// version 0.8
// 7/1/2021 23:56:33


package rs.ac.bg.etf.pp1.ast;

public class ArrayDesignPart extends DesignatorPart {

    private ArrayIndexBegin ArrayIndexBegin;
    private Expr Expr;
    private ArrayIndexEnd ArrayIndexEnd;

    public ArrayDesignPart (ArrayIndexBegin ArrayIndexBegin, Expr Expr, ArrayIndexEnd ArrayIndexEnd) {
        this.ArrayIndexBegin=ArrayIndexBegin;
        if(ArrayIndexBegin!=null) ArrayIndexBegin.setParent(this);
        this.Expr=Expr;
        if(Expr!=null) Expr.setParent(this);
        this.ArrayIndexEnd=ArrayIndexEnd;
        if(ArrayIndexEnd!=null) ArrayIndexEnd.setParent(this);
    }

    public ArrayIndexBegin getArrayIndexBegin() {
        return ArrayIndexBegin;
    }

    public void setArrayIndexBegin(ArrayIndexBegin ArrayIndexBegin) {
        this.ArrayIndexBegin=ArrayIndexBegin;
    }

    public Expr getExpr() {
        return Expr;
    }

    public void setExpr(Expr Expr) {
        this.Expr=Expr;
    }

    public ArrayIndexEnd getArrayIndexEnd() {
        return ArrayIndexEnd;
    }

    public void setArrayIndexEnd(ArrayIndexEnd ArrayIndexEnd) {
        this.ArrayIndexEnd=ArrayIndexEnd;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ArrayIndexBegin!=null) ArrayIndexBegin.accept(visitor);
        if(Expr!=null) Expr.accept(visitor);
        if(ArrayIndexEnd!=null) ArrayIndexEnd.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ArrayIndexBegin!=null) ArrayIndexBegin.traverseTopDown(visitor);
        if(Expr!=null) Expr.traverseTopDown(visitor);
        if(ArrayIndexEnd!=null) ArrayIndexEnd.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ArrayIndexBegin!=null) ArrayIndexBegin.traverseBottomUp(visitor);
        if(Expr!=null) Expr.traverseBottomUp(visitor);
        if(ArrayIndexEnd!=null) ArrayIndexEnd.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ArrayDesignPart(\n");

        if(ArrayIndexBegin!=null)
            buffer.append(ArrayIndexBegin.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Expr!=null)
            buffer.append(Expr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ArrayIndexEnd!=null)
            buffer.append(ArrayIndexEnd.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ArrayDesignPart]");
        return buffer.toString();
    }
}
