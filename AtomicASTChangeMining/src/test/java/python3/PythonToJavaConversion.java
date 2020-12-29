package python3;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.internal.compiler.parser.Parser;
import org.testng.Assert;
import org.testng.annotations.Test;
import python3.typeinference.PyASTVisitor;
import python3.typeinference.core.TypeASTNode;
import utils.JavaASTUtil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

public class PythonToJavaConversion {
    private static org.apache.log4j.Logger log = Logger.getLogger(LineCounterTestASTVisitor.class);
    @Test
    public void testIfExpression1(){
        String content = "def add_arrays():\n" +
                "    self.is_ts = is_ts if is_ts is not None and is_ts is None  else isinstance(coo,boo)";
        CompilationUnit converted = Convert(content);
        Assert.assertEquals(converted.toString(),
                "public class PyDummyClass1 {\n" +
                        "  void add_arrays(){\n" +
                        "    self.is_ts=((is_ts != None) && (is_ts == None)) ? is_ts : isinstance(coo,boo);\n" +
                        "  }\n" +
                        "}\n");
    }

    @Test
    public void testIfExpression2(){
        String content = "def add_arrays():\n" +
                "    self.is_ts = is_ts if is_ts is not None or is_ts is None  else isinstance(coo,boo)";
        CompilationUnit converted = Convert(content);
        Assert.assertEquals(converted.toString(),
                "public class PyDummyClass1 {\n" +
                        "  void add_arrays(){\n" +
                        "    self.is_ts=((is_ts != None) || (is_ts == None)) ? is_ts : isinstance(coo,boo);\n" +
                        "  }\n" +
                        "}\n");
    }

