package com.nichoshop.services

import com.nichoshop.Environment.driver.simple._
import com.nichoshop.models.helpers.DB
import com.nichoshop.models.{MessageFolderEntity, MessageFolders}

/** 
 *  Created by Nursultan on 6/6/2022
 */

class MessageFolderService {

    val folders = MessageFolders.query

    def createFolder(x: MessageFolderEntity) = DB.write { implicit session =>
        folders.insert(x)
    }

    def folderList(userId: Int) = DB.read { implicit session =>
        folders.filter(_.userId === userId)
                .sortBy(_.title.desc)
                .list
    }

    def renameFolder(id: Int, title: String) = DB.write{ implicit session =>
        folders.filter(_.id === id)
                .map(_.title)
                .update(title)
    }

    def deleteFolderById(id: Int) = DB.write{ implicit session =>
        folders.filter(_.id === id).delete
    }

}