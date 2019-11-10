import java.util.Scanner;
import java.util.Vector;

public class Main {

    static class Vertex {

        double x;
        double y;

        public Vertex(double a, double b) {
            x = a;
            y = b;
        }

    }

    static class FuzzyVar {

        String name;
        double crispValue;
        int setNum;


        Vector<FuzzySet> sets;

        public void setName(String name) {
            this.name = name;
        }

        public void setCrispValue(double crispValue) {
            this.crispValue = crispValue;
        }

        public void setSetNum(int setNum) {
            this.setNum = setNum;
        }

        public int getSetNum() {
            return setNum;
        }

        public void setSetsVector() {
            this.sets = new Vector<FuzzySet>();
        }

        public String getName() {
            return name;
        }

        public double getCrispValue() {
            return crispValue;
        }

        public Vector<FuzzySet> getSets() {
            return sets;
        }

        public void setSets(Vector<FuzzySet> sets) {
            this.sets = sets;
        }
    }

    static class FuzzySet {

        String name;
        String type;  //0 is triangle... 1 is trapezium
        double fuzzyMembership;

        public double getFuzzyMembership() {
            return fuzzyMembership;
        }

        public void setFuzzyMembership(double fuzzyMembership) {
            this.fuzzyMembership = fuzzyMembership;
        }

        int nVertices;


        Vector<Vertex> vertices;

        public void setName(String setName) {
            this.name = setName;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setVerticesVector() {
            vertices = new Vector<Vertex>();
        }

        public void setnVertices() {
            if (this.type.equals("triangle")) {
                this.nVertices = 3;
            } else {
                this.nVertices = 4;
            }
        }

        public int getnVertices() {
            return nVertices;
        }

        public Vector<Vertex> getVertices() {
            return vertices;
        }

        public String getName() {
            return name;
        }

        public void setYs() {

            if (this.type.equals("triangle")) {
                vertices.get(1).y = 1;
            } else {
                vertices.get(1).y = 1;
                vertices.get(2).y = 1;
            }
        }
    }


    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        int nVars = Integer.parseInt(sc.nextLine());
        Vector<FuzzyVar> vars = new Vector<FuzzyVar>();

        for (int i = 0; i < nVars; i++) {
            FuzzyVar var = new FuzzyVar();
            String lines = sc.nextLine();
            String[] strs = lines.trim().split("\\s+");
            var.setName(strs[0]);
            var.setCrispValue(Double.parseDouble(strs[1]));
            var.setSetNum(Integer.parseInt(sc.nextLine()));
            var.setSetsVector();

            for (int j = 0; j < var.getSetNum(); j++) {
                FuzzySet set = new FuzzySet();
                lines = sc.nextLine();
                strs = lines.trim().split("\\s+");
                set.setName(strs[0]);
                set.setType(strs[1]);
                set.setVerticesVector();
                set.setnVertices();

                lines = sc.nextLine();
                strs = lines.trim().split("\\s+");
                for (String s : strs)
                    set.getVertices().add(new Vertex(Double.parseDouble(s), 0));


                set.setYs();
                var.getSets().add(set);
            }
            vars.add(var);
        }

        fuzzify(vars);  //for each Fuzzy set in each fuzzy var... calculate the set's membership


        //---------------------
        // Output var
        //---------------------

        FuzzyVar output = new FuzzyVar();
        output.setName(sc.nextLine());
        output.setSetNum(Integer.parseInt(sc.nextLine()));
        output.setSetsVector();

        for (int j = 0; j < output.getSetNum(); j++) {
            FuzzySet set = new FuzzySet();
            String lines = sc.nextLine();
            String[] strs = lines.trim().split("\\s+");
            set.setName(strs[0]);
            set.setType(strs[1]);
            set.setVerticesVector();
            set.setnVertices();

            lines = sc.nextLine();
            strs = lines.trim().split("\\s+");
            for (String s : strs)
                set.getVertices().add(new Vertex(Double.parseDouble(s), 0));


            set.setYs();
            output.getSets().add(set);
        }

        printVar(vars.get(0));
        printVar(vars.get(1));
        printVar(vars.get(2));
        printVar(output);


        //----------------------
        //Rules
        //-----------------------

        int nRules = Integer.parseInt(sc.nextLine());
        String[] rules = new String[nRules];
        for (int i = 0; i < nRules; i++)
            rules[i] = sc.nextLine();

        for (String s : rules)
            System.out.println(s);

        Vector<Double> fuzzyRulesResults = new Vector<>();
        String fuzzyOutputSet = "";

        for (int i = 0; i < nRules; i++) {

            String[] strs = rules[i].split("\\s=\\s|\\s");
            fuzzyOutputSet = strs[strs.length - 1];

            int nConditions = Integer.parseInt(strs[0]);
            double resultForThisRule = 0;
            Vector<Double> conditionValues = new Vector<>(nConditions);
            Vector<String> conditionOperators = new Vector<>(nConditions - 1);


            for (int j = 0; ; ) {
                String fuzzyVarToLookFor = strs[++j];
                String fuzzySetToLookFor = strs[++j];

                //This part calculates the condition's membership value
                conditionValues.add(lookForSetMembership(vars, fuzzyVarToLookFor, fuzzySetToLookFor));
                //We have calculated the condition's membership and added it to the conditionValues vector

                ++j;
                if (strs[j].equals("AND") || strs[j].equals("OR")) {
                    conditionOperators.add(strs[j]);
                } else if (strs[j].equals("then")) {
                    break;
                }
            }

            int c = 0;
            for (int k = 0; k < conditionOperators.size(); k++) {

                if (conditionOperators.get(k).equals("AND"))
                    resultForThisRule = AND(conditionValues.get(c), conditionValues.get(++c));
                else
                    resultForThisRule = OR(conditionValues.get(c), conditionValues.get(++c));
            }
            fuzzyRulesResults.add(resultForThisRule);
        }

