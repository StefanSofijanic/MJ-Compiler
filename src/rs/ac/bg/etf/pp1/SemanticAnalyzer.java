package rs.ac.bg.etf.pp1;
import org.apache.log4j.*;


import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.*;
import rs.etf.pp1.symboltable.visitors.DumpSymbolTableVisitor;
import rs.etf.pp1.symboltable.visitors.SymbolTableVisitor;


public class SemanticAnalyzer extends VisitorAdaptor{
	
	Logger log = Logger.getLogger(getClass());
	
	int printCallCount = 0;
	int varDeclCount = 0;
	
	boolean errorDetected = false;
	Struct currentType = Tab.noType;
	

	Obj boolNode = Tab.noObj, numberNode = Tab.noObj, charNode = Tab.noObj;
	Obj currentClass = Tab.noObj, currentMethod = Tab.noObj;
	Obj designObj = Tab.noObj, identFactor = Tab.noObj, leftIdent = Tab.noObj;
	Obj typeNode = Tab.noObj, retNew = Tab.noObj, leftNewIdent = Tab.noObj;

	Scope universeScope, globalScope;

	Struct factType = Tab.noType, factTypeOld = Tab.noType;
	Struct factTypeOldArray = Tab.noType, retType = Tab.noType;

	boolean visitedBool = false;
	boolean visitedChar = false;
	boolean visitedNum = false;
	boolean insideClass = false;
	boolean insideMethod = false;
	boolean classCorrect = true;
	boolean insideAbs = false;
	boolean voidVisited = false;
	boolean mainExist = false;
	boolean returned = false;
	boolean NoExprVisited = false;
	boolean assignOp = false;
	boolean incOp = false;
	boolean decOp = false;
	boolean element = false;
	boolean elementLeft = false;
	boolean readVisited = false;
	boolean designatorVisited = false;
	boolean elementReturn = false;
	boolean newVisited = false;
	boolean exprInNew = false;
	boolean lastNewFactor = false;
	boolean abstractMethod = false;
	boolean newLBracket = false;
	boolean elementNewIdent = false;
	boolean addopVisit = false;
	boolean mulopVisit = false;
	boolean firstArrayBegin = false;
	boolean printVisit = false;
	boolean modopVisit = false;
	boolean divopVisit = false;
	boolean subopVisit = false;

	SymbolTableVisitor stv;

	int numClass = 0, numMethod = 0, numGlobalVar = 0, numConst = 0, numGlobalArray = 0;
	int numLocalArray = 0, numLocalVar = 0, numStatement = 0, numFunctionCall = 0;

	int openCnt = 0, constBool, constNum;

	char constChar;

	int nVars;

	public void report_error(String message, SyntaxNode info) {
		errorDetected = true;
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0 : info.getLine();
		if (line != 0)
			msg.append(" na liniji ").append(line);
		log.error(msg.toString());
	}

	public void report_info(String message, SyntaxNode info) {
		StringBuilder msg = new StringBuilder(message); 
		int line = (info == null) ? 0 : info.getLine();
		if (line != 0)
			msg.append(" na liniji ").append(line);
		log.info(msg.toString());
	}
	
	public void visit(Print print)
	{
		printVisit = true;
	}
	
	public void visit(PrintStatement print) {
		if (insideMethod && currentMethod.getName().equals("main"))
			numStatement++;
		printVisit = false;
		element = false;
	}

	//Program
	
	public void visit(Program program) {
		nVars = Tab.currentScope.getnVars();
		
		if (!mainExist)
			report_error("Greska!!! Ne postoji main funkcija u glavnom programu!!! " , null);
		
		Tab.chainLocalSymbols(program.getProgName().obj);
		Tab.closeScope();
		
		log.info("=======================================SINTAKSNA ANALIZA=====================================");
		log.info(numClass + "    classes");
		log.info(numMethod + "    methods in program");
		log.info(numGlobalVar + "    global variables");
		log.info(numConst + "    constants");
		log.info(numGlobalArray + "    global arrays");
		log.info(numLocalVar + "    local variables in main");
		log.info(numLocalArray + "    local arrays in main");
		log.info(numStatement + "    statements in main");
		log.info(numFunctionCall + "    function calls in main");
	}
	
	public void visit(ProgName progName) {
		log.info("=======================================SEMANTICKA ANALIZA=====================================");
		
		progName.obj = Tab.insert(Obj.Prog, progName.getProgName(), Tab.noType);
		universeScope = Tab.currentScope;
		Tab.openScope();
		
		globalScope = Tab.currentScope;
		report_info("Zapocinjemo sa glavnim programom", progName);
	}

