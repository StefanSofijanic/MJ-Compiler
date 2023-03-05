// generated with ast extension for cup
// version 0.8
// 7/1/2021 23:56:32


package rs.ac.bg.etf.pp1.ast;

public class Char extends Const {

    private Character character;

    public Char (Character character) {
        this.character=character;
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character=character;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("Char(\n");

        buffer.append(" "+tab+character);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [Char]");
        return buffer.toString();
    }
}
