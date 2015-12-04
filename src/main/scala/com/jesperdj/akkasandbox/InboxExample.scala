/*
 * Copyright 2015 Jesper de Jong
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jesperdj.akkasandbox

import java.util.concurrent.TimeUnit

import akka.actor.ActorDSL._
import akka.actor.ActorSystem
import akka.util.Timeout
import com.jesperdj.akkasandbox.HelloActor.{HelloRequest, HelloResponse}

import scala.concurrent.Await

object InboxExample extends App {
  implicit val system = ActorSystem("InboxExample")

  val actor = system.actorOf(HelloActor.props)

  // The inbox (method from ActorDSL)
  implicit val in = inbox()

  // Send a message to the actor. Note that '!' takes an implicit ActorRef, which is used as the sender of the message.
  // There is an implicit conversion from Inbox to ActorRef which returns the actor associated with the Inbox;
  // this actor is used as the sender, so that the HelloActor will send its response to that actor.
  actor ! HelloRequest("Inbox")

  // Receive the response by getting it from the inbox
  val response = in.receive().asInstanceOf[HelloResponse]
  println(response.message)

  // Terminate the actor system
  Await.ready(system.terminate(), Timeout(3, TimeUnit.SECONDS).duration)
}