	//Tip
	
	public void visit(Type type){
		typeNode=Tab.find(type.getTypeName());
		
		if (typeNode == Tab.noObj){
			report_error("Nije pronadjen tip " + type.getTypeName() + " u tabeli simbola! ", null);
			currentType = Tab.noType;
		} else {
			if (Obj.Type == typeNode.getKind()){
				currentType = typeNode.getType();
			} else {
				report_info("Greska: Ime " + type.getTypeName() + " ne predstavlja tip!", type);
				currentType = Tab.noType;
				type.struct = currentType;
			}
		}
	}
	
	// Promenljiva

	public void visit (VarIdent varIdent) {
		Obj var = Tab.currentScope.findSymbol(varIdent.getVarName());
	  
		if (var != null) {
			report_error("Greska na " + varIdent.getLine() + ":" + varIdent.getVarName() + " vec deklarisano", null);
		}
		else
		{
			if (insideClass && !insideMethod) {
				//ako je u klasi a nije u metodi klase onda je polje fld inace je var
				Obj varDecl = Tab.insert(Obj.Fld,varIdent.getVarName(), currentType);
			  
				stv = new DumpSymbolTableVisitor();
				stv.visitObjNode(varDecl);
				  
				report_info("Deklarisana lokalna promenljiva: " + varIdent.getVarName() + " na liniji " + varIdent.getLine() + " => " + stv.getOutput(), null);  
			}
			else
			{
				Obj varDecl=Tab.insert(Obj.Var, varIdent.getVarName(), currentType);
		
				stv=new DumpSymbolTableVisitor();
				stv.visitObjNode(varDecl);
		  
				if (insideMethod) {
					if (currentMethod.getName().equals("main"))
						numLocalVar++;
					report_info("Deklarisana lokalna promenljiva: " + varIdent.getVarName() + " u funkciji " + currentMethod.getName() + "()" + " na liniji " + varIdent.getLine() + " => " + stv.getOutput(), null);
				}
				else
				{
					numGlobalVar++;
					report_info("Deklarisana globalna promenljiva: " + varIdent.getVarName() + " na liniji " + varIdent.getLine() + " => " + stv.getOutput(), null);
				}
			}
		}
	}

	// NIZ
	
	public void visit (VarArray varArray) {
		if (Tab.currentScope.findSymbol(varArray.getArrayName())!=null) {
			report_error("Greska" + varArray.getLine() + ":" + varArray.getArrayName() + "vec deklarisano", null);
		}
		else
		{
			if (insideClass && !insideMethod) {  
				Obj	arrayObj= Tab.insert(Obj.Fld, varArray.getArrayName(), new Struct(Struct.Array, currentType));
		
				stv = new DumpSymbolTableVisitor();
				stv.visitObjNode(arrayObj);
				  
				report_info("Deklarisan lokalni niz: " + varArray.getArrayName() + " na liniji " + varArray.getLine() + " => " + stv.getOutput(), null);
			}
			else
			{
				Obj arrayObj = Tab.insert(Obj.Var,varArray.getArrayName(), new Struct(Struct.Array,currentType));
				
				stv = new DumpSymbolTableVisitor();
				stv.visitObjNode(arrayObj);
		  
				if (insideMethod) {
					if (currentMethod.getName().equals("main"))
						numLocalArray++;
					report_info("Deklarisan lokalni niz: " + varArray.getArrayName() + " u funkciji " + currentMethod.getName() + "()" + " na liniji " + varArray.getLine() + " => " + stv.getOutput(), null);
				}
				else
				{
					numGlobalArray++;
					report_info("Deklarisan globalni niz: " + varArray.getArrayName() + " na liniji " + varArray.getLine() + " => " + stv.getOutput(), null);
				}
			}
		}
	}

	//Konstanta
	
