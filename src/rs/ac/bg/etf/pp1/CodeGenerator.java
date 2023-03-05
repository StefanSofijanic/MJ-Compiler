package rs.ac.bg.etf.pp1;
import org.apache.log4j.*;

import rs.ac.bg.etf.pp1.CounterVisitor.VarCounter;
import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.*;

public class CodeGenerator extends VisitorAdaptor {
	
	Logger log = Logger.getLogger(getClass());
	
	boolean assignOp = false;
	boolean mulOp = false;
	boolean divOp = false;
	boolean subOp = false;
	boolean modOp = false;
	boolean addOp = true;
	boolean newVisit = false;
	boolean element = false;
	boolean arrayLeft = false;
	boolean addopBefore = true;
	boolean incOp = false;
	boolean decOp = false;
	boolean termMinus = false;	//sluzi za terMinus i za read
	
	boolean firstArray = false;	//sluzi za nivo ugnjezdenosti niza u nizu.. 
								//ako je true znaci da smo u prvom nizu npr.
								//niz[.. nalazimo se u niz
	
	int currPC, jumpPC;
	int openCnt = 0;
	boolean readVisited = false;
	
	Obj designObj = Tab.noObj;

	private int mainPc;
	
	public int getMainPc() {
		return mainPc;
	}
	
	public void visit(PrintStatement print) {
		if (print.getExpr() instanceof ExprTerm)
		{
			if (((ExprTerm) print.getExpr()).getExpr1().struct.getKind() == Struct.Int) {
				Code.loadConst(5); //da bi znao koliko da uzme sa steka da isprinta
				Code.put(Code.print);
			}
			else if (((ExprTerm) print.getExpr()).getExpr1().struct.getKind() == Struct.Char) {
				Code.loadConst(1);
				Code.put(Code.bprint);
			}
			else if (((ExprTerm) print.getExpr()).getExpr1().struct.getKind() == Struct.Bool){
				Code.loadConst(5);
				Code.put(Code.print);
			}
			else if (((ExprTerm) print.getExpr()).getExpr1().struct.getKind() == Struct.Array) {
				if (((ExprTerm) print.getExpr()).getExpr1().struct.getElemType().getKind() == Struct.Int) {
					Code.loadConst(5);
					Code.put(Code.print);
				}
				else if (((ExprTerm) print.getExpr()).getExpr1().struct.getElemType().getKind() == Struct.Char) {
					Code.loadConst(1);
					Code.put(Code.bprint);
				}
				else if (((ExprTerm) print.getExpr()).getExpr1().struct.getElemType().getKind() == Struct.Bool) {
					Code.loadConst(5);
					Code.put(Code.print);
				}
			}
		}
		else
		{
			if (((ExprCond) print.getExpr()).getExpr11().struct.getKind() == Struct.Int) {
				Code.loadConst(5); //da bi znao koliko da uzme sa steka da isprinta
				Code.put(Code.print);
			}
			else if (((ExprCond) print.getExpr()).getExpr11().struct.getKind() == Struct.Char) {
				Code.loadConst(1);
				Code.put(Code.bprint);
			}
			else if (((ExprCond) print.getExpr()).getExpr11().struct.getKind() == Struct.Bool){
				Code.loadConst(5);
				Code.put(Code.print);
			}
			else if (((ExprCond) print.getExpr()).getExpr11().struct.getKind() == Struct.Array) {
				if (((ExprCond) print.getExpr()).getExpr11().struct.getElemType().getKind() == Struct.Int) {
					Code.loadConst(5);
					Code.put(Code.print);
				}
				else if (((ExprCond) print.getExpr()).getExpr11().struct.getElemType().getKind() == Struct.Char) {
					Code.loadConst(1);
					Code.put(Code.bprint);
				}
				else if (((ExprCond) print.getExpr()).getExpr11().struct.getElemType().getKind() == Struct.Bool) {
					Code.loadConst(5);
					Code.put(Code.print);
				}
			}
		}
	}

	public void visit (NumberFactor num) {
		Obj con = Tab.insert(Obj.Con, "$", num.struct);
		
		con.setLevel(0);
		con.setAdr(num.getFact());
		
		Code.load(con);
		
		if (termMinus && !firstArray) {
			Code.put(Code.neg);
			termMinus = false;
		}
	}

	public void visit(CharFactor factor) {
		Obj con = Tab.insert(Obj.Con, "$", factor.struct);
		
		con.setLevel(0);
		con.setAdr(factor.getFact());
		
		Code.load(con);
	}


	public void visit(BoolFactor bool) {
		Obj con = Tab.insert(Obj.Con, "$", bool.struct);
		
		con.setLevel(0);
		con.setAdr(bool.getFact());
		
		Code.load(con);
	}

