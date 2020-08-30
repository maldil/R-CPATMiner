package python3.PyVisitors;

import org.jpp.astnodes.Visitor;
import org.jpp.astnodes.ast.Import;
import org.jpp.astnodes.ast.List;

import java.util.ArrayList;

public class PyImportVisitor extends Visitor {
    ArrayList<Import> imports = new ArrayList<Import>();
    @Override
    public Object visitImport(Import node) throws Exception {
        imports.add(node);
        return super.visitImport(node);
    }
    public ArrayList<Import> getImport(){
        return imports;
    }
}
