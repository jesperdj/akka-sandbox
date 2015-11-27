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

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.util.Timeout
import com.jesperdj.akkasandbox.HelloActor.{HelloRequest, HelloResponse}

import scala.concurrent.Await

object AskExample extends App {
  val system = ActorSystem("AskExample")

  val actor = system.actorOf(HelloActor.props)

  // The '?' used below creates a temporary actor, which must be stopped when it's done.
  // This implicit timeout is used by '?' for stopping the temporary actor.
  implicit val askActorTimeout = Timeout(2, TimeUnit.SECONDS)

  // Send the actor a message, returns a future that will get the response
  val future = actor ? HelloRequest("World")

  // Wait for the future to be completed and get the response
  val response = Await.result(future, Timeout(1, TimeUnit.SECONDS).duration).asInstanceOf[HelloResponse]
  println(response.message)

  // Terminate the actor system
  Await.ready(system.terminate(), Timeout(3, TimeUnit.SECONDS).duration)
}
