package python3;

import org.apache.log4j.Logger;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.testng.Assert;
import org.testng.annotations.Test;
import python3.typeinference.core.TypeASTNode;
import utils.JavaASTUtil;

import java.util.HashMap;

public class PythonToJavaConversion {
    private static org.apache.log4j.Logger log = Logger.getLogger(LineCounterTestASTVisitor.class);
    @Test
    public void testIfExpression1(){
        String content = "def add_arrays():\n" +
                "    self.is_ts = is_ts if is_ts is not None and is_ts is None  else isinstance(coo,boo)";
        CompilationUnit converted = Convert(content);
        Assert.assertEquals(converted.toString(),
                "public class PyDummyClass {\n" +
                "  void add_arrays(){\n" +
                "    self.is_ts=(is_ts != None) && (is_ts == None) ? is_ts : isinstance(coo,boo);\n" +
                "  }\n" +
                "}\n");
    }

    @Test
    public void testIfExpression2(){
        String content = "def add_arrays():\n" +
                "    self.is_ts = is_ts if is_ts is not None or is_ts is None  else isinstance(coo,boo)";
        CompilationUnit converted = Convert(content);
        Assert.assertEquals(converted.toString(),
                "public class PyDummyClass {\n" +
                        "  void add_arrays(){\n" +
                        "    self.is_ts=(is_ts != None) || (is_ts == None) ? is_ts : isinstance(coo,boo);\n" +
                        "  }\n" +
                        "}\n");
    }

    @Test
    public void testIfExpression3(){
        String content = "def add_arrays():\n" +
                "    self.is_ts = is_ts if is_ts is not None and is_ts is None and 1!=4 or foo<kool  else isinstance(coo,boo)";
        CompilationUnit converted = Convert(content);
        Assert.assertEquals(converted.toString(),
                "public class PyDummyClass {\n" +
                        "  void add_arrays(){\n" +
                        "    self.is_ts=(is_ts != None) && (is_ts == None) && (1 != 4) || (foo < kool) ? is_ts : isinstance(coo,boo);\n" +
                        "  }\n" +
                        "}\n");
    }

