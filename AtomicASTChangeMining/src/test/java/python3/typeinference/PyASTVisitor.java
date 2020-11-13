package python3.typeinference;

import org.jpp.astnodes.Visitor;
import org.jpp.astnodes.ast.Assign;
import org.jpp.astnodes.ast.AsyncFor;
import org.jpp.astnodes.ast.AsyncFunctionDef;
import org.jpp.astnodes.ast.AsyncWith;
import org.jpp.astnodes.ast.Attribute;
import org.jpp.astnodes.ast.AugAssign;
import org.jpp.astnodes.ast.Await;
import org.jpp.astnodes.ast.BinOp;
import org.jpp.astnodes.ast.BoolOp;
import org.jpp.astnodes.ast.Break;
import org.jpp.astnodes.ast.Bytes;
import org.jpp.astnodes.ast.Call;
import org.jpp.astnodes.ast.ClassDef;
import org.jpp.astnodes.ast.Compare;
import org.jpp.astnodes.ast.Continue;
import org.jpp.astnodes.ast.Delete;
import org.jpp.astnodes.ast.Dict;
import org.jpp.astnodes.ast.DictComp;
import org.jpp.astnodes.ast.Ellipsis;
import org.jpp.astnodes.ast.ExceptHandler;
import org.jpp.astnodes.ast.Expr;
import org.jpp.astnodes.ast.Expression;
import org.jpp.astnodes.ast.ExtSlice;
import org.jpp.astnodes.ast.For;
import org.jpp.astnodes.ast.FormattedValue;
import org.jpp.astnodes.ast.FunctionDef;
import org.jpp.astnodes.ast.GeneratorExp;
import org.jpp.astnodes.ast.Global;
import org.jpp.astnodes.ast.If;
import org.jpp.astnodes.ast.IfExp;
import org.jpp.astnodes.ast.Import;
import org.jpp.astnodes.ast.ImportFrom;
import org.jpp.astnodes.ast.Index;
import org.jpp.astnodes.ast.Interactive;
import org.jpp.astnodes.ast.Lambda;
import org.jpp.astnodes.ast.List;
import org.jpp.astnodes.ast.ListComp;
import org.jpp.astnodes.ast.Module;
import org.jpp.astnodes.ast.Name;
import org.jpp.astnodes.ast.NameConstant;
import org.jpp.astnodes.ast.Nonlocal;
import org.jpp.astnodes.ast.Num;
import org.jpp.astnodes.ast.Pass;
import org.jpp.astnodes.ast.Raise;
import org.jpp.astnodes.ast.Return;
import org.jpp.astnodes.ast.Set;
import org.jpp.astnodes.ast.SetComp;
import org.jpp.astnodes.ast.Slice;
import org.jpp.astnodes.ast.Starred;
import org.jpp.astnodes.ast.Str;
import org.jpp.astnodes.ast.Subscript;
import org.jpp.astnodes.ast.Suite;
import org.jpp.astnodes.ast.Try;
import org.jpp.astnodes.ast.TryExcept;
import org.jpp.astnodes.ast.TryFinally;
import org.jpp.astnodes.ast.Tuple;
import org.jpp.astnodes.ast.UnaryOp;
import org.jpp.astnodes.ast.While;
import org.jpp.astnodes.ast.With;
import org.jpp.astnodes.ast.Yield;
import org.jpp.astnodes.ast.YieldFrom;

import java.util.HashMap;