	public void visit(Designator designator) {
		SyntaxNode parent = designator.getParent();
		
		if (DesignatorStatement.class != parent.getClass() &&
				designator.obj.getType().getKind() != Struct.Array &&
				FuncCall.class != parent.getClass() &&
				ProcCall.class != parent.getClass())
		{
			 Code.load(designator.obj);
			 
			 if (termMinus && !firstArray) {
				 Code.put(Code.neg); //ako je usao u TermMinus tj MINUS od Term Minus
				 termMinus = false;
			 }
		}
		//pusuj na stek designator objekat samo ako je u izrazu tj ako nije sa leve strane =, ako nije niz i ako nije funkcija
		//kada se pusuje obj pusuje se vrednost zapravo
		  
		// if(designator.obj.getType().getKind()==Struct.Array && !element && !assignOp)
		//	 Code.load(designator.obj);
	
		//niz bez elemenata sa leve strane, samo za alokaciju jer se mora ucitati njegova adresa 
		 
		if (element && !readVisited) {
			Obj indexHelper = new Obj(Obj.Var, "$", Tab.intType);
			 
			Code.store(indexHelper);
			Code.load(designator.obj);
			Code.load(indexHelper);
			
			if (DesignatorStatement.class != parent.getClass()) {
				//ako nije sa leve strane =
				Code.put(Code.aload); 
				
				if (termMinus && !firstArray) {
					Code.put(Code.neg); //ako je usao u TermMinus tj MINUS od Term Minus
					termMinus = false;
				}
			}  //element niza u expr stavi mu vrednost na stek samo na stek
			else
				arrayLeft = true;	//element niza je sa leve strane zapamti tu sitiaciju kad stignes i designstatemen
			                   	//jer na steku je adresa niz indeks i josa se samo ceka vrdnost u designStmt da se ubaci na stek i u element niza
		}	//element niza gde god bio

		if (readVisited && designator.obj.getType().getKind() == Struct.Array) {
			Obj indexHelper = new Obj(Obj.Var,"Dule Car", Tab.intType);

			Code.store(indexHelper);
			Code.load(designator.obj);
			Code.load(indexHelper);
			
			if (!firstArray) 
				readVisited = false; //za niz na vrhu read(niz[niz1[...]]) kad stigne do niz radi
			else  
				Code.put(Code.aload); //ako ima ugnezden niz u niz u read
		}	//za slucaj read(niz[...]);

		element = false;
		designObj = designator.obj;
	}

	public void visit(MethodName methName) {
		if (methName.getMethodName().equals("main")) {
			mainPc = Code.pc;
		}

		methName.obj.setAdr(Code.pc);
		
		//Dohvatanje lokalnih variabli
		SyntaxNode methodNode = methName.getParent();
		VarCounter varCnt = new VarCounter();
		
		methodNode.traverseTopDown(varCnt);
		
		//Enter funkcije
		Code.put(Code.enter);
		Code.put(0);
		Code.put(varCnt.getCount());
	}