    @Test
    public void testIfExpression3(){

        String content = "def add_arrays():\n" +
                "    self.is_ts = is_ts if is_ts is not None and is_ts is None and 1!=4 or foo<kool  else isinstance(coo,boo)";
        CompilationUnit converted = Convert(content);
        Assert.assertEquals(converted.toString(),
                "public class PyDummyClass1 {\n" +
                        "  void add_arrays(){\n" +
                        "    self.is_ts=(((is_ts != None) && (is_ts == None) && (1 != 4)) || (foo < kool)) ? is_ts : isinstance(coo,boo);\n" +
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
                "  void ArkaneSpecies_1(){\n" +
                "    i=12345;\n" +
                "  }\n" +
                "  void __init__(  ArkaneSpecies self){\n" +
                "    PyTypeError boo;\n" +
                "    super(ArkaneSpecies,self);\n" +
                "    if ((conformer != None)) {\n" +
                "      self.conformer=conformer;\n" +
                "    }\n" +
                " else     if (((label == None) && (species != None))) {\n" +
                "      boo=noo;\n" +
                "    }\n" +
                " else {\n" +
                "      self.label=label;\n" +
                "    }\n" +
                "    if (((label == None) && (species != None))) {\n" +
                "      self.label=species.label;\n" +
                "    }\n" +
                " else {\n" +
                "      self.label=label;\n" +
                "    }\n" +
                "    if (((species == None) && (conformer == None))) {\n" +
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
                "    self.is_ts=(((one != None) && voo && cool) || tool) ? is_ts : isinstance(coo,boo);\n" +
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
        Assert.assertEquals(converted.toString(),"public class PyDummyClass1 {\n" +
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
        Assert.assertEquals(converted.toString(),"public class PyDummyClass1 {\n" +
                "  void load_yaml(){\n" +
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
        Assert.assertEquals(converted.toString(),"public class PyDummyClass1 {\n" +
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
        Assert.assertEquals(converted.toString(),"public class PyDummyClass1 {\n" +
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
                "      number=next(      gen (key for       DummyTerminalTypeNode DummyTerminalNode,      PyTypeError key,      PyTypeError value : symbol_by_number.items()) );"+
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
//                                "   y = [x_i + np.random.rand() for x_i in x]\n"+
                                "   filename = join(c.foo for c in label if goo==hoo )";
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
        Assert.assertEquals(Arrays.stream(converted.toString().split("\n")).skip(4).collect(Collectors.joining( "\n" )),
                "    for (    PyTypeError item : os.listdir(base_dir_path)) {\n" +
                "      item_path=os.path.join(base_dir_path,item);\n" +
                "      if (os.path.isfile(item_path)) {\n" +
                "        item_extension=os.path.splitext(item_path)[-1];\n" +
                "        if ((        item in files_to_delete || (        item_extension in file_extensions_to_delete &&         item not in files_to_keep))) {\n" +
                "          os.remove(item_path);\n" +
                "        }\n" +
                "      }\n" +
                " else {\n" +
                "        if (        os.path.split(item_path)[-1] in sub_dir_to_keep) {\n" +
                "          continue;\n" +
                "        }\n" +
                "        shutil.rmtree(item_path);\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}");

    }

    @Test
    public void testConversion15(){
        String content = readFile("common.py");
        CompilationUnit converted = Convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void testConversion16(){
        String content = "mass_by_symbol = {\n" +
                "    'Og': [[294.78, 294.21392]]}\n" +
                "def get_center_of_mass(coords, numbers=None, symbols=None):\n" +
                "    \"\"\"\n" +
                "    Calculate and return the 3D position of the center of mass of the current geometry.\n" +
                "    Either ``numbers`` or ``symbols`` must be given.\n" +
                "\n" +
                "    Args:\n" +
                "        coords (np.array): Entries are 3-length lists of xyz coordinates for an atom.\n" +
                "        numbers (np.array, list): Entries are atomic numbers corresponding to coords.\n" +
                "        symbols (list): Entries are atom symbols corresponding to coords.\n" +
                "\n" +
                "    Returns:\n" +
                "        np.array: The center of mass coordinates.\n" +
                "    \"\"\"\n" +
                "    number = next(key+value for key, value in symbol_by_number.items())\n" +
                "    if symbols is None and numbers is None:\n" +
                "        raise IndexError('Either symbols or numbers must be given.')\n" +
                "    if numbers is not None:\n" +
                "        symbols = [symbol_by_number[number] for number in numbers]\n" +
                "    center, total_mass = np.zeros(3, np.float64), 0\n" +
                "    number = next(key+value for key, value in symbol_by_number)\n" +
                "    for coord, symbol in zip(coords, symbols):\n" +
                "        mass = get_element_mass(symbol)[0]\n" +
                "        center += mass * coord\n" +
                "        total_mass += mass\n" +
                "    center /= total_mass\n" +
                "    number = next(key+value for key, value in symbol_by_number.items() if value == input_element)\n" +
                "    return center";
        CompilationUnit converted = Convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
        Assert.assertEquals(Arrays.stream(converted.toString().split("\n")).skip(5).collect(Collectors.joining( "\n" )),"    number=next(    gen ((key + value)     for     PyTypeError value,    PyTypeError key : symbol_by_number.items()) );\n" +
                "    if (((symbols == None) && (numbers == None))) {\n" +
                "      throw new IndexError(\"Either symbols or numbers must be given.\");\n" +
                "    }\n" +
                "    if ((numbers != None)) {\n" +
                "      symbols=      listc (symbol_by_number[number]       for       PyTypeError number : numbers) ;\n" +
                "    }\n" +
                "    (    (total_mass pyjavatuple center))=(    (0 pyjavatuple np.zeros(3,np.float64)));\n" +
                "    number=next(    gen ((key + value)     for     PyTypeError value,    PyTypeError key : symbol_by_number) );\n" +
                "    for (    PyTypeError coord,    PyTypeError symbol : zip(coords,symbols)) {\n" +
                "      mass=get_element_mass(symbol)[0];\n" +
                "      center+=(mass * coord);\n" +
                "      total_mass+=mass;\n" +
                "    }\n" +
                "    center/=total_mass;\n" +
                "    number=next(    gen ((key + value)     for     PyTypeError value,    PyTypeError key : symbol_by_number.items() if (value == input_element)) );\n" +
                "    return center;\n" +
                "  }\n" +
                "  public static Map<String,float[][]> mass_by_symbol=Map.of(\"Og\",new float[][]{{294.78,294.21392}});\n" +
                "}");
    }

    @Test
    public void testConversion17(){
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
                "            self.smiles = species.molecule[0].to_smiles()\n" +
                "            self.adjacency_list = species.molecule[0].to_adjacency_list()\n" +
                "            self.charge = species.molecule[0].get_net_charge()\n" +
                "            self.multiplicity = species.molecule[0].multiplicity\n" +
                "            self.formula = species.molecule[0].get_formula()\n" +
                "            try:\n" +
                "                inchi = to_inchi(species.molecule[0], backend='try-all', aug_level=0)\n" +
                "            except ValueError:\n" +
                "                inchi = ''\n" +
                "            try:\n" +
                "                inchi_key = to_inchi_key(species.molecule[0], backend='try-all', aug_level=0)\n" +
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
                "                cp = []\n";

        CompilationUnit converted = Convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
        Assert.assertEquals(Arrays.stream(converted.toString().split("\n")).skip(9).collect(Collectors.joining( "\n" )),"    if ((species == None)) {\n" +
                "      throw new ValueError(\"No species was passed to ArkaneSpecies\");\n" +
                "    }\n" +
                "    self.label=(self.label || species.label);\n" +
                "    if (isinstance(species,TransitionState)) {\n" +
                "      self.imaginary_frequency=species.frequency;\n" +
                "      if ((species.conformer != None)) {\n" +
                "        self.conformer=species.conformer;\n" +
                "        self.xyz=self.update_xyz_string();\n" +
                "      }\n" +
                "    }\n" +
                " else     if (((species.molecule != None) && (len(species.molecule) > 0))) {\n" +
                "      self.smiles=species.molecule[0].to_smiles();\n" +
                "      self.adjacency_list=species.molecule[0].to_adjacency_list();\n" +
                "      self.charge=species.molecule[0].get_net_charge();\n" +
                "      self.multiplicity=species.molecule[0].multiplicity;\n" +
                "      self.formula=species.molecule[0].get_formula();\n" +
                "      try {\n" +
                "        inchi=to_inchi(species.molecule[0],\"try-all\",0);\n" +
                "      }\n" +
                " catch (      ValueError PyCpatDummy) {\n" +
                "        inchi=\"\";\n" +
                "      }\n" +
                "      try {\n" +
                "        inchi_key=to_inchi_key(species.molecule[0],\"try-all\",0);\n" +
                "      }\n" +
                " catch (      ValueError PyCpatDummy) {\n" +
                "        inchi_key=\"\";\n" +
                "      }\n" +
                "      self.inchi=inchi;\n" +
                "      self.inchi_key=inchi_key;\n" +
                "      if ((species.conformer != None)) {\n" +
                "        self.conformer=species.conformer;\n" +
                "        self.xyz=self.update_xyz_string();\n" +
                "      }\n" +
                "      self.molecular_weight=species.molecular_weight;\n" +
                "      if ((species.symmetry_number != -1)) {\n" +
                "        self.symmetry_number=species.symmetry_number;\n" +
                "      }\n" +
                "      if ((species.transport_data != None)) {\n" +
                "        self.transport_data=species.transport_data;\n" +
                "      }\n" +
                "      if ((species.energy_transfer_model != None)) {\n" +
                "        self.energy_transfer_model=species.energy_transfer_model;\n" +
                "      }\n" +
                "      if ((species.thermo != None)) {\n" +
                "        self.thermo=species.thermo.as_dict();\n" +
                "        data=species.get_thermo_data();\n" +
                "        h298=(data.get_enthalpy(298) / 4184.0);\n" +
                "        s298=(data.get_entropy(298) / 4.184);\n" +
                "        temperatures=np.array(new int[]{300,400,500,600,800,1000,1500,2000,2400});\n" +
                "        cp=new Any[]{};\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}");



    }
    @Test
    public void testConversion18(){

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
                "            self.smiles = species.molecule[0].to_smiles()\n" +
                "            self.adjacency_list = species.molecule[0].to_adjacency_list()\n" +
                "            self.charge = species.molecule[0].get_net_charge()\n" +
                "            self.multiplicity = species.molecule[0].multiplicity\n" +
                "            self.formula = species.molecule[0].get_formula()\n" +
                "            try:\n" +
                "                inchi = to_inchi(species.molecule[0], backend='try-all', aug_level=0)\n" +
                "            except ValueError:\n" +
                "                inchi = ''\n" +
                "            try:\n" +
                "                inchi_key = to_inchi_key(species.molecule[0], backend='try-all', aug_level=0)\n" +
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
                "                for t in temperatures:\n" +
                "                    cp.append(data.get_heat_capacity(t) / 4.184)\n" +
                "\n" +
                "                self.thermo_data = ThermoData(H298=(h298, 'kcal/mol'),\n" +
                "                                              S298=(s298, 'cal/(mol*K)'),\n" +
                "                                              Tdata=(temperatures, 'K'),\n" +
                "                                              Cpdata=(cp, 'cal/(mol*K)'),\n" +
                "                                              )";
        CompilationUnit converted = Convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void testConversion19(){
        String content = "i = 1\n" +
                "while i > 3:\n" +
                "                if network.get_leak_coefficient(T=temperature, P=pressure) > self.explore_tol * kchar:\n" +

                "                    bath_gas = {species_dict[spec]: fraction for spec, fraction in bathGas.items()} if bathGas else None\n" ;
        CompilationUnit converted = Convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void testConversion20(){
        String content = "def explorer(source, explore_tol=0.01, energy_tol=np.inf, flux_tol=0.0, bathGas=None, maximumRadicalElectrons=np.inf):\n" +
                "    \"\"\"Generate an explorer job\"\"\"\n" +
                "    global job_list, species_dict\n" +
                "    for job in job_list:\n" +
                "        if isinstance(job, PressureDependenceJob):\n" +
                "            pdepjob = job\n" +
                "            break\n" +
                "    else:\n" +
                "        raise InputError('the explorer block must occur after the pressureDependence block')\n" +
                "\n" +
                "    source = [species_dict[name] for name in source]\n" +
                "\n" +
                "    bath_gas = {species_dict[spec]: fraction for spec, fraction in bathGas.items()} if bathGas else None\n" +
                "    bath_gas1 = next(_id for _id, _names in _valid_software_names.items())\n"+
                "\n" +
                "    job = ExplorerJob(source=source, pdepjob=pdepjob, explore_tol=explore_tol,\n" +
                "                      energy_tol=energy_tol, flux_tol=flux_tol, bath_gas=bath_gas,\n" +
                "                      maximum_radical_electrons=maximumRadicalElectrons)\n" +
                "    job_list.append(job)"+
                "    for coord, symbol in zip(coords, symbols):\n" +
                "        mass = get_element_mass(symbol)[0]\n" +
                "        center += mass * coord\n" +
                "        total_mass += mass\n";
        CompilationUnit converted = Convert(content);
        Assert.assertEquals(converted.getProblems().length,0);

        int boo = 4^2;

    }

    @Test
    public void testConversion21(){
        String check = "public class Main {\n" +
                "  public static void main(String[ ] args) {\n" +
                "    try {\n" +
                "      int[] myNumbers = {1, 2, 3};\n" +
                "      System.out.println(myNumbers[10]);\n" +
                "    } catch (Exception e) {\n" +
                "      System.out.println(\"Something went wrong.\");\n" +
                "    }\n" +
                "     else{\n"+
                "           boo=3;\n"+
                "       }\n"+
                "  }\n" +
                "}";
//        _software_ids = {_name: _id for _id, _names in _valid_software_names.items() for _name in _names}
        CompilationUnit cu = (CompilationUnit)JavaASTUtil.parseSource(check);
//        "number = (key for key in symbol_by_number.items() if value > input_element);"+
        Assert.assertEquals(cu.getProblems().length,0);
    }

    @Test
    public void testConversion22(){
        String content = "def draw(self, reaction, file_format, path=None):\n" +
                        "        for i in range(n_left, -1, -1):\n" +
                                        "            top = well_rects[i][1]\n" +
                                        "            bottom = top + well_rects[i][3]\n" +
                                        "            for column in columns:\n" +
                                        "                for c in column:\n" +
                                        "                    top0 = well_rects[c][1]\n" +
                                        "                    bottom0 = top + well_rects[c][3]\n" +
                                        "                    if (top0 <= top <= bottom0) or (top <= top0 <= bottom):\n" +
                                        "                        # Can't put it in this column\n" +
                                        "                        break\n" +
                                        "                else:\n" +
                                        "                    # Can put it in this column\n" +
                                        "                    column.append(i)\n" +
                                        "                    break";
        CompilationUnit converted = Convert(content);
//        "number = (key for key in symbol_by_number.items() if value > input_element);"+
        Assert.assertEquals(converted.getProblems().length,0);
        Assert.assertEquals(Arrays.stream(converted.toString().split("\n")).skip(6).collect(Collectors.joining( "\n" )),"    for (    PyTypeError i : range(n_left,-1,-1)) {\n" +
                "      top=well_rects[i][1];\n" +
                "      bottom=(top + well_rects[i][3]);\n" +
                "      for (      PyTypeError column : columns) {\n" +
                "        for (        PyTypeError c : column) {\n" +
                "          top0=well_rects[c][1];\n" +
                "          bottom0=(top + well_rects[c][3]);\n" +
                "          if (((top0 <= top && top <= bottom0) || (top <= top0 && top0 <= bottom))) {\n" +
                "            break;\n" +
                "          }\n" +
                "        }\n" +
                "        else{\n" +
                "          column.append(i);\n" +
                "          break;\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}");
    }

    @Test
    public void testConversion23(){
        String content =
                "def get_software_id(name: str):\n" +
                        "    try:\n" +
                        "        return _software_ids[standardize_name(name)]\n" +
                        "    except KeyError:\n" +
                        "        raise ValueError(f'\\\"{name}\\\" is an invalid quantum chemistry software')\n" +
                        "        _software_ids = [_name for _id, _names in _valid_software_names.items() for _name in _names if goo2]\n" +
                        "    finally:\n" +
                        "        System.out.println(\"The 'try catch' is finished.\");\n" +
                        "    "  ;
        CompilationUnit converted = Convert(content);
//        _software_ids = [_name for _id, _names in _valid_software_names.items() for _name in _names if goo]
        Assert.assertEquals(converted.getProblems().length,0);

        Assert.assertEquals(Arrays.stream(converted.toString().split("\n")).skip(3).collect(Collectors.joining( "\n" )),"    try {\n" +
                "      return _software_ids[standardize_name(name)];\n" +
                "    }\n" +
                " catch (    KeyError PyCpatDummy) {\n" +
                "      throw new ValueError();\n" +
                "      _software_ids=      listc (_name       for       PyTypeError _names,      PyTypeError _id : _valid_software_names.items() if DUMMY_IF       for       PyTypeError _name : _names if goo2) ;\n" +
                "    }\n" +
                " finally {\n" +
                "      System.out.println(\"The 'try catch' is finished.\");\n" +
                "    }\n" +
                "  }\n" +
                "}");
    }


    @Test
    public void testConversion24(){
        String content =
                "def get_software_id(name: str):\n" +
                        "    try:\n" +
                        "        return _software_ids[standardize_name(name)]\n" +
                        "    except KeyError:\n" +
                        "        raise ValueError(f'\\\"{name}\\\" is an invalid quantum chemistry software')\n" +
                        "        _software_ids = [[i for i in range(5)] for _ in range(6)]" ;
        CompilationUnit converted = Convert(content);

        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void testConversion25(){  //TODO support ::n_samples + 1
        String content =
                "def get_software_id(name: str):\n" +
                        "        tmp -= np.sum(tmp, axis=0)\n" +
                        "        dA = np.diag(tmp).copy()\n" +
                        "        tmp.clip(0, np.inf, tmp)\n" +
                        "        tmp.flat[::n_samples + 1] = dA" ;
        CompilationUnit converted = Convert(content);

        Assert.assertEquals(converted.getProblems().length,0);
    }


    @Test
    public  void testConversion26(){
        String content = "def _iterate_sparse_X(X):\n" +
                "    \"\"\"This little hack returns a densified row when iterating over a sparse\n" +
                "    matrix, instead of constructing a sparse matrix for every row that is\n" +
                "    expensive.\n" +
                "    \"\"\"\n" +
                "    n_samples = X.shape[0]\n" +
                "    X_indices = X.indices\n" +
                "    X_data = X.data\n" +
                "    X_indptr = X.indptr\n" +
                "\n" +
                "    for i in range(n_samples):\n" +
                "        row = np.zeros(X.shape[1])\n" +
                "        startptr, endptr = X_indptr[i], X_indptr[i + 1]\n" +
                "        nonzero_indices = X_indices[startptr:endptr]\n" +
                "        row[nonzero_indices] = X_data[startptr:endptr]\n" +
                "        yield row";
        CompilationUnit converted = Convert(content);

        Assert.assertEquals(converted.getProblems().length,0);

        Assert.assertEquals(Arrays.stream(converted.toString().split("\n")).skip(8).collect(Collectors.joining( "\n" )),"    n_samples=X.shape[0];\n" +
                "    X_indices=X.indices;\n" +
                "    X_data=X.data;\n" +
                "    X_indptr=X.indptr;\n" +
                "    for (    PyTypeError i : range(n_samples)) {\n" +
                "      row=np.zeros(X.shape[1]);\n" +
                "      (      (endptr pyjavatuple startptr))=(      (X_indptr[(i + 1)] pyjavatuple X_indptr[i]));\n" +
                "      nonzero_indices=X_indices[\"startptr_PyCpatDummy__endptr\"];\n" +
                "      row[nonzero_indices]=X_data[\"startptr_PyCpatDummy__endptr\"];\n" +
                "      yieldr row;\n" +
                "    }\n" +
                "  }\n" +
                "}");
    }

    @Test
    public void testConversion27(){  //TODO support ::n_samples + 1
        String content =
                "def get_software_id(name: str):\n" +
                        "       csr_linestyle = (0, (3, 1, 1, 1, 1, 1))  # densely dashdotdotted\n" +
                        "       dense_linestyle = (0, ())  # solid" ;
        CompilationUnit converted = Convert(content);

        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void testConversion28(){
        String content = "def divide(x, y):\n" +
                "    try:\n" +
                "        result = x / y\n" +
                "    except ZeroDivisionError:\n" +
                "        print(\"division by zero!\")\n" +
                "    else:\n" +
                "        print(\"result is\", result)\n" +
                "    finally:\n" +
                "        print(\"executing finally clause\")";
        CompilationUnit converted = Convert(content);

        Assert.assertEquals(converted.getProblems().length,0);
        Assert.assertEquals(Arrays.stream(converted.toString().split("\n")).skip(3).collect(Collectors.joining( "\n" )),"    try {\n" +
                "      result=(x / y);\n" +
                "    }\n" +
                " catch (    ZeroDivisionError PyCpatDummy) {\n" +
                "      print(\"division by zero!\");\n" +
                "    }\n" +
                " finally {\n" +
                "      print(\"result is\",result);\n" +
                "    }\n" +
                " else {\n" +
                "      print(\"executing finally clause\");\n" +
                "    }\n" +
                "  }\n" +
                "}");

    }

    @Test
    public void testConversion29(){
        String content = "def execute(self, output_file, plot, file_format='pdf', print_summary=True):\n" +
                "        \"\"\"Execute a PressureDependenceJob\"\"\"\n" +
                "        for config in self.network.isomers + self.network.reactants + self.network.products:\n" +
                "            for spec in config.species:\n" +
                "                if spec.conformer.E0 is None:\n" +
                "                    raise AttributeError('species {0} is missing energy for its conformer'.format(spec.label))\n" +
                "\n" +
                "        # set transition state Energy if not set previously using same method as RMG pdep\n" +
                "        for reaction in self.network.path_reactions:\n" +
                "            transition_state = reaction.transition_state\n" +
                "            if transition_state.conformer and transition_state.conformer.E0 is None:\n" +
                "                transition_state.conformer.E0 = (sum([spec.conformer.E0.value_si for spec in reaction.reactants])\n" +
                "                                                 + reaction.kinetics.Ea.value_si, 'J/mol')\n" +
                "                logging.info('Approximated transitions state E0 for reaction {3} from kinetics '\n" +
                "                             'A={0}, n={1}, Ea={2} J/mol'.format(reaction.kinetics.A.value_si,\n" +
                "                                                                 reaction.kinetics.n.value_si,\n" +
                "                                                                 reaction.kinetics.Ea.value_si, reaction.label))\n" +
                "        if print_summary:\n" +
                "            self.network.log_summary()\n" +
                "\n" +
                "        if output_file is not None:\n" +
                "            self.draw(os.path.dirname(output_file), file_format)\n" +
                "\n" +
                "        self.initialize()\n" +
                "\n" +
                "        self.K = self.network.calculate_rate_coefficients(self.Tlist.value_si, self.Plist.value_si, self.method)\n" +
                "\n" +
                "        self.fit_interpolation_models()\n" +
                "\n" +
                "        if output_file is not None:\n" +
                "            self.save(output_file)\n" +
                "            if plot:\n" +
                "                self.plot(os.path.dirname(output_file))\n" +
                "            if self.sensitivity_conditions is not None:\n" +
                "                perturbation = 0.1  # kcal/mol\n" +
                "                logging.info('\\n\\nRunning sensitivity analysis...')\n" +
                "                for i in range(3):\n" +
                "                    try:\n" +
                "                        SensAnalysis(self, os.path.dirname(output_file), perturbation=perturbation)\n" +
                "                    except (InvalidMicrocanonicalRateError, ModifiedStrongCollisionError) as e:\n" +
                "                        logging.warning('Could not complete the sensitivity analysis with a perturbation of {0} '\n" +
                "                                        'kcal/mol, trying {1} kcal/mol instead.'.format(\n" +
                "                            perturbation, perturbation / 2.0))\n" +
                "                        perturbation /= 2.0\n" +
                "                    else:\n" +
                "                        break\n" +
                "                else:\n" +
                "                    logging.error(\"Could not complete the sensitivity analysis even with a perturbation of {0}\"\n" +
                "                                  \" kcal/mol\".format(perturbation))\n" +
                "                    raise e\n" +
                "                logging.info(\"Completed the sensitivity analysis using a perturbation of {0} kcal/mol\".format(\n" +
                "                    perturbation))\n" +
                "        logging.debug('Finished pdep job for reaction {0}.'.format(self.network.label))\n" +
                "        logging.debug(repr(self.network))";
        CompilationUnit converted = Convert(content);

        Assert.assertEquals(converted.getProblems().length,0);
        Assert.assertEquals(Arrays.stream(converted.toString().split("\n")).skip(4).collect(Collectors.joining( "\n" )),"    for (    PyTypeError config : ((self.network.isomers + self.network.reactants) + self.network.products)) {\n" +
                "      for (      PyTypeError spec : config.species) {\n" +
                "        if ((spec.conformer.E0 == None)) {\n" +
                "          throw new AttributeError(\"species {0} is missing energy for its conformer\".format(spec.label));\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "    for (    PyTypeError reaction : self.network.path_reactions) {\n" +
                "      transition_state=reaction.transition_state;\n" +
                "      if ((transition_state.conformer && (transition_state.conformer.E0 == None))) {\n" +
                "        transition_state.conformer.E0=(        (\"J/mol\" pyjavatuple (sum(        listc (spec.conformer.E0.value_si         for         PyTypeError spec : reaction.reactants) ) + reaction.kinetics.Ea.value_si)));\n" +
                "        logging.info(\"Approximated transitions state E0 for reaction {3} from kinetics A={0}, n={1}, Ea={2} J/mol\".format(reaction.kinetics.A.value_si,reaction.kinetics.n.value_si,reaction.kinetics.Ea.value_si,reaction.label));\n" +
                "      }\n" +
                "    }\n" +
                "    if (print_summary) {\n" +
                "      self.network.log_summary();\n" +
                "    }\n" +
                "    if ((output_file != None)) {\n" +
                "      self.draw(os.path.dirname(output_file),file_format);\n" +
                "    }\n" +
                "    self.initialize();\n" +
                "    self.K=self.network.calculate_rate_coefficients(self.Tlist.value_si,self.Plist.value_si,self.method);\n" +
                "    self.fit_interpolation_models();\n" +
                "    if ((output_file != None)) {\n" +
                "      self.save(output_file);\n" +
                "      if (plot) {\n" +
                "        self.plot(os.path.dirname(output_file));\n" +
                "      }\n" +
                "      if ((self.sensitivity_conditions != None)) {\n" +
                "        perturbation=0.1;\n" +
                "        logging.info(\"\\n\\nRunning sensitivity analysis...\");\n" +
                "        for (        PyTypeError i : range(3)) {\n" +
                "          try {\n" +
                "            SensAnalysis(self,os.path.dirname(output_file),perturbation);\n" +
                "          }\n" +
                " catch (          InvalidMicrocanonicalRateError|ModifiedStrongCollisionError e) {\n" +
                "            logging.warning(\"Could not complete the sensitivity analysis with a perturbation of {0} kcal/mol, trying {1} kcal/mol instead.\".format(perturbation,(perturbation / 2.0)));\n" +
                "            perturbation/=2.0;\n" +
                "          }\n" +
                " else {\n" +
                "            break;\n" +
                "          }\n" +
                "        }\n" +
                "        else{\n" +
                "          logging.error(\"Could not complete the sensitivity analysis even with a perturbation of {0} kcal/mol\".format(perturbation));\n" +
                "          throw new PythonException();\n" +
                "        }\n" +
                "        logging.info(\"Completed the sensitivity analysis using a perturbation of {0} kcal/mol\".format(perturbation));\n" +
                "      }\n" +
                "    }\n" +
                "    logging.debug(\"Finished pdep job for reaction {0}.\".format(self.network.label));\n" +
                "    logging.debug(repr(self.network));\n" +
                "  }\n" +
                "}");
    }

    @Test
    public void testConversion30() {
        String content = "def draw(self, reaction, file_format, path=None):\n" +
                "        for i in range(n_left, -1, -1):\n" +
                "            top = well_rects[i][1]\n" +
                "            bottom = top + well_rects[i][3]\n" +
                "            for column, row2 in columns:\n" +
                "                for c in column:\n" +
                "                    top0 = well_rects[c][1]\n" +
                "                    bottom0 = top + well_rects[c][3]\n" +
                "                    if (top0 <= top <= bottom0) or (top <= top0 <= bottom):\n" +
                "                        # Can't put it in this column\n" +
                "                        break\n" +
                "                else:\n" +
                "                    # Can put it in this column\n" +
                "                    column.append(i)\n" +
                "                    break";
        CompilationUnit converted = Convert(content);
        Assert.assertEquals(converted.getProblems().length,0);
    }

    @Test
    public void testConversion31(){
        String check = "public class PyDummyClass1 {\n" +
                "  void explorer(){\n" +
                "    bath_gas1=next(    gen (_id for     DummyTerminalTypeNode DummyTerminalNode,    PyTypeError _id,    PyTypeError _names : valid_software_names.items() if goo1>5 for     DummyTerminalTypeNode DummyTerminalNode,    PyTypeError _id,    PyTypeError _names : _valid_software_names.items() if goo>5) );\n" +
                "    job=ExplorerJob(source,pdepjob,explore_tol,energy_tol,flux_tol,bath_gas,maximumRadicalElectrons);\n" +
                "    job_list.append(job);\n" +
                "  }\n" +
                "}\n\n";

        CompilationUnit cu = (CompilationUnit)JavaASTUtil.parseSource(check);
//        "number = (key for key in symbol_by_number.items() if value > input_element);"+
        Assert.assertEquals(cu.getProblems().length,0);

    }

    @Test
    public void testConversion32(){
        String content =
                "def get_software_id(name: str):\n" +
                        "    try:\n" +
                        "        return _software_ids[standardize_name(name)]\n" +
                        "    except KeyError:\n" +
                        "        raise ValueError(f'\\\"{name}\\\" is an invalid quantum chemistry software')\n" +
                        "        _software_ids = [_name  for _id, _names in _valid_software_names.items() if goo & noo for _name in _names]\n" +
                        "    finally:\n" +
                        "        System.out.println(\"The 'try catch' is finished.\");\n";

        CompilationUnit converted = Convert(content);
//        _software_ids = [_name for _id, _names in _valid_software_names.items() for _name in _names if goo]
        Assert.assertEquals(converted.getProblems().length,0);

        Assert.assertEquals(Arrays.stream(converted.toString().split("\n")).skip(3).collect(Collectors.joining( "\n" )),"    try {\n" +
                "      return _software_ids[standardize_name(name)];\n" +
                "    }\n" +
                " catch (    KeyError PyCpatDummy) {\n" +
                "      throw new ValueError();\n" +
                "      _software_ids=      listc (_name       for       PyTypeError _names,      PyTypeError _id : _valid_software_names.items() if (goo & noo)       for       PyTypeError _name : _names) ;\n" +
                "    }\n" +
                " finally {\n" +
                "      System.out.println(\"The 'try catch' is finished.\");\n" +
                "    }\n" +
                "  }\n" +
                "}");
    }


    @Test
    public void testConversion33(){
        String content =
                "def get_software_id(name: str):\n" +
                        "    try:\n" +
                        "        return _software_ids[standardize_name(name)]\n" +
                        "    except KeyError:\n" +
                        "        raise ValueError(f'\\\"{name}\\\" is an invalid quantum chemistry software')\n" +
                        "        _software_ids = [_name for _id, _names in xxx.items() if _id>0 ]\n" +
                        "    finally:\n" +
                        "        System.out.println(\"The 'try catch' is finished.\");\n";

        CompilationUnit converted = Convert(content);
//        _software_ids = [_name for _id, _names in _valid_software_names.items() for _name in _names if goo]
        Assert.assertEquals(converted.getProblems().length,0);

        Assert.assertEquals(Arrays.stream(converted.toString().split("\n")).skip(3).collect(Collectors.joining( "\n" )),"    try {\n" +
                "      return _software_ids[standardize_name(name)];\n" +
                "    }\n" +
                " catch (    KeyError PyCpatDummy) {\n" +
                "      throw new ValueError();\n" +
                "      _software_ids=      listc (_name       for       PyTypeError _names,      PyTypeError _id : xxx.items() if (_id > 0)) ;\n" +
                "    }\n" +
                " finally {\n" +
                "      System.out.println(\"The 'try catch' is finished.\");\n" +
                "    }\n" +
                "  }\n" +
                "}");
    }

    @Test
    public void testConversion34(){
        String content =
                "public class PyDummyClass1 {\n" +
                        "  void get_software_id(){\n" +
                        "    PyTypeError _software_ids;\n" +
                        "    try {\n" +
                        "      return _software_ids[standardize_name(name)];\n" +
                        "    }\n" +
                        " catch (    KeyError PyCpatDummy) {\n" +
                        "      throw new ValueError();\n" +
                        "      _software_ids=      dictc (_name ::: _id for       DummyTerminalTypeNode DummyTerminalNode,      PyTypeError _id,      PyTypeError _names : xxx.items() if (_id && _cool) for DummyTerminalTypeNode DummyTerminalNode, PyTypeError _name : _names) ;\n" +
                        "    }\n" +
                        " finally {\n" +
                        "      System.out.println(\"The 'try catch' is finished.\");\n" +
                        "    }\n" +
                        "  }\n" +
                        "}\n";

        CompilationUnit cu = (CompilationUnit)JavaASTUtil.parseSource(content);
//        "number = (key for key in symbol_by_number.items() if value > input_element);"+
        Assert.assertEquals(cu.getProblems().length,0);
    }

    @Test
    public void testConversion35(){
        String content =
                "def get_software_id(name: str):\n" +
                        "    try:\n" +
                        "        return _software_ids[standardize_name(name)]\n" +
                        "    except KeyError:\n" +
                        "        raise ValueError(f'\\\"{name}\\\" is an invalid quantum chemistry software')\n" +
                        "        _software_ids = {_name:_id  for _id, _names in _valid_software_names.items() if goo and noo for _name in _names}\n" +
                        "    finally:\n" +
                        "        System.out.println(\"The 'try catch' is finished.\");\n";

        CompilationUnit converted = Convert(content);

        Assert.assertEquals(converted.getProblems().length,0);

        Assert.assertEquals(Arrays.stream(converted.toString().split("\n")).skip(3).collect(Collectors.joining( "\n" )),"    try {\n" +
                "      return _software_ids[standardize_name(name)];\n" +
                "    }\n" +
                " catch (    KeyError PyCpatDummy) {\n" +
                "      throw new ValueError();\n" +
                "      _software_ids=      dictc (_id ::: _name       for       PyTypeError _names,      PyTypeError _id : _valid_software_names.items() if (goo && noo)       for       PyTypeError _name : _names) ;\n" +
                "    }\n" +
                " finally {\n" +
                "      System.out.println(\"The 'try catch' is finished.\");\n" +
                "    }\n" +
                "  }\n" +
                "}");

    }

    @Test
    public void testConversion36() {
        String content =
                "def get_software_id(name: str):\n" +
                        "    try:\n" +
                        "        return _software_ids[standardize_name(name)]\n" +
                        "    except KeyError:\n" +
                        "        raise ValueError(f'\\\"{name}\\\" is an invalid quantum chemistry software')\n" +
                        "        _software_ids = {_name for _id, _names in _valid_software_names.items() if goo and noo for _name in _names}\n" +
                        "    finally:\n" +
                        "        System.out.println(\"The 'try catch' is finished.\");\n";

        CompilationUnit converted = Convert(content);

        Assert.assertEquals(converted.getProblems().length, 0);

        Assert.assertEquals(Arrays.stream(converted.toString().split("\n")).skip(3).collect(Collectors.joining( "\n" )),"    try {\n" +
                "      return _software_ids[standardize_name(name)];\n" +
                "    }\n" +
                " catch (    KeyError PyCpatDummy) {\n" +
                "      throw new ValueError();\n" +
                "      _software_ids=      dictc (SET_PYTHON ::: _name       for       PyTypeError _names,      PyTypeError _id : _valid_software_names.items() if (goo && noo)       for       PyTypeError _name : _names) ;\n" +
                "    }\n" +
                " finally {\n" +
                "      System.out.println(\"The 'try catch' is finished.\");\n" +
                "    }\n" +
                "  }\n" +
                "}");
    }

    @Test
    public void testConversion37(){
        String content = "def power(self, power):\n" +
                "        if 0 < power < 1 > goo:\n" +
                "            # Extreme Stable or Normal distribution\n" +
                "            self._lower_bound = DistributionBoundary(-np.Inf, inclusive=False)\n" +
                "        self._power = power";
        CompilationUnit converted = Convert(content);

        Assert.assertEquals(converted.getProblems().length, 0);

        Assert.assertEquals(converted.toString(),"public class PyDummyClass1 {\n" +
                "  void power(){\n" +
                "    if ((0 < power && power < 1 && 1 > goo)) {\n" +
                "      self._lower_bound=DistributionBoundary(-np.Inf,false);\n" +
                "    }\n" +
                "    self._power=power;\n" +
                "  }\n" +
                "}\n");
    }



    @Test
    public void testConversion38(){
        String content = "def power(self, power):\n" +
                "        if 0 < power:\n" +
                "            # Extreme Stable or Normal distribution\n" +
                "            self._lower_bound = DistributionBoundary(-np.Inf, inclusive=False)\n" +
                "        self._power = power";
        CompilationUnit converted = Convert(content);

        Assert.assertEquals(converted.getProblems().length, 0);

        Assert.assertEquals(converted.toString(),"public class PyDummyClass1 {\n" +
                "  void power(){\n" +
                "    if ((0 < power)) {\n" +
                "      self._lower_bound=DistributionBoundary(-np.Inf,false);\n" +
                "    }\n" +
                "    self._power=power;\n" +
                "  }\n" +
                "}\n");
    }

    @Test
    public void testConversion39(){
        String content = "def _synth_regression_dataset(n_samples=100000, n_features=100,\n" +
                "                              dtype=np.float32):\n" +
                "    X, y = make_regression(n_samples=n_samples, n_features=n_features,\n" +
                "                           n_informative=n_features // 10, noise=50,\n" +
                "                           random_state=0)\n" +
                "    X = X.astype(dtype, copy=False)\n" +
                "    X = StandardScaler().fit_transform(X)\n" +
                "\n" +
                "    X, X_val, y, y_val = train_test_split(X, y, test_size=0.1, random_state=0)\n" +
                "    return X, X_val, y, y_val";

        CompilationUnit converted = Convert(content);

        Assert.assertEquals(converted.getProblems().length, 0);
    }

    @Test
    public void testConversion40(){
        String content = "def _synth_regression_dataset\n"+
                        "X, y = datasets.make_regression(\n" +
                "        effective_rank=n_features * 2 // 3, tail_strength=0.6,\n" +
                "        n_samples=1_000,\n" +
                "        n_features=n_features,\n" +
                "        n_informative=n_features * 2 // 3,\n" +
                "        random_state=seed,\n" +
                "    )";

        CompilationUnit converted = Convert(content);
//        "number = (key for key in symbol_by_number.items() if value > input_element);"+
        Assert.assertEquals(converted.getProblems().length,0);

    }

    @Test
    public void testConversion41(){
        String content = "def _yield_checks(estimator):\n" +
                "    yield \n";

        CompilationUnit converted = Convert(content);

        Assert.assertEquals(converted.getProblems().length, 0);
    }

    @Test
    public void testConversion42(){
        String content = "def _synth_regression_dataset\n"+
                "    with pytest.raises(ValueError, match=msg):\n" +
                "        x=pipe[start:end:-1]";

        CompilationUnit converted = Convert(content);
//        "number = (key for key in symbol_by_number.items() if value > input_element);"+
        Assert.assertEquals(converted.getProblems().length,0);

    }

    @Test
    public void testConversion43(){
        String content = "def _synth_regression_dataset\n"+
                "    pipeline.set_params(steps=[('junk', ())])";

        CompilationUnit converted = Convert(content);
//        "number = (key for key in symbol_by_number.items() if value > input_element);"+
        Assert.assertEquals(converted.getProblems().length,0);

    }

    @Test
    public void testConversion44(){
        String content = "def divide(x, y):\n" +
                "    try:\n" +
                "       try:\n" +
                "           result = x / y\n" +
                "       except ZeroDivisionError:\n" +
                "           print(\"division by zero!\")\n" +
                "       else:\n" +
                "           print(\"result is\", result)\n" +
                "       gooo()\n"+
                "    finally:\n" +
                "        print(\"executing finally clause\")";
        CompilationUnit converted = Convert(content);

        Assert.assertEquals(converted.getProblems().length,0);


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


}