	public void visit(ConstPart constPart) {
		Obj help = Tab.currentScope.findSymbol(constPart.getConstName());
		
		if (help != null) {
			report_error("Greska"+constPart.getLine()+":"+constPart.getConstName()+ " vec deklarisano", null );
		}
		else
		{
			Obj con = Tab.insert(Obj.Con,constPart.getConstName(), currentType);
				
			if (visitedBool) {
				if (boolNode.getType().getKind() != currentType.getKind())
					report_error("Greska!!! Ne poklapanje tipa i vrednosti konstante", constPart);
				else
					con.setAdr(constBool);
			}
			else if (visitedNum) {
				if (numberNode.getType().getKind() != currentType.getKind())
					report_error("Greska!!! Ne poklapanje tipa i vrednosti konstante", constPart);
				else
					con.setAdr(constNum);
			}
			else if (visitedChar) {
				if (charNode.getType().getKind() != currentType.getKind())
					report_error("Greska!!! Ne poklapanje tipa i vrednosti konstante", constPart);
				else
					con.setAdr(constChar);
			}
			
			stv = new DumpSymbolTableVisitor();
			stv.visitObjNode(con);
				  
			report_info("Deklarisana konstanta: " + constPart.getConstName() + " na liniji " + constPart.getLine() + " => " + stv.getOutput(), null);
			numConst++;
		}
				
		visitedBool = false;
		visitedNum = false;
		visitedChar = false;
		boolNode = Tab.noObj;
		numberNode = Tab.noObj;
		charNode = Tab.noObj;
	}
	
	public void visit(Num number) {
		numberNode = new Obj (Obj.Con, "", Tab.intType);
		
		constNum = number.getNumber();
		numberNode.setAdr(constNum);
		visitedNum = true;
	}
	  
	public void visit (Bool bool) {
		boolNode = new Obj (Obj.Con, "", new Struct(Struct.Bool));
	
		constBool = bool.getBool();
		boolNode.setAdr(constBool);
		visitedBool = true;
		
	}
	  
	public void visit (Char c) {
		charNode = new Obj (Obj.Con, "",Tab.charType);
		
		constChar = c.getCharacter();
		visitedChar = true;
	}

	//KLASA

	public void visit(ClassName className) {
		insideClass = true;
		
		if (Tab.currentScope().findSymbol(className.getClassName()) != null) {
			report_error("Greska na liniji " + className.getLine() + " klasa " + className.getClassName() + " vec deklarisana", null);
			classCorrect = false;
		}
		else
		{
			currentClass = Tab.insert(Obj.Type, className.getClassName(), new Struct(Struct.Class));
			classCorrect = true;
			report_info("Ulazimo u klasu " + className.getClassName(), className);
			numClass++;
		}
		
		Tab.openScope();
	}
	
	
	public void visit(ClassDecl classDecl) {
		Tab.chainLocalSymbols(currentClass.getType());
	
		Tab.closeScope();
		currentClass = Tab.noObj;
		insideClass = false;
		classCorrect = true;
		if (!insideAbs && abstractMethod)
			report_error("Apstraktna metoda u obicnoj klasi!!!", null);
		insideAbs = false;
		abstractMethod = false;
	}
	
	public void visit (ExtendClass extend) {
		if (Tab.find(extend.getType().getTypeName()) == Tab.noObj)
			report_error("Greska!!! Klasa koja se izvodi " + extend.getType().getTypeName() + " nije deklarisana", null);
			
		classCorrect = false;
	}
	
	//Metode
	
	public void visit(MethodName methodName) {
		Obj methodObj = Tab.currentScope.findSymbol(methodName.getMethodName());
		
		if (methodObj != null)
			report_error("Greska na liniji " + methodName.getLine() + " ime "+ methodName.getMethodName() + " vec postoji", null);
		else
		{
			if (methodName.getMethodName().equals("main")) {
				if (!voidVisited)
					report_error("Greska!!! Main metoda mora biti void", null);
				else
				{
					currentMethod = Tab.insert(Obj.Meth, methodName.getMethodName(), Tab.noType);
					methodName.obj=currentMethod;
				
					report_info("Ulazimo u MAIN funkciju", methodName);
					numMethod++;
				}
				
				mainExist = true;
				insideMethod = true;
			}
			else
			{
				currentMethod = Tab.insert(Obj.Meth, methodName.getMethodName(), currentType);
				insideMethod = true;
			
				methodName.obj = currentMethod;
				
				report_info("Ulazimo u funkciju " + methodName.getMethodName(), methodName);
				if (!insideClass)
					numMethod++;
			}
		}
		
		Tab.openScope();
	}
	
	public void visit(ReturnVoid retVoid) {
		voidVisited = true;
		currentType = Tab.noType;
	}
	
	public void visit(FormParVar formPars) {
		if (Tab.currentScope.findSymbol(formPars.getParName()) != null) {
			report_error("Greska na liniji " + formPars.getLine() + " deklarisani parametar " + formPars.getParName() + "vec postoji", null);
		}
		else if (currentMethod.getName().equals("main"))
			report_error("Main metoda ne sme da ima argumente!!!", formPars);
		else Tab.insert(Obj.Var, formPars.getParName(), currentType);
	}
	
