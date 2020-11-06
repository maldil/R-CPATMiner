
from typing import Any
def __getattr__(name: Any) -> Any: ...
# Caught error in pytype: Don't know how to match <class 'pytype.pytd.pytd.NamedType'> against <class 'pytype.pytd.pytd.Literal'>
# Traceback (most recent call last):
#   File "/Users/malinda/Documents/Research_Topic_2/VMS/Twitter/lib/python3.7/site-packages/pytype/io.py", line 152, in check_or_generate_pyi
#     src=src, options=options, loader=loader)
#   File "/Users/malinda/Documents/Research_Topic_2/VMS/Twitter/lib/python3.7/site-packages/pytype/io.py", line 110, in generate_pyi
#     errorlog, (mod, builtins) = _call(analyze.infer_types, src, options, loader)
#   File "/Users/malinda/Documents/Research_Topic_2/VMS/Twitter/lib/python3.7/site-packages/pytype/io.py", line 64, in wrapper
#     return f(*args, **kwargs)
#   File "/Users/malinda/Documents/Research_Topic_2/VMS/Twitter/lib/python3.7/site-packages/pytype/io.py", line 82, in _call
#     deep=deep)
#   File "/Users/malinda/Documents/Research_Topic_2/VMS/Twitter/lib/python3.7/site-packages/pytype/analyze.py", line 720, in infer_types
#     ast = convert_structural.convert_pytd(ast, builtins_pytd, protocols_pytd)
#   File "/Users/malinda/Documents/Research_Topic_2/VMS/Twitter/lib/python3.7/site-packages/pytype/convert_structural.py", line 279, in convert_pytd
#     mapping, result = solve(ast, builtins_pytd, protocols_pytd)
#   File "/Users/malinda/Documents/Research_Topic_2/VMS/Twitter/lib/python3.7/site-packages/pytype/convert_structural.py", line 211, in solve
#     ast, builtins_pytd, protocols_pytd).solve(), extract_local(ast)
#   File "/Users/malinda/Documents/Research_Topic_2/VMS/Twitter/lib/python3.7/site-packages/pytype/convert_structural.py", line 168, in solve
#     factory_partial, solver_partial, partial, complete)
#   File "/Users/malinda/Documents/Research_Topic_2/VMS/Twitter/lib/python3.7/site-packages/pytype/convert_structural.py", line 95, in match_call_record
#     matcher.match_Function_against_Function(call_record, complete, {}))
#   File "/Users/malinda/Documents/Research_Topic_2/VMS/Twitter/lib/python3.7/site-packages/pytype/pytd/type_match.py", line 450, in match_Function_against_Function
#     for s1 in f1.signatures)
#   File "/Users/malinda/Documents/Research_Topic_2/VMS/Twitter/lib/python3.7/site-packages/pytype/pytd/booleq.py", line 355, in And
#     return simplify_exprs(exprs, _And, FALSE, TRUE)
#   File "/Users/malinda/Documents/Research_Topic_2/VMS/Twitter/lib/python3.7/site-packages/pytype/pytd/booleq.py", line 117, in simplify_exprs
#     for e in exprs:
#   File "/Users/malinda/Documents/Research_Topic_2/VMS/Twitter/lib/python3.7/site-packages/pytype/pytd/type_match.py", line 450, in <genexpr>
#     for s1 in f1.signatures)
#   File "/Users/malinda/Documents/Research_Topic_2/VMS/Twitter/lib/python3.7/site-packages/pytype/pytd/type_match.py", line 445, in match_Signature_against_Function
#     return booleq.And(make_or(inner) for inner in visitors.ExpandSignature(sig))
#   File "/Users/malinda/Documents/Research_Topic_2/VMS/Twitter/lib/python3.7/site-packages/pytype/pytd/booleq.py", line 355, in And
#     return simplify_exprs(exprs, _And, FALSE, TRUE)
#   File "/Users/malinda/Documents/Research_Topic_2/VMS/Twitter/lib/python3.7/site-packages/pytype/pytd/booleq.py", line 117, in simplify_exprs
#     for e in exprs:
#   File "/Users/malinda/Documents/Research_Topic_2/VMS/Twitter/lib/python3.7/site-packages/pytype/pytd/type_match.py", line 445, in <genexpr>
#     return booleq.And(make_or(inner) for inner in visitors.ExpandSignature(sig))
#   File "/Users/malinda/Documents/Research_Topic_2/VMS/Twitter/lib/python3.7/site-packages/pytype/pytd/type_match.py", line 444, in make_or
#     for s in f.signatures)
#   File "/Users/malinda/Documents/Research_Topic_2/VMS/Twitter/lib/python3.7/site-packages/pytype/pytd/booleq.py", line 370, in Or
#     return simplify_exprs(exprs, _Or, TRUE, FALSE)
#   File "/Users/malinda/Documents/Research_Topic_2/VMS/Twitter/lib/python3.7/site-packages/pytype/pytd/booleq.py", line 117, in simplify_exprs
#     for e in exprs:
#   File "/Users/malinda/Documents/Research_Topic_2/VMS/Twitter/lib/python3.7/site-packages/pytype/pytd/type_match.py", line 444, in <genexpr>
#     for s in f.signatures)
#   File "/Users/malinda/Documents/Research_Topic_2/VMS/Twitter/lib/python3.7/site-packages/pytype/pytd/type_match.py", line 434, in match_Signature_against_Signature
#     equalities.append(self.match_type_against_type(p1.type, p2.type, subst))
#   File "/Users/malinda/Documents/Research_Topic_2/VMS/Twitter/lib/python3.7/site-packages/pytype/pytd/type_match.py", line 293, in match_type_against_type
#     t1, t2, subst)
#   File "/Users/malinda/Documents/Research_Topic_2/VMS/Twitter/lib/python3.7/site-packages/pytype/pytd/type_match.py", line 326, in _match_type_against_type
#     for u in t2.type_list)
#   File "/Users/malinda/Documents/Research_Topic_2/VMS/Twitter/lib/python3.7/site-packages/pytype/pytd/booleq.py", line 370, in Or
#     return simplify_exprs(exprs, _Or, TRUE, FALSE)
#   File "/Users/malinda/Documents/Research_Topic_2/VMS/Twitter/lib/python3.7/site-packages/pytype/pytd/booleq.py", line 117, in simplify_exprs
#     for e in exprs:
#   File "/Users/malinda/Documents/Research_Topic_2/VMS/Twitter/lib/python3.7/site-packages/pytype/pytd/type_match.py", line 326, in <genexpr>
#     for u in t2.type_list)
#   File "/Users/malinda/Documents/Research_Topic_2/VMS/Twitter/lib/python3.7/site-packages/pytype/pytd/type_match.py", line 293, in match_type_against_type
#     t1, t2, subst)
#   File "/Users/malinda/Documents/Research_Topic_2/VMS/Twitter/lib/python3.7/site-packages/pytype/pytd/type_match.py", line 342, in _match_type_against_type
#     for t in self.expand_superclasses(t1))
#   File "/Users/malinda/Documents/Research_Topic_2/VMS/Twitter/lib/python3.7/site-packages/pytype/pytd/booleq.py", line 370, in Or
#     return simplify_exprs(exprs, _Or, TRUE, FALSE)
#   File "/Users/malinda/Documents/Research_Topic_2/VMS/Twitter/lib/python3.7/site-packages/pytype/pytd/booleq.py", line 117, in simplify_exprs
#     for e in exprs:
#   File "/Users/malinda/Documents/Research_Topic_2/VMS/Twitter/lib/python3.7/site-packages/pytype/pytd/type_match.py", line 342, in <genexpr>
#     for t in self.expand_superclasses(t1))
#   File "/Users/malinda/Documents/Research_Topic_2/VMS/Twitter/lib/python3.7/site-packages/pytype/pytd/type_match.py", line 293, in match_type_against_type
#     t1, t2, subst)
#   File "/Users/malinda/Documents/Research_Topic_2/VMS/Twitter/lib/python3.7/site-packages/pytype/pytd/type_match.py", line 380, in _match_type_against_type
#     type(t1), type(t2)))
# AssertionError: Don't know how to match <class 'pytype.pytd.pytd.NamedType'> against <class 'pytype.pytd.pytd.Literal'>