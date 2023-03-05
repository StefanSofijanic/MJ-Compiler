// generated with ast extension for cup
// version 0.8
// 7/1/2021 23:56:33


package rs.ac.bg.etf.pp1.ast;

public class NewFactor extends Factor {

    private NewType NewType;
    private ExprOptionalBracket ExprOptionalBracket;

    public NewFactor (NewType NewType, ExprOptionalBracket ExprOptionalBracket) {
        this.NewType=NewType;
        if(NewType!=null) NewType.setParent(this);
        this.ExprOptionalBracket=ExprOptionalBracket;
        if(ExprOptionalBracket!=null) ExprOptionalBracket.setParent(this);
    }

    public NewType getNewType() {
        return NewType;
    }

    public void setNewType(NewType NewType) {
        this.NewType=NewType;
    }

    public ExprOptionalBracket getExprOptionalBracket() {
        return ExprOptionalBracket;
    }

    public void setExprOptionalBracket(ExprOptionalBracket ExprOptionalBracket) {
        this.ExprOptionalBracket=ExprOptionalBracket;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(NewType!=null) NewType.accept(visitor);
        if(ExprOptionalBracket!=null) ExprOptionalBracket.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(NewType!=null) NewType.traverseTopDown(visitor);
        if(ExprOptionalBracket!=null) ExprOptionalBracket.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(NewType!=null) NewType.traverseBottomUp(visitor);
        if(ExprOptionalBracket!=null) ExprOptionalBracket.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("NewFactor(\n");

        if(NewType!=null)
            buffer.append(NewType.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ExprOptionalBracket!=null)
            buffer.append(ExprOptionalBracket.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [NewFactor]");
        return buffer.toString();
    }
}