	public void visit(FormParArray formPars) {
		if (Tab.currentScope.findSymbol(formPars.getParName()) != null) {
			report_error("Greska!!! Deklarisani parametar " + formPars.getParName() + " vec postoji", formPars);
		}
		else if (currentMethod.getName().equals("main"))
			report_error("Main metoda ne sme da ima argumente!!!", formPars);
		else Tab.insert(Obj.Var, formPars.getParName(), new Struct(Struct.Array, currentType));
	}
	
	public void visit(MethodDecl methodDecl) {
		if ((!voidVisited && (returned && NoExprVisited ) || (!voidVisited && !returned)))
			report_error("Greska na liniji " + methodDecl.getLine() + ". Funkcija " + currentMethod.getName() + "() mora imati povratnu vrednost odgovarajuceg tipa", null);
		else
		{
			if (voidVisited && !returned)
				retType=Tab.noType;
			
			if (newVisited) {
				if (!(retNew.getType().compatibleWith(currentMethod.getType())))
					report_error("Greska na liniji " + methodDecl.getLine() + ". Funkcija " + currentMethod.getName() + "() nema odgovarajucu povratnu vrednost", null);
				newVisited = false;
				retNew = Tab.noObj;
			}
			else if (currentMethod.getType().getKind() == Struct.Class || retType.getKind() == Struct.Class) {
				if (currentMethod.getType().getKind() == Struct.Class && identFactor.getType().getKind() == Struct.Class) {
					if (!(currentMethod.getType().equals(identFactor.getType())))
						report_error("Nisu ista klasa!!!", null);
				}
				else
					report_error("Greska na liniji " + methodDecl.getLine() + ". Funkcija " + currentMethod.getName() + "() nema odgovarajucu povratnu vrednost", null);
			}
			else if (currentMethod.getType().getKind() != Struct.Array && retType.getKind() == Struct.Array) {
				if (!elementReturn)
					report_error("Greska!!! " + identFactor.getName() + " nije element niza, vec niz", null);
				
				if (retType.getElemType().getKind() != currentMethod.getType().getKind())
					report_error("Greska na liniji " + methodDecl.getLine() + ". Funkcija " + currentMethod.getName() + "() nema odgovarajucu povratnu vrednost", null);
			}
			else if (retType.getKind() != currentMethod.getType().getKind()) 
				report_error("Greska na liniji " + methodDecl.getLine() + ". Funkcija " + currentMethod.getName() + "() nema odgovarajucu povratnu vrednost", null);
		}
		
		Tab.chainLocalSymbols(currentMethod);
		Tab.closeScope();
		
		currentMethod = Tab.noObj;
		returned = false;
		factType = Tab.noType;
		NoExprVisited = false;
		voidVisited = false;
		retType = Tab.noType;
		insideMethod = false;
		elementReturn = false;
			
		newVisited = false;
	}
	
	public void visit(ReturnStmt ret) {
		returned = true;
		if (!insideMethod)
			report_error("Ne moze RETURN van funkcije", ret);
	}
	
	public void visit(ReturnStatement ret) {
		elementReturn=element;
		
		if (mulopVisit || addopVisit || divopVisit || modopVisit || subopVisit) {
			if (factType.getKind() == Struct.Class)
				report_error("Ne moze klasa u da bude u izrazu ", ret);
			
			if (factType.getKind() == Struct.Array) {
				if (!element)
					report_error("Greska!! Niz " + identFactor.getName() + " ne moze da bude u izrazu ", ret);
					
				if (factType.getElemType().getKind() != Struct.Int)
					report_error("Greska!!! Samo int moze biti u izrazu + - * / ", ret);
			}
			else
			{
				if (factType.getKind() != Struct.Int)
					report_error("Greska!!! Samo int moze biti u izrazu + - * / ", ret);
			}
			
			//da proveri da li je poslednji u nizu expr u return naredbi int ako je pre bilo addop mulop.. Jer addop mulop proveravaju samo factor pre a ne i posle
			//i onda poslednji nema ko da proveri
		}
		
		mulopVisit = false;
		addopVisit = false;
		subopVisit = false;
		modopVisit = false;
		divopVisit = false;
		element = false;
	}
	
	//Factor
	
