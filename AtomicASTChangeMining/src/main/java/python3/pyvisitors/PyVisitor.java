package python3.pyvisitors;

import org.apache.log4j.Logger;
import org.jpp.astnodes.Visitor;
import org.jpp.astnodes.ast.Assign;
import org.jpp.astnodes.ast.Attribute;
import org.jpp.astnodes.ast.Import;
import org.jpp.astnodes.ast.Name;
import org.jpp.heart.AstList;
import python3.typeinference.core.TypeDecNeeds;
import python3.typeinference.core.TypeInformation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class PyVisitor extends Visitor {
    static Logger logger = Logger.getLogger(Visitor.class);
    private ArrayList<Import> imports = new ArrayList<Import>();
    private Set<Assign> assignment = new HashSet<Assign>();
    private Set<Name> name= new HashSet<Name>();
    private Set<Attribute> attributes= new HashSet<Attribute>();
    private Set<TypeDecNeeds> typeDecNeeds = new HashSet<TypeDecNeeds>();

    @Override
    public Object visitImport(Import node) throws Exception {
        imports.add(node);
        return super.visitImport(node);
    }

    @Override
    public Object visitAssign(Assign node) throws Exception {

        for (Object ch : (AstList) node.getTargets()){
            if (ch instanceof Name){
                TypeDecNeeds typeDecNode = new TypeDecNeeds();
                typeDecNode.setName(String.valueOf(((Name) ch).getId()));
                typeDecNode.setRow(((Name) ch).getLine());
                typeDecNode.setCol_offset(((Name) ch).getCol_offset());
                typeDecNeeds.add(typeDecNode);
            }
            assignment.add(node);
        }


        return super.visitAssign(node);
    }

    @Override
    public Object visitName(Name node) throws Exception {
        name.add(node);
        return super.visitName(node);
    }

    @Override
    public Object visitAttribute(Attribute node) throws Exception {
        attributes.add(node);
        return super.visitAttribute(node);
    }


    public Set<TypeDecNeeds> getTypeDecNeeds() {
        return typeDecNeeds;
    }

    public ArrayList<Import> getImports() {
        return imports;
    }

    public Set<Assign> getAssignment() {
        return assignment;
    }

    public Set<Name> getName() {
        return name;
    }

    public Set<Attribute> getAttributes() {
        return attributes;
    }

    public ArrayList<Import> getImport(){
        return imports;
    }
}
