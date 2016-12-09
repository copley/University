package DecisionTree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Daniel Braithwaite on 16/03/2016.
 */
public class DecisionTree {

    private DecisionTreeElement root;
    private ArrayList<String> attrs;
    private String baseCategory;
    private double baseCategoryProb;

    public DecisionTree(ArrayList<String> attrs, ArrayList<Instance> instances) {
        this.baseCategory = findBaseCategory(instances);
        this.baseCategoryProb = computeCategoryProb(instances, baseCategory);
        this.attrs = attrs;
        this.root = buildTree(baseCategory, baseCategoryProb, attrs, new ArrayList<String>(), instances);
    }

    public String classify(Instance i) {
        return root.classify(attrs, i);
    }

    public String getBaseCategory() {
        return baseCategory;
    }

    public String getTex() {
        return root.getTex() + ";";
    }

    @Override
    public String toString() {
        return root.report(0);
    }

    private static DecisionTreeElement buildTree(String baseCategory, double baseCategoryProb, ArrayList<String> attrs, ArrayList<String> used, ArrayList<Instance> instances) {
        if (instances.isEmpty()) {
            return new DecisionTreeLeaf(baseCategory, baseCategoryProb);
        } else if (isPure(instances)) {
            return new DecisionTreeLeaf(instances.get(0).getCategory(), 1);
        } else if (attrs.size() == used.size()) {
            String category = findBaseCategory(instances);
            double prob  = computeCategoryProb(instances, category);
            return new DecisionTreeLeaf(category, prob);
        } else {
            double bestImpurity = Integer.MAX_VALUE;
            String bestAttr = null;
            ArrayList<Instance> bestTrue = null;
            ArrayList<Instance> bestFalse = null;

            for (String attr : attrs) {

                if (used.contains(attr)) {
                    continue;
                }

                ArrayList<Instance> t = new ArrayList<>();
                ArrayList<Instance> f = new ArrayList<>();

                for (Instance instance : instances) {
                    if (instance.getAttr(attr)) {
                        t.add(instance);
                    } else {
                        f.add(instance);
                    }
                }

                double impurity = weightedImpurity(t, f);
                if (impurity <= bestImpurity) {
                    bestImpurity = impurity;
                    bestAttr = attr;
                    bestTrue = t;
                    bestFalse = f;
                }
            }

            ArrayList<String> newUsed = (ArrayList<String>) used.clone();
            newUsed.add(bestAttr);

            DecisionTreeElement left = buildTree(baseCategory, baseCategoryProb, attrs, newUsed, bestTrue);
            DecisionTreeElement right = buildTree(baseCategory, baseCategoryProb, attrs, newUsed, bestFalse);

            return new DecisionTreeNode(bestAttr, left, right);
        }
    }

    private static double computeInformationGain(ArrayList<Instance> all, ArrayList<Instance> t, ArrayList<Instance> f) {
        double total = t.size() + f.size();

        double tg = t.size() != 0 ? t.size()/total * b(numPositive(t)/t.size()) : 0;
        double fg = f.size() != 0 ? f.size()/total * b(numPositive(f)/f.size()) : 0;

        double positive = numPositive(all);

        return b(positive/all.size()) - (tg + fg);
    }

    private static double b(double prob) {
        if (prob == 0 || prob == 1) {
            return 0;
        }

        return -(prob * (Math.log10(prob)/Math.log10(2)) + (1-prob) * (Math.log10(1 - prob)/Math.log10(2)));
    }

    private static double numPositive(ArrayList<Instance> instances) {
        double count = 0;
        for (Instance instance : instances) {
            count += instance.isPositive() ? 1 : 0;
        }

        return count;
    }










    private static double weightedImpurity(ArrayList<Instance> t, ArrayList<Instance> f) {
        double total = t.size() + f.size();

        double trueImpurity = calculateImpurity(t);
        double falseImpurity = calculateImpurity(f);


        return ((((double) t.size())/total) * trueImpurity) + ((((double) f.size())/total) * falseImpurity);
    }

    private static double calculateImpurity(ArrayList<Instance> instances) {
        double countA = 0;
        double countB = 0;

        if (instances.size() == 0) {
            return 0;
        }

        String a = instances.get(0).getCategory();

        for (Instance instance : instances) {
            if (a.equals(instance.getCategory())) {
                countA++;
            } else {
                countB++;
            }
        }

        return (countA * countB) / Math.pow(instances.size(), 2);
        //return (((double) countA) / ((double) instances.size())) * (((double) countB) / ((double) instances.size()));
        //return ((float) countA) / ((float) countB);
    }

    /**
     * Determins if a list of instances is pure or not
     * i.e. wether they all have the same category or not
     *
     * @param instances
     * @return true iff instances are pure
     */
    private static boolean isPure(ArrayList<Instance> instances) {
        String c = instances.get(0).getCategory();
        for (Instance i : instances) {
            if (!i.getCategory().equals(c)) {
                return false;
            }
        }

        return true;
    }

    private static String findBaseCategory(ArrayList<Instance> instances) {
        Map<String, Integer> counts = new HashMap<>();

        for (Instance i : instances) {
            if (!counts.containsKey(i.getCategory())) {
                counts.put(i.getCategory(), 0);
            }

            counts.put(i.getCategory(), counts.get(i.getCategory()) + 1);
        }

        int bestCount = 0;
        String best = null;
        for (String c : counts.keySet()) {
            if (counts.get(c) > bestCount) {
                bestCount = counts.get(c);
                best = c;
            }
        }

        return best;
    }

    private static double computeCategoryProb(ArrayList<Instance> instances, String category) {
        int c = 0;
        for (Instance instance : instances) {
            if (instance.getCategory().equals(category)) {
                c++;
            }
        }

        return ((double) c) / ((double) instances.size());
    }

    public static class Instance {
        private String category;
        private boolean[] attrs;
        private Map<String, Boolean> namesToAttrs;
        private boolean positive;

        public Instance(String category, ArrayList<String> attrNames, boolean[] attrs, boolean positive) {
            this.category = category;
            this.attrs = attrs;
            this.positive = positive;

            namesToAttrs = new HashMap<>();
            for (int i = 0; i < attrNames.size(); i++) {
                namesToAttrs.put(attrNames.get(i), attrs[i]);
            }
        }

        public boolean isPositive() {
            return positive;
        }

        public String getCategory() {
            return category;
        }

        public boolean getAttr(int i) {
            return attrs[i];
        }

        public boolean getAttr(String name) {
            return namesToAttrs.get(name);
        }

        @Override
        public String toString() {
            String s = category;

            for (boolean b : attrs) {
                s += " " + b;
            }

            return s;
        }
    }

}
