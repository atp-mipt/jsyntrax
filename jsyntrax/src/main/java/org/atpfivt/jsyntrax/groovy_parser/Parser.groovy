package org.atpfivt.jsyntrax.groovy_parser

import org.atpfivt.jsyntrax.units.Unit
import org.atpfivt.jsyntrax.units.tracks.Track
import org.codehaus.groovy.control.CompilationFailedException
import org.codehaus.groovy.control.CompilerConfiguration

class Parser {
    Track track
    Map<String, String> urlMap

    void parse(String scriptText) throws CompilationFailedException {
        def config = new CompilerConfiguration()
        config.setScriptBaseClass(SyntraxScript.class.name)
        def sharedData = new Binding()
        def shell = new GroovyShell(this.class.classLoader, sharedData, config)
        shell.setVariable("None", null)

        shell.evaluate(scriptText)

        this.track = sharedData.getProperty("track") as Track
        this.urlMap = sharedData.getProperty("url_map") as Map<String, String>
    }
}
