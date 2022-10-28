package com.nichoshop.services

import com.nichoshop.services.memcached.BaseService
import com.nichoshop.services._

/**
 * Created by Evgeny Zhoga on 25.06.15.
 */
object Services {
  
  val authService = new AuthService with memcached.AuthService
  
  val permissions = new PermissionsService

  val usersService = new UsersService

  val categoryService = new CategoryService

  val addressService = new AddressService

  val sucService = new SUCService

  val messageService = new MessageService

  val mFolderService = new MessageFolderService

  val bidService = new BidService

  val cartService = new CartService

  val orderService = new OrderService

  val creditCardService = new CreditCardService

  val feedBackService = new FeedBackService

  val itemService = new ItemService

  val duoConfirmService = new DuoConfirmService

  val sellOrderService = new SellOrderService

  val csService = new CSService

  val adminNoteService = new AdminNoteService

}
