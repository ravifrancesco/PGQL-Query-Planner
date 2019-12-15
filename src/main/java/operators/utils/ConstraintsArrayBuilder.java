package operators.utils;

import oracle.pgql.lang.ir.QueryEdge;
import oracle.pgql.lang.ir.QueryExpression;
import oracle.pgql.lang.ir.QueryVertex;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

public class ConstraintsArrayBuilder {

    protected ArrayList<Constraint> constraintsVertexArrayBuilder(QueryVertex queryVertex, Set<QueryExpression> constraintsSet) {

        Iterator<QueryExpression> itr = constraintsSet.iterator();
        ArrayList<Constraint> constraintArrayList = new ArrayList<>();

        while(itr.hasNext()){
            Constraint tempConstraint = new Constraint(itr.next());
            if (tempConstraint.getName().equalsIgnoreCase(queryVertex.getName())) {
                constraintArrayList.add(tempConstraint);
            }
        }

        return constraintArrayList;

    }

    protected ArrayList<Constraint> constraintsEdgeArrayBuilder(QueryEdge queryEdge, Set<QueryExpression> constraintsSet) {

        Iterator<QueryExpression> itr = constraintsSet.iterator();
        ArrayList<Constraint> constraintArrayList = new ArrayList<>();

        while(itr.hasNext()){
            Constraint tempConstraint = new Constraint(itr.next());
            if (tempConstraint.getName().equalsIgnoreCase(queryEdge.getName())) {
                constraintArrayList.add(tempConstraint);
            }
        }

        return constraintArrayList;

    }

}
