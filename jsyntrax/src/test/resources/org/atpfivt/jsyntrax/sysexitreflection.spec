def systemClass = Class.forName("java.lang.System")
def exitMethod = systemClass.getDeclaredMethod("exit", Integer.TYPE)
exitMethod.invoke(null, 0)