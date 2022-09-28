package com.play2.crud.playaction.controllers

import scala.collection.JavaConversions._

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import com.play2.crud.CrudGenerator
import com.play2.crud.playaction.views.html.crudGen


case class CrudGen(pkgName: String, classList: List[String])

object CrudController extends Controller {

  val crudForm = Form(
    mapping(
      "pkgName" -> nonEmptyText,
      "classList" -> list(text)
    )(CrudGen.apply)(CrudGen.unapply)
  )


  def show() = Action {
    Ok(crudGen("", List()))
  }


  def generate() = Action { implicit request =>
    crudForm.bindFromRequest.fold(
      formWithErrors => BadRequest(crudGen("", List[String]())),
      cg => {
        if(cg.classList==null || cg.classList.length==0) {

          //Ok(crudGen(cg.pkgName, List.empty))
          Ok(crudGen(cg.pkgName, List("models.User", "models.Role", "models.transaction.Merchant")))
        } else {
          //for(c <- cg.classList){
          //  println("Class: [" + c + "]")
          //}
          //val crudGenerator = new CrudGenerator(cg.pkgName)
          //val crudGenerator = new CrudGenerator("models.transaction.SuspiciousTrnxn")
          val crudGenerator = new CrudGenerator("models.auth.UserGroup")
          crudGenerator.process()

          Ok(crudGen(cg.pkgName, cg.classList))
        }
      })
  }

}