public class PyASTVisitor extends Visitor {
    private int module= 0;
    private int interactive= 0;
    private int expression= 0;
    private int suite= 0;
    private int asyncFunctionDef= 0;
    private int classDef= 0;
    private int Return= 0;
    private int Delete= 0;
    private int Assign = 0;
    private int AugAssign= 0;
    private int For = 0;
    private int AsyncFor= 0;
    private int While= 0;
    private int If = 0;
    private int With = 0;
    private int AsyncWith = 0;
    private int Raise = 0;
    private int Try = 0;
    private int AssertStmt = 0;
    private int Import = 0;
    private int ImportFrom= 0;
    private int Global= 0;
    private int Nonloca= 0;
    private int Expr= 0;
    private int Pass= 0;
    private int Break= 0;
    private int Continue = 0;
    private int BoolOp = 0;
    private int BinOp = 0;
    private int UnaryOp = 0;
    private int Lambda = 0;
    private int IfExp = 0;
    private int Dict = 0;
    private int Set = 0;
    private int ListComp = 0;
    private int SetComp = 0;
    private int DictComp = 0;
    private int GeneratorExp = 0;
    private int Await = 0;
    private int Yield = 0;
    private int YieldFrom = 0;
    private int Compare = 0;
    private int Call = 0;
    private int Num = 0;
    private int Str = 0;
    private int Bytes = 0;
    private int NameConstant = 0;
    private int Ellipsis  = 0;
    private int Attribute  = 0;
    private int Subscript  = 0;
    private int Starred  = 0;
    private int Name  = 0;
    private int List  = 0;
    private int Slice  = 0;
    private int ExtSlice  = 0;
    private int Index   = 0;
    private int ExceptHandler   = 0;
    private int TryFinally   = 0;
    private int TryExcept   = 0;
    private int FormattedValue   = 0;
    private int Tuple=0;
    private int FunctionDef=0;
    HashMap<String,Integer> pyNodeMap = new HashMap<>();
    @Override
    public Object visitModule(Module node) throws Exception {
        module+=1;
        return super.visitModule(node);
    }
    @Override
    public Object visitInteractive(Interactive node) throws Exception {
        interactive+=1;
        return super.visitInteractive(node);
    }
    @Override
    public Object visitExpression(Expression node) throws Exception {
        expression+=1;
        return super.visitExpression(node);
    }
    @Override
    public Object visitSuite(Suite node) throws Exception {
        suite+=1;
        return super.visitSuite(node);
    }
    @Override
    public Object visitFunctionDef(FunctionDef node) throws Exception {
        FunctionDef+=1;
        return super.visitFunctionDef(node);
    }
    @Override
    public Object visitAsyncFunctionDef(AsyncFunctionDef node) throws Exception {
        asyncFunctionDef+=1;
        return super.visitAsyncFunctionDef(node);
    }
    @Override
    public Object visitClassDef(ClassDef node) throws Exception {
        classDef+=1;
        return super.visitClassDef(node);
    }
    @Override
    public Object visitReturn(Return node) throws Exception {
        Return+=1;
        return super.visitReturn(node);
    }
    @Override
    public Object visitDelete(Delete node) throws Exception {
        Delete +=1;
        return super.visitDelete(node);
    }
    @Override
    public Object visitAssign(Assign node) throws Exception {
        Assign +=1;
        return super.visitAssign(node);
    }
    @Override
    public Object visitAugAssign(AugAssign node) throws Exception {
        AugAssign+=1;
        return super.visitAugAssign(node);
    }
    @Override
    public Object visitFor(For node) throws Exception {
        For +=1;
        return super.visitFor(node);
    }
    @Override
    public Object visitAsyncFor(AsyncFor node) throws Exception {
        AsyncFor+=1;
        return super.visitAsyncFor(node);
    }
    @Override
    public Object visitWhile(While node) throws Exception {
        While+=1;
        return super.visitWhile(node);
    }
    @Override
    public Object visitIf(If node) throws Exception {
        If+=1;
        return super.visitIf(node);
    }
    @Override
    public Object visitWith(With node) throws Exception {
        With+=1;
        return super.visitWith(node);
    }
    @Override
    public Object visitAsyncWith(AsyncWith node) throws Exception {
        AsyncWith+=1;
        return super.visitAsyncWith(node);
    }
    @Override
    public Object visitRaise(Raise node) throws Exception {
        Raise+=1;
        return super.visitRaise(node);
    }
    @Override
    public Object visitTry(Try node) throws Exception {
        Try+=1;
        return super.visitTry(node);
    }
    @Override
    public Object visitAssert(org.jpp.astnodes.ast.Assert node) throws Exception {
        AssertStmt+=1;
        return super.visitAssert(node);
    }
    @Override
    public Object visitImport(Import node) throws Exception {
        Import+=1;
        return super.visitImport(node);
    }
    @Override
    public Object visitImportFrom(ImportFrom node) throws Exception {
        ImportFrom +=1;
        return super.visitImportFrom(node);
    }
    @Override
    public Object visitGlobal(Global node) throws Exception {
        Global+=1;
        return super.visitGlobal(node);
    }
    @Override
    public Object visitNonlocal(Nonlocal node) throws Exception {
        Nonloca+=1;
        return super.visitNonlocal(node);
    }
    @Override
    public Object visitExpr(Expr node) throws Exception {
        Expr+=1;
        return super.visitExpr(node);
    }
    @Override
    public Object visitPass(Pass node) throws Exception {
        Pass+=1;
        return super.visitPass(node);
    }
    @Override
    public Object visitBreak(Break node) throws Exception {
        Break+=1;
        return super.visitBreak(node);
    }
    @Override
    public Object visitContinue(Continue node) throws Exception {
        Continue+=1;
        return super.visitContinue(node);
    }
    @Override
    public Object visitBoolOp(BoolOp node) throws Exception {
        BoolOp+=1;
        return super.visitBoolOp(node);
    }
    @Override
    public Object visitBinOp(BinOp node) throws Exception {
        BinOp+=1;
        return super.visitBinOp(node);
    }
    @Override
    public Object visitUnaryOp(UnaryOp node) throws Exception {
        UnaryOp+=1;
        return super.visitUnaryOp(node);
    }
    @Override
    public Object visitLambda(Lambda node) throws Exception {
        Lambda+=1;
        return super.visitLambda(node);
    }
    @Override
    public Object visitIfExp(IfExp node) throws Exception {
        IfExp+=1;
        return super.visitIfExp(node);
    }
    @Override
    public Object visitDict(Dict node) throws Exception {
        Dict+=1;
        return super.visitDict(node);
    }
    @Override
    public Object visitSet(Set node) throws Exception {
        Set+=1;
        return super.visitSet(node);
    }
    @Override
    public Object visitListComp(ListComp node) throws Exception {
        ListComp+=1;
        return super.visitListComp(node);
    }
    @Override
    public Object visitSetComp(SetComp node) throws Exception {
        SetComp+=1;
        return super.visitSetComp(node);
    }
    @Override
    public Object visitDictComp(DictComp node) throws Exception {
        DictComp+=1;
        return super.visitDictComp(node);
    }
    @Override
    public Object visitGeneratorExp(GeneratorExp node) throws Exception {
        GeneratorExp+=1;
        return super.visitGeneratorExp(node);
    }
    @Override
    public Object visitAwait(Await node) throws Exception {
        Await+=1;
        return super.visitAwait(node);
    }
    @Override
    public Object visitYield(Yield node) throws Exception {
        Yield+=1;
        return super.visitYield(node);
    }
    @Override
    public Object visitYieldFrom(YieldFrom node) throws Exception {
        YieldFrom+=1;
        return super.visitYieldFrom(node);
    }
    @Override
    public Object visitCompare(Compare node) throws Exception {
        Compare+=1;
        return super.visitCompare(node);
    }
    @Override
    public Object visitCall(Call node) throws Exception {
        Call+=1;
        return super.visitCall(node);
    }
    @Override
    public Object visitNum(Num node) throws Exception {
        Num+=1;
        return super.visitNum(node);
    }
    @Override
    public Object visitStr(Str node) throws Exception {
        Str+=1;
        return super.visitStr(node);
    }
    @Override
    public Object visitBytes(Bytes node) throws Exception {
        Bytes+=1;
        return super.visitBytes(node);
    }
    @Override
    public Object visitNameConstant(NameConstant node) throws Exception {
        NameConstant+=1;
        return super.visitNameConstant(node);
    }
    @Override
    public Object visitEllipsis(Ellipsis node) throws Exception {
        Ellipsis+=1;
        return super.visitEllipsis(node);
    }
    @Override
    public Object visitAttribute(Attribute node) throws Exception {
        Attribute+=1;
        return super.visitAttribute(node);
    }
    @Override
    public Object visitSubscript(Subscript node) throws Exception {
        Subscript+=1;
        return super.visitSubscript(node);
    }
    @Override
    public Object visitStarred(Starred node) throws Exception {
        Starred+=1;
        return super.visitStarred(node);
    }
    @Override
    public Object visitName(Name node) throws Exception {
        Name+=1;
        return super.visitName(node);
    }
    @Override
    public Object visitList(List node) throws Exception {
        List+=1;
        return super.visitList(node);
    }
    @Override
    public Object visitTuple(Tuple node) throws Exception {
        Tuple+=1;
        return super.visitTuple(node);
    }
    @Override
    public Object visitSlice(Slice node) throws Exception {
        Slice+=1;
        return super.visitSlice(node);
    }
    @Override
    public Object visitExtSlice(ExtSlice node) throws Exception {
        ExtSlice+=1;
        return super.visitExtSlice(node);
    }
    @Override
    public Object visitIndex(Index node) throws Exception {
        Index+=1;
        return super.visitIndex(node);
    }
    @Override
    public Object visitExceptHandler(ExceptHandler node) throws Exception {
        ExceptHandler+=1;
        return super.visitExceptHandler(node);
    }
    @Override
    public Object visitTryFinally(TryFinally node) throws Exception {
        TryFinally+=1;
        return super.visitTryFinally(node);
    }
    @Override
    public Object visitTryExcept(TryExcept node) throws Exception {
        TryExcept+=1;
        return super.visitTryExcept(node);
    }
    @Override
    public Object visitFormattedValue(FormattedValue node) throws Exception {
        FormattedValue+=1;
        return super.visitFormattedValue(node);
    }

