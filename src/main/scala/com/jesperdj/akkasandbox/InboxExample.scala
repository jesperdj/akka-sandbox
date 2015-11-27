package com.jesperdj.akkasandbox

import java.util.concurrent.TimeUnit

import akka.actor.ActorDSL._
import akka.actor.{ActorSystem, Props}
import akka.util.Timeout

import scala.concurrent.Await

object InboxExample extends App {
  implicit val system = ActorSystem("InboxExample")

  val actor = system.actorOf(Props[HelloActor])

  // The inbox (method from ActorDSL)
  implicit val i = inbox()

  // Send a message to the actor. Note that '!' takes an implicit ActorRef, which is used as the sender of the message.
  // There is an implicit conversion from Inbox to ActorRef which returns the actor associated with the Inbox;
  // this actor is used as the sender, so that the HelloActor will send its response to that actor.
  actor ! HelloRequest("Inbox")

  // Receive the response by getting it from the inbox
  val response = i.receive().asInstanceOf[HelloResponse]
  println(response.message)

  // Terminate the actor system
  Await.ready(system.terminate(), Timeout(3, TimeUnit.SECONDS).duration)
}