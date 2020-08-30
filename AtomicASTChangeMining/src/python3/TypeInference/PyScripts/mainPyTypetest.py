from __future__ import print_function
import ast
import textwrap
from pytype import config
from pytype.tools.annotate_ast import annotate_ast
from typed_ast import ast27 as ast27
from typed_ast import ast3
from pytype.tools.traces import traces
from pytype import file_utils
import sys
import json

def annotate(source):
  source = textwrap.dedent(source.lstrip('\n'))
  ast_factory = lambda unused_options: ast
  pytype_options = config.Options.create(python_version=(3,7),
                                         imports_map='/Users/malinda/Documents/Research_Topic_2/PytypeTest/.pytype/imports/main.imports')

  module = annotate_ast.annotate_source(source, ast_factory, pytype_options)
  return module

def get_annotations_dict( module):
    return [{"lineNumber":_get_node_key(node)[0],"col_offset":_get_node_key(node)[1],"nodeName":_get_node_key(node)[2],"type":node.resolved_annotation}
            for node in ast.walk(module)
            if hasattr(node, 'resolved_type')]

def _get_node_key(node):
  # AST Name = node.__class__.__name__
  base = (node.lineno,node.col_offset)
  if isinstance(node, ast.Name):
    return base + (node.id,)
  elif isinstance(node, ast.Attribute):
    return base + (node.attr,)
  elif isinstance(node, ast.FunctionDef):
    return base + (node.name,)
  elif isinstance(node, ast.Param):
    return base + (node.name,)
  else:
    return base

def get_ast(options):
  major = options.python_version[0]
  return {2: ast27, 3: ast3}[major]

def main1():
  if len(sys.argv)>1:
    filename = sys.argv[1]
  else:
    filename = '/Users/malinda/Documents/Research_Topic_2/CPatMiner/AtomicASTChangeMining/resources/TestPyType/main.py'
  with open(filename, 'r') as f:
    src = f.read()
  module = annotate(src)
  dic_str = get_annotations_dict(module)
  # print(dic_str)
  json_object = json.dumps(dic_str)

  print(json_object)

if __name__ == '__main__':
  main1()

