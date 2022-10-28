package com.nichoshop.controllers.admin

import com.nichoshop.utils.Marshallers
import com.nichoshop.services.Services
import com.nichoshop.controllers.{AuthController,BaseController}
import com.nichoshop.models.AdminNoteEntity
import com.nichoshop.models.CSEntity
import org.json4s.JsonAST.JArray

import scala.collection.JavaConversions._

/**
 * Created by Nursultan on 7/7/2022
 */

class CSController extends AuthController {

  def name = "admin/customersupport"

  val csService = Services.csService
  val auth = Services.authService
  val adminNoteService = Services.adminNoteService

  // get customer support list
  get("/"){
    auth.withUser { user =>() =>
      csService.getList()  
    }
  }

  // create customer support
  post("/") {
    val csBody = parsedBody.extract[CSBody]

    auth.withUser{ user => () =>
        csService.create(csBody.toEntity)
    }
  }

  // get admin notes
  get("/note"){
    val csId = params.getOrElse("cs_id","").toInt
    auth.withUser{ user => () =>
        adminNoteService.getList(csId)
    }
  }

  // create note
  post("/note"){
    val noteBody = parsedBody.extract[NoteBody]

    auth.withUser{ user => () =>
        adminNoteService.create(noteBody.toEntity)
    }
  }

  // update note
  put("/note"){
    val noteBody = parsedBody.extract[NoteBody]

    auth.withUser{ user => () =>
        adminNoteService.update(noteBody.toEntity)
    }
  }

}
case class CSBody(
            fname: String,
            lname: String,
            password: String,
            email: String,
            phone: String,
            depart: Int,
            subDepart: Int,
            timezone: String,
            contact: Boolean,
            monToFri: Int,
            saturday: Int,
            sunday: Int,
            currencyId: Int
) {
    def toEntity : CSEntity = CSEntity(
        username = "",
        fname = fname,
        lname = lname,
        password = password,
        email = email,
        phone = phone,
        depart = depart,
        subDepart = subDepart,
        timezone = timezone,
        contact = contact,
        monToFri = monToFri,
        saturday = saturday,
        sunday = sunday,
        currencyId = currencyId
    )
}

case class NoteBody(id: Option[Int] = None, subject: String, desc: String,csId: Int){
      def toEntity : AdminNoteEntity = new AdminNoteEntity(
          id = id,
          subject = subject,
          desc = desc,
          csId = csId,
      )
}
