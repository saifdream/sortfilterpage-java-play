package com.play2.crud.playaction.controllers


import play.api._
import play.api.mvc._

import com.play2.crud.playaction.views.html.modelGen


object ModelController extends Controller {
  /*
  def generateModelCode() = Action {
    Ok(com.play2.crud.playaction.views.html.crudGen("", List()))
  }
  */

  def generateModelCode() = Action {
    Ok(modelGen())
  }

}