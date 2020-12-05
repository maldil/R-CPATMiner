package python3;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AssertStatement;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.internal.compiler.parser.Parser;
import org.eclipse.jdt.internal.compiler.parser.Scanner;
import org.eclipse.jdt.internal.compiler.problem.ProblemReporter;
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
import org.testng.annotations.Test;
import python3.typeinference.PyASTVisitor;
import utils.JavaASTUtil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class TestRealPyASTNodes {
    private static org.apache.log4j.Logger log = Logger.getLogger(TestRealPyASTNodes.class);
    private int numberOfNodes=0;

    @Test
    public void runAllTest(){
        file1();
        file2();
        file3();
        file4();
        file5();
        file6();
        file8();
        file9();
        file10();
        file11();
//        file12();
        file13();
        file14();
        file15();
        file16();
        file17();
        file18();
        file19();
        file20();
        file21();
        file22();
        file23();
        file24();
        file26();
        file27();
        file28();
        file29();
        file30();
        file31();
        file32();
        file33();
        file34();
        file35();
     //   file36();
        file37();
        file38();
        file39();
        file40();
        file41();
        file42();
        file43();
        file44();
        file45();
        file46();
        file47();
        file48();
        file49();
        file50();
        file51();
        file52();
       // file53();  tuple in tuples
        file54();
        file55();
        file56();
        file57();
        file58();
        file59();
        file60();
        file61();
        file62();
        file63();
        file64();
        file65();
        file66();
        file67();
        file68();
        file69();
        file70();
        file71();
        file72();
        file73();
        file74();
        file75();
        file76();
        file77();
        file78();
        file79();
        file80();
        file81();
        file82();
        file83();
        file84();
        file85();
        file86();
        file87();
        file88();
        file89();
        file90();
        file91();
        file92();
        file93();
        file94();
        file95();
        file96();
        file97();
        file98();
        file99();
        file100();
        file101();
        file102();
        file103();
        file104();
        file105();
        file106();
        file107();
        file108();
        file109();
        file110();
        file111();
        file112();
        file114();
        file115();
        file116();
        file117();
        file118();
        file119();
        file120();
        file121();
        file122();
        file123();
        file124();
        file125();
  //      file126();
        file127();
        file128();
        file129();
        file130();
        file131();
        file132();
        file133();
        file134();
        file135();
        file136();
        file137();
        file138();
        file139();
        file140();
        file142();
        file143();
        file144();
        file145();
        file146();
        file147();
        file149();
        file150();
        file151();
        file152();
        file153();
        file154();
        file155();
        file156();
        file157();
        file158();
        file159();
        file160();
        file161();
        file162();
        file163();
        file164();
        file165();
        file166();
        file167();
        file168();
        file170();
        file171();
        file173();
        file174();
        file175();
        file176();
        file177();
        file178();
        file179();
        file180();
        file181();
        file182();
        file183();
        file184();
        file186();
        file187();
        file188();
        file189();
        file190();
        file191();
        file192();
        file193();
        file195();
        file196();
        file197();
        file198();
        file199();
        file200();
        file201();
        file202();
        file203();
        file204();
        file205();
        file206();
        file207();
        file208();
        file209();
        file210();
        file211();
        file212();
        file213();
        file214();
        file215();
        file216();
        file218();
        file219();
     //   file220(); List comp with conditional
        file221();
        file222();
        file223();
     //   file224(); List comp with conditional
        file225();
        file226();
        file228();
        file229();
        file230();
        file231();
        file232();
        file233();
        file235();
        file236();
        file237();
        file238();
        file239();
    //    file240();  //conditional list comp
        file241();
        file242();
        file243();
        file244();
        file245();
   //     file246(); //conditional list comp
   //     file247(); //conditional list comp
   //     file248(); //conditional list comp
        file249();
        file250();
        file251();
        file252();
        file253();
        file254();
        file255();
        file256();
        file257();
   //     file258(); //conditional list comp
        file259();
        file260();
   //     file261();  //conditional list comp
        file262();
        file263();
        file264();
        file265();
        file267();
        file268();
        file269();
        file270();
        file271();
        file272();
        file273();
        file274();
        file275();
        file276();
        file277();
        file279();
        file280();
        file281();
        file282();
        file283();
        file284();
        file285();
        file286();
        file287();
        file288();
        file289();
        file290();
        file291();
        file292();
        file293();
        file294();
        file295();
        file296();
        file297();
        file298();
        file299();
        file300();
        file301();
        file302();
        file303();
        file304();
        file305();
        file306();
        file307();
        file308();
        file309();
        file310();
        file311();
        file312();
        file314();
        file315();
        file316();
    //    file317(); //conditional list comp
        file318();
        file319();
        file320();
        file321();
        file322();
        file323();
        file324();
        file325();
        file326();
        file327();
        file328();
        file329();
    //    file330(); //conditional list comp
    //    file331();  //conditional list comp
        file332();
        file333();
        file334();
        file335();
        file336();
        file337();
        file338();
        file339();
        file340();
        file341();
        file342();
        file343();
        file344();
        file345();
        file346();
        file347();
        file348();
        file349();
        file350();
        file351();
        file352();
        file353();
        file354();
        file355();
        file356();
        file357();
        file358();
        file359();
        file360();
        file361();
        file362();
        file363();
        file364();
        file365();
        file366();
        file367();
        file368();
        //    file369();  List Comp is with Conditionals
        file370();
        file371();
      //  file372(); lambdas in arguments
        file373();
        file374();
        file375();
        file376();
        file377();
        file378();
        file379();
        file380();
        file381();
        file382();
        file383();
        file384();
        file385();
        file386();
        file387();
        file388();
        file389();
        file390();
        file391();
        file392();
        file393();
 //       file394(); yield from
        file395();
        file396();
        file397();
        file398();
        file399();
        file400();
        file401();
        file403();
        file404();
        file405();
        file406();
        file407();
        file408();
        file409();
        file410();
        file411();
        file412();
        file413();
        file414();
        file415();
        file416();
        file417();
        file418();
        file419();
        file420();
        file421();
        file422();
        file423();
        file424();
        file425();
        file426();
        file427();
        file428();
        file429();
        file430();
        file431();
        file432();
        file433();
        file434();
        file435();
        file436();
        file437();
        file438();
        file439();
        file440();
        file441();
        file442();
        file443();
        file444();
     //   file445();  Fix this
        file446();
        file447();
        file448();
        file449();
        file450();
        file451();
        file452();
        file453();
        file454();
        file455();
 //       file456();  list comp with conditional exp
        file457();
        file458();
        file459();
        file460();
        file461();
        file462();
        file463();
        file464();
        file465();
   //     file466();  LMBDA in argument
        file467();
        file468();
        file469();
        file470();
        file471();
        file472();
        file473();
        file474();
        file475();
        file476();
        file477();
        file478();
        file479();
        file480();
 //       file481();  Lambda in argmuments
 //       file482();  List comp with generators
        file483();
        file484();
        file485();
        file486();
        file487();
        file488();
        file489();
        file490();
        file491();
        file492();
        file493();
        file494();
        file495();
        file496();
        file497();
        file498();
        file499();
        file500();
        file501();
  //      file502();  List comp with conditionals
        file503();
        file504();
        file505();
        file506();
        file507();
        file508();
        file509();
        file510();
        file511();
        file512();
        printStats();
    }

    @Test
    public void file1(){
        String content = readFile("common.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file2(){
        String content = readFile("commonTest.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file3(){
        String content = readFile("exception.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file4(){
        String content = readFile("explorer.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file5(){
        String content = readFile("explorerTest.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file6(){
        String content = readFile("input.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file7(){
        String content = readFile("inputTest.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file8(){
        String content = readFile("kinetics.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file9(){
        String content = readFile("kineticsTest.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file10(){
        String content = readFile("main.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file11(){
        String content = readFile("mainTest.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test  // This is not parsable due to a error in python to java
    public void file12(){
        String content = readFile("modelchem.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file13(){
        String content = readFile("modelchemTest.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file14(){
        String content = readFile("output.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file15(){
        String content = readFile("outputTest.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file16(){
        String content = readFile("pdep.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file17(){
        String content = readFile("pdepTest.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file18(){
        String content = readFile("sensitivity.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file19(){
        String content = readFile("statmech.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file20(){
        String content = readFile("statmechTest.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file21(){
        String content = readFile("thermo.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file22(){
        String content = readFile("thermoTest.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file23(){
        String content = readFile("sklearn/_affinity_propagation.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file24(){
        String content = readFile("sklearn/_agglomerative.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file25(){
        String content = readFile("sklearn/_bicluster.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file26(){
        String content = readFile("sklearn/_birch.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file27(){
        String content = readFile("sklearn/_reingold_tilford.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file28(){
        String content = readFile("sklearn/_dbscan.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file29(){
        String content = readFile("sklearn/_feature_agglomeration.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file30(){
        String content = readFile("sklearn/_kmeans.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file31(){
        String content = readFile("sklearn/_mean_shift.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file32(){
        String content = readFile("sklearn/_optics.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file33(){
        String content = readFile("sklearn/_spectral.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file34(){
        String content = readFile("sklearn/glm_distribution.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file35(){
        String content = readFile("sklearn/openmp_helpers.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file36(){
        String content = readFile("sklearn/pre_build_helpers.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file37(){
        String content = readFile("sklearn/test_glm_distribution.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file38(){
        String content = readFile("sklearn/benchmark/cluster.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file39(){
        String content = readFile("sklearn/benchmark/cluster.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file40(){
        String content = readFile("sklearn/benchmark/common.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file41(){
        String content = readFile("sklearn/benchmark/datasets.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file42(){
        String content = readFile("sklearn/benchmark/decomposition.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file43(){
        String content = readFile("sklearn/benchmark/ensemble.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file44(){
        String content = readFile("sklearn/benchmark/linear_model.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file45(){
        String content = readFile("sklearn/benchmark/manifold.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file46(){
        String content = readFile("sklearn/benchmark/metrics.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file47(){
        String content = readFile("sklearn/benchmark/model_selection.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file48(){
        String content = readFile("sklearn/benchmark/neighbors.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file49(){
        String content = readFile("sklearn/benchmark/svm.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file50(){
        String content = readFile("sklearn/benchmark/utils.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file51(){
        String content = readFile("sklearn/benchmarks/bench_20newsgroups.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file52(){
        String content = readFile("sklearn/benchmarks/bench_covertype.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file53(){
        String content = readFile("sklearn/benchmarks/bench_feature_expansions.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file54(){
        String content = readFile("sklearn/benchmarks/bench_glm.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file55(){
        String content = readFile("sklearn/benchmarks/bench_glmnet.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file56(){
        String content = readFile("sklearn/benchmarks/bench_hist_gradient_boosting.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file57(){
        String content = readFile("sklearn/benchmarks/bench_hist_gradient_boosting_higgsboson.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file58(){
        String content = readFile("sklearn/benchmarks/bench_hist_gradient_boosting_threading.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file59(){
        String content = readFile("sklearn/benchmarks/bench_isolation_forest.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file60(){
        String content = readFile("sklearn/benchmarks/bench_isotonic.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file61(){
        String content = readFile("sklearn/benchmarks/bench_lasso.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }


    @Test
    public void file62(){
        String content = readFile("sklearn/benchmarks/bench_lof.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file63(){
        String content = readFile("sklearn/benchmarks/bench_mnist.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file64(){
        String content = readFile("sklearn/benchmarks/bench_multilabel_metrics.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file65(){
        String content = readFile("sklearn/benchmarks/bench_plot_fastkmeans.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file66(){
        String content = readFile("sklearn/benchmarks/bench_plot_hierarchical.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file67(){
        String content = readFile("sklearn/benchmarks/bench_plot_lasso_path.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file68(){
        String content = readFile("sklearn/benchmarks/bench_plot_neighbors.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file69(){
        String content = readFile("sklearn/benchmarks/bench_plot_nmf.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file70(){
        String content = readFile("sklearn/benchmarks/bench_plot_omp_lars.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file71(){
        String content = readFile("sklearn/benchmarks/bench_plot_parallel_pairwise.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file72(){
        String content = readFile("sklearn/benchmarks/bench_plot_polynomial_kernel_approximation.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file73(){
        String content = readFile("sklearn/benchmarks/bench_plot_polynomial_kernel_approximation.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file74(){
        String content = readFile("sklearn/benchmarks/bench_plot_randomized_svd.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file75(){
        String content = readFile("sklearn/benchmarks/bench_plot_svd.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file76(){
        String content = readFile("sklearn/benchmarks/bench_plot_ward.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file77(){
        String content = readFile("sklearn/benchmarks/bench_random_projections.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file78(){
        String content = readFile("sklearn/benchmarks/bench_rcv1_logreg_convergence.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file79(){
        String content = readFile("sklearn/benchmarks/bench_saga.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file80(){
        String content = readFile("sklearn/benchmarks/bench_sample_without_replacement.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file81(){
        String content = readFile("sklearn/benchmarks/bench_sgd_regression.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file82(){
        String content = readFile("sklearn/benchmarks/bench_sparsify.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file83(){
        String content = readFile("sklearn/benchmarks/bench_text_vectorizers.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file84(){
        String content = readFile("sklearn/benchmarks/bench_tree.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file85(){
        String content = readFile("sklearn/benchmarks/bench_tsne_mnist.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file86(){
        String content = readFile("sklearn/svm/tests/test_bounds.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file87(){
        String content = readFile("sklearn/svm/tests/test_sparse.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file88(){
        String content = readFile("sklearn/svm/tests/test_svm.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file89(){
        String content = readFile("sklearn/svm/_base.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file90(){
        String content = readFile("sklearn/svm/_bounds.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file91(){
        String content = readFile("sklearn/svm/_classes.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file92(){
        String content = readFile("sklearn/svm/setup.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file93(){
        String content = readFile("sklearn/utils/optimize.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file94(){
        String content = readFile("sklearn/utils/fixes.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file95(){
        String content = readFile("sklearn/utils/deprecation.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file96(){
            //error due to index error in yield statment
        String content = readFile("sklearn/utils/estimator_checks.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file97(){
        String content = readFile("sklearn/utils/multiclass.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file98(){
        String content = readFile("sklearn/utils/graph.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file99(){
        String content = readFile("sklearn/utils/_joblib.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file100(){
        String content = readFile("sklearn/utils/tests/test_deprecation.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file101(){
        String content = readFile("sklearn/utils/tests/test_pprint.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file102(){
        String content = readFile("sklearn/utils/tests/test_utils.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file103(){
        String content = readFile("sklearn/utils/tests/test_metaestimators.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file104(){
        String content = readFile("sklearn/utils/tests/test_seq_dataset.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file105(){
        String content = readFile("sklearn/utils/tests/conftest.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file106(){
        String content = readFile("sklearn/utils/tests/test_stats.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file107(){
        String content = readFile("sklearn/utils/tests/test_validation.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file108(){
        String content = readFile("sklearn/utils/tests/test_optimize.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file109(){
        String content = readFile("sklearn/utils/tests/test_show_versions.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file110(){
        String content = readFile("sklearn/utils/tests/test_shortest_path.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file111(){
        String content = readFile("sklearn/utils/tests/test_cython_blas.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file112(){
        String content = readFile("sklearn/utils/tests/test_fast_dict.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
//    @Test
//    public void file113(){
//        String content = readFile("sklearn/utils/tests/__init__.py");
//        CompilationUnit converted = convert(content);
//        Assert.assertEquals(converted.getProblems().length,0);
//    }
    @Test
    public void file114(){
        String content = readFile("sklearn/utils/tests/test_class_weight.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file115(){
        String content = readFile("sklearn/utils/tests/test_estimator_checks.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file116(){
        String content = readFile("sklearn/utils/tests/test_multiclass.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file117(){
        String content = readFile("sklearn/utils/tests/test_parallel.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file118(){
        String content = readFile("sklearn/utils/tests/test_fixes.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file119(){
        String content = readFile("sklearn/utils/tests/test_sparsefuncs.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file120(){
        String content = readFile("sklearn/utils/tests/test_encode.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file121(){
        String content = readFile("sklearn/utils/tests/test_mocking.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file122(){
        String content = readFile("sklearn/utils/tests/test_murmurhash.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file123(){  //Error when parsing Python to Java
        String content = readFile("sklearn/utils/tests/test_estimator_html_repr.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file124(){
        String content = readFile("sklearn/utils/tests/test_extmath.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file125(){
        String content = readFile("sklearn/utils/tests/test_random.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file126(){
        String content = readFile("sklearn/utils/tests/test_testing.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file127(){
        String content = readFile("sklearn/utils/_pprint.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file128(){
        String content = readFile("sklearn/utils/random.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file129(){
        String content = readFile("sklearn/utils/_mocking.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file130(){
        String content = readFile("sklearn/utils/setup.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file131(){
        String content = readFile("sklearn/utils/stats.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file132(){
        String content = readFile("sklearn/utils/class_weight.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file133(){
        String content = readFile("sklearn/utils/_estimator_html_repr.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file134(){
        String content = readFile("sklearn/utils/_show_versions.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file135(){  //steps: List[Any]
        String content = readFile("sklearn/utils/metaestimators.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file136(){
        String content = readFile("sklearn/utils/_testing.py");
         CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file137(){
        String content = readFile("sklearn/utils/extmath.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file138(){
        String content = readFile("sklearn/utils/_encode.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file139(){
        String content = readFile("sklearn/utils/sparsefuncs.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file140(){
        String content = readFile("sklearn/utils/_mask.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file141(){
        String content = readFile("sklearn/utils/validation.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file142(){
        String content = readFile("sklearn/tree/tests/test_tree.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file143(){
        String content = readFile("sklearn/tree/tests/test_reingold_tilford.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file144(){
        String content = readFile("sklearn/tree/tests/test_export.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file145(){
        String content = readFile("sklearn/tree/_export.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file146(){
        String content = readFile("sklearn/tree/setup.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file147(){
        String content = readFile("sklearn/tree/_classes.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file149(){
        String content = readFile("sklearn/tests/test_metaestimators.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file150(){
        String content = readFile("sklearn/tests/test_common.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file151(){
        String content = readFile("sklearn/tests/test_multioutput.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file152(){
        String content = readFile("sklearn/tests/test_isotonic.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file153(){
        String content = readFile("sklearn/tests/test_docstring_parameters.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file154(){
        String content = readFile("sklearn/tests/test_dummy.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file155(){
        String content = readFile("sklearn/tests/test_check_build.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file156(){
        String content = readFile("sklearn/tests/test_discriminant_analysis.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file157(){
        String content = readFile("sklearn/tests/test_build.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file158(){
        String content = readFile("sklearn/tests/test_multiclass.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file159(){
        String content = readFile("sklearn/tests/test_min_dependencies_readme.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file160(){
        String content = readFile("sklearn/tests/test_config.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file161(){
        String content = readFile("sklearn/tests/test_kernel_ridge.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file162(){
        String content = readFile("sklearn/tests/test_calibration.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file163(){
        String content = readFile("sklearn/tests/test_naive_bayes.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file164(){
        String content = readFile("sklearn/tests/test_base.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file165(){
        String content = readFile("sklearn/tests/test_init.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file166(){
        String content = readFile("sklearn/tests/test_pipeline.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file167(){
        String content = readFile("sklearn/tests/test_random_projection.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file168(){
        String content = readFile("sklearn/tests/test_kernel_approximation.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file170(){
        String content = readFile("sklearn/semi_supervised/tests/test_label_propagation.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file171(){
        String content = readFile("sklearn/semi_supervised/_label_propagation.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file173(){
        String content = readFile("sklearn/preprocessing/_data.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file174(){
        String content = readFile("sklearn/preprocessing/_encoders.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file175(){
        String content = readFile("sklearn/preprocessing/tests/test_common.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file176(){
        String content = readFile("sklearn/preprocessing/tests/test_label.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file177(){
        String content = readFile("sklearn/preprocessing/tests/test_encoders.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file178(){
        String content = readFile("sklearn/preprocessing/tests/test_function_transformer.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file179(){
        String content = readFile("sklearn/preprocessing/tests/test_discretization.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file180(){
        String content = readFile("sklearn/preprocessing/tests/test_data.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file181(){
        String content = readFile("sklearn/preprocessing/_discretization.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file182(){
        String content = readFile("sklearn/preprocessing/setup.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file183(){
        String content = readFile("sklearn/preprocessing/_label.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file184(){
        String content = readFile("sklearn/preprocessing/_function_transformer.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file186(){
        String content = readFile("sklearn/neural_network/_base.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file187(){
        String content = readFile("sklearn/neural_network/_rbm.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file188(){
        String content = readFile("sklearn/neural_network/tests/test_mlp.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file189(){
        String content = readFile("sklearn/neural_network/tests/test_rbm.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file190(){
        String content = readFile("sklearn/neural_network/tests/test_base.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file191(){
        String content = readFile("sklearn/neural_network/tests/test_stochastic_optimizers.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file192(){
        String content = readFile("sklearn/neural_network/_stochastic_optimizers.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file193(){
        String content = readFile("sklearn/neural_network/_multilayer_perceptron.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file195(){
        String content = readFile("sklearn/neighbors/_classification.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file196(){
        String content = readFile("sklearn/neighbors/_nca.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file197(){
        String content = readFile("sklearn/neighbors/_base.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file198(){
        String content = readFile("sklearn/neighbors/_graph.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file199(){
        String content = readFile("sklearn/neighbors/_regression.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file200(){
        String content = readFile("sklearn/neighbors/tests/test_nearest_centroid.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file201(){
        String content = readFile("sklearn/neighbors/tests/test_kde.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file202(){
        String content = readFile("sklearn/neighbors/tests/test_neighbors_pipeline.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file203(){
        String content = readFile("sklearn/neighbors/tests/test_dist_metrics.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file204(){
        String content = readFile("sklearn/neighbors/tests/test_lof.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file205(){
        String content = readFile("sklearn/neighbors/tests/test_kd_tree.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file206(){
        String content = readFile("sklearn/neighbors/tests/test_neighbors_tree.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file207(){
        String content = readFile("sklearn/neighbors/tests/test_nca.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file208(){
        String content = readFile("sklearn/neighbors/tests/test_graph.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file209(){
        String content = readFile("sklearn/neighbors/tests/test_neighbors.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file210(){
        String content = readFile("sklearn/neighbors/tests/test_quad_tree.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file211(){
        String content = readFile("sklearn/neighbors/tests/test_ball_tree.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file212(){
        String content = readFile("sklearn/neighbors/_nearest_centroid.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file213(){
        String content = readFile("sklearn/neighbors/_unsupervised.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file214(){
        String content = readFile("sklearn/neighbors/_kde.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file215(){
        String content = readFile("sklearn/neighbors/setup.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file216(){
        String content = readFile("sklearn/neighbors/_lof.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file218(){
        String content = readFile("sklearn/model_selection/_search.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file219(){
        String content = readFile("sklearn/model_selection/tests/test_split.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file220(){
        String content = readFile("sklearn/model_selection/tests/test_validation.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file221(){
        String content = readFile("sklearn/model_selection/tests/test_search.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file222(){
        String content = readFile("sklearn/model_selection/tests/common.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file223(){
        String content = readFile("sklearn/model_selection/tests/test_successive_halving.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file224(){
//        {
//            key: np.asarray([score[key] for score in scores])
//            if isinstance(scores[0][key], numbers.Number)
//        else [score[key] for score in scores]
//            for key in scores[0]
//        }
        String content = readFile("sklearn/model_selection/_validation.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file225(){
        String content = readFile("sklearn/model_selection/_search_successive_halving.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file226(){
        String content = readFile("sklearn/model_selection/_split.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file228(){
        String content = readFile("sklearn/mixture/_base.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file229(){
        String content = readFile("sklearn/mixture/tests/test_mixture.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file230(){
        String content = readFile("sklearn/mixture/tests/test_bayesian_mixture.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file231(){
        String content = readFile("sklearn/mixture/tests/test_gaussian_mixture.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file232(){
        String content = readFile("sklearn/mixture/_bayesian_mixture.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file233(){
        String content = readFile("sklearn/mixture/_gaussian_mixture.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file235(){
        String content = readFile("sklearn/metrics/_scorer.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file236(){
        String content = readFile("sklearn/metrics/_classification.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file237(){
        String content = readFile("sklearn/metrics/_base.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file238(){
        String content = readFile("sklearn/metrics/cluster/_bicluster.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file239(){
        String content = readFile("sklearn/metrics/cluster/tests/test_supervised.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file240(){
        String content = readFile("sklearn/metrics/cluster/tests/test_common.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file241(){
        String content = readFile("sklearn/metrics/cluster/tests/test_bicluster.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file242(){
        String content = readFile("sklearn/metrics/cluster/tests/test_unsupervised.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file243(){
        String content = readFile("sklearn/metrics/cluster/_unsupervised.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file244(){
        String content = readFile("sklearn/metrics/cluster/setup.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file245(){
        String content = readFile("sklearn/metrics/cluster/_supervised.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file246(){
        String content = readFile("sklearn/metrics/_plot/det_curve.py");

        CompilationUnit converted = convert(content);

        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file247(){
        String content = readFile("sklearn/metrics/_plot/tests/test_plot_precision_recall.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file248(){
        String content = readFile("sklearn/metrics/_plot/tests/test_plot_roc_curve.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file249(){
        String content = readFile("sklearn/metrics/_plot/tests/test_plot_curve_common.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file250(){
        String content = readFile("sklearn/metrics/_plot/tests/test_plot_confusion_matrix.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file251(){
        String content = readFile("sklearn/metrics/_plot/tests/test_plot_det_curve.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file252(){
        String content = readFile("sklearn/metrics/_plot/precision_recall_curve.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file253(){
        String content = readFile("sklearn/metrics/_plot/roc_curve.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file254(){
        String content = readFile("sklearn/metrics/_plot/confusion_matrix.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file255(){
        String content = readFile("sklearn/metrics/_plot/base.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file256(){
        String content = readFile("sklearn/metrics/_regression.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file257(){
        String content = readFile("sklearn/metrics/tests/test_common.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file258(){
        String content = readFile("sklearn/metrics/tests/test_score_objects.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file259(){
        String content = readFile("sklearn/metrics/tests/test_ranking.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file260(){
        String content = readFile("sklearn/metrics/tests/test_pairwise.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file261(){
        String content = readFile("sklearn/metrics/tests/test_classification.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file262(){
        String content = readFile("sklearn/metrics/tests/test_regression.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file263(){
        String content = readFile("sklearn/metrics/setup.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file264(){
        String content = readFile("sklearn/metrics/pairwise.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file265(){
        String content = readFile("sklearn/metrics/_ranking.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file267(){
        String content = readFile("sklearn/manifold/_locally_linear.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file268(){
        String content = readFile("sklearn/manifold/_t_sne.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file269(){
        String content = readFile("sklearn/manifold/tests/test_isomap.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file270(){
        String content = readFile("sklearn/manifold/tests/test_mds.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file271(){
        String content = readFile("sklearn/manifold/tests/test_t_sne.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file272(){
        String content = readFile("sklearn/manifold/tests/test_spectral_embedding.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file273(){
        String content = readFile("sklearn/manifold/tests/test_locally_linear.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file274(){
        String content = readFile("sklearn/manifold/_isomap.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file275(){
        String content = readFile("sklearn/manifold/setup.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file276(){
        String content = readFile("sklearn/manifold/_mds.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file277(){
        String content = readFile("sklearn/manifold/_spectral_embedding.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file279(){
        String content = readFile("sklearn/linear_model/_base.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file280(){
        String content = readFile("sklearn/linear_model/_least_angle.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file281(){
        String content = readFile("sklearn/linear_model/_glm/link.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file282(){
        String content = readFile("sklearn/linear_model/_glm/glm.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file283(){
        String content = readFile("sklearn/linear_model/_glm/tests/test_link.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file284(){
        String content = readFile("sklearn/linear_model/_glm/tests/test_glm.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file285(){
        String content = readFile("sklearn/linear_model/_sag.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file286(){
        String content = readFile("sklearn/linear_model/_passive_aggressive.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file287(){
        String content = readFile("sklearn/linear_model/tests/test_logistic.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file288(){
        String content = readFile("sklearn/linear_model/tests/test_sgd.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file289(){
        String content = readFile("sklearn/linear_model/tests/test_huber.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file290(){
        String content = readFile("sklearn/linear_model/tests/test_bayes.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file291(){
        String content = readFile("sklearn/linear_model/tests/test_theil_sen.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file292(){
        String content = readFile("sklearn/linear_model/tests/test_ridge.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file293(){
        String content = readFile("sklearn/linear_model/tests/test_passive_aggressive.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file294(){
        String content = readFile("sklearn/linear_model/tests/test_least_angle.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file295(){
        String content = readFile("sklearn/linear_model/tests/test_ransac.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file296(){
        String content = readFile("sklearn/linear_model/tests/test_coordinate_descent.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file297(){
        String content = readFile("sklearn/linear_model/tests/test_sparse_coordinate_descent.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file298(){
        String content = readFile("sklearn/linear_model/tests/test_perceptron.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file299(){
        String content = readFile("sklearn/linear_model/tests/test_sag.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file300(){
        String content = readFile("sklearn/linear_model/tests/test_base.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file301(){
        String content = readFile("sklearn/linear_model/tests/test_omp.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file302(){
        String content = readFile("sklearn/linear_model/_logistic.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file303(){
        String content = readFile("sklearn/linear_model/_omp.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file304(){
        String content = readFile("sklearn/linear_model/_perceptron.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file305(){
        String content = readFile("sklearn/linear_model/setup.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file306(){
        String content = readFile("sklearn/linear_model/_theil_sen.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file307(){
        String content = readFile("sklearn/linear_model/_coordinate_descent.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file308(){
        String content = readFile("sklearn/linear_model/_ridge.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file309(){
        String content = readFile("sklearn/linear_model/_stochastic_gradient.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file310(){
        String content = readFile("sklearn/linear_model/_huber.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file311(){
        String content = readFile("sklearn/linear_model/_bayes.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file312(){
        String content = readFile("sklearn/linear_model/_ransac.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file314(){
        String content = readFile("sklearn/inspection/_partial_dependence.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file315(){
        String content = readFile("sklearn/inspection/_plot/tests/test_plot_partial_dependence.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file316(){
        String content = readFile("sklearn/inspection/_plot/partial_dependence.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file317(){
        String content = readFile("sklearn/inspection/tests/test_partial_dependence.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file318(){
        String content = readFile("sklearn/inspection/tests/test_permutation_importance.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file319(){
        String content = readFile("sklearn/inspection/_permutation_importance.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file320(){
        String content = readFile("sklearn/inspection/setup.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file321(){
        String content = readFile("sklearn/impute/_base.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file322(){
        String content = readFile("sklearn/impute/_knn.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file323(){
        String content = readFile("sklearn/impute/tests/test_common.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file324(){
        String content = readFile("sklearn/impute/tests/test_knn.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file325(){
        String content = readFile("sklearn/impute/tests/test_base.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file326(){
        String content = readFile("sklearn/impute/tests/test_impute.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file327(){
        String content = readFile("sklearn/impute/_iterative.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file328(){
        String content = readFile("sklearn/gaussian_process/_gpc.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file329(){
        String content = readFile("sklearn/gaussian_process/tests/test_gpr.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file330(){
        String content = readFile("sklearn/gaussian_process/tests/test_kernels.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file331(){
        String content = readFile("sklearn/gaussian_process/tests/_mini_sequence_kernel.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file332(){
        String content = readFile("sklearn/gaussian_process/tests/test_gpc.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file333(){
        String content = readFile("sklearn/gaussian_process/kernels.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file334(){
        String content = readFile("sklearn/gaussian_process/_gpr.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file335(){
        String content = readFile("sklearn/feature_selection/_base.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file336(){
        String content = readFile("sklearn/feature_selection/_variance_threshold.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file337(){
        String content = readFile("sklearn/feature_selection/tests/test_variance_threshold.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file338(){
        String content = readFile("sklearn/feature_selection/tests/test_rfe.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file339(){
        String content = readFile("sklearn/feature_selection/tests/test_from_model.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file340(){
        String content = readFile("sklearn/feature_selection/tests/test_sequential.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file341(){
        String content = readFile("sklearn/feature_selection/tests/test_feature_select.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file342(){
        String content = readFile("sklearn/feature_selection/tests/test_chi2.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file343(){
        String content = readFile("sklearn/feature_selection/tests/test_base.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file344(){
        String content = readFile("sklearn/feature_selection/tests/test_mutual_info.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file345(){
        String content = readFile("sklearn/feature_selection/_rfe.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file346(){
        String content = readFile("sklearn/feature_selection/_from_model.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file347(){
        String content = readFile("sklearn/feature_selection/_mutual_info.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file348(){
        String content = readFile("sklearn/feature_selection/_univariate_selection.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file349(){
        String content = readFile("sklearn/feature_selection/_sequential.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file350(){
        String content = readFile("sklearn/feature_selection/_base.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file351(){
        String content = readFile("sklearn/feature_selection/_variance_threshold.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file352(){
        String content = readFile("sklearn/feature_selection/tests/test_variance_threshold.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file353(){
        String content = readFile("sklearn/feature_selection/tests/test_rfe.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file354(){
        String content = readFile("sklearn/feature_selection/tests/test_from_model.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file355(){
        String content = readFile("sklearn/feature_selection/tests/test_sequential.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file356(){
        String content = readFile("sklearn/feature_selection/tests/test_feature_select.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file357(){
        String content = readFile("sklearn/feature_selection/tests/test_chi2.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file358(){
        String content = readFile("sklearn/feature_selection/tests/test_base.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file359(){
        String content = readFile("sklearn/feature_selection/tests/test_mutual_info.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file360(){
        String content = readFile("sklearn/feature_selection/_rfe.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file361(){
        String content = readFile("sklearn/feature_selection/_from_model.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file362(){
        String content = readFile("sklearn/feature_selection/_mutual_info.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file363(){
        String content = readFile("sklearn/feature_selection/_univariate_selection.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file364(){
        String content = readFile("sklearn/feature_selection/_sequential.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file365(){
        String content = readFile("sklearn/externals/conftest.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file366(){
        String content = readFile("sklearn/externals/_pilutil.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file367(){
        String content = readFile("sklearn/externals/_lobpcg.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file368(){
        String content = readFile("sklearn/externals/_pep562.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file369(){
        String content = readFile("sklearn/externals/_arff.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file370(){
        String content = readFile("sklearn/feature_extraction/tests/test_dict_vectorizer.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file371(){
        String content = readFile("sklearn/feature_extraction/tests/test_image.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file372(){
        String content = readFile("sklearn/feature_extraction/tests/test_text.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file373(){
        String content = readFile("sklearn/feature_extraction/tests/test_feature_hasher.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file374(){
        String content = readFile("sklearn/feature_extraction/_hash.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file375(){
        String content = readFile("sklearn/feature_extraction/_dict_vectorizer.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file376(){
        String content = readFile("sklearn/feature_extraction/setup.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file377(){
        String content = readFile("sklearn/feature_extraction/_stop_words.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file378(){
        String content = readFile("sklearn/feature_extraction/text.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file379(){
        String content = readFile("sklearn/feature_extraction/image.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file380(){
        String content = readFile("sklearn/ensemble/_base.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file381(){
        String content = readFile("sklearn/ensemble/_forest.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file382(){
        String content = readFile("sklearn/ensemble/_voting.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file383(){
        String content = readFile("sklearn/ensemble/_bagging.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file384(){
        String content = readFile("sklearn/ensemble/tests/test_common.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file385(){
        String content = readFile("sklearn/ensemble/tests/test_voting.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file386(){
        String content = readFile("sklearn/ensemble/tests/test_forest.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file387(){
        String content = readFile("sklearn/ensemble/tests/test_bagging.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file388(){
        String content = readFile("sklearn/ensemble/tests/test_gradient_boosting_loss_functions.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file389(){
        String content = readFile("sklearn/ensemble/tests/test_stacking.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file390(){
        String content = readFile("sklearn/ensemble/tests/test_weight_boosting.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file391(){
        String content = readFile("sklearn/ensemble/tests/test_gradient_boosting.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file392(){
        String content = readFile("sklearn/ensemble/tests/test_iforest.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file393(){
        String content = readFile("sklearn/ensemble/tests/test_base.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file394(){
        String content = readFile("sklearn/ensemble/_gb.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file395(){
        String content = readFile("sklearn/ensemble/_weight_boosting.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file396(){
        String content = readFile("sklearn/ensemble/_hist_gradient_boosting/predictor.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file397(){
        String content = readFile("sklearn/ensemble/_hist_gradient_boosting/binning.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file398(){
        String content = readFile("sklearn/ensemble/_hist_gradient_boosting/gradient_boosting.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file399(){
        String content = readFile("sklearn/ensemble/_hist_gradient_boosting/tests/test_monotonic_contraints.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file400(){
        String content = readFile("sklearn/ensemble/_hist_gradient_boosting/tests/test_grower.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file401(){
        String content = readFile("sklearn/ensemble/_hist_gradient_boosting/tests/test_binning.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
//    @Test
//    public void file402(){
//        String content = readFile("sklearn/ensemble/_hist_gradient_boosting/tests/__init__.py");
//        CompilationUnit converted = convert(content);
//        Assert.assertEquals(converted.getProblems().length,0);
//    }
    @Test
    public void file403(){
        String content = readFile("sklearn/ensemble/_hist_gradient_boosting/tests/test_splitting.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file404(){
        String content = readFile("sklearn/ensemble/_hist_gradient_boosting/tests/test_predictor.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file405(){
        String content = readFile("sklearn/ensemble/_hist_gradient_boosting/tests/test_loss.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file406(){
        String content = readFile("sklearn/ensemble/_hist_gradient_boosting/tests/test_gradient_boosting.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file407(){
        String content = readFile("sklearn/ensemble/_hist_gradient_boosting/tests/test_warm_start.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file408(){
        String content = readFile("sklearn/ensemble/_hist_gradient_boosting/tests/test_compare_lightgbm.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file409(){
        String content = readFile("sklearn/ensemble/_hist_gradient_boosting/tests/test_histogram.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file410(){
        String content = readFile("sklearn/ensemble/_hist_gradient_boosting/__init__.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file411(){
        String content = readFile("sklearn/ensemble/_hist_gradient_boosting/loss.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file412(){
        String content = readFile("sklearn/ensemble/_hist_gradient_boosting/grower.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file413(){
        String content = readFile("sklearn/ensemble/setup.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file414(){
        String content = readFile("sklearn/ensemble/_gb_losses.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file415(){
        String content = readFile("sklearn/ensemble/_iforest.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file416(){
        String content = readFile("sklearn/ensemble/_stacking.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file417(){
        String content = readFile("sklearn/decomposition/_base.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file418(){
        String content = readFile("sklearn/decomposition/_factor_analysis.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file419(){
        String content = readFile("sklearn/decomposition/_nmf.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file420(){
        String content = readFile("sklearn/decomposition/_sparse_pca.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file421(){
        String content = readFile("sklearn/decomposition/_kernel_pca.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file422(){
        String content = readFile("sklearn/decomposition/tests/test_dict_learning.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file423(){
        String content = readFile("sklearn/decomposition/tests/test_fastica.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file424(){
        String content = readFile("sklearn/decomposition/tests/test_truncated_svd.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file425(){
        String content = readFile("sklearn/decomposition/tests/test_nmf.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file426(){
        String content = readFile("sklearn/decomposition/tests/test_online_lda.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file427(){
        String content = readFile("sklearn/decomposition/tests/test_kernel_pca.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file428(){
        String content = readFile("sklearn/decomposition/tests/test_sparse_pca.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file429(){
        String content = readFile("sklearn/decomposition/tests/test_factor_analysis.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file430(){
        String content = readFile("sklearn/decomposition/tests/test_incremental_pca.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file431(){
        String content = readFile("sklearn/decomposition/tests/test_pca.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file432(){
        String content = readFile("sklearn/decomposition/_incremental_pca.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file433(){
        String content = readFile("sklearn/decomposition/_fastica.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file434(){
        String content = readFile("sklearn/decomposition/_pca.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file435(){
        String content = readFile("sklearn/decomposition/setup.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file436(){
        String content = readFile("sklearn/decomposition/_truncated_svd.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file437(){
        String content = readFile("sklearn/decomposition/_dict_learning.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file438(){
        String content = readFile("sklearn/decomposition/_lda.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void file439(){
        String content = readFile("sklearn/datasets/_base.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file440(){
        String content = readFile("sklearn/datasets/_samples_generator.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file441(){
        String content = readFile("sklearn/datasets/_olivetti_faces.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file442(){
        String content = readFile("sklearn/datasets/_kddcup99.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file443(){
        String content = readFile("sklearn/datasets/_california_housing.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file444(){
        String content = readFile("sklearn/datasets/_species_distributions.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file445(){
        String content = readFile("sklearn/datasets/_covtype.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file446(){
        String content = readFile("sklearn/datasets/tests/test_california_housing.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file447(){
        String content = readFile("sklearn/datasets/tests/conftest.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file448(){
        String content = readFile("sklearn/datasets/tests/test_common.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file449(){
        String content = readFile("sklearn/datasets/tests/test_lfw.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file450(){
        String content = readFile("sklearn/datasets/tests/test_rcv1.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file451(){
        String content = readFile("sklearn/datasets/tests/test_kddcup99.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file452(){
        String content = readFile("sklearn/datasets/tests/test_covtype.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file453(){
        String content = readFile("sklearn/datasets/tests/test_svmlight_format.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file454(){
        String content = readFile("sklearn/datasets/tests/test_20news.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file455(){
        String content = readFile("sklearn/datasets/tests/test_samples_generator.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file456(){
        String content = readFile("sklearn/datasets/tests/test_openml.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file457(){
        String content = readFile("sklearn/datasets/tests/test_base.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file458(){
        String content = readFile("sklearn/datasets/tests/test_olivetti_faces.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file459(){
        String content = readFile("sklearn/datasets/setup.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file460(){
        String content = readFile("sklearn/datasets/_rcv1.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file461(){
        String content = readFile("sklearn/datasets/_lfw.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file462(){
        String content = readFile("sklearn/datasets/_svmlight_format_io.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file463(){
        String content = readFile("sklearn/datasets/_twenty_newsgroups.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file464(){
        String content = readFile("sklearn/datasets/_openml.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file465(){
        String content = readFile("sklearn/cross_decomposition/cluster/tests/test_mean_shift.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file466(){
        String content = readFile("sklearn/cross_decomposition/cluster/tests/test_k_means.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file467(){
        String content = readFile("sklearn/cross_decomposition/cluster/tests/test_feature_agglomeration.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file468(){
        String content = readFile("sklearn/cross_decomposition/cluster/tests/test_dbscan.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file469(){
        String content = readFile("sklearn/cross_decomposition/cluster/tests/test_birch.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file470(){
        String content = readFile("sklearn/cross_decomposition/cluster/tests/test_affinity_propagation.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file471(){
        String content = readFile("sklearn/cross_decomposition/cluster/tests/test_optics.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file472(){
        String content = readFile("sklearn/cross_decomposition/cluster/tests/test_hierarchical.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file473(){
        String content = readFile("sklearn/cross_decomposition/cluster/tests/common.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file474(){
        String content = readFile("sklearn/cross_decomposition/cluster/tests/test_bicluster.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file475(){
        String content = readFile("sklearn/cross_decomposition/cluster/tests/test_spectral.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file476(){
        String content = readFile("sklearn/cross_decomposition/cluster/setup.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file477(){
        String content = readFile("sklearn/cross_decomposition/conftest.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file478(){
        String content = readFile("sklearn/cross_decomposition/multiclass.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file479(){
        String content = readFile("sklearn/cross_decomposition/compose/_target.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file480(){
        String content = readFile("sklearn/cross_decomposition/compose/tests/test_target.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file481(){
        String content = readFile("sklearn/cross_decomposition/compose/tests/test_column_transformer.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file482(){
        String content = readFile("sklearn/cross_decomposition/compose/_column_transformer.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file483(){
        String content = readFile("sklearn/cross_decomposition/kernel_approximation.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file484(){
        String content = readFile("sklearn/cross_decomposition/tests/test_pls.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file485(){
        String content = readFile("sklearn/cross_decomposition/random_projection.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file486(){
        String content = readFile("sklearn/cross_decomposition/isotonic.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file487(){
        String content = readFile("sklearn/cross_decomposition/multioutput.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file488(){
        String content = readFile("sklearn/cross_decomposition/kernel_ridge.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file489(){
        String content = readFile("sklearn/cross_decomposition/conftest copy.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file490(){
        String content = readFile("sklearn/cross_decomposition/_distributor_init.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file491(){
        String content = readFile("sklearn/cross_decomposition/covariance/_graph_lasso.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file492(){
        String content = readFile("sklearn/cross_decomposition/covariance/_empirical_covariance.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file493(){
        String content = readFile("sklearn/cross_decomposition/covariance/_shrunk_covariance.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file494(){
        String content = readFile("sklearn/cross_decomposition/covariance/_elliptic_envelope.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file495(){
        String content = readFile("sklearn/cross_decomposition/covariance/tests/test_covariance.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file496(){
        String content = readFile("sklearn/cross_decomposition/covariance/tests/test_robust_covariance.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file497(){
        String content = readFile("sklearn/cross_decomposition/covariance/tests/test_graphical_lasso.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file498(){
        String content = readFile("sklearn/cross_decomposition/covariance/tests/test_elliptic_envelope.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file499(){
        String content = readFile("sklearn/cross_decomposition/covariance/_robust_covariance.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file500(){
        String content = readFile("sklearn/cross_decomposition/_min_dependencies.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file501(){
        String content = readFile("sklearn/cross_decomposition/naive_bayes.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file502(){
        String content = readFile("sklearn/cross_decomposition/pipeline.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file503(){
        String content = readFile("sklearn/cross_decomposition/maint_tools/check_pxd_in_installation.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file504(){
        String content = readFile("sklearn/cross_decomposition/maint_tools/sort_whats_new.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file505(){
        String content = readFile("sklearn/cross_decomposition/maint_tools/test_docstrings.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file506(){
        String content = readFile("sklearn/cross_decomposition/discriminant_analysis.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file507(){
        String content = readFile("sklearn/cross_decomposition/exceptions.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file508(){
        String content = readFile("sklearn/cross_decomposition/_config.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file509(){
        String content = readFile("sklearn/cross_decomposition/_pls.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file510(){
        String content = readFile("sklearn/cross_decomposition/dummy.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file511(){
        String content = readFile("sklearn/cross_decomposition/base.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
    @Test
    public void file512(){
        String content = readFile("sklearn/cross_decomposition/calibration.py");
        CompilationUnit converted = convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }
   // "search_holder"
    private CompilationUnit convert(String content) {
        mod ast = PyASTParser.parsePython(content);
        PythonASTUtil pythonASTUtil = new PythonASTUtil();
        PyCompilationUnit pyCompilationUnit = pythonASTUtil.createPyCompilationUnit(ast);

        PyNodeCounter pyNodeCounter = new PyNodeCounter();
        try {
            pyNodeCounter.visit(ast);
        } catch (Exception e) {
            e.printStackTrace();
        }
        PyASTVisitor astParser = new PyASTVisitor();
        try {
            astParser.visit(ast);
        } catch (Exception e) {
            e.printStackTrace();
        }


        JDTASTVisitor jdtastVisitor = new JDTASTVisitor();
        CompilationUnit cu = (CompilationUnit) JavaASTUtil.parseSource(pyCompilationUnit.toString());
        log.warn("Errors "+cu.getProblems().length);
        cu.accept(jdtastVisitor);
        comparePythonAndJDTnodes(astParser,jdtastVisitor);

        return cu;
    }

    public void comparePythonAndJDTnodes(PyASTVisitor pyAstParser,JDTASTVisitor jdtastVisitor){
        Assert.assertEquals(pyAstParser.getPythonASTStats().get("For").intValue(),jdtastVisitor.getStatFor("Java_EnhancedForStatement"));
        Assert.assertTrue(pyAstParser.getPythonASTStats().get("ListComp").intValue()<=jdtastVisitor.getStatFor("Java_PyListComprehension"));
        Assert.assertEquals(pyAstParser.getPythonASTStats().get("With").intValue(),jdtastVisitor.getStatFor("Java_PyWithStatement"));
        Assert.assertTrue(pyAstParser.getPythonASTStats().get("DictComp").intValue() <=jdtastVisitor.getStatFor("Java_PyDictComprehensiont"));
        Assert.assertEquals(pyAstParser.getPythonASTStats().get("If").intValue(),jdtastVisitor.getStatFor("Java_IfStatement"));
        Assert.assertEquals(pyAstParser.getPythonASTStats().get("Raise").intValue(),jdtastVisitor.getStatFor("Java_ThrowStatement"));
        Assert.assertEquals(pyAstParser.getPythonASTStats().get("Return").intValue(),jdtastVisitor.getStatFor("Java_Return"));
        Assert.assertEquals(pyAstParser.getPythonASTStats().get("TryExcept").intValue()+pyAstParser.getPythonASTStats().get("TryFinally").intValue(),
                jdtastVisitor.getStatFor("Java_TryStatement"));
        Assert.assertEquals(pyAstParser.getPythonASTStats().get("Continue").intValue(),jdtastVisitor.getStatFor("Java_ContinueStatement"));
        Assert.assertEquals(pyAstParser.getPythonASTStats().get("AssertStmt").intValue(),jdtastVisitor.getStatFor("Java_AssertStatement"));
        Assert.assertEquals(pyAstParser.getPythonASTStats().get("While").intValue(),jdtastVisitor.getStatFor("Java_WhileStatement"));
        Assert.assertTrue(pyAstParser.getPythonASTStats().get("Lambda").intValue()<=jdtastVisitor.getStatFor("Java_LambdaExpression"));

    }

    public String readFile(String fileName) {
        Path resourceDirectory = Paths.get("src","test","resources","ASTConversion",fileName);
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





    private void printStats(){

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

        pyNodeMap.entrySet().stream().sorted(Map.Entry.comparingByValue()).forEach(k->{
        log.warn("Number of "+k.getKey()+" : "+k.getValue());
        });

        int numberOfNodes = pyNodeMap.values().stream().mapToInt(i -> i).sum();
//        int totalNodes =    FunctionDef+module+interactive+expression+suite+asyncFunctionDef+classDef+Return+Delete+Return
//        +Assign+AugAssign+For+AsyncFor+While+If+With+AsyncWith+Raise +Try+AssertStmt+Import+ImportFrom+Global+Nonloca+Expr
//        +Pass+Break+Continue+BoolOp+BinOp+UnaryOp+Lambda+IfExp+Dict+Set+ListComp+SetComp+DictComp+GeneratorExp+ Await+Yield+ YieldFrom+ Compare
//        +  Call + Num+ Str+ Bytes+  NameConstant+ Ellipsis+Attribute+ Subscript+ Starred+ Name+ List+Slice+ ExtSlice+
//                Index + ExceptHandler + TryFinally+ TryExcept+ FormattedValue+ Tuple;

        log.warn("Total Number of  Nodes  : "+ numberOfNodes );



    }


    public class PyNodeCounter extends Visitor {
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
    }


}
