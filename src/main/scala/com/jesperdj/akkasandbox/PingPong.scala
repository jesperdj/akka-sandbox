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

import akka.actor._

object PingPong extends App {
  // Create the actor system
  val system = ActorSystem("PingPong")

  // Create actors in the system
  val pingActor = system.actorOf(Props[PingActor], name = "Ping")
  val pongActor = system.actorOf(Props[PongActor], name = "Pong")

  // Send a start message to the first actor
  pingActor ! StartMessage(pongActor)
}

// Messages
case class StartMessage(target: ActorRef)
case object PingMessage
case object PongMessage
case object StopMessage

class PingActor extends Actor with ActorLogging {
  private var count = 0

  def receive = {
    case StartMessage(target) ⇒
      log.info("Start")
      target ! PingMessage

    case PongMessage ⇒
      count += 1
      log.info("Pong {}", count)
      if (count < 10) {
        sender ! PingMessage
      } else {
        log.info("Stop Ping")
        sender ! StopMessage
        context.stop(self)
      }
  }
}

class PongActor extends Actor with ActorLogging {
  private var count = 0

  def receive = {
    case PingMessage ⇒
      count += 1
      log.info("Ping {}", count)
      sender ! PongMessage

    case StopMessage ⇒
      log.info("Stop Pong")
      context.stop(self)
      context.system.terminate()
  }
}
