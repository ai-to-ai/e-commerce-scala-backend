package com.nichoshop.legacy.models

case class Purchase(saleDate: Long, trackingNumber: Option[String], image: String, title: String, sellerId: Int, sellerName: String, totalPrice: Int, itemNumber: Int, score: Int, scoreRate: Double, hasFeedback: Boolean)

object Purchase {
  def apply(pr: PurchaseRow): Purchase = Purchase(pr.sell.soldTime, pr.trackingNumber, pr.images, pr.title, pr.sellerId, pr.sellerName,
    pr.totalPrice, pr.sell.productId, pr.score, pr.scoreRate, pr.feedbackId.isDefined)
}