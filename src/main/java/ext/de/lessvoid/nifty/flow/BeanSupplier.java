package ext.de.lessvoid.nifty.flow;

public interface BeanSupplier {

    <T> T getBean(String var1, Class<T> var2);
}