    public HashMap<String, Integer> getPythonASTStats(){
        pyNodeMap.put("FunctionDef",FunctionDef);
        pyNodeMap.put("module",module);
        pyNodeMap.put("interactive",interactive);
        pyNodeMap.put("expression",expression);
        pyNodeMap.put("suite",suite);
        pyNodeMap.put("asyncFunctionDef",asyncFunctionDef);
        pyNodeMap.put("classDef",classDef);
        pyNodeMap.put("Return",Return);
        pyNodeMap.put("Delete",Delete);
        pyNodeMap.put("Assign",Assign);
        pyNodeMap.put("AugAssign",AugAssign);
        pyNodeMap.put("For",For);
        pyNodeMap.put("AsyncFor",AsyncFor);
        pyNodeMap.put("While",While);
        pyNodeMap.put("If",If);
        pyNodeMap.put("With",With);
        pyNodeMap.put("AsyncWith",AsyncWith);
        pyNodeMap.put("Raise",Raise);
        pyNodeMap.put("Try",Try);
        pyNodeMap.put("AssertStmt",AssertStmt);
        pyNodeMap.put("Import",Import);
        pyNodeMap.put("ImportFrom",ImportFrom);
        pyNodeMap.put("Global",Global);
        pyNodeMap.put("Nonloca",Nonloca);
        pyNodeMap.put("Expr",Expr);
        pyNodeMap.put("Pass",Pass);
        pyNodeMap.put("Break",Break);
        pyNodeMap.put("Continue",Continue);
        pyNodeMap.put("BoolOp",BoolOp);
        pyNodeMap.put("BinOp",BinOp);
        pyNodeMap.put("UnaryOp",UnaryOp);
        pyNodeMap.put("Lambda",Lambda);
        pyNodeMap.put("IfExp",IfExp);
        pyNodeMap.put("Dict",Dict);
        pyNodeMap.put("Set",Set);
        pyNodeMap.put("ListComp",ListComp);
        pyNodeMap.put("SetComp",SetComp);
        pyNodeMap.put("DictComp",DictComp);
        pyNodeMap.put("GeneratorExp",GeneratorExp);
        pyNodeMap.put("Await",Await);
        pyNodeMap.put("Yield",Yield);
        pyNodeMap.put("YieldFrom",YieldFrom);
        pyNodeMap.put("Compare",Compare);
        pyNodeMap.put("Call",Call);
        pyNodeMap.put("Num",Num);
        pyNodeMap.put("Str",Str);
        pyNodeMap.put("Bytes",Bytes);
        pyNodeMap.put("NameConstant",NameConstant);
        pyNodeMap.put("Ellipsis",Ellipsis);
        pyNodeMap.put("Attribute",Attribute);
        pyNodeMap.put("Subscript",Subscript);
        pyNodeMap.put("Starred",Starred);
        pyNodeMap.put("Name",Name);
        pyNodeMap.put("List",List);
        pyNodeMap.put("Slice",Slice);
        pyNodeMap.put("ExtSlice",ExtSlice);
        pyNodeMap.put("Index",Index);
        pyNodeMap.put("ExceptHandler",ExceptHandler);
        pyNodeMap.put("TryFinally",TryFinally);
        pyNodeMap.put("TryExcept",TryExcept);
        pyNodeMap.put("FormattedValue",FormattedValue);
        pyNodeMap.put("Tuple",Tuple);
        return pyNodeMap;
    }

}
