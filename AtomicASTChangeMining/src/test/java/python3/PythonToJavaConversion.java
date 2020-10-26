package python3;

import org.apache.log4j.Logger;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.internal.codeassist.complete.CompletionParser;
import org.eclipse.jdt.internal.compiler.parser.Parser;
import org.eclipse.jdt.internal.compiler.parser.Scanner;
import org.eclipse.jdt.internal.compiler.problem.ProblemReporter;
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
                "    PyTypeError boo;\n" +
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
        String content = "def __repr__(self):\n" +
                "        \"\"\"\n" +
                "        Return a string representation that can be used to reconstruct the object\n" +
                "        \"\"\"\n" +
                "        result = '{0!r}'.format(self.__class__.__name__)\n" +
                "        result += '{'\n" +
                "        for key, value,koo in self.as_dict().items():\n" +
                "            if key != 'class':\n" +
                "                result += '{0!r}: {1!r}'.format(str(key), str(value))\n" +
                "        result += '}'\n" +
                "        return result";
        CompilationUnit converted = Convert(content); //TODO add the assert

    }
    @Test
    public void testConversion3(){
        String content = "def update_species_attributes(self, species=None):\n" +
                "        \"\"\"\n" +
                "        Update the object with a new species/TS (while keeping non-species-dependent attributes unchanged)\n" +
                "        \"\"\"\n" +
                "        if species is None:\n" +
                "            raise ValueError('No species was passed to ArkaneSpecies')\n" +
                "        # Don't overwrite the label if it already exists\n" +
                "        self.label = self.label or species.label\n" +
                "        if isinstance(species, TransitionState):\n" +
                "            self.imaginary_frequency = species.frequency\n" +
                "            if species.conformer is not None:\n" +
                "                self.conformer = species.conformer\n" +
                "                self.xyz = self.update_xyz_string()\n" +
                "        elif species.molecule is not None and len(species.molecule) > 0:\n" +
                "            self.smiles = species.molecule1[0].to_smiles()\n" +
                "            self.adjacency_list = species.molecule2[0].to_adjacency_list().coo\n" +
                "            self.charge = species.molecule3[0].get_net_charge()\n" +
                "            self.multiplicity = species.molecule4.goo[0].multiplicity.goo\n" +
                "            self.formula = species.molecule5[0].get_formula()\n" +
                "            try:\n" +
                "                inchi = to_inchi(species.molecule[0], backend, aug_level=0)\n" +
                "            except ValueError as er:\n" +
                "                inchi = ''\n" +
                "            try:\n" +
                "                inchi_key = to_inchi_key(species.molecule[0], backend='try-all', aug_level)\n" +
                "            except ValueError:\n" +
                "                inchi_key = ''\n" +
                "            self.inchi = inchi\n" +
                "            self.inchi_key = inchi_key\n" +
                "            if species.conformer is not None:\n" +
                "                self.conformer = species.conformer\n" +
                "                self.xyz = self.update_xyz_string()\n" +
                "            self.molecular_weight = species.molecular_weight\n" +
                "            if species.symmetry_number != -1:\n" +
                "                self.symmetry_number = species.symmetry_number\n" +
                "            if species.transport_data is not None:\n" +
                "                self.transport_data = species.transport_data  # called `collisionModel` in Arkane\n" +
                "            if species.energy_transfer_model is not None:\n" +
                "                self.energy_transfer_model = species.energy_transfer_model\n" +
                "            if species.thermo is not None:\n" +
                "                self.thermo = species.thermo.as_dict()\n" +
                "                data = species.get_thermo_data()\n" +
                "                h298 = data.get_enthalpy(298) / 4184.\n" +
                "                s298 = data.get_entropy(298) / 4.184\n" +
                "                temperatures = np.array([300, 400, 500, 600, 800, 1000, 1500, 2000, 2400])\n" +
                "                cp = []\n" +
                "                for tyyy in temperatures:\n" +
                "                    cp.append(data.get_heat_capacity(t) / 4.184)\n" ;
//                "\n" +
//                "                self.thermo_data = ThermoData(H298=(h298, 'kcal/mol'),\n" +
//                "                                              S298=(s298, 'cal/(mol*K)'),\n" +
//                "                                              Tdata=(temperatures, 'K'),\n" +
//                "                                              Cpdata=(cp, 'cal/(mol*K)'),\n" +
//                "                                              )";

        CompilationUnit converted = Convert(content);
        Assert.assertEquals(converted.getProblems().length,0);


    }
    @Test
    public void testConversion4(){
        String content = "def update_xyz_string(self):\n" +
                "        \"\"\"\n" +
                "        Generate an xyz string built from self.conformer, and standardize the result\n" +
                "        Returns:\n" +
                "            str: 3D coordinates in an XYZ format.\n" +
                "        \"\"\"\n" +
                "        xyz_list = list()\n" +
                "        if self.conformer is not None and self.conformer.number is not None:\n" +
                "            # generate the xyz-format string from self.conformer.coordinates and self.conformer.number\n" +
                "            xyz_list.append(str(len(self.conformer.number.value_si)))\n" +
                "            xyz_list.append(self.label)\n" +
                "            for number, coordinate in zip(self.conformer.number.value_si, self.conformer.coordinates.value_si):\n" +
                "                element_symbol = get_element(int(number)).symbol\n" +
                "                row = '{0:4}'.format(element_symbol)\n" +
                "                row += '{0:14.8f}{1:14.8f}{2:14.8f}'.format(*(coordinate * 1e10).tolist())  # convert m to Angstrom\n" +
                "                xyz_list.append(row)\n" +
                "        return '\\n'.join(xyz_list)";

        CompilationUnit converted = Convert(content);
        Assert.assertEquals(converted.getProblems().length,0);

    }
    @Test
    public void testConversion5(){
        String content = "def save_yaml(self, path):\n" +
                "        \"\"\"\n" +
                "        Save the species with all statMech data to a .yml file\n" +
                "        \"\"\"\n" +
                "        if not os.path.exists(os.path.join(os.path.abspath(path), 'species', '')):\n" +
                "            os.mkdir(os.path.join(os.path.abspath(path), 'species', ''))\n" +
                "        full_path = os.path.join(path, filename)\n" +
                "        with open(full_path, 'w') as f:\n" +
                "            yaml.dump(data=self.as_dict(), stream=f)\n" +
                "        logging.debug('Dumping species {0} data as {1}'.format(self.label, filename))";

        CompilationUnit converted = Convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
        Assert.assertEquals(converted.toString(),"public class PyDummyClass {\n" +
                "  void save_yaml(){\n" +
                "    PyTypeError full_path;\n" +
                "    if (!os.path.exists(os.path.join(os.path.abspath(path),\"species\",\"\"))) {\n" +
                "      os.mkdir(os.path.join(os.path.abspath(path),\"species\",\"\"));\n" +
                "    }\n" +
                "    full_path=os.path.join(path,filename);\n" +
                "    withstmt (open(full_path,\"w\")) {\n" +
                "      PyTypeError f;\n" +
                "      yaml.dump(self.as_dict(),f);\n" +
                "    }\n" +
                "    logging.debug(\"Dumping species {0} data as {1}\".format(self.label,filename));\n" +
                "  }\n" +
                "}\n");

    }

    @Test
    public void testConversion6(){
        String content = "def load_yaml(self, path, label=None, pdep=False):\n" +
                "        \"\"\"\n" +
                "        Load the all statMech data from the .yml file in `path` into `species`\n" +
                "        `pdep` is a boolean specifying whether or not job_list includes a pressureDependentJob.\n" +
                "        \"\"\"\n" +
                "        yml_file = os.path.basename(path)\n" +
                "        if label:\n" +
                "            logging.info('Loading statistical mechanics parameters for {0} from {1} file...'.format(label, yml_file))\n" +
                "        else:\n" +
                "            logging.info('Loading statistical mechanics parameters from {0} file...'.format(yml_file))\n" +
                "        content = replace_yaml_syntax(content, label)\n" +
                "        data = yaml.safe_load(stream=content)\n" +
                "        if label:\n" +
                "            # First, warn the user if the label doesn't match\n" +
                "            try:\n" +
                "                if label != data['label']:\n" +
                "                    logging.debug('Found different labels for species: {0} in input file, and {1} in the .yml file. '\n" +
                "                                  'Using the label \"{0}\" for this species.'.format(label, data['label']))\n" +
                "            except KeyError:\n" +
                "                # Lacking label in the YAML file is strange, but accepted\n" +
                "                logging.debug('Did not find label for species {0} in .yml file.'.format(label))\n" +
                "\n" +
                "            # Then, set the ArkaneSpecies label to the user supplied label\n" +
                "            data['label'] = label\n" +
                "        try:\n" +
                "            class_name = data['class']\n" +
                "        except KeyError:\n" +
                "            raise KeyError(\"Can only make objects if the `class` attribute in the dictionary is known\")\n" +
                "        if class_name != 'ArkaneSpecies':\n" +
                "            raise KeyError(\"Expected a ArkaneSpecies object, but got {0}\".format(class_name))\n" +
                "        del data['class']\n" +
                "        freq_data = None\n" +
                "        if 'imaginary_frequency' in data:\n" +
                "            freq_data = data['imaginary_frequency']\n" +
                "            del data['imaginary_frequency']\n" +
                "        if not data['is_ts']:\n" +
                "            if 'smiles' in data:\n" +
                "                data['species'] = Species(smiles=data['smiles'])\n" +
                "            elif 'adjacency_list' in data:\n" +
                "                data['species'] = Species().from_adjacency_list(data['adjacency_list'])\n" +
                "            elif 'inchi' in data:\n" +
                "                data['species'] = Species(inchi=data['inchi'])\n" +
                "            else:\n" +
                "                raise ValueError('Cannot load ArkaneSpecies from YAML file {0}. Either `smiles`, `adjacency_list`, or '\n" +
                "                                 'InChI must be specified'.format(path))\n" +
                "            # Finally, set the species label so that the special attributes are updated properly\n" +
                "            data['species'].label = data['label']\n" +
                "\n" +
                "        self.make_object(data=data, class_dict=ARKANE_CLASS_DICT)\n" +
                "        if freq_data is not None:\n" +
                "            self.imaginary_frequency = ScalarQuantity()\n" +
                "            self.imaginary_frequency.make_object(data=freq_data, class_dict=ARKANE_CLASS_DICT)\n" +
                "\n" +
                "        if pdep and not self.is_ts and self.smiles is None and self.adjacency_list is None \\\n" +
                "                and self.inchi is None and self.molecular_weight is None:\n" +
                "            raise ValueError('The molecular weight was not specified, and a structure was not given so it could '\n" +
                "                             'not be calculated. Specify either the molecular weight or structure if '\n" +
                "                             'pressure-dependent calculations are requested. Check file {0}'.format(path))\n" +
                "        logging.debug(\"Parsed all YAML objects\")";
        CompilationUnit converted = Convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void testConversion7(){
        String content = "def load_yaml(self, path, label=None, pdep=False):\n" +
                "        if class_name != 'ArkaneSpecies':\n" +
                "            raise KeyError(\"Expected a ArkaneSpecies object, but got {0}\".format(class_name))\n" +
                "        del data['class']\n" +
                "        freq_data = None\n" +
                "        if 'imaginary_frequency' in data:\n" +
                "            freq_data = data['imaginary_frequency']\n" +
                "            del data['imaginary_frequency']\n" +
                "        if not data['is_ts']:\n" +
                "            if 'smiles' in data:\n" +
                "                data['species'] = Species(smiles=data['smiles'])\n" +
                "            elif 'adjacency_list' in data:\n" +
                "                data['species'] = Species().from_adjacency_list(data['adjacency_list'])\n" +
                "            elif 'inchi' in data:\n" +
                "                data['species'] = Species(inchi=data['inchi'])\n" +
                "            else:\n" +
                "                raise ValueError('Cannot load ArkaneSpecies from YAML file {0}. Either `smiles`, `adjacency_list`, or '\n" +
                "                                 'InChI must be specified'.format(path))\n" +
                "            # Finally, set the species label so that the special attributes are updated properly\n" +
                "            data['species'].label = data['label']" ;
        CompilationUnit converted = Convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
        Assert.assertEquals(converted.toString(),"public class PyDummyClass {\n" +
                "  void load_yaml(){\n" +
                "    PyTypeError freq_data;\n" +
                "    PyTypeError freq_data;\n" +
                "    if ((class_name != \"ArkaneSpecies\")) {\n" +
                "      throw new KeyError(\"Expected a ArkaneSpecies object, but got {0}\".format(class_name));\n" +
                "    }\n" +
                "    del(data[\"class\"]);\n" +
                "    freq_data=None;\n" +
                "    if (    \"imaginary_frequency\" in data) {\n" +
                "      freq_data=data[\"imaginary_frequency\"];\n" +
                "      del(data[\"imaginary_frequency\"]);\n" +
                "    }\n" +
                "    if (!data[\"is_ts\"]) {\n" +
                "      if (      \"smiles\" in data) {\n" +
                "        data[\"species\"]=Species(data[\"smiles\"]);\n" +
                "      }\n" +
                " else       if (      \"adjacency_list\" in data) {\n" +
                "        data[\"species\"]=Species().from_adjacency_list(data[\"adjacency_list\"]);\n" +
                "      }\n" +
                " else       if (      \"inchi\" in data) {\n" +
                "        data[\"species\"]=Species(data[\"inchi\"]);\n" +
                "      }\n" +
                " else {\n" +
                "        throw new ValueError(\"Cannot load ArkaneSpecies from YAML file {0}. Either `smiles`, `adjacency_list`, or InChI must be specified\".format(path));\n" +
                "      }\n" +
                "      data[\"species\"].label=data[\"label\"];\n" +
                "    }\n" +
                "  }\n" +
                "}\n");


    }

    @Test
    public void testConversion8(){
        String content = "def get_element_mass(input_element, isotope=None):\n" +
                "    \"\"\"\n" +
                "    Returns the mass and z number of the requested isotope for a given element.\n" +
                "    'input_element' can be wither the atomic number (integer) or an element symbol.\n" +
                "    'isotope' is an integer of the atomic z number. If 'isotope' is None, returns the most common isotope.\n" +
                "    Data taken from NIST, https://physics.nist.gov/cgi-bin/Compositions/stand_alone.pl (accessed October 2018)\n" +
                "    \"\"\"\n" +
                "    symbol = None\n" +
                "    number = None\n" +
                "\n" +
                "    if isinstance(input_element, int):\n" +
                "        symbol = symbol_by_number[input_element]\n" +
                "        number = input_element\n" +
                "    elif isinstance(input_element, str):\n" +
                "        symbol = input_element\n" +
                "\n" +
                "    if symbol is None or number is None:\n" +
                "        raise ValueError('Could not identify element {0}'.format(input_element))\n" +
                "\n" +
                "    mass_list = mass_by_symbol[symbol]\n" +
                "\n" +
                "    if isotope is not None:\n" +
                "        # a specific isotope is required\n" +
                "        for iso_mass in mass_list:\n" +
                "            if iso_mass[0] == isotope:\n" +
                "                mass = iso_mass[1]\n" +
                "        else:\n" +
                "            raise ValueError(\"Could not find requested isotope {0} for element {1}\".format(isotope, symbol))\n" ;

        CompilationUnit converted = Convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void testConversion9(){
        String content = "def get_element_mass(input_element, isotope=None):\n" +
                "            if iso_mass[0] in isotope:\n" +
                "                mass = iso_mass[1]\n" ;

        CompilationUnit converted = Convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
        Assert.assertEquals(converted.toString(),"public class PyDummyClass {\n" +
                "  void get_element_mass(){\n" +
                "    PyTypeError mass;\n" +
                "    if (    iso_mass[0] in isotope) {\n" +
                "      mass=iso_mass[1];\n" +
                "    }\n" +
                "  }\n" +
                "}\n");

    }

    @Test
    public void testConversion10(){
        String content = "def get_element_mass(input_element, isotope=None):\n" +
                "    \"\"\"\n" +
                "    Returns the mass and z number of the requested isotope for a given element.\n" +
                "    'input_element' can be wither the atomic number (integer) or an element symbol.\n" +
                "    'isotope' is an integer of the atomic z number. If 'isotope' is None, returns the most common isotope.\n" +
                "    Data taken from NIST, https://physics.nist.gov/cgi-bin/Compositions/stand_alone.pl (accessed October 2018)\n" +
                "    \"\"\"\n" +
                "    symbol = None\n" +
                "    number = None\n" +
                "\n" +
                "    if isinstance(input_element, int):\n" +
                "        symbol = symbol_by_number[input_element]\n" +
                "        number = input_element\n" +
                "    elif isinstance(input_element, str):\n" +
                "        symbol = input_element\n" +
                "        number = next(key+value for key, value in symbol_by_number.items() if value == input_element)\n" +
                "\n" +
                "    if symbol is None or number is None:\n" +
                "        raise ValueError('Could not identify element {0}'.format(input_element))\n" +
                "\n" +
                "    mass_list = mass_by_symbol[symbol]\n" +
                "\n" +
                "    if isotope is not None:\n" +
                "        # a specific isotope is required\n" +
                "        for iso_mass in mass_list:\n" +
                "            if iso_mass[0] == isotope:\n" +
                "                mass = iso_mass[1]\n" +
                "                break\n" +
                "        else:\n" +
                "            raise ValueError(\"Could not find requested isotope {0} for element {1}\".format(isotope, symbol))\n" +
                "    else:\n" +
                "        # no specific isotope is required\n" +
                "        if len(mass_list[0]) == 2:\n" +
                "            # isotope weight is unavailable, use the first entry\n" +
                "            mass = mass_list[0][1]\n" +
                "            number = next(key for value in symbol_by_number.items() if value == input_element)\n" +
                "            logging.warning('Assuming isotope {0} is representative of element {1}'.format(mass_list[0][0], symbol))\n" +
                "        else:\n" +
                "            # use the most common isotope\n" +
                "            max_weight = mass_list[0][2]\n" +
                "            mass = mass_list[0][1]\n" +
                "            for iso_mass in mass_list:\n" +
                "                if iso_mass[2] > max_weight:\n" +
                "                    max_weight = iso_mass[2]\n" +
                "                    mass = iso_mass[1]\n" +
                "    return mass, number";
        CompilationUnit converted = Convert(content);
        Assert.assertEquals(converted.getProblems().length,0);

    }

    @Test
    public void testConversion11(){  //Tuple
        String content = "def get_element_mass(input_element, isotope=None):\n" +
                "            if iso_mass[0] in isotope:\n" +
                "                goo,1,goo.loo()=iso_mass[1]   \n" ;
        CompilationUnit converted = Convert(content);

        Assert.assertEquals(converted.getProblems().length,0);
        Assert.assertEquals(converted.toString(),"public class PyDummyClass {\n" +
                "  void get_element_mass(){\n" +
                "    if (    iso_mass[0] in isotope) {\n" +
                "      (      (goo.loo() pyjavatuple 1 pyjavatuple goo))=iso_mass[1];\n" +
                "    }\n" +
                "  }\n" +
                "}\n");
    }

    @Test
    public void testConversion12(){
//
//        String content = "def get_element_mass(input_element, isotope=None):\n" +
//                "\"\"\"\n" +
//                "    Returns the mass and z number of the requested isotope for a given element.\n" +
//                "    'input_element' can be wither the atomic number (integer) or an element symbol.\n" +
//                "    'isotope' is an integer of the atomic z number. If 'isotope' is None, returns the most common isotope.\n" +
//                "    Data taken from NIST, https://physics.nist.gov/cgi-bin/Compositions/stand_alone.pl (accessed October 2018)\n" +
//                "    \"\"\"\n" +
//                "symbol = None\n" +
//                "number = None\n" +
//                "if isinstance(input_element, int):\n" +
//                "symbol = symbol_by_number[input_element]\n" +
//                "number = input_element\n" +
//                "elif isinstance(input_element, str):\n" +
//                "symbol = input_element\n" +
//                "number = next(key+value for key, value in symbol_by_number.items() if value == input_element)\n" +
//                "if symbol is None or number is None:\n" +
//                "raise ValueError('Could not identify element {0}'.format(input_element))"+
//                "}}";
//        CompilationUnit converted = Convert(content);
//
//        Assert.assertEquals(converted.getProblems().length,0);
        String check = "public class PyDummyClass {\n" +
                "  void get_element_mass(){\n" +
                "    PyTypeError number;\n" +
                "    PyTypeError symbol;\n" +
                "    PyTypeError symbol;\n" +
                "    PyTypeError number;\n" +
                "    PyTypeError symbol;\n" +
                "    PyTypeError number;\n" +
                "    symbol=None;\n" +
                "    number=None;\n" +
                "    if (isinstance(input_element,integer)) {\n" +
                "      symbol=symbol_by_number[input_element];\n" +
                "      number=input_element;\n" +
                "    }\n" +
                " else     if (isinstance(input_element,str)) {\n" +
                "      symbol=input_element;\n" +
                "      number=next(      gen (key + value pyjavatuple value for      DummyTerminalTypeNode DummyTerminalNode,      PyTypeError iso_mass1,      PyTypeError iso_mass2 : symbol_by_number.items() if (value == input_element)) );\n" +
                "      number=next(      gen (key + value pyjavatuple value for      DummyTerminalTypeNode DummyTerminalNode,      PyTypeError iso_mass1,      PyTypeError iso_mass2 : symbol_by_number.items() if (value == input_element)) );\n" +
                "      number=next(      gen (key for       DummyTerminalTypeNode DummyTerminalNode,      PyTypeError key,      PyTypeError value : symbol_by_number.items() if (value == input_element)) );"+
                "      if ((symbol == None) || (number == None)) {\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";
        CompilationUnit cu = (CompilationUnit)JavaASTUtil.parseSource(check);
//        "number = (key for key in symbol_by_number.items() if value > input_element);"+
        Assert.assertEquals(cu.getProblems().length,0);
    }


    @Test
    public void testConversion13(){  //Tuple
        String content = "def get_element_mass(input_element, isotope=None):\n" +
                                "y = [x_i + np.random.rand() for x_i in x]\n" ;
        CompilationUnit converted = Convert(content);

        Assert.assertEquals(converted.getProblems().length,0);

    }

    @Test
    public void testConversion14(){  //Tuple
        String content = "def clean_dir(base_dir_path: str = '',\n" +
                "              files_to_delete: List[str] = None,\n" +
                "              file_extensions_to_delete: List[str] = None,\n" +
                "              files_to_keep: List[str] = None,\n" +
                "              sub_dir_to_keep: List[str] = None,\n" +
                "              ) -> None:\n" +
                "    \"\"\"\n" +
                "    Clean up a directory. Commonly used for removing unwanted files after unit tests.\n" +
                "\n" +
                "    Args:\n" +
                "        base_dir_path (str): absolute path of the directory to clean up.\n" +
                "        files_to_delete (list[str]): full name of the file (includes extension) to delete.\n" +
                "        file_extensions_to_delete: extensions of files to delete.\n" +
                "        files_to_keep: full name of the file (includes extension) to keep, files specified here will NOT be deleted even\n" +
                "                       if its extension is also in file_extensions_to_delete.\n" +
                "        sub_dir_to_keep: name of the subdirectories in the base directory to keep.\n" +
                "    \"\"\"\n" +
                "    for item in os.listdir(base_dir_path):\n" +
                "        item_path = os.path.join(base_dir_path, item)\n" +
                "        if os.path.isfile(item_path):\n" +
                "            item_extension = os.path.splitext(item_path)[-1]\n" +
                "            if item in files_to_delete or (item_extension in file_extensions_to_delete and item not in files_to_keep):\n" +
                "                os.remove(item_path)\n" +
                "        else:\n" +
                "            # item is sub-directory\n" +
                "            if os.path.split(item_path)[-1] in sub_dir_to_keep:\n" +
                "                continue\n" +
                "            shutil.rmtree(item_path)" ;
        CompilationUnit converted = Convert(content);

        Assert.assertEquals(converted.getProblems().length,0);

    }

}