	public void visit(NumberFactor fact) {
		factType = new Struct(Struct.Int);
		
		if (returned)
			retType = new Struct(Struct.Int); 
	}	
	
	public void visit(BoolFactor fact) {
		factType = new Struct(Struct.Bool);
		
		if (returned)
			retType = new Struct(Struct.Bool);
	}
	
	public void visit(CharFactor fact) {
		factType = new Struct(Struct.Char);
		
		if (returned)
			retType = new Struct(Struct.Char);
	}
	
	public void visit(DesignatorFactor df) {
		identFactor = Tab.find(df.getDesignator().getName());
		
		//df.getDesignator().obj=identFactor;
		
		if (identFactor.getType().getKind() == Struct.Array) {
			factType = new Struct(identFactor.getType().getKind(), identFactor.getType().getElemType());
		}
		else
			factType = new Struct(identFactor.getType().getKind());
		
		if (returned) {
			if (identFactor.getType().getKind() == Struct.Array) {
				retType = new Struct(identFactor.getType().getKind(), identFactor.getType().getElemType());
			}
			else
				retType = new Struct(identFactor.getType().getKind());
		}
	}
	
	public void visit(NoExpr noExpr) {
		retType = Tab.noType;
		NoExprVisited = true;
	}
	
	//Designator
	
	public void visit(DesignatorStatement designatorStmt) {
		if (insideMethod && currentMethod.getName().equals("main"))
			numStatement++;
		
		if (assignOp) {
			designatorStmt.getDesignator().obj = leftIdent;
			
			//log.info(designatorStmt.getDesignator().obj.getName());
			if (mulopVisit || addopVisit || divopVisit || modopVisit || subopVisit) {
				if (factType.getKind() == Struct.Class)
					report_error("Ne moze klasa u izrazu!!!", designatorStmt);
				
				if (factType.getKind() == Struct.Array) {
					if (!element)
						report_error("Greska!! Niz " + identFactor.getName() + " ne moze biti u izrazu ", designatorStmt);
					
					if (factType.getElemType().getKind() != Struct.Int)
						report_error("Greska!!! Samo int moze biti u izrazu + - * /", designatorStmt);
				}
				else
				{
					if (factType.getKind() != Struct.Int)
						report_error("Greska!!! Samo int moze biti u izrazu + - * /", designatorStmt);
				}
			
				//da proveri da li je poslednji u nizu expr u assignop naredbi int ako je pre bilo addop mulop.. Jer addop mulop proveravaju samo factor pre a ne i posle
				//i onda poslednji nema ko da proveri
			}
			
			if (leftIdent.getType().getKind() == Struct.Class || factType.getKind() == Struct.Class) {
				if (leftIdent.getType().getKind() == Struct.Class && identFactor.getType().getKind() == Struct.Class) {
					if (!(leftIdent.getType().compatibleWith(identFactor.getType())))
						report_error("Greksa na liniji " + designatorStmt.getLine() + " promenljive nisu istog klasnog tipa", null);
				}
				else
					report_error("Obe instance moraju biti klasnog tipa", designatorStmt);
			}
			else if (leftIdent.getType().getKind() == Struct.Array || factType.getKind() == Struct.Array) {
				if (leftIdent.getType().getKind() == Struct.Array && factType.getKind() == Struct.Array) {
					if ((!elementLeft && element) || (!element && elementLeft))
						report_error("Greska!!! Ne moze se nizu dodeliti element niza i obrnuto", designatorStmt);
						
				    if (factType.getElemType().getKind() != leftIdent.getType().getElemType().getKind())
				    	report_error("Greska!!! Nisu istog tipa pri dodeli!!!", null);
				}
				else if (leftIdent.getType().getKind() != Struct.Array && factType.getKind() == Struct.Array) {
					if (!element)
						report_error("Greska!!! " + identFactor.getName() + " nije element niza, vec niz", designatorStmt);
					
					if (factType.getElemType().getKind() != leftIdent.getType().getKind())
						report_error("Greska!!! Nisu istog tipa pri dodeli!!!", designatorStmt);
				}
				else if (leftIdent.getType().getKind() == Struct.Array && factType.getKind() != Struct.Array) {
					if (factType.getKind() != leftIdent.getType().getElemType().getKind())
						report_error("Greska!!! Nisu istog tipa pri dodeli!!!", designatorStmt);

					if (!elementLeft)
						report_error("Greska!!! " + leftIdent.getName() + " nije element niza, vec niz", designatorStmt);
				}
			}
			else if (factType.getKind() != leftIdent.getType().getKind())
				report_error("Greska!!! Nisu istog tipa pri dodeli!!!", designatorStmt);
					
			assignOp = false;
			elementLeft = false;
			element = false;
		}
		
		else if (decOp || incOp) {
			if (designObj.getType().getKind() == Struct.Class)
				report_error("Ne moze klasa u izrazu za inc i dec!!!", designatorStmt);
			
			if (designObj.getType().getKind() == Struct.Array) {
				if (!element)
					report_error("Greska!!! " + identFactor.getName() + " nije element niza, vec niz", designatorStmt);
					
				if (designObj.getType().getElemType().getKind() != Struct.Int)
					report_error("Greska!!! Samo int moze biti u izrazu + - * /", designatorStmt);
			}
			else
			{
				if (designObj.getType().getKind() != Struct.Int)
					report_error("Greska!!! Samo int moze biti u izrazu za inc i dec", designatorStmt);
			}
		}
		
		mulopVisit = false;
		addopVisit = false;
		subopVisit = false;
		modopVisit = false;
		divopVisit = false;
		element = false;
		decOp = false;
		incOp = false;
		assignOp = false;
		
		//elementNewIdent=false;
	}	
		
