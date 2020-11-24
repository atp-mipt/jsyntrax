package org.atpfivt.jsyntrax.groovy_parser

import org.atpfivt.jsyntrax.units.tracks.Track
import org.codehaus.groovy.control.CompilerConfiguration

class Parser {
    static Track parse(File file) {
        def config = new CompilerConfiguration()
        config.setScriptBaseClass(SyntraxScript.class.name)
        def shell = new GroovyShell(this.class.classLoader, new Binding(), config)
        shell.setVariable("None", null)
        println "None is null? " + (shell.getVariable("None") == null)
        println 'None is "null"? ' + (shell.getVariable("None") == "null")

        return shell.evaluate(file) as Track
    }
}
