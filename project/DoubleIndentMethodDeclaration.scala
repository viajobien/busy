import scalariform.formatter.preferences.BooleanPreferenceDescriptor

case object DoubleIndentMethodDeclaration extends BooleanPreferenceDescriptor {
  val key = "doubleIndentMethodDeclaration"
  val description = "Double indent a method's parameters"
  val defaultValue = false
}
