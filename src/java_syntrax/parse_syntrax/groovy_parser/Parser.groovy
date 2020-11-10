package java_syntrax.parse_syntrax.groovy_parser

import java_syntrax.parse_syntrax.units.tracks.Track
import org.codehaus.groovy.control.CompilerConfiguration

class Parser {
    static Track parse(String program) {
        def config = new CompilerConfiguration()
        config.scriptBaseClass = 'SyntraxScript'

        def shell = new GroovyShell(this.class.classLoader, new Binding(), config)
        return shell.evaluate(program) as Track
    }
}
