package vn.mannd.pde;

public class PluginConstants
{

	public static final String RULES_CONFIG_FILE_PATH = "config/rules.xml";
	public static final String CHECKERS_CONFIG_FILE_PATH = "config/checkers.xml";
	public static final String CHANGE_CREATORS_CONFIG_FILE_PATH = "config/change_creators.xml";
	public static final String FACTORIES_CONFIG_FILE_PATH = "config/factories.xml";
	public static final String FACTORY_MAPPINGS_CONFIG_FILE_PATH = "config/factory_mappings.xml";

	public static final String MARKER_ID = Activator.PLUGIN_ID + ".problem";
	public static final String MARKER_GENERATOR_ID = Activator.PLUGIN_ID + ".problemMarkerContentGenerator";
	public static final String MARKER_RULE_TYPE = "MARKER_RULE_TYPE";
	public static final String MARKER_CHECKER_TYPE = "MARKER_CHECKER_TYPE";
	public static final String MARKER_NODE_TYPE = "MARKER_NODE_TYPE";
	public static final String MARKER_CATEGORY = "MARKER_CATEGORY";
	public static final String MARKER_SEVERITY = "MARKER_SEVERITY";

	public static final String INFO_VIEW_ID = "vn.dongpv.pde.problemInfoView";

	public static final String JAVA_COMPILATION_UNIT_TYPE_NAME = "source file";
	public static final String JAVA_PACKAGE_TYPE_NAME = "package";
	public static final String JAVA_PROJECT_TYPE_NAME = "project";

	public static final String EMPTY_STRING = "";
	public static final String ZERO_STRING = "0";
	public static final String UNKNOWN_STRING = "Unknown";

	public static final String NON_PRIMITIVE_CLASS_NAME = "NONE_CLASS_NAME";

	public static final String BOOLEAN_CLASS_NAME = "java.lang.Boolean";
	public static final String CHARACTER_CLASS_NAME = "java.lang.Character";
	public static final String BYTE_CLASS_NAME = "java.lang.Byte";
	public static final String SHORT_CLASS_NAME = "java.lang.Short";
	public static final String INTEGER_CLASS_NAME = "java.lang.Integer";
	public static final String LONG_CLASS_NAME = "java.lang.Long";
	public static final String FLOAT_CLASS_NAME = "java.lang.Float";
	public static final String DOUBLE_CLASS_NAME = "java.lang.Double";
	public static final String STRING_CLASS_NAME = "java.lang.String";

	public static final String ABSTRACT_LIST_CLASS_NAME = "java.util.AbstractList";
	public static final String CLASS_LOADER_CLASS_NAME = "java.lang.ClassLoader";
	public static final String DATA_INPUT_CLASS_NAME = "java.io.DataInput";
	public static final String DATA_OUTPUT_CLASS_NAME = "java.io.DataOutput";
	public static final String DRIVER_MANAGER_CLASS_NAME = "java.sql.DriverManager";
	public static final String CLONE_NOT_SUPPORTED_EXCEPTION_CLASS_NAME = "java.lang.CloneNotSupportedException";
	public static final String COLLECTION_CLASS_NAME = "java.util.Collection";
	public static final String IO_EXCEPTION_CLASS_NAME = "java.io.IOException";
	public static final String LINKED_LIST_CLASS_NAME = "java.util.LinkedList";
	public static final String MATH_CLASS_NAME = "java.lang.Math";
	public static final String MAP_CLASS_NAME = "java.util.Map";
	public static final String OBJECT_CLASS_NAME = "java.lang.Object";
	public static final String OBJECT_INPUT_STREAM_CLASS_NAME = "java.io.ObjectInputStream";
	public static final String OBJECT_OUTPUT_STREAM_CLASS_NAME = "java.io.ObjectOutputStream";
	public static final String PRINT_STREAM_CLASS_NAME = "java.io.PrintStream";
	public static final String PRIVILEGED_ACTION_CLASS_NAME = "java.security.PrivilegedAction";
	public static final String PRIVILEGED_EXCEPTION_ACTION_CLASS_NAME = "java.security.PrivilegedExceptionAction";
	public static final String PROPERTIES_CLASS_NAME = "java.util.Properties";
	public static final String RANDOM_CLASS_NAME = "java.util.Random";
	public static final String RUNTIME_CLASS_NAME = "java.lang.Runtime";
	public static final String SERIALIZABLE_CLASS_NAME = "java.io.Serializable";
	public static final String STATEMENT_CLASS_NAME = "java.sql.Statement";
	public static final String STRING_TOKENIZER_CLASS_NAME = "java.util.StringTokenizer";
	public static final String THREAD_CLASS_NAME = "java.lang.Thread";
	public static final String THROWABLE_CLASS_NAME = "java.lang.Throwable";
	public static final String VECTOR_CLASS_NAME = "java.util.Vector";

	public static final String OBJECT_NAME = "Object";

	public static final String BOOLEAN_NAME = "Boolean";
	public static final String CHARACTER_NAME = "Character";
	public static final String BYTE_NAME = "Byte";
	public static final String SHORT_NAME = "Short";
	public static final String INTEGER_NAME = "Integer";
	public static final String LONG_NAME = "Long";
	public static final String FLOAT_NAME = "Float";
	public static final String DOUBLE_NAME = "Double";
	public static final String STRING_NAME = "String";

	public static final String BOOL = "boolean";
	public static final String CHAR = "char";
	public static final String BYTE = "byte";
	public static final String SHORT = "short";
	public static final String INT = "int";
	public static final String LONG = "long";
	public static final String FLOAT = "float";
	public static final String DOUBLE = "double";

	public static final String NUMBER_NAME = "Number";

	private PluginConstants()
	{
	}

	@Override
	public final Object clone() throws java.lang.CloneNotSupportedException
	{
		throw new java.lang.CloneNotSupportedException();
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}
