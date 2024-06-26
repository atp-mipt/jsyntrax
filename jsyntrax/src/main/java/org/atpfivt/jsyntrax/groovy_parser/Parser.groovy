package org.atpfivt.jsyntrax.groovy_parser

import org.atpfivt.jsyntrax.Configuration
import org.atpfivt.jsyntrax.units.Unit
import org.codehaus.groovy.control.CompilationFailedException
import org.codehaus.groovy.control.CompilerConfiguration
import org.codehaus.groovy.control.customizers.SecureASTCustomizer

class Parser {

    private final String scriptText
    private final Unit result
    private final String title

    Parser(String scriptText) throws CompilationFailedException {
        this.scriptText = scriptText
        final SecureASTCustomizer secure = new SecureASTCustomizer()
        secure.with {
            indirectImportCheckEnabled = true

            importsWhitelist = ['java.lang.Object']
        }
        def config = new CompilerConfiguration()
        config.setScriptBaseClass(SyntraxScript.class.name)
        config.addCompilationCustomizers(secure)
        def sharedData = new Binding()
        def shell = new GroovyShell(this.class.classLoader, sharedData, config)
        shell.setVariable("None", null)
        def script = shell.parse(scriptText)
        result = script.run() as Unit
        title = script.title
    }

    Configuration getNode()  {
        result.getConfiguration()
    }

    String getTitle() {
        title
    }
}
