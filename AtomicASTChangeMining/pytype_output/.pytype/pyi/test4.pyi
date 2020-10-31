
from typing import Any
def __getattr__(name: Any) -> Any: ...
# Caught error in pytype: Don't know how to match <class 'pytype.pytd.pytd.NamedType'> against <class 'pytype.pytd.pytd.Literal'>
# Traceback (most recent call last):
#   File "/Library/Frameworks/Python.framework/Versions/3.7/lib/python3.7/site-packages/pytype/io.py", line 157, in check_or_generate_pyi
#     input_filename=options.input, options=options, loader=loader)
#   File "/Library/Frameworks/Python.framework/Versions/3.7/lib/python3.7/site-packages/pytype/io.py", line 114, in generate_pyi
#     analyze.infer_types, input_filename, options, loader)
#   File "/Library/Frameworks/Python.framework/Versions/3.7/lib/python3.7/site-packages/pytype/io.py", line 66, in wrapper
#     return f(*args, **kwargs)
#   File "/Library/Frameworks/Python.framework/Versions/3.7/lib/python3.7/site-packages/pytype/io.py", line 85, in _call
#     deep=deep)
#   File "/Library/Frameworks/Python.framework/Versions/3.7/lib/python3.7/site-packages/pytype/analyze.py", line 720, in infer_types
#     ast = convert_structural.convert_pytd(ast, builtins_pytd, protocols_pytd)
#   File "/Library/Frameworks/Python.framework/Versions/3.7/lib/python3.7/site-packages/pytype/convert_structural.py", line 279, in convert_pytd
#     mapping, result = solve(ast, builtins_pytd, protocols_pytd)
#   File "/Library/Frameworks/Python.framework/Versions/3.7/lib/python3.7/site-packages/pytype/convert_structural.py", line 211, in solve
#     ast, builtins_pytd, protocols_pytd).solve(), extract_local(ast)
#   File "/Library/Frameworks/Python.framework/Versions/3.7/lib/python3.7/site-packages/pytype/convert_structural.py", line 168, in solve
#     factory_partial, solver_partial, partial, complete)
#   File "/Library/Frameworks/Python.framework/Versions/3.7/lib/python3.7/site-packages/pytype/convert_structural.py", line 95, in match_call_record
#     matcher.match_Function_against_Function(call_record, complete, {}))
#   File "/Library/Frameworks/Python.framework/Versions/3.7/lib/python3.7/site-packages/pytype/pytd/type_match.py", line 466, in match_Function_against_Function
#     for s1 in f1.signatures)
#   File "/Library/Frameworks/Python.framework/Versions/3.7/lib/python3.7/site-packages/pytype/pytd/booleq.py", line 370, in And
#     return simplify_exprs(exprs, _And, FALSE, TRUE)
#   File "/Library/Frameworks/Python.framework/Versions/3.7/lib/python3.7/site-packages/pytype/pytd/booleq.py", line 132, in simplify_exprs
#     for e in exprs:
#   File "/Library/Frameworks/Python.framework/Versions/3.7/lib/python3.7/site-packages/pytype/pytd/type_match.py", line 466, in <genexpr>
#     for s1 in f1.signatures)
#   File "/Library/Frameworks/Python.framework/Versions/3.7/lib/python3.7/site-packages/pytype/pytd/type_match.py", line 461, in match_Signature_against_Function
#     return booleq.And(make_or(inner) for inner in visitors.ExpandSignature(sig))
#   File "/Library/Frameworks/Python.framework/Versions/3.7/lib/python3.7/site-packages/pytype/pytd/booleq.py", line 370, in And
#     return simplify_exprs(exprs, _And, FALSE, TRUE)
#   File "/Library/Frameworks/Python.framework/Versions/3.7/lib/python3.7/site-packages/pytype/pytd/booleq.py", line 132, in simplify_exprs
#     for e in exprs:
#   File "/Library/Frameworks/Python.framework/Versions/3.7/lib/python3.7/site-packages/pytype/pytd/type_match.py", line 461, in <genexpr>
#     return booleq.And(make_or(inner) for inner in visitors.ExpandSignature(sig))
#   File "/Library/Frameworks/Python.framework/Versions/3.7/lib/python3.7/site-packages/pytype/pytd/type_match.py", line 460, in make_or
#     for s in f.signatures)
#   File "/Library/Frameworks/Python.framework/Versions/3.7/lib/python3.7/site-packages/pytype/pytd/booleq.py", line 385, in Or
#     return simplify_exprs(exprs, _Or, TRUE, FALSE)
#   File "/Library/Frameworks/Python.framework/Versions/3.7/lib/python3.7/site-packages/pytype/pytd/booleq.py", line 132, in simplify_exprs
#     for e in exprs:
#   File "/Library/Frameworks/Python.framework/Versions/3.7/lib/python3.7/site-packages/pytype/pytd/type_match.py", line 460, in <genexpr>
#     for s in f.signatures)
#   File "/Library/Frameworks/Python.framework/Versions/3.7/lib/python3.7/site-packages/pytype/pytd/type_match.py", line 450, in match_Signature_against_Signature
#     equalities.append(self.match_type_against_type(p1.type, p2.type, subst))
#   File "/Library/Frameworks/Python.framework/Versions/3.7/lib/python3.7/site-packages/pytype/pytd/type_match.py", line 309, in match_type_against_type
#     t1, t2, subst)
#   File "/Library/Frameworks/Python.framework/Versions/3.7/lib/python3.7/site-packages/pytype/pytd/type_match.py", line 342, in _match_type_against_type
#     for u in t2.type_list)
#   File "/Library/Frameworks/Python.framework/Versions/3.7/lib/python3.7/site-packages/pytype/pytd/booleq.py", line 385, in Or
#     return simplify_exprs(exprs, _Or, TRUE, FALSE)
#   File "/Library/Frameworks/Python.framework/Versions/3.7/lib/python3.7/site-packages/pytype/pytd/booleq.py", line 132, in simplify_exprs
#     for e in exprs:
#   File "/Library/Frameworks/Python.framework/Versions/3.7/lib/python3.7/site-packages/pytype/pytd/type_match.py", line 342, in <genexpr>
#     for u in t2.type_list)
#   File "/Library/Frameworks/Python.framework/Versions/3.7/lib/python3.7/site-packages/pytype/pytd/type_match.py", line 309, in match_type_against_type
#     t1, t2, subst)
#   File "/Library/Frameworks/Python.framework/Versions/3.7/lib/python3.7/site-packages/pytype/pytd/type_match.py", line 358, in _match_type_against_type
#     for t in self.expand_superclasses(t1))
#   File "/Library/Frameworks/Python.framework/Versions/3.7/lib/python3.7/site-packages/pytype/pytd/booleq.py", line 385, in Or
#     return simplify_exprs(exprs, _Or, TRUE, FALSE)
#   File "/Library/Frameworks/Python.framework/Versions/3.7/lib/python3.7/site-packages/pytype/pytd/booleq.py", line 132, in simplify_exprs
#     for e in exprs:
#   File "/Library/Frameworks/Python.framework/Versions/3.7/lib/python3.7/site-packages/pytype/pytd/type_match.py", line 358, in <genexpr>
#     for t in self.expand_superclasses(t1))
#   File "/Library/Frameworks/Python.framework/Versions/3.7/lib/python3.7/site-packages/pytype/pytd/type_match.py", line 309, in match_type_against_type
#     t1, t2, subst)
#   File "/Library/Frameworks/Python.framework/Versions/3.7/lib/python3.7/site-packages/pytype/pytd/type_match.py", line 396, in _match_type_against_type
#     type(t1), type(t2)))
# AssertionError: Don't know how to match <class 'pytype.pytd.pytd.NamedType'> against <class 'pytype.pytd.pytd.Literal'>