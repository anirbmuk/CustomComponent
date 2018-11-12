package blog.anirbanm.cc.comp;

import javax.el.ELContext;
import javax.el.ExpressionFactory;

import javax.el.MethodExpression;
import javax.el.ValueExpression;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;

import oracle.adf.view.rich.event.QueryEvent;
import oracle.adf.view.rich.event.QueryOperationEvent;
import oracle.adf.view.rich.model.QueryDescriptor;
import oracle.adf.view.rich.model.QueryModel;

public class TablesearchDefinition {
    
    public TablesearchDefinition() {
        super();
    }
    
    private TablesearchComponent getComponent() {
        return (TablesearchComponent) resolveExpression("#{comp}");
    }
    
    private Object resolveExpression(String expression) {
        FacesContext facesContext = getFacesContext();
        Application app = facesContext.getApplication();
        ExpressionFactory elFactory = app.getExpressionFactory();
        ELContext elContext = facesContext.getELContext();
        ValueExpression valueExp = elFactory.createValueExpression(elContext, expression, Object.class);
        return valueExp.getValue(elContext);
    }
    
    private Object resolveMethodExpression(String expression, Class returnType, Class[] argTypes, Object[] argValues) {
        FacesContext facesContext = getFacesContext();
        Application app = facesContext.getApplication();
        ExpressionFactory elFactory = app.getExpressionFactory();
        ELContext elContext = facesContext.getELContext();
        MethodExpression methodExpression = elFactory.createMethodExpression(elContext, expression, returnType, argTypes);
        return methodExpression.invoke(elContext, argValues);
    }
    
    private FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    public QueryDescriptor getSearchComponentValue() {
        return (QueryDescriptor) resolveExpression("#{bindings." + getCriteriaName() + ".queryDescriptor}");
    }

    public QueryModel getSearchComponentModel() {
        return (QueryModel) resolveExpression("#{bindings." + getCriteriaName() + ".queryModel}");
    }
    
    public void searchQueryListener(final QueryEvent queryEvent) {
        resolveMethodExpression("#{bindings." + getCriteriaName() + ".processQuery}", Object.class,
                                new Class[] { QueryEvent.class }, new Object[] { queryEvent });
    }

    public void queryOperationListener(final QueryOperationEvent queryOperationEvent) {
        resolveMethodExpression("#{bindings." + getCriteriaName() + ".processQueryOperation}", Object.class,
                                new Class[] { QueryOperationEvent.class }, new Object[] { queryOperationEvent });
    }
    
    public Object getSearchTableValue() {
        return resolveExpression("#{bindings." + getTableBinding() + ".collectionModel}");
    }
    
    public Object[] getAttributeDefs() {
        return (Object[])resolveExpression("#{bindings." + getComponent().getTableBinding() + "Iterator.attributeDefs}");
    }

    private String getCriteriaName() {
        return getComponent().getCriteriaName();
    }
    
    private String getTableBinding() {
        return getComponent().getTableBinding();
    }
}
