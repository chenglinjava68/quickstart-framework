package com.bookislife.jgsk.scala._23_trait

/**
 * Created by SidneyXu on 2015/10/04.
 */
trait OutputLogger extends Logger {
    override def log(msg: String): Unit = println(msg)
}