	public void visit(Designator designator) { 
		//trazi iz currentscope pa ako tu ne nadje ide dalje
		designObj = Tab.find(designator.getName());
		
		if (designObj == Tab.noObj)
			report_error("Greska " + designator.getLine() + ":" + designator.getName() + " nije deklarisan", null);
		else if (designObj.getKind() != Obj.Meth) {
			stv = new DumpSymbolTableVisitor();
			stv.visitObjNode(designObj);
			report_info("Pretraga na " + designator.getLine() + ", nadjeno " + designObj.getName() + " => " + stv.getOutput(), null);
		}
		 
		if (readVisited) {
			designatorVisited = true;
			readVisited = false;
		}
		
		designator.obj = designObj;
	}
	
	//Operacije
	
	public void visit(Assignop assign) {
		assignOp = true;
		leftIdent = designObj;
		
		if (designObj.getKind() == Obj.Con)
			report_error("Ne moze se dodeljivati konstanti ", assign);
		
		if (designObj.getType().getKind() == Struct.Array) {
			if (element) {
				elementLeft = true;
				elementNewIdent = true;
			} 
			else
			{
				elementLeft = false;
				elementNewIdent = false;
			}
			
			element=false;
		} 
		
		//da ako je levo niz upamtimo da li je element niza ili samo niz
		//elementNeweIdent jer ne sme niz[1]=new int[...]
		
		mulopVisit = false;
		addopVisit = false;
		subopVisit = false;
		modopVisit = false;
		divopVisit = false;
		
		// u slucaju da je sa sjedne stane charniz[1+1+1]=char;
		// da ne ostane mulop ili addop i onda da se proverava u designstmt 
		// da li je int a ne mora da bude int
	}
	
	public void visit(Factors factors) {
		//provreava da li je index niza int i da li je izraz u Type[..] int
		//sustinski sluzi za poslenji factor u njima i za slucaj kad je samo jedan jer nema addop i mulop
		if ((firstArrayBegin || exprInNew) && !lastNewFactor) { 
			
			//nijedan index niza niti factor u new [...] ne mogu biti razl od int
			if (factType.getKind() == Struct.Class)
				report_error("Ne moze klasa u indeksu niza ili u izrazu za dodelu!!!", factors);
			
			if (factType.getKind() == Struct.Array) {
				if (!element)
					report_error("Greska!!! " + identFactor.getName() + " je niz i ne moze biti u indeksu ili izrazu za dodelu", factors);

				if (factType.getElemType().getKind() != Struct.Int)
					report_error("Greska!!! Samo int moze biti u izrazu + - * /", factors);
			}
			else
			{
				if (factType.getKind() != Struct.Int)
					report_error("Greska!!! Samo int moze biti u indeksu niza ili u izrazu new", factors);
			}
		}
		
		if (printVisit) {
			if (factType.getKind() == Struct.Class)
				report_error("Ne moze klasa u izrazu za print!!!", factors);

			if (factType.getKind() == Struct.Array && !element) 
				report_error("Greska!!! Mora biti int, char ili bool u izrazu za print", factors);
		}	
		
		lastNewFactor = false;
		
		//ne sme element=false jer ce napraviti problem u niz[1]+ kad u addop proverava sta je niz
		
		//za gen koda
		factors.getFactor().struct = factType;
	}
	
