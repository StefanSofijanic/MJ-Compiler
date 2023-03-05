// generated with ast extension for cup
// version 0.8
// 7/1/2021 23:56:33


package rs.ac.bg.etf.pp1.ast;

public class CondList extends Condition {

    private ConditionList ConditionList;

    public CondList (ConditionList ConditionList) {
        this.ConditionList=ConditionList;
        if(ConditionList!=null) ConditionList.setParent(this);
    }

    public ConditionList getConditionList() {
        return ConditionList;
    }

    public void setConditionList(ConditionList ConditionList) {
        this.ConditionList=ConditionList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ConditionList!=null) ConditionList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConditionList!=null) ConditionList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConditionList!=null) ConditionList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("CondList(\n");

        if(ConditionList!=null)
            buffer.append(ConditionList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [CondList]");
        return buffer.toString();
    }
}
