package heuristic;

import operators.EdgeMatchPlan;
import operators.QueryPlan;
import oracle.pgql.lang.ir.GraphPattern;
import oracle.pgql.lang.ir.GraphQuery;
import oracle.pgql.lang.ir.QueryExpression;

import java.util.Set;

public class HeuristicPlanner {
    private GraphQuery query;

    public HeuristicPlanner(GraphQuery query){
        this.query = query;
    }

    /*
    Generates the execution plan given a GraphQuery argument
     */
    public static QueryPlan generatePlan(GraphQuery query) {

        GraphPattern gPattern = query.getGraphPattern();
        Set<QueryExpression> constraints = gPattern.getConstraints();
        QueryExpression[] expArr = constraints.toArray(new QueryExpression[constraints.size()]);

        int rootVertices = 0;
        int constantVertices = 0;

        //checks how many root vertices and constant vertices we have in the query
        for(QueryExpression expr : expArr){
            String expType = expr.getExpType().toString();
            if(expType.equals("FUNCTION_CALL")){
                rootVertices++;
            } else {
                constantVertices++;
            }
        }
        //System.out.println(expArr[0].getExpType().toString());

        /*

        if constantVertices >= 1
            plan <-- findOptimalConstVertexOrder()
        else
            plan <-- findMaxRootVertexSel()

        subPlan(plan.lastVert, verticesLeft)


        function subPlan(plan.lastVert, verticesLeft)
            for v in verticesLeft
                for op in operators
                   selMap.put(op(v), op(v).sel)
            orderMap(selMap)

            for key in orderMap with value within 10% difference
                remove key.v from verticesLeft
                plan.lastVert.addChild(op(v))
                subPlan(plan.lastVert, verticesLeft)

        run Dijktra on plan to find the shortest path

         */

        return null;

    }
}