	public void visit(Expr1 expr) {
		expr.struct = factType;
	}
	
	public void visit(Increment inc) {
		incOp = true;
	}
	
	public void visit (Decrement dec) {
		decOp = true;
	}
	
	public void visit(ArrayIndexBegin index) {
		if (openCnt == 0)
			firstArrayBegin = true;	
		
		openCnt++;
	}
	
	public void visit(ArrayIndexEnd index) {
		openCnt--;
		element = true; 
		
		if (openCnt==0)
			firstArrayBegin = false;
	}
	
	public void visit(Plus plus) {
		if (factType.getKind() == Struct.Class)
			report_error("Ne moze se sabirati instanca klase!!!", plus);
		
		if (factType.getKind() == Struct.Array) {
			if (!element)
				report_error("Greska!!! " + identFactor.getName() + " je niz i ne moze se sabirati", plus);
			
			if (factType.getElemType().getKind() != Struct.Int)
				report_error("Greska!!! Samo int moze biti u izrazu + - * /", plus);
		}
		else
		{
			if (factType.getKind() != Struct.Int)
				report_error("Greska!!! Samo int moze biti u izrazu + - * /", plus);
		}
		
		if (!firstArrayBegin) {
			addopVisit = true;
		}
		
		//sluzi za kasniju proveru poslednjeg factora (ovde rpoveravajunsamo factor pre ...factor; nema ko da proveri)
		//ali samo u slucaju da nije element niza jer svaki element niza se proverava u factors
		
		element = false;
	}
	
	public void visit(Mul mul) {
		if (factType.getKind() == Struct.Class)
			report_error("Ne moze se instanca klase mnoziti!!!", mul);
		
		if (factType.getKind() == Struct.Array) {
			if (!element)
				report_error("Greska!!! " + identFactor.getName() + " nije element niza, vec niz i ne moze se mnoziti!!!", mul);

			if (factType.getElemType().getKind() != Struct.Int)
				report_error("Greska!!! Samo int moze biti u izrazu + - * /", mul);
		}
		else
		{
			if (factType.getKind() != Struct.Int)
				report_error("Greska!!! Samo int moze biti u izrazu + - * /", mul);
		}
		
		if (!firstArrayBegin) {
			mulopVisit = true;
		}

		element=false;
	}
	
	//Mulop i addop proveravaju da li je factor pre njeih int
	
	public void visit(Minus minus) {
		if (factType.getKind() == Struct.Class)
			report_error("Ne moze instanca klase u izrazu sa minus!!!", minus);
		
		if (factType.getKind() == Struct.Array) {
			if (!element)
				report_error("Greska!!! " + identFactor.getName() + " nije element niza, vec niz i ne moze se oduzimati", minus);

			if (factType.getElemType().getKind() != Struct.Int)
				report_error("Greska!!! Samo int moze biti u izrazu + - * /", minus);
		}
		else
		{
			if (factType.getKind() != Struct.Int)
				report_error("Greska!!! Samo int moze biti u izrazu + - * /", minus);
		}
		
		if (!firstArrayBegin) {
			subopVisit = true;
		}

		element=false;
	}
	
	public void visit(Div div) {
		if (factType.getKind() == Struct.Class)
			report_error("Ne moze se instanca klase deliti!!!", div);
		
		if (factType.getKind() == Struct.Array) {
			if (!element)
				report_error("Greska!!! " + identFactor.getName() + " nije element niza, vec niz i ne moze se deliti", div);

			if (factType.getElemType().getKind() != Struct.Int)
				report_error("Greska!!! Samo int moze biti u izrazu + - * /", div);
		}
		else
		{
			if (factType.getKind() != Struct.Int)
				report_error("Greska!!! Samo int moze biti u izrazu + - * /", div);
		}
		
		if (!firstArrayBegin) {
			divopVisit = true;
		}

		element = false;
	}
	
	public void visit(Mod mod) {
		if (factType.getKind() == Struct.Class)
			report_error("Ne moze klasa u izrazu sa mod!!!", mod);
		
		if (factType.getKind() == Struct.Array) {
			if (!element)
				report_error("Greska!!! " + identFactor.getName() + " nije element niza, vec niz i ne moze sa mod", mod);

			if (factType.getElemType().getKind() != Struct.Int)
				report_error("Greska!!! Samo int moze biti u izrazu + - * /", mod);
		}
		else
		{
			if (factType.getKind() != Struct.Int)
				report_error("Greska!!! Samo int moze biti u izrazu + - * /", mod);
		}
		
		if (!firstArrayBegin) {
			modopVisit = true;
		}

		element = false;
	}
	
