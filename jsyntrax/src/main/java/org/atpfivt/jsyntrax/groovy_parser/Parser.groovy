package org.atpfivt.jsyntrax.groovy_parser

import org.atpfivt.jsyntrax.units.Unit
import org.atpfivt.jsyntrax.units.tracks.Track
import org.codehaus.groovy.control.CompilationFailedException
import org.codehaus.groovy.control.CompilerConfiguration

class Parser {
    static Unit parse(String scriptText) throws CompilationFailedException {
        def config = new CompilerConfiguration()
        config.setScriptBaseClass(SyntraxScript.class.name)
        def shell = new GroovyShell(this.class.classLoader, new Binding(), config)
        shell.setVariable("None", null)

        return shell.evaluate(scriptText) as Track
    }
}
