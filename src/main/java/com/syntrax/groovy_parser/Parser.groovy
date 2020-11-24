package com.syntrax.groovy_parser

import com.syntrax.Specification
import com.syntrax.units.tracks.Track
import org.codehaus.groovy.control.CompilerConfiguration

class Parser {
    static Specification parse(File file) {
        def config = new CompilerConfiguration()
        config.setScriptBaseClass(SyntraxScript.class.name)
        def shell = new GroovyShell(this.class.classLoader, new Binding(), config)
        shell.setVariable("None", null)

        return new Specification(shell.evaluate(file) as Track, new HashMap<String, URL>())
    }
}
