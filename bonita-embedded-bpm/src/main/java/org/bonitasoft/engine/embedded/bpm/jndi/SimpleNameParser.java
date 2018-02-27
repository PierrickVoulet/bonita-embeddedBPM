package org.bonitasoft.engine.embedded.bpm.jndi;

import javax.naming.CompositeName;
import javax.naming.Name;
import javax.naming.NameParser;
import javax.naming.NamingException;

public class SimpleNameParser implements NameParser {

    public SimpleNameParser(final String name) {}

    public Name parse(final String name) throws NamingException {
        return new CompositeName(name);
    }
}