	public void visit(MethodDecl methodDecl) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}

	public void visit(DesignatorStatement designStmt) {
		if (assignOp) {
			if (!arrayLeft)
				Code.store(designStmt.getDesignator().obj);
				//hvata designator sa leve strane u design statement
				//i njegovom prostoru dodeljuje vrednost sa steka izraza
			else
			{
				Code.store(new Obj(Obj.Elem,designStmt.getDesignator().obj.getName(),new Struct(Struct.Array,designStmt.getDesignator().obj.getType().getElemType())));
				//vrednost sa steka dodeljuje elemntu niza
				//na steku mora biti nizadr,indeks,val
				//arrayLeft je fleg da je levo od = bio element niza
			}
		}

		arrayLeft = false;
		assignOp = false;
	}

	public void visit(Assignop assign) {
		assignOp = true;
	}

	public void visit(AddopExpr add) {
		Code.put(Code.add);
		addOp = false;
	}

	public void visit(SubopExpr sub) {
		Code.put(Code.sub);
		subOp = false;
	}

	public void visit(Plus plus) {
		addOp = true;
	}

	public void visit(Minus minus) {
		subOp = true;
	}

	public void visit(MulopExpr mul) {
		Code.put(Code.mul);
		mulOp = false;
	}

	public void visit(DivopExpr div) {
		Code.put(Code.div);
		divOp = false;
	}

	public void visit(ModopExpr mod) {
		Code.put(Code.rem);
		modOp = false;
	}

	public void visit(Mul mul) {
		mulOp = true;
	}

	public void visit(Div div) {
		divOp = true;
	}

	public void visit(Mod mod) {
		modOp = true;
	}

	public void visit (Increment inc) {
		if (designObj.getType().getKind()==Struct.Array) {
			Obj incHelper = new Obj(Obj.Var, "$", Tab.intType);

			Code.store(incHelper);
			
			Code.load(incHelper);
			Code.load(designObj);
			Code.load(incHelper);
			
			Code.put(Code.aload);
	
			Obj con = new Obj(Obj.Con, "$", new Struct(Struct.Int));

			con.setLevel(0);
			con.setAdr(1);

			Code.load(con);
			
			Code.put(Code.add);

			Code.store(new Obj(Obj.Elem, designObj.getName(), new Struct(Struct.Array, designObj.getType().getElemType())));
		}
		else
		{
			Code.load(designObj);
	
			Obj con = new Obj(Obj.Con, "$", new Struct(Struct.Int));

			con.setLevel(0);
			con.setAdr(1);

			Code.load(con);

			Code.put(Code.add);
			
			Code.store(designObj);
		}
	}
	
	public void visit(Decrement dec) {
		if (designObj.getType().getKind() == Struct.Array) {
			Obj incHelper = new Obj(Obj.Var, "$", Tab.intType);

			Code.store(incHelper);
			
			Code.load(incHelper);
			Code.load(designObj);
			Code.load(incHelper);

			Code.put(Code.aload);

			Obj con = new Obj(Obj.Con, "$", new Struct(Struct.Int));

			con.setLevel(0);
			con.setAdr(1);

			Code.load(con);

			Code.put(Code.sub);

			Code.store(new Obj(Obj.Elem, designObj.getName(), new Struct(Struct.Array, designObj.getType().getElemType())));
		}
		else
		{
			Code.load(designObj);
	
			Obj con = new Obj(Obj.Con, "$", new Struct(Struct.Int));
	
			con.setLevel(0);
			con.setAdr(1);

			Code.load(con);

			Code.put(Code.sub);

			Code.store(designObj);
		}
	}

	public void visit(NewFactor newFactor) {
		Code.put(Code.newarray);

		if (newFactor.getNewType().getType().struct.getKind() == Struct.Int)
			Code.put(1);
		else
			Code.put(0);
	}

	public void visit(ArrayIndexEnd index) {
		element = true;

		if (termMinus ||readVisited)
			openCnt--;

		if (openCnt == 0 && (termMinus || readVisited))
			firstArray = false; //kada se poslednja zagrada dok je termMinus zatvori znaci stizemo da ciljanog niza
	}

	public void visit (ArrayIndexBegin index) {
		if (termMinus || readVisited) {
			firstArray = true;
			openCnt++;
		}	// za slucajeve -niz[...]
			// kada je - odn termMinus
			// pocni da racunas otvorene zagrade
	}

	public void visit(MinusTerm term) {
		termMinus = true;
	}

	public void visit(FuncCall funcCall) {
		Obj functionObj = funcCall.getDesignator().obj;
		int offset = functionObj.getAdr() - Code.pc;
		
		Code.put(Code.call);  
		Code.put2(offset);
	}

	public void visit(ProcCall procCall) {
		Obj functionObj = designObj;
		int offset = functionObj.getAdr() - Code.pc;
		
		Code.put(Code.call);
		Code.put2(offset);

		if (designObj.getType().getKind() != Tab.noType.getKind()) {
			Code.put(Code.pop);
		}
	}

	public void visit(ReturnStatement returnStmt) {
		Code.put(Code.exit);
		Code.put(Code.return_);	
	}

	public void visit(ReadStatement readStmt) {
		if (designObj.getType().getKind() == Struct.Int ||
				designObj.getType().getKind() == Struct.Bool)
			Code.put(Code.read);
		else if (designObj.getType().getKind() == Struct.Char)
			Code.put(Code.bread);
		else if(designObj.getType().getKind() == Struct.Array)
		{
			if (designObj.getType().getElemType().getKind() == Struct.Int ||
					designObj.getType().getElemType().getKind() == Struct.Bool)
				Code.put(Code.read);
			else if (designObj.getType().getElemType().getKind() == Struct.Char)
				Code.put(Code.bread);
		}

		if (designObj.getType().getKind() == Struct.Array) {
			Code.store(new Obj(Obj.Elem, designObj.getName(), new Struct(Struct.Array, designObj.getType().getElemType())));
		}
		else
			Code.store(designObj);

		readVisited = false;
	}

	public void visit(Read read) {
		readVisited = true;
	}
	
	public void visit(Expr1 expr)
	{
		if (expr.getParent() instanceof ExprCond) {
			if (((ExprCond) expr.getParent()).getExpr1().equals(expr)) {
				// Uslov
				Code.put(Code.const_n);
				Code.put(Code.jcc + Code.eq);
				
				currPC = Code.pc;
				
				Code.put(Code.const_1);
				Code.put(Code.const_1);
			}
			else if (((ExprCond) expr.getParent()).getExpr11().equals(expr)) {
				// True vrednost
				Code.put(Code.jmp);
				
				jumpPC = Code.pc;
				
				Code.put(Code.const_1);
				Code.put(Code.const_1);
				
				Code.fixup(currPC);
			}
			else if (((ExprCond) expr.getParent()).getExpr12().equals(expr)) {
				// False vrednost
				Code.fixup(jumpPC);
			}
		}
	}
}