    @Test
    public void testConversion1(){
        String content = "class ArkaneSpecies(RMGObject):\n" +
                "    \"\"\"\n" +
                "    A class for archiving an Arkane species including its statmech data into .yml files\n" +
                "    \"\"\"\n" +
                "    i = 12345\n" +
                "    def __init__(self, species=None, conformer=None, author='', level_of_theory='', model_chemistry='',\n" +
                "                 frequency_scale_factor=None, use_hindered_rotors=None, use_bond_corrections=None, atom_energies='',\n" +
                "                 chemkin_thermo_string='', smiles=None, adjacency_list=None, inchi=None, inchi_key=None, xyz=None,\n" +
                "                 molecular_weight=None, symmetry_number=None, transport_data=None, energy_transfer_model=None,\n" +
                "                 thermo=None, thermo_data=None, label=None, datetime=None, RMG_version=None, reactants=None,\n" +
                "                 products=None, reaction_label=None, is_ts=None, charge=None, formula=None, multiplicity=None):\n" +
                "        # reactants/products/reaction_label need to be in the init() to avoid error when loading a TS YAML file,\n" +
                "        # but we don't use them\n" +
                "        super(ArkaneSpecies, self).__init__()\n" +
                "        if conformer is not None:\n" +
                "            self.conformer = conformer\n" +
                "        elif label is None and species is not None:\n" +
                "            boo = noo\n" +
                "        else:\n" +
                "            self.label = label\n" +
                "        if label is None and species is not None:\n" +
                "            self.label = species.label\n" +
                "        else:\n" +
                "            self.label = label\n" +
                "        if species is None and conformer is None:\n" +
                "            # Expecting to get a species or a TS when generating the object within Arkane,\n" +
                "            # or a conformer when parsing from YAML.\n" +
                "            raise ValueError('No species (or TS) or conformer was passed to the ArkaneSpecies object')\n" +
                "        self.author = author\n" +
                "        self.level_of_theory = level_of_theory\n" +
                "        self.model_chemistry = model_chemistry\n" +
                "        self.frequency_scale_factor = frequency_scale_factor\n" +
                "        self.use_hindered_rotors = use_hindered_rotors\n" +
                "        self.use_bond_corrections = use_bond_corrections\n" +
                "        self.atom_energies = atom_energies\n" +
                "        self.xyz = xyz\n" +
                "        self.molecular_weight = molecular_weight\n" +
                "        self.symmetry_number = symmetry_number\n" +
                "        self.charge = charge\n" +
                "        self.multiplicity = multiplicity\n" +
                "        self.is_ts = is_ts if one is not None and voo and cool or tool else isinstance(coo,boo)\n" +
                "        if not self.is_ts:\n" +
                "            self.chemkin_thermo_string = chemkin_thermo_string\n" +
                "            self.smiles = smiles\n" +
                "            self.adjacency_list = adjacency_list\n" +
                "            self.inchi = inchi\n" +
                "            self.inchi_key = inchi_key\n" +
                "            self.transport_data = transport_data\n" +
                "            self.energy_transfer_model = energy_transfer_model\n" +
                "            self.thermo = thermo\n" +
                "            self.thermo_data = thermo_data\n" +
                "            self.formula = formula\n" +
                "        else:\n" +
                "            # initialize TS-related attributes\n" +
                "            self.imaginary_frequency = None\n" +
                "            self.reaction_label = ''\n" +
                "            self.reactants = list()\n" +
                "            self.products = list()\n" +
                "        if species is not None:\n" +
                "            self.update_species_attributes(species)\n" +
                "        self.RMG_version = RMG_version if RMG_version is not None else __version__\n" +
                "        self.datetime = datetime if datetime is not None else time.strftime(\"%Y-%m-%d %H:%M\")";

        CompilationUnit convert = Convert(content);
        Assert.assertEquals(convert.toString(),"public class ArkaneSpecies extends RMGObject {\n" +
                "  void __init__(  ArkaneSpecies self){\n" +
                "    super(ArkaneSpecies,self);\n" +
                "    if ((conformer != None)) {\n" +
                "      self.conformer=conformer;\n" +
                "    }\n" +
                " else     if ((label == None) && (species != None)) {\n" +
                "      boo=noo;\n" +
                "    }\n" +
                " else {\n" +
                "      self.label=label;\n" +
                "    }\n" +
                "    if ((label == None) && (species != None)) {\n" +
                "      self.label=species.label;\n" +
                "    }\n" +
                " else {\n" +
                "      self.label=label;\n" +
                "    }\n" +
                "    if ((species == None) && (conformer == None)) {\n" +
                "      throw new ValueError(\"No species (or TS) or conformer was passed to the ArkaneSpecies object\");\n" +
                "    }\n" +
                "    self.author=author;\n" +
                "    self.level_of_theory=level_of_theory;\n" +
                "    self.model_chemistry=model_chemistry;\n" +
                "    self.frequency_scale_factor=frequency_scale_factor;\n" +
                "    self.use_hindered_rotors=use_hindered_rotors;\n" +
                "    self.use_bond_corrections=use_bond_corrections;\n" +
                "    self.atom_energies=atom_energies;\n" +
                "    self.xyz=xyz;\n" +
                "    self.molecular_weight=molecular_weight;\n" +
                "    self.symmetry_number=symmetry_number;\n" +
                "    self.charge=charge;\n" +
                "    self.multiplicity=multiplicity;\n" +
                "    self.is_ts=(one != None) && voo && cool || tool ? is_ts : isinstance(coo,boo);\n" +
                "    if (!self.is_ts) {\n" +
                "      self.chemkin_thermo_string=chemkin_thermo_string;\n" +
                "      self.smiles=smiles;\n" +
                "      self.adjacency_list=adjacency_list;\n" +
                "      self.inchi=inchi;\n" +
                "      self.inchi_key=inchi_key;\n" +
                "      self.transport_data=transport_data;\n" +
                "      self.energy_transfer_model=energy_transfer_model;\n" +
                "      self.thermo=thermo;\n" +
                "      self.thermo_data=thermo_data;\n" +
                "      self.formula=formula;\n" +
                "    }\n" +
                " else {\n" +
                "      self.imaginary_frequency=None;\n" +
                "      self.reaction_label=\"\";\n" +
                "      self.reactants=list();\n" +
                "      self.products=list();\n" +
                "    }\n" +
                "    if ((species != None)) {\n" +
                "      self.update_species_attributes(species);\n" +
                "    }\n" +
                "    self.RMG_version=(RMG_version != None) ? RMG_version : __version__;\n" +
                "    self.datetime=(datetime != None) ? datetime : time.strftime(\"%Y-%m-%d %H:%M\");\n" +
                "  }\n" +
                "}\n");


    }

    private CompilationUnit Convert(String content) {
        HashMap<TypeASTNode, String> typeASTNodeStringHashMap = new HashMap<>();
        PythonASTUtil pythonASTUtil = new PythonASTUtil();
        PyCompilationUnit pyCompilationUnit = pythonASTUtil.parseSource(content,typeASTNodeStringHashMap);
        return (CompilationUnit)JavaASTUtil.parseSource(pyCompilationUnit.toString());
    }

    @Test
    public void testConversion2(){

    }
    @Test
    public void testConversion3(){

    }
    @Test
    public void testConversion4(){

    }
    @Test
    public void testConversion5(){

    }
}