        System.out.println("Memberships");
        for (FuzzyVar var : vars) {
            for (FuzzySet set : var.sets) {
                System.out.println(set.name + " = " + set.fuzzyMembership);
            }
        }

        System.out.println();
        System.out.println("Results of fuzzy rules");
        for (double d : fuzzyRulesResults) {
            System.out.println(d);
        }
        System.out.println(fuzzyOutputSet);
        //------------------------
        //Defuzzification
        //------------------------


        //What am I trying to do??
        //I want to use the results from the statements before " then "
        //1- get results from fuzzyOutputSet (one entry for each fuzzy rule... so anding and oring already complete)
        //2- know which variable to set in (FuzzyVar)output
        //3- make that set equal to the corresponding fuzzyOutputSet
        //4- for each FuzzySet in output:
        //  4.1- calculate its centroidArea
        //  4.2- calculate its xCentroid
        //  4.3- multiply the xCentroid by the fuzzyMembership
        //  4.4 calculate the weighted mean using what you calculated in 4.3 --> This is the defuzzified value

    }

    static class FuzzyOutputPair{

        //This this helps keep track of the output FuzzyVar's sets when we assign membership

        String name;
        double membership;
    }

    private static double calculateCentroidArea(FuzzySet set) {

        double area = 0;

        for (int i = 0; i < set.nVertices - 1; i++) {
            double xi = set.vertices.get(i).x;
            double yi = set.vertices.get(i).y;
            double xiplus1 = set.vertices.get(i + 1).x;
            double yiplus1 = set.vertices.get(i + 1).y;

            area += (xi * yiplus1) - (xiplus1 * yi);
        }

        return (0.5 * area);
    }

    private static double calculatexCentroid(FuzzySet set, double area) {

        double xCentroidVal = 0;

        for (int i = 0; i < set.nVertices - 1; i++) {
            double xi = set.vertices.get(i).x;
            double yi = set.vertices.get(i).y;
            double xiplus1 = set.vertices.get(i + 1).x;
            double yiplus1 = set.vertices.get(i + 1).y;

            xCentroidVal += (xi + xiplus1) * ((xi * yiplus1) - (xiplus1 * yi));
        }

        return (1 / (area * 6)) * xCentroidVal;
    }

    public static double AND(double d1, double d2) {
        return Math.min(d1, d2);
    }

    public static double OR(double d1, double d2) {
        return Math.max(d1, d2);
    }

    public static double lookForSetMembership(Vector<FuzzyVar> vars, String fuzzyVarToLookFor, String fuzzySetToLookFor) {

        for (FuzzyVar var : vars) {
            if (var.name.equals(fuzzyVarToLookFor)) {
                for (FuzzySet set : var.sets) {
                    if (set.name.equals(fuzzySetToLookFor)) {
                        return set.fuzzyMembership;

                    }
                }

            }

        }

        System.out.println("Could not find the fuzzySet " + fuzzySetToLookFor + " in fuzzyVar " + fuzzyVarToLookFor);
        System.out.println("Terminating. \nPlease insert correct values next time");
        System.exit(0);
        return 0;
    }

    private static void fuzzify(Vector<FuzzyVar> vars) {

        for (FuzzyVar var : vars) {

            double crisp = var.crispValue;
            Vertex vertex1 = null, vertex2 = null;
            for (FuzzySet set : var.sets) {

                set.fuzzyMembership = 0;
                if (doesIntersect(set, crisp)) {
                    Vertex[] tempv = getIntersectionLinePoints(set, crisp);
                    vertex1 = tempv[0];
                    vertex2 = tempv[1];
                    set.fuzzyMembership = calculateMembership(vertex1, vertex2, crisp);
                }

            }


        }
    }

    private static double calculateMembership(Vertex vertex1, Vertex vertex2, double crisp) {

        double gradient;
        double yIntercept;

        double x1 = vertex1.x;
        double x2 = vertex2.x;
        double y1 = vertex1.y;
        double y2 = vertex2.y;

        if (x2 - x1 == 0) {
            System.out.println("Gradient is undefined");
            System.out.println("Assuming membership = 0");
            return 0;
        }

        gradient = ((y2 - y1) / (x2 - x1));
        yIntercept = y1 - (x1 * gradient);

        double membership = (gradient * crisp) + yIntercept;
        return membership;
    }

    public static boolean doesIntersect(FuzzySet set, double crisp) {


        for (int i = 0; i < set.nVertices - 1; i++) {

            if (crisp >= set.vertices.get(i).x && crisp <= set.vertices.get(i + 1).x) {
                return true;
            }

        }
        return false;
    }

    public static Vertex[] getIntersectionLinePoints(FuzzySet set, double crisp) {

        Vertex[] verts = new Vertex[2];
        for (int k = 0; k < set.nVertices - 1; k++) {

            if (crisp >= set.vertices.get(k).x && crisp <= set.vertices.get(k + 1).x) {
                verts[0] = set.vertices.get(k);
                verts[1] = set.vertices.get(k + 1);
                break;

            }
        }

        return verts;
    }

    public static void printVar(FuzzyVar var) {

        System.out.println(var.name + " " + var.crispValue);
        System.out.println(var.setNum);
        for (int i = 0; i < var.setNum; i++) {

            System.out.println(var.sets.get(i).name + " " + var.sets.get(i).type);
            for (Vertex j : var.sets.get(i).getVertices())
                System.out.print("(" + j.x + " , " + j.y + ")");
            System.out.println();

        }

    }
}
