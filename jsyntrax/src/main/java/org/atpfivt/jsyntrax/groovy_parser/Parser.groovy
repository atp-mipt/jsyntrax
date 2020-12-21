package org.atpfivt.jsyntrax.groovy_parser

import org.atpfivt.jsyntrax.Configuration
import org.atpfivt.jsyntrax.units.Unit
import org.atpfivt.jsyntrax.units.tracks.Track
import org.codehaus.groovy.control.CompilationFailedException
import org.codehaus.groovy.control.CompilerConfiguration

class Parser {
    static Configuration parse(String scriptText) throws CompilationFailedException {
        def config = new CompilerConfiguration()
        config.setScriptBaseClass(SyntraxScript.class.name)
        def sharedData = new Binding()
        def shell = new GroovyShell(this.class.classLoader, sharedData, config)
        shell.setVariable("None", null)

        Object result = shell.evaluate(scriptText)

        if (result instanceof Configuration) {
            return result as Configuration
        } else if (result instanceof Unit) {
            return new Configuration((Unit) result)
        } else {
            assert false
        }
    }
}
