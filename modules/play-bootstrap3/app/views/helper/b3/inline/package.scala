/**
 * Copyright 2014 Adrian Hurtado (adrianhurt)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package views.html.helper.b3

package object inline {

  import play.twirl.api.Html
  import play.api.mvc.Call
  import views.html.helper._

  /**
   * Declares the class for the Inline FieldConstructor.
   */
  class InlineFieldConstructor extends B3FieldConstructor {
    /* Define the class of the corresponding form */
    val formClass = "form-inline"
    /* Renders the corresponding template of the field constructor */
    def apply(fieldInfo: B3FieldInfo, inputHtml: Html) = b3FieldConstructor(fieldInfo, inputHtml)
    /* Renders the corresponding template of the form group */
    def apply(contentHtml: Html, extraClasses: Option[String], argsMap: Map[Symbol, Any]) = b3FormGroup(contentHtml, extraClasses, argsMap)
  }

  /**
   * Creates a new InlineFieldConstructor to use for specific forms or scopes (don't use it as a default one).
   * If a default B3FieldConstructor and a specific InlineFieldConstructor are within the same scope, the more
   * specific will be chosen.
   */
  val fieldConstructorSpecific: InlineFieldConstructor = new InlineFieldConstructor()

  /**
   * Returns it as a B3FieldConstructor to use it as default within a template
   */
  implicit val fieldConstructor: B3FieldConstructor = fieldConstructorSpecific

  /**
   * **********************************************************************************************************************************
   * SHORTCUT HELPERS
   * *********************************************************************************************************************************
   */
  def form(action: Call, args: (Symbol, Any)*)(body: InlineFieldConstructor => Html) =
    views.html.helper.b3.form(action, args: _*)(body(fieldConstructorSpecific))(fieldConstructorSpecific)

}