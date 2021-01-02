package python3;

import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.jpp.PyASTParser;
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
import org.jpp.astnodes.base.mod;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import python3.typeinference.PyASTVisitor;
import utils.JavaASTUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class TestASTConversion {
    private static org.apache.log4j.Logger log = Logger.getLogger(TestASTConversion.class);
    HashMap<String, Integer> pyNodeMap = new HashMap<>();
    private int numberOfNodes = 0;
    private mod md;
    private int module = 0;
    private int interactive = 0;
    private int expression = 0;
    private int suite = 0;
    private int asyncFunctionDef = 0;
    private int classDef = 0;
    private int Return = 0;
    private int Delete = 0;
    private int Assign = 0;
    private int AugAssign = 0;
    private int For = 0;
    private int AsyncFor = 0;
    private int While = 0;
    private int If = 0;
    private int With = 0;
    private int AsyncWith = 0;
    private int Raise = 0;
    private int Try = 0;
    private int AssertStmt = 0;
    private int Import = 0;
    private int ImportFrom = 0;
    private int Global = 0;
    private int Nonloca = 0;
    private int Expr = 0;
    private int Pass = 0;
    private int Break = 0;
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
    private int Ellipsis = 0;
    private int Attribute = 0;
    private int Subscript = 0;
    private int Starred = 0;
    private int Name = 0;
    private int List = 0;
    private int Slice = 0;
    private int ExtSlice = 0;
    private int Index = 0;
    private int ExceptHandler = 0;
    private int TryFinally = 0;
    private int TryExcept = 0;
    private int FormattedValue = 0;
    private int Tuple = 0;
    private int FunctionDef = 0;

    @AfterMethod
    public void afterMethod(ITestResult result) {
        if (result.getStatus() == ITestResult.SUCCESS) {
            // Setting driver used to false as this test case is pass
            PyNodeCounter pyNodeCounter = new PyNodeCounter();
            try {
                pyNodeCounter.visit(md);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void saveStats() {

        Gson gson = new Gson();
        Reader reader = null;
        Map<String, Integer> map = null;
        final File path = new File("./node_map1.json");
        if (path.exists()) {
            try {
                reader = Files.newBufferedReader(Paths.get("node_map1.json"));
                Map<String, Double> map1 = gson.fromJson(reader, Map.class);

                map = map1.entrySet().stream().collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().intValue()));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Gson gson1 = new Gson();
        Map<String, Integer> toserialize;
        //serialization process
        if (map != null) {
            Map<String, Integer> finalMap = map;
            toserialize = pyNodeMap.entrySet().stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            e -> e.getValue() + finalMap.get(e.getKey())
                    ));

        } else {
            toserialize = pyNodeMap;
        }
        String jsonFormat = gson1.toJson(toserialize);
        try {
            Files.write(Paths.get("node_map1.json"), jsonFormat.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    @Test
//    public void runAllTest() {
//        file1();
//        file2();
//        file3();
//        file4();
//        file5();
//        file6();
//        file8();
//        file9();
//        file10();
//        file11();
////        file12();
//        file13();
//        file14();
//        file15();
//        file16();
//        file17();
//        file18();
//        file19();
//        file20();
//        file21();
//        file22();
//        file23();
//        file24();
//        file26();
//        file27();
//        file28();
//        file29();
//        file30();
//        file31();
//        file32();
//        file33();
//        file34();
//        file35();
//        //   file36();
//        file37();
//        file38();
//        file39();
//        file40();
//        file41();
//        file42();
//        file43();
//        file44();
//        file45();
//        file46();
//        file47();
//        file48();
//        file49();
//        file50();
//        file51();
//        file52();
//        // file53();  tuple in tuples
//        file54();
//        file55();
//        file56();
//        file57();
//        file58();
//        file59();
//        file60();
//        file61();
//        file62();
//        file63();
//        file64();
//        file65();
//        file66();
//        file67();
//        file68();
//        file69();
//        file70();
//        file71();
//        file72();
//        file73();
//        file74();
//        file75();
//        file76();
//        file77();
//        file78();
//        file79();
//        file80();
//        file81();
//        file82();
//        file83();
//        file84();
//        file85();
//        file86();
//        file87();
//        file88();
//        file89();
//        file90();
//        file91();
//        file92();
//        file93();
//        file94();
//        file95();
//        file96();
//        file97();
//        file98();
//        file99();
//        file100();
//        file101();
//        file102();
//        file103();
//        file104();
//        file105();
//        file106();
//        file107();
//        file108();
//        file109();
//        file110();
//        file111();
//        file112();
//        file114();
//        file115();
//        file116();
//        file117();
//        file118();
//        file119();
//        file120();
//        file121();
//        file122();
//        file123();
//        file124();
//        file125();
//        //      file126();
//        file127();
//        file128();
//        file129();
//        file130();
//        file131();
//        file132();
//        file133();
//        file134();
//        file135();
//        file136();
//        file137();
//        file138();
//        file139();
//        file140();
//        file142();
//        file143();
//        file144();
//        file145();
//        file146();
//        file147();
//        file149();
//        file150();
//        file151();
//        file152();
//        file153();
//        file154();
//        file155();
//        file156();
//        file157();
//        file158();
//        file159();
//        file160();
//        file161();
//        file162();
//        file163();
//        file164();
//        file165();
//        file166();
//        file167();
//        file168();
//        file170();
//        file171();
//        file173();
//        file174();
//        file175();
//        file176();
//        file177();
//        file178();
//        file179();
//        file180();
//        file181();
//        file182();
//        file183();
//        file184();
//        file186();
//        file187();
//        file188();
//        file189();
//        file190();
//        file191();
//        file192();
//        file193();
//        file195();
//        file196();
//        file197();
//        file198();
//        file199();
//        file200();
//        file201();
//        file202();
//        file203();
//        file204();
//        file205();
//        file206();
//        file207();
//        file208();
//        file209();
//        file210();
//        file211();
//        file212();
//        file213();
//        file214();
//        file215();
//        file216();
//        file218();
//        file219();
//        //   file220(); List comp with conditional
//        file221();
//        file222();
//        file223();
//        //   file224(); List comp with conditional
//        file225();
//        file226();
//        file228();
//        file229();
//        file230();
//        file231();
//        file232();
//        file233();
//        file235();
//        file236();
//        file237();
//        file238();
//        file239();
//        //    file240();  //conditional list comp
//        file241();
//        file242();
//        file243();
//        file244();
//        file245();
//        //     file246(); //conditional list comp
//        //     file247(); //conditional list comp
//        //     file248(); //conditional list comp
//        file249();
//        file250();
//        file251();
//        file252();
//        file253();
//        file254();
//        file255();
//        file256();
//        file257();
//        //     file258(); //conditional list comp
//        file259();
//        file260();
//        //     file261();  //conditional list comp
//        file262();
//        file263();
//        file264();
//        file265();
//        file267();
//        file268();
//        file269();
//        file270();
//        file271();
//        file272();
//        file273();
//        file274();
//        file275();
//        file276();
//        file277();
//        file279();
//        file280();
//        file281();
//        file282();
//        file283();
//        file284();
//        file285();
//        file286();
//        file287();
//        file288();
//        file289();
//        file290();
//        file291();
//        file292();
//        file293();
//        file294();
//        file295();
//        file296();
//        file297();
//        file298();
//        file299();
//        file300();
//        file301();
//        file302();
//        file303();
//        file304();
//        file305();
//        file306();
//        file307();
//        file308();
//        file309();
//        file310();
//        file311();
//        file312();
//        file314();
//        file315();
//        file316();
//        //    file317(); //conditional list comp
//        file318();
//        file319();
//        file320();
//        file321();
//        file322();
//        file323();
//        file324();
//        file325();
//        file326();
//        file327();
//        file328();
//        file329();
//        //    file330(); //conditional list comp
//        //    file331();  //conditional list comp
//        file332();
//        file333();
//        file334();
//        file335();
//        file336();
//        file337();
//        file338();
//        file339();
//        file340();
//        file341();
//        file342();
//        file343();
//        file344();
//        file345();
//        file346();
//        file347();
//        file348();
//        file349();
//        file350();
//        file351();
//        file352();
//        file353();
//        file354();
//        file355();
//        file356();
//        file357();
//        file358();
//        file359();
//        file360();
//        file361();
//        file362();
//        file363();
//        file364();
//        file365();
//        file366();
//        file367();
//        file368();
//        //    file369();  List Comp is with Conditionals
//        file370();
//        file371();
//        //  file372(); lambdas in arguments
//        file373();
//        file374();
//        file375();
//        file376();
//        file377();
//        file378();
//        file379();
//        file380();
//        file381();
//        file382();
//        file383();
//        file384();
//        file385();
//        file386();
//        file387();
//        file388();
//        file389();
//        file390();
//        file391();
//        file392();
//        file393();
//        //       file394(); yield from
//        file395();
//        file396();
//        file397();
//        file398();
//        file399();
//        file400();
//        file401();
//        file403();
//        file404();
//        file405();
//        file406();
//        file407();
//        file408();
//        file409();
//        file410();
//        file411();
//        file412();
//        file413();
//        file414();
//        file415();
//        file416();
//        file417();
//        file418();
//        file419();
//        file420();
//        file421();
//        file422();
//        file423();
//        file424();
//        file425();
//        file426();
//        file427();
//        file428();
//        file429();
//        file430();
//        file431();
//        file432();
//        file433();
//        file434();
//        file435();
//        file436();
//        file437();
//        file438();
//        file439();
//        file440();
//        file441();
//        file442();
//        file443();
//        file444();
//        //   file445();  Fix this
//        file446();
//        file447();
//        file448();
//        file449();
//        file450();
//        file451();
//        file452();
//        file453();
//        file454();
//        file455();
//        //       file456();  list comp with conditional exp
//        file457();
//        file458();
//        file459();
//        file460();
//        file461();
//        file462();
//        file463();
//        file464();
//        file465();
//        //     file466();  LMBDA in argument
//        file467();
//        file468();
//        file469();
//        file470();
//        file471();
//        file472();
//        file473();
//        file474();
//        file475();
//        file476();
//        file477();
//        file478();
//        file479();
//        file480();
//        //       file481();  Lambda in argmuments
//        //       file482();  List comp with generators
//        file483();
//        file484();
//        file485();
//        file486();
//        file487();
//        file488();
//        file489();
//        file490();
//        file491();
//        file492();
//        file493();
//        file494();
//        file495();
//        file496();
//        file497();
//        file498();
//        file499();
//        file500();
//        file501();
//        //      file502();  List comp with conditionals
//        file503();
//        file504();
//        file505();
//        file506();
//        file507();
//        file508();
//        file509();
//        file510();
//        file511();
//        file512();
//        file513();
//        file514();
//        file515();
//        file516();
//        file517();
//        file518();
//        file519();
//        file520();
//        file521();
//        file522();
//        file523();
//        file524();
//        file525();
//        file526();
//        file527();
//        file528();
//        file529();
//        file530();
//        file531();
//        file532();
//        file533();
//        file534();
//        file535();
//        file536();
//        file537();
//        file538();
//        file539();
//        file540();
//        file541();
//        file542();
//        file543();
//        file544();
//        file545();
//        file546();
//        file547();
//        file548();
//        file549();
//        file550();
//        file551();
//        file552();
//        file553();
//        file554();
//        file555();
//        file556();
//        file557();
//        file558();
//        file559();
//        file560();
//        file561();
//        file562();
//        file563();
//        file564();
//        file565();
//        file566();
//        file567();
//        file568();
//        file569();
//        file570();
//        file571();
//        file572();
//        file573();
//        file574();
//        file575();
//        file576();
//        file577();
//        file578();
//        file579();
//        file580();
//        file581();
//        file582();
//        file583();
//        file584();
//        file585();
//        file586();
//        file587();
//        file588();
//        file589();
//        file590();
//        file591();
//        file592();
//        file593();
//        file594();
//        file595();
//        file596();
//        file597();
//        file598();
//        file599();
//        file600();
//        file601();
//        file602();
//        file603();
//        file604();
//        file605();
//        file606();
//        file607();
//        file608();
//        file609();
//        file610();
//        file611();
//        file612();
//        file613();
//        file614();
//        file615();
//        //      file616(); List Comp with conditional
//        file617();
//        file618();
//        file619();
//        file620();
//        file621();
//        file622();
//        file623();
//        file624();
//        file625();
//        file626();
//        file627();
//        file628();
//        file629();
//        file630();
//        file631();
//        file632();
//        file633();
//        file634();
//        file635();
//        file636();
//        file637();
//        file638();
//        file639();
//        file640();
//        file641();
//        file642();
//        file643();
//        file644();
//        file645();
//        file646();
//        file647();
//        file648();
//        file649();
//        file650();
//        file651();
//        file652();
//        file653();
//        file654();
//        file655();
//        file656();
//        file657();
//        file658();
//        file659();
//        file660();
//        file661();
//        file662();
//        file663();
//        file664();
//        file665();
//        file666();
//        file667();
//        file668();
//        file669();
//        file670();
//        file671();
//        file672();
//        file673();
//        file674();
//        file675();
//        file676();
//        file677();
//        file678();
//        file679();
//        file680();
//        file681();
//        file682();
//        file683();
//        file684();
//        file685();
//        file686();
//        file687();
//        file688();
//        file689();
//        file690();
//        file691();
//        file692();
//        file693();
//        file694();
//        file695();
//        file696();
//        file697();
//        file698();
//        file699();
//        file700();
//        file701();
//        file702();
//        file703();
//        file704();
//        file705();
//        file706();
//        //     file707();
//        file708();
//        file709();
//        file710();
//        file711();
//        file712();
//        file713();
//        file714();
//        file715();
//        file716();
//        file717();
//        file718();
//        file719();
//        file720();
//        file721();
//        file722();
//        file723();
//        file724();
//        file725();
//        file726();
//        file727();
//        file728();
//        file729();
//        file730();
//        file731();
//        file732();
//        file733();
//        file734();
//        file735();
//        file736();
//        file737();
//        file738();
//        file739();
//        file740();
//        file741();
//        file742();
//        file743();
//        file744();
//        file745();
//        file746();
//        file747();
//        file748();
//        file749();
//        file750();
//        file751();
//        file752();
//        file753();
//        file754();
//        file755();
//        file756();
//        file757();
//        file758();
//        file759();
//        file760();
//        file761();
//        file762();
//        file763();
//        file764();
//        file765();
//        file766();
//        file767();
//        file768();
//        file769();
//        file770();
//        file771();
//        file772();
//        file773();
//        file774();
//        file775();
//        file776();
//        file777();
//        file778();
//        printStats();
//    }



    // "search_holder"
    protected CompilationUnit convert(String content) {
        mod ast = PyASTParser.parsePython(content);
        md = ast;
        PythonASTUtil pythonASTUtil = new PythonASTUtil();
        PyCompilationUnit pyCompilationUnit = pythonASTUtil.createPyCompilationUnit(ast);


        PyASTVisitor astParser = new PyASTVisitor();
        try {
            astParser.visit(ast);
        } catch (Exception e) {
            e.printStackTrace();
        }


        JDTASTVisitor jdtastVisitor = new JDTASTVisitor();
        CompilationUnit cu = (CompilationUnit) JavaASTUtil.parseSource(pyCompilationUnit.toString());
        log.warn("Errors " + cu.getProblems().length);
        cu.accept(jdtastVisitor);
        comparePythonAndJDTnodes(astParser, jdtastVisitor);

        return cu;
    }

    public void comparePythonAndJDTnodes(PyASTVisitor pyAstParser, JDTASTVisitor jdtastVisitor) {
        Assert.assertEquals(pyAstParser.getPythonASTStats().get("For").intValue(), jdtastVisitor.getStatFor("Java_EnhancedForStatement"));
//        Assert.assertTrue(pyAstParser.getPythonASTStats().get("ListComp").intValue()<=jdtastVisitor.getStatFor("Java_PyListComprehension"));
        Assert.assertEquals(pyAstParser.getPythonASTStats().get("With").intValue(), jdtastVisitor.getStatFor("Java_PyWithStatement"));
        Assert.assertTrue(pyAstParser.getPythonASTStats().get("DictComp").intValue() <= jdtastVisitor.getStatFor("Java_PyDictComprehensiont"));
        Assert.assertEquals(pyAstParser.getPythonASTStats().get("If").intValue(), jdtastVisitor.getStatFor("Java_IfStatement"));
        Assert.assertEquals(pyAstParser.getPythonASTStats().get("Raise").intValue(), jdtastVisitor.getStatFor("Java_ThrowStatement"));
        Assert.assertEquals(pyAstParser.getPythonASTStats().get("Return").intValue(), jdtastVisitor.getStatFor("Java_Return"));
        Assert.assertTrue(pyAstParser.getPythonASTStats().get("TryExcept").intValue() + pyAstParser.getPythonASTStats().get("TryFinally").intValue() ==
                jdtastVisitor.getStatFor("Java_TryStatement") || pyAstParser.getPythonASTStats().get("TryExcept").intValue()
                == jdtastVisitor.getStatFor("Java_TryStatement"));
        Assert.assertEquals(pyAstParser.getPythonASTStats().get("Continue").intValue(), jdtastVisitor.getStatFor("Java_ContinueStatement"));
        Assert.assertEquals(pyAstParser.getPythonASTStats().get("AssertStmt").intValue(), jdtastVisitor.getStatFor("Java_AssertStatement"));
        Assert.assertEquals(pyAstParser.getPythonASTStats().get("While").intValue(), jdtastVisitor.getStatFor("Java_WhileStatement"));
//        Assert.assertTrue(pyAstParser.getPythonASTStats().get("Lambda").intValue()<=jdtastVisitor.getStatFor("Java_LambdaExpression"));

    }

    public String readFile(String fileName) {
        Path resourceDirectory = Paths.get("src", "test", "resources", "ASTConversion", fileName);
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(String.valueOf(resourceDirectory));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String everything = null;
        try {
            everything = IOUtils.toString(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return everything;
    }

    protected void resetValues() {
        module = 0;
        interactive = 0;
        expression = 0;
        suite = 0;
        asyncFunctionDef = 0;
        classDef = 0;
        Return = 0;
        Delete = 0;
        Assign = 0;
        AugAssign = 0;
        For = 0;
        AsyncFor = 0;
        While = 0;
        If = 0;
        With = 0;
        AsyncWith = 0;
        Raise = 0;
        Try = 0;
        AssertStmt = 0;
        Import = 0;
        ImportFrom = 0;
        Global = 0;
        Nonloca = 0;
        Expr = 0;
        Pass = 0;
        Break = 0;
        Continue = 0;
        BoolOp = 0;
        BinOp = 0;
        UnaryOp = 0;
        Lambda = 0;
        IfExp = 0;
        Dict = 0;
        Set = 0;
        ListComp = 0;
        SetComp = 0;
        DictComp = 0;
        GeneratorExp = 0;
        Await = 0;
        Yield = 0;
        YieldFrom = 0;
        Compare = 0;
        Call = 0;
        Num = 0;
        Str = 0;
        Bytes = 0;
        NameConstant = 0;
        Ellipsis = 0;
        Attribute = 0;
        Subscript = 0;
        Starred = 0;
        Name = 0;
        List = 0;
        Slice = 0;
        ExtSlice = 0;
        Index = 0;
        ExceptHandler = 0;
        TryFinally = 0;
        TryExcept = 0;
        FormattedValue = 0;
        Tuple = 0;
        FunctionDef = 0;
    }

    @AfterClass
    protected void printNodeStatistics() {
        pyNodeMap.put("FunctionDef", FunctionDef);
        pyNodeMap.put("module", module);
        pyNodeMap.put("interactive", interactive);
        pyNodeMap.put("expression", expression);
        pyNodeMap.put("suite", suite);
        pyNodeMap.put("asyncFunctionDef", asyncFunctionDef);
        pyNodeMap.put("classDef", classDef);
        pyNodeMap.put("Return", Return);
        pyNodeMap.put("Delete", Delete);
        pyNodeMap.put("Assign", Assign);
        pyNodeMap.put("AugAssign", AugAssign);
        pyNodeMap.put("For", For);
        pyNodeMap.put("AsyncFor", AsyncFor);
        pyNodeMap.put("While", While);
        pyNodeMap.put("If", If);
        pyNodeMap.put("With", With);
        pyNodeMap.put("AsyncWith", AsyncWith);
        pyNodeMap.put("Raise", Raise);
        pyNodeMap.put("Try", Try);
        pyNodeMap.put("AssertStmt", AssertStmt);
        pyNodeMap.put("Import", Import);
        pyNodeMap.put("ImportFrom", ImportFrom);
        pyNodeMap.put("Global", Global);
        pyNodeMap.put("Nonloca", Nonloca);
        pyNodeMap.put("Expr", Expr);
        pyNodeMap.put("Pass", Pass);
        pyNodeMap.put("Break", Break);
        pyNodeMap.put("Continue", Continue);
        pyNodeMap.put("BoolOp", BoolOp);
        pyNodeMap.put("BinOp", BinOp);
        pyNodeMap.put("UnaryOp", UnaryOp);
        pyNodeMap.put("Lambda", Lambda);
        pyNodeMap.put("IfExp", IfExp);
        pyNodeMap.put("Dict", Dict);
        pyNodeMap.put("Set", Set);
        pyNodeMap.put("ListComp", ListComp);
        pyNodeMap.put("SetComp", SetComp);
        pyNodeMap.put("DictComp", DictComp);
        pyNodeMap.put("GeneratorExp", GeneratorExp);
        pyNodeMap.put("Await", Await);
        pyNodeMap.put("Yield", Yield);
        pyNodeMap.put("YieldFrom", YieldFrom);
        pyNodeMap.put("Compare", Compare);
        pyNodeMap.put("Call", Call);
        pyNodeMap.put("Num", Num);
        pyNodeMap.put("Str", Str);
        pyNodeMap.put("Bytes", Bytes);
        pyNodeMap.put("NameConstant", NameConstant);
        pyNodeMap.put("Ellipsis", Ellipsis);
        pyNodeMap.put("Attribute", Attribute);
        pyNodeMap.put("Subscript", Subscript);
        pyNodeMap.put("Starred", Starred);
        pyNodeMap.put("Name", Name);
        pyNodeMap.put("List", List);
        pyNodeMap.put("Slice", Slice);
        pyNodeMap.put("ExtSlice", ExtSlice);
        pyNodeMap.put("Index", Index);
        pyNodeMap.put("ExceptHandler", ExceptHandler);
        pyNodeMap.put("TryFinally", TryFinally);
        pyNodeMap.put("TryExcept", TryExcept);
        pyNodeMap.put("FormattedValue", FormattedValue);
        pyNodeMap.put("Tuple", Tuple);
        pyNodeMap.put("TotalNodes", pyNodeMap.values().stream().mapToInt(i -> i).sum());
//        int numberOfNodes = pyNodeMap.values().stream().mapToInt(i -> i).sum();
//        log.fatal(" Total AST Nodes "+numberOfNodes);
        saveStats();

    }

    protected int printStats() {
        pyNodeMap.put("FunctionDef", FunctionDef);
        pyNodeMap.put("module", module);
        pyNodeMap.put("interactive", interactive);
        pyNodeMap.put("expression", expression);
        pyNodeMap.put("suite", suite);
        pyNodeMap.put("asyncFunctionDef", asyncFunctionDef);
        pyNodeMap.put("classDef", classDef);
        pyNodeMap.put("Return", Return);
        pyNodeMap.put("Delete", Delete);
        pyNodeMap.put("Assign", Assign);
        pyNodeMap.put("AugAssign", AugAssign);
        pyNodeMap.put("For", For);
        pyNodeMap.put("AsyncFor", AsyncFor);
        pyNodeMap.put("While", While);
        pyNodeMap.put("If", If);
        pyNodeMap.put("With", With);
        pyNodeMap.put("AsyncWith", AsyncWith);
        pyNodeMap.put("Raise", Raise);
        pyNodeMap.put("Try", Try);
        pyNodeMap.put("AssertStmt", AssertStmt);
        pyNodeMap.put("Import", Import);
        pyNodeMap.put("ImportFrom", ImportFrom);
        pyNodeMap.put("Global", Global);
        pyNodeMap.put("Nonloca", Nonloca);
        pyNodeMap.put("Expr", Expr);
        pyNodeMap.put("Pass", Pass);
        pyNodeMap.put("Break", Break);
        pyNodeMap.put("Continue", Continue);
        pyNodeMap.put("BoolOp", BoolOp);
        pyNodeMap.put("BinOp", BinOp);
        pyNodeMap.put("UnaryOp", UnaryOp);
        pyNodeMap.put("Lambda", Lambda);
        pyNodeMap.put("IfExp", IfExp);
        pyNodeMap.put("Dict", Dict);
        pyNodeMap.put("Set", Set);
        pyNodeMap.put("ListComp", ListComp);
        pyNodeMap.put("SetComp", SetComp);
        pyNodeMap.put("DictComp", DictComp);
        pyNodeMap.put("GeneratorExp", GeneratorExp);
        pyNodeMap.put("Await", Await);
        pyNodeMap.put("Yield", Yield);
        pyNodeMap.put("YieldFrom", YieldFrom);
        pyNodeMap.put("Compare", Compare);
        pyNodeMap.put("Call", Call);
        pyNodeMap.put("Num", Num);
        pyNodeMap.put("Str", Str);
        pyNodeMap.put("Bytes", Bytes);
        pyNodeMap.put("NameConstant", NameConstant);
        pyNodeMap.put("Ellipsis", Ellipsis);
        pyNodeMap.put("Attribute", Attribute);
        pyNodeMap.put("Subscript", Subscript);
        pyNodeMap.put("Starred", Starred);
        pyNodeMap.put("Name", Name);
        pyNodeMap.put("List", List);
        pyNodeMap.put("Slice", Slice);
        pyNodeMap.put("ExtSlice", ExtSlice);
        pyNodeMap.put("Index", Index);
        pyNodeMap.put("ExceptHandler", ExceptHandler);
        pyNodeMap.put("TryFinally", TryFinally);
        pyNodeMap.put("TryExcept", TryExcept);
        pyNodeMap.put("FormattedValue", FormattedValue);
        pyNodeMap.put("Tuple", Tuple);


//        pyNodeMap.entrySet().stream().sorted(Map.Entry.comparingByValue()).forEach(k->{
//        log.warn("Number of "+k.getKey()+" : "+k.getValue());
//        });
//
//        int numberOfNodes = pyNodeMap.values().stream().mapToInt(i -> i).sum();
////        int totalNodes =    FunctionDef+module+interactive+expression+suite+asyncFunctionDef+classDef+Return+Delete+Return
////        +Assign+AugAssign+For+AsyncFor+While+If+With+AsyncWith+Raise +Try+AssertStmt+Import+ImportFrom+Global+Nonloca+Expr
////        +Pass+Break+Continue+BoolOp+BinOp+UnaryOp+Lambda+IfExp+Dict+Set+ListComp+SetComp+DictComp+GeneratorExp+ Await+Yield+ YieldFrom+ Compare
////        +  Call + Num+ Str+ Bytes+  NameConstant+ Ellipsis+Attribute+ Subscript+ Starred+ Name+ List+Slice+ ExtSlice+
////                Index + ExceptHandler + TryFinally+ TryExcept+ FormattedValue+ Tuple;
//
//        log.warn("Total Number of  Nodes  : "+ numberOfNodes );
//
//        HashMap<String,Integer> oldPyNodes = null;
//        final File path =  new File("./node_map.bin");
//        if(path.exists()){
//            try {
//                FileInputStream is = new FileInputStream("./node_map.bin");
//                ObjectInputStream ois = new ObjectInputStream(is);
//                oldPyNodes = (HashMap<String,Integer> ) ois.readObject();
//                ois.close();
//                is.close();
//            } catch (ClassNotFoundException | IOException e) {
//                e.printStackTrace();
//            }
//        }
//        File outFile = new File("./node_map.bin");
//        try (FileOutputStream fos = new FileOutputStream(outFile);
//             BufferedOutputStream bos = new BufferedOutputStream(fos);
//             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
//            if (oldPyNodes!=null) {
//                HashMap<String, Integer> finalOldPyNodes = oldPyNodes;
//                Map<String, Integer> toserialize =
//                        pyNodeMap.entrySet().stream()
//                                .collect(Collectors.toMap(
//                                        Map.Entry::getKey,
//                                        e -> e.getValue() + finalOldPyNodes.get(e.getKey())
//                                ));
//                oos.writeObject(toserialize);
//            }
//            else {
//                oos.writeObject(pyNodeMap);
//            }
//            oos.flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        resetValues();
        return numberOfNodes;

    }


    public class PyNodeCounter extends Visitor {
        @Override
        public Object visitModule(Module node) throws Exception {
            module += 1;
            return super.visitModule(node);
        }

        @Override
        public Object visitInteractive(Interactive node) throws Exception {
            interactive += 1;
            return super.visitInteractive(node);
        }

        @Override
        public Object visitExpression(Expression node) throws Exception {
            expression += 1;
            return super.visitExpression(node);
        }

        @Override
        public Object visitSuite(Suite node) throws Exception {
            suite += 1;
            return super.visitSuite(node);
        }

        @Override
        public Object visitFunctionDef(FunctionDef node) throws Exception {
            FunctionDef += 1;
            return super.visitFunctionDef(node);
        }

        @Override
        public Object visitAsyncFunctionDef(AsyncFunctionDef node) throws Exception {
            asyncFunctionDef += 1;
            return super.visitAsyncFunctionDef(node);
        }

        @Override
        public Object visitClassDef(ClassDef node) throws Exception {
            classDef += 1;
            return super.visitClassDef(node);
        }

        @Override
        public Object visitReturn(Return node) throws Exception {
            Return += 1;
            return super.visitReturn(node);
        }

        @Override
        public Object visitDelete(Delete node) throws Exception {
            Delete += 1;
            return super.visitDelete(node);
        }

        @Override
        public Object visitAssign(Assign node) throws Exception {
            Assign += 1;
            return super.visitAssign(node);
        }

        @Override
        public Object visitAugAssign(AugAssign node) throws Exception {
            AugAssign += 1;
            return super.visitAugAssign(node);
        }

        @Override
        public Object visitFor(For node) throws Exception {
            For += 1;
            return super.visitFor(node);
        }

        @Override
        public Object visitAsyncFor(AsyncFor node) throws Exception {
            AsyncFor += 1;
            return super.visitAsyncFor(node);
        }

        @Override
        public Object visitWhile(While node) throws Exception {
            While += 1;
            return super.visitWhile(node);
        }

        @Override
        public Object visitIf(If node) throws Exception {
            If += 1;
            return super.visitIf(node);
        }

        @Override
        public Object visitWith(With node) throws Exception {
            With += 1;
            return super.visitWith(node);
        }

        @Override
        public Object visitAsyncWith(AsyncWith node) throws Exception {
            AsyncWith += 1;
            return super.visitAsyncWith(node);
        }

        @Override
        public Object visitRaise(Raise node) throws Exception {
            Raise += 1;
            return super.visitRaise(node);
        }

        @Override
        public Object visitTry(Try node) throws Exception {
            Try += 1;
            return super.visitTry(node);
        }

        @Override
        public Object visitAssert(org.jpp.astnodes.ast.Assert node) throws Exception {
            AssertStmt += 1;
            return super.visitAssert(node);
        }

        @Override
        public Object visitImport(Import node) throws Exception {
            Import += 1;
            return super.visitImport(node);
        }

        @Override
        public Object visitImportFrom(ImportFrom node) throws Exception {
            ImportFrom += 1;
            return super.visitImportFrom(node);
        }

        @Override
        public Object visitGlobal(Global node) throws Exception {
            Global += 1;
            return super.visitGlobal(node);
        }

        @Override
        public Object visitNonlocal(Nonlocal node) throws Exception {
            Nonloca += 1;
            return super.visitNonlocal(node);
        }

        @Override
        public Object visitExpr(Expr node) throws Exception {
            Expr += 1;
            return super.visitExpr(node);
        }

        @Override
        public Object visitPass(Pass node) throws Exception {
            Pass += 1;
            return super.visitPass(node);
        }

        @Override
        public Object visitBreak(Break node) throws Exception {
            Break += 1;
            return super.visitBreak(node);
        }

        @Override
        public Object visitContinue(Continue node) throws Exception {
            Continue += 1;
            return super.visitContinue(node);
        }

        @Override
        public Object visitBoolOp(BoolOp node) throws Exception {
            BoolOp += 1;
            return super.visitBoolOp(node);
        }

        @Override
        public Object visitBinOp(BinOp node) throws Exception {
            BinOp += 1;
            return super.visitBinOp(node);
        }

        @Override
        public Object visitUnaryOp(UnaryOp node) throws Exception {
            UnaryOp += 1;
            return super.visitUnaryOp(node);
        }

        @Override
        public Object visitLambda(Lambda node) throws Exception {
            Lambda += 1;
            return super.visitLambda(node);
        }

        @Override
        public Object visitIfExp(IfExp node) throws Exception {
            IfExp += 1;
            return super.visitIfExp(node);
        }

        @Override
        public Object visitDict(Dict node) throws Exception {
            Dict += 1;
            return super.visitDict(node);
        }

        @Override
        public Object visitSet(Set node) throws Exception {
            Set += 1;
            return super.visitSet(node);
        }

        @Override
        public Object visitListComp(ListComp node) throws Exception {
            ListComp += 1;
            return super.visitListComp(node);
        }

        @Override
        public Object visitSetComp(SetComp node) throws Exception {
            SetComp += 1;
            return super.visitSetComp(node);
        }

        @Override
        public Object visitDictComp(DictComp node) throws Exception {
            DictComp += 1;
            return super.visitDictComp(node);
        }

        @Override
        public Object visitGeneratorExp(GeneratorExp node) throws Exception {
            GeneratorExp += 1;
            return super.visitGeneratorExp(node);
        }

        @Override
        public Object visitAwait(Await node) throws Exception {
            Await += 1;
            return super.visitAwait(node);
        }

        @Override
        public Object visitYield(Yield node) throws Exception {
            Yield += 1;
            return super.visitYield(node);
        }

        @Override
        public Object visitYieldFrom(YieldFrom node) throws Exception {
            YieldFrom += 1;
            return super.visitYieldFrom(node);
        }

        @Override
        public Object visitCompare(Compare node) throws Exception {
            Compare += 1;
            return super.visitCompare(node);
        }

        @Override
        public Object visitCall(Call node) throws Exception {
            Call += 1;
            return super.visitCall(node);
        }

        @Override
        public Object visitNum(Num node) throws Exception {
            Num += 1;
            return super.visitNum(node);
        }

        @Override
        public Object visitStr(Str node) throws Exception {
            Str += 1;
            return super.visitStr(node);
        }

        @Override
        public Object visitBytes(Bytes node) throws Exception {
            Bytes += 1;
            return super.visitBytes(node);
        }

        @Override
        public Object visitNameConstant(NameConstant node) throws Exception {
            NameConstant += 1;
            return super.visitNameConstant(node);
        }

        @Override
        public Object visitEllipsis(Ellipsis node) throws Exception {
            Ellipsis += 1;
            return super.visitEllipsis(node);
        }

        @Override
        public Object visitAttribute(Attribute node) throws Exception {
            Attribute += 1;
            return super.visitAttribute(node);
        }

        @Override
        public Object visitSubscript(Subscript node) throws Exception {
            Subscript += 1;
            return super.visitSubscript(node);
        }

        @Override
        public Object visitStarred(Starred node) throws Exception {
            Starred += 1;
            return super.visitStarred(node);
        }

        @Override
        public Object visitName(Name node) throws Exception {
            Name += 1;
            return super.visitName(node);
        }

        @Override
        public Object visitList(List node) throws Exception {
            List += 1;
            return super.visitList(node);
        }

        @Override
        public Object visitTuple(Tuple node) throws Exception {
            Tuple += 1;
            return super.visitTuple(node);
        }

        @Override
        public Object visitSlice(Slice node) throws Exception {
            Slice += 1;
            return super.visitSlice(node);
        }

        @Override
        public Object visitExtSlice(ExtSlice node) throws Exception {
            ExtSlice += 1;
            return super.visitExtSlice(node);
        }

        @Override
        public Object visitIndex(Index node) throws Exception {
            Index += 1;
            return super.visitIndex(node);
        }

        @Override
        public Object visitExceptHandler(ExceptHandler node) throws Exception {
            ExceptHandler += 1;
            return super.visitExceptHandler(node);
        }

        @Override
        public Object visitTryFinally(TryFinally node) throws Exception {
            TryFinally += 1;
            return super.visitTryFinally(node);
        }

        @Override
        public Object visitTryExcept(TryExcept node) throws Exception {
            TryExcept += 1;
            return super.visitTryExcept(node);
        }

        @Override
        public Object visitFormattedValue(FormattedValue node) throws Exception {
            FormattedValue += 1;
            return super.visitFormattedValue(node);
        }

    }


}
