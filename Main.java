import java.util.Scanner;
import java.util.Vector;

public class Main {

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
        int nVertices;


        Vector<Double> vertices;

        public void setName(String setName) {
            this.name = setName;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setVerticesVector() {
            vertices = new Vector<Double>();
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

        public Vector<Double> getVertices() {
            return vertices;
        }

        public String getName() {
            return name;
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
                    set.getVertices().add(Double.parseDouble(s));


                var.getSets().add(set);
            }
            vars.add(var);
        }

//        printVar(vars.get(0));
        fuzzify(vars);

    }

    private static void fuzzify(Vector<FuzzyVar> vars) {

        for (FuzzyVar var : vars) {

            double crisp = var.crispValue;
            //now I want to find the sets that this crisp value belongs to

            for (FuzzySet set : var.sets) {

                for (int k = 0; k < set.nVertices; k++) {

                    if (crisp < set.vertices.get(k)){
                        //get this point and the point before it
                        //Postpone this part till we implement the
                        // a way to keep track of points
                    }

                }
            }


        }
    }

    public static void printVar(FuzzyVar var) {

        System.out.println(var.name + " " + var.crispValue);
        System.out.println(var.setNum);
        for (int i = 0; i < var.setNum; i++) {

            System.out.println(var.sets.get(i).name + " " + var.sets.get(i).type);
            for (Double j : var.sets.get(i).getVertices())
                System.out.print(j + "  ");
            System.out.println();

        }

    }
}
