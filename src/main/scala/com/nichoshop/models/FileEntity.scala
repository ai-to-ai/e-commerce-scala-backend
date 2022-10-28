package com.nichoshop.models

import com.nichoshop.Environment.driver.simple._
import com.nichoshop.models.helpers._

/**
 * Created by Nursultan on 6/6/2022
 */

 case class FileEntity(

            override val id: Option[Int] = None,
            name: String,
            path: String,
            fileSize: Int,
            fileType: String

 ) extends IdAsPrimaryKey

class Files(tag:Tag) extends TableWithIdAsPrimaryKey[FileEntity](tag, "files") {

    def name = column[String]("name")
    def filePath = column[String]("path")
    def fileSize = column[Int]("size")
    def fileType = column[String]("type")

    def * = (id.?, name, filePath, fileSize, fileType) <> (FileEntity.tupled, FileEntity.unapply)

}

object Files extends QueryForTableWithIdAsPrimaryKey[FileEntity, Files](TableQuery[Files]){
    
}