import os
import sys


@pytest.mark.parametrize("fit_intercept", [False, True])
def test_multinomial_identifiability_on_iris(fit_intercept):
    """Test that the multinomial classification is identifiable.

    A multinomial with c classes can be modeled with
    probability_k = exp(X@coef_k) / sum(exp(X@coef_l), l=1..c) for k=1..c.
    This is not identifiable, unless one chooses a further constraint.
    According to [1], the maximum of the L2 penalized likelihood automatically
    satisfies the symmetric constraint:
    sum(coef_k, k=1..c) = 0

    Further details can be found in the appendix of [2].

    Reference
    ---------
    .. [1] Zhu, Ji and Trevor J. Hastie. "Classification of gene microarrays by
    penalized logistic regression". Biostatistics 5 3 (2004): 427-43.
    https://doi.org/10.1093/biostatistics%2Fkxg046

    .. [2] Powers, Scott, Trevor J. Hastie and Robert Tibshirani. "Nuclear
    penalized multinomial regression with an application to predicting at bat
    outcomes in baseball." Statistical modelling 18 5-6 (2017): 388-410 .
    https://arxiv.org/pdf/1706.10272.pdf
    """
    # Test logistic regression with the iris dataset
    n_samples, n_features = iris.data.shape
    target = iris.target_names[iris.target]

    clf = LogisticRegression(C=len(iris.data), solver='lbfgs', max_iter=300,
                             multi_class='multinomial',
                             fit_intercept=fit_intercept
                             )
    clf.fit(iris.data, target)

    # axis=0 is sum over classes
    assert_allclose(clf.coef_.sum(axis=0), 0, atol=1e-10)
    if fit_intercept:
        clf.intercept_.sum(axis=0) == pytest.approx(0, abs=1e-15)
