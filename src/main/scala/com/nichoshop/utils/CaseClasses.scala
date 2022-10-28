package  com.nichoshop.utils

object CaseClasses{

    case class CreateCategory(name: String)

    case class CreateSpecific(name: String)

    case class SpecificClass(id: Int, name: String, defaultValue: String, required: Boolean)

    case class UpdateCategory(conditions: List[String], specifics: List[SpecificClass] = List())
    // case class UpdateCategory(conditions: List[String], specifics: String)


}
