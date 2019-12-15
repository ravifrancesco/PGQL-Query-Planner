package operators.utils;

import graph.statistics.Comparators;
import oracle.pgql.lang.ir.QueryExpression;

/**
 * This class is used to parse a QueryExpression where constraint to use
 * it for calculating selectivity.
 *
 * type indicates the contraint type of the object:
 *      - if == WHERE, it is a contraint like a.name = 'Dave' and [name = "a", attribute = "name", value = "Dave"]
 *      - if == HAS_LABEL it is a contraint like a:STUDENT and [name = "a", attribute = "student"]
 */

public class Constraint {

    ConstraintType type = ConstraintType.WHERE;
    Comparators constraintComparator;

    String name;
    String attribute;
    String value;

    // contructor
    public Constraint(QueryExpression constraint) {

        comparatorType(constraint.getExpType());
        String[] temp1 = castConstraint(constraint);

        if (type == ConstraintType.WHERE) {
            this.value = temp1[1];
            String[] temp2 = temp1[0].split(".", 2);
            this.name = temp2[0];
            this.attribute = temp2[1];
        } else if (type == ConstraintType.HAS_LABEL) {
            name = temp1[0];
            attribute = temp1[1];
        } else {
            // shouldn't enter else statement
        }

    }

    // used to provide Comparator enum value, refer to pgql documentation (class QueryExpression)
    private void comparatorType(QueryExpression.ExpressionType expressionType) {

        if (expressionType == QueryExpression.ExpressionType.EQUAL) {
            this.constraintComparator = Comparators.EQUAL;
        } else if (expressionType == QueryExpression.ExpressionType.NOT_EQUAL) {
            this.constraintComparator =  Comparators.NOT_EQUAL;
        } else if (expressionType == QueryExpression.ExpressionType.LESS) {
            this.constraintComparator =  Comparators.LESS;
        } else if (expressionType == QueryExpression.ExpressionType.GREATER) {
            this.constraintComparator = Comparators.GREATER;
        } else if (expressionType == QueryExpression.ExpressionType.LESS_EQUAL) {
            this.constraintComparator =  Comparators.LESS_EQUAL;
        } else if (expressionType == QueryExpression.ExpressionType.GREATER_EQUAL) {
            this.constraintComparator =  Comparators.GREATER_EQUAL;
        } else if (expressionType == QueryExpression.ExpressionType.FUNCTION_CALL){
            this.type = ConstraintType.HAS_LABEL;
        } else {
            // gestire tipo diverso
        }

    }

    // provides expressions from the constraint, refer to pgql documentation (class QueryExpression)
    private String[] castConstraint(QueryExpression constraint) {

        Class c = constraint.getClass();
        String[] expressions = new String[2];

        if (c.equals(QueryExpression.RelationalExpression.Equal.class)) {
            QueryExpression.RelationalExpression.Equal constraintCasted = (QueryExpression.RelationalExpression.Equal) constraint;
            expressions[0] = constraintCasted.getExp1().toString();
            expressions[1] = constraintCasted.getExp2().toString();
        } else if (c.equals(QueryExpression.RelationalExpression.NotEqual.class)) {
            QueryExpression.RelationalExpression.NotEqual constraintCasted = (QueryExpression.RelationalExpression.NotEqual) constraint;
            expressions[0] = constraintCasted.getExp1().toString();
            expressions[1] = constraintCasted.getExp2().toString();
        } else if (c.equals(QueryExpression.RelationalExpression.Less.class)) {
            QueryExpression.RelationalExpression.Less constraintCasted = (QueryExpression.RelationalExpression.Less) constraint;
            expressions[0] = constraintCasted.getExp1().toString();
            expressions[1] = constraintCasted.getExp2().toString();
        } else if (c.equals(QueryExpression.RelationalExpression.Greater.class)) {
            QueryExpression.RelationalExpression.Greater constraintCasted = (QueryExpression.RelationalExpression.Greater) constraint;
            expressions[0] = constraintCasted.getExp1().toString();
            expressions[1] = constraintCasted.getExp2().toString();
        } else if (c.equals(QueryExpression.RelationalExpression.LessEqual.class)) {
            QueryExpression.RelationalExpression.LessEqual constraintCasted = (QueryExpression.RelationalExpression.LessEqual) constraint;
            expressions[0] = constraintCasted.getExp1().toString();
            expressions[1] = constraintCasted.getExp2().toString();
        } else if (c.equals(QueryExpression.RelationalExpression.GreaterEqual.class)) {
            QueryExpression.RelationalExpression.GreaterEqual constraintCasted = (QueryExpression.RelationalExpression.GreaterEqual) constraint;
            expressions[0] = constraintCasted.getExp1().toString();
            expressions[1] = constraintCasted.getExp2().toString();
        } else if (c.equals(QueryExpression.FunctionCall.class)) {
            QueryExpression.FunctionCall constraintCasted = (QueryExpression.FunctionCall) constraint;
            Object[] object = constraintCasted.getArgs().toArray();
            expressions[0] = object[0].toString();
            expressions[1] = object[1].toString();
        } else {
            // gestire classe diversa
        }

        return expressions;

    }

    // getters
    public ConstraintType getType() {
        return type;
    }

    public Comparators getConstraintComparator() {
        return constraintComparator;
    }

    public String getName() {
        return name;
    }

    public String getAttribute() {
        return attribute;
    }

    public String getValue() {
        return value;
    }

}
