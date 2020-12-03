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
        file36();
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
