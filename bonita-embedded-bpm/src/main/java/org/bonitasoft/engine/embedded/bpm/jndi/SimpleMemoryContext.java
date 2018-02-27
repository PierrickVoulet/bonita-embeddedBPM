package org.bonitasoft.engine.embedded.bpm.jndi;

import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NameClassPair;
import javax.naming.NameNotFoundException;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

/**
 * A JNDI context implementation that uses the memory as a dictionary of objects.
 */
public class SimpleMemoryContext implements Context {

    private final Map<String, Object> dictionary = new ConcurrentHashMap<String, Object>();

    public void clear() {
        dictionary.clear();
    }

    public Object lookup(final Name name) throws NamingException {
        return lookup(name.toString());
    }

    public Object lookup(final String name) throws NamingException {
        // System.out.println(toString() + " ~~~~ lookup " + name + " contains ? " + dictionary.containsKey(name));
        if (dictionary.containsKey(name)) {
            return dictionary.get(name);
        }
        throw new NameNotFoundException("Name " + name + " is not bound !");
    }

    public void bind(final Name name, final Object o) throws NamingException {
        bind(name.toString(), o);
    }

    public void bind(final String name, final Object o) throws NamingException {
        // System.out.println(toString() + " ~~~~ binding " + name + " with " + o + " already bound ? " + dictionary.containsKey(name));
        if (dictionary.containsKey(name)) {
            throw new NameAlreadyBoundException("Name " + name + " already bound!");
        }
        rebind(name, o);
    }

    public void rebind(final Name name, final Object o) {
        rebind(name.toString(), o);
    }

    public void rebind(final String name, final Object o) {
        dictionary.put(name, o);
    }

    public void unbind(final Name name) throws NamingException {
        unbind(name.toString());
    }

    public void unbind(final String name) throws NamingException {
        if (!dictionary.containsKey(name)) {
            throw new NameNotFoundException("No such name " + name + " is bound!");
        }
        dictionary.remove(name);
    }

    public void rename(final Name oldName, final Name newName) throws NamingException {
        rename(oldName.toString(), newName.toString());
    }

    
    public void rename(final String oldName, final String newName) throws NamingException {
        final Object object = lookup(oldName);
        bind(newName, object);
        unbind(oldName);
    }

    
    public NamingEnumeration<NameClassPair> list(final Name name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
    public NamingEnumeration<NameClassPair> list(final String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
    public NamingEnumeration<Binding> listBindings(final Name name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
    public NamingEnumeration<Binding> listBindings(final String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
    public void destroySubcontext(final Name name) {
        destroySubcontext(name.toString());
    }

    
    public void destroySubcontext(final String name) {
        dictionary.remove(name);
    }

    
    public Context createSubcontext(final Name name) throws NamingException {
        return createSubcontext(name.toString());
    }

    
    public Context createSubcontext(final String name) throws NamingException {
        final Context subContext = new SimpleMemoryContext();
        bind(name, subContext);
        return subContext;
    }

    
    public Object lookupLink(final Name name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
    public Object lookupLink(final String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
    public NameParser getNameParser(final Name name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
    public NameParser getNameParser(final String name) {
        return new SimpleNameParser(name);
    }

    
    public Name composeName(final Name name, final Name name1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
    public String composeName(final String string, final String string1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
    public Object addToEnvironment(final String string, final Object o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
    public Object removeFromEnvironment(final String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
    public Hashtable<?, ?> getEnvironment() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
    public void close() {
        // System.out.println("Closing SimpleMemoryContext");
        // Thread.dumpStack();
        // clear();
    }

    
    public String getNameInNamespace() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
