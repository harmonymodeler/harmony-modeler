package com.harmonymodeler.Testing.app

import org.springframework.shell.command.annotation.Command
import org.springframework.stereotype.Component

@Component
@Command(command = ["harmony-modeler"])
class Example {

    @Command(command = ["something"])
    fun something() {

    }
}