	public void visit(Read read) {
		readVisited = true;
	}
	
	public void visit(ReadStatement read) {
		if (!designatorVisited)
			report_error("Greska!!! Nije designator u READ naredbi!!!", read);
		else if (designObj.getType().getKind() == Struct.Array && !element || designObj.getType().getKind() == Struct.Class)
			report_error("Greska!!! Ne mogu niz i klasa u read!!!", read);

		designatorVisited = false;
		readVisited = false;
		
		element = false;
		
		if (insideMethod && currentMethod.getName().equals("main"))
			numStatement++;
	}
	
	public void visit(NewType newType) {
		leftNewIdent = designObj;
		
		//zapamti obj sa leve strane = new Type[];
		assignOp = false; //ne zelim da se ponasa kao assignOp jer ce porediti ono sa leve strane i prvi izraz u []
	}
	
	public void visit(NewFactor newFactor) {
		if (newLBracket) {
			if (returned) {
				newVisited = true;
				
				if (typeNode.getType().getKind() == Struct.Class)
					report_error("Alokacija niza ne klase!!!", newFactor);
				retNew = Tab.noObj;
			}
			//ne moze niz da bude povratna vrednost f-je pa da bi tamo bacilo gresku
			else if (typeNode.getType().getKind() == Struct.Class)
				report_error("Alokacija niza ne klase!!!", newFactor);
			else if (leftNewIdent.getType().getKind() != Struct.Array || elementNewIdent)
				report_error("Mora se alocirati niz!!!", newFactor);
			else if (leftNewIdent.getType().getElemType().getKind() != typeNode.getType().getKind())
				report_error("Nisu odgovarajuceg tipa!!!", newFactor);
		}
		else
		{
			if (returned) {
				newVisited = true;
				retNew = typeNode;
			}
			else if (leftNewIdent.getType().getKind() == Struct.Class && typeNode.getType().getKind() == Struct.Class) {
				if (!(leftNewIdent.getType().compatibleWith(typeNode.getType())))
					report_error("Nisu kompatibilni tipovi pri alokaciji!!!", null);
			}
			else
			{
				System.out.println("SKK");
				report_error("Mora biti klasnog tipa!!!", null);
			}
		}

		lastNewFactor = true;
		newLBracket = false;
		leftNewIdent = Tab.noObj;
		
		exprInNew = false;
		
		newFactor.getNewType().getType().struct=currentType;
		
		//element=false; ili ovde ili u designatorStatement svejedno
	}
	
	public void visit(ExprBox bracket) {
		newLBracket = true;
		
		exprInNew = true;
		//u [...] smo ne moze nista sem int
	}
	
	public void visit (FuncCall funcCall) {
		Obj funcObj = Tab.find(funcCall.getDesignator().getName());
		
		funcCall.getDesignator().obj = funcObj;
		factType = funcObj.getType();
		
		if (Obj.Meth == funcObj.getKind()) {
			if (Tab.noType.getKind() == funcObj.getType().getKind())
				report_error("Greska!!! Ne moze void funkcija " + funcObj.getName() + " u izrazu!!!", funcCall);
			else
			{
				report_info("Pronadjen poziv funkcije " + funcObj.getName(), funcCall);
				funcCall.struct = funcObj.getType();
				
				if (insideMethod && currentMethod.getName().equals("main"))
					numFunctionCall++;
			}
		}
		else
		{
			report_error("Greska! Nadjeno ime " + funcObj.getName() + " nije funkcija!", funcCall);
			funcCall.struct = Tab.noType;
		}
	}
	
	public void visit(TermMinus termMinus) {
		if (factType.getKind() == Struct.Class)
			report_error("Ne moze instanca klase u izrazu -Term!!!", termMinus);

		if (factType.getKind() == Struct.Array) {
			if (!element)
				report_error("Greska!!! " + identFactor.getName() + " nije element niza, vec niz i ne moze biti u -Term ", termMinus);

			if (factType.getElemType().getKind() != Struct.Int)
				report_error("Greska!!! Samo int moze biti u izrazu -Term", termMinus);
		}
		else
		{
			if (factType.getKind() != Struct.Int)
				report_error("Greska!!! Samo int moze biti u izrazu -Term", termMinus);
		}
	}
	
	public boolean passed(){
		return !errorDetected;
	}
}
