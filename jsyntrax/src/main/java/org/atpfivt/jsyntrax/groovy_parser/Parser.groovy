package org.atpfivt.jsyntrax.groovy_parser

import org.atpfivt.jsyntrax.Configuration
import org.atpfivt.jsyntrax.units.Unit
import org.codehaus.groovy.control.CompilationFailedException
import org.codehaus.groovy.control.CompilerConfiguration

class Parser {
    static Configuration parse(String scriptText) throws CompilationFailedException {
        def config = new CompilerConfiguration()
        config.setScriptBaseClass(SyntraxScript.class.name)
        def sharedData = new Binding()
        def shell = new GroovyShell(this.class.classLoader, sharedData, config)
        shell.setVariable("None", null)
        Unit result = shell.evaluate(scriptText) as Unit
        result.getConfiguration()
    }
}
