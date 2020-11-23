package com.syntrax.groovy_parser

import com.syntrax.units.tracks.Track
import org.codehaus.groovy.control.CompilerConfiguration

class Parser {
    static Track parse(File file) {
        def config = new CompilerConfiguration()
        config.setScriptBaseClass(SyntraxScript.class.name)
        def shell = new GroovyShell(this.class.classLoader, new Binding(), config)
        return shell.evaluate(file) as Track
    }
}
