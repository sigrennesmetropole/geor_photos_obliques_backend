package org.georchestra.photosobliques.service.sm.statistiques.visitor;

public abstract class AbstractStatistiquesVisitor implements StatistiquesVisitor{

    private final String  methodName;

    protected AbstractStatistiquesVisitor(String methodName) {
        this.methodName = methodName;
    }

    @Override
    public boolean accept(String methodName) {
        return this.methodName.equals(methodName);
    }
}
