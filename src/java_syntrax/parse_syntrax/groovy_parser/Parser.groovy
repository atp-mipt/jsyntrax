package java_syntrax.parse_syntrax.groovy_parser

import java_syntrax.parse_syntrax.units.tracks.Track
import org.codehaus.groovy.control.CompilerConfiguration

class Parser {
  static Track parse(File file) {
    def config = new CompilerConfiguration()
    config.scriptBaseClass = SyntraxScript.class.name
    def shell = new GroovyShell(this.class.classLoader, new Binding(), config)
    return shell.evaluate(file) as Track
  }
}