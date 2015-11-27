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

import akka.actor.{Actor, Props}

class HelloActor extends Actor {
  import HelloActor._

  def receive = {
    case HelloRequest(name) ⇒ sender ! HelloResponse(s"Hello, $name")
  }
}

object HelloActor {
  case class HelloRequest(name: String)
  case class HelloResponse(message: String)

  def props = Props[HelloActor]